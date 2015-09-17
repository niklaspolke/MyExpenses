<%--
Copyright 2015 Niklas Polke

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
--%>
<%@page language="Java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="header.jsp"/>


<h3>List Expenses:</h3>

<fmt:setLocale value="de_DE"/>
<table class="tableList bordered">
    <thead>
        <tr>
            <th>Id</th>
            <th>Date</th>
            <th>Category</th>
            <th>Amount</th>
            <th>Reason</th>
            <th>Modify</th>
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
                <td style="border:none">
                    <a href="editexpense?id=${expense.id}"><img src="img/pencil_24.png" alt="edit expense" title="edit expense" width="24" height="24"/></a>
                    <a href="deleteexpense?id=${expense.id}"><img src="img/sign-delete_24.png" alt="delete expense" title="delete expense" width="24" height="24"/></a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>


<jsp:include page="footer.jsp"/>
