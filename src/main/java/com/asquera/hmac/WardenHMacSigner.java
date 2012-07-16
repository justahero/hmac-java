package com.asquera.hmac;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class WardenHMacSigner {
    
    private final String algorithm;
    private final Map<String, Object> options;
    private static final Map<String, Object> defaultOptions = createDefaultMap();
    
    private static Map<String, Object> createDefaultMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("auth_scheme", "HMAC");
        map.put("auth_param", "auth");
        map.put("auth_header", "Authorization");
        map.put("auth_header_format", "%{auth_scheme} %{signature}");
        //map.put("nonce_header", interpolateString());
        return Collections.unmodifiableMap(map);
    }
    
    public WardenHMacSigner() {
        this("sha1", new HashMap<String, Object>());
    }
    
    public WardenHMacSigner(final String algorithm) {
        this(algorithm, new HashMap<String, Object>());
    }
    
    public WardenHMacSigner(final String algorithm, final Map<String, Object> options) {
        this.algorithm = algorithm;
        this.options = new HashMap<String, Object>(defaultOptions);
        this.options.putAll(options);
    }
}
