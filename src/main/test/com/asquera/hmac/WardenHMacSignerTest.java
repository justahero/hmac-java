package com.asquera.hmac;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WardenHMacSignerTest {
    WardenHMacSigner signer;
    RequestParams options;
    
    @Before
    public void setUp() throws NoSuchAlgorithmException {
        signer = new WardenHMacSigner();
        options = new RequestParams();
    }
    
    private Map<String, String> toMap(List<NameValuePair> list) {
        Map<String, String> result = new HashMap<String, String>();
        for (NameValuePair pair : list) {
            result.put(pair.getName(), pair.getValue());
        }
        return result;
    }
    
    @Test
    public void signUrl() throws URISyntaxException, InvalidKeyException, NoSuchAlgorithmException {
        options.setDate(2012, 06, 19, 14, 36, 10);
        
        String actual = signer.signUrl("http://www.google.com", "SHAREDSECRET", options);
        String expected = "http://www.google.com?auth%5Bdate%5D=Thu%2C+19+Jul+2012+14%3A36%3A10+GMT";
        Assert.assertTrue(actual.startsWith(expected));
    }
    
    @Test
    public void signUrlAndDecodeResult() throws Exception {
        options.setDate(2012, 06, 19, 14, 36, 10);
        
        String signedUrl  = signer.signUrl("http://www.google.com", "SHAREDSECRET", options);
        String encodedUrl = URLDecoder.decode(signedUrl, "UTF-8");
        String expected   = "http://www.google.com?auth[date]=Thu, 19 Jul 2012 14:36:10 GMT";
        
        Assert.assertTrue(encodedUrl.startsWith(expected));
    }
    
    @Test
    public void signUrlContainsUrlWithoutQuery() throws InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
        options.setDate(2012, 06, 18, 15, 42, 01);
        options.setNonce("TESTNONCE");
        String url = "http://example.org?foo=temp&bar=void";
        
        String signedUrl = signer.signUrl(url, "SHAREDSECRET", options);
        String[] parts = signedUrl.split("\\?");
        
        Assert.assertEquals("http://example.org", parts[0]);
    }
    
    @Test
    public void signUrlWithQueryParameters() throws InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
        options.setDate(2012, 06, 18, 15, 42, 01);
        options.setNonce("TESTNONCE");
        
        String signedUrl = signer.signUrl("http://example.org?foo=temp&bar=void", "SHAREDSECRET", options);
        
        URI uri = new URI(signedUrl);
        Map<String, String> queries = toMap(URLEncodedUtils.parse(uri, "UTF-8"));
        
        Assert.assertTrue(queries.containsKey("foo"));
        Assert.assertEquals("temp", queries.get("foo"));
        Assert.assertTrue(queries.containsKey("bar"));
        Assert.assertEquals("void", queries.get("bar"));
        
        Assert.assertTrue(queries.containsKey("auth[date]"));
        Assert.assertEquals("Wed, 18 Jul 2012 15:42:01 GMT", queries.get("auth[date]"));
        Assert.assertTrue(queries.containsKey("auth[nonce]"));
        Assert.assertEquals("TESTNONCE", queries.get("auth[nonce]"));
        Assert.assertTrue(queries.containsKey("auth[date]"));
     }
}
