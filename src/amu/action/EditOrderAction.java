package amu.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import amu.database.AddressDAO;
import amu.database.CustomerDAO;
import amu.database.OrderDAO;
import amu.model.Address;
import amu.model.Customer;
import amu.model.Order;

public class EditOrderAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");
        List<String> messages = new ArrayList<String>();
        request.setAttribute("messages", messages);

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "viewCustomer"); 
            return actionResponse;
        }

        OrderDAO orderDAO = new OrderDAO();
        List<Order> orders = orderDAO.browse(customer);
        
        AddressDAO addressDAO = new AddressDAO();
        List<Address> addresses = addressDAO.browse(customer);
        
        // parse and validate order id
        int orderId;
        try {
        	orderId = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
        	messages.add("Couldn't find the order specified");
        	return new ActionResponse(ActionResponseType.FORWARD, "editOrder");
        }

        Iterator<Order> iterator = orders.iterator();
        while (iterator.hasNext()) {
        	Order order = iterator.next();
        	if (order.getId() == orderId) { // the order is owned by the customer
        		if (order.getStatus() != Order.PENDING) { // the order is not pending
        			messages.add("You cannot edit this order.");
        			return new ActionResponse(ActionResponseType.FORWARD, "editOrder");
        		}

        		if (request.getMethod().equals("POST")) {
        			String nonce = request.getParameter("nonce");
                    if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
                    	messages.add("Something went wrong, please try again.");
                    	return new ActionResponse(ActionResponseType.FORWARD, "editOrder");
                    }
                    
                    // parse and validate address id
                    int addrId;
                    try {
                    	addrId = Integer.parseInt(request.getParameter("address"));
                    } catch (Exception e) {
                    	messages.add("Invalid address.");
                    	return new ActionResponse(ActionResponseType.FORWARD, "editOrder");
                    }
                    
                    Iterator<Address> ai = addresses.iterator();
                    while (ai.hasNext()) {
                    	Address address = ai.next();
                    	if (address.getId() == addrId && !address.equals(order.getAddress())) {
                    		// valid address, do the stuff
                    		order.setAddress(address);
                    		if (orderDAO.edit(order)) {
                    			session.removeAttribute("nonce");
                    			return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
                    		}
                    	}
                    }
        		} else { // (request.getMethod().equals("GET")) 
        			request.setAttribute("order", order);
        			addresses.remove(order.getAddress());
        			request.setAttribute("addresses", addresses);

        	        // add a nonce to the session and the form
        	        String nonce = CustomerDAO.generateSalt();
        	        request.setAttribute("nonce", nonce);
        			session.setAttribute("nonce", nonce);
        	        
        			return new ActionResponse(ActionResponseType.FORWARD, "editOrder");
        		}
        			
        	}
        }
        messages.add("An error occured."); 
        return new ActionResponse(ActionResponseType.FORWARD, "editOrder");
	}

}
