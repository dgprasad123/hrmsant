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
        <title>:: HRMS ::</title>
        <style type="text/css">
            .pgHeader{
                font-size:12px;
                font-family:verdana;
                font-weight: bold;
            }
            .tblHeader{
                font-size:10px;
                font-family:verdana, sans-serif;
                font-weight: bold;
                border-top:1px solid black;
                border-bottom:1px solid black;
                border-left:1px solid black;
                border-right:1px solid black;
            }
        </style>
    </head>
    <body>
    <div style="width:97%;margin: 0 auto;">
        <table width="100%" border="0">
            <tr>
                <td colspan="2" style="text-align:center;font-size: 18px;"> <b><c:out value="${GisAndFaHeader.reportName}"/></b> </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:center"> 
                    FOR THE MONTH OF <c:out value="${GisAndFaHeader.recMonth}"/>-<c:out value="${GisAndFaHeader.recYear}"/>
                </td>
            </tr>
            <tr style="height: 7px">
                <td colspan="2">&nbsp;</td>
            </tr>
        </table>
    </div>
    
    <div style="width:99%;margin: 0 auto;font-size:13px; font-family:verdana;">
        <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;" id="innercontainertbl">
            <thead></thead>
            <tr style="height: 25px;">
                <td width="25%" class="pgHeader">NAME OF THE DEPARTMENT:</td>
                <td width="75%"><c:out value="${GisAndFaHeader.deptName}"/></td>
            </tr>
            <tr style="height: 25px;">
                <td class="pgHeader">OFFICE CODE:</td>
                <td><c:out value="${GisAndFaHeader.offName}"/></td>
            </tr>
            <tr style="height: 25px;">
                <td class="pgHeader">DESIGNATION OF DDO:</td>
                <td><c:out value="${GisAndFaHeader.ddoName}"/></td>
            </tr>
            <tr style="height: 25px;">
                <td class="pgHeader">NAME OF TREASURY::</td>
                <td><c:out value="${GisAndFaHeader.treasuryName}"/></td>
            </tr>
            <tr style="height: 25px;">
                <td class="pgHeader">Bill No:</td>
                <td><c:out value="${GisAndFaHeader.billdesc}"/></td>
            </tr>
        </table>
             
        <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
            <tr class="tblHeader">
                <td width="3%" align="center" class="tblHeader">SL NO.</td>
                <td width="20%" class="tblHeader">NAME OF THE EMPLOYEE/ DESIGNATION</td>
                <td width="15%" align="center" class="tblHeader">T.V. NO. IN WHICH ORIGINAL ADV DRAWN WITH TREASURY NAME</td>
                <td width="8%" align="center" class="tblHeader">ACCOUNT NO.</td>
                <td width="8%" align="center" class="tblHeader">AMOUNT OF ORIGINAL ADVANCE </td>
                <td width="8%" align="center" class="tblHeader">NO OF INSTALLMENT OF RECOVERY</td>
                <td width="8%" align="center" class="tblHeader">AMOUNT DEDUCTED IN THE BILL</td>
                <td width="8%" align="center" class="tblHeader">RECOVERY UPTO THE MONTH</td>
                <td width="8%" align="center" class="tblHeader">BALANCE OUTSTANDING</td>
                <td width="8%" align="center" class="tblHeader">REMARKS</td>
            </tr>
        
            <c:if test="${empty GisAndFaList}">
                <tr style="height:30px">
                    <td width="5%" colspan="10" align="center" style="border-bottom:1px solid #000000;">
                        &nbsp; No Matching Record Found
                    </td>
                </tr>
            </c:if>            
            <c:if test="${not empty GisAndFaList}">
            <c:forEach var="eachEmpGisAndFa" items="${GisAndFaList}">
                <tr style="height:30px">
                    <td width="3%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpGisAndFa.slno}"/>&nbsp;</td>
                    <td width="20%" align="left" style="border-bottom:1px solid #000000;">&nbsp;
                        <c:out value="${eachEmpGisAndFa.empname}"/><br/>&nbsp; <c:out value="${eachEmpGisAndFa.curDesg}"/>
                    </td>
                    <td width="15%" align="center" style="border-bottom:1px solid #000000;">&nbsp;
                        <c:out value="${eachEmpGisAndFa.treasuryVoucherNo}"/> (<c:out value="${eachEmpGisAndFa.treasuryVoucherDate}"/>)<br />
                        <c:out value="${eachEmpGisAndFa.treasuryName}"/>
                    </td>
                    <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpGisAndFa.accountno}"/>&nbsp;</td>
                    <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpGisAndFa.originalAmount}"/>&nbsp;</td>
                    <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpGisAndFa.noofInstallment}"/>&nbsp;</td>
                    <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpGisAndFa.deductedAmount}"/>&nbsp;</td>
                    <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpGisAndFa.recoveryUptoMonth}"/>&nbsp;</td>
                    <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpGisAndFa.balance}"/></td>
                    <td width="8%" align="left" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpGisAndFa.remark}"/></td>
                </tr>
                <c:if test="${not empty eachEmpGisAndFa.pagebreakFA}">
                    <tr>
                        <td colspan="6" class="pgHeader" style="text-align:right; text-transform: uppercase;">Carry Forward:</td>
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpGisAndFa.carryFrdAmt}"/></td> 
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> &nbsp; </td>
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> &nbsp; </td>
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> &nbsp; </td>
                    </tr>
                    <tr><td colspan="10"> <hr/> </td></tr>
                    <tr>
                        <td colspan="10" class="pgHeader" style="text-align:right;text-transform: uppercase;">Page No:<%=pageNo++%> </td>
                    </tr>
                </table>
                    <c:out value="${eachEmpGisAndFa.pagebreakFA}" escapeXml="false"/>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">          
                    <c:out value="${eachEmpGisAndFa.pageHeaderFA}" escapeXml="false"/>
                </c:if>
            </c:forEach>
            </c:if>        
        </table>
        
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr>
                <td width="43%" class="printData" colspan="4">&nbsp;
                    RECOVERY FOR THE MONTH OF <b><c:out value="${GisAndFaHeader.recMonth}"/>-<c:out value="${GisAndFaHeader.recYear}"/></b>
                </td>
                <td width="8%">&nbsp;</td>
                <td width="8%">&nbsp;</td>
                <td width="8%" align="center">&nbsp; <b><c:out value="${TotalAmt}"/></b></td>
                <td width="8%">&nbsp;</td>
                <td width="8%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="10" height="5px"><hr/></td>
            </tr>
            <tr>
                <td colspan="4">&nbsp;</td>
                <td>&nbsp;</td>
                <td colspan="3" class="printData">RUPEES&nbsp;<b><c:out value="${TotalFig}"/></b>&nbsp;ONLY</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td colspan="10" height="5px"><hr/></td>
            </tr>
        </table>
        
        <table width="100%" border="0" style="font-size:12px;font-family:verdana;">
            <thead> </thead>
            <tr>
                <td colspan="2" width="50%">&nbsp;</td>
                <td colspan="2" width="20%" class="printData" style="text-align: right">
                    Signature of D.D.O.<br>
                    <c:out value="${GisAndFaHeader.ddoName}"/><br>
                    Date :
                </td>
                <td width="10%" colspan="5">&nbsp;</td>
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
