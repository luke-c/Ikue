package com.ikue.japanesedictionary.interfaces;

import com.ikue.japanesedictionary.models.DictionarySearchResultItem;

import java.util.List;

/**
 * Created by luke_c on 22/02/2017.
 */

public interface GetHistoryAsyncCallbacks {
    void onResult(List<DictionarySearchResultItem> results);
}
