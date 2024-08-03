<%-- 
    Document   : AddIncrementMasterData
    Created on : 28 Jun, 2016, 3:46:09 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>

        
    </head>
    <body onblur="javascript:window.close();">


            <div class="container-fluid">
                <div class="panel panel-default" style="height:600px;overflow:auto;">
                    
                    <div class="panel-heading" style="background:#004D95;color:#FFFFFF;font-weight:bold;margin-top:60px;">
                        PAY DRAW LIST FOR THE LAST 1 YEAR
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr style="background:#EAEAEA;">
                                    <th>Salary Date</th>
                                    <th>Gross Salary</th>
                                    <th>Deduction</th>
                                    <th>Private Deduction</th>
                                    <th>Net Salary</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${payList}" var="list">
                                    <tr>
                                        <td>${list.salaryDate}</td>
                                        <td>${list.grossAmount}</td>
                                        <td>${list.deduction}</td>
                                        <td>${list.pvtDeduction}</td>
                                        <td>${list.netAmount}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>                            
                        </table>

                    </div>

                </div>
            </div>


    </body>
</html>
