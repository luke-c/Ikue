package com.ikue.japanesedictionary.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.adapters.DetailViewAdapter;
import com.ikue.japanesedictionary.database.DictionaryDatabase;
import com.ikue.japanesedictionary.models.DictionaryItem;
import com.ikue.japanesedictionary.models.KanjiElement;
import com.ikue.japanesedictionary.models.ReadingElement;

import java.util.List;

/**
 * Created by luke_c on 05/02/2017.
 */

public class EntryDetailFragment extends Fragment {

    private static final String ARG_ENTRY_ID = "ENTRY_ID";

    private TextView otherReadingsTextView;

    private static DictionaryDatabase helper;
    private static AsyncTask task;
    private static DictionaryItem dictionaryItem;

    private CollapsingToolbarLayout collapsingToolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private boolean isEntrySaved;

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
        setRetainInstance(true);

        // Get a database on startup. Copying from assets folder is all handled
        // by SQLiteAssetHelper
        helper = DictionaryDatabase.getInstance(this.getActivity());
        int entryId = getArguments().getInt(ARG_ENTRY_ID, 0);
        task = new GetEntryTask().execute(entryId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false); // Inflate the view

        // Wire up UI components
        ((AppCompatActivity) getActivity()).setSupportActionBar((Toolbar) v.findViewById(R.id.toolbar));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);

        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add item to favourites
                toggleFab();
            }
        });

        otherReadingsTextView = (TextView) v.findViewById(R.id.other_forms_element);

        recyclerView = (RecyclerView) v.findViewById(R.id.meanings_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        // Disables scrolling for RecyclerView,
        recyclerView.setNestedScrollingEnabled(false);

        // Makes RecyclerView wrap its content
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        return v;
    }

    private void toggleFab() {
        if(isEntrySaved) {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_border_white));
        } else {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_white));
        }
        isEntrySaved = !isEntrySaved;
    }

    private void updateViews() {
        // Set the toolbar title to the main kanji element value + reading if it exists,
        // if not then just use the first reading element value
        List<KanjiElement> kanjiElementList = dictionaryItem.getKanjiElements();
        List<ReadingElement> readingElementList = dictionaryItem.getReadingElements();
        String toolbarTitle;
        if (kanjiElementList != null && !kanjiElementList.isEmpty()) {
            toolbarTitle = kanjiElementList.get(0).getValue()
                    + " [" + readingElementList.get(0).getValue() + "]";
        }
        else toolbarTitle = readingElementList.get(0).getValue();

        // TODO: Handle case where value is too big and parts are cutoff, see entry id: 1004000
        collapsingToolbar.setTitle(toolbarTitle);

        // Set the meanings section
        recyclerView.setAdapter(new DetailViewAdapter(dictionaryItem.getSenseElements()));

        // TODO: Refactor into RecyclerView
        // Set the other readings section
        String readings = "";
        for(ReadingElement readingElement : readingElementList) {
            if(!readingElement.getReadingRelation().isEmpty()) {
                // The reading only applies to certain Kanji Elements
                for(String readingRelation : readingElement.getReadingRelation()) {
                    readings += readingRelation + " [" + readingElement.getValue() + "],";
                }

            } else {
                // There are no Kanji Elements, so just display every Reading Element value
                if(kanjiElementList == null || kanjiElementList.isEmpty()) {
                    readings += readingElement.getValue() + ",";
                }
                // The reading is for every Kanji Element
                for(KanjiElement kanjiElement : kanjiElementList) {
                    readings += kanjiElement.getValue() + " [" + readingElement.getValue() + "],";
                }
            }
        }

        // Remove trailing comma
        if(readings.endsWith(",")) {
            readings = readings.substring(0, readings.length() -1);
        }

        // Set the resulting string
        otherReadingsTextView.setText(readings);
    }

    @Override
    public void onDestroy() {
        // Cancel the AsyncTask if it is running when Activity is about to close
        // cancel(false) is safer and doesn't force an instant cancellation
        if(task !=null) {
            task.cancel(false);
        }

        // Close the SQLiteHelper instance
        helper.close();
        super.onDestroy();
    }

    // The types specified here are the input data type, the progress type, and the result type
    private class GetEntryTask extends AsyncTask<Integer, Void, DictionaryItem> {

        protected DictionaryItem doInBackground(Integer... id) {
            return helper.getEntry(id[0]);
        }


        protected void onPostExecute(DictionaryItem result) {
            // This method is executed in the UIThread
            // with access to the result of the long running task
            dictionaryItem = result;
            updateViews();
        }
    }
}
