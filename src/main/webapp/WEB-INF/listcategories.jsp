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
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="header.jsp"/>

<div class="w3-container">
    <h3><fmt:message key="listcategories.title"/></h3>
</div>

<c:if test="${not empty param.error}"><div class="w3-panel w3-leftbar w3-pale-red w3-border-red">
    <fmt:message key="${param.error}"/>
</div></c:if>

<div class="w3-panel">
    <a href="addcategory.jsp" title="<fmt:message key="listcategories.addcategories.tooltip"/>">
        <fmt:message key="listcategories.addcategories.label"/><img src="img/sign-add_24.png" alt="<fmt:message key="listcategories.addcategories.tooltip"/>" title="<fmt:message key="listcategories.addcategories.tooltip"/>" width="24" height="24"/>
    </a>
</div>

<div class="w3-panel w3-padding-8">
    <table class="w3-table-all">
        <thead>
            <tr>
                <th><fmt:message key="listcategories.table.header.name"/></th>
                <th><fmt:message key="listcategories.table.header.actions"/></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="category" items="${requestScope.categories}">
                <tr>
                    <td><c:out value="${category.name}"/></td>
                    <td style="border:none">
                        <a href="editcategory.jsp?id=${category.id}"><img src="img/pencil_24.png" alt="<fmt:message key="listcategories.table.editcategory.tooltip"/>" title="<fmt:message key="listcategories.table.editcategory.tooltip"/>" width="24" height="24"/></a>
                        <a id="delete${category.id}" href="deletecategory.jsp?id=${category.id}" onclick="return prompt('delete${category.id}', '${fn:replace(category, '\"', '&quot;')}')"><img src="img/sign-delete_24.png" alt="<fmt:message key="listcategories.table.deletecategory.tooltip"/>" title="<fmt:message key="listcategories.table.deletecategory.tooltip"/>" width="24" height="24"/></a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<script type="text/javascript">
function prompt(id, category) {
    var confirmed = confirm('<fmt:message key="warn.deletecategory"><fmt:param value="' + category + '"/></fmt:message>');
    if (confirmed) {
        var deleteLink = document.getElementById(id);
        deleteLink.setAttribute('href', deleteLink.getAttribute('href') + '&confirmed=yes');
        return true;
    } else {
        return false;
    }
}
</script>

<jsp:include page="footer.jsp"/>
