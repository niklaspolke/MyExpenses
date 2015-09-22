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
<script type="text/javascript" src="js/Chart.min.js"></script>


<jsp:include page="header.jsp"/>


<h3>Test Statistics:</h3>

    <canvas id="myChart" width="300" height="300" style="width:300px; height:300px;"></canvas>
    <div id="legend"></div>

<script type="text/javascript">
document.body.onload = function() {
    var ctx = document.getElementById("myChart").getContext("2d");
    var values = JSON.parse('${sessionScope.mychart}');
    var options = {
            segmentShowStroke:true,
            segmentStrokeColor:"#fff",
            segmentStrokeWidth:2,
            percentageInnerCutout:0,
            animateRotate:false,
            animateScale:false
    };
    var myPieChart = new Chart(ctx).Pie(values.parts,options);
    myPieChart.draw();
    document.getElementById("legend").innerHTML = myPieChart.generateLegend();
}
</script>


<jsp:include page="footer.jsp"/>
