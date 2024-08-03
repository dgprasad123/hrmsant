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
    </head>
    <body>

        <div align="center" style="font-size:15px; font-family:verdana;font-weight:bold;">
            <b><c:out value="${ITHeader.officeName}"/></b>
        </div>
        <div align="center" style="font-size:15px; font-family:verdana;">
            <b><c:out value="${ITHeader.scheduleName}"/></b>
        </div>
        <div align="center" style="font-size:15px; font-family:verdana;">
            MEMORANDUM INDICATING THE AMOUNTS CREDITABLE
        </div>
        <div align="center" style="font-size:15px; font-family:verdana;">
            TO CENTRAL AS SHOWN BELOW
        </div>
        <div align="center" style="font-size:12px; font-family:verdana;">
            FOR THE MONTH OF&nbsp; 
            <b><c:out value="${ITHeader.aqmonth}"/> - <c:out value="${ITHeader.aqyear}"/></b>
        </div>

        <div align="center" style="font-size:10px; font-family:verdana;"  >
            <table  width="99%" border="0" cellspacing="0" style="font-size:12px; font-family:verdana;" id="innercontainertbl">
                <thead></thead>
                <tr style="height:40px">
                    <td width="30%"></td> 
                    <td width="69%"></td>
                </tr>
                <tr>
                    <td>Bill No:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><c:out value="${ITHeader.billNo}"/></b></td>
                    <td>TAN No:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b><c:out value="${ITHeader.tanno}"/></b></td>
                </tr>
                <tr>
                    <td>
                        &nbsp;&nbsp;<b><c:out value="${ITHeader.btId}"/> - INCOME TAX DEDUCTION</b>
                    </td>
                </tr>
            </table>
        </div>

        <div align="center" style="width:99%;font-family:verdana;">
            <table border="1" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                <tr style="height: 30px">
                    <th width="5%" style="text-align: center">Sl. No.</th>
                    <th width="20%" style="text-align: center">Name and Designation of Employee</th>
                    <th width="10%" style="text-align: center">PAN No</th>
                    <th width="10%" style="text-align: center">Gross Salary</th>
                    <th width="10%" style="text-align: center">Deduction</th>
                </tr>
            </table>
            <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                <c:if test="${not empty ITEmpList}">
                    <c:forEach var="eachEmpIt" items="${ITEmpList}">
                        <tr style="height:30px">
                            <td width="5%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpIt.slno}"/></td>
                            <td width="20%" align="center" style="border-bottom:1px solid #000000;">&nbsp;
                                <c:out value="${eachEmpIt.empname}"/><br><c:out value="${eachEmpIt.empdesg}"/>
                            </td>
                            <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpIt.empPanNo}"/></td>
                            <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpIt.empBasicSal}"/></td>
                            <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpIt.empDedutAmount}"/>&nbsp;</td>
                        </tr>
                        <c:if test="${not empty eachEmpIt.pagebreakIT}">
                            <tr>
                                <td colspan="4" class="pgHeader" style="text-align:right; text-transform: uppercase;">Carry Forward:</td>
                                <td style="text-align:center;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpIt.carryForward}"/> </td> 
                            </tr>
                            <tr><td colspan="5"> <hr/> </td></tr>
                            <tr>
                                <td colspan="5" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page No:<%=pageNo++%> </td>
                            </tr>
                        </table>
                        <c:out value="${eachEmpIt.pagebreakIT}" escapeXml="false"/>
                        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">          
                            <c:out value="${eachEmpIt.pageHeaderIT}" escapeXml="false"/>
                        </c:if>
                    </c:forEach>
                </c:if>        
            </table>

            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                <tr>
                    <td colspan="4" width="35%" align="right" style="font-size:12px;font-family:verdana;"><b>* Grand Total *</b>&nbsp;</td>
                    <td width="10%" align="center" style="font-size:12px;font-family:verdana;"><b><c:out value="${DedutAmount}"/></b></td>
                </tr>
                <tr>
                    <td align="center" colspan="5" style="font-size:12px;font-family:verdana;">
                        In Words( &nbsp;RUPEES&nbsp;&nbsp;<b><c:out value="${TotalFig}"/></b>)&nbsp;&nbsp;ONLY
                    </td>
                </tr>
                <tr style="height: 25px">
                    <td colspan="5">&nbsp;</td>
                </tr>
                <tr>
                    <td align="center" colspan="5" style="font-size:12px;font-family:verdana;">
                        Designation of the Drawing Officer:
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="5" style="font-size:12px;font-family:verdana;">
                        <c:out value="${ITHeader.ddoDegn}"/>
                    </td>
                </tr>
                <tr>
                    <td align="right" colspan="5" style="font-size:12px;font-family:verdana;">Page No.<%=pageNo++%></td>
                </tr>

            </table>
        </div>    
    </body>
</html>
