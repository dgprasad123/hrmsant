<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: VOUCHER SLIP ::</title>
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
            .donotPrintPaging{
                display:none;
            }
            .printData{
                font-family:verdana; 
                font-size:12px;
                text-transform: none;
                text-align: left;
            }
            .borderRight{
                border-right: 1px solid;
                font-family:verdana; 
                font-size:12px;
                text-transform: none;
                text-align: left;
            }
            .borderTopRightBottom{
                border-right: 1px solid;
                border-top: 1px solid;
                border-bottom: 1px solid;
                font-family:verdana; 
                font-size:12px;
                text-transform: none;
                text-align: left;
            }
            .borderTop{
                border-top: 1px solid;
                font-family:verdana; 
                font-size:12px;
                text-transform: none;
                text-align: left;
            }
            
            .borderBottom{
                border-bottom: 1px solid;
                font-family:verdana; 
                font-size:12px;
                text-transform: none;
                text-align: left;
            }
            
            .borderBottomRight{
                border-bottom: 1px solid;
                border-right: 1px solid;
                font-family:verdana; 
                font-size:12px;
                text-transform: none;
                text-align: left;
            }
            
            .borderTopBottom{
                border-top: 1px solid;
                border-bottom: 1px solid;
                font-family:verdana; 
                font-size:12px;
                text-transform: none;
                text-align: left;
            }
        </style>
    </head>
    <body>
    <div style="width:90%;margin: 0 auto;">
        <table width="100%" border="0">
            <tr>
                <td class="printData" style="text-align:center;font-size:18px;font-weight:bold;">
                    <b>VOUCHER SLIP</b>
                </td>
            </tr>
            <tr>
                <td class="printData" style="text-align:center;font-size:17px;">
                    FORM O.G.F.R. -25<br/><br/>
                    (See: Rule 318)<br/><br/>
                    (To be returned in original by the Treasury Officer)
                </td>
            </tr>
            <tr>
                <td class="printData" style="text-align:center;font-size:15px;">
                    Demand No. &nbsp;<c:out value="${VoucherHeadr.demandno}"/>
                </td>
            </tr>
            <tr>
                <td class="printData" style="text-align:center;font-size:15px;">
                    Major Head  &nbsp;<c:out value="${VoucherHeadr.majorhead}"/>
                </td>
            </tr>
        </table>
    </div>
           
    <div style="width:98%;margin: 0 auto;font-size:13px; font-family:verdana;">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td class="borderTopRightBottom" width="50%">
                    To<br/>
                    &nbsp;The Treasury Officer, <c:out value="${VoucherHeadr.treasuryName}"/><br/><br/>
                    Please Furnish the Treasury Voucher No and Date of the Bill sent Herewith for encashment.<br/>
                    <br/><br/>
                    (Drawing Officer)<br/>
                    &nbsp;<!-- DDO Name will be put here if needed -->
                </td>
                <td class="borderTopBottom" width="50%">
                    (To be filled in the Treasury)<br/><br/>
                    To<br/>
                    &nbsp;The &nbsp;<!-- DDO Name will be put here if needed --><br/>
                    <br/>
                    Return with Treasury Voucher No. and date as noted below
                    <br/>
                    <br/>
                    (Signature Treasury Officer)
                </td>
            </tr>
        </table>    
                
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr style="height:50px">
                <td class="borderRight" width="50%">Bill No. : <b><c:out value="${VoucherHeadr.billDesc}"/></b>
                </td>
                <td class="printData" width="50%">Amount Paid :</td>
            </tr>
            <tr style="height:50px">
                <td class="borderRight">Bill Particulars : </td>
                <td class="printData">Treasury Voucher No :</td>
            </tr>
            <tr style="height:50px">
                <td class="borderRight">
                    Monthly Pay bill of _____________________________________________/ </br></br><c:out value="${VoucherHeadr.officeName}"/>
                    for <c:out value="${VoucherHeadr.month}"/> / <c:out value="${VoucherHeadr.year}"/>
                </td>
                <td class="printData">
                    Date
                </td>
            </tr>
            <tr style="height:50px">
                <td class="borderRight">Gross : <b><c:out value="${VoucherHeadr.grossAmount}"/></b>&nbsp;</td>
                <td class="printData">&nbsp;</td>
            </tr>
            <tr style="height:50px">
                <td class="borderRight">Net : <b><c:out value="${VoucherHeadr.netAmount}"/></b></td>
                <td class="printData">&nbsp;</td>
            </tr>
            <tr style="height:50px">
                <td class="borderBottomRight">(Signature of the Accountant)</td>
                <td class="borderBottom">(Signature of the Treasury Accountant)</td>
            </tr>
        </table>
                
    </div>  
</body>
</html>
