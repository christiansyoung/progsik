package amu.action;

import amu.database.CreditCardDAO;
import amu.model.CreditCard;
import amu.model.Customer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

class AddCreditCardAction implements Action {
    
    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "loginCustomer");
            actionResponse.addParameter("from", "addCreditCard");
            return actionResponse;
        }
        
        if (request.getMethod().equals("POST")) {
            Map<String, String> messages = new HashMap<String, String>();
            request.setAttribute("messages", messages);
            
            Calendar expiryDate = Calendar.getInstance();
            try {
            	expiryDate.set(Integer.parseInt(request.getParameter("expiryYear")), Integer.parseInt(request.getParameter("expiryMonth")), 1);
            }
            catch(Exception e) {
            	messages.put("error", "Could not parse dates.");
            }
            
            // Sanitize user input
            //String cardNumber = Jsoup.clean(request.getParameter("creditCardNumber"), Whitelist.none());
            //String cardName = Jsoup.clean(request.getParameter("cardholderName"), Whitelist.none());
            String cardNumber = request.getParameter("creditCardNumber");
            String cardName   = request.getParameter("cardholderName");
            CreditCardDAO creditCardDAO = new CreditCardDAO();
            CreditCard creditCard = new CreditCard(
                    customer, 
                    cardNumber, 
                    expiryDate,
                    cardName);

            Map<String, String> values = new HashMap<String, String>();
            request.setAttribute("values", values);
            values.put("creditCardNumber", cardNumber);
            values.put("expiryDate", expiryDate.toString());
            values.put("cardholderName", cardName);
            
            if (creditCardDAO.add(creditCard)) {
                return new ActionResponse(ActionResponseType.REDIRECT, "viewCustomer");
            }
            
            messages.put("error", "An error occured.");
        }
        
        // (request.getMethod().equals("GET"))
        Calendar calendar = new GregorianCalendar();
        
        List<String> years = new ArrayList<String>();
        request.setAttribute("years", years);
        for (Integer offset = 0; offset < 10; offset++) {
            years.add(Integer.valueOf(calendar.get(Calendar.YEAR) + offset).toString());
        }

        Map<String, String> months = new HashMap<String, String>();
        request.setAttribute("months", months);
        for (Integer month = 0; month < 12; month++) {
            months.put(month.toString(), Integer.valueOf(month - 1).toString());
        }
        
        return new ActionResponse(ActionResponseType.FORWARD, "addCreditCard");
    }
}
