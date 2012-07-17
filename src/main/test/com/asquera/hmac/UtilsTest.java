package com.asquera.hmac;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.apache.http.NameValuePair;
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
    public void queryStringsReturnsEmptyMapWithoutQuery() throws Exception {
        List<NameValuePair> queries = Utils.getQueryStrings("http://www.example.com");
        Assert.assertNotNull(queries);
        Assert.assertTrue(queries.isEmpty());
    }
    
    @Test
    public void queryStringsReturnsSingleEntryFromQuery() throws Exception {
        List<NameValuePair> queries = Utils.getQueryStrings("http://www.example.com/test?key=value");
        Assert.assertNotNull(queries);
        Assert.assertFalse(queries.isEmpty());
        Assert.assertEquals("key", queries.get(0).getName());
        Assert.assertEquals("value", queries.get(0).getValue());
    }
    
    @Test
    public void queryStringsSortsEntries() throws Exception {
        List<NameValuePair> queries = Utils.getQueryStrings("http://www.example.com/test?value=1&temp=2");
        Assert.assertEquals(2, queries.size());
        Assert.assertEquals("temp", queries.get(0).getName());
        Assert.assertEquals("value", queries.get(1).getName());
    }
    
    @Test
    public void queryStringsSortsEntriesWithSameKey() throws Exception {
        List<NameValuePair> queries = Utils.getQueryStrings("http://www.example.com/test?value=def&value=abc");
        Assert.assertEquals(2, queries.size());
        Assert.assertEquals("abc", queries.get(0).getValue());
        Assert.assertEquals("def", queries.get(1).getValue());
    }

}

