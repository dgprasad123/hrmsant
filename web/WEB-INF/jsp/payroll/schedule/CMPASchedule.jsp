<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    int pageNo = 1;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: CMPA Schedule ::</title>
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
                        <b> 5-7610-loans to Government Servants etc 00-800-Other Advances-1018-Other Items-48057-Personal Computer Advance</b>
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
                    <td width="16%" class="pgHeader">Name of the Department:</td>
                    <td width="80%"><c:out value="${AllHeader.deptName}"/></td>
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
                    <td class="pgHeader">Name of Treasury:</td>
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
            <table border="1" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
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
                    <c:forEach var="eachEmpHbaP" items="${EmpPrincpalList}">
                        <tr style="height:30px">
                            <td width="5%" align="center" style="border-bottom:1px solid #000000;">
                                <c:if test="${eachEmpHbaP.slno > 0}">
                                    <c:out value="${eachEmpHbaP.slno}"/>
                                </c:if>
                            </td>
                            <td width="20%" align="left" style="border-bottom:1px solid #000000;">
                                <c:out value="${eachEmpHbaP.empNameDesg}" escapeXml="false"/>
                            </td>
                            <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;
                                <c:out value="${eachEmpHbaP.vchNo}"/>&nbsp;/<c:out value="${eachEmpHbaP.vchDate}"/>
                            </td>
                            <td width="8%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpHbaP.originalAmt}"/>&nbsp;</td>
                            <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpHbaP.instalmentRec}"/>&nbsp;</td>
                            <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpHbaP.decutedAmt}"/>&nbsp;</td>
                            <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpHbaP.recAmt}"/>&nbsp;</td>
                            <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpHbaP.balOutstanding}"/>&nbsp;</td>
                            <td width="8%" align="center" style="border-bottom:1px solid #000000;">&nbsp;<c:out value="${eachEmpHbaP.gpfNo}"/></td>
                        </tr>
                        <c:if test="${not empty eachEmpHbaP.pagebreakLA}">
                            <tr>
                                <td colspan="9" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
                            </tr>
                            </table>
                            <c:out value="${eachEmpHbaP.pagebreakLA}" escapeXml="false"/>
                            <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                            <c:out value="${eachEmpHbaP.pageHeaderLA}" escapeXml="false"/>
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
                    <td width="10%" colspan="4" class="printData">RUPEES&nbsp;<b><c:out value="${TotalPFig}"/></b>&nbsp;ONLY</td>
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
                    <td  colspan="9" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
                </tr>
            </table>
        </div>  
        
        <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>
        
        <c:if test="${not empty EmpInterestList}">        
            <div style="width:97%;margin: 0 auto;">
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
                            <b> 0049-Interest Receipts-04-Interest Receipts of State / University Govt.-800-receipts of State / University Govt.-800
                                -receipts-10019 
                            </b>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:center" class="printData">
                            <b> -Interest on loans and Advances to Govt. Servants-231-Interest for Purchase of Personal Computer, 
                                Interest on Computer Advance. 
                            </b>
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
                        <td colspan="2" style="text-align:center">---------- * ---------</td>
                    </tr>
                </table>
                <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;" id="innercontainertbl">
                    <thead></thead>
                    <tr>
                        <td width="16%" class="pgHeader">Name of the Department:</td>
                        <td width="80%"><c:out value="${AllHeader.deptName}"/></td>
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
                        <td width="10%" rowspan="2" class="printData">Account No</td>
                        <td width="5%" colspan="6" style="text-align:center" class="printData">INTEREST</td>
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
                    <c:if test="${not empty EmpInterestList}">
                        <c:forEach var="eachEmpIntHba" items="${EmpInterestList}">
                            <tr style="height:30px">
                                <td width="5%" align="center" style="border-bottom:1px solid #000000;">
                                    <c:if test="${eachEmpIntHba.slno > 0}">
                                        <c:out value="${eachEmpIntHba.slno}"/>
                                    </c:if>
                                </td>
                                <td width="20%" align="left" style="border-bottom:1px solid #000000;">
                                    <c:out value="${eachEmpIntHba.empNameDesg}" escapeXml="false"/>
                                </td>
                                <td width="10%" align="center" style="border-bottom:1px solid #000000;">&nbsp;
                                    <c:out value="${eachEmpIntHba.vchNo}"/>&nbsp;/<c:out value="${eachEmpIntHba.vchDate}"/>
                                </td>
                                <td width="10%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpIntHba.accNo}"/></td> 
                                <td width="8%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpIntHba.originalAmt}"/>&nbsp;</td>
                                <td width="8%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpIntHba.instalmentRec}"/>&nbsp;</td>
                                <td width="8%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpIntHba.decutedAmt}"/>&nbsp;</td>
                                <td width="8%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpIntHba.recAmt}"/>&nbsp;</td>
                                <td width="8%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpIntHba.balOutstanding}"/>&nbsp;</td>
                                <td width="8%" align="center" style="border-bottom:1px solid #000000;"><c:out value="${eachEmpIntHba.gpfNo}"/>&nbsp;</td>
                            </tr>
                            <c:if test="${not empty eachEmpIntHba.pagebreakLA}">
                                <tr>
                                    <td colspan="9" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%> </td>
                                </tr>
                                </table>
                                <c:out value="${eachEmpIntHba.pagebreakLA}" escapeXml="false"/>
                                <table border="0" width="100%" cellspacing="0" style="font-size:12px; font-family:verdana;">
                                <c:out value="${eachEmpIntHba.pageHeaderLA}" escapeXml="false"/>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </table>  

                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-size:12px;font-family:verdana;">
                    <tr>
                        <td colspan="10" height="5px"><hr/></td>
                    </tr>
                    <tr>
                        <td width="5%" class="printData" colspan="5">&nbsp;
                            RECOVERY FOR THE MONTH OF <c:out value="${AllHeader.month}"/>-<c:out value="${AllHeader.year}"/>
                        </td>
                        <td width="10%">&nbsp;</td>
                        <td width="10%" class="printData" align="center">&nbsp;<b><c:out value="${TotalAmtI}"/></b></td>
                        <td width="10%">&nbsp;</td>
                        <td width="10%">&nbsp;</td>
                        <td width="10%">&nbsp;</td>
                    </tr>
                    <tr>
                        <td colspan="10" height="5px"><hr/></td>
                    </tr>
                    <tr>
                        <td width="5%">&nbsp;</td>
                        <td width="20%">&nbsp;</td>
                        <td width="10%">&nbsp;</td>
                        <td width="10%">&nbsp;</td>
                        <td width="10%">&nbsp;</td>
                        <td width="10%" colspan="5" class="printData">RUPEES&nbsp;<b><c:out value="${TotalIFig}"/></b>&nbsp;ONLY</td>
                    </tr>
                    <tr style="height:30px" colspan="10">
                        <td>&nbsp;</td>
                    </tr>
                </table>
                <table width="100%" border="0" style="font-size:12px;font-family:verdana;">
                    <thead> </thead>
                    <tr>
                        <td width="10%" colspan="5">&nbsp;</td>
                        <td colspan="2" width="40%">&nbsp;</td>
                        <td colspan="3" width="30%" class="printData" style="text-align: center">
                            Signature of D.D.O.<br>
                            <c:out value="${AllHeader.ddoName}"/><br>
                            Date :
                        </td>
                        
                    </tr>
                    
                    <tr>
                        <td colspan="9" class="pgHeader" style="text-align:right; text-transform: uppercase;">Page:<%=pageNo++%></td>
                    </tr>
                </table>
            </div>   
        </c:if>
    </body>
</html>
