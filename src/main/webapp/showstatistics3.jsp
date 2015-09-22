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


<jsp:include page="header.jsp"/>
<script type="text/javascript" src="js/zingchart.min.js"></script>

<h3>Test Statistics:</h3>

<div id="myChart" class="ct-chart" style="height: 400px;width: 600px;"></div>

<script type="text/javascript">
    var chartData={
    	        "type":"pie",
    	        "legend":{ },
    	        "background-color":"none",
    	        "value-box":{
    	            "visible":false
    	          },
    	        "series":[
    	          {
    	            "text":"Apples",
                    "values":[5]
    	          },
    	          {
    	            "text":"Oranges",
    	            "values":[8]
    	          },
    	          {
    	            "text":"Bananas",
    	            "values":[22]
    	          },
    	          {
    	            "text":"Grapes",
    	            "values":[16]
    	          },
    	          {
    	            "text":"Cherries",
    	            "values":[12]
    	          }
    	        ]
    };
    window.onload=function(){  // Render Method[2]
            zingchart.render({ 
            id:'myChart',
            data:chartData,
            height:400,
            width:600
        });
    };
</script>


<jsp:include page="footer.jsp"/>
