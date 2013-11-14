package amu.model;

import java.util.Date;
import java.util.List;

public class Review {
	private int id;
    private int book;
    private Customer customer;
    private int rating;
    private String text;
    private Date date;
    private List<Vote> votes;
    private boolean voted = false;
    private int positive = 0;
    private int negative = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
	
    public int getBook() {
    	return book;
    }
    
    public void setBook(int book){
    	this.book = book;
    }
    
    public Customer getCustomer() {
    	return customer;
    }
    
    public void setCustomer(Customer customer) {
    	this.customer = customer;
    }
    
    public int getRating() {
    	return rating;
    }
    
    public void setRating(int rating) {
    	this.rating = rating;
    }
    
    public String getText() {
    	return text;
    }
    
    public void setText(String text) {
    	this.text = text;
    }
    
    public Date getDate() {
    	return date;
    }
    
    public void setDate(Date date) {
    	this.date = date;
    }
    
    public List<Vote> getVotes() {
    	return votes;
    }
    
    public void setVotes(List<Vote> votes) {
    	this.votes = votes;
    }
    
    public void addVote(Vote vote) {
    	this.votes.add(vote);
    }
    
    public boolean isVoted() {
    	return voted;
    }
    
    public void vote() {
    	this.voted = true;
    }
    
    public int getPositive() {
    	return positive;
    }
    
    public void setPositive(int positive) {
    	this.positive = positive;
    }
    
    public int getNegative() {
    	return negative;
    }
    
    public void setNegative(int negative) {
    	this.negative = negative;
    }
}
