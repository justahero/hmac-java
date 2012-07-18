package com.asquera.hmac;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class Query {
    
    private final static String Encoding = "UTF-8";
    
    private final List<NameValuePair> queries = new ArrayList<NameValuePair>();
    
    public Query() {
    }
    
    public Query(String url) throws URISyntaxException {
        this(new URI(url));
    }
    
    public Query(URI uri) {
        add(URLEncodedUtils.parse(uri, "UTF-8"));
    }
    
    private void add(List<NameValuePair> params) {
        for (NameValuePair pair : params) {
            add(pair.getName(), pair.getValue());
        }
    }

    public void add(String name, String value) {
        this.queries.add(new BasicNameValuePair(name, value));
    }
    
    public void add(String name, List<NameValuePair> list) {
        for (NameValuePair pair : list) {
            add(String.format("%s.%s", name, pair.getName()), pair.getValue());
        }
    }
    
    public String query() {
        try {
            Collections.sort(this.queries, new PairComparator());
            List<String> params = new ArrayList<String>();
            for (NameValuePair pair : this.queries) {
                String decodedKey = URLDecoder.decode(pair.getName(), Encoding);
                String decodedName = URLDecoder.decode(pair.getValue(), Encoding);
                params.add(String.format("%s=%s", decodedKey, decodedName));
            }
            return Utils.join(params, "&");
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }
    
    public String encodedQuery() {
        Collections.sort(this.queries, new PairComparator());
        return URLEncodedUtils.format(this.queries, "UTF-8");
    }
}

