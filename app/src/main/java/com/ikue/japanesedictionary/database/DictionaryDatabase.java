package com.ikue.japanesedictionary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ikue.japanesedictionary.models.DictionaryItem;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by luke_c on 01/02/2017.
 */

public class DictionaryDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase mDatabase;

    public DictionaryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDatabase = getWritableDatabase();
    }

    // Don't return cursor! Get a DictionaryItem from the cursor!
    public DictionaryItem getEntry(int id) {

        return new DictionaryItem();
    }

}
