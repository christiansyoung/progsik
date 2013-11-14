<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<form class="form-signin" action="changePassword.do" method="post">
			<input type="hidden" name="nonce" value="${nonce}">
			<h2 class="form-signin-heading">Change password</h2>
			<div class="input-group">
				<input name="old" id="old" type="password" class="form-control" 
					placeholder="Old password" autofocus />
				<span class="input-group-addon">Old</span>
			</div>&nbsp;
			<div class="input-group">
				<input name="password" id="password" type="password"
					class="form-control"
					placeholder="<c:out value="${not empty messages ? values.email[0] : 'New password'}" />"/>
				<span class="input-group-addon">New</span>
			</div>
			&nbsp;
			<div class="input-group">
			<input name="password" id="password"
				type="password" class="form-control"
				placeholder="<c:out value="${not empty messages ? values.email[1] : 'Repeat password'}" />" />
				<span class="input-group-addon">Repeat</span>
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