package com.example.localcooking_v3t.model;

public class GoogleLoginRequest {
    private String idToken;
    
    public GoogleLoginRequest(String idToken) {
        this.idToken = idToken;
    }
    
    public String getIdToken() {
        return idToken;
    }
    
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
