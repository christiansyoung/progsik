<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h1>Delete address</h1>
		<c:if test="${not empty messages}">
			<c:forEach var="message" items="${messages}">
				<div class="alert alert-danger">${message}</div>
			</c:forEach>
		</c:if>
		<c:choose>
			<c:when test="${not empty address}">
				<p>
					Do you want to delete the following address? <strong>This
						operation cannot be undone</strong>.
				</p>
				<pre class="alert alert-info"><c:out value="${address.address}" /></pre>
				<form action="deleteAddress.do" method="post">
					<input type="hidden" name="nonce" value="${nonce}">
					<input name="id" value="${address.id}" type="hidden" />
					<p>
						<button class="btn btn-lg btn-warning" type="submit">Confirm</button>
						<a class="btn btn-lg btn-primary btn-success"
							href="<c:url value="/viewCustomer.do" />">No, take me back</a>
					</p>
				</form>
			</c:when>
			<c:otherwise>
				<a class="btn btn-lg btn-primary btn-block"
					href="<c:url value="/viewCustomer.do" />">Go Back</a>
			</c:otherwise>
		</c:choose>
	</div>
</div>