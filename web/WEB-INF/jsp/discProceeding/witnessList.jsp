<%-- 
    Document   : witnessList
    Created on : Feb 22, 2018, 12:19:13 PM
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
        <form:form commandName="witnessbean" method="post" action="employeeWitnessList.htm">

        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading" style="text-align: center">
                    <b>Witness List </b>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                         <thead>
                                <tr>
                                    <th width="5%">Select</th>
                                    <%-- <th width="5%">Sl No</th> --%> 
                                    <th width="30%">Employee Name</th>
                                    <th width="45%">Detail</th>
                                    <th width="15%">Status</th>
                                </tr>
                            </thead>
                        <tbody>
                                <c:set var="slno" value="0"/>
                                <c:forEach items="${witnessList}" var="witness" varStatus="cnt">
                                    <c:set var="slno" value="${slno+(cnt.index+1)}"/>
                                    <tr>
                                        <td><input type="checkbox" value="${witness.dacwid}" name="selecteddacwid"/></td>
                                        <%--  <td>${slno}</td> --%> 
                                        <td>${witness.empname}</td>
                                        <td>${witness.spn}</td> 
                                        <td>Inside HRMS</td> 
                                    </tr>
                                </c:forEach>                                    
                                <c:forEach items="${witnessOtherList}" var="witness" varStatus="cnt">
                                    <c:set var="slno" value="${slno+(cnt.index+1)}"/>
                                    <tr>
                                        <td><input type="checkbox" value="${witness.dacwid}" name="selecteddacwid"/></td>
                                            <%--<td>${slno}</td> --%> 
                                        <td>${witness.witnessName}</td>
                                        <td>${witness.addressat},${witness.addresspo},${witness.addressps},${witness.dist},${witness.pincode},${witness.mobile},${witness.email}</td> 
                                        <td>Outside HRMS</td> 
                                    </tr>
                                </c:forEach>
                            </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                        <form:hidden path="dacid"/>
                        <form:hidden path="daId"/>
                        <input type="submit" name="action" value="Add Witness (HRMS)" class="btn btn-default"/>
                        <input type="submit" name="action" value="Add Witness (NonHRMS)" class="btn btn-default"/>
                        <input type="submit" name="action" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to Delete?')"/>
                        <input type="submit" name="action" value="Back" class="btn btn-default"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>