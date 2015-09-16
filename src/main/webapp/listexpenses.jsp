<%@page language="Java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="header.jspf" charEncoding="UTF-8"/>

<h3>Expenses:</h3>

<fmt:setLocale value="de_DE"/>
<table class="tableList bordered">
    <thead>
        <tr>
            <th>Id</th>
            <th>Date</th>
            <th>Category</th>
            <th>Amount</th>
            <th>Reason</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="expense" items="${sessionScope.expenses}">
            <tr>
                <td class="number"><c:out value="${expense.id}"/></td>
                <td><c:out value="${expense.getReadableDateAsString()}"/></td>
                <td><c:out value="${expense.category.name}"/></td>
                <td class="number"><fmt:formatNumber value="${expense.amount}" type="currency"/></td>
                <td><c:out value="${expense.reason}"/></td>
            </tr>
        </c:forEach>
    </tbody>
</table>


<c:import url="footer.jspf" charEncoding="UTF-8"/>
