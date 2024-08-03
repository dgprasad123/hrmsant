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
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="panel-footer">
                    <div class="row">
                        <div class="col-sm-1">
                            <form:form action="LeaveSanctionControllerDataNew.htm" method="post">
                                <button type="submit" class="btn btn-default">New Leave</button>
                            </form:form>
                        </div>
                        <div class="col-sm-1">

                        </div>
                    </div>
                </div>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th width="15%">Date of Entry <br>in the Service Book </th>
                                <th width="9%">Type of Leave</th>
                                <th width="9%">From Date</th>
                                <th width="9%">To Date</th>
                                <th align="center" >Suffix From<br> Date</th>
                                <th align="center" >Suffix To <br> Date</th>
                                <th align="center" >Prefix From<br> Date</th>
                                <th align="center" >Prefix To<br> Date</th>
                                <th align="center" >If<br>Long Term Leave</th>


                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${leaveList}" var="jlist">
                                <tr>
                                    <td>${jlist.servicedate}</td>
                                    <td>${jlist.notOrdNo}</td>

                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <div class="row">
                        <div class="col-sm-1">
                            <form:form action="LeaveSanctionControllerDataNew.htm" method="post">
                                <button type="submit" class="btn btn-default">New Leave</button>
                            </form:form>
                        </div>
                        <div class="col-sm-1">
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>