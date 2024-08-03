<%-- 
    Document   : viewEmpListCadreWise
    Created on : 27 Apr, 2022, 3:19:22 PM
    Author     : Devikrushna
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>       
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>

        <script language="javascript" type="text/javascript" >
            $(document).ready(function() {
                $('#example').DataTable({
                    "order": [[0, "asc"]]
                });
            });
        </script>
        <style>
            .row-margin{
                margin-bottom: 20px;
                margin-top: 30px;
            }
            .first-row{
                margin-top: 20px;
            }
            .heading-color{
                color: #337ab7;
                margin-left: 15px;
                margin-bottom: 30px;
            }
        </style>
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
                                    <i class="fa fa-file"></i> Cadrewise Employee List
                                </li>                            
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <form:form action="viewEmpList.htm" commandName="cadre" method="post">
                        <div class="col-lg-12">
                            <h3 class="heading-color">Cadrewise Employee List</h3>
                            <div class="table-responsive first-row">
                                <table class="table table-bordered table-hover table-striped" id="example" style="width:100%">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>Hrms Id</th>
                                            <th>GPF NO</th>
                                            <th>Name of Employee</th>
                                            <th>Office Name</th>
                                            <th>Designation</th>
                                        </tr>
                                    </thead>
                                    <tbody>                                       
                                        <c:forEach items="${empList}" var="emp" varStatus="count">
                                            <tr>                                                 
                                                <td>${count.index + 1}</td>
                                                <td>${emp.hrmsId}</td>
                                                <td>${emp.gpfno}</td>
                                                <td>${emp.empfullName}</td>
                                                <td>${emp.officeName}</td>
                                                <td>${emp.designation}</td>                                                                                            
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="form-group col-sm-12" style="margin-top:20px">
                                <label class="control-label col-sm-1"></label>
                                <div class="text-left col-sm-12" >     
                                    <a href="cadreList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>
                                </div>
                            </div>   
                        </div>
                        </form:form>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>



