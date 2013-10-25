<div class="container">
    <h1>Customer Support</h1>
    <div class="general-form">
        <form method="post" action="customerSupport.do">
            <table class="general-table">
                <tr>
                    <td>Choose department:</td>
                    <td>
                        <select name="department">
                            <option selected value="sales">Sales</option>
                            <option value="tech">Technical support</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Subject</td>
                    <td><input name="subject" type="text"></input></td>
                </tr>
            </table>
            <div> <textarea name="content" rows="10" cols="40"></textarea></div>
            <input type="hidden" name="csrf_token" value="${csrf_token}" />
            <div> <input type="submit" value="Send" /></div>
        </form>
    </div>
</div>