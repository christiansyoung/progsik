package amu.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class Book implements Serializable {
    private int id;
    private Title title;
    private Publisher publisher;
    private List<Author> author;
    private String published; 
    private int edition;
    private String binding; 
    private String isbn10;
    private String isbn13;
    private String description;
    private float price; 
    private List<Review> reviews;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    public int getEdition() {
        return edition;
    }

    public void setEdition(int edition) {
        this.edition = edition;
    }
    
    public List<Review> getReviews() {
    	return reviews;
    }
    
    public void setReviews(List<Review> reviews) {
    	this.reviews = reviews;
    }
    
    public void addReview(Review review) {
    	this.reviews.add(review);
    }
    
    public int getRating() {
    	int rating = 0;
    	Iterator<Review> iterator = reviews.iterator();
    	int count = 0;
    	while (iterator.hasNext()) {
    		Review review = iterator.next();
    		rating += review.getRating();
    		count++;
    	}
    	if (count != 0) {
    		rating = Math.round(rating / count);
    	}
    	return rating;
    }
}