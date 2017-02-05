package com.ikue.japanesedictionary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikue.japanesedictionary.database.DictionaryDatabase;
import com.ikue.japanesedictionary.models.DictionaryItem;

/**
 * Created by luke_c on 05/02/2017.
 */

public class EntryDetailFragment extends Fragment {

    private static final String ARG_ENTRY_ID = "ENTRY_ID";

    private static DictionaryDatabase mHelper;
    private static AsyncTask task;
    private static DictionaryItem mDictionaryItem;

    private TextView mTestTextView;
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

        // Set Collapsing Toolbar layout to the screen
        mCollapsingToolbar = (CollapsingToolbarLayout) v.findViewById(R.id.collapsing_toolbar);

        // Set title of Detail page
        mCollapsingToolbar.setTitle(getString(R.string.item_title));

        mTestTextView = (TextView) v.findViewById(R.id.place_detail);

        return v;
    }

    private void updateViews() {
        mCollapsingToolbar.setTitle(mDictionaryItem.getKanjiElements().get(0).getValue());
        mTestTextView.setText(mDictionaryItem.getReadingElements().get(0).getValue());
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
