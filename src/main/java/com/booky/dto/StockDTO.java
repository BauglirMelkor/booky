package com.booky.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.booky.entity.Book;
import com.booky.entity.Stock;

public class StockDTO {
		
	private Book book;

	private Date date;

	private int sold;


	public StockDTO(Stock stock) {
		this.book=stock.getBook();
		this.date=stock.getDate();
		this.sold=stock.getSold();
	}


	public Book getBook() {
		return book;
	}


	public void setBook(Book book) {
		this.book = book;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public int getSold() {
		return sold;
	}


	public void setSold(int sold) {
		this.sold = sold;
	}

}
