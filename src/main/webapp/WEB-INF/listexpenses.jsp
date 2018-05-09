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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="header.jsp"/>

<div class="w3-container">
<h3><c:choose>
    <c:when test="${requestScope.mode eq 'topten'}">
        <c:set var="title" scope="page">
        <fmt:message key="listexpenses.topten.title">
            <fmt:param value="${requestScope.category}"/>
            <fmt:param value="${requestScope.month}"/>
        </fmt:message>
        </c:set>
        <c:out value="${title}"/>
    </c:when>
    <c:when test="${requestScope.mode eq 'monthly'}">
        <fmt:message key="listexpenses.monthly.title">
            <fmt:param value="${requestScope.monthCurrent}"/>
        </fmt:message>
    </c:when>
    <c:when test="${requestScope.mode eq 'search'}">
        <fmt:message key="searchexpenses.result.title">
            <fmt:param value="${requestScope.searchText}"/>
        </fmt:message>
    </c:when>
    <c:otherwise>
        <fmt:message key="listexpenses.title"/>
    </c:otherwise>
</c:choose></h3>
</div>

<c:if test="${requestScope.mode eq 'monthly'}"><div class="w3-panel">
    <a href="importmonthly.jsp?month=${requestScope.monthCurrent}" title="<fmt:message key="listexpenses.importmonthly.tooltip"/>">
        <fmt:message key="listexpenses.importmonthly.label"/><img src="img/box-in_24.png" alt="<fmt:message key="listexpenses.importmonthly.tooltip"/>" title="<fmt:message key="listexpenses.importmonthly.tooltip"/>" width="24" height="24"/>
    </a>
</div></c:if>
<c:if test="${not empty param.message}"><div class="w3-panel w3-leftbar w3-pale-orange w3-border-orange">
    <fmt:message key="${param.message}">
        <fmt:param value="${param.msgparam}"/>
    </fmt:message>
</div></c:if>

