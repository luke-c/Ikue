package com.ikue.japanesedictionary.models;

/**
 * Created by luke_c on 02/02/2017.
 */

public class Priority {

    // Records information about the relative priority of the entry, e.g. news1/2, ichi1/2, spec1/2.
    private String mValue;

    // If false, then a priority associated with the Reading Element.
    private boolean mIsKanjiPriority;

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public boolean isKanjiPriority() {
        return mIsKanjiPriority;
    }

    public void setKanjiPriority(boolean kanjiPriority) {
        mIsKanjiPriority = kanjiPriority;
    }
}
