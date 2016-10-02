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
<script type="text/javascript">
function w3_open() {
    document.getElementById("mySidenav").style.display = "inherit";
    document.getElementById("mySidenav").style.top = "0px";
}
function w3_close() {
    document.getElementById("mySidenav").style.display = "none";
    document.getElementById("mySidenav").style.top = "";
}
function myAccFunc() {
    var x = document.getElementById("topten");
    if (x.className.indexOf("w3-show") == -1) {
        x.className += " w3-show";
        x.previousElementSibling.className += " w3-green";
    } else {
        x.className = x.className.replace(" w3-show", "");
        x.previousElementSibling.className = x.previousElementSibling.className.replace(" w3-green", "");
    }
}
</script>
            </section>
        </div>
    </body>
</html>
