package com.ikue.japanesedictionary.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.adapters.TipsAdapter;
import com.ikue.japanesedictionary.utils.EdgeToEdgeUtils;
import com.ikue.japanesedictionary.utils.TipsUtils;

public class TipsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_tips);

        // Setup the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tips_activity_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Lookup the recyclerview in activity layout
        RecyclerView recyclerView = findViewById(R.id.tips_recyclerview);

        TipsAdapter adapter = new TipsAdapter(TipsUtils.getTips());

        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);

        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        EdgeToEdgeUtils.applySystemBarsPadding(toolbar);
        EdgeToEdgeUtils.applyRecyclerViewPadding(recyclerView);
    }
}
