package com.ikue.japanesedictionary.database;

import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.GetFavouritesAsyncCallbacks;
import com.ikue.japanesedictionary.interfaces.GetHistoryAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionarySearchResultItem;

import java.util.List;

/**
 * Created by luke_c on 22/02/2017.
 */

// TODO: Switch to AsyncTaskLoader so the task can survive configuration changes
// The types specified here are the input data type, the progress type, and the result type
public class GetHistoryTask extends AsyncTask<Void, Void, List<DictionarySearchResultItem>> {
    private GetHistoryAsyncCallbacks listener;
    private DictionaryDbHelper helper;

    public GetHistoryTask(GetHistoryAsyncCallbacks listener, DictionaryDbHelper helper) {
        this.listener = listener;
        this.helper = helper;
    }

    @Override
    protected List<DictionarySearchResultItem> doInBackground(Void... params) {
        return helper.getHistory();
    }

    @Override
    protected void onPostExecute(List<DictionarySearchResultItem> result) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onResult(result);
    }
}