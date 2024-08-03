<%-- 
    Document   : ThreadList
    Created on : Oct 30, 2017, 4:59:26 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <script>
            $(document).ready(function() {
                var url = 'runPayBillThreadStatus.htm';
                $.getJSON(url, function(data) {
                    console.log(data);
                    $("#status7").html(data.ThreadStatus);
                });

            });
        </script>
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
                                    <i class="fa fa-file"></i> Thread List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Thread
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Role List</h2>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>Thread Name</th>
                                            <th>Status</th>
                                            <th>Action</th>                                            
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${serviceThreadList}" var="serviceThread" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index}</td>                                            
                                                <td>${serviceThread.serviceName}</td>
                                                <td>${serviceThread.status}</td>
                                                <td><a href="${serviceThread.startUrl}">Start</a></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>
