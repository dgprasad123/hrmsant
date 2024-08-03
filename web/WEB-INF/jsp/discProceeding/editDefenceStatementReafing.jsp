<%-- 
    Document   : editDefenceStatementReafing
    Created on : Aug 24, 2018, 1:46:42 PM
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

        <title>Bootstrap Example</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

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
                if ($("#uploadDocument").val() == "") {
                    alert("please upload document");
                    $("#uploadDocument").focus();
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form action="updateDefenceStatementReafing.htm" method="POST" commandName="discChargeBean" class="form-inline">
            <div class="panel panel-default">
                <div class="panel-heading" align="center">
                    Charge and Charge Details
                </div>
                <div class="panel-body" align="center">
                    <b>Charge:</b>${discChargeBean.articleOfCharge}
                    <br/>
                    <b>Charge Details:</b>${discChargeBean.statementOfImputation}
                </div>                 
            </div>

            <div class="panel panel-default">
                <div class="panel-heading" align="center">Defence Statement and Documents Submited by <b>${delinquentOfficerDtls.fname} ${delinquentOfficerDtls.mname} ${delinquentOfficerDtls.lname}  ${delinquentOfficerDtls.spn}</b></div>                
                <div class="panel-body">                                                                
                ${defencebean.defenceRemark} 
                <br/>
                <c:forEach items="${defencebean.attachments}" var="attachment">
                    <a href="downloadEmployeeAttachment.htm?attachmentid=${attachment.attachmentId}" class="btn btn-default">
                    ${attachment.orgFileName}
                    </a>
                </c:forEach>
                </div>
            </div>
            
            <div class="form-group">
                <label class="control-label col-sm-2" > Enter Remark: </label>
                <div class="col-sm-8"> 
                    <form:hidden path="dacid"/>
                     <form:hidden path="dadid"/>
                    <form:hidden path="daId"/>
                    <form:hidden path="taskId"/>
                    <form:textarea class="form-control" path="remark"  rows="2" cols="100"/><br><br><br>
                    <input type="submit" name="action" value="Save">
                    <input type="submit" name="action" value="Back">
                </div>
            </div>

        </form:form>

    </body>
</html>