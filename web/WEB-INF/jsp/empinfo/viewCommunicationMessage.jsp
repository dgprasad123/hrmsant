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
            input[type="file"] {
                display: none;
            }
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
        </script>
    </head>
    <body>
        <div class="container-fluid" style="padding-top: 5px;">
            <div class="row">
                <div class="col-lg-1">
                    &nbsp;
                </div>
                <div class="panel panel-primary">
                    <div class="panel-body">
                        <div class="text-center">
                            <img src="images/attention.jpg" alt=""  style="width:180px;height:70px;" />
                        </div>
                        <div class="row">
                            <label>1. Please See the attachment. Ignore, if no attachment is there.</label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
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
                                <%-- <c:if test="${not empty employeemessage.attachementname}"> --%>
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
                                <%--   </c:if>  --%>
                            </div>
                        </div>
                    </c:forEach>

                </div>
            </div>

            <div class="col-lg-1">
                &nbsp;
            </div>
        </div>
   
    <div class="panel-footer">
        <a  href="backviewCommunicationDetails.htm?" class="btn btn-success" >Back</a>
    </div>
</body>
</html>
