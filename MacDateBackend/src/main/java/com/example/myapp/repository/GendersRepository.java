package com.example.myapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.example.myapp.model.Gender;
import com.example.myapp.model.Genders;

@Repository
public interface GendersRepository extends CrudRepository<Genders,Integer>{
    Optional<Genders> findByGender(Gender gender);
}