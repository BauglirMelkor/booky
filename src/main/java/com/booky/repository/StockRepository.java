package com.booky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.booky.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

}