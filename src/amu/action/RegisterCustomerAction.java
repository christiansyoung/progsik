package amu.action;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

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

    	HttpSession session = request.getSession(true);

        if (request.getMethod().equals("POST")) {
        	Map<String, String> values = new HashMap<String, String>();
            request.setAttribute("values", values);
            CustomerDAO customerDAO = new CustomerDAO();
            Customer customer;

            String nonce = request.getParameter("nonce");
            if (nonce == null || nonce.trim().equals("") || !nonce.equals(session.getAttribute("nonce"))) {
            	request.setAttribute("register_error", "Something went wrong, please try again.");
            	return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
            } else {
            	request.setAttribute("nonce", nonce);
            }
            
            // email validations
            String[] emails = request.getParameterValues("email");
            if (emails == null || emails.length != 2) {
            	return new ActionResponse(ActionResponseType.REDIRECT, "registrationError");
            }
            if (!emails[0].trim().equals(emails[1].trim())) {
            	request.setAttribute("register_error", "Email addresses differ.");
            	return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
            }
            String email = emails[0].trim();
            if (email.equals("")) {
            	request.setAttribute("register_error", "Please specify an email address.");
            	return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
            }
            values.put("email", email);
            
            // name validations
            String name = request.getParameter("name");
            if (name == null || name.trim().equals("")) {
            	request.setAttribute("register_error", "Please specify a name.");
            	return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
            }
            values.put("name", name);
            
            // password validations
            String[] passwords = request.getParameterValues("password");
            if (passwords == null || passwords.length != 2) {
            	return new ActionResponse(ActionResponseType.REDIRECT, "registrationError");
            }
            if (!passwords[0].trim().equals(passwords[1].trim())) {
            	request.setAttribute("register_error", "Passwords differ.");
            	return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
            }
            String password = passwords[0].trim();
            if (password.equals("")) {
            	request.setAttribute("register_error", "Please specify a password.");
            	return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
            }
            values.put("password", password);

            // verify captcha
            try {
                URL url = new URL("http://www.google.com/recaptcha/api/verify");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                
                String pkey = "privatekey=6LfsoegSAAAAAJ-12ClVdsl-psuBS1c1R1yNkcN0";
                String rip  = "remoteip="+request.getRemoteAddr();
                String challenge = "challenge="+request.getParameter("recaptcha_challenge_field");
                String uresponse = "response="+request.getParameter("recaptcha_response_field");
                String params = pkey+"&"+rip+"&"+challenge+"&"+uresponse;
                
                conn.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(params);
                wr.flush();
                wr.close();
                
        		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        		String captcha_result = in.readLine();
        		in.close();

        		if (!captcha_result.equals("true")) {
        			request.setAttribute("register_error", "Incorrect captcha, try again.");
                	return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
        		}

            } catch (MalformedURLException e) {
            	return new ActionResponse(ActionResponseType.REDIRECT, "registrationError");
            } catch (IOException e) {
            	return new ActionResponse(ActionResponseType.REDIRECT, "registrationError");
            }
            
            try {
            	customer = customerDAO.findByEmail(email);
            } catch (Exception ex) {
            	return new ActionResponse(ActionResponseType.REDIRECT, "registrationError");
            }
            

            if (customer == null) {
            	String salt = CustomerDAO.generateSalt();
                customer = new Customer();
                customer.setEmail(email);
                customer.setName(name);
                customer.setPassword(CustomerDAO.hashPassword(password, salt));
                customer.setActivationToken(CustomerDAO.generateActivationCode());
                customer.setSalt(salt);
                try {
                	customer = customerDAO.register(customer);
                } catch (Exception exception) {
                	return new ActionResponse(ActionResponseType.REDIRECT, "registrationError");
                }
                
                ActionResponse actionResponse = new ActionResponse(ActionResponseType.REDIRECT, "activateCustomer");
                actionResponse.addParameter("email", customer.getEmail());
                
                StringBuilder sb = new StringBuilder();
                sb.append("Welcome to Amu-Darya, the really insecure bookstore!\n\n");
                sb.append("To activate your account, click <a href='http://");
                sb.append(request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/");
                sb.append(actionResponse.getURL() + actionResponse.getParameterString());
                sb.append("&activationToken=" + customer.getActivationToken());
                sb.append("'>here</a>, or use this activation code: " + customer.getActivationToken());
               
                Mailer.send(customer.getEmail(), "Activation required", sb.toString());
 
                session.removeAttribute("nonce");
                return actionResponse;
            } else {
            	request.setAttribute("register_error", "The user already exists.");
            	return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
            }
        }

        // add a nonce to the session and the form
        String nonce = CustomerDAO.generateSalt();
        request.setAttribute("nonce", nonce);
		session.setAttribute("nonce", nonce);
        
        // Else we show the register form
        return new ActionResponse(ActionResponseType.FORWARD, "registerCustomer");
    }
}
