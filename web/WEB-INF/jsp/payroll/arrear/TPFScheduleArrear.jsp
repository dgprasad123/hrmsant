<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    int pageNo = 1;
    
    String totalAbsractAmt = "";
    String amtwordsAbsract = "";
    String totalAbsractAmt1 = "";
    String amtwordsAbsract1 = "";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: Human Resources Management System, Government of Odisha ::</title>
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
        <c:out value="${GPFHeader.pageHeaderTable}" escapeXml="false"/>
        <c:if test="${not empty GPFTypeList}">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                <tr style="height:30px">
                    <th align="center" style="border-bottom:1px solid #000000;">SL No</th>
                    <th align="left" style="border-bottom:1px solid #000000;">&nbsp;
                        GPF No
                    </th>
                    <th align="left" style="border-bottom:1px solid #000000;">&nbsp;
                        Name / Designation
                    </th>
                    <th align="center" style="border-bottom:1px solid #000000;">
                        Basic / GP / Pay Scale
                    </th>
                    <th align="center" style="border-bottom:1px solid #000000;">Total Amount</th>
                    <th align="center" style="border-bottom:1px solid #000000;">&nbsp;
                        Date of Birth / Date of Superannuation
                    </th>
                </tr>
                <c:forEach var="eachGpf" items="${GPFTypeList}">  
                    <tr style="height:30px">
                        <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachGpf.slno}"/></td>
                        <td align="left" style="border-bottom:1px solid #000000;">&nbsp;
                            <c:out value="${eachGpf.accountNo}"/>
                        </td>
                        <td align="left" style="border-bottom:1px solid #000000;">&nbsp;
                            <c:out value="${eachGpf.empName}"/> / <c:out value="${eachGpf.designation}"/>
                        </td>
                        <td align="center" style="border-bottom:1px solid #000000;">
                            <c:out value="${eachGpf.basicPay}"/> / <c:out value="${eachGpf.gradePay}"/> / <c:out value="${eachGpfType.scaleOfPay}"/>
                        </td>

                        <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachGpf.totalReleased}"/></td>
                        <td align="center" style="border-bottom:1px solid #000000;">&nbsp;
                            <c:out value="${eachGpf.dob}"/> / <c:out value="${eachGpf.dor}"/>
                        </td>
                    </tr>
                    <c:if test="${not empty eachGpf.pagebreakchild}">
                        </table>
                        <c:out value="${eachGpf.carryForward}" escapeXml="false"/>
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
                        <c:out value="${eachGpf.pagebreakchild}" escapeXml="false"/>
                        <c:out value="${GPFHeader.pageHeaderTable}" escapeXml="false"/>
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                            <tr style="height:30px">
                                <th align="center" style="border-bottom:1px solid #000000;">SL No</th>
                                <th align="left" style="border-bottom:1px solid #000000;">&nbsp;
                                    GPF No
                                </th>
                                <th align="left" style="border-bottom:1px solid #000000;">&nbsp;
                                    Name / Designation
                                </th>
                                <th align="center" style="border-bottom:1px solid #000000;">
                                    Basic / GP / Pay Scale
                                </th>
                                <th align="center" style="border-bottom:1px solid #000000;">Total Amount</th>
                                <th align="center" style="border-bottom:1px solid #000000;">&nbsp;
                                    Date of Birth / Date of Superannuation
                                </th>
                            </tr>
                        <c:out value="${eachGpf.broughtForward}" escapeXml="false"/>
                    </c:if>
                </c:forEach>
            </table>
        </c:if>
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
                <td width="8%" style="text-align:center;font-weight: bold;">&nbsp;<c:out value="${TotAmt}"/></td>
                <td width="15%">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="8" height="5px"><hr/></td>
            </tr>
            <tr>
                <td width="50%" colspan="4" style="text-align:right;">&nbsp;</td>
                <td width="50%" colspan="4" style="text-align:center;">&nbsp;(RUPEES &nbsp; <c:out value="${TotFig}"/>) ONLY</td>
            </tr>
        </table> 

        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
            <tr style="height:30px">
                <td>&nbsp;</td>
            </tr>
            <tr align="right">
                <td width="70%" style="text-align: right;" class="txtf">&nbsp</td>  
                <td width="30%" style="text-align: center;" class="txtf">
                    Signature of the D.D.O. with Seal <br />
                    <c:out value="${GPFHeader.ddoDesg}"/>, <c:out value="${GPFHeader.officeName}"/>
                    <br />
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
    <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
        <thead> </thead>
        <tr style="height:15px"><td>&nbsp;</td></tr>
    </table>
            <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>

        <c:if test="${not empty GPFAbstractList}">
            <c:forEach var="GpfTypeBean" items="${GPFAbstractList}">
                <c:if test="${not empty GpfTypeBean.carryForward}">
                    <c:set var="totAbsAmt" value="${GpfTypeBean.carryForward}"/>
                    <%
                        totalAbsractAmt = (String) pageContext.getAttribute("totAbsAmt");
                    %>
                </c:if>
                <c:if test="${not empty GpfTypeBean.amountInWords}">
                    <c:set var="totAbsWords" value="${GpfTypeBean.amountInWords}"/>
                    <%
                        amtwordsAbsract = (String) pageContext.getAttribute("totAbsWords");
                    %>
                </c:if>
                <c:if test="${not empty GpfTypeBean.carryForward1}">
                    <c:set var="totAbsAmt1" value="${GpfTypeBean.carryForward1}"/>
                    <%
                        totalAbsractAmt1 = (String) pageContext.getAttribute("totAbsAmt1");
                    %>
                </c:if>
                <c:if test="${not empty GpfTypeBean.amountInWords1}">
                    <c:set var="totAbsWords1" value="${GpfTypeBean.amountInWords1}"/>
                    <%
                        amtwordsAbsract1 = (String) pageContext.getAttribute("totAbsWords1");
                    %>
                </c:if>
            </c:forEach>
        </c:if>

        <%----------------------------------------------CHALLAN PAGE---------------------------------------%>
        <br /><br />
        <table width="80%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;" align="center">
            <tr>
                <td align="center" colspan="2" style="font-size:15px;"> Under Rs <%=totalAbsractAmt1%>(RUPEES <%=amtwordsAbsract1%>) ONLY </td>
            </tr>
            <tr style="height:30px;">
                <td style="font:italic bold 12px verdana;"> Schedule LIII - Form No. 186 </td>
                <td align="right" style="font:italic bold 12px verdana;"> ORIGINAL/DUPLICATE/TRIPLICATE </td>
            </tr>
            <tr style="height:30px;">
                <td><span style="margin-left:40px;font-style:italic;">(See S.R.s 52)</span></td>
                <td>CHALLAN NO.</td>
            </tr>
            <tr style="height:30px;">
                <td style="font-style:italic;" colspan="2"><span style="margin-left:40px;">(O.T.C-06)Challan of Cash paid into the Treasury/Sub-Treasury at <c:out value="${eachEMP.offLocation}"/></span></td>
            </tr>
            <tr style="height:30px;">
                <td style="margin-left:30px;font-style:italic;" colspan="2"><span style="margin-left:40px;">State/Reserve Bank of India</span></td>
            </tr>
        </table>

        <table width="95%" border="1" cellpadding="0" cellspacing="0" style="font-size:11px;" align="center">
            <tr style="height:40px;">
                <td style="border:1px solid black;" align="center" colspan="4"> To be filled in by the remitter </td>
                <td style="border:1px solid black;" align="center" colspan="2"> To be filled by the Departmental Officer of the Treasury </td>
            </tr>
            <tr>
                <td width="7%" style="border:1px solid black;" align="center"> By Whom tendered </td>
                <td width="20%" style="border:1px solid black;" align="center"> Name [Designation] and address of the person <br> on whose behalf money is paid </td>
                <td width="20%" style="border:1px solid black;" align="center"> Full particulars of the remittance and of authority [if any] </td>
                <td width="10%" style="border:1px solid black;" align="center"> &nbsp;&nbsp;&nbsp;&nbsp;Amount&nbsp;&nbsp;&nbsp;&nbsp; </td>
                <td width="15%" style="border:1px solid black;" align="center"> Head of account </td>
                <td width="15%" style="border:1px solid black;" align="center"> Order of the Bank </td>
            </tr>
            <tr style="height:180px;">
                <td style="border:1px solid black;" valign="middle">
                    <span style="display:block;-webkit-transform: rotate(-90deg);-moz-transform: rotate(-90deg);-ms-transform: rotate(-90deg);-o-transform: rotate(-90deg);transform: rotate(-90deg);">
                        Name:
                        <br /><br /><br />
                        Signature:
                    </span>
                </td>
                <td style="border:1px solid black;" align="center"> <c:out value="${offname}"/> </td>
                <td style="border:1px solid black;">
                    Monthly Subscription of TPF for the month of&nbsp;<c:out value="${billMonth}"/> of <c:out value="${TOTALNOOFEMP}"/> 
                    no of employees in Bill No - <b><c:out value="${billDesc}"/></b><br /><br /> Total
                </td>
                <td style="border:1px solid black;" align="center">
                    Rs ${TotAmt}<br /><br /><br />
                    Rs ${TotAmt}
                </td>
                <td style="border:1px solid black;" align="center">
                    <span style="display:block;-webkit-transform: rotate(-90deg);-moz-transform: rotate(-90deg);-ms-transform: rotate(-90deg);-o-transform: rotate(-90deg);transform: rotate(-90deg);">
                        8009 State Provident Fund<br>& Other Provident Fund<br>Misc Provident Fund of the <br> employees of the Aided <br> Educational Institutions.
                    </span>
                </td>
                <td style="border:1px solid black;" align="center">
                    <span style="display:block;-webkit-transform: rotate(-90deg);-moz-transform: rotate(-90deg);-ms-transform: rotate(-90deg);-o-transform: rotate(-90deg);transform: rotate(-90deg);">
                        Signature and full<br>designation of the officer<br />ordering the money to be paid in.<br><br><br>Date-
                    </span>
                </td>
            </tr>
            <tr style="height:80px;">
                <td colspan="6" style="border:1px solid black;" align="center">
                    1) To be used only in the case of remittances to Bank through an Officer of the Government<br />
                    (in words) Rs ${TotAmt}/- (RUPEES <b>${TotFig}</b>) ONLY<br />Rs ${TotAmt}/- (RUPEES <b>${TotFig}</b>) ONLY by T.C to 8009 GPF of AEI
                </td>
            </tr>
        </table>
        <table width="80%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;" align="center">
            <tr style="height:80px;">
                <td align="center"> Received payment<br />Treasurer </td>
                <td align="center"> Accountant </td>
                <td align="center"> Date..............................................Treasury Officer/Agent<br/>(See Instructions on overleaf) </td>
            </tr>
        </table>
    </body>
</html>
