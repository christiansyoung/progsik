package amu.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import amu.Mailer;
import amu.database.CustomerDAO;
import amu.model.Customer;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class RegisterCustomerAction extends HttpServlet implements Action {
    
    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        if (request.getMethod().equals("POST")) {
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer = customerDAO.findByEmail(request.getParameter("email"));

            if (customer == null) {
                customer = new Customer();
                if(!verifyEmail(request.getParameter("email")))
                        return new ActionResponse(ActionResponseType.REDIRECT, "registrationEmailError"); //Burde kanskje lage en ny.
                customer.setEmail(request.getParameter("email"));
                customer.setName(request.getParameter("name"));
                customer.setPassword(CustomerDAO.hashPassword(request.getParameter("password")));
                customer.setActivationToken(CustomerDAO.generateActivationCode());
                customer = customerDAO.register(customer);
                
                ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "activateCustomer");
                actionResponse.addParameter("email", customer.getEmail());
                
                StringBuilder sb = new StringBuilder();
                sb.append("Welcome to Amu-Darya, the stupidly secure bookstore!\n\n");
                sb.append("To activate your account, click <a href='http://");
                sb.append(request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
                sb.append(actionResponse.getURL() + actionResponse.getParameterString());
                sb.append("&activationToken=" + customer.getActivationToken());
                sb.append("'>here</a>, or use this activation code: " + customer.getActivationToken());
               
                Mailer.send(customer.getEmail(), "Activation required", sb.toString());
 
                return actionResponse;
            } else {
                return new ActionResponse(ActionResponseType.REDIRECT, "registrationError");
            }
        }
        
        // Else we show the register form
        return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
    }
    // Check to see if the string is a valid email-adress.
    private boolean verifyEmail(String param) {
        if(param == null)
            return false;
        String emailPattern = "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+
                              "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(param);
        return matcher.matches();
    }
}
