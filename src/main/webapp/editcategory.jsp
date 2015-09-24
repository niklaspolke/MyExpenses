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


<h3>Edit Category</h3>

<form action="editcategory" method="post">
    <fieldset>
        <legend>Category</legend>
        <table>
            <tbody>
                <tr>
                    <th>
                        <label for="name">name</label>
                    </th>
                    <td>
                        <input
                            type="text"
                            name="name"
                            size="40"
                            maxlength="30"
                            title="name of category"
                            placeholder="category"
                            required="required"
                            value="${sessionScope.category.name}"
                            autofocus>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="reset" value="Reset">
                    </td>
                    <td>
                        <input type="submit" value="Save Category">
                    </td>
                </tr>
            </tbody>
        </table>
    </fieldset>
</form>


<jsp:include page="footer.jsp"/>
