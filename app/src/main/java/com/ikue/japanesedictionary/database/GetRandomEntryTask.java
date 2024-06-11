package com.ikue.japanesedictionary.database;

import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.DetailAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionaryEntry;

public class GetRandomEntryTask extends AsyncTask<Void, Void, DictionaryEntry> {
    private final DetailAsyncCallbacks listener;
    private final DictionaryDbHelper helper;

    public GetRandomEntryTask(DetailAsyncCallbacks listener, DictionaryDbHelper helper) {
        this.listener = listener;
        this.helper = helper;
    }

    @Override
    protected DictionaryEntry doInBackground(Void... params) {
        return helper.getRandomEntry();
    }

    @Override
    protected void onPostExecute(DictionaryEntry result) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onResult(result);
    }
}
