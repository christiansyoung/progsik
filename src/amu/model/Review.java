package amu.model;

import java.util.HashMap;

public class Review {
	
	private int id;
    private String text;
    private int rating;
    private Customer customer;
    private Book book;
    //private int helpful;
    //private HashMap<Customer, Boolean> alreadyHelped; 
    		
    public Review() {
    	//this.helpful = 0;
    	//this.alreadyHelped = new HashMap<Customer, Boolean>();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    public int getRating() {
    	return rating;
    }
    
    public void setRating(int rating) {
    	this.rating = rating;
    }
    
    public Customer getCustomer() {
    	return this.customer;
    }
    
    public void setCustomer(Customer customer) {
    	this.customer = customer;
    }
    
    public Book getBook() {
    	return this.book;
    }
    
    public void setBook(Book book) {
    	this.book = book;
    }
    
    /*public void markAsHelpful(Customer customer) {
    	if(!alreadyHelped.containsKey(customer)) {
    		this.helpful += 1;
    		alreadyHelped.put(customer, true);
    	}
    }
    
    public void markAsUnhelpful(Customer customer) {
    	if(!alreadyHelped.containsKey(customer)) {
    		this.helpful -= 1;
    		alreadyHelped.put(customer, true);
    	}
    }*/

}
