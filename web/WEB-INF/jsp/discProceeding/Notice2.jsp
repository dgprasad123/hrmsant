<%-- 
    Document   : Notice2
    Created on : Sep 25, 2018, 3:47:17 PM
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
        <form:form action="sendNotice.htm" method="POST" commandName="discChargeBean" class="form-inline">
            <div class="panel panel-default">
                <div class="panel-body">

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
                                    <td colspan="2" align="center" class="headr">Notice-2<br><br></td>
                                </tr>
                            </table>

                            <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">

                                <tr>
                                    <td>
                                        Whereas, charges Framed against sri/smt
                                        <c:forEach items="${dpviewbean.delinquent}" var="delinquent">
                                            ${delinquent.doEmpName} ${delinquent.doEmpCurDegn},
                                        </c:forEach>
                                        vide....Department Proceeding No...... under rule15 of OCS(C.C&A) Rules, 1962;<br><br><br>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Whereas,
                                        <c:forEach items="${investingOfficerList}" var="ciobean">
                                            ${ciobean.ioEmpName} ${ciobean.ioEmpCurDegn}
                                        </c:forEach>
                                        who Was appointed as the Inquiring Officer,vide this Department Office Order No.... dt.... to enquiry into the
                                        charges framed against sri/smt.....furnished  ${dpviewbean.gender}Inquiry Report in ${dpviewbean.gender}
                                        letter No......dt..... <br><br> 
                                    </td>
                                </tr>
                                <tr>    
                                    <td>
                                        Whereas in pursuance of rule 15 (10) (i) (a) of O.C.S. (CC&A) Rules, 1962,
                                        Sri/smt ${dpviewbean.approveAuthName},${dpviewbean.approveAuthCurDegn} was called upon to  submit  ${dpviewbean.gender}
                                        show cause represantation if  ${dpviewbean.gender} Wishes to make against the findings of the Inquiry Authority within a period of
                                        15 days  vide Notice No. ....., dtd.....<br><br>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Whereas sri/smt ..... not submited  ${dpviewbean.gender} show cause representation against the findings
                                        of Inquiring Authority.<br><br>

                                    </td>
                                </tr>
                                <tr>  
                                    <td>
                                        Now, in pursuance of rule 15 (10) (i) (a) of O.C.S. (CC&A) Rules, 1962,
                                        Sri/smt ${dpviewbean.approveAuthName},${dpviewbean.approveAuthCurDegn} is here by called upon to submit  ${dpviewbean.gender}
                                        show cause representation if  ${dpviewbean.gender} Wishes to make against the proposed penalty within a period of
                                        one month failing which the same shall  be disposed of exparte.

                                    </td>
                                </tr>

                                <tr>
                                    <td>&nbsp;</td>
                                </tr>

                                <tr>
                                    <td colspan="2" align="right" class="headr">By Order of ${viewbean.getApproveAuthCurDegn()}<br>
                                       

                                    </td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                </tr>


                            </table>

                        </div>                    
                    </div>
                </div>    
            </div>    

            <div class="panel-footer">
                <div class="panel panel-default">
                    <c:if test="${taskdtls.istaskcompleted eq 'N' }">
                        <div class="panel-heading" align="center"> <b> Remark of Delinquent Officer</b></div>  
                        <div class="panel-body">

                            <br>
                            Enter Remark:&nbsp; <form:textarea class="form-control" path="remark"  rows="2" cols="100"/><br><br>
                            <input type="submit" name="action" value="Submit Reply of Notice2">
                        </c:if>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
