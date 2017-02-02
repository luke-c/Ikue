package com.ikue.japanesedictionary.models;

import java.util.List;

/**
 * Created by luke_c on 02/02/2017.
 */

public class ReadingElement {

    // Unique ID for the Reading Element
    private int mReadingElementId;

    // Restricted to kana and related characters such as chouon and kurikaeshi.
    // Kana usage will be consistent between the keb and reb elements.
    private String mValue;

    // Indicates that the reb, while associated with the keb, cannot be regarded as a true
    // reading of the kanji. E.g. foreign place names.
    private boolean mIsTrueReading;

    // Used to indicate when the reading only applies to a subset of the keb elements in the entry.
    // In its absence, all readings apply to all kanji elements. The contents of this element
    // must exactly match those of one of the keb elements.
    private List<String> mReadingRelation;

    public int getReadingElementId() {
        return mReadingElementId;
    }

    public void setReadingElementId(int readingElementId) {
        mReadingElementId = readingElementId;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public boolean isTrueReading() {
        return mIsTrueReading;
    }

    public void setTrueReading(boolean trueReading) {
        mIsTrueReading = trueReading;
    }

    public List<String> getReadingRelation() {
        return mReadingRelation;
    }

    public void setReadingRelation(List<String> readingRelation) {
        mReadingRelation = readingRelation;
    }
}
