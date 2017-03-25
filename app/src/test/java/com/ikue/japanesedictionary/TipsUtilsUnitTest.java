package com.ikue.japanesedictionary;

import com.ikue.japanesedictionary.models.Tip;
import com.ikue.japanesedictionary.utils.TipsUtils;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

public class TipsUtilsUnitTest {
    @Test
    public void testGetRandomTip() {
        Tip tip = TipsUtils.getRandomTip();

        assertNotNull(tip.getTitle());
        assertNotNull(tip.getBody());

        assertFalse(tip.getTitle().isEmpty());
        assertFalse(tip.getBody().isEmpty());
    }
}
