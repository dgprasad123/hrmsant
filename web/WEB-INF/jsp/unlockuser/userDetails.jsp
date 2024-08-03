<%-- 
    Document   : userDetails
    Created on : Jan 7, 2019, 11:24:01 AM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>        
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
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> User List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> New User
                                </li>
                            </ol>
                        </div>
                    </div>
                    <form:form action="userlistview.htm" method="POST" commandName="users" class="form-inline">            
                        <div class="form-group">
                            <label class="control-label" >User Type:</label>
                            <form:select class="form-control" path="usertype">
                                <option value="">Select</option>
                                <c:forEach items="${usertypes}" var="usert">
                                    <option value="${usert}">${usert}</option>
                                </c:forEach>                                        
                            </form:select>
                            <input type="submit" name="action" value="Search" class="btn btn-default" />
                            <input type="submit" name="action" value="Add New" class="btn btn-default" />
                        </div>
                        <table class="table table-condensed">
                            <c:forEach items="${userList}" var="user" varStatus="cnt">
                                <tr>
                                    <td>  ${cnt.index + 1}</td>
                                    <td>  ${user}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
