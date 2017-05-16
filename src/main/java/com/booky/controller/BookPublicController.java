package com.booky.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booky.dto.BookDTO;
import com.booky.entity.Book;
import com.booky.exception.BookNotFoundException;
import com.booky.service.BookService;
import com.wordnik.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/book/public")
public class BookPublicController {

    private final Logger log = LoggerFactory.getLogger(BookPublicController.class);

    private final BookService bookService;

    public BookPublicController(BookService bookService) {
	this.bookService = bookService;
    }

    @GetMapping
    public List<BookDTO> bookList(@ApiParam Pageable pageable) throws BookNotFoundException {
	log.debug("Getting books");
	Page<Book> bookPage = bookService.getBooks(pageable);
	return bookListToBookDTOList(bookPage);

    }

    @GetMapping("/bookByIsbn/{isbn}")
    public BookDTO bookByIsbn(@PathVariable Long isbn) throws BookNotFoundException {
	log.debug("Getting book with isbn {}", isbn);
	return new BookDTO(bookService.getBookByIsbn(isbn));

    }

    @GetMapping("/bookByName/{name}")
    public BookDTO bookByName(@PathVariable String name) throws BookNotFoundException {
	log.debug("Getting book by name {}", name);
	return new BookDTO(bookService.getBookByName(name));

    }
    
    @GetMapping("/bookByCategory/{categoryName}")
    public List<BookDTO> bookByCategoryName(@ApiParam Pageable pageable,@PathVariable String categoryName) throws BookNotFoundException {
	log.debug("Getting book by category name {}", categoryName);
	Page<Book> bookPage = bookService.getBookByCategoryName(pageable, categoryName);
	return bookListToBookDTOList(bookPage);

    }
    
    public List<BookDTO> bookListToBookDTOList(Page<Book> bookPage){
    List<BookDTO> bookDTOList = new ArrayList<BookDTO>();
	if (bookPage.getContent() != null && bookPage.getContent().size() > 0) {
	    for (Book book : bookPage.getContent()) {
		bookDTOList.add(new BookDTO(book));
	    }
	    return bookDTOList;
	} else {
	    throw new BookNotFoundException("book list not found");
	}
    }

}