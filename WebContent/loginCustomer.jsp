<c:choose>
	<c:when test="${empty customer}">
		<div class="row">
			<div class="col-lg-4"></div>
			<div class="col-lg-4">
				<form class="form-signin" action="loginCustomer.do" method="post">
					<input type="hidden" name="nonce" value="${nonce}">
					<c:if test="${not empty values.from}">
						<input type="hidden" name="from" value="${values.from}">
					</c:if>
					<h2 class="form-signin-heading">Please sign in</h2>
					<div class="input-group">
					<input name="email" id="email" type="text" class="form-control"
						placeholder="Email address" autofocus value="${values.email}" />
					<span class="input-group-addon">Email</span>
					</div>&nbsp;
					<div class="input-group">
					<input id="password" name="password" type="password"
						class="form-control" placeholder="Password" />
					<span class="input-group-addon">Password</span>
					</div>&nbsp;
					<c:if test="${not empty messages.error}">
						<div class="alert alert-danger">${messages.error}</div>
					</c:if>
					<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
						in</button>
				</form>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<!-- Jumbotron -->
		<div class="jumbotron">
			<h1>Welcome again!</h1>
			<p class="lead">We really missed you. Please go and shop some
				books or check that the information in your profile is accurate.</p>
			<p>
				<a class="btn btn-lg btn-success"
					href="<c:url value='/viewCustomer.do' />">View profile</a>
			</p>
		</div>
	</c:otherwise>
</c:choose>