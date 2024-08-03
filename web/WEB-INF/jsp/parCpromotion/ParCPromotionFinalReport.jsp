<%-- 
    Document   : ParCPromotionReport
    Created on : May 28, 2020, 12:53:17 PM
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
        <script type="text/javascript">
            $(document).ready(function() {
                $.post("GetFiscalYearListForGroupCJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });
            });
            function openaddGroupCWindow() {
                $('#empDetail').modal('show');
            }
            function viewrevertPAROfGroupC(groupCpromotionId, taskid) {

                //var dataURL = RevertReasonForGroupCPAR.htm.attr('data-href');
                $('#revertgroupCParbody').load("RevertReasonForGroupCPAR.htm?groupcPromotionid=" + groupCpromotionId + "&taskid=" + taskid, function() {
                    $('#revertPAROfGroupC').modal('show');
                });
            }


        </script>

    </head>
    <body style="background-color: #FFFFFF;">
        <form:form action="parCPromotionReport.htm" method="POST" commandName="groupCInitiatedbean" class="form-inline">
            <form:hidden path="pendingAtempId" />
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading" align="center" style="background-color: #0071c5;color: #ffffff;font-size: xx-large;"> Group C Promotion</div>

                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>                                
                                    <th width="20%">Created By</th>                                    
                                    <th width="8%">Created On Date</th>
                                    <th width="8%">Financial Year</th>
                                    <th width="20%">Status</th>
                                    <th width="20%">Pending At</th>
                                    <th width="20%">Action</th>

                                </tr>                            
                            </thead>
                            <tbody>
                                <c:forEach items="${promotionReportList}" var="revieweddetails" varStatus="count">
                                    <tr>   
                                        <td>${revieweddetails.reportingempname}, ${revieweddetails.reportingpost}</td>                                        
                                        <td>${revieweddetails.createdondate}</td>
                                        <td>${revieweddetails.fiscalyear}</td>
                                        <td>
                                            <c:if test="${revieweddetails.statusId eq 114}">
                                                <label>
                                                    <b>Reverted By Reviewing Authority</b>
                                                </label>
                                            </c:if>
                                            <c:if test="${revieweddetails.statusId eq 115}">
                                                <label>
                                                    <b>Reverted By Accepting Authority</b>
                                                </label>
                                            </c:if>
                                            <c:if test="${revieweddetails.statusId eq 95}">
                                                <label>
                                                    <b>Completed</b>
                                                </label>
                                            </c:if>
                                            <c:if test="${revieweddetails.statusId eq 91}">
                                                <label>
                                                    <b>Forwarded To Accepting Authority</b>
                                                </label>
                                            </c:if>
                                            <c:if test="${revieweddetails.statusId eq 84}">
                                                <label>
                                                    <b>Forwarded To Reviewing Authority</b>
                                                </label>
                                            </c:if>
                                            
                                        </td>
                                        <td>
                                            <c:if test="${not empty revieweddetails.pendingAtempId and revieweddetails.taskId ne 0}">
                                                ${revieweddetails.pendingAtempId}
                                            </c:if>
                                            <c:if test="${empty revieweddetails.pendingAtempId and revieweddetails.taskId ne 0 and revieweddetails.statusId eq 95}">
                                                <label>
                                                    <b>Completed</b>
                                                </label>
                                            </c:if>
                                            <c:if test="${empty revieweddetails.pendingAtempId and revieweddetails.statusId eq 114}">
                                                <label>
                                                    <b>Reverted By Reviewing Authority</b>
                                                </label>
                                            </c:if>
                                            <c:if test="${empty revieweddetails.pendingAtempId and revieweddetails.statusId eq 115}">
                                                <label>
                                                    <b>Reverted By Accepting Authority</b>
                                                </label>
                                            </c:if>
                                        </td>
                                        <td>

                                            <a href="ViewSelectedEmpDetail.htm?groupCpromotionId=${revieweddetails.groupCpromotionId}&taskId=${revieweddetails.taskId}" class="btn-default"><button type="button" class="btn btn-default">View</button></a> 
                                            <%--<c:if test="${revieweddetails.taskId eq 0}"> --%>
                                            <c:if test="${empty revieweddetails.pendingAtempId and revieweddetails.taskId eq 0 || revieweddetails.statusId eq '114' }">
                                                <a href="showGroupCEmployee.htm?groupCpromotionId=${revieweddetails.groupCpromotionId}&fiscalyear=${revieweddetails.fiscalyear}" class="btn-default"><button type="button" class="btn btn-default">Add Employee</button></a>
                                                <a href="removenewparCPromotionReport.htm?groupCpromotionId=${revieweddetails.groupCpromotionId}" class="btn-default"><button type="button" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?')">Delete</button></a>
                                            </c:if>
                                            <c:if test="${revieweddetails.statusId eq '114' || revieweddetails.statusId eq '115'}">
                                                <a href='javascript:void(0)' onclick="return viewrevertPAROfGroupC('${revieweddetails.groupCpromotionId}', '${revieweddetails.taskId}')"> <img src='./images/revert.png' width='25' height='25' alt='Reverted PAR' title='Revert Reason'/></a> 
                                                </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>

                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <%--    <input type="submit" name="action" class="btn btn-default" value="Add New"/> --%>
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary" onclick="openaddGroupCWindow()">Add New</button>
                        </div>
                    </div>
                </div>
            </div>

            <%-- Search Groupc Employee modal--%>
            <div id="empDetail" class="modal fade" role="dialog">
                <div class="modal-dialog  modal-lg">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Choose Fiscal Year for Performance Appraisal</h4>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label class="control-label col-sm-8">Fiscal Year: </label>
                                <div class="col-sm-4">
                                    <select name="fiscalyear" id="fiscalyear" class="form-control">
                                        <option value="">Year</option>
                                    </select>
                                    <%--<form:select path="fiscalyear" class="form-control">
                                        <form:option value="2020-21" >2020-21</form:option>                                        
                                    </form:select>  --%>

                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">

                            <div class="col-sm-10" >
                                <input type="submit" class="btn btn-primary" name="action" value="Create"/>                                
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <div class="modal fade" id="revertPAROfGroupC" role="dialog" >
                <div class="modal-dialog  modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Reverted PAR By</h4>
                        </div>
                        <div class="modal-body" id="revertgroupCParbody">

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>


        </form:form>
    </body>
</html>