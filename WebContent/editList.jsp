<div class="row">
	<div class="col-lg-4"></div>
	<div class="col-lg-4">
		<h1>Edit list</h1>
		<c:if test="${not empty messages}">
			<c:forEach var="message" items="${messages}">
				<div class="alert alert-danger">${message}</div>
			</c:forEach>
		</c:if>
		<form action="editList.do" method="post">
			<input type="hidden" name="nonce" value="${nonce}"/>
			<input type="hidden" name="id" value="${list.id}"/>
			<input class="form-control" id="title" name="title" value="${list.title}" autofocus />
			&nbsp;
			<textarea id="description" name="description" rows="5" cols="40"
				class="form-control"><c:out
					value="${list.description}" />
			</textarea>
			&nbsp;
			<div class="input-group">
				<div class="input-group-addon">
					<input type="checkbox" name="public"
						<c:if test="${list.public}">checked="checked"</c:if> 
					/>
				</div>
				<span class="form-control"> Publicly available?</span>
			</div>
			&nbsp;
			<p>
				<button class="btn btn-lg btn-success" type="submit">Save</button>
				<a class="btn btn-lg btn-primary"
					href="<c:url value="/viewCustomer.do" />">No, take me back</a>
			</p>
		</form>
	</div>
</div>