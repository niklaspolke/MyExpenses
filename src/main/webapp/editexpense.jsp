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


<h3>Edit Expense</h3>

<form action="editexpense" method="post">
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
                title="day - 1-31"
                placeholder="31"
                value="${sessionScope.expense.getDay().get(5)}"
                required="required" >
            <label class="w3-label">Day</label>
        </div><div class="w3-third">
            <input class="w3-input w3-border w3-round-large"
                type="number"
                name="month"
                size="4"
                maxlength="2"
                min="1"
                max="12"
                title="month - 1-12"
                placeholder="12"
                value="${sessionScope.expense.getDay().get(2)+1}"
                required="required">
            <label class="w3-label">Month</label>
        </div><div class="w3-third">
            <input class="w3-input w3-border w3-round-large"
                type="number"
                name="year"
                size="6"
                maxlength="4"
                min="2000"
                max="2100"
                title="year - yyyy"
                placeholder="2015"
                value="${sessionScope.expense.getDay().get(1)}"
                required="required">
            <label class="w3-label" for="year">Year</label>
        </div>
    </div>
</div><div class="w3-panel">
    <select class="w3-input w3-border w3-round-large"
        name="category"
        title="category of expense"
        required="required"
        autofocus>
        <c:forEach items="${sessionScope.categories}" var="singlecategory">
            <option value="${singlecategory.id}" ${singlecategory.id eq sessionScope.expense.categoryId ? 'selected' : ''}>${singlecategory.name}</option>
        </c:forEach>
    </select>
    <label class="w3-label" for="category">Category</label>
</div><div class="w3-panel">
    <div class="w3-row-padding">
        <div class="w3-half">
            <input class="w3-input w3-border w3-round-large"
                type="text"
                name="amount"
                size="40"
                maxlength="40"
                title="amount of expense - #0.00"
                placeholder="0.00"
                pattern="[-+]?[0-9]*[,.]?[0-9]{0,2}"
                required="required"
                autocomplete="off"
                value="${sessionScope.expense.amount}">
            <label class="w3-label" for="amount">Amount (€)</label>
        </div><div class="w3-half">
            <input class="w3-check" type="checkbox" name="monthly" value="true" ${sessionScope.expense.monthly ? 'checked' : ''}>
            <label class="w3-label" for="fixedcost">Monthly</label>
        </div>
    </div>
</div><div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="reason"
        size="40"
        maxlength="40"
        title="reason for expense - at least 3 characters"
        placeholder="gone shopping"
        pattern=".{3,}"
        required="required"
        value="${sessionScope.expense.reason}">
    <label class="w3-label" for="reason">Reason</label>
</div><div class="w3-panel">
    <div class="w3-row">
        <input class="w3-btn w3-green w3-xlarge w3-round-xxlarge" type="submit" value="Save Expense">
        <input class="w3-btn w3-red w3-tiny w3-round-xxlarge" type="reset" value="Reset">
    </div>
</div>
</form>


<jsp:include page="footer.jsp"/>
