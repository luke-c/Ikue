package com.ikue.japanesedictionary.interfaces;

import com.ikue.japanesedictionary.models.DictionaryListEntry;

import java.util.List;

public interface SearchAsyncCallbacks {
    void toggleProgressBar(boolean toShow);

    void onResult(List<DictionaryListEntry> results);
}
