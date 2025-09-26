package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="swipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Swipes {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name="swiper_id", referencedColumnName="user_id")
    private UserInfo swiper;

    @ManyToOne
    @JoinColumn(name="swipee_id", referencedColumnName="user_id")
    private UserInfo swipee;

    private Boolean liked;
}
