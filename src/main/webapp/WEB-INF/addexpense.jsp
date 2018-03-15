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

<div class="w3-container">
    <h3><fmt:message key="addexpense.title"/></h3>
</div>

<form action="addexpense.jsp" method="post">
<div class="w3-panel">
    <div class="w3-row-padding">
        <div class="w3-quarter">
            <input class="w3-input w3-border w3-round-large"
                type="number"
                id="day"
                name="day"
                size="4"
                maxlength="2"
                min="1"
                max="31"
                title="<fmt:message key="addexpense.day.tooltip"/>"
                placeholder="<fmt:message key="addexpense.day.default"/>"
                value="<c:out value="${requestScope.expense.getDay().get(5)}"/>"
                required="required" >
            <label class="w3-label"><fmt:message key="addexpense.day.label"/></label>
        </div><div class="w3-quarter">
            <input class="w3-input w3-border w3-round-large"
                type="number"
                id="month"
                name="month"
                size="4"
                maxlength="2"
                min="1"
                max="12"
                title="<fmt:message key="addexpense.month.tooltip"/>"
                placeholder="<fmt:message key="addexpense.month.default"/>"
                value="<c:out value="${requestScope.expense.getDay().get(2)+1}"/>"
                required="required">
            <label class="w3-label"><fmt:message key="addexpense.month.label"/></label>
        </div><div class="w3-quarter">
            <input class="w3-input w3-border w3-round-large"
                type="number"
                id="year"
                name="year"
                size="6"
                maxlength="4"
                min="2000"
                max="2100"
                title="<fmt:message key="addexpense.year.tooltip"/>"
                placeholder="<fmt:message key="addexpense.year.default"/>"
                value="<c:out value="${requestScope.expense.getDay().get(1)}"/>"
                required="required">
            <label class="w3-label" for="year"><fmt:message key="addexpense.year.label"/></label>
        </div><c:if test="${not empty requestScope.originalday}"><div class="w3-quarter">
            <input class="w3-btn w3-blue w3-round-xxlarge" type="button" id="resetdatebutton" value="<fmt:message key="addexpense.resetdatebutton.label"/>" onclick="resetToOriginalDay(${requestScope.originalday.get(5)}, ${requestScope.originalday.get(2)+1}, ${requestScope.originalday.get(1)});">
        </div></c:if>
    </div>
</div><div class="w3-panel">
    <div class="w3-row-padding"><div class="w3-col s12">
    <select class="w3-input w3-border w3-round-large"
        name="category"
        title="<fmt:message key="addexpense.category.tooltip"/>"
        required="required"
        ${requestScope.categoryPreset ? '' : 'autofocus'}>
        <c:forEach items="${requestScope.categories}" var="singlecategory">
            <option value="${singlecategory.id}" ${singlecategory.id eq requestScope.expense.categoryId ? 'selected' : ''}><c:out value="${singlecategory.name}"/></option>
        </c:forEach>
    </select>
    <label class="w3-label" for="category"><fmt:message key="addexpense.category.label"/></label>
    </div></div>
</div><div class="w3-panel">
    <div class="w3-row-padding">
        <div class="w3-third">
            <input class="w3-input w3-border w3-round-large"
                type="text"
                name="amount"
                size="40"
                maxlength="40"
                title="<fmt:message key="addexpense.amount.tooltip"/>"
                placeholder="<fmt:message key="addexpense.amount.default"/>"
                pattern="[-+]?[0-9]+[,.]?[0-9]{0,2}"
                required="required"
                autocomplete="off"
                value="<c:out value="${requestScope.expense.amount != 0.0 ? requestScope.expense.amount : ''}"/>"
                ${requestScope.categoryPreset ? 'autofocus' : ''}>
            <label class="w3-label" for="amount"><fmt:message key="addexpense.amount.label"/></label>
        </div><div class="w3-third">
            <input class="w3-check" type="checkbox" name="monthly" title="<fmt:message key="addexpense.monthly.tooltip"/>" value="true" ${requestScope.expense.monthly ? 'checked' : ''}>
            <label class="w3-label" for="monthly"><fmt:message key="addexpense.monthly.label"/></label>
        </div><div class="w3-third">
            <input class="w3-check" type="checkbox" name="income" title="<fmt:message key="addexpense.income.tooltip"/>" value="true" ${requestScope.expense.income ? 'checked' : ''}>
            <label class="w3-label" for="income"><fmt:message key="addexpense.income.label"/></label>
        </div>
    </div>
</div><div class="w3-panel">
    <div class="w3-row-padding"><div class="w3-col s12">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="reason"
        size="40"
        maxlength="40"
        title="<fmt:message key="addexpense.reason.tooltip"/>"
        placeholder="<fmt:message key="addexpense.reason.default"/>"
        pattern=".{3,}"
        required="required"
        value="<c:out value="${requestScope.expense.reason}"/>">
    <label class="w3-label" for="reason"><fmt:message key="addexpense.reason.label"/></label>
    </div></div>
</div><div class="w3-panel">
    <div class="w3-row">
        <input class="w3-btn w3-green w3-xlarge w3-round-xxlarge" type="submit" value="<fmt:message key="addexpense.button.label"/>">
        <input class="w3-btn w3-red w3-tiny w3-round-xxlarge" type="reset" value="<fmt:message key="addexpense.resetbutton.label"/>">
    </div>
</div>
</form>

<script>
function resetToOriginalDay(originalDay, originalMonth, originalYear) {
    document.getElementById('day').value   = originalDay;
    document.getElementById('month').value = originalMonth;
    document.getElementById('year').value  = originalYear;
    document.getElementById('resetdatebutton').style.display = 'none';
}
</script>


<jsp:include page="footer.jsp"/>
