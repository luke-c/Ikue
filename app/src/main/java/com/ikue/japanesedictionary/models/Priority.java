package com.ikue.japanesedictionary.models;

public class Priority {

    // Unique ID for the Priority
    private int priorityId;

    // Records information about the relative priority of the entry, e.g. news1/2, ichi1/2, spec1/2.
    private String value;

    // If false, then a priority associated with the Reading Element.
    private boolean isKanjiReadingPriority;

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isKanjiReadingPriority() {
        return isKanjiReadingPriority;
    }

    public void setKanjiReadingPriority(boolean kanjiReadingPriority) {
        isKanjiReadingPriority = kanjiReadingPriority;
    }
}
