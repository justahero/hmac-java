package com.asquera.hmac;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MapUtilsTest {

    Map<String, Object> left  = new HashMap<String, Object>();
    Map<String, Object> right = new HashMap<String, Object>();
    
    @Before
    public void setUp() {
        left  = new HashMap<String, Object>();
        right = new HashMap<String, Object>();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void mergesEmptyMaps() {
        Map<String, Object> result = MapUtils.mergeMaps(left, right);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void mergesLeftMapWithEntry() {
        left.put("auth_scheme", new String("HMAC"));
        Map<String, Object> result = MapUtils.mergeMaps(left, right);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.containsKey("auth_scheme"));
        Assert.assertEquals("HMAC", result.get("auth_scheme"));
    }
    
    @Test
    public void in() {
        left.put("auth_scheme", new String("HMAC"));
        left.put("extra_auth_params", new HashMap<String, String>());
        Map<String, Object> result = MapUtils.mergeMaps(left, right);
        
        Assert.assertTrue(result.get("auth_scheme") instanceof String);
        Assert.assertTrue(result.get("extra_auth_params") instanceof HashMap);
    }
    
    @Test
    public void mergesRightMapWithEntry() {
        right.put("query_based", new Boolean(false));
        Map<String, Object> result = MapUtils.mergeMaps(left, right);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.containsKey("query_based"));
        Assert.assertTrue(result.get("query_based") instanceof Boolean);
        Assert.assertEquals(false, result.get("query_based"));
    }
    
    @Test
    public void mergesTwoMapsWithDisjunctEntries() {
        left.put("auth_param", "auth");
        right.put("auth_scheme", "HMAC");
        Map<String, Object> result = MapUtils.mergeMaps(left, right);
        
        Assert.assertEquals(2, result.size());
        Assert.assertEquals("auth", result.get("auth_param"));
        Assert.assertEquals("HMAC", result.get("auth_scheme"));
    }
    
    @Test
    public void mergeMapsWithSameKey() {
        left.put("auth_header", "test");
        right.put("auth_header", "Authentication");
        Map<String, Object> result = MapUtils.mergeMaps(left, right);
        
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.get("auth_header") instanceof String);
        Assert.assertEquals("Authentication", result.get("auth_header"));
    }

}
