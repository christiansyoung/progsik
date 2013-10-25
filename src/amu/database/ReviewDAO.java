package amu.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import amu.model.Customer;
import amu.model.Review;

public class ReviewDAO {
	
	Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    
    public boolean addReview(Review review) {
    	try {
            connection = Database.getConnection();

            String query = "INSERT INTO `review` (text, rating, customer_id, book_id) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, review.getText());
            statement.setInt(2, review.getRating());
            statement.setInt(3, review.getCustomer().getId());
            statement.setInt(4, review.getBook().getId());
            statement.executeUpdate();
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                // TODO: Add OrderItems
                return true;
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception + "FUCK OFF");
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return false;
    }

}
