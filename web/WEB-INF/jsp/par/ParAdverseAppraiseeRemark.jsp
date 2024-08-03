<%-- 
    Document   : ParAdverseRemarkAppraisee
    Created on : 4 Aug, 2020, 10:55:39 AM
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
            <c:if test="${parBriefDetail.adverseCommunicationStatusId eq 0 or parBriefDetail.adverseCommunicationStatusId eq 102 or parBriefDetail.adverseCommunicationStatusId eq 113}">
                <form:form action="parPDFAdverseAppraiseCommunication.htm" method="POST" commandName="parAdverseCommunicationDetail" class="form-horizontal" enctype="multipart/form-data">
                    <div class="panel panel-default">
                        <div class="panel-heading">Par Adverse Remark</div>
                        <div class="panel-body" style="height: 200px;overflow: auto;">
                            <form:hidden path="adverseRemarkstype"/>
                            <form:hidden path="toempId"/>
                            <form:hidden path="tospc"/>
                            <form:hidden path="adverseCommunicationStatusId"/>
                            <div class="form-group">

                                <c:if test="${parBriefDetail.adverseCommunicationStatusId ne 102}">
                                    <label class="control-label col-sm-2">Adverse Communication With Appraise:</label>
                                    <div class="col-sm-6">
                                        ${parAdverseCommunicationDetail.toempName},${parAdverseCommunicationDetail.toPost} 
                                    </div>
                                </c:if>

                                <c:if test="${parBriefDetail.adverseCommunicationStatusId eq 102}">
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
                            <input type="submit" name="action" class="btn btn-default" value="Submit" onclick="return confirm('Are you sure to Submit?')"/> 
                            <input type="submit" name="action" class="btn btn-default" value="Back"/>
                        </div>
                    </div>
                </form:form>  
            </c:if>
            <div class="table-responsive">
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
                            <th width="15%">Attachment</th>
                        </tr>
                    </thead>
                    <tbody>                                        
                        <c:forEach items="${custodianadverseremarkList}" var="adversecommunication" varStatus="count">
                            <tr>                                    
                                <td>${count.index + 1}</td>
                                <td>${adversecommunication.fromempName},(${adversecommunication.fromAuthType})</td>
                                <td>${adversecommunication.fromPost}</td>
                                <td>${adversecommunication.toempName},(${adversecommunication.toAuthType})</td>
                                <td>${adversecommunication.toPost}</td>
                                <td>${adversecommunication.adverseCommunicationOnDate}
                                <td>${adversecommunication.remarksdetail}</td>
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


