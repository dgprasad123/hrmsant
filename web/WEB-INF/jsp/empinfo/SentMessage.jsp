<%-- 
    Document   : SentMessage
    Created on : Jan 12, 2018, 11:56:43 AM
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

        <script type="text/javascript">
            function gotoPage() {
                self.location = 'getSentMessageList.htm?pageno=' + $("#pagecombo").val();
            }

            function openAttachmentList() {
                $('#myModal').modal('show');
            }

            $(document).ready(function() {
                $('.openPopup').on('click', function() {
                    var dataURL = $(this).attr('data-href');
                    $('.myModalBody').load(dataURL, function() {
                        $('#myModal').modal({show: true});
                    });
                });
            });


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
                            <form:form commandName="adminUsers" method="post" action="getSentMessageList.htm">
                                <div class="container-fluid">
                                    <!-- Page Heading -->
                                    <div class="row">
                                        <div class="col-lg-12">                            
                                            <ol class="breadcrumb">
                                                <li>
                                                    <i class="fa fa-dashboard"></i>  <a href="#">Dashboard</a>
                                                </li>
                                                <li class="active">
                                                    <i class="fa fa-file"></i> Sent Message
                                                </li>

                                            </ol>
                                        </div>
                                    </div>
                                    <div class="row vertical-align">
                                        <div class="col-lg-3 col-xs-3"> <h1> Message List </h1> </div>                        
                                        <div class="col-lg-6 col-xs-6">


                                            <c:if test="${LoginUserBean.loginuserid eq 'alfaodisha'}">
                                                <div class="panel-body">  
                                                    <table class="table table-bordered table-hover table-striped">
                                                        <tr>
                                                            <td style="width: 30%;align:center;"><b>Local Fund Offices:</b> </td>
                                                            <td style="width: 80%">
                                                                <form:select path="searchBox"  class="form-control">
                                                                    <form:option value="">-Select-</form:option>
                                                                    <form:options items="${audituser}" itemLabel="fullName" itemValue="empId"/>                                                                                
                                                                </form:select> 

                                                            </td>
                                                            <td><input type="submit" value="Search" name="action" class="btn btn-primary"/></td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </c:if>

                                        </div>
                                        <div class="col-lg-3 col-xs-3" style="vertical-align: bottom;" align="right">
                                            Page : 
                                            <select name="pageno" id="pageno">
                                                <c:forEach var="i" begin="0" end="${totalPage-1}">
                                                    <option value="${i}" <c:if test="${adminUsers.pageno eq i}">selected="selected"</c:if>>${i+1}</option>
                                                </c:forEach>
                                            </select>
                                            <input type="submit" name="action" value="Go"/>
                                        </div>
                                    </div>
                                    <div class="row">                                                
                                        <div class="col-lg-12">
                                            <div class="table-responsive">
                                                <table class="table table-bordered table-hover table-striped">
                                                    <thead>
                                                        <tr>
                                                            <th width="2%">#</th>
                                                            <th width="15%">Sender Name</th>
                                                            <th width="15%">Receiver Name / Current Post / Current Office</th>
                                                            <th width="40%">Message</th>
                                                            <th width="10%">Attachment</th>
                                                            <th width="10%">Message send date</th>
                                                            <th width="5%">Whether Viewed by Receiver</th>
                                                            <th width="10%">Message Viewed date</th>
                                                            <th width="10%">View</th>
                                                            <th width="5%">Whether Reply Received</th>
                                                            <th width="10%">Date of Receipt of Reply</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>                                        
                                                        <c:forEach items="${employeemessagelist}" var="employeemessage" varStatus="cnt">
                                                            <tr>
                                                                <td>${employeemessage.sino}</td>
                                                                <td>${employeemessage.senderName}</td>
                                                                <td>${employeemessage.empname}, ${employeemessage.recieverSpc}</td>
                                                                <td>${employeemessage.message}</td>

                                                                <td>

                                                                    <c:if test="${employeemessage.noofAttachments gt 0}">
                                                                        <a href="javascript:void(0);" data-href="EmployeemessageAttachment.htm?messageId=${employeemessage.messageId}" class="openPopup">View Attachments <span class="badge">${employeemessage.noofAttachments}</span></a>
                                                                        </c:if> 

                                                                </td>
                                                                <td>${employeemessage.messageondate}</td>
                                                                <c:if test="${employeemessage.isviewed eq 'Y'}"><td align="center" style="background-color: #99ff99;font-weight: bold;"><span class="glyphicon glyphicon-ok"></span> Yes</td></c:if>
                                                                <c:if test="${empty employeemessage.isviewed}"><td align="center">Not Yet</td></c:if>
                                                                <td>${employeemessage.viewondate}</td>
                                                                <td><a href="viewMessageCommunicationDtls.htm?messageId=${employeemessage.messageId}">View</a></td>

                                                                <c:if test="${not empty employeemessage.repliedondate}"><td align="center" style="background-color: #99ff99;font-weight: bold;"><span class="glyphicon glyphicon-ok"></span> Yes</td></c:if>
                                                                <c:if test="${empty employeemessage.repliedondate}"><td align="center"><span class="glyphicon glyphicon-remove"></span> No</td></c:if>
                                                                <c:if test="${empty employeemessage.viewondate}"><td align="center">Not Yet</td></c:if>
                                                                <td>${employeemessage.repliedondate}</td>
                                                            </tr>
                                                        </c:forEach>                                        
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form:form>

                    <div class="modal fade" id="myModal" role="dialog" >
                        <div class="modal-dialog  modal-lg">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">View Attachment Detail</h4>
                                </div>
                                <div class="modal-body myModalBody">
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </body>
                </html>