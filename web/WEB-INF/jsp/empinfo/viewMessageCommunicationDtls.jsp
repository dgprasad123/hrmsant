<%-- 
    Document   : viewMessageCommunicationDtls
    Created on : Sep 1, 2018, 8:47:18 PM
    Author     : Manas
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
        <script type="text/javascript">

        </script>
    </head>
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
                                <jsp:include page="../tab/hrmsadminmenu.jsp"/>
                                <div id="page-wrapper">
                                </c:otherwise>
                            </c:choose>
                            <div class="container-fluid">
                                <!-- Page Heading -->
                                <div class="row">
                                    <div class="col-lg-12">                            
                                        <ol class="breadcrumb">
                                            <li>
                                                <i class="fa fa-dashboard"></i>  <a href="getSentMessageList.htm">Message List</a>
                                            </li>
                                            <li class="active">
                                                <i class="fa fa-file"></i> Message Details
                                            </li>

                                        </ol>
                                    </div>
                                </div>



                                <div class="row">
                                    <div class="col-lg-2" style="padding-left: 10px;">
                                        <div class="text-center">
                                            <img src="images/attention.jpg" alt=""  style="width:180px;height:70px;" />

                                        </div>
                                    </div>
                                    <div class="col-lg-8">
                                        <div class="row">
                                            <label>1. Please See the attachment. Ignore, if no attachment is there.</label>
                                        </div>
                                    </div>
                                </div>


                                <div class="row">
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
                                                        Message Date:
                                                    </label>
                                                    <div class="col-sm-6" >
                                                        <p>${employeemessage.messageondate}</p>
                                                    </div>
                                                </div> 
                                                <div class="row">
                                                    <label class="col-sm-2" >
                                                        Sender Name:
                                                    </label>
                                                    <div class="col-sm-6" >
                                                        <p>${employeemessage.senderName}</p>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <label class="col-sm-2" >
                                                        Receiver Name :
                                                    </label>
                                                    <div class="col-sm-6" >
                                                        <p>${employeemessage.receiverName}</p>
                                                    </div>
                                                </div> 
                                                <div class="row">
                                                    <label class="col-sm-2" >
                                                        Message Details:
                                                    </label>
                                                    <div class="col-sm-6" >
                                                        ${employeemessage.message}
                                                    </div>
                                                </div>  
                                                <c:if test="${not empty employeemessage.attachements}">
                                                    <div class="row">
                                                        <label class="col-sm-2" >
                                                            Attachment: 
                                                        </label>
                                                        <div class="col-sm-6" >
                                                            <c:forEach items="${employeemessage.attachements}" var="attachment" varStatus="cnt">
                                                                <p><a href="downloadEmployeeAttachment.htm?attachmentid=${attachment.attachmentid}">${attachment.attachementname}</a></p>
                                                                </c:forEach>
                                                        </div>
                                                    </div>      
                                                </c:if>
                                            </div>
                                        </div>
                                    </c:forEach>

                                </div>
                                <div class="panel-footer">
                                    <%--<form:hidden path="${employeeMessage.messageId}" /> --%>
                                    <a href="getSentMessageList.htm">
                                        <input type="button" class="btn btn-success" name="action" value="Back"  />
                                    </a>

                                    <a href="SentMessageDetailPDF.htm?messageId=${employeeMessage.messageId}" class="btn-default" target="_blank"><button type="button" class="btn btn-primary">Download</button></a>


                                </div>
                            </div>
                        </div>
                </body>
                </html>
