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
        <link rel="stylesheet" href="css/styles.css">
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
        <title>MyExpenses</title>
    </head>

<c:set var="disabled" value="${param.disabled eq 'true'}" scope="page"/>

    <body>
        <header>
            <div style="background-color:lightgray;display:table;width:100%">
                <div style="display:table-cell;vertical-align:middle;align:center;width:100%;padding:5px 30px 5px 30px;">
                    <h1 class="title" style="width:100%;margin:auto">MyExpenses</h1>
                </div>
                <div style="display:table-cell;text-align:center;height:130px;padding:5px 30px 5px 30px;">
                    <div style="display:inline-block;width:96px">
                        <c:if test="${not disabled}">
                            <a href="editaccount" title="edit account"><img src="img/user-id_96.png" alt="user profile" title="user profile"/><br/><span style="font-weight:bold"><c:out value="${sessionScope.account.login}"/></span></a>
                        </c:if>
                    </div>
                </div>
                <div style="display:table-cell;text-align:center;height:130px;padding:5px 30px 5px 30px;">
                    <div style="display:inline-block;width:96px">
                        <c:if test="${not disabled}">
                            <a href="logout" title="logout"><img src="img/flag-alt_96.png" alt="logout" title="logout"/><br/><span style="font-weight:bold">Logout</span></a>
                        </c:if>
                    </div>
                </div>
            </div>
        </header>

        <div class="inline-block" style="background-color:lightgray;margin:0px;padding:15px;padding-top:0px">
            <ul class="menu">
                <li class="menu-item" style="${disabled ? '' : 'background-color:white'}"><a ${disabled ? '' : 'href="addexpense"'} title="add expense">
                    <img src="img/sign-add_96.png" alt="add expense" title="add expense"/>
                    <br/>Add Expense
                </a></li>
                <li class="menu-item" style="${disabled ? '' : 'background-color:white'}"><a ${disabled ? '' : 'href="listexpenses"'} title="list expenses">
                    <img src="img/folder_96.png" alt="list expenses" title="list expenses"/>
                    <br/>List Expenses
                </a></li>
                <li class="menu-item" style="${disabled ? '' : 'background-color:white'}"><a ${disabled ? '' : 'href="listcategories"'} title="list categories">
                    <img src="img/layers_96.png" alt="list categories" title="list categories"/>
                    <br/>List Categories
                </a></li>
                <li class="menu-item" style="${disabled ? '' : 'background-color:white'}"><a ${disabled ? '' : 'href="showstatistics"'} title="show statistics">
                    <img src="img/file-powerpoint_96.png" alt="show statistics" title="show statistics"/>
                    <br/>Show Statistics
                </a></li>
            </ul>
        </div>

        <div class="inline-block">
