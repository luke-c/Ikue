package com.ikue.japanesedictionary.models;

import java.util.List;

/**
 * Created by luke_c on 02/02/2017.
 */

public class SenseElement {

    // Unique ID for the Sense Element
    private int mSenseElementId;

    // Part of speech information about the Sense Element, e.g. Noun, Verb, Adverb.
    private List<String> mPartOfSpeech;

    // Field of application information about the Sense Element, e.g. Medical, Computing.
    // Absence implies general application.
    private List<String> mFieldOfApplication;

    // Dialect information about the Sense Element, e.g. Kansai-ben, Nagoya-ben, Osaka-ben.
    private List<String> mDialect;

    // Target-language words or phrases which are equivalents to the Japanese word.
    // May be omitted in entries which are purely for cross-referencing.
    private List<String> mGlosses;

    public List<String> getDialect() {
        return mDialect;
    }

    public void setDialect(List<String> dialect) {
        mDialect = dialect;
    }

    public List<String> getFieldOfApplication() {
        return mFieldOfApplication;
    }

    public void setFieldOfApplication(List<String> fieldOfApplication) {
        mFieldOfApplication = fieldOfApplication;
    }

    public List<String> getGlosses() {
        return mGlosses;
    }

    public void setGlosses(List<String> glosses) {
        mGlosses = glosses;
    }

    public List<String> getPartOfSpeech() {
        return mPartOfSpeech;
    }

    public void setPartOfSpeech(List<String> partOfSpeech) {
        mPartOfSpeech = partOfSpeech;
    }

    public int getSenseElementId() {
        return mSenseElementId;
    }

    public void setSenseElementId(int senseElementId) {
        mSenseElementId = senseElementId;
    }
}
