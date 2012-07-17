package com.asquera.hmac;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WardenHMacSignerTest {
    
    private WardenHMacSigner defaultSigner;
    private Map<String, Object> params;
    
    @Before
    public void setUp() throws NoSuchAlgorithmException {
        Mac mac = Mac.getInstance("HmacMD5");
        defaultSigner = new WardenHMacSigner(mac);
        params = new HashMap<String, Object>();
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
