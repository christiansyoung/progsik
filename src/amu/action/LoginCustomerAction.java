package amu.action;

import amu.database.CustomerDAO;
import amu.model.Cart;
import amu.model.Customer;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class LoginCustomerAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	if (!request.isSecure()) {
    		// redirect the user to HTTPs
    		String port = (System.getProperty("HTTPS_LISTENER_PORT") != null) ? ":"+System.getProperty("HTTPS_LISTENER_PORT") : "";
    		String url = "https://"+request.getServerName()+port+request.getRequestURI().replace(".do", "");
    		ActionResponse ar = new ActionResponse(ActionResponseType.REDIRECT, url);
    		if (request.getQueryString() != null) {
    			ar.setParameterString("?"+request.getQueryString());
    		}
    		return ar;
    	}
    	
        Map<String, String> values = new HashMap<String, String>();
        HttpSession session = request.getSession(true);
        request.setAttribute("values", values);
        if (ActionFactory.hasKey(request.getParameter("from"))) {
            values.put("from", request.getParameter("from"));
        }

        if (request.getMethod().equals("POST")) {

            Map<String, String> messages = new HashMap<String, String>();
            request.setAttribute("messages", messages);
            request.setAttribute("action", "loginCustomer");
            
            String nonce = request.getParameter("nonce");
            if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
            	messages.put("error", "Something went wrong, please try again.");
            	return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
            }

            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = customerDAO.findByEmail(request.getParameter("email"));

            values.put("email", request.getParameter("email"));
            Thread.sleep(1000);
            if (customer != null) {
				String salt = customer.getSalt();
				if (customer.getPassword().equals(CustomerDAO.hashPassword(request.getParameter("password"), salt))) {
					if (customer.getActivationToken() != null) {
						ActionResponse action_response = new ActionResponse(ActionResponseType.REDIRECT,
																			"activateCustomer");
						action_response.addParameter("email", customer.getEmail());
						return action_response;
					}

					String query = (String) session.getAttribute("saved_parameters");
					session.removeAttribute("saved_parameters");
					Cart cart = (Cart) session.getAttribute("cart");

					session.invalidate();
					session = request.getSession();
					session.setAttribute("cart", cart);
					session.setAttribute("customer", customer);
					session.removeAttribute("nonce");

					if (ActionFactory.hasKey(request.getParameter("from"))) {
						ActionResponse action_response = new ActionResponse(
								ActionResponseType.REDIRECT,
								request.getParameter("from"));
						action_response.setParameterString(query);
						return action_response;
					}
				} else { // Wrong password
					messages.put("error", "Invalid username or password.");
				}
            } else { // findByEmail returned null -> no customer with that email exists
                messages.put("error", "Invalid username or password.");
            }

            // Forward to login form with error messages
            return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
        }

        // add a nonce to the session and the form
        String nonce = CustomerDAO.generateSalt();
        request.setAttribute("nonce", nonce);
		session.setAttribute("nonce", nonce);
        
        return new ActionResponse(ActionResponseType.FORWARD, "loginCustomer");
    }
}
