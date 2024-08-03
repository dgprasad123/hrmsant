<%-- 
    Document   : Main24Q
    Created on : 5 Nov, 2019, 5:20:38 PM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>24Q Form:: Challan</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <style type="text/css">
            .frmTable td{padding:8px;}
        </style>
    </head>
    <body style="margin-top:10px;">

        <jsp:include page="ITTab.jsp">
            <jsp:param name="menuHighlight" value="24Q" />
        </jsp:include>
        <div style="width:98%;margin:0px auto;">
<p style="margin:0px;"><a href="Challan.htm" class="btn btn-primary btn-lg" style="background:#890000;">Challan Form</a>
            <a href="Annexure-I-24Q.htm" class="btn btn-primary btn-lg" style="background:#890000;">Annexure-I</a></p>            
            <h1 style="margin:0px;margin-top:8px;font-size:13pt;font-weight:bold;text-align:center;">Form No: 24Q</h1>
        </div>
        <div style="width:98%;margin:0px auto;border:1px solid #DADADA;margin-top:10px;">
            <table width="100%" class="table table-bordered" style="font-size:9pt;">
                <tr style="color:#890000;">
                    <td>Sr No.</td>
                    <td>TDS</td>
                    <td>Surcharge</td>
                    <td>Education Cess</td>
                    <td>Interest</td>
                    <td>Fee</td>
                    <td>Penalty/ Others</td>
                    <td>Last total tax deposited</td>
                    <td>Total amount deposited as per challan/Book Adjustment (2+3+4+5+6+7)</td>
                    <td>Check/DD No.(if any)</td>
                    <td>Last BSR Code/24G Receipt Number</td>
                    <td>BSR Code/ Receipt Number of Form No. 24G</td>
                    <td>Last Date on which Tax Deposited</td>
                    <td>Date on which Amount Deposited through challan/ Date of Transfer Voucher (DD/MM/YYYY)</td>
                    <td>Last DDO/ Transfer Voucher/ Challan Serial No.</td>
                    <td>Challan Serial No.DDO Serial No. of Form 24G</td>
                    <td>Mode of Deposit through Book Adjustment (Yes/No)</td>
                    <td>Minor Head of Challan 200-TDS payable by taxpayer 400-TDS regular assessment (Raised by IT Dept)</td>
                    <td>Challan Balance as per Consolidated File</td>
                </tr>
                <tr style="background:#EAEAEA;">
                    <td align="center">1</td>
                    <td align="center">2</td>
                    <td align="center">3</td>
                    <td align="center">4</td>
                    <td align="center">5</td>
                    <td align="center">6</td>
                    <td align="center">7</td>
                    <td align="center">8</td>
                    <td align="center">9</td>
                    <td align="center">10</td>
                    <td align="center">11</td>
                    <td align="center">12</td>
                    <td align="center">13</td>
                    <td align="center">14</td>
                    <td align="center">15</td>
                    <td align="center">16</td>
                    <td align="center">17</td>
                    <td align="center">18</td>
                    <td align="center">19</td>
                </tr>
                <tr>
                    <td align="center">[301]</td>
                    <td align="center">[302]</td>
                    <td></td>
                    <td align="center">[303]</td>
                    <td align="center">[304]</td>
                    <td align="center">[305]</td>
                    <td align="center">[306]</td>
                    <td></td>
                    <td align="center">[307]</td>
                    <td></td>
                    <td></td>
                    <td align="center">[309]</td>
                    <td></td>
                    <td align="center">[311]</td>
                    <td></td>
                    <td align="center">[310]</td>
                    <td align="center">[308]</td>
                    <td align="center">[312]</td>
                    <td></td>
                </tr>
                <c:forEach items="${IB.dataList}" var="list" varStatus="cnt">
                    <tr>
                        <td>
                            ${cnt.index+1}
                        </td>
                        <td>
                            ${list.tdsAmount}
                        </td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td>${list.tdsAmount}</td>                                            
                        <td></td>
                        <td></td>
                        <td>${list.challanNo}</td>
                        <td></td>
                        <td>${list.dateDeposited}</td>                                            
                        <td></td>                                           
                        <td>${list.ddoSerial}</td>
                        <td>Yes</td> 
                        <td></td> 
                        <td></td> 
                    </tr>
                </c:forEach>
            </table>            
        </div>
    </body>
</html>
