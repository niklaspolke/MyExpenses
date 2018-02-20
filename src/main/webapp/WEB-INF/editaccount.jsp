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
    <h3><fmt:message key="editaccount.statistics.title"/></h3>
</div>

<div class="w3-container">
<div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="amountOfExpenses"
        title="<fmt:message key="editaccount.statistics.amountofexpenses.tooltip"/>"
        disabled="disabled"
        value="<c:out value="${requestScope.amountOfExpenses}"/>" >
    <label class="w3-label" for="amountOfExpenses"><fmt:message key="editaccount.statistics.amountofexpenses.label"/></label>
</div>
</div>

<div class="w3-container">
    <h3><fmt:message key="editaccount.title"/></h3>
</div>

<form action="editaccount.jsp" method="post">
<c:if test="${not empty requestScope.errorMessage}"><div class="w3-panel w3-leftbar w3-pale-red w3-border-red">
    <fmt:message key="${requestScope.errorMessage}"/>
</div></c:if>
<div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="login"
        size="40"
        maxlength="20"
        title="<fmt:message key="editaccount.username.tooltip"/>"
        placeholder="<fmt:message key="editaccount.username.default"/>"
        pattern=".{4,}"
        value="<c:out value="${sessionScope.account.login}"/>" >
    <label class="w3-label" for="login"><fmt:message key="editaccount.username.label"/></label>
</div><div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="password"
        name="oldpassword"
        size="40"
        maxlength="30"
        title="<fmt:message key="editaccount.oldpassword.tooltip"/>"
        placeholder="<fmt:message key="editaccount.oldpassword.default"/>"
        pattern=".{4,}"
        required="required"
        value=""
        autofocus>
    <label class="w3-label" for="oldpassword"><fmt:message key="editaccount.oldpassword.label"/></label>
</div><div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="password"
        name="newpassword1"
        size="40"
        maxlength="30"
        title="<fmt:message key="editaccount.newpassword1.tooltip"/>"
        placeholder="<fmt:message key="editaccount.newpassword1.default"/>"
        pattern=".{4,}"
        value="">
    <label class="w3-label" for="newpassword1"><fmt:message key="editaccount.newpassword1.label"/></label>
</div><div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="password"
        name="newpassword2"
        size="40"
        maxlength="30"
        title="<fmt:message key="editaccount.newpassword2.tooltip"/>"
        placeholder="<fmt:message key="editaccount.newpassword2.default"/>"
        pattern=".{4,}"
        value="">
    <label class="w3-label" for="newpassword2"><fmt:message key="editaccount.newpassword2.label"/></label>
</div><div class="w3-panel">
    <div class="w3-row">
        <input class="w3-btn w3-green w3-xlarge w3-round-xxlarge" type="submit" value="<fmt:message key="editaccount.button.label"/>" onclick="return checkPasswords()">
        <input class="w3-btn w3-red w3-tiny w3-round-xxlarge" type="reset" value="<fmt:message key="editaccount.resetbutton.label"/>">
    </div>
</div>
</form>

<script type="text/javascript">
function checkPasswords() {
	var passwd1 = document.forms[0]["newpassword1"].value;
	var passwd2 = document.forms[0]["newpassword2"].value;
	if (passwd1 != passwd2) {
	    alert("<fmt:message key="error.editaccount.passwordsnotequal"/>");
	    return false;
	} else {
		return true;
	}
}
</script>

<jsp:include page="footer.jsp"/>
