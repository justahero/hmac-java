package com.asquera.hmac;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.uri.URI;

public class Request {
    
    private final static String EOL = System.getProperty("line.separator");
    
    private final RequestParams options;
    private final URI uri;
    
    public Request(final String url, final RequestParams options) throws URISyntaxException {
        if (url == null)
            throw new IllegalArgumentException();
        if (options == null)
            throw new IllegalArgumentException();
        
        this.uri = URI.parse(url);
        this.options = new RequestParams(options);
    }
    
    public String canonicalRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append(options.method()).append(EOL);
        builder.append("date:").append(dateAsString()).append(EOL);
        builder.append("nonce:").append(options.nonce()).append(EOL);
        for (NameValuePair pair : options.headers()) {
            builder.append(pair.getName().toLowerCase()).append(':').append(pair.getValue()).append(EOL);
        }
        builder.append(path());
        
        String query = uri.sortQuery().query();
        if (query != null && !query.isEmpty()) {
            builder.append('?').append(query);
        }
        return builder.toString();
    }
    
    public URI uri() {
        return this.uri;
    }
    
    public String path() {
        return (uri.path() == null) ? "/" : uri.path();
    }
    
    public String query() {
        return uri.query();
    }
    
    public boolean isQueryBased() {
        return options.isQueryBased();
    }
    
    public String url() throws URISyntaxException {
        this.uri.sortQuery();
        return uri.toASCII();
    }
    
    public String dateAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        return (dateFormat.format(options.date()) + " GMT");
    }
    
    public void addHeaders(List<NameValuePair> headers) {
        this.options.addHeaders(headers);
    }
    
    public List<NameValuePair> headers() {
        return this.options.headers();
    }
    
    public Map<String, String> headersAsMap() {
        Map<String, String> result = new HashMap<String, String>();
        for (NameValuePair pair : this.options.headers()) {
            result.put(pair.getName(), pair.getValue());
        }
        return result;
    }
    
    public void addParam(String name, List<NameValuePair> auth_params) {
        for (NameValuePair pair : auth_params) {
            uri.addParam(String.format("%s[%s]", name, pair.getName()), pair.getValue());
        }
    }
    
    public void addParam(String name, String value) {
        uri.addParam(name, value);
    }
}
