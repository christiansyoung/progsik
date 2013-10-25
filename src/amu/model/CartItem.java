package amu.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private Book book;
    private int quantity;

    public CartItem(Book book, int quantity)
    {
        this.book = book;
        this.quantity = quantity;
    }
    
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
    	int q = Math.abs(quantity);
        return q;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    void addQuantity(int quantity) {
       this.quantity += quantity;
    }
}
