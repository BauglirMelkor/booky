package com.booky.test.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

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
import com.booky.service.BookService;
import com.booky.service.CategoryService;

@RunWith(SpringRunner.class)
public class BookServiceTest {

	@MockBean
	private BookRepository bookRepository;

	@MockBean
	private CategoryService categoryService;

	@MockBean
	private StockRepository stockRepository;

	private BookService bookService;

	private Optional<Book> bookOptional;

	private Book book = null;

	@Before
	public void setUp() throws Exception {
		bookService = spy(new BookService(bookRepository, categoryService, stockRepository));
		book = new Book();
		book.setId(1L);
		book.setName("Turing's Cathedral");
		Category category = new Category();
		category.setName("Science");
		book.setCategory(category);
		book.setPrice(10d);
		bookOptional = Optional.of(book);
	}

	@Test
	public void orderBookSuccessful() throws InterruptedException, ExecutionException {
		List<BasketDTO> basketDTOList = new ArrayList<BasketDTO>();
		BasketDTO basketDTO = new BasketDTO();
		BookDTO bookDTO = new BookDTO();
		bookDTO.setId(1L);
		bookDTO.setIsbn(1111111111L);
		bookDTO.setName("Vampire Of the Mists");
		basketDTO.setBook(bookDTO);
		basketDTO.setQuantity(3);
		basketDTOList.add(basketDTO);
		basketDTOList.add(basketDTO);
		when(stockRepository.save(any(Stock.class))).thenReturn(new Stock());
		when(bookRepository.findOne(bookDTO.getId())).thenReturn(book);
		Future<List<StockDTO>> stockFuture = bookService.orderBook(basketDTOList);
		assert (stockFuture.get().get(0).getSold() == 6);
	}

	@Test
	public void createBookSuccessful() throws InterruptedException, ExecutionException {
		when(bookRepository.save(book)).thenReturn(book);
		when(bookRepository.findOneByName(any())).thenReturn(Optional.ofNullable(null));
		when(bookRepository.findOneByIsbn(book.getIsbn())).thenReturn(Optional.ofNullable(null));
		Future<Book> bookFuture = bookService.createBook(new BookDTO(book));
		assert (bookFuture.get().getName().equals("Turing's Cathedral"));
	}

	@Test(expected = BookAlreadyExistsException.class)
	public void createBookBookAlreadyExistsByName() throws InterruptedException, ExecutionException {
		when(bookRepository.save(book)).thenReturn(book);
		when(bookRepository.findOneByName(any())).thenReturn(bookOptional);
		when(bookRepository.findOneByIsbn(book.getIsbn())).thenReturn(bookOptional);
		bookService.createBook(new BookDTO(book));

	}

	@Test
	public void updateBookSuccessful() throws InterruptedException, ExecutionException {
		when(bookRepository.save(book)).thenReturn(book);
		when(bookRepository.findOne(book.getId())).thenReturn(book);
		Future<Book> bookFuture = bookService.updateBook(new BookDTO(book));
		assert (bookFuture.get().getName().equals("Turing's Cathedral"));
	}

	@Test(expected = BookNotFoundException.class)
	public void updateBookBookNotFound() throws InterruptedException, ExecutionException {
		when(bookRepository.save(book)).thenReturn(book);
		when(bookRepository.findOne(book.getId())).thenReturn(null);
		bookService.updateBook(new BookDTO(book));

	}

}
