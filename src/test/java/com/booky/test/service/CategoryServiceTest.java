package com.booky.test.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import com.booky.dto.UserDTO;
import com.booky.entity.BookyUser;
import com.booky.entity.Category;
import com.booky.exception.CategoryAlreadyExistsException;
import com.booky.exception.CategoryNotFoundException;
import com.booky.exception.UserNotFoundException;
import com.booky.repository.CategoryRepository;
import com.booky.service.CategoryService;

@RunWith(SpringRunner.class)
public class CategoryServiceTest {

	@MockBean
	private CategoryRepository categoryRepository;

	private CategoryService categoryService;
	
	private Optional<Category> categoryOptional;
	
	private Category category=null;

	@Before
	public void setUp() throws Exception {
		categoryService = spy(new CategoryService(categoryRepository));
		category = new Category();
		category.setId(1L);
		category.setName("roman");
		categoryOptional=Optional.of(category);
	}

	@Test
	public void createCategorySuccessful() throws InterruptedException, ExecutionException {
		when(categoryRepository.save(category)).thenReturn(category);
		when(categoryRepository.findOneByName("roman")).thenReturn(Optional.ofNullable(null));
		Future<Category> category = categoryService.createCategory("roman");
		assert (category.get().getName().equals("roman"));		
	}
	
	@Test(expected=CategoryAlreadyExistsException.class)
	public void createCategoryCategoryAlreadyExists() throws InterruptedException, ExecutionException {
		when(categoryRepository.save(category)).thenReturn(category);
		when(categoryRepository.findOneByName("roman")).thenReturn(Optional.ofNullable(new Category()));
		categoryService.createCategory("roman");
		
	}
	
	@Test
	public void getCategoryByNameSuccessful(){
		when(categoryRepository.findOneByName("roman")).thenReturn(categoryOptional);
		Category category=categoryService.getCategoryByName("roman");
		assert (category.getName().equals("roman"));		
	}
	
	@Test(expected=CategoryNotFoundException.class)
	public void getCategoryByNameCategoryNotFound(){
		when(categoryRepository.findOneByName("roman")).thenReturn(Optional.ofNullable(null));
		categoryService.getCategoryByName("roman");			
	}

}
