package com.ikue.japanesedictionary;

import com.ikue.japanesedictionary.utils.EntryUtils;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class EntryUtilsUnitTest {
    @Test
    public void testIsCommonEntry() {
        assertTrue(EntryUtils.isCommonEntry(new HashSet<>(Arrays.asList("news1", "news2", "ichi1"))));
        assertTrue(EntryUtils.isCommonEntry(new HashSet<>(Arrays.asList("spec1", "news2", "nf05"))));
        assertTrue(EntryUtils.isCommonEntry(new HashSet<>(Arrays.asList("random", "ichi1", "nf99"))));
        assertTrue(EntryUtils.isCommonEntry(new HashSet<>(Arrays.asList("random", "news2", "spec2"))));
        assertTrue(EntryUtils.isCommonEntry(new HashSet<>(Arrays.asList("news2", "gai1", "spec2"))));

        assertFalse(EntryUtils.isCommonEntry(new HashSet<>(Arrays.asList("news2", "gai2", "nf01"))));
        assertFalse(EntryUtils.isCommonEntry(new HashSet<>(Arrays.asList("ichi2", "news2", "gai2"))));
        assertFalse(EntryUtils.isCommonEntry(new HashSet<>(Arrays.asList("ichi2", "news2"))));
        assertFalse(EntryUtils.isCommonEntry(new HashSet<>(Arrays.asList("nf123", "news2", "random"))));
    }

    @Test
    public void testGetDetailedPriorityInformation() {
        Map<String, String> information = EntryUtils.getDetailedPriorityInformation();

        assertTrue(information.size() == 10);

        assertTrue(information.containsKey("news1"));
        assertTrue(information.containsKey("news2"));
        assertTrue(information.containsKey("ichi1"));
        assertTrue(information.containsKey("ichi2"));
        assertTrue(information.containsKey("spec1"));
        assertTrue(information.containsKey("spec2"));
        assertTrue(information.containsKey("gai1"));
        assertTrue(information.containsKey("gai2"));
        assertTrue(information.containsKey("nf"));
        assertTrue(information.containsKey("common"));
    }
}
