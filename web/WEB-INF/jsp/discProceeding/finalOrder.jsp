											
<%-- 
    Document   : finalOrder
    Created on : Sep 25, 2018, 3:07:49 PM
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

        <div class="panel panel-default">

            <div class="panel-body">
                <form:form action="FinalReport.htm" method="POST" commandName="noticeBean" class="form-inline">

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
                                <tr>
                                    <td colspan="2" align="center" class="headr">MEMORANDUM<br><br></td>
                                </tr>
                            </table>

                            <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">

                                <tr>
                                    <td>
                                        Whereas, Charges were Framed against sri/smt.
                                        <c:forEach items="${dpviewbean.delinquent}" var="delinquent">
                                            ${delinquent.doEmpName} ${delinquent.doEmpCurDegn},
                                        </c:forEach>

                                        vide this Department Proceeding No. Dtd ;<br><br><br>

                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Whereas,
                                        <c:forEach items="${investingOfficerList}" var="ciobean">
                                            ${ciobean.ioEmpName} ${ciobean.ioEmpCurDegn}
                                        </c:forEach>
                                        who Was appointed as the Inquiring Officer,vide this Department Office Order No.... dt.... Furnished ${dpviewbean.gender1} Inquiry Report
                                        dated <br><br> 
                                    </td>
                                </tr>
                                <tr>    
                                    <td>
                                        Whereas, after careful Consideration of the Charges ,findings of Inquiring Officer 
                                        and the representation submitted by the delinquent officer against the findings of the Inquiring
                                        officer u/r 15(10)(i)(a) and against the Proposed penalty u/r 15(10)(i)(b) of OCS (CC&A) Rule 
                                        , 1962, Government have been pleased to impose the Following penalty against sri/smt 
                                        <c:forEach items="${dpviewbean.delinquent}" var="delinquent">
                                            ${delinquent.doEmpName} ${delinquent.doEmpCurDegn}
                                        </c:forEach><br><br>
                                    </td>
                                </tr>
                                <tr>
                                    <td><b>
                                            Name of the Penalty:</b>

                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <c:forEach items="${penalty}" var="penalty" varStatus="cnt" >
                                            ${cnt.index + 1}. ${penalty.description} <br>                                 
                                        </c:forEach><br> 
                                    </td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                </tr>

                                <tr>
                                    <td align="right" class="headr">By order Of <br/> ${viewbean.getApproveAuthCurDegn()} 
                                        <br/><br><br>
                                    </td>
                                </tr>





                            </table>

                        </div>                    
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <input type="submit" name="action" value="Add Penalty" class="btn btn-default"/> 
                <input type="submit" name="action" value="Approve Order" class="btn btn-default"/> 
            </div>
        </form:form>
    </body>

</html>
