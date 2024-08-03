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
        <title>:: VE Schedule ::</title>
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
    <div style="width:90%;margin: 0 auto;">
        <table width="100%" border="0">
            <tr>
                <td colspan="2" style="text-align:center" class="printData">
                    <b><c:out value="${AllHeader.scheduleOfRecovery}"/></b>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:center" class="printData">
                    <b> OF <c:out value="${AllHeader.deptName}"/></b>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:center" class="printData">
                    FOR THE MONTH OF <c:out value="${AllHeader.month}"/>-<c:out value="${AllHeader.year}"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:center" class="printData">
                    Demand No :<c:out value="${AllHeader.demandNo}"/> &nbsp; <c:out value="${AllHeader.scheduleName}"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:center" class="printData">
                    <b>T.V No / Date:</b>&nbsp; <c:out value="${VchNo}"/> / <c:out value="${VchDate}"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:center">---------- * ---------</td>
            </tr>
        </table>
    </div>
    
    <div style="width:97%;margin: 0 auto;font-size:13px; font-family:verdana;">
        <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;" id="innercontainertbl">
            <thead></thead>
            <tr>
                <td width="25%" class="pgHeader">Name of the Department:</td>
                <td width="75%"><c:out value="${AllHeader.deptName}"/></td>
            </tr>
            <tr>
                <td class="pgHeader">Name of the Office:</td>
                <td><c:out value="${AllHeader.offName}"/></td>
            </tr>
            <tr>
                <td class="pgHeader">Designation of DDO:</td>
                <td><c:out value="${AllHeader.ddoName}"/></td>
            </tr>
            <tr>
                <td class="pgHeader">Name of Treasury::</td>
                <td><c:out value="${AllHeader.trName}"/></td>
            </tr>
            <tr>
                <td class="pgHeader">Bill No:</td>
                <td><c:out value="${AllHeader.billdesc}"/></td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
        </table>
            
        <table border="1" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
            <tr class="tblHeader">
                <td width="4%" rowspan="2" class="printData">Sl. No.</td>
                <td width="20%" rowspan="2" class="printData">Name of the Employee/<br>Designation</td>
                <td width="10%" rowspan="2" class="printData">Month in which Original Advance was Drawn</td>
                <td width="5%" colspan="6" style="text-align:center" class="printData">PRINCIPAL</td>
            </tr>
            <tr class="tblHeader">    
                <td width="8%" align="center" class="printData">Amount of<br>Original<br>Advance</td>
                <td width="8%" align="center" class="printData">No of<br>Installment of<br>Recovery</td>
                <td width="8%" align="center" class="printData">Amount<br>Deducted in<br>the Bill</td>
                <td width="8%" align="center" class="printData">Recovery<br>Upto the<br>Month</td>
                <td width="8%" align="center" class="printData">Balance<br>Outstanding</td>
                <td width="8%" align="center" class="printData">Remarks</td>
            </tr>
        </table>
            
        <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">                
            <c:if test="${not empty EmpPrincpalList}">
                <c:forEach var="eachEmpVeP" items="${EmpPrincpalList}">
                    <tr style="height:30px">
                        <td width="5%" align="center" style="border-bottom:1px solid #000000;">
                            <c:if test="${eachEmpVeP.slno > 0}">
                                <c:out value="${eachEmpVeP.slno}"/>
                            </c:if>
                        </td>
                        <td width="20%" align="left" style="border-bottom:1px solid #000000;">&nbsp;
                            <c:out value="${eachEmpVeP.empNameDesg}" escapeXml="false"/>
                        </td>
                        <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;
                            <c:out value="${eachEmpVeP.vchNo}"/>&nbsp;/<c:out value="${eachEmpVeP.vchDate}"/>
                        </td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpVeP.originalAmt}"/>&nbsp;</td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpVeP.instalmentRec}"/>&nbsp;</td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpVeP.decutedAmt}"/>&nbsp;</td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpVeP.recAmt}"/>&nbsp;</td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpVeP.balOutstanding}"/>&nbsp;</td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpVeP.gpfNo}"/></td>
                    </tr>
                    <c:if test="${not empty eachEmpVeP.pagebreakLA}">
                        <tr>
                            <td colspan="9" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
                        </tr>
                        </table>
                        <c:out value="${eachEmpVeP.pagebreakLA}" escapeXml="false"/>
                        <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                        <c:out value="${eachEmpVeP.pageHeaderLA}" escapeXml="false"/>
                    </c:if>
                </c:forEach>
            </c:if>        
        </table>
        
        <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
            <tr>
                <td colspan="9" height="5px"><hr/></td>
            </tr>
            <tr>
                <td width="5%" class="printData" colspan="5">&nbsp;
                    RECOVERY FOR THE MONTH OF <c:out value="${AllHeader.month}"/>-<c:out value="${AllHeader.year}"/>
                </td>
                <td width="10%" class="printData" align="right">&nbsp;<b><c:out value="${TotalAmtP}"/></b></td>
                <td width="10%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="9" height="5px"><hr/></td>
            </tr>
            <tr>
                <td width="5%">&nbsp;</td>
                <td width="20%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
                <td width="10%">&nbsp;</td>
                <td width="10%" colspan="4" class="printData">RUPEES&nbsp;<c:out value="${TotalPFig}"/>&nbsp;ONLY</td>
            </tr>
            <tr style="height:30px" colspan="9">
                <td>&nbsp;</td>
            </tr>
        </table>
        
        <table width="100%" border="0" style="font-size:12px;font-family:verdana;">
            <thead> </thead>
            <tr>
                <td width="10%" colspan="5">&nbsp;</td>
                <td colspan="2" width="40%">&nbsp;</td>
                <td colspan="2" width="30%" class="printData" style="text-align: center">
                    Signature of D.D.O.<br>
                    <c:out value="${AllHeader.ddoName}"/><br>
                    Date :
                </td>
            </tr>
            <tr>
                <td colspan="9" height="5px"><hr/></td>
            </tr>
            <tr>
                <td colspan="9" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
            </tr>
        </table>
    </div>  
    
    <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
            
    <c:if test="${not empty EmpInterestList}">
        <div style="width:90%;margin: 0 auto;">
            <table width="100%" border="0">
                <tr>
                    <td colspan="2" style="text-align:center" class="printData">
                        <b><c:out value="${AllHeader.scheduleOfRecovery}"/></b>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align:center" class="printData">
                        <b> OF <c:out value="${AllHeader.deptName}"/></b>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align:center" class="printData">
                        FOR THE MONTH OF <c:out value="${AllHeader.month}"/>-<c:out value="${AllHeader.year}"/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align:center" class="printData">
                        Demand No :<c:out value="${AllHeader.demandNo}"/> &nbsp; <c:out value="${AllHeader.scheduleName}"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>
        </div>
    
        <div style="width:97%;margin: 0 auto;font-size:13px; font-family:verdana;">           
            <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;" id="innercontainertbl">
                <thead></thead>
                <tr>
                    <td width="25%" class="pgHeader">Name of the Department:</td>
                    <td width="75%"><c:out value="${AllHeader.deptName}"/></td>
                </tr>
                <tr>
                    <td class="pgHeader">Name of the Office:</td>
                    <td><c:out value="${AllHeader.offName}"/></td>
                </tr>
                <tr>
                    <td class="pgHeader">Designation of DDO:</td>
                    <td><c:out value="${AllHeader.ddoName}"/></td>
                </tr>
                <tr>
                    <td class="pgHeader">Name of Treasury::</td>
                    <td><c:out value="${AllHeader.trName}"/></td>
                </tr>
                <tr>
                    <td class="pgHeader">Bill No:</td>
                    <td><c:out value="${AllHeader.billdesc}"/></td>
                </tr>
            </table>
            <table border="1" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                <tr class="tblHeader">
                    <td width="4%" rowspan="2" class="printData">Sl. No.</td>
                    <td width="20%" rowspan="2" class="printData">Name of the Employee/<br>Designation</td>
                    <td width="10%" rowspan="2" class="printData">T.V. No. & Date in<br>which original adv<br>drawn with<br>Treasury Name</td>
                    <td width="5%" colspan="6" style="text-align:center" class="printData">INTEREST</td>
                </tr>
                <tr class="tblHeader">    
                    <td width="8%" class="printData">Amount of<br>Original<br>Advance</td>
                    <td width="8%" class="printData">No of<br>Installment of<br>Recovery</td>
                    <td width="8%" class="printData">Amount<br>Deducted in<br>the Bill</td>
                    <td width="8%" class="printData">Recovery<br>Upto the<br>Month</td>
                    <td width="8%" class="printData">Balance<br>Outstanding</td>
                    <td width="8%" class="printData">Remarks</td>
                </tr>
            </table>

            <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">                
                <c:if test="${not empty EmpInterestList}">
                <c:forEach var="eachEmpIntVe" items="${EmpInterestList}">
                    <tr style="height:30px">
                        <td width="5%" align="center" style="border-bottom:1px solid #000000;">
                            <c:if test="${eachEmpIntVe.slno > 0}">
                                <c:out value="${eachEmpIntVe.slno}"/>
                            </c:if>
                        </td>
                        <td width="15%" align="left" style="border-bottom:1px solid #000000;">&nbsp;
                            <c:out value="${eachEmpIntVe.empNameDesg}" escapeXml="false"/>
                        </td>
                        <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;
                            <c:out value="${eachEmpIntVe.vchNo}"/>&nbsp;/<c:out value="${eachEmpIntVe.vchDate}"/>
                        </td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpIntVe.originalAmt}"/>&nbsp;</td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpIntVe.instalmentRec}"/>&nbsp;</td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpIntVe.decutedAmt}"/>&nbsp;</td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpIntVe.recAmt}"/>&nbsp;</td>
                        <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpIntVe.balOutstanding}"/>&nbsp;</td>
                        <td width="10%" align="right" style="border-bottom:1px solid #000000;">&nbsp;</td>
                        <c:if test="${not empty eachEmpIntVe.pagebreakLA}">
                            <tr>
                                <td colspan="9" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
                            </tr>
                            </table>
                            <c:out value="${eachEmpIntVe.pagebreakLA}" escapeXml="false"/>
                            <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                            <c:out value="${eachEmpIntVe.pageHeaderLA}" escapeXml="false"/>
                        </c:if>
                    </tr>
                </c:forEach> 
                </c:if>
            </table>    
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                <tr>
                    <td colspan="9" height="5px"><hr/></td>
                </tr>
                <tr>
                    <td width="5%" class="printData" colspan="5">&nbsp;
                        RECOVERY FOR THE MONTH OF <c:out value="${AllHeader.month}"/>-<c:out value="${AllHeader.year}"/>
                    </td>
                    <td width="10%" class="printData" align="right">&nbsp;<b><c:out value="${TotalAmtI}"/></b></td>
                    <td width="10%">&nbsp;</td>
                    <td width="10%">&nbsp;</td>
                    <td width="10%">&nbsp;</td>
                </tr>
                <tr>
                    <td colspan="9" height="5px"><hr/></td>
                </tr>
                <tr>
                    <td width="5%">&nbsp;</td>
                    <td width="20%">&nbsp;</td>
                    <td width="10%">&nbsp;</td>
                    <td width="10%">&nbsp;</td>
                    <td width="10%">&nbsp;</td>
                    <td width="10%" colspan="4" class="printData">RUPEES&nbsp;<c:out value="${TotalIFig}"/>&nbsp;ONLY</td>
                </tr>
                <tr style="height:30px" colspan="9">
                    <td>&nbsp;</td>
                </tr>
            </table>
            <table width="100%" border="0" style="font-size:12px;font-family:verdana;">
                <thead> </thead>
                <tr>
                    <td width="10%" colspan="5">&nbsp;</td>
                    <td colspan="2" width="40%">&nbsp;</td>
                    <td colspan="2" width="30%" class="printData" style="text-align: center">
                        Signature of D.D.O.<br>
                        <c:out value="${AllHeader.ddoName}"/><br>
                        Date :
                    </td>
                </tr>
                <tr>
                    <td colspan="9" height="5px"><hr/></td>
                </tr>
                <tr>
                    <td  colspan="9" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%></td>
                </tr>
            </table>
        </div>  
    </c:if>
</body>
</html>
