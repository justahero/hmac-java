package com.asquera.hmac;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    
    private final Map<String, String> headers = new HashMap<String, String>();
    private final Map<String, String> extraAuthParams = new HashMap<String, String>();
    
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
    
    public Map<String, String> headers() {
        return this.headers;
    }
    
    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }
    
    public void addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }
    
    public void clearHeaders() {
        this.headers.clear();
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
    
    public Map<String, String> extraAuthParams() {
        return this.extraAuthParams;
    }
    
    public void addExtraAuthParam(String name, String value) {
        this.extraAuthParams.put(name, value);
    }
    
    public void addExtraAuthParams(Map<String, String> extraAuthParams) {
        this.extraAuthParams.putAll(extraAuthParams);
    }
    
    public void clearExtraAuthParams() {
        this.extraAuthParams.clear();
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
