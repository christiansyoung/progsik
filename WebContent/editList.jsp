<div class="container">
    <h1>EditList</h1>
    <form action="editList.do" method="post"> //EditAddress.do
        <c:if test="${not empty messages}">
            <c:forEach var="message" items="${messages}">
                <div>
                    <span class="error">${message}</span>
                </div>
            </c:forEach>
        </c:if>
        <input name="id" value="${address.id}" type="hidden" />
        <div>
            <div><label for="address">Edit Title: </label></div>
            <textarea id="address" name="address" rows="5" cols="40">${address.address}</textarea>
        </div>
        <div>
            <div><label for="address">Edit description: </label></div>
            <textarea id="address" name="address" rows="5" cols="40">${address.address}</textarea>
        </div>
        <div><input type="submit" value="Submit" /></div>
    </form>
</div>