package com.booky.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booky.dto.CategoryDTO;
import com.booky.entity.Category;
import com.booky.service.CategoryService;


@RestController
@RequestMapping("/category/private")
public class CategoryController {

    private final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
	this.categoryService = categoryService;
    }

    @GetMapping("/createCategory/{name}")
   
    public CategoryDTO createBook(@PathVariable String name) throws InterruptedException, ExecutionException {
	log.debug("Creating category");
	Future<Category> category=categoryService.createCategory(name);
	while(!category.isDone()){
	    Thread.sleep(10);
	}
	return new CategoryDTO(category.get());
    }
    
    @GetMapping("/getCategoryById/{id}")
       public CategoryDTO getCategoryById(@PathVariable Long id) {
	log.debug("Geting category by id "+ id);
	return new CategoryDTO(categoryService.getCategoryById(id));
    }
    
    @GetMapping("/getCategoryByName/{name}")
      public CategoryDTO getCategoryByName(@PathVariable String name) {
	log.debug("Geting category by name "+ name);
	return new CategoryDTO(categoryService.getCategoryByName(name));
    }
}