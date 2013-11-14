<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h2>Select payment option</h2>
		<div>
			Select a credit card amongst the following, or <a
				href="addCreditCard.do?from=selectPaymentOption">add a new card</a>.
		</div>
		<form method="post" action="selectPaymentOption.do">
			<input type="hidden" name="nonce" value="${nonce}">
			<c:forEach var="creditCard" items="${creditCards}" varStatus="counter">
				<h4>
					<span class="label label-default">Credit card <span class="badge">${counter.count}</span></span>
				</h4>
				<div class="input-group">
					<span class="input-group-addon">
						<input type="radio" name="creditCardID" value="${creditCard.id}" />
					</span>
					<span class="form-control well well-sm">${creditCard.maskedCreditCardNumber}</span>
					<span class="input-group-addon">Card no.</span>
				</div>&nbsp;
				<div class="input-group">
					<span class="input-group-addon">CSC</span>
					<input class="form-control" id="cardSecurityCode" name="cardSecurityCode" type="text"
						placeholder="Card Security Code" />
					<a class="input-group-addon" href="http://en.wikipedia.org/wiki/Card_security_code">What's this?</a>
				</div>&nbsp;
				<p>Cardholder's name: <c:out value="${creditCard.cardholderName}" /><br />
				 Expiration date: <fmt:formatDate value="${creditCard.expiryDate.time}" type="date" dateStyle="short" /></p>
			</c:forEach>
			<br />
			<button type="submit" class="btn btn-success">Use selected card</button>
		</form>
	</div>
</div>