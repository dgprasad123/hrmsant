<%-- 
    Document   : employeeList
    Created on : Feb 22, 2018, 3:38:48 PM
    Author     : manisha
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8" autoFlush="true" buffer="64kb"%>

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
            function openOfficeList() {
                $('#myModal').modal('show');
            }
        </script>
    </head>
    <body>
        <form:form commandName="witnessbean" method="post" action="saveAddWitness.htm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">

                        </div>
                    </div>
                    <div class="panel-body table-responsive">
                        <table class="table table-bordered" >
                            <thead>
                                <tr>
                                    <th width="5%">Select</th>
                                    <th width="5%">Sl No</th>
                                    <th width="55%">Employee Name</th>
                                    <th width="45%">Designation</th>
                                </tr>
                            </thead>

                            <tbody>                                        
                                <c:forEach items="${empList}" var="employee" varStatus="cnt">
                                    <tr>
                                        <td><input type="checkbox" name="selectedHrmsid" value="${employee.empid}-${employee.spc}"></td>
                                        <td>${cnt.index+1}</td>
                                        <td>${employee.fullname}</td>
                                        <td>${employee.post}</td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        
                            <form:hidden path="dacid"/>
                            <form:hidden path="daId"/>
                            <input type="submit" name="action" value="Save" class="btn btn-default"/>
                            <input type="submit" name="action" value="Back" class="btn btn-default"/>
                        
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>