<%-- 
    Document   : PostIncumbencyChart
    Created on : Jan 6, 2017, 4:48:35 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/common.js" type="text/javascript"></script>
        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                loadCombo("getDeptListJSON.htm", "department", "deptName", "deptCode");
            });
            function loadoffice() {
                var deptCode = $("#department").val();
                loadCombo("getOfficeListJSON.htm?deptcode=" + deptCode, "office", "label", "value");
            }
            function loadPost() {
                var office = $("#office").val();
                loadCombo("getCadreWiseOfficeWiseSPC.htm?offcode=" + office + "&cadrecode=1101", "post", "spn", "spc");
            }
            function searchIncumbency() {
                url = "getIncumbancyChart.htm?spc=" + $("#post").val();
                $('#incumbancyList').empty();
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        relievedate = "";
                        empname = "";
                        if (obj.relievedate) {
                            relievedate = obj.relievedate;
                        }
                        if (obj.fname) {
                            empname = obj.fname;
                        }
                        if (obj.mname) {
                            empname = empname + " " + obj.mname;
                        }
                        if (obj.lname) {
                            empname = empname + " " + obj.lname;
                        }
                        $('#incumbancyList').append('<tr><td>' + (i + 1) + '</td><td>' + empname + '</td><td>' + obj.joindate + '</td><td>' + relievedate + '</td><td>' + obj.jointype + '</td><td><a href="javascript:viewEmployeeIncumbency(\'' + obj.empid + '\')">View</a></td></tr>');
                    });
                });

            }
            function viewEmployeeIncumbency(empId) {
                $("#myModal").modal('show');
                $('#incumbancyEmployeeList').empty();
                url = "getEmployeeIncumbancyChart.htm?empid="+empId;
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        relievedate = "";
                        empname = "";
                        if (obj.relievedate) {
                            relievedate = obj.relievedate;
                        }
                        if (obj.fname) {
                            empname = obj.fname;
                        }
                        if (obj.mname) {
                            empname = empname + " " + obj.mname;
                        }
                        if (obj.lname) {
                            empname = empname + " " + obj.lname;
                        }
                        $('#incumbancyEmployeeList').append('<tr><td>' + (i + 1) + '</td><td>' + empname + '</td><td>' + obj.joindate + '</td><td>' + relievedate + '</td><td>' + obj.jointype + '</td><td><a href="javascript:viewEmployeeIncumbency(\'' + obj.empid + '\')">View</a></td></tr>');
                    });
                });
                
            }
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
                                    <i class="fa fa-file"></i> Employee List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Incumbency Chart
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="department">Department:</label>
                                    <div class="col-sm-10">
                                        <select class="form-control"  name="department" id="department" onchange="loadoffice()">

                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="office">Office:</label>
                                    <div class="col-sm-10">          
                                        <select class="form-control"  name="office" id="office" onchange="loadPost()">

                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="post">Post:</label>
                                    <div class="col-sm-10">          
                                        <select class="form-control"  name="post" id="post">

                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">        
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <button type="button" class="btn btn-default" onclick="searchIncumbency()">Search</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Incumbency List</h2>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>Name</th>
                                            <th>Join Date</th>
                                            <th>Relieve date</th>
                                            <th>Join Type</th>
                                            <th>Action</th>                                            
                                        </tr>
                                    </thead>
                                    <tbody id="incumbancyList">                                        

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Modal -->
            <div id="myModal" class="modal fade" role="dialog">
                <div class="modal-dialog">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Incumbency List</h4>
                        </div>
                        <div class="modal-body">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Sl No</th>
                                        <th>Name</th>
                                        <th>Join Date</th>
                                        <th>Relieve date</th>
                                        <th>Join Type</th>
                                        <th>Action</th>                                            
                                    </tr>
                                </thead>
                                <tbody id="incumbancyEmployeeList">                                        

                                </tbody>
                            </table>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>

                </div>
            </div>
            <!-- Modal -->
    </body>
</html>
