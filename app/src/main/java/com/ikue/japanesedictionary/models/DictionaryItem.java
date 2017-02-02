package com.ikue.japanesedictionary.models;

import java.util.List;

/**
 * Created by luke_c on 01/02/2017.
 */

public class DictionaryItem {

    // A unique number for each entry
    private int mEntryId;

    // Can be 0 or many
    private List<KanjiElement> mKanjiElements;

    // Always 1 or many
    private List<ReadingElement> mReadingElements;

    // Always 1 or many
    private List<SenseElement> mSenseElements;

    public int getEntryId() {
        return mEntryId;
    }

    public void setEntryId(int entryId) {
        mEntryId = entryId;
    }

    public List<KanjiElement> getKanjiElements() {
        return mKanjiElements;
    }

    public void setKanjiElements(List<KanjiElement> kanjiElements) {
        mKanjiElements = kanjiElements;
    }

    public List<SenseElement> getSenseElements() {
        return mSenseElements;
    }

    public void setSenseElements(List<SenseElement> senseElements) {
        mSenseElements = senseElements;
    }

    public List<ReadingElement> getReadingElements() {
        return mReadingElements;
    }

    public void setReadingElements(List<ReadingElement> readingElements) {
        mReadingElements = readingElements;
    }
}
