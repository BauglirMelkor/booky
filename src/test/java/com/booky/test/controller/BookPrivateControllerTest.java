package com.booky.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.booky.controller.BookPrivateController;
import com.booky.dto.BasketDTO;
import com.booky.dto.BookDTO;
import com.booky.dto.StockDTO;
import com.booky.entity.Book;
import com.booky.entity.Category;
import com.booky.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(BookPrivateController.class)
public class BookPrivateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@Autowired
	private ObjectMapper objectMapper;

	private Book book = null;

	@Before
	public void setup() {
		book = new Book();
		book.setId(1L);
		book.setName("Spring in Action");
		book.setIsbn(1111111111L);
		book.setPrice(10d);
		Category category = new Category();
		category.setId(1L);
		category.setName("roman");
		book.setCategory(category);
	}

	@Test
	@WithMockUser
	public void testCreateBookSuccessfully() throws Exception {

		Future bookFuture = new AsyncResult<>(book);

		when(bookService.createBook(any(BookDTO.class))).thenReturn(bookFuture);

		mockMvc.perform(post("/book/private").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new BookDTO(book)))).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is("Spring in Action")));
	}

	@Test
	@WithMockUser
	public void testUpdateBookSuccessfully() throws Exception {

		Future bookFuture = new AsyncResult<>(book);

		when(bookService.updateBook(any(BookDTO.class))).thenReturn(bookFuture);

		mockMvc.perform(put("/book/private").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new BookDTO(book)))).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is("Spring in Action")));
	}

	@Test
	@WithMockUser
	public void testDeleteBookSuccessfully() throws Exception {

		Future bookFuture = new AsyncResult<>(book);

		when(bookService.deleteBook(1L)).thenReturn(bookFuture);

		mockMvc.perform(delete("/book/private/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new BookDTO(book)))).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is("Spring in Action")));
	}

	@Test
	@WithMockUser
	public void testOrderBookSuccessfully() throws Exception {

		List<BasketDTO> basketDTOList = new ArrayList<BasketDTO>();
		BasketDTO basketDTO = new BasketDTO();
		basketDTO.setBook(new BookDTO(book));
		basketDTO.setQuantity(3);
		basketDTOList.add(basketDTO);
		List<StockDTO> stockDTOList = new ArrayList<StockDTO>();
		StockDTO stockDTO = new StockDTO();
		stockDTO.setBook(book);
		stockDTO.setDate(new Date());
		stockDTO.setSold(3);
		stockDTOList.add(stockDTO);
		Future bookFuture = new AsyncResult<>(stockDTOList);

		when(bookService.orderBook(basketDTOList)).thenReturn(bookFuture);

		mockMvc.perform(post("/book/private/order").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(basketDTOList))).andExpect(status().isOk())
				.andExpect(jsonPath("[0].book.id", is(1))).andExpect(jsonPath("[0].book.name", is("Spring in Action")));
	}

}
