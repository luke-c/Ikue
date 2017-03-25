package com.ikue.japanesedictionary.utils;

import com.ikue.japanesedictionary.models.Tip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO: Extract to string resources
public class TipsUtils {
    private static final List<Tip> TIPS = initTips();

    private static List<Tip> initTips() {
        List<Tip> tips = new ArrayList<>();
        tips.add(new Tip("Wildcards #1", "You can use the two wildcards * and ? to search for unknown characters. * searches for zero or more characters, and ? searches for exactly one character."));
        tips.add(new Tip("Wildcards #2", "If you want to search for a word containing \"物\", you can by searching \"*物*\"."));
        tips.add(new Tip("Wildcards #3", "If you want to search for a word that starts with \"日\" you can by searching \"日*\"."));
        tips.add(new Tip("Wildcards #4", "If you want to search for a word ending with \"本\" you can by searching \"*本\"."));
        tips.add(new Tip("Wildcards #5", "If you want to search for a two character word that starts with \"食\", you can by searching \"食?\"."));
        tips.add(new Tip("Wildcards #6", "If you want to search for a three character word that contains \"大\" in the middle, you can by searching \"?大?\"."));
        tips.add(new Tip("Search #1", "You can search in Japanese, English, or even Romaji."));
        tips.add(new Tip("Search #2", "If there is some ambiguity as to whether you intended to search in English or Romaji, a popup will appear at the bottom of the results screen letting you switch results."));
        tips.add(new Tip("Search #3", "If you type in upper-case with a Romaji search, the upper-case characters will be converted to Katakana. For example, \"MOTEru\" will be converted to \"モテる\"."));
        tips.add(new Tip("Search #4", "Searches in English are case insensitive."));
        tips.add(new Tip("Search #5", "Ikue automatically converts latin characters into Kana when possible. For example, \"tabemono\" will be converted to \"たべもの\"."));
        tips.add(new Tip("Settings #1", "You can change the default search in the settings. The default search is for an exact match."));
        tips.add(new Tip("Settings #2", "You can limit the number of search results in the settings. Doing so will increase the speed of wildcard searches."));
        tips.add(new Tip("Settings #3", "You can limit the number of entries to show in your history page in the settings. This won't have any impact on performance."));
        tips.add(new Tip("Settings #4", "You can limit the number of entries to show in your favourites page in the settings. This won't have any impact on performance."));
        tips.add(new Tip("Settings #5", "You can change the default page you see when you launch the application in the settings."));
        tips.add(new Tip("History #1", "When you view an entry's detail page, that entry is automatically added to your history page."));
        tips.add(new Tip("Favourites #1", "You can favourite/un-favourite an entry by clicking on the star icon in an entry's detail page."));
        return tips;
    }

    // Get a random tip
    public static Tip getRandomTip() {
        return TIPS.get(new Random().nextInt(TIPS.size()));
    }
}
