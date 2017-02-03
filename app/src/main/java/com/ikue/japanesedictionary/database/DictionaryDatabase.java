package com.ikue.japanesedictionary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.KanjiElementTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.ReadingElementTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.ReadingRelationTable;
import com.ikue.japanesedictionary.models.DictionaryItem;
import com.ikue.japanesedictionary.models.KanjiElement;
import com.ikue.japanesedictionary.models.ReadingElement;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Arrays;
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
                KanjiElementTable.Cols._ID,
                KanjiElementTable.Cols.VALUE
        };

        // Filter results by WHERE ENTRY_ID = id
        String selection = KanjiElementTable.Cols.ENTRY_ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        Cursor cursor = db.query(
          KanjiElementTable.NAME, // Table to query
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
            int elementId = cursor.getInt(cursor.getColumnIndexOrThrow(KanjiElementTable.Cols._ID));
            String value = cursor.getString(cursor.getColumnIndexOrThrow(KanjiElementTable.Cols.VALUE));

            kanjiElement.setKanjiElementId(elementId);
            kanjiElement.setValue(value);

            kanjiElements.add(kanjiElement);
        }
        return kanjiElements;
    }

    private List<ReadingElement> getReadingElements(int id) {
        SQLiteDatabase db = getReadableDatabase();

        String[] arguments = new String[]{Integer.toString(id)};

        String select = "SELECT r." + ReadingElementTable.Cols._ID + ", r."
                + ReadingElementTable.Cols.VALUE + " AS read_value" + ", r."
                + ReadingElementTable.Cols.IS_TRUE_READING + ", group_concat(rel."
                + ReadingElementTable.Cols.VALUE + ") AS rel_value ";

        String from = "FROM " + ReadingElementTable.NAME + " AS r ";

        String join = "LEFT JOIN " + ReadingRelationTable.NAME + " AS rel ON rel."
                + ReadingRelationTable.Cols.READING_ELEMENT_ID + " = r."
                + ReadingElementTable.Cols._ID + " ";

        String where = "WHERE r." + ReadingElementTable.Cols.ENTRY_ID + " = ? ";

        String groupBy = "GROUP BY r." + ReadingElementTable.Cols._ID;

        Cursor cursor = db.rawQuery(select + from + join + where + groupBy, arguments);

        List<ReadingElement> readingElements = new ArrayList<>();

        while(cursor.moveToNext()) {
            ReadingElement readingElement = new ReadingElement();
            int elementId = cursor.getInt(cursor.getColumnIndexOrThrow(ReadingElementTable.Cols._ID));
            String value = cursor.getString(cursor.getColumnIndexOrThrow("read_value"));
            int isTrueReading = cursor.getInt(cursor.getColumnIndexOrThrow(ReadingElementTable.Cols.IS_TRUE_READING));
            String relationValue = cursor.getString(cursor.getColumnIndexOrThrow("rel_value")); // SAME COLUMN NAME!

            // Split the string on commas, then store each value as a String in a List
            // If the string is null or empty then don't try to split
            List<String> relationValueList = (relationValue != null && !relationValue.isEmpty())
                    ? Arrays.asList(relationValue.split(",")) : null;

            readingElement.setReadingElementId(elementId);
            readingElement.setValue(value);
            readingElement.setTrueReading(!(isTrueReading == 1)); // Inverse the result
            readingElement.setReadingRelation(relationValueList);

            readingElements.add(readingElement);
        }
        return readingElements;
    }

}
