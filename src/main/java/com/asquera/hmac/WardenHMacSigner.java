package com.asquera.hmac;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

class WardenHMacSigner {
    
    private static final String[] NecessaryParameters = { "method", "date", "nonce", "headers", "path", "query", };
    
    private final Map<String, Object> defaultOptions;
    
    private final Mac algorithm;
    private final String defaultAuthScheme;
    private final Map<String, Object> options;
    
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
    
    public WardenHMacSigner(final Mac algorithm) {
        this(algorithm, new HashMap<String, Object>());
    }
    
    public WardenHMacSigner(final Mac algorithm, final Map<String, Object> options) {
        this.defaultAuthScheme = options.containsKey("auth_scheme") && options.get("auth_scheme") instanceof String 
                ? (String)options.get("auth_scheme")
                : "HMAC";
        this.defaultOptions = getDefaultOptions(defaultAuthScheme);
        this.algorithm = algorithm;
        this.options = new HashMap<String, Object>(defaultOptions);
        this.options.putAll(options);
    }
    
    public String canonicalRepresentation(final Map<String, Object> params) throws IllegalArgumentException {
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
    
    public String signRequest(final String url, final String secret, final Map<String, Object> inputOptions)
            throws MalformedURLException, URISyntaxException {
        
        Map<String, Object> options = new HashMap<String, Object>(this.defaultOptions);
        options.putAll(inputOptions);
        
        RequestInfo request = new RequestInfo(url, options);
        
        return "";
    }
    
    public String signUrl(final String url, final String secretKey, final Map<String, Object> options) {
        if (options.containsKey("query_based")) {
            options.remove("query_based");
        }
        options.put("query_based", new Boolean(true));
        
        return "";
    }
    
    protected String generateSignature(final Map<String, Object> params)
            throws IllegalArgumentException, NoSuchAlgorithmException, InvalidKeyException {
        if (params == null || !params.containsKey("secret")) {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> inputParams = new HashMap<String, Object>(params);
        String secret = (String)params.get("secret");
        inputParams.remove("secret");
        
        return hash_hmac(canonicalRepresentation(inputParams), secret);
    }
    
    private String hash_hmac(final String message, final String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(this.algorithm.getAlgorithm());
        byte[] keyBytes = secretKey.getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, mac.getAlgorithm());
        mac.init(keySpec);
        
        // the same as OpenSSL::HMAC.hexDigest
        byte[] rawHmac = mac.doFinal(message.getBytes());
        return Hex.encodeHexString(rawHmac);
    }
}

