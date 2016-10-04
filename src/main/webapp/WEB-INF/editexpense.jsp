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
    <h3><fmt:message key="editexpense.title"/></h3>
</div>

<form action="editexpense.jsp" method="post">
<input type="hidden" name="id" value="${requestScope.expense.id}">
<div class="w3-panel">
    <div class="w3-row-padding">
        <div class="w3-third">
            <input class="w3-input w3-border w3-round-large"
                type="number"
                name="day"
                size="4"
                maxlength="2"
                min="1"
                max="31"
                title="<fmt:message key="editexpense.day.tooltip"/>"
                placeholder="<fmt:message key="editexpense.day.default"/>"
                value="<c:out value="${requestScope.expense.getDay().get(5)}"/>"
                required="required" >
            <label class="w3-label"><fmt:message key="editexpense.day.label"/></label>
        </div><div class="w3-third">
            <input class="w3-input w3-border w3-round-large"
                type="number"
                name="month"
                size="4"
                maxlength="2"
                min="1"
                max="12"
                title="<fmt:message key="editexpense.month.tooltip"/>"
                placeholder="<fmt:message key="editexpense.month.default"/>"
                value="<c:out value="${requestScope.expense.getDay().get(2)+1}"/>"
                required="required">
            <label class="w3-label"><fmt:message key="editexpense.month.label"/></label>
        </div><div class="w3-third">
            <input class="w3-input w3-border w3-round-large"
                type="number"
                name="year"
                size="6"
                maxlength="4"
                min="2000"
                max="2100"
                title="<fmt:message key="editexpense.year.tooltip"/>"
                placeholder="<fmt:message key="editexpense.year.default"/>"
                value="<c:out value="${requestScope.expense.getDay().get(1)}"/>"
                required="required">
            <label class="w3-label" for="year"><fmt:message key="editexpense.year.label"/></label>
        </div>
    </div>
</div><div class="w3-panel">
    <div class="w3-row-padding"><div class="w3-col s12">
    <select class="w3-input w3-border w3-round-large"
        name="category"
        title="<fmt:message key="editexpense.category.tooltip"/>"
        required="required"
        autofocus>
        <c:forEach items="${requestScope.categories}" var="singlecategory">
            <option value="${singlecategory.id}" ${singlecategory.id eq requestScope.expense.categoryId ? 'selected' : ''}><c:out value="${singlecategory.name}"/></option>
        </c:forEach>
    </select>
    <label class="w3-label" for="category"><fmt:message key="editexpense.category.label"/></label>
    </div></div>
</div><div class="w3-panel">
    <div class="w3-row-padding">
        <div class="w3-third">
            <input class="w3-input w3-border w3-round-large"
                type="text"
                name="amount"
                size="40"
                maxlength="40"
                title="<fmt:message key="editexpense.amount.tooltip"/>"
                placeholder="<fmt:message key="editexpense.amount.default"/>"
                pattern="[-+]?[0-9]*[,.]?[0-9]{0,2}"
                required="required"
                autocomplete="off"
                value="<c:out value="${requestScope.expense.amount}"/>">
            <label class="w3-label" for="amount"><fmt:message key="editexpense.amount.label"/></label>
        </div><div class="w3-third">
            <input class="w3-check" type="checkbox" name="monthly" title="<fmt:message key="editexpense.monthly.tooltip"/>" value="true" ${requestScope.expense.monthly ? 'checked' : ''}>
            <label class="w3-label" for="fixedcost"><fmt:message key="editexpense.monthly.label"/></label>
        </div><div class="w3-third">
            <input class="w3-check" type="checkbox" name="income" title="<fmt:message key="editexpense.income.tooltip"/> "value="true" ${requestScope.expense.income ? 'checked' : ''}>
            <label class="w3-label" for="income"><fmt:message key="editexpense.income.label"/></label>
        </div>
    </div>
</div><div class="w3-panel">
    <div class="w3-row-padding"><div class="w3-col s12">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="reason"
        size="40"
        maxlength="40"
        title="<fmt:message key="editexpense.reason.tooltip"/>"
        placeholder="<fmt:message key="editexpense.reason.default"/>"
        pattern=".{3,}"
        required="required"
        value="<c:out value="${requestScope.expense.reason}"/>">
    <label class="w3-label" for="reason"><fmt:message key="editexpense.reason.label"/></label>
    </div></div>
</div><div class="w3-panel">
    <div class="w3-row-padding">
        <input class="w3-btn w3-green w3-xlarge w3-round-xxlarge" type="submit" value="<fmt:message key="editexpense.button.label"/>">
        <input class="w3-btn w3-red w3-tiny w3-round-xxlarge" type="reset" value="<fmt:message key="editexpense.resetbutton.label"/>">
    </div>
</div>
</form>


<jsp:include page="footer.jsp"/>
