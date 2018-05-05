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
<!DOCTYPE html>
<html>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/w3.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    <title><fmt:message key="title"/></title>
</head>

<c:set var="showmenu" value="${not empty sessionScope.account}" scope="page"/>

<body>
    <div class="w3-row w3-grey">
        <c:if test="${showmenu}"><span class="w3-col s3 w3-opennav w3-xlarge w3-hide-large w3-grey" onclick="w3_open()">
            <fmt:message key="menu.title"/>
        </span></c:if>
        <header class="w3-col s9 l6">
            <h1 style="text-align:center"><fmt:message key="title"/></h1>
        </header>
        <c:if test="${showmenu}"><nav class="w3-col l3 w3-hide-medium w3-hide-small">
            <ul class="w3-ul">
                <li class="w3-hover-yellow">
                    <a href="editaccount.jsp" title="<fmt:message key="menu.editaccount.tooltip"/>">
                        <img class="w3-left w3-margin-right" src="img/user-id_48.png" style="width:40px" alt="<fmt:message key="menu.editaccount.tooltip"/>" title="<fmt:message key="menu.editaccount.tooltip"/>">
                        <span class="w3-xlarge"><c:out value="${sessionScope.account.login}"/></span>
                    </a>
                </li>
            </ul>
        </nav>
        <nav class="w3-col l3 w3-hide-medium w3-hide-small">
            <ul class="w3-ul">
                <li class="w3-hover-yellow">
                    <a href="logout.jsp" title="<fmt:message key="menu.logout.tooltip"/>">
                        <img class="w3-left w3-margin-right" src="img/flag-alt_48.png" style="width:40px" alt="<fmt:message key="menu.logout.tooltip"/>" title="<fmt:message key="menu.logout.tooltip"/>">
                        <span class="w3-xlarge"><fmt:message key="menu.logout.label"/></span>
                    </a>
                </li>
            </ul>
        </nav></c:if>
    </div>
    <div class="w3-row">
        <c:if test="${showmenu}"><nav class="w3-sidenav w3-col s10 m6 l4 w3-collapse w3-animate-left w3-grey" style="position:relative !important" id="mySidenav">
            <a href="javascript:void(0)" onclick="w3_close()" class="w3-closenav w3-xlarge w3-hide-large"><fmt:message key="menu.close.label"/></a>
            <ul class="w3-ul">
                <li class="w3-hover-yellow">
                    <c:if test="${sessionScope.topten.size() gt 0}"><div class="w3-accordion"></c:if>
                        <a ${sessionScope.topten.size() gt 0 ? 'onclick="myAccFunc()"' : 'href="addexpense.jsp"'} title="<fmt:message key="menu.addexpense.tooltip"/>">
                            <img class="w3-left w3-margin-right" src="img/sign-add_48.png" style="width:40px" alt="<fmt:message key="menu.addexpense.tooltip"/>" title="<fmt:message key="menu.addexpense.tooltip"/>">
                            <span class="w3-xlarge"><fmt:message key="menu.addexpense.label"/> ${sessionScope.topten.size() gt 0 ? '&#x23EC;' : ''}</span>
                        </a>
                        <c:if test="${sessionScope.topten.size() gt 0}"><div id="topten" class="w3-accordion-content w3-white w3-card-4">
                            <a href="addexpense.jsp" title="<fmt:message key="menu.addexpense.tooltip"/>"><fmt:message key="menu.addexpense.default"/></a>
                            <c:forEach var="expense" items="${sessionScope.topten}">
                                <a href="addexpense.jsp?category=${expense.categoryId}&reason=${expense.reason}" title="<fmt:message key="menu.addexpense.tooltip"/>"><c:out value="${expense.categoryName += ' - ' += expense.reason}"/></a>
                            </c:forEach>
                        </div>
                    </div></c:if>
                </li><li class="w3-hover-yellow">
                    <a href="listexpenses.jsp" title="<fmt:message key="menu.listexpenses.tooltip"/>">
                        <img class="w3-left w3-margin-right" src="img/folder_48.png" style="width:40px" alt="<fmt:message key="menu.listexpenses.tooltip"/>" title="<fmt:message key="menu.listexpenses.tooltip"/>">
                        <span class="w3-xlarge"><fmt:message key="menu.listexpenses.label"/></span>
                    </a>
                </li><li class="w3-hover-yellow">
                    <a href="searchexpenses.jsp" title="<fmt:message key="menu.searchexpenses.tooltip"/>">
                        <img class="w3-left w3-margin-right" src="img/search_48.png" style="width:40px" alt="<fmt:message key="menu.searchexpenses.tooltip"/>" title="<fmt:message key="menu.searchexpenses.tooltip"/>">
                        <span class="w3-xlarge"><fmt:message key="menu.searchexpenses.label"/></span>
                    </a>
                </li><li class="w3-hover-yellow">
                    <a href="listexpenses.jsp?monthly=true" title="<fmt:message key="menu.listexpenses.monthly.tooltip"/>">
                        <img class="w3-left w3-margin-right" src="img/calendar_48.png" style="width:40px" alt="<fmt:message key="menu.listexpenses.monthly.tooltip"/>" title="<fmt:message key="menu.listexpenses.monthly.tooltip"/>">
                        <span class="w3-xlarge"><fmt:message key="menu.listexpenses.monthly.label"/></span>
                    </a>
                </li><li class="w3-hover-yellow">
                    <a href="listcategories.jsp" title="<fmt:message key="menu.listcategories.tooltip"/>">
                        <img class="w3-left w3-margin-right" src="img/layers_48.png" style="width:40px" alt="<fmt:message key="menu.listcategories.tooltip"/>" title="<fmt:message key="menu.listcategories.tooltip"/>">
                        <span class="w3-xlarge"><fmt:message key="menu.listcategories.label"/></span>
                    </a>
                </li><li class="w3-hover-yellow">
                    <a href="showstatistics.jsp" title="<fmt:message key="menu.showstatistics.tooltip"/>">
                        <img class="w3-left w3-margin-right" src="img/file-powerpoint_48.png" style="width:40px" alt="<fmt:message key="menu.showstatistics.tooltip"/>" title="<fmt:message key="menu.showstatistics.tooltip"/>">
                        <span class="w3-xlarge"><fmt:message key="menu.showstatistics.label"/></span>
                    </a>
                </li><li class="w3-hover-yellow w3-hide-large">
                    <a href="editaccount.jsp" title="<fmt:message key="menu.editaccount.tooltip"/>">
                        <img class="w3-left w3-margin-right" src="img/user-id_48.png" style="width:40px" alt="<fmt:message key="menu.editaccount.tooltip"/>" title="<fmt:message key="menu.editaccount.tooltip"/>">
                        <span class="w3-xlarge"><c:out value="${sessionScope.account.login}"/></span>
                    </a>
                </li><li class="w3-hover-yellow w3-hide-large">
                    <a href="logout.jsp" title="<fmt:message key="menu.logout.tooltip"/>">
                        <img class="w3-left w3-margin-right" src="img/flag-alt_48.png" style="width:40px" alt="<fmt:message key="menu.logout.tooltip"/>" title="<fmt:message key="menu.logout.tooltip"/>">
                        <span class="w3-xlarge"><fmt:message key="menu.logout.label"/></span>
                    </a>
                </li>
            </ul>
        </nav></c:if>
    <section class="w3-main w3-rest w3-light-grey" style="overflow:auto" onclick="w3_close()">
