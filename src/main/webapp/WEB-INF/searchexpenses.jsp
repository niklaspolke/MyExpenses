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
    <h3><fmt:message key="searchexpenses.title"/></h3>
</div>

<form action="searchexpenses.jsp" method="post">
<div class="w3-panel">
    <div class="w3-row-padding"><div class="w3-col s12">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="searchtext"
        size="40"
        maxlength="40"
        title="<fmt:message key="searchexpenses.text.tooltip"/>"
        placeholder="<fmt:message key="searchexpenses.text.default"/>"
        pattern=".{3,}"
        required="required"
        value="">
    <label class="w3-label" for="reason"><fmt:message key="searchexpenses.text.label"/></label>
    </div></div>
</div><div class="w3-panel">
    <div class="w3-row">
        <input class="w3-btn w3-green w3-xlarge w3-round-xxlarge" type="submit" value="<fmt:message key="searchexpenses.button.label"/>">
        <input class="w3-btn w3-red w3-tiny w3-round-xxlarge" type="reset" value="<fmt:message key="searchexpenses.resetbutton.label"/>">
    </div>
</div>
</form>


<jsp:include page="footer.jsp"/>
