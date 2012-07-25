package com.asquera.web;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ClientTest {
    
    private final static String SECRET = "TESTSECRET";
    private Client client;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        client = new Client(Mac.getInstance("HmacMD5"));
    }
    
    @Test(expected=Exception.class)
    public void failsOnWrongPort() throws Exception {
        String signedUrl = client.signUrl("http://localhost:3111", SECRET);
        client.sendGetRequest(signedUrl);
    }
    
    @Test
    public void canConnectToMainPath() throws Exception {
        String signedUrl = client.signUrl("http://localhost:4567/", SECRET);
        client.sendGetRequest(signedUrl);
    }
    
    @Test
    public void successfullySendsSignedUrl() throws Exception {
        String signedUrl = client.signUrl("http://localhost:4567/test", SECRET);
        String response = client.sendGetRequest(signedUrl);
        Assert.assertEquals("everything is fine", response);
    }
}
