package com.ikue.japanesedictionary.models;

import java.util.List;

/**
 * Created by luke_c on 02/02/2017.
 */

public class SenseElement {

    // Unique ID for the Sense Element
    private int senseElementId;

    // Part of speech information about the Sense Element, e.g. Noun, Verb, Adverb.
    private List<String> partOfSpeech;

    // Field of application information about the Sense Element, e.g. Medical, Computing.
    // Absence implies general application.
    private List<String> fieldOfApplication;

    // Dialect information about the Sense Element, e.g. Kansai-ben, Nagoya-ben, Osaka-ben.
    private List<String> dialect;

    // Target-language words or phrases which are equivalents to the Japanese word.
    // May be omitted in entries which are purely for cross-referencing.
    private List<String> glosses;

    public List<String> getDialect() {
        return dialect;
    }

    public void setDialect(List<String> dialect) {
        this.dialect = dialect;
    }

    public List<String> getFieldOfApplication() {
        return fieldOfApplication;
    }

    public void setFieldOfApplication(List<String> fieldOfApplication) {
        this.fieldOfApplication = fieldOfApplication;
    }

    public List<String> getGlosses() {
        return glosses;
    }

    public void setGlosses(List<String> glosses) {
        this.glosses = glosses;
    }

    public List<String> getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(List<String> partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public int getSenseElementId() {
        return senseElementId;
    }

    public void setSenseElementId(int senseElementId) {
        this.senseElementId = senseElementId;
    }
}
