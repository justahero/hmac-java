package com.asquera.hmac;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

public class RequestParams {
    private Date date = new Date();
    
    private String nonce      = "";
    private String method     = "GET";
    private String authScheme = "HMAC";
    private String authParam  = "auth";
    private String authHeader = "Authorization";
    private String authHeaderFormat = "%{auth_scheme} %{signature}";
    
    private boolean isQueryBased = false;
    private boolean useAlternateDateHeader = false;
    
    private final List<NameValuePair> headers = new ArrayList<NameValuePair>();
    private final List<NameValuePair> extraAuthParams = new ArrayList<NameValuePair>();
    
    public RequestParams() {
    }
    
    public String nonce() {
        return this.nonce;
    }
    
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    
    public Date date() {
        return this.date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setDate(int year, int month, int day, int hours, int minutes, int seconds) {
        Calendar calendar = new GregorianCalendar(year, month, day, hours, minutes, seconds);
        setDate(calendar.getTime());
    }
    
    public List<NameValuePair> headers() {
        Collections.sort(this.headers, new PairComparator());
        return this.headers;
    }
    
    public void addHeader(String name, String value) {
        this.headers.add(new BasicNameValuePair(name, value));
    }
    
    public void addHeaders(List<NameValuePair> headers) {
        this.headers.addAll(headers);
    }
    
    public String method() {
        return this.method.toUpperCase();
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    public String authScheme() {
        return this.authScheme;
    }
    
    public void setAuthScheme(String authScheme) {
        this.authScheme = authScheme;
    }
    
    public String authParam() {
        return this.authParam;
    }
    
    public void setAuthParam(String authParam) {
        this.authParam = authParam;
    }
    
    public List<NameValuePair> extraAuthParams() {
        return this.extraAuthParams;
    }
    
    public void addExtraAuthParam(String name, String value) {
        this.extraAuthParams.add(new BasicNameValuePair(name, value));
    }
    
    public void addExtraAuthParams(List<NameValuePair> extraAuthParams) {
        this.extraAuthParams.addAll(extraAuthParams);
    }
    
    public String authHeader() {
        return this.authHeader;
    }
    
    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }
    
    public String authHeaderFormat(Map<String, String> replacements) {
        return Utils.interpolateString(this.authHeaderFormat, replacements);
    }
    
    public void setAuthHeaderFormat(String format) {
        this.authHeaderFormat = format;
    }
    
    public String nonceHeader() {
        return String.format("X-%s-Nonce", this.authScheme);
    }
    
    public String alternateDateHeader() {
        return String.format("X-%s-Date", this.authScheme);
    }
    
    public boolean isQueryBased() {
        return this.isQueryBased;
    }
    
    public void setQueryBased(boolean queryBased) {
         this.isQueryBased = queryBased;
    }
    
    public boolean useAlternateDateHeader() {
        return this.useAlternateDateHeader;
    }
    
    public void setUseAlternateDateHeader(boolean useAlternateDateHeader) {
        this.useAlternateDateHeader = useAlternateDateHeader;
    }
}
