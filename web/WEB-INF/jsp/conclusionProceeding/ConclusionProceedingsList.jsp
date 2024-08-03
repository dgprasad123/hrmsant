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
                    Departmental Proceeding
                </div>
                <div class="panel-body">
                    <%--<form:hidden path="empid"/> --%>
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th  align="center" nowrap="nowrap">Date of Entry in Service Book</th>
                                <th  align="center" nowrap="nowrap">Order Number</th>
                                <th  align="center" nowrap="nowrap">Date</th>
                                    <%--  <th  align="center" nowrap="nowrap">Date of Issue of<br/> Show Cause Notice</th> --%>
                                    <%-- <th  align="center" nowrap="nowrap">Compliance Reciept Date</th> --%>
                                <th  align="center" nowrap="nowrap">Proceeding Rule</th>
                                    <%--<th  align="center" nowrap="nowrap">Charges</th> --%>
                                    <%--  <th  align="center" nowrap="nowrap">If Censure</th> --%>
                                    <%--  <th align="left" >View <br> </th> --%>
                                <th  align="center" nowrap="nowrap">Attachment</th>
                                <th  align="center" nowrap="nowrap">Narration</th>
                                <th  align="center" >Edit <br></th>
                                <th  align="center" >Action <br></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${procList}" var="proc">
                                <tr>
                                    <td>${proc.doesvbk}</td>
                                    <td>${proc.concprocOrNo}</td>
                                    <td>${proc.concprocOrdate}</td>
                                    <%--  <td>${proc.doiscnotice}</td> --%>
                                    <%--  <td>${proc.comprcptdate}</td> --%>
                                    <td>${proc.ruleofproc}</td>
                                    <%-- <td>${proc.causeofproc}</td> --%>
                                    <td>
                                        <c:if test="${not empty proc.originalFilename}">
                                            <a href="downloadAttachmentOfInitiationProceeding.htm?concprocid=${proc.concprocid}" class="btn btn-default">
                                                <span class="glyphicon glyphicon-paperclip"></span> ${proc.originalFilename}</a>
                                            </c:if>
                                    </td>
                                    <%-- <td>${proc.ifsensure}</td> --%>
                                    <%--  <td><a href="conclusionProceedingView.htm?concprocid=${proc.concprocid}">View</a></td> --%>
                                    <td>${proc.punishmentRewarded}</td>
                                    <td>
                                        <%--  <c:if test="${proc.isValidated eq 'N'}"> --%>
                                            <a href="conclusionProceedingEdit.htm?concprocid=${proc.concprocid}">Edit</a>
                                            <a href="deleteInitiationDetail.htm?concprocid=${proc.concprocid}&crnotid=${proc.crnotid}" class="label label-danger" onclick="return confirm('Are you sure to Delete?')">Delete</a>
                                       <%-- </c:if> --%>
                                    </td>
                                    <td>
                                        <a href="conclusionProceedingView.htm?concprocid=${proc.concprocid}">View</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <form:form action="ConclusionProceedingsFirstPage.htm" method="post" commandName="redesignationForm">

                        <%--<button type="submit" class="btn btn-default">Backlog Entry</button> --%>
                        <input type="submit" name="action" value="Backlog Entry" class="btn btn-default"/>
                        <input type="submit" name="action" value="Back" class="btn btn-default"/>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
