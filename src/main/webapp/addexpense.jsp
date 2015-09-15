<%@ include file="header.jspf" %>

<form action="addexpense" method="post">
    <fieldset>
        <legend>Add expense</legend>
        <table>
            <tbody>
                <tr>
                    <th>
                        <label for="amount">amount:</label>
                    </th>
                    <td>
                        <input
                            type="text"
                            name="amount"
                            size="40"
                            maxlength="40"
                            title="amount of expense - #0.00"
                            placeholder="0.00"
                            pattern="[-+]?[0-9]*[,.]?[0-9]{0,2}"
                            required="required">
                    </td>
                </tr>
                <tr>
                    <th>
                        <label for="reason">reason:</label>
                    </th>
                    <td>
                        <input
                            type="text"
                            name="reason"
                            size="40"
                            maxlength="40"
                            title="reason for expense - at least 3 characters"
                            placeholder="gone shopping"
                            pattern=".{3,}"
                            required="required">
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input type="reset">
                        <input type="submit">
                    </td>
                </tr>
            </tbody>
        </table>
    </fieldset>
</form>

<%@ include file="footer.jspf" %>
