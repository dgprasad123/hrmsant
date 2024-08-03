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
        <title>:: SWERAGE RENT SCHEDULE ::</title>
    </head>
    <body>
        <div align="center" style="width:97%;font-family:verdana;">
          <table width="100%" border="0">
            <thead></thead>
            <tr>
                <td align="center" colspan="2" style="font-size:17px;"> <b><c:out value="${WRRHeader.reportName}"/></b> </td>
            </tr>  
            <tr>
                <td align="center" colspan="2" style="font-size:17px;"> <b>0215-WATER SUPPLY AND SANITATION-01-WATER SUPPLY-103-RECEIPTS FROM</b> </td>
            </tr>
	    <tr>
                <td align="center" colspan="2" style="font-size:17px;"> <b>URBAN WATER SUPPLY SCHEMES-0175-Water Rate / Cess-02171-Water Supply for</b> </td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="font-size:17px;">
                    <b>supply of drinking water&nbsp;&nbsp;&nbsp;<c:out value="${WRRHeader.poolName}"/></b>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="font-size:13px;">FOR THE MONTH OF&nbsp;
                   <b><c:out value="${WRRHeader.txtMonth}"/>-<c:out value="${WRRHeader.txtYear}"/></b>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="font-size:13px;"> <c:out value="${WRRHeader.demandNo}"/> </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr style="font-size:13px;">
                <td width="20%" align="left"> Name of the Department: </td>
                <td width="80%"align="left"> <c:out value="${WRRHeader.deptName}"/> </td>
            </tr>
            <tr style="font-size:13px;"> 
                <td width="20%" align="left"> Name of the Office: </td>
                <td  width="80%" align="left"> <c:out value="${WRRHeader.officeName}"/> </td>
            </tr>
            <tr style="font-size:13px;">
                <td width="20%" align="left"> Designation of DDO: </td>
                <td width="80%" align="left"> <c:out value="${WRRHeader.ddoDegn}"/> </td>
            </tr>
            <tr style="font-size:13px;">
                <td width="20%" align="left" > Bill No: </td>
                <td  width="80%" align="left"> <c:out value="${WRRHeader.billDesc}"/> </td>
            </tr>
          </table>    
        </div>
        
        <div align="center" style="width:97%;font-family:verdana;">
        <table border="1" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr style="height: 30px">
                <th width="8%" style="text-align: center"> Sl. No. </th>
                <th width="15%" style="text-align: center"> GPF No. </th>
                <th width="25%" style="text-align: center"> Name of the Employee/Designation </th>
                <th width="12%" style="text-align: center"> Amount Recovered </th>
                <th width="17%" style="text-align: center"> Quarter No. & Address </th>
            </tr>
        </table>
        
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <c:if test="${not empty WRREmpList}">
            <c:forEach var="eachEmpWrr" items="${WRREmpList}">
                <tr style="height:55px">
                    <td width="8%"  class="printData" style="text-align: center;border-bottom:1px solid #000000;">
                        <c:out value="${eachEmpWrr.slNo}"/>&nbsp; 
                    </td>
                    <td width="15%"  class="printData" style="text-align: center;border-bottom:1px solid #000000;">
                        <c:out value="${eachEmpWrr.gpfNo}"/>&nbsp;
                    </td>
                    <td width="25%"  class="printData" style="text-align: center;border-bottom:1px solid #000000;">
                        <c:out value="${eachEmpWrr.empname}"/><br>
                        <c:out value="${eachEmpWrr.empdesg}"/>
                    </td>
                    <td width="12%"  class="printData" style="text-align: center;border-bottom:1px solid #000000;">
                        <c:out value="${eachEmpWrr.amount}"/>&nbsp; 
                    </td>
                    <td width="17%" class="printData" style="text-align: center;border-bottom:1px solid #000000;">
                        <c:out value="${eachEmpWrr.quarterNo}"/><br>
                        <c:out value="${eachEmpWrr.address}"/><br>
                   </td>
                </tr>
                <c:if test="${not empty eachEmpWrr.pagebreakWR}">
                    <tr>
                        <td colspan="3" class="pgHeader" style="text-align:right; text-transform: uppercase;">Carry Forward:</td>
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpWrr.carryForward}"/> </td> 
                        <td>&nbsp;</td>
                    </tr>
                    <tr><td colspan="5"> <hr/> </td></tr>
                    <tr>
                        <td colspan="5" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page No:<%=pageNo++%> </td>
                    </tr>
                </table>
                    <c:out value="${eachEmpWrr.pagebreakWR}" escapeXml="false"/>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">          
                    <c:out value="${eachEmpWrr.pageHeaderWR}" escapeXml="false"/>
                </c:if>
            </c:forEach>
        </c:if>
        </table>
              
                
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr>
                <td colspan="5" height="5px"><hr/></td>
            </tr>
            <tr>
                <td width="23%" colspan="2" style="text-align: left;font-family:verdana;">
                    TOTAL FOR THE MONTH OF&nbsp;<c:out value="${WRRHeader.txtMonth}"/>-<c:out value="${WRRHeader.txtYear}"/>&nbsp;</td>
                <td width="25%" style="text-align: center">&nbsp;</td>
                <td width="12%" style="text-align: center;padding-left: 10px;"><b><c:out value="${Amount}"/></b></td>                            
                <td width="17%" style="text-align: center">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="5" height="5px"><hr/></td>
            </tr>
            <tr>
                <td colspan="3" style="text-align: center">&nbsp;</td> 
                <td colspan="2" height="5px" style="text-align:left;font-family:verdana;" class="txtf">
                    Rupees ( <b><c:out value="${TotalFig}"/></b> )&nbsp;Only
                </td>
            </tr>
            <tr>
                <td colspan="5" height="5px"><hr/></td>
            </tr>
        </table>
        
        <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <thead></thead>
            <tr style="height: 55px">
                <td colspan="2">&nbsp;</td>
            </tr>
        </table>
        <table width="100%" border="0" style="font-size:12px;font-family:verdana;">
            <thead></thead>
            <tr>
                <td>&nbsp;</td>
                <td style="text-align: center" class="printData"> Signature of D.D.O.<br> <c:out value="${offName}"/> </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td style="text-align: center" class="printData"> Date : </td>
            </tr>
            <tr>
                <td width="76%" class="printData">
                    Note:1.&nbsp;Recoveries in respect of advance drawn and disbursed by the Drawing Officer who has taken recovery.<br>
                    Note:2.&nbsp;Recoveries in respect of advance drawn and paid by the Drawing Officer, Taken to state correct policy no.<br><br>
                    Certificates:1.&nbsp;Certified that the total recoveries whom in column where agree with the amount actually as shown in the 
                    body of the bill.<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2.&nbsp;Certified that the recoveries affected have duly used in the register of advance(****).
                </td>
                <td width="24%">&nbsp;</td>
            </tr>
        </table>        
        <table width="100%" border="0"  style="left: 18px;">
            <thead> </thead>
            <tr>
                <td colspan="3" height="5px"><hr/></td>
            </tr>
            <tr>
                <td class="printData">&nbsp;</td>
                <td class="printFooter" style="text-align:center">&nbsp;</td>
                <td class="printData" style="text-align:right; text-transform: uppercase;font-size: 11px;">Page:<%=pageNo++%> </td>
            </tr>
        </table>        
                
    </div>    
</body>
</html>
