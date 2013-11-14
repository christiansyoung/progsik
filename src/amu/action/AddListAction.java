package amu.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookListDAO;
import amu.database.CustomerDAO;
import amu.model.BookList;
import amu.model.Customer;

public class AddListAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        // Handle referrals
        Map<String, String> values = new HashMap<String, String>();
        request.setAttribute("values", values);
        if (ActionFactory.hasKey(request.getParameter("from"))) {
            values.put("from", request.getParameter("from"));
        }

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "addList");
            return actionResponse;
        }

        request.setAttribute("public", false);

        // Non-idempotent add address request
        if (request.getMethod().equals("POST")) {
            List<String> messages = new ArrayList<String>();
            request.setAttribute("messages", messages);
            boolean error = false;

            // verify nonce
            String nonce = request.getParameter("nonce");
            if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
            	messages.add("Something went wrong, please try again.");
            	error = true;
            }

            // get and verify title
            String title = request.getParameter("title");
            if (title == null || title.trim().equals("")) {
            	messages.add("Please, specify a title for your list.");
            	error = true;
            }
            request.setAttribute("title", title);
            
            //get and verify description
            String description = request.getParameter("description");
            if (description == null || description.trim().equals("")) {
            	messages.add("Please, specify a description for your list.");
            	error = true;
            }
            request.setAttribute("description", description);
            
            // get public checkbox
            String public_param = request.getParameter("public");
            Boolean is_public = false;
            if (public_param != null) {
            	is_public = public_param.equals("on");
            	request.setAttribute("public", is_public);
            }
            
            if (error) {
            	return new ActionResponse(ActionResponseType.FORWARD, "addList");
            }
            
            BookListDAO listDAO = new BookListDAO();
            BookList list = new BookList();
            list.setTitle(title);
            list.setDescription(description);
            list.setCustomer(customer.getId());
            list.setPublic(is_public);

            if (listDAO.add(list)) {
            	session.removeAttribute("nonce");
                if (ActionFactory.hasKey(request.getParameter("from"))) {
                    return new ActionResponse(ActionResponseType.REDIRECT, request.getParameter("from"));
                } else {
                    // Return to viewCustomer from addList by default
                    return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
                }
            }

            messages.add("An error occured.");
        }
        
        // add a nonce to the session and the form
        String nonce = CustomerDAO.generateSalt();
        request.setAttribute("nonce", nonce);
		session.setAttribute("nonce", nonce);

        // (request.getMethod().equals("GET")) 
        return new ActionResponse(ActionResponseType.FORWARD, "addList");
	}

}
