package com.ikue.japanesedictionary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ikue.japanesedictionary.models.DictionaryItem;
import com.ikue.japanesedictionary.models.KanjiElement;
import com.ikue.japanesedictionary.models.ReadingElement;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luke_c on 01/02/2017.
 */

public class DictionaryDatabase extends SQLiteAssetHelper {

    private static DictionaryDatabase sInstance;

    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 1;

    public static synchronized DictionaryDatabase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DictionaryDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    public DictionaryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Don't return cursor! Get a DictionaryItem from the cursor!
    public DictionaryItem getEntry(int id) {

        DictionaryItem entry = new DictionaryItem();
        entry.setEntryId(id);
        entry.setKanjiElements(getKanjiElements(id));
        entry.setReadingElements(getReadingElements(id));
        //entry.setSenseElements(getSenseElements(id));
        //entry.setPriorities(getPriorities(id));

        return entry;
    }

    private List<KanjiElement> getKanjiElements(int id) {
        SQLiteDatabase db = getReadableDatabase();

        // The columns from the database I will use after the query
        String[] projection = {
                "_ID",
                "VALUE"
        };

        // Filter results by WHERE ENTRY_ID = id
        String selection = "ENTRY_ID" + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        Cursor cursor = db.query(
          "JMdict_Kanji_Element", // Table to query
          projection, // The columns to return
          selection, // The columns for the WHERE clause
          selectionArgs, // The values for the WHERE clause
          null, // Group by
          null, // Filter by
          null  // Sort order
        );

        List<KanjiElement> kanjiElements = new ArrayList<>();

        while(cursor.moveToNext()) {
            KanjiElement kanjiElement = new KanjiElement();
            int entryId = cursor.getInt(cursor.getColumnIndexOrThrow("_ID"));
            String value = cursor.getString(cursor.getColumnIndexOrThrow("VALUE"));

            kanjiElement.setKanjiElementId(entryId);
            kanjiElement.setValue(value);

            kanjiElements.add(kanjiElement);
        }
        return kanjiElements;
    }

    private List<ReadingElement> getReadingElements(int id) {
        return new ArrayList<>();
    }

}
