package com.ikue.japanesedictionary.models;

import java.util.List;

/**
 * Created by luke_c on 02/02/2017.
 */

public class DictionarySearchResultItem {

    // A unique ID for each entry in the dictionary
    private int mEntryId;

    // The first Kanji Element 'keb' value of an entry, null if there are no Kanji Elements.
    private String mKanjiElementValue;

    // The first Reading Element 'reb' value of an entry, will never be null.
    private String mReadingElementValue;

    // All gloss values of an entry as a single string
    private List<String> mGlossValue;

    public int getEntryId() {
        return mEntryId;
    }

    public void setEntryId(int entryId) {
        mEntryId = entryId;
    }

    public List<String> getGlossValue() {
        return mGlossValue;
    }

    public void setGlossValue(List<String> glossValue) {
        mGlossValue = glossValue;
    }

    public String getKanjiElementValue() {
        return mKanjiElementValue;
    }

    public void setKanjiElementValue(String kanjiElementValue) {
        mKanjiElementValue = kanjiElementValue;
    }

    public String getReadingElementValue() {
        return mReadingElementValue;
    }

    public void setReadingElementValue(String readingElementValue) {
        mReadingElementValue = readingElementValue;
    }
}
