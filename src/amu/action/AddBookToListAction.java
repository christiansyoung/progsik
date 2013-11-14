package amu.action;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookDAO;
import amu.database.BookListDAO;
import amu.model.Book;
import amu.model.BookList;
import amu.model.Customer;

public class AddBookToListAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		Customer customer = (Customer) session.getAttribute("customer");

		if (customer == null) {
			ActionResponse actionResponse = new ActionResponse(
					ActionResponseType.REDIRECT, "loginCustomer");
			actionResponse.addParameter("from", "addBookToList");

			if (request.getMethod().equals("POST")) {
				ActionResponse responseBack = new ActionResponse(
						ActionResponseType.REDIRECT, "addBookToList");
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

		// find book
		BookDAO bookDAO = new BookDAO();
		Book book = bookDAO.findByISBN(isbn);
		if (book == null) {
			return new ActionResponse(ActionResponseType.REDIRECT, "listBooks");
		}

		// get list
		int list_id;
		try {
			list_id = Integer.parseInt(request.getParameter("list"));
		} catch (Exception e) {
			ActionResponse ar = new ActionResponse(ActionResponseType.REDIRECT,
					"viewBook");
			ar.addParameter("isbn", isbn);
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "error get list");
			return ar;
		}

		// check if list exists and is owned by the user
		BookListDAO listDAO = new BookListDAO();
		BookList list = listDAO.findById(list_id);
		if (list == null || customer.getId() != list.getCustomer()) {
			ActionResponse ar = new ActionResponse(ActionResponseType.REDIRECT,
					"viewBook");
			ar.addParameter("isbn", isbn);
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "list doesnt exist or is not owned by the user");
			return ar;
		}
		
		// check if it's already in the list
		if (!listDAO.isBookOnList(list, book)) {
			// no, add it!
			list = listDAO.addBook(list, book);
		}
		
		session.removeAttribute("nonce");
		ActionResponse finalResponse = new ActionResponse(
				ActionResponseType.REDIRECT, "viewBook");
		finalResponse.addParameter("isbn", isbn);
		return finalResponse;
	}

}
