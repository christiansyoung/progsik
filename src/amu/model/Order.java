package amu.model;

import java.util.Calendar;

public class Order {
    
	public final static int CANCELLED = -1;
	public final static int PENDING = 0;
	public final static int SHIPPED = 1;
	public final static int DELIVERED = 2;
	
    private Integer id;
    private Customer customer;
    private Address address;
    private Calendar createdDate;
    private String value;
    private int status;
    private int operation;
    
    // TODO: Add OrderItems

    public Order(int id, int operation, Customer customer, Address address, Calendar createdDate, String value, int status) {
        this.id = id;
        this.operation = operation;
        this.customer = customer;
        this.address = address;
        this.createdDate = createdDate;
        this.value = value;
        this.status = status;
    }

    public Order(Customer customer, int operation, Address address, String subtotal) {
        this.id = null;
        this.operation = operation;
        this.customer = customer;
        this.address = address;
        this.createdDate = null;
        this.value = subtotal;
        this.status = 0;
    }
    
    public Order(Customer customer, Address address, String subtotal) {
        this.id = null;
        this.customer = customer;
        this.address = address;
        this.createdDate = null;
        this.value = subtotal;
        this.status = 0;
    }

    public Integer getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Address getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
    	this.address = address;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
    	this.value = value;
    }

    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
    	this.status = status;
    }
    
    public String getStatusText() {
        switch (status)
        {
            case DELIVERED: 
                return "Delivered";
            case SHIPPED:
                return "Shipped";
            case CANCELLED:
                return "Cancelled";
            case PENDING:
            default:
                return "Pending";
        }
    }
    
    public int getOperation() {
    	return operation;
    }
    
    public void setOperation(int id) {
    	this.operation = id;
    }
}
