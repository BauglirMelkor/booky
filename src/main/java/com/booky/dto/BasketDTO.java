package com.booky.dto;

import java.util.List;

public class BasketDTO {
	
	BookDTO book;
	
	private int quantity;

	public BookDTO getBook() {
		return book;
	}

	public void setBook(BookDTO book) {
		this.book = book;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	 @Override
	    public boolean equals(Object o) {

	        if (o == this) return true;
	        if (!(o instanceof BasketDTO)) {
	            return false;
	        }

	        BasketDTO basketDTO = (BasketDTO) o;

	        return basketDTO.book.getId().equals(book.getId());
	    }

	  
	    @Override
	    public int hashCode() {
	        int result = 17;
	        result = 31 * result + book.getId().hashCode();
	        return result;
	    }
}
