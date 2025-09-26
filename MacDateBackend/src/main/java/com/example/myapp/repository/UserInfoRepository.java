package com.example.myapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.myapp.model.UserInfo;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo,Long>{
    Optional<UserInfo> findByUserEmail(String email);
}