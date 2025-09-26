package com.example.myapp.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="genders")
public class Genders {
    @Id
    @GeneratedValue
    @Column(name="gender_id")
    private int genderId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(mappedBy = "orientations")
    @JsonIgnore
    private Set<UserInfo> users = new HashSet<>();

    public Genders(Gender gender) {
        this.gender = gender;
        this.users = new HashSet<>();
    }
}