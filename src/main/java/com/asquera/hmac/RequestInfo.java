package com.asquera.hmac;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

public class RequestInfo {
    
    private final List<NameValuePair> queries;
    private final List<String> headers;
    private final String method;
    private final Date date;
    
    public RequestInfo(final String url, final Map<String, Object> options) throws URISyntaxException {
        queries = Utils.getQueryStrings(url);
        headers = parseHeaders(options);
        date    = parseDate(options);
        method  = parseMethod(options);
    }
    
    public final List<NameValuePair> queries() {
        return this.queries;
    }
    
    public final List<String> headers() {
        return this.headers;
    }
    
    public String date() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("D, dd MM yyyy HH:mm:ss");
        return dateFormat.format(this.date) + " GMT";
    }
    
    private static List<String> parseHeaders(final Map<String, Object> options) {
        if (options.containsKey("headers") && options.get("headers") instanceof List) {
            return (List<String>)options.get("headers");
        }
        return new ArrayList<String>();
    }
    
    private Date parseDate(Map<String, Object> options) {
        if (options.containsKey("date")) {
            Object dateOption = options.get("date");
            if (!(dateOption instanceof Date)) {
                // TODO
                return new Date();
            }
            return (Date)dateOption;
        }
        return new Date();
    }
/*
        if (!empty($opts["date"])){
            $date = $opts["date"];
            
            if(!($date instanceof DateTime)) {
                if (is_int($date) || is_numeric($date)) {
                    $date = new DateTime("@".$date);
                } else {
                    // woah, we've done all we could, let's see what datetime makes of this
                    $date = new DateTime($date, new DateTimeZone("UTC"));
                }
            }
            
            $date->setTimezone(new DateTimeZone("UTC"));
        } else {
            $date = new DateTime("now", new DateTimeZone("UTC"));
        }
        $date = $date->format('D, d M Y H:i:s')." GMT";
 */
    
    private String parseMethod(Map<String, Object> options) {
        if (options.containsKey("method") && options.get("method") instanceof String) {
            String method = (String)options.get("method");
            return method.toUpperCase();
        }
        return "GET";
    }
}
