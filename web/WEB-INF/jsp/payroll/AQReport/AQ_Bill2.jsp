
<%@page import="hrms.common.Numtowordconvertion"%>
<%@page import="hrms.common.AqFunctionalities"%>
<%@page import="hrms.model.payroll.schedule.SectionWiseAqBean"%>
<%@page import="hrms.model.payroll.schedule.ADDetailsHealperBean"%>
<%@page import="hrms.model.payroll.schedule.AqreportHelperBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
                        <td width="50%" align="center">NON GOVT. AIDED SCHEDULE-A STATE HEAD QUATERS FORM NO-58 <br />
                            PAY BILL FOR <c:out value="${aqBillReport.offen}"/><br/>
                            MONTHLY PAY BILL FOR <c:out  value="${aqBillReport.month}"/>-<c:out value="${aqBillReport.year}"/></td>
                        <td width="15%" align="center">BILL NO:<c:out value="${aqBillReport.billdesc}" /><br> BILL DT:<c:out value="${aqBillReport.billdate}" /></td>
                        <td width="7%">PAGE:1</td>
                    </tr>

                </table>
                <table width="100%" style="border-top: 0;border-bottom: 0" id="tableData">
                    <tr>
                        <td width="2%">SL NO</td>
                        <td width="11%">NAME/<br/>DESG/<br/>PAY SCALE </td>
                        <td width="4%">BASIC<br/>SPL PAY<br/>GP<br/>IR</td>
                        <td width="4%">DP<br/>P.PAY</td>
                        <td width="2%">DA</td>
                        <td width="3%">HRA</td>
                        <td width="4%">OTHER<br/>ALLOWANCE</td>
                        <td width="5%">GROSS<br/>PAY</td>
                        <td width="5%"> ${column9NameList} </td>
                        <td width="5%"> ${column10NameList} </td>
                        <td width="4%"> ${column11NameList} </td>
                        <td width="5%"> ${column12NameList} </td>
                        <td width="4%"> ${column13NameList} </td>
                        <td width="7%"> ${column14NameList} </td>
                        <td width="5%"> ${column15NameList} </td>
                        <td width="5%"> ${column16NameList} </td>
                        <td width="4%"> ${column17NameList} </td>
                        <td width="5%"> ${column18NameList} </td>
                        <td width="5%">TOTAL<br/>DEDN</td>
                        <td width="4%">NET PAY </td>
                        <td width="7%">REMARKS<br/> A/C NO </td>
                    </tr>
                    <tr>
                        <td width="2%" >(1)</td>
                        <td width="11%">(2)</td>
                        <td width="4%">(3)</td>
                        <td width="4%">(4)</td>
                        <td width="2%">(5)</td>
                        <td width="3%">(6)</td>
                        <td width="4%">(7)</td>
                        <td width="5%">(8)</td>
                        <td width="5%">(9)</td>
                        <td width="5%">(10)</td>
                        <td width="4%">(11)</td>
                        <td width="5%">(12)</td>
                        <td width="4%">(13)</td>
                        <td width="7%">(14)</td>
                        <td width="5%">(15)</td>
                        <td width="5%">(16)</td>
                        <td width="4%">(17)</td>
                        <td width="5%">(18)</td>
                        <td width="5%">(19)</td>
                        <td width="4%">(20)</td>
                        <td width="7%">(21)</td>
                    </tr>
                </table>
                <c:set var="month"  value="${aqBillReport.month}"/>
                <c:set  value="${0}" var="aqlistbasic" />
                <c:set var="aqGrandDeduction" value="${0}"/>
                <c:set var="aqGrandNet" value="${0}"/>

                <c:forEach var="aqdata1"  items="${aqBillReport.aqlist}"> 
                    <c:set  value="${aqdata1.aqlistSectionWise}" var="aqlistSectionWise" />
                    <%
                        int rowcnt = 0;
                        ctr++;
                        int sectionPageCount = 0;
                    %>
                    <c:out value = "${aqlistSectionWise.size()}"/>                    
                    <table width="100%" border="0" id="tableData"> 

                        <c:set value="${aqdata1.aqlistSectionWise}" var="aqlistSectionWise" />
                        <c:out value = "${aqdata1.sectionname}"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <c:if test="${aqdata1.sectionwiseOffEn ne ''}">
                            <c:out value = "${aqdata1.sectionwiseOffEn}"/>
                        </c:if>
                        <c:set var="alsize" value = "${aqlistSectionWise.size()}"/>
                        <%
                            sectionPageCount++;
                        %>
                        <c:set var="aqBasic" value="${0}"/>
                        <c:set var="aqDA" value="${0}"/>
                        <c:set var="aqGross" value="${0}"/>
                        <c:set var="aqDeduction" value="${0}"/>
                        <c:set var="aqNet" value="${0}"/>

                        <c:forEach var="aqData"  begin="0" end="${aqlistSectionWise.size()}" items="${aqlistSectionWise}" >

                            <c:set var="aqBasic" value="${aqData.basic+aqBasic}"/>                            


                            <tr style="height: 70px;">
                                <td width="2%" valign="top"><c:out value="${aqData.slno}" /></td>
                                <td width="11%" style="text-align:left">
                                    <c:out value="${aqData.empname}" /><br/>
                                    <c:out value="${aqData.desg}" /><br/>
                                    <c:out value="${aqData.payscale}" /><br/>
                                    <c:out value="${aqData.gpfacct}" />
                                </td>
                                <td width="4%">
                                    <c:out value="${aqData.basic}" />
                                    <c:forEach items="${aqData.col3}" var="ahb">
                                        <div>${ahb.adamt}</div>
                                    </c:forEach>                            
                                </td>
                                <td width="4%">
                                    <c:forEach items="${aqData.col4}" var="ahb">                                        
                                        <div>${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="2%" style="text-align:left;">
                                    <c:forEach items="${aqData.col5}" var="ahb">                                       
                                        <div>${ahb.adamt}</div>                                        
                                        <c:set var="aqDA" value="${ahb.adamt+aqDA}"/>
                                    </c:forEach>
                                </td>
                                <td width="3%" style="text-align:left;"> 
                                    <c:forEach items="${aqData.col6}" var="ahb">
                                        <div>${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="3%" style="text-align:left;">
                                    <c:forEach items="${aqData.col7}" var="ahb">
                                        <div>${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%" style="text-align:left;"> 
                                    <c:forEach items="${aqData.col8}" var="ahb">
                                        <div>${ahb.adamt}</div>
                                        <c:set var="aqGross" value="${ahb.adamt+aqGross}"/>
                                    </c:forEach>
                                </td>
                                <td width="6%" style="text-align:left;">
                                    <c:forEach items="${aqData.col9}" var="ahb">
                                        <div>${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="6%" style="text-align:left;">
                                    <c:forEach items="${aqData.col10}" var="ahb">
                                        <div>${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="4%" style="text-align:left;">
                                    <c:forEach items="${aqData.col11}" var="ahb">
                                        <div>${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%" style="text-align:left;">
                                    <c:forEach items="${aqData.col12}" var="ahb">
                                        <div>${ahb.adamt}</div>
                                    </c:forEach>
                                </td>
                                <td width="4%" style="text-align:left;">
                                    <c:forEach items="${aqData.col13}" var="ahb">
                                        <div>${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="8%" style="text-align:left;">
                                    <c:forEach items="${aqData.col14}" var="ahb">
                                        <div>${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="4%" style="text-align:left;">
                                    <c:forEach items="${aqData.col15}" var="ahb">
                                        <div>${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="4%" style="text-align:left;">
                                    <c:forEach items="${aqData.col16}" var="ahb">
                                        <div>${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="2%" style="text-align:left;">
                                    <c:forEach items="${aqData.col17}" var="ahb">
                                        <div>${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="4%" style="text-align:left;">
                                    <c:forEach items="${aqData.col18}" var="ahb">
                                        <div>${ahb.adamt}${ahb.refdesc}</div>
                                    </c:forEach>
                                </td>
                                <td width="5%" style="text-align:left;">
                                    <c:forEach items="${aqData.col19}" var="ahb">
                                        <div>${ahb.adamt}</div>                                       
                                        <c:set var="aqDeduction" value="${ahb.adamt+aqDeduction}"/>                                      

                                    </c:forEach>
                                </td>
                                <td width="3%" style="text-align:left;">
                                    <c:forEach items="${aqData.col20}" var="ahb">
                                        <div>${ahb.adamt}</div>                                        
                                        <c:set var="aqNet" value="${ahb.adamt+aqNet}"/>                                       
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
                    <tr style="font-weight: bold; font-size: 40px;">
                        <td width="2%"></td>
                        <td width="11%">Total:</td>
                        <td width="4%">${aqBasic}</td>
                        <td width="4%"></td>
                        <td width="4%" style="text-align:left;">${aqDA}</td>
                        <td width="2%"></td>
                        <td width="2%"></td>
                        <td width="4%" style="text-align:left;">${aqGross}</td>
                        <td width="4%"></td>
                        <td width="4%"></td>
                        <td width="4%"></td>
                        <td width="4%"></td>
                        <td width="4%"></td>
                        <td width="4%"></td>
                        <td width="4%"></td>
                        <td width="4%"></td>
                        <td width="4%"></td>
                        <td width="4%"></td>
                        <td width="4%" style="text-align:left;">${aqDeduction}</td>
                        <td width="4%" style="text-align:left;">${aqNet}</td>
                        <td width="4%"></td>
                    </tr>

                </table>
                <c:set var="aqGrandDeduction" value="${aqGrandDeduction+aqDeduction}"/>
                <c:set var="aqGrandNet" value="${aqGrandNet+aqNet}"/>                

            </c:forEach>

            <table width="100%" border="1">
                <tr>
                    <td width="13%" colspan="2" >GRAND TOTAL: </td>

                    <td width="4%" ><c:out value="${aqBillReport.col3Tot}"/></td>
                    <td width="4%"><c:out value="${aqBillReport.col4Tot}"/></td>
                    <td width="4%" style="text-align:left;"><c:out value="${aqBillReport.col5Tot}"/></td>      
                    <td width="3%" style="text-align:left;"><c:out value="${aqBillReport.col6Tot}"/></td>
                    <td width="3%" style="text-align:left;"><c:out value="${aqBillReport.col7Tot}"/></td>
                    <td width="5%" style="text-align:left;"><c:out value="${aqBillReport.col8Tot}"/></td>
                    <td width="6%"><c:out value="${aqBillReport.col9Tot}"/></td>
                    <td width="6%"><c:out value="${aqBillReport.col10Tot}"/></td>
                    <td width="4%" style="text-align:left;"><c:out value="${aqBillReport.col11Tot}"/></td>
                    <td width="5%"><c:out value="${aqBillReport.col12Tot}"/></td>
                    <td width="4%"><c:out value="${aqBillReport.col13Tot}"/></td>
                    <td width="7%"><c:out value="${aqBillReport.col14Tot}"/></td>
                    <td width="4%"><c:out value="${aqBillReport.col15Tot}"/></td>
                    <td width="4%"><c:out value="${aqBillReport.col16Tot}"/></td>
                    <td width="4%"><c:out value="${aqBillReport.col17Tot}"/></td>
                    <td width="5%"><c:out value="${aqBillReport.col18Tot}"/></td>
                    <td width="5%" style="text-align:left;"><c:out value="${aqGrandDeduction}"/></td>
                    <td width="4%" style="text-align:left;"><c:out value="${aqGrandNet}"/></td>
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
            <table border="0" width="20%">
                <c:forEach items="${dedAbstractList}" var ="dedObj">
                    <c:if test="${dedObj.scheduleName eq 'PROFESSIONAL TAX'}">
                        <%-- <tr>
                          <td width="25%">
                              ${dedObj.scheduleName}
                          </td>
                          <td width="25%">
                              ${dedObj.amount}
                          </td>
                      </tr>  --%>
                        <tr>
                            <td width="35%">
                                ${dedObj.scheduleName}
                            </td>
                            <td WIDTH="15%">
                                =
                            </td>
                            <td width="50%">
                                <c:out value="${aqBillReport.col11Tot}"/>
                            </td>
                        </tr>
                        <tr>
                            <td WIDTH="100%" colspan="3">
                                <hr> 
                            </td>

                        </tr> 
                        <tr>
                            <td WIDTH="35%">
                                Total Deduction
                            </td>
                            <td WIDTH="15%">
                                =
                            </td>

                            <td WIDTH="50%">
                                <c:out value="${aqGrandDeduction}"/>
                            </td>
                        </tr>
                        <tr>
                            <td WIDTH="100%" colspan="3">
                                <hr> 
                            </td>

                        </tr> 
                    </c:if>
                </c:forEach>
            </table>
        </div>        
    </form:form>
</body>
</html>
