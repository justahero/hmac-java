package com.asquera.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;

import sun.net.www.protocol.http.HttpURLConnection;

import com.asquera.hmac.Request;
import com.asquera.hmac.RequestParams;
import com.asquera.hmac.WardenHMacSigner;

public class Client {
    
    private final WardenHMacSigner signer;
    
    public Client(Mac mac) {
        this.signer = new WardenHMacSigner(mac);
    }
    
    public Request signRequest(String message, String secret) throws InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
        RequestParams options = new RequestParams();
        return signer.signRequest(message, secret, options);
    }
    
    public String signUrl(String message, String secret) throws InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
        return signer.signUrl(message, secret);
    }
    
    public String sendGetRequest(String targetUrl) throws Exception {
        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        connection.setDoInput(true);
        connection.setUseCaches(false);
        
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        
        String line = "";
        StringBuffer response = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }
        
        reader.close();
        return response.toString();
    }
    
    public static void main(String[] args) {
        try {
            Client client = new Client(Mac.getInstance("HmacMD5"));
            String signedUrl = client.signUrl("http://localhost:4567/test", "TESTSECRET");
            
            String response = client.sendGetRequest(signedUrl);
            System.out.println(response);
            
        } catch (Exception e) {
            System.out.println("failed to send request");
        }
    }
}

