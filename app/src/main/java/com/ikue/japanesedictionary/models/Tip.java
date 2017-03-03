package com.ikue.japanesedictionary.models;

/**
 * Created by luke_c on 03/03/2017.
 */

public class Tip {
    private String title;
    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Tip(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
