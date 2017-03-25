package com.ikue.japanesedictionary;

import com.ikue.japanesedictionary.utils.GlobalConstants;
import com.ikue.japanesedictionary.utils.SearchUtils;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class SearchUtilsUnitTest {
    @Test
    public void testGetSearchType_english() {
        assertEquals(SearchUtils.getSearchType("blast"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("shot"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("parents"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("wasp"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("plastic"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("book"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("library"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("computer"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("speaker"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("lamp"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("deodorant"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("emulator"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("monitor"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("mug"), GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertEquals(SearchUtils.getSearchType("calender"), GlobalConstants.SearchTypes.ENGLISH_TYPE);

        // The following entries will be converted to Romaji
        assertFalse(SearchUtils.getSearchType("home") == GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertFalse(SearchUtils.getSearchType("date") == GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertFalse(SearchUtils.getSearchType("banana") == GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertFalse(SearchUtils.getSearchType("woman") == GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertFalse(SearchUtils.getSearchType("man") == GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertFalse(SearchUtils.getSearchType("penguin") == GlobalConstants.SearchTypes.ENGLISH_TYPE);
        assertFalse(SearchUtils.getSearchType("tea") == GlobalConstants.SearchTypes.ENGLISH_TYPE);
    }

    @Test
    public void testGetSearchType_romaji() {
        assertEquals(SearchUtils.getSearchType("tabemono"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("honmono"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("date"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("home"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("toshokan"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("iku"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("hontou"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("ru-ku"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("ikue"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("enryo"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("watashi"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
        assertEquals(SearchUtils.getSearchType("kyanseru"), GlobalConstants.SearchTypes.ROMAJI_TYPE);
    }

    @Test
    public void testGetSearchType_kana() {
        assertEquals(SearchUtils.getSearchType("たべもの"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("ほんもの"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("だて"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("ほめ"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("としょかん"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("いく"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("ほんとう"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("るーく"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("いくえ"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("えんりょ"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("わたし"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("きゃんせる"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("ケーキ"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("ダンス"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("ポケモン"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("バナナ"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("モテる"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("サボる"), GlobalConstants.SearchTypes.KANA_TYPE);
        assertEquals(SearchUtils.getSearchType("モルモット"), GlobalConstants.SearchTypes.KANA_TYPE);

        assertFalse(SearchUtils.getSearchType("食べ物") == GlobalConstants.SearchTypes.KANA_TYPE);
        assertFalse(SearchUtils.getSearchType("本物") == GlobalConstants.SearchTypes.KANA_TYPE);
        assertFalse(SearchUtils.getSearchType("行く") == GlobalConstants.SearchTypes.KANA_TYPE);
        assertFalse(SearchUtils.getSearchType("あああああああ行") == GlobalConstants.SearchTypes.KANA_TYPE);
        assertFalse(SearchUtils.getSearchType("ああああ食べるあああ") == GlobalConstants.SearchTypes.KANA_TYPE);
    }

    @Test
    public void testGetSearchType_kanji() {
        assertEquals(SearchUtils.getSearchType("食べ物"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("本当"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("伊達"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("褒め"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("図書館"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("行く"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("本物"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("幾重"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("育英"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("遠慮"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("私"), GlobalConstants.SearchTypes.KANJI_TYPE);
        assertEquals(SearchUtils.getSearchType("来る"), GlobalConstants.SearchTypes.KANJI_TYPE);
    }

    @Test
    public void testIsStringAllUppercase_lowercase() {
        assertFalse(SearchUtils.isStringAllUppercase("lowercase"));
        assertFalse(SearchUtils.isStringAllUppercase("this is a test string"));
        assertFalse(SearchUtils.isStringAllUppercase("tabemono"));
    }

    @Test
    public void testIsStringAllUppercase_mixedcase() {
        assertFalse(SearchUtils.isStringAllUppercase("MiXeDCaSe"));
        assertFalse(SearchUtils.isStringAllUppercase("this is A TEST string"));
        assertFalse(SearchUtils.isStringAllUppercase("tabeMONO"));
        assertFalse(SearchUtils.isStringAllUppercase("MOTEru"));
    }

    @Test
    public void testIsStringAllUppercase_uppercase() {
        assertTrue(SearchUtils.isStringAllUppercase("UPPERCASE"));
        assertTrue(SearchUtils.isStringAllUppercase("THIS IS A TEST STRING"));
        assertTrue(SearchUtils.isStringAllUppercase("TABEMONO"));
        assertTrue(SearchUtils.isStringAllUppercase("MOTERU"));
        assertTrue(SearchUtils.isStringAllUppercase("LOUD NOISES"));
    }

    @Test
    public void testContainsWildcards_withWildcards() {
        Assert.assertTrue(SearchUtils.containsWildcards("*test"));
        Assert.assertTrue(SearchUtils.containsWildcards("*test*"));
        Assert.assertTrue(SearchUtils.containsWildcards("t*est"));
        Assert.assertTrue(SearchUtils.containsWildcards("test*"));
        Assert.assertTrue(SearchUtils.containsWildcards("test?"));
        Assert.assertTrue(SearchUtils.containsWildcards("?test?"));
        Assert.assertTrue(SearchUtils.containsWildcards("?test"));
        Assert.assertTrue(SearchUtils.containsWildcards("te?st"));
        Assert.assertTrue(SearchUtils.containsWildcards("*te?st*"));
        Assert.assertTrue(SearchUtils.containsWildcards("*te?st"));
    }

    @Test
    public void testContainsWildcards_withoutWildcards() {
        Assert.assertFalse(SearchUtils.containsWildcards("test"));
        Assert.assertFalse(SearchUtils.containsWildcards("testing for wildcards"));
        Assert.assertFalse(SearchUtils.containsWildcards("library"));
        Assert.assertFalse(SearchUtils.containsWildcards("commas, are, cool"));
        Assert.assertFalse(SearchUtils.containsWildcards("%"));
        Assert.assertFalse(SearchUtils.containsWildcards("_"));
        Assert.assertFalse(SearchUtils.containsWildcards("home"));
        Assert.assertFalse(SearchUtils.containsWildcards("manchester!"));
    }

    @Test
    public void testRemoveWildcards() {
        Assert.assertEquals(SearchUtils.removeWildcards("*test"), "test");
        Assert.assertEquals(SearchUtils.removeWildcards("*test*"), "test");
        Assert.assertEquals(SearchUtils.removeWildcards("t*est"), "test");
        Assert.assertEquals(SearchUtils.removeWildcards("test*"), "test");
        Assert.assertEquals(SearchUtils.removeWildcards("test?"), "test");
        Assert.assertEquals(SearchUtils.removeWildcards("?test?"), "test");
        Assert.assertEquals(SearchUtils.removeWildcards("?test"), "test");
        Assert.assertEquals(SearchUtils.removeWildcards("te?st"), "test");
        Assert.assertEquals(SearchUtils.removeWildcards("*te?st*"), "test");
        Assert.assertEquals(SearchUtils.removeWildcards("*te?st"), "test");
    }

    @Test
    public void testGetTrueWildcardString() {
        Assert.assertEquals(SearchUtils.getTrueWildcardString("*test"), "%test");
        Assert.assertEquals(SearchUtils.getTrueWildcardString("*test*"), "%test%");
        Assert.assertEquals(SearchUtils.getTrueWildcardString("t*est"), "t%est");
        Assert.assertEquals(SearchUtils.getTrueWildcardString("test*"), "test%");
        Assert.assertEquals(SearchUtils.getTrueWildcardString("test?"), "test_");
        Assert.assertEquals(SearchUtils.getTrueWildcardString("?test?"), "_test_");
        Assert.assertEquals(SearchUtils.getTrueWildcardString("?test"), "_test");
        Assert.assertEquals(SearchUtils.getTrueWildcardString("te?st"), "te_st");
        Assert.assertEquals(SearchUtils.getTrueWildcardString("*te?st*"), "%te_st%");
        Assert.assertEquals(SearchUtils.getTrueWildcardString("*te?st"), "%te_st");
    }
}
