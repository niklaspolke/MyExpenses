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


<h3>List Categories:</h3>

<p>
    <a href="addcategory.jsp" title="add category">
        Add Category<img src="img/sign-add_96.png" alt="add category" title="add category" width="24" height="24"/>
    </a>
</p>


<fmt:setLocale value="de_DE"/>
<table class="tableList bordered">
    <thead>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Modify</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="category" items="${sessionScope.categories}">
            <tr>
                <td class="number"><c:out value="${category.id}"/></td>
                <td><c:out value="${category.name}"/></td>
                <td style="border:none">
                    <a href="editcategory?id=${category.id}"><img src="img/pencil_24.png" alt="edit category" title="edit category" width="24" height="24"/></a>
                    <a href="deletecategory?id=${category.id}"><img src="img/sign-delete_24.png" alt="delete category" title="delete category" width="24" height="24"/></a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>


<jsp:include page="footer.jsp"/>
