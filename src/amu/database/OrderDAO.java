package amu.database;

import amu.model.Customer;
import amu.model.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderDAO {

    public List<Order> browse(Customer customer) {
    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet resultSet = null;
        List<Order> orders = new ArrayList<Order>();

        try {
            connection = Database.getConnection();
            String query = "SELECT * FROM `order` WHERE customer_id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, customer.getId());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                AddressDAO addressDAO = new AddressDAO();
                Calendar createdDate = Calendar.getInstance();
                createdDate.setTime(resultSet.getDate("created"));
                orders.add(new Order(resultSet.getInt("id"),
                		resultSet.getInt("operation"),
                        customer, 
                        addressDAO.read(resultSet.getInt("address_id")), 
                        createdDate, 
                        resultSet.getString("value"), 
                        resultSet.getInt("status")));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        Map<Integer, Order> processed = new HashMap<Integer, Order>();
        // process orders
        Iterator<Order> iterator = orders.iterator();            
        while (iterator.hasNext()) {
        	Order order = iterator.next();
        	if (order.getStatus() == Order.PENDING) {
        		processed.put(order.getOperation(), order);
        	}
        	if (order.getStatus() == Order.CANCELLED) {
        		processed.get(order.getOperation()).setStatus(Order.CANCELLED);
        	}
        }

        return new ArrayList<Order>(processed.values());
    }
    
    public boolean add(Order order) {
    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet resultSet = null;

        try {
            connection = Database.getConnection();
            
            // add a new operation for accounting purposes
            String query = "INSERT INTO `accounting` (id) VALUES (null);";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            int operation = 0;
            if (resultSet.next()) {
            	operation = resultSet.getInt(1);
            }
            
            statement.close();
			resultSet.close();

            // add the order referencing the new accounting operation
            query = "INSERT INTO `order` (operation, customer_id, address_id, created, value, status) "+
            		"VALUES (?, ?, ?, CURDATE(), ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, operation);
            statement.setInt(2, order.getCustomer().getId());
            statement.setInt(3, order.getAddress().getId());
            statement.setBigDecimal(4, new BigDecimal(order.getValue()));
            statement.setInt(5, order.getStatus());
            statement.executeUpdate();
            
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return false;
    }
    
    public boolean cancel(Order order) {
    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO `order` (operation, customer_id, address_id, created, value, status) "+
            		"VALUES (?, ?, ?, CURDATE(), ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, order.getOperation());
            statement.setInt(2, order.getCustomer().getId());
            statement.setInt(3, order.getAddress().getId());
            statement.setBigDecimal(4, new BigDecimal(order.getValue()).negate());
            statement.setInt(5, Order.CANCELLED);
            statement.executeUpdate();
            
            if (statement.getGeneratedKeys().next()) {
                return true;
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return false;
    }
    
    public boolean edit(Order order) {
    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet resultSet = null;

        try {
        	// get the old one first
            connection = Database.getConnection();
            String query = "SELECT * FROM `order` WHERE id=?";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, order.getId());
            resultSet = statement.executeQuery();

			if (resultSet.next()) {
				AddressDAO addressDAO = new AddressDAO();
				CustomerDAO customerDAO = new CustomerDAO();
				Calendar createdDate = Calendar.getInstance();
				createdDate.setTime(resultSet.getDate("created"));
				Order old = new Order(resultSet.getInt("id"),
									  resultSet.getInt("operation"),
									  customerDAO.findByID(resultSet.getInt("customer_id")),
									  addressDAO.read(resultSet.getInt("address_id")),
									  createdDate,
									  resultSet.getString("value"),
									  resultSet.getInt("status"));

				// cancel it!
				this.cancel(old);
				statement.close();
				resultSet.close();

				// add a new one
	            query = "INSERT INTO `order` (operation, customer_id, address_id, created, value, status) "+
	            		"VALUES (?, ?, ?, CURDATE(), ?, ?)";
	            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	            statement.setInt(1, order.getOperation());
	            statement.setInt(2, order.getCustomer().getId());
	            statement.setInt(3, order.getAddress().getId());
	            statement.setBigDecimal(4, new BigDecimal(order.getValue()));
	            statement.setInt(5, order.getStatus());
	            statement.executeUpdate();
	            
	            resultSet = statement.getGeneratedKeys();
	            if (resultSet.next()) {
	                return true;
	            }
			}

        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }


    	return false;
    }
}
