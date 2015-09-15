<%@page language="Java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="header.jspf" charEncoding="UTF-8"/>

<h3>Expenses:</h3>

<fmt:setLocale value="de_DE"/>
<table class="tableList">
    <thead>
        <tr>
            <th>Id</th>
            <th>Date</th>
            <th>Amount</th>
            <th>Reason</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="expense" items="${sessionScope.expenses}">
            <tr>
                <td class="number bordered"><c:out value="${expense.id}"/></td>
                <td class="number bordered"><c:out value="${expense.getReadableDateAsString()}"/></td>
                <td class="number bordered"><fmt:formatNumber value="${expense.amount}" type="currency"/></td>
                <td class="bordered"><c:out value="${expense.reason}"/></td>
            </tr>
        </c:forEach>
    </tbody>
</table>


<c:import url="footer.jspf" charEncoding="UTF-8"/>
