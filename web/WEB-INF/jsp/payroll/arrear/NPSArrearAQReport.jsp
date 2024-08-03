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
        <title>:: Arrear AQ Mast ::</title>
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
                        MONTHLY PAY BILL FOR <c:out value="${monthName1}"/>-<c:out value="${year1}"/> to <c:out value="${monthName2}"/>-<c:out value="${year2}"/></td>
                    <td width="15%" align="center">BILL NO:<c:out value="${commonreportbean.billdesc}" /><br> BILL DT:<c:out value="${commonreportbean.billdate}" /></td>
                    <td width="7%">PAGE:1</td>
                </tr>
            </table>

            <c:set var="pagesize" value="7"/>
            <c:set var="arrear100" value="0"/>
            <c:set var="arrear40" value="0"/>
            <c:set var="totalcpf" value="0"/>            
            <c:set var="totalnet" value="0"/>

            <c:set var="count" value="0" scope="page" />
            <c:forEach begin="0" end="${fn:length(ArrEmpList)/pagesize}">
                <table width="100%" style="border-top: 0;border-bottom: 0">
                    <tr>
                        <td width="10%"  align="center">SL NO</td>
                        <td width="25%">NAME AND DESGIGNATION </td>                                                                       
                        <td width="15%" align="center">ARREAR 100%</td>
                        <td width="15%" align="center">ARREAR 5%<br />(10% of Balance 50%)</td>
                        <td width="15%" align="center">TOTAL CPF</td>                                               
                        <td width="15%" align="center">TOTAL<br />DEDN</td>
                        <td width="5%" align="center">NET PAY </td>
                        <td width="15%" align="center">REMARKS<br /> A/C NO </td>
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
                    </tr>

                    <c:set var="month"  value="${aqBillReport.month}"/>

                    <c:forEach var="eachArrEmp" items="${ArrEmpList}" begin="${count}" end="${count+pagesize}">
                        <c:set var="count" value="${count + 1}" scope="page"/>

                        <c:set var="arrear100" value="${arrear100 + eachArrEmp.grandTotArr100}"/>
                        <c:set var="arrear40" value="${arrear40 + eachArrEmp.grandTotArr40}"/>
                        <c:set var="totalcpf" value="${totalcpf + eachArrEmp.cpfHead}"/>
                        <c:set var="totalpt" value="${totalpt + eachArrEmp.professionalTax}"/>
                        <c:set var="totalit" value="${totalit + eachArrEmp.incomeTaxAmt}"/>
                        <c:set var="totalnet" value="${totalnet + (eachArrEmp.grandTotArr40-(eachArrEmp.cpfHead+eachArrEmp.professionalTax+eachArrEmp.incomeTaxAmt))}"/>
                        <tr style="height: 60px;">
                            <td width="10%" valign="top" align="center">${eachArrEmp.slno}</td>
                            <td width="25%" style="text-align:left;padding-left:50px;">
                                ${eachArrEmp.empName}<br/>
                                ${eachArrEmp.curDesg}<br/>
                                ${eachArrEmp.gpfAccNo}
                            </td>                            
                            <td width="15%" valign="top" align="center">${eachArrEmp.grandTotArr100}</td>
                            <td width="15%" valign="top" align="center">${eachArrEmp.cpfHead}</td>
                            <td width="15%" valign="top" align="center">${eachArrEmp.cpfHead}</td>
                            <td width="15%" valign="top" align="center">${eachArrEmp.cpfHead}</td>
                            <td width="5%" valign="top" align="center">0</td>
                            <td width="15%" valign="top">${eachArrEmp.remark}</td>
                        </tr>
                    </c:forEach>
                    <tr style="height: 60px;">
                        <td valign="top" align="center">&nbsp;</td>
                        <td valign="top" align="center">Grand Total</td>
                        <td valign="top" align="center"><c:out value="${arrear100}"/></td>
                        <td valign="top" align="center"><c:out value="${totalcpf}"/></td>                        
                        <td valign="top" align="center"><c:out value="${totalcpf}"/></td>
                        <td valign="top" align="center"><c:out value="${totalcpf}"/></td>
                        <td valign="top" align="center">&nbsp;</td>
                        <td valign="top">&nbsp;</td>
                    </tr>
                </table>
                <div class="pagebreak"> </div>
            </c:forEach>
            <table width="100%" border="1">
                <tr>
                    <td width="10%" valign="top" align="center">&nbsp;</td>
                    <td width="25%" valign="top" align="center">&nbsp;</td>
                    <td width="15%" valign="top" align="center">&nbsp;</td>
                    <td width="15%" valign="top" align="center">&nbsp;</td>                   
                    <td width="15%" valign="top" align="center">&nbsp;</td>
                    <td width="15%" valign="top" align="center"></td>
                    <td width="5%" valign="top" align="center">0</td>
                    <td width="15%" valign="top" align="center">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="8">
                        <b>
                            <div align="right">Rupees &nbsp;
                                <%
                                    String cpfstring = pageContext.getAttribute("totalcpf").toString();
                                    Double cpfdouble = Double.parseDouble(cpfstring);
                                    if (!cpfdouble.equals("0")) {
                                        Double netamountinword = cpfdouble;
                                        //out.print(Numtowordconvertion.convertNumber(netamountinword.intValue()));
                                        out.print(Numtowordconvertion.convertNumber(0));
                                    }
                                %>  
                                only 
                            </div>
                        </b>
                    </td>
                </tr>
            </table>
        </div>
    </body>
</html>
