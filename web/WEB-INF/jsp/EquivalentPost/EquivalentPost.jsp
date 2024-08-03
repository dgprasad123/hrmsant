<%-- 
    Document   : EquivalantPost
    Created on : 30 Dec, 2021, 11:15:02 AM
    Author     : Devikrushna
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
        </script>
    </head>
    <body>
        <form:form action="EquivalentPost.htm" method="post" commandName="equivalpostForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Equivalent Post
                    </div>
                    <div class="panel-body">                       
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                    <th>Sl Number</th>
                                    <th>Date of Entry</th>
                                    <th>Order Number</th>
                                    <th>Order Date</th>                                                                    
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                               <c:forEach var="item" items="${EquivalentpostList}" varStatus="count"> 
                                    <tr>
                                        <td>${count.index + 1}</td>
                                        <td>${item.doe}</td>
                                        <td>${item.notOrdNo}</td>
                                        <td>${item.notOrdDt}</td>                                                                        
                                        <td>                                           
                                            <c:if test="${item.isValidated eq 'N'}"> 
                                                <a href="editEquivalentPost.htm?notId=${item.notId}">Edit</a>
                                            </c:if> 
                                            <c:if test="${item.isValidated eq 'Y'}">    
                                                <a href="viewEquivalentpost.htm?notId=${item.notId}">View</a>
                                            </c:if>                                        
                                        </td>
                                    </tr>
                             </c:forEach> 
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden class="form-control" path="empid" id="empid"/>
                         <a href="addNewEquivalentPost.htm">
                             <button type="button" class="btn btn-info">Add New Equivalent post</button>  </a>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
