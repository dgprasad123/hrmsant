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
        <title>:: Conveyance Schedule ::</title>
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
            #priDataDiv   { page-break-after:always; }
        </style>
    </head>
    <body>
        
    <div style="width:90%;margin: 0 auto;">
        <table width="100%" border="0">
            <tr>
                <td style="text-align:center" class="printData">
                    <b><c:out value="${CsHeader.officename}"/></b>
                </td>
            </tr>
            <tr>
                <td style="text-align:center" class="printData">
                    <b> RECOVERY SCHEDULE OF CONVEYANCE CHARGES for the month of : <c:out value="${CsHeader.aqMonthAsName}"/> - <c:out value="${CsHeader.aqyear}"/></b> </b>
                </td>
            </tr>
            <tr>
                <td style="text-align:center" class="printData"> <b> Bill No : <c:out value="${CsHeader.billdesc}"/></b> </td>
            </tr>
            <tr>
                <td style="text-align:center">---------- * ---------</td>
            </tr>
        </table>
    </div>
    
    <div id="priDataDiv" style="width:97%;margin: 0 auto;font-size:13px; font-family:verdana;">
        
        <table border="1" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
            <tr class="tblHeader">
                <td width="4%" class="printData" align="center">Sl. No.</td>
                <td width="20%" class="printData">Name of the Employee</td>
                <td width="10%" class="printData">Designation</td>
                <td width="5%" class="printData" align="center">Amount Deducted</td>
            </tr>
        </table>
        
        <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
            <c:if test="${not empty CsDataList}">
            <c:forEach var="eachEmpCS" items="${CsDataList}">
                <tr style="height:30px">
                    <td width="4%" align="center" style="border-bottom:1px solid #000000;"> <c:out value="${eachEmpCS.slNo}"/></td>
                    <td width="20%" align="left" style="border-bottom:1px solid #000000;"> <c:out value="${eachEmpCS.empName}"/></td>
                    <td width="10%" align="left" style="border-bottom:1px solid #000000;"> <c:out value="${eachEmpCS.empDesg}"/></td>
                    <td width="5%" align="center" style="border-bottom:1px solid #000000;"> <c:out value="${eachEmpCS.amtDed}"/>&nbsp;</td>
                </tr>
            </c:forEach>
            </c:if>        
        </table>
        
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr>
                <td colspan="4" height="5px"><hr/></td>
            </tr>
            <tr>
                <td class="printData" colspan="3" align="right"> <b>Grand Total</b> &nbsp;&nbsp;</td>
                <td width="13%" class="printData" align="center"><b><c:out value="${GTotal}"/> &nbsp;</b></td>
            </tr>
            <tr>
                <td colspan="4" height="5px"><hr/></td>
            </tr>
            <tr>
                <td colspan="4" class="printData" align="right">RUPEES &nbsp; <b><c:out value="${GTotalFig}"/></b>&nbsp;ONLY</td>
            </tr>
            <tr style="height:30px" colspan="4">
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td width="10%" colspan="4" align="right">&nbsp;
                    <c:out value="${CsHeader.ddoname}"/><br>
                </td>
            </tr>
        </table>
                    
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr>
                <td width="10%">&nbsp; </td>
                <td width="10%">&nbsp; </td>
                <td width="10%">&nbsp; </td>
                <td width="3%" align="left">&nbsp; Date : </td>
            </tr>
        </table>
    </div>  
           
</body>
</html>
