package com.asquera.web;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ClientTest {
    
    @Before
    public void setUp() {
    }
    
    @Test(expected=Exception.class)
    public void failsOnWrongPort() throws Exception {
        Client.sendGetRequest("http://localhost:3111");
    }
    
    @Test
    public void canConnectToMainPath() throws Exception {
        Client.sendGetRequest("http://localhost:4567/");
    }
    
    @Test
    public void successfullySendsSignedUrl() throws Exception {
        String response = Client.sendGetRequest("http://localhost:4567/test");
        Assert.assertEquals("everything is fine", response);
    }
}
