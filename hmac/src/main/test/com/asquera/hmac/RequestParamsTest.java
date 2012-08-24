package com.asquera.hmac;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class RequestParamsTest {

    private RequestParams options;

    @Before
    public void setUp() {
        options = new RequestParams();
    }
    
    @Test
    public void getsAuthHeaderWithReplacedSignatureParam() {
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("signature", "1234567890");
        
        String actual = options.authHeaderFormat(replacements);
        Assert.assertEquals("HMAC 1234567890", actual);
    }
    
    @Test
    public void getAuthHeaderWithDifferentAuthScheme() {
        options.setAuthScheme("MYHMAC");
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("signature", "445566");
        
        String actual = options.authHeaderFormat(replacements);
        Assert.assertEquals("MYHMAC 445566", actual);
    }
    
    @Test
    public void getNonceHeaderWithDefaultValue() {
        String actual = options.nonceHeader();
        Assert.assertEquals("X-HMAC-Nonce", actual);
    }
    
    @Test
    public void getNonceHeaderWithValue() {
        options.setAuthScheme("TEST");
        Assert.assertEquals("X-TEST-Nonce", options.nonceHeader());
    }
    
    @Test
    public void getAlternateDateHeaderWithDefaultValue() {
        String actual = options.alternateDateHeader();
        Assert.assertEquals("X-HMAC-Date", actual);
    }
    
    @Test
    public void getAlternateDateHeader() {
        options.setAuthScheme("MyTest");
        Assert.assertEquals("X-MyTest-Date", options.alternateDateHeader());
    }
    
    @Test
    public void dateAsDateGetsFormatted() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.set(2012, 06, 17, 10, 20, 30);
        Date date = calendar.getTime();
        options.setDate(date);
        
        String actualDate = options.dateAsString();
        Assert.assertEquals("Tue, 17 Jul 2012 10:20:30 GMT", actualDate);
    }

}
