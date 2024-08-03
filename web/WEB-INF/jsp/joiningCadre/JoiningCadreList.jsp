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
    </head>
    <body>
        <form:form action="newJoiningCadre.htm" method="POST" commandName="joiningCadreForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="20%">Date of Entry</th>
                                    <th width="30%">Notification Order No</th>
                                    <th width="25%">Notification Order Date</th>
                                    <th width="20%">Notification Type</th>
                                    <th width="15%" align="center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${joiningCadrelist}" var="jclist">
                                    <tr>
                                        <td>${jclist.doe}</td>
                                        <td>${jclist.ordNo}</td>
                                        <td>${jclist.ordDate}</td>
                                        <td>${jclist.notType}</td>
                                        <td><a href="editJoiningCadre.htm?notId=${jclist.notId}">Edit</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <button type="submit" class="btn btn-default">New</button>  
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
