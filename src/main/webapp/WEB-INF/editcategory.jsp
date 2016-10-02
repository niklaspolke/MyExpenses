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

<div class="w3-container">
<h3>Edit Category</h3>
</div>

<form action="editcategory.jsp" method="post">
<input type="hidden" name="id" value="${requestScope.category.id}">
<div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="name"
        size="40"
        maxlength="30"
        title="name of category"
        placeholder="category"
        required="required"
        value="${requestScope.category.name}"
        autofocus>
    <label class="w3-label" for="name">Name</label>
</div><div class="w3-panel">
    <div class="w3-row">
        <input class="w3-btn w3-green w3-xlarge w3-round-xxlarge" type="submit" value="Save Category">
        <input class="w3-btn w3-red w3-tiny w3-round-xxlarge" type="reset" value="Reset">
    </div>
</div>
</form>


<jsp:include page="footer.jsp"/>
