package com.SHAudit.singHealthAudit.jwt;
import java.io.Serializable;

public class JWTTokenResponse implements Serializable {

    private static final long serialVersionUID = 8317676219297719109L;

    private final String token;

    public JWTTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}