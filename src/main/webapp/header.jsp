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
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <link rel="stylesheet" href="css/styles.css">
        <title>MyExpenses</title>
    </head>

    <body>
        <header>
            <h1 class="title">MyExpenses</h1>
        </header>

        <div class="inline-block">
            <ul class="menu">
                <li class="menu-item"><a href="addexpense" title="add expense">
                    <img src="img/sign-add_96.png" alt="add expense" title="add expense"/>
                    <br/>Add Expense
                </a></li>
                <li class="menu-item"><a href="listexpenses" title="list expenses">
                    <img src="img/folder_96.png" alt="list expenses" title="list expenses"/>
                    <br/>List Expenses
                </a></li>
                <li class="menu-item"><a href="listcategories" title="list categories">
                    <img src="img/layers_96.png" alt="list categories" title="list categories"/>
                    <br/>List Categories
                </a></li>
                <li class="menu-item"><a href="showstatistics" title="show statistics">
                    <img src="img/file-powerpoint_96.png" alt="show statistics" title="show statistics"/>
                    <br/>Show Statistics
                </a></li>
            </ul>
        </div>

        <div class="inline-block">
