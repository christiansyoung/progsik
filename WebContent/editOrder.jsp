<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h2>Edit order</h2>
		<c:if test="${not empty messages}">
			<c:forEach var="message" items="${messages}">
				<div class="alert alert-danger">${message}</div>
			</c:forEach>
		</c:if>
		<c:if test="${empty messages}">
			<p>You can still edit your order. That means you can select a different shipping address.
				If you want to add a new address for this order, please add it and then come back here.</p>
			<h4><span class="label label-default">Current address</span></h4>
			<pre class="alert alert-info"><c:out value="${order.address.address }"/></pre>
			<c:forEach var="address" items="${addresses}" varStatus="counter">
				<h4>
					<span class="label label-default">Address <span class="badge">${counter.count}</span></span>
				</h4>
				<pre><c:out value="${address.address}" /></pre>
				<form method="post" action="editOrder.do">
					<input type="hidden" name="nonce" value="${nonce}"/>
					<input type="hidden" name="address" value="${address.id}"/>
					<input type="hidden" name="id" value="${order.id}"/>
					<button type="submit" class="btn btn-success btn-xs">Select this address &raquo;</button>
				</form>
				<br/>
			</c:forEach>
			<c:if test="${empty addresses}">
				<p>You have no more addresses registered. Please add them before editing the order.</p>
			</c:if>
		</c:if>
		<a href="<c:url value="/viewCustomer.do"/>" class="btn btn-primary">Go back</a>
	</div>
</div>