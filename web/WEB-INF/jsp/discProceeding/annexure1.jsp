<%-- 
    Document   : annexure1
    Created on : Jun 23, 2018, 12:46:32 PM
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
                    <h4 class="modal-title">IRREGULARITIES</h4>
                </div>
                <div class="panel-body">
                    <form:hidden path="offCode"/>
                    <form:hidden path="daId"/>
                    <form:hidden path="taskId"/>
                    <div align="center" width="99%" style="margin-top:5px;margin-bottom:10px;height:55%;">
                        <div align="center" class="easyui-panel" title="IRREGULARITIES" width="99%">
                            <table border="0" width="99%">

                                <tr>
                                    <td colspan="2" align="center" class="headr">Article of charges<br><br></td>

                                </tr>
                                <tr>
                                    <td colspan="2" align="center" class="headr">[Annexure-I]<br><br></td>
                                </tr>
                            </table>

                            <table width="100%" border="0" style="font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 13px;">

                                <tr>
                                    <td>
                                        <c:forEach items="${dpviewbean.delinquent}" var="delinquent"  >
                                           ${delinquent.doEmpName}, ${delinquent.doEmpCurDegn}
                                        </c:forEach>
                                   
                                    
                                        has committed gross irregularities for which the following articles of charge are framed against him for violating
                                        Rule 3 of the Orissa Government servants conduct Rules, 1959.<br><br><br>

                                        <c:forEach items="${dpviewbean.chargeListOnly}" var="chargeBean" varStatus="cnt">
                                            ${cnt.index+1}. ${chargeBean.articleOfCharge}<br>
                                        </c:forEach>
                                    </td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                </tr>

                            </table>
                        </div>  
                        <div class="panel-footer">
                            
                             <form:hidden path="taskId"/>
                            <input type="submit" name="action" value="Back" class="btn btn-default"/> 
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
