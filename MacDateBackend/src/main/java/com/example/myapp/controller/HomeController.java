package com.example.myapp.controller;

import com.example.myapp.response.HomeResponse;
import com.example.myapp.service.HomeService;
import com.example.myapp.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;


@RequestMapping("/home")
@RestController
public class HomeController {

    private final HomeService homeService;
    private final JwtService jwtService;

    public HomeController(HomeService homeService, JwtService jwtService) {
        this.homeService = homeService;
        this.jwtService = jwtService;
    }

    @GetMapping("/load")
    public ResponseEntity<?> loadHomePage(HttpServletRequest request){
        try{
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String userEmail = jwtService.extractUsername(token);
                return ResponseEntity.ok(homeService.LoadUsers(userEmail));
            }
            else{
                throw new Exception("Token not found");
            }

        }
        catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
