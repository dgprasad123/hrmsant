<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

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
                font-size:12px;
                font-family:verdana;
                font-weight: bold;
                
                border-bottom:1px dashed #000;
            }
        </style>
    </head>
    <body>
    <div style="width:99%;margin: 0 auto;">
        <table width="100%" border="0">
            <tr>
                <th colspan="3" style="text-align:center" class="pgHeader"><b><c:out value="${VSHeader.offName}"/></b></th>
            </tr>
            <tr>
                <th colspan="3" style="text-align:center" class="pgHeader">FORM NO. O.T.C. 23</th>
            </tr>
            <tr>
                <th colspan="3" style="text-align:center" class="pgHeader">ABSENTEE STATEMENT</th>
            </tr>
            
            <tr>
                <th colspan="3" style="text-align:center" class="pgHeader">[See Subsidiary Rule 223]</th>
            </tr>
            <tr>
                <th width="35%" style="text-align:left" class="pgHeader">Bill No: <b><c:out value="${VSHeader.billNo}"/></b></th>
                <td width="30%">&nbsp;</td>
                <td width="35%">&nbsp;</td>
            </tr>
            <tr>
                <th style="text-align:left" class="pgHeader">Schedule LIII-Form No.218<br>------------------------------</th>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <th style="text-align:left" class="pgHeader">(Financial Rule Form No.6)</th>
                <td>&nbsp;</td>
                <td><c:set var="pgNo" value="1"/></td>
            </tr>
        </table>
            
        <table width="97%" border="0" align="center" style="font-size:12px; font-family:verdana;">
            <tr align="center">
                <td width="15%" class="tblHeader" style="border-top:1px dashed #000;">Sl NO.</td>
                <td width="40%" class="tblHeader" style="border-top:1px dashed #000;">DESIGNATION</td>
                <td width="15%" class="tblHeader" style="border-top:1px dashed #000;">SCALE OF PAY</td>
                <td width="15%" class="tblHeader" style="border-top:1px dashed #000;">NO. OF POST</td>
            </tr>
            <tr align="center">
                <td class="tblHeader">1</td>
                <td class="tblHeader">2</td>
                <td class="tblHeader">3</td>
                <td class="tblHeader">4</td>
            </tr>
        </table>
            
        <table width="97%" border="0" align="center" style="font-size:11px; font-family:verdana;">   
            <c:if test="${not empty VEpeList}">
                <c:forEach var="eachEmpVe" items="${VEpeList}">
                    <tr align="center" height="7px">
                        <td width="15%" style="text-align:center;font-size: 12;">&nbsp;<c:out value="${eachEmpVe.slno}"/></td>
                        <td width="40%" style="text-align:center;font-size: 12;"><c:out value="${eachEmpVe.designation}"/>&nbsp;</td>
                        <td width="15%" style="text-align:center;font-size: 12;">&nbsp;<c:out value="${eachEmpVe.payscale}"/></td>
                        <td width="15%" style="text-align:center;font-size: 12;">&nbsp;<c:out value="${eachEmpVe.postno}"/></td>
                    </tr>
                </c:forEach>
                    <tr>
                        <td colspan="4" style="border-bottom:1px dashed #000;">&nbsp;</td>
                    </tr>
                    <tr>
                        <td><b>* Grand Total * :</b></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td align="center"><b>&nbsp;<c:out value="${Gtotal}"/></b></td>
                    </tr>
                    <tr>
                        <td colspan="4" style="border-top:1px dashed #000;">&nbsp;</td>
                    </tr>
            </c:if>
        </table>
            
        <table width="97%" border="0" align="center" style="font-size:11px; font-family:verdana;">
            <tr>
                <th align="left">Date :____________________</th>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td align="center">UNDER SECRETARY TO GOVT.</td>
            </tr>
        </table>
    </div>
            
</body>
</html>
