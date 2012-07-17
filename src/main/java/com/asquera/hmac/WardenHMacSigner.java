package com.asquera.hmac;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

class WardenHMacSigner {
    
    private final Mac algorithm;
    
    public WardenHMacSigner() throws NoSuchAlgorithmException {
        this(Mac.getInstance("HmacSHA1"));
    }
    
    public WardenHMacSigner(final Mac algorithm) {
        this.algorithm = algorithm;
    }
    
    public String signRequest(final String url, final String secret, final RequestParams options)
            throws URISyntaxException, InvalidKeyException, NoSuchAlgorithmException {
        
        RequestInfo request = new RequestInfo(url, options);
        String signature = generateSignature(secret, request);
        
        /*
        Map<String, Object> options = new HashMap<String, Object>(this.defaultOptions);
        options.putAll(inputOptions);
        
        RequestInfo request = new RequestInfo(url, options);
        String signature = generateSignature(secret, request);
        
        if (request.isQueryBased()) {
            Map<String, String> auth_params = new HashMap<String, String>();
        }
        */
        
        return "";
    }
/*
        if ($opts["query_based"]) {
            $auth_params = array_merge($opts["extra_auth_params"], array(
                "date" => $date,
                "signature" => $signature
            ));
            
            if (!empty( $opts["nonce"])) {
                $auth_params["nonce"] = $opts["nonce"];
            }
            
            $query_values[$opts["auth_param"]] = $auth_params;
            
        } else {
            
            $headers[$opts["auth_header"]]   = $this->interpolateString($opts["auth_header_format"], array_merge($opts, $opts["extra_auth_params"], array("signature" => $signature)));
            if (!empty($opts["nonce"])) {
                $headers[$opts["nonce_header"]]  = $opts["nonce"];
            }
            
            if (!empty($opts["use_alternate_date_header"])) {
                $headers[$opts["use_alternate_date_header"]] = $date;
            } else {
                $headers["Date"] = $date;
            }
        }
 */
    
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

