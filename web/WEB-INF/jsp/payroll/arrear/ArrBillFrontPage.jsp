

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    int j = 0;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bill Front Page</title>
        <style type="text/css">

            .borderTop{
                border-top: 2px solid black;
            }
            .borderBottom{
                border-bottom: 2px solid black;
            }
            .borderTopandRight{
                border-top: 2px solid black;
                border-right: 2px solid black;
            }
            .borderTopandLeft{
                border-top: 2px solid black;
                border-left: 2px solid black;
            }
            .borderLeft{
                border-left: 2px solid black;
            }
            .borderRight{
                border-right: 2px solid black;
            }
            .borderTopRightandBottom{
                border-top: 2px solid black;
                border-right: 2px solid black;
                border-bottom: 2px solid black;
            }
            .borderTopandBottom{
                border-top: 2px solid black;
                border-bottom: 2px solid black;
            }
            .borderRightandBottom{
                border-right: 2px solid black;
                border-bottom: 2px solid black;
            }
            #tableset tr td div{
                text-indent:20px;
                font-size: 15px;
                font-family:verdana;
            }
            .fdiv{
                position: absolute;
                left: 35%;
                bottom: 35%;
            }
            @media print {
                .fdiv{
                    position: absolute;
                    left: 35%;
                    bottom: 35%;
                }
            }
        </style>
    </head>
    <body>
        <div align="center">
            <div style="width:1000px;margin-top: 10px">
                <table width="100%" border="0" style="font-family:verdana;">
                    <tr>
                        <td width="40%" height="40" style="font-size:16px;font-weight: bold"> Schedule LIII - Form No. 188 </td>
                        <td width="30%"><div align="center" style="vertical-align:middle;height:25px;border:1px solid #000000;margin: 0px 35px 0px 35px;">  &nbsp; ${billChartOfAccount.ddoName} </div></td>
                        <td width="24%" style="font-size:12px;font-weight: bold">Bill No. ${billChartOfAccount.billdesc}  </td>
                        <td width="6%" rowspan="3" style="font-size: 48px">P</td>
                    </tr>
                    <tr>
                        <td colspan="2" style="font-size:12px">Detailed Pay Bill of Permanent/Temporary Establishment of the  <c:out value="${billChartOfAccount.offName}"/>  </td>
                        <td style="font-size:12px;font-weight: bold">(O.T.C.Form No.22)</td>
                    </tr>
                    <c:if test="${billtype eq 'ARREAR'}">
                        <tr>
                            <td colspan="2" style="font-size:12px">for the month of <b><c:out value="${monthNam1}"/>-<c:out value="${year1}"/> to <c:out value="${monthName2}"/>-<c:out value="${year2}"/></b></td>
                            <td style="font-size:12px">District : ${billChartOfAccount.district}</td>
                        </tr>
                    </c:if>
                    <c:if test="${billtype ne 'ARREAR'}">
                        <tr>
                            <td colspan="2" style="font-size:12px">for the month of 
                                <b>
                                    <c:out value="${monthNam1}"/>-<c:out value="${year1}"/> to <c:out value="${monthName2}"/>-<c:out value="${year2}"/>
                                </b>
                            </td>
                            <td style="font-size:12px">District : ${billChartOfAccount.district}</td>
                        </tr>
                    </c:if>
                        
                    <tr>
                        <td > HRMS BILL No: <span style="font-weight: bold;"> ${billChartOfAccount.billid} </span>  </td>
                        <td > Token No:&nbsp;&nbsp; <span style="font-weight: bold;"> ${billChartOfAccount.tokenNo}</span>  </td>
                        <td style="font-size:12px;"> Ben Ref No:&nbsp;&nbsp;<span style="font-weight: bold;"> ${billChartOfAccount.benRefNo}</span> </td>
                    </tr>    
                        
                    
                </table>


                <table width="100%" border="0" cellpadding="0" cellspacing="0" style="font-family:verdana;" id="tableset">
                    <tr>
                        <td width="60%" class="borderTopRightandBottom" style="font-size:15px;padding-left:5px;" valign="top"> 
                            <br>Space for classification stamp of manuscript entries of classification to be filled in by Drawing Officer,
                            Name of detailed heads and corresponding amounts should be recorded by him in adjacent column.
                            <table border="0" width="100%" style="font-size:15px;margin-top: 15px"> 
                                <tr> 
                                    <td>Demand no</td>
                                    <td>- ${billChartOfAccount.demandNo} </td>
                                    <td> &nbsp; </td>
                                </tr>
                                <tr> 
                                    <td>Major head </td>
                                    <td>- ${billChartOfAccount.majorHead}</td>
                                    <td>&nbsp;</td>
                                </tr>
                                <tr> 
                                    <td>Sub Major head  </td>
                                    <td>- ${billChartOfAccount.subMajorHead}</td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr> 
                                    <td>Minor head  </td>
                                    <td>- ${billChartOfAccount.minorHead}</td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr> 
                                    <td>Sub head  </td>
                                    <td>- ${billChartOfAccount.subMinorHead1}</td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr> 
                                    <td>Detail head</td>
                                    <td> - ${billChartOfAccount.subMinorHead2}</td>
                                    <td>&nbsp;  </td>
                                </tr>
                                <tr> 
                                    <td> Plan Status  </td>
                                    <td> - ${billChartOfAccount.planName}</td>
                                    <td>&nbsp;  </td>
                                </tr>
                                <tr> 
                                    <td>Charge/Voted</td>
                                    <td>- ${billChartOfAccount.subMinorHead3}</td>
                                    <td>&nbsp; </td>
                                </tr>
                                <tr> 
                                    <td>Sector</td>
                                    <td>- ${billChartOfAccount.sectorName}</td>
                                    <td>&nbsp; </td>
                                </tr>
                            </table>
                            <br> N.B - <br>

                            <div>
                                <p style="text-align: justify;">1.Hold over amounts should be entered in red ink in the appropriate col. 3,4,5 and 6 as the case may 
                                    be and ignored in totaling. Leave salary the amount of which is not known, should Similarly be 
                                    entered in red in col. 4 at the same rate as pay if he has remained on duty (S.T.R. 55).</p>
                            </div>
                            <div>
                                <p style="text-align: justify;">2. In the Remarks col.umn 15 should be recorded all unusual permanent events such as death, retirements, 
                                    transfers and first appointments which find no place increment certificates or absentee statement.</p>
                            </div>
                            <div>
                                <p style="text-align: justify;">3. When the increment claimed operates to carry a Government Servant Govt. to efficiency bar it should be 
                                    supported by a declaration that the Government servants in question is fit to pass the bar (S.T.R. 6).</p>
                            </div>
                            <div>
                                <p style="text-align: justify;">4. Names of Government servants in inferior services as well as those mentioned [S.T.R.55(3)]
                                    may be omitted from pay bill (S.T.R.55).</p>
                            </div>
                            <div>
                                <p style="text-align: justify;">5. A red line should be drawn right across the sheet after each section of the punishment and
                                    under it is totals of columns 4,5,6 and 7 and 8 of the section should be shown in red ink.</p>
                            </div>
                            <div>
                                <p style="text-align: justify;">6. In cases where the amount of leave salary is based on average pay separate statement showing the 
                                    calculation of average pay duly attested by Drawing Officer should be attachment to this bill vide [S.T.R.55 (3)].</p>
                            </div>
                            <div>
                                <p style="text-align: justify;">7. The names of men holding post substantively should be entered in order of Seniority as measured by substantive  
                                    pay drawn and below those will be shown the parts left vacant and the men officiating in the vacancies.</p>
                            </div>
                            <div>
                                <p style="text-align: justify;">8. Officiating pay should be record in the section of the bill appropriate to that in which the
                                    Government servant officiates and transit pay should be recorded in the same section as that in which
                                    the duty pay of the Government servant after transfer is recorded.</p>
                            </div>
                            <div>
                                <p style="text-align: justify;">9. The following abbreviations should be use in this and in all other document, submitted with pay bill.</p>
                            </div>

                            <br>
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-family:verdana;font-size:15px"> 
                                <tr> 
                                    <td>Leave on average pay </td>
                                    <td>- LAP</td>
                                    <td>Under suspension </td>
                                    <td>- SP </td>
                                </tr>
                                <tr> 
                                    <td>Leave on quarter average pay    </td>
                                    <td>- LIP</td>
                                    <td>Vacant</td>
                                    <td>- A</td>
                                </tr>
                                <tr> 
                                    <td>On other duty   </td>
                                    <td>- OD</td>
                                    <td>Post Life Insurance</td>
                                    <td>- I</td>
                                </tr>
                                <tr> 
                                    <td>Leave Salary   </td>
                                    <td>- LS</td>
                                    <td>Last Pay Certificate</td>
                                    <td>- LP</td>
                                </tr>
                                <tr> 
                                    <td>Conveyance allowance   </td>
                                    <td>- CA</td>
                                    <td>Subsistence grant</td>
                                    <td>- Sub grant</td>
                                </tr>
                                <tr> 
                                    <td>Transit pay </td>
                                    <td>- TP</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                </tr>
                            </table>  
                            <br>
                            <div>
                                10. In cases where any fast one sesiocladeo in pay bill, a separate schepe showing the
                                particulars of deduction relating to each fund should accompany the bill.
                            </div>
                        </td>
                        <td width="40%" class="borderTopandBottom" valign="top">
                            <table border="0" width="100%" style="font-family:verdana;" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td valign="top">
                                        <table border="0" width="100%" style="font-family:verdana;" cellpadding="0" cellspacing="0">
                                            <tr style="height:30px">
                                                <td colspan="4" align="center" style="font-size:18px">VOUCHER</td>
                                            </tr>
                                            <tr style="font-size:14px">
                                                <td>&nbsp; of </td>
                                                <td>&nbsp; </td>
                                                <td>&nbsp; </td>
                                                <td>list </td>
                                            </tr>
                                            <tr style="font-size:14px">
                                                <td>&nbsp; for </td>
                                                <td>&nbsp; </td>
                                                <td>&nbsp; </td>
                                                <td>&nbsp; </td>
                                            </tr>
                                            <tr style="font-size:14px">
                                                <td>&nbsp; </td>
                                                <td>&nbsp; </td>
                                                <td>&nbsp; </td>
                                                <td>&nbsp; </td>
                                            </tr>
                                            <tr style="font-size:14px">
                                                <td class="borderTop" width="35%">&nbsp; </td>
                                                <td class="borderTopandRight" width="30%">&nbsp; </td>
                                                <td class="borderTopandRight" align="center" width="25%">RS </td>
                                                <td class="borderTop" width="10%">&nbsp;P </td>
                                            </tr>
                                            <tr style="font-family:verdana;font-size:14px">
                                                <td colspan="2" class="borderRight" >&nbsp; Pay of permanent Establishment</td>
                                                <td class="borderTopandRight">&nbsp; </td>
                                                <td class="borderTop">&nbsp; </td>
                                            </tr>
                                            <tr style="font-family:verdana;font-size:14px">
                                                <td colspan="2" class="borderRight">&nbsp; Pay of temporary Establishment</td>
                                                <td class="borderRight">&nbsp; </td>
                                                <td>&nbsp; </td>
                                            </tr>

                                           <c:if test="${billtype eq 'ARREAR' || billtype eq 'ARREAR_6'}">

                                                <tr style="font-family:verdana;font-size:18px">
                                                    <td>&nbsp; ${payHead}</td>
                                                    <td class="borderRight">Arrear Allowance</td>   
                                                    <td class="borderRight" align="right"><c:out value="${payAmt}"/> &nbsp;</td> 
                                                    <td>&nbsp; </td>
                                                </tr>

                                            </c:if>
                                            <c:if test="${billtype eq 'ARREAR_J' || billtype eq 'ARREAR_6_J'}">

                                                <tr style="font-family:verdana;font-size:18px">
                                                    <td>&nbsp; ${payHead}</td>
                                                    <td class="borderRight">Arrear Pay</td>   
                                                    <td class="borderRight" align="right"><c:out value="${payAmt}"/> &nbsp;</td> 
                                                    <td>&nbsp; </td>
                                                </tr>

                                            </c:if>
                                            <c:if test="${billtype ne 'ARREAR'}">
                                                <c:if test="${billtype ne 'ARREAR_NPS'}">
                                                    <c:forEach var="eachOA" items="${OAList}">                                            
                                                        <tr style="font-family:verdana;font-size:18px">
                                                            <td>&nbsp; <c:out value="${eachOA.objectHead}"/></td>
                                                            <td class="borderRight"><c:out value="${eachOA.scheduleName}"/> &nbsp;</td>   
                                                            <td class="borderRight" align="right"><c:out value="${eachOA.schAmount}"/> &nbsp;</td> 
                                                            <td>&nbsp; </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:if>
                                                <c:if test="${billtype eq 'ARREAR_NPS'}">
                                                    <tr style="font-family:verdana;font-size:18px">
                                                        <td>&nbsp; 855</td>
                                                        <td class="borderRight">Arrear &nbsp;</td>   
                                                        <td class="borderRight" align="right"><c:out value="${TotOaAmt}"/> &nbsp;</td> 
                                                        <td>&nbsp; </td>
                                                    </tr>
                                                </c:if>
                                            </c:if>


                                            <tr style="font-family:verdana;font-size:18px">
                                                <td class="borderTop">&nbsp; </td>
                                                <td class="borderTopandRight" align="right">Total&nbsp;</td>
                                                <td class="borderTopRightandBottom" align="right"><c:out value="${TotOaAmt}"/> &nbsp;</td>
                                                <td class="borderTopandBottom">&nbsp;<c:out value="${TotOaAmtPaise}"/></td>
                                            </tr>
                                            <tr style="font-family:verdana;font-size:14px">
                                                <td>Deduct- </td>
                                                <td class="borderRight">&nbsp;</td>
                                                <td class="borderRight" align="right"> &nbsp;</td>
                                                <td>&nbsp; </td>
                                            </tr>
                                              
                                            <c:if test="${not empty scheduleList}">
                                                <c:forEach var="eachSch" items="${scheduleList}">
                                                    <tr style="font-family:verdana;font-size:18px">
                                                        <td>&nbsp; <c:out value="${eachSch.objectHead}"/></td>
                                                        <td class="borderRight"><c:out value="${eachSch.scheduleName}"/> &nbsp;</td>   
                                                        <td class="borderRight" align="right"><c:out value="${eachSch.schAmount}"/> &nbsp;</td> 
                                                        <td>&nbsp; </td>
                                                    </tr>
                                                    <%j++;%>
                                                </c:forEach>
                                            </c:if>

                                            
                                        </table>

                                        <table width="100%" style="font-family:verdana;font-size:14px" cellpadding="0" cellspacing="0">
                                            <% for (int i = 0; i < 50 - j; i++) {%>
                                            <tr style="font-family:verdana;font-size:14px">
                                                <td width="40%">&nbsp; </td>
                                                <td class="borderRight" width="25%"> &nbsp;</td>
                                                <td class="borderRight" align="right" width="25%"> &nbsp;</td>
                                                <td width="10%">&nbsp; </td>
                                            </tr>
                                            <%}%>
                                        </table>
                                    </td>
                                </tr>
                            </table>

                            <table border="0" width="100%" style="height:80px;font-family:verdana;font-size:14px" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="borderRight" width="65%" >&nbsp;Total deductions </td>
                                    <td class="borderTopRightandBottom" width="25%" align="right" style="font-size:18px"><c:out value="${TotDeductAmt}"/> &nbsp; </td>
                                    <td class="borderTopandBottom" width="10%" style="font-size:18px">&nbsp; </td>
                                </tr>
                                <tr>
                                    <td class="borderRight" >&nbsp;Net Total </td>
                                    <td class="borderRight" align="right" style="font-size:18px"><c:out value="${TotNetAmt}"/> &nbsp; </td>
                                    <td style="font-size:18px">&nbsp;  </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <table border="0" width="100%" style="font-family:verdana;font-size:18px">
                    <tr>
                        <td colspan="3" align="center"> <b> FOR THE USE OF THE ACCOUNT GENERAL'S OFFICE </b> </td>
                    </tr>
                    <tr style="font-size:16px">
                        <td><b> Admitted Rs. </b> </td>
                        <td>&nbsp; </td>
                        <td>&nbsp; </td>
                    </tr>
                    <tr style="font-size:16px">
                        <td> <b> Object Rs. </b> </td>
                        <td>&nbsp; </td>
                        <td>&nbsp; </td>
                    </tr>
                    <tr style="font-size:16px">
                        <td width="30%"><b> Auditor </b> </td>
                        <td width="30%"><b> Superintendent </b> </td>
                        <td width="60%"><b> Gazetted Officer </b> </td>
                    </tr>
                </table>
                </br>
                </br>
                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-family:verdana;font-size:14px">
                    <tr>
                        <td class="borderTop"> S.T.R. means Subsidiary Rules under the Orissa Treasury Rules. </td>
                    </tr>
                    <tr>
                        <td> The deduct entries relating to the Provident fund should be posted separately for the Sterling and Ordinary Brand as. </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
