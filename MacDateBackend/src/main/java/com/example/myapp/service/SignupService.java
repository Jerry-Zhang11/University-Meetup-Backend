package com.example.myapp.service;

import java.util.Optional;

import com.example.myapp.dto.UserAgeDto;
import com.example.myapp.dto.UserGenderDto;
import com.example.myapp.dto.UserOrientationsDto;
import com.example.myapp.model.Gender;
import com.example.myapp.model.Genders;
import com.example.myapp.model.User;
import com.example.myapp.model.UserInfo;
import com.example.myapp.model.UserPics;
import com.example.myapp.repository.GendersRepository;
import com.example.myapp.repository.UserInfoRepository;
import com.example.myapp.repository.UserPicsRepository;
import com.example.myapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.io.IOException;


@Service
public class SignupService{
    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final GendersRepository gendersRepository;
    private final UserPicsRepository userPicsRepository;

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Value("${file.upload.web-path}")
    private String webPath;

    public SignupService(UserInfoRepository userInfoRepository, UserRepository userRepository, GendersRepository gendersRepository, UserPicsRepository userPicsRepository){
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
        this.gendersRepository = gendersRepository;
        this.userPicsRepository = userPicsRepository;
    }

    @Transactional
    public void saveUserGender(UserGenderDto input){
        Optional<User> maybeUser = userRepository.findByEmail(input.getEmail());
        if(maybeUser.isPresent()){
            User user = maybeUser.get();
            Optional<UserInfo> maybeUserInfo = userInfoRepository.findByUserEmail(input.getEmail());
            if(maybeUserInfo.isPresent()){
                UserInfo userInfo = maybeUserInfo.get();
                userInfo.setGender(input.getGender());
                userInfoRepository.save(userInfo);
            }
            else{
                UserInfo userInfo = new UserInfo();
                userInfo.setGender(input.getGender());
                userInfo.setUser(user);

                userInfoRepository.save(userInfo);
            }

        }
        else{
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    public void saveUserAge(UserAgeDto input){
        Optional<UserInfo> maybeUserInfo = userInfoRepository.findByUserEmail(input.getEmail());
        if(maybeUserInfo.isPresent()){
            UserInfo userInfo = maybeUserInfo.get();

            userInfo.setAge(input.getAge());
            userInfoRepository.save(userInfo);
        }
        else{
            throw new RuntimeException("User Info not found");
        }
    }

    @Transactional
    public void saveUserOrientations(UserOrientationsDto input){
        Optional<UserInfo> maybeUserInfo = userInfoRepository.findByUserEmail(input.getEmail());
        if(maybeUserInfo.isPresent()){
            UserInfo userInfo = maybeUserInfo.get();
            userInfo.removeAllOrientations();
            for (Gender gender : input.getOrientations()){
                Optional<Genders> maybeGender = gendersRepository.findByGender(gender);
                if(maybeGender.isPresent()){
                    Genders orientation = maybeGender.get();
                    userInfo.addOrientations(orientation);
                }
                else{
                    throw new RuntimeException("Enter correct Gender"); }
            }
            userInfoRepository.save(userInfo);
        }
        else{
            throw new RuntimeException("User Info not found");
        }
    }


    @Transactional
    public void saveUserPics(String email, MultipartFile[] photos) throws IOException{
        Optional<UserInfo> maybeUserInfo = userInfoRepository.findByUserEmail(email);
        Optional<User> maybeUser = userRepository.findByEmail(email);
        if(maybeUserInfo.isPresent() && maybeUser.isPresent()){
            UserInfo userInfo = maybeUserInfo.get();
            for (MultipartFile photo : photos){
                String filename = saveFile(photo);
                String originalFilename = photo.getOriginalFilename();
                UserPics userPics = new UserPics();
                String photoUrl = webPath + "/" + filename;

                userPics.setPhotoUrl(photoUrl);
                userPics.setOriginalFilename(originalFilename);
                userPics.setUserInfo(userInfo);
                userPicsRepository.save(userPics);

                User user = maybeUser.get();
                user.setIsNewUser(false);
                userRepository.save(user);
            }
        }
        else{
            throw new RuntimeException("User Info not found");
        }
    }

    public String saveFile(MultipartFile file) throws IOException {
        String uniqueFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(uniqueFilename);

        // Save the actual file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename;
    }



}
