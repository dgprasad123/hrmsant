<%-- 
    Document   : DetailReportOfPARCemplist
    Created on : Mar 13, 2020, 4:52:44 PM
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
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script language="javascript" src="js/servicehistory.js" type="text/javascript"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            var selectedobj;
            $(document).ready(function() {
                $("#showDateWindow").hide();

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
            function savePeriodFromToReporting(me, assessmentTypeReporting, periodFromReporting, periodToReporting, promotionId) {
                tassessmentTypeReporting = $("input[name='assessmentTypeReporting']:checked").val();
                alert(tassessmentTypeReporting);
                if (tassessmentTypeReporting == 'fullPeriod') {
                    tperiodFromReporting = "01-APR-2022";
                    alert(tperiodFromReporting);
                    tperiodToReporting = "31-MAR-2023";
                    alert(tperiodToReporting);

                } else {
                    tperiodFromReporting = $("#periodFromReporting").val();
                    alert(tperiodFromReporting);
                    tperiodToReporting = $("#periodToReporting").val();
                    alert(tperiodToReporting);
                }
                tpromotionId = $("#promotionId").val();
                if (tassessmentTypeReporting == null) {
                    alert("Choose Any Of Radio Button");
                    $("#tassessmentTypeReporting").focus();
                    return false;
                }
                if (tperiodFromReporting == '') {
                    alert("Please enter From Date");
                    return false;
                }
                if (tperiodToReporting == '') {
                    alert("Please enter To Date");
                    return false;
                }
                if ((tperiodFromReporting != '') && (tperiodToReporting != '')) {
                    var ftemp = tperiodFromReporting.split("-");
                    var ttemp = tperiodToReporting.split("-");
                    var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                    var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);
                    if (fdt > tdt) {
                        alert("From Date must be less than To Date");
                        return false;
                    }
                }
                //alert($("#promotionId").val());
                var url = "saveFromAndToDateForReporting.htm";
                $.post(url, {assessmentTypeReporting: tassessmentTypeReporting, periodFromReporting: tperiodFromReporting, periodToReporting: tperiodToReporting, promotionId: tpromotionId})
                        .done(function(data) {
                            alert("Saved Successfully");
                            $('#periodDetail').modal('hide');
                            //alert($(selectedobj).parent().prev().text());
                            $(selectedobj).parent().prev().text(tperiodFromReporting);
                            $(selectedobj).prev().text(tperiodToReporting);
                        });
            }
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
            function openEditPeriodDetailWindow(me, promotionId) {
                selectedobj = me;
                var url = "getFromDateToDateJSON.htm?promotionId=" + promotionId;
                /*$.getJSON(url, function(data) {
                 alert(data.periodFromReporting);
                 });*/
                $.ajax({
                    url: url,
                    dataType: "json",
                    success: function(data) {
                        alert(data);
                        alert(data.periodFromReporting);
                        $("#periodFromReporting").val(data.periodFromReporting);
                        $("#periodToReporting").val(data.periodToReporting);
                        $("#assessmentTypeReporting").val(data.assessmentTypeReporting);
                        $("#promotionId").val(promotionId);
                    }
                });
                $('#periodDetail').modal('show');
            }
            function radioClicked() {
                $("#showDateWindow").hide();
                var radioValue = $("input[name='assessmentTypeReporting']:checked").val();
                if (radioValue == "fullPeriod") {
                    alert("From Date Should be 1-04-2022 and To Date Should be 31-03-2022");
                    alert("Are You Sure You Want to Choose ?");
                } else {
                    $("#showDateWindow").show();
                }

            }


        </script>
    </head>
    <form:form action="parCPromotionReport.htm" method="POST" commandName="groupCEmployee" class="form-inline">
        <form:hidden path="promotionId"/>
        <form:hidden path="taskId"/>
        <div class="row">
            <div class="col-lg-12">
                <div class="panel-heading" align="center" style="background-color: #0071c5;color: #ffffff;font-size: xx-large;"> Group C employee List</div>
                <div class="table-responsive" style="min-height: 500px;">
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>                                
                                <th width="3%">#</th>
                                <th width="15%" style="text-align: center">Employee Name</th>
                                <th width="15%" style="text-align: center">Designation</th>
                                <th width="15%" style="text-align: center">Remarks</th>
                                    <%--<th width="20%" style="text-align: center">Period From</th>
                                    <th width="20%" style="text-align: center">Period To</th> --%>
                                    <c:if test="${empty isPendingAtEmpId}">
                                    <th width="17%" style="text-align: center">Action</th>
                                        <c:if test="${taskId eq 0}">
                                        <th width="12%" style="text-align: center">Remove</th>
                                        </c:if>
                                    </c:if>
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
                                            <c:if test="${not empty employee.originalFilename }">
                                                <a href="downloadAttachmentOfGroupCForNotFit.htm?promotionId=${employee.promotionId}" class="btn btn-default" >
                                                    <span class="glyphicon glyphicon-paperclip"></span> ${employee.originalFilename}
                                                </a> 
                                            </c:if>

                                        </c:if>
                                    </td>
                                    <%-- <td style="text-align: center">${employee.periodFromReporting}</td>
                                     <td style="text-align: center"><span>${employee.periodToReporting}</span>
                                         <button type="button"  onclick="openEditPeriodDetailWindow(this, '${employee.promotionId}')" class="btn btn-info">
                                             <span class="glyphicon glyphicon-pencil">Edit</span>
                                         </button>
                                     </td> --%>
                                    <c:if test="${empty isPendingAtEmpId}">
                                        <td>
                                            <div class="btn-group">
                                                <c:if test="${employee.isfitforShoulderingResponsibilityReporting eq 'Y'}">
                                                    <a href="remarkOfNotFitForPromotionReport.htm?promotionId=${employee.promotionId}&groupCpromotionId=${groupCEmployee.groupCpromotionId}" class="btn-default" ><button type="button" class="btn btn-danger">Not Fit For Shouldering Higher Responsibility</button></a>
                                                </c:if>

                                                <c:if test="${employee.isfitforShoulderingResponsibilityReporting eq 'N'}">                                                
                                                    <a href="deleteremarkOfNotFitForPromotionReport.htm?promotionId=${employee.promotionId}&groupCpromotionId=${groupCEmployee.groupCpromotionId}" class="btn-default"><button type="button" class="btn btn-primary" onclick="return confirm('Are You Sure to Mark As Fit For Shouldering Higher Responsibility?')">Fit For Shouldering Higher Responsibility</button></a>
                                                    <a href="editremarkOfNotFitForPromotionReport.htm?promotionId=${employee.promotionId}&groupCpromotionId=${groupCEmployee.groupCpromotionId}" class="btn-default">
                                                    </a>
                                                </c:if>   
                                            </div>
                                        </td>
                                        <c:if test="${empty isPendingAtEmpId and taskId eq 0 || statusId eq 114}">
                                            <td>
                                                <a href="removeselectedEmployeeDetail.htm?promotionId=${employee.promotionId}&groupCpromotionId=${groupCEmployee.groupCpromotionId}">
                                                    <button type="button" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?')"><span class="glyphicon glyphicon-remove"></span></button></a> 
                                            </td>
                                        </c:if>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <form:hidden path="groupCpromotionId"/>
                    <form:hidden path="pendingAtEmpId"/>
                    <form:hidden path="fiscalyear"/>
                    <c:if test="${empty isPendingAtEmpId and taskId eq 0 || statusId eq 114 || statusId eq 115}">
                        <input type="submit" name="action" class="btn btn-default" value="Forward"/>
                    </c:if>
                    <c:if test="${not empty isPendingAtEmpId  || statusId eq 114 || statusId eq 115 || statusId eq 95}">
                        <a href="GroupCEmployeeForAppraiseePDF.htm?groupCpromotionId=${groupCEmployee.groupCpromotionId}" class="btn-default" target="_blank"><button type="button" class="btn btn-primary">Download</button></a>
                        <%--<input type="submit" name="action" class="btn btn-default" value="Download"/> --%>
                    </c:if>
                    <input type="submit" name="action" class="btn btn-default" value="Back"/>  

                </div>
            </div>
        </div>
    </form:form>

    <div id="periodDetail" class="modal fade" role="dialog">
        <div class="modal-dialog  modal-lg">
            <input type="hidden" name="hidpromotionId" value="hidpromotionId"/>
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title" style="text:align center"><b>Edit Period Detail</b></h4>
                </div>
                <div class="modal-body"> 
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="fa">&nbsp;</label>
                        </div>

                        <div class="col-lg-2"> 
                            <input type="radio" name="assessmentTypeReporting" value="fullPeriod" onclick="radioClicked()"/><b>Full Period</b>
                        </div>
                        <div class="col-lg-2">
                            <input type="radio" name="assessmentTypeReporting" value="halfPeriod" onclick="radioClicked()"/><b>Partial Period</b>
                        </div>                                                                    
                    </div>
                    <div class="row" style="margin-bottom: 7px;" id="showDateWindow">
                        <div class="col-lg-1">
                            <label for="fa"></label>
                        </div>
                        <div class="col-lg-2">
                            <label for="from date">From Date</label>
                        </div>
                        <div class="col-lg-3">
                            <div class="input-group date">
                                <input class="form-control datepickerclass" name="periodFromReporting" id="periodFromReporting" readonly="true"/> 
                            </div> 
                        </div>
                        <div class="col-lg-1">
                            <label for="To Date">To Date</label>
                        </div>
                        <div class="col-lg-3">
                            <div class="input-group date">
                                <input class="form-control datepickerclass"  name="periodToReporting" id="periodToReporting" readonly="true"/> 
                            </div>  
                        </div>
                    </div>

                </div>
                <div class="modal-footer">                                                         
                    <button type="button"  onclick="savePeriodFromToReporting(this, '${groupCEmployee.assessmentTypeReporting}', '${groupCEmployee.periodFromReporting}', '${groupCEmployee.periodToReporting}', '${groupCEmployee.promotionId}')" class="btn btn-default">Save</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>


</html>
