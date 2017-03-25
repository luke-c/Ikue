package com.ikue.japanesedictionary;

import com.ikue.japanesedictionary.utils.DateTimeUtils;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DateTimeUtilsUnitTest {
    @Test
    public void testGetDifferenceInDays() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);

        assertTrue((DateTimeUtils.getDifferenceInDays(sdf.parse("21/12/2012"), sdf.parse("21/12/2012")) == 0));
        assertTrue(DateTimeUtils.getDifferenceInDays(sdf.parse("21/12/2012"), sdf.parse("21/12/2013")) > 1);
        assertTrue(DateTimeUtils.getDifferenceInDays(sdf.parse("21/12/2012"), sdf.parse("21/12/2011")) < 0);
        assertTrue(DateTimeUtils.getDifferenceInDays(sdf.parse("21/12/2015"), sdf.parse("21/06/2015")) < 0);
        assertTrue(DateTimeUtils.getDifferenceInDays(sdf.parse("21/01/2015"), sdf.parse("21/08/2015")) > 1);
        assertTrue(DateTimeUtils.getDifferenceInDays(sdf.parse("21/01/2015"), sdf.parse("31/01/2015")) > 1);
        assertTrue(DateTimeUtils.getDifferenceInDays(sdf.parse("31/01/2015"), sdf.parse("30/01/2015")) < 0);
        assertTrue(DateTimeUtils.getDifferenceInDays(sdf.parse("01/01/2017"), sdf.parse("31/02/2016")) < 0);
        assertTrue(DateTimeUtils.getDifferenceInDays(sdf.parse("01/01/2017"), sdf.parse("01/02/2017")) > 1);
        assertTrue(DateTimeUtils.getDifferenceInDays(sdf.parse("01/01/2017"), sdf.parse("01/01/2018")) > 1);
    }
}