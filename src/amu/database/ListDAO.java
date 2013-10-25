package amu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import amu.model.Book;
import amu.model.BookList;
import amu.model.Customer;

public class ListDAO {
	
	
	/**
	 * Returns all the lists that are affilliated with the customer.
	 * @param customer
	 * @return
	 */
	public List<BookList> getListsByCustomer(Customer customer) {
        List<BookList> lists = new ArrayList<BookList>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();
            String query = "SELECT id, title, description, FROM list WHERE customer_id=?";
            statement = connection.prepareStatement(query);

            statement.setInt(1, customer.getId());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                
                lists.add(new BookList(
                        customer,
                		resultSet.getInt("id"), 
                        resultSet.getString("title"), 
                        resultSet.getString("description")));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }

        return lists;
    }
	
	/**
	 * Fills the booklist with the books that it contains.
	 * @param bookList
	 * @return
	 */
	public List<Book> fillBookList(BookList bookList) {
		List<Book> books = new ArrayList<Book>();
		
		Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = Database.getConnection();
            statement = connection.createStatement();
            
            String query = "SELECT "
                    + "book.isbn13, "
                    + "FROM book, list_x_book "
                    + "WHERE list_x_book.book_id=book.id "
                    + "AND list_x_book.list_id = " + bookList.getId() + " ";
            resultSet = statement.executeQuery(query);
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByBookID SQL Query: " + query);
            
            BookDAO bdao = new BookDAO();
            while (resultSet.next()) {
//            	Book b = bdao.findByISBN(resultSet.getString("book.isbn13"));
                books.add(bdao.findByISBN(resultSet.getString("book.isbn13")));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
		return books;
	}

	
	/**
	 * Legger til en ny (NB: TOM!) Liste i databasen.
	 * @param bookList
	 * @return
	 */
	public boolean add(BookList bookList) {
		Connection connection = null;
	        PreparedStatement statement = null;
	        ResultSet resultSet = null;

	        try {
	            connection = Database.getConnection();

	            String query = "INSERT INTO list (title, description, customer_id) VALUES (?, ?, ?)";
	            statement = connection.prepareStatement(query);
	            statement.setString(1, bookList.getName());
	            statement.setString(2, bookList.getDescription());
	            statement.setInt(3, bookList.getOwner().getId());

	            if (statement.executeUpdate() > 0) {
	                return true;
	            }
	        } catch (SQLException exception) {
	            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
	        } finally {
	            Database.close(connection, statement, resultSet);
	        }

	        return false;
	}
	
	/**
	 * Adds a book to the given list.
	 * @param bookListID
	 * @param bookID
	 * @return
	 */
	public boolean addItem(int bookListID, int bookID) {
		Connection connection = null;
	        PreparedStatement statement = null;
	        ResultSet resultSet = null;

	        try {
	            connection = Database.getConnection();

	            String query = "INSERT INTO list_x_book (list_id, book_id) VALUES (?, ?)";
	            statement = connection.prepareStatement(query);
	            statement.setInt(1, bookListID);
	            statement.setInt(2, bookID);

	            if (statement.executeUpdate() > 0) {
	                return true;
	            }
	        } catch (SQLException exception) {
	            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
	        } finally {
	            Database.close(connection, statement, resultSet);
	        }

	        return false;
	}
	
	/**
	 * Sletter en liste ut ifra iden.
	 * @param id
	 * @return
	 */
	public boolean delete(int id) {
	        Connection connection = null;
	        PreparedStatement statement = null;
	        ResultSet resultSet = null;

	        try {
	            connection = Database.getConnection();

	            String query = "DELETE FROM list WHERE id=?";
	            statement = connection.prepareStatement(query);
	            statement.setInt(1, id);

	            if (statement.executeUpdate() > 0) {
	                return true;
	            }
	        } catch (SQLException exception) {
	            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
	        } finally {
	            Database.close(connection, statement, resultSet);
	        }

	        return false;
	    }
	
	
}
