<%-- 
    Document   : MinisterDetailsList
    Created on : 1 Feb, 2022, 4:46:40 PM
    Author     : Devi
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
                if ($('#off_as').val() == "")
                {
                    alert("Please Select Minister Type and Search Minister List");
                    $('#off_as').focus();
                    return false;
                }                
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
                                    <i class="fa fa-dashboard"></i>  <a href="#">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Minister Details 
                                </li>
                               
                            </ol>
                        </div>
                    </div>
        <div class="row">
            <form:form action="MinisterDetailSearch.htm" method="post" commandName="mindetailsForm">            
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Minister Details
                    </div>
                    <div class="panel-body"> 
                        <div class="form-group" style="margin-bottom: 60px;">
                            <label class="control-label col-sm-2" style="font-size: 15px;">Select Minister Type: </label>
                                <div class="col-sm-6"> 
                                    <form:select class="form-control" path="off_as" id="off_as">
                                        <form:option value="">Select</form:option>
                                        <form:options items="${Officiatinglist}" itemLabel="officiatingName" itemValue="officiatingId"/>                                                                               
                                    </form:select>
                                </div>
                                <input type="submit" id="search-btn" 
                                        name="action" value="search" class="btn btn-primary"/>     
                         </div>
                         <form:hidden class="form-control" path="hidlmid" id="hidlmid"/>
                          <form:hidden class="form-control" path="deptcode" id="deptcode"/>
                        <table class="table table-bordered table-striped">
                            <thead>                               
                                <tr>
                                    <th>Sl Number</th>                                  
                                    <th>Name Of Ministers</th>
                                    <th>User Id</th>
                                    <th>Password</th>
                                    <th>Current Post</th>                                                                    
                                    <th>Current Department</th>
                                    <th>Mobile Number</th>  
                                    <th>Active Status</th>  
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${MinisterDetailsList}" varStatus="count">
                                <tr>
                                    <td>${count.index + 1}</td>                                  
                                    <td>${item.fullname}</td>
                                    <td>${item.userid}</td>
                                    <td>${item.password}</td>
                                    <td>${item.ministertype}</td>
                                    <td style="width: 30%;">${item.deptname}</td>
                                    <td>${item.mobileno}</td>   
                                    <td>
                                        <c:if test="${item.activemember eq 'Y'}">
                                            <span>Yes</span>
                                        </c:if>
                                        <c:if test="${item.activemember eq 'N'}">
                                            <span>No</span>
                                        </c:if>
                                    </td>
                                   
                                    <td>                                                                   
                                          <a href="editMinisterDetail.htm?hidlmid=${item.hidlmid}&userid=${item.userid}">Edit</a>
            
                                          <a href="viewMinisterDetail.htm?hidlmid=${item.hidlmid}&userid=${item.userid}">View</a>   
                                   
                                    </td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">                     
                        <a href="AddNewMinisterDetail.htm?off_as=${mindetailsForm.off_as}">
                            <button type="button" class="btn btn-info" onclick="return addCheck()">Add New Minister Details</button>  </a>
                    </div>
                </div>         
            </form:form>
        </div>
                </div>
            </div>
        </body>
        </html>

