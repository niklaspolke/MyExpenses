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

<jsp:include page="header.jsp">
    <jsp:param value="true" name="disabled"/>
</jsp:include>

<h3>Login</h3>

<div style="margin: auto; width:400px">
    <form action="login" method="post">
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
                            <label for="password">Password:</label>
                        </th>
                        <td>
                            <input
                                type="password"
                                name="password"
                                size="40"
                                maxlength="30"
                                title="login password"
                                placeholder="<password>"
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
                            <input type="submit" value="Login">
                        </td>
                    </tr>
                </tbody>
            </table>
        </fieldset>
    </form>
    <a href="register.jsp">Register new user</a>
</div>


<jsp:include page="footer.jsp"/>
