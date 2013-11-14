<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h2>Select shipping address</h2>
		<div>
			Select a shipping address amongst the following, or <a
				href="addAddress.do?from=selectShippingAddress">enter a new
				address</a>.
		</div>
		<c:forEach var="address" items="${addresses}" varStatus="counter">
			<h4>
				<span class="label label-default">Address <span class="badge">${counter.count}</span></span>
			</h4>
			<pre><c:out value="${address.address}" /></pre>
			<a class="btn btn-success btn-xs"
				href="selectShippingAddress.do?id=${address.id}">Select this
				address &raquo;</a>
			<a class="btn btn-primary btn-xs"
				href="<c:url value="editAddress.do?id=${address.id}" />"> <span
				class="icon-edit"></span>&nbsp;Edit
			</a>
			<a class="btn btn-danger btn-xs"
				href="<c:url value="deleteAddress.do?id=${address.id}" />"><span
				class="icon-trash"></span>&nbsp;Delete</a>
			<br/><br/>
			
		</c:forEach>
	</div>
</div>