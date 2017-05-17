package com.booky.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.booky.controller.BookPublicController;
import com.booky.dto.BookDTO;
import com.booky.entity.Book;
import com.booky.entity.Category;
import com.booky.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
@WebMvcTest(BookPublicController.class)
public class BookPublicControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@Autowired
	private ObjectMapper objectMapper;

	private Book book = null;

	private List<BookDTO> bookDTOList = new ArrayList<BookDTO>();

	private List<Book> bookList = new ArrayList<Book>();

	@Before
	public void setup() {
		book = new Book();
		book.setId(1L);
		book.setName("Spring in Action");
		book.setIsbn(1111111111L);
		book.setPrice(10d);
		Category category = new Category();
		category.setId(1L);
		category.setName("teknik");
		book.setCategory(category);
		bookDTOList.add(new BookDTO(book));
		bookList.add(book);

	}

	@Test
	@WithMockUser
	public void getBookListSuccessful() throws Exception {
		Page<Book> bookPage = new PageImpl<>(bookList);
		when(bookService.getBooks(any(PageRequest.class))).thenReturn(bookPage);
		mockMvc.perform(get("/book/public?page=0&size=2").contentType(MediaType.APPLICATION_JSON)
				.content("")).andExpect(status().isOk())
				.andExpect(jsonPath("[0].id", is(1))).andExpect(jsonPath("[0].name", is("Spring in Action")));
	}
	
	@Test
	@WithMockUser
	public void getBookByIsbnSuccessful() throws Exception {
		
		when(bookService.getBookByIsbn(1111111111L)).thenReturn(book);
		mockMvc.perform(get("/book/public/bookByIsbn/1111111111").contentType(MediaType.APPLICATION_JSON)
				.content("")).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is("Spring in Action")));
	}
	
	@Test
	@WithMockUser
	public void bookByCategoryNameSuccessful() throws Exception {
		Page<Book> bookPage = new PageImpl<>(bookList);
		when(bookService.getBookByCategoryName(any(PageRequest.class),any())).thenReturn(bookPage);
		mockMvc.perform(get("/book/public/bookByCategory/teknik?page=0&size=2").contentType(MediaType.APPLICATION_JSON)
				.content("page=0&size=2")).andExpect(status().isOk())
				.andExpect(jsonPath("[0].id", is(1))).andExpect(jsonPath("[0].name", is("Spring in Action")));
	}
	
	@Test
	@WithMockUser
	public void getBookByNameSuccessful() throws Exception {
		
		when(bookService.getBookByName("Spring in Action")).thenReturn(book);
		mockMvc.perform(get("/book/public/bookByName/Spring in Action").contentType(MediaType.APPLICATION_JSON)
				.content("")).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is("Spring in Action")));
	}

}
