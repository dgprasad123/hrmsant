<%-- 
    Document   : LeaveAccountOutput
    Created on : Jul 17, 2018, 3:13:58 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>::Employee Leave Account::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">      
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            .leaveaccttbl thead tr td{
                font-weight: bold;
                text-align:center;
            }
            .printData{
                text-align:center;
                font-size: 14px;
            }
            .printLabel{
                text-align:center;
                font-size: 14px;
                font-weight: bold;
            }
        </style>

    </head>
    <body style="margin: 10px;">
        <div class="container container-fluid">
            <table width="100%" height="70px" border="0" cellpadding="0" cellspacing="0" style="font-size:12px; font-family:verdana;border-color:#000000; ">
                <tr>
                    <td width="16%" class="printLabel" style="text-align:left;font-size: 14;">Employee Name</td>
                    <td width="35%" class="printData" style="text-align:left;font-size: 14;font-weight: bold;"> ${eap.empName}&nbsp;</td>
                    <td width="10%" class="printLabel" style="text-align:left;font-size: 14;">HRMS Id </td>
                    <td width="22%" class="printData" style="text-align:left;font-size: 14;font-weight: bold;">${eap.empId}&nbsp;</td>
                </tr>
                <tr>
                    <td  class="printLabel" style="text-align:left;font-size: 14;">GPF / PPAN No.</td>
                    <td  class="printData" style="text-align:left;font-size: 14;font-weight: bold;">${eap.empGpf}&nbsp;</td>
                    <c:if test="${eap.duration != ''}">
                        <td  class="printLabel" style="text-align:left;font-size: 14;"> For the Period </td>
                        <td  class="printData" style="text-align:left;font-size: 14;font-weight: bold;">${eap.duration}&nbsp;</td>
                    </c:if>
                    <c:if test="${eap.duration == ''}">
                        <td  class="printLabel" style="text-align:left;font-size: 14;">&nbsp;</td>
                        <td  class="printData" style="text-align:left;font-size: 14;">&nbsp;</td>
                    </c:if>       
                </tr>
            </table>
            <table width="100%" border="1" cellpadding="0" cellspacing="0" class="leaveaccttbl">
                <thead>
                    <tr>
                        <td colspan="11" height="35">${eap.leaveType} ACCOUNT </td>
                    </tr>
                    <tr>
                        <td height="100" colspan="2">Period of Account </td>
                        <td width="8%" rowspan="2"><p align="center">No of </br>Complete</br> Months </p></td>
                        <td width="8%" rowspan="2">Leave Credit in Days </td>
                        <td width="8%" rowspan="2">Total</br> No.of </br>Leave</br>(EOL)</br> Availed </td>
                        <td width="8%" rowspan="2">EL to be Deducted </td>
                        <td width="8%" rowspan="2">Balance</br> on </br>return</br> from </br>Leave </td>
                        <td width="30%" colspan="3">Leave Availed </td>
                        <td width="6%" rowspan="2">Balance Leave </td>
                    </tr>
                    <tr>
                        <td width="12%" height="23" >From</td>
                        <td width="12%">To</td>
                        <td width="12%">From</td>
                        <td width="12%">To</td>
                        <td width="6%">Total</td>
                    </tr>
                </thead>

                <c:if test="${eap.leaveOBalDate != ''}">    

                    <tr>
                        <td class="printLabel" colspan="6" style="text-align:left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OPENING BALANCE ON DATE &nbsp;&nbsp;${eap.leaveOBalDate}&nbsp;${eap.fnan}</td>
                        <td width="8%" class="printLabel">&nbsp;${eap.leaveOBal}</td>
                        <td width="36%" colspan="4" class="printData">&nbsp;</td>
                    </tr>

                </c:if>

                <c:forEach items="${eap.creditLvList}" var="creditDetails">                
                    <c:if test="${creditDetails.creditType eq 'U'}">            
                        <tr>
                            <td colspan="2" class="printData" style="text-align:center;font-size: 14;">
                                UNAVAILED JOINING TIME
                            </td>
                            <td width="8%" rowspan="2" class="printData">&nbsp;${creditDetails.compMonths}</td>
                            <td width="8%" rowspan="2" class="printData">&nbsp;${creditDetails.leaveCredited}</td>
                            <td width="8%" rowspan="2" class="printData">&nbsp; ${creditDetails.totEOLNumber}</td>
                            <td width="8%" rowspan="2" class="printData">&nbsp;${creditDetails.leaveDeduct}</td>
                            <td width="8%" rowspan="2" class="printData">&nbsp;${creditDetails.creditBalShow}</td>
                            <td width="36%" rowspan="2" class="printData">&nbsp;</td>
                        </tr>
                        <tr>
                            <td  width="12%" class="printData">&nbsp;${creditDetails.fromDate}</td>
                            <td  width="12%" class="printData">&nbsp;${creditDetails.toDate}</td>
                        </tr>
                    </c:if>                    
                    <c:if test="${creditDetails.creditType eq 'G'}">                    
                        <tr>
                            <td width="11%" class="printData">&nbsp;${creditDetails.fromDate}</td>
                            <td width="12%" class="printData">&nbsp;${creditDetails.toDate}</td>
                            <td width="8%" class="printData">&nbsp;${creditDetails.compMonths}</td>
                            <td width="8%" class="printData">&nbsp;${creditDetails.leaveCredited}</td>
                            <td width="8%" class="printData">&nbsp;${creditDetails.totEOLNumber}</td>
                            <td width="8%" class="printData">&nbsp;${creditDetails.leaveDeduct}</td>
                            <td width="8%" class="printData">&nbsp;${creditDetails.creditBalShow}</td>
                            <td class="printData" colspan="4">&nbsp;</td>
                        </tr>
                    </c:if>
                    <c:forEach items="${creditDetails.availedLeave}" var="aLeave">
                        <tr>
                            <td colspan="7" width="62%">&nbsp;</td>
                            <td width="14%" class="printData">${aLeave.fromdate}</td>
                            <td width="14%" class="printData">${aLeave.todate}</td>
                            <td width="5%" class="printData">${aLeave.totalNoofdays}</td>
                            <td width="5%" class="printData">${aLeave.balanceLeave}</td>
                        </tr>
                    </c:forEach>
                    <c:forEach items="${creditDetails.surrenderedLeave}" var="surrenderedLeave">
                        <tr>
                            <td colspan="7" width="62%">&nbsp;</td>
                            <td width="14%" class="printData">SURRENDERED</td>
                            <td width="14%" class="printData">LEAVE</td>
                            <td width="5%" class="printData">${surrenderedLeave.surrenderDays}</td>
                            <td width="5%" class="printData">${surrenderedLeave.balanceLeave}</td>
                        </tr>
                    </c:forEach>    
                </c:forEach>
            </table>
        </div>
    </body>
</html>
