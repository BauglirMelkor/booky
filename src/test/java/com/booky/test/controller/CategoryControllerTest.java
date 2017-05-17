package com.booky.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.web.bind.annotation.PathVariable;

import com.booky.controller.CategoryController;
import com.booky.entity.Category;
import com.booky.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	@Autowired
	private ObjectMapper objectMapper;

	private Category category = null;

	@Before
	public void setup() {
		category = new Category();
		category.setId(1L);
		category.setName("roman");
	}

	@Test
	@WithMockUser
	public void createCategorySuccessful() throws JsonProcessingException, Exception {

		Future categoryFuture = new AsyncResult<>(category);

		when(categoryService.createCategory("roman")).thenReturn(categoryFuture);

		mockMvc.perform(get("/category/private/createCategory/roman").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes("roman"))).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is("roman")));

	}
	
	@Test
	@WithMockUser
	public void getCategoryByIdSuccessful() throws JsonProcessingException, Exception {

		when(categoryService.getCategoryById(1L)).thenReturn(category);

		mockMvc.perform(get("/category/private/getCategoryById/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes("roman"))).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is("roman")));

	}
	
	@Test
	@WithMockUser
	public void getCategoryByNameSuccessful() throws JsonProcessingException, Exception {

		when(categoryService.getCategoryByName("roman")).thenReturn(category);

		mockMvc.perform(get("/category/private/getCategoryByName/roman").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes("roman"))).andExpect(status().isOk())
				.andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is("roman")));
	}



}
