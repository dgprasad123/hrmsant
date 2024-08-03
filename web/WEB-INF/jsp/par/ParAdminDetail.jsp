<%-- 
    Document   : ParAdminDetail
    Created on : Jun 28, 2019, 3:33:50 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
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
        <script type = "text/javascript">
            function confirmUnreviewed(parId) {
                if (confirm("Are you sure to Un Review this PAR?")) {
                    $("#parId").val(parId);
                    $('#unreviewed').modal('show');
                } else {
                    return false;
                }
            }

            function viewrevertPAR(parId) {
                alert(parId);

                var dataURL = "par/RevertReason.htm?parid=" + parId;
                $('#revertPAR').modal('show');
                $('#revertPAR .modal-body').load(dataURL, function() {

                });

            }
            function saveUnreviewedPARDetail() {
                var tunReviewedReason = $("#unReviewedReason").val();
                var tparId = $("#parId").val();
                if (tunReviewedReason) {
                    $.post("saveUnReviewedPARdetail.htm", {unReviewedReason: tunReviewedReason, parId: tparId})
                            .done(function(data) {
                                alert("UnReviewed Sucessfully");
                                $('#unreviewed').modal('hide');
                                $('#myModal').modal('hide');

                            })
                } else if (!tunReviewedReason) {
                    alert("Please Enter UnReviewed Reason");
                }
            }

            function confirmRevert() {
                var radioValue = $("input[name='rdparid']:checked").val();
                alert(radioValue);
                if (radioValue != "") {
                    var idArr = radioValue.split("-");
                    tParid = idArr[0];
                    tTaskId = idArr[1];
                    if (confirm("Are you sure to Revert this PAR?")) {
                        $("#parid").val(tParid);
                        $("#taskid").val(tTaskId);
                        $('#revert').modal('show');
                    } else {
                        return false;
                    }
                } else {
                    alert("Select Par");
                }
            }
            function confirmDelete() {
                var radioValue = $("input[name='rdparid']:checked").val();
                alert(radioValue);
                if (radioValue) {
                    if (confirm("Are you sure to Delete this PAR?")) {
                        //$("#parId").val(parId);
                        $('#parDelete').modal('show');
                    } else {
                        return false;
                    }
                } else {
                    ("Please Choose the PAR You Want to Delete");
                }
            }

            function saveDeletePARDetail() {
                var radioValue = $("input[name='rdparid']:checked").val();
                var tdeletedReason = $("#deletedReason").val();
                var idArr = radioValue.split("-");
                tParid = idArr[0];
                tTaskId = idArr[1];
                if (tdeletedReason) {
                    $.post("saveDeletePARdetail.htm", {deletedReason: tdeletedReason, parId: tParid})
                            .done(function(data) {
                                alert("PAR is Deleted Sucessfully");
                                $('#parDelete').modal('hide');
                                $('#myModal').modal('hide');

                            })
                } else if (!tdeletedReason) {
                    alert("Please Enter Delete Reason");
                }
            }

            function saveRevertPARDetail() {
                var trevertremarks = $("#revertremarks").val();
                var tparid = $("#parid").val();
                var ttaskid = $("#taskid").val();
                if (trevertremarks) {
                    $.post("saveRevertPARdetailbyAdmin.htm", {revertremarks: trevertremarks, parid: tparid, taskid: ttaskid})
                            .done(function(data) {
                                alert("Revert Sucessfully");
                                $('#revert').modal('hide');
                                $('#myModal').modal('hide');

                            })
                } else if (!trevertremarks) {
                    alert("Please Enter Revert Reason");
                }
            }

            function forceforward() {
                var radioValue = $("input[name='rdparid']:checked").val();
                if (radioValue) {
                    if (confirm("Are you sure to Force Forward this PAR?")) {
                        var idArr = radioValue.split("-");
                        tParid = idArr[0];
                        tTaskId = idArr[1];
                        $.post("forceforward.htm", {parId: tParid, taskId: tTaskId})
                                .done(function(data) {
                                    if (data.msg == "Y") {
                                        alert("Force Forwarded Sucessfully");
                                    } else {
                                        alert("Some error occured");
                                    }
                                });
                    }
                } else {
                    alert("Select Par");
                }
            }
            function toggleButtons(me) {
                if ($(me).is(":checked")) {
                    if ($(me).attr("parstatusid") == 17) {
                        $("#buttonarea").hide();
                    } else {
                        $("#buttonarea").show();
                    }
                }
            }
        </script>
        <style type="text/css">
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
            }


        </style>
    </head>
    <body style="background-color:#FFFFFF">
        <form:form action="viewPARAdmindetail.htm" method="POST" commandName="ParAdminProperties" class="form-inline">      
            <div class="panel-body table-responsive">
                <table class="table table-bordered" >
                    <thead>
                        <tr>
                            <th width="5%">Sl No</th>
                            <th width="10%">Par Id</th>
                            <th width="10%">Period</th>
                            <th width="10%">Designation During the period of Report</th>
                            <th width="10%" style="text-align: center">Status</th>
                            <th width="5%" colspan="2" style="text-align: center">Action</th>
                            <th width="5%">Attachment</th>
                        </tr>
                    </thead>
                    <tbody>                                        
                        <c:forEach items="${pardetail}" var="pardetails" varStatus="cnt">
                            <tr>
                                <td><input type="radio" onclick="toggleButtons(this)" name="rdparid" parstatusid="${pardetails.parstatusid}" isreview="${pardetails.isreview}" value="${pardetails.parId}-${pardetails.taskId}"/> </td>
                                <td>${pardetails.parId}</td>
                                <td>${pardetails.prdFrmDate} to ${pardetails.prdToDate}</td>
                                <td>${pardetails.postName}</td>
                                <td align="center"><span style="position:relative;">
                                        ${pardetails.parstatus}
                                        <c:if test="${not empty pardetails.initiatedByEmpId}">
                                            <span>(Initiated By Authority)</span>
                                        </c:if>
                                        <c:if test="${pardetails.isreview  eq 'Y' && pardetails.parstatusid  ne '17'}">
                                            <img src="images/reviewed.png" style="position:absolute;left:-35px;top:-15px;" height="70">
                                        </c:if> 
                                        <c:if test="${pardetails.parstatusid  eq '17' && pardetails.isreview  eq 'Y'}">
                                            <img src="images/accepted.png" style="position:absolute;left:0px;top:-15px;" height="70">
                                        </c:if> 
                                        <c:if test="${pardetails.isadversed  eq 'Y' && pardetails.parstatusid  ne '17'}">
                                            <img src="images/adversed_1.png" style="position:absolute;left:0px;top:-15px;" height="70">
                                        </c:if> 

                                    </span>                
                                </td>
                                <td>
                                    &nbsp;
                                    <c:if test="${pardetails.parstatusid  eq '17'}">
                                        <%--<a target="_blank" href="getNRCdetail.htm?empName=${pardetails.empName}&prdFrmDate=${pardetails.prdFrmDate}&prdToDate=${pardetails.prdToDate}&parId=${pardetails.parId}">view</a> --%>
                                        <a target="_blank" href="getNRCdetail.htm?parId=${pardetails.parId}">view</a>
                                    </c:if> 


                                    <%--   <c:if test="${(pardetails.postGroupType eq 'A' && pardetails.isreview eq 'Y') or pardetails.postGroupType eq 'B' && (pardetails.parstatus ne 17)}">--%>
                                    <c:if test="${pardetails.parstatusid  ne '17' && pardetails.isreview  ne 'Y'}">
                                        <a target="_blank" href="getviewPARAdmindetail.htm?parId=${pardetails.parId}&taskId=${pardetails.taskId}&fiscalyear=${pardetails.fiscalyear}">view</a>
                                    </c:if>
                                   
                                    <c:if test="${pardetails.parstatusid  ne '17' && pardetails.isreview  eq 'Y'}">
                                        <%--<a target="_blank" href="viewReviewedPARPdf.htm?parId=${pardetails.parId}">view</a> --%>
                                        <a target="_blank" href="viewReviewedPARPdf.htm?empName=${pardetails.empName}&prdFrmDate=${pardetails.prdFrmDate}&prdToDate=${pardetails.prdToDate}&parId=${pardetails.parId}">view</a>
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${pardetails.isreview  eq 'Y' && usertype eq 'A'}">
                                        <a href='javascript:void(0)' onclick="return confirmUnreviewed('${pardetails.parId}')"> <img src='./images/revert.png' width='25' height='25' alt='Un Reviewed' title='Un Reviewed'/></a>
                                        </c:if> 
                                        ${pardetails.parstatus}
                                        <c:if test="${pardetails.parstatusid  eq '16' || pardetails.parstatusid  eq '18' || pardetails.parstatusid  eq '19'}">
                                        <a href='javascript:void(0)' onclick="return viewrevertPAR('${pardetails.parId}')"> <img src='./images/revert.png' width='25' height='25' alt='Reverted PAR' title='Revert Reason'/><span>(Revert Reason)</span></a>
                                        </c:if>

                                    <c:if test="${pardetails.parstatusid  ne '17' && pardetails.isadversed  eq 'Y'}">
                                        ${pardetails.hasSendAppraiseeAdverse}
                                        <a target="_blank" href="parPDFAdverseAppraiseCommunication.htm?parId=${pardetails.parId}">communication</a>
                                        <c:if test="${pardetails.adverseCommunicationStatusId eq 101}">
                                            (Adverse Remark Communicated)
                                        </c:if>
                                        <c:if test="${pardetails.adverseCommunicationStatusId eq 102}">
                                            (Representation Received)
                                        </c:if>
                                        <c:if test="${pardetails.adverseCommunicationStatusId eq 103}">
                                            (Substantiation Called For)
                                        </c:if>
                                        <c:if test="${pardetails.adverseCommunicationStatusId eq 113}">
                                            (Substantiation reply Received)
                                        </c:if>

                                    </c:if>
                                </td>

                                <td>
                                    &nbsp;
                                    <c:if test="${pardetails.parstatusid  eq '17' && pardetails.isNRCAttchPresent  eq 'Y'}">
                                        <a target="_blank" href="DownloadparNRCAttachment.htm?parId=${pardetails.parId}">view Attachment</a>
                                    </c:if>
                                    <c:if test="${pardetails.parstatusid  eq '116'}">
                                        <a target="_blank" href="downloadAttachmentOfPreviousYearPAR.htm?parId=${pardetails.parId}">view Attachment</a>
                                    </c:if>
                                    <c:if test="${pardetails.parstatusid  eq '120'}">
                                        <a target="_blank" href="downloadAttachmentOfPreviousYearPAR.htm?parId=${pardetails.parId}">view Attachment</a>
                                    </c:if>
                                </td>

                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <div class="panel-footer">
                    <c:if test="${usertype eq 'A'}">
                        <span id="buttonarea">
                            <input type="button" name="action" value="Revert" class="btn btn-default" onclick="confirmRevert()"/>
                           <%-- <input type="button" name="action" value="Force Forward" class="btn btn-default" onclick="forceforward()"/>
                            <input type="button" name="action" value="Delete" class="btn btn-default" onclick="confirmDelete()"/> --%>
                        </span>
                    </c:if>
                </div>
            </div>
        </form:form>
        <div id="unreviewed" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">

                    <input type="hidden" id="parId"/>
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Reason For Un Reviewed</h4>
                    </div>
                    <div class="modal-body">
                        <label class="control-label col-sm-2" >Remarks</label>
                        <div class="col-sm-8"> 
                            <textarea class="form-control" id="unReviewedReason" name="unReviewedReason"></textarea>   
                        </div>
                    </div>
                    <div class="modal-footer">
                        <a href="javascript:saveUnreviewedPARDetail()" class="btn btn-primary">Save</a> 
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    </div>

                </div>
            </div>
        </div>


        <div id="parDelete" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">

                    <input type="hidden" id="parId"/>
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Reason For Delete PAR</h4>
                    </div>
                    <div class="modal-body">
                        <label class="control-label col-sm-2" >Remarks</label>
                        <div class="col-sm-8"> 
                            <textarea class="form-control" id="deletedReason" name="deletedReason"></textarea>   
                        </div>
                    </div>
                    <div class="modal-footer">
                        <a href="javascript:saveDeletePARDetail()" class="btn btn-primary">Save</a> 
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    </div>

                </div>
            </div>
        </div>

        <div id="revertPAR" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">

                    <input type="hidden" id="parId"/>
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Reason For Revert</h4>
                    </div>
                    <div class="modal-body">

                    </div>
                </div>
            </div>
        </div>

        <div id="revert" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">

                    <input type="hidden" id="parid"/>
                    <input type="hidden" id="taskid"/>
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Your PAR is reverted by Higher Authority</h4>
                    </div>
                    <div class="modal-body">
                        <label class="control-label col-sm-2" >Remarks</label>
                        <div class="col-sm-8"> 
                            <textarea class="form-control" id="revertremarks" name="revertremarks" rows="4" cols="80"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <a href="javascript:saveRevertPARDetail()" class="btn btn-primary">Save</a>  
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    </div>

                </div>
            </div>
        </div> 
    </body>
</body>
</html>
