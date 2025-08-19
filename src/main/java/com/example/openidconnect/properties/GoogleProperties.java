package com.example.openidconnect.properties;

public class GoogleProperties {
    private String clientId = System.getenv("CLIENTID");
    private String clientSecret = System.getenv("CLIENTSECRET");

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
}