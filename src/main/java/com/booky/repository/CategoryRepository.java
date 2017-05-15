package com.booky.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booky.entity.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findOneByName(String name);

}