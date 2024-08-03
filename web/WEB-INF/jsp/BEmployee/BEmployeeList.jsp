<%-- 
    Document   : BEmployeeList
    Created on : 11 Sep, 2023, 4:09:16 PM
    Author     : Hp
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function deleteEmployee(empId)
            {
                if(confirm("Are you sure you want to delete?"))
                {
                    self.location = 'deleteBEmployee.htm?id='+empId;
                }
            }
            </script>
    </head>
    <body style ="margin:10px;">
        <form:form action="BAddNewEmployee.htm" method="POST" cOmmandName="employeeModel">
            <!--Add List here-->
             <div class="container-fluid">
                <!-- Body-->
                <div class="panel panel-default">

                    <!-- Header-->
                    <div class="panel-heading">
                        Employee Recruitment
                    </div>  
                    <!-- Header End-->

                    <!--Inside body-->
                    <div class="panel-body">
                             <table class="table table-bordered">
                                 <thead>
                                <tr>
                                    <th>First Name</th>
                                    <th>Middle Name</th>
                                    <th>Last Name</th>
                                    <th>Mobile No.</th>
                                    <th>Age</th>
                                    <th>Department</th>
                                    <th>District</th>
                                    <th>Office</th>
                                    <th style="text-align:center">Action</th>
                                </tr>
                                 <tbody>
                                     <c:forEach items="${emplist}" var="blist">
                                    <tr>
                                        <td>${blist.firstName}</td>
                                        <td>${blist.middleName}</td>
                                        <td>${blist.lastName}</td>
                                        <td>${blist.mobile}</td>
                                        <td>${blist.age}</td>
                                        <td>${blist.deptCode}</td>
                                        <td>${blist.distCode}</td>
                                        <td>${blist.offCode}</td>
                                        <td style="text-align:center"><a href="BEditEmployee.htm?id=${blist.empId}">Edit</a> | 
                                            <a href="javas cript:void(0)" onclick="javascript: deleteEmployee(${blist.empId})">Delete</a>
                                        </td>

                                    </tr>
                                </c:forEach>
                                     
                                 </tbody>
                            </thead>
                             </table>
                    </div>
                    </div>
             </div>
                        
             <div class="panel-footer">
            <button type="submit" class="btn btn-success">Add New Employee</button>
             </div>
        </form:form>      
    </body>
</html>
