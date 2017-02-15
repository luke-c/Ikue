package com.ikue.japanesedictionary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.GlossTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.KanjiElementTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.PriorityTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.ReadingElementTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.ReadingRelationTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.SenseDialectTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.SenseElementTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.SenseFieldTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.Jmdict.SensePosTable;
import com.ikue.japanesedictionary.models.DictionaryItem;
import com.ikue.japanesedictionary.models.DictionarySearchResultItem;
import com.ikue.japanesedictionary.models.KanjiElement;
import com.ikue.japanesedictionary.models.Priority;
import com.ikue.japanesedictionary.models.ReadingElement;
import com.ikue.japanesedictionary.models.SenseElement;
import com.ikue.japanesedictionary.utils.WanaKanaJava;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import static com.ikue.japanesedictionary.utils.Constants.SearchTypes.*;

/**
 * Created by luke_c on 01/02/2017.
 */

// TODO: Add catch blocks for queries
public class DictionaryDatabase extends SQLiteAssetHelper {

    private static DictionaryDatabase instance;
    private static SQLiteDatabase db;

    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 1;

    private final String LOG_TAG = this.getClass().toString();

    public static synchronized DictionaryDatabase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (instance == null) {
            instance = new DictionaryDatabase(context.getApplicationContext());
        }
        return instance;
    }

    private DictionaryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //setForcedUpgrade();
    }

    private static String getSearchByKanaQuery() {
        String select = "SELECT re." + ReadingElementTable.Cols.ENTRY_ID + ", group_concat(ke."
                + KanjiElementTable.Cols.VALUE + ", '§') AS kanji_value, group_concat(re."
                + ReadingElementTable.Cols.VALUE + ", '§') AS read_value, group_concat(gloss."
                + GlossTable.Cols.VALUE + ", '§') AS gloss_value ";

        String from = "FROM " + ReadingElementTable.NAME + " AS re ";

        String join = "JOIN " + GlossTable.NAME + " AS gloss ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = gloss." + GlossTable.Cols.ENTRY_ID
                + " LEFT JOIN " + KanjiElementTable.NAME + " AS ke ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = ke." + KanjiElementTable.Cols.ENTRY_ID
                + " ";

        String where = "WHERE re." + ReadingElementTable.Cols.ENTRY_ID + " IN ";

        String whereSubQuery = "(SELECT " + ReadingElementTable.Cols.ENTRY_ID + " FROM "
                + ReadingElementTable.NAME + " WHERE VALUE LIKE ?) ";

        String groupBy = "GROUP BY re." + ReadingElementTable.Cols.ENTRY_ID;

        return select + from + join + where + whereSubQuery + groupBy;
    }

    private static String getSearchByKanjiQuery() {
        String select = "SELECT re." + ReadingElementTable.Cols.ENTRY_ID + ", group_concat(ke."
                + KanjiElementTable.Cols.VALUE + ", '§') AS kanji_value, group_concat(re."
                + ReadingElementTable.Cols.VALUE + ", '§') AS read_value, group_concat(gloss."
                + GlossTable.Cols.VALUE + ", '§') AS gloss_value ";

        String from = "FROM " + ReadingElementTable.NAME + " AS re ";

        String join = "JOIN " + GlossTable.NAME + " AS gloss ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = gloss." + GlossTable.Cols.ENTRY_ID
                + " JOIN " + KanjiElementTable.NAME + " AS ke ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = ke." + KanjiElementTable.Cols.ENTRY_ID
                + " ";

        String where = "WHERE ke." + KanjiElementTable.Cols.ENTRY_ID + " IN ";

        String whereSubQuery = "(SELECT " + KanjiElementTable.Cols.ENTRY_ID + " FROM "
                + KanjiElementTable.NAME + " WHERE VALUE LIKE ?) ";

        String groupBy = "GROUP BY re." + ReadingElementTable.Cols.ENTRY_ID;

        return select + from + join + where + whereSubQuery + groupBy;
    }

    private static String getSearchByEnglishQuery() {
        String select = "SELECT re." + ReadingElementTable.Cols.ENTRY_ID + ", group_concat(ke."
                + KanjiElementTable.Cols.VALUE + ", '§') AS kanji_value, group_concat(re."
                + ReadingElementTable.Cols.VALUE + ", '§') AS read_value, group_concat(gloss."
                + GlossTable.Cols.VALUE + ", '§') AS gloss_value ";

        String from = "FROM " + ReadingElementTable.NAME + " AS re ";

        String join = "LEFT JOIN " + KanjiElementTable.NAME + " AS ke ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = ke."
                + KanjiElementTable.Cols.ENTRY_ID + " JOIN " + GlossTable.NAME + " AS gloss ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = gloss." + GlossTable.Cols.ENTRY_ID + " ";

        String where = "WHERE gloss." + GlossTable.Cols.ENTRY_ID + " IN ";

        String whereSubQuery = "(SELECT " + GlossTable.Cols.ENTRY_ID + " FROM "
                + GlossTable.NAME + " WHERE VALUE LIKE ?) ";

        String groupBy = "GROUP BY re." + ReadingElementTable.Cols.ENTRY_ID;

        return select + from + join + where + whereSubQuery + groupBy;
    }

    public List<DictionarySearchResultItem> searchDictionary(String searchTerm, int searchType) {
        String query;
        switch (searchType) {
            case KANA_TYPE:
                query = getSearchByKanaQuery();
                break;
            case ROMAJI_TYPE:
                // If the search type is romaji, we need to convert the search string to kana form
                // so we can search
                WanaKanaJava wanaKanaJava = new WanaKanaJava(false);
                searchTerm = wanaKanaJava.toKana(searchTerm);
                query = getSearchByKanaQuery();
                break;
            case KANJI_TYPE:
                query = getSearchByKanjiQuery();
                break;
            case ENGLISH_TYPE:
                query = getSearchByEnglishQuery();
                break;
            default:
                return Collections.emptyList();
        }

        // TODO: Support various getSearchResults methods: Non-wildcard, single-wildcard, exact-match
        // Have to add wildcards here, or query will fail
        String[] arguments = new String[]{"%" + searchTerm + "%"};

        // Create a new List of Search Results to store the results of our query
        List<DictionarySearchResultItem> searchResults = new ArrayList<>();

        Cursor cursor = null;

        try {
            db = getReadableDatabase();
            cursor = db.rawQuery(query, arguments);

            while (cursor.moveToNext()) {
                DictionarySearchResultItem result = new DictionarySearchResultItem();
                int entryId = cursor.getInt(cursor.getColumnIndexOrThrow(ReadingElementTable.Cols.ENTRY_ID));
                String kanjiValue = cursor.getString(cursor.getColumnIndexOrThrow("kanji_value"));
                String readingValue = cursor.getString(cursor.getColumnIndexOrThrow("read_value"));
                String glossValue = cursor.getString(cursor.getColumnIndexOrThrow("gloss_value"));

                result.setEntryId(entryId);

                List<String> formattedKanjiElements = formatString(kanjiValue);
                if (!formattedKanjiElements.isEmpty()) {
                    // If the list is empty, trying to call .get will give a IndexOutOfBounds
                    result.setKanjiElementValue(formatString(kanjiValue).get(0));
                } else {
                    result.setKanjiElementValue("");
                }

                List<String> formattedReadingElements = formatString(readingValue);
                if (!formattedReadingElements.isEmpty()) {
                    // If the list is empty, trying to call .get will give a IndexOutOfBounds
                    result.setReadingElementValue(formatString(readingValue).get(0));
                } else {
                    result.setReadingElementValue("");
                }

                result.setGlossValue(formatString(glossValue));

                searchResults.add(result);
            }
            return searchResults;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public DictionaryItem getEntry(int id) {

        DictionaryItem entry = new DictionaryItem();

        try {
            db = getReadableDatabase();

            entry.setEntryId(id);
            entry.setKanjiElements(getKanjiElements(id));
            entry.setReadingElements(getReadingElements(id));
            entry.setSenseElements(getSenseElements(id));
            entry.setPriorities(getPriorities(id));

            return entry;
        }
        // TODO: Handle errors more gracefully
        catch (SQLException error) {
            Log.e(LOG_TAG, error.getMessage());
            return new DictionaryItem();
        } finally {
            db.close();
        }
    }

    // Get all Kanji Elements associated with a given ID
    private List<KanjiElement> getKanjiElements(int id) {
        // Create a new List of Kanji Elements to store the results of our query
        List<KanjiElement> kanjiElements = new ArrayList<>();

        // The columns from the database I will use after the query
        String[] projection = {
                KanjiElementTable.Cols._ID,
                KanjiElementTable.Cols.VALUE
        };

        // Filter results by WHERE ENTRY_ID = id
        String selection = KanjiElementTable.Cols.ENTRY_ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};
        Cursor cursor = null;

        try {
            // Execute the query
            cursor = db.query(
                    KanjiElementTable.NAME, // Table to query
                    projection, // The columns to return
                    selection, // The columns for the WHERE clause
                    selectionArgs, // The values for the WHERE clause
                    null, // Group by
                    null, // Filter by
                    null  // Sort order
            );

            // Iterate over the rows returned and assign to the POJO
            while (cursor.moveToNext()) {
                KanjiElement kanjiElement = new KanjiElement();
                int elementId = cursor.getInt(cursor.getColumnIndexOrThrow(KanjiElementTable.Cols._ID));
                String value = cursor.getString(cursor.getColumnIndexOrThrow(KanjiElementTable.Cols.VALUE));

                kanjiElement.setKanjiElementId(elementId);
                kanjiElement.setValue(value);

                kanjiElements.add(kanjiElement);
            }
            return kanjiElements;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Get all Reading Elements associated with a given ID
    private List<ReadingElement> getReadingElements(int id) {
        // Create a new List of Reading Elements to store the results of our query
        List<ReadingElement> readingElements = new ArrayList<>();

        String[] arguments = new String[]{Integer.toString(id)};

        String select = "SELECT r." + ReadingElementTable.Cols._ID + ", r."
                + ReadingElementTable.Cols.VALUE + " AS read_value" + ", r."
                + ReadingElementTable.Cols.IS_TRUE_READING + ", group_concat(rel."
                + ReadingElementTable.Cols.VALUE + ", '§') AS rel_value ";

        String from = "FROM " + ReadingElementTable.NAME + " AS r ";

        String join = "LEFT JOIN " + ReadingRelationTable.NAME + " AS rel ON rel."
                + ReadingRelationTable.Cols.READING_ELEMENT_ID + " = r."
                + ReadingElementTable.Cols._ID + " ";

        String where = "WHERE r." + ReadingElementTable.Cols.ENTRY_ID + " = ? ";

        String groupBy = "GROUP BY r." + ReadingElementTable.Cols._ID;

        StringBuilder builder = new StringBuilder();
        builder.append(select).append(from).append(join).append(where).append(groupBy);

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(builder.toString(), arguments);

            while (cursor.moveToNext()) {
                ReadingElement readingElement = new ReadingElement();
                int elementId = cursor.getInt(cursor.getColumnIndexOrThrow(ReadingElementTable.Cols._ID));
                String value = cursor.getString(cursor.getColumnIndexOrThrow("read_value"));
                int isTrueReading = cursor.getInt(cursor.getColumnIndexOrThrow(ReadingElementTable.Cols.IS_TRUE_READING));
                String relationValue = cursor.getString(cursor.getColumnIndexOrThrow("rel_value")); // SAME COLUMN NAME!

                readingElement.setReadingElementId(elementId);
                readingElement.setValue(value);
                readingElement.setTrueReading(!(isTrueReading == 1)); // Inverse the result
                readingElement.setReadingRelation(formatString(relationValue));

                readingElements.add(readingElement);
            }
            return readingElements;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Get all Sense Elements associated with a given ID
    private List<SenseElement> getSenseElements(int id) {
        // Create a new List of Sense Elements to store the results of our query
        List<SenseElement> senseElements = new ArrayList<>();

        String[] arguments = new String[]{Integer.toString(id)};

        String select = "SELECT se." + SenseElementTable.Cols._ID + ", group_concat(pos."
                + SensePosTable.Cols.VALUE + ", '§') AS pos_value, group_concat(foa."
                + SenseFieldTable.Cols.VALUE + ", '§') AS foa_value, group_concat(dial."
                + SenseDialectTable.Cols.VALUE + ", '§') AS dial_value, group_concat(gloss."
                + GlossTable.Cols.VALUE + ", '§') AS gloss_value ";

        String from = "FROM " + SenseElementTable.NAME + " AS se ";

        String join = "LEFT JOIN " + SensePosTable.NAME + " AS pos ON pos."
                + SensePosTable.Cols.SENSE_ID + " = se." + SenseElementTable.Cols._ID
                + " LEFT JOIN " + SenseFieldTable.NAME + " AS foa ON foa."
                + SenseFieldTable.Cols.SENSE_ID + " = se." + SenseElementTable.Cols._ID
                + " LEFT JOIN " + SenseDialectTable.NAME + " AS dial ON dial."
                + SenseDialectTable.Cols.SENSE_ID + " = se." + SenseElementTable.Cols._ID
                + " LEFT JOIN " + GlossTable.NAME + " AS gloss ON gloss."
                + GlossTable.Cols.SENSE_ID + " = se." + SenseElementTable.Cols._ID + " ";

        String where = "WHERE se." + SenseElementTable.Cols.ENTRY_ID + " = ? ";

        String groupBy = "GROUP BY se." + SenseElementTable.Cols._ID;

        StringBuilder builder = new StringBuilder();
        builder.append(select).append(from).append(join).append(where).append(groupBy);

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(builder.toString(), arguments);

            while (cursor.moveToNext()) {
                SenseElement senseElement = new SenseElement();
                int elementId = cursor.getInt(cursor.getColumnIndexOrThrow(SenseElementTable.Cols._ID));
                String posValue = cursor.getString(cursor.getColumnIndexOrThrow("pos_value"));
                String foaValue = cursor.getString(cursor.getColumnIndexOrThrow("foa_value"));
                String dialValue = cursor.getString(cursor.getColumnIndexOrThrow("dial_value"));
                String glossValue = cursor.getString(cursor.getColumnIndexOrThrow("gloss_value"));

                senseElement.setSenseElementId(elementId);
                senseElement.setPartOfSpeech(formatString(posValue));
                senseElement.setFieldOfApplication(formatString(foaValue));
                senseElement.setDialect(formatString(dialValue));
                senseElement.setGlosses(formatString(glossValue));

                senseElements.add(senseElement);
            }
            return senseElements;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Get all Priorities associated with a given ID
    private List<Priority> getPriorities(int id) {
        // Create a new List of Priorities to store the results of our query
        List<Priority> priorities = new ArrayList<>();

        // The columns from the database I will use after the query
        String[] projection = {
                PriorityTable.Cols._ID,
                PriorityTable.Cols.VALUE,
                PriorityTable.Cols.TYPE
        };

        // Filter results by WHERE ENTRY_ID = id
        String selection = PriorityTable.Cols.ENTRY_ID + " = ?";
        String[] selectionArgs = {Integer.toString(id)};

        Cursor cursor = null;

        try {
            // Execute the query
            cursor = db.query(
                    PriorityTable.NAME, // Table to query
                    projection, // The columns to return
                    selection, // The columns for the WHERE clause
                    selectionArgs, // The values for the WHERE clause
                    null, // Group by
                    null, // Filter by
                    null  // Sort order
            );

            // Iterate over the rows returned and assign to the POJO
            while (cursor.moveToNext()) {
                Priority priority = new Priority();
                int elementId = cursor.getInt(cursor.getColumnIndexOrThrow(PriorityTable.Cols._ID));
                String value = cursor.getString(cursor.getColumnIndexOrThrow(PriorityTable.Cols.VALUE));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(PriorityTable.Cols.TYPE));

                // If the type is Kanji Reading, set the bool to true, otherwise false.
                boolean isKanjiReadingPriority = type.equals("Kanji_Element");

                priority.setPriorityId(elementId);
                priority.setValue(value);
                priority.setKanjiReadingPriority(isKanjiReadingPriority);

                // Add the priority to the list
                priorities.add(priority);
            }
            return priorities;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // Split the string on the separator character into a list, then
    // convert to a LinkedHashSet to remove duplicate values.
    @Nullable
    private static List<String> formatString(String stringToFormat) {
        if (stringToFormat != null && !stringToFormat.isEmpty()) {
            return new ArrayList<>(new LinkedHashSet<>(Arrays.asList(stringToFormat.split("§"))));
        } else {
            // We never want a null value in our DictionaryItem, so just return an empty list
            return Collections.emptyList();
        }
    }
}
