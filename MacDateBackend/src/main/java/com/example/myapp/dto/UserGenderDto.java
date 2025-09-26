package com.example.myapp.dto;

import com.example.myapp.model.Gender;
import lombok.Data;

@Data
public class UserGenderDto{
    private String email;
    private Gender gender;
}