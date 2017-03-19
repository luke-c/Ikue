package com.ikue.japanesedictionary.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.adapters.DetailViewAdapter;
import com.ikue.japanesedictionary.database.AddToHistoryTask;
import com.ikue.japanesedictionary.database.DictionaryDbHelper;
import com.ikue.japanesedictionary.database.GetEntryDetailTask;
import com.ikue.japanesedictionary.database.ToggleFavouriteTask;
import com.ikue.japanesedictionary.interfaces.AddToHistoryAsyncCallbacks;
import com.ikue.japanesedictionary.interfaces.DetailAsyncCallbacks;
import com.ikue.japanesedictionary.interfaces.ToggleFavouriteAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionaryItem;
import com.ikue.japanesedictionary.models.KanjiElement;
import com.ikue.japanesedictionary.models.Priority;
import com.ikue.japanesedictionary.models.ReadingElement;
import com.ikue.japanesedictionary.utils.EntryUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fisk.chipcloud.ChipCloud;
import fisk.chipcloud.ChipCloudConfig;
import fisk.chipcloud.ChipListener;

/**
 * Created by luke_c on 05/02/2017.
 */

public class EntryDetailFragment extends Fragment implements DetailAsyncCallbacks, ToggleFavouriteAsyncCallbacks, AddToHistoryAsyncCallbacks {

    private static final String ARG_ENTRY_ID = "ENTRY_ID";

    private TextView otherFormsContentTextView;
    private TextView otherFormsHeaderTextView;
    private View otherFormsDivider;

    private TextView freqInfoHeaderTextView;
    private FlexboxLayout freqInfoFlexBox;
    private View freqInfoDivider;

