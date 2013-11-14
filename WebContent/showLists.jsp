<div class="row">
	<div class="col-sm-12">
		<h2>Lists of books</h2>
		<p>Here you will find all book lists made public by Amu-Darya users.</p>
		<c:forEach var="list" items="${lists}" varStatus="counter">
			<h4><span class="badge">${list.amount}</span>&nbsp;
			<a href="<c:url value="/viewList.do?id=${list.id}" />"><c:out value="${list.title}" /></a></h4>
			<p><c:out value="${list.description}"/></p>
		</c:forEach>
	</div>
</div>
