package com.ikue.japanesedictionary.utils;


import androidx.annotation.NonNull;

import com.ikue.japanesedictionary.database.DictionaryDbSchema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class DbUtils {
    // Split the string on the separator character into a list, then
    // convert to a LinkedHashSet to remove duplicate values.
    @NonNull
    public static List<String> formatString(String stringToFormat) {
        if (stringToFormat != null && !stringToFormat.isEmpty()) {
            return new ArrayList<>(new LinkedHashSet<>(Arrays.asList(stringToFormat.split("§"))));
        } else {
            // We never want a null value in our DictionaryEntry, so just return an empty list
            return Collections.emptyList();
        }
    }

    public static String getSearchByKanaQuery(boolean hasWildcard) {
        String searchType = hasWildcard ? "LIKE" : "=";

        String select = "SELECT re." + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + ", group_concat(ke."
                + DictionaryDbSchema.Jmdict.KanjiElementTable.Cols.VALUE + ", '§') AS kanji_value, group_concat(re."
                + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.VALUE + ", '§') AS read_value, group_concat(gloss."
                + DictionaryDbSchema.Jmdict.GlossTable.Cols.VALUE + ", '§') AS gloss_value ";

        String from = "FROM " + DictionaryDbSchema.Jmdict.ReadingElementTable.NAME + " AS re ";

        String join = "JOIN " + DictionaryDbSchema.Jmdict.GlossTable.NAME + " AS gloss ON re."
                + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + " = gloss." + DictionaryDbSchema.Jmdict.GlossTable.Cols.ENTRY_ID
                + " LEFT JOIN " + DictionaryDbSchema.Jmdict.KanjiElementTable.NAME + " AS ke ON re."
                + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + " = ke." + DictionaryDbSchema.Jmdict.KanjiElementTable.Cols.ENTRY_ID
                + " ";

        String where = "WHERE re." + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + " IN ";

        String whereSubQuery = "(SELECT " + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + " FROM "
                + DictionaryDbSchema.Jmdict.ReadingElementTable.NAME + " WHERE VALUE " + searchType + " ?) ";

        String groupBy = "GROUP BY re." + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID;

        return select + from + join + where + whereSubQuery + groupBy;
    }

    public static String getSearchByKanjiQuery(boolean hasWildcard) {
        String searchType = hasWildcard ? "LIKE" : "=";

        String select = "SELECT re." + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + ", group_concat(ke."
                + DictionaryDbSchema.Jmdict.KanjiElementTable.Cols.VALUE + ", '§') AS kanji_value, group_concat(re."
                + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.VALUE + ", '§') AS read_value, group_concat(gloss."
                + DictionaryDbSchema.Jmdict.GlossTable.Cols.VALUE + ", '§') AS gloss_value ";

        String from = "FROM " + DictionaryDbSchema.Jmdict.ReadingElementTable.NAME + " AS re ";

        String join = "JOIN " + DictionaryDbSchema.Jmdict.GlossTable.NAME + " AS gloss ON re."
                + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + " = gloss." + DictionaryDbSchema.Jmdict.GlossTable.Cols.ENTRY_ID
                + " JOIN " + DictionaryDbSchema.Jmdict.KanjiElementTable.NAME + " AS ke ON re."
                + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + " = ke." + DictionaryDbSchema.Jmdict.KanjiElementTable.Cols.ENTRY_ID
                + " ";

        String where = "WHERE ke." + DictionaryDbSchema.Jmdict.KanjiElementTable.Cols.ENTRY_ID + " IN ";

        String whereSubQuery = "(SELECT " + DictionaryDbSchema.Jmdict.KanjiElementTable.Cols.ENTRY_ID + " FROM "
                + DictionaryDbSchema.Jmdict.KanjiElementTable.NAME + " WHERE VALUE " + searchType + " ?) ";

        String groupBy = "GROUP BY re." + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID;

        return select + from + join + where + whereSubQuery + groupBy;
    }

    public static String getSearchByEnglishQuery(boolean hasWildcard) {
        String searchType = hasWildcard ? "LIKE" : "=";

        String select = "SELECT re." + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + ", group_concat(ke."
                + DictionaryDbSchema.Jmdict.KanjiElementTable.Cols.VALUE + ", '§') AS kanji_value, group_concat(re."
                + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.VALUE + ", '§') AS read_value, group_concat(gloss."
                + DictionaryDbSchema.Jmdict.GlossTable.Cols.VALUE + ", '§') AS gloss_value ";

        String from = "FROM " + DictionaryDbSchema.Jmdict.ReadingElementTable.NAME + " AS re ";

        String join = "LEFT JOIN " + DictionaryDbSchema.Jmdict.KanjiElementTable.NAME + " AS ke ON re."
                + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + " = ke."
                + DictionaryDbSchema.Jmdict.KanjiElementTable.Cols.ENTRY_ID + " JOIN " + DictionaryDbSchema.Jmdict.GlossTable.NAME + " AS gloss ON re."
                + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID + " = gloss." + DictionaryDbSchema.Jmdict.GlossTable.Cols.ENTRY_ID + " ";

        String where = "WHERE gloss." + DictionaryDbSchema.Jmdict.GlossTable.Cols.ENTRY_ID + " IN ";

        String whereSubQuery = "(SELECT " + DictionaryDbSchema.Jmdict.GlossTable.Cols.ENTRY_ID + " FROM "
                + DictionaryDbSchema.Jmdict.GlossTable.NAME + " WHERE VALUE " + searchType + " ?) ";

        String groupBy = "GROUP BY re." + DictionaryDbSchema.Jmdict.ReadingElementTable.Cols.ENTRY_ID;

        return select + from + join + where + whereSubQuery + groupBy;
    }
}
