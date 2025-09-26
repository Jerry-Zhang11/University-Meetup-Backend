package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="match")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name="user1_id", referencedColumnName="user_id")
    private UserInfo user1;

    @ManyToOne
    @JoinColumn(name="user2_id", referencedColumnName="user_id")
    private UserInfo user2;
}
