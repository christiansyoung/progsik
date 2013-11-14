package amu;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public final static String JDBC_RESOURCE = "jdbc/bookstore";
    
    public final static String EMAIL_SMTP_HOST = "smtp.gmail.com";
    public final static String EMAIL_SMTP_PORT = "587";
    public final static String EMAIL_SMTP_USER = "tdt4237.amu.darya";
    public final static String EMAIL_SMTP_PASSWORD = "jcmrgkikcdqoyjms"; // Application-specific password
    
    public final static String EMAIL_FROM_ADDR = "tdt4237.amu.darya@gmail.com";
    public final static String EMAIL_FROM_NAME = "Amu-Darya";
    public final static Map<String, String> SUPPORT_ADDRESSES = new HashMap<String, String>() {{
    	put("Sales", "tdt4237.amu.darya@gmail.com");
    	put("Technical", "tdt4237.amu.darya@gmail.com");
    }};
}
