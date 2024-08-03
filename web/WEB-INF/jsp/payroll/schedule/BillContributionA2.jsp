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
        <title>:: Annexure-2 ::</title>
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
                <td style="text-align:center;font-size:15px;font-weight:bold;"><u>ANNEXURE- II</u></td>
            </tr>
            <tr>
                <td  style="text-align:center;font-size:15px;"> FORMAT OF SCHEDULE OF GOVERNMENT SERVANT'S CONTRIBUTIONS </td>
            </tr>
            <tr>
                <td style="text-align:center;font-size:15px;"> TOWARDS TIER-I OF THE NEW PENSION SCHEME </td>
            </tr>
            <tr>
                <td style="text-align:center;font-size:15px;"> (TO BE  ATTACHED WITH THE PAY BILL FOR DRAWAL OF GOVT.CONTRIBUTION) </td>
            </tr>
        </table>
    </div>
    
    <div style="width:99%;margin: 0 auto;font-size:13px; font-family:verdana;">
        <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">
            <thead></thead>
            <tr style="height:23px;">
                <td width="25%" class="pgHeader">BILL NO / DATE:</td>
                <td width="40%"><c:out value="${NPSHeader.billDesc}"/>&nbsp;/&nbsp;<c:out value="${NPSHeader.billDate}"/></td>
                <td width="15%" class="pgHeader">MONTH / YEAR:</td>
                <td width="15%"><c:out value="${NPSHeader.billMonth}"/>&nbsp;/&nbsp;<c:out value="${NPSHeader.billYear}"/></td>
            </tr>
            <tr style="height:23px;">
                <td class="pgHeader">NAME OF THE DDO / REGISTRATION NO:</td>
                <td><c:out value="${NPSHeader.ddoName}"/>&nbsp;/&nbsp;<c:out value="${NPSHeader.ddoRegdNo}"/></td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr style="height:23px;">
                <td class="pgHeader">NAME OF OFFICE & ADDRESS:</td>
                <td><c:out value="${NPSHeader.offName}"/></td>
                <td class="pgHeader">DTO REGISTRATION NO:</td>
                <td><c:out value="${NPSHeader.dtoRegdNo}"/></td>
            </tr>
            <tr style="height:23px;">
                <td colspan="4">&nbsp;</td>
            </tr>
        </table>
            
        <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
            <tr>
                <td width="4%" class="tblHeader">Sl No</td>
                <td width="12%" class="tblHeader">PRAN</td>
                <td width="22%" class="tblHeader">Employee Name</td>
                <td width="20%" class="tblHeader">Designation</td>
                <td width="10%" class="tblHeader">Basic Pay<br> + GP (Rs.)</td>
                <td width="8%" class="tblHeader">D.A<br>(Rs.)</td>
                <td width="15%" class="tblHeader">Govt's Contribution</td>
                <td width="14%" class="tblHeader">Remarks</td>
            </tr>
            <tr>
                <td class="tblHeader">1</td>
                <td class="tblHeader">2</td>
                <td class="tblHeader">3</td>
                <td class="tblHeader">4</td>
                <td class="tblHeader">5</td>
                <td class="tblHeader">6</td>
                <td class="tblHeader">7</td>
                <td class="tblHeader">8</td>
            </tr>
            <c:if test="${not empty NpsEmpList}">
            <c:forEach var="eachEmpNps" items="${NpsEmpList}">
                <tr style="height:35px">
                    <td align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpNps.slno}"/>&nbsp;</td>
                    <td align="left" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpNps.gpfNo}"/></td>
                    <td align="left" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpNps.empname}"/></td>
                    <td align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpNps.empdesg}"/>&nbsp;</td>
                    <td align="center" style="border-bottom:1px solid #000000;">&nbsp;
                        <c:out value="${eachEmpNps.empBasicSal}"/>&nbsp; </br><c:out value="${eachEmpNps.empGradepay}"/>
                    </td>
                    <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpNps.empDearnespay}"/>&nbsp;</td>
                    <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpNps.empGcpf}"/></td>
                    <td align="center" style="border-bottom:1px solid #000000;">&nbsp;</td>
                </tr>
                <c:if test="${not empty eachEmpNps.pagebreakAnx}">
                    <tr>
                        <td colspan="6" class="pgHeader" style="text-align:right; text-transform: uppercase;">Carry Forward:</td>
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpNps.totCaryFrd}"/> </td> 
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> &nbsp; </td> 
                    </tr>
                    <tr><td colspan="8"> <hr/> </td></tr>
                    <tr>
                        <td colspan="8" class="pgHeader" style="text-align:right;text-transform: uppercase;">Page No:<%=pageNo++%> </td>
                    </tr>
                </table>
                    <c:out value="${eachEmpNps.pagebreakAnx}" escapeXml="false"/>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">          
                    <c:out value="${eachEmpNps.pageHeaderAnx}" escapeXml="false"/>
                </c:if>
            </c:forEach>
            </c:if>
        </table>
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr style="height:30px">
                <td width="4%">&nbsp;</td>
                <td width="12%">&nbsp;</td>
                <td width="22%">&nbsp;</td>
                <td width="17%">&nbsp;</td>
                <td width="12%">&nbsp;</td>
                <td width="8%"><b>Grand Total</b></td>
                <td width="15%" align="center"><b><c:out value="${TotGcpf}"/></b></td>
                <td width="15%">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="8" height="5px"><hr/></td>
            </tr>
            <tr style="height:30px">
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td colspan="2">RUPEES&nbsp;<b><c:out value="${TotGcpfFig}"/></b>&nbsp;ONLY</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td colspan="8" height="5px"><hr/></td>
            </tr>
        </table>    
        
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr width="100%">
                <td colspan="11" style="font-size:13px;">
                    <div style="margin-top:8px;margin-bottom:8px;">
                        <span style="padding-left:20px;">
                            The Basic pay entered in the column 5 of the above statement has been verified with
                            the entries made in the Service Book Pay Bill.
                        </span>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="11">
                    <div style="margin-top:8px;margin-bottom:8px;">
                        <span style="padding-left:20px;">
                            (Rupees ..........................................................................................)</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="11" style="text-align: left;font-size:13px;">
                    <div style="margin-top:8px;margin-bottom:8px;">
                        <span style="padding-left:20px;">This is to certify that the employees mentioned above is/are appointed in a
                                                        pensionable establishment request
                        </span>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="11" style="text-align: left;font-size:13px;">
                    <div style="margin-top:8px;margin-bottom:8px;">
                        <span style="padding-left:20px;">vacancy/vacancies</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="11" width="25%" style="text-align:center;font-size:14px;" class="txtf">
                    <div style="margin-top:8px;margin-bottom:8px;">
                        <span style="padding-left:500px;">Signature</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="11" width="25%" style="text-align:center;font-size:14px;" class="txtf">
                    <div style="margin-top:8px;margin-bottom:8px;">
                        <span style="padding-left:500px;">Drawing and Disbursing Officer</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="11" width="25%" style="text-align:center;font-size:14px;" class="txtf">
                    <div style="margin-top:8px;margin-bottom:8px;">
                        <span style="padding-left:500px;">With the Designation and Date</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="11" width="25%" style="text-align:center;font-size:14px;" class="txtf">
                    <div style="margin-top:8px;margin-bottom:8px;">
                        <span style="padding-left:500px;">(With Seal)</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td align="right" colspan="5" style="font-size:12px;font-family:verdana;">Page No.<%=pageNo++%></td>
            </tr>
            
            
            
        </table> 
            
            
            
            
        
    </div>  
</body>
</html>
