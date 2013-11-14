package amu.model;

public class Vote {

	private int id;
	private int review_id;
	private int customer_id;
	private boolean helpful;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getReview() {
		return review_id;
	}
	
	public void setReview(int review_id) {
		this.review_id = review_id;
	}
	
	public int getCustomer() {
		return customer_id;
	}
	
	public void setCustomer(int customer_id) {
		this.customer_id = customer_id;
	}
	
	public boolean getHelpful() {
		return helpful;
	}
	
	public void setHelpful(boolean helpful) {
		this.helpful = helpful;
	}
}
