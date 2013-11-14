package amu.action;

import amu.database.CustomerDAO;
import amu.model.Customer;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class ChangePasswordAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "changePassword");
            return actionResponse;
        }

        if (request.getMethod().equals("POST")) {
            List<String> messages = new ArrayList<String>();
            request.setAttribute("messages", messages);

            String nonce = request.getParameter("nonce");
            if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
            	messages.add("Something went wrong, please try again.");
            	return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }
            
            // get and validate input
            String old = request.getParameter("old");
            String[] password = request.getParameterValues("password");
            if (old == null || password == null || password.length != 2) {
            	messages.add("You need to specify both the old and the new password.");
            	return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }
            
            // validate old password
            String old_salt = customer.getSalt();
            if (!customer.getPassword().equals(CustomerDAO.hashPassword(old, old_salt))) {
            	messages.add("The old password does not match.");
            	return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }

            // Validate that new email is typed in the same both times
            if (password[0].equals(password[1]) == false) {
                messages.add("Password and repeated password did not match. Please try again.");
                return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }

            // Validation OK, do business logic
            String new_salt = CustomerDAO.generateSalt();
            CustomerDAO customerDAO = new CustomerDAO();
            customer.setPassword(CustomerDAO.hashPassword(password[0], new_salt));
            customer.setSalt(new_salt);
            if (customerDAO.edit(customer) == false) {
                messages.add("An error occured.");
                return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
            }
            
            // Email change successful, return to viewCustomer
            session.removeAttribute("nonce");
            return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");

        } 

        // add a nonce to the session and the form
        String nonce = CustomerDAO.generateSalt();
        request.setAttribute("nonce", nonce);
		session.setAttribute("nonce", nonce);
        
        // (request.getMethod().equals("GET")) 
        return new ActionResponse(ActionResponseType.FORWARD, "changePassword");
    }
}
