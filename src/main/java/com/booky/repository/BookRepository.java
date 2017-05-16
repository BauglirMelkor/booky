package com.booky.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.booky.entity.Book;
import com.booky.entity.BookyUser;
import com.booky.entity.Category;

@Repository
public interface BookRepository  extends JpaRepository<Book, Long> {
    
    Optional<Book> findOneByIsbn(Long isbn);

    Page<Book> findAll(Pageable pageable);

    Optional<Book> findOneByName(String name);

    Page<Book> findOneByCategory(Pageable pageable,Category catefory);

    @Query("select p from Book p join p.category category where category.name=:categoryName")
    Page<Book> findByCategoryName(Pageable pageable, @Param(value="categoryName") String categoryName);

  

}