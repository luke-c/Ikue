package com.ikue.japanesedictionary.interfaces;

import com.ikue.japanesedictionary.models.DictionaryListEntry;

import java.util.List;

public interface GetHistoryAsyncCallbacks {
    void onResult(List<DictionaryListEntry> results);
}
