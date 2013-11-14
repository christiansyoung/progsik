package amu.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import amu.model.Customer;
import amu.model.Review;

public class ReviewDAO {

	public Review findByID(int id) {
		Review review = null;
		
		Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = Database.getConnection();
            
            String query = "SELECT * FROM review WHERE review.id = ? limit 1;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByID SQL Query: " + query);
            
            while (resultSet.next()) {
            	CustomerDAO customerDAO = new CustomerDAO();
            	VoteDAO voteDAO = new VoteDAO();
            	review = new Review();
            	review.setId(resultSet.getInt("id"));
            	review.setBook(resultSet.getInt("book_id"));
            	review.setCustomer(customerDAO.findByID(resultSet.getInt("customer_id")));
            	review.setRating(resultSet.getInt("rating"));
            	review.setText(resultSet.getString("text"));
            	review.setDate(resultSet.getDate("date"));
            	review.setVotes(voteDAO.findByReview(review));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        
        return review;
	}
	
	public List<Review> findByBookID(int bookID) {
        List<Review> reviews = new ArrayList<Review>();
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = Database.getConnection();
            
            String query = "SELECT * FROM review WHERE review.book_id = ?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, bookID);
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByBookID SQL Query: " + query);
            
            while (resultSet.next()) {
            	CustomerDAO customerDAO = new CustomerDAO();
            	VoteDAO voteDAO = new VoteDAO();
            	Review review = new Review();
            	review.setId(resultSet.getInt("id"));
            	review.setBook(resultSet.getInt("book_id"));
            	review.setCustomer(customerDAO.findByID(resultSet.getInt("customer_id")));
            	review.setRating(resultSet.getInt("rating"));
            	review.setText(resultSet.getString("text"));
            	review.setDate(resultSet.getDate("date"));
            	review.setVotes(voteDAO.findByReview(review));
            	reviews.add(review);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        
        return reviews;
    }
	
	public Review findByBookIDAndCustomer(int bookID, Customer customer) {
		List<Review> reviews = findByBookID(bookID);
		Iterator<Review> iterator = reviews.iterator();
		while (iterator.hasNext()) {
			Review review = iterator.next();
			if (review.getCustomer().getId() == customer.getId()) {
				return review;
			}
		}
		return null;
	}
	
	public Review add(Review review) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO review (book_id, customer_id, rating, text, date) VALUES (?,?,?,?,?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, review.getBook());
            statement.setInt(2, review.getCustomer().getId());
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getText());
            java.util.Date today = new java.util.Date();
            statement.setDate(5, new Date(today.getTime()));
            statement.executeUpdate();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "register SQL Query: " + query);

        } catch (SQLException exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } catch (Exception exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement);
        }

		return review;
	}
	
}
