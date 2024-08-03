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
        <title>:: GPF Schedule ::</title>
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

        <c:if test="${not empty GPFTypeList}">
            <c:forEach var="eachGpf" items="${GPFTypeList}">  
                
                <c:out value="${eachGpf.pageheaderparent}" escapeXml="false"/>

                <c:if test="${not empty eachGpf.helperList}">
                    <c:forEach var="eachGpfType" items="${eachGpf.helperList}">
                        <tr style="height:30px">
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachGpfType.slno}"/></td>
                            <td align="left" style="border-bottom:1px solid #000000;">&nbsp;
                                <c:out value="${eachGpfType.accountNo}"/> / <c:out value="${eachGpfType.dateOfEntry}"/>
                            </td>
                            <td align="left" style="border-bottom:1px solid #000000;">&nbsp;
                                <c:out value="${eachGpfType.empName}"/> / <c:out value="${eachGpfType.designation}"/>
                            </td>
                            <td align="center" style="border-bottom:1px solid #000000;">
                                <c:out value="${eachGpfType.basicPay}"/><br/><c:out value="${eachGpfType.gradePay}"/><br/><c:out value="${eachGpfType.scaleOfPay}"/>
                            </td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachGpfType.monthlySub}"/></td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;
                                <c:out value="${eachGpfType.towardsLoan}"/>&nbsp;
                                <c:if test="${not empty eachGpfType.noOfInstalment}">
                                    (<c:out value="${eachGpfType.noOfInstalment}"/>)
                                </c:if>
                                <c:if test="${empty eachGpfType.noOfInstalment}">
                                    &nbsp;
                                </c:if>
                            </td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachGpfType.totalReleased}"/></td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;
                                <c:out value="${eachGpfType.dob}"/> /<br/>&nbsp;<c:out value="${eachGpfType.dor}"/></td>
                        </tr>
                        <c:if test="${not empty eachGpfType.pagebreakchild}">
                            </table>
                            <c:out value="${eachGpfType.carryForward}" escapeXml="false"/>
                            <table width="100%" border="0" style="left: 18px;" cellpadding="0" cellspacing="0" style="font-size:11px;">
                                <tr>
                                     <td colspan="3" height="5px">
                                        <hr/>
                                     </td>
                                </tr>
                                <tr>
                                    <td class="printData">&nbsp;</td>
                                    <td class="printFooter" style="text-align:center">&nbsp;</td>
                                    <td class="printData" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%></td>
                                </tr>
                            </table>
                            <c:out value="${eachGpfType.pagebreakchild}" escapeXml="false"/>
                            <c:out value="${eachGpfType.pageHeaderTable}" escapeXml="false"/>
                            <c:out value="${eachGpfType.broughtForward}" escapeXml="false"/>
                        </c:if>
                    </c:forEach>
                </c:if>      
                </table>

                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                    <tr>
                        <td width="100%" colspan="8" height="5px"><hr/></td>
                    </tr>
                    <tr>
                        <td width="3%">&nbsp;</td>
                        <td width="15%">&nbsp;</td>
                        <td width="25%">&nbsp;</td>
                        <td width="15%">&nbsp;</td>
                        <td width="8%">&nbsp;</td>
                        <td width="15%" style="text-align:center;">Total :</td>
                        <td width="8%" style="text-align:center;font-weight: bold;">&nbsp;<c:out value="${eachGpf.releaseTot}"/></td>
                        <td width="15%">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="8" height="5px"><hr/></td>
                    </tr>
                    <tr>
                        <td width="50%" colspan="4" style="text-align:right;">&nbsp;</td>
                        <td width="50%" colspan="4" style="text-align:center;">&nbsp;(RUPEES &nbsp; <c:out value="${eachGpf.releaseTotFig}"/>) ONLY</td>
                    </tr>
                </table> 

                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                    <tr style="height:30px">
                        <td>&nbsp;</td>
                    </tr>
                    <tr align="right">
                        <td width="70%" style="text-align: right;" class="txtf">&nbsp</td>  
                        <td width="30%" style="text-align: center;" class="txtf">
                            Signature of the D.D.O. with Seal <br>
                            <c:out value="${GPFHeader.ddoDesg}"/>, <c:out value="${GPFHeader.officeName}"/>
                            <br>
                            Date:
                        </td>
                    </tr>
                </table>  

                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                    <thead></thead>
                    <tr width="100%">
                        <td width="100%" colspan="2">Certified that all particulars of recovery have been correctly furnished as 
                            per the instruction issued in respect of preparation of G.P.F. Schedules.
                        </td>
                    </tr>
                    <tr width="100%">
                        <td width="70%" style="text-align:left;" class="txtf">
                            Voucher No....................................
                        </td>
                        <td width="30%" style="text-align:left;" class="txtf">
                            Date of Encashment: / /
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr width="100%" >
                        <td colspan="2" style="text-align: center;font-weight: bold;" class="txtf">
                            FOR USE IN AUDIT OFFICE
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">Certified that the name and account No. of individual deduction and total shown in column - 6
                            have been checked with ref. to the bill vide page 224 of the Audit Manual.
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">Certified that the rates of pay shown in column - 4 have been verified with amount drawn in this bill.</td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="text-align:right;font-weight: bold;" colspan="2">AUDITOR</td>
                    </tr>
                    <tr style="height:50px">
                        <td colspan="2">
                            &nbsp;
                        </td>
                    </tr>
                </table> 
                
                <table width="100%" border="0" style="left: 18px;" cellpadding="0" cellspacing="0" style="font-size:11px;">
                    <tr>
                         <td colspan="3" height="5px">
                            <hr/>
                         </td>
                    </tr>
                    <tr>
                        <td class="printData">&nbsp;</td>
                        <td class="printFooter" style="text-align:center">&nbsp;</td>
                        <td class="printData" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%></td>
                    </tr>
                </table>
                            
                <c:out value="${eachGpf.pagebreakparent}" escapeXml="false"/>
                </table>
                    
            </c:forEach>
        </c:if>


        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
            <thead> </thead>
            <tr style="height:15px"><td>&nbsp;</td></tr>
        </table>

        <c:if test="${not empty GPFTypeList}">
            <div style="width:90%;margin: 0 auto;">
                <table width="100%" border="0">
                    <tr>
                        <td width="100%" style="text-align:center" class="printData">
                            <b>GPF ABSTRACT</b>
                        </td>
                    </tr>
                    <tr>
                        <td width="100%" style="text-align:center" class="printData">
                            <b>BILL NO:</b> <c:out value="${BillDesc}"/>
                        </td>
                    </tr>
                    <tr style="height:40px;text-align:center" class="printData">
                        <td><b>T.V No / Date: </b><c:out value="${VchNo}"/> / <c:out value="${VchDate}"/></td> 
                    </tr>
                </table>
            </div>

            <div style="width:99%;margin: 0 auto;font-family:verdana;">
                <table border="1" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                    <tr class="tblHeader" height="45px">
                        <td width="50%" style="text-align:center;border:1px solid;" class="printData">PF CODE</td>
                        <td width="50%" style="text-align:center;border:1px solid;" class="printData">TOTAL AMOUNT</td>
                    </tr>
                    <c:forEach var="eachGpfAbst" items="${GPFAbstractList}">
                        <tr style="height:30px">
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachGpfAbst.gpfType}"/></td>
                            <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachGpfAbst.totalAmount}"/>&nbsp;</td>
                        </tr>
                    </c:forEach>
                </table>

                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                    <tr>
                        <td width="100%" colspan="3" height="5px"><hr/></td>
                    </tr>
                    <tr>
                        <td width="50%" style="text-align:left;">&nbsp;</td>
                        <td width="23%" style="text-align:center;">Total</td>
                        <td width="27%" style="text-align:left;font-weight: bold;">&nbsp;<c:out value="${TotAmt}"/>&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="3" height="5px"><hr/></td>
                    </tr>
                </table>    
                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                    <tr>
                        <td width="50%" style="text-align:right;">&nbsp;</td>
                        <td colspan="2" style="text-align:right;">(RUPEES &nbsp; <c:out value="${TotFig}"/>) ONLY</td>
                    </tr>
                </table>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                    <tr style="height:30px">
                        <td>&nbsp;</td>
                    </tr>
                    <tr align="right">
                        <td width="70%" style="text-align: right;" class="txtf">&nbsp</td>  
                        <td width="30%" style="text-align: center;" class="txtf">
                            Signature of the D.D.O. with Seal <br>
                            <c:out value="${GPFHeader.ddoDesg}"/>, <c:out value="${GPFHeader.officeName}"/>
                            <br>
                            Date:
                        </td>
                    </tr>
                </table>  

                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                    <thead></thead>
                    <tr width="100%">
                        <td width="100%" colspan="2">Certified that all particulars of recovery have been correctly furnished as 
                            per the instruction issued in respect of preparation of G.P.F. Schedules.
                        </td>
                    </tr>
                    <tr width="100%">
                        <td width="70%" style="text-align:left;" class="txtf">
                            Voucher No....................................
                        </td>
                        <td width="30%" style="text-align:left;" class="txtf">
                            Date of Encashment: / /
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr width="100%" >
                        <td colspan="2" style="text-align: center;font-weight: bold;" class="txtf">
                            FOR USE IN AUDIT OFFICE
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">Certified that the name and account No. of individual deduction and total shown in column - 6
                            have been checked with ref. to the bill vide page 224 of the Audit Manual.
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">Certified that the rates of pay shown in column - 4 have been verified with amount drawn in this bill.</td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td style="text-align:right;font-weight: bold;" colspan="2">AUDITOR</td>
                    </tr>
                    <tr style="height:50px">
                        <td colspan="2" >
                            &nbsp;
                        </td>
                    </tr>
                </table>   
            </div>
        </c:if> 

    </body>
</html>
