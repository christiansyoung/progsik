package amu.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookDAO;
import amu.database.CustomerDAO;
import amu.database.ReviewDAO;
import amu.model.Book;
import amu.model.Customer;
import amu.model.Review;

public class ReviewBookAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
        	ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "reviewBook");

        	if (request.getMethod().equals("GET")) {
                ActionResponse responseBack = new ActionResponse(ActionResponseType.REDIRECT, "reviewBook");
                Map<String, String[]> parameters = request.getParameterMap();
                for (Map.Entry<String, String[]> parameter: parameters.entrySet()) {
                	for (String value: parameter.getValue()) {
                		responseBack.addParameter(parameter.getKey(), value);
                	}
                }
                session.setAttribute("saved_parameters", responseBack.getParameterString());        		
        	}
            
            return actionResponse;
        }
        
        List<String> messages = new ArrayList<String>();
        request.setAttribute("messages", messages);
        
        // search for book
        String isbn = request.getParameter("isbn");
        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.findByISBN(isbn);
        if (book == null) {
        	messages.add("Cannot find a book with that ISBN");
        	return new ActionResponse(ActionResponseType.FORWARD, "reviewBookError");
        }
        request.setAttribute("book", book);
        
        ReviewDAO reviewDAO = new ReviewDAO();
        Review existing = reviewDAO.findByBookIDAndCustomer(book.getId(), customer); 
        if (existing != null) {
        	messages.add("You cannot review a book more than once!");
        	return new ActionResponse(ActionResponseType.FORWARD, "reviewBookError");
        }
        
        if (request.getMethod().equals("POST")) {
            String nonce = request.getParameter("nonce");
            if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
            	messages.add("Something went wrong, please try again.");
            	return new ActionResponse(ActionResponseType.FORWARD, "reviewBook");
            }

            // get rating
            int rating;
            try {
            	rating = Integer.parseInt(request.getParameter("rating"));
            } catch (Exception e) {
            	messages.add("Please, rate the book.");
            	return new ActionResponse(ActionResponseType.FORWARD, "reviewBook");
            }
            
            // get text
            String text = request.getParameter("review");
            if (text == null || text.trim().equals("")) {
            	messages.add("Come on, why writing a review if you don't write anything?");
            	return new ActionResponse(ActionResponseType.FORWARD, "reviewBook");
            }
            
            Review review = new Review();
            review.setBook(book.getId());
            review.setCustomer(customer);
            review.setRating(rating);
            review.setText(text);
            
            try {
            	reviewDAO.add(review);
            } catch (Exception e) {
            	return new ActionResponse(ActionResponseType.FORWARD, "reviewBookError");
            }
            
            session.removeAttribute("nonce");
            request.setAttribute("isbn", isbn);
            return new ActionResponse(ActionResponseType.FORWARD, "reviewBookSuccessful");
        }

        // add a nonce to the session and the form
        String nonce = CustomerDAO.generateSalt();
        request.setAttribute("nonce", nonce);
		session.setAttribute("nonce", nonce);

		return new ActionResponse(ActionResponseType.FORWARD, "reviewBook");
	}

}
