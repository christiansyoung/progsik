package amu.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.ReviewDAO;
import amu.database.VoteDAO;
import amu.model.Customer;
import amu.model.Review;
import amu.model.Vote;

public class VoteReviewAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		Customer customer = (Customer) session.getAttribute("customer");

		if (customer == null) {
			ActionResponse actionResponse = new ActionResponse(
					ActionResponseType.REDIRECT, "loginCustomer");
			actionResponse.addParameter("from", "voteReview");

			if (request.getMethod().equals("POST")) {
				ActionResponse responseBack = new ActionResponse(
						ActionResponseType.REDIRECT, "voteReview");
				Map<String, String[]> parameters = request.getParameterMap();
				for (Map.Entry<String, String[]> parameter : parameters
						.entrySet()) {
					for (String value : parameter.getValue()) {
						responseBack.addParameter(parameter.getKey(), value);
					}
				}
				session.setAttribute("saved_parameters",
						responseBack.getParameterString());
			}
			return actionResponse;
		}

		// get isbn
		String isbn = request.getParameter("isbn");
		if (isbn == null) {
			return new ActionResponse(ActionResponseType.REDIRECT, "listBooks");
		}

		String nonce = request.getParameter("nonce");
		if (nonce == null || nonce.trim().equals("")
				|| !nonce.equals(session.getAttribute("nonce"))) {
			ActionResponse ar = new ActionResponse(ActionResponseType.REDIRECT,
					"viewBook");
			ar.addParameter("isbn", isbn);
			return ar;
		}

		// get vote
		String helpful = request.getParameter("helpful");
		if (helpful == null || !(helpful.equals("yes") || helpful.equals("no"))) {
			ActionResponse ar = new ActionResponse(ActionResponseType.REDIRECT,
					"viewBook");
			ar.addParameter("isbn", isbn);
			return ar;
		}

		// get review
		int review_id;
		try {
			review_id = Integer.parseInt(request.getParameter("review"));
		} catch (Exception e) {
			ActionResponse ar = new ActionResponse(ActionResponseType.REDIRECT,
					"viewBook");
			ar.addParameter("isbn", isbn);
			return ar;
		}

		// check if review exists
		ReviewDAO reviewDAO = new ReviewDAO();
		Review review = reviewDAO.findByID(review_id);
		if (review == null) {
			ActionResponse ar = new ActionResponse(ActionResponseType.REDIRECT,
					"viewBook");
			ar.addParameter("isbn", isbn);
			return ar;
		}

		// check if the customer wrote the review
		if (review.getCustomer().getId() == customer.getId()) {
			ActionResponse ar = new ActionResponse(ActionResponseType.REDIRECT,
					"viewBook");
			ar.addParameter("isbn", isbn);
			return ar;
		}

		// check if we already voted
		List<Vote> votes = review.getVotes();
		Iterator<Vote> iterator = votes.iterator();
		while (iterator.hasNext()) {
			Vote v = iterator.next();
			if (v.getCustomer() == customer.getId()) {
				ActionResponse ar = new ActionResponse(
						ActionResponseType.REDIRECT, "viewBook");
				ar.addParameter("isbn", isbn);
				return ar;
			}
		}

		VoteDAO voteDAO = new VoteDAO();
		Vote vote = new Vote();
		vote.setCustomer(customer.getId());
		vote.setReview(review_id);
		vote.setHelpful(helpful.equals("yes"));
		voteDAO.add(vote);

		session.removeAttribute("nonce");
		ActionResponse finalResponse = new ActionResponse(
				ActionResponseType.REDIRECT, "viewBook");
		finalResponse.addParameter("isbn", isbn);
		return finalResponse;
	}

}
