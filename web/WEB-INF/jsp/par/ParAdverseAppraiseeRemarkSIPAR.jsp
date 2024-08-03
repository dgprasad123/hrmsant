<%-- 
    Document   : ParAdverseAppraiseeRemarkSIPAR
    Created on : 23 Aug, 2023, 11:35:08 AM
    Author     : Manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }
            .tblTrColor{
                background: rgb(174,238,209);
                background: radial-gradient(circle, rgba(174,238,209,0.9976191160057774) 0%, rgba(148,231,233,1) 100%);
                color: #000000;
                font-weight: bold;
            }
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .pageHeadingTxt{
                background-color: #DDEDE0;
                font-weight: bold;
                text-transform: uppercase;
                text-align: center;
                color:#000;
                font-size:18px;
                line-height: 20px;
                position: relative;
                letter-spacing: 0.2px;
                padding: 10px 0px;
            }
            .pageSubHeading{
                background-color: #DDEDE0;
                font-weight: bold;
                text-transform: uppercase;
                text-align: center;
                color:#0D8CE7;
                font-size:17px;
                line-height: 20px;
                position: relative;
                letter-spacing: 0.2px;
                padding: 10px 0px;
            }
            .myModalBody{}
            .new_sty{width:33%;}
            .new_sty li{width: 98%;}
            .new_sty li a{width: 100%;}
        </style>
        <script type="text/javascript">
            var allowedExtensions = /(\.pdf|\.jpeg|\.jpg)$/i;
            function uploadValidation(me) {
                if ($(me).val() != '') {
                    var fileId = $(me).attr("id");
                    var fi = document.getElementById(fileId);
                    var fsize = fi.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 4096) {
                        alert("File too Big, please select a file less than 4mb");
                        $("#" + fileId).val('');
                        return false;
                    } else {
                        if (!allowedExtensions.exec($("#" + fileId).val())) {
                            alert('Please upload file having extensions .jpeg/.jpg/.pdf only.');
                            $("#" + fileId).val('');
                            return false;
                        } else {
                            $("#clearbtn").show();
                            return true;
                        }

                    }
                }
            }
        </script>

    </head>
    <body>
        <div class="container-fluid">
            <c:if test="${parBriefDetail.adverseCommunicationStatusId eq 0 or parBriefDetail.adverseCommunicationStatusId eq 122 or parBriefDetail.adverseCommunicationStatusId eq 124}">
                <form:form action="parPDFAdverseAppraiseCommunicationSiPAR.htm" method="POST" commandName="parAdverseCommunicationDetail" class="form-horizontal" enctype="multipart/form-data">
                    <div class="panel panel-default">
                        <div class="panel-heading tblTrColor" >Par Adverse Remark</div>
                        <div class="panel-body" style="height: 200px;overflow: auto;">
                            <form:hidden path="adverseRemarkstype"/>
                            <form:hidden path="toempId"/>
                            <form:hidden path="tospc"/>
                            <form:hidden path="adverseCommunicationStatusId"/>
                            <div class="form-group">

                                <c:if test="${parBriefDetail.adverseCommunicationStatusId ne 122}">
                                    <label class="control-label col-sm-2">Adverse Communication With Appraise:</label>
                                    <div class="col-sm-6">
                                        ${parAdverseCommunicationDetail.toempName},${parAdverseCommunicationDetail.toPost} 
                                    </div>
                                </c:if>
                                <c:if test="${parBriefDetail.adverseCommunicationStatusId eq 122}">
                                    <label class="control-label col-sm-2">Authority:</label>
                                    <div class="col-sm-6">
                                        <form:select path="authoritytype"  class="form-control">
                                            <c:forEach items="${reporting}" var="reportingvar">
                                                <option value="REPORTING-${reportingvar.authid}">${reportingvar.authorityname},${reportingvar.authorityspn} (Reporting Authority)</option>
                                            </c:forEach> 
                                            <c:forEach items="${reviewing}" var="reviewingvar">
                                                <option value="REVIEWING-${reviewingvar.authid}">${reviewingvar.authorityname},${reviewingvar.authorityspn} (Reviewing Authority)</option>
                                            </c:forEach>  
                                            <c:forEach items="${accepting}" var="acceptingingvar">
                                                <option value="ACCEPTING-${acceptingingvar.authid}">${acceptingingvar.authorityname},${acceptingingvar.authorityspn} (Accepting Authority)</option>
                                            </c:forEach>        
                                        </form:select>
                                    </div>

                                </c:if>
                            </div>
                            <div class="form-group">
                                <c:if test="${parBriefDetail.adverseCommunicationStatusId ne 102}">
                                    <label class="control-label col-sm-2" >Adverse Remarks</label>
                                    <div class="col-sm-6"> 
                                        <form:textarea class="form-control" path="remarksdetail"/>             
                                    </div>
                                </c:if>
                                <c:if test="${parBriefDetail.adverseCommunicationStatusId eq 102}">
                                    <label class="control-label col-sm-2" >Substantiation Remarks</label>
                                    <div class="col-sm-6"> 
                                        <form:textarea class="form-control" path="remarksdetail"/>             
                                    </div>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-2">Document</label>
                                <div class="col-sm-2"> 
                                    <input type="file" name="uploadDocument" id="uploadDocument"  class="form-control-file" onchange="return uploadValidation(this)" />
                                    <span style="color: red;">(Only .pdf and .jpg files are allowed)</span>
                                </div>
                                ${adversecommunication.originalFilename}
                            </div>
                        </div>
                        <div class="panel-footer">
                            <form:hidden path="parId"/>
                            <form:hidden path="taskId"/>
                            <input type="submit" name="action" class="btn btn-success" value="Submit" onclick="return confirm('Are you sure to Submit?')"/> 
                            <c:if test="${parAdverseCommunicationDetail.detailIdForCustodianEntry eq 0}">
                                <input type="submit" name="action" class="btn btn-success" value="Enter Adverse Remarks Detail">
                            </c:if>
                            <!-- <input type="submit" name="action" class="btn btn-default" value="Back"/> -->
                        </div>
                    </div>
                </form:form>  
            </c:if>

            <div class="tblTrColor">
                <table class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>                                
                            <th width="3%">#</th>
                            <th width="12%">Custodian Employee Id<br>Custodian Name</th>
                            <th width="15%">Custodian Designation</th>
                            <th width="15%">Financial Year</th>
                            <th width="25%">Adverse Remarks Detail</th>
                            <th width="10%">Final Grading</th>
                            <th width="8%">Communication <br/>On Date</th>
                            <th width="8%" style="text-align: center">Action</th> 
                            <th width="8%" align="center">Download</th> 
                        </tr>
                    </thead>
                    <tbody>                                        
                        <c:forEach items="${custodianEntryList}" var="communicationEntry" varStatus="count">
                            <tr>                                    
                                <td>${count.index + 1}</td>
                                <td> <b style="color:#0000FF">${communicationEntry.custodianEmpId}</b> </br> <b style="color:#FF4500">${communicationEntry.custodianFullName}</b></td>
                                <td><b style="color:#0000FF">${communicationEntry.custodianDesignation}</b></td>
                                <td><b style="color:#0000FF">${communicationEntry.fiscalYear}</b></td>
                                <td><b style="color:#0000FF">${communicationEntry.remarksdetail}</b></td>
                                <td><b style="color:#0000FF">${communicationEntry.finalGrading}</b></td>
                                <td><b style="color:#0000FF">${communicationEntry.adverseCommunicationOnDate}</b></td>
                                <td align="center">
                                    <a href="editAdverseRemarksDetailEntryForAppraise.htm?encParId=${communicationEntry.encParId}" class="btn btn-warning" >Edit </a>
                                </td>
                                <td align="center">
                                    <c:if test="${count.index+1 eq 1}">
                                        <a href="siParAdvRemarkIntimation.htm?encParId=${communicationEntry.encParId}" target="_blank">
                                            <img style="" src="images/pdf_icon.png" height="25"/> 
                                        </a> 
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="tblTrColor">
                <table class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>                                
                            <th width="3%">#</th>
                            <th width="15%">From</th>
                            <th width="15%">From Designation</th>
                            <th width="15%">To</th>
                            <th width="15%">To Designation</th>
                            <th width="10%">Representation On Date</th>
                            <th width="15%">Representation</th>
                            <th width="8%">Attachment</th>
                            <!-- <th width="8%">Download</th> -->
                        </tr>
                    </thead>
                    <tbody>                                        
                        <c:forEach items="${custodianadverseremarkList}" var="adversecommunication" varStatus="count">
                            <tr>                                    
                                <td>${count.index + 1}</td>
                                <td><b style="color:#0000FF">${adversecommunication.fromempName}</b></br><b style="color:#FF4500">${adversecommunication.fromAuthType}</b></td>
                                <td><b style="color:#0000FF">${adversecommunication.fromPost}</b></td>
                                <td><b style="color:#0000FF">${adversecommunication.toempName}</b></br> <b style="color:#FF4500">${adversecommunication.toAuthType}</b></td>
                                <td><b style="color:#0000FF">${adversecommunication.toPost}</b></td>
                                <td><b style="color:#0000FF">${adversecommunication.adverseCommunicationOnDate}</b></td>
                                <td><b style="color:#0000FF">${adversecommunication.remarksdetail}</b></td>
                                <td>
                                    <c:if test="${not empty adversecommunication.originalFilename}">
                                        <a href="downloadAttachmentforAdversePAR.htm?parId=${parAdverseCommunicationDetail.parId}&communicationId=${adversecommunication.communicationId}" class="btn btn-default" >
                                            <span class="glyphicon glyphicon-paperclip"></span> ${adversecommunication.originalFilename}
                                        </a> 
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

        </div>
    </body>
</html>


