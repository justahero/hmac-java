package com.asquera.hmac;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WardenHMacSignerTest {
    
    private WardenHMacSigner defaultSigner;
    private Map<String, String> params;
    
    @Before
    public void setUp() {
        defaultSigner = new WardenHMacSigner();
        params = new HashMap<String, String>();
        params.put("method", "foo");
        params.put("date", "12062012");
        params.put("nonce", "123456");
        params.put("headers", "HEADERS");
        params.put("path", "PATH");
        params.put("query", "Query");
    }
    
    @Test
    public void canonicalRepresentationThrowsExceptionWhenNoMapGiven() {
        try {
            defaultSigner.canonicalRepresentation(null);
            Assert.fail("Must throw exception when parameters map is null");
        } catch (IllegalArgumentException e) {
        }
    }
    
    @Test
    public void canonicalRepresentationThrowsExceptionWhenMethodParamNotGiven() {
        params.remove("method");
        try {
            defaultSigner.canonicalRepresentation(params);
            Assert.fail("Must throw exception when \"method\" not in map");
        } catch (IllegalArgumentException e) {
        }
    }

}
