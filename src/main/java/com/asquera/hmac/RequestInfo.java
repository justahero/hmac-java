package com.asquera.hmac;

import java.net.URI;
import java.net.URISyntaxException;
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
    
    private final List<NameValuePair> queries;
    private final List<String> headers;
    private final String method;
    private final Date date;
    private final String nonce;
    
    public RequestInfo(final String url, final Map<String, Object> options) throws URISyntaxException {
        if (url == null)
            throw new IllegalArgumentException();
        if (options == null)
            throw new IllegalArgumentException();
        
        queries = parseQueryStrings(url);
        headers = parseHeaders(options);
        date    = parseDate(options);
        method  = parseMethod(options);
        nonce   = "";
    }
    
    public String canonicalRepresentation() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(method()).append(EOL);
        buffer.append("date:").append(date()).append(EOL);
        buffer.append("nonce:").append(nonce()).append(EOL);
        return buffer.toString();
    }
/*
    # returns the canonical representation for the given list of parameters
        $rep .= strtoupper($params["method"]) . "\n";
        $rep .= "date:".$params["date"]."\n";
        $rep .= "nonce:".$params["nonce"]."\n";
    
        if (empty($params["headers"])) {
            $headers = array();
        } else {
            $headers = $params["headers"];
        }
        ksort($headers);
        foreach($headers as $name => $value) {
            $rep .= strtolower($name).":".$value."\n";
        }
        
        $rep .= $params["path"];
        
        if(!empty($params["query"])) {
            $t = array();
            $q = $params["query"];
            ksort($q);
            
            foreach($q as $key => $value) {
                $t[] = urldecode($key)."=".urldecode($value);
            }
            
            $rep .= "?".join($t, "&");
        }
        
        return $rep;
    }
*/

    public final List<NameValuePair> queries() {
        return this.queries;
    }
    
    public final List<String> headers() {
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
    
    private static List<NameValuePair> parseQueryStrings(String url) throws URISyntaxException {
        URI uri = new URI(url);
        List<NameValuePair> queries = URLEncodedUtils.parse(uri, "UTF-8");
        Collections.sort(queries, new PairComparator());
        return queries;
    }
    
    private static List<String> parseHeaders(final Map<String, Object> options) {
        if (options.containsKey("headers") && options.get("headers") instanceof List) {
            List<NameValuePair> headers = (List<NameValuePair>)options.get("headers");
            Collections.sort(headers, new PairComparator());
            return (List<String>)options.get("headers");
        }
        return new ArrayList<String>();
    }
    
    private Date parseDate(Map<String, Object> options) {
        if (options.containsKey("date")) {
            Object dateOption = options.get("date");
            if (dateOption instanceof Integer) {
                return new Date((Integer)dateOption);
            } else if (dateOption instanceof String) {
                try {
                    // 15 01 2012 16:43:21
                    SimpleDateFormat defaultFormat = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
                    Date date = defaultFormat.parse((String)dateOption);
                    return date;
                } catch (ParseException e) {}
            } else if (dateOption instanceof Date) {
                return (Date)dateOption;
            }
        }
        return new Date();
    }
    
    private String parseMethod(Map<String, Object> options) {
        if (options.containsKey("method") && options.get("method") instanceof String) {
            String method = (String)options.get("method");
            return method.toUpperCase();
        }
        return "GET";
    }
}
