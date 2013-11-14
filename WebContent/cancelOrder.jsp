<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h1>Cancel order</h1>
		<c:if test="${not empty messages}">
			<c:forEach var="message" items="${messages}">
				<div class="alert alert-danger">${message}</div>
			</c:forEach>
		</c:if>
		<c:choose>
			<c:when test="${not empty order}">
				<p>Do you want to cancel the following order? <strong>This
					operation cannot be undone</strong>.</p>
				<div>
					Date:
					<fmt:formatDate value="${order.createdDate.time}" type="date"
						dateStyle="short" />
				</div>
				<div>Value: ${order.value}</div>
				<div>Status: <c:out value="${order.statusText}" /></div>
				<div>Delivery address:</div>
				<pre><c:out value="${order.address.address}" /></pre>
				<form action="cancelOrder.do" method="post">
					<input type="hidden" name="nonce" value="${nonce}">
					<input name="id" value="${order.id}" type="hidden" />
					<p>
						<button class="btn btn-lg btn-danger" type="submit">Confirm</button>
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