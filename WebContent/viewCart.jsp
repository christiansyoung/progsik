<c:choose>
	<c:when test="${empty cart.items}">
		<div class="jumbotron">
			<h2>Nothing here (yet)</h2>
			<p>Why don't you browse our list of books and buy some?</p>
			<a class="btn btn-lg btn-success"
				href="<c:url value="/listBooks.do" />">View our books</a>
		</div>
	</c:when>
	<c:otherwise>
		<div class="row">
			<div class="col-lg-4">
				<h2>
					<span class="icon-shopping-cart"></span>&nbsp;Shopping cart
				</h2>
				<form action="updateCart.do" method="post">
					<c:forEach items="${cart.items}" var="item">
						<h3>
							<a
								href="<c:url value="/viewBook.do?isbn=" /><c:out value="${item.value.book.isbn13}" />"
								class="label label-default"><span class="icon-book"></span>
								&nbsp;${item.value.book.title.name}</a>
						</h3>
						<div>Price: ${item.value.book.price}</div>
						<input type="hidden" name="isbn" value="${item.value.book.isbn13}" />
						<div class="input-group">
							<span class="input-group-addon">Amount</span> <input type="text"
								name="quantity" value="${item.value.quantity}"
								class="form-control" />
						</div>
					</c:forEach>
					<br />
					<button class="btn btn-xs btn-primary" type="submit">
						<span class="icon-refresh"></span>&nbsp;Update cart
					</button>
				</form>
				<c:choose>
					<c:when test="${cart.numberOfItems == 1}">
						<div>Subtotal: ${cart.subtotal}</div>
					</c:when>
					<c:otherwise>
						<div>Subtotal (${cart.numberOfItems} items):
							${cart.subtotal}</div>
					</c:otherwise>
				</c:choose>
				<br /> <a class="btn btn-primary" href="listBooks.do">Continue
					shopping</a> <a class="btn btn-success" href="selectShippingAddress.do">Go
					to checkout</a>
			</div>
		</div>
	</c:otherwise>
</c:choose>