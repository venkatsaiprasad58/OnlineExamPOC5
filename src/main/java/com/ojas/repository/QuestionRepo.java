package com.ojas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.ojas.model.Question;



@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer> {

}