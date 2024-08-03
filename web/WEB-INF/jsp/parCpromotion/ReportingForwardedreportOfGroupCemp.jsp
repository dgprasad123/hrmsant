<%-- 
    Document   : ReportingForwardedreportOfGroupCemp
    Created on : Jun 3, 2020, 5:24:33 PM
    Author     : manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">  
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('.datepickertxt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('.datepickerclass').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });


            });
            function markNotfitforpromotion(me, reviewedEmpId, reviewedSpc) {
                var url = "notfitforpromotion.htm";
                $.post(url, {reviewedempId: reviewedEmpId, reviewedspc: reviewedSpc})
                        .done(function(data) {
                            $(me).parent().parent().prev().html('<span class="label label-danger">Not Fit For Shouldering Higher Responsibility</span>');
                            $(me).attr("disabled", true);
                            $(me).prev().attr("disabled", false);
                            ;
                        });

            }
            function markFitforpromotion(me, reviewedEmpId, reviewedSpc) {
                var url = "fitforpromotion.htm";
                $.post(url, {reviewedempId: reviewedEmpId, reviewedspc: reviewedSpc})
                        .done(function(data) {
                            $(me).parent().parent().prev().html('<span class="label label-success">Fit For Shouldering Higher Responsibility</span>');
                            $(me).attr("disabled", true);
                            $(me).next().attr("disabled", false);
                            ;
                        });

            }

            function savePeriodFromToReviewing() {

            }



        </script>
    </head>

    <form:form action="parCPromotionReport.htm" method="POST" commandName="groupCEmployee" class="form-inline">
        <div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">Group C Employee List<br>
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">For <br>
                            <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;"> Financial Year : ${groupCInitiatedbean.fiscalyear} <br>
                                <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: large;">Initiated By Reporting Authority: ${groupCInitiatedbean.reportingempname},${groupCInitiatedbean.reportingpost}
                                </div>
                            </div>
                        </div>
                    </div>


                    <div class="table-responsive">
                        <table class="table table-bordered table-hover table-striped">
                            <thead>
                                <tr>                                
                                    <th width="3%">#</th>
                                    <th width="15%">Employee Name</th>
                                    <th width="15%">Designation</th>
                                    <th width="15%">Remarks of Reporting Authority</th>
                                        <%--   <th width="20%">Period From Reporting</th>
                                           <th width="20%">Period To Reporting</th>
                                           <th width="20%">Period From Reviewing</th>
                                           <th width="20%">Period From Reviewing</th> --%>
                                    <th width="15%">Remarks of Reviewing Authority</th>
                                    <th width="17%">Action</th>
                                </tr>
                            </thead>
                            <tbody>                                        
                                <c:forEach items="${empList}" var="employee" varStatus="count">
                                    <tr>                                    
                                        <td>${count.index + 1}</td>
                                        <td>${employee.reviewedempname}</td>
                                        <td>${employee.reviewedpost}</td>
                                        <td>
                                            <c:if test="${employee.isfitforShoulderingResponsibilityReporting eq 'Y'}">
                                                <span class="label label-success">Fit For Shouldering Higher Responsibility</span>
                                            </c:if>
                                            <c:if test="${employee.isfitforShoulderingResponsibilityReporting eq 'N'}">
                                                <span class="label label-danger">Not Fit For Shouldering Higher Responsibility</span>
                                                <div>${employee.reportingRemarks}</div>
                                                <c:if test="${not empty employee.originalFilename}">
                                                    <a href="downloadAttachmentOfGroupCForNotFit.htm?promotionId=${employee.promotionId}" class="btn btn-default" >
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${employee.originalFilename}
                                                    </a> 
                                                </c:if>
                                            </c:if>
                                        </td>
                                       <%-- <td>${employee.periodFromReporting}</td>
                                        <td>${employee.periodToReporting}</td> --%> 

                                        <td id="reviewingRemarks">
                                            <c:if test="${employee.isfitforShoulderingResponsibilityReviewing eq 'Y'}">
                                                <span class="label label-success">Fit For Shouldering Higher Responsibility</span>
                                                <div>${employee.reviewingRemarks}</div>

                                            </c:if>
                                            <c:if test="${employee.isfitforShoulderingResponsibilityReviewing eq 'N'}">
                                                <span class="label label-danger">Not Fit For Shouldering Higher Responsibility</span>
                                                <div>${employee.reviewingRemarks}</div>
                                            </c:if>
                                        </td>
                                        <%--<td> 
                                            <div class="col-lg-8">
                                                <div class="input-group date">
                                                    <input class="form-control datepickerclass" name="periodFromReviewing" id="periodFromReporting${employee.promotionId}" readonly="true" value="${employee.periodFromReporting}"/> 
                                                    <span class="input-group-addon">
                                                        <span class="glyphicon glyphicon-time"></span>
                                                    </span> 
                                                </div>                                
                                            </div>
                                        </td>
                                        <td>
                                            <div class="col-lg-8">
                                                <div class="input-group date">
                                                    <input class="form-control datepickerclass"  name="periodToReviewing" id="periodToReporting${employee.promotionId}" readonly="true" value="${employee.periodToReporting}"/> 
                                                    <span class="input-group-addon">
                                                        <span class="glyphicon glyphicon-time"></span>
                                                    </span> 
                                                </div> 
                                                <c:if test="${empty isPendingAtEmpId and taskId eq 0}">
                                                    <button type="button" onclick="savePeriodFromToReviewing('${employee.groupCpromotionId}', '${employee.promotionId}')" class="btn btn-default">Save</button>
                                                </c:if>
                                            </div>
                                        </td> --%>
                                        <td>
                                            <div class="btn-group">

                                                <c:choose> 

                                                    <c:when test = "${employee.isfitforShoulderingResponsibilityReviewing eq 'Y'}">
                                                        <a href="remarkOfNotFitForPromotionReviewingReport.htm?promotionId=${employee.promotionId}&groupCpromotionId=${groupCEmployee.groupCpromotionId}&taskId=${groupCEmployee.taskId}" class="btn-default" ><button type="button" class="btn btn-danger">Not Fit For Shouldering Higher Responsibility</button></a> 
                                                    </c:when>
                                                    <c:when test = "${employee.isfitforShoulderingResponsibilityReviewing eq 'N'}">
                                                        <a href="remarkOfFitForPromotionReviewingReport.htm?promotionId=${employee.promotionId}&groupCpromotionId=${groupCEmployee.groupCpromotionId}&taskId=${groupCEmployee.taskId}" class="btn-default"><button type="button" class="btn btn-primary">Fit For Shouldering Higher Responsibility</button></a> 
                                                    </c:when>

                                                    <c:otherwise>  
                                                        <a href="remarkOfFitForPromotionReviewingReport.htm?promotionId=${employee.promotionId}&groupCpromotionId=${groupCEmployee.groupCpromotionId}&taskId=${groupCEmployee.taskId}" class="btn-default"><button type="button" class="btn btn-primary">Fit For Shouldering Higher Responsibility</button></a> 
                                                        <a href="remarkOfNotFitForPromotionReviewingReport.htm?promotionId=${employee.promotionId}&groupCpromotionId=${groupCEmployee.groupCpromotionId}&taskId=${groupCEmployee.taskId}" class="btn-default" disabled="disabled"><button type="button" class="btn btn-danger">Not Fit For Shouldering Higher Responsibility</button></a> 
                                                    </c:otherwise>
                                                </c:choose>

                                            </div> 
                                        </td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden path="groupCpromotionId"/>
                        <form:hidden path="remarkauthoritytype"/>
                        <form:hidden path="taskId"/>
                        <c:if test="${not empty statusId}">

                            <a href='javascript:void(0)' onclick="return viewrevertPAROfGroupC('${groupCEmployee.groupCpromotionId}', '${groupCEmployee.taskId}')"> <img src='./images/revert.png' width='25' height='25' alt='Reverted PAR' title='Revert Reason'/></a> 
                            <div>
                                ${authorityType}<br>${authorityName}
                                ${revertRemaeks}
                            </div>
                        </c:if>
                        <c:if test="${empty isReviewingGivesAllRemark or isReviewingGivesAllRemark == true}">
                            <input type="submit"  name="action" class="btn btn-primary" value="Forward" />
                        </c:if>
                        <c:if test="${not empty isReviewingGivesAllRemark or isReviewingGivesAllRemark == false}">
                            <input type="submit" disabled="true" name="action" class="btn btn-primary" value="Forward" />
                        </c:if>
                        <c:if test="${statusId ne 115}">
                            <input type="submit" name="action" class="btn btn-primary" value="Revert" />
                        </c:if>
                        <a href="GroupCEmployeeForReportingPDF.htm?groupCpromotionId=${groupCEmployee.groupCpromotionId}" class="btn-default" target="_blank"><button type="button" class="btn btn-primary">Download</button></a>

                    </div>    

                    <%--  <c:if test="${groupCEmployee.mode eq 'getemplist' or groupCEmployee.mode eq 'edit'}">  --%>

                </div>
            </div>
        </div>


    </form:form>
</html>
