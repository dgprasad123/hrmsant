<%-- 
    Document   : BrassAllot
    Created on : Jun 27, 2018, 12:48:56 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <form:form action="empBrassAllotList.htm" commandName="empBrassList" method="post">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h4>Brass Allottment List</h4>
                            </div>
                        </div>
                    </div>
                    <div class="row" style="margin-top:10px;margin-left:5px;">                           
                        
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-lg-12">

                                <div class="table-responsive">
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Sl No</th>
                                                <th>Date of Entry</th>
                                                <th>Order No</th>
                                                <th>Order Date</th>
                                                <th>With Effect Date</th>
                                                <th>Brass No</th> 
                                                <th>Action</th> 
                                            </tr>
                                        </thead>
                                        <tbody>                                        
                                            <c:forEach items="${brassList}" var="bslist" varStatus="count">
                                                <tr>
                                                    <td>${count.index + 1}</td>                                                    
                                                    <td>${bslist.doe}</td>
                                                    <td>${bslist.ordno}</td>
                                                    <td>${bslist.ordDate}</td>
                                                    <td>${bslist.wefDt}</td>
                                                    <td>${bslist.brassNo}</td>
                                                    <td><a href="editBrassAllot.htm?notId=${bslist.notId}&mode='E'">Edit</a></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="submit" class="btn btn-default btn-success" style="width:150px" value="AddNew"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>


