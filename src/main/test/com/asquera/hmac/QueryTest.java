package com.asquera.hmac;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Test;

public class QueryTest {
    
    @Test
    public void parsesUrlWithoutQuery() throws URISyntaxException {
        Query params = new Query("http://www.example.com");
        Assert.assertEquals("", params.query());
    }
    
    @Test
    public void parsesUrlWithSingleQueryEntry() throws URISyntaxException {
        Query params = new Query("http://www.example.com/test?key=value");
        String actualQuery = params.query();
        Assert.assertEquals("key=value", actualQuery);
    }
    
    @Test
    public void queryStringsSortsSimpleEntries() throws URISyntaxException {
        Query params = new Query("http://www.example.com/test?value=1&temp=2");
        String actualQuery = params.query();
        Assert.assertEquals("temp=2&value=1", actualQuery);
    }
    
    @Test
    public void queryStringsSortsSimpleEntriesWithSameKey() throws Exception {
        Query params = new Query("?value=def&value=abc");
        Assert.assertEquals("value=abc&value=def", params.query());
    }
    
    @Test
    public void queryStringWithNestedQueryParameters() throws URISyntaxException {
        Query params = new Query("?one[two][three]=four");
        String actualQuery = params.query();
        Assert.assertEquals("one[two][three]=four", actualQuery);
    }
    
    @Test
    public void queryStringWithAddedParameters() throws URISyntaxException {
        Query params = new Query("?auth=2");
        params.add("bcd", "22");
        params.add("abc", "http");
        Assert.assertEquals("abc=http&auth=2&bcd=22", params.query());
    }
    
    @Test
    public void queryStringWithAddedEmptyParamsList() throws URISyntaxException {
        Query params = new Query("?terminal=true");
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        params.add("auth", list);
        Assert.assertEquals("terminal=true", params.query());
    }
    
    @Test
    public void queryStringWithAddedParamsList() throws URISyntaxException {
        Query params = new Query("?terminal=true");
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("signature", "12345678"));
        list.add(new BasicNameValuePair("time", "4444"));
        params.add("auth", list);
        Assert.assertEquals("auth[signature]=12345678&auth[time]=4444&terminal=true", params.query());
    }
}