    private static DictionaryDbHelper helper;
    private static AsyncTask detailsTask;
    private static AsyncTask favouritesTask;
    private static AsyncTask addToHistoryTask;
    private static DictionaryItem dictionaryItem;
    private static DetailAsyncCallbacks detailAsyncCallbacks;
    private static ToggleFavouriteAsyncCallbacks toggleFavouriteAsyncCallbacks;
    private static AddToHistoryAsyncCallbacks addToHistoryAsyncCallbacks;

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
        addToHistoryAsyncCallbacks = this;

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
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setHasOptionsMenu(true);

        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setEnabled(false);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFab();
            }
        });

        otherFormsContentTextView = (TextView) view.findViewById(R.id.other_forms_element);
        otherFormsHeaderTextView = (TextView) view.findViewById(R.id.other_forms_header);
        otherFormsDivider = view.findViewById(R.id.other_forms_divider);

        freqInfoHeaderTextView = (TextView) view.findViewById(R.id.freq_info_header);
        freqInfoFlexBox = (FlexboxLayout) view.findViewById(R.id.freq_info_flexbox);
        freqInfoDivider = (View) view.findViewById(R.id.freq_info_divider);

        recyclerView = (RecyclerView) view.findViewById(R.id.meanings_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        // Disables scrolling for RecyclerView,
        recyclerView.setNestedScrollingEnabled(false);

        // Makes RecyclerView wrap its content
        layoutManager.setAutoMeasureEnabled(true);
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
            getActivity().onBackPressed();
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
        if (dictionaryItem.getIsFavourite()) {
            favouritesTask = new ToggleFavouriteTask(toggleFavouriteAsyncCallbacks, helper, entryId, false).execute();
        } else {
            favouritesTask = new ToggleFavouriteTask(toggleFavouriteAsyncCallbacks, helper, entryId, true).execute();
        }
    }

    private void updateViews() {
        setToolbar();
        setFavourite();

        // Set the meanings section
        recyclerView.setAdapter(new DetailViewAdapter(dictionaryItem.getSenseElements()));

        setOtherReadings();
        setFrequencyInformation();
    }

    private void setToolbar() {
        // Set the toolbar title to the main kanji element value + reading if it exists,
        // if not then just use the first reading element value
        List<KanjiElement> kanjiElementList = dictionaryItem.getKanjiElements();
        List<ReadingElement> readingElementList = dictionaryItem.getReadingElements();

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
        if (dictionaryItem.getIsFavourite()) {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_white));
        }

        // Once we have set the initial state of the FAB, let the user click it
        floatingActionButton.setEnabled(true);
    }

    private void setOtherReadings() {
        List<KanjiElement> kanjiElementList = dictionaryItem.getKanjiElements();
        List<ReadingElement> readingElementList = dictionaryItem.getReadingElements();
        // TODO: Refactor into RecyclerView
        // Set the other readings section
        String otherForms = "";
        for (ReadingElement readingElement : readingElementList) {
            if (!readingElement.getReadingRelation().isEmpty()) {
                // The reading only applies to certain Kanji Elements
                for (String readingRelation : readingElement.getReadingRelation()) {
                    otherForms += readingRelation + " [" + readingElement.getValue() + "]\n";
                }

            } else {
                // There are no Kanji Elements, so just display every Reading Element value
                if (kanjiElementList == null || kanjiElementList.isEmpty()) {
                    otherForms += readingElement.getValue() + "\n";
                } else {
                    // The reading is for every Kanji Element
                    for (KanjiElement kanjiElement : kanjiElementList) {
                        otherForms += kanjiElement.getValue() + " [" + readingElement.getValue() + "]\n";
                    }
                }
            }
        }

        // Remove main reading as it's already displayed in the toolbar
        otherForms = otherForms.replace(toolbarTitle + "\n", "");

        // If there are no other forms, we hide the related section
        if(otherForms.isEmpty()) {
            otherFormsDivider.setVisibility(View.GONE);
            otherFormsHeaderTextView.setVisibility(View.GONE);
            otherFormsContentTextView.setVisibility(View.GONE);
        } else {
            // Remove trailing new line
            if (otherForms.endsWith("\n")) {
                otherForms = otherForms.substring(0, otherForms.length() - 1);
            }

            // Set the resulting string
            otherFormsContentTextView.setText(otherForms);
        }
    }

    // Set the priorities section
    // TODO: Refactor into RecyclerView
    // TODO: Separate out into Kanji and Reading priorities
    private void setFrequencyInformation() {
        List<Priority> priorities = dictionaryItem.getPriorities();

        // Convert to a linked hash set to remove duplicates
        Set<String> unifiedPriorities = new LinkedHashSet<>();
        for (Priority priority : priorities) {
            unifiedPriorities.add(priority.getValue());
        }

        // If there are no priorities, remove the Priorities TextViews from view
        if (unifiedPriorities.isEmpty()) {
            freqInfoDivider.setVisibility(View.GONE);
            freqInfoHeaderTextView.setVisibility(View.GONE);
            freqInfoFlexBox.setVisibility(View.GONE);
        } else {
            // Specify the config for our chips
            ChipCloudConfig config = new ChipCloudConfig()
                    .selectMode(ChipCloud.SelectMode.single)
                    .checkedChipColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                    .checkedTextColor(ContextCompat.getColor(getContext(), R.color.white))
                    .uncheckedChipColor(ContextCompat.getColor(getContext(), R.color.light_grey))
                    .uncheckedTextColor(ContextCompat.getColor(getContext(), R.color.default_text))
                    .useInsetPadding(true);

            //Create a new ChipCloud with a Context and ViewGroup:
            final ChipCloud chipCloud = new ChipCloud(getActivity(), freqInfoFlexBox, config);

            // Get whether the entry is common or not
            if (EntryUtils.isCommonEntry(unifiedPriorities)) {
                // If our entry is common, add a 'common' chip
                chipCloud.addChip("common");
            }

            // Add every priority as a chip
            for (String value : unifiedPriorities) {
                chipCloud.addChip(value);
            }

            // Get the map of detailed priority information we use for our dialogs.
            final Map<String, String> detailedPriorityInformation = EntryUtils.getDetailedPriorityInformation();

            chipCloud.setListener(new ChipListener() {
                @Override
                public void chipCheckedChange(final int index, boolean checked, boolean userClicked) {
                    // If the user clicks on a chip
                    if (checked && userClicked) {
                        // Get the text of the chip
                        String label = chipCloud.getLabel(index);
                        String message;

                        // Get the detailed message from the label
                        if (label.startsWith("nf")) {
                            message = detailedPriorityInformation.get("nf");
                        } else {
                            message = detailedPriorityInformation.get(label);
                        }

                        // Create an AlertDialog showing the detailed information
                        new AlertDialog.Builder(getActivity())
                                .setTitle(label)
                                .setMessage(message)
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        // When the user dismisses the dialog, deselect the chip
                                        chipCloud.deselectIndex(index);
                                    }
                                })
                                .show();
                    }
                }
            }, true);
        }
    }

    @Override
    public void onResult(DictionaryItem result) {
        dictionaryItem = result;
        updateViews();
    }

    @Override
    public void onToggleFavouriteResult(boolean toBeAdded, boolean wasSuccessful) {
        if (toBeAdded) {
            if (wasSuccessful) {
                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_white));
                dictionaryItem.setIsFavourite(true);

                if (getView() != null) {
                    Snackbar.make(getView(), R.string.success_add_favourite, Snackbar.LENGTH_LONG).show();
                }
            } else if (getView() != null) {
                Snackbar.make(getView(), R.string.error_add_favourite, Snackbar.LENGTH_LONG).show();
            }
        } else {
            if (wasSuccessful) {
                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_border_white));
                dictionaryItem.setIsFavourite(false);

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
