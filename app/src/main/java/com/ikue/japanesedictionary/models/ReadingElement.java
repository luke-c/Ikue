package com.ikue.japanesedictionary.models;

import java.util.List;

/**
 * Created by luke_c on 02/02/2017.
 */

public class ReadingElement {

    // Unique ID for the Reading Element
    private int readingElementId;

    // Restricted to kana and related characters such as chouon and kurikaeshi.
    // Kana usage will be consistent between the keb and reb elements.
    private String value;

    // Indicates that the reb, while associated with the keb, cannot be regarded as a true
    // reading of the kanji. E.g. foreign place names.
    private boolean isTrueReading;

    // Used to indicate when the reading only applies to a subset of the keb elements in the entry.
    // In its absence, all readings apply to all kanji elements. The contents of this element
    // must exactly match those of one of the keb elements.
    private List<String> readingRelation;

    public int getReadingElementId() {
        return readingElementId;
    }

    public void setReadingElementId(int readingElementId) {
        this.readingElementId = readingElementId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isTrueReading() {
        return isTrueReading;
    }

    public void setTrueReading(boolean trueReading) {
        isTrueReading = trueReading;
    }

    public List<String> getReadingRelation() {
        return readingRelation;
    }

    public void setReadingRelation(List<String> readingRelation) {
        this.readingRelation = readingRelation;
    }
}
