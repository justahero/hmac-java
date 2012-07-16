package com.asquera.hmac;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class WardenHMacSigner {
    
    private final Map<String, Object> defaultOptions;
    
    private final String algorithm;
    private final String defaultAuthScheme;
    private final Map<String, Object> options;
    
    private static Map<String, Object> createDefaultOptions(final String scheme) {
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
        this.defaultOptions = createDefaultOptions(defaultAuthScheme);
        this.algorithm = algorithm;
        this.options = new HashMap<String, Object>(defaultOptions);
        this.options.putAll(options);
    }
    
}
