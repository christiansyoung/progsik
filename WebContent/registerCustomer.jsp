<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h2 class="form-signin-heading">Register</h2>
		<c:choose>
			<c:when test="${empty sessionScope.customer}">
				<c:choose>
					<c:when test="${not empty register_success}">
						<p>
							You've successfully created a user. Maybe you should <a
								href="<c:url value="/loginCustomer.do" />">sign in</a>?
						</p>
					</c:when>
					<c:otherwise>
						<script type="text/javascript">
							var RecaptchaOptions = {
						   		theme : 'white'
							};
						</script>
						<form class="form-signin" action="registerCustomer.do" method="post">
							<input type="hidden" name="nonce" value="${nonce}">
							<c:if test="${not empty values.from}">
								<input type="hidden" name="from" value="${values.from}">
							</c:if>
							<div class="input-group">
								<input name="email" id="email" type="text" class="form-control"
									placeholder="john@doe.com" autofocus value="<c:out value="${values.email}" />" />
								<span class="input-group-addon">Email</span>
							</div>
							&nbsp;
							<div class="input-group">
								<input name="email" id="email" type="text" class="form-control"
									placeholder="Repeat your email" value="<c:out value="${values.email}" />" />
								<span class="input-group-addon">Email</span>
							</div>
							&nbsp;
							<div class="input-group">
								<input name="name" id="name" type="text" class="form-control"
									placeholder="John Doe" value="<c:out value="${values.name}" />" /> <span class="input-group-addon">Name</span>
							</div>
							&nbsp;
							<div class="input-group">
								<input id="password" name="password" type="password"
									class="form-control" placeholder="Password" autocomplete="off" />
								<span class="input-group-addon">Password</span>
							</div>
							&nbsp;
							<div class="input-group">
								<input id="password" name="password" type="password"
									class="form-control" placeholder="Repeat your password"
									autocomplete="off" /> <span class="input-group-addon">Password</span>
							</div>
							&nbsp;
							<script type="text/javascript"
								src="https://www.google.com/recaptcha/api/challenge?k=6LfsoegSAAAAAN2L9wD-hVHHkJRnYOJJC5CS5wgn">
								
							</script>
							<noscript>
								<iframe
									src="https://www.google.com/recaptcha/api/noscript?k=6LfsoegSAAAAAN2L9wD-hVHHkJRnYOJJC5CS5wgn"
									height="300" width="500" frameborder="0"></iframe>
								<br>
								<textarea name="recaptcha_challenge_field" rows="3" cols="40"></textarea>
								<input type="hidden" name="recaptcha_response_field"
									value="manual_challenge">
							</noscript>
							&nbsp;
							<c:if test="${not empty register_error}">
								<div class="alert alert-danger">${register_error}</div>
							</c:if>
							<button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
						</form>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<p>You're already logged in. Now, don't be greedy, one account
					should be enough.</p>
			</c:otherwise>
		</c:choose>
	</div>
</div>
