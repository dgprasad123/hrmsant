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
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

        <style type="text/css">
            a:hover, a:active {
                background-color: red;
                color: white;
            }
        </style>
    </head>
    <body>
        <form:form class="form-inline" action="ddofeedback.htm" method="GET" commandName="visitstatusform">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="align-center">
                            <div class="align-center">
                                <h2 class="align-center">
                                    District Coordinator Visiting Status
                                </h2>
                            </div>
                        </div>
                    </div>

                    <div class="panel-body" >
                        <table class="table table-bordered" width="100%" >
                            <thead>
                                <tr>
                                    <th width="5%">Sl No</th>
                                    <th width="10%">District Name</th>
                                     <th width="10%">Total No Of DDO</th>
                                    <th width="10%">Total No of Offices Visited</th>
                                    <th width="10%">Total No of Offices Not Visited</th>
                                    <th width="10%">Total No Of Feedback given by DDO</th>
                                   
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${not empty feedbacklist}">
                                    <c:forEach items="${feedbacklist}" var="visitlist" varStatus="count">
                                        <tr>
                                            <td><c:out value="${count.index + 1}"/></td>
                                            <td>
                                                ${visitlist.distName}
                                            </td>
                                              <td>
                                                ${visitlist.totDDOOffice}

                                            </td>
                                            <td>
                                                ${visitlist.totDDOOfficeVisited}

                                            </td>
                                            <td>
                                                ${visitlist.totDDOOfficeNotVisited}

                                            </td>
                                            <td>
                                                ${visitlist.totDDOFeedbackGiven}

                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">

                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>

