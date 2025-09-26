package com.example.myapp.repository;

import com.example.myapp.model.Swipes;
import com.example.myapp.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwipesRepository extends JpaRepository<Swipes, Long> {

    @Query("SELECT DISTINCT s.swipee.userId FROM Swipes s WHERE s.swiper.userId = :swiperId")
    List<Long> findSwipedUserIdsBySwiper(@Param("swiperId") Long swiperId);

    @Query("""
        SELECT ui FROM UserInfo ui 
        WHERE ui.userId != :currentUserId 
        AND ui.userId NOT IN :swipedUserIds 
        AND ui.gender IN (
            SELECT g.gender FROM Genders g WHERE g IN :orientations
        )   
        """)
    List<UserInfo> findEligibleUsers(
            @Param("currentUserId") Long currentUserId,
            @Param("swipedUserIds") List<Long> swipedUserIds,
            @Param("orientations") java.util.Set<com.example.myapp.model.Genders> orientations
    );
}
