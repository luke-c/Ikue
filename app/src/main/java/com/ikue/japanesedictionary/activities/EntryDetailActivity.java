package com.ikue.japanesedictionary.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ikue.japanesedictionary.fragments.EntryDetailFragment;
import com.ikue.japanesedictionary.R;

public class EntryDetailActivity extends AppCompatActivity {

    private static final String EXTRA_ENTRY_ID = "DETAIL_ACTIVITY_ENTRY_ID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            int entryId = getIntent().getIntExtra(EXTRA_ENTRY_ID, 0);
            fragment = EntryDetailFragment.newInstance(entryId);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    public static Intent newIntent(Context packageContext, int entryId) {
        Intent i = new Intent(packageContext, EntryDetailActivity.class);
        i.putExtra(EXTRA_ENTRY_ID, entryId);
        return i;
    }
}
