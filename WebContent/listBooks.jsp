<div class="row">
	<div class="col-sm-12">
		<h2>Books available in the store</h2>
		<ul>
			<c:forEach var="book" items="${books}" varStatus="counter">
				<li><a
					href="<c:url value="viewBook.do" />?isbn=${book.getIsbn13()}">${book.getTitle().getName()}</a></li>
			</c:forEach>
		</ul>
	</div>
</div>
