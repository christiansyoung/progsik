<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h2 class="form-signin-heading">Activate account</h2>
		<p>Your account has been created, but before we activate it, we
			need to verify that the email address given belongs to you. We have
			sent you an email with instructions on how to activate your account.
			In order to activate your account you can either click the link in
			the email or fill in this form:</p>
		<div>
			<form method="post" action="activateCustomer.do">
				<c:choose>
					<c:when test="${empty email}">
						<div class="input-group">
							<input type="text" name="email" value="" class="form-control"
								placeholder="john@doe.com" /> <span class="input-group-addon">Email</span>
						</div>
						&nbsp;
					</c:when>
					<c:otherwise>
						<input type="hidden" name="email"
							value="<c:out value="${email}" />" />
					</c:otherwise>
				</c:choose>
				<div class="input-group">
					<input type="text" name="activationToken" class="form-control" placeholder="ABCDEF" />
					<span class="input-group-addon">Activation token</span>
				</div>
				&nbsp;
				<button class="btn btn-lg btn-primary btn-block" type="submit">Activate</button>
			</form>
		</div>
	</div>
</div>