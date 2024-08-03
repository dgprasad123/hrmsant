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
    <div style="width:99%;margin: 0 auto;">
        <table width="100%" border="0" style="font-size:12px; font-family:verdana;">
            <tr>
                <td style="text-align:center" class="printData"><b>SCHEDULE OF RECOVERY OF PLI PREMIUM</b></td>
            </tr>
            <tr>
                <td style="text-align:center" class="printData">FOR THE MONTH OF <b><c:out value="${pliHeader.monthYear}"/></b></td>
            </tr>
            <tr>
                <td style="text-align:center" class="printData"><c:out value="${pliHeader.officeName}"/></td>
            </tr>
            <tr>
                <td style="text-align:center" class="printData">BILL NO: <c:out value="${pliHeader.billdesc}"/></td>
            </tr>
            <tr>
                <td><hr/><c:set var="pgNo" value="1" /></td>
            </tr>
        </table>
    </div>
    
    <div style="width:99%;margin: 0 auto;">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px; font-family:verdana;text-align:left;">
        <thead></thead>
            <tr >
                <td width="45%" class="printData"><u>POSTAL LIFE INSURANCE</u></td>
                <td width="10%">&nbsp;</td>
                <td width="45%" class="printData" style="text-align:right;"><u>Form No.Tr. 183 </u></td>
            </tr>  
            <tr>
                <td valign="top" class="printData">Orissa State Government Servent's policies statement showing deduction<br>
                    on account of premium towards Postal Life Insurance Policies from pay salary bill <br> for ________________ 20
                </td>
                <td>&nbsp;</td>
                <td class="printData">Designation of Drawing Officer ..........................
                    ................................... / This statement should be completed in
                    triplicated every month. Two copies to be sent along with the pay
                    bill & the other to be retained in the offices along with the copy of the
                    pay bill.
                </td>
            </tr> 
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td class="printData">Name:............................................................</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td class="printData">Address:.........................................................</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td class="printData">Institution:......................................................</td>
            </tr>
            <tr style="height: 7px;">
                <td></td>
            </tr>
        </table>
        <table border="1" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
            <tr class="tblHeader" style="height:40px" align="center">
                <td class="printData" width="4%">Sl No.</td>
                <td class="printData" width="20%">Name of the Employee</td>
                <td class="printData" width="20%">Designation</td>
                <td class="printData" width="10%">Policy No.</td>
                <td class="printData" width="10%">Recovery Month</td>
                <td class="printData" width="10%">Amount</td>
                <td class="printData" width="10%">Total</td>
                <td class="printData" width="10%">Remark</td>
            </tr> 
        </table>
        <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
            <c:if test="${not empty PliEmpList}">
            <c:forEach var="eachEmpPli" items="${PliEmpList}">
                <tr style="height:30px">
                    <td width="4%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpPli.slno}"/></td>
                    <td width="20%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpPli.empname}"/></td>
                    <td width="20%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpPli.empdesg}"/></td>
                    <td width="10%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpPli.policyNo}"/>&nbsp;</td>
                    <td width="10%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpPli.recMonth}"/>&nbsp;</td>
                    <td width="10%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpPli.amount}"/>&nbsp;</td>
                    <td width="10%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpPli.total}"/></td>
                    <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;</td>
                </tr>
            </c:forEach>
            </c:if>
        </table>
       
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr>
                <td colspan="8" height="5px"><hr/></td>
            </tr>
            <tr>
                <td width="4%" class="printData" colspan="3">&nbsp;
                    TOTAL FOR THE MONTH OF <c:out value="${pliHeader.monthYear}"/>
                </td>
                <td width="20%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
                <td width="10%" class="printData" align="center">&nbsp;<b><c:out value="${TotalAmt}"/></b></td>   
                <td width="10%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="8" height="5px"><hr/></td>
            </tr>
            <tr>
                <td width="4%">&nbsp;</td>
                <td width="20%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
                <td width="10%" colspan="3" class="printData">RUPEES&nbsp;<b><c:out value="${TotalAmtFig}"/></b>&nbsp;ONLY</td>
            </tr>
            <tr style="height:30px" colspan="8">
                <td>&nbsp;</td>
            </tr>
        </table>
            
        <table width="100%" border="0" style="font-size:12px;font-family:verdana;">
            <thead> </thead>
            <tr>
                <td colspan="2" width="50%">&nbsp;</td>
                <td colspan="2" width="20%" class="printData" style="text-align: left">
                    Signature of D.D.O.<br>
                    Date :
                </td>
            </tr>
        </table>
            
        <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-size:12px;font-family:verdana;">
            <thead></thead>
            <tr>
                <td class="printData" width="4%"> Note:-</td>
                <td class="printData" width="90%">1.&nbsp;The avoid credit for premiums being given to wrong accounts care should be taken to state 
                    the correct policy numbers.
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td class="printData">2.&nbsp;If remittance is made by cheque the No. Amount & Drawing Bank should be mentioned in the remark column.</td>
            </tr>
            <tr>
                <td class="printData"> &nbsp;</td>
                <td class="printData">3.&nbsp;Please give full particulars of previous office in case of transfer to your office. In the column's 
                    Remark-above.
                </td>
            </tr>
            <tr>
                <td colspan="9" height="5px"><hr/></td>
            </tr>
            <tr>
                <td  colspan="9" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
            </tr>
        </table>    
            
    </div>    
</body>
</html>
