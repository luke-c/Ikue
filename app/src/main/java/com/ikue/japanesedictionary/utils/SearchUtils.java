package com.ikue.japanesedictionary.utils;

import static com.ikue.japanesedictionary.utils.Constants.SearchTypes.*;

/**
 * Created by luke_c on 15/02/2017.
 */

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
            String romajiForm = wk.toHiragana(searchTerm);

            for (char c : romajiForm.toCharArray()) {
                // If a character couldn't be converted to Hiragana, then we can assume the user
                // meant to search in English (or mistyped when using Romaji)
                if (Character.UnicodeBlock.of(c) != Character.UnicodeBlock.HIRAGANA) {
                    return ENGLISH_TYPE;
                }
            }
            // If every character successfully converted to Romaji, then we assume the user
            // meant to search in Romaji. (Naive!)
            // TODO: Additional checks before assuming Romaji
            return ROMAJI_TYPE;
        }
    }
}
