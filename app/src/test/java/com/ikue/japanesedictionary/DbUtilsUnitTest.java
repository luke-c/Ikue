package com.ikue.japanesedictionary;

import com.ikue.japanesedictionary.utils.DbUtils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


public class DbUtilsUnitTest {
    @Test
    public void testFormatString_nullParameter() {
        assertTrue(DbUtils.formatString(null).isEmpty());
        assertTrue(DbUtils.formatString("").isEmpty());
    }

    @Test
    public void testFormatString_withSeparator() {
        List<String> strings = DbUtils.formatString("this§is§a§test");
        assertEquals(4, strings.size());

        assertTrue(strings.contains("this"));
        assertTrue(strings.contains("is"));
        assertTrue(strings.contains("a"));
        assertTrue(strings.contains("test"));

        String[] expected = new String[] {"this", "is", "a", "test"};
        Assert.assertTrue(strings.containsAll(Arrays.asList(expected)));
    }

    @Test
    public void testGetSearchByKanaQuery_withWildcard() {
        String query = DbUtils.getSearchByEnglishQuery(true);
        assertTrue(query.contains("LIKE"));
    }

    @Test
    public void testGetSearchByKanaQuery_withoutWildcard() {
        String query = DbUtils.getSearchByEnglishQuery(false);
        assertFalse(query.contains("LIKE"));
    }

    @Test
    public void testGetSearchByKanjiQuery_withWildcard() {
        String query = DbUtils.getSearchByKanjiQuery(true);
        assertTrue(query.contains("LIKE"));
    }

    @Test
    public void testGetSearchByKanjiQuery_withoutWildcard() {
        String query = DbUtils.getSearchByKanjiQuery(false);
        assertFalse(query.contains("LIKE"));
    }

    @Test
    public void testGetSearchByEnglishQuery_withWildcard() {
        String query = DbUtils.getSearchByEnglishQuery(true);
        assertTrue(query.contains("LIKE"));
    }

    @Test
    public void testGetSearchByEnglishQuery_withoutWildcard() {
        String query = DbUtils.getSearchByEnglishQuery(false);
        assertFalse(query.contains("LIKE"));
    }


}
