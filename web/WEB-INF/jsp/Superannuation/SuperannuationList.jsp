<%-- 
    Document   : SuperannuationList
    Created on : Oct 11, 2022, 10:06:12 AM
    Author     : shantanu
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>District Coordinator :: Superannuation List</title>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           


        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>

        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>

        <script language="javascript" type="text/javascript" >
            $(document).ready(function() {
                $('#example').DataTable({
                    "pageLength": 50,
                    // "order": [[ 5, "desc" ]]
                    // "ordering": false;
                    "order": []
                });
            });
            function archieve_report() {
                window.location = "onlineticketlistResolved.htm";
            }
            $(function() {
                $('#txtperiodFrom').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtperiodTo').datetimepicker({
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
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Superannuation List 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <br/>
                    <h2>Superannuation List</h2>
                    <hr />
                    <br/>
                    <form:form autocomplete="off" role="form" action="#" commandName="superannuationList" method="GET">

                        <div class="row" >
                            <div class="col-lg-1"  style="left:50px;">Year:</div>

                            <div class="col-lg-2" >

                                <div style="left:50px;" class='input-group date' id='processDate'>

                                    <form:select path="supearannuationYear" id="supearannuationYear" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="2022">2022</form:option>
                                        <form:option value="2023">2023</form:option>
                                        <form:option value="2024">2024</form:option>
                                        <form:option value="2025">2025</form:option>
                                        <form:option value="2026">2026</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="col-lg-1" style="left:50px;" >Month:</div>
                            <div class="col-lg-2" >
                                <div class='input-group date' id='processDate'>
                                    <form:select path="supearannuationMonth" id="supearannuationMonth" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="1">January</form:option>
                                        <form:option value="2">February</form:option>
                                        <form:option value="3">March</form:option>
                                        <form:option value="4">April</form:option>
                                        <form:option value="5">May</form:option>
                                        <form:option value="6">June</form:option>
                                        <form:option value="7">July</form:option>
                                        <form:option value="8">August</form:option>
                                        <form:option value="9">September</form:option>
                                        <form:option value="10">October</form:option>
                                        <form:option value="11">November</form:option>
                                        <form:option value="12">December</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="col-lg-2" >
                                <input type="submit" name="action" value="Search" class="btn btn-success"/>
                            </div>
                        </div> 


                    </form:form>  

                    <hr/>    
                    <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0">
                        <thead>
                            <tr>
                                <th style='width:10%'>DDO Code</th>
                                <th style='width:10%'>DDO HRMS Id</th>
                                <th style='width:10%'>DDO Designation</th>
                                <th style='width:10%'>DDO Mobile</th>                                
                                <th style='width:35%'>Employee Designation</th>
                                <th style='width:5%'>Employee HRMS Id</th>
                                <th style='width:5%'>Employee Mobile</th>
                                <th style='width:5%'>GPF No</th>
                                <th style='width:5%'>Employee Name</th>
                                <th style='width:5%'>Employee DOB</th>
                                <th style='width:5%'>Employee DOS</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach var="employeelist" items="${employeeList}" varStatus="theCount">
                                <tr>
                                    <td nowrap><c:out value="${employeelist.ddoCode}"/></td>
                                    <td><c:out value="${employeelist.ddoHrmsId}"/></td>
                                    <td><c:out value="${employeelist.ddoDesignation}"/></td>
                                    <td><c:out value="${employeelist.ddoMobile}"/></td>
                                    <td><c:out value="${employeelist.empDesignation}"/></td>
                                    <td><c:out value="${employeelist.empHrmsId}"/></td>
                                    <td><c:out value="${employeelist.empMobile}"/></td>
                                    <td><c:out value="${employeelist.empGpfNo}"/></td>
                                    <td><c:out value="${employeelist.empName}"/></td>
                                    <td><c:out value="${employeelist.empDob}"/></td>
                                    <td><c:out value="${employeelist.empDos}"/></td>
                                </tr>
                            </c:forEach>                    
                    </table>




                </div>
            </div>
        </div>

    </body>
</html>

