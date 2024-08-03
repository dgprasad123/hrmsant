<%-- 
    Document   : Increment Proposal List
    Created on : 20 Jun, 2016, 12:14:12 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Quarter Detail List</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/color.css">
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <link href="css/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
        <link  rel="stylesheet" type="text/css"  href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> 


        <script type="text/javascript">
            function popupSplCase(me) {
                var splcase = confirm("Are you sure of submitting this request?");
                if (splcase) {
                    currentRow = me;
                    var dataURL = $(me).attr('data-href');
                    $("#quarterModal").modal('show');
                    $('.modal-body').load(dataURL, function() {
                        $('#quarterModal').modal({show: true});
                    });
                }
            }

            function popupmessageInbox(me) {
                currentRow = me;
                var dataURL = $(me).attr('data-href');
                $("#messageModal").modal('show');
                $('.modal-body').load(dataURL, function() {
                    $('#messageModal').modal({show: true});
                });
            }


          
            function requestsubmitdocument(me, appraiseeId, cno) {
                var cons = confirm("Do you want to submit the document?");
                if (cons) {
                    currentRow = me;
                    var dataURL = $(me).attr('data-href');
                    $("#quarterModal").modal('show');
                    $('.modal-body').load(dataURL, function() {
                        $('#quarterModal').modal({show: true});
                    });
                }
            }

            function showcauseReply(me) {
                var cons = confirm("Do you want to upload show cause reply?");
                if (cons) {
                    currentRow = me;
                    var dataURL = $(me).attr('data-href');
                    $("#quarterModal").modal('show');
                    $('.modal-body').load(dataURL, function() {
                        $('#quarterModal').modal({show: true});
                    });

                }
            }
            function appealUpload(me) {
                var cons = confirm("Do you want to upload Appeal Document?");
                if (cons) {
                    currentRow = me;
                    var dataURL = $(me).attr('data-href');
                    $("#quarterModal").modal('show');
                    $('.modal-body').load(dataURL, function() {
                        $('#quarterModal').modal({show: true});
                    });

                }
            }

            $(document).ready(function() {
                $('#tvd').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });

            });
            function validateElectricity() {
                var fup = document.getElementById('uploadDocumentphd');
                if ($("#uploadDocumentphd").val() == "") {
                    alert("Please upload the Electricity Bill for NOC Processing");
                    return false;
                }
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                var ext = ext.toLowerCase();
                if ((ext != "pdf" && ext != "zip")) {
                    alert("Upload pdf/zip files only");
                    fup.focus();
                    return false;
                }
                var fi = document.getElementById("uploadDocumentphd");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 2048) {
                    alert("File too Big, please select a file less than 2mb");
                    $("#uploadDocumentphd").val('');
                    return false;
                }
            }
        </script>



    </head>

    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <strong style="font-size:15pt;">Occupant Quarter Details</strong>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr bgcolor="#FAFAFA">
                                <th>Consumer No</th>
                                <th>QTRS NO</th>
                                <th>QTRS Type</th>
                                <th>Unit/Area</th>
                                <th>Address</th> 
                                <th>QTRS  Action/Status</th> 
                               
                                <th>&nbsp;</th> 




                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${quarterList}" var="quarter" varStatus="cnt">
                                <tr>

                                    <td>${quarter.consumerNo}</td>
                                    <td>${quarter.quarterNo}</td>
                                    <td>${quarter.qrtrtype}</td>
                                    <td>${quarter.qrtrunit}</td>
                                    <td>${quarter.address}</td>  

                                    <td>

                                        <c:if test = "${not empty quarter.retentionStatus && quarter.retentionStatus eq 'Y' }">
                                            <a href="GeneratePDFRetention.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${quarter.occupationTypes}&empId=${quarter.empId}" class="btn btn-info">
                                                Download Retention Permission<br/></a>
                                            </c:if> 

                                        <c:if test = "${not empty quarter.transferId &&  not empty quarter.retentionStatus }">
                                            <a href="javascript:void(0);" data-href="UploadSplQrtCase.htm?trackingId=${quarter.trackingId}&consumerNo=${quarter.consumerNo}&empId=${quarter.empId}" onclick="popupSplCase(this)"  data-target="#quarterModal" class='btn btn-warning' >Request for Extension of retention Period</a>
                                        </c:if>

                                        <c:if test = "${not empty quarter.extensionFromDate && (quarter.splCaseStatus eq '0' || empty quarter.splCaseStatus ) }" >
                                            <a href="downloadExtensionRequest.htm?consumerNo=${quarter.consumerNo}&empId=${quarter.empId}"  class="btn btn-success">Download Extended Retention Request</a>
                                        </c:if>
                                        <c:if test = "${not empty quarter.originalFilename   && (quarter.splCaseStatus eq '0' || empty quarter.splCaseStatus ) }">
                                            <a href="downloadSplQrtCase.htm?trackingId=${quarter.trackingId}&consumerNo=${quarter.consumerNo}&empId=${quarter.empId}"  class="btn btn-warning">Attachment</a>
                                        </c:if>     

                                        <c:if test = "${quarter.splCaseStatus eq '2'  }">
                                            <h3 class="text-danger">&nbsp;Extension Rejected</h3>
                                        </c:if> 

                                        <c:if test = "${quarter.splCaseStatus eq '1'}">
                                            <!-- <h5 class="text-success">&nbsp;Extension Allowed</h5>-->
                                            <c:if test = "${not empty quarter.orderNumber}">
                                                <a href="GeneratePDFExtendedRetention.htm?consumerNo=${quarter.consumerNo}&empId=${quarter.empId}" class="btn btn-warning">
                                                    Download Extended Retention Permission<br/></a>
                                                </c:if> 
                                            </c:if> 


                                        <c:if test = "${ quarter.vacateStatus eq 'No'   }">
                                            <a href="GeneratePDFOPP.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-primary">Download OPP Requisition</a>

                                        </c:if>   
                                        <c:if test = "${ quarter.vacateStatus eq 'Yes'   }">
                                            <a href="GeneratePDFIntimation.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-primary">Download Intimation Letter</a>
                                        </c:if>  
                                        <c:if test = "${  not empty quarter.oppCaseId &&    quarter.oppCaseId ne 0   }">
                                            <a href="downloadNoticeOPP.htm?oppCaseId=${quarter.oppCaseId}"  class="btn btn-danger">Download Show Cause Notice(4(1))</a>

                                        </c:if>  

                                        <c:if test = "${  empty  quarter.showCauseReply && not empty quarter.oppCaseId &&  quarter.oppCaseId ne 0    }">                                                                              
                                            <a href="javascript:void(0);" data-href="uploadshowcauseReply.htm?oppCaseId=${quarter.oppCaseId}&consumerNo=${quarter.consumerNo}&empId=${quarter.empId}" onclick="showcauseReply(this)"  data-target="#quarterModal" class='btn btn-warning' >Reply Show Cause</a>
                                        </c:if>
                                        <c:if test = "${ not  empty  quarter.showCauseReply   }">                                                                              
                                            <a href="downloadsubmissionDocument.htm?consumerNo=${quarter.consumerNo}&empId=${quarter.empId}&oppCaseId=${quarter.oppCaseId}&dsStatus=S" class="btn btn-default" >
                                                <span class="glyphicon glyphicon-paperclip"></span>  Download Show Cause Reply
                                            </a> 

                                        </c:if>    

                                        <c:if test = "${  not empty quarter.evictionNotice  &&  quarter.vacateStatus eq 'No' }">
                                            <a href="GeneratePDFEvictionNotice.htm?consumerNo=${quarter.consumerNo}&occupationTypes=${occupationTypes}&empId=${quarter.empId}"  class="btn btn-danger">Download Eviction Order(5(2))</a>
                                           

                                        </c:if>
                                        <c:if test = "${not empty quarter.orderFiveOne  && quarter.orderFiveOne ne 0 }">
                                            <a href="DownloadFiveOneOrder.htm?orderFiveOne=${quarter.orderFiveOne}" class="btn btn-info">
                                                Download Order 5(1)</a>
                                            </c:if> 


                                        <c:if test = "${not empty quarter.noticeFiveOne && quarter.noticeFiveOne ne 0 }">
                                            <a href="DownloadFiveOneNotice.htm?orderFiveOne=${quarter.noticeFiveOne}" class="btn btn-success">
                                                Download Notice 5(1)</a>

                                            <c:if test = "${  empty  quarter.estateAppeal     }">                                                                              
                                                <a href="javascript:void(0);" data-href="uploadAppeal.htm?oppCaseId=${quarter.oppCaseId}&consumerNo=${quarter.consumerNo}&empId=${quarter.empId}" onclick="appealUpload(this)"  data-target="#quarterModal" class='btn btn-warning' >Upload Appeal Document</a>
                                            </c:if>
                                            <c:if test = "${ not  empty  quarter.estateAppeal   }">                                                                              
                                                <a href="downloadsubmissionDocument.htm?consumerNo=${quarter.consumerNo}&empId=${quarter.empId}&oppCaseId=${quarter.oppCaseId}&dsStatus=A" class="btn btn-default" >
                                                    <span class="glyphicon glyphicon-paperclip"></span>  Download Appeal Document
                                                </a> 

                                            </c:if>  
                                        </c:if> 
                                        <c:if test = "${not empty quarter.appealNotice && quarter.appealNotice ne 0 }">
                                             <a href="DownloadAppealNotice.htm?orderFiveOne=${quarter.appealNotice}" class="btn btn-info">
                                                Download Appeal Notice</a>
                                            </c:if>                                       



                                    </td>    
                                 
                                </tr>

                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- MODAL WINDOW FOR Upload permission--> 

                <div id="quarterModal" class="modal fade" role="dialog">
                    <div class="modal-dialog" style="width:1000px;">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">&nbsp;</h4>
                            </div>
                            <div class="modal-body">

                            </div>
                            <div class="modal-footer">                       
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="messageModal" class="modal fade" role="dialog">
                    <div class="modal-dialog" style="width:1000px;">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">&nbsp;</h4>
                            </div>
                            <div class="modal-body">

                            </div>
                            <div class="modal-footer">                       
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>



