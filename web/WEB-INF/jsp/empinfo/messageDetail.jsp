<%-- 
    Document   : messageDetail
    Created on : Dec 30, 2017, 12:13:38 PM
    Author     : manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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

        <script type="text/javascript">
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
                        '<td align="left" style="border-top: none;"><input type="file" name="uploadedFile" id="uploadedFile"' + num + ' class="form-control" onchange="return uploadValidation(this)"/>' +
                        '<td style="border-top: none;"><input type="button" onclick="romoveRow()" value="RemoveAttach" class="btn"/><\/td>' +
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
            function PreviewImage() {
                $('#myModal').modal('show');
                pdffile = document.getElementById("uploadedFile0").files[0];
                pdffile_url = URL.createObjectURL(pdffile);
                $('#viewer').attr('src', pdffile_url);
            }

        </script>

    </head>
    <body>
        <c:choose>
            <c:when test = "${fn:contains(LoginUserBean.loginuserid, 'alf')}">
            <body style="margin-top:0px;background:#188B7A;">
                <jsp:include page="../tab/AlfaMenu.jsp"/>  
                <div id="wrapper"> 
                    <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                    </c:when>
                    <c:otherwise>
                        <body>
                            <div id="wrapper">
                                <jsp:include page="../tab/agOdishaMenu.jsp"/>
                                <div id="page-wrapper">
                                </c:otherwise>
                            </c:choose>
                            <form:form action="saveEmployeeMessage.htm" commandName="employeeMessage" method="post" enctype="multipart/form-data">
                                <div class="form-group">
                                    <label for="usr">Send to</label>

                                    <form:input path="recieverSpn" class="form-control" readonly="true"/> 
                                    <!--
                                    <c:if test=" ${empty recieverSpn}">
                                        
                                    </c:if>
                                    -->
                                    <form:hidden path="empid"/>
                                    <form:hidden path="receiverUserType"/>
                                    <form:hidden path="recieverSpc"/>


                                </div>
                                <div class="form-group">
                                    <label for="usr">Message Title</label>

                                    <form:input path="msgTitle" class="form-control" />
                                </div>
                                <div class="form-group">
                                    <label for="usr">Message Description</label>

                                    <form:textarea path="message" class="form-control" rows="10" cols="60"/>
                                </div>
                                <div class="form-group">
                                    <label for="usr">Upload Document <span style="color: #c83939;font-style: italic;">(Maximum File size is 4 mb per attachment, Only PDF/jpg/jpeg file can be uploaded.)</span></label>
                                    <table  class="table table-borderless"   id="fileTable">  
                                        <tbody id="moreAttach">
                                            <tr>                                                
                                                <td width="35%" style="border-top: none;">
                                                    <input type="file" name="uploadedFile" id="uploadedFile0" class="form-control" onchange="return uploadValidation(this)"/>
                                                     </td>
                                                    

                                                <td width="35%" style="border-top: none;">
                                                    <input type ="button" value="AttachMore" onclick="addAttachmentRow()" />
                                                    <input type ="button" value="RemoveAttach" id="clearbtn" onclick="clearAttachment()" />
                                                    <input type="hidden" name="attachmentNum" value="0" id="attachmentNum"/>
                                                </td>
                                                 <td width="30%" style="border-top: none;">
                                                    <input type="button" value="Preview" onclick="PreviewImage();" />
                                                </td>
                                            </tr>   
                                        </tbody>
                                    </table>


                                </div>
                                <div class="panel-footer">
                                    <input type="submit" name="action" class="btn btn-primary" value="Submit" onsubmit="return myFunction()" /> 
                                    <input type="submit" name="action" class="btn btn-info" value="Back" /> 
                                </div>
                            </form:form>
                        </div>
                    </div>



                    <div class="modal fade" id="myModal" role="dialog" >
                        <div class="modal-dialog  modal-lg">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title" style="text-align: center">View Attachment Detail</h4>
                                </div>
                                <div class="modal-body myModalBody" style="height: 500px;">
                                    <iframe id="viewer" frameborder="0" scrolling="no" width="100%" height="100%"></iframe>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>

                </body>
                </html>
