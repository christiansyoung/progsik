<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h2>Review order</h2>
		<div>Please, carefully review the details of your order before proceeding:</div>
		<h4>Shopping cart</h4>
		<c:choose>
			<c:when test="${empty cart.items}">
				<p>No items in shopping cart.</p>
			</c:when>
			<c:otherwise>
				<c:forEach items="${cart.items}" var="item">
					<p><c:out value="${item.value.book.title.name}" /><br />
					Price: <c:out value="${item.value.book.price}" /><br />
					Quantity: <c:out value="${item.value.quantity}" /></p>
				</c:forEach>
				<c:choose>
					<c:when test="${cart.numberOfItems == 1}">
						<p>Subtotal: <c:out value="${cart.subtotal}" /></p>
					</c:when>
					<c:otherwise>
						<p>Subtotal (${cart.numberOfItems} items):
							${cart.subtotal}</p>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
		<h4>Shipping address</h4>
		<pre><c:out value="${cart.shippingAddress.address}" /></pre>	
		<h4>Payment method</h4>
		<p>Credit card number: ${cart.creditCard.maskedCreditCardNumber}<br />
    	Expiry date: <fmt:formatDate value="${cart.creditCard.expiryDate.time}" type="date" dateStyle="short" /><br />
    	Cardholder's name: <c:out value="${cart.creditCard.cardholderName}" /></p>
    	<a class="btn btn-success btn-block" href="<c:url value="/placeOrder.do" />">Place order</a>
    </div>
</div>