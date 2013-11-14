<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<c:if test="${not empty list}">
			<h2><c:out value="${list.title}" /></h2>
			<p><c:out value="${list.description}" /></p>
			<c:if test="${empty books and not empty list}">
				<p>Sorry, no books on this list yet.</p>
			</c:if>
			<c:if test="${authorized}">
				<form action="<c:url value="/deleteBookFromList.do" />" method="post">
					<input type="hidden" name="nonce" value="${nonce}" />
					<c:forEach var="book" items="${books}" varStatus="counter">
						<h4>
						<button class="btn btn-danger btn-xs" type="submit" name="isbn" value="${book.isbn13}">
							<span class="icon-trash"></span>
						</button>
						<a href="<c:url value="/viewBook.do?isbn=${book.isbn13}" />">${book.title.name}</a></h4>
					</c:forEach>
				</form>
			</c:if>
			<c:if test="${not authorized}">
				<c:forEach var="book" items="${books}" varStatus="counter">
					<h4><a href="<c:url value="/viewBook.do?isbn=${book.isbn13}" />">${book.title.name}</a></h4>
				</c:forEach>
			</c:if>
		</c:if>
		<c:if test="${not empty messages}">
			<h2>Unknown list</h2>
			<c:forEach var="message" items="${messages}">
				<div class="alert alert-danger">${message}</div>
			</c:forEach>
		</c:if>
		<a class="btn btn-primary"
			href="<c:url value="/showLists.do" />">Browse lists</a>
		<a class="btn btn-primary"
			href="<c:url value="/listBooks.do" />">Browse books</a>
	</div>
</div>