<%-- 
    Document   : AerAuthorizationList
    Created on : Feb 4, 2019, 12:38:04 PM
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
                <form:form action="aerauthorizationlist.htm" method="post" commandName="aerAuthorizationBean">
                    <form:hidden path="fiscalYear"/>
                    <div class="panel-body">
                        <h4> Aer Authorization Detail List </h4>
                        <table class="table table-bordered">
                            <thead>
                                <tr> 
                                    <th width="20%">Si No</th>
                                    <th width="20%">Operator</th>
                                    <th width="20%">Approver</th>
                                </tr>                            
                            </thead>
                            <tbody>

                               <c:forEach items="${aerauthorizationList}" var="aer" varStatus="cnt">
                                    <tr> 
                                        <td>${cnt.index+1}</td>
                                        <td>${aer.operator}</td>
                                        <td>${aer.approver}</td>
                                    </tr>
                               </c:forEach>
                              
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Add New" class="btn btn-primary"/> 
                         <input type="submit" name="action" value="Back" class="btn btn-primary"/> 
                    </form:form>
                </div>
            </div>
    </body>
</html>

