<%-- 
    Document   : dispatchList
    Created on : Sep 7, 2018, 12:08:01 PM
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
                <form:form action="dispatchList.htm" method="post" commandName="DispatchDetailsBean">
                    <div class="panel-body">
                        <h4> Dispatch details List </h4>
                        <table class="table table-bordered">
                            <thead>
                                <tr> 
                                    <th width="40%">Post Office Name</th>
                                    <th width="20%">Ems No.</th>
                                    <th width="10%">Ems Copy Attach</th>
                                    <th width="10%">Track Receipt Attach</th>
                                    <th width="10%">Delete</th>
                                </tr>                            
                            </thead>
                            <tbody>

                                <c:forEach items="${dispatchList}" var="dispatch">
                                    <tr> 
                                        <td>${dispatch.postoffcName}</td>
                                        <td>${dispatch.emsNo}</td>
                                        <td><c:if test="${not empty dispatch.scanfilename}">
                                                <a href="downloadDispatchAttachment.htm?ddid=${dispatch.ddid}" class="btn btn-default" >
                                                    <span class="glyphicon glyphicon-paperclip"></span> ${dispatch.scanfilename}
                                                </a>
                                            </c:if>
                                        </td>
                                        <td>${dispatch.scantrackReport}</td>
                                        <td><a href="removeDispatchAttachment.htm?ddid=${dispatch.ddid}" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-remove"></span> Delete</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">

                        <form:hidden path="comTypeReference"/>
                        <form:hidden path="comType"/>
                        <form:hidden path="daId"/>
                        <input type="submit" name="action" value="Add New Dispatch" class="btn btn-default"/> 
                        <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                    </div>

                </form:form>
            </div>
        </div>
    </body>
</html>

