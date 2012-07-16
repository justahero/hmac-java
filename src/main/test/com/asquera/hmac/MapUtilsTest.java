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
        Assert.assertTrue(result.get("auth_scheme") instanceof String);
        Assert.assertEquals("HMAC", result.get("auth_scheme"));
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

}
