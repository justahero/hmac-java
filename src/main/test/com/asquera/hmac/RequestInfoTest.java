package com.asquera.hmac;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RequestInfoTest {
    
    private Map<String, Object> options;
    
    @Before
    public void setUp() {
        options = new HashMap<String, Object>();
    }
    
    @Test
    public void dateAsStringIsFormatted() {
//        options.put("date", value)
    }
    
    @Test
    public void dateAsDateIsFormatted() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.set(2012, 07, 17, 10, 20, 30);
        Date date = calendar.getTime();
        options.put("date", date);
        
        RequestInfo request = new RequestInfo("http://example.com", options);
        Assert.assertEquals("TUE, 17 07 2012 10:20:30 GMT", request.date());
    }
}
