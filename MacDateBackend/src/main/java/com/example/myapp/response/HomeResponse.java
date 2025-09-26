package com.example.myapp.response;

import com.example.myapp.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomeResponse {
    private String username;
    private int age;
    private Gender gender;
    private Set<String> photoUrls;
    private Set<String> orientations;
}
