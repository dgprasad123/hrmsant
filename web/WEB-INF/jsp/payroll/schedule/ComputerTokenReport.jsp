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
        <title>:: Computer Token ::</title>
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
            .printData{
                font-family:verdana; 
                font-size:13px;
                text-transform: none;
                text-align: center;
            }
        </style>
        <style type="text/css" media="print">            
            table#reportgrid{
                font-family: Verdana,Arial,Helvetica,sans-serif;
                color:#000000;
                empty-cells: show;
                border-collapse: collapse;
            }
            table#reportgrid th{
                background-color: #E9E9EA;
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                color: #000000;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 13px;
                height: 20px;
            }
            table#reportgrid td{                
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                padding-left:5px;
                color: #000000;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 10px;
                height: 20px;
            }
            .alternateTR{               
                background-color:#E2F4FA;                
            }
            .alternateNormalTR{               
                background-color:#FFFFFF;                
            }
            .lastcolumn{                               
                border-right: 1px solid #091470;               
            }
            .reportHeader{
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                border-right: 1px solid #091470;
            }
            .showinprinting{
            }
            .comboshow
            {
                 display:none;
            }
        </style>
        <style type="text/css" media="screen">
            table#reportgrid{
                empty-cells: show;
                border-collapse: collapse;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                color:#000000;
            }
            .showinprinting{
            }
            table#reportgrid th{
                background-color: #E9E9EA;
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                color: #000000;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 13px;
                height: 20px;
            }
            table#reportgrid td{                
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                padding-left:10px;
                color: #000000;
                font-family: Verdana,Arial,Helvetica,sans-serif;
                font-size: 10px;
                height: 20px;
            }
            .alternateTR{               
                background-color:#E2F4FA;                
            }
            .alternateNormalTR{               
                background-color:#FFFFFF;                
            }
            .lastcolumn{                               
                border-right: 1px solid #091470;               
            }            
            .reportHeader{
                border-top: 1px solid #091470;
                border-left: 1px solid #091470;
                border-right: 1px solid #091470;
            }
        </style>
    </head>
    <body>
        <div style="width:95%;margin: 0 auto;" class="printData">
        <table width="100%" border="0" >
            <tr>
                <td colspan="4" style="text-align:center;font-size:16px;font-weight:bold;">
                    <b>COMPUTER TOKEN</b>
                </td>
            </tr>
        </table>
        
        <table border="0" cellpadding="0" class="printData" cellspacing="0" width="100%" style="align:center">
            <tr>
                <td colspan="4" style="text-align:left;font-size:13px;font-weight:bold;">
                    <b>Treasury Name: <c:out value="${CTokenHeader.treasuryName}"/></b>
                </td>
            </tr>
            <tr>
                <td colspan="4" style="text-align:left;font-size:12px;font-weight:bold;">
                    <b>Reference Id: <c:out value="${CTokenHeader.benRefNo}"/></b>
                </td>
            </tr>
            <tr>
                <td colspan="4">&nbsp;</td>
            </tr>
        </table>
        
        <table cellpadding="1" id="reportgrid" cellspacing="0" width="100%" style="align:center; border: 1px solid black">
            <tr>
                <th width="34%" align="center"  class="printData" style="font-weight:bold;border: 1px solid black;">
                    Computer Token No: &nbsp; <c:out value="${CTokenHeader.tokenNo}"/>
                </th>
                <td width="32%" style="font-weight:bold;border: 1px solid black;">&nbsp;</td>
                <th width="34%" align="center" class="printData" style="font-weight:bold;border: 1px solid black;">Metal Token No</th>
            </tr>
            <tr>
                <td style="font-weight:bold;border: 1px solid black;">&nbsp;</td>
                <td style="font-weight:bold;border: 1px solid black;">&nbsp;</td>
                <td style="font-weight:bold;border: 1px solid black;">&nbsp;</td>
            </tr>
            <tr class="printData">
                <th align="center" class="printData" style="font-weight:bold;border: 1px solid black;">BILL NO</th>
                <td style="font-weight:bold;border: 1px solid black;">&nbsp;</td>
                <th align="center"  class="printData" style="font-weight:bold;border: 1px solid black;">BILL DATE</th>
            </tr>
            <tr>
                <td align="center"  class="printData" style="font-weight:bold;border: 1px solid black;">
                    <c:if test="${not empty CTokenHeader.billDesc}">
                        <c:out value="${CTokenHeader.billDesc}"/>
                    </c:if>
                </td>
                <td style="font-weight:bold;border: 1px solid black;">&nbsp;</td>
                <td align="center"  class="printData" style="font-weight:bold;border: 1px solid black;">
                    <c:if test="${not empty CTokenHeader.billDate}">
                        <c:out value="${CTokenHeader.billDate}"/>
                    </c:if>
                </td>
            </tr>
            <tr>
                <th align="center"  class="printData" style="font-weight:bold;border: 1px solid black;">
                    Bill Type <BR>
                    ID/Description <BR>
                    (as per annexture) 
                </th>
                <td align="center"  class="printData" style="font-weight:bold;border: 1px solid black;"> 
                    <c:if test="${not empty CTokenHeader.billType}">
                        <c:out value="${CTokenHeader.billType}"/>
                    </c:if>
                </td>
                <th align="center" class="printData" style="font-weight:bold;border: 1px solid black;">
                    Establishment Pay Bill
                </th>
            </tr>
        </table>       
    </div> 
                
    <div style="width:95%;margin: 0 auto;margin-top:15px" class="printData">
        <table border="1" cellpadding="0" id="reportgrid" cellspacing="0" width="100%" style="align:center">
            <tr>
                <th width="34%" align="center" style="font-weight:bold;border: 1px solid black;">DDO Code</th>
                <td width="66%" style="border: 1px solid black;"> 
                    &nbsp;<b><c:out value="${CTokenHeader.ddoCode}"/></b>
                </td>
            </tr>
            <tr>
                <th width="34%" align="center" style="font-weight:bold;border: 1px solid black;">DDO Name</th>
                <td width="66%" style="border: 1px solid black;"> 
                    &nbsp;<b><c:out value="${CTokenHeader.ddoName}"/></b>
                </td>
            </tr>
        </table>
    </div>
    
    <div align="center" class="printData" style="width:90%;padding-top:20px;font-weight:bold;">
        CHART OF ACCOUNT
    </div>
    <div style="width:95%;margin: 0 auto;" class="printData">
        <table border="1" cellpadding="0"  id="reportgrid" cellspacing="0" width="100%" style="align:center">
            <tr>
                <th width="15%" align="center" style="font-weight:bold;border: 1px solid black;">Demand </th>
                <th width="15%" align="center" style="font-weight:bold;border: 1px solid black;">Major </th>
                <th width="20%" align="center" style="font-weight:bold;border: 1px solid black;">Sub Major </th>
                <th width="15%" align="center" style="font-weight:bold;border: 1px solid black;">Minor </th>
                <th width="15%" align="center" style="font-weight:bold;border: 1px solid black;">Sub </th>
                <th width="20%" align="center" style="font-weight:bold;border: 1px solid black;">Detail </th>
            </tr>
            <tr>
                <td align="center" style="border: 1px solid black;">
                    &nbsp; <c:out value="${CTokenHeader.demandNo}"/>
                </td>
                <td align="center" style="border: 1px solid black;"> 
                    &nbsp; <c:out value="${CTokenHeader.majorHead}"/>
                </td>
                <td align="center" style="border: 1px solid black;">
                     &nbsp; <c:out value="${CTokenHeader.subMajorHead}"/>
                </td>
                <td align="center" style="border: 1px solid black;"> 
                    &nbsp; <c:out value="${CTokenHeader.minor}"/>
                </td>
                <td align="center" style="border: 1px solid black;">
                     &nbsp; <c:out value="${CTokenHeader.sub}"/>
                </td>
                <td align="center" style="border: 1px solid black;"> 
                    &nbsp; <c:out value="${CTokenHeader.detail}"/>
                </td>
            </tr>
        </table>
    </div>
                
    <div align="center" style="width:95%;padding-top:15px;margin: 0 auto;" class="printData">
        <table border="1" cellpadding="0" id="reportgrid" cellspacing="0" width="100%" style="align:center">
            <tr>
                <th width="34%" align="center" style="border: 1px solid black;">
                    <c:if test="${not empty CTokenHeader.planOrNonPlan}">
                        <c:choose>
                            <c:when test="${CTokenHeader.planOrNonPlan=='0'}">
                                Non Planned
                            </c:when> 
                            <c:when test="${CTokenHeader.planOrNonPlan=='1'}">
                                State Planned
                            </c:when>
                            <c:when test="${CTokenHeader.planOrNonPlan=='2'}">
                                CP
                            </c:when>
                            <c:otherwise>
                                CSP
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </th>
                <th width="32%" align="center" style="border: 1px solid black;"> 
                    <c:if test="${not empty CTokenHeader.chargedOrVoted}">
                        <c:choose>
                            <c:when test="${CTokenHeader.chargedOrVoted=='0'}">
                                Voted
                            </c:when> 
                            <c:when test="${CTokenHeader.chargedOrVoted=='1'}">
                                Charged
                            </c:when>
                        </c:choose>
                    </c:if>
                </th>
                <th width="34%" align="center"  style="border: 1px solid black;">
                    <c:if test="${not empty CTokenHeader.sector}">
                        <c:choose>
                            <c:when test="${CTokenHeader.sector=='0'}">
                                <c:out value="${CTokenHeader.sectorDesc}"/>
                            </c:when> 
                            <c:when test="${CTokenHeader.sector=='1'}">
                                <c:out value="${CTokenHeader.sectorDesc}"/>
                            </c:when>
                            <c:when test="${CTokenHeader.sector=='2'}">
                                <c:out value="${CTokenHeader.sectorDesc}"/>
                            </c:when>    
                        </c:choose>
                    </c:if>
                </th>
            </tr>
        </table>
    </div>            
                
    <div align="center" style="width:95%;padding-top:15px;margin: 0 auto;" class="printData">
        <table border="1" cellpadding="0" id="reportgrid" cellspacing="0" width="100%" style="align:center">
            <tr>
                <th width="34%" align="center" style="font-weight:bold;border: 1px solid black;">Object</th>
                <th width="32%" align="center" style="font-weight:bold;border: 1px solid black;">Item Description</th>
                <th width="34%" align="center"  style="font-weight:bold;border: 1px solid black;">Amount</th>
            </tr>
            <c:if test="${not empty CTokenHeader.allowanceList}">
                <c:forEach var="eachEmpA" items="${CTokenHeader.allowanceList}">
                    <tr>
                        <td align="center" style="border: 1px solid black;">
                            <c:out value="${eachEmpA.objecthead}"/>&nbsp;
                        </td>
                        <td align="center" style="border: 1px solid black;"> 
                            <c:out value="${eachEmpA.adname}"/>&nbsp;
                        </td>
                        <td align="center"  style="border: 1px solid black;">
                            <c:out value="${eachEmpA.adamount}"/>&nbsp;
                        </td>
                    </tr>
                </c:forEach>
            </c:if>    
        </table>            
    </div>
    
    <div align="center" style="width:95%;padding-top:15px;margin: 0 auto;" class="printData">
        <div align="center"><b>//DEDUCTION//</b></div>
        <table border="1" cellpadding="0" id="reportgrid" cellspacing="0" width="100%" style="align:center">
            <tr>
                <th width="34%" align="center" style="font-weight:bold;border: 1px solid black;">BT HEAD</th>
                <th width="32%" align="center" style="font-weight:bold;border: 1px solid black;">Item Description</th>
                <th width="34%" align="center"  style="font-weight:bold;border: 1px solid black;">Amount</th>
            </tr>
            <c:if test="${not empty CTokenHeader.deductionList}">
                <c:forEach var="eachEmpD" items="${CTokenHeader.deductionList}">
                    <tr>
                        <td align="center" style="border: 1px solid black;">
                            <c:out value="${eachEmpD.objecthead}"/>&nbsp;
                        </td>
                        <td align="center" style="border: 1px solid black;"> 
                            <c:out value="${eachEmpD.adname}"/>&nbsp;
                        </td>
                        <td align="center"  style="border: 1px solid black;">
                            <c:out value="${eachEmpD.adamount}"/>&nbsp;
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </table>
    </div>
                
    <div align="center" style="width:95%;padding-top:15px;padding-bottom:25px;margin: 0 auto;" class="printData">
        <table border="1" cellpadding="0" id="reportgrid" cellspacing="0" width="100%" style="align:center">
            <tr>
                <th width="34%" align="center" style="font-weight:bold;border: 1px solid black;">Gross Amount</th>
                <th width="32%" align="center" style="font-weight:bold;border: 1px solid black;">Net Amount</th>
                <th width="34%" align="center"  style="font-weight:bold;border: 1px solid black;">By Transfer Amount</th>
            </tr>
            <tr>
                <td align="center" style="border: 1px solid black;">
                    &nbsp;<c:out value="${CTokenHeader.grossAmt}"/>
                </td>
                <td align="center" style="border: 1px solid black;"> 
                    &nbsp;<c:out value="${CTokenHeader.netAmt}"/>
                </td>
                <td align="center"  style="border: 1px solid black;">
                    &nbsp;<c:out value="${CTokenHeader.byTransferAmt}"/>
                </td>
            </tr>
        </table>
    </div>            
                
</body>
</html>
