<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/common.js" type="text/javascript"></script>
    </head>
    <body>
        <form:form id="fm" action="substantivepost.htm" method="post" name="myForm" commandName="officewisepostgrplist">
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <center><h2>SUBSTANTIVE POST </h2>
                                <h3>${offname}</h3></center>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>                                            
                                            <th>POST</th>
                                            <th>PLAN</th>
                                            <th>GROUP</th>
                                            <th>MEN IN POSITION</th>
                                            <th>POST CREATION DETAILS</th>
                                        </tr>
                                    </thead>
                                    <tbody>  
                                        <c:forEach items="${postList}" var="substantivepostlist" varStatus="counter">
                                            <tr>
                                                <th scope="row">${counter.index+1}</th>                                                
                                                <td>${substantivepostlist.txtPost} </td>     
                                                <td>${substantivepostlist.plan}</td>
                                                <td>${substantivepostlist.group}</td>
                                                <td>${substantivepostlist.meninposition}</td>
                                                <td>&nbsp;</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
