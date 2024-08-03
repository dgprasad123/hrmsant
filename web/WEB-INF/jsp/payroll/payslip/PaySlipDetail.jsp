<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
 <%
 int x=1;
 int y=1;
 int z = 1;
 %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pay Slip Detail</title>
    </head>
    <body>
        <div align="center" style="font-family: Verdana;font-size:16px;">
            PAY SLIP<br />
            (&nbsp;<c:out value="${empdata.offName}"/>)<br/>
            <span style="font-family: Verdana;font-size:14px;font-weight: bold;"><c:out value="${empdata.typeOfEmp}"/></span>
        </div>
         <div align="right" style="font-family: Verdana;font-size:10px;">
                       
        </div>
        <div align="center">
			<br />
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td colspan="6">
                        Name , Designation and DOJ of the Incumbent:&nbsp;
                        <c:out value="${empdata.empName}"/>,&nbsp;<c:out value="${empdata.curDesig}"/>,&nbsp;<c:out value="${empdata.doj}"/>
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="6">
                        DDO NAME:&nbsp;
                        <c:out value="${empdata.ddoName}"/>
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade"/>
                    </td>
                </tr>
                <tr>
                    <td width="15%">GPF/PRAN Ac No :</td>
                    <td width="17%"><c:out value="${empdata.gpfno}"/></td>
                    <td width="18%">For the month of :</td>
                    <td width="15%"><c:out value="${empdata.forMonth}"/>-<c:out value="${empdata.forYear}"/></td>
                    <td width="17%">Bank :</td>
                    <td width="17%"><c:out value="${empdata.bank}"/></td>
                </tr>
                <tr>
                    <td>Scale of Pay :</td>
                    <td>
                        <c:out value="${empdata.scalePay}"/>
                    </td>
                    <td>No of days worked :</td>
                    <td>
                        <c:out value="${empdata.daysWork}"/>
                    </td>
                    <td>Bank A/c No :</td>
                    <td>
                        <c:out value="${empdata.bankAcno}"/>
                    </td>
                </tr>
                <tr>
                    <td>Current Basic :</td>
                    <td>
                        <c:out value="${empdata.empBasic}"/>
                    </td>
                    <td>Pay of the Month :</td>
                    <td>
                        <c:out value="${empdata.curBasic}"/>
                    </td>
                    <td>Bill No :</td>
                    <td>
                        <c:out value="${empdata.billNo}"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        TV No:
                    </td>
                    <td>
                        <c:out value="${empdata.vchno}"/>
                    </td>
                    <td>
                        TV Date:
                    </td>
                    <td>
                        <c:out value="${empdata.vchdate}"/>
                    </td>
                    <td>
                        Bill Date:
                    </td>
                    <td>
                        <c:out value="${empdata.billdate}"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        Bill Gross/Net:
                    </td>
                    <td>
                        <c:out value="${gross}"/> / <c:out value="${netded}"/>
                    </td>
                    <td>
                        PAN No:
                    </td>
                    <td>
                        <c:out value="${empdata.panno}"/>
                    </td>
                    <td>
                        DA Rate:
                    </td>                    
                    <td>
                        <c:out value="${empdata.darate}"/>
                    </td>
                </tr>
            </table>
        </div>

        <div align="center">
            <table width="100%" border="0" cellspacing="0" style="font-size:11px; font-family:verdana;">
                <tr>
                    <td width="32%" style="font-size:11px; font-family:verdana;" valign="top">
                        <table width="100%" border="0" cellspacing="0" style="font-size:12px; font-family:verdana;border: 1px dashed #000000;" >
                            <tr style="height:30px">
                                <td align="center" width="13%">SL</td>
                                <td width="60%">Allowances</td>
                                <td align="right" width="30%">Amount</td>
                            </tr>
                        </table>
                        <table width="100%" border="0" style="font-size:11px; font-family:verdana;">
                            <c:if test="${not empty allowancelist}">
                                <c:forEach var="allow" items="${allowancelist}">
                                    <tr>
                                        <td width="13%" align="center"> <%=x++%> </td>
                                        <td width="60%">
                                            <c:out value="${allow.adCode}"/>
                                        </td>
                                        <td width="30%" align="right">
                                            <c:out value="${allow.adAmt}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </table>
                    </td>
                    <td width="2%">
                        &nbsp;
                    </td>
                    <td width="32%" valign="top">
                        <table width="100%" border="0" cellspacing="0" style="font-size:12px; font-family:verdana;border: 1px dashed #000000;" >
                            <tr style="height:30px">
                                <td align="center" width="13%">SL</td>
                                <td width="60%">Deductions</td>
                                <td align="right" width="30%">Amount</td>
                            </tr>
                        </table>
                        <table width="100%" border="0" style="font-size:11px; font-family:verdana;">
                            <c:if test="${not empty deductionlist}">
                                <c:forEach var="deduct" items="${deductionlist}">
                                    <tr>
                                        <td width="13%" align="center"><%=y++%></td>
                                        <td width="60%">
                                            <c:out value="${deduct.adCode}"/>
                                        </td>
                                        <td width="30%" align="right">
                                            <c:out value="${deduct.adAmt}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${not empty loanList}">
                                <c:forEach var="loan" items="${loanList}">
                                    <tr>
                                        <td width="13%" align="center"><%=y++%></td>
                                        <td width="60%">
                                            <c:out value="${loan.adCode}"/>
                                        </td>
                                        <td width="30%" align="right">
                                            <c:out value="${loan.adDesc}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </table>
                    </td>
                    <td width="2%">
                        &nbsp;
                    </td>
                    <td width="32%" valign="top">
                        <table width="100%" border="0" cellspacing="0" style="font-size:12px; font-family:verdana;border: 1px dashed #000000;" >
                            <tr style="height:30px">
                                <td align="center" width="13%">SL</td>
                                <td width="60%">Pvt Deductions</td>
                                <td align="right" width="30%">Amount</td>
                            </tr>
                        </table>
                        <table width="100%" border="0" style="font-size:11px; font-family:verdana;">
                            <c:if test="${not empty privateDeductionlist}">
                                <c:forEach var="pvtdeduct" items="${privateDeductionlist}">
                                    <tr>
                                        <td width="13%" align="center"><%=z++%></td>
                                        <td width="60%">
                                            <c:out value="${pvtdeduct.adCode}"/>
                                        </td>
                                        <td width="30%" align="right">
                                            <c:out value="${pvtdeduct.adDesc}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                        </table>
                    </td>
                </tr>
            </table>
            <table width="100%" border="0" cellspacing="0" cellpadding="1" style="font-size:12px; font-family:verdana;">
                <tr style="height:50px">
                    <td width="17%">
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade"/>
                        Total Allowance
                    </td>
                    <td align="right" width="16%">
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade" align="left"/>
                        <c:out value="${totalAllowance}"/>
                    </td>
                    <td width="17%">
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade" width="92%" align="right"/>
                        &nbsp;&nbsp;
                        Total Deduction
                    </td>
                    <td align="right" width="16%">
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade" />
                        <c:out value="${totalDeduction}"/>
                    </td>
                    <td width="18%">
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade" width="92%" align="right" />
                        &nbsp;&nbsp;
                        Private Deduction
                    </td>
                    <td align="right" width="16%">
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade"/>
                        <c:out value="${totalPrivateDeduction}"/>
                    </td>
                </tr>
                <tr style="height:30px">
                    <td>
                        Gross Amount  <hr style="border: 2px dashed #000000;" size="1" noshade="noshade"/>
                    </td>
                    <td align="right">
                        <c:out value="${gross}"/>
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade" align="left"/>
                    </td>
                    <td>
                        &nbsp;
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade"  width="92%" align="right"/>
                    </td>
                    <td align="right">
                        &nbsp;
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade" />
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;Net amount <hr style="border: 2px dashed #000000;" size="1" noshade="noshade" width="92%" align="right"/>
                    </td>
                    <td align="right">
                        <c:out value="${netded}"/>
                        <hr style="border: 2px dashed #000000;" size="1" noshade="noshade" />
                    </td>
                </tr>
                <tr style="height:30px">
                    <td colspan="6" align="right" style="text-transform:uppercase">
                        <i>NET AMOUNT RUPEES&nbsp;<c:out value="${netAmtWords}"/> ONLY</i>
                    </td>
                </tr>
                <tr style="height:90px">
                    <td colspan="2">
                        <img id="qrCode" src="https://api.qrserver.com/v1/create-qr-code/?data=HRMS Id=${empdata.empCode},Employee Name=${empdata.empName},Gross Pay=${gross},Net Pay=${netded}&amp;size=50x50" id="QRImage" height="150px" width="140px" border="2"/>
                    </td>
                    <td colspan="4" align="center" valign="bottom">
                        Signature of D.D.O.<p>Date :</p>
                    </td>
                </tr>
                <tr>
                    <td colspan="6" valign="bottom">
                        N.B : This Payslip is computer generated and does not require the DDO's signature or the stamp in order to be considered valid.<br />
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>
