package com.ikue.japanesedictionary.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.fragments.SearchResultFragment;

/**
 * Created by luke_c on 08/02/2017.
 */

public class SearchResultActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String query = null;
        int type = 0;
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }

        setContentView(R.layout.activity_detail);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = SearchResultFragment.newInstance(query);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
