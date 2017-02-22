package com.ikue.japanesedictionary.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.adapters.SearchResultAdapter;
import com.ikue.japanesedictionary.database.DictionaryDbHelper;
import com.ikue.japanesedictionary.database.GetHistoryTask;
import com.ikue.japanesedictionary.interfaces.GetHistoryAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionarySearchResultItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke_c on 22/02/2017.
 */

// TODO: Put a limit on how far back history goes, perhaps let the user choose
public class HistoryFragment extends Fragment implements GetHistoryAsyncCallbacks {
    // Singleton variable. DO NOT CHANGE
    private static DictionaryDbHelper helper;

    private static AsyncTask task;
    private GetHistoryAsyncCallbacks listener;

    private SearchResultAdapter adapter;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Setup RecyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        // User may have viewed an entry when they come back to the history list, so
        // re-query and get the history again

        // As onResume is always called we only need to do the query here. This will also be called
        // Whenever the user navigates back so we can ensure we always have the correct history
        task = new GetHistoryTask(listener, helper).execute();
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
        // TODO: Detect more granular changes instead of reloading the whole list
        adapter.swapItems(results);
    }
}
