<%-- 
    Document   : replyCommunicationMessage
    Created on : Nov 6, 2019, 12:59:36 PM
    Author     : manisha
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script> 
        <style>

            .custom-file-upload {
                border: 1px solid #ccc;
                display: inline-block;
                padding: 6px 12px;
                cursor: pointer;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#replypanel").hide();
                $("#uploadedFile").bind("change", function() {
                    $("#uploadedFilename").html($(this).val());
                });
            });
            function openreplywindow() {
                $("#replypanel").show();
            }
            function closereplywindow() {
                $("#replypanel").hide();
            }
            var allowedExtensions = /(\.pdf|\.jpeg|\.jpg)$/i;
            function myFunction() {
                if (confirm("Are you sure to submit?")) {
                    return true;
                } else {
                    return false;
                }

            }

            function addAttachmentRow() {
                var num = $("#attachmentNum").val();
                num = parseInt(num) + 1;
                $("#attachmentNum").val(num);
                $('#moreAttach tr:last').after('<tr>' +
                        '<td align="left" ><input type="file" name="uploadedFile" id="uploadedFile"' + num + ' class="form-control" onchange="return uploadValidation(this)"/>' +
                        '<td><input type="button" onclick="romoveRow()" value="RemoveAttach" class="btn"/><\/td>' +
                        '<\/tr>');

                //remove more file components if remove is clicked


            }
            function romoveRow() {
                var num = $("#attachmentNum").val();
                if (num > 0) {
                    document.getElementById("fileTable").deleteRow(num);
                    num = parseInt(num) - 1;
                    $("#attachmentNum").val(num);
                }
                //remove more file components if remove is clicked
            }
            function clearAttachment() {
                $("#uploadedFile0").val('');
                $("#clearbtn").hide();
            }
            $(document).ready(function() {
                $("#clearbtn").hide();
            });
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
        <div class="container-fluid" style="padding-top: 5px;">
            <div class="row">
                <div class="col-lg-1">
                    &nbsp;
                </div>
                <div class="col-lg-9">
                    <c:forEach items="${employeemessagelist}" var="employeemessage" varStatus="cnt">
                        <div class="panel panel-primary">
                            <div class="panel-body">
                                <div class="row">
                                    <label class="col-sm-2">Message Title:</label>
                                    <div class="col-sm-6">
                                        <p>${employeemessage.msgTitle}</p>
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-sm-2" >
                                        <p>Message Date:</p>
                                    </label>
                                    <div class="col-sm-6" >
                                        <p>${employeemessage.messageondate}</p>
                                    </div>
                                </div> 
                                <div class="row">
                                    <label class="col-sm-2" >
                                        <p>Sender Name:</p>
                                    </label>
                                    <div class="col-sm-6" >
                                        <p>${employeemessage.senderName}</p>
                                    </div>
                                </div>
                                <div class="row">
                                    <label class="col-sm-2" >
                                        <p>Receiver Name:</p>
                                    </label>
                                    <div class="col-sm-6" >
                                        <p>${employeemessage.receiverName}</p>
                                    </div>
                                </div> 
                                <div class="row">
                                    <label class="col-sm-2" >
                                        <p>Message Details:</p>
                                    </label>
                                    <div class="col-sm-6" >
                                        <p>${employeemessage.message}</p>
                                    </div>
                                </div>  
                                <div class="row">
                                    <label class="col-sm-2" >
                                        <p>Attachment:</p>
                                    </label>
                                    <div class="col-sm-6" >
                                        <c:forEach items="${employeemessage.attachements}" var="attachment" varStatus="cnt">
                                            <p><a href="downloadEmployeeAttachment.htm?attachmentid=${attachment.attachmentid}">${attachment.attachementname}</a></p>
                                            </c:forEach>
                                    </div>
                                    <%--<div class="col-sm-6" >
                                        <p><a href="downloadEmployeeAttachment.htm?attachmentid=${employeemessage.attachmentid}">${employeemessage.attachementname}</a></p>
                                    </div> --%>
                                </div> 
                            </div>
                        </div>
                    </c:forEach>
                    <div class="panel panel-primary">
                        <div class="panel-footer">

                            <form:form action="viewCommunicationDetails.htm" commandName="employeeMessage" method="post" enctype="multipart/form-data" >
                                <form:hidden path="messageId"/>
                                <form:hidden path="senderId"/>
                                <form:hidden path="senderUserType"/>
                                <button type="submit" class="btn btn-success" name="action"/>Back
                                </button>
                                <button type="button" class="btn btn-success" name="action" value="Reply" onclick="openreplywindow()">
                                    <span class="glyphicon glyphicon-search"></span> Reply
                                </button>
                                <div id="replypanel">
                                    <div class="form-group">
                                        <label for="usr">Message Description</label>

                                        <form:textarea path="message" class="form-control" rows="10" cols="60"/>
                                    </div>
                                    <%--<div class="form-group">                                        
                                    <label for="uploadedFile" class="custom-file-upload">
                                        <i class="fa fa-cloud-upload"></i> Upload File
                                    </label>
                                    <span id="uploadedFilename"></span>
                                    <input type="file" name="uploadedFile" id="uploadedFile"/>                                        
                                    </div> --%>
                                    <div class="form-group">
                                        <label for="usr">Upload Document <span style="color: #c83939;font-style: italic;">(Maximum File size is 4 mb per attachment, Only PDF/jpg/jpeg  file can be uploaded.)</span></label>
                                        <table width="716" border="0" id="fileTable">  
                                            <tbody id="moreAttach">
                                                <tr>                                                
                                                    <td width="34%"><input type="file" name="uploadedFile" id="uploadedFile0" class="form-control" onchange="return uploadValidation(this)"/></td>
                                                    <td width="37%">
                                                        <input type ="button" value="AttachMore" onclick="addAttachmentRow()" />
                                                        <input type ="button" value="RemoveAttach" id="clearbtn" onclick="clearAttachment()" />
                                                        <input type="hidden" name="attachmentNum" value="0" id="attachmentNum"/>
                                                    </td>
                                                </tr>   
                                            </tbody>
                                        </table>


                                    </div>

                                    <div class="panel-footer">
                                        <input type="button" class="btn btn-success" name="action" value="Cancel"  onclick="closereplywindow()"/>
                                        <input type="submit" class="btn btn-success" name="action" value="Submit" onclick="return confirm('Are you sure to Submit?')"/>                                        
                                    </div>
                                </div>
                            </form:form>

                        </div>
                    </div>
                </div>
                <div class="col-lg-1">
                    &nbsp;
                </div>
            </div>
        </div>
    </body>
</html>
