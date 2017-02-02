package com.ikue.japanesedictionary.models;

/**
 * Created by luke_c on 02/02/2017.
 */

public class KanjiElement {

    // Unique ID for the Kanji Element
    private int mKanjiElementId;

    // Contains a word or short phrase in Japanese which is written using at least one
    // non-kana character (usually kanji, but can be other characters).
    private String mValue;

    public int getKanjiElementId() {
        return mKanjiElementId;
    }

    public void setKanjiElementId(int kanjiElementId) {
        mKanjiElementId = kanjiElementId;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }


}
