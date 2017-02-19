package com.ikue.japanesedictionary.database;

/**
 * Created by luke_c on 19/02/2017.
 */

import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.GetFavouritesAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionarySearchResultItem;

import java.util.List;

// TODO: Switch to AsyncTaskLoader so the task can survive configuration changes
// The types specified here are the input data type, the progress type, and the result type
public class GetFavouritesTask extends AsyncTask<Void, Void, List<DictionarySearchResultItem>> {
    private GetFavouritesAsyncCallbacks listener;
    private DictionaryDbHelper helper;

    public GetFavouritesTask(GetFavouritesAsyncCallbacks listener, DictionaryDbHelper helper) {
        this.listener = listener;
        this.helper = helper;
    }

    @Override
    protected List<DictionarySearchResultItem> doInBackground(Void... params) {
        return helper.getAllFavourites();
    }

    @Override
    protected void onPostExecute(List<DictionarySearchResultItem> result) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onResult(result);
    }
}