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
            $(document).ready(function() {
                $(".txtNum").keypress(function(e) {
                    //if the letter is not digit then display error and don't type anything
                    if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
                        //display error message
                        $("#errmsg").html("Digits Only").show().fadeOut("slow");
                        return false;
                    }
                });
            });
        </script>
        <style type="text/css">
            body{
                font-family: Verdana;
                font-size:16px;
            }
        </style>
    </head>
    <body>
        <form:form action="newDetention.htm" method="post" commandName="leavingForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h2>DETENTION ON VACATION</h2>       
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="30%">Date of entry<br/>in the Service Book</th>
                                    <th width="10%">From Date</th>
                                    <th width="10%">To Date</th>
                                    <th width="10%">No Of Days</th>
                                    <th width="10%">Type Of leave</th>
                                    <th colspan="2" width="15%" align="center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${detentionList}" var="tlist">
                                    <tr>
                                        <td>${tlist.doe}</td>                                       
                                        <td>${tlist.strFdate}</td>
                                        <td>${tlist.strEdate}</td>
                                        <td>${tlist.l_days}</td>
                                        <td>${tlist.type}</td>
                                        <td>
                                            <c:if test="${tlist.isValidated eq 'N'}">
                                                <a href="editDetention.htm?detentionId=${tlist.dv_id}">Edit</a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden class="form-control" path="empid" id="empid"/>
                        <button type="submit" class="btn btn-default">Add New DETENTION</button>  
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
