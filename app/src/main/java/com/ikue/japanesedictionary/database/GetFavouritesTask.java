package com.ikue.japanesedictionary.database;

import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.GetFavouritesAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionaryListEntry;

import java.util.List;

// TODO: Switch to AsyncTaskLoader so the task can survive configuration changes
// The types specified here are the input data type, the progress type, and the result type
public class GetFavouritesTask extends AsyncTask<Void, Void, List<DictionaryListEntry>> {
    private final GetFavouritesAsyncCallbacks listener;
    private final DictionaryDbHelper helper;

    public GetFavouritesTask(GetFavouritesAsyncCallbacks listener, DictionaryDbHelper helper) {
        this.listener = listener;
        this.helper = helper;
    }

    @Override
    protected List<DictionaryListEntry> doInBackground(Void... params) {
        return helper.getFavourites();
    }

    @Override
    protected void onPostExecute(List<DictionaryListEntry> result) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onResult(result);
    }
}