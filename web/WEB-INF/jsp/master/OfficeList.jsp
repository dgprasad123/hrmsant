<%-- 
    Document   : OfficeList
    Created on : Nov 18, 2017, 1:02:16 PM
    Author     : manisha
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
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            var ofcType = "B";
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
                                    <i class="fa fa-file"></i> Office List 
                                </li>

                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <form:form action="getDeptWiseOfficeList.htm" commandName="Office" method="post">
                            <form:hidden id="hidOfcType" path="hidOfcType"/>
                            <div class="col-lg-2">Department Name</div>
                            <div class="col-lg-8">
                                <form:select path="deptCode" class="form-control">
                                    <form:option value="">Select</form:option>
                                    <form:options items="${departmentList}" itemValue="deptCode" itemLabel="deptName"/>                                
                                </form:select>                            
                            </div>
                            <div class="col-lg-2"><button type="submit" class="form-control">Search</button> </div>
                        </form:form>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Office List</h2>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped" width="100">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>Office Name</th>
                                            <th>DDO Code</th>
                                            <th colspan="2">Action</th>                                            
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${officeList}" var="office" varStatus="count">
                                            <tr>
                                                <td width="10">${count.index + 1}</td>
                                                <td width="70">${office.offName}</td>
                                                <td width="5">${office.ddoCode}</td>
                                                <%--<td><a href="editOfficeDetails.htm?offCode=${office.offCode}">Edit</td>--%>
                                                <td width="10">Edit</td>
                                                <td width="15">
                                                    <a class="dropdown-item" href="createNewEmployee.htm?offCode=${office.offCode}" target="_blank">Create Employee</a> | <br />
                                                    <a class="dropdown-item" href="substantivePostDetails.htm?hiddenDeptName=&hiddenOfficeName=&deptCode=${Office.deptCode}&offCode=${office.offCode}" target="_blank">Create Substantive Post</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>


