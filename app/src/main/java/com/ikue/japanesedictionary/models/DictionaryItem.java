package com.ikue.japanesedictionary.models;

import java.util.List;

/**
 * Created by luke_c on 01/02/2017.
 */

public class DictionaryItem {

    // A unique number for each entry
    private int entryId;

    private boolean isFavourite;

    // Can be 0 or many
    private List<KanjiElement> kanjiElements;

    // Always 1 or many
    private List<ReadingElement> readingElements;

    // Always 1 or many
    private List<SenseElement> senseElements;

    // Can be 0 or many
    private List<Priority> priorities;

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public boolean getIsFavourite() { return isFavourite; }

    public void setIsFavourite(boolean isFavourite) { this.isFavourite = isFavourite; }

    public List<KanjiElement> getKanjiElements() {
        return kanjiElements;
    }

    public void setKanjiElements(List<KanjiElement> kanjiElements) {
        this.kanjiElements = kanjiElements;
    }

    public List<SenseElement> getSenseElements() {
        return senseElements;
    }

    public void setSenseElements(List<SenseElement> senseElements) {
        this.senseElements = senseElements;
    }

    public List<ReadingElement> getReadingElements() {
        return readingElements;
    }

    public void setReadingElements(List<ReadingElement> readingElements) {
        this.readingElements = readingElements;
    }

    public List<Priority> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<Priority> priorities) {
        this.priorities = priorities;
    }
}
