package com.asquera.hmac;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RequestInfoTest {
    
    private RequestParams options;
    
    @Before
    public void setUp() {
        options = new RequestParams();
    }
    
    @Test
    public void constructorThrowsExceptionWhenMapIsNull() throws URISyntaxException {
        try {
            new RequestInfo("http://www.example.com", null);
            Assert.fail("Must throw exception when map is null");
        } catch (IllegalArgumentException e) {
        }
    }
    
    @Test
    public void constructorThrowsExceptionWhenNoUrl() throws URISyntaxException {
        try {
            new RequestInfo(null, options);
            Assert.fail("Must throw exception when url is null");
        } catch (IllegalArgumentException e) {
        }
    }
    
    @Test
    public void pathFromUrlWithoutQuery() throws URISyntaxException {
        RequestInfo info = new RequestInfo("http://www.example.com", options);
        String actualPath = info.path();
        Assert.assertEquals("", actualPath);
    }
    
    @Test
    public void pathFromUrlWithPath() throws URISyntaxException {
        RequestInfo info = new RequestInfo("http://www.example.com/images/test.png", options);
        String actualPath = info.path();
        Assert.assertEquals("/images/test.png", actualPath);
    }
    
    @Test
    public void pathFromUrlWithQuery() throws URISyntaxException {
        RequestInfo info = new RequestInfo("http://www.test.com/blog?key=value&test=true", options);
        String actualPath = info.path();
        Assert.assertEquals("/blog", actualPath);
    }
    
    @Test
    public void dateAsDateGetsFormatted() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.set(2012, 06, 17, 10, 20, 30);
        Date date = calendar.getTime();
        options.setDate(date);
        
        RequestInfo request = new RequestInfo("http://example.com", options);
        String actualDate = request.dateAsString();
        Assert.assertEquals("TUE, 17 07 2012 10:20:30 GMT", actualDate);
    }
    
    
    @Test
    public void isNotQueryBasedWithDefaultOption() throws URISyntaxException {
        RequestInfo request = new RequestInfo("http://www.example.com", options);
        Assert.assertFalse(request.isQueryBased());
    }
    
    @Test
    public void isNotQueryBasedWhenOptionIsFalse() throws URISyntaxException {
        options.setQueryBased(false);
        RequestInfo request = new RequestInfo("http://www.example.com", options);
        Assert.assertFalse(request.isQueryBased());
    }
    
    @Test
    public void isQueryBasedWhenOptionIsTrue() throws URISyntaxException {
        options.setQueryBased(true);
        RequestInfo request = new RequestInfo("http://www.example.com", options);
        Assert.assertTrue(request.isQueryBased());
    }
}
