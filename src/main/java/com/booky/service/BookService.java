package com.booky.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.booky.dto.StockDTO;
import com.booky.entity.Book;
import com.booky.entity.Category;
import com.booky.entity.Stock;
import com.booky.exception.BookAlreadyExistsException;
import com.booky.exception.BookNotFoundException;
import com.booky.repository.BookRepository;
import com.booky.repository.StockRepository;

@Service
@Transactional
public class BookService {

	private final Logger log = LoggerFactory.getLogger(BookService.class);

	private final BookRepository bookRepository;

	private final CategoryService categoryService;
	
	private final StockRepository stockRepository;

	public BookService(BookRepository bookRepository, CategoryService categoryService,StockRepository stockRepository) {
		this.bookRepository = bookRepository;
		this.categoryService = categoryService;
		this.stockRepository=stockRepository;
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
		
		try{
			bookRepository.save(book);
		}catch(Exception e){
			log.error(e.getMessage());
		}

		return new AsyncResult<>(book);
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
	public Future<List<StockDTO>> orderBook(List<BasketDTO> basketDTOList) {
		
		Map<BasketDTO, Long> result =
				basketDTOList.stream().collect(
                Collectors.groupingBy(
                        Function.identity(), Collectors.counting()
                )
        );
		List<StockDTO> stockDTOList=new ArrayList<StockDTO>();
		for(BasketDTO basketDTO:basketDTOList){
			Long amount=result.get(basketDTO);
			if(amount!=null){
			Stock stock=new Stock();
			Book book=bookRepository.findOne(basketDTO.getBook().getId());
			stock.setBook(book);
			stock.setSold(Integer.parseInt(amount.toString()));
			stock.setDate(new Date());
			stockRepository.save(stock);
			stockDTOList.add(new StockDTO(stock));
			result.remove(basketDTO);
			}
		}
		
		return new AsyncResult<>(stockDTOList);
	}

	public Page<Book> getBookByCategoryName(Pageable pageable, String categoryName) {
		return bookRepository.findByCategoryName(pageable, categoryName);
	}

}
