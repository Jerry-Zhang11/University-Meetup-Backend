package com.example.myapp.response;

import lombok.Data;

@Data
public class LoginResponse{
    private String token;
    private long expiresIn;
    private Boolean isNewUser;

    public LoginResponse(String token, long expiresIn, Boolean isNewUser){
        this.token = token;
        this.expiresIn = expiresIn;
        this.isNewUser = isNewUser;
    }
}
