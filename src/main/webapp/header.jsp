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
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/w3.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    <title>MyExpenses</title>
</head>

<c:set var="disabled" value="${param.disabled eq 'true'}" scope="page"/>

<body>
    <div class="w3-row w3-grey">
        <c:if test="${not disabled}"><span class="w3-col s3 w3-opennav w3-xlarge w3-hide-large w3-grey" onclick="w3_open()">
            &#9776; Menü
        </span></c:if>
        <header class="w3-col s9 l6">
            <h1 style="text-align:center">MyExpenses</h1>
        </header>
        <c:if test="${not disabled}"><nav class="w3-col l3 w3-hide-medium w3-hide-small">
            <ul class="w3-ul">
                <li class="w3-hover-yellow">
                    <a href="editaccount" title="edit account">
                        <img class="w3-left w3-margin-right" src="img/user-id_48.png" style="width:40px" alt="user profile" title="user profile">
                        <span class="w3-xlarge"><c:out value="${sessionScope.account.login}"/></span>
                    </a>
                </li>
            </ul>
        </nav>
        <nav class="w3-col l3 w3-hide-medium w3-hide-small">
            <ul class="w3-ul">
                <li class="w3-hover-yellow">
                    <a href="logout" title="logout">
                        <img class="w3-left w3-margin-right" src="img/flag-alt_48.png" style="width:40px" alt="logout" title="logout">
                        <span class="w3-xlarge">Logout</span>
                    </a>
                </li>
            </ul>
        </nav></c:if>
    </div>
    <div class="w3-row">
        <c:if test="${not disabled}"><nav class="w3-sidenav w3-col s10 m6 l4 w3-collapse w3-animate-left w3-grey" style="position:relative !important" id="mySidenav">
            <a href="javascript:void(0)" onclick="w3_close()" class="w3-closenav w3-xlarge w3-hide-large">Close X</a>
            <ul class="w3-ul">
                <li class="${disabled ? '' : 'w3-hover-yellow'}">
                    <c:if test="${sessionScope.topten.size() gt 0}"><div class="w3-dropdown-hover"></c:if>
                        <a ${disabled ? '' : 'href="addexpense"'} title="add expense">
                            <img class="w3-left w3-margin-right" src="img/sign-add_48.png" style="width:40px" alt="add expense" title="add expense">
                            <span class="w3-xlarge">Add Expense${sessionScope.topten.size() gt 0 ? ' &#x23EC;' : ''}</span>
                        </a>
                        <c:if test="${sessionScope.topten.size() gt 0}"><div class="w3-dropdown-content w3-white w3-card-4">
                            <c:forEach var="expense" items="${sessionScope.topten}">
                                <a href="addexpense?category=${expense.categoryId}&reason=${expense.reason}">${expense.categoryName} - ${expense.reason}</a>
                            </c:forEach>
                        </div>
                    </div></c:if>
                </li><li class="${disabled ? '' : 'w3-hover-yellow'}">
                    <a ${disabled ? '' : 'href="listexpenses"'} title="list expenses">
                        <img class="w3-left w3-margin-right" src="img/folder_48.png" style="width:40px" alt="list expenses" title="list expenses">
                        <span class="w3-xlarge">List Expenses</span>
                    </a>
                </li><li class="${disabled ? '' : 'w3-hover-yellow'}">
                    <a ${disabled ? '' : 'href="listexpenses?monthly=true"'} title="list monthly expenses">
                        <img class="w3-left w3-margin-right" src="img/calendar_48.png" style="width:40px" alt="list monthly expenses" title="list monthly expenses">
                        <span class="w3-xlarge">List Monthly Expenses</span>
                    </a>
                </li><li class="${disabled ? '' : 'w3-hover-yellow'}">
                    <a ${disabled ? '' : 'href="listcategories"'} title="list categories">
                        <img class="w3-left w3-margin-right" src="img/layers_48.png" style="width:40px" alt="list categories" title="list categories">
                        <span class="w3-xlarge">List Categories</span>
                    </a>
                </li><li class="${disabled ? '' : 'w3-hover-yellow'}">
                    <a ${disabled ? '' : 'href="showstatistics"'} title="show statistics">
                        <img class="w3-left w3-margin-right" src="img/file-powerpoint_48.png" style="width:40px" alt="show statistics" title="show statistics">
                        <span class="w3-xlarge">Show Statistics</span>
                    </a>
                </li><li class="w3-hover-yellow w3-hide-large">
                    <a href="editaccount" title="edit account">
                        <img class="w3-left w3-margin-right" src="img/user-id_48.png" style="width:40px" alt="user profile" title="user profile">
                        <span class="w3-xlarge"><c:out value="${sessionScope.account.login}"/></span>
                    </a>
                </li><li class="w3-hover-yellow w3-hide-large">
                    <a href="logout" title="logout">
                        <img class="w3-left w3-margin-right" src="img/flag-alt_48.png" style="width:40px" alt="logout" title="logout">
                        <span class="w3-xlarge">Logout</span>
                    </a>
                </li>
            </ul>
        </nav></c:if>
    <section class="w3-main w3-rest w3-light-grey" style="overflow:auto" onclick="w3_close()">
