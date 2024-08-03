<%-- 
    Document   : refeaingIoReport
    Created on : Sep 1, 2018, 4:07:03 PM
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

        <script type="text/javascript">

            function validation() {
                if ($("#ioRemark").val() == "") {
                    alert("please Enter the remark");
                    $("#ioRemark").focus();
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <form:form action="viewIoReport.htm" method="post" commandName="ioReportBean">
                    <form:hidden path="daioid"/>
                    <form:hidden path="taskId"/>
                    <div class="panel-body">

                        <table class="table table-bordered">
                            <thead>
                                <tr> 
                                    <th width="5%">SI No</th>
                                   
                                    <th width="20%">Document Attach</th>
                                    <th width="30%">IO Statement</th>
                                    <th width="20%">Remark Details</th>
                                    <th width="20%">Action</th>
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
                                        <td>${ioreport.ioRemark}</td>
                                        <c:if test="${taskdtls.istaskcompleted eq 'N'}"> 
                                        <td><a href="viewfinalIoReport.htm?ioRemarkId=${ioreport.ioRemarkId}&taskId=${ioReportBean.taskId}" class="btn btn-default">Remark</a></td>
                                        </c:if>
                                    </tr>
                                </c:forEach>

                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">


                    </div><br>
                    <div class="panel-footer">
                        <c:if test="${taskdtls.istaskcompleted eq 'N'}">

                            <input type="submit" name="action" value="Submit Remark" class="btn btn-default" onclick="return validation()"/> 
                        </c:if>
                    </div>



                </form:form>
            </div>
        </div>
    </body>
</html>

