<%-- 
    Document   : delinquentOfficerList
    Created on : Feb 5, 2018, 12:47:46 PM
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
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <form:form action="editRule15Proceeding.htm" method="post" commandName="ProceedingBean">
                    <div class="panel-body">
                        <h4> List of employee on which disciplinary proceeding is being initiated </h4>
                        <table class="table table-bordered">
                            <thead>
                                <tr> 
                                    <th width="5%">Select</th>
                                    <th width="20%">Employee Name</th>
                                    <th width="30%">Dispatch Details</th>

                                </tr>                            
                            </thead>
                            <tbody>
                                <c:forEach items="${delinquentOfficer}" var="employee">
                                    <tr> 
                                        <td>
                                            <c:if test="${dpviewBean.isapprovedbyauthority ne 'Y'}">
                                                <input type="checkbox" name="delinquent" value="${employee.empid}" >
                                            </c:if>
                                        </td>
                                        <td>${employee.fullname}</td>

                                        <td><a href="dispatchList.htm?daId=${ProceedingBean.daId}&comType=COMM_DELINQUENT&comTypeReference=${employee.refid}" class="label label-success"> Dispatch Detail</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden path="daId"/>
                        <c:if test="${iseditable eq true}">
                            <c:if test="${dpviewBean.isapprovedbyauthority ne 'Y'}">
                                <input type="submit" name="action" class="btn btn-default" value="Add Delinquent Officer"/>
                            </c:if>                        
                            <input type="submit" name="action" class="btn btn-default" value="Article of Charge"/>
                        </c:if>
                        <input type="submit" name="action" class="btn btn-default" value="Back"/>  
                        <c:if test="${dpviewBean.isapprovedbyauthority ne 'Y'}">
                            <input type="submit" name="action" class="btn btn-default" value="Delete"/> 
                        </c:if>

                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>

