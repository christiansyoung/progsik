package amu.action;

import amu.Config;
import amu.Mailer;
import amu.database.CustomerDAO;
import amu.model.Customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class CustomerSupportAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		Customer customer = (Customer) session.getAttribute("customer");

		if (customer == null) {
			ActionResponse actionResponse = new ActionResponse(
					ActionResponseType.REDIRECT, "loginCustomer");
			actionResponse.addParameter("from", "customerSupport");
			return actionResponse;
		}

		if (request.getMethod().equals("POST")) {

            String nonce = request.getParameter("nonce");
            if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
            	return new ActionResponse(ActionResponseType.FORWARD, "customerSupport");
            }
            
			// validate department
			String department = request.getParameter("department");
			if (department != null) {
				if (Config.SUPPORT_ADDRESSES.containsKey(department)) {
					Mailer.send(Config.SUPPORT_ADDRESSES.get(department),
							request.getParameter("subject"),
							request.getParameter("content"),
							request.getParameter("fromAddr"),
							request.getParameter("fromName"));
					// TODO: Send receipt to customer
					session.removeAttribute("nonce");
					return new ActionResponse(ActionResponseType.REDIRECT,
							"customerSupportSuccessful");
				}
			}
		}

        // add a nonce to the session and the form
        String nonce = CustomerDAO.generateSalt();
        request.setAttribute("nonce", nonce);
		session.setAttribute("nonce", nonce);

		return new ActionResponse(ActionResponseType.FORWARD, "customerSupport");
	}
}
