package com.ikue.japanesedictionary.models;

/**
 * Created by luke_c on 02/02/2017.
 */

public class Priority {

    private int mPriorityId;

    // Records information about the relative priority of the entry, e.g. news1/2, ichi1/2, spec1/2.
    private String mValue;

    // If false, then a priority associated with the Reading Element.
    private boolean mIsKanjiReadingPriority;

    public int getPriorityId() {
        return mPriorityId;
    }

    public void setPriorityId(int priorityId) {
        mPriorityId = priorityId;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public boolean isKanjiReadingPriority() {
        return mIsKanjiReadingPriority;
    }

    public void setKanjiReadingPriority(boolean kanjiReadingPriority) {
        mIsKanjiReadingPriority = kanjiReadingPriority;
    }
}
