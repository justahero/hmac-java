package com.asquera.hmac;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public class RequestInfo {
    
    private final static String EOL = System.getProperty("line.separator");
    
    private final RequestParams options;
    private final String query;
    private final String path;
    
    public RequestInfo(final String url, final RequestParams options) throws URISyntaxException {
        if (url == null)
            throw new IllegalArgumentException();
        if (options == null)
            throw new IllegalArgumentException();
        
        this.options = options;
        
        URI uri = new URI(url);
        query = parseQueryStrings(uri);
        path  = uri.getPath();
    }
    
    public String canonicalRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append(options.method()).append(EOL);
        builder.append("date:").append(options.date()).append(EOL);
        builder.append("nonce:").append(options.nonce()).append(EOL);
        for (NameValuePair pair : options.headers()) {
            builder.append(pair.getName().toLowerCase()).append(':').append(pair.getValue()).append(EOL);
        }
        builder.append(path()).append(sortedQuery());
        return builder.toString();
    }
    
    public String path() {
        return this.path;
    }
    
    public String sortedQuery() {
        return this.query;
    }
    
    public boolean isQueryBased() {
        return options.isQueryBased();
    }
    
    public String dateAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MM yyyy HH:mm:ss");
        return (dateFormat.format(options.date()) + " GMT").toUpperCase();
    }
    
    private static String parseQueryStrings(URI uri) {
        String encoding = "UTF-8";
        List<NameValuePair> queries = URLEncodedUtils.parse(uri, encoding);
        Collections.sort(queries, new PairComparator());
        
        try {
            List<String> parts = new ArrayList<String>();
            for (NameValuePair pair : queries) {
                String decodedName  = URLDecoder.decode(pair.getName(), encoding);
                String decodedValue = URLDecoder.decode(pair.getValue(), encoding);
                parts.add(String.format("%s=%s", decodedName, decodedValue));
            }
            return Utils.join(parts, "&");
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }
}
