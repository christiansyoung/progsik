package amu.action;

import amu.database.AddressDAO;
import amu.database.CustomerDAO;
import amu.model.Address;
import amu.model.Customer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class EditAddressAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer");
            return actionResponse;
        }
        
        AddressDAO addressDAO = new AddressDAO();
        List<Address> addresses = addressDAO.browse(customer);
        int addressId = Integer.parseInt(request.getParameter("id"));
        List<String> messages = new ArrayList<String>();
        request.setAttribute("messages", messages);

        Iterator<Address> iterator = addresses.iterator();
        while (iterator.hasNext()) {
        	Address address = iterator.next();
        	if (address.getId() == addressId) { // the address is owned by the customer
        		if (request.getMethod().equals("POST")) {
        			String nonce = request.getParameter("nonce");
                    if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
                    	messages.add("Something went wrong, please try again.");
                    	return new ActionResponse(ActionResponseType.FORWARD, "editAddress");
                    }

                    address.setAddress(request.getParameter("address"));
        			if (addressDAO.edit(address)) {
        				session.removeAttribute("nonce");
        				return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
        			}
        		} else { // (request.getMethod().equals("GET"))
        			request.setAttribute("address", address);

        	        // add a nonce to the session and the form
        	        String nonce = CustomerDAO.generateSalt();
        	        request.setAttribute("nonce", nonce);
        			session.setAttribute("nonce", nonce);

        			return new ActionResponse(ActionResponseType.FORWARD, "editAddress");
        		}
        		break;
        	}
        	
        }
        messages.add("An error occured.");
        return new ActionResponse(ActionResponseType.FORWARD, "editAddress");
    }

}
