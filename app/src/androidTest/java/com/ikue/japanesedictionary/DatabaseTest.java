package com.ikue.japanesedictionary;

import android.content.Context;

import com.ikue.japanesedictionary.database.DictionaryDbHelper;
import com.ikue.japanesedictionary.models.DictionaryEntry;
import com.ikue.japanesedictionary.models.DictionaryListEntry;
import com.ikue.japanesedictionary.utils.GlobalConstants.SearchTypes;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private static DictionaryDbHelper helper;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertTrue(appContext.getPackageName().equals("com.ikue.japanesedictionary")
                || appContext.getPackageName().equals("com.ikue.japanesedictionary.debug"));
    }

    @Before
    public void setup() {
        helper = DictionaryDbHelper.getInstance(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testAddingToHistory() {
        int entryIdToAdd = 1000020;
        helper.addToHistory(entryIdToAdd);

        List<DictionaryListEntry> results = helper.getHistory();

        if (results.get(0) != null) {
            assertEquals(results.get(0).getEntryId(), entryIdToAdd);
        }

        boolean containsEntryToAdd = false;
        for (DictionaryListEntry entry : results) {
            if (entry.getEntryId() == entryIdToAdd) {
                containsEntryToAdd = true;
                break;
            }
        }
        assertTrue(containsEntryToAdd);

        helper.removeFromHistory(entryIdToAdd);
    }

    @Test
    public void testRemovingFromHistory() {
        int entryIdToRemove = 1000010;
        helper.addToHistory(entryIdToRemove);
        helper.removeFromHistory(entryIdToRemove);

        List<DictionaryListEntry> results = helper.getHistory();

        if (results.get(0) != null) {
            assertNotEquals(results.get(0).getEntryId(), entryIdToRemove);
        }

        for (DictionaryListEntry entry : results) {
            assertNotEquals(entry.getEntryId(), entryIdToRemove);
        }
    }

    @Test
    public void testGettingHistory_notEmpty() {
        helper.addToHistory(1000000);

        List<DictionaryListEntry> history = helper.getHistory();
        assertFalse(history.isEmpty());

        helper.removeFromHistory(1000000);
    }

    @Test
    public void testAddingToFavourites() {
        int entryIdToAdd = 1000030;
        helper.addToFavourites(entryIdToAdd);

        List<DictionaryListEntry> results = helper.getFavourites();

        if (results.get(0) != null) {
            assertEquals(results.get(0).getEntryId(), entryIdToAdd);
        }

        boolean containsAddedEntry = false;
        for (DictionaryListEntry entry : results) {
            if (entry.getEntryId() == entryIdToAdd) {
                containsAddedEntry = true;
                break;
            }
        }
        assertTrue(containsAddedEntry);

        helper.removeFromFavourites(entryIdToAdd);
    }

    @Test
    public void testRemovingFromFavourites() {
        int entryIdToRemove = 1000040;
        helper.addToFavourites(entryIdToRemove);
        helper.removeFromFavourites(entryIdToRemove);

        List<DictionaryListEntry> results = helper.getFavourites();

        for (DictionaryListEntry entry : results) {
            assertNotEquals(entry.getEntryId(), entryIdToRemove);
        }
    }

    @Test
    public void testGettingFavourites_notEmpty() {
        helper.addToFavourites(1000050);

        List<DictionaryListEntry> results = helper.getFavourites();
        assertFalse(results.isEmpty());

        helper.removeFromFavourites(1000050);
    }

    @Test
    public void testGettingEntryFromId() {
        int entryId = 1000060;
        DictionaryEntry entry = helper.getEntry(entryId);

        assertNotNull(entry);
        assertEquals(entry.getEntryId(), entryId);
    }

    @Test
    public void testGettingRandomEntry() {
        DictionaryEntry entry = helper.getRandomEntry();

        assertNotNull(entry);

        // 0 is returned when a random entry couldn't be retrieved
        // as well as the default int value
        assertNotEquals(0, entry.getEntryId());

        assertFalse(entry.getReadingElements().isEmpty());
        assertFalse(entry.getSenseElements().isEmpty());
    }

    @Test
    public void testSearchingDictionary_english() {
        List<DictionaryListEntry> entries = helper.searchDictionary("house", SearchTypes.ENGLISH_TYPE);

        assertFalse(entries.isEmpty());

        // Every results should contain the string "pot"
        for (DictionaryListEntry entry : entries) {
            assertTrue(entry.getGlossValue().contains("house"));
        }
    }

    @Test
    public void testSearchingDictionary_romaji() {
        List<DictionaryListEntry> entries = helper.searchDictionary("tabemono", SearchTypes.ROMAJI_TYPE);

        assertFalse(entries.isEmpty());

        // Every results should contain the string "たべもの". This is "tabemono" converted to kana
        for (DictionaryListEntry entry : entries) {
            assertTrue(entry.getReadingElementValue().contains("たべもの"));
        }
    }

    @Test
    public void testSearchingDictionary_kana() {
        List<DictionaryListEntry> entries = helper.searchDictionary("としょかん", SearchTypes.KANA_TYPE);

        assertFalse(entries.isEmpty());

        // Every results should contain the string "としょかん".
        for (DictionaryListEntry entry : entries) {
            assertTrue(entry.getReadingElementValue().contains("としょかん"));
        }
    }

    @Test
    public void testSearchingDictionary_kanji() {
        List<DictionaryListEntry> entries = helper.searchDictionary("学生", SearchTypes.KANJI_TYPE);

        assertFalse(entries.isEmpty());

        // Every results should contain the string "学生".
        for (DictionaryListEntry entry : entries) {
            assertTrue(entry.getKanjiElementValue().contains("学生"));
        }
    }

    @After
    public void cleanup() {
        helper.close();
    }
}
