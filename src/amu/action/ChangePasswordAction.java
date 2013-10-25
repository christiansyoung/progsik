package amu.action;

import amu.Config;
import amu.database.CustomerDAO;
import amu.model.Customer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

class ChangePasswordAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");
        CustomerDAO customerDAO = new CustomerDAO();

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "changePassword");
            return actionResponse;
        }

        if (request.getMethod().equals("POST")) {
        	if (request.getMethod().equals("POST")) {
            	String request_csrf_token = request.getParameter("csrf_token");
            	if ( (request_csrf_token == null) || ("".equals(request_csrf_token)) ) {
            		ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
                    actionResponse.addParameter("from", "changePassword");
                    return actionResponse;
            	}
            	
            	String csrf_token = (String) session.getAttribute("csrf_token");
            	if (!request_csrf_token.equals(csrf_token)) {
            		ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
                    actionResponse.addParameter("from", "changePassword");
                    return actionResponse;
            	}
            	session.setAttribute("csrf_token", UUID.randomUUID().toString());
        	
            List<String> messages = new ArrayList<String>();
            request.setAttribute("messages", messages);

            String[] password = request.getParameterValues("password");
            
            //Validate that the old password match
            if(customer.getPassword().equals(CustomerDAO.hashPassword(password[0]))) {
            	 // Validate that new email is typed in the same both times
                if (password[1].equals(password[2])) {
                	messages.add("Password changed correctly");
                	customer.setPassword(CustomerDAO.hashPassword(password[1]));
                    if (customerDAO.edit(customer) == false) {
                        messages.add("An error occured.");
                        return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
                    }
                    //return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
                }
                else 
                	messages.add("Password and repeated password did not match. Please try again.");
            }
            
            else{
            	messages.add("The password typed in was incorrect");
            	return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }

        } 
        return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
    }
}
