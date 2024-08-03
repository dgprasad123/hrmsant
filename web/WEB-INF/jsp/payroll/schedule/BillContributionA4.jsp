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
        <title>:: Annexure-4 ::</title>
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
                <td style="text-align:center;font-size:15px;font-weight:bold;"><u>ANNEXURE- IV</u></td>
            </tr>
            <tr>
                <td  style="text-align:center;font-size:15px;"> FORMAT IN WHICH INFORMATION ON CONTRIBUTIONS IS REQUIRED TO BE SENT BY </td>
            </tr>
            <tr>
                <td style="text-align:center;font-size:15px;"> TREASURY OFFICER TO THE TRUSTEE BANK.</td>
            </tr>
        </table>
    </div>
    
    <div style="width:99%;margin: 0 auto;font-size:13px; font-family:verdana;">
        <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">
            <thead></thead>
            <tr style="height:23px;">
                <td width="20%" class="pgHeader">NAME OF THE DDO / REGISTRATION NO:</td>
                <td width="35%"><c:out value="${NPSHeader.ddoName}"/>&nbsp;/&nbsp;<c:out value="${NPSHeader.ddoRegdNo}"/></td>
                <td width="15%" class="pgHeader">BILL NO / DATE:</td>
                <td width="20%"><c:out value="${NPSHeader.billDesc}"/>&nbsp;/&nbsp;<c:out value="${NPSHeader.billDate}"/></td>
            </tr>
            <tr style="height:23px;">
                <td class="pgHeader">DEDUCTION FOR THE MONTH/YEAR OF:</td>
                <td><c:out value="${NPSHeader.billMonth}"/>&nbsp;/&nbsp;<c:out value="${NPSHeader.billYear}"/></td>
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
                <td width="10%" class="tblHeader">Treasury Officers <br/>Regd No(issued by CRA)</td>
                <td width="17%" class="tblHeader">PRAN</td>
                <td width="25%" class="tblHeader">Employee Name</td>
                <td width="10%" class="tblHeader">Basic Pay + GP + DA Rs.</td>
                <td width="10%" class="tblHeader">Employees Contribution under Tier-I Rs.</td>
                <td width="10%" class="tblHeader">Government Contribution under Tier-I Rs.</td>
                <td width="10%" class="tblHeader">Total</td>
                <td width="10%" class="tblHeader">Remark</td>
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
                <td class="tblHeader">9</td>
            </tr>
            <c:if test="${not empty NpsEmpList}">
            <c:forEach var="eachEmpNps" items="${NpsEmpList}">
                <tr style="height:35px">
                    <td align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpNps.slno}"/>&nbsp;</td>
                    <td align="left" style="border-bottom:1px solid #000000;">&nbsp;</td>
                    <td align="left" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpNps.gpfNo}"/></td>
                    <td align="left" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpNps.empname}"/></td>
                    <td align="center" style="border-bottom:1px solid #000000;">&nbsp;
                        <c:out value="${eachEmpNps.empBasicSal}"/>&nbsp;+&nbsp;<c:out value="${eachEmpNps.empGradepay}"/>&nbsp;+&nbsp;
                        </br><c:out value="${eachEmpNps.empDearnespay}"/>&nbsp;
                    </td>
                    <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpNps.empCpf + eachEmpNps.arrearAmt}"/></td>
                    <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpNps.empGcpf}"/></td>
                    <td align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpNps.totalAnnexure3}"/> </td>
                    <td align="center" style="border-bottom:1px solid #000000;">&nbsp; </td>
                </tr>
                <c:if test="${not empty eachEmpNps.pagebreakAnx}">
                    <tr>
                        <td colspan="5" class="pgHeader" style="text-align:right; text-transform: uppercase;">Carry Forward:</td>
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpNps.totCaryFrd}"/> </td> 
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpNps.totCaryFrd}"/> </td> 
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpNps.grandTotal}"/> </td> 
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> &nbsp; </td>
                    </tr>
                    <tr><td colspan="9"> <hr/> </td></tr>
                    <tr>
                        <td colspan="9" class="pgHeader" style="text-align:right;text-transform: uppercase;">Page No:<%=pageNo++%> </td>
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
                <td width="10%">&nbsp;</td>
                <td width="25%">&nbsp;</td>
                <td width="17%">&nbsp;</td>
                <td width="10%" align="center"><b>Grand Total</b></td>
                <td width="10%" align="center"><b><c:out value="${TotCpf}"/></b></td>
                <td width="10%" align="center"><b><c:out value="${TotGcpf}"/></b></td>
                <td width="10%" align="center"><b><c:out value="${GrandTot}"/></b></td>
                <td width="10%">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="9" height="5px"><hr/></td>
            </tr>
            <tr style="height:30px">
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td colspan="2">RUPEES&nbsp;<b><c:out value="${TotCpfFig}"/></b>&nbsp;ONLY</td>
                <td colspan="2">RUPEES&nbsp;<b><c:out value="${TotGcpfFig}"/></b>&nbsp;ONLY</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td colspan="9" height="5px"><hr/></td>
            </tr>
        </table>    
        
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr>
                <td colspan="9" height="5px">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="11" width="25%" style="text-align:center;font-size:14px;" class="txtf">
                    <div style="margin-top:8px;margin-bottom:8px;">
                        <span style="padding-left:500px;">Treasury Officer</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="11" width="25%" style="text-align:center;font-size:14px;" class="txtf">
                    <div style="margin-top:8px;margin-bottom:8px;">
                        <span style="padding-left:500px;"><c:out value="${NPSHeader.treasuryName}"/></span>
                    </div>
                </td>
            </tr>
            
            <tr>
                <td align="right" colspan="5" style="font-size:12px;font-family:verdana;">Page No.<%=pageNo%><%pageNo++;%></td>
            </tr>
            
            
            
        </table> 
            
            
            
            
        
    </div>  
</body>
</html>
