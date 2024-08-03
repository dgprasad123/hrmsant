<%-- 
    Document   : querywindow
    Created on : Apr 24, 2020, 11:12:20 AM
    Author     : Manas
--%>

<%@ page contentType="text/html;charset=windows-1252" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
        <style>
            .table          { overflow-y: auto; height: 100px; }
            .table thead th { position: sticky; top: 0; }
            th     { background:#eee; }
        </style>
    </head>
    <body>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="executeQueryCommand.htm" commandName="queryBuilderBean" method="post">
                    <div class="row">
                        <div class="col-12" style="padding: 10px;">
                            <form:textarea path="query" class="form-control"/>                            
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-12" style="padding: 10px;">
                            <div class="btn-group">
                                <input type="submit" value="Execute" name="action"  class="btn btn-primary"/> 
                                <!--
                                <button type="button" class="btn btn-primary">Show Tables</button>
                                <button type="button" class="btn btn-primary">Show Query</button>
                                -->
                                <input type="submit" value="Download" name="action" class="btn btn-primary"/>                           
                            </div> 
                        </div>
                    </div>
                    <div class="row" style="height: 700px;overflow: auto;">
                        <div class="col-12" style="padding: 10px;">
                            <c:if test="${not empty errormsg}">
                                <div style="color: red;">${errormsg}</div>
                            </c:if>
                            <table class="table table-bordered">
                                <c:forEach items="${datalist}" var="datarow" varStatus="cnt">
                                    <c:if test="${cnt.index eq 0}">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                    <c:forEach items="${datarow}" var="data">
                                                    <th>${data}</th>
                                                    </c:forEach>
                                            </tr>
                                        </thead>
                                    </c:if>
                                    <c:if test="${cnt.index ne 0}">
                                        <tr>
                                            <td>${cnt.index}</td>
                                            <c:forEach items="${datarow}" var="data">
                                                <td>${data}</td>
                                            </c:forEach>
                                        </tr>
                                    </c:if>

                                </c:forEach>
                            </table>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
