package com.asquera.hmac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class UtilsTest {
    
    private Map<String, String> replacements;
    
    @Before
    public void setUp() {
        this.replacements = new HashMap<String, String>();
    }
    
    @Test
    public void interpolatesStringThrowsExceptionWhenReplacementsNull() {
        try {
            Utils.interpolateString("", null);
            Assert.fail("throws exception when replacements map is null");
        } catch (IllegalArgumentException e) {
        }
    }
    
    @Test
    public void interpolatesStringThrowsExceptionWhenStringIsNull() {
        try {
            Utils.interpolateString(null, replacements);
        } catch (IllegalArgumentException e) {
        }
    }
    
    @Test
    public void interpolatesStringReturnsEmptyString() {
        String result = Utils.interpolateString("", replacements);
        Assert.assertNotNull(result);
        Assert.assertEquals("", result);
    }
    
    @Test
    public void interpolatesStringReturnsStringWithoutReplacements() {
        String result = Utils.interpolateString("auth_header_format", replacements);
        Assert.assertNotNull(result);
        Assert.assertEquals("auth_header_format", result);
    }
    
    @Test
    public void interpolatesStringReturnsStringWithoutPlaceholder() {
        replacements.put("header", "value");
        String result = Utils.interpolateString("auth_header", replacements);
        Assert.assertEquals("auth_header", result);
    }
    
    @Test
    public void interpolatesStringReturnsStringWithNoMatch() {
        String result = Utils.interpolateString("X-%{scheme}-Nonce", replacements);
        Assert.assertNotNull(result);
        Assert.assertEquals("X-%{scheme}-Nonce", result);
    }
    
    @Test
    public void interpolatesStringMatchesReplacement() {
        replacements.put("scheme", "HMAC");
        String result = Utils.interpolateString("X-%{scheme}-Date", replacements);
        Assert.assertNotNull(result);
        Assert.assertEquals("X-HMAC-Date", result);
    }
    
    @Test
    public void joinsEmptyStringArray() {
        List<String> strings = new ArrayList<String>();
        Assert.assertEquals("", Utils.join(strings, "&"));
    }
    
    @Test
    public void joinArrayWithSingleString() {
        List<String> strings = new ArrayList<String>();
        strings.add("test");
        Assert.assertEquals("test", Utils.join(strings, ";"));
    }
    
    @Test
    public void joinArrayWithTwoStrings() {
        List<String> strings = new ArrayList<String>();
        strings.add("first");
        strings.add("second");
        Assert.assertEquals("first&second", Utils.join(strings, "&"));
    }
}

