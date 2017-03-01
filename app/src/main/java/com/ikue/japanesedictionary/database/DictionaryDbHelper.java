package com.ikue.japanesedictionary.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
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
import com.ikue.japanesedictionary.database.DictionaryDbSchema.User.FavouritesTable;
import com.ikue.japanesedictionary.database.DictionaryDbSchema.User.HistoryTable;
import com.ikue.japanesedictionary.models.DictionaryItem;
import com.ikue.japanesedictionary.models.DictionarySearchResultItem;
import com.ikue.japanesedictionary.models.KanjiElement;
import com.ikue.japanesedictionary.models.Priority;
import com.ikue.japanesedictionary.models.ReadingElement;
import com.ikue.japanesedictionary.models.SenseElement;
import com.ikue.japanesedictionary.utils.SearchUtils;
import com.ikue.japanesedictionary.utils.WanaKanaJava;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ikue.japanesedictionary.utils.Constants.SearchTypes.ENGLISH_TYPE;
import static com.ikue.japanesedictionary.utils.Constants.SearchTypes.KANA_TYPE;
import static com.ikue.japanesedictionary.utils.Constants.SearchTypes.KANJI_TYPE;
import static com.ikue.japanesedictionary.utils.Constants.SearchTypes.ROMAJI_TYPE;
import static com.ikue.japanesedictionary.utils.DbUtils.formatString;
import static com.ikue.japanesedictionary.utils.DbUtils.getSearchByEnglishQuery;
import static com.ikue.japanesedictionary.utils.DbUtils.getSearchByKanaQuery;
import static com.ikue.japanesedictionary.utils.DbUtils.getSearchByKanjiQuery;

/**
 * Created by luke_c on 01/02/2017.
 */

// TODO: Add catch blocks for queries
public class DictionaryDbHelper extends SQLiteAssetHelper {

    private static DictionaryDbHelper instance;
    private static SQLiteDatabase db;
    private static SharedPreferences sharedPref;

    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 4;

    private final String LOG_TAG = this.getClass().getName();

    public static synchronized DictionaryDbHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (instance == null) {
            instance = new DictionaryDbHelper(context.getApplicationContext());
        }

