package amu.action;

import amu.model.BookList;
import amu.model.Cart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

class ViewListAction implements Action {

    public ViewListAction() {
    }

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        BookList list = (BookList) session.getAttribute("bookList");
        
        if (list == null)
        {
            list = new BookList();
            session.setAttribute("list", list);
        }
        
        return new ActionResponse(ActionResponseType.FORWARD, "viewBookList"); //This is the url in which one should be forwarded to
    }
    
}
