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

<h3><fmt:message key="login.title"/></h3>

<form action="login.jsp" method="post">
<c:if test="${not empty param.error}"><div class="w3-panel w3-leftbar w3-pale-red w3-border-red">
    <fmt:message key="${param.error}"/>
</div></c:if>
<c:if test="${not empty param.info}"><div class="w3-panel w3-leftbar w3-pale-green w3-border-green">
    <fmt:message key="${param.info}"/>
</div></c:if>
<div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        name="login"
        type="text"
        size="40"
        maxlength="20"
        title="<fmt:message key="login.username.tooltip"/>"
        placeholder="<fmt:message key="login.username.default"/>"
        pattern=".{4,}"
        required="required"
        autofocus>
    <label class="w3-label" for="login"><fmt:message key="login.username.label"/></label>
</div><div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        name="password"
        type="password"
        size="40"
        maxlength="30"
        title="<fmt:message key="login.password.tooltip"/>"
        placeholder="<fmt:message key="login.password.default"/>"
        required="required">
    <label class="w3-label" for="password"><fmt:message key="login.password.label"/></label>
</div><div class="w3-panel">
    <select class="w3-input w3-border w3-round-large icon-menu"
        name="locale"
        title="locale"
        required="required">
        <option value="de" ${sessionScope.locale eq 'de' ? 'selected' : ''}>Deutsch</option>
        <option value="en" ${sessionScope.locale eq 'en' ? 'selected' : ''}>English</option>
    </select>
</div><div class="w3-panel">
    <div class="w3-row">
        <input class="w3-btn w3-green w3-xlarge w3-round-xxlarge" type="submit" value="<fmt:message key="login.button.label"/>">
        <input class="w3-btn w3-red w3-tiny w3-round-xxlarge" type="reset" value="<fmt:message key="login.resetbutton.label"/>">
    </div>
</div>
</form>
<div class="w3-panel">
    <a href="register.jsp" title="<fmt:message key="login.registerlink.tooltip"/>"><fmt:message key="login.registerlink.label"/></a>
</div><div class="w3-panel">
    <a href="applicationstatistics.jsp" title="<fmt:message key="login.applicationstatisticslink.tooltip"/>"><fmt:message key="login.applicationstatisticslink.label"/></a>
</div>

<jsp:include page="footer.jsp"/>
