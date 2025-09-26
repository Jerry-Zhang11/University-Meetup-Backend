package com.example.myapp.controller;

import com.example.myapp.dto.UserAgeDto;
import com.example.myapp.dto.UserGenderDto;
import com.example.myapp.dto.UserOrientationsDto;
import com.example.myapp.service.SignupService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RequestMapping("/signup")
@RestController
public class SignupController{

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);
    private final SignupService signupService;

    public SignupController(SignupService signupService){
        this.signupService = signupService;
    }

    @PostMapping("/gender")
    public ResponseEntity<?> saveUserGender(@RequestBody UserGenderDto input){
        try{
            signupService.saveUserGender(input);
            return ResponseEntity.ok("User gender saved");
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/age")
    public ResponseEntity<?> saveUserAge(@RequestBody UserAgeDto input){
        try{
            signupService.saveUserAge(input);
            return ResponseEntity.ok("User age updated");
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/orientations")
    public ResponseEntity<?> saveUserOrientations(@RequestBody UserOrientationsDto input){
        try{
            signupService.saveUserOrientations(input);
            return ResponseEntity.ok("User orientations updated");
        }
        catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/pics")
    public ResponseEntity<?> saveUserPics(@RequestParam("email") String email, @RequestParam("photos") MultipartFile[] photos){
        try{
            signupService.saveUserPics(email, photos);
            return ResponseEntity.ok("User pics updated");
        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}