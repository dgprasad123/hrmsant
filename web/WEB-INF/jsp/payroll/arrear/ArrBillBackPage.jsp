<%-- 
    Document   : BillBackPage
    Created on : Dec 29, 2016, 11:21:42 AM
    Author     : Administrator
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: Arrear Bill Back page ::</title>

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
        </style>

    </head>
    <body>
        <div align="center">
            <div style="width:1000px;margin-top:50px">
                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:verdana">
                    <tr>
                        <td width="50%" valign="top" class="borderTopandRight">
                            <table style="font-size:15px" border="0" width="100%" cellpadding="0" cellspacing="0" >
                                <tr> 
                                    <td colspan="3" class="borderRightandBottom">Total column 8 <br> Deduct:Undisturbed pay as detailed below - </td>
                                    <td align="right" class="borderBottom" style="font-size:20px"> <c:out value="${payAmt}"/> &nbsp;</td>
                                </tr>

                                <tr style="font-size: 20px">
                                    <td width="30%">IT&nbsp;</td>
                                    <td width="20%" class="borderRight"> <c:out value="${itObjHead}"/></td>
                                    <td width="25%" align="right" class="borderRight"><c:out value="${itAmt}"/>&nbsp; </td>
                                    <td width="25%">&nbsp; </td>
                                </tr>
                                <tr style="font-size: 20px">
                                    <td width="30%"><c:out value="${acctType}"/>&nbsp;</td>
                                    <td width="20%" class="borderRight"> <c:out value="${cpfObjHead}"/></td>
                                    <td width="25%" align="right" class="borderRight"><c:out value="${cpfAmt}"/>&nbsp; </td>
                                    <td width="25%">&nbsp; </td>
                                </tr>
                                <tr style="font-size: 20px">
                                    <td width="30%">PT&nbsp;</td>
                                    <td width="20%" class="borderRight"> <c:out value="${ptfObjHead}"/></td>
                                    <td width="25%" align="right" class="borderRight"><c:out value="${ptAmt}"/>&nbsp; </td>
                                    <td width="25%">&nbsp; </td>
                                </tr>
                                <c:if test="${orAmt > 0}">
                                    <tr style="font-size: 20px">
                                        <td width="30%">OR&nbsp;</td>
                                        <td width="20%" class="borderRight"> <c:out value="${orObjHead}"/></td>
                                        <td width="25%" align="right" class="borderRight"><c:out value="${orAmt}"/>&nbsp; </td>
                                        <td width="25%">&nbsp; </td>
                                    </tr>
                                </c:if>
                                <%--<c:if test="${drAmt > 0}">
                                    <tr style="font-size: 20px">
                                        <td width="30%">DR&nbsp;</td>
                                        <td width="20%" class="borderRight"> <c:out value="${drObjHead}"/></td>
                                        <td width="25%" align="right" class="borderRight"><c:out value="${drAmt}"/>&nbsp; </td>
                                        <td width="25%">&nbsp; </td>
                                    </tr>
                                </c:if>--%>
                            </table>
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:15px">
                                <tr>
                                    <td width="75%" colspan="3" class="borderTopandRight">
                                        In respect of subscribers to the sterling Branch it
                                        should be noted on the bill and in the Fund Schedule
                                        attached to the bill that the recoveries related to the
                                        Sterling Branches.
                                    </td>
                                    <td width="25%" class="borderBottom">&nbsp; </td>
                                </tr>
                                <tr style="height:30px">
                                    <td colspan="3" class="borderRight" align="right">
                                        Total deduction&nbsp; 
                                    </td>
                                    <td class="borderBottom" align="right" style="font-size:20px"><c:out value="${TotDedut}"/> &nbsp; </td>
                                </tr>
                                <tr style="height:40px">
                                    <td colspan="3" class="borderRight">
                                        The amount required for payment (in words)&nbsp; 
                                    </td>
                                    <td align="right" style="font-size:20px"> <c:out value="${NetTotal}"/> &nbsp; </td>
                                </tr>
                                <tr style="height:40px">
                                    <td colspan="3" class="borderRightandBottom" style="font-size:18px">
                                        <c:out value="${NetTotalWord}"/> &nbsp;
                                    </td>
                                    <td class="borderBottom" align="right"> &nbsp; </td>
                                </tr>
                            </table>
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-size:16px">
                                <tr style="height:50px">
                                    <td colspan="4" class="borderBottom" align="center">
                                        DETAIL OF PAY OF ABSENTEES REFUNDED
                                    </td>
                                </tr>
                                <tr>
                                    <td width="30%" align="center" class="borderRightandBottom">
                                        Traction of
                                        Establishment &nbsp; 
                                    </td>
                                    <td width="30%" align="center" class="borderRightandBottom"> 
                                        Name of incumbent &nbsp;
                                    </td>
                                    <td width="15%" align="center" class="borderRightandBottom">
                                        Period &nbsp; 
                                    </td>
                                    <td width="25%" align="center" class="borderBottom"> Amount &nbsp; </td>
                                </tr>
                            </table>
                        </td>
                        <td width="50%" class="borderTopandRight" style="font-size:14px;padding-left:4px">
                            1.Received contents and certificated that I have satisfied myself 1 month/
                            2month/3month that all emoluments included in bills drawn previous to this
                            date which the exception of those detailed below (of which the total has been
                            refunded by deduction from this bill) have been taken and filled in my office
                            with receipt stamps duly canceled for every payment in excess of Rs. 20.
                            ++One line to be used and the other scored out. <br><br>

                            2.Certificated that no person in superior service has been absent either on
                            other duty or suspension or with or without leave (except on casual leave)
                            during the month 
                            <p style="font-weight:bold"> RECEIVED CONTENTS </p>
                            Note-When an absentee statement accompanies the bill the second
                            certificate should be struck out. <p style="font-weight:bold; text-align:right;padding-right:10px;"> RECEIVED PAYMENTS </p> <br>

                            3.Certificated that no leave has been granted until by reference to the
                            applicant's service book leave accounts and to the leave rule, applicable to
                            him. I have satisfied myself that it was admissible, and that all grants of
                            leave and departures on, and returns from leave and all periods of
                            suspension and other duty and other events which are required under the
                            rules to be so recorded have been recorded in the service books and leave
                            accounts under my attestation.  <br> <p style="font-weight:bold; text-align:right;padding-right:10px;"> SIGNATURE ATTESTED </p>

                            4.Certified that all appointments and substantive promotion and such of the
                            officiating promotions as have to be entered in the service books as per
                            columns in the standard from No. FR-10 have been entered in the service
                            book of the persons concerned under my attestation. <br>

                            5.Certified that all Government servants whose names are omitted from but
                            for whom pay has been drawn in the bill has actually been entertained during
                            the month (S.R.35 Bihar and Orissa Account Code). <br>

                            6.Certified that no persons for whom house rent allowance has been drawn
                            in this bill has been in occupation of rent - free Government quarters during
                            the period for which the allowances has been drawn. <br>

                            7.Certified that except in the case of Government servant whose names
                            appear in the appended list and in whose case the appropriate certificate
                            required under S.T.R.55 (3) has been furnished on leave salary for any
                            Government servants drawn in the bill is equal to his actual pay. <br>

                            8.Certified that no leave salary for end Government servants (except the
                            following in whose service book noted regarding allocation has been
                            recorded) drawn in this bill form is debitable to any Government. Etc. other
                            than the Government of Orissa.
                            <br>
                            1&nbsp;&nbsp;&nbsp;&nbsp;4<br>
                            2&nbsp;&nbsp;&nbsp;&nbsp;5<br>
                            3&nbsp;&nbsp;&nbsp;&nbsp;6
                            <br>
                            9. Certified that in respect of fixed traveling allowance claims drawn in the
                            previous month, quarter, half-year or full year as the case may be the
                            necessary journals have been examined to see that the Government
                            servants concerned made the requisite tours and that in case where the
                            requisite tours have not been made the necessary recoveries have been
                            effected. The particulars of recoveries made or yet to be made are furnished
                            below:- 
                            </br>
                            </br>
                            </br>
                            </br>
                            Under Rs. <b><c:out value="${NetTotalUnder}"/></b> (<b><c:out value="${NetTotalWordUnder}"/></b>)
                            </br>
                            </br>
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-family:verdana;font-size:12px">
                                <tr> 
                                    <td width="9%" class="borderTopandRight" >&nbsp; </td>
                                    <td width="9%" class="borderTopandRight"> <div id="verticaltext">Name Of the officer</div> </td> 
                                    <td width="9%" class="borderTopandRight"> <div id="verticaltext">Desig<br>nation</div> </td>
                                    <td width="19%" class="borderTopandRight"> 
                                        Period for which <br>
                                        minimum tour<br> is prescribed
                                        month,quarter,<br>
                                        half-year,year 
                                    </td>
                                    <td width="9%" class="borderTopandRight"> <div id="verticaltext">Minim<br>um tour <br> requi<br>red</div> </td>
                                    <td width="9%" class="borderTopandRight"> <div id="verticaltext">Short<br>age <br>in tour</div> </td>
                                    <td width="9%" class="borderTopandRight"> <div id="verticaltext">Amou<br>nt recov<br>ered</div> </td>
                                    <td width="9%" class="borderTopandRight"> <div id="verticaltext">Amou<br>nt yet due</div> </td>
                                    <td width="9%" class="borderTopandRight"> <div id="verticaltext">Date <br>of recov<br>ery</div> </td>
                                    <td width="9%" class="borderTop"> <div id="verticaltext">Remar<br>ks</div> </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <table width="100%" border="0" cellspacing="0" cellpadding="0" style="font-family:verdana;font-size:14px">
                    <tr>
                        <td width="50%" class="borderTopandRight">
                            Pay to _____________________________________<br><br>
                            Station
                            Dated _____________20<br><br>
                            <div align="center">
                                Signature
                                <br>
                                Designation of Drawing Officer
                            </div>
                        </td>
                        <td width="50%" valign="top" style="height:100%">
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-family:verdana;font-size:12px">
                                <tr style="height:150px"> 
                                    <td width="9%" class="borderTopRightandBottom" > <div id="verticaltext">&nbsp;</div> </td>
                                    <td width="9%" class="borderTopRightandBottom"><div id="verticaltext"> &nbsp;</div> </td> 
                                    <td width="9%" class="borderTopRightandBottom"> &nbsp; </td>
                                    <td width="19%" class="borderTopRightandBottom"><div id="verticaltext">&nbsp;</div></td>
                                    <td width="9%" class="borderTopRightandBottom"><div id="verticaltext"> &nbsp;</div></td>
                                    <td width="9%" class="borderTopRightandBottom"><div id="verticaltext">&nbsp;</div></td>
                                    <td width="9%" class="borderTopRightandBottom"><div id="verticaltext">&nbsp;</div></td>
                                    <td width="9%" class="borderTopRightandBottom"><div id="verticaltext">&nbsp;</div></td>
                                    <td width="9%" class="borderTopRightandBottom"><div id="verticaltext">&nbsp;</div></td>
                                    <td width="9%" class="borderTopRightandBottom"><div id="verticaltext">&nbsp;</div></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
                <table border="0" cellpadding="0" cellspacing="0" width="100%" style="font-family:verdana;font-size:14px">
                    <tr>
                        <td width="5%">Pay Rs.</td>
                        <td width="15%"><div align="center" style="height:35px;border:1px solid #000000;margin: 0 5 0 5;"> &nbsp; </div></td>
                        <td width="60%">&nbsp;Rupees_______________________________________________________________ as follows</td>
                    </tr>
                    <tr align="center">
                        <td colspan="2">&nbsp;</td>
                        <td> In cash Rs __________________________</td>
                    </tr>
                    <tr align="center">
                        <td colspan="2">&nbsp;</td>
                        <td>Deduct - By transfer credit to personal deposits Rs __________________________</td>
                    </tr>
                    <tr align="center">    
                        <td colspan="2">&nbsp;</td>
                        <td>P.L.I Premia Rs __________________________</td>
                    </tr>
                    <tr align="center">
                        <td colspan="2">&nbsp;</td>
                        <td>IV - Taxes on income Rs.__________________________</td>
                    </tr>
                    <tr align="center">
                        <td colspan="2">&nbsp;</td>
                        <td>XXXIX - Civil Works Rs.__________________________</td>
                    </tr>
                </table>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" style="font-family:verdana;font-size:14px">
                    <tr> 
                        <td width="34%">Examined and entere <br> Treasury Accountant</td>
                        <td width="33%">Dated...........................20 </td> 
                        <td width="33%"> Treasury Officer </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
