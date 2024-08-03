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
        <title>:: Excess Pay ::</title>
        <style type="text/css">
            .pgHeader{
                font-size:12px;
                font-family:verdana;
                font-weight: bold;
            }
            .tblHeader{
                font-size:12px;
                text-align:center;
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
        <div style="width:90%;margin: 0 auto;">
            <table width="100%" border="0">
                <tr>
                    <td style="text-align:center;font-size:17px;font-weight:bold;">EXCESS PAY RECOVERY</td>
                </tr>
                <tr>
                    <td  style="text-align:center;font-size:15px;">
                        FOR THE MONTH OF&nbsp;<b><c:out value="${ExcessHeader.monthYear}"/></b>
                    </td>
                </tr>
            </table>
        </div>

        <div style="width:99%;margin: 0 auto;font-size:13px; font-family:verdana;">
            <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">
                <thead></thead>
                <tr style="height:23px;">
                    <td colspan="2">&nbsp;</td>
                </tr>
                <tr style="height:23px;">
                    <td width="7%" class="pgHeader">Name of the Department:</td>
                    <td width="40%"><c:out value="${ExcessHeader.deptName}"/></td>
                </tr>
                <tr style="height:23px;">
                    <td class="pgHeader">Name of the Office:</td>
                    <td><c:out value="${ExcessHeader.offName}"/></td>
                </tr>
                <tr style="height:23px;">
                    <td class="pgHeader">Designation of DDO:</td>
                    <td><c:out value="${ExcessHeader.ddoDegn}"/></td>
                </tr>
                <tr style="height:23px;">
                    <td class="pgHeader">Bill No:</td>
                    <td><c:out value="${ExcessHeader.billDesc}"/></td>
                </tr>
            </table>

            <table border="1" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                <tr style="height:40px">
                    <td width="4%" class="tblHeader">Sl No</td>
                    <td width="25%" class="tblHeader">Employee Name</td>
                    <td width="17%" class="tblHeader">Designation</td>
                    <td width="10%" class="tblHeader">Gross Salary</td>
                    <td width="10%" class="tblHeader">Excess Pay</td>
                    <td width="10%" class="tblHeader">Remark</td>
                </tr> 
            </table>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                <c:if test="${not empty ExcessList}">
                    <c:forEach var="eachEmpExcess" items="${ExcessList}">
                        <tr style="height:35px">
                            <td width="4%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpExcess.slno}"/>&nbsp;</td>
                            <td width="25%" align="left" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpExcess.empName}"/></td>
                            <td width="17%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpExcess.empDegn}"/>&nbsp;</td>
                            <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpExcess.empGrossSal}"/>&nbsp;</td>
                            <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpExcess.empTaxOnProffesion}"/>&nbsp;</td>
                            <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;</td>
                        </tr>
                        <c:if test="${not empty eachEmpExcess.pagebreakEP}">
                            <tr>
                                <td colspan="4" class="pgHeader" style="text-align:right; text-transform: uppercase;">Carry Forward:</td>
                                <td colspan="2" style="text-align:left;padding-left: 50px;font-weight: bold;"><c:out value="${eachEmpExcess.totalTax}"/> </td>
                            </tr>
                            <tr>
                                <td colspan="6" class="pgHeader" style="text-align:right; text-transform: uppercase;"><hr/></td>
                            </tr>
                            <tr>
                                <td colspan="6" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page No:<%=pageNo++%> </td>
                            </tr>
                        </table>
                        <c:out value="${eachEmpExcess.pagebreakEP}" escapeXml="false"/>
                        <table width="100%" border="0">
                            <tr>
                                <td style="text-align:center;font-size:15px;font-weight:bold;">EXCESS PAY RECOVERY</td>
                            </tr>
                            <tr>
                                <td  style="text-align:center;font-size:13px;">
                                    FOR THE MONTH OF&nbsp;<b><c:out value="${ExcessHeader.monthYear}"/></b>
                                </td>
                            </tr>
                            <tr><td>&nbsp;</td></tr>
                        </table>
                        <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                                <c:out value="${eachEmpExcess.pageHeaderEP}" escapeXml="false"/>
                        </c:if>

                    </c:forEach>
                </c:if>
            </table>
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                <tr style="height:25px">
                    <td width="4%">&nbsp;</td>
                    <td width="25%">TOTAL FOR THE MONTH OF <c:out value="${ExcessHeader.monthYear}"/></td>
                    <td width="17%">&nbsp;</td>
                    <td width="10%" align="center"><c:out value="${GrossTot}"/></td>   
                    <td width="10%" align="center"><b><c:out value="${TaxTot}"/></b></td>
                    <td width="10%">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="6" height="5px"><hr/></td>
                </tr>
                <tr style="height:25px">
                    <td width="4%">&nbsp;</td>
                    <td width="25%">&nbsp;</td>
                    <td width="17%">&nbsp;</td>
                    <td width="10%">&nbsp;</td>   
                    <td width="10%" colspan="2">(RUPEES &nbsp;<c:out value="${TaxTotFig}"/>&nbsp;) ONLY</td>
                </tr>
                <tr>
                    <td colspan="6" height="30px">&nbsp;</td>
                </tr>
            </table>    

            <table width="100%" border="0" cellpadding="0" cellspacing="0" >
                <thead></thead>
                <tr>
                    <td width="60%">&nbsp;</td>
                    <td width="40%" align="center" style="font-size:11px; font-family:verdana;">Signature of D.D.O.</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td align="center" style="font-size:11px; font-family:verdana;"><b><c:out value="${ExcessHeader.offName}"/></b></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td align="center" style="font-size:11px; font-family:verdana;">Date:</td>
                </tr>
            </table>

            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                <thead></thead>
                <tr style="height:20px">
                    <td style="font-size:12px;" colspan="2">To be filled by the Treasury Officer/ Sub Treasury Officer/ Special Treasury Officer</td>
                </tr>
                <tr style="height:20px">
                    <td style="font-size:12px;">a.</td>
                    <td style="font-size:12px;"> T.V. No. _________________ and Date __________________ of encashment of Bill</td>
                </tr>
                <tr style="height:20px">
                    <td style="font-size:12px;">b.</td>
                    <td style="font-size:12px;"> Sl. No. __________________ and Date __________________ of the receipt Schedule in which accounted</td>
                </tr>
                <tr style="height:20px">
                    <td style="font-size:12px;"></td>
                    <td style="font-size:12px;">by Transfer Credit.</td>
                </tr>
                <tr style="height:20px">
                    <td style="font-size:12px;">&nbsp;</td>
                    <td style="font-size:12px;" align="right">Page No:<%=pageNo++%></td>
                </tr>
            </table>         

        </div>

    </body>
</html>
