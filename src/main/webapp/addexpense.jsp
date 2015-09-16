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
                    <th></th>
                    <th>
                        <label for="day">day</label>
                    </th>
                    <th>
                        <label for="month">month</label>
                    </th>
                    <th>
                        <label for="year">year</label>
                    </th>
                </tr>
                <tr>
                    <th class="right">
                        <label>date:</label>
                    </th>
                    <td>
                        <input
                            type="number"
                            name="day"
                            size="4"
                            maxlength="2"
                            min="1"
                            max="31"
                            title="day - 1-31"
                            placeholder="31"
                            required="required">
                    </td>
                    <td>
                        <input
                            type="number"
                            name="month"
                            size="4"
                            maxlength="2"
                            min="1"
                            max="12"
                            title="month - 1-12"
                            placeholder="12"
                            required="required">
                    </td>
                    <td>
                        <input
                            type="number"
                            name="year"
                            size="6"
                            maxlength="4"
                            min="2000"
                            max="2100"
                            title="year - yyyy"
                            placeholder="2015"
                            required="required">
                    </td>
                </tr>
                <tr>
                    <th class="right">
                        <label for="category">category:</label>
                    </th>
                    <td colspan="3">
                        <select
                            name="category"
                            title="category of expense"
                            required="required">
                            <c:forEach items="${sessionScope.categories}" var="singlecategory">
                                <option value="${singlecategory.id}">${singlecategory.name}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="right">
                        <label for="amount">amount (€):</label>
                    </th>
                    <td colspan="3">
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
                    <th class="right">
                        <label for="reason">reason:</label>
                    </th>
                    <td colspan="3">
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
                        <input type="reset" value="Reset">
                    </td>
                    <td colspan="2">
                        <input type="submit" value="Create Expense">
                    </td>
                </tr>
            </tbody>
        </table>
    </fieldset>
</form>

<c:import url="footer.jspf" charEncoding="UTF-8"/>
