package amu.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookListDAO;
import amu.database.CustomerDAO;
import amu.model.BookList;
import amu.model.Customer;

public class EditListAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
			ActionResponse actionResponse = new ActionResponse(
					ActionResponseType.REDIRECT, "loginCustomer");
			actionResponse.addParameter("from", "editList");

			if (request.getMethod().equals("GET")) {
				ActionResponse responseBack = new ActionResponse(
						ActionResponseType.REDIRECT, "editList");
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

        List<String> messages = new ArrayList<String>();
        request.setAttribute("messages", messages);

        // get list id
        int listId;
        try {
        	listId = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
        	messages.add("Cannot find list.");
        	return new ActionResponse(ActionResponseType.FORWARD, "editList");
        }

        // check if the list exists and is owned by the user
        BookListDAO listDAO = new BookListDAO();
        List<BookList> lists = listDAO.findByCustomer(customer);
        Iterator<BookList> iterator = lists.iterator();
        while (iterator.hasNext()) {
        	BookList list = iterator.next();
        	if (list.getId() == listId) { // the address is owned by the customer
        		if (request.getMethod().equals("POST")) {
        			String nonce = request.getParameter("nonce");
                    if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
                    	messages.add("Something went wrong, please try again.");
                    	return new ActionResponse(ActionResponseType.FORWARD, "editList");
                    }

                    // get and verify title
                    String title = request.getParameter("title");
                    if (title == null || title.trim().equals("")) {
                    	messages.add("Please, specify a title for your list.");
                    	return new ActionResponse(ActionResponseType.FORWARD, "addList");
                    }
                    request.setAttribute("title", title);
                    
                    //get and verify description
                    String description = request.getParameter("description");
                    if (description == null || description.trim().equals("")) {
                    	messages.add("Please, specify a description for your list.");
                    	return new ActionResponse(ActionResponseType.FORWARD, "addList");
                    }
                    request.setAttribute("description", description);
                    
                    // get public checkbox
                    String public_param = request.getParameter("public");
                    Boolean is_public = false;
                    if (public_param == null) {
                    	messages.add("Please, tell us if the list should be public or not.");
                    	return new ActionResponse(ActionResponseType.FORWARD, "addList");
                    }
                    is_public = public_param.equals("on");
                    
                    list.setTitle(title);
                    list.setDescription(description);
                    list.setPublic(is_public);
                    
                    if (listDAO.edit(list)) {
        				session.removeAttribute("nonce");
        				ActionResponse ar = new ActionResponse(ActionResponseType.REDIRECT, "viewList");
        				ar.addParameter("id", Integer.toString(listId));
        				return ar;
        			}
        		} else { // (request.getMethod().equals("GET"))
        			request.setAttribute("list", list);

        	        // add a nonce to the session and the form
        	        String nonce = CustomerDAO.generateSalt();
        	        request.setAttribute("nonce", nonce);
        			session.setAttribute("nonce", nonce);

        			return new ActionResponse(ActionResponseType.FORWARD, "editList");
        		}
        		break;
        	}
        	
        }
        messages.add("An error occured.");
        return new ActionResponse(ActionResponseType.FORWARD, "editList");
	}

}
