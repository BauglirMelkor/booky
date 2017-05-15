package com.booky.service;

import java.util.Optional;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.booky.entity.Category;
import com.booky.exception.CategoryAlreadyExistsException;
import com.booky.exception.CategoryNotFoundException;
import com.booky.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService {

	private final Logger log = LoggerFactory.getLogger(CategoryService.class);

	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Async
	public Future<Category> createCategory(String name) throws CategoryNotFoundException {
		log.debug("Creating category with the name {}", name);
		Optional<Category> categoryOptional = categoryRepository.findOneByName(name);
		if (categoryOptional.isPresent()) {
			throw new CategoryAlreadyExistsException("category already exists with the name " + name);
		} else {
			Category category = new Category();
			category.setName(name);
			categoryRepository.save(category);
			return new AsyncResult<>(category);
		}

	}

	public Category getCategoryByName(String name) throws CategoryNotFoundException {
		log.debug("Finding category by name {}", name);
		Optional<Category> category = categoryRepository.findOneByName(name);
		if (category.isPresent()) {
			return category.get();
		} else {
			throw new CategoryNotFoundException("category not found with the name " + name);
		}

	}

	public Category getCategoryById(Long id) throws CategoryNotFoundException {
		log.debug("Finding category by id {}", id);
		Category category = categoryRepository.findOne(id);
		if (category != null) {
			return category;
		} else {
			throw new CategoryNotFoundException("category not found with the id " + id);
		}

	}

}