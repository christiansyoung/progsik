package amu.model;

import java.io.Serializable;
import java.util.List;

public class BookList implements Serializable {
	private Customer owner;
	private String name;
	private String description;
	private Integer id;
	private List<Book> books;

	//CONSTRUCTORS
	public BookList(Customer owner) {
		//Default constructor.
	}
	public BookList(Customer owner, int id, String name, String description) {
		this.id = id;
		this.name = name;
	}
	
	//NAME
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//ID
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	//BOOKS
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	//OWNER
	public Customer getOwner() {
		return this.owner;
	}
	
	//DESCRIPTION
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
