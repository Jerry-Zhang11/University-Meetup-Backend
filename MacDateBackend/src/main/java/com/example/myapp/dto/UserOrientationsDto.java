package com.example.myapp.dto;

import java.util.HashSet;
import java.util.Set;
import com.example.myapp.model.Gender;
import lombok.Data;

@Data
public class UserOrientationsDto{
    private String email;
    private Set<Gender> orientations = new HashSet<>();

    public void addOrientations(Gender gender){
        orientations.add(gender);
    }
}