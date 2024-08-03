<%-- 
    Document   : PropertyStatementStatusReportForAuthority
    Created on : 11 Apr, 2022, 11:42:43 AM
    Author     : Manisha
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Property Statement Report</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            var rowSize = 20;
            var totalPages = 0;
            $(document).ready(function() {
                $.post("GetFiscalYearListCYWiseJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });

            });

            function getpropertyListForAuthority(fiscalyear) {
                var fiscalyear = $("#fiscalyear").val();
                if ($("#fiscalyear").val() == "") {
                    alert("please Choose the Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                } else {
                    alert(fiscalyear);
                    window.location = "groupwisePropertyStatementReportForAuthority.htm?fiscalyear=" + fiscalyear;
                }

            }

        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                <div class="container-fluid">
                    <div class="panel panel-primary">
                        <div class="panel-heading">Property Statement Report</div>
                        <div class="panel-body" style="margin-top:20px;">
                            <div class="row">
                                <div class="col-lg-2">
                                    Select Financial Year
                                </div>
                                <div class="col-lg-8">
                                    <select name="fiscalyear" id="fiscalyear" class="form-control">
                                        <option value="">Year</option>
                                    </select> 	                           
                                </div>
                                <div class="col-lg-2">
                                    <button class="btn-primary" onclick="getpropertyListForAuthority('${fiscalyear}');">Property Statement Report</button>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer"></div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>




