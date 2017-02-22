package com.ikue.japanesedictionary.interfaces;

import com.ikue.japanesedictionary.models.DictionarySearchResultItem;

import java.util.List;

/**
 * Created by luke_c on 19/02/2017.
 */

public interface GetFavouritesAsyncCallbacks {
    void onResult(List<DictionarySearchResultItem> results);
}
