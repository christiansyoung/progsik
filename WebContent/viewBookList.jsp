<div class="container">
	<h1>Your lists</h1>
	<c:choose>
		<c:when test="${empty book}">
			<h2>No lists found!</h2>
			<div class="index-item">
				<h3>Menu</h3>
				<div>
					<a href="debug/bookLists.jsp">List of public books</a>
				</div>
				<div>
					<a href="viewCustomer.jsp">Back to profile</a>
				</div>	
			</div>
		</c:when>
		<c:otherwise>
			<h2>${list.title.name}</h2>
			<div>
				<ul>
					<li><b>Creator:</b></li>
				</ul>
			</div>
			<div>${book.description}</div>
		</c:otherwise>
	</c:choose>
</div>