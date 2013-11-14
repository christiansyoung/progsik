<div class="row">
	<div class="col-lg-12">
		<h2>Write a review</h2>
		<p>Here you can submit your own review of the book <strong><em>${book.title.name}</em></strong>. Please, be fair!</p>
		<c:if test="${not empty messages}">
			<c:forEach var="message" items="${messages}">
				<div class="alert alert-danger alert-dismissable">
					<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
					${message}
				</div>
			</c:forEach>
		</c:if>
	</div>
</div>
<form class="form-signin" action="reviewBook.do" method="post">
	<div class="row">
		<div class="col-lg-2">
			<input type="hidden" name="nonce" value="${nonce}">
			<input type="hidden" name="isbn" value="${book.isbn13}">
			<div class="input-group">
				<span class="input-group-addon">
					<input type="radio" name="rating" value="5"/>
				</span>
				<span class="form-control" style="color: #ffaa33;">
					<c:forEach begin="1" end="5">
						<span class="icon-star"></span>
					</c:forEach>
				</span>
			</div>
			<div class="input-group">
				<span class="input-group-addon">
					<input type="radio" name="rating" value="4"/>
				</span>
				<span class="form-control" style="color: #ffaa33;">
					<c:forEach begin="1" end="4">
						<span class="icon-star"></span>
					</c:forEach>
					<c:forEach begin="1" end="1">
						<span class="icon-star-empty"></span>
					</c:forEach>
				</span>
			</div>
			<div class="input-group">
				<span class="input-group-addon">
					<input type="radio" name="rating" value="3"/>
				</span>
				<span class="form-control" style="color: #ffaa33;">
					<c:forEach begin="1" end="3">
						<span class="icon-star"></span>
					</c:forEach>
					<c:forEach begin="1" end="2">
						<span class="icon-star-empty"></span>
					</c:forEach>
				</span>
			</div>
			<div class="input-group">
				<span class="input-group-addon">
					<input type="radio" name="rating" value="2"/>
				</span>
				<span class="form-control" style="color: #ffaa33;">
					<c:forEach begin="1" end="2">
						<span class="icon-star"></span>
					</c:forEach>
					<c:forEach begin="1" end="3">
						<span class="icon-star-empty"></span>
					</c:forEach>
				</span>
			</div>
			<div class="input-group">
				<span class="input-group-addon">
					<input type="radio" name="rating" value="1"/>
				</span>
				<span class="form-control" style="color: #ffaa33;">
					<c:forEach begin="1" end="1">
						<span class="icon-star"></span>
					</c:forEach>
					<c:forEach begin="1" end="4">
						<span class="icon-star-empty"></span>
					</c:forEach>
				</span>
			</div>
			<br />
			<button class="btn btn-primary" type="submit">Submit</button>
			<a href="<c:url value="/viewBook.do?isbn=${book.isbn13}" />" class="btn btn-success">Go back</a>
		</div>
		<div class="col-lg-10">
			<textarea name="review" rows="15" class="form-control" placeholder="Write your review here..."></textarea>
		</div>
	</div>
</form>