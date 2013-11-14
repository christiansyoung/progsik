package amu.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookListDAO;
import amu.database.CustomerDAO;
import amu.model.BookList;
import amu.model.Customer;

public class DeleteListAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer"); 
            return actionResponse;
        }

        BookListDAO listDAO = new BookListDAO();
        List<BookList> lists = listDAO.findByCustomer(customer);
        int listId = Integer.parseInt(request.getParameter("id"));
        List<String> messages = new ArrayList<String>();
        request.setAttribute("messages", messages);

        Iterator<BookList> iterator = lists.iterator();
        while (iterator.hasNext()) {
        	BookList list = iterator.next();
        	if (list.getId() == listId) { // the list is owned by the customer
        		if (request.getMethod().equals("POST")) {
        			String nonce = request.getParameter("nonce");
                    if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
                    	messages.add("Something went wrong, please try again.");
                    	return new ActionResponse(ActionResponseType.FORWARD, "deleteList");
                    }

        			if (listDAO.delete(listId)) {
        				session.removeAttribute("nonce");
        				return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
        			}
        		} else { // (request.getMethod().equals("GET")) 
        			request.setAttribute("list", list);

        	        // add a nonce to the session and the form
        	        String nonce = CustomerDAO.generateSalt();
        	        request.setAttribute("nonce", nonce);
        			session.setAttribute("nonce", nonce);
        	        
        			return new ActionResponse(ActionResponseType.FORWARD, "deleteList");
        		}
        			
        	}
        }
        messages.add("An error occured."); 
        return new ActionResponse(ActionResponseType.FORWARD, "deleteList");
	}

}
