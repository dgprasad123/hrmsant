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
            $(document).ready(function () {
                $(".txtNum").keypress(function (e) {
                    //if the letter is not digit then display error and don't type anything
                    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                        //display error message
                        $("#errmsg").html("Digits Only").show().fadeOut("slow");
                        return false;
                    }
                });
            });
            function confirmUser() {
                var answer = window.confirm("Are you sure to delete this record?");
                if (answer)
                    return true;
                else
                    return false;
            }
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
        <form:form action="AbsorptionList.htm" method="post" commandName="absorptionForm">
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
                                    <th width="15%">Notification Type</th>
                                    <th width="20%" align="center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${absorptionlist}" var="abslist">
                                    <tr>
                                        <td>${abslist.doe}</td>
                                        <td>${abslist.ordno}</td>
                                        <td>${abslist.ordDate}</td>
                                        <td>${abslist.notType}</td>
                                        <td><a href="editAbsorption.htm?notId=${abslist.notId}">Edit</a>
                                            <a href="deleteAbsorption.htm?notId=${abslist.notId}&payId=${abslist.hpayid}&cadreId=${abslist.hCadId}" onclick="confirmUser()">Delete</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden class="form-control" path="empid" id="empid"/>
                        <button type="submit" class="btn btn-default">New Absorption</button> 

                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
