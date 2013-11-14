package amu.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import amu.database.BookListDAO;
import amu.model.BookList;

public class ShowListsAction implements Action {

	@Override
	public ActionResponse execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		BookListDAO listDAO = new BookListDAO();

		List<BookList> lists = listDAO.findAllPublic();
        request.setAttribute("lists", lists);
    	
        return new ActionResponse(ActionResponseType.FORWARD, "showLists");
	}

}
