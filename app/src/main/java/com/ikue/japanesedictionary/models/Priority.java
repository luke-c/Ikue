package com.ikue.japanesedictionary.models;

/**
 * Created by luke_c on 02/02/2017.
 */

public class Priority {

    private String mValue;

    // If false, then Reading priority
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
