<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: Beneficiary List ::</title>
    </head>
    <body>
        <div align="center">
            <c:out value="${crb.offcode}"/>
        </div>
        <div align="center">
            <c:out value="${crb.officeen}"/>
        </div>
        <div align="center">
            Bill Wise Beneficiary List Report
        </div>
        <table width="90%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="5%"></td>
                <td width="30%"></td>
                <td width="15%"></td>
                <td width="15%"></td>
                <td width="10%"></td>
                <td width="15%"></td>
                <td width="15%"></td>
                <td width="15%"></td>
            </tr>
            <tr style="height:30px;">
                <td colspan="8"></td>
            </tr>
            <tr style="height:30px;">
                <td colspan="8">
                    <hr />
                </td>
            </tr>

            <tr>
                <td colspan="2">
                    Treasury Details:
                </td>
                <td colspan="6">
                    <c:out value="${crb.treasuryname}"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    Bill Date and No:
                </td>
                <td colspan="6">
                    <c:out value="${crb.billdate}"/> &emsp;&emsp;&emsp;<c:out value="${crb.billdesc}"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    Gross and Net Amt:
                </td>
                <td colspan="6">
                    <c:out value="${crb.billGrossAmt}"/> &emsp;&emsp;&emsp;<c:out value="${netAmt}"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    Reference Id:
                </td>
                <td colspan="6">
                    <c:out value="${crb.benRefNo}"/>
                </td>
            </tr>
            <tr style="height:30px;">
                <td colspan="8"></td>
            </tr>
            <tr>
                <th>Sl No</th>
                <th>Beneficiary Name</th>
                <th>Bank IFSC Code</th>
                <th>MICR Number</th>
                <th>Account</th>
                <th>Account No</th>
                <th>Mobile</th>
                <th>Amount</th>
            </tr>
            <td colspan="8">
                <hr />
            </td>
            <c:if test="${not empty emplist}">
                <c:forEach var="list" items="${emplist}" varStatus="cnt">
                    <tr style="height:25px;">
                        <td style="padding-left:25px;">
                            ${cnt.index + 1}
                        </td>
                        <td style="padding-left:30px;">
                            <c:out value="${list.beneficiaryName}"/>
                        </td>
                        <td style="padding-left:30px;">
                            <c:out value="${list.ifsCode}"/>
                        </td>
                        <td style="padding-left:30px;">
                            <c:out value="${list.micrNo}"/>
                        </td>
                        <td style="padding-left:50px;">
                            <c:out value="${list.bankAccountType}"/>
                        </td>
                        <td style="padding-left:30px;">
                            <c:out value="${list.banckAccNo}"/>
                        </td>
                        <td style="padding-left:20px;">
                            <c:out value="${list.mobile}"/>
                        </td>
                        <td style="padding-left:20px;">
                            <c:out value="${list.amount}"/>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </table>
    </body>
</html>
