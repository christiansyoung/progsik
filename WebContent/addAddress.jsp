<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h1>Add address</h1>
		<c:if test="${not empty messages}">
			<c:forEach var="message" items="${messages}">
				<div class="alert alert-danger">${message}</div>
			</c:forEach>
		</c:if>
		<form action="addAddress.do" method="post">
			<input type="hidden" name="nonce" value="${nonce}">
			<textarea id="address" name="address" rows="5" cols="40"
				class="form-control" autofocus><c:out
					value="${address.address}" /></textarea>
			&nbsp;
			<p>
				<button class="btn btn-lg btn-success" type="submit">Add</button>
				<a class="btn btn-lg btn-primary"
					href="<c:url value="/viewCustomer.do" />">No, take me back</a>
			</p>
		</form>
	</div>
</div>