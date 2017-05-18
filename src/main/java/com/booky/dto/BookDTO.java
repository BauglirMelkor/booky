package com.booky.dto;

import com.booky.entity.Book;

public class BookDTO {

    private Long id;

    private String name;

    private Long isbn;

    private CategoryDTO category = new CategoryDTO();
    
    private Double price;
    
    public BookDTO() {
	
    }

    public BookDTO(Book book) {
	this.id = book.getId();
	this.name = book.getName();
	this.isbn = book.getIsbn();
	this.price=book.getPrice();
	this.category.setId(book.getCategory().getId());
	this.category.setName(book.getCategory().getName());

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

	public Double getPrice() {
		if(price==null){
			price=0d;
		}
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}