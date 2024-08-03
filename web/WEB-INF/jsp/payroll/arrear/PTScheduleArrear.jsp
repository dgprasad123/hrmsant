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
        <title>:: PT Schedule ::</title>
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
                border-top:1px solid black;
                border-bottom:1px solid black;
                border-left:1px solid black;
                border-right:1px solid black;
            }
        </style>    
    </head>
    <body>
        <div align="center" style="width:97%;font-family:verdana;">
          <table width="100%" border="0">
                <thead></thead>
                <tr>
                    <td align="center" colspan="2" style="font-size:17px;"> <b>SCHEDULE OF RECOVERY OF TAX ON PROFESSION</b> </td>
                </tr>
                <tr>
                    <td align="center" colspan="2" style="font-size:13px;"> FOR THE MONTH OF&nbsp;<b>  <c:out value="${PTHeader.monthYear}"/></b> </td>
                </tr>
                <tr>
                <td align="center" colspan="2" style="font-size:13px;"> DEMAND NO.- 0028-OTHER TAXES ON INCOME AND EXPENDITURE - 107 - TAXES ON </td>
            </tr>
            <tr>
                <td align="center" colspan="2" style="font-size:13px;"> PROFESSION TRADES,CALLINGS AND EMPLOYMENT-9913780-TAXES ON PROFESSION. </td>
            </tr>
            <tr>
                <td> &nbsp; </td>
            </tr>
            </table>    
        </div>
        
        <div align="left" style="font-size:13px; font-family:verdana;">
            <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;" id="innercontainertbl">
            <thead></thead>
            <tr>
                <td width="25%" class="pgHeader">Name of the Department:</td>
                <td width="75%"><c:out value="${PTHeader.deptName}"/></td>
            </tr>
            <tr>
                <td class="pgHeader">Name of the Office:</td>
                <td><c:out value="${PTHeader.officeName}"/></td>
            </tr>
            
            <tr>
                <td class="pgHeader">Designation of DDO:</td>
                <td><c:out value="${PTHeader.ddoDegn}"/></td>
            </tr>
            <tr>
                <td class="pgHeader">Bill No: </td>
                <td><c:out value="${PTHeader.billDesc}"/></td>
            </tr>
            <tr>
                <td class="pgHeader">&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
           </table>
         </div>            
                     
                    
        <div align="center" style="width:97%;font-family:verdana;">
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr style="height:40px">
                <td align="center" width="5%" class="tblHeader">Sl No.</td>
                <td align="center" width="25%" class="tblHeader">Name of the Employee</td>
                <td align="center" width="35%" class="tblHeader">Designation</td>
                <td align="center" width="15%" class="tblHeader">Gross Salary</td>
                <td align="center" width="10%" class="tblHeader">Tax on Profession</td>
                <td align="center" width="10%" class="tblHeader">Remark</td>
            </tr>
        </table>
        
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <c:if test="${not empty PTEmpList}">
            <c:forEach var="eachEmpPt" items="${PTEmpList}">
                <tr style="height:50px">
                    <td width="5%"  class="printData" style="text-align: center;border-bottom:1px solid #000000;">
                        &nbsp;<c:out value="${eachEmpPt.slno}"/> 
                    </td>
                    <td width="25%"  class="printData" style="border-bottom:1px solid #000000;padding-left: 10px;">
                        &nbsp;<c:out value="${eachEmpPt.empname}"/>
                    </td>
                    <td width="35%"  class="printData" style="border-bottom:1px solid #000000;padding-left: 10px;">
                        &nbsp;<c:out value="${eachEmpPt.empdesg}"/>
                    </td>
                    <td width="15%"  class="printData" style="text-align: center;border-bottom:1px solid #000000;">
                        <c:out value="${eachEmpPt.empGrossSal}"/>&nbsp; 
                    </td>
                    <td width="10%"  class="printData" style="text-align: center;border-bottom:1px solid #000000;">
                        &nbsp;<c:out value="${eachEmpPt.empTaxOnProffesion}"/>
                   </td>
                   <td width="10%"  class="printData" style="text-align: center;border-bottom:1px solid #000000;">
                        &nbsp;
                   </td>
                </tr>
                <c:if test="${not empty eachEmpPt.pagebreakPT}">
                    <tr>
                        <td colspan="3" class="pgHeader" style="text-align:right; text-transform: uppercase;">Carry Forward:</td>
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpPt.totalGross}"/> </td> 
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> <c:out value="${eachEmpPt.totalTax}"/> </td> 
                        <td style="text-align:center;padding-left: 10px;font-weight: bold;"> &nbsp; </td>
                    </tr>
                    <tr><td colspan="6"> <hr/> </td></tr>
                    <tr>
                        <td colspan="6" class="pgHeader" style="text-align:right;text-transform: uppercase;">Page No:<%=pageNo++%> </td>
                    </tr>
                </table>
                    <c:out value="${eachEmpPt.pagebreakPT}" escapeXml="false"/>
                    <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">          
                    <c:out value="${eachEmpPt.pageHeaderPT}" escapeXml="false"/>
                </c:if>
            </c:forEach>
        </c:if>
        </table>
        
        <table border="0" cellpadding="0" cellspacing="0" width="100%"> 
            <tr height="20px">
                <td align="left" width="65%" colspan="3"  style="font-size:12px; font-family:verdana; ">
                    <b> TOTAL FOR THE MONTH OF  <c:out value="${PTHeader.monthYear}"/></b></td>
                <td align="center" width="15%" style="font-size:12px; font-family:verdana;padding-left: 10px;">&nbsp;&nbsp;<b><c:out value="${TotGros}"/></b></td>
                <td align="center" width="10%" style="font-size:12px; font-family:verdana;padding-left: 10px;">&nbsp;&nbsp;<b><c:out value="${TotTax}"/></b></td>
                <td align="center" width="10%" style="font-size:12px; font-family:verdana;">&nbsp;</td>
            </tr>
        </table>    
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td colspan="8" height="5px"><hr/></td>
            </tr>
            <tr>
                <td align="center" style="font-size:12px;font-family:verdana;">
                (RUPEES &nbsp;<b><c:out value="${TotalFig}"/></b>&nbsp;) ONLY
                <td>
            </tr>    
        </table>
        <table width="100%" border="0" cellpadding="1" cellspacing="1">
            <thead></thead>
            <tr height="5px">
                <td>&nbsp;</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td width="50%" align="center" style="font-size:10px; font-family:verdana;">Signature of D.D.O.</td>
                <td width="50%">&nbsp;</td>
            </tr>
            <tr>
                <td align="center" style="font-size:10px; font-family:verdana;"><c:out value="${PTHeader.officeName}"/></td>
               <td>&nbsp;</td>
            </tr>
            <tr>
               <td align="center" style="font-size:10px; font-family:verdana;">Date:</td>
                <td>&nbsp;</td>
            </tr>
        </table>  
        
        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <thead></thead>
            <tr>
                <td colspan="2" style="font-size:10px;">To be filled by the Treasury Officer/ Sub Treasury Officer/ Special Treasury Officer</td>
            </tr>
            <tr height="25px">
                <td style="font-size:12px;">a.</td>
                <td style="font-size:12px;"> T.V. No. _____________ and Date ____________ of encashment of Bill</td>
            </tr>
            <tr height="25px">
                <td style="font-size:12px;">b.</td>
                <td style="font-size:12px;"> Sl. No. _____________ and Date ____________ of the receipt Schedule in which accounted by Transfer Credit.</td>
            </tr>
            <tr>
                <td style="font-size:12px;">&nbsp;</td>
                <td style="font-size:12px;" align="right">Page:<%=pageNo++%></td>
            </tr>
        </table>        
               
    </div>    
</body>
</html>
