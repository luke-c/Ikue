package com.ikue.japanesedictionary.database;

import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.DetailAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionaryEntry;

// The types specified here are the input data type, the progress type, and the result type
public class GetEntryDetailTask extends AsyncTask<Void, Void, DictionaryEntry> {
    private DetailAsyncCallbacks listener;
    private DictionaryDbHelper helper;
    private int entryId;

    public GetEntryDetailTask(DetailAsyncCallbacks listener, DictionaryDbHelper helper, int entryId) {
        this.listener = listener;
        this.helper = helper;
        this.entryId = entryId;
    }

    @Override
    protected DictionaryEntry doInBackground(Void... params) {
        return helper.getEntry(entryId);
    }

    @Override
    protected void onPostExecute(DictionaryEntry result) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onResult(result);
    }
}