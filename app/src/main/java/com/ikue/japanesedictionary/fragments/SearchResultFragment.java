package com.ikue.japanesedictionary.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
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
import com.ikue.japanesedictionary.utils.Constants.SearchTypes;
import com.ikue.japanesedictionary.utils.SearchUtils;
import com.ikue.japanesedictionary.utils.WanaKanaJava;

import java.util.List;

import static com.ikue.japanesedictionary.utils.Constants.SearchTypes.ENGLISH_TYPE;
import static com.ikue.japanesedictionary.utils.Constants.SearchTypes.ROMAJI_TYPE;

/**
 * Created by luke_c on 08/02/2017.
 */

public class SearchResultFragment extends Fragment {
    private static final String ARG_SEARCH_TERM = "SEARCH_TERM";

    private static DictionaryDatabase helper;
    private static AsyncTask task;
    private static List<DictionarySearchResultItem> searchResults;
    private static WanaKanaJava wanaKanaJava;

    private static int searchType;
    private static String searchQuery;

    private RecyclerView recyclerView;
    private ContentLoadingProgressBar progressBar;

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

        wanaKanaJava = new WanaKanaJava(false);

        // Get a database on startup.
        helper = DictionaryDatabase.getInstance(this.getActivity());

        // Get the string the user searched for from the received Intent, and get the type
        searchQuery = getArguments().getString(ARG_SEARCH_TERM, null);
        searchType = SearchUtils.getSearchType(searchQuery);

        // If we will search in Romaji, then make sure we convert to Kana first
        if(searchType == SearchTypes.ROMAJI_TYPE) {
            searchQuery = wanaKanaJava.toKana(searchQuery);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false); // Inflate the view
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Toolbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle(R.string.results_view_toolbar_title);
        setHasOptionsMenu(true);

        // Progressbar
        progressBar = (ContentLoadingProgressBar) view.findViewById(R.id.search_progress_bar);

        // Recycler view
        recyclerView = (RecyclerView) view.findViewById(R.id.search_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // We need to run the AsyncTask here instead of onCreate so we know that ProgressBar has been
        // instantiated. If we run it on onCreate the AsyncTask will try to show a ProgressBar on a
        // possible non-existing ProgressBar and crash.
        task = new GetSearchResultsTask().execute();
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
        showSwitchSearchSnackbar();
    }

    private void showSwitchSearchSnackbar() {
        // TODO: Fix ProgressBar not showing on executing new AsyncTask
        // Give the user the option to show Romaji results instead
        if(searchType == ENGLISH_TYPE) {
            Snackbar.make(getView(), R.string.search_view_snackbar_english, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.search_view_snackbar_english_action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchType = ROMAJI_TYPE;
                    searchQuery = wanaKanaJava.toHiragana(searchQuery);
                    new GetSearchResultsTask().execute();
                }
            }).show();

            // Give the user the option to show English results instead, due to our naive
            // classification of Romaji
        } else if(searchType == ROMAJI_TYPE) {
            Snackbar.make(getView(), R.string.search_view_snackbar_romaji, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.search_view_snackbar_romaji_action, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            searchType = ENGLISH_TYPE;
                            searchQuery = wanaKanaJava.toRomaji(searchQuery);
                            new GetSearchResultsTask().execute();
                        }
                    }).show();
        }
    }


    // The types specified here are the input data type, the progress type, and the result type
    private class GetSearchResultsTask extends AsyncTask<Void, Void, List<DictionarySearchResultItem>> {
        @Override
        protected void onPreExecute() {
            // Show the ProgressBar just before we search the database
            progressBar.show();
        }

        @Override
        protected List<DictionarySearchResultItem> doInBackground(Void... params) {
            return helper.searchDictionary(searchQuery, searchType);
        }

        @Override
        protected void onPostExecute(List<DictionarySearchResultItem> result) {
            // This method is executed in the UIThread
            // with access to the result of the long running task
            searchResults = result;

            // Update the view and hide the ProgressBar
            updateViews();
            progressBar.hide();
        }
    }
}
