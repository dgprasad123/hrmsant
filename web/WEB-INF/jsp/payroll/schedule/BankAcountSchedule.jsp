<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    int pageNo = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: BANK SCHEDULE ::</title>
        <style type="text/css">
            .pgHeader{
                font-size:12px;
                font-family:verdana;
                font-weight: bold;
            }
            .tblHeader{
                font-size:12px;
                font-family:verdana;
                font-weight: bold;
                border-top:1px solid black;
                border-bottom:1px solid black;
                border-left:1px solid black;
                border-right:1px solid black;
            }
        </style>
    </head>
    <body>
    <div style="width:99%;margin: 0 auto;">
        <table width="100%" border="0">
            <tr>
                <th colspan="3" style="text-align:center" class="pgHeader"><b><c:out value="${BankAcHeader.offName}"/></b></th>
            </tr>
            <tr>
                <th colspan="3" style="text-align:center" class="pgHeader">Bill No: <b><c:out value="${BankAcHeader.billdesc}"/></b></th>
            </tr>
            <tr>
                <th colspan="3" style="text-align:center" class="pgHeader">Schedule - Bank Statement</th>
            </tr>
            <tr>
                <th colspan="3" style="text-align:center" class="pgHeader">FOR <c:out value="${BankAcHeader.month}"/> -- <c:out value="${BankAcHeader.year}"/></th>
            </tr>
            <tr>
                <th width="35%" style="text-align:left" class="pgHeader">Amount Credited to CA A/C of DDO</th>
                <td width="35%">&nbsp;</td>
                <td width="30%">&nbsp;</td>
            </tr>
            <tr>
                <th style="text-align:left" class="pgHeader">Name of the DDO who maintains these accounts :</th>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
        </table>
            
       <table border="0" width="100%" cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;" id="innercontainertbl">
            <tr align="center">
                <td width="3%" class="tblHeader"><b>Sl No.</b></td>
                <td width="18%" align="left" class="tblHeader"><b>SB A/C No. /  <br> Name of the Bank</b></td>
                <td width="30%" align="left" class="tblHeader"><b>Name/<br> Designation</b></td>
                <td width="12%" align="right" class="tblHeader"><b>(PF No) <br> Net Amount <br> (in Rs)</b></td> 
                <td width="12%" align="right" class="tblHeader"><b>Loan/ Advance <br> Liability <br> Amount (in Rs)</b></td> 
                <td width="12%" align="right" class="tblHeader"><b>Amt Credited to <br> SB A/C of the <br> employee (in Rs)</b></td>  
                <td width="18%" align="right" class="tblHeader"><b>Amt Credited to <br> CA A/C of the <br> DDO (in Rs) </b></td>  
            </tr>
            <c:if test="${not empty BankAccList}">
                <c:forEach var="eachEmpDtls" items="${BankAccList}">
                    <tr style="height:50px;">
                        <td style="text-align:center;font-size: 11;border-bottom:1px solid #000000;"><c:out value="${eachEmpDtls.slno}"/></td>
                        <td style="text-align:left;font-size: 11;border-bottom:1px solid #000000;"><c:out value="${eachEmpDtls.accountNo}"/></td>
                        
                        <td style="text-align:left;font-size: 11;border-bottom:1px solid #000000;">
                           <c:out value="${eachEmpDtls.empname}"/><br>
                            <c:out value="${eachEmpDtls.designation}"/>&nbsp;
                        </td>
                        <td style="text-align:right;font-size: 11;border-bottom:1px solid #000000;">
                            <c:out value="${eachEmpDtls.gpfNo}"/>&nbsp; <br>
                            <c:out value="${eachEmpDtls.netAmount}"/>&nbsp; 
                        </td>
                        <td style="text-align:right;font-size: 11;border-bottom:1px solid #000000;"><c:out value="${eachEmpDtls.towardsLoan}"/>&nbsp;&nbsp; </td>
                        <td style="text-align:right;font-size: 11;border-bottom:1px solid #000000;"><c:out value="${eachEmpDtls.totalReleased}"/>&nbsp;&nbsp; </td>
                        <td style="text-align:right;font-size: 11;border-bottom:1px solid #000000;"><c:out value="${eachEmpDtls.otherDeposits}"/>&nbsp;&nbsp; </td>
                    </tr>
                    <c:if test="${not empty eachEmpDtls.pagebreakBS}">
                        <tr>
                            <td colspan="3" class="pgHeader" style="text-align:right; text-transform: uppercase;">Carry Forward:</td>
                            <td style="text-align:right;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpDtls.netCfTot}"/> &nbsp;&nbsp; </td> 
                            <td style="text-align:center;padding-left: 10px;font-weight: bold;"> &nbsp; </td>
                            <td style="text-align:right;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpDtls.carryForward}"/> &nbsp;&nbsp; </td> 
                            <td style="text-align:right;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpDtls.carryForwardDDO}"/> &nbsp;&nbsp; </td>
                        </tr>
                        <tr><td colspan="7"> <hr/> </td></tr>
                        <tr>
                            <td colspan="7" class="pgHeader" style="text-align:right;text-transform: uppercase;">Page No:<%=pageNo++%> </td>
                        </tr>
                    </table>
                    <c:out value="${eachEmpDtls.pagebreakBS}" escapeXml="false"/>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">          
                        <c:out value="${eachEmpDtls.pageHeaderBS}" escapeXml="false"/>
                    </c:if>
                </c:forEach>
                    <tr style="height:10px;">
                        <td colspan="2" style="text-align:left;font-size: 11;"> &nbsp; </td>
                        <td>&nbsp;</td>
                        <td style="text-align:right;font-size: 11;font-weight: bold;"> Total &nbsp; <c:out value="${NetTotal}"/></td>    
                        <td>&nbsp;</td>
                        <td style="text-align:right;font-size: 11;font-weight: bold;">Total &nbsp;<c:out value="${TotalSbAc}"/>&nbsp;&nbsp; </td>    
                        <td style="text-align:right;font-size: 11;font-weight: bold;"><c:out value="${TotalDDO}"/>&nbsp;&nbsp; </td>  
                    </tr>
                    <tr><td colspan="7"> <hr/> </td></tr>
            </c:if>
            
        </table>
        <table width="99%" border="0" cellpadding="0" cellspacing="0" style="font-size:10px;font-family:verdana">
            <tr align="right" >
                <td width="70%" style="text-align: right;" class="txtf">&nbsp;</td>
                <td width="30%" style="text-align: center;" class="txtf">&nbsp;</td>
            </tr> 
            <tr align="right" >
                <td style="text-align: right;" class="txtf">&nbsp;</td>
                <td style="text-align: center;" class="txtf"><b>Signature of the D.D.O. with Seal</b> <br>
                    <c:out value="${BankAcHeader.ddoDesg}"/> 
                    <c:out value="${BankAcHeader.offName}"/><br>
                    Date:</td>
            </tr> 
            <tr>
                <td style="font-size:12px;">&nbsp;</td>
                <td style="text-align:right;text-transform: uppercase;" align="right">Page No:<%=pageNo++%></td>
            </tr>
        </table>
    </div>
            
</body>
</html>
