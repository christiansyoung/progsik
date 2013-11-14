<div class="jumbotron">
	<h1>Oooops!</h1>
	<c:if test="${not empty messages}">
		<c:forEach var="message" items="${messages}">
			<p class="lead">${message}</p>
		</c:forEach>
	</c:if>
	<c:if test="${empty messages}">
		<p class="lead">Wow, we are terribly sorry but something failed
			when getting your review. We are looking into it, but meanwhile, why
			don't you try again?</p>
	</c:if>
	<br />
	<p>
		<a class="btn btn-lg btn-success"
			href="<c:url value='/listBooks.do' />">View more books</a>
	</p>
</div>