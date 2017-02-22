package com.ikue.japanesedictionary.database;

import android.database.SQLException;
import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.AddToHistoryAsyncCallbacks;

/**
 * Created by luke_c on 22/02/2017.
 */

public class AddToHistoryTask extends AsyncTask<Void, Void, Boolean> {
    private AddToHistoryAsyncCallbacks listener;
    private DictionaryDbHelper helper;
    private int entryId;

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
