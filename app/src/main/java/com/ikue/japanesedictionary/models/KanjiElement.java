package com.ikue.japanesedictionary.models;

public class KanjiElement {

    // Unique ID for the Kanji Element
    private int kanjiElementId;

    // Contains a word or short phrase in Japanese which is written using at least one
    // non-kana character (usually kanji, but can be other characters).
    private String value;

    public int getKanjiElementId() {
        return kanjiElementId;
    }

    public void setKanjiElementId(int kanjiElementId) {
        this.kanjiElementId = kanjiElementId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
