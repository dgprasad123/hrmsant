<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">
                <div class="panel panel-default">
                    <h3 style="text-align:center">District Wise Chief Minister Medal Nomination List</h3>
                    <div class="panel-body">
                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Employee Name</th>
                                            <th colspan="3" style="text-align: center">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${data}" var="obj" varStatus="counter">
                                            <tr>
                                                <td width="10%">${counter.count}</td>
                                                <td width="10%">
                                                   
                                                      ${obj.fullname}
                                                    </a>
                                                </td>
                                                <td width="10%" style="text-align: center"> 
                                                    
                                                    <a href="awardormedalDGPPDF.htm?rewardMedalId=${obj.rewardMedalId}&awardMedalTypeId=${obj.awardMedalTypeId}&sltAwardOccasion=${obj.sltAwardOccasion}" target="_blank" title="Annexure-I">
                                                       <img border="0" alt="PDF" src="images/pdf.png" height="20">  Annexure-I
                                                    </a>
                                                </td>
                                                <td width="10%" style="text-align: center">
                                                    <a href="" title="Annexure-II"><img border="0" alt="PDF" src="images/pdf.png" height="20"> Annexure-II</a>
                                                </td>
                                                <td width="10%" style="text-align: center">
                                                    <a href="" title="Annexure-III"><img border="0" alt="PDF" src="images/pdf.png" height="20"> Annexure-III</a>
                                                </td>
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
