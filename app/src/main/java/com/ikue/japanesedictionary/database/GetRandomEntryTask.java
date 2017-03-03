package com.ikue.japanesedictionary.database;

import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.DetailAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionaryItem;

/**
 * Created by luke_c on 02/03/2017.
 */

public class GetRandomEntryTask extends AsyncTask<Void, Void, DictionaryItem> {
    private DetailAsyncCallbacks listener;
    private DictionaryDbHelper helper;

    public GetRandomEntryTask(DetailAsyncCallbacks listener, DictionaryDbHelper helper) {
        this.listener = listener;
        this.helper = helper;
    }

    @Override
    protected DictionaryItem doInBackground(Void... params) {
        return helper.getRandomEntry();
    }

    @Override
    protected void onPostExecute(DictionaryItem result) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onResult(result);
    }
}
