package amu.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.BookListDAO;
import amu.model.Book;
import amu.model.BookList;
import amu.model.Customer;

public class ViewListAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		Customer customer = (Customer) session.getAttribute("customer");
		
		List<String> messages = new ArrayList<String>();
        request.setAttribute("messages", messages);
        request.setAttribute("authorized", false);
		
        // get and verify list ID
        int id;
        try {
        	id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
        	messages.add("Invalid list ID");
        	return new ActionResponse(ActionResponseType.FORWARD, "viewList");
        }
        
        // get the list
		BookListDAO listDAO = new BookListDAO();
		BookList list = listDAO.findById(id);

		// check that we are logged in
		if (customer == null && !list.isPublic()) {
        	ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewList");

        	if (request.getMethod().equals("GET")) {
                ActionResponse responseBack = new ActionResponse(ActionResponseType.REDIRECT, "viewList");
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

		// check the owner
		if (customer != null && list.getCustomer() != customer.getId() && !list.isPublic()) {
			messages.add("Couldn't find that list.");
	        return new ActionResponse(ActionResponseType.FORWARD, "viewList");
		} else if (customer != null && list.getCustomer() == customer.getId()) {
			request.setAttribute("authorized", true);
		}
		
		// get the books in the list
		List<Book> books = listDAO.getBooks(list);
		request.setAttribute("books", books);

		request.setAttribute("list", list);
        return new ActionResponse(ActionResponseType.FORWARD, "viewList");
	}

}
