package com.asquera.hmac;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

class WardenHMacSigner {
    
    private final Mac algorithm;
    
    public WardenHMacSigner() throws NoSuchAlgorithmException {
        this(Mac.getInstance("HmacSHA1"));
    }
    
    public WardenHMacSigner(final Mac algorithm) {
        this.algorithm = algorithm;
    }
    
    public RequestInfo signRequest(final String url, final String secret, final RequestParams options)
            throws URISyntaxException, InvalidKeyException, NoSuchAlgorithmException {
        
        RequestInfo request = new RequestInfo(url, options);
        String signature = generateSignature(secret, request);
        
        if (options.isQueryBased()) {
            
            List<NameValuePair> auth_params = new ArrayList<NameValuePair>(options.extraAuthParams());
            auth_params.add(new BasicNameValuePair("date", request.dateAsString()));
            auth_params.add(new BasicNameValuePair("signature", signature));
            
            if (!options.nonce().isEmpty()) {
                auth_params.add(new BasicNameValuePair("nonce", options.nonce()));
            }
            
            request.query().add(options.authParam(), auth_params);
        
        } else {
            
            List<NameValuePair> headers = new ArrayList<NameValuePair>();
            
            Map<String, String> map = new HashMap<String, String>();
            for (NameValuePair pair : options.extraAuthParams()) {
                map.put(pair.getName(), pair.getValue());
            }
            map.put("signature", signature);
            
            headers.add(new BasicNameValuePair("auth_header", options.authHeaderFormat(map)));
            if (!options.nonce().isEmpty()) {
                headers.add(new BasicNameValuePair("nonce", options.nonce()));
            }
            
            if (options.useAlternateDateHeader()) {
                headers.add(new BasicNameValuePair(options.alternateDateHeader(), request.dateAsString()));
            } else {
                headers.add(new BasicNameValuePair("Date", request.dateAsString()));
            }
        }
        
        return request;
    }
    
    public String signUrl(final String url, final String secret, final RequestParams options)
            throws InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
        
        options.setQueryBased(true);
        RequestInfo request = signRequest(url, secret, options);
        return request.url();
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

