<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    int j = 0;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bill Front Page</title>
        <style type="text/css">

            .borderTop{
                border-top: 2px solid black;
            }
            .borderBottom{
                border-bottom: 2px solid black;
            }
            .borderTopandRight{
                border-top: 2px solid black;
                border-right: 2px solid black;
            }
            .borderTopandLeft{
                border-top: 2px solid black;
                border-left: 2px solid black;
            }
            .borderLeft{
                border-left: 2px solid black;
            }
            .borderRight{
                border-right: 2px solid black;
            }
            .borderTopRightandBottom{
                border-top: 2px solid black;
                border-right: 2px solid black;
                border-bottom: 2px solid black;
            }
            .borderTopandBottom{
                border-top: 2px solid black;
                border-bottom: 2px solid black;
            }
            .borderRightandBottom{
                border-right: 2px solid black;
                border-bottom: 2px solid black;
            }
            #tableset tr td div{
                text-indent:20px;
                font-size: 15px;
                font-family:verdana;
            }
            .fdiv{
                position: absolute;
                left: 35%;
                bottom: 35%;
            }
            @media print {
                .fdiv{
                    position: absolute;
                    left: 35%;
                    bottom: 20%;
                }
            }
        </style>
    </head>
    <body>
        <div align="center">
            <div style="width:1000px;margin-top: 20px">
                <table width="100%" border="0" style="font-family:verdana;">
                    <tr>
                        <td width="40%" height="40" style="font-size:16px;font-weight: bold"> Schedule LIII - Form No. 188 </td>
                        <td width="30%"><div align="center" style="vertical-align:middle;height:25px;border:1px solid #000000;margin: 0px 35px 0px 35px;">  &nbsp; ${billChartOfAccount.ddoName} </div></td>
                        <td width="24%" style="font-size:12px;font-weight: bold">Bill No. ${billChartOfAccount.billdesc}  </td>
                        <td width="6%" rowspan="3" style="font-size: 48px">P</td>
                    </tr>
                    <tr>
                        <td colspan="2" style="font-size:12px">Detailed Pay Bill of Permanent/Temporary Establishment of the  <c:out value="${billChartOfAccount.offName}"/>  </td>
                        <td style="font-size:12px;font-weight: bold">(O.T.C.Form No.22)</td>
                    </tr>
                    <tr>
                        <td colspan="2" style="font-size:12px">for the month of <b><c:out value="${billMonth}"/></b></td>
                        <td style="font-size:12px">District : ${billChartOfAccount.district}</td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                        <td style="font-size:12px;"> IFMS Ref No:&nbsp;&nbsp;${billChartOfAccount.benRefNo}</td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                        <td style="font-size:12px;"> Token No:&nbsp;&nbsp;${billChartOfAccount.tokenNo}</td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                        <td style="font-size:12px;"> HRMS Bill ID:&nbsp;&nbsp;${billChartOfAccount.billid}</td>
                    </tr>
                    <tr>
                        <td colspan="2">&nbsp;</td>
                        <td style="font-size:12px;"> Voucher No./Date:&nbsp;&nbsp;${billChartOfAccount.voucherNo}/${billChartOfAccount.voucherDate}</td>
                    </tr>
                </table>

                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-family:verdana;" id="tableset">
                    <tr>
                        <td width="60%" class="borderTopRightandBottom" style="font-size:12px;padding-left:5px;" valign="top"> 
                            <span style="font-size:18px">Name of detailed heads filled in by the Drawing Officer</span>
                            <table border="0" width="100%" style="font-size:15px;margin-top: 15px"> 
                                <tr> 
                                    <td class="borderTop">Demand no</td>
                                    <td class="borderTop">- ${billChartOfAccount.demandNo} </td>
                                    <td class="borderTop"> &nbsp; </td>
                                </tr>
                                <tr> 
                                    <td>Major head </td>
                                    <td>- ${billChartOfAccount.majorHead}</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr> 
                                    <td>Sub Major head  </td>
                                    <td>- ${billChartOfAccount.subMajorHead}</td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr> 
                                    <td>Minor head  </td>
                                    <td>- ${billChartOfAccount.minorHead}</td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr> 
                                    <td>Sub head  </td>
                                    <td>- ${billChartOfAccount.subMinorHead1}</td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr> 
                                    <td>Detail head</td>
                                    <td> - ${billChartOfAccount.subMinorHead2}</td>
                                    <td>&nbsp;  </td>
                                </tr>
                                <tr> 
                                    <td> Plan Status  </td>
                                    <td> - ${billChartOfAccount.planName}</td>
                                    <td>&nbsp;  </td>
                                </tr>
                                <tr> 
                                    <td>Charge/Voted</td>
                                    <td>- ${billChartOfAccount.subMinorHead3}</td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr> 
                                    <td>Sector</td>
                                    <td>- ${billChartOfAccount.sectorName}</td>
                                    <td>&nbsp; </td>
                                </tr>
                            </table>

                            <br />

                            <br>
                        </td>
                        <td width="40%" class="borderTopandBottom" valign="top">
                            <table border="0" width="100%" style="font-family:verdana;" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td valign="top">
                                        <table border="0" width="100%" style="font-family:verdana;" cellpadding="0" cellspacing="0">
                                            <tr style="height:30px">
                                                <td colspan="4" align="center" style="font-size:18px">Unit-wise Expenditure & Deduction Details</td>
                                            </tr>
                                            <tr style="font-size:14px">
                                                <td class="borderTop" width="40%">&nbsp; </td>
                                                <td class="borderTopandRight" width="25%">&nbsp; </td>
                                                <td class="borderTopandRight" align="center" width="25%">RS </td>
                                                <td class="borderTop" width="10%">&nbsp;P </td>
                                            </tr>
                                            <tr style="font-family:verdana;font-size:14px">
                                                <td colspan="2" class="borderRight" >&nbsp; Pay of permanent Establishment</td>
                                                <td class="borderTopandRight">&nbsp; </td>
                                                <td class="borderTop">&nbsp; </td>
                                            </tr>
                                            <tr style="font-family:verdana;font-size:14px">
                                                <td colspan="2" class="borderRight">&nbsp; Pay of temporary Establishment</td>
                                                <td class="borderRight">&nbsp; </td>
                                                <td>&nbsp; </td>
                                            </tr>
                                            <c:if test = "${PayAmt ne '0'}">
                                                <tr style="font-family:verdana;font-size:18px">
                                                    <td>&nbsp; <c:out value="${payHead}"/> </td>
                                                    <td class="borderRight"> - PAY </td>   
                                                    <td class="borderRight" align="right"><c:out value="${PayAmt}"/> &nbsp;</td> 
                                                    <td>&nbsp; </td>
                                                </tr>
                                            </c:if>
                                            <c:if test = "${SpAmt ne '0'}">
                                                <tr style="font-family:verdana;font-size:18px">
                                                    <td>&nbsp; 136 </td>
                                                    <td class="borderRight"> - SP </td>   
                                                    <td class="borderRight" align="right"><c:out value="${SpAmt}"/> &nbsp;</td> 
                                                    <td>&nbsp; </td>
                                                </tr>
                                            </c:if>
                                            <c:if test = "${IrAmt ne '0'}">
                                                <tr style="font-family:verdana;font-size:18px">
                                                    <td>&nbsp; 136 </td>
                                                    <td class="borderRight"> - IR </td>   
                                                    <td class="borderRight" align="right"><c:out value="${IrAmt}"/> &nbsp;</td> 
                                                    <td>&nbsp; </td>
                                                </tr>
                                            </c:if>
                                            <c:forEach var="eachOA" items="${OAList}">                                            
                                                <c:if test = "${eachOA.objectHead ne '136' && eachOA.objectHead ne '000'}">

                                                    <tr style="font-family:verdana;font-size:18px">
                                                        <td>&nbsp; <c:out value="${eachOA.objectHead}"/></td>
                                                        <td class="borderRight"><c:out value="${eachOA.scheduleName}"/> &nbsp;</td>   
                                                        <td class="borderRight" align="right"><c:out value="${eachOA.schAmount}"/> &nbsp;</td> 
                                                        <td>&nbsp; </td>
                                                    </tr>
                                                    <%j++;%>
                                                </c:if>
                                            </c:forEach>


                                            <tr style="height: 30px;font-size:12px">
                                                <td>&nbsp; </td>
                                                <td class="borderRight">&nbsp; </td>
                                                <td class="borderRight">&nbsp; </td>
                                                <td>&nbsp; </td>
                                            </tr>
                                            <tr style="font-family:verdana;font-size:14px">
                                                <td class="borderTop">&nbsp; </td>
                                                <td class="borderTopandRight" align="right">Total&nbsp;</td>
                                                <td class="borderTopRightandBottom" align="right"><b><c:out value="${TotOaAmt}"/></b> &nbsp;</td>
                                                <td class="borderTopandBottom">&nbsp;<c:out value="${TotOaAmtPaise}"/></td>
                                            </tr>
                                            <tr style="font-family:verdana;font-size:14px">
                                                <td>Deduct- </td>
                                                <td class="borderRight">&nbsp;</td>
                                                <td class="borderRight" align="right"> &nbsp;</td>
                                                <td>&nbsp; </td>
                                            </tr>
                                            <c:if test="${empty scheduleList}">
                                                <tr style="font-family:verdana;font-size:14px">
                                                    <td>&nbsp;  </td>
                                                    <td> &nbsp;</td>
                                                    <td align="right">&nbsp; </td>
                                                    <td>&nbsp; </td>
                                                </tr>
                                            </c:if>    

                                            <c:if test="${not empty scheduleList}">
                                                <c:forEach var="eachSch" items="${scheduleList}">
                                                    <tr style="font-family:verdana;font-size:18px">
                                                        <td>&nbsp; <c:out value="${eachSch.objectHead}"/></td>
                                                        <td class="borderRight"><c:out value="${eachSch.scheduleName}"/> &nbsp;</td>   
                                                        <td class="borderRight" align="right"><c:out value="${eachSch.schAmount}"/> &nbsp;</td> 
                                                        <td>&nbsp; </td>
                                                    </tr>
                                                    <%j++;%>
                                                </c:forEach>
                                            </c:if>
                                        </table>

                                        <table width="100%" style="font-family:verdana;font-size:14px" cellpadding="0" cellspacing="0">
                                            <% for (int i = 0; i < 10 - j; i++) {%>
                                            <tr style="font-family:verdana;font-size:14px">
                                                <td width="40%">&nbsp; </td>
                                                <td class="borderRight" width="25%"> &nbsp;</td>
                                                <td class="borderRight" align="right" width="25%"> &nbsp;</td>
                                                <td width="10%">&nbsp; </td>
                                            </tr>
                                            <%}%>
                                        </table>
                                    </td>
                                </tr>
                            </table>

                            <table border="0" width="100%" style="height:80px;font-family:verdana;font-size:14px" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="borderRight" width="65%" >&nbsp;Total deductions </td>
                                    <td class="borderTopRightandBottom" width="25%" align="right" style="font-size:18px"><c:out value="${TotDeductAmt}"/> &nbsp; </td>
                                    <td class="borderTopandBottom" width="10%" style="font-size:18px">&nbsp;<c:out value="${TotDeductAmtPaise}"/> </td>
                                </tr>
                                <tr>
                                    <td class="borderRight" >&nbsp;Net Total </td>
                                    <td class="borderRight" align="right" style="font-size:18px"><c:out value="${TotNetAmt}"/> &nbsp; </td>
                                    <td style="font-size:18px">&nbsp;<c:out value="${TotNetAmtPaise}"/> </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <br />
                <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-family:verdana;font-size:15px"> 
                    <tr> 
                        <td width="20%" class="borderLeft borderTopRightandBottom" style="padding:5px;"><strong>Category of Employees</strong></td>
                        <td width="10%" class="borderTopRightandBottom" style="padding:5px;"><strong>Number</strong></td>
                        <td width="20%">&nbsp;</td>
                        <td width="20%" class="borderLeft borderTopRightandBottom" style="padding:5px;">Amount Credited to Beneficiary Account </td>
                        <td width="20%" class="borderTopRightandBottom" style="padding:5px;"><c:out value="${amtToBeneficiary}"/></td>
                    </tr>
                    <tr> 
                        <td class="borderLeft borderRightandBottom" style="padding:5px;">Group - A</td>
                        <td class="borderRightandBottom" style="padding:5px;"><c:out value="${grpACount}"/></td>
                        <td>&nbsp;</td>
                        <td class="borderLeft borderRightandBottom" style="padding:5px;">Amount Credited to DDO Account </td>
                        <td class="borderRightandBottom" style="padding:5px;"><c:out value="${amtToDDO}"/></td>
                    </tr>
                    <tr> 
                        <td class="borderLeft borderRightandBottom" style="padding:5px;">Group - B</td>
                        <td class="borderRightandBottom" style="padding:5px;"><c:out value="${grpBCount}"/></td>
                        <td>&nbsp;</td>
                        <td class="borderLeft borderRightandBottom" style="padding:5px;"><strong>TOTAL(Net Amount)</strong></td>
                        <td class="borderRightandBottom" style="padding:5px;"><c:out value="${TotNetAmt}"/></td>
                    </tr>
                    <tr> 
                        <td class="borderLeft borderRightandBottom" style="padding:5px;">Group - C</td>
                        <td class="borderRightandBottom" style="padding:5px;"><c:out value="${grpCCount}"/></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr> 
                        <td class="borderLeft borderRightandBottom" style="padding:5px;">Group - D</td>
                        <td class="borderRightandBottom" style="padding:5px;"><c:out value="${grpDCount}"/></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr> 
                        <td class="borderLeft borderRightandBottom" style="padding:5px;">Contractual</td>
                        <td class="borderRightandBottom" style="padding:5px;"><c:out value="${contractualCount}"/></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr> 
                        <td class="borderLeft borderRightandBottom" style="padding:5px;">Consolidated</td>
                        <td class="borderRightandBottom" style="padding:5px;"><c:out value="${consolidatedCount}"/></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr> 
                        <td class="borderLeft borderRightandBottom" style="padding:5px;">Adhoc</td>
                        <td class="borderRightandBottom">&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                    </tr>
                </table>
                <br />
                <table border="0" width="100%" cellspacing="0" cellpadding="0">
                    <tr style="height:30px;">
                        <td colspan="4" align="center" class="borderLeft borderTopRightandBottom">DETAIL OF PAY OF ABSENTEES REFUNDED</td>
                    </tr>
                    <tr style="height:30px;">
                        <td width="25%" align="center" class="borderLeft borderRightandBottom">Transaction of Establishment</td>
                        <td width="25%" align="center" class="borderRightandBottom">Name of the Incumbent</td>
                        <td width="25%" align="center" class="borderRightandBottom">Period</td>
                        <td width="25%" align="center" class="borderRightandBottom">Amount</td>
                    </tr>
                    <tr style="height:30px;">
                        <td colspan="4" align="center">&nbsp;</td>
                    </tr>
                    <tr style="height:30px;">
                        <td colspan="4">
                            <span style="margin-left:150px;">Rupees(in words):</span>&nbsp;
                            <c:out value="${totNetAmtInWord}"/>
                        </td>
                    </tr>
                    <tr style="height:30px;">
                        <td colspan="4">
                            <span style="margin-left:150px;">Under Rupees:</span>&nbsp;
                            <c:out value="${totNetAmtUnderInWord}"/>
                        </td>
                    </tr>
                    <tr style="height:30px;">
                        <td colspan="4">
                            <span style="margin-left:100px;">Received content</span>
                        </td>
                    </tr>
                    <tr style="height:30px;">
                        <td colspan="2">
                            <span style="margin-left:100px;">Received payment</span>
                        </td>
                        <td colspan="2" align="right">
                            <span style="margin-left:100px;">Signature of the DDO</span>
                        </td>
                    </tr>
                </table>
                <br />
                <div style="page-break-before: always;"> </div>
                <div align="left" style="font-size:18px;font-weight:bold;">
                    Pay Order of the Treasury Officer
                </div>
                <div align="left">
                    <span style="margin-left:100px;margin-top: 10px;">
                        Net payment credited to Bank Account Rs.&nbsp;<c:out value="${TotNetAmt}"/>
                    </span>
                </div>
                <div align="left">
                    <div style="margin-left:100px;margin-top: 10px;">
                        Deductions(Treasury by transfers)<br />
                        <table width="60%" border="0" cellspacing="0" cellpadding="0" style="margin-top: 10px;">
                            <thead>
                                <tr>
                                    <th width="40%" class="borderRight borderLeft borderTop borderBottom">Nature of Deductions</th>
                                    <th width="20%" class="borderRight borderTop borderBottom">Amount</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:if test="${not empty scheduleListTR}">
                                    <c:forEach var="eachSch" items="${scheduleListTR}">
                                        <tr style="font-family:verdana;font-size:14px;">
                                            <td class="borderRight"><c:out value="${eachSch.scheduleName}"/> &nbsp;</td>   
                                            <td class="borderRight" align="right"><c:out value="${eachSch.schAmount}"/> &nbsp;</td> 
                                            <td>&nbsp; </td>
                                        </tr>
                                    </c:forEach>
                                    <tr style="font-family:verdana;font-size:18px">
                                        <td class="borderRight borderTop">TOTAL(Treasury Gross)</td>   
                                        <td class="borderRight borderTop" align="right"><c:out value="${TotDeductAmtTR}"/> &nbsp;</td> 
                                        <td>&nbsp; </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <br />
                <table border="0" width="100%" style="font-family:verdana;font-size:14px">
                    <tr style="font-size:14px">
                        <td>Examined and Entered </td>
                        <td>Treasury Accountant</td>
                        <td>Treasury Officer</td>
                    </tr>
                </table>
                <br />
                <table border="0" width="100%" style="font-family:verdana;font-size:14px">
                    <tr>
                        <td colspan="3" align="center"> <b> FOR THE USE OF THE ACCOUNTANT GENERAL'S OFFICE </b> </td>
                    </tr>
                    <tr style="font-size:14px">
                        <td><b> Admitted Rs. </b> </td>
                        <td>&nbsp; </td>
                        <td>&nbsp; </td>
                    </tr>
                    <tr style="font-size:14px">
                        <td> <b> Object Rs. </b> </td>
                        <td>&nbsp; </td>
                        <td>&nbsp; </td>
                    </tr>
                    <tr style="font-size:14px">
                        <td width="30%"><b> Auditor </b> </td>
                        <td width="30%"><b> Superintendent </b> </td>
                        <td width="60%"><b> Gazetted Officer </b> </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
