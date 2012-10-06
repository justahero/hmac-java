package com.asquera.hmac;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.uri.NameValuePair;

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
    
    private final Vector<NameValuePair> headers = new Vector<NameValuePair>();
    private final Vector<NameValuePair> extraAuthParams = new Vector<NameValuePair>();
    
    public RequestParams() {
    }
    
    public RequestParams(Date date) {
        this();
        this.date = date;
    }
    
    public RequestParams(RequestParams options) {
        date             = new Date(options.date.getTime());
        nonce            = options.nonce;
        method           = options.method;
        authScheme       = options.authScheme;
        authParam        = options.authParam;
        authHeader       = options.authHeader;
        authHeaderFormat = options.authHeaderFormat;
        isQueryBased     = options.isQueryBased;
        useAlternateDateHeader = options.useAlternateDateHeader;
        
        headers.clear();
        headers.addAll(options.headers);
        extraAuthParams.clear();
        extraAuthParams.addAll(options.extraAuthParams);
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
    
    public String dateAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        return (dateFormat.format(date()) + " GMT");
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public void setDate(int year, int month, int day, int hours, int minutes, int seconds) {
        Calendar calendar = new GregorianCalendar(year, month, day, hours, minutes, seconds);
        Date date = calendar.getTime();
        setDate(date);
    }
    
    public List<NameValuePair> headers() {
        Collections.sort(this.headers);
        return this.headers;
    }
    
    public void addHeader(String name, String value) {
        this.headers.add(new NameValuePair(name, value));
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
        this.extraAuthParams.add(new NameValuePair(name, value));
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
        Map<String, String> map = new HashMap<String, String>();
        map.put("auth_scheme", authScheme());
        for (NameValuePair pair : extraAuthParams()) {
            map.put(pair.key, pair.value);
        }
        map.putAll(replacements);
        
        return Utils.interpolateString(this.authHeaderFormat, map);
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
