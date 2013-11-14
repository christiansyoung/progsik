<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h1>Add credit card</h1>
		<c:if test="${not empty messages.error}">
			<div class="alert alert-danger">${messages.error}</div>
		</c:if>
		<form action="addCreditCard.do" method="post">
			<input type="hidden" name="nonce" value="${nonce}">
			<div class="input-group">
				<input class="form-control" id="creditCardNumber" name="creditCardNumber" type="text"
					value="<c:out value="${values.creditCardNumber}" />" />
				<span class="input-group-addon">Card number</span>
			</div>&nbsp;
			<c:if test="${not empty messages.creditCardNumber}">
				<div class="alert alert-danger">${messages.creditCardNumber}</div>&nbsp;
			</c:if>
			<div class="input-group">
				<input class="form-control" id="cardholderName" name="cardholderName" type="text"
					value="<c:out value="${values.cardholderName}" />" />
				<span class="input-group-addon">Cardholder's name</span>
			</div>&nbsp;
			<c:if test="${not empty messages.cardholderName}">
				<div class="alert alert-danger">${messages.cardholderName}</div>&nbsp;
			</c:if>
			<div class="row">
				<div class="col-lg-6">
					<div class="input-group">
						<select class="form-control" id="expiryDate" name="expiryMonth">
							<c:forEach var="month" items="${months}">
								<option value="${month}">${month+1}</option>
							</c:forEach>
						</select>
						<span class="input-group-addon">Month</span>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="input-group">
						<select class="form-control" id="expiryDate" name="expiryYear">
							<c:forEach var="year" items="${years}">
		                    	<option value="${year}">${year}</option>
		                	</c:forEach>
						</select>
						<span class="input-group-addon">Year</span>
					</div>
				</div>
			</div>
			&nbsp;
			<c:if test="${not empty messages.expiryDate}">
				<div class="alert alert-danger">${messages.expiryDate}</div>&nbsp;
			</c:if>
			<p>
				<button class="btn btn-lg btn-success" type="submit">Add</button>
				<a class="btn btn-lg btn-primary"
					href="<c:url value="/viewCustomer.do" />">No, take me back</a>
			</p>
		</form>
	</div>
</div>