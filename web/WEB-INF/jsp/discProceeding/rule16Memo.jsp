<%-- 
    Document   : rule16Memo
    Created on : Jul 4, 2018, 12:11:29 PM
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
        <form:form action="saveRule15Memo.htm" method="POST" commandName="pbean" class="form-inline">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="modal-title">IRREGULARITIES</h4>
                </div>
                <div class="panel-body">
                    <form:hidden path="offCode"/>
                    <form:hidden path="daId"/>
                    <div align="center" width="99%" style="margin-top:5px;margin-bottom:10px;height:55%;">
                        <div align="center" class="easyui-panel" title="IRREGULARITIES" width="99%">
                            <table border="0" width="99%">
                                <tr>
                                    <td colspan="2" align="center" class="headr">Government of Odisha
                                        <br/>[<c:out value="${proceedingbean.deptName}"/> DEPARTMENT]
                                    </td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr>
                                    <td width="50%" align="left">
                                        <div class="form-group">
                                            <label>Memorandum No: </label>
                                            <form:input path="memoNo" class="form-control"/>
                                        </div>
                                    </td>
                                    <td width="50%" align="center"><label>Date:</label>
                                        <div class='input-group date' id='processDate'>
                                            <form:input class="form-control" id="memoDate" path="memoDate" />
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>
                                    </td>
                                </tr> 
                                <tr>
                                    <td>&nbsp;</td>
                                </tr>
                            </table>

                            <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">
                                <tr>
                                    <td>
                                        <c:forEach items="${proceedingbean.employees}" var="employee">
                                            ${employee.fullname}
                                        </c:forEach>
                                        is here by informed that it is proposed to hold an inquiry against him/her under Rule16 of the Orissa Civil Services(Classification, Control and Appeals)Rules, 1962.
                                        The Substance of the imputations of conduct in respect of which the inquiry is proposed to held is set out in the enclosed Statement of articles of charge (Annexure-I).
                                        A Statement of the imputations of misconduct in support of the articles of charge in enclosed <br>(Annexure-II)along with Memo of Evidence(Annexure-III).
                                        <br>


                                        1. sri/smt <c:forEach items="${proceedingbean.employees}" var="employee">
                                            ${employee.fullname}
                                        </c:forEach>
                                        is here by given an opportunity to make representations as he/she wishes to make against the proposed disciplinary action. 
                                        <br>

                                        2. If He/She fails to submit his/her representations  within  15 days of receipt of this Memorandum, it will be presumed 
                                        that he/she has no representation to submit and orders as deemed proper will be passed against him/her in accordance with law.<br>

                                        4.The receipt of the memorandum should be acknowledged by sri/smt<br>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2" align="right" class="headr">By order Of the Governor
                                        <br/><br><br>Additional Secretary to Govt.
                                    </td>
                                </tr>
                                <tr>
                                    <td style="padding-left: 20px;">
                                        <form:textarea path="annex1Charge" class="form-control" rows="5"/>                                        
                                    </td>
                                </tr>    
                                <tr>
                                    <td>&nbsp;</td>
                                </tr>
                            </table>
                        </div>
                    </div>                    
                </div>
                <div class="panel-footer">
                    <input type="submit" name="btnAnnexture1" value="Previous" class="form-control"/>    
                    <input type="submit" name="btnAnnexture1" value="Save" class="form-control" onclick="return saveMemo()"/>
                </div>
            </div>
        </form:form>
    </body>
</html>
