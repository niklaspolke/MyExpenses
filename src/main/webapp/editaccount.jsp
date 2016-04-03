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


<h3>Edit Account</h3>

<form action="editaccount" method="post">
    <fieldset>
        <legend>Account</legend>
        <table>
            <tbody>
                <tr>
                    <th class="right">
                        <label>Username:</label>
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
                            value="${sessionScope.account.login}"
                            disabled>
                    </td>
                </tr>
                <tr>
                    <th class="right">
                        <label for="password">Old Password:</label>
                    </th>
                    <td>
                        <input
                            type="password"
                            name="oldpassword"
                            size="40"
                            maxlength="30"
                            title="login password"
                            placeholder="<password>"
                            required="required"
                            value=""
                            autofocus>
                    </td>
                </tr>
                <tr>
                    <th class="right">
                        <label for="password2">New Password:</label>
                    </th>
                    <td>
                        <input
                            type="password"
                            name="newpassword1"
                            size="40"
                            maxlength="30"
                            title="repeat new password"
                            placeholder="<password>"
                            pattern=".{4,}"
                            required="required"
                            value="">
                    </td>
                </tr>
                <tr>
                    <th class="right">
                        <label for="password2">Repeat New Password:</label>
                    </th>
                    <td>
                        <input
                            type="password"
                            name="newpassword2"
                            size="40"
                            maxlength="30"
                            title="repeat new password"
                            placeholder="<password>"
                            pattern=".{4,}"
                            required="required"
                            value="">
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
                        <input type="submit" value="Update Account" onclick="return checkPasswords()">
                    </td>
                </tr>
            </tbody>
        </table>
    </fieldset>
</form>

<script type="text/javascript">
function checkPasswords() {
	var passwd1 = document.forms[0]["newpassword1"].value;
	var passwd2 = document.forms[0]["newpassword2"].value;
	if (passwd1 != passwd2) {
	    alert("Error: New Passwords aren't equal!");
	    return false;
	} else {
		return true;
	}
}
</script>

<jsp:include page="footer.jsp"/>
