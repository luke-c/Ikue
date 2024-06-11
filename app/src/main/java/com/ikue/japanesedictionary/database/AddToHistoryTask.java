package com.ikue.japanesedictionary.database;

import android.database.SQLException;
import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.AddToHistoryAsyncCallbacks;

public class AddToHistoryTask extends AsyncTask<Void, Void, Boolean> {
    private final AddToHistoryAsyncCallbacks listener;
    private final DictionaryDbHelper helper;
    private final int entryId;

    public AddToHistoryTask(AddToHistoryAsyncCallbacks listener, DictionaryDbHelper helper,
                            int entryId) {
        this.listener = listener;
        this.helper = helper;
        this.entryId = entryId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            helper.addToHistory(entryId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean wasSuccessful) {
        // This method is executed in the UIThread
        // with access to the result of the long running task
        listener.onAddToHistoryResult(wasSuccessful);
    }
}
