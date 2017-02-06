package com.ikue.japanesedictionary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikue.japanesedictionary.database.DictionaryDatabase;
import com.ikue.japanesedictionary.models.DictionaryItem;
import com.ikue.japanesedictionary.models.KanjiElement;
import com.ikue.japanesedictionary.models.ReadingElement;
import com.ikue.japanesedictionary.models.SenseElement;

import java.util.List;

/**
 * Created by luke_c on 05/02/2017.
 */

public class EntryDetailFragment extends Fragment {

    private static final String ARG_ENTRY_ID = "ENTRY_ID";

    private static DictionaryDatabase mHelper;
    private static AsyncTask task;
    private static DictionaryItem mDictionaryItem;

    private TextView mReadingsTextView;
    private CollapsingToolbarLayout mCollapsingToolbar;

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
        mHelper = DictionaryDatabase.getInstance(this.getActivity());
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

        mCollapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);
        mReadingsTextView = (TextView) v.findViewById(R.id.sense_element);

        return v;
    }

    private void updateViews() {
        // Set the toolbar title to the main kanji element value + reading if it exists,
        // if not then just use the first reading element value
        List<KanjiElement> kanjiElementList = mDictionaryItem.getKanjiElements();
        List<ReadingElement> readingElementList = mDictionaryItem.getReadingElements();
        String toolbarTitle;
        if (kanjiElementList != null && !kanjiElementList.isEmpty()) {
            toolbarTitle = kanjiElementList.get(0).getValue()
                    + " [" + readingElementList.get(0).getValue() + "]";
        }
        else toolbarTitle = readingElementList.get(0).getValue();

        // TODO: Handle case where value is too big and parts are cutoff, see entry id: 1004000
        mCollapsingToolbar.setTitle(toolbarTitle);


        // TODO: Refactor. Perhaps add TextFields programmatically.
        // Build a string for the 'Meanings' section
        List<SenseElement> senseElementList = mDictionaryItem.getSenseElements();
        String meaningSection = "";
        int glossNumber = 0;
        boolean posExists = false;
        for (SenseElement senseElement : senseElementList) {

            // TODO: Simplify Part of Speech values, currently long and too detailed
            // Get all the Part of Speech elements, and join them into a single string.
            List<String> partOfSpeech = senseElement.getPartOfSpeech();
            if(partOfSpeech != null) {
                // Only add a new line if it is not the first occurrence
                String prefix = posExists == false ? "" : "\n";
                meaningSection += prefix + TextUtils.join(", ", partOfSpeech) + "\n";
                posExists = true;
            }

            // Get all the glosses for a Sense element, and join them into a single string
            List<String> glosses = senseElement.getGlosses();
            if(glosses != null) {
                glossNumber++;
                meaningSection += Integer.toString(glossNumber) + ". "
                        + TextUtils.join("; ", glosses);
            }

            // Get all the Field of Application elements, and join them into a single string
            List<String> fieldOfApplication = senseElement.getFieldOfApplication();
            if(fieldOfApplication != null) {
                meaningSection += " " + TextUtils.join(", ", fieldOfApplication);
            }

            // Get all the Dialect elements, and join them into a single string
            List<String> dialect = senseElement.getDialect();
            if(dialect != null) {
                meaningSection += " " + TextUtils.join(", ", dialect);
            }
            meaningSection+= "\n";
        }
        mReadingsTextView.setText(meaningSection);
    }

    @Override
    public void onDestroy() {
        // Cancel the AsyncTask if it is running when Activity is about to close
        // cancel(false) is safer and doesn't force an instant cancellation
        if(task!=null) {
            task.cancel(false);
        }

        // Close the SQLiteHelper instance
        mHelper.close();
        super.onDestroy();
    }

    // The types specified here are the input data type, the progress type, and the result type
    private class GetEntryTask extends AsyncTask<Integer, Void, DictionaryItem> {

        protected DictionaryItem doInBackground(Integer... id) {
            return mHelper.getEntry(id[0]);
        }


        protected void onPostExecute(DictionaryItem result) {
            // This method is executed in the UIThread
            // with access to the result of the long running task
            mDictionaryItem = result;
            updateViews();
        }
    }
}
