package com.ikue.japanesedictionary.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.adapters.DetailViewAdapter;
import com.ikue.japanesedictionary.database.AddToHistoryTask;
import com.ikue.japanesedictionary.database.DictionaryDbHelper;
import com.ikue.japanesedictionary.database.GetEntryDetailTask;
import com.ikue.japanesedictionary.database.ToggleFavouriteTask;
import com.ikue.japanesedictionary.interfaces.AddToHistoryAsyncCallbacks;
import com.ikue.japanesedictionary.interfaces.DetailAsyncCallbacks;
import com.ikue.japanesedictionary.interfaces.ToggleFavouriteAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionaryEntry;
import com.ikue.japanesedictionary.models.KanjiElement;
import com.ikue.japanesedictionary.models.Priority;
import com.ikue.japanesedictionary.models.ReadingElement;
import com.ikue.japanesedictionary.utils.EntryUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EntryDetailFragment extends Fragment implements DetailAsyncCallbacks, ToggleFavouriteAsyncCallbacks, AddToHistoryAsyncCallbacks {

    private static final String ARG_ENTRY_ID = "ENTRY_ID";

    private TextView otherFormsContentTextView;
    private TextView otherFormsHeaderTextView;
    private View otherFormsDivider;

    private TextView freqInfoHeaderTextView;
    private ChipGroup chipGroup;
    private View freqInfoDivider;

    private static DictionaryDbHelper helper;
    private static AsyncTask detailsTask;
    private static AsyncTask favouritesTask;
    private static AsyncTask addToHistoryTask;
    private static DictionaryEntry dictionaryEntry;
    private static DetailAsyncCallbacks detailAsyncCallbacks;
    private static ToggleFavouriteAsyncCallbacks toggleFavouriteAsyncCallbacks;

    private static int entryId;

    private String toolbarTitle;

    private CollapsingToolbarLayout collapsingToolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    public static EntryDetailFragment newInstance(int entryId) {
        Bundle args = new Bundle();
        args.putInt(ARG_ENTRY_ID, entryId);

        EntryDetailFragment fragment = new EntryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain the fragment so rotation does not repeatedly fire off new AsyncTasks
        // TODO: Switch to Loaders and remove this
        setRetainInstance(true);

        // Setup callbacks
        detailAsyncCallbacks = this;
        toggleFavouriteAsyncCallbacks = this;
        AddToHistoryAsyncCallbacks addToHistoryAsyncCallbacks = this;

        // Get a database on startup. Copying from assets folder is all handled
        // by SQLiteAssetHelper
        helper = DictionaryDbHelper.getInstance(this.getActivity());

        entryId = getArguments().getInt(ARG_ENTRY_ID, 0);

        // Add the current entry to the user's history. Might need to move to onActivityCreated
        // to ensure getActivity doesn't return null in our results callback
        addToHistoryTask = new AddToHistoryTask(addToHistoryAsyncCallbacks, helper, entryId).execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false); // Inflate the view
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setHasOptionsMenu(true);

        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);

        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setEnabled(false);
        floatingActionButton.setOnClickListener(view1 -> toggleFab());

        otherFormsContentTextView = view.findViewById(R.id.other_forms_element);
        otherFormsHeaderTextView = view.findViewById(R.id.other_forms_header);
        otherFormsDivider = view.findViewById(R.id.other_forms_divider);

        freqInfoHeaderTextView = view.findViewById(R.id.freq_info_header);
        chipGroup = view.findViewById(R.id.chip_group);
        freqInfoDivider = view.findViewById(R.id.freq_info_divider);

        recyclerView = view.findViewById(R.id.meanings_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        // Disables scrolling for RecyclerView,
        recyclerView.setNestedScrollingEnabled(false);

        // Makes RecyclerView wrap its content
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        detailsTask = new GetEntryDetailTask(detailAsyncCallbacks, helper, entryId).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            // We want to go back to the previous Activity, either the list of search results
            // or the home screen
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        // Cancel the AsyncTask if it is running when Activity is about to close
        // cancel(false) is safer and doesn't force an instant cancellation
        if (detailsTask != null) {
            detailsTask.cancel(false);
        }
        if (favouritesTask != null) {
            favouritesTask.cancel(false);
        }
        if (addToHistoryTask != null) {
            addToHistoryTask.cancel(false);
        }

        // Close the SQLiteHelper instance
        helper.close();
        super.onDestroy();
    }

    private void toggleFab() {
        floatingActionButton.setEnabled(false);
        if (dictionaryEntry.getIsFavourite()) {
            // Remove from favourites
            favouritesTask = new ToggleFavouriteTask(toggleFavouriteAsyncCallbacks, helper, entryId, false).execute();
        } else {
            // Add to favourites
            favouritesTask = new ToggleFavouriteTask(toggleFavouriteAsyncCallbacks, helper, entryId, true).execute();
        }
    }

    private void updateViews() {
        setToolbar();
        setFavourite();

        // Set the meanings section
        recyclerView.setAdapter(new DetailViewAdapter(dictionaryEntry.getSenseElements()));

        setOtherReadings();
        setFrequencyInformation();
    }

    private void setToolbar() {
        // Set the toolbar title to the main kanji element value + reading if it exists,
        // if not then just use the first reading element value
        List<KanjiElement> kanjiElementList = dictionaryEntry.getKanjiElements();
        List<ReadingElement> readingElementList = dictionaryEntry.getReadingElements();

        if (kanjiElementList != null && !kanjiElementList.isEmpty()) {
            toolbarTitle = kanjiElementList.get(0).getValue()
                    + " [" + readingElementList.get(0).getValue() + "]";
        } else toolbarTitle = readingElementList.get(0).getValue();

        // TODO: Handle case where value is too big and parts are cutoff, e.g. see entry id: 1004000
        collapsingToolbar.setTitle(toolbarTitle);
    }

    private void setFavourite() {
        // Default state is not favourited, if the entry has been favourited we need to update the
        // FAB icon
        if (dictionaryEntry.getIsFavourite()) {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_white));
        }

        // Once we have set the initial state of the FAB, let the user click it
        floatingActionButton.setEnabled(true);
    }

    private void setOtherReadings() {
        List<KanjiElement> kanjiElementList = dictionaryEntry.getKanjiElements();
        List<ReadingElement> readingElementList = dictionaryEntry.getReadingElements();
        // TODO: Refactor into RecyclerView
        // Set the other readings section
        StringBuilder otherForms = new StringBuilder();
        for (ReadingElement readingElement : readingElementList) {
            if (!readingElement.getReadingRelation().isEmpty()) {
                // The reading only applies to certain Kanji Elements
                for (String readingRelation : readingElement.getReadingRelation()) {
                    otherForms.append(readingRelation).append(" [").append(readingElement.getValue()).append("]\n");
                }

            } else {
                // There are no Kanji Elements, so just display every Reading Element value
                if (kanjiElementList == null || kanjiElementList.isEmpty()) {
                    otherForms.append(readingElement.getValue()).append("\n");
                } else {
                    // The reading is for every Kanji Element
                    for (KanjiElement kanjiElement : kanjiElementList) {
                        otherForms.append(kanjiElement.getValue()).append(" [").append(readingElement.getValue()).append("]\n");
                    }
                }
            }
        }

        // Remove main reading as it's already displayed in the toolbar
        otherForms = new StringBuilder(otherForms.toString().replace(toolbarTitle + "\n", ""));

        // If there are no other forms, we hide the related section
        if(otherForms.length() == 0) {
            otherFormsDivider.setVisibility(View.GONE);
            otherFormsHeaderTextView.setVisibility(View.GONE);
            otherFormsContentTextView.setVisibility(View.GONE);
        } else {
            // Remove trailing new line
            if (otherForms.toString().endsWith("\n")) {
                otherForms = new StringBuilder(otherForms.substring(0, otherForms.length() - 1));
            }

            // Set the resulting string
            otherFormsContentTextView.setText(otherForms.toString());
        }
    }

    // Set the priorities section
    // TODO: Refactor into RecyclerView
    // TODO: Separate out into Kanji and Reading priorities
    private void setFrequencyInformation() {
        List<Priority> priorities = dictionaryEntry.getPriorities();

        // Convert to a linked hash set to remove duplicates
        Set<String> unifiedPriorities = new LinkedHashSet<>();
        for (Priority priority : priorities) {
            unifiedPriorities.add(priority.getValue());
        }

        // If there are no priorities, remove the Priorities TextViews from view
        if (unifiedPriorities.isEmpty()) {
            freqInfoDivider.setVisibility(View.GONE);
            freqInfoHeaderTextView.setVisibility(View.GONE);
            chipGroup.setVisibility(View.GONE);
        } else {
            // Get whether the entry is common or not
            if (EntryUtils.isCommonEntry(unifiedPriorities)) {
                // If our entry is common, add a 'common' chip
                Chip chip = new Chip(requireContext());
                chip.setText(getString(R.string.common_chip));
                chip.setId(View.generateViewId());
                chip.setCheckable(true);
                chip.setCheckedIconVisible(false);
                chipGroup.addView(chip);
            }

            // Add every priority as a chip
            for (String value : unifiedPriorities) {
                Chip chip = new Chip(requireContext());
                chip.setText(value);
                chip.setId(View.generateViewId());
                chip.setCheckable(true);
                chip.setCheckedIconVisible(false);
                chipGroup.addView(chip);
            }

            // Get the map of detailed priority information we use for our dialogs.
            final Map<String, String> detailedPriorityInformation = EntryUtils.getDetailedPriorityInformation();

            chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
                if (checkedIds.isEmpty()) {
                    return;
                }

                int id = chipGroup.getCheckedChipId();
                Chip chip = chipGroup.findViewById(id);

                // Get the text of the chip
                String label = chip.getText().toString();
                String message;

                // Get the detailed message from the label
                if (label.startsWith("nf")) {
                    message = detailedPriorityInformation.get("nf");
                } else {
                    message = detailedPriorityInformation.get(label);
                }

                // Create an AlertDialog showing the detailed information
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(label)
                        .setMessage(message)
                        .setOnCancelListener(dialogInterface -> {
                            // When the user dismisses the dialog, deselect the chip
                            chipGroup.clearCheck();
                        })
                        .show();
            });
        }
    }

    @Override
    public void onResult(DictionaryEntry result) {
        dictionaryEntry = result;
        updateViews();
    }

    @Override
    public void onToggleFavouriteResult(boolean toBeAdded, boolean wasSuccessful) {
        if (toBeAdded) {
            if (wasSuccessful) {
                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_white));
                dictionaryEntry.setIsFavourite(true);

                if (getView() != null) {
                    Snackbar.make(getView(), R.string.success_add_favourite, Snackbar.LENGTH_LONG).show();
                }
            } else if (getView() != null) {
                Snackbar.make(getView(), R.string.error_add_favourite, Snackbar.LENGTH_LONG).show();
            }
        } else {
            if (wasSuccessful) {
                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_border_white));
                dictionaryEntry.setIsFavourite(false);

                if (getView() != null) {
                    Snackbar.make(getView(), R.string.success_remove_favourite, Snackbar.LENGTH_LONG).show();
                }
            } else if (getView() != null) {
                Snackbar.make(getView(), R.string.error_remove_favourite, Snackbar.LENGTH_LONG).show();
            }
        }
        // Re-enable the button after our database operations have completed and we have reset
        // the FAB state
        floatingActionButton.setEnabled(true);
    }

    @Override
    public void onAddToHistoryResult(boolean wasSuccessful) {
        // If there was an error then display a message informing the user
        if (!wasSuccessful) {
            Toast.makeText(getActivity(), R.string.error_add_to_history, Toast.LENGTH_SHORT).show();
        }
    }
}
