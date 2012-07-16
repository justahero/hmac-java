package com.asquera.hmac;

import java.util.HashMap;
import java.util.Map;

class WardenHMacSigner {
    
    private final String algorithm;
    private final Map<String, Object> defaultOptions;
    
    public WardenHMacSigner() {
        this("sha1", new HashMap<String, Object>());
    }
    
    public WardenHMacSigner(final String algorithm) {
        this(algorithm, new HashMap<String, Object>());
    }
    
    public WardenHMacSigner(final String algorithm, final Map<String, Object> options) {
        this.algorithm = algorithm;
        this.defaultOptions = options;
    }
}