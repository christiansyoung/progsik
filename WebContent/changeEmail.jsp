<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<form class="form-signin" action="changeEmail.do" method="post">
			<input type="hidden" name="nonce" value="${nonce}">
			<h2 class="form-signin-heading">Change email</h2>
			<p>Your current email is:</p>
			<div class="alert alert-info"><c:out value="${customer.email}" /></div>
			<div class="input-group">
				<input name="password" id="password" type="password" class="form-control"
					placeholder="Type your password" autofocus/>
				<span class="input-group-addon">Password</span>
			</div>&nbsp;
			<div class="input-group">
				<input name="email" id="email" type="text" class="form-control"
					placeholder="<c:out value="${not empty messages ? values.email[0] : customer.email}" />"/>
				<span class="input-group-addon">New email</span>
			</div>&nbsp;
			<div class="input-group">
				<input name="email" id="email" type="text"
					class="form-control"
					placeholder="<c:out value="${not empty messages ? values.email[1] : 'Repeat address'}" />" />
				<span class="input-group-addon">Repeat email</span>
			</div>
			&nbsp;
			<c:if test="${not empty messages}">
				<c:forEach var="message" items="${messages}">
					<div class="alert alert-danger">${messages}</div>
				</c:forEach>
			</c:if>
			<p>
				<button class="btn btn-lg btn-primary btn-warning" type="submit">Change!</button>
				<a class="btn btn-lg btn-primary btn-success"
					href="<c:url value="/viewCustomer.do" />">No, take me back</a>
			</p>
		</form>
	</div>
</div>