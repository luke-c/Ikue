package com.ikue.japanesedictionary.database;

import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.OnShortTaskCompleted;
import com.ikue.japanesedictionary.models.DictionaryItem;

/**
 * Created by luke_c on 15/02/2017.
 */

// The types specified here are the input data type, the progress type, and the result type
public class GetEntryDetailTask extends AsyncTask<Void, Void, DictionaryItem> {
    private OnShortTaskCompleted listener;
    private DictionaryDatabase helper;
    private int entryId;

    public GetEntryDetailTask(OnShortTaskCompleted listener, DictionaryDatabase helper, int entryId) {
        this.listener = listener;
        this.helper = helper;
        this.entryId = entryId;
    }

    @Override
    protected DictionaryItem doInBackground(Void... params) {
        return helper.getEntry(entryId);
    }

    @Override
    protected void onPostExecute(DictionaryItem result) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onResult(result);
    }
}