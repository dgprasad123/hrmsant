<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>

    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                </div>
                <div class="panel-body">
                    <c:if test = "${not empty arrAcqList}"> 
                        <table class="table table-bordered">
                            <c:forEach items="${arrheaderList}" var="aqdb">

                                <th align='center'>${aqdb.payMonthName}-${aqdb.payYear}</th>


                            </c:forEach>

                            <c:forEach items="${arrAcqList}" var="aqdb">
                                <tr>
                                    <td colspan="20"><strong>${aqdb.empName}</strong></td>
                                </tr>

                                <tr>

                                    <c:forEach items="${aqdb.empList}" var="aqdb23">
                                        <td>
                                            <table class="table table-bordered">
                                                <c:forEach items="${aqdb23.payList}" var="paydtls">
                                                    <tr>

                                                        <td>${paydtls.adType} &nbsp;</td>
                                                        <td>${paydtls.alreadyPaid} &nbsp;</td>
                                                        <td>${paydtls.toBePaid} &nbsp;</td>
                                                    </tr> 
                                                </c:forEach>
                                            </table>
                                        </td>

                                    </c:forEach>

                                </tr>
                            </c:forEach>                         


                        </table>
                    </c:if>
                    <c:if test = "${empty arrAcqList}">
                        <h3 style='color:red'>Data is not available</h3>

                    </c:if>     
                </div>
                <div class="panel-footer">

                </div>
            </div>
        </div>

        <!-- Print Bill Modal -->



    </body>
</html>
