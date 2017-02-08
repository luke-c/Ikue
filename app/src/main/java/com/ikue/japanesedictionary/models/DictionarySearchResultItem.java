package com.ikue.japanesedictionary.models;

import java.util.List;

/**
 * Created by luke_c on 02/02/2017.
 */

public class DictionarySearchResultItem {

    // A unique ID for each entry in the dictionary
    private int entryId;

    // The first Kanji Element 'keb' value of an entry, null if there are no Kanji Elements.
    private String kanjiElementValue;

    // The first Reading Element 'reb' value of an entry, will never be null.
    private String readingElementValue;

    // All gloss values of an entry as a single string
    private List<String> glossValue;

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public List<String> getGlossValue() {
        return glossValue;
    }

    public void setGlossValue(List<String> glossValue) {
        this.glossValue = glossValue;
    }

    public String getKanjiElementValue() {
        return kanjiElementValue;
    }

    public void setKanjiElementValue(String kanjiElementValue) {
        this.kanjiElementValue = kanjiElementValue;
    }

    public String getReadingElementValue() {
        return readingElementValue;
    }

    public void setReadingElementValue(String readingElementValue) {
        this.readingElementValue = readingElementValue;
    }
}
