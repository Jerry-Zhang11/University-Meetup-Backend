package com.example.myapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.myapp.model.UserPics;

@Repository
public interface UserPicsRepository extends CrudRepository<UserPics,Long>{
    Optional<UserPics> findById(long id);
}
