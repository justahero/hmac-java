package com.asquera.hmac;

import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;

class WardenHMacSigner {
    
    private static final Map<String, String> SupportedAlgorithms = getSupportedAlgorithmsMap();
    private static final String[] NecessaryParameters = { "method", "date", "nonce", "headers", "path", "query", };
    
    private final Map<String, Object> defaultOptions;
    
    private final String algorithm;
    private final String defaultAuthScheme;
    private final Map<String, Object> options;
    
    private static Map<String, String> getSupportedAlgorithmsMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sha1", "HmacSHA1");
        map.put("md5",  "HmacMD5");
        return Collections.unmodifiableMap(map);
    }

    private static Map<String, Object> getDefaultOptions(final String scheme) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("auth_scheme", "HMAC");
        map.put("auth_param", "auth");
        map.put("auth_header", "Authorization");
        map.put("auth_header_format", "%{auth_scheme} %{signature}");
        
        Map<String, String> schemeReplacements = new HashMap<String, String>();
        schemeReplacements.put("scheme", scheme);
        map.put("nonce_header", Utils.interpolateString("X-%{scheme}-Nonce", schemeReplacements));
        map.put("alternate_date_header", Utils.interpolateString("X-%{scheme}-Date", schemeReplacements));
        
        map.put("query_based", new Boolean(true));
        map.put("use_alternate_date_header", new Boolean(false));
        map.put("extra_auth_paths", new HashMap<String, Object>());
        
        return Collections.unmodifiableMap(map);
    }
    
    public WardenHMacSigner() {
        this("sha1", new HashMap<String, Object>());
    }
    
    public WardenHMacSigner(final String algorithm) {
        this(algorithm, new HashMap<String, Object>());
    }
    
    public WardenHMacSigner(final String algorithm, final Map<String, Object> options) {
        this.defaultAuthScheme = options.containsKey("auth_scheme") && options.get("auth_scheme") instanceof String 
                ? (String)options.get("auth_scheme")
                : "HMAC";
        this.defaultOptions = getDefaultOptions(defaultAuthScheme);
        this.algorithm = algorithm;
        this.options = new HashMap<String, Object>(defaultOptions);
        this.options.putAll(options);
    }
    
    public String signRequest(final String url, final String secret, final Map<String, Object> options) {
        return null;
    }
    
    public String canonicalRepresentation(final Map<String, String> params) throws IllegalArgumentException {
        if (params == null) {
            throw new IllegalArgumentException();
        }
        
        for (String neededParam : NecessaryParameters) {
            if (!params.containsKey(neededParam))
                throw new IllegalArgumentException(neededParam + " must be given!");
        }
        
        String result = "";
        return result;
    }
    
    protected String generateSignature(final Map<String, String> params)
            throws IllegalArgumentException, NoSuchAlgorithmException {
        if (params == null || !params.containsKey("secret")) {
            throw new IllegalArgumentException();
        }
        Map<String, String> inputParams = new HashMap<String, String>(params);
        String secret = params.get("secret");
        inputParams.remove("secret");
        
        Mac mac = getMacAlgorithm(this.algorithm);
        return null;
    }
    
    private static Mac getMacAlgorithm(final String algorithm) throws NoSuchAlgorithmException {
        if (!SupportedAlgorithms.containsKey(algorithm)) {
            throw new NoSuchAlgorithmException(algorithm + " algorithm not available");
        }
        return Mac.getInstance(SupportedAlgorithms.get(algorithm));
    }
}

