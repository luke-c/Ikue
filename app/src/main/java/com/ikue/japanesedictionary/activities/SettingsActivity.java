package com.ikue.japanesedictionary.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.fragments.SettingsFragment;

/**
 * Created by luke_c on 27/02/2017.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // Setup the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings_activity_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getFragmentManager().findFragmentById(R.id.container)==null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container,
                            new SettingsFragment()).commit();
        }
    }
}
