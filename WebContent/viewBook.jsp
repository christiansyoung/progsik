<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:choose>
	<c:when test="${empty book}">
		<div class="jumbotron">
			<h1>Book not found</h1>
			<p class="lead">We could not find that book in the database.
				Please go back to see another one, or try again later.</p>
			<p>
				<a class="btn btn-lg btn-success"
					href="<c:url value='/listBooks.do' />">Go back</a>
			</p>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-lg-1"></div>
			<div class="col-lg-4">
				<h2><c:out value="${book.title.name}" /></h2>
				<p>by <c:forEach items="${book.author}"
							var="author" varStatus="it">
                            ${author.name}<c:if test="${!it.last}">, </c:if>
						</c:forEach></p>
				<span style="color: #ffaa33;">
					<c:forEach begin="1" end="${book.getRating()}">
						<span class="icon-star"></span>
					</c:forEach>
					<c:forEach begin="1" end="${5 - book.getRating()}">
						<span class="icon-star-empty"></span>
					</c:forEach>
				</span>
				<br /><br />
				<form action="addBookToCart.do" method="post" class="form-inline">
						<input class="form-control" type="hidden" name="isbn"
							value="${book.isbn13}" />
						<div class="input-group">
							<span class="input-group-addon">${book.price}&nbsp;
								<span class="icon-bitcoin"></span></span>
							<input class="form-control" type="text" name="quantity" value="1" />
							<span class="input-group-btn">
							<button class="btn btn-success" type="submit">Add
								to cart</button></span>
						</div>
				</form>
				<br />
				<h4>Description</h4>
				<p><c:out value="${book.description}" /></p>
				<hr/>
				<h4>Product details</h4>
				<ul>
					<li><b>Publisher:</b> ${book.publisher.name}</li>
					<li><b>Published:</b> ${book.published}</li>
					<li><b>Edition:</b> ${book.edition} (${book.binding})</li>
					<li><b>ISBN-10:</b> ${book.isbn10}</li>
					<li><b>ISBN-13:</b> ${book.isbn13}</li>
				</ul>
				<c:if test="${not empty lists}">
					<hr/>
					<p>Add this book to your lists:</p>
					<form method="post" action="<c:url value="addBookToList.do" />">
						<input type="hidden" name="isbn" value="${book.isbn13}"/>
						<input type="hidden" name="nonce" value="${nonce}" />
						<div class="input-group">
							<select name="list" class="form-control">
								<c:forEach var="list" items="${lists}" varStatus="counter">
									<option value="${list.id}">${list.title}</option>
								</c:forEach>
							</select>
							<span class="input-group-btn">
							<button class="btn btn-success" type="submit">
								<span class="icon-plus-sign"></span>&nbsp;Add
							</button>
							</span>
						</div>
					</form>
				</c:if>
			</div>
			<div class="col-lg-6">
				<br />
				<h4>Customer reviews</h4>
				<p>${book.reviews.size()} review<c:if
					test="${fn:length(book.reviews) ne 1}">s</c:if>
				</p>
				<c:forEach items="${book.reviews}" var="review" varStatus="it">
					<p>
						<span style="color: #ffaa33;">
							<c:forEach begin="1" end="${review.rating}">
								<span class="icon-star"></span>
							</c:forEach>
							<c:forEach begin="1" end="${5 - review.rating}">
								<span class="icon-star-empty"></span>
							</c:forEach>
						</span>
						<span class="pull-right">
							<span class="label label-default">${review.votes.size()} votes</span>
							<span class="label label-success">
								<span class="icon-thumbs-up-alt"></span>&nbsp;${review.positive}
							</span>&nbsp;
							<span class="label label-danger">
								<span class="icon-thumbs-down-alt"></span>&nbsp;${review.negative}
							</span>
						</span>
					</p>
					<p>By <strong>${review.customer.name}</strong>
						<span class="pull-right">
						<fmt:formatDate value="${review.date}" var="formattedDate" 
						                type="date" pattern="MMMM d, yyyy" />
						${formattedDate}
						</span>
					</p><br />
					<p>${review.text}</p>
					<c:if test="${not review.voted}">
						<div class="pull-right">
							<form action="<c:url value="/voteReview.do" />" method="post">
								<input type="hidden" name="review" value="${review.id}" />
								<input type="hidden" name="isbn" value="${book.isbn13}" />
								<input type="hidden" name="nonce" value="${nonce}" />
								Was this review helpful?
								<button class="btn btn-success btn-xs" name="helpful" value="yes" type="submit">Yes</button>
								<button class="btn btn-danger btn-xs" name="helpful" value="no" type="submit">No</button>
							</form>
						</div>
					</c:if>
					<div style="clear: both;"></div>
					<hr/>
				</c:forEach>
				<div>
					<a class="btn btn-primary btn" href="<c:url value="/reviewBook.do?isbn=${book.isbn13}" />">
						Write your own review!</a>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>
