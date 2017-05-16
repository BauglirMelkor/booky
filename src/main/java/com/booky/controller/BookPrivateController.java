package com.booky.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booky.dto.BookDTO;
import com.booky.entity.Book;
import com.booky.exception.BookNotFoundException;
import com.booky.service.BookService;


@RestController
@RequestMapping("/book/private")
public class BookPrivateController {

    private final Logger log = LoggerFactory.getLogger(BookPrivateController.class);

    private final BookService bookService;

    public BookPrivateController(BookService bookService) {
	this.bookService = bookService;
    }

    @PostMapping
    public BookDTO createBook(@RequestBody BookDTO bookDTO)
	    throws BookNotFoundException, InterruptedException, ExecutionException {
	log.debug("Creating book with the name {}", bookDTO.getName());
	Future<Book> book = bookService.createBook(bookDTO);
	while (!book.isDone()) {
	    Thread.sleep(10);
	}
	return new BookDTO(book.get());

    }

    @PutMapping
    public BookDTO updateBook(@RequestBody BookDTO bookDTO)
	    throws BookNotFoundException, InterruptedException, ExecutionException {
	log.debug("Updating book with the name {}", bookDTO.getName());
	Future<Book> book = bookService.updateBook(bookDTO);
	while (!book.isDone()) {
	    Thread.sleep(10);
	}
	return new BookDTO(book.get());

    }
    
    @DeleteMapping("/{id}")
    public BookDTO deleteBook(@PathVariable Long id)
	    throws BookNotFoundException, InterruptedException, ExecutionException {
	log.debug("Deleting book with the id {}", id);
	Future<Book> book = bookService.deleteBook(id);
	while (!book.isDone()) {
	    Thread.sleep(10);
	}
	return new BookDTO(book.get());

    }
}