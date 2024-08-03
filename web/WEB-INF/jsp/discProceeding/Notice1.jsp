<%-- 
    Document   : Notice1
    Created on : Sep 25, 2018, 3:22:59 PM
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
        <form:form action="sendNotice.htm" method="POST" commandName="noticeBean" class="form-inline">
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
                                    <td colspan="2" align="center" class="headr">Notice-1<br><br></td>
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
                                    <td>
                                        Whereas  ${dpviewbean.gender} not furnished ${dpviewbean.gender1} explanations to the charges Framed against 
                                        ${dpviewbean.gender} in the said Proceedings (vide  ${dpviewbean.gender} represantation dated...) <br><br> 
                                    </td>
                                </tr>
                                <tr>    
                                    <td>
                                        Whereas,
                                        <c:forEach items="${investingOfficerList}" var="ciobean">
                                            ${ciobean.ioEmpName} ${ciobean.ioEmpCurDegn}
                                        </c:forEach>
                                        .was appointed as the Inquiring Officer vide......
                                        Department Office Order No.......to enquiry into the Charges framed against Sri/Smt....
                                        in the said proceeding${dpviewbean.gender} may peruse the relevant records in the office of 

                                        and take relevant extracts thereof to submit ${dpviewbean.gender1} written statement of defence with permission from the competetent authority.<br><br>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Whereas the Inquiring Officer Furnished  ${dpviewbean.gender} Inquiry Report in  ${dpviewbean.gender}
                                        letter No.......<br><br>

                                    </td>
                                </tr>
                                <tr>  
                                    <td>
                                        Now, in pursuance of rule 15 (10) (i) (a) of O.C.S. (CC&A) Rules, 1962,
                                        Sri/smt ${dpviewbean.approveAuthName},${dpviewbean.approveAuthCurDegn} is here by called upon to submit  ${dpviewbean.gender}
                                        representation as  ${dpviewbean.gender} Wishes to make against the findings of the Inquiry Authority within a period of
                                        15 days failing which the matter will be disposed of on its own merit.

                                    </td>
                                </tr>

                                <tr>
                                    <td>&nbsp;</td>
                                </tr>

                                <tr>
                                    <td colspan="2" align="right" class="headr">By Order Of ${viewbean.getApproveAuthCurDegn()}

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
                    <div class="panel-heading" align="center"> <b> Remark of Delinquent Officer</b></div>  
                    <div class="panel-body">
                        <c:if test="${taskdtls.istaskcompleted eq 'N'}">

                            <br>
                            Enter Remark:&nbsp; <form:textarea class="form-control" path="remark"  rows="2" cols="100"/><br><br>
                            <input type="submit" name="action" value="Submit Reply">
                        </c:if>

                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
