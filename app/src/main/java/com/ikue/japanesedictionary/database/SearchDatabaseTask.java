package com.ikue.japanesedictionary.database;

import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.OnTaskCompleted;
import com.ikue.japanesedictionary.models.DictionarySearchResultItem;

import java.util.List;

/**
 * Created by luke_c on 15/02/2017.
 */

// The types specified here are the input data type, the progress type, and the result type
public class SearchDatabaseTask extends AsyncTask<Void, Void, List<DictionarySearchResultItem>> {
    private OnTaskCompleted listener;
    private DictionaryDatabase helper;
    private String searchQuery;
    private int searchType;

    public SearchDatabaseTask(OnTaskCompleted listener, DictionaryDatabase helper, String searchQuery, int searchType) {
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
    protected List<DictionarySearchResultItem> doInBackground(Void... params) {
        // Make sure to trim any leading or trailing whitespace in the search query
        return helper.searchDictionary(searchQuery.trim(), searchType);
    }

    @Override
    protected void onPostExecute(List<DictionarySearchResultItem> result) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onResult(result);

        // Update the view and hide the ProgressBar
        listener.toggleProgressBar(false);
    }
}