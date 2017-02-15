package com.ikue.japanesedictionary.interfaces;

import com.ikue.japanesedictionary.models.DictionarySearchResultItem;

import java.util.List;

/**
 * Created by luke_c on 15/02/2017.
 */

public interface OnTaskCompleted {
    void toggleProgressBar(boolean toShow);

    void onResult(List<DictionarySearchResultItem> results);
}
