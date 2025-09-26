package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user_pics")
public class UserPics {
    @Id
    @GeneratedValue
    private long id;

    @Column(name="photo_url")
    private String photoUrl;

    @Column(name="original_filename")
    private String originalFilename;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_info_id", referencedColumnName="user_id")
    private UserInfo userInfo;
}
