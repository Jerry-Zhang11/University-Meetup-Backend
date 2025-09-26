package com.example.myapp.service;

import com.example.myapp.dto.RegisterUserDto;
import com.example.myapp.dto.VerifyUserDto;
import com.example.myapp.model.User;
import com.example.myapp.repository.UserRepository;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

import jakarta.mail.MessagingException;

import com.example.myapp.dto.LoginUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


@Service
public class AuthenticationService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
    }

    public User SignUp(RegisterUserDto input){
        User user = new User(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(getVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        user.setIsNewUser(true);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    public User Authenticate(LoginUserDto input){
        User user = userRepository.findByEmail(input.getEmail()).orElseThrow(()->new RuntimeException("User not found"));
        if(!user.isEnabled()){
            throw new RuntimeException("User is not verified");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword()));
        return user;
    }

    public void VerifyUser(VerifyUserDto input){
        Optional<User> maybeUser = userRepository.findByEmail(input.getEmail());
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            if(user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())){
                throw new RuntimeException("Verification code expired");
            }

            if(user.getVerificationCode().equals(input.getVerificationCode())){
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationCodeExpiresAt(null);
                userRepository.save(user);
            }
            else{
                throw new RuntimeException("Invalid verification code");
            }
        }
        else{
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email){
        Optional<User> maybeUser = userRepository.findByEmail(email);
        if(maybeUser.isPresent()){
            User user = maybeUser.get();

            if(user.isEnabled()){
                throw new RuntimeException("User already verified");
            }

            user.setVerificationCode(getVerificationCode());
            user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
            sendVerificationEmail(user);
            userRepository.save(user);
        }
        else{
            throw new RuntimeException("User not found");
        }
    }

    public void sendVerificationEmail(User user){
        String subject = "Verify your email";
        String text = user.getVerificationCode();
        try{
            emailService.sendVerificationEmail(user.getEmail(), subject, text);
        }
        catch(MessagingException e){
            e.printStackTrace();
        }
    }

    private String getVerificationCode(){
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

}