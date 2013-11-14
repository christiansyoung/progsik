package amu.action;

import amu.database.AddressDAO;
import amu.model.Address;
import amu.model.Cart;
import amu.model.Customer;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class SelectShippingAddressAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Customer customer = (Customer) session.getAttribute("customer");

        if (cart == null) {
            return new ActionResponse(ActionResponseType.REDIRECT, "viewCart");
        }

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "selectShippingAddress");
            return actionResponse;
        }

        AddressDAO addressDAO = new AddressDAO();
        List<Address> addresses = addressDAO.browse(customer);
        request.setAttribute("addresses", addresses);
        
        // Handle shipping address selection submission
        String id = request.getParameter("id");
        if (id != null) {
        	try {
	        	int addrid = Integer.parseInt(id);
	            Iterator<Address> iterator = addresses.iterator();
	            while (iterator.hasNext()) {
	            	Address address = iterator.next();
	            	if (address.getId() == addrid) {
	            		cart.setShippingAddress(addressDAO.read(addrid));
	            		return new ActionResponse(ActionResponseType.REDIRECT, "selectPaymentOption");
	            	}
	            }
        	} catch(Exception e) {}
        }

        // Else GET request
        return new ActionResponse(ActionResponseType.FORWARD, "selectShippingAddress");
    }
}
