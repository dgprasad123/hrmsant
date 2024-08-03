<%-- 
    Document   : ProfileUpdatedEmpList
    Created on : May 31, 2019, 12:24:00 PM
    Author     : manisha
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
        <script>

        </script>
    </head>
    <body>
        <form:form commandName="employee" method="post" action="EmployeeProfileList.htm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" align="center"> 
                    <center><h2><b>Profile Updated Employee List </b></h2></center>
                </div>
                   

                    <div class="row">                                                
                        <div class="col-lg-12">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="5%">Sl No</th>
                                            <th width="55%">Employee Name</th>
                                            <th width="45%">Designation</th>
                                            <th width="45%">Is Profile Verified</th>
                                        </tr>
                                    </thead>

                                    <tbody>                                        
                                        <c:forEach items="${employeeList}" var="employee" varStatus="cnt">
                                            <tr <c:if test="${employee.ifprofileVerified eq 'N'}"> style="color: red;font-weight: bold;" </c:if> >
                                                <td>${cnt.index+1}</td>
                                                <td>${employee.fname}</td>
                                                <td>${employee.curDesg}</td>
                                                <td>${employee.ifprofileVerified}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>