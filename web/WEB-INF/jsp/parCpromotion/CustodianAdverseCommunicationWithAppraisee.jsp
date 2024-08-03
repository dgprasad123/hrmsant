<%-- 
    Document   : CustodianAdverseCommunicationWithAppraisee
    Created on : 23 Dec, 2020, 11:29:25 AM
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
            var allowedExtensions = /(\.pdf)$/i;
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
                            alert('Please upload file having extensions .pdf only.');
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
            <form:form action="groupCCustdianCommunicationDetail.htm" method="POST" commandName="groupCCustodianCommunication" class="form-horizontal" enctype="multipart/form-data">
                <div class="panel panel-default">
                    <div class="panel-heading">Group C Custodian Communication Detail</div>
                    <div class="panel-body" style="height: 550px;overflow: auto;">
                        <div class="form-group">
                            <label class="control-label col-sm-2">Group C Employee Name:</label>
                            ${groupCCustodianCommunication.reviewedempname} , ${groupCCustodianCommunication.reviewedPost} 
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-2" >Remarks</label>
                            <div class="col-sm-6"> 
                                <form:textarea class="form-control" path="messagedetail"/>             
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2">Document</label>
                            <div class="col-sm-2"> 
                                <input type="file" name="uploadDocument" id="uploadDocument"  class="form-control-file" onchange="return uploadValidation(this)" />
                                <span style="color: red;">(Only .pdf are allowed)</span>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <form:hidden path="promotionId"/>
                        <form:hidden path="taskId"/>
                        <input type="submit" name="action" class="btn btn-default" value="Submit"/> 
                        <input type="submit" name="action" class="btn btn-default" value="Back"/>
                    </div>
                </div>
            </form:form>  
            <div class="table-responsive">
                <table class="table table-bordered table-hover table-striped">
                    <thead>
                        <tr>                                
                            <th width="3%">#</th>
                            <th width="15%">From Custodian</th>
                            <th width="15%">Custodian Designation</th>
                            <th width="15%">Remark of Custodian</th>
                            <th width="15%">Attachment</th>${custodiancommunication.communicationId}
                        </tr>
                    </thead>
                    <tbody>                                        
                        <c:forEach items="${custodianremarkList}" var="custodiancommunication" varStatus="count">
                            <tr>                                    
                                <td>${count.index + 1}</td>
                                <td>${custodiancommunication.fromempName}</td>
                                <td>${custodiancommunication.fromspc}</td>
                                <td>${custodiancommunication.messagedetail}</td>
                                <td> 
                                    <a href="downloadAttachmentOfGroupC.htm?communicationId=${custodiancommunication.communicationId}" class="btn btn-default" >
                                        <span class="glyphicon glyphicon-paperclip"></span> ${custodiancommunication.originalFilename}
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

        </div>
    </body>
</html>


