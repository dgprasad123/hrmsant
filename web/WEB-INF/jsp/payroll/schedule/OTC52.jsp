<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: OTC 52 ::</title>
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
                <td colspan="4" style="text-align:center;font-size:18px;font-weight:bold;"><b>(O.T.C NO.52)</b></td>
            </tr>
            <tr>
                <td  colspan="4" style="text-align:center;font-size:14px;">(See Rules-138(I), 265(I) & 368)</td>
            </tr>
            <tr>
                <td  colspan="4" style="text-align:center;font-size:14px;"> **********-*-********** </td>
            </tr>
        </table>
    </div>
           
    <div style="width:98%;margin: 0 auto;font-size:13px; font-family:verdana;">
        <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:12px; font-family:verdana;">
            <thead></thead>
            <tr>
                <td width="5%">&nbsp;</td>
                <td height="10px" width="80%">To,</td>
                <td width="5%">&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td style="font-size:13px;">&nbsp;&nbsp;&nbsp; The Treasury Officer,</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${OTC52Header.treasuryOffice}"/></td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td>&nbsp;&nbsp;&nbsp;<c:out value="${OTC52Header.branchManager}"/></td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td height="10px" colspan="3">&nbsp;</td>
            </tr>
        </table>    
                
        <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:14px; font-family:verdana;">
            <thead></thead>
            <tr>
                <td width="5%">&nbsp;</td>
                <td height="10px" width="80%">&nbsp;Sir,</td>
                <td width="5%">&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td class="printData" style="text-align:left;" >
                    <p style="line-height: 25px;text-align: justify;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;With reference to Finance Department 
                        Memo No. PRC-32/0433553/F dated 13-JUL-2005 please pay endorsed Bill No <b><c:out value="${OTC52Header.billDesc}"/></b> 
                        Dated <b><c:out value="${OTC52Header.billDate}"/></b>  for net amount Rs. <b><c:out value="${OTC52Header.netAmount}"/></b>. 
                        In words <b>(Rupees <c:out value="${OTC52Header.netAmountWord}"/></b>) Only, in shape of Transfer of credit and a copy of statement 
                        duly signed by you showing amount credit to bank Account of the Employees and current account of D.D.O may please be handed 
                        over to Sri...................................................................................
                        of this Directorate whose signature attested by me below.</br>
                    </p>
                </td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td height="10px" colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td height="10px" colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td style="text-align:right">Yours Faithfully,</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td style="text-align:left">Attested Specimen Signature</br>
                    of the Manager.
                </td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td style="text-align:right">Signature of Drawing & Disbursing Officer</td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td style="text-align:center"><b><u>Details of Transfer of Credit</u></b></td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td style="text-align:left">
                    <p style="line-height: 25px;">
                    ..................................................S.B Account of the........................................................</br>
                    No. of employees Rs. ...................................................................................No. of D.D.OS </br>
                    No. 10977507719 of........  ...........................................................................No. of employees </br>
                    ............................................................................................to Current Account of D.D.O towards loan.
                    </p>
                </td>
                <td>&nbsp;</td>
            </tr>
        </table>
                
    </div>  
</body>
</html>
