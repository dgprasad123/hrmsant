<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form class="form-inline" action="viewWorkingHourDetail.htm" method="POST" commandName="clupdateform">
                    <div class="container-fluid">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="align-center">
                                    <div class="align-center">
                                        <h2 class="align-center">
                                            Working Hour
                                            <form:hidden path="offCode"/>
                                            <form:hidden path="postCode"/>
                                              <form:hidden path="sltyear"/>
                                            <form:hidden path="sltmonth"/>
                                        </h2>
                                    </div>
                                </div>
                            </div>

                            <div class="panel-body" >
                                <table class="table table-bordered" width="100%" >
                                    <thead>
                                        <tr>
                                            <th width="10%">Sl No</th>
                                            <th width="15%">Working Date</th>
                                            <th width="15%">Working Day</th>
                                            <th width="15%">Inpunch Time</th>
                                            <th width="15%">Outpunch Time</th>
                                            <th width="15%">Working Hour</th>

                                        </tr>

                                    </thead>
                                    <tbody>
                                        <c:if test="${not empty monthlyWorkingHourList}">
                                            <c:forEach items="${monthlyWorkingHourList}" var="workingHourList" varStatus="count">
                                                <tr>
                                                    <td><c:out value="${count.index + 1}"/></td>
                                                    <td>${workingHourList.workingDate}</td>
                                                    <td>${workingHourList.workingDay}</td>
                                                    <td>${workingHourList.inpunchTime}</td>
                                                    <td>${workingHourList.outpunchTime}</td>
                                                    <td>
                                                        ${workingHourList.dailyWorkingHour}
                                                    </td>

                                                </tr>
                                            </c:forEach>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                            <div class="panel-footer">
                                <input type="submit" value="Back" name="btn" class="btn btn-danger" />
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
