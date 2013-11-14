package amu.model;

public class BookList {
	private int id;
	private int customer_id;
	private String title;
	private String description;
	private boolean is_public;
	private int amount;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCustomer() {
		return customer_id;
	}
	
	public void setCustomer(int customer_id) {
		this.customer_id = customer_id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isPublic() {
		return is_public;
	}
	
	public void setPublic(boolean pub) {
		this.is_public = pub;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
