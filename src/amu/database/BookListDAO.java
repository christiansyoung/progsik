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
import amu.model.Publisher;
import amu.model.Title;

public class BookListDAO {

	public BookList findById(int id) {
		BookList list = new BookList();
    	
    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet resultSet = null;
    	
    	try {
            connection = Database.getConnection();
            
            String query = "SELECT list.*, ("+
            		"SELECT COUNT(*) FROM book_x_list WHERE list_id = list.id"+
            		") AS amount FROM list WHERE list.id = ?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findById SQL Query: " + query);
            
            while (resultSet.next()) {
            	list.setId(resultSet.getInt("id"));
            	list.setCustomer(resultSet.getInt("customer_id"));
            	list.setTitle(resultSet.getString("title"));
            	list.setDescription(resultSet.getString("description"));
            	list.setPublic(resultSet.getBoolean("public"));
            	list.setAmount(resultSet.getInt("amount"));
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
    	
    	return list;
	}
	
    public List<BookList> findByCustomer(Customer customer) {
    	List<BookList> lists = new ArrayList<BookList>();
    	
    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet resultSet = null;
    	
    	try {
            connection = Database.getConnection();
            
            String query = "SELECT list.*, ("+
            		"SELECT COUNT(*) FROM book_x_list WHERE list_id = list.id"+
            		") AS amount FROM list WHERE list.customer_id = ?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, customer.getId());
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findByCustomer SQL Query: " + query);
            
            while (resultSet.next()) {
            	BookList list = new BookList();
            	list.setId(resultSet.getInt("id"));
            	list.setCustomer(resultSet.getInt("customer_id"));
            	list.setTitle(resultSet.getString("title"));
            	list.setDescription(resultSet.getString("description"));
            	list.setPublic(resultSet.getBoolean("public"));
            	list.setAmount(resultSet.getInt("amount"));

                lists.add(list);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
    	
    	return lists;
    }
	
    public List<BookList> findAll() {
    	List<BookList> lists = new ArrayList<BookList>();
    	
    	Connection connection = null;
    	Statement statement = null;
    	ResultSet resultSet = null;
    	
    	try {
            connection = Database.getConnection();
            statement = connection.createStatement();
            
            String query = "SELECT list.*, ("+
            		"SELECT COUNT(*) FROM book_x_list WHERE list_id = list.id"+
            		") AS amount FROM list;";
            resultSet = statement.executeQuery(query);
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findAll SQL Query: " + query);
            
            while (resultSet.next()) {
            	BookList list = new BookList();
            	list.setId(resultSet.getInt("id"));
            	list.setCustomer(resultSet.getInt("customer_id"));
            	list.setTitle(resultSet.getString("title"));
            	list.setDescription(resultSet.getString("description"));
            	list.setPublic(resultSet.getBoolean("public"));
            	list.setAmount(resultSet.getInt("amount"));

                lists.add(list);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
    	
    	return lists;
    }
    
    public List<BookList> findAllPublic() {
    	List<BookList> lists = new ArrayList<BookList>();
    	
    	Connection connection = null;
    	Statement statement = null;
    	ResultSet resultSet = null;
    	
    	try {
            connection = Database.getConnection();
            statement = connection.createStatement();
            
            String query = "SELECT list.*, ("+
            		"SELECT COUNT(*) FROM book_x_list WHERE list_id = list.id"+
            		") AS amount FROM list WHERE list.public = 1;";
            resultSet = statement.executeQuery(query);
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "findAll SQL Query: " + query);
            
            while (resultSet.next()) {
            	BookList list = new BookList();
            	list.setId(resultSet.getInt("id"));
            	list.setCustomer(resultSet.getInt("customer_id"));
            	list.setTitle(resultSet.getString("title"));
            	list.setDescription(resultSet.getString("description"));
            	list.setPublic(resultSet.getBoolean("public"));
            	list.setAmount(resultSet.getInt("amount"));

                lists.add(list);
            }
        } catch (SQLException exception) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, null, exception);
        } finally {
            Database.close(connection, statement, resultSet);
        }
    	
    	return lists;
    }
    
    public boolean add(BookList list) {
    	Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT INTO list (customer_id, title, description, public) VALUES (?,?,?,?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, list.getCustomer());
            statement.setString(2, list.getTitle());
            statement.setString(3, list.getDescription());
            statement.setBoolean(4, list.isPublic());
            
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "add list SQL Query: " + query);
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
    
    public boolean edit(BookList list) {
    	Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();

            String query = "UPDATE list SET title = ?, description = ?, public = ? WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, list.getTitle());
            statement.setString(2, list.getDescription());
            statement.setBoolean(3, list.isPublic());
            statement.setInt(4, list.getId());
            
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "edit list SQL Query: " + query);
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

    public boolean delete(int id) {
    	Connection connection = null;
    	PreparedStatement statement = null;

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
            Database.close(connection, statement);
        }

        return false;
    }
    
    public List<Book> getBooks(BookList list) {
    	List<Book> books = new ArrayList<Book>();
    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet resultSet = null;
    	
    	try {
    		connection = Database.getConnection();

	        String query = "SELECT * FROM book, publisher, title, book_x_list " +
					   "WHERE book.title_id = title.id " +
					   "AND book.publisher_id = publisher.id "+
					   "AND book.id = book_x_list.book_id "+
					   "AND book_x_list.list_id = ?;";
	        statement = connection.prepareStatement(query);
	        statement.setInt(1, list.getId());
	        resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "getBooks SQL Query: " + query);
            
            while (resultSet.next()) {
            	AuthorDAO authorDAO = new AuthorDAO();
                ReviewDAO reviewDAO = new ReviewDAO();

            	Book book = new Book();
            	book.setId(resultSet.getInt("book.id"));
                book.setTitle(new Title(resultSet.getInt("title.id"), resultSet.getString("title.name")));
                book.setPublisher(new Publisher(resultSet.getInt("publisher.id"), resultSet.getString("publisher.name")));
                book.setPublished(resultSet.getString("book.published"));
                book.setEdition(resultSet.getInt("book.edition"));
                book.setBinding(resultSet.getString("book.binding"));
                book.setIsbn10(resultSet.getString("book.isbn10"));
                book.setIsbn13(resultSet.getString("book.isbn13"));
                book.setDescription(resultSet.getString("book.description"));
                book.setAuthor(authorDAO.findByBookID(resultSet.getInt("book.id")));
                book.setPrice(resultSet.getFloat("book.price"));
                book.setReviews(reviewDAO.findByBookID(resultSet.getInt("book.id")));
            	
                books.add(book);
            }
	    } catch (SQLException exception) {
	        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
	    } finally {
	        Database.close(connection, statement);
	    }
    	
    	return books;
    }
    
    public BookList addBook(BookList list, Book book) {
    	Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = Database.getConnection();

            String query = "INSERT IGNORE INTO book_x_list (book_id, list_id) VALUES (?,?)";
            statement = connection.prepareStatement(query);
            statement.setInt(1, book.getId());
            statement.setInt(2, list.getId());
            
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "addBook SQL Query: " + query);
            if (statement.executeUpdate() > 0) {
                list.setAmount(list.getAmount()+1);
            }
        } catch (SQLException exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } catch (Exception exception) {
        	Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, exception);
        } finally {
            Database.close(connection, statement);
        }
    	
    	return list;
    }
    
    public boolean isBookOnList(BookList list, Book book) {
    	Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getConnection();

            String query = "SELECT * FROM book_x_list WHERE book_id = ? AND list_id = ?;";
            statement = connection.prepareStatement(query);
            statement.setInt(1, book.getId());
            statement.setInt(2, list.getId());
            resultSet = statement.executeQuery();
            Logger.getLogger(this.getClass().getName()).log(Level.FINE, "addBook SQL Query: " + query);
            
            while (resultSet.next()) {
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
