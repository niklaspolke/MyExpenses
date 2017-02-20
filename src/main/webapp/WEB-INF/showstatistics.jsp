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
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="header.jsp"/>
<link rel="stylesheet" href="css/chartist.min.css">
<link rel="stylesheet" href="css/styles.css">
<script type="text/javascript" src="js/chartist.min.js"></script>

<div class="w3-container">
    <h3><fmt:message key="statistics.title"/></h3>
</div>

<div class="w3-panel w3-row-padding">
    <div class="w3-col m8">
        <form action="showstatistics.jsp" method="post">
            <select class="w3-input w3-border w3-round-large"
                name="month"
                title="<fmt:message key="statistics.month.tooltip"/>"
                required="required" onchange="this.form.submit()"
                autofocus>
                <c:forEach var="month" items="${requestScope.months}">
                    <option value="${month}" ${requestScope.month eq month ? 'selected' : ''}><c:out value="${month}"/></option>
                </c:forEach>
            </select>
            <label class="w3-label" for="month"><fmt:message key="statistics.month.label"/></label>
        </form>
    </div><div class="w3-col m4">
        <a href="exportstatistics.jsp?month=${requestScope.month}" title="<fmt:message key="statistics.export.tooltip"/>">
            <fmt:message key="statistics.export.label"/><img src="img/cloud-down_24.png" alt="<fmt:message key="statistics.export.tooltip"/>" title="<fmt:message key="statistics.export.tooltip"/>" width="24" height="24"/>
        </a>
    </div>
</div>

<div class="w3-panel w3-row-padding">
    <div class="w3-col m6" id="myChart" style="${requestScope.statistics.size() > 1 ? 'height: 300px' : ''}"></div>

    <div class="w3-col m6">
        <h4><fmt:message key="statistics.expenses.title"/></h4>
        <fmt:setLocale value="de_DE"/>
        <table class="w3-table-all">
            <tr>
                <th><fmt:message key="statistics.header.category"/></th>
                <th style="width:100px"><fmt:message key="statistics.header.amount"/></th>
            </tr>
            <c:forEach var="category" items="${requestScope.statistics}">
                <tr ${category.name eq 'Total' ? 'class="total-cost"' : ''}>
                    <td>
                        <c:choose>
                        <c:when test="${category.name ne 'Total' and category.value gt 0}"><a href="listexpenses.jsp?month=${requestScope.month}&category=${category.id}"><c:out value="${category.name}"/></a></c:when>
                        <c:otherwise><fmt:message key="statistics.total"/></c:otherwise>
                        </c:choose>
                    </td><td class="number nowrap" style="text-align:right">
                        <fmt:formatNumber value="${category.value}" type="currency" pattern="0.00 €"/>
                    </td>
                </tr>
            </c:forEach>
            <tr>
            </tr>
        </table>
    </div>
</div>
<div class="w3-row-padding">
    <div class="w3-col m6">
        <h4><fmt:message key="statistics.income.title"/></h4>
        <fmt:setLocale value="de_DE"/>
        <table class="w3-table-all">
            <tr>
                <th><fmt:message key="statistics.header.category"/></th>
                <th style="width:100px"><fmt:message key="statistics.header.amount"/></th>
            </tr>
            <c:forEach var="category" items="${requestScope.statisticsIncome}">
                <tr ${category.name eq 'Total' ? 'class="total-income"' : ''}>
                    <td>
                        <c:choose>
                        <c:when test="${category.name eq 'Total'}"><fmt:message key="statistics.total"/></c:when>
                        <c:otherwise><c:out value="${category.name}"/></c:otherwise>
                        </c:choose>
                    </td><td class="number nowrap" style="text-align:right">
                        <fmt:formatNumber value="${category.value}" type="currency" pattern="0.00 €"/>
                    </td>
                </tr>
            </c:forEach>
            <tr>
            </tr>
        </table>
    </div>
    <div class="w3-col m6">
    <h4><fmt:message key="statistics.monthlycosts.title"/></h4>
        <fmt:setLocale value="de_DE"/>
        <table class="w3-table-all">
            <tr>
                <th><fmt:message key="statistics.header.category"/></th>
                <th style="width:100px"><fmt:message key="statistics.header.amount"/></th>
            </tr>
            <c:forEach var="category" items="${requestScope.statisticsMonthlyCosts}">
                <tr ${category.name eq 'Total' ? 'class="total-cost"' : ''}>
                    <td>
                        <c:choose>
                        <c:when test="${category.name eq 'Total'}"><fmt:message key="statistics.total"/></c:when>
                        <c:otherwise><c:out value="${category.name}"/></c:otherwise>
                        </c:choose>
                    </td><td class="number nowrap" style="text-align:right">
                        <fmt:formatNumber value="${category.value}" type="currency" pattern="0.00 €"/>
                    </td>
                </tr>
            </c:forEach>
            <tr>
            </tr>
        </table>
    </div>
</div>
<div class="w3-row-padding">
    <div class="w3-col m6">
        <h4><fmt:message key="statistics.result.title"/></h4>
        <div id="myBarChart" style="height: 100px"></div>
        <div class="${requestScope.sum > 0 ? 'total-income' : 'total-cost'} nowrap"><fmt:message key="statistics.result.sum"/> <fmt:formatNumber value="${requestScope.sum}" type="currency" pattern="0.00 €"/></div>
    </div>
</div>

<script type="text/javascript">
document.body.onload = function() {
    var chart = JSON.parse('${requestScope.chart}');
    new Chartist.Pie('#myChart', chart);

    var barchart = JSON.parse('${requestScope.barchart}');
    var barchartoptions = JSON.parse('${requestScope.barchartoptions}');
    new Chartist.Bar('#myBarChart', barchart, barchartoptions);
}
</script>


<jsp:include page="footer.jsp"/>
