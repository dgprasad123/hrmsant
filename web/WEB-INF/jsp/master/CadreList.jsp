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
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">   
             function addCheck(){
                if ($('#dept').val() == "")
                {
                    alert("Please select Department and Search Cadre List");
                    $('#dept').focus();
                    return false;
                }                
            }
            
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
                                    <i class="fa fa-file"></i> Cadre List 
                                </li>                            
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <form:form action="cadreList.htm" commandName="cadre" method="post">
                        <div class="col-lg-2">Select Department Name</div>
                        <div class="col-lg-8">
                            <form:select path="deptCode" id="dept" class="form-control">
                                <form:option value="">Select Department</form:option>
                                <form:options items="${departmentList}" itemValue="deptCode" itemLabel="deptName"/>                                
                            </form:select>                            
                        </div>
                        <div class="col-lg-2" id="search-bttn"><button type="submit" class="form-control">Search</button> </div>
                        </form:form>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h3 class="heading-color">Cadre List</h3>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>Cadre Code</th>
                                            <th>Cadre Name</th>
                                            <th>Action</th>
                                            <th style="text-align:center;">Get Employee List</th>
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${cadrelist}" var="cadre" varStatus="count">
                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td>${cadre.value}</td>
                                                <td>${cadre.label}</td>
                                                <td><a href="getCadreDetail.htm?cadreCode=${cadre.value}">Edit</a></td>
                                                <td align="center"><a href="viewEmpList.htm?cadreCode=${cadre.value}">Employee List</a></td>
                                            </tr>
                                        </c:forEach>                                     
                                    </tbody>
                                </table>
                            </div>
                           
                            <div class="panel-footer" id="add-new">
                                <a href="getCadreDetail.htm?deptCode=${cadre.deptCode}"><button type="button" class="btn btn-info" onclick="return addCheck()">Add New Cadre</button>  </a>
                                <a href="cadreList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>    
                            </div>
                            
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>


