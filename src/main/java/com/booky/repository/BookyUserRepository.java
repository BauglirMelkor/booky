package com.booky.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booky.entity.BookyUser;

@Repository
public interface BookyUserRepository extends JpaRepository<BookyUser, Long> {

	Optional<BookyUser> findOneByEmail(String email);

	Page<BookyUser> findAll(Pageable pageable);
}