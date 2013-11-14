package amu.action;

import amu.database.CustomerDAO;
import amu.model.Customer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class ChangeNameAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");
        CustomerDAO customerDAO = new CustomerDAO();

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "changeName");
            return actionResponse;
        }

        if (request.getMethod().equals("POST")) {

            Map<String, String> messages = new HashMap<String, String>();
            request.setAttribute("messages", messages);

            String nonce = request.getParameter("nonce");
            if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
            	messages.put("name", "Something went wrong, please try again.");
            	return new ActionResponse(ActionResponseType.FORWARD, "changeName");
            }
            
            customer.setName(request.getParameter("name"));

            if (customerDAO.edit(customer)) { // Customer name was successfully changed
            	session.removeAttribute("nonce");
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            } else {
                messages.put("name", "Something went wrong here.");
                return new ActionResponse(ActionResponseType.FORWARD, "changeName");
            }
        }

        // add a nonce to the session and the form
        String nonce = CustomerDAO.generateSalt();
        request.setAttribute("nonce", nonce);
		session.setAttribute("nonce", nonce);
        
        // (request.getMethod().equals("GET")) {
        return new ActionResponse(ActionResponseType.FORWARD, "changeName");
    }
}
