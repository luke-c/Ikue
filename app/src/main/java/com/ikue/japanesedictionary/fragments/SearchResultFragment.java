package com.ikue.japanesedictionary.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.adapters.SearchResultAdapter;
import com.ikue.japanesedictionary.database.DictionaryDatabase;
import com.ikue.japanesedictionary.models.DictionarySearchResultItem;

import java.lang.Character.UnicodeBlock;
import java.util.List;

/**
 * Created by luke_c on 08/02/2017.
 */

public class SearchResultFragment extends Fragment {
    private static final String ARG_SEARCH_TERM = "SEARCH_TERM";

    private static DictionaryDatabase helper;
    private static AsyncTask task;
    private static List<DictionarySearchResultItem> searchResults;

    private RecyclerView recyclerView;

    private static final int KANA_TYPE = 0;
    private static final int ROMAJI_TYPE = 1;
    private static final int KANJI_TYPE = 2;
    private static final int ENGLISH_TYPE = 3;

    public static SearchResultFragment newInstance(String query) {
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_TERM, query);

        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain the fragment so rotation does not repeatedly fire off new AsyncTasks
        setRetainInstance(true);

        // Get a database on startup.
        helper = DictionaryDatabase.getInstance(this.getActivity());

        String searchQuery = getArguments().getString(ARG_SEARCH_TERM, null);
        int searchType = getSearchType(searchQuery);

        task = new GetSearchResultsTask(searchQuery, searchType).execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false); // Inflate the view

        // Toolbar
        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle(R.string.results_view_toolbar_title);
        setHasOptionsMenu(true);

        // Recycler view
        recyclerView = (RecyclerView) v.findViewById(R.id.search_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        // Cancel the AsyncTask if it is running when Activity is about to close
        // cancel(false) is safer and doesn't force an instant cancellation
        if(task !=null) {
            task.cancel(false);
        }

        // Close the SQLiteHelper instance
        helper.close();
        super.onDestroy();
    }

    private void updateViews() {
        recyclerView.setAdapter(new SearchResultAdapter(this.getContext(), searchResults));
    }

    // TODO: Support for Romaji
    // Get what type the search term is. Can either be Kanji, Kana, or English.
    private int getSearchType(String searchTerm) {
        boolean containsKana = false;

        // Check every character of the string
        for(char c : searchTerm.toCharArray()) {
            // If the current character is a Kanji (or Chinese/Korean character)
            if(UnicodeBlock.of(c) == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                // Once we find a single Kanji character, we know to search the Kanji Element
                return KANJI_TYPE;
                // If the current character is a Hiragana or Katakana character
            } else if(UnicodeBlock.of(c) == UnicodeBlock.HIRAGANA || UnicodeBlock.of(c) == UnicodeBlock.KATAKANA) {
                containsKana = true;
            }
        }
        // If by the end there was no Kana characters, search in English
        return containsKana ? KANA_TYPE : ENGLISH_TYPE;
    }

    // The types specified here are the input data type, the progress type, and the result type
    private class GetSearchResultsTask extends AsyncTask<Void, Void, List<DictionarySearchResultItem>> {

        private String searchQuery;
        private int type;

        public GetSearchResultsTask(String searchQuery, int type) {
            this.searchQuery = searchQuery;
            this.type = type;
        }

        @Override
        protected List<DictionarySearchResultItem> doInBackground(Void... params) {
            return helper.searchDictionary(searchQuery, type);
        }

        @Override
        protected void onPostExecute(List<DictionarySearchResultItem> result) {
            // This method is executed in the UIThread
            // with access to the result of the long running task
            searchResults = result;
            updateViews();
        }
    }
}
