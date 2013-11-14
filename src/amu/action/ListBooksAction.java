package amu.action;

import amu.database.BookDAO;
import amu.model.Book;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class ListBooksAction implements Action {

    @Override
    public ActionResponse execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BookDAO bookDAO = new BookDAO();

    	List<Book> books = bookDAO.findAll();
        request.setAttribute("books", books);
    	
        return new ActionResponse(ActionResponseType.FORWARD, "listBooks");
    }
}