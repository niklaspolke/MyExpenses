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
<link rel="stylesheet" href="css/chartist.min.css">


<jsp:include page="header.jsp"/>
<script type="text/javascript" src="js/chartist.min.js"></script>

<h3>Test Statistics:</h3>

<div id="myChart" style="height: 300px;width: 300px;"></div>

<script type="text/javascript">
document.body.onload = function() {
    var data = JSON.parse('${sessionScope.chart}');
    new Chartist.Pie('#myChart', data);
}
</script>


<jsp:include page="footer.jsp"/>
