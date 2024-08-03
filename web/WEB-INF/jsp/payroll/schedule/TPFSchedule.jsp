<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    int i = 0;
    int pageNo = 1;
    int j = 0;
    int l = 0;
    boolean gpfType1 = false;
    String billno = "";
    String total = "";
    String amtwords = "";
    String totalAbsractAmt = "";
    String amtwordsAbsract = "";
    String totalAbsractAmt1 = "";
    String amtwordsAbsract1 = "";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
    </head>
    <body>
        <c:if test="${not empty billNo}">
            <c:set var="billno1" value="${billNo}"/>
            <% 
                billno = (String)pageContext.getAttribute("billno1"); 
            %> 
        </c:if>
        
        <c:if test="${not empty TPFList}">
            <c:forEach var="eachGPFType" items="${TPFList}">
                <c:if test="${eachGPFType.empno > 0}">
                    <% 
                        i = 0;
                        gpfType1 = true;
                        j = 0; 
                    %>
                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                    <tr>
                        <td width="100%" style="text-align:center;" class="txtf"><b>TEACHER PROVIDENT FUND</b></td>   
                    </tr>
                    <tr>
                        <td width="100%" style="text-align:center;" class="txtf">
                            <b>BILL NO:</b> <c:out value="${billDesc}"/>
                        </td> 
                    </tr>
                    <tr>
                        <td width="100%" style="text-align:center;" class="txtf">SCHEDULE OF 
                             <c:if test="${not empty eachGPFType.gpfType}">
                                 <b><c:out value="${eachGPFType.gpfType}"/></b>
                             </c:if> 
                        </td>   
                    </tr>
                    <tr>
                        <td style="text-align:center;">Demand No-"8009- State G.P.F Withdrawals"</td>
                    </tr>
                </table>

                <table id="displaylink" width="100%" border="0" class="donotPrintPaging">
                    <tr>
                        
                    </tr>
                </table>

                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                    <tr>
                        <td style="text-align:left;">1. This form should not be used for transactions of Teacher Provident Fund for which Form No. O.T.C. 63 has been provided. The
                            account Nos. should be arranged in serial order.
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align:left;">2. Col. 1 quote account number unfailingly. The guide letters e.g. I.C.S.(ICS Provident Fund) etc., should be
                            unvariably, prefixed to account numbers.
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align:left;">3. In the remark column give reasons for discontinuance of subscription such as 'Proceeded on leave', 'Transfered to
                            ........................... office ................... District ....................', 'Quitted service', 'Died or Discontinued under Rule II'.
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align:left;">4. In the remark column write description against every new name such as 'New Subscriber', 'came on transfer from
                            ........................... office ................... District ..................... resumed subscription'.
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align:left;">5. Separate Schedule should be prepared in respect of person whose accounts are kept by different Accountant-General.
                        </td>
                    </tr>
                    <tr>
                        <td>
                            &nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align:center;"><c:out value="${offName}"/> </br>DEDUCTION MADE FROM THE SALARY FOR <b><c:out value="${billMonth}"/></b> <b><c:out value="${billYear}"/></b>
                    </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                </table>

                <c:if test="${not empty eachGPFType.empGpfList}">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                        <thead></thead>        
                        <tr>
                            <td width="5%" style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">Sl No</td>
                            <td width="15%" style="text-align:left;border-bottom:1px solid #000000;font-weight:bold" class="txtf">ACCOUNT NO./<br/>DATE OF ENTRY <br/>INTO GOVT. SER.</td>
                            <td width="25%" style="text-align:left;border-bottom:1px solid #000000;font-weight:bold" class="txtf">NAME OF THE SUBSCRIBER/<br/>DESIGNATION</td>
                            <td width="10%" style="text-align:left;border-bottom:1px solid #000000;font-weight:bold" class="txtf">BASIC PAY/ <br/>SCALE OF PAY</td>
                            <td width="10%" style="text-align:left;border-bottom:1px solid #000000;font-weight:bold" class="txtf">MONTHLY SUBSCRIPTION</td>
                            <td width="8%"  style="text-align:left;border-bottom:1px solid #000000;font-weight:bold" class="txtf">REFUND OF WITHDRAWALS AMT/NO. OF INST.</td>
                            <td width="8%"  style="text-align:left;border-bottom:1px solid #000000;font-weight:bold" class="txtf">TOTAL RELEASED</td>
                            <td width="15%" style="text-align:left;border-bottom:1px solid #000000;font-weight:bold" class="txtf">REMARKS <br/>D.O.B and D.O.R.</td>
                        </tr>
                        <tr>
                            <td style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">1</td>
                            <td style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">2</td>
                            <td style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">3</td>
                            <td style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">4</td>
                            <td style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">5</td>
                            <td style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">6</td>
                            <td style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">7</td>
                            <td style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">8</td>
                        </tr>
                    </table>

                    <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                        <c:forEach var="eachEMP" items="${eachGPFType.empGpfList}">
                            <% i++;
                                j++;
                                l++;%>
                            <tr style="height:45px">
                                <td width="5%" style="text-align:center;border-bottom:1px solid #000000;" class="txtf">
                                    <%=i%>
                                </td>
                                <td width="15%" style="text-align:left;border-bottom:1px solid #000000;" class="txtf">
                                    <c:out value="${eachEMP.accNo}"/> / <c:out value="${eachEMP.dateOfEntry}"/>
                                </td>
                                <td width="25%" style="text-align:left;border-bottom:1px solid #000000;" class="txtf">
                                    <c:out value="${eachEMP.name}"/> / <c:out value="${eachEMP.designation}"/>
                                </td>
                                <td width="14%" style="text-align:left;border-bottom:1px solid #000000;" class="txtf">
                                    <c:out value="${eachEMP.basicPay}"/><br><c:out value="${eachEMP.scaleOfPay}"/>
                                </td>
                                <td width="10%" style="text-align:left;border-bottom:1px solid #000000;" class="txtf">
                                    <c:out value="${eachEMP.monthlySub}"/>
                                </td>
                                <td width="8%" style="text-align:left;border-bottom:1px solid #000000;" class="txtf">
                                    <c:out value="${eachEMP.towardsLoan}"/>
                                    <c:if test="${not empty eachEMP.noOfInst}">
                                        (<c:out value="${eachEMP.noOfInst}"/>)
                                    </c:if>
                                </td>
                                <td width="8%" align="left" style="border-bottom:1px solid #000000;" class="txtf">
                                    <c:out value="${eachEMP.totalReleased}"/>
                                </td>
                                <td width="15%" style="text-align:left;border-bottom:1px solid #000000;" class="txtf">&nbsp;
                                    <c:out value="${eachEMP.dob}"/><br><c:out value="${eachEMP.dor}"/>
                                </td>
                            </tr>
                            <% if (j == 14 && gpfType1 == true) {
                                    gpfType1 = false;
                                    l = 0;%>
                    </table>

                    <table width="100%" border="0" id="tblcarryforward" style="left: 18px;" cellpadding="0" cellspacing="0" style="font-size:11px;">
                        <tr style="height:30px">
                            <td colspan="8" style="text-align:right;" class="txtf">
                                Carry Forward  <c:out value="${eachEMP.carryForward}"/>
                            </td>
                        </tr>
                    </table>

                    <table width="100%" border="0"  style="left: 18px;" cellpadding="0" cellspacing="0" style="font-size:11px;">
                        <thead> </thead>
                        <tr>
                            <td colspan="8" height="5px">
                                <hr/>
                            </td>
                        </tr>
                        <tr>
                            <td class="printData">&nbsp;</td>
                            <td class="printFooter" style="text-align:center">&nbsp;</td>
                            <td class="printData" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
                        </tr>
                    </table>
                    <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>


                    <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                        <thead></thead>
                        <tr>
                            <td width="5%" style="text-align:center;border-bottom:1px solid;" class="txtf">1</td>
                            <td width="15%" style="text-align:center;border-bottom:1px solid;" class="txtf">2</td>
                            <td width="25%" style="text-align:center;border-bottom:1px solid;" class="txtf">3</td>
                            <td width="10%" style="text-align:center;border-bottom:1px solid;" class="txtf">4</td>
                            <td width="10%" style="text-align:center;border-bottom:1px solid;" class="txtf">5</td>
                            <td width="8%" style="text-align:center;border-bottom:1px solid;" class="txtf">6</td>
                            <td width="8%" style="text-align:center;border-bottom:1px solid;" class="txtf">7</td>
                            <td width="15%" style="text-align:center;border-bottom:1px solid;" class="txtf">8</td>
                        </tr>
                        <tr>
                            <td style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">Sl No</td>
                            <td style="text-align:left;border-bottom:1px solid;" class="txtf">ACCOUNT NO./<br/>DATE OF ENTRY <br/>INTO GOVT. SER.</td>
                            <td style="text-align:left;border-bottom:1px solid;" class="txtf">NAME OF THE SUBSCRIBER/<br/>DESIGNATION</td>
                            <td style="text-align:left;border:1px solid;" class="txtf">BASIC PAY/ <br/>SCALE OF PAY</td>
                            <td style="text-align:left;border:1px solid;" class="txtf">MONTHLY SUBSCRIPTION</td>
                            <td style="text-align:left;border:1px solid;" class="txtf">REFUND OF WITHDRAWALS AMT/NO. OF INST.</td>
                            <td style="text-align:left;border:1px solid;" class="txtf">TOTAL RELEASED</td>
                            <td style="text-align:left;border:1px solid;" class="txtf">REMARKS <br/>D.O.B and D.O.R.</td>
                        </tr>
                    </table>

                    <table id="tblbroughforward" width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                        <thead></thead>
                        <tr>
                            <td colspan="8" style="text-align:right;" class="txtf">
                                Brought Forward  <c:out value="${eachEMP.carryForward}"/>
                            </td>
                        </tr>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                            <% j = 0;
                            } else if (j == 17) {
                                l = 0;%>
                        </table>
                        <table width="100%" border="0" id="tblcarryforward" style="left: 18px;" style="font-size:11px;" cellpadding="0" cellspacing="0">
                            <tr style="height:30px">
                                <td colspan="8" style="text-align:right;" class="txtf">
                                    Carry Forward  <c:out value="${eachEMP.carryForward}"/>
                                </td>
                            </tr>
                        </table>
                        <table width="100%" border="0"  style="left: 18px;" cellpadding="0" cellspacing="0" style="font-size:11px;">
                            <thead> </thead>
                            <tr>
                                <td colspan="8" height="5px">
                                    <hr/>
                                </td>
                            </tr>
                            <tr>
                                <td class="printData">&nbsp;</td>
                                <td class="printFooter" style="text-align:center">&nbsp;</td>
                                <td class="printData" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
                            </tr>
                        </table>
                        <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>      

                        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;font-family:verdana">
                            <thead>
                                <tr>
                                    <td width="5%" style="text-align:center;border-bottom:1px solid;" class="txtf">1</td>
                                    <td width="15%" style="text-align:center;border-bottom:1px solid;" class="txtf">2</td>
                                    <td width="25%" style="text-align:center;border-bottom:1px solid;" class="txtf">3</td>
                                    <td width="14%" style="text-align:center;border-bottom:1px solid;" class="txtf">4</td>
                                    <td width="10%" style="text-align:center;border-bottom:1px solid;" class="txtf">5</td>
                                    <td width="8%" style="text-align:center;border-bottom:1px solid;" class="txtf">6</td>
                                    <td width="8%" style="text-align:center;border-bottom:1px solid;" class="txtf">7</td>
                                    <td width="15%" style="text-align:center;border-bottom:1px solid;" class="txtf">8</td>
                                </tr>
                            </thead>
                            <tr>
                                <td width="5%" style="text-align:center;border-bottom:1px solid #000000;font-weight:bold" class="txtf">Sl No</td>
                                <td width="15%" style="text-align:left;border:1px solid;" class="txtf">ACCOUNT NO./<br/>DATE OF ENTRY <br/>INTO GOVT. SER.</td>
                                <td width="25%" style="text-align:left;border:1px solid;" class="txtf">NAME OF THE SUBSCRIBER/<br/>DESIGNATION</td>
                                <td width="14%" style="text-align:left;border:1px solid;" class="txtf">BASIC PAY/ <br/>SCALE OF PAY</td>
                                <td width="10%" style="text-align:left;border:1px solid;" class="txtf">MONTHLY SUBSCRIPTION</td>
                                <td width="8%" style="text-align:left;border:1px solid;" class="txtf">REFUND OF WITHDRAWLS AMT/NO. OF INST.</td>
                                <td width="8%" style="text-align:left;border:1px solid;" class="txtf">TOTAL RELEASED</td>
                                <td width="15%" style="text-align:left;border:1px solid;" class="txtf">REMARKS <br/>D.O.B and D.O.R.</td>
                            </tr>
                        </table>
                        <table id="tblbroughforward" width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                            <thead></thead>
                            <tr>
                                <td colspan="8" style="text-align:right;" class="txtf">
                                    Brought Forward  <c:out value="${eachEMP.carryForward}"/>
                            </td>
                            </tr>
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                                <% j = 0;
                                    } %>
                                    <c:if test="${not empty eachEMP.carryForward}">
                                        <c:set var="total5" value="${eachEMP.carryForward}"/>
                                        <% 
                                            total = (String)pageContext.getAttribute("total5"); 
                                        %>
                                    </c:if>
                                    <c:if test="${not empty eachEMP.amountInWords}">
                                        <c:set var="amtwords5" value="${eachEMP.amountInWords}"/>
                                        <% 
                                            amtwords = (String)pageContext.getAttribute("amtwords5"); 
                                        %>
                                    </c:if>
                            </c:forEach>
                                <tr>
                                    <td colspan="8" height="5px">
                                        <hr/>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="5%" style="text-align:left;" class="txtf">&nbsp;</td>
                                    <td width="15%" style="text-align:left;" class="txtf">&nbsp;</td>
                                    <td width="22%" style="text-align:left;" class="txtf">&nbsp;</td>
                                    <td width="14%" style="text-align:left;" class="txtf">&nbsp;</td>
                                    <td width="13%" style="text-align:left;" class="txtf">&nbsp;</td>
                                    <td width="8%" style="text-align:left;" class="txtf"><b>Total </b></td>
                                    <td width="8%" style="text-align:left;" class="txtf"><b><%=total%></b></td> 
                                    <td width="20%" style="text-align:left;" class="txtf">&nbsp;</td>
                                </tr>
                                <tr>
                                    <td colspan="8" height="5px">
                                        <hr/>
                                    </td>
                                </tr>
                                    
                                <tr>
                                    <td colspan="8" style="text-align:right;" class="txtf">
                                        (RUPEES <%=amtwords%>) ONLY
                                    </td>
                                </tr>
                                <tr>
                                    <td>&nbsp;</td>
                                </tr>
                            </table>
                            <br>

                            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                                <tr align="right">
                                    <td colspan="2"  width="70%" style="text-align: right;" class="txtf">&nbsp</td>
                                    <td colspan="2"  width="30%" style="text-align: center;" class="txtf">Signature of the D.D.O. with Seal <br><c:out value="${ddodesg}"/>, <c:out value="${offname}"/><br>Date:</td>
                                </tr>
                            </table>

                            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
                                <thead></thead>
                                <tr width="100%">
                                    <td width="100%" colspan="2">Certified that all particulars of recovery have been correctly furnished as per the instruction issued in respect of preparation of G.P.F. Schedules.
                                    </td>
                                </tr>
                                <tr width="100%">
                                    <td width="70%" style="text-align:left;" class="txtf">
                                        Voucher No....................................
                                    </td>
                                    <td width="30%" style="text-align:left;" class="txtf">
                                        Date of Encashment: &nbsp;&nbsp;/&nbsp;&nbsp;&nbsp;/
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr width="100%" >
                                    <td colspan="2" style="text-align: center;" class="txtf">
                                        FOR USE IN AUDIT OFFICE
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">Certified that the name and account No. of individual deduction and total shown in column - 6 have been checked with ref. to the bill vide page 224 of the Audit Manual.
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">Certified that the rates of pay shown in column - 4 have been verified with amount drawn in this bill.
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        &nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td style="text-align:right;" colspan="2">AUDITOR</td>
                                </tr>

                            </table>

                            </c:if>
                            <%if (l <= 8 && gpfType1 == true) {
                                    if (pageNo == 1) {
                                        for (int ctr = 1; ctr <= 28 - (l * 3); ctr++) {

                            %>
                            <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;">
                                <thead> </thead>
                                <tr style="height:15px">
                                    <td colspan="8" >
                                        &nbsp;
                                    </td>
                                </tr>
                            </table>
                            <% }
                            } else {
                                for (int ctr = 1; ctr <= (28 - (l * 3)) - 3; ctr++) {%>
                                    <table width="100%" border="0"  cellpadding="0" cellspacing="0" style="font-size:12px;">
                                        <thead> </thead>
                                        <tr style="height:15px">
                                            <td colspan="8" >
                                                &nbsp;
                                            </td>
                                        </tr>
                                    </table>
                                    <%}
                            }%>    
                    <table width="100%" border="0"  style="left: 18px;" style="font-size:12px;">
                        <thead> </thead>
                        <tr>
                            <td colspan="8" height="5px">
                                <hr/>
                            </td>
                        </tr>
                        <tr>
                            <td class="printData">&nbsp;</td>
                            <td class="printFooter" style="text-align:center">&nbsp;</td>
                            <td class="printData" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
                        </tr>
                    </table>
                    <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                    <% l = 0;
                    } else {
                        for (int ctr = 1; ctr <= 40 - (l * 3); ctr++) {%>
                            <table width="100%" border="0"  cellpadding="0" cellspacing="0" style="font-size:12px;">
                                <thead> </thead>
                                <tr style="height:15px">
                                    <td colspan="8" >
                                        &nbsp;
                                    </td>
                                </tr>
                            </table>
                        <%}%>
                        <table width="100%" border="0"  style="left: 18px;" style="font-size:12px;">
                            <thead> </thead>
                            <tr>
                                <td colspan="8" height="5px">
                                    <hr/>
                                </td>
                            </tr>
                            <tr>
                                <td class="printData">&nbsp;</td>
                                <td class="printFooter" style="text-align:center">&nbsp;</td>
                                <td class="printData" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
                            </tr>
                        </table>
                        <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
                    <%}%>
                </c:if>
            </c:forEach>
        </c:if>
        <table width="100%" border="0"  cellpadding="0" cellspacing="0" style="font-size:12px;">
            <thead> </thead>
            <tr style="height:30px">
                <td colspan="8" >
                    &nbsp;
                </td>
            </tr>
            <tr>
                <td width="100%" style="text-align:center;" class="txtf"><b>TPF ABSTRACT</b></td>   
            </tr>
            <tr>
                <td width="100%" style="text-align:center;" class="txtf"><b>BILL NO:</b> <c:out value="${billNo}"/></td>   
            </tr>
            <tr style="height:50px">
                <td colspan="8" >
                    &nbsp;
                </td>
            </tr>
        </table>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
            <tr>
                <td width="50%" style="text-align:center;border:1px solid;" height="45px" class="txtf">PF CODE</td>
                <td width="50%" style="text-align:center;border:1px solid;" class="txtf">TOTAL AMOUNT </td>
            </tr>
            <c:if test="${not empty TPFAbstract}">
                <c:forEach var="GpfTypeBean" items="${TPFAbstract}">
                    <tr>
                        <td width="50%" style="text-align:center;" class="txtf" height="45px">
                            <c:out value="${GpfTypeBean.pfcode}"/>
                        </td>
                        <td width="50%" style="text-align:center;" class="txtf">
                            <c:out value="${GpfTypeBean.totalamt}"/>
                        </td>
                    </tr>
                    <c:if test="${not empty GpfTypeBean.carryForward}">
                        <c:set var="totAbsAmt" value="${GpfTypeBean.carryForward}"/>
                        <% 
                            totalAbsractAmt = (String)pageContext.getAttribute("totAbsAmt"); 
                        %>
                    </c:if>
                    <c:if test="${not empty GpfTypeBean.amountInWords}">
                        <c:set var="totAbsWords" value="${GpfTypeBean.amountInWords}"/>
                        <% 
                            amtwordsAbsract = (String)pageContext.getAttribute("totAbsWords"); 
                        %>
                    </c:if>
                    <c:if test="${not empty GpfTypeBean.carryForward1}">
                        <c:set var="totAbsAmt1" value="${GpfTypeBean.carryForward1}"/>
                        <% 
                            totalAbsractAmt1 = (String)pageContext.getAttribute("totAbsAmt1"); 
                        %>
                    </c:if>
                    <c:if test="${not empty GpfTypeBean.amountInWords1}">
                        <c:set var="totAbsWords1" value="${GpfTypeBean.amountInWords1}"/>
                        <% 
                            amtwordsAbsract1 = (String)pageContext.getAttribute("totAbsWords1"); 
                        %>
                    </c:if>
                </c:forEach>
            </c:if>
        </table>
        <table id="tblbroughforward" width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
            <thead></thead>
            <tr>
                <td colspan="8" style="text-align:right;" class="txtf">

                </td>
            </tr>
        </table>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
            <tr>
                <td colspan="8" height="5px">
                    <hr/>
                </td>
            </tr>
            <tr>
                <td width="15%" style="text-align:left;" class="txtf">&nbsp;</td>
                <td width="22%" style="text-align:left;" class="txtf">&nbsp;</td>
                <td width="14%" style="text-align:left;" class="txtf">&nbsp;</td>
                <td width="13%" style="text-align:left;" class="txtf">&nbsp;</td>
                <td width="8%" style="text-align:left;" class="txtf"><b>Total </b></td>
                <td width="8%" style="text-align:left;" class="txtf"><b><%=totalAbsractAmt%></b></td> 
                <td width="20%" style="text-align:left;" class="txtf">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="8" height="5px">
                    <hr/>
                </td>
            </tr>
            <tr>
                <td colspan="8" style="text-align:right;" class="txtf">
                    (RUPEES <%=amtwordsAbsract%> ) ONLY 
            </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
        </table>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
            <tr align="right">
                <td colspan="2"  width="70%" style="text-align: right;" class="txtf">&nbsp</td>
                <td colspan="2"  width="30%" style="text-align: center;" class="txtf">Signature of the D.D.O. with Seal <br><c:out value="${ddodesg}"/>, <c:out value="${offname}"/><br>Date:</td>
            </tr>
        </table>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;">
            <thead></thead>
            <tr width="100%">
                <td width="100%" colspan="2">Certified that all particulars of recovery have been correctly furnished as per the instruction issued in respect of preparation of G.P.F. Schedules.
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
                <td colspan="2">
                    &nbsp;
                </td>
            </tr>
            <tr width="100%" >
                <td colspan="2" style="text-align: center;" class="txtf">
                    FOR USE IN AUDIT OFFICE
                </td>
            </tr>
            <tr>
                <td colspan="2">Certified that the name and account No. of individual deduction and total shown in column - 6 have been checked with ref. to the bill vide page 224 of the Audit Manual.
                </td>
            </tr>
            <tr>
                <td colspan="2">Certified that the rates of pay shown in column - 4 have been verified with amount drawn in this bill.
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    &nbsp;
                </td>
            </tr>
            <tr>
                <td style="text-align:right;" colspan="2">AUDITOR</td>
            </tr>
            <tr style="height:50px">
                <td colspan="2" >
                    &nbsp;
                </td>
            </tr>
        </table>
        <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>

        <%----------------------------------------------CHALLAN PAGE---------------------------------------%>
        <br /><br />
        <table width="80%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;" align="center">
            <tr>
                <td align="center" colspan="2" style="font-size:15px;">
                    Under Rs <%=totalAbsractAmt1%>(RUPEES <%=amtwordsAbsract1%>) ONLY
            </td>
            </tr>
            <tr style="height:30px;">
                <td style="font:italic bold 12px verdana;">
                    Schedule LIII - Form No. 186
                </td>
                <td align="right" style="font:italic bold 12px verdana;">
                    ORIGINAL/DUPLICATE/TRIPLICATE
                </td>
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
        <table width="80%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;" align="center">
            <tr style="height:40px;">
                <td style="border:1px solid black;" align="center" colspan="4">
                    To be filled in by the remitter
                </td>
                <td style="border:1px solid black;" align="center" colspan="2">
                    To be filled by the Departmental Officer of the Treasury
                </td>
            </tr>
            <tr>
                <td style="border:1px solid black;" align="center">
                    By Whom tendered
                </td>
                <td style="border:1px solid black;" align="center">
                    Name [Designation] and address of the person on whose behalf money is paid
                </td>
                <td style="border:1px solid black;" align="center">
                    Full particulars of the remittance and of authority [if any]
                </td>
                <td style="border:1px solid black;" align="center">
                    &nbsp;&nbsp;&nbsp;&nbsp;Amount&nbsp;&nbsp;&nbsp;&nbsp;
                </td>
                <td style="border:1px solid black;" align="center">
                    Head of account
                </td>
                <td style="border:1px solid black;" align="center">
                    Order of the Bank
                </td>
            </tr>
            <tr style="height:180px;">
                <td style="border:1px solid black;" valign="middle">
                    <span style="display:block;-webkit-transform: rotate(-90deg);-moz-transform: rotate(-90deg);-ms-transform: rotate(-90deg);-o-transform: rotate(-90deg);transform: rotate(-90deg);">
                        Name:
                        <br /><br /><br />
                        Signature:
                    </span>
                </td>
                <td style="border:1px solid black;" align="center">
                    <c:out value="${offname}"/>
                </td>
                <td style="border:1px solid black;">
                    Monthly Subscription of TPF for the month of&nbsp;<c:out value="${billMonth}"/> of <c:out value="${TOTALNOOFEMP}"/> no of employees in Bill No - <c:out value="${billdesc}"/><br /><br /> Total
                </td>
                <td style="border:1px solid black;" align="center">
                    Rs <c:out value="${eachEMP.carryForward1}"/><br /><br /><br />
                    Rs <c:out value="${eachEMP.carryForward1}"/>
                </td>
                <td style="border:1px solid black;" align="center">
                    <span style="display:block;-webkit-transform: rotate(-90deg);-moz-transform: rotate(-90deg);-ms-transform: rotate(-90deg);-o-transform: rotate(-90deg);transform: rotate(-90deg);">
                        8009 State Provident Fund<br>& Other Provident Fund<br>Misc Provident Fund of the employees of the Aided<br>Educational Institutions.
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
                    (in words) Rs <c:out value="${eachEMP.carryForward}"/>/- (RUPEES <c:out value="${eachEMP.amountInWords}"/>) ONLY<br />Rs <c:out value="${eachEMP.carryForward}"/>/- (RUPEES <c:out value="${eachEMP.amountInWords}"/>) ONLY by T.C to 8009 GPF of AEI
            </td>
            </tr>
        </table>
        <table width="80%" border="0" cellpadding="0" cellspacing="0" style="font-size:11px;" align="center">
            <tr style="height:80px;">
                <td align="center">
                    Received payment<br />Treasurer
                </td>
                <td align="center">
                    Accountant
                </td>
                <td align="center">
                    Date..............................................Treasury Officer/Agent<br/>(See Instructions on overleaf)
                </td>
            </tr>
        </table>
</body>
</html>
