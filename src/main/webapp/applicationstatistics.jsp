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
<%@page import="vu.de.npolke.myexpenses.util.ApplicationStatistics" %>
<%@page import="vu.de.npolke.myexpenses.util.ApplicationStatisticTypes" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="WEB-INF/header.jsp"/>

<h3><fmt:message key="applicationstatistics.title"/></h3>

<% ApplicationStatistics statistics = ApplicationStatistics.getSingleton(); %>
<div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="applicationStart"
        title="<fmt:message key="applicationstatistics.applicationstart.tooltip"/>"
        disabled="disabled"
        value="<c:out value="<%= statistics.getStartOfApplication() %>"/>" >
    <label class="w3-label" for="applicationStart"><fmt:message key="applicationstatistics.applicationstart.label"/></label>
</div><div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="amountOfLogins"
        title="<fmt:message key="applicationstatistics.amountoflogins.tooltip"/>"
        disabled="disabled"
        value="<c:out value="<%= statistics.getCounterForStatisticType(ApplicationStatisticTypes.LOGINS) %>"/>" >
    <label class="w3-label" for="amountOfLogins"><fmt:message key="applicationstatistics.amountoflogins.label"/></label>
</div><div class="w3-panel">
    <input class="w3-input w3-border w3-round-large"
        type="text"
        name="amountOfNewExpenses"
        title="<fmt:message key="applicationstatistics.amountofnewexpenses.tooltip"/>"
        disabled="disabled"
        value="<c:out value="<%= statistics.getCounterForStatisticType(ApplicationStatisticTypes.NEW_EXPENSES) %>"/>" >
    <label class="w3-label" for="amountOfNewExpenses"><fmt:message key="applicationstatistics.amountofnewexpenses.label"/></label>
</div>
<div class="w3-panel">
    <a href="login.jsp" title="<fmt:message key="applicationstatistics.backlink.tooltip"/>"><fmt:message key="applicationstatistics.backlink.label"/></a>
</div>

<jsp:include page="WEB-INF/footer.jsp"/>
