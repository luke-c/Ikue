package com.ikue.japanesedictionary;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ikue.japanesedictionary.database.DictionaryDatabase;
import com.ikue.japanesedictionary.models.DictionaryItem;

/**
 * Created by luke_c on 03/02/2017.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String EXTRA_ENTRY_ID = "DETAIL_ACTIVITY_ENTRY_ID";

    private static DictionaryDatabase mHelper;
    private static AsyncTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        // Get a database on startup. Copying from assets folder is all handled
        // by SQLiteAssetHelper
        mHelper = DictionaryDatabase.getInstance(this);
        int entryId = getIntent().getIntExtra(EXTRA_ENTRY_ID, 0);
        task = new GetEntryTask().execute(new Integer(entryId));

        // Set title of Detail page
        collapsingToolbar.setTitle(getString(R.string.item_title));
    }

    public static Intent newIntent(Context packageContext, int entryId) {
        Intent i = new Intent(packageContext, DetailActivity.class);
        i.putExtra(EXTRA_ENTRY_ID, entryId);
        return i;
    }

    @Override
    protected void onDestroy() {
        // Cancel the AsyncTask if it is running when Activity is about to close
        if(task!=null) {
            task.cancel(false);
        }

        // Close the SQLiteHelper instance
        mHelper.close();
        super.onDestroy();
    }

    // The types specified here are the input data type, the progress type, and the result type
    private class GetEntryTask extends AsyncTask<Integer, Void, DictionaryItem> {

        protected DictionaryItem doInBackground(Integer... id) {
            DictionaryItem entry = mHelper.getEntry(id[0]);
            return entry;
        }


        protected void onPostExecute(DictionaryItem result) {
            // This method is executed in the UIThread
            // with access to the result of the long running task
        }
    }
}
