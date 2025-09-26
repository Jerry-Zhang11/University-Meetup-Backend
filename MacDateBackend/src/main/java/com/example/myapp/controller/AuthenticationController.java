package com.example.myapp.controller;

import com.example.myapp.dto.LoginUserDto;
import com.example.myapp.dto.RegisterUserDto;
import com.example.myapp.dto.VerifyUserDto;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import com.example.myapp.model.User;
import com.example.myapp.response.LoginResponse;
import com.example.myapp.service.JwtService;
import com.example.myapp.service.AuthenticationService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController{
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService){
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> SignUp(@RequestBody RegisterUserDto input){
        User registerUser = authenticationService.SignUp(input);
        return ResponseEntity.ok(registerUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> Authenticate(@RequestBody LoginUserDto input){
        User authenticateUser = authenticationService.Authenticate(input);
        Boolean isNewUser = authenticateUser.getIsNewUser();
        String token = jwtService.generateToken(authenticateUser);
        LoginResponse loginResponse = new LoginResponse(token, jwtService.getExpirationTime(), isNewUser);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> VerifyUser(@RequestBody VerifyUserDto input){
        try{
            authenticationService.VerifyUser(input);
            return ResponseEntity.ok("User verified");
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/resend")
    public ResponseEntity<?> ResendVerificationCode(@RequestParam String email){
        try{
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code resent");
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}