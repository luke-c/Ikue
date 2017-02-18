package com.ikue.japanesedictionary.utils;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by luke_c on 18/02/2017.
 */

public class DbUtils {
    // Split the string on the separator character into a list, then
    // convert to a LinkedHashSet to remove duplicate values.
    @Nullable
    public static List<String> formatString(String stringToFormat) {
        if (stringToFormat != null && !stringToFormat.isEmpty()) {
            return new ArrayList<>(new LinkedHashSet<>(Arrays.asList(stringToFormat.split("§"))));
        } else {
            // We never want a null value in our DictionaryItem, so just return an empty list
            return Collections.emptyList();
        }
    }
}
