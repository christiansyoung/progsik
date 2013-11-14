package amu.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import amu.model.Review;
import amu.model.Vote;

public class VoteDAO {

	public List<Vote> findByReview(Review review) {
        List<Vote> votes = new ArrayList<Vote>();
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = Database.getConnection();
            
            String query = "SELECT * FROM vote WHERE vote.review_id = ?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, review.getId());
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByReview SQL Query: " + query);
            
            while (resultSet.next()) {
            	Vote vote = new Vote();
            	vote.setId(resultSet.getInt("id"));
            	vote.setReview(review.getId());
            	vote.setHelpful(resultSet.getBoolean("helpful"));
            	vote.setCustomer(resultSet.getInt("customer_id"));

            	votes.add(vote);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        
        return votes;
    }
	

	public int countHelpfulByReview(Review review) {
        int votes = 0;
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = Database.getConnection();
            
            String query = "SELECT * FROM vote WHERE vote.review_id = ? AND vote.helpful = 1;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, review.getId());
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "countHelpfulByReview SQL Query: " + query);
            
            while (resultSet.next()) {
            	votes++;
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        
        return votes;
    }
	
	
	public boolean add(Vote vote) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO vote (review_id, customer_id, helpful) VALUES (?,?,?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, vote.getReview());
            statement.setInt(2, vote.getCustomer());
            statement.setBoolean(3, vote.getHelpful());

            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "add vote SQL Query: " + query);
            if (statement.executeUpdate() > 0) {
                return true;
            }
        } catch (SQLException exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } catch (Exception exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement);
        }

		return false;
	}
	
}
