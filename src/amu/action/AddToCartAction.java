package amu.action;

import amu.database.BookDAO;
import amu.model.Book;
import amu.model.Cart;
import amu.model.CartItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class AddToCartAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        int quantity = 0;
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null)
        {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        
        try {
        	quantity = Math.abs(Integer.parseInt(request.getParameter("quantity")));
        } catch (Exception e) { }
        
        if (request.getParameter("isbn") != null && quantity != 0)
        {
            BookDAO bookDAO = new BookDAO();
            Book book = bookDAO.findByISBN(request.getParameter("isbn"));
            
            cart.addItem(new CartItem(book, quantity));
        }

        return new ActionResponse(ActionResponseType.REDIRECT, "viewCart");
    }
    
}
