<%-- 
    Document   : Report
    Created on : Sep 26, 2018, 11:41:22 AM
    Author     : manisha
--%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
    </head>
    <body>
        <form:form action="FinalReport.htm" method="POST" commandName="noticeBean" class="form-inline">
            <div id="page-wrapper">

                <form:hidden path="taskId"/>

                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-body"> 

                            <div class="panel panel-default">                            
                                <div class="panel-heading" align="center"><b>Delinquent officer List</b></div>
                                <div class="panel-body">
                                    <c:forEach items="${delinquentOfficerList}" var="employee">
                                        <div>${cnt.index+1}. ${employee.fullname}, ${employee.spn}</div>
                                    </c:forEach>
                                </div>                            
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default">                            
                        <div class="panel-heading" align="center"><b>Statment of imputations</b></div>
                        <div class="panel-body">
                            ${dpviewbean.irrgularDetails}
                        </div>

                    </div>

                    <div class="panel panel-default">                            
                        <div class="panel-heading" align="center"><b>  Article of Charge:</b><br></div>
                        <div class="panel-body">   
                            <c:forEach items="${discchargelist}" var="chargeBean" varStatus="cnt">
                                <div>${cnt.index+1}.  ${chargeBean.charge} ${chargeBean.dacid}</div>
                                <c:if test="${fn:length(chargeBean.witnessList) > 0}">
                                    <div><b>Witness:</b></div>
                                </c:if>
                                <c:forEach items="${chargeBean.witnessList}" var="witness" varStatus="cntw">
                                    <div style="padding-left: 30px;">${cntw.index+1}. ${witness.empname}</div>
                                </c:forEach>
                            </c:forEach>

                        </div>  
                    </div>

                    <div class="panel panel-default">                            
                        <div class="panel-heading" align="center"> <b>Memo of Evidence:<br>
                                Documents by which the charge is proposed to be sustained:</b><br></div>
                        <div class="panel-body">   
                            <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                                <tr>
                                    <td>                                        
                                        <c:forEach items="${articleOfChargeList}" var="chargeBean" varStatus="cnt">
                                            ${cnt.index+1}. <a href="downloadEmployeeAttachment.htm?attachmentid=${chargeBean.uploadFileId}" class="btn btn-default" >
                                                <span class="glyphicon glyphicon-paperclip"></span> 
                                                ${chargeBean.uploadFileName} ${chargeBean.uploadDocument}
                                            </a>
                                            <br/><br/>
                                        </c:forEach>
                                    </td>
                                </tr>
                            </table>


                        </div>  
                    </div>
                    <div class="panel panel-default"> 
                        <div class="panel-heading" align="center"><b>Defence Statement Submited by</b><br>
                            <div>

                                <c:forEach items="${delinquentOfficerList}" var="delinquent">
                                    ${delinquent.fname} ${delinquent.mname} ${delinquent.lname}  ${delinquent.spn}
                                </c:forEach>
                            </div>
                        </div>
                        <div class="panel-body">

                            ${defencebean.defenceRemark}
                        </div>
                    </div>

                    <div class="panel panel-default">                            
                        <div class="panel-heading" align="center"><b> Io Report:</b><br></div>
                        <div class="panel-body">

                            <table class="table table-bordered">
                                <thead>
                                    <tr> 
                                        <th width="5%">SI No</th>
                                        
                                        <th width="20%">Document Attach</th>
                                        <th width="30%">IO Statement</th>
                                        <th width="20%">Remark Of Initiating Authority</th>

                                    </tr>                            
                                </thead>
                                <tbody>

                                    <c:forEach items="${investingreports}" var="ioreport" varStatus="cnt">
                                        <tr> 
                                            <td>${cnt.index+1}</td>
                                           
                                            <td>
                                                <c:forEach items="${ioreport.attachments}" var="attachment">
                                                    <a href="downloadEmployeeAttachment.htm?attachmentid=${attachment.attachmentId}" class="btn btn-default" >
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${attachment.orgFileName}
                                                    </a>
                                                </c:forEach>                                          
                                            </td>
                                            <td>${ioreport.remark}</td>
                                            <td>${ioreport.ioRemark}</td>

                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </div>
                    <c:if test="${not empty dpviewbean.notice1ondate}">
                        <div class="panel panel-default">                            
                            <div class="panel-heading" align="center"><b>Reply of Notice1</b></div>
                            <div class="panel-body">
                                <div>

                                    <c:forEach items="${noticeBeanList}" var="noticeBean" >
                                        ${noticeBean.remark} <br><br>                                   
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${not empty dpviewbean.notice2ondate}">
                        <div class="panel panel-default">                            
                            <div class="panel-heading" align="center"><b>Reply of Notice2</b></div>

                            <div class="panel-body">
                                <div>
                                    <c:forEach items="${noticeBeanList}" var="noticeBean" >
                                        ${noticeBean.remark} <br><br>                                   
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <div class="panel-footer">
                        <form:hidden path="daId"/>
                        <c:if test="${not empty dpviewbean.notice1ondate}">
                        <input type="submit" name="action" value="Final Order" class="btn btn-default"/> 
                        </c:if>
                    </div>

                </div>
            </div>
        </form:form>
    </body>
</html>