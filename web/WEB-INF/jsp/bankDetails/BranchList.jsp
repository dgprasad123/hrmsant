<%-- 
    Document   : BranchList
    Created on : Aug 1, 2018, 4:23:43 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">

        </script>
        <style>
            .close {
                color: white;
                float: right;
                font-size: 28px;
                font-weight: bold;
            }

            .close:hover,
            .close:focus {
                color: #000;
                text-decoration: none;
                cursor: pointer;
            }

            .modal-header {
                padding: 2px 16px;
                background-color: lightblue;
                color: white;
            }

            .modal-body {padding: 5px 30px;height: 200px;}

            .modal-footer {
                padding: 2px 16px;
                background-color: lightblue;
                color: white;
            }

            #myInput {
                background-image: url('/css/searchicon.png');
                background-position: 10px 10px;
                background-repeat: no-repeat;
                width: 100%;
                font-size: 16px;
                padding: 12px 20px 12px 40px;
                border: 1px solid #ddd;
                margin-bottom: 12px;
            }

            #myTable {
                border-collapse: collapse;
                width: 100%;
                border: 1px solid #ddd;
                font-size: 16px;
            }

            #myTable th, #myTable td {
                text-align: left;
                padding: 12px;
            }

            #myTable tr {
                border-bottom: 1px solid #ddd;
            }

            #myTable tr.header {
                background-color: darkslategrey;
                color: #FFFFFF;
            }
        </style>
    </head>
    <body>
        <form:form action="branchDetails.htm" commandName="bankbranchModel" method="post">

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
                                        <i class="fa fa-file"></i> Branch List
                                    </li>                                
                                </ol>
                            </div>
                        </div>                   
                        <form:hidden id="bankcode" path="bankcode"/>
                        <div class="row" style="margin-top: 10px;">
                            <div class="table-responsive">
                                <table id="myTable" class="table table-striped table-bordered table-success" width="100%" cellspacing="0">
                                    <thead>
                                    <div class="row">
                                        <label class="control-label col-sm-4" style="font-size:16px">BANK NAME: </label>
                                        <div  style="font-size:20px; font-style: bold;" >
                                           ${bankName}  
                                        </div>
                                        <div class="col-50"> 
                                            <div class="pull-right"> 
                                                <input type="submit" name="submit" class="btn btn-success" style="width:150px" value="Back"/>
                                                <button type="button" class="btn btn-success" data-toggle="modal" data-target="#newBranch"  style="width:150px">Add New Branch</button>                                        
                                            </div>
                                        </div>
                                    </div><br/>
                                    <tr class="header">
                                        <th>SL NO</th>
                                        <th>BRANCH CODE</th>
                                        <th>BRANCH NAME</th>
                                        <th>IFSC CODE</th>
                                        <th>MICR CODE</th>
                                        <th>ACTION</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="branchList1" items="${branch_list}" varStatus="count">
                                            <tr>
                                                <td><c:out value="${count.index+1}"/></td>
                                                <td><c:out value="${branchList1.branchcode}"/></td>
                                                <td><c:out value="${branchList1.branchname}"/></td>
                                                <td><c:out value="${branchList1.ifsccode}"/></td>
                                                <td><c:out value="${branchList1.micrcode}"/></td> 
                                                <td><a href="#">Edit</a></td> 
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container">
                <div class="modal fade" id="newBranch" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->

                        <div class="modal-content modal-lg">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Add New Branch</h4>
                            </div>
                            <div class="modal-body">                        
                                <br>
                                <div class="row align-center"> 
                                    <table border="0" width="90%"  cellspacing="0" style="font-size:12px; font-family:verdana; alignment-adjust: central">
                                        <tr>
                                            <td style="text-align:left; font-size: 12px;padding-left:70px">
                                                <form:label path="mdlbranchname">BRANCH NAME <span class="add-on" style="color: red">*</span></form:label>

                                                </td>
                                                <td style="padding-right:20px">
                                                <form:input path="mdlbranchname" class="form-control" size="width:100%"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="text-align:left; font-size: 12px;padding-left:70px">
                                                <form:label path="mdlifsccode">IFSC CODE <span class="add-on" style="color: red">*</span></form:label>
                                                </td>
                                                <td style="padding-right:20px">
                                                <form:input path="mdlifsccode" class="form-control" size="width:100%" />
                                            </td>
                                        </tr>
                                        <tr>
                                            <td style="text-align:left; font-size: 12px;padding-left:70px">
                                                <form:label path="mdlmicrcode">MICR CODE</form:label>
                                                </td>
                                                <td style="padding-right:20px">
                                                <form:input path="mdlmicrcode" class="form-control" size="width:100%"/>
                                            </td>
                                        </tr>
                                        
                                    </table>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <input type="submit" name="submit" class="btn btn-default" style="width:70px" value="Add" onclick="javascript: blankfield()"/> 
                                <button type="button" class="btn btn-default" data-dismiss="modal" style="width:70px">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
