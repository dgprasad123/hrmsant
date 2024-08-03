<%-- 
    Document   : DPPendingReportDepartmentWise
    Created on : 2 Jul, 2022, 3:39:20 PM
    Author     : Manisha
--%>




<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Par Statement Report</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            var rowSize = 20;
            var totalPages = 0;
            $(document).ready(function() {
                $.post("GetFiscalYearListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });
            });

            function getDPListDepartmentwise() {
                
                    window.location = "departmentwiseDepartmentProceedingReport.htm";
                

            }

        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                <div class="container-fluid">
                    <div class="panel panel-primary">
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Departmental Proceeding Submission Report</div>
                        <div class="panel-body" style="margin-top:20px;">
                            <div class="row">
                                <form:form action="pendingDepartmentProceedingReport.htm" commandName="dPPendingReportBean" method="post" autocomplete="off" class="form-horizontal">
                                    <div class="container-fluid">
                                        <div class="form-group">
                                            <input type="submit" name="action" value="Departmentwise Dp Report" class="btn-primary">
                                            <input type="submit" name="action" value="Cadrewise Dp Report" class="btn-primary">
                                        </div>
                                    </div>                            
                                </form:form>


                            </div>


                        </div>
                        <div class="panel-footer"></div>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>


