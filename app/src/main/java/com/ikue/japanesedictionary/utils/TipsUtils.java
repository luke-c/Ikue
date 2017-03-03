package com.ikue.japanesedictionary.utils;

import com.ikue.japanesedictionary.models.Tip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by luke_c on 03/03/2017.
 */

public class TipsUtils {
    private static final List<Tip> TIPS = initTips();

    private static List<Tip> initTips() {
        List<Tip> tips = new ArrayList<>();
        tips.add(new Tip("Wildcards #1", "Did you know you can search via wildcards? Type * for 0 or more matches, and ? for an exact match."));
        tips.add(new Tip("Wildcards #2", "If you want to search for a word containing \"test\", you can do so by searching \"*test*\"!"));
        tips.add(new Tip("Wildcards #3", "If you want to search for a word that starts with \"test\" you can do so by searching \"*test\"!"));
        tips.add(new Tip("Wildcards #4", "If you want to search for a word that ends with \"test\" you can do so by searching \"test*\"!"));
        tips.add(new Tip("Wildcards #5", "If you want to search for a word that follows three preceding characters, and ends with \"test\", you can do so by searching \"???test\"!"));
        tips.add(new Tip("Search #1", "You can search anyway you want! With Kanji, Kana, English, or even Romaji!"));
        tips.add(new Tip("Search #2", "If there is some ambiguity as to whether you wanted to search in English or Romaji, a snackbar will appear letting you switch results!"));
        tips.add(new Tip("Search #3", "If you type in all upper-case with a Romaji search, it will search via Katakana!"));
        tips.add(new Tip("Settings #1", "You can enable case sensitive Romaji search within the settings. Enabling this would give you different results for \"moteru\" and \"MOTEru\""));
        tips.add(new Tip("Settings #2", "You can change the default search within the settings. The default search is for an exact match, but you can choose a wildcard search as the default!"));
        tips.add(new Tip("Settings #3", "You can limit the number of search results within the settings. Doing so will increase the speed of searches!"));
        tips.add(new Tip("Settings #4", "You can limit the number of entries to show in your history within the settings. This won't have any impact on performance."));
        tips.add(new Tip("Settings #5", "You can limit the number of entries to show in your favourites within the settings. This won't have any impact on performance."));
        tips.add(new Tip("Settings #6", "You can change the default startup page within settings."));
        tips.add(new Tip("History #1", "When you view an entry's detail page it's automatically added to your history!"));
        tips.add(new Tip("Favourites #1", "You can favourite/un-favourite an entry by clicking the star icon in its detail page."));
        return tips;
    }

    public static Tip getRandomTip() {
        return TIPS.get(new Random().nextInt(TIPS.size()));
    }
}
