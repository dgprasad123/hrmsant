<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Special Category</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <style>
            /* Set height of the grid so .sidenav can be 100% (adjust if needed) */
            .row.content {height: 875px}

            /* Set gray background color and 100% height */
            .sidenav {
                background-color: #f1f1f1;
                height: 100%;
            }

            /* Set black background color, white text and some padding */
            footer {
                background-color: #555;
                color: white;
                padding: 15px;
            }

            /* On small screens, set height to 'auto' for sidenav and grid */
            @media screen and (max-width: 767px) {
                .sidenav {
                    height: auto;
                    padding: 15px;
                }
                .row.content {height: auto;} 
            }
        </style>

        <script type="text/javascript">
            $(document).ready(function() {
                
                $('.date').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                
            });
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>   
            <div class="container-fluid">
                <div class="row content">  
                    <div class="col-sm-12">
                        <div class="panel panel-default">
                            <div class="panel-heading" style="color:green;text-align: center;"><h2>MIS report for Special category employees in HRMS</h2> </div>
                            <div class="panel-body">
                                <form:form action="EmployeeSpecialCategoryReport.htm" commandName="EmployeeSpecialCategoryReport"  method="GET">


                                    <div class="row" >
                                        <div class="col-lg-1"  style="left:50px;">From Date:</div>

                                        <div class="col-lg-2" >

                                            <div style="left:50px;" class='input-group date' id='processDate'>

                                                <form:input class="date" path="txtperiodFrom" id="txtperiodFrom" readonly="true"/>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="col-lg-1" style="left:50px;" >To Date:</div>
                                        <div class="col-lg-2" >
                                            <div class='input-group date' id='processDate'>
                                                <form:input class="date" path="txtperiodTo" id="txtperiodTo" readonly="true"/>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="col-lg-2" >
                                            <input type="submit" name="action" value="Search" class="btn btn-success"/>
                                        </div>
                                        <div align="right" > <a href="DownloadEmployeeSpecialCategoryReportExcel.htm;"><input type="button" value="Export to Excel" class="btn btn-primary"/></a></div>



                                        <div class="table-responsive" style="overflow-x:auto;">
                                            <table class="table table-bordered" style="margin-top:10px;font-size:12px;overflow: scroll;">
                                                <thead>
                                                    <tr bgcolor="#FAFAFA">
                                                        <th>Sl No.</th>
                                                        <th>Employee Name </th>
                                                        <th>HRMS ID</th>                                       
                                                        <th>Date of Special Category</th> 
                                                        <th>Special Category Sub Type</th>
                                                        <th>Office Name</th>
                                                        <th>Department name</th>                                      
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${misList}" var="plist" varStatus="cnt">
                                                        <tr>
                                                            <td style="width:1%">
                                                                ${cnt.index + 1}
                                                            </td>
                                                            <td style="width:20%">${plist.empname}</td>
                                                            <td>${plist.hrmsid}</td> 
                                                            <td>${plist.dateofspecialcategory}</td> 
                                                            <td>${plist.specialcategorysubtype}</td> 
                                                            <td>${plist.officename}</td> 
                                                            <td>${plist.departmentname}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>  

                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div





                </div>
            </div>
    </body>
</html>