package com.booky.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booky.dto.BasketDTO;
import com.booky.dto.BookDTO;
import com.booky.entity.Book;
import com.booky.entity.Category;
import com.booky.exception.BookAlreadyExistsException;
import com.booky.exception.BookNotFoundException;
import com.booky.repository.BookRepository;

@Service
@Transactional
public class BookService {

	private final Logger log = LoggerFactory.getLogger(BookService.class);

	private final BookRepository bookRepository;

	private final CategoryService categoryService;

	public BookService(BookRepository bookRepository, CategoryService categoryService) {
		this.bookRepository = bookRepository;
		this.categoryService = categoryService;
	}

	public Book getBookByIsbn(Long isbn) throws BookNotFoundException {
		log.debug("Finding book by isbn number {}", isbn);
		Optional<Book> book = bookRepository.findOneByIsbn(isbn);
		if (book.isPresent()) {
			return book.get();
		} else {
			throw new BookNotFoundException("book not found with isbn " + isbn);
		}

	}

	public Book getBookById(Long id) throws BookNotFoundException {
		log.debug("Finding book by id  {}", id);
		Book book = bookRepository.findOne(id);
		if (book != null) {
			return book;
		} else {
			throw new BookNotFoundException("book not found with id " + id);
		}

	}

	public Book getBookByName(String name) {
		log.debug("Finding book by name  {}", name);
		Optional<Book> book = bookRepository.findOneByName(name);
		if (book.isPresent()) {
			return book.get();
		} else {
			throw new BookNotFoundException("book not found with name " + name);
		}
	}

	public Page<Book> getBooks(Pageable pageable) {
		log.debug("Finding books");
		Page<Book> bookPage = bookRepository.findAll(pageable);
		if (bookPage.getContent() == null || bookPage.getContent().size() == 0) {
			throw new BookNotFoundException("book list not found");
		}
		return bookPage;
	}

	@Async
	public Future<Book> createBook(BookDTO bookDTO) {
		Book book = null;
		try {
			if (getBookByIsbn(bookDTO.getIsbn()) != null) {
				throw new BookAlreadyExistsException("Book with isbn " + bookDTO.getIsbn() + " already exists");
			} else if (getBookByName(bookDTO.getName()) != null) {
				throw new BookAlreadyExistsException("Book with name " + bookDTO.getName() + " already exists");
			} else {
				book = new Book(bookDTO);
			}
		} catch (BookNotFoundException e) {
			book = new Book(bookDTO);
		}

		if (bookDTO.getCategory() != null) {
			Category category = categoryService.getCategoryById(bookDTO.getCategory().getId());
			book.setCategory(category);
		}

		return new AsyncResult<>(bookRepository.save(book));
	}

	@Async
	public Future<Book> updateBook(BookDTO bookDTO) {
		Book book = getBookById(bookDTO.getId());
		if (book == null) {
			throw new BookNotFoundException("Book with id " + bookDTO.getId() + " cannot be found");
		} else {
			if (bookDTO.getCategory() != null) {
				Category category = categoryService.getCategoryById(bookDTO.getCategory().getId());
				book.setCategory(category);
			}
			book.setIsbn(bookDTO.getIsbn());
			book.setName(bookDTO.getName());

			return new AsyncResult<Book>(bookRepository.save(book));

		}
	}

	@Async
	public Future<Book> deleteBook(Long id) {
		Book book = getBookById(id);
		if (book == null) {
			throw new BookNotFoundException("Book with id " + id + " cannot be found");
		} else {
			bookRepository.delete(book);
			return new AsyncResult<Book>(book);

		}
	}
	
	@Async
	public Future<List<BasketDTO>> orderBook(List<BasketDTO> basketDTO) {
		return null;
	}

	public Page<Book> getBookByCategoryName(Pageable pageable, String categoryName) {
		return bookRepository.findByCategoryName(pageable, categoryName);
	}

}