        if (sharedPref == null) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return instance;
    }

    private DictionaryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // When the version number increases, always force a complete upgrade.
        // TODO: This will lose all data so make sure to export user data beforehand and import after
        setForcedUpgrade();
    }

    // Search the dictionary for a given term
    public List<DictionarySearchResultItem> searchDictionary(String searchTerm, int searchType) {
        String query;

        // Stores whether we will do a wildcard search or not
        boolean isWildcardSearch;

        // Get the user's chosen default search type
        int defaultSearchType = Integer.parseInt(sharedPref.getString("pref_defaultSearchType", "0"));

        // If the default search is for an exact match:
        if(defaultSearchType == 0) {
            // Check for any wildcards in the search, ? or * operators
            isWildcardSearch = SearchUtils.containsWildcards(searchTerm);
        } else {
            // All other searches will be using a wildcard
            isWildcardSearch = true;
        }

        switch (searchType) {
            case KANA_TYPE:
                query = getSearchByKanaQuery(isWildcardSearch);
                break;
            case ROMAJI_TYPE:
                // TODO: Case insensitive Kana search. Show Hiragana and Katakana results
                WanaKanaJava wanaKanaJava = new WanaKanaJava(false);

                // If the search type is romaji, we need to convert the search string to kana form
                // so we can search. If the search term is all uppercase then search in Katakana
                if(SearchUtils.isStringAllUppercase(SearchUtils.removeWildcards(searchTerm))) {
                    searchTerm = wanaKanaJava.toKatakana(searchTerm);
                } else {
                    if(!sharedPref.getBoolean("pref_caseSensitiveRomajiSearch", true)) {
                        searchTerm = wanaKanaJava.toHiragana(searchTerm);
                    } else {
                        searchTerm = wanaKanaJava.toKana(searchTerm);
                    }
                }
                query = getSearchByKanaQuery(isWildcardSearch);
                break;
            case KANJI_TYPE:
                query = getSearchByKanjiQuery(isWildcardSearch);
                break;
            case ENGLISH_TYPE:
                query = getSearchByEnglishQuery(isWildcardSearch);
                break;
            default:
                return Collections.emptyList();
        }

        // Get the user's chosen max results. Default '0' means don't limit.
        int maxResults = Integer.parseInt(sharedPref.getString("pref_limitSearchResults", "0"));
        if(maxResults != 0) {
            // If there is a max result value we add LIMIT to our SQL query
            query += " LIMIT " + maxResults;
        }

        String argument;
        boolean hasWildcards = SearchUtils.containsWildcards(searchTerm);

        // If the user has not typed any wildcards manually, assume they want to use their default
        // chosen search preference
        if(!hasWildcards) {
            switch(defaultSearchType) {
                // Exact match
                case 0:
                    argument = searchTerm;
                    break;
                // *token
                case 1:
                    // Prefix the string with % wildcard
                    argument = "%" + SearchUtils.getTrueWildcardString(searchTerm);
                    break;
                // token*
                case 2:
                    // Add the % wildcard to the end of the string
                    argument = SearchUtils.getTrueWildcardString(searchTerm) + "%";
                    break;
                // *token*
                case 3:
                    argument = "%" + SearchUtils.getTrueWildcardString(searchTerm) + "%";
                    break;
                // Assume exact match for default
                default:
                    argument = searchTerm;
                    break;
            }
        } else {
            // If there is already wildcards in the query, ignore the user's default search type
            // and just use the entered wildcards
            argument = SearchUtils.getTrueWildcardString(searchTerm);
        }

        // Make sure to convert any ? and * to _ and % respectively before searching
        String[] arguments = new String[]{argument};

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

    // Get a list of all the entries viewed by the user
    public List<DictionarySearchResultItem> getHistory() {
        List<DictionarySearchResultItem> history = new ArrayList<>();

        String select = "SELECT re." + ReadingElementTable.Cols.ENTRY_ID + ", group_concat(ke."
                + KanjiElementTable.Cols.VALUE + ", '§') AS kanji_value, group_concat(re."
                + ReadingElementTable.Cols.VALUE + ", '§') AS read_value, group_concat(gloss."
                + GlossTable.Cols.VALUE + ", '§') AS gloss_value ";

        String from = "FROM " + ReadingElementTable.NAME + " AS re ";

        String join = "JOIN " + GlossTable.NAME + " AS gloss ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = gloss." + GlossTable.Cols.ENTRY_ID
                + " LEFT JOIN " + KanjiElementTable.NAME + " AS ke ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = ke." + KanjiElementTable.Cols.ENTRY_ID
                + " JOIN " + HistoryTable.NAME + " AS hist ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = hist." + HistoryTable.Cols.ENTRY_ID + " ";

        String where = "WHERE re." + ReadingElementTable.Cols.ENTRY_ID + " IN ";

        String whereSubQuery = "(SELECT " + HistoryTable.Cols.ENTRY_ID + " FROM "
                + HistoryTable.NAME + ") ";

        String groupBy = "GROUP BY re." + ReadingElementTable.Cols.ENTRY_ID + " ";

        String orderBy = "ORDER BY DATETIME(" + HistoryTable.Cols.SQLTIME + ") DESC";

        StringBuilder builder = new StringBuilder();
        builder.append(select).append(from).append(join).append(where).append(whereSubQuery)
                .append(groupBy).append(orderBy);

        // Get the user's chosen max results. Default '0' means don't limit.
        int maxResults = Integer.parseInt(sharedPref.getString("pref_limitHistoryResults", "0"));
        if(maxResults != 0) {
            // If there is a max result value we add LIMIT to our SQL query
            String limitBy = " LIMIT " + maxResults;
            builder.append(limitBy);
        }

        Cursor cursor = null;

        try {
            db = getReadableDatabase();
            cursor = db.rawQuery(builder.toString(), new String[]{});

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

                history.add(result);
            }
            return history;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    // Add an entry to the user's history
    public void addToHistory(int id) throws SQLException {
        db = getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(HistoryTable.Cols.ENTRY_ID, id);

            // If the entry already exists in our history table, then we want to replace it
            // with the current timestamp so it appears as the latest entry viewed.
            db.insertWithOnConflict(HistoryTable.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            // Log the exception, then throw it further up the stack to catch in the UI
            Log.d(LOG_TAG, "Error while trying to add to history with ID: " + id);
            throw e;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Remove an entry from the user's history
    public void removeFromHistory(int id) throws SQLException {
        db = getWritableDatabase();
        db.beginTransaction();

        try {
            String whereClause = HistoryTable.Cols.ENTRY_ID + " = ?";
            String[] whereArgs = {Integer.toString(id)};
            db.delete(HistoryTable.NAME, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            // Log the exception, then throw it further up the stack to catch in the UI
            Log.e(LOG_TAG, "Error while trying to delete from history with ID: " + id);
            throw e;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Get a list of all the favourites the user has
    public List<DictionarySearchResultItem> getAllFavourites() {
        List<DictionarySearchResultItem> favourites = new ArrayList<>();

        String select = "SELECT re." + ReadingElementTable.Cols.ENTRY_ID + ", group_concat(ke."
                + KanjiElementTable.Cols.VALUE + ", '§') AS kanji_value, group_concat(re."
                + ReadingElementTable.Cols.VALUE + ", '§') AS read_value, group_concat(gloss."
                + GlossTable.Cols.VALUE + ", '§') AS gloss_value ";

        String from = "FROM " + ReadingElementTable.NAME + " AS re ";

        String join = "JOIN " + GlossTable.NAME + " AS gloss ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = gloss." + GlossTable.Cols.ENTRY_ID
                + " LEFT JOIN " + KanjiElementTable.NAME + " AS ke ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = ke." + KanjiElementTable.Cols.ENTRY_ID
                + " JOIN " + FavouritesTable.NAME + " AS fav ON re."
                + ReadingElementTable.Cols.ENTRY_ID + " = fav." + FavouritesTable.Cols.ENTRY_ID + " ";

        String where = "WHERE re." + ReadingElementTable.Cols.ENTRY_ID + " IN ";

        String whereSubQuery = "(SELECT " + FavouritesTable.Cols.ENTRY_ID + " FROM "
                + FavouritesTable.NAME + ") ";

        String groupBy = "GROUP BY re." + ReadingElementTable.Cols.ENTRY_ID + " ";

        String orderBy = "ORDER BY DATETIME(" + FavouritesTable.Cols.SQLTIME + ") DESC";

        StringBuilder builder = new StringBuilder();
        builder.append(select).append(from).append(join).append(where).append(whereSubQuery)
                .append(groupBy).append(orderBy);

        // Get the user's chosen max results. Default '0' means don't limit.
        int maxResults = Integer.parseInt(sharedPref.getString("pref_limitFavouriteResults", "0"));
        if(maxResults != 0) {
            // If there is a max result value we add LIMIT to our SQL query
            String limitBy = " LIMIT " + maxResults;
            builder.append(limitBy);
        }

        Cursor cursor = null;

        try {
            db = getReadableDatabase();
            cursor = db.rawQuery(builder.toString(), new String[]{});

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

                favourites.add(result);
            }
            return favourites;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    // Add an entry to the user's favourites
    public void addFavourite(int id) throws SQLException {
        db = getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(FavouritesTable.Cols.ENTRY_ID, id);

            db.insertOrThrow(FavouritesTable.NAME, null, values);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            // Log the exception, then throw it further up the stack to catch in the UI
            Log.d(LOG_TAG, "Error while trying to add favourite with ID: " + id);
            throw e;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Remove an entry from the user's favourites
    public void removeFavourite(int id) throws SQLException {
        db = getWritableDatabase();
        db.beginTransaction();

        try {
            String whereClause = FavouritesTable.Cols.ENTRY_ID + " = ?";
            String[] whereArgs = {Integer.toString(id)};
            db.delete(FavouritesTable.NAME, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            // Log the exception, then throw it further up the stack to catch in the UI
            Log.e(LOG_TAG, "Error while trying to delete favourite with ID: " + id);
            throw e;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public DictionaryItem getEntry(int id) {

        DictionaryItem entry = new DictionaryItem();

        try {
            db = getReadableDatabase();

            entry.setEntryId(id);
            entry.setIsFavourite(isFavourite(id));
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

    // Get whether an entry has been favourited or not
    private boolean isFavourite(int id) {
        String[] arguments = new String[]{Integer.toString(id)};

        String query = "SELECT EXISTS(SELECT " + FavouritesTable.Cols.ENTRY_ID + " FROM " +
                FavouritesTable.NAME + " WHERE " + FavouritesTable.Cols.ENTRY_ID +
                " = ?) AS isFavourite";

        Cursor cursor = null;

        // Default value is not favourited
        int isFavourite = 0;

        try {
            // Execute the query
            cursor = db.rawQuery(query, arguments);

            // Iterate over the rows returned and assign to the POJO
            while (cursor.moveToNext()) {
                isFavourite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavourite"));
            }
            // Returns true if the ID has been favourited
            return isFavourite == 1;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
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
}
