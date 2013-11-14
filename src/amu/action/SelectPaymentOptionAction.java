package amu.action;

import amu.database.CreditCardDAO;
import amu.database.CustomerDAO;
import amu.model.Cart;
import amu.model.CreditCard;
import amu.model.Customer;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class SelectPaymentOptionAction implements Action {

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
            actionResponse.addParameter("from", "selectPaymentOption");
            return actionResponse;
        }
        
        if (cart.getShippingAddress() == null) {
            return new ActionResponse(ActionResponseType.REDIRECT, "selectShippingAddress");
        }

        CreditCardDAO creditCardDAO = new CreditCardDAO();
        List<CreditCard> creditCards = creditCardDAO.browse(customer);
        request.setAttribute("creditCards", creditCards);
        
        // Handle credit card selection submission
        if (request.getMethod().equals("POST")) {

            String nonce = request.getParameter("nonce");
            if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
            	return new ActionResponse(ActionResponseType.FORWARD, "selectPaymentOption");
            }
            
        	try {
	        	int cardId = Integer.parseInt(request.getParameter("creditCardID"));
	            Iterator<CreditCard> iterator = creditCards.iterator();
	            while (iterator.hasNext()) {
	            	CreditCard card = iterator.next();
	            	if (card.getId() == cardId) {
	            		cart.setCreditCard(card);
	            		session.removeAttribute("nonce");
	            		return new ActionResponse(ActionResponseType.REDIRECT, "reviewOrder");
	            	}
	            }
        	} catch(Exception e) {}
        }

        // add a nonce to the session and the form
        String nonce = CustomerDAO.generateSalt();
        request.setAttribute("nonce", nonce);
		session.setAttribute("nonce", nonce);

        // Else GET request
        return new ActionResponse(ActionResponseType.FORWARD, "selectPaymentOption");
    }

}
