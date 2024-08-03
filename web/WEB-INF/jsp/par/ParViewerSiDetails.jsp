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
            .tblTrColor{
                background: rgb(174,238,209);
                background: radial-gradient(circle, rgba(174,238,209,0.9976191160057774) 0%, rgba(148,231,233,1) 100%);
                color: #000000;
                font-weight: bold;
            }
        </style>
        
    </head>
    <body style="background-color:#FFFFFF">
        <form:form action="viewSiParDetail.htm" method="POST" commandName="ParAdminProperties" class="form-inline">      
            <div class="panel-body table-responsive">
                
                <div width="100%"> 
                    <div align="center" style="margin-bottom: 5px;">
                        <span id="buttonarea">
                            <input type="hidden" name="fiscalyear" id="fiscalyear" value="${Fyear}"/>
                            <input type="submit" class="btn btn-primary btn-md" id="searchbtn" name="Back" value="Back"/>
                        </span>
                    </div>
                </div>
                
                <table class="table table-bordered table-hover table-striped">
                    <thead class="tblTrColor">
                        <tr>
                            <th width="3%">Sl No</th>
                            <th width="10%">Employee Name, </br> Par Id</th>
                            <th width="10%">Period</th>
                            <th width="10%">Designation During the period of Report</th>
                            
                            <th width="20%">Status, <br>Pending At Authority with Designation</th> 
                            <th width="10%">Pending At Authority From</th>
                            <th width="5%" style="text-align: center">Action</th>
                            <th width="5%">Attachment</th>
                        </tr>
                    </thead>
                    
                    <tbody>
                        <c:if test="${not empty pardetail}">
                            <c:forEach items="${pardetail}" var="pardetails" varStatus="cnt">
                                <tr>
                                    <!-- <td><input type="radio" onclick="toggleButtons(this)" name="rdparid" parstatusid="${pardetails.parstatusid}" isreview="${pardetails.isreview}" value="${pardetails.parId}-${pardetails.taskId}"/> </td> -->
                                    <td>${cnt.index + 1}</td>

                                    <td> <b style="color:#0000FF">${pardetails.empName}</b> </br> <b style="color:#FF4500">${pardetails.parId}</b> </td>
                                    <td> ${pardetails.prdFrmDate} to ${pardetails.prdToDate} </td>
                                    <td> ${pardetails.postName} </td>

                                    <td> <b style="color:#FF4500">${pardetails.parstatus}</b>
                                        <c:if test="${pardetails.pendingAtAuthName ne ''}">
                                            , </br><b style="color:#0000FF">${pardetails.pendingAtAuthName}, </br> ${pardetails.pendingAtSpc}</b>
                                        </c:if>
                                        <c:if test="${pardetails.parstatus eq 'REQUESTED FOR NRC'}">
                                            , </br><b style="color:#0000FF">${pardetails.nrcDetails}</b>
                                        </c:if>     
                                    </td>
                                    <td> ${pardetails.parPendingDateFrom} </td>
                                    
                                    <td align="center">
                                        <c:if test="${PrivType eq 'Y' && pardetails.parstatus ne 'REQUESTED FOR NRC'}">
                                            <a target="_blank" href="viewTotalSiParDetail.htm?parId=${pardetails.encParId}&taskId=${pardetails.encTaskId}"> View PAR </a> 
                                        </c:if>
                                    </td>
                                    
                                    <td>
                                        <c:if test="${pardetails.parstatusid  eq '17' && pardetails.isNRCAttchPresent eq 'Y'}">
                                            <a target="_blank" href="DownloadparNRCAttachment.htm?parId=${pardetails.parId}">view Attachment</a>
                                        </c:if>
                                        <c:if test="${pardetails.parstatusid  eq '116'}">
                                            <a target="_blank" href="downloadAttachmentOfPreviousYearPAR.htm?parId=${pardetails.parId}">view Attachment</a>
                                        </c:if>
                                    </td>

                                </tr>
                            </c:forEach>
                        </c:if>
                    </tbody>
                </table>

                <div class="panel-footer">
                    <c:if test="${usertype eq 'A'}">
                        <span id="buttonarea">
                            <input type="button" name="action" value="Force Forward" class="btn btn-default" onclick="forceforward()"/>
                            <input type="button" name="action" value="Revert" class="btn btn-default" onclick="confirmRevert()"/>
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
