package com.ikue.japanesedictionary.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by luke_c on 08/02/2017.
 */

public class SearchResultFragment extends Fragment {
    private static final String ARG_SEARCH_TERM = "SEARCH_TERM";
    private static final String ARG_SEARCH_TYPE = "SEARCH_TYPE";

    public static SearchResultFragment newInstance(String query, int type) {
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_TERM, query);
        args.putInt(ARG_SEARCH_TYPE, type);

        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
