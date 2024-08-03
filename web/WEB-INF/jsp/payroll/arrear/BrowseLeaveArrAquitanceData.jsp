<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                <div class="panel-heading" align="center" style="font-weight: bold;">
                    <c:if test="${not empty HeaderDataList}">
                        <c:forEach var="hdrData" items="${HeaderDataList}">
                            <b> ARREAR PAY BILL  FROM <c:out value="${hdrData.fromMonth}"/> / <c:out value="${hdrData.fromYear}"/> TO 
                                <c:out value="${hdrData.toMonth}"/> / <c:out value="${hdrData.toYear}"/> OF MISCELLANEOUS, <br/>
                                <c:out value="${OffName}"/>, <c:out value="${DeptName}"/>. <br/>
                                Bill No:- <c:out value="${hdrData.billDesc}"/> </b>
                            </c:forEach>
                        </c:if>
                </div>

                <div class="panel-heading" align="center" style="font-weight: bold;">
                    <table width="100%" border="0">
                        <tr>
                            <th width="10%" style="text-align: center;">Name :</th>
                            <td width="20%" align="left"><c:out value="${arrAqMastBean.empName}"/> <b style="color: #0000FF;">(${arrAqMastBean.empCode})</b></td>
                            <th width="15%" style="text-align: center;">Designation :</th>
                            <td width="30%" align="left"><c:out value="${arrAqMastBean.curDesg}"/></td>                            
                            <td width="8%" align="center"> <!-- <input type="submit" value="Back"/> --> </td>
                        </tr>
                    </table>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered table-hover">
                        <tr>
                            <th width="3%">Sl No </th>
                            <th width="9%">Month</th>
                            <th width="9%">Year</th>
                            <th width="9%">Basic</th>
                            <th width="9%">Process</th>
                            <th width="9%">Edit</th>
                            <th width="9%">Delete</th>
                        </tr>
                        <c:forEach var="arrAqMast" items="${arrAqMastModels}" varStatus="cnt">
                            <tr>
                                <td>${cnt.index+1}</td>
                                <td>${arrAqMast.payMonth}</td>
                                <td>${arrAqMast.payYear}</td>
                                <td>${arrAqMast.curBasic}</td>
                                <td><a href="#" class="btn btn-default"><img src="images/process.png" height="20" alt="Reproess"/></a></td>
                                <td><img src="images/view_icon.png" alt="View Detail"/></td>
                                <td><a href="#" class="btn btn-default"><img src="images/delete_icon.png" alt="Delete"/></a></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
                            
            </div>
        </div>
    </body>
</html>
