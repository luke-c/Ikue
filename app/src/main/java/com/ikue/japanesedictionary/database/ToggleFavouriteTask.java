package com.ikue.japanesedictionary.database;

import android.database.SQLException;
import android.os.AsyncTask;

import com.ikue.japanesedictionary.interfaces.ToggleFavouriteAsyncCallbacks;

public class ToggleFavouriteTask extends AsyncTask<Void, Void, Boolean> {
    private final ToggleFavouriteAsyncCallbacks listener;
    private final DictionaryDbHelper helper;
    private final int entryId;
    private final boolean toBeAdded;

    public ToggleFavouriteTask(ToggleFavouriteAsyncCallbacks listener, DictionaryDbHelper helper,
                               int entryId, boolean toBeAdded) {
        this.listener = listener;
        this.helper = helper;
        this.entryId = entryId;
        this.toBeAdded = toBeAdded;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (toBeAdded) {
                helper.addToFavourites(entryId);
            } else {
                helper.removeFromFavourites(entryId);
            }
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
        listener.onToggleFavouriteResult(toBeAdded, wasSuccessful);
    }
}
