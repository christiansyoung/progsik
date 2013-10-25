<%@page import="java.sql.*"%>
<%@page import="amu.database.Database"%>
<div class="container">
    <h1>Book</h1>
    <c:choose>
        <c:when test="${empty book}">
            <h2>Book not found!</h2>
            <div class = "index-item"><a href="debug/list_books.jsp">List books</a></div>
        </c:when>
        <c:otherwise>
            <h2>${book.title.name}</h2>
            <div>
                <ul>
                    <li>
                        <b>Authors:</b> 
                        <c:forEach items="${book.author}" var="author" varStatus="it">
                            ${author.name}<c:if test="${!it.last}">, </c:if>
                        </c:forEach>
                    </li>
                    <li><b>Publisher:</b> ${book.publisher.name}</li>
                    <li><b>Published:</b> ${book.published}</li>
                    <li><b>Edition:</b> ${book.edition} (${book.binding})</li>
                    <li><b>ISBN:</b> ${book.isbn13}</li>
                    <li><b>Price:</b> ${book.price}</li>
                </ul>
            </div>
            <div>
                ${book.description}
            </div>
            <div>
                <form action="addBookToCart.do" method="post">
                    <input type="hidden" name="isbn" value="${book.isbn13}" />
                    <input type="text" name="quantity" value="1" />
                    <input type="submit" value="Add to cart" />
                </form>
            </div>
            <div>
             	<form action="addReview.do" method="post">
             		<label for="text">Add review: </label><br />
                 	<textarea id="text" name="text" rows="5" cols="20">${review.text}</textarea><br />
            		<label for="rating">Add rating 1-5: </label><br />
            		<input type="number" min="1" max="5" id="rating" name="rating" value="${review.rating}" />
            		<input type="hidden" name="bookid" id="bookid" value="${book.isbn13}" />
                    <input type="submit" value="Add review" />
                </form>
            </div>
            <%

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    
    try {
        connection = Database.getConnection();
        statement = connection.createStatement();
        
        String query = "SELECT "
                + ""
                + "review.text, "
                + "review.rating, "
                + "customer.name, "
                + "FROM review, book, customer "
                + "WHERE book.id = review.book_id AND customer.id = review.customer_id;";
        resultSet = statement.executeQuery(query);
        
        while (resultSet.next()) {
            out.println("<h4>Review by: ");
            out.println(resultSet.getString("customer.name"));
            out.println("</h4>");
            
            out.println();
            out.println("Text:" + resultSet.getString("review.text"));
            
            out.println("Rating: " + resultSet.getString("review.rating"));
        }
    } catch (SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
    } finally {
        Database.close(connection, statement, resultSet);
    }
    
%>
        </c:otherwise>
    </c:choose>
</div>