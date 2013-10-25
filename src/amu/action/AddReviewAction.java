package amu.action;

import amu.database.BookDAO;
import amu.database.ReviewDAO;
import amu.model.Book;
import amu.model.Customer;
import amu.model.Review;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class AddReviewAction implements Action {
    
    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "addReview");
            return actionResponse;
        }
        
        if (request.getMethod().equals("POST")) {
            Map<String, String> messages = new HashMap<String, String>();
            request.setAttribute("messages", messages);
            
            String text = request.getParameter("text");
            Integer rating = 0;
            String bookIdString = request.getParameter("bookid");
       
            try {
            	rating = Integer.parseInt(request.getParameter("rating"));
            }catch(Exception e){
            	messages.put("error", "Could not parse ints");
            	return new ActionResponse(ActionResponseType.FORWARD, "addReview");
            }
            
            BookDAO bookDOA = new BookDAO();
            Book book = bookDOA.findByISBN(bookIdString);
            
            Review review = new Review();
            review.setText(text);
            review.setRating(rating);
            review.setCustomer(customer);
            review.setBook(book);
            
            Map<String, String> values = new HashMap<String, String>();
            request.setAttribute("values", values);
            values.put("text", text);
            values.put("rating", rating.toString());
            
            ReviewDAO reviewDAO = new ReviewDAO();
            
            if (reviewDAO.addReview(review)) {
                return new ActionResponse(ActionResponseType.REDIRECT, "viewBook");
            }
            
            messages.put("error", "An error occured.");
        }
               
        return new ActionResponse(ActionResponseType.FORWARD, "addReview");
    }
}

