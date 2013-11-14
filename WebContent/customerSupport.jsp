<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<form class="form-signin" action="customerSupport.do" method="post">
			<input type="hidden" name="nonce" value="${nonce}">
			<c:if test="${not empty values.from}">
				<input type="hidden" name="from" value="${values.from}">
			</c:if>
			<h2 class="form-signin-heading">Customer support</h2>
			<select name="department" class="form-control">
				<option selected value="Sales">Sales</option>
				<option value="Technical">Technical
					support</option>
			</select> &nbsp; <input name="subject" id="subject" type="text"
				class="form-control" placeholder="Subject" autofocus /> &nbsp;
			<textarea name="content" rows="10" class="form-control">Write your message here...</textarea>
			&nbsp;
			<button class="btn btn-lg btn-primary btn-block" type="submit">Send
				message</button>
		</form>
	</div>
</div>