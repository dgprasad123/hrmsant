<%-- 
    Document   : GranteeOfficeList
    Created on : 7 Jan, 2019, 2:12:01 PM
    Author     : Surendra
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <jsp:include page="../report/aerTab.jsp">
            <jsp:param name="menuHighlight" value="PART_B" />
        </jsp:include>

        <form:form action="getDeptWiseOfficeList.htm.htm" commandName="Office">


            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 align="center"> ${OffName} </h1>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12" style="padding-bottom:0px;">
                                <h3 align="right"> 
                                    <a href="getGranteeDetail.htm?aerId=${aerId}" class="btn btn-primary btn-lg" style="margin:0px;font-size:14pt;">Add New Office &raquo;</a>
                                </h3> 
                            </div>
                        </div>    
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered table-hover table-striped">
                            <thead>
                                <tr>
                                    <th>Sl No</th>
                                    <th>Office Name</th>
                                    <th>Office Code</th>
                                    <th>Edit</th>   
                                    <th>AER</th> 
                                </tr>
                            </thead>
                            <tbody>                                        
                                <c:forEach items="${officeList}" var="office" varStatus="count">
                                    <tr>
                                        <td>${count.index + 1}</td>
                                        <td>${office.offName}</td>
                                        <td>${office.offCode}</td>
                                        <td><a href="getGranteeDetail.htm?offCode=${office.offCode}&aerId=${aerId}">Edit</td>
                                        <%--<td><a href="granteeofficewiseEmployee.htm?offCode=${office.offCode}">Manage AER</a></td>--%>
                                        <td><a href="granteeofficewiseAer.htm?offCode=${office.offCode}&aerId=${aerId}">Manage AER</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        
                    </div>
                </div>
            </div>
        </form:form>








        

    </body>
</html>
