<%-- 
    Document   : GPFSchedule
    Created on : 5 Nov, 2016, 11:30:57 AM
    Author     : Prashant
--%>

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
        <title>:: Audit Recovery ::</title>
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
        <div style="width:90%;margin: 0 auto;">
            <table width="100%" border="0">
                <tr>
                    <td width="100%" style="text-align:center" class="printData">
                        <b>SCHEDULE OF DEDUCTION OF AUDIT RECOVERIES FOR THE MONTH OF -&nbsp;<c:out value="${ARHeader.aqMonth}"/></b>
                    </td>
                </tr>
                <tr>
                    <td width="100%" style="text-align:center" class="printData">
                        <b>Head of Account in which deduction shall be credited- </b>
                    </td>
                </tr>
                <tr>
                    <td width="100%" style="text-align:center" class="printData"> 0056-jails-800-0097-02054 Misc. Receipts</b> </td>
                </tr>
                <tr>
                    <td width="100%" style="text-align:center">&nbsp;</td>
                </tr>
            </table>
        </div>
                    
                    
        <div style="width:99%;margin: 0 auto;">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:13px;">
                <thead></thead>
                <tr>
                    <td>&nbsp;</td>    
                </tr>
                <tr>
                    <td style="text-align:left;font-size:14px;text-align:left;"><b>Office Name:-</b> <c:out value="${ARHeader.officeName}"/></td>
                </tr>
                <tr>
                    <td style="text-align:left;font-size:14px;text-align:left;"><b>Bill No:-</b> <c:out value="${ARHeader.billDesc}"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>    
                </tr>
            </table>
        </div>
                    
        <div style="width:99%;margin: 0 auto;font-family:verdana;">
            <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                <tr class="tblHeader" height="45px">
                    <td width="5%" align="center" class="tblHeader">Sl No</td>
                    <td width="22%" align="center" class="tblHeader">Name of the Incumbents with Designation</td>
                    <td width="10%" align="center" class="tblHeader">Amount of Recoveries</td>
                    <td width="10%" align="center" class="tblHeader">Amount to be deducted now</td>
                    <td width="10%" align="center" class="tblHeader">No. of instalment</td>
                    <td width="10%" align="center" class="tblHeader">Balance</td>
                    <td width="10%" align="center" class="tblHeader">Audit report No.& para</td>
                    <td width="10%" align="center" class="tblHeader">Head of Account to be Credited</td>
                    <td width="8%" align="center" class="tblHeader">REMARKS</td>
                </tr>
                <c:if test="${not empty AREmpList}">
                    <c:forEach var="eachArType" items="${AREmpList}">
                        <tr style="height:30px">
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachArType.slno}"/></td>
                            <td align="left" style="border-bottom:1px solid #000000;">
                                <c:out value="${eachArType.empname}"/> / <c:out value="${eachArType.empdesg}"/>
                            </td>
                            <td align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachArType.amtRec}" escapeXml="false"/></td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachArType.amtDeduct}"/></td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachArType.noofInstallment}"/></td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachArType.balance}"/></td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;</td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;</td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;</td>
                        </tr>
                        
                        <c:if test="${not empty eachArType.pagebreakAR}">
                            <tr>
                                <td colspan="3" class="pgHeader" style="text-align:right; text-transform: uppercase;">Carry Forward:</td>
                                <td style="text-align:center;padding-left: 10px;font-weight: bold;">  <c:out value="${eachArType.carryFordAmt}"/> </td> 
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                                <td> &nbsp; </td>
                            </tr>
                            <tr><td colspan="9"> <hr/> </td></tr>
                            <tr>
                                <td colspan="9" class="pgHeader" style="text-align:right;text-transform: uppercase;">Page No:<%=pageNo++%> </td>
                            </tr>
                        </table>
                        <c:out value="${eachArType.pagebreakAR}" escapeXml="false"/>
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">          
                            <c:out value="${eachArType.pageHeaderAR}" escapeXml="false"/>
                        </c:if>
                            
                    </c:forEach>
                </c:if>      
            </table>
            
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                <tr>
                    <td width="100%" colspan="9" height="5px"><hr/></td>
                </tr>
                <tr>
                    <td width="5%">&nbsp;</td>
                    <td width="22%">&nbsp;</td>
                    <td width="10%" style="text-align:center;"><b>Total :<b></td>
                    <td width="10%" style="text-align:center;font-weight: bold;">&nbsp;<c:out value="${TotalDAmt}"/></td>
                    <td width="10%">&nbsp;</td>
                    <td width="10%">&nbsp;</td>
                    <td width="11%">&nbsp;</td>
                    <td width="10%">&nbsp;</td>
                    <td width="8%">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="9" height="5px"><hr/></td>
                </tr>
                <tr>
                    <td width="50%" colspan="2" style="text-align:right;">&nbsp;</td>
                    <td width="50%" colspan="7" style="text-align:left;">&nbsp;(RUPEES &nbsp; <b><c:out value="${TotalFig}"/></b>) ONLY</td>
                </tr>
                <tr>
                <td colspan="9" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
            </tr>
            </table> 
        </div>
</body>
</html>
