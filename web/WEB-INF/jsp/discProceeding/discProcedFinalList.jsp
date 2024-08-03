<%-- 
    Document   : discProcedFinalList
    Created on : Nov 6, 2018, 12:42:47 PM
    Author     : manisha
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Departmental Proceedings
                </div>
                <input type="hidden" name="dadid" value="dadid" />
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead><b>Ongoing Proceeding / New Proceeding</b>
                        <tr>                                
                            <%--<th width="5%">Disciplinary Proceeding Status</th> --%>
                            <th width="5%">Under Rule</th> 
                            <th width="5%">Initiated On</th>
                            <th width="30%">Forwarded To</th>
                            <th width="10%">Forwarded Date</th>                                
                            <th width="10%">Status</th>
                            <th width="10%">Action</th>
                            <th width="10%">Court Intervention</th>
                            <th width="10%">Case Diary</th>
                                <%-- <th width="10%">Final Order</th>--%>

                        </tr>                            
                        </thead>
                        <tbody>
                            <c:forEach items="${dpEmpList}" var="dp">
                                <tr>                                    
                                    <%--<td>
                                        <c:if test="${dp.mode eq 'newproceeding'}">
                                            New Proceeding
                                        </c:if>
                                        <c:if test="${dp.mode eq 'ongoingproceeding'}">
                                            Ongoing Proceeding
                                        </c:if>
                                    </td> --%>
                                    <td>${dp.underRule}</td>
                                    <td>${dp.disciplinaryInitiatedOnDate}</td>
                                    <td>${dp.forwardNameAndDegn}</td>
                                    <td>${dp.forwardDate}</td>
                                    <td><span class="label label-success">${dp.dpStatus}</span></td>
                                    <td>
                                        <c:if test="${dp.taskId == 0 and dp.mode eq 'newproceeding'}">
                                            <a href="editRule15Proceeding.htm?action=Edit&daId=${dp.daid}" class="label label-success">Edit</a>
                                        </c:if>
                                        <c:if test="${dp.taskId == 0 and dp.underRule eq 'rule15'}">
                                            <a href="editRule15ProceedingForOngoingDPRule15.htm?daId=${dp.daid}" class="label label-success">Edit</a> 

                                        </c:if>
                                        <c:if test="${dp.taskId == 0 and dp.underRule eq 'rule16'}">
                                            <a href="editRule16ProceedingForOngoingDP.htm?daId=${dp.daid}" class="label label-success">Edit</a>
                                        </c:if>
                                        <c:if test="${dp.taskId != 0}">
                                            <a href="editRule15Proceeding.htm?action=Edit&daId=${dp.daid}" class="label label-success">Dispatch Detail</a>
                                        </c:if>
                                        <%--<c:if test="${dp.taskId == 0}">
                                            <a href="editRule15Proceeding.htm?action=DeleteDP&daId=${dp.daid}" class="label label-danger" onclick="return confirm('Are you sure to Delete?')">Delete</a>
                                        </c:if> --%>
                                        <c:if test="${dp.taskId == 0 and dp.mode eq 'newproceeding'}">
                                            <a href="forwardRule15DiscProceding.htm?daId=${dp.daid}" class="label label-success">Forward</a>
                                        </c:if>

                                        <%--  <c:if test="${dp.taskId == 0 and dp.mode eq 'ongoingproceeding'}">
                                              <a href="rule15Controller.htm?action=Approval Order&daId=${dp.daid}" class="label label-success">Approval Order</a>
                                          </c:if> --%>
                                    </td>
                                    <td><a href="courtCaseForDP.htm?daId=${dp.daid}" class="label label-success">Court Intervention</a> </td>
                                    <td><a href="caseDiaryForRule15ProceedingForOngoingDPRule15.htm?daId=${dp.daid}">Case Diary</a></td>
                                    <%-- <td><a href="discFinalNoticePDF.htm?daId=${dp.daid}" target="_blank">Final Order</a></td> --%>                              
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <table class="table table-bordered">
                        <thead><b>Disposed Proceeding With Penalty</b>
                        <tr>                                
                            <%--<th width="5%">Disciplinary Proceeding Status</th> --%>
                            <th width="5%">Employee Name</th>
                            <th width="5%">Post</th> 
                            <th width="5%">Date of Entry in Service Book</th>
                            <th width="5%">Order Number</th>
                            <th width="30%">Date</th>
                            <th width="10%">Proceeding Rule</th>                                
                            <th width="10%">Attachment</th>
                            <th width="10%">Narration</th>
                           <th width="10%">Edit</th>
                            <th width="10%">Action</th> 
                        </tr>                            
                        </thead>
                        <tbody>
                            <c:forEach items="${procListofficewise}" var="proc">
                                <tr>
                                    <td>${proc.fullName}</td>
                                    <td>${proc.post}</td>
                                    <td>${proc.doesvbk}</td>
                                    <td>${proc.concprocOrNo}</td>
                                    <td>${proc.concprocOrdate}</td>
                                    <td>${proc.ruleofproc}</td>
                                    <td>
                                        <c:if test="${not empty proc.originalFilename}">
                                            <a href="downloadAttachmentOfInitiationProceeding.htm?concprocid=${proc.concprocid}" class="btn btn-default">
                                                <span class="glyphicon glyphicon-paperclip"></span> ${proc.originalFilename}</a>
                                            </c:if>
                                    </td>
                                    <td>${proc.punishmentRewarded}</td>
                                   <td>
                                        <a href="conclusionProceedingEdit.htm?concprocid=${proc.concprocid}">Edit</a>
                                       <%-- <a href="deleteInitiationDetail.htm?concprocid=${proc.concprocid}&crnotid=${proc.crnotid}" class="label label-danger" onclick="return confirm('Are you sure to Delete?')">Delete</a> --%>
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
                    <%--  <form:form action="chooseRuleForDepartmentalProceeding.htm"> --%>
                    <a href="ConclusionProceedingsEmpList.htm?" class="btn-default" ><button type="button" class="btn btn-primary">Disposed Proceeding With Penalty</button></a>
                    <a href="chooseRuleForDepartmentalProceeding.htm?" class="btn-default" ><button type="button" class="btn btn-primary">Ongoing Proceeding / New Proceeding</button></a>
                    <%-- </form:form> --%>
                </div>
            </div>
        </div>
    </body>
</html>