package com.ikue.japanesedictionary.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntryUtils {
    // These are priorities defined as "common" by JMdict
    // "Entries with news1, ichi1, spec1/2 and gai1 values are marked with a "(P)" in the EDICT and EDICT2 files."
    private static final String[] SET_VALUES = new String[] { "news1", "ichi1", "spec1", "spec2", "gai1" };
    private static final Set<String> COMMON_PRIORITIES = new HashSet<>(Arrays.asList(SET_VALUES));

    // Return true if an entry is 'common'
    public static boolean isCommonEntry(Set<String> priorities) {
        return !Collections.disjoint(priorities, COMMON_PRIORITIES);
    }

    // TODO: Extract to string resources
    public static Map<String, String> getDetailedPriorityInformation() {
        Map<String, String> detailedPriorities = new HashMap<>();
        detailedPriorities.put("news1", "Appears in the first 12,000 words of the \"wordfreq\" file compiled by Alexandre Girardi from the Mainichi Shimbun.");
        detailedPriorities.put("news2", "Appears in the second 12,000 words of the \"wordfreq\" file compiled by Alexandre Girardi from the Mainichi Shimbun.");
        detailedPriorities.put("ichi1", "Appears in \"Ichimango goi bunruishuu\", Senmon Kyouiku Publishing, Tokyo, 1998.");
        detailedPriorities.put("ichi2", "Appears in \"Ichimango goi bunruishuu\", Senmon Kyouiku Publishing, Tokyo, 1998. Demoted from ichi1 due to low frequency in newspapers and online.");
        detailedPriorities.put("spec1", "A small number of words use this marker when they are detected as being common, but are not included in other lists.");
        detailedPriorities.put("spec2", "A small number of words use this marker when they are detected as being common, but are not included in other lists.");
        detailedPriorities.put("gai1", "A common loanword. Based on the \"wordfreq\" file compiled by Alexandre Girardi from the Mainichi Shimbun.");
        detailedPriorities.put("gai2", "A common loanword. Based on the \"wordfreq\" file compiled by Alexandre Girardi from the Mainichi Shimbun.");
        detailedPriorities.put("nf", "An indicator of frequency-of-use ranking, with the number being the set of 500 words where the entry can be found.");
        detailedPriorities.put("common", "Entries with news1, ichi1, spec1/2, or gai1 values are marked as common.");
        return detailedPriorities;
    }
}
