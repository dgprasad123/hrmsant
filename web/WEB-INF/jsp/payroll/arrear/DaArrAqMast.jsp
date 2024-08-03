
<%@page import="hrms.common.Numtowordconvertion"%>
<%@page import="hrms.common.AqFunctionalities"%>
<%@page import="hrms.model.payroll.schedule.SectionWiseAqBean"%>
<%@page import="hrms.model.payroll.schedule.ADDetailsHealperBean"%>
<%@page import="hrms.model.payroll.schedule.AqreportHelperBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>



<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: DA Arrear AQ Mast ::</title>
        <style type="text/css">

            #divId table
            {
                border-width: 1px 1px 1px 1px;
                border-spacing: 0;
                border-collapse: collapse;
                border-color: #600;
                border-style: solid;
            }
            #divId table tr
            {
                border: 1px;
            }
            #divId table tr td
            {
                margin: 0;
                padding: 4px;
                border: 0;
                font-style: verdana;
                font-size: 9pt;
                text-align:center;
                vertical-align: top;
                border-bottom: 1px solid;
            }


            #divIdlast table
            {
                border-width: 0px 0px 0px 0px;
                border-spacing: 0;
                border-collapse: collapse;
            }
            #divIdlast table tr
            {
                border: 0px;
            }
            #divIdlast table tr td
            {
                margin: 0;
                padding: 4px;
                border: 0;
                font-style: verdana;
                font-size: 9pt;
                text-align:left;
                vertical-align: top;
            }
            .pagebreak { page-break-before: always; } 
        </style>
    </head>
    <body>

        <div height="700" id="divId">
            <table width="100%" border="0">
                <tr>
                    <td width="8%">STATE-<c:out value="${aqBillReport.state}" /></td>
                    <td width="10%" align="center">DIST-<c:out value="${aqBillReport.district}" /></td>        
                    <td width="50%" align="center">SCHEDULE-A STATE HEAD QUATERS FORM NO-58 <br />
                        ARREAR BILL FOR <c:out value="${aqBillReport.offen}"/><br/>
                        MONTHLY PAY BILL FOR <c:out  value="${aqBillReport.month}"/>-<c:out value="${aqBillReport.year}"/></td>
                    <td width="15%" align="center">BILL NO:<c:out value="${aqBillReport.billdesc}" /><br> BILL DT:<c:out value="${aqBillReport.billdate}" /></td>
                    <td width="7%">PAGE:1</td>
                </tr>
            </table>

            <c:set var="pagesize" value="7"/>
            <c:set var="arrear100" value="0"/>
            <c:set var="arrear40" value="0"/>
            <c:set var="totalcpf" value="0"/>
            <c:set var="totalpt" value="0"/>
            <c:set var="totalit" value="0"/>
            <c:set var="totalnet" value="0"/>

            <c:set var="count" value="0" scope="page" />
            <c:forEach begin="0" end="${fn:length(ArrEmpList)/pagesize}">
                <table width="100%" style="border-top: 0;border-bottom: 0">
                    <tr>
                        <td width="2%"  align="center">SL NO</td>
                        <td width="20%">NAME AND DESGIGNATION </td>
                        <td width="5%" align="center">Arrear</td>
                        <td width="5%" align="center">GPF/CPF/TPF</td>
                        <td width="5%" align="center">P.TAX</td>
                        <td width="5%" align="center">I.TAX</td>                        
                        <td width="5%" align="center">TOTAL<br />DEDN</td>
                        <td width="5%" align="center">NET PAY </td>
                        <td width="7%" align="center">REMARKS<br /> A/C NO </td>
                    </tr>
                    <tr>
                        <td>(1)</td>
                        <td>(2)</td>
                        <td>(3)</td>
                        <td>(4)</td>
                        <td>(5)</td>
                        <td>(6)</td>
                        <td>(7)</td>
                        <td>(8)</td>
                        <td>(9)</td>
                    </tr>
                </table>
                <c:set var="month"  value="${aqBillReport.month}"/>

                <table width="100%" border="0" id="tableData">
                    <c:forEach var="eachArrEmp" items="${ArrEmpList}" begin="${count}" end="${count+pagesize}">
                        <c:set var="count" value="${count + 1}" scope="page"/>

                        <c:set var="arrear100" value="${arrear100 + eachArrEmp.grandTotArr100}"/>
                        <c:set var="totalcpf" value="${totalcpf + eachArrEmp.cpfHead}"/>
                        <c:set var="totalpt" value="${totalpt + eachArrEmp.professionalTax}"/>
                        <c:set var="totalit" value="${totalit + eachArrEmp.incomeTaxAmt}"/>
                        <c:set var="totalnet" value="${totalnet + (eachArrEmp.grandTotArr100 - (eachArrEmp.cpfHead+eachArrEmp.professionalTax+eachArrEmp.incomeTaxAmt))}"/>
                        <tr style="height: 60px;">
                            <td width="3%" valign="top" align="center">${eachArrEmp.slno}</td>
                            <td width="20%" style="text-align:left">
                                ${eachArrEmp.empName}<br/>
                                ${eachArrEmp.curDesg}<br/>
                                ${eachArrEmp.gpfAccNo}
                            </td>
                            <td width="5%" valign="top" align="center">${eachArrEmp.grandTotArr100}</td>
                            <td width="5%" valign="top" align="center">${eachArrEmp.cpfHead}</td>
                            <td width="5%" valign="top" align="center">${eachArrEmp.professionalTax}</td>
                            <td width="5%" valign="top" align="center">${eachArrEmp.incomeTaxAmt}</td>
                            <td width="5%" valign="top" align="center">${eachArrEmp.cpfHead + eachArrEmp.professionalTax + eachArrEmp.incomeTaxAmt}</td>
                            <td width="5%" valign="top" align="center">${eachArrEmp.grandTotArr100 - (eachArrEmp.cpfHead + eachArrEmp.professionalTax + eachArrEmp.incomeTaxAmt)}</td>
                            <td width="7%" valign="top">${eachArrEmp.remark}</td>
                        </tr>
                    </c:forEach>
                    <tr style="height: 60px;">
                        <td valign="top" align="center">&nbsp;</td>
                        <td style="text-align:left">PAGE TOTAL</td>
                        <td valign="top" align="center">&nbsp;</td>
                        <td valign="top" align="center">&nbsp;</td>
                        <td valign="top" align="center">&nbsp;</td>
                        <td valign="top" align="center">&nbsp;</td>
                        <td valign="top" align="center">&nbsp;</td>
                        <td valign="top" align="center">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                    </tr>
                </table>
                <div class="pagebreak"> </div>
            </c:forEach>
            <table width="100%" border="1">
                <tr>
                    <td width="3%" valign="top" align="center">&nbsp;</td>
                    <td width="20%" >GRAND TOTAL: </td>
                    <td width="5%" valign="top" align="center">${arrear100}</td>
                    <td width="5%" valign="top" align="center">${totalcpf}</td>
                    <td width="5%" valign="top" align="center">${totalpt}</td>
                    <td width="5%" valign="top" align="center">${totalit}</td>
                    <td width="5%" valign="top" align="center">${totalnet}</td>
                    <td width="7%" valign="top" align="center">&nbsp;</td>
                </tr>

                <tr>
                    <td colspan="8"><b><div align="right">Rupees &nbsp; 
                                <%
                                    if(!(((String)pageContext.getAttribute("totalnet")).equals("0"))){
                                    Long netamountinword = (Long) pageContext.getAttribute("totalnet");
                                    out.print(Numtowordconvertion.convertNumber(netamountinword.intValue()));
                                    }
                                %>  
                                only </div></b></td>
                </tr>

            </table>  
        </div>
                

    </body>
</html>
