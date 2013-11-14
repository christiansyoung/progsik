<div class="row">
	<div class="col-xs-12">
		<h2>
			<span class="icon-male"></span>&nbsp;My profile
		</h2>
		<p>
			Hello,
			<c:out value="${customer.name}" />!
			Here you can change your basic information:
		</p>
		<a class="btn btn-primary" href="<c:url value="/changeName.do" />">
			<span class="icon-edit"></span>&nbsp;Change name &raquo;
		</a> <a class="btn btn-primary" href="<c:url value="/changeEmail.do" />">
			<span class="icon-edit"></span>&nbsp;Change email &raquo;
		</a> <a class="btn btn-primary"
			href="<c:url value="/changePassword.do" />"><span
			class="icon-edit"></span>&nbsp;Change password &raquo;</a>
	</div>
</div>
<div class="row">
	<div class="col-sm-4">
		<h2>
			<span class="icon-credit-card"></span>&nbsp;My credit cards
		</h2>
		<p>Add or delete your credit cards to be able to pay your orders.</p>
		<c:forEach var="creditCard" items="${creditCards}" varStatus="counter">
			<div>
				<h4>
					<span class="label label-default"> Credit card <span
						class="badge">${counter.count}</span></span>
					<a class="btn btn-danger btn-xs"
						href="<c:url value="deleteCreditCard.do?id=${creditCard.id}" />">
						<span class="icon-trash"></span>&nbsp;Delete
					</a>
				</h4>
				<div>
					Credit card number:
					<c:out value="${creditCard.maskedCreditCardNumber}" />
				</div>
				<div>
					Cardholder's name:
					<c:out value="${creditCard.cardholderName}" />
				</div>
				<p>
					Expiration date:
					<fmt:formatDate value="${creditCard.expiryDate.time}" type="date"
						dateStyle="short" />
				</p>
			</div>
		</c:forEach>
		<p>
			<a class="btn btn-success"
				href="<c:url value="/addCreditCard.do?from=viewCustomer" />"><span
				class="icon-plus"></span>&nbsp;Add a credit card &raquo;</a>
		</p>
	</div>
	<div class="col-sm-4">
		<h2>
			<span class="icon-globe"></span>&nbsp;My addresses
		</h2>
		<p>Please, enter your address so that we know where to ship the
			books you buy.</p>
		<c:forEach var="address" items="${addresses}" varStatus="counter">
			<div>
				<h4>
					<span class="label label-default">Address <span
						class="badge">${counter.count}</span></span>
					<a class="btn btn-primary btn-xs"
					href="<c:url value="editAddress.do?id=${address.id}" />"> <span
					class="icon-edit"></span>&nbsp;Edit</a>
					<a class="btn btn-danger btn-xs"
					href="<c:url value="deleteAddress.do?id=${address.id}" />"><span
					class="icon-trash"></span>&nbsp;Delete</a>
				</h4>
				<pre><c:out value="${address.address}" /></pre>
			</div>
		</c:forEach>
		<p>
			<a class="btn btn-success"
				href="<c:url value="/addAddress.do?from=viewCustomer" />"><span
				class="icon-plus"></span>&nbsp;Add an address &raquo;</a>
		</p>
	</div>
	<div class="col-sm-4">
		<h2><span class="icon-list"></span>&nbsp;My lists</h2>
		<p>Manage your lists of books here. Add new ones, edit or remove the existing ones.</p>
		<c:forEach var="list" items="${lists}" varStatus="counter">
			<div>
				<h4>
					<span class="badge"><c:out value="${list.amount}" /></span>
					<a class="btn btn-primary btn-xs"
					href="<c:url value="editList.do?id=${list.id}" />"> <span
					class="icon-edit"></span>&nbsp;Edit</a>
					<a class="btn btn-danger btn-xs"
					href="<c:url value="deleteList.do?id=${list.id}" />"><span
					class="icon-trash"></span>&nbsp;Delete</a>
					<c:if test="${list.public}">
						<span class="icon-eye-open"></span>
					</c:if>
					<a href="<c:url value="/viewList.do?id=${list.id}" />"><c:out value="${list.title}" /></a>
					
				</h4>
				<p><c:out value="${list.description}" /></p>
			</div>
		</c:forEach>
		<p>Note that lists marked with <span class="icon-eye-open"></span> are public, and therefore everyone can see them.</p>
		<p>
			<a class="btn btn-success"
				href="<c:url value="/addList.do?from=viewCustomer" />"><span
				class="icon-plus"></span>&nbsp; Add a list &raquo;</a>
		</p>
	</div>
</div>
<div class="row">
	<div class="col-sm-12">
		<h2>
			<span class="icon-truck"></span>&nbsp;My orders
		</h2>
		<c:forEach var="order" items="${orders}" varStatus="counter">
			<div>
				<h4>
					<span class="label label-default">Order <span class="badge">${counter.count}</span></span>
					<c:if test="${order.status == 0}">
						<a class="btn btn-primary btn-xs"
						href="<c:url value="editOrder.do?id=${order.id}" />"> <span
						class="icon-edit"></span>&nbsp;Modify</a>
						<a class="btn btn-danger btn-xs"
						href="<c:url value="cancelOrder.do?id=${order.id}" />"><span
						class="icon-trash"></span>&nbsp;Cancel</a>
					</c:if>
				</h4>
				<div>
					Date:
					<fmt:formatDate value="${order.createdDate.time}" type="date"
						dateStyle="short" />
				</div>
				<div>Value: ${order.value}</div>
				<div>Status: <c:out value="${order.statusText}" /></div>
				<div>Delivery address:</div>
				<pre><c:out value="${order.address.address}" /></pre>
				
			</div>
		</c:forEach>
		<c:if test="${empty orders}">
			<p>No orders yet.</p>
		</c:if>
	</div>
</div>
