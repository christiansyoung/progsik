package amu.action;

import java.util.Iterator;
import java.util.List;

import amu.database.BookDAO;
import amu.database.BookListDAO;
import amu.database.CustomerDAO;
import amu.database.VoteDAO;
import amu.model.Book;
import amu.model.BookList;
import amu.model.Customer;
import amu.model.Review;
import amu.model.Vote;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class ViewBookAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession(true);
		Customer customer = (Customer) session.getAttribute("customer");

        BookDAO bookDAO = new BookDAO();
        Book book = bookDAO.findByISBN(request.getParameter("isbn"));
        
        if (book != null) {
            request.setAttribute("book", book);
            
            VoteDAO voteDAO = new VoteDAO();

            if (customer != null) {
            	request.setAttribute("customer", customer);
            	
            	BookListDAO listDAO = new BookListDAO();
            	List<BookList> lists = listDAO.findByCustomer(customer);
            	request.setAttribute("lists", lists);
            }

			// look for all the reviews...
			List<Review> reviews = book.getReviews();
			Iterator<Review> ir = reviews.iterator();
			while (ir.hasNext()) {
				Review review = ir.next();
				int positive = voteDAO.countHelpfulByReview(review);
				review.setPositive(positive);
				review.setNegative(review.getVotes().size() - positive);

				// our own review
				if (customer != null && review.getCustomer().getId() == customer.getId()) {
					review.vote();
					continue;
				}

				// ... and their respective votes
				List<Vote> votes = review.getVotes();
				Iterator<Vote> iv = votes.iterator();
				while (iv.hasNext()) {
					Vote vote = iv.next();
					if (customer != null && vote.getCustomer() == customer.getId()) {
						// we have already voted this review!
						review.vote();
					}
				}
			}
        }

        // add a nonce to the session and the form
        String nonce = CustomerDAO.generateSalt();
        request.setAttribute("nonce", nonce);
		session.setAttribute("nonce", nonce);
        
        return new ActionResponse(ActionResponseType.FORWARD, "viewBook");
    }
}
