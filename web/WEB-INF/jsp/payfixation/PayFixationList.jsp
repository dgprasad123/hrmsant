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
    </head>
    <body>
        <form:form action="newPayFixation.htm" method="POST" commandName="payFixation">
            <form:hidden path="notType" id="notType"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="15%">Date of Entry</th>
                                    <th width="15%">Grade Pay</th>
                                    <th width="25%">Revised Scale of Pay/Pay Band</th>
                                    <th width="15%">Revised Basic</th>
                                    <th width="15%">W.E.F<br />Date & Time</th>
                                    <th width="20%">Date of <br>Next Increment</th>
                                    <th width="15%" align="center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${payfixationList}" var="plist">
                                    <tr>
                                        <td>${plist.doe}</td>
                                        <td>${plist.gradePay}</td>
                                        <td>${plist.revPayScale}</td>
                                        <td>${plist.curbasic}</td>
                                        <td>${plist.wefDate}<br />${plist.wefTime}</td>
                                        <td>${plist.dateOfNextIncrement}</td>
                                        <td>
                                            <c:if test="${plist.isValidated eq 'N'}">
                                                <a href="editPayFixation.htm?notId=${plist.notId}&payFixationId=">Edit</a>
                                                <c:if test="${empty plist.canceltype}">
                                                    <a href="SupersedePayFixation.htm?notId=${plist.notId}&notType=${payFixation.notType}">Supersede</a>
                                                    <a href="CancelPayFixation.htm?notId=${plist.notId}&notType=${payFixation.notType}">Cancel</a>
                                                </c:if>
                                                <c:if test="${not empty plist.canceltype}">
                                                    <c:if test="${plist.canceltype eq 'PAYFIXATION'}">
                                                        <a href="SupersedePayFixation.htm?notId=0&supersedeid=${plist.cancelnotid}">Supersede</a>
                                                    </c:if>
                                                    <c:if test="${plist.canceltype eq 'CANCELLATION'}">
                                                        <a href="CancelPayFixation.htm?notId=0&cancelnotId=${plist.cancelnotid}">Cancel</a>
                                                    </c:if>
                                                </c:if>
                                            </c:if>
                                            <c:if test="${plist.isValidated eq 'Y'}">
                                                <a href="viewPayFixation.htm?notId=${plist.notId}&payFixationId=">View</a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${payFixation.notType == 'PAYFIXATION'}">
                            <button type="submit" class="btn btn-default">New Pay Fixation</button>
                        </c:if>
                        <c:if test="${payFixation.notType == 'PAYREVISION'}">
                            <button type="submit" class="btn btn-default">New Pay Revision</button>
                        </c:if>
                        <c:if test="${payFixation.notType == 'STEPUP'}">
                            <button type="submit" class="btn btn-default">New Step-up of Pay</button>
                        </c:if>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
