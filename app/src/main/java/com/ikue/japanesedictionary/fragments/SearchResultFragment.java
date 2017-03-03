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
import com.ikue.japanesedictionary.database.DictionaryDbHelper;
import com.ikue.japanesedictionary.database.SearchDatabaseTask;
import com.ikue.japanesedictionary.interfaces.SearchAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionarySearchResultItem;
import com.ikue.japanesedictionary.utils.SearchUtils;

import java.util.ArrayList;
import java.util.List;

import static com.ikue.japanesedictionary.utils.Constants.SearchTypes.ENGLISH_TYPE;
import static com.ikue.japanesedictionary.utils.Constants.SearchTypes.ROMAJI_TYPE;

/**
 * Created by luke_c on 08/02/2017.
 */

public class SearchResultFragment extends Fragment implements SearchAsyncCallbacks {
    private static final String ARG_SEARCH_TERM = "SEARCH_TERM";

    // Singleton variable. DO NOT CHANGE
    private static DictionaryDbHelper helper;

    private static AsyncTask task;
    private SearchResultAdapter adapter;
    private SearchAsyncCallbacks listener;

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

        // Set the OnTaskComplete listener
        listener = this;

        // Get a database on startup.
        helper = DictionaryDbHelper.getInstance(this.getActivity());

        adapter = new SearchResultAdapter(this.getContext(), new ArrayList<DictionarySearchResultItem>());

        // Get the string the user searched for from the received Intent, and get the type
        searchQuery = getArguments().getString(ARG_SEARCH_TERM, null);
        searchType = SearchUtils.getSearchType(SearchUtils.removeWildcards(searchQuery));
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
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setTitle(R.string.results_view_toolbar_title);
            activity.getSupportActionBar().setSubtitle(searchQuery);
        }
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
        recyclerView.setAdapter(adapter);

        // We need to run the AsyncTask here instead of onCreate so we know that ProgressBar has been
        // instantiated. If we run it on onCreate the AsyncTask will try to show a ProgressBar on a
        // possible non-existing ProgressBar and crash.
        task = new SearchDatabaseTask(listener, helper, searchQuery, searchType).execute();
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
        } else if (id == android.R.id.home) {
            // We want to go back to the previous Activity
            // or the home screen
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        // Cancel the AsyncTask if it is running when Activity is about to close
        // cancel(false) is safer and doesn't force an instant cancellation
        if (task != null) {
            task.cancel(false);
        }

        // Close the SQLiteHelper instance
        helper.close();
        super.onDestroy();
    }

    @Override
    public void onResult(List<DictionarySearchResultItem> results) {
        adapter.swapItems(results);
        showSwitchSearchSnackbar();
    }

    @Override
    public void toggleProgressBar(boolean toShow) {
        // TODO: Fix ProgressBar not showing on executing new AsyncTask
        if (toShow) {
            progressBar.show();
        } else {
            progressBar.hide();
        }
    }

    private void showSwitchSearchSnackbar() {
        // First make sure the view is not null
        if (getView() != null) {
            // Give the user the option to show Romaji results instead
            if (searchType == ENGLISH_TYPE) {
                Snackbar.make(getView(), R.string.search_view_snackbar_english, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.search_view_snackbar_english_action, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                searchType = ROMAJI_TYPE;
                                new SearchDatabaseTask(listener, helper, searchQuery, searchType).execute();
                            }
                        }).show();

                // Give the user the option to show English results instead, due to our naive
                // classification of Romaji
            } else if (searchType == ROMAJI_TYPE) {
                // TODO: Show the actual term the user searched for (Hiragana or Katakana)
                Snackbar.make(getView(), R.string.search_view_snackbar_romaji, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.search_view_snackbar_romaji_action, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                searchType = ENGLISH_TYPE;
                                new SearchDatabaseTask(listener, helper, searchQuery, searchType).execute();
                            }
                        }).show();
            }
        }
    }
}
