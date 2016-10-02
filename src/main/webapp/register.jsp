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

<jsp:include page="WEB-INF/header.jsp">
    <jsp:param value="true" name="disabled"/>
</jsp:include>


<h3>Register new user</h3>

<form action="register" method="post">
<c:if test="${not empty requestScope.errorMessage}"><div class="w3-panel w3-leftbar w3-pale-red w3-border-red">
    ${requestScope.errorMessage}
</div></c:if>
<div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="login"
        size="40"
        maxlength="20"
        title="username - at least 4 characters"
        placeholder="<username>"
        pattern=".{4,}"
        required="required"
        autofocus>
    <label class="w3-label" for="login">Username</label>
</div><div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="password"
        name="password1"
        size="40"
        maxlength="30"
        title="password"
        placeholder="<password>"
        pattern=".{4,}"
        required="required">
    <label class="w3-label" for="password1">Password</label>
</div><div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="password"
        name="password2"
        size="40"
        maxlength="30"
        title="repeat password"
        placeholder="<password>"
        pattern=".{4,}"
        required="required">
    <label class="w3-label" for="password2">Repeat Password</label>
</div><div class="w3-panel">
    <div class="w3-row">
        <input class="w3-btn w3-green w3-xlarge w3-round-xxlarge" type="submit" value="Register" onclick="return checkPasswords()">
        <input class="w3-btn w3-red w3-tiny w3-round-xxlarge" type="reset" value="Reset">
    </div>
</div>
</form>

<script type="text/javascript">
function checkPasswords() {
	var passwd1 = document.forms[0]["password1"].value;
	var passwd2 = document.forms[0]["password2"].value;
	if (passwd1 != passwd2) {
	    alert("Error: Passwords aren't equal!");
	    return false;
	} else {
		return true;
	}
}
</script>


<jsp:include page="WEB-INF/footer.jsp"/>
