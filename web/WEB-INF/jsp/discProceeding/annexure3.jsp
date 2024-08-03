<%-- 
    Document   : annexure3
    Created on : Jun 27, 2018, 2:32:16 PM
    Author     : manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:HRMS:</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>

        <style type="text/css">
            .headr{
                font-weight: bold;
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 16px;
            }
        </style>

        <script language="javascript" type="text/javascript">
            $(document).ready(function() {
                $('#memoDate').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
            });
            function saveMemo() {
                var memoNo = $('#rule15MemoNo').val();
                if (memoNo == '') {
                    alert("Please enter Memorandum No");
                    return false;
                }
                if (memoNo.length >= 18) {
                    alert("Please Enter a Memorandum No between 1 and 18");
                    return false;
                }
                var ordrDate = $('#rule15MemoDate').datebox('getValue');// will get the date value
                if (ordrDate == '') {
                    alert("Please enter Date");
                    return false;
                }
                var charge = $('#annex1Charge').val();
                if (charge.length >= 495) {
                    alert("Please Enter value between 0 and 495");
                    return false;
                }

            }
        </script>
    </head>
    <body style="padding:0px;">        
        <form:form action="discApprovedAction.htm" method="POST" commandName="pbean" class="form-inline">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="modal-title">Article Of Charge Against Delinquent Officer</h4>
                </div>
                <div class="panel-body">

                    <form:hidden path="daId"/>
                    <form:hidden path="taskId"/>
                    <div class="panel-body">  

                        <table class="table table-bordered table-hover table-striped">
                            <thead>
                                <tr>
                                    <th width="3%">#</th>
                                    <th width="25%">Article of Charge</th>
                                    <th width="25%">Statement Of Imputation</th>
                                    <th width="25%">Memo Of Evidence</th>
                                    <th width="25%">Action</th>
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
                                            <a href="editNewDisccharge.htm?daId=${articleOfCharge.daId}&openfrom=${param.openfrom}" class="btn btn-default" >Edit </a>
                                        </td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>



                    </div>


                </div>
                <div class="panel-footer">
                    <input type="hidden" name="openfrom" value="T"/>

                    <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                </div>
            </div>
        </form:form>
    </body>
</html>
