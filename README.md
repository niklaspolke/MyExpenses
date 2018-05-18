# MyExpenses
Web Application to track and analyse my expenses
* Project:  https://waffle.io/niklaspolke/MyExpenses
* Build: [![Build Status](https://travis-ci.org/niklaspolke/MyExpenses.svg)](https://travis-ci.org/niklaspolke/MyExpenses)
* Coverage: [![codecov.io](http://codecov.io/github/niklaspolke/MyExpenses/coverage.svg?branch=master)](http://codecov.io/github/niklaspolke/MyExpenses?branch=master)
* Application: https://posthuman.selfhost.eu/myexpenses

### Features:
* Expenses: amount, reason, date, category, income (true/false), monthly (true/false)
* Category: name
* Account: username, password
* do with Expenses: Add, Edit, Delete, List, Search
* do with Categories: Add, Rename, Delete
* do with Account: Register, Edit, Delete, Login, Logout
* show statistics per month (top 10 per category, pie chart with sum per category, income vs expenses)
* export to csv (month or year)

### Planned features:
* ?

### Architecture:
* Java
* JSP / EL / JSTL / Chartist.js / W3.CSS
* JDBC / HSQLDB
* Maven / Gradle
* Tomcat
#### Dependencies:
* javax.servlet-api 3.1.0
* javax.servlet.jsp.jstl 1.2.1
* jsp-api 2.2
* hsqldb 2.0
* only for test:
  * junit 4.12
  * mockito-core 1.10.19

### License
[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
