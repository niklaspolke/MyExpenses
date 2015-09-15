<%@page language="Java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="header.jspf" charEncoding="UTF-8"/>

<h3>Add Expense</h3>

<form action="addexpense" method="post">
    <fieldset>
        <legend>Expense</legend>
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

<c:import url="footer.jspf" charEncoding="UTF-8"/>
