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
<script type="text/javascript" src="js/chartist.min.js"></script>

<h3>Test Statistics:</h3>

<div class="inline-block">
    <form action="showstatistics" method="post">
        <select
            name="month"
            title="month of expense"
            style="height:30px;font-size:1.5em;margin-bottom: 30px"
            required="required" onchange="this.form.submit()">
            <c:forEach var="month" items="${sessionScope.months}">
                <option value="${month}" ${sessionScope.month eq month ? 'selected' : ''}>${month}</option>
            </c:forEach>
        </select>
    </form>

    <div id="myChart" style="height: 300px;width: 300px;"></div>
</div>

<div class="inline-block">
    <fmt:setLocale value="de_DE"/>
    <table class="tableList bordered">
        <tr>
            <th>Category</th>
            <th style="width:100px">Value</th>
        </tr>
        <c:forEach var="category" items="${sessionScope.statistics}">
            <tr>
                <td>
                    ${category.name}
                </td><td class="number">
                    <fmt:formatNumber value="${category.value}" type="currency"/>
                </td>
            </tr>
        </c:forEach>
        <tr>
        </tr>
    </table>
</div>

<script type="text/javascript">
document.body.onload = function() {
    var chart = JSON.parse('${sessionScope.chart}');
    new Chartist.Pie('#myChart', chart);
}
</script>


<jsp:include page="footer.jsp"/>
