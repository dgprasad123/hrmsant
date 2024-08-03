
<%@page import="hrms.common.Numtowordconvertion"%>
<%@page import="hrms.common.AqFunctionalities"%>
<%@page import="hrms.model.payroll.schedule.SectionWiseAqBean"%>
<%@page import="hrms.model.payroll.schedule.ADDetailsHealperBean"%>
<%@page import="hrms.model.payroll.schedule.AqreportHelperBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!
    String offname = null;
    String deptname = null;
    String distname = null;
    String statename = null;
    String billdesc = null;
    String billdate = null;
    int alsize = 0;
    int totsec = 0;
    ArrayList aqlistsize = new ArrayList();
    int ctr = 0;
    ArrayList aqlistcount = new ArrayList();
    ArrayList aqlistcount1 = new ArrayList();
    int tot = 0;
    int oa1 = 0;
    int mypage = 0;
%>   
<%
    mypage = 1;
    alsize = 0;
    ctr = 0;
%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
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
                font-size: 7pt;
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
                font-size: 7pt;
                text-align:left;
                vertical-align: top;
            }

        </style>
    </head>
    <body>
        <form:form action="aqbillreport.htm" commandName="aqBillReport"  method="GET">
            <div height="800" id="divId">
                <table width="100%" border="0">
                    <tr>
                        <td width="8%">
                            STATE-<c:out value="${aqBillReport.state}" /><br />
                            VCH NO - <c:out value="${vchno}" />
                        </td>
                        <td width="10%" align="center">
                            DIST-<c:out value="${aqBillReport.district}" /><br />
                            VCH DATE - <c:out value="${vchdate}" />
                        </td>   
                        <td width="50%" align="center">SCHEDULE-A STATE HEAD QUATERS FORM NO-58 <br />
                            PAY BILL FOR <c:out value="${aqBillReport.offen}"/><br/>
                            MONTHLY PAY BILL FOR <c:out  value="${aqBillReport.month}"/>-<c:out value="${aqBillReport.year}"/></td>
                        <td width="15%" align="center">BILL NO:<c:out value="${aqBillReport.billdesc}" /><br> BILL DT:<c:out value="${aqBillReport.billdate}" /></td>
                        <td width="7%">PAGE:1</td>
                    </tr>

                </table>
                <table width="100%" style="border-top: 0;border-bottom: 0">
                    <tr>
                        <td width="2%">SL</td>
                        <td width="8%">NAME/<br />DESG/<br />PAY SCALE </td>
                        <td width="5%">PAY<br/>GP</td>          
                        <td width="3%">DA</td>
                        <td width="3%">HRA</td>
                        <td width="6%">OTHER<br />ALLOWANCE</td>
                        <td width="6%">GROSS<br />PAY</td>
                        <td width="5%">PLI<br />LIC</td>
                        <td width="5%">GPF/CPF/TPF</td>
                        <td width="4%">P.TAX<br />I.TAX</td>
                        <td width="5%">QTR DEDN<br />WATER TAX<br />SWG </td>
                        <td width="5%">HB</td>
                        <td width="5%">MC</td>
                        <td width="5%">CAR ADV.</td>
                        <td width="5%">MED ADV.</td>
                        <td width="5%">FEST. ADV</td>
                        <td width="5%">GIS ADV</td>
                        <td width="5%">TOTAL<br />DEDN</td>
                        <td width="3%">NET PAY </td>
                        <td width="7%">REMARKS<br /> A/C NO </td>
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
                        <td>(10)</td>
                        <td>(11)</td>
                        <td>(12)</td>
                        <td>(13)</td>  
                        <td>(14)</td>
                        <td>(15)</td>
                        <td>(16)</td>
                        <td>(17)</td>
                        <td>(18)</td>
                        <td>(19)</td>
                        <td>(20)</td>
                    </tr>
                </table>
                <c:set var="month"  value="${aqBillReport.month}"/>

                <c:forEach var="aqdata1"  items="${aqBillReport.aqlist}">  
                    <c:set  value="${aqdata1.aqlistSectionWise}" var="aqlistSectionWise" />
                    <%
                        int rowcnt = 0;
                        ctr++;
                        int sectionPageCount = 0;
                    %>
                    <c:out value = "${aqlistSectionWise.size()}"/>
                    <table width="100%" border="0" id="tableData"> 
                        <c:out value = "${aqdata1.sectionname}"/>
                        <c:set var="alsize" value = "${aqlistSectionWise.size()}"/>
                        <%
                            sectionPageCount++;
                        %>
                        <c:forEach var="aqData"  begin="0" end="${aqlistSectionWise.size()}" items="${aqlistSectionWise}" >
                            <tr style="height: 70px;">
                                <td width="2%" valign="top"><c:out value="${aqData.slno}" /></td>
                                <td width="8%" style="text-align:left">
                                    <c:out value="${aqData.empname}" /><br/>
                                    <c:out value="${aqData.desg}" /><br/>
                                    <c:out value="${aqData.payscale}" /><br/>
                                    <c:out value="${aqData.gpfacct}" />
                                </td>
                                <td width="5%">
                                    <c:out value="${aqData.basic}" />
                                    <c:forEach items="${aqData.col3}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>                            
                                </td>
                                <%--<td width="4%">
                                    <c:forEach items="${aqData.col4}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>--%>
                                <td width="3%">
                                    <c:forEach items="${aqData.col5}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="3%"> 
                                    <c:forEach items="${aqData.col6}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="6%">
                                    <c:forEach items="${aqData.col7}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="6%"> 
                                    <c:forEach items="${aqData.col8}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%">
                                    <c:forEach items="${aqData.col9}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%">
                                    <c:forEach items="${aqData.col10}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="4%">
                                    <c:forEach items="${aqData.col11}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%">
                                    <c:forEach items="${aqData.col12}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%">
                                    <c:forEach items="${aqData.col13}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%">
                                    <c:forEach items="${aqData.col14}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%">
                                    <c:forEach items="${aqData.col15}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%">
                                    <c:forEach items="${aqData.col16}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%">
                                    <c:forEach items="${aqData.col17}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%">
                                    <c:forEach items="${aqData.col18}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%">
                                    <c:forEach items="${aqData.col19}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="3%">
                                    <c:forEach items="${aqData.col20}" var="ahb">
                                        <div>&nbsp;${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="7%"><c:out value="${aqData.accNo}" /></td>
                            </tr> 
                            <c:if test="${not empty aqData.pagebreakLA}">

                            </table>
                            <c:out value="${aqData.pagebreakLA}" escapeXml="false"/>
                            <c:out value="${aqData.pageHeaderLA}" escapeXml="false"/>
                        </c:if>



                    </c:forEach>

                </table>

            </c:forEach>

            <table width="100%" border="1">
                <tr>
                    <td width="13%" >GRAND TOTAL: </td>

                    <td width="4%" ><c:out value="${aqBillReport.col3Tot}"/></td>
                    <td width="4%"><c:out value="${aqBillReport.col4Tot}"/></td>
                    <td width="2%"><c:out value="${aqBillReport.col5Tot}"/></td>      
                    <td width="3%"><c:out value="${aqBillReport.col6Tot}"/></td>
                    <td width="4%"><c:out value="${aqBillReport.col7Tot}"/></td>
                    <td width="5%"><c:out value="${aqBillReport.col8Tot}"/></td>
                    <td width="6%"><c:out value="${aqBillReport.col9Tot}"/></td>
                    <td width="6%"><c:out value="${aqBillReport.col10Tot}"/></td>
                    <td width="4%"><c:out value="${aqBillReport.col11Tot}"/></td>
                    <td width="5%"><c:out value="${aqBillReport.col12Tot}"/></td>
                    <td width="4%"><c:out value="${aqBillReport.col13Tot}"/></td>
                    <td width="8%"><c:out value="${aqBillReport.col14Tot}"/></td>
                    <td width="5%"><c:out value="${aqBillReport.col15Tot}"/></td>
                    <td width="5%"><c:out value="${aqBillReport.col16Tot}"/></td>
                    <td width="3%"><c:out value="${aqBillReport.col17Tot}"/></td>
                    <td width="5%"><c:out value="${aqBillReport.col18Tot}"/></td>
                    <td width="5%"><c:out value="${aqBillReport.col19Tot}"/></td>
                    <td width="3%"><c:out value="${aqBillReport.col20Tot}"/></td>
                    <td width="7%">&nbsp;</td>          
                </tr>

                <tr>
                    <td colspan="21"><b><div align="right">Rupees &nbsp;<c:out value="${aqBillReport.netPay}"/> only </div></b></td>
                </tr>

            </table>  
        </div>
        <div align="left" id="divIdlast" style="MARGIN-TOP:30PX">
            <table border="0" width="15%">
                <tr>
                    <td WIDTH="25%">
                        Pay 
                    </td>
                    <td WIDTH="25%">
                        =
                    </td>
                    <td WIDTH="25%">
                        <c:out value="${aqBillReport.pay}"/>
                    </td>
                    <td WIDTH="25%">
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td WIDTH="25%">
                        Dp+P Pay 
                    </td>
                    <td WIDTH="25%">
                        =
                    </td>
                    <td WIDTH="25%">
                        <c:out value="${aqBillReport.dp}"/>
                    </td>
                    <td WIDTH="25%">
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td WIDTH="25%">
                        DA 
                    </td>
                    <td WIDTH="25%">
                        =
                    </td>
                    <td WIDTH="25%">
                        <c:out value="${aqBillReport.da}"/>
                    </td>
                    <td WIDTH="25%">
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td WIDTH="25%">
                        HRA 
                    </td>
                    <td WIDTH="25%">
                        =
                    </td>
                    <td WIDTH="25%">
                        <c:out value="${aqBillReport.hra}"/>
                    </td>
                    <td WIDTH="25%">
                        &nbsp;
                    </td>
                </tr>
                <tr>
                    <td WIDTH="25%">
                        OA 
                    </td>
                    <td WIDTH="25%">
                        =
                    </td>
                    <td WIDTH="25%">
                        <c:out value="${aqBillReport.oa}"/>
                    </td>
                    <td WIDTH="25%">
                        &nbsp;
                    </td>
                </tr>
            </table>
            &nbsp;

        </div>
        <div align="left" id="divIdlast" style="MARGIN-TOP:1PX">
            <table border="0" width="15%">
                <tr>
                    <td WIDTH="100%" colspan="3">
                        <hr> 
                    </td>

                </tr> 
                <tr>
                    <td WIDTH="25%">
                        Total
                    </td>
                    <td WIDTH="25%">
                        =
                    </td>
                    <td WIDTH="50%">
                        <c:out value="${aqBillReport.totAbstract}"/>
                    </td>

                </tr> 
                <tr>
                    <td WIDTH="100%" colspan="3">
                        <hr> 
                    </td>

                </tr> 
            </table>
        </div>   
        <div align="center" id="divIdlast" style="MARGIN-TOP:10PX">
            <table border="0" width="50%">
                <c:forEach items="${dedAbstractList}" var ="dedObj">
                    <tr>
                        <td width="25%">
                            ${dedObj.scheduleName}
                        </td>
                        <td width="25%">
                            ${dedObj.amount}
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>          
    </form:form>
</body>
</html>