<div class="w3-panel w3-padding-8">
<table class="w3-table-all">
    <thead>
        <c:if test="${requestScope.pageMax - requestScope.pageMin + 1 gt 1 or requestScope.mode eq 'monthly'}">
        <tr>
            <c:if test="${requestScope.pageMax - requestScope.pageMin + 1 gt 1}"><th class="w3-light-grey" colspan="5">
                <c:choose>
                <c:when test="${requestScope.page <= requestScope.pageMin}">
                    <img src="img/sign-left_24_inactive.png" alt="<fmt:message key="listexpenses.navigation.left.inactive.tooltip"/>" title="<fmt:message key="listexpenses.navigation.left.inactive.tooltip"/>" width="24" height="24"/>
                </c:when>
                <c:otherwise>
                    <a href="listexpenses.jsp?page=${requestScope.page - 1}"><img src="img/sign-left_24.png" alt="<fmt:message key="listexpenses.navigation.left.active.tooltip"/>" title="<fmt:message key="listexpenses.navigation.left.active.tooltip"/>" width="24" height="24"/></a>
                </c:otherwise>
                </c:choose>
                <c:choose>
                <c:when test="${requestScope.page >= requestScope.pageMax}">
                    <img src="img/sign-right_24_inactive.png" alt="<fmt:message key="listexpenses.navigation.right.inactive.tooltip"/>" title="<fmt:message key="listexpenses.navigation.right.inactive.tooltip"/>" width="24" height="24"/>
                </c:when>
                <c:otherwise>
                    <a href="listexpenses.jsp?page=${requestScope.page + 1}">
                        <img src="img/sign-right_24.png" alt="<fmt:message key="listexpenses.navigation.right.active.tooltip"/>" title="<fmt:message key="listexpenses.navigation.right.active.tooltip"/>" width="24" height="24"/>
                    </a>
                </c:otherwise>
                </c:choose>
            </th></c:if>
            <c:if test="${requestScope.mode eq 'monthly'}"><th class="w3-light-grey" colspan="5">
                <c:choose>
                <c:when test="${requestScope.monthMin eq requestScope.monthCurrent}">
                    <img src="img/sign-left_24_inactive.png" alt="<fmt:message key="listexpenses.monthlynavigation.left.inactive.tooltip"/>" title="<fmt:message key="listexpenses.monthlynavigation.left.inactive.tooltip"/>" width="24" height="24"/>
                </c:when>
                <c:otherwise>
                    <a href="listexpenses.jsp?monthly=true&month=${requestScope.monthCurrent.previous()}"><img src="img/sign-left_24.png" alt="<fmt:message key="listexpenses.monthlynavigation.left.active.tooltip"/>" title="<fmt:message key="listexpenses.monthlynavigation.left.active.tooltip"/>" width="24" height="24"/></a>
                </c:otherwise>
                </c:choose>
                <c:choose>
                <c:when test="${requestScope.monthMax eq requestScope.monthCurrent}">
                    <img src="img/sign-right_24_inactive.png" alt="<fmt:message key="listexpenses.monthlynavigation.right.inactive.tooltip"/>" title="<fmt:message key="listexpenses.monthlynavigation.right.inactive.tooltip"/>" width="24" height="24"/>
                </c:when>
                <c:otherwise>
                    <a href="listexpenses.jsp?monthly=true&month=${requestScope.monthCurrent.next()}">
                        <img src="img/sign-right_24.png" alt="<fmt:message key="listexpenses.monthlynavigation.right.active.tooltip"/>" title="<fmt:message key="listexpenses.monthlynavigation.right.active.tooltip"/>" width="24" height="24"/>
                    </a>
                </c:otherwise>
                </c:choose>
            </th></c:if>
        </tr>
        </c:if>
        <tr>
            <c:if test="${requestScope.mode ne 'monthly'}"><th><fmt:message key="listexpenses.table.header.date"/></th></c:if>
            <c:if test="${requestScope.mode ne 'topten'}"><th><fmt:message key="listexpenses.table.header.category"/></th></c:if>
            <th><fmt:message key="listexpenses.table.header.amount"/></th>
            <th><fmt:message key="listexpenses.table.header.reason"/></th>
            <th><fmt:message key="listexpenses.table.header.actions"/></th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="expense" items="${requestScope.expenses}">
            <tr style="${expense.income ? 'color:green' : ''}">
                <c:if test="${requestScope.mode ne 'monthly'}"><td><c:out value="${expense.getReadableDayAsString()}"/></td></c:if>
                <c:if test="${requestScope.mode ne 'topten'}"><td><c:out value="${expense.categoryName}"/></td></c:if>
                <td class="number nowrap" style="text-align:right"><fmt:formatNumber value="${expense.amount}" type="currency" pattern="0.00 €"/></td>
                <td><c:out value="${expense.reason}"/></td>
                <td style="border:none">
                    <a href="editexpense.jsp?id=${expense.id}"><img src="img/pencil_24.png" alt="<fmt:message key="listexpenses.table.editexpense.tooltip"/>" title="<fmt:message key="listexpenses.table.editexpense.tooltip"/>" width="24" height="24"/></a>
                    <a href="addexpense.jsp?id=${expense.id}"><img src="img/sign-add_24.png" alt="<fmt:message key="listexpenses.table.copyexpense.tooltip"/>" title="<fmt:message key="listexpenses.table.copyexpense.tooltip"/>" width="24" height="24"/></a>
                    <a id="delete${expense.id}" href="deleteexpense.jsp?id=${expense.id}" onclick="return prompt('delete${expense.id}', '${fn:replace(expense, '\"', '&quot;')}')"><img src="img/sign-delete_24.png" alt="<fmt:message key="listexpenses.table.deleteexpense.tooltip"/>" title="<fmt:message key="listexpenses.table.deleteexpense.tooltip"/>" width="24" height="24"/></a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${requestScope.pageMax - requestScope.pageMin + 1 gt 1}"><th class="w3-light-grey" colspan="5">
            <c:choose>
            <c:when test="${requestScope.page <= requestScope.pageMin}">
                <img src="img/sign-left_24_inactive.png" alt="<fmt:message key="listexpenses.navigation.left.inactive.tooltip"/>" title="<fmt:message key="listexpenses.navigation.left.inactive.tooltip"/>" width="24" height="24"/>
            </c:when>
            <c:otherwise>
                <a href="listexpenses.jsp?page=${requestScope.page - 1}"><img src="img/sign-left_24.png" alt="<fmt:message key="listexpenses.navigation.left.active.tooltip"/>" title="<fmt:message key="listexpenses.navigation.left.active.tooltip"/>" width="24" height="24"/></a>
            </c:otherwise>
            </c:choose>
            <c:choose>
            <c:when test="${requestScope.page >= requestScope.pageMax}">
                <img src="img/sign-right_24_inactive.png" alt="<fmt:message key="listexpenses.navigation.right.inactive.tooltip"/>" title="<fmt:message key="listexpenses.navigation.right.inactive.tooltip"/>" width="24" height="24"/>
            </c:when>
            <c:otherwise>
                <a href="listexpenses.jsp?page=${requestScope.page + 1}">
                    <img src="img/sign-right_24.png" alt="<fmt:message key="listexpenses.navigation.right.active.tooltip"/>" title="<fmt:message key="listexpenses.navigation.right.active.tooltip"/>" width="24" height="24"/>
                </a>
            </c:otherwise>
            </c:choose>
        </th></c:if>
    </tbody>
</table>
<c:if test="${requestScope.mode eq 'topten'}">
    <a href="showstatistics.jsp?back=true" title="<fmt:message key="listexpenses.backtostatistics.tooltip"/>"><fmt:message key="listexpenses.backtostatistics.label"/></a>
</c:if>
</div>

<script>
function prompt(id, expense) {
    var confirmed = confirm('<fmt:message key="warn.deleteexpensey"><fmt:param value="' + expense + '"/></fmt:message>');
    if (confirmed) {
        var deleteLink = document.getElementById(id);
        deleteLink.setAttribute('href', deleteLink.getAttribute('href') + '&confirmed=yes');
        return true;
    } else {
        return false;
    }
}
</script>


<jsp:include page="footer.jsp"/>
