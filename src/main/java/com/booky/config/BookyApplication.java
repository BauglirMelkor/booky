package com.booky.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class BookyApplication {
	
	@RequestMapping("/")
    String hello() {
        return "hello world";
    }

	public static void main(String[] args) {
		SpringApplication.run(BookyApplication.class, args);
	}
}
