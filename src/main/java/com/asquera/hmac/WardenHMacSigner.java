package com.asquera.hmac;

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
    
    public WardenHMacSigner() throws NoSuchAlgorithmException {
        this(Mac.getInstance("HmacSHA1"));
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
    
    public String signRequest(final String url, final String secret, final Map<String, Object> inputOptions)
            throws URISyntaxException, InvalidKeyException, NoSuchAlgorithmException {
        
        Map<String, Object> options = new HashMap<String, Object>(this.defaultOptions);
        options.putAll(inputOptions);
        
        RequestInfo request = new RequestInfo(url, options);
        String signature = generateSignature(secret, request);
        
        return "";
    }
    
    public String signUrl(final String url, final String secret, final Map<String, Object> options) {
        if (options.containsKey("query_based")) {
            options.remove("query_based");
        }
        options.put("query_based", new Boolean(true));
        
        return "";
    }
    
    protected String generateSignature(final String secret, final RequestInfo request) throws InvalidKeyException, NoSuchAlgorithmException {
        return hash_hmac(request.canonicalRepresentation(), secret);
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

