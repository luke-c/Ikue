package com.ikue.japanesedictionary.database;

import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.SearchAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionaryListEntry;

import java.util.List;

// TODO: Switch to AsyncTaskLoader so the task can survive configuration changes
// The types specified here are the input data type, the progress type, and the result type
public class SearchDatabaseTask extends AsyncTask<Void, Void, List<DictionaryListEntry>> {
    private final SearchAsyncCallbacks listener;
    private final DictionaryDbHelper helper;
    private final String searchQuery;
    private final int searchType;

    public SearchDatabaseTask(SearchAsyncCallbacks listener, DictionaryDbHelper helper,
                              String searchQuery, int searchType) {
        this.listener = listener;
        this.helper = helper;
        this.searchQuery = searchQuery;
        this.searchType = searchType;
    }

    @Override
    protected void onPreExecute() {
        // Show the ProgressBar just before we search the database
        listener.toggleProgressBar(true);
    }

    @Override
    protected List<DictionaryListEntry> doInBackground(Void... params) {
        // Make sure to trim any leading or trailing whitespace in the search query
        return helper.searchDictionary(searchQuery.trim(), searchType);
    }

    @Override
    protected void onPostExecute(List<DictionaryListEntry> result) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onResult(result);

        // Update the view and hide the ProgressBar
        listener.toggleProgressBar(false);
    }
}