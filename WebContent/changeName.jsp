<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<form class="form-signin" action="changeName.do" method="post">
			<input type="hidden" name="nonce" value="${nonce}">
			<h2 class="form-signin-heading">Change name</h2>
			<p>Your current name is:</p>
			<div class="alert alert-info"><c:out value="${customer.name}" /></div>
			<div class="input-group">
				<input name="name" id="name" type="text" class="form-control"
					placeholder="Write your new name" autofocus />
				<span class="input-group-addon">New name</span>
			</div>
			&nbsp;
			<c:if test="${not empty messages.name}">
				<div class="alert alert-danger">${messages.name}</div>
			</c:if>
			<p>
				<button class="btn btn-lg btn-primary btn-warning" type="submit">Change!</button>
				<a class="btn btn-lg btn-primary btn-success"
					href="<c:url value="/viewCustomer.do" />">No, take me back</a>
			</p>
		</form>
	</div>
</div>
