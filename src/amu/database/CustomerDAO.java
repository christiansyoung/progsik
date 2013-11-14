package amu.database;

import amu.model.Customer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.logging.*;
import javax.xml.bind.DatatypeConverter;

public class CustomerDAO {

    public static String hashPassword(String plainTextPassword, String salt) {
        String hashedPassword = null;
        try {
            // Calculate SHA1(password+salt)
			hashedPassword = DatatypeConverter.printHexBinary(MessageDigest
					.getInstance("SHA-256")
					.digest((plainTextPassword
							+ System.getProperty("AMU_DARYA_SALT") + salt)
							.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return hashedPassword;
    }

    public static String generateActivationCode() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[8];
        random.nextBytes(bytes);
        return DatatypeConverter.printHexBinary(bytes);
    }
    
    public static String generateSalt() {
    	SecureRandom random = new SecureRandom();
    	byte bytes[] = new byte[64];
    	random.nextBytes(bytes);
    	return DatatypeConverter.printHexBinary(bytes);
    }

    public Customer findByEmail(String email) {
        Customer customer = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM customer WHERE email=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByEmail SQL Query: " + query);


            if (resultSet.next()) {
                customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPassword(resultSet.getString("password"));
                customer.setName(resultSet.getString("name"));
                customer.setActivationToken(resultSet.getString("activation_token"));
                customer.setSalt(resultSet.getString("salt"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } catch (Exception exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return customer;
    }
    
    public Customer findByID(int id) {
        Customer customer = null;

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM customer WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByID SQL Query: " + query);


            if (resultSet.next()) {
                customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPassword(resultSet.getString("password"));
                customer.setName(resultSet.getString("name"));
                //customer.setActivationToken(resultSet.getString("activation_token"));
                //customer.setSalt(resultSet.getString("salt"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } catch (Exception exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return customer;
    }

    public boolean edit(Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "UPDATE customer SET email=?, password=?, name=?, salt=? WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getPassword());
            statement.setString(3, customer.getName());
            statement.setString(4, customer.getSalt());
            statement.setInt(5, customer.getId());
          
            if (statement.executeUpdate() == 0) {
                return false; // No rows were affected
            } else {
                return true;
            }

        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
            return false;
        } catch (Exception exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
            return false;
        } finally {
            Database.close(connection, statement, resultSet);
        }
    }
    
    public Customer register(Customer customer) {

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO customer (email, password, name, activation_token, salt) VALUES (?,?,?,?,?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, customer.getEmail());
            statement.setString(2, customer.getPassword());
            statement.setString(3, customer.getName());
            statement.setString(4, customer.getActivationToken());
            statement.setString(5, customer.getSalt());
            statement.executeUpdate();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "register SQL Query: " + query);

        } catch (SQLException exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } catch (Exception exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement);
        }

        return findByEmail(customer.getEmail());
    }

    public Customer activate(Customer customer) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "UPDATE customer SET activation_token=NULL WHERE email=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, customer.getEmail());
            statement.executeUpdate();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "activate SQL Query: " + query);

            customer.setActivationToken(null);

        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } catch (Exception exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
        return customer;
    }
    
}