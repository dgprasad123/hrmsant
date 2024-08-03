<%-- 
    Document   : discApprovedAction
    Created on : May 25, 2018, 3:27:49 PM
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
            function openAnnexture1() {
                location.href = "";
            }
        </script>
    </head>
    <body style="padding:0px;">        
        <form:form action="discApprovedAction.htm" method="POST" commandName="pbean" class="form-inline">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="modal-title">Disciplinary Authority Approval</h4>
                </div>
                <div class="panel-body">

                    <form:hidden path="offCode"/>
                    <form:hidden path="daId"/>
                    <form:hidden path="taskId"/>
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
                                <tr>
                                    <td colspan="2" align="center" class="headr">MEMORANDUM<br><br></td>
                                </tr>
                            </table>

                            <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">

                                <tr>
                                    <td>
                                        <c:forEach items="${dpviewbean.delinquent}" var="delinquent">
                                            ${delinquent.doEmpName}, ${delinquent.doEmpCurDegn}
                                        </c:forEach>


                                        is here by informed that it is proposed to take Departmental Action against him/her under Rule 15 of the Orissa Civil Services (Classification, Control and Appeal)
                                        Rules, 1962. The Substance of the imputation in respect of which the inquiry is proposed to be held is set out in the enclosed statement of articles of charges(Annexure-I)
                                        . The Statement of imputations in support of the article of charges is enclosed(Annexure-II) along with Memo of Evidence (Annexure-III) .<br><br><br>

                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        1 ${dpviewbean.initAuthName}, ${dpviewbean.initAuthCurDegn}
                                        is hereby given an opportunity to make representations as ${dpviewbean.gender} wishes to make against the Proposed Disciplinary action.<br><br> 
                                    </td>
                                </tr>
                                <tr>    
                                    <td>
                                        2  ${dpviewbean.gender} may peruse the relevant records in the office of 

                                        and take relevant extracts thereof to submit ${dpviewbean.gender1} written statement of defence with permission from the competetent authority.<br><br>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        3 If ${dpviewbean.gender} fails to submit ${dpviewbean.gender1} representation within 15 days of the receipt of this Memorandum, it will be presumed that  ${dpviewbean.gender} has no representation to submit and 
                                        orders as deemed proper will be passed against him/her in accordance of the law.<br><br>

                                    </td>
                                </tr>
                                <tr>  
                                    <td>
                                        4 The receipt of the memorandum should be acknowledged  by sri/smt  ${dpviewbean.approveAuthName},${dpviewbean.approveAuthCurDegn}

                                    </td>
                                </tr>

                                <tr>
                                    <td>&nbsp;</td>
                                </tr>

                                <tr>
                                    <td colspan="2" align="right" class="headr">By Order Of 
                                        ${dpviewbean.approveAuthCurDegn}
                                    </td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                </tr>

                            </table>

                            <div class="panel-footer">                                
                                <input type="submit" name="action" value="Approved" class="form-control" onclick="return confirm('Are you sure to Approved?')"/>
                                <input type="submit" name="action" value="Declined" class="form-control"/>
                               <input type="submit" name="action" value="Article Of Charge" class="form-control"/>
                               <input type="hidden" name="openfrom" value="T"/>
                              
                            </div>




                        </div>                    


                    </div>
                </form:form>
                </body>
                </html>
