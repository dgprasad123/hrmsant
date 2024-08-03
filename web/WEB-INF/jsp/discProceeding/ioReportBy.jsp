<%-- 
    Document   : ioReportBy
    Created on : Aug 31, 2018, 11:15:23 AM
    Author     : manisha
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
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
        <div class="container-fluid">
            <div class="panel panel-default">
                <form:form action="viewIoReport.htm" method="post" commandName="ioReportBean">
                    <form:hidden path="daioid"/>

                    <div class="panel panel-default">
                        <div class="panel-body"> 
                            <div class="panel panel-default">                            
                                <div class="panel-heading" align="center">
                                    <b>Delinquent officer List:</b>
                                </div>
                                <div class="panel-body">   

                                    <c:forEach items="${delinquentOfficerList}" var="employee">
                                        <div>${cnt.index+1}. ${employee.fullname}, ${employee.spn}</div>
                                    </c:forEach>
                                </div>  
                            </div>

                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-body"> 
                            <div class="panel panel-default">                            
                                <div class="panel-heading" align="center">
                                    <b>Article of Charge:</b>
                                </div>
                                <div class="panel-body">   
                                    <c:forEach items="${discchargelist}" var="chargeBean" varStatus="cnt">
                                        <div>${cnt.index+1}.  ${chargeBean.articleOfCharge} ${chargeBean.dacid}</div>
                                        <c:forEach items="${chargeBean.witnessList}" var="witness" varStatus="cntw">
                                            <div style="padding-left: 30px;">${cntw.index+1}. ${witness.empname}</div>
                                        </c:forEach>
                                    </c:forEach>
                                </div>  
                            </div>

                        </div>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-body"> 
                            <div class="panel panel-default">                            
                                <div class="panel-heading" align="center">
                                    <b>Statment of imputations</b>
                                </div>
                                <div class="panel-body">   
                                    ${dpviewbean.statementOfImputation}
                                </div>  
                            </div>
                        </div>
                    </div>


                    <div class="panel panel-default">                            
                        <div class="panel-heading" align="center"><b> Memo of Evidence:<br>
                                Documents by which the charge is proposed to be sustained:</b><br></div>
                        <div class="panel-body">   

                            <c:forEach items="${articleOfChargeList}" var="chargeBean" varStatus="cnt">
                                ${cnt.index+1}. <a href="downloadEmployeeAttachment.htm?attachmentid=${chargeBean.uploadFileId}" class="btn btn-default" >
                                    <span class="glyphicon glyphicon-paperclip"></span> 
                                    ${chargeBean.uploadFileName} ${chargeBean.uploadDocument}
                                </a>
                                <br/><br/>
                            </c:forEach>

                        </div>  
                    </div>

                    <div class="panel panel-default">
                        <form:hidden path="daId"/>
                        <form:hidden path="taskId"/>
                        <div class="panel-heading" align="center">
                            <b>Defence Statement Submitted by Delinquent Officer</b><br>
                        </div>
                        <div class="panel-body">   
                            <c:forEach items="${delinquentOfficerList}" var="delinquent">
                                ${delinquent.fname} ${delinquent.mname} ${delinquent.lname}  ${delinquent.spn}
                            </c:forEach>
                            <div class="panel-body">
                                Remarks:
                                ${defencebean.defenceRemark}
                            </div>
                        </div> 
                    </div>

                    <div class="panel panel-default">
                        <form:hidden path="daId"/>
                        <form:hidden path="taskId"/>
                        <div class="panel-heading" align="center"><b> Reply Of Authority On Defence Statement Remark:</b><br></div>
                        <div class="panel-body">   
                            <c:forEach items="${discchargelist}" var="chargeBean" varStatus="cnt">
                                Remark: ${chargeBean.remark}<br><br>
                            </c:forEach>
                        </div>  
                    </div>
                </div>


                <div class="panel-body">

                    <table class="table table-bordered">
                        <thead>
                            <tr> 
                                <th width="5%">SI No</th>                                        
                                <th width="30%">Document Attach</th>
                                <th width="30%">Remark</th>

                            </tr>                            
                        </thead>
                        <tbody>

                            <c:forEach items="${investingreport}" var="ioreport" varStatus="cnt">
                                <tr> 
                                    <td>${cnt.index+1}</td>                                            
                                    <td>
                                        <c:if test="${not empty ioreport.documentName}">
                                            <a href="downloadEmployeeAttachment.htm?attachmentid=${ioreport.documentId}" class="btn btn-default" >
                                                <span class="glyphicon glyphicon-paperclip"></span> ${ioreport.documentName}
                                            </a>
                                        </c:if>                                            
                                    </td>
                                    <td>${ioreport.remark}</td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">

                    <c:if test="${taskdtls.istaskcompleted eq 'N'}">
                        <input type="submit" name="action" class="btn btn-default" value="Add Exihibits"/>
                        <input type="submit" name="action" class="btn btn-default" value="Submit" onclick="return confirm('Are you sure to Submit?')"/>
                    </c:if>

                </div>
            </form:form>
        </div>
    </div>
</body>
</html>

