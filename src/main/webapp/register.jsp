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

<jsp:include page="header.jsp">
    <jsp:param value="true" name="disabled"/>
</jsp:include>


<h3>Register new user</h3>

<div style="margin: auto; width:400px">
    <form action="register" method="post">
        <fieldset>
            <legend>User</legend>
            <table>
                <tbody>
                    <tr>
                        <th class="right">
                            <label for="login">Username:</label>
                        </th>
                        <td>
                            <input
                                type="text"
                                name="login"
                                size="40"
                                maxlength="20"
                                title="username - at least 4 characters"
                                placeholder="<username>"
                                pattern=".{4,}"
                                required="required"
                                autofocus>
                        </td>
                    </tr>
                    <tr>
                        <th class="right">
                            <label for="password1">Password:</label>
                        </th>
                        <td>
                            <input
                                type="password"
                                name="password1"
                                size="40"
                                maxlength="30"
                                title="password"
                                placeholder="<password>"
                                pattern=".{4,}"
                                required="required">
                        </td>
                    </tr>
                    <tr>
                        <th class="right">
                            <label for="password2">Repeat Password:</label>
                        </th>
                        <td>
                            <input
                                type="password"
                                name="password2"
                                size="40"
                                maxlength="30"
                                title="repeat password"
                                placeholder="<password>"
                                pattern=".{4,}"
                                required="required">
                        </td>
                    </tr>
                    <tr>
                        <td class="error" colspan="2">${requestScope.errorMessage}</td>
                    </tr>
                    <tr>
                        <td>
                            <input type="reset" value="Reset">
                        </td>
                        <td>
                            <input type="submit" value="Register" onclick="return checkPasswords()">
                        </td>
                    </tr>
                </tbody>
            </table>
        </fieldset>
    </form>
</div>

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


<jsp:include page="footer.jsp"/>
