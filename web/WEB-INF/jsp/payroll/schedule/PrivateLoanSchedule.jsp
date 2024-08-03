<%-- 
    Document   : GPFSchedule
    Created on : 5 Nov, 2016, 11:30:57 AM
    Author     : Prashant
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    int pageNo = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: Private Loan Schedule ::</title>
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
        <div align="center" style="font-family:verdana;">
            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td class="printData" style="font-size:17px;font-family:verdana;text-align:center;"><b><c:out value="${PLHeader.officeName}"/></b></td>
                </tr>
                <tr>
                    <td class="printData" style="font-size:14px;font-family:verdana;text-align:center;">
                        <c:out value="${PLHeader.deductedAmtDesc}"/>  SCHEDULE FOR THE MONTH OF : <b><c:out value="${PLHeader.month}"/> - <c:out value="${PLHeader.year}"/></b>
                    </td>
                </tr>
                <tr>
                    <td class="printData" style="font-size:14px;font-family:verdana;text-align:center;">
                        Bill No : <b> <c:out value="${PLHeader.billDesc}"/> </b>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </div>
        
        <div align="center">
            <table width="100%" border="0" cellpadding="1" cellspacing="1">
                <tr style="height:40px;">
                    <td width="5%" class="printData" style="font-family:verdana;font-size:12px;text-align:center;border:1px solid black;">
                        <b>SL NO</b>
                    </td>
                    <td width="30%" class="printData" style="font-family:verdana;font-size:12px;text-align:center;border:1px solid black;">
                        <b>NAME AND DESIGNATION</b>				
                    </td>
                    <td width="20%" class="printData" style="font-family:verdana;font-size:12px;text-align:center;border:1px solid black;">
                        <b>AMOUNT(PVT LOAN /DED DESCRIPTION)</b>
                    </td>
                </tr>
                
            <c:if test="${not empty PLEmpList}">
                <c:forEach var="eachPl" items="${PLEmpList}">
                    <tr style="height:30px">
                        <td align="center" style="border-bottom:1px solid #000000;font-family:verdana;font-size:12px;">&nbsp;<c:out value="${eachPl.slno}"/></td>
                        <td align="left" style="border-bottom:1px solid #000000;font-family:verdana;font-size:12px;">&nbsp;
                            <c:out value="${eachPl.empname}"/> / <c:out value="${eachPl.empdesg}"/>
                        </td>
                        <td align="center" style="border-bottom:1px solid #000000;font-family:verdana;font-size:12px;">&nbsp;
                            <c:if test="${not empty eachPl.helperList}">
                                <c:forEach var="eachPlAmt" items="${eachPl.helperList}">
                                    <c:out value="${eachPlAmt.deductedAmt}"/> / <c:out value="${eachPlAmt.deductedAmtDesc}"/> <br/>
                                </c:forEach>
                            </c:if>    
                        </td>
                    </tr>
                    <c:if test="${not empty eachPl.pagebreakPLS}">
                        <tr>
                            <td colspan="2" class="pgHeader" style="text-align:right; text-transform: uppercase;">Carry Forward:</td>
                            <td style="text-align:center;padding-left: 10px;font-weight: bold;"> <c:out value="${eachPl.pageTotalPLS}"/> </td> 
                        </tr>
                        <tr><td colspan="3"> <hr/> </td></tr>
                        <tr>
                            <td colspan="3" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page No:<%=pageNo++%> </td>
                        </tr>
                        </table>
                            <c:out value="${eachPl.pagebreakPLS}" escapeXml="false"/>
                            <div align="center" style="font-family:verdana;">
                            <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                            <tr>
                                <td class="printData" style="font-size:17px;font-family:verdana;text-align:center;"><b><c:out value="${PLHeader.officeName}"/></b></td>
                            </tr>
                            <tr>
                                <td class="printData" style="font-size:14px;font-family:verdana;text-align:center;">
                                    PRIVATE LOAN / DEDUCTION SCHEDULE FOR THE MONTH OF : <b><c:out value="${PLHeader.month}"/> - <c:out value="${PLHeader.year}"/></b>
                                </td>
                            </tr>
                            <tr>
                                <td class="printData" style="font-size:14px;font-family:verdana;text-align:center;">
                                    Bill No : <b> <c:out value="${PLHeader.billDesc}"/> </b>
                                </td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                            </tr>
                            </table>
                            </div>    
                            <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">                           
                            <c:out value="${eachPl.pageHeaderPLS}" escapeXml="false"/>
                    </c:if>
                </c:forEach>
            </c:if>
                    <tr style="height:30px;">
                        <td colspan="2" class="printData" style="font-family:verdana;text-align:left;border-top: 1px solid #000000;border-bottom:1px solid black;">Grand Total</td>
                        <td class="printData" style="font-family:verdana;font-size:12px;text-align:center;border-top: 1px solid #000000;border-bottom:1px solid black;"><b> <c:out value="${TotDAmt}"/> </b></td>
                    </tr>
                    <tr>
                        <td colspan="3" class="printData" style="font-family:verdana;font-size:12px;text-align:center;border-bottom: 1px dashed #000000;">
                            In Words (Rupees <c:out value="${TotDAmtFig}"/> ) Only
                        </td>
                    </tr>
                    <tr style="height:20px;"></tr>
                    <tr>
                        <td colspan="3" class="printData" style="font-family:verdana;font-size:12px;text-align:left;border-bottom: 1px dashed #000000;">
                            Please pay Rs._______________________________ by transfer credit to the DDO's Current Account No 
                            <b><c:out value="${PLHeader.ddoAccountNo}"/></b> recoveries of loans from banks and financial institutions 
                            in respect of _____________ number of employees.
                        </td>
                    </tr>
                    <tr><td colspan="3" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page No:<%=pageNo++%></td></tr>
            </table>
        </div>            
                    
    </body>
</html>


