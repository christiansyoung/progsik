package amu.action;

import amu.database.CustomerDAO;
import amu.model.Customer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class ChangeEmailAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "changeEmail");
            return actionResponse;
        }

        if (request.getMethod().equals("POST")) {
        	String request_csrf_token = request.getParameter("csrf_token");
        	if ( (request_csrf_token == null) || ("".equals(request_csrf_token)) ) {
        		ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
                actionResponse.addParameter("from", "changeEmail");
                return actionResponse;
        	}
        	
        	String csrf_token = (String) session.getAttribute("csrf_token");
        	if (!request_csrf_token.equals(csrf_token)) {
        		ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
                actionResponse.addParameter("from", "changeEmail");
                return actionResponse;
        	}		
        	session.setAttribute("csrf_token", UUID.randomUUID().toString());
        	
            Map<String, String[]> values = new HashMap<String, String[]>();
            request.setAttribute("values", values);

            List<String> messages = new ArrayList<String>();
            request.setAttribute("messages", messages);

            String[] email = request.getParameterValues("email");
            values.put("email", email);

            // Validate that new email is typed in the same both times
            if (email[0].equals(email[1]) == false) {
                messages.add("New email and repeated email did not match. Please check for typing errors.");
                return new ActionResponse(ActionResponseType.FORWARD, "changeEmail");
            }

            // Validation OK, do business logic
            CustomerDAO customerDAO = new CustomerDAO();
            customer.setEmail(email[0]);
            if (customerDAO.edit(customer) == false) {
                messages.add("DB update unsuccessful, likely there is already a user with this email address.");
                return new ActionResponse(ActionResponseType.FORWARD, "changeEmail");
            }
            
            // Email change successful, return to viewCustomer
            return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");

        } 
        
        // (request.getMethod().equals("GET")) 
        return new ActionResponse(ActionResponseType.FORWARD, "changeEmail");
    }
}
