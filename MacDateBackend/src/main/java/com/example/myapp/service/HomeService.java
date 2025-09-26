package com.example.myapp.service;

import com.example.myapp.model.Genders;
import com.example.myapp.model.User;
import com.example.myapp.model.UserInfo;
import com.example.myapp.model.UserPics;
import com.example.myapp.repository.SwipesRepository;
import com.example.myapp.repository.UserInfoRepository;
import com.example.myapp.response.HomeResponse;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HomeService {

    private final SwipesRepository swipesRepository;
    private final UserInfoRepository userInfoRepository;

    public HomeService(SwipesRepository swipesRepository, UserInfoRepository userInfoRepository) {
        this.swipesRepository = swipesRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public List<HomeResponse> LoadUsers(String userEmail) {
        Optional<UserInfo> maybeUserInfo = userInfoRepository.findByUserEmail(userEmail);

        if(maybeUserInfo.isPresent()){
            UserInfo userInfo = maybeUserInfo.get();
            long userId = userInfo.getUserId();
            Set<Genders> orientations = userInfo.getOrientations();

            // Handle empty orientations
            if (orientations.isEmpty()) {
                return new ArrayList<>(); // Return empty list if no orientations set
            }

            List<Long> swipedUsers = swipesRepository.findSwipedUserIdsBySwiper(userId);

            // Handle empty swipedUsers list for query compatibility
            if (swipedUsers.isEmpty()) {
                swipedUsers = List.of(-1L); // dummy ID that won't match any real user
            }

            List<UserInfo> usersToLoad = swipesRepository.findEligibleUsers(userId, swipedUsers, orientations);
            List<HomeResponse> homeResponses = new ArrayList<>();

            // Load up to 5 users, but don't exceed available users
            int maxUsers = Math.min(5, usersToLoad.size());

            for(int i = 0; i < maxUsers; i++){
                UserInfo userToLoad = usersToLoad.get(i);

                // Null check
                if (userToLoad.getUser() == null) {
                    continue; // Skip this user if User entity is null
                }

                User user = userToLoad.getUser();
                String username = user.getUsername();

                Set<String> userPhotos = new HashSet<>();
                Set<String> userOrientations = new HashSet<>();
                for(UserPics userPics : userToLoad.getUserPics()){
                    userPhotos.add(userPics.getPhotoUrl());
                }
                for(Genders orientation : userToLoad.getOrientations()){
                    userOrientations.add(orientation.getGender().toString());
                }
                HomeResponse homeResponse = new HomeResponse(username, userToLoad.getAge(), userToLoad.getGender(), userPhotos, userOrientations);
                homeResponses.add(homeResponse);
            }

            return homeResponses;
        }
        else{
            throw new RuntimeException("User Info not found");
        }
    }
}
