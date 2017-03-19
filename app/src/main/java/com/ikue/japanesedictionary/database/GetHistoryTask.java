package com.ikue.japanesedictionary.database;

import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.GetHistoryAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionaryListEntry;

import java.util.List;

// TODO: Switch to AsyncTaskLoader so the task can survive configuration changes
// The types specified here are the input data type, the progress type, and the result type
public class GetHistoryTask extends AsyncTask<Void, Void, List<DictionaryListEntry>> {
    private GetHistoryAsyncCallbacks listener;
    private DictionaryDbHelper helper;

    public GetHistoryTask(GetHistoryAsyncCallbacks listener, DictionaryDbHelper helper) {
        this.listener = listener;
        this.helper = helper;
    }

    @Override
    protected List<DictionaryListEntry> doInBackground(Void... params) {
        return helper.getHistory();
    }

    @Override
    protected void onPostExecute(List<DictionaryListEntry> result) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onResult(result);
    }
}