package com.ikue.japanesedictionary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ikue.japanesedictionary.utils.GlobalConstants.SearchTypes.*;

public class SearchUtils {

    // Get what type the search term is. Can either be Kanji, Kana, Romaji, or English.
    public static int getSearchType(String searchTerm) {
        boolean containsKana = false;

        // Check every character of the string
        for (char c : searchTerm.toCharArray()) {
            // If the current character is a Kanji (or Chinese/Korean character)
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                // Once we find a single Kanji character, we know to search the Kanji Element
                return KANJI_TYPE;
                // If the current character is a Hiragana or Katakana character
            } else if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HIRAGANA
                    || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.KATAKANA) {
                // We can't immediately return a KANA_TYPE yet because there could be Kanji
                // characters further in the string
                containsKana = true;
            }
        }
        // If we have parsed the whole string, have not encountered a Kanji character, and there
        // is at least one Kana character in the string then we know to search the Reading Element
        if (containsKana) {
            return KANA_TYPE;
        } else {
            // False because we don't care about obsolete Kana
            WanaKanaJava wk = new WanaKanaJava(false);
            String kanaForm = wk.toKana(searchTerm);

            for (char c : kanaForm.toCharArray()) {
                // If a character couldn't be converted to Hiragana or Katakana, then we can assume
                // the user meant to search in English (or mistyped when using Romaji)
                if (Character.UnicodeBlock.of(c) != Character.UnicodeBlock.HIRAGANA
                        && Character.UnicodeBlock.of(c) != Character.UnicodeBlock.KATAKANA) {
                    return ENGLISH_TYPE;
                }
            }
            // If every character successfully converted to Romaji, then we assume the user
            // meant to search in Romaji. (Naive!)
            // TODO: Additional checks before assuming Romaji
            return ROMAJI_TYPE;
        }
    }

    // Check if a given string is all uppercase
    public static boolean isStringAllUppercase(String string) {
        for (char c : string.toCharArray()) {
            if(!Character.isUpperCase(c)) {
                return false;
            }
        }
        return true;
    }

    // Check a given string for any pseudo wildcard characters
    public static boolean containsWildcards(String string) {
        Pattern pattern = Pattern.compile("\\*+|\\?+");
        Matcher matcher = pattern.matcher(string);
        // Check if any wildcards are being used
        return matcher.find();
    }

    // Remove any pseudo wildcard characters from a given string
    public static String removeWildcards(String string) {
        return string.replaceAll("\\*+|\\?+", "");
    }

    // Replace the pseudo wildcard characters we use with the real ones to query
    // with SQLite
    public static String getTrueWildcardString(String string) {
        String firstRound = string.replaceAll("\\*", "%");
        return firstRound.replaceAll("\\?", "_");
    }
}
