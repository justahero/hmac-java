package com.asquera.hmac;

import java.util.HashMap;
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
    public void interpolatesStringThrowsExceptionWhenReplacementsIsNull() {
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
    public void interpolatesStringWithEmptyString() {
        String result = Utils.interpolateString("", replacements);
        Assert.assertNotNull(result);
        Assert.assertEquals("", result);
    }
    
    @Test
    public void sameStringWithoutEmptyReplacements() {
        String result = Utils.interpolateString("auth_header_format", replacements);
        Assert.assertNotNull(result);
        Assert.assertEquals("auth_header_format", result);
    }
    
    @Test
    public void sameStringWithReplacementButNoPlaceHolder() {
        replacements.put("header", "value");
        String result = Utils.interpolateString("auth_header", replacements);
        Assert.assertEquals("auth_header", result);
    }
    
    @Test
    public void sameStringWithoutMatchingReplacements() {
        String result = Utils.interpolateString("X-%{scheme}-Nonce", replacements);
        Assert.assertNotNull(result);
        Assert.assertEquals("X-%{scheme}-Nonce", result);
    }
    
    @Test
    public void matchesStringWithReplacement() {
        replacements.put("scheme", "HMAC");
        String result = Utils.interpolateString("X-%{scheme}-Date", replacements);
        Assert.assertNotNull(result);
        Assert.assertEquals("X-HMAC-Date", result);
    }
}

