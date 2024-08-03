<%-- 
    Document   : articleOfChargeList
    Created on : Feb 5, 2018, 4:10:42 PM
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
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="table-responsive">
                    <form:form action="addNewDisccharge.htm" method="post" commandName="chargebean">
                        <div class="panel panel-default">
                            <div class="panel-body">  
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="3%">#</th>
                                            <th width="25%">Article of Charge</th>
                                            <th width="25%">Statement Of Imputation</th>
                                            <th width="25%">Memo Of Evidence</th>
                                            <th width="15%">Witness</th>
                                            <th width="15%">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${articleOfChargeList}" var="articleOfCharge" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index + 1}</td>
                                                <td>
                                                    ${articleOfCharge.articleOfCharge} ${articleOfCharge.articlesofChargeoriginalfilename}
                                                    <c:if test="${not empty articleOfCharge.articlesofChargeoriginalfilename}">
                                                        <a href="downloadFileForArticleOfCharge.htm?dacid=${articleOfCharge.dacid}&documentTypeName=articlecharge" class="btn btn-default">
                                                           <span class="glyphicon glyphicon-paperclip"></span> ${articleOfCharge.articlesofChargeoriginalfilename}</a>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    ${articleOfCharge.statementOfImputation}
                                                    <c:if test="${not empty articleOfCharge.statementOfImputationoriginalfilename}">
                                                        <a href="downloadFileForArticleOfCharge.htm?dacid=${articleOfCharge.dacid}&documentTypeName=statementimputation" class="btn btn-default">
                                                            <span class="glyphicon glyphicon-paperclip"></span> ${articleOfCharge.statementOfImputationoriginalfilename}</a>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    ${articleOfCharge.memoOfEvidence}
                                                    <c:if test="${not empty articleOfCharge.memoofEvidenceoriginalfilename}">
                                                        <a href="downloadFileForArticleOfCharge.htm?dacid=${articleOfCharge.dacid}&documentTypeName=memoevidence" class="btn btn-default" >
                                                           <span class="glyphicon glyphicon-paperclip"></span> ${articleOfCharge.memoofEvidenceoriginalfilename}</a>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <a  href="employeeWitnessList.htm?action=witnessList&dacid=${articleOfCharge.dacid}&daId=${chargebean.daId}" class="btn btn-default" >Witness</a>                                                    
                                                </td> 
                                               
                                                <td>
                                                    <a href="editNewDisccharge.htm?dacid=${articleOfCharge.dacid}&daId=${chargebean.daId}" class="btn btn-default" >Edit </a>
                                                </td>                            
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="panel-footer">
                                <form:hidden path="daId"/>
                                <input type="submit" name="action" value="Add New" class="btn btn-default"/>
                                <input type="submit" name="action" value="Back" class="btn btn-default"/>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>

