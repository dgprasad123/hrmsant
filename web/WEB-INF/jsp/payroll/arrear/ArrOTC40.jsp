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
        <title>:: OTC 40 ::</title>
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
                    <td class="printData" style="text-align:center" colspan="4"> <b><c:out value="${Otc40Header.officeName}"/></b></td>
                </tr>  
                <tr>
                    <td class="printData" style="text-align:left" colspan="4">TAN No.<c:out value="${Otc40Header.tanNo}"/></td>
                </tr>  
                <tr>
                    <td width="20%">&nbsp;</td>
                    <td width="15%">&nbsp;</td>
                    <td class="printData" style="text-align:center" width="35%">
                        <b>O.T.C. 40
                            <c:if test="${not empty Otc40Header.otcStatus}">
                                (<c:out value="${Otc40Header.otcStatus}"/>)
                            </c:if>
                        </b>
                    </td>
                    <td class="printData" style="text-align:left" width="30%">
                        <b>(Ref Id- 
                            <c:if test="${not empty Otc40Header.benRefNo}">
                                <c:out value="${Otc40Header.benRefNo}"/>
                            </c:if>
                            )
                        </b>
                        (Token No -
                        <c:if test="${not empty Otc40Header.token}">
                            <b><c:out value="${Otc40Header.token}"/></b>
                        </c:if>
                        )
                    </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" width="20%"> Go No. &nbsp;62710 </td>
                    <td class="printData" style="text-align:left" width="15%"> Dated &nbsp;07-SEP-1996 </td>
                    <td class="printData" style="text-align:center" width="35%"> Bill No.<c:out value="${Otc40Header.billDesc}"/></td>
                    <td class="printData" style="text-align:center" width="30%"> Date:<c:out value="${Otc40Header.billDate}"/></td>
                </tr>   
                <tr>
                    <td class="printData" style="text-align:left" colspan="4" >&nbsp;</td>
                </tr>
            </table>
        </div>

        <div style="width:95%;margin: 0 auto;font-size:13px; font-family:verdana;">
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="align:center">
                <tr>
                    <td class="printData" style="text-align:center;"> 
                        <b>Head of Account: <c:out value="${Otc40Header.ddoCode}"/></b>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>

            <table border="1" cellpadding="0" cellspacing="0" width="100%" style="align:center">
                <tr>
                    <td width="15%" align="center"> Demand No </td>
                    <td width="15%" align="center"> Major </td>
                    <td width="15%" align="center"> Sub Major </td>
                    <td width="15%" align="center"> Minor </td>
                    <td width="15%" align="center"> Sub Minor </td>
                    <td width="15%" align="center"> Detail </td>
                    <td width="15%" align="center"> Charge Voted </td>
                </tr>
                <tr>
                    <td width="15%" align="center" >&nbsp;
                        <c:out value="${Otc40Header.demandNo}"/>
                    </td>
                    <td width="15%" align="center">&nbsp; 
                        <c:out value="${Otc40Header.majorHead}"/>
                    </td>
                    <td width="15%" align="center" >&nbsp;
                        <c:out value="${Otc40Header.subMajorHead}"/>
                    </td>
                    <td width="15%" align="center" > &nbsp;
                        <c:out value="${Otc40Header.minorHead}"/> 
                    </td>
                    <td width="15%" align="center" >&nbsp;
                        <c:out value="${Otc40Header.subMinorHead}"/>
                    </td>
                    <td width="15%" align="center" >&nbsp;
                        <c:out value="${Otc40Header.subMinorHead2}"/>
                    </td>
                    <td width="15%" align="center" >&nbsp;
                        <c:out value="${Otc40Header.subMinorHead3}"/>
                    </td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="printData" style="text-align:left" colspan="3">&nbsp;</td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" colspan="3">
                        &nbsp;&nbsp;Received the sum of Rs.&nbsp; <c:out value="${grossTotal}"/> (<c:out value="${grandTotinWord}"/>) 
                        only being the Grant-in to&nbsp; <c:out value="${Otc40Header.officeName}"/> for the month of
                        &nbsp;<c:out value="${monthName1}"/>-<c:out value="${year1}"/> to <c:out value="${monthName2}"/>-<c:out value="${year2}"/> placed by the _____________________ in 
                        his letter No. &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; dated.
                    </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" colspan="3">&nbsp;</td>
                </tr>
                <tr>
                    <c:if test="${billtypeRegGrant eq '42'}">
                        <td class="printData" style="text-align:left" width="10%"> <b>Pay + GP </b></td>
                        <td class="printData" style="text-align:left" width="10%"> <b>136</b></td>
                    </c:if>
                    <c:if test="${billtypeRegGrant eq '21' || billtypeRegGrant eq '69'}">
                        <td class="printData" style="text-align:left" width="10%"> <b>Pay</b></td>
                        <td class="printData" style="text-align:left" width="10%"> <b>921</b></td>
                    </c:if>
                    <td class="printData" style="text-align:left" width="80%"> <b><c:out value="${payAmt}"/></b></td>
                </tr>
                <c:if test="${billtype eq 'ARREAR'}">
                </c:if>
                <c:if test="${billtype ne 'ARREAR'}">
                    <c:if test="${billtypeRegGrant eq '42'}">
                        <c:if test="${dahead ne 0}">
                            <tr>
                                <td class="printData" style="text-align:left" width="10%">
                                    <b>DA</b>
                                </td>
                                <td class="printData" style="text-align:left" width="10%" >
                                    <b>156</b>
                                </td>
                                <td class="printData" style="text-align:left" width="80%">
                                    <b><c:out value="${daAmt}"/></b>                      
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${hrahead ne 0}">
                            <tr>
                                <td class="printData" style="text-align:left" width="10%">
                                    <b>HRA</b>
                                </td>
                                <td class="printData" style="text-align:left" width="10%" >
                                    <b>403</b>
                                </td>
                                <td class="printData" style="text-align:left" width="80%">
                                    <b><c:out value="${hraAmt}"/></b>                      
                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${oahead ne 0}">
                            <tr>
                                <td class="printData" style="text-align:left" width="10%">
                                    <b>OA</b>
                                </td>
                                <td class="printData" style="text-align:left" width="10%" >
                                    <b>523</b>
                                </td>
                                <td class="printData" style="text-align:left" width="80%">
                                    <b><c:out value="${oaAmt}"/></b>                      
                                </td>
                            </tr>
                        </c:if>
                    </c:if>
                </c:if>

                <tr>
                    <td class="printData" style="text-align:left" width="10%">&nbsp;</td>
                    <td class="printData" style="text-align:left" width="10%" >&nbsp; </td>
                    <td class="printData" style="text-align:left" width="80%">
                <u>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </u>  
                </td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="printData" style="text-align:left" width="10%"> <b>Grand Total</b> </td>
                    <td class="printData" style="text-align:left" width="10%"> <b><c:out value="${grossTotal}"/></b> </td>
                    <td class="printData" style="text-align:left"  width="80%" >&nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center" width="20%"> &nbsp; </td>
                    <td class="printData" style="text-align:left" width="40%"></td>
                    <td class="printData" style="text-align:right" width="40%"></td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center" width="20%"></td>
                    <td class="printData" style="text-align:left" width="40%"></td>
                    <td class="printData" style="text-align:center" width="40%"> <c:out value="${Otc40Header.ddoName}"/></td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center" width="20%"></td>
                    <td class="printData" style="text-align:left" width="40%"></td>
                    <td class="printData" style="text-align:center" width="40%"><c:out value="${Otc40Header.officeName}"/></td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" colspan="3"> &nbsp;</td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" colspan="3">
                        Counter signed and passed for Rs <c:out value="${grossTotal}"/>(<c:out value="${grandTotinWord}"/>)only
                    </td>
                </tr>
                <tr>
                    <td class="pgHeader" style="text-align:right;text-transform: uppercase;" colspan="3">Page No:<%=pageNo++%></td>
                </tr>

            </table>
            <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>        
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="printData" style="text-align:left" width="10%">&nbsp;</td>
                    <td class="printData" style="text-align:left" width="20%">&nbsp;</td>
                    <td class="printData" style="text-align:left" width="30%"> Allotment Received </td>
                    <td class="printData" style="text-align:left" width="40%">&nbsp;</td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" width="10%">&nbsp;</td>
                    <td class="printData" style="text-align:left" width="20%"> Collector/Sub-Collector </td>
                    <td class="printData" style="text-align:left" width="30%">
                <u>Expenditure including this bill&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>
                </td>
                <td class="printData" style="text-align:left" width="40%">&nbsp;</td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" width="10%">&nbsp;</td>
                    <td class="printData" style="text-align:left" width="20%">&nbsp;</td>
                    <td class="printData" style="text-align:left" width="30%"> Balance </td>
                    <td class="printData" style="text-align:left" width="40%">&nbsp;</td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" width="10%">&nbsp;</td>
                    <td class="printData" style="text-align:left" width="20%">
                <u> Details of bill</u>
                </td>
                <td class="printData" style="text-align:center" width="30%">&nbsp;</td>
                <td class="printData" style="text-align:left" width="40%">&nbsp;</td>
                </tr>
            </table>    

            <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
                <tr>
                    <td class="printData" style="text-align:left" width="10%"> <b>Net amount</b> </td>
                    <td class="printData" style="text-align:left" width="10%" >&nbsp;</td>
                    <td class="printData" style="text-align:left" width="40%"> <b><c:out value="${totNetAmt}"/></b> </td>
                    <td class="printData" style="text-align:center" width="40%"> <c:out value="${Otc40Header.ddoName}"/> &nbsp;</td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" width="10%">&nbsp;</td>
                    <td class="printData" style="text-align:left" width="10%">&nbsp;</td>
                    <td class="printData" style="text-align:left" width="40%">&nbsp;</td>
                    <td class="printData" style="text-align:center" width="40%"> <c:out value="${Otc40Header.officeName}"/> &nbsp; </td>
                </tr>
            </table>    

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="printData" style="text-align:left" width="10%">
                        <b>IT</b>
                    </td>
                    <td class="printData" style="text-align:center" width="10%">
                        <b><c:out value="${itObjHead}"/></b>                               
                    </td>
                    <td class="printData" style="text-align:left">
                        <b><c:out value="${itAmt}"/></b>               
                    </td>
                    <td class="printData" style="text-align:center">&nbsp;</td>    
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" width="10%">
                        <b>CPF</b>
                    </td>
                    <td class="printData" style="text-align:center" width="10%">
                        <b><c:out value="${cpfObjHead}"/></b>                               
                    </td>
                    <td class="printData" style="text-align:left">
                        <b><c:out value="${cpfAmt}"/></b>               
                    </td>
                    <td class="printData" style="text-align:center">&nbsp;</td>    
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" width="10%">
                        <b>PT</b>
                    </td>
                    <td class="printData" style="text-align:center" width="10%">
                        <b><c:out value="${ptfObjHead}"/></b>                               
                    </td>
                    <td class="printData" style="text-align:left">
                        <b><c:out value="${ptAmt}"/></b>               
                    </td>
                    <td class="printData" style="text-align:center">&nbsp;</td>    
                </tr>

                <tr>
                    <td class="printData" style="text-align:left"> &nbsp; </td>
                    <td class="printData" style="text-align:left"> &nbsp; </td>
                    <td class="printData" style="text-align:left"> 
                <u>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</u>                    
                </td>
                <td class="printData" style="text-align:left"> &nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left"> Gross Total </td>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:left"> <b><c:out value="${deductTot}"/></b></td>
                    <td class="printData" style="text-align:left"> &nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" colspan="4"> &nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left"> &nbsp; </td>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:left"><u><b>FOR USE IN THE TREASURY</b> </u></td>
                <td class="printData" style="text-align:left"> &nbsp; </td>
                </tr>
            </table>        

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="printData" style="text-align:left" width="30%"> Cash </td>
                </tr> 
                <tr>
                    <td class="printData" style="text-align:left"> Transferred to GPF 8690(55545) </td>
                </tr> 
                <tr>
                    <td class="printData" style="text-align:left"> Professional Tax </td>
                </tr> 
                <tr>
                    <td class="printData" style="text-align:left"> Income Tax </td>
                </tr> 
                <tr>
                    <td class="printData" style="text-align:left"> LIC </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left"> Grand Total </td>
                </tr> 
                <tr>
                    <td class="printData" style="text-align:left"> Examined </td>
                </tr>  
                <tr>
                    <td class="printData" style="text-align:left" width="30%"> Treasury Accountant </td>
                    <td class="printData" style="text-align:left" width="30%"> DA </td>
                    <td class="printData" style="text-align:left" width="60%"> <c:out value="${Otc40Header.treasuryName}"/> </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> Admitted </td>
                    <td class="printData" style="text-align:left" colspan="2"> &nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> Collection </td>
                    <td class="printData" style="text-align:left" colspan="2"> &nbsp; </td>
                </tr>
                <tr>
                    <td class="pgHeader" style="text-align:right;text-transform: uppercase;" colspan="3">Page No:<%=pageNo++%></td>
                </tr>
            </table>

            <input type="button" name="pagebreak1" style="page-break-before: always;width: 0;height: 0"/>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="printData" style="text-align:right" width="30%"> Reason for Objection </td>
                    <td class="printData" style="text-align:left" width="35%"> &nbsp; </td>
                    <td class="printData" style="text-align:left" width="35%"> &nbsp; </td>
                </tr> 
                <tr>
                    <td class="printData" style="text-align:right"> Auditor </td>
                    <td class="printData" style="text-align:left"> &nbsp; </td>
                    <td class="printData" style="text-align:left"> &nbsp; </td>
                </tr> 
                <tr>
                    <td class="printData" style="text-align:left"> &nbsp; </td>
                    <td class="printData" style="text-align:left"> Superintendent </td>
                    <td class="printData" style="text-align:left"> &nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" colspan="3">
                        Under Rs  <c:out value="${totNetAmtUnder}"/>( <c:out value="${totNetAmtUnderInWord}"/> )Only                 
                    </td>
                </tr>
            </table>        

            <table width="100%" border="0" cellspacing="0" cellpadding="0">    
                <tr>
                    <td class="printData" style="text-align:center" width="20%"> &nbsp; </td>
                    <td class="printData" style="text-align:left" width="40%"> &nbsp; </td>
                    <td class="printData" style="text-align:right" width="40%"> &nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.ddoName}"/> </td>
                    <td class="printData" style="text-align:left"> &nbsp; </td>
                    <td class="printData" style="text-align:right"> &nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left" colspan="3"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${Otc40Header.officeName}"/> </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center">  &nbsp; </td>
                    <td class="printData" style="text-align:left"> RECEIVED CONTENTS </td>
                    <td class="printData" style="text-align:center"> Submitted by </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:left"> &nbsp; </td>
                    <td class="printData" style="text-align:right"> &nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.ddoName}"/> </td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.ddoName}"/> </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.officeName}"/> </td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.officeName}"/> </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:left"> II nd discharge to </td>
                    <td class="printData" style="text-align:center">  &nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center">&nbsp;</td>
                    <td class="printData" style="text-align:left">&nbsp;</td>
                    <td class="printData" style="text-align:right">&nbsp;</td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.ddoName}"/> </td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.ddoName}"/> </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.officeName}"/> </td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.officeName}"/> </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center">&nbsp;</td>
                    <td class="printData" style="text-align:left">&nbsp;</td>
                    <td class="printData" style="text-align:center">&nbsp;</td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:left">RECEIVED PAYMENTS</td>
                    <td class="printData" style="text-align:center"> Signature Attested </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center">&nbsp;</td>
                    <td class="printData" style="text-align:left">&nbsp;</td>
                    <td class="printData" style="text-align:center">&nbsp; </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.ddoName}"/></td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.ddoName}"/></td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center"> &nbsp; </td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.officeName}"/></td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.officeName}"/></td>
                </tr>
            </table>        

            <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-size:14px; font-family:verdana;">
                <tr>
                    <td class="printData" style="text-align:left">
                        1.Certified that the D.A. claimed @ 119% vide Go No 27766/F Dated 17.10.2015          
                    </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left">
                        2. Certified that no arrear salary and leave salary has been included in this bill.         
                    </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left">
                        3.Certified that the amount claimed in this bill was not drawn previously.         
                    </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left">
                        4.Certified that the amount claimed in this bill in favour of this college U.C of previous quarters has already been submitted.        
                    </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left">
                        5.Certified that this previous drawal have already examined.
                    </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left">
                        6.certified that the allotment has been fully and properly utilized.
                    </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left">
                        7.Certified that the fees and fines has been deposited in Govt. Treasury.
                    </td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:left">
                        8.Certified that the net claim of this will be transferred electronically in to the bank account of the beneficiaries and the 
                        correct bank details of the<br> beneficiaries have been furnished in a separate list after due verification and uploaded in 
                        the treasury portal bearing reference ID - <b><c:out value="${Otc40Header.benRefNo}"/></b>
                    </td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="printData" style="text-align:left" width="50%">&nbsp;</td>
                    <td class="printData" style="text-align:right" width="50%">&nbsp;</td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center">&nbsp;</td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.ddoName}"/></td>
                </tr>
                <tr>
                    <td class="printData" style="text-align:center">&nbsp;</td>
                    <td class="printData" style="text-align:center"> <c:out value="${Otc40Header.officeName}"/> </td>
                </tr>
                <tr>
                    <td class="pgHeader" style="text-align:right;text-transform: uppercase;" colspan="2">Page No:<%=pageNo++%></td>
                </tr>
            </table>        

        </div>  
    </body>
</html>
