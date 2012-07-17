package com.asquera.hmac;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
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
    public void dateAsStringGetsFormatted() throws Exception {
        options.put("date", "15 01 2012 16:43:21");
        
        RequestInfo request = new RequestInfo("http://example.com/test", options);
        Assert.assertEquals("SUN, 15 01 2012 16:43:21 GMT", request.date());
    }
    
    @Test
    public void dateAsDateGetsFormatted() throws Exception {
        Calendar calendar = new GregorianCalendar();
        calendar.set(2012, 06, 17, 10, 20, 30);
        Date date = calendar.getTime();
        options.put("date", date);
        
        RequestInfo request = new RequestInfo("http://example.com", options);
        String actualDate = request.date();
        Assert.assertEquals("TUE, 17 07 2012 10:20:30 GMT", actualDate);
    }

    @Test
    public void queryStringsReturnsEmptyMapWithoutQuery() throws Exception {
        RequestInfo request = new RequestInfo("http://www.example.com", options);
        List<NameValuePair> queries = request.queries();
        
        Assert.assertNotNull(queries);
        Assert.assertTrue(queries.isEmpty());
    }

    @Test
    public void queryStringsReturnsSingleEntryFromQuery() throws Exception {
        RequestInfo request = new RequestInfo("http://www.example.com/test?key=value", options);
        List<NameValuePair> queries = request.queries();
        
        Assert.assertNotNull(queries);
        Assert.assertFalse(queries.isEmpty());
        Assert.assertEquals("key", queries.get(0).getName());
        Assert.assertEquals("value", queries.get(0).getValue());
    }

    @Test
    public void queryStringsSortsEntries() throws Exception {
        RequestInfo request = new RequestInfo("http://www.example.com/test?value=1&temp=2", options);
        List<NameValuePair> queries = request.queries();
        
        Assert.assertEquals(2, queries.size());
        Assert.assertEquals("temp", queries.get(0).getName());
        Assert.assertEquals("value", queries.get(1).getName());
    }

    @Test
    public void queryStringsSortsEntriesWithSameKey() throws Exception {
        RequestInfo request = new RequestInfo("http://www.example.com/test?value=def&value=abc", options);
        List<NameValuePair> queries = request.queries();
        
        Assert.assertEquals(2, queries.size());
        Assert.assertEquals("abc", queries.get(0).getValue());
        Assert.assertEquals("def", queries.get(1).getValue());
    }
    
}
