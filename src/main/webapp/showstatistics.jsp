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
<link rel="stylesheet" href="css/chartist.min.css">
<link rel="stylesheet" href="css/styles.css">
<script type="text/javascript" src="js/chartist.min.js"></script>

<div class="w3-container">
<h3>Statistics</h3>
</div>

<div class="w3-panel">
    <form action="showstatistics" method="post">
        <select class="w3-input w3-border w3-round-large"
            name="month"
            title="month of expense"
            required="required" onchange="this.form.submit()"
            autofocus>
            <c:forEach var="month" items="${sessionScope.months}">
                <option value="${month}" ${sessionScope.month eq month ? 'selected' : ''}>${month}</option>
            </c:forEach>
        </select>
        <label class="w3-label" for="month">Month</label>
    </form>
</div>

<div class="w3-panel w3-row-padding">
    <div class="w3-col m6" id="myChart" style="${sessionScope.statistics.size() > 1 ? 'height: 300px' : ''}"></div>

    <div class="w3-col m6">
        <h4>Expenses</h4>
        <fmt:setLocale value="de_DE"/>
        <table class="w3-table-all">
            <tr>
                <th>Category</th>
                <th style="width:100px">Value</th>
            </tr>
            <c:forEach var="category" items="${sessionScope.statistics}">
                <tr ${category.name eq 'Total' ? 'class="total-cost"' : ''}>
                    <td>
                        <c:choose>
                        <c:when test="${category.name ne 'Total' and category.value gt 0}"><a href="listexpenses?month=${sessionScope.month}&category=${category.id}">${category.name}</a></c:when>
                        <c:otherwise>${category.name}</c:otherwise>
                        </c:choose>
                    </td><td class="number" style="text-align:right">
                        <fmt:formatNumber value="${category.value}" type="currency"/>
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
        <h4>Income</h4>
        <fmt:setLocale value="de_DE"/>
        <table class="w3-table-all">
            <tr>
                <th>Category</th>
                <th style="width:100px">Value</th>
            </tr>
            <c:forEach var="category" items="${sessionScope.statisticsIncome}">
                <tr ${category.name eq 'Total' ? 'class="total-income"' : ''}>
                    <td>
                        ${category.name}
                    </td><td class="number" style="text-align:right">
                        <fmt:formatNumber value="${category.value}" type="currency"/>
                    </td>
                </tr>
            </c:forEach>
            <tr>
            </tr>
        </table>
    </div>
    <div class="w3-col m6">
    <h4>Monthly Costs</h4>
        <fmt:setLocale value="de_DE"/>
        <table class="w3-table-all">
            <tr>
                <th>Category</th>
                <th style="width:100px">Value</th>
            </tr>
            <c:forEach var="category" items="${sessionScope.statisticsMonthlyCosts}">
                <tr ${category.name eq 'Total' ? 'class="total-cost"' : ''}>
                    <td>
                        ${category.name}
                    </td><td class="number" style="text-align:right">
                        <fmt:formatNumber value="${category.value}" type="currency"/>
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
        <h4>Result</h4>
        <div id="myBarChart" style="height: 100px"></div>
        <div class="${sessionScope.sum > 0 ? 'total-income' : 'total-cost'}">Total: <fmt:formatNumber value="${sessionScope.sum}" type="currency"/></div>
    </div>
</div>

<script type="text/javascript">
document.body.onload = function() {
    var chart = JSON.parse('${sessionScope.chart}');
    new Chartist.Pie('#myChart', chart);

    var barchart = JSON.parse('${sessionScope.barchart}');
    var barchartoptions = JSON.parse('${sessionScope.barchartoptions}');
    new Chartist.Bar('#myBarChart', barchart, barchartoptions);
}
</script>


<jsp:include page="footer.jsp"/>
