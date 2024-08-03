<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
    </head>
    <body>
        <div align="center" style="margin-top:5px;margin-bottom:7px;font-family:Verdana;">
            BILL FOR WITHDRAWING FINAL GPF/PART FINAL GPF/TEMPORARY GPF<br />
            FORM No. O.T.C 79-A<br />
            [See Subsidiary Rule 668(1)]<br />
            ADJUSTABLE BY THE ACCOUNTANT-GENERAL ODISHA
        </div>
        <div align="center" style="margin-top:5px;margin-bottom:7px;font-family:Verdana;">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">
                <tr style="height:40px;">
                    <td width="20%">&nbsp;</td>
                    <td width="20%">&nbsp;</td>
                    <td width="20%">&nbsp;</td>
                    <td width="20%">&nbsp;</td>
                </tr>
                <tr style="height:40px;">
                    <td style="padding-left:20px;">Department</td>
                    <td style="padding-left:20px;" colspan="3">
                        <c:out value="${billData.deptName}"/>
                    </td>
                </tr>
                <tr style="height:40px;">
                    <td style="padding-left:20px;">DDO Code</td>
                    <td style="padding-left:20px;">
                        <c:out value="${billData.ddoCode}"/>
                    </td>
                    <td style="padding-left:20px;">Token No/Date</td>
                    <td style="padding-left:20px;">
                        <c:out value="${billData.tokenNo}"/>/<c:out value="${billData.tokenDate}"/>
                    </td>
                </tr>
                <tr style="height:40px;">
                    <td style="padding-left:20px;">Bill No</td>
                    <td style="padding-left:20px;">
                        <c:out value="${billData.billDesc}"/>
                    </td>
                    <td style="padding-left:20px;">Voucher No/Date</td>
                    <td style="padding-left:20px;">
                        <c:out value="${billData.voucherNo}"/>/<c:out value="${billData.voucherDate}"/>
                    </td>
                </tr>
                <tr style="height:40px;">
                    <td style="padding-left:20px;">Bill Dt</td>
                    <td style="padding-left:20px;">
                        <c:out value="${billData.billDate}"/>
                    </td>
                    <td style="padding-left:20px;">Withdrawal of GPF/TPF</td>
                    <td style="padding-left:20px;">
                        <c:out value="${totalAmt}"/>
                    </td>
                </tr>
            </table>
        </div>
        <div align="center" style="margin-top:5px;margin-bottom:7px;">
            <table width="100%" border="1" cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">
                <tr style="height:40px;">
                    <td width="10%" style="padding-left:20px;border-top:1px solid #000000;border-bottom:1px solid #000000;font-weight:bold">
                        SL No
                    </td>
                    <td width="30%" style="padding-left:20px;border-top:1px solid #000000;border-bottom:1px solid #000000;font-weight:bold">
                        Name<br />Designation<br />Pay
                    </td>
                    <td width="10%" style="padding-left:20px;border-top:1px solid #000000;border-bottom:1px solid #000000;font-weight:bold">
                        GPF Series
                    </td>
                    <td width="20%" style="padding-left:20px;border-top:1px solid #000000;border-bottom:1px solid #000000;font-weight:bold">
                        GPF Account<br />No
                    </td>
                    <td width="15%" style="padding-left:20px;border-top:1px solid #000000;border-bottom:1px solid #000000;font-weight:bold">
                        No and Date of<br />Sanction Letter of<br />Authority
                    </td>
                    <td width="15%" style="padding-left:20px;border-top:1px solid #000000;border-bottom:1px solid #000000;font-weight:bold">
                        Withdrawal of<br />GPF/TPF
                    </td>
                </tr>
                <c:if test="{not empty paymentList}">
                    <c:forEach var="list" items="${paymentList}">
                        <tr style="height:40px;">
                            <td>
                                <c:out value="${list.slno}"/>
                            </td>
                            <td>
                                <c:out value="${list.empName}"/><br />
                                <c:out value="${list.empDesg}"/><br />
                                <c:out value="${list.basic}"/>
                            </td>
                            <td>
                                <c:out value="${list.gpfseries}"/>
                            </td>
                            <td>
                                <c:out value="${list.gpfNo}"/>
                            </td>
                            <td>
                                <c:out value="${list.ordNo}"/><br />
                                <c:out value="${list.ordDt}"/>
                            </td>
                            <td align="right">
                                <c:out value="${list.amount}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                <tr style="height:40px;">
                    <td colspan="6" style="border-bottom:1px solid #000000;">&nbsp;</td>
                </tr>
                <tr style="height:40px;">
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td align="center">
                        Total
                    </td>
                    <td align="right">
                        <c:out value="${totalAmt}"/>
                    </td>
                </tr>
                <tr style="height:40px;">
                    <td colspan="6">
                        Total(in words) - Rupees <c:out value="${totalAmtinWords}"/> Only
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>
