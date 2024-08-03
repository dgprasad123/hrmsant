<%-- 
    Document   : ParAdverseRemarkReplySiPAR
    Created on : 23 Aug, 2023, 1:20:12 PM
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
            <div class="table-responsive">
                <div class="panel panel-primary">
                    <div class="panel-heading">Communication From : ${communicationDetail.fromempName}, ${communicationDetail.fromPost}</div>
                    <div class="panel-footer">Communication To: ${communicationDetail.toempName}</div>
                    <div class="panel-body">Communication Detail : ${communicationDetail.remarksdetail}</div>
                    <div class="panel-body">Attachment : 
                        <c:if test="${not empty communicationDetail.originalFilename}">
                            <a href="downloadAttachmentforAdversePAR.htm?parId=${communicationDetail.parId}&communicationId=${communicationDetail.communicationId}" class="btn btn-default" >
                                <span class="glyphicon glyphicon-paperclip"></span> ${communicationDetail.originalFilename}
                            </a> 
                        </c:if>
                    </div>

                </div>

            </div>
            <form:form action="parAdverseCommunicationReplySiPAR.htm" method="POST" commandName="parAdverseCommunicationDetail" class="form-horizontal" enctype="multipart/form-data">
                <div class="panel panel-default">
                    <div class="panel-heading">Par Adverse Remark</div>
                    <div class="panel-body" style="height: 200px;overflow: auto;">
                        <form:hidden path="adverseRemarkstype"/>
                        <form:hidden path="toempId"/>
                        <form:hidden path="tospc"/>
                        <form:hidden path="parId"/>
                        <form:hidden path="adverseCommunicationStatusId"/>
                        <div class="form-group">
                            <label class="control-label col-sm-2">To Custodian:</label>
                           ${communicationDetail.fromempName}, ${communicationDetail.fromPost}                            
                        </div>
                        <c:if test="${parAdverseCommunicationDetail.adverseCommunicationStatusId eq 121}">
                              <div class="form-group">
                                  <label class="control-label col-sm-2" >Representation On Adverse Remark</label>
                                  <div class="col-sm-6"> 
                                      <form:textarea class="form-control" path="remarksdetail"/>             
                                  </div>
                              </div>
                        </c:if>
                        <c:if test="${parAdverseCommunicationDetail.adverseCommunicationStatusId eq 123}">
                              <div class="form-group">
                                  <label class="control-label col-sm-2" >Representation On Substantiation</label>
                                  <div class="col-sm-6"> 
                                      <form:textarea class="form-control" path="remarksdetail"/>             
                                  </div>
                              </div>
                        </c:if>
                        

                        <div class="form-group">
                            <label class="control-label col-sm-2">Document</label>
                            <div class="col-sm-2"> 
                                <input type="file" name="uploadDocument" id="uploadDocument"  class="form-control-file" onchange="return uploadValidation(this)" />
                                <span style="color: red;">(Only .pdf and .jpg files are allowed)</span>
                            </div>
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

        </div>
    </body>
</html>


