package com.asquera.hmac;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * Comparator for NameValuePair objects, used for sorting.
 */
class PairComparator implements Comparator<NameValuePair> {
    public int compare(final NameValuePair left, final NameValuePair right) {
        if (left.getName().equalsIgnoreCase(right.getName())) {
            return left.getValue().compareToIgnoreCase(right.getValue());
        }
        return left.getName().compareToIgnoreCase(right.getName());
    }
}

public class RequestInfo {
    
    private final static String EOL = System.getProperty("line.separator");
    
    private final String query;
    private final List<NameValuePair> headers;
    private final String method;
    private final Date date;
    private final String nonce;
    private final String path;
    private final Boolean queryBased;
    
    public RequestInfo(final String url, final Map<String, Object> options) throws URISyntaxException {
        if (url == null)
            throw new IllegalArgumentException();
        if (options == null)
            throw new IllegalArgumentException();
        
        URI uri = new URI(url);
        
        query      = parseQueryStrings(uri);
        headers    = parseHeaders(options);
        date       = parseDate(options);
        method     = parseMethod(options);
        nonce      = "";
        path       = uri.getPath();
        queryBased = parseIsQueryBased(options);
    }
    
    public String canonicalRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append(method()).append(EOL);
        builder.append("date:").append(date()).append(EOL);
        builder.append("nonce:").append(nonce()).append(EOL);
        for (NameValuePair pair : headers) {
            builder.append(pair.getName().toLowerCase()).append(':').append(pair.getValue()).append(EOL);
        }
        builder.append(path()).append(sortedQuery());
        return builder.toString();
    }
    
    public List<NameValuePair> headers() {
        return this.headers;
    }
    
    public String date() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MM yyyy HH:mm:ss");
        return (dateFormat.format(this.date) + " GMT").toUpperCase();
    }
    
    public String method() {
        return this.method;
    }
    
    public String nonce() {
        return this.nonce;
    }
    
    public String path() {
        return this.path;
    }
    
    public String sortedQuery() {
        return this.query;
    }
    
    public boolean isQueryBased() {
        return this.queryBased;
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
    
    private static List<NameValuePair> parseHeaders(final Map<String, Object> options) {
        if (options.containsKey("headers") && options.get("headers") instanceof List) {
            List<NameValuePair> headers = (List<NameValuePair>)options.get("headers");
            Collections.sort(headers, new PairComparator());
            return (List<NameValuePair>)options.get("headers");
        }
        return new ArrayList<NameValuePair>();
    }
    
    private static Date parseDate(Map<String, Object> options) {
        if (options.containsKey("date")) {
            Object option = options.get("date");
            if (option instanceof Integer) {
                return new Date((Integer)option);
            } else if (option instanceof String) {
                try {
                    SimpleDateFormat defaultFormat = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
                    Date date = defaultFormat.parse((String)option);
                    return date;
                } catch (ParseException e) {}
            } else if (option instanceof Date) {
                return (Date)option;
            }
        }
        return new Date();
    }
    
    private static String parseMethod(Map<String, Object> options) {
        if (options.containsKey("method") && options.get("method") instanceof String) {
            String method = (String)options.get("method");
            return method.toUpperCase();
        }
        return "GET";
    }
    
    private static boolean parseIsQueryBased(Map<String, Object> options) {
        if (options.containsKey("query_based")) {
            Object option = options.get("query_based");
            if (option instanceof Boolean) {
                return (Boolean)option;
            } else if (option instanceof String) {
                Boolean queryBased = new Boolean((String)option);
                return queryBased.booleanValue();
            }
        }
        return false;
    }
}
