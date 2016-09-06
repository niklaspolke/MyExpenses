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
        <span class="w3-col s3 w3-opennav w3-xlarge w3-hide-large w3-grey" onclick="w3_open()">&#9776; Menü</span>
        <header class="w3-col s9 l6">
            <h1 style="text-align:center">MyExpenses</h1>
        </header>
        <c:if test="${not disabled}"><nav class="w3-col l3 w3-hide-medium w3-hide-small">
            <ul class="w3-ul">
                <li class="w3-hover-yellow">
                    <a href="editaccount" title="edit account">
                        <img class="w3-left w3-margin-right" src="img/user-id_96.png" alt="user profile" title="user profile" style="width:40px">
                        <span class="w3-xlarge"><c:out value="${sessionScope.account.login}"/></span>
                    </a>
                </li>
            </ul>
        </nav>
        <nav class="w3-col l3 w3-hide-medium w3-hide-small">
            <ul class="w3-ul">
                <li class="w3-hover-yellow">
                    <a href="logout" title="logout">
                        <img class="w3-left w3-margin-right" src="img/flag-alt_96.png" alt="logout" title="logout" style="width:40px">
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
                    <a ${disabled ? '' : 'href="addexpense"'} title="add expense">
                        <img class="w3-left w3-margin-right" src="img/sign-add_96.png" alt="add expense" title="add expense" style="width:40px">
                        <span class="w3-xlarge">Add Expense</span>
                    </a>
                </li><li class="${disabled ? '' : 'w3-hover-yellow'}">
                    <a ${disabled ? '' : 'href="listexpenses"'} title="list expenses">
                        <img class="w3-left w3-margin-right" src="img/folder_96.png" alt="list expenses" title="list expenses" style="width:40px">
                        <span class="w3-xlarge">List Expenses</span>
                    </a>
                </li><li class="${disabled ? '' : 'w3-hover-yellow'}">
                    <a ${disabled ? '' : 'href="listcategories"'} title="list categories">
                        <img class="w3-left w3-margin-right" src="img/layers_96.png" alt="list categories" title="list categories" style="width:40px">
                        <span class="w3-xlarge">List Categories</span>
                    </a>
                </li><li class="${disabled ? '' : 'w3-hover-yellow'}">
                    <a ${disabled ? '' : 'href="showstatistics"'} title="show statistics">
                        <img class="w3-left w3-margin-right" src="img/file-powerpoint_96.png" alt="show statistics" title="show statistics" style="width:40px">
                        <span class="w3-xlarge">Show Statistics</span>
                    </a>
                </li><li class="w3-hover-yellow w3-hide-large">
                    <a href="editaccount" title="edit account">
                        <img class="w3-left w3-margin-right" src="img/user-id_96.png" alt="user profile" title="user profile" style="width:40px">
                        <span class="w3-xlarge">TestAccount</span>
                    </a>
                </li><li class="w3-hover-yellow w3-hide-large">
                    <a href="logout" title="logout">
                        <img class="w3-left w3-margin-right" src="img/flag-alt_96.png" alt="logout" title="logout" style="width:40px">
                        <span class="w3-xlarge">Logout</span>
                    </a>
                </li>
            </ul>
        </nav></c:if>
    <section class="w3-main w3-rest w3-light-grey w3-container" onclick="w3_close()">
