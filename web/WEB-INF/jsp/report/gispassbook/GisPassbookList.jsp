<%-- 
    Document   : SectionDefination
    Created on : Nov 21, 2016, 3:12:08 PM
    Author     : Manas Jena
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <style type="text/css">
            @media print {

                tr{
                    page-break-after: always;
                    display: block;
                }
            } 
        </style>
    </head>
    <body style="margin:0px" class="Background-Color:#FFFFFF;">
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-body"> 
                    <table style="height:70px;align:center" id="innercontainertbl" border="0" cellpadding="0" cellspacing="0" width="100%">
                        <thead> </thead>
                        <tbody>
                            <c:forEach items="${empInfo}" var="empinfo" >
                                <tr>
                                    <th class="printLabel" style="text-align:left;font-size: 14;" width="15%">Employee Name</th>
                                    <td width="35%">&nbsp;<font class="printData" style="text-align:left;font-size: 14;">${empinfo.empName}</font>&nbsp;</td>
                                    <th class="printLabel" style="text-align:left;font-size: 14;" width="20%">HRMS ID</th> 
                                    <td class="printData" style="text-align:left;font-size: 14;" width="30%">&nbsp;<font class="printData" style="text-align:left;font-size: 14;">${empinfo.empId}</font>&nbsp;</td>
                                </tr>
                                <tr>
                                    <th class="printLabel" style="text-align:left;font-size: 14;" width="15%">GPF/ PPAN No.</th>
                                    <td class="printData" style="text-align:left;font-size: 14;" width="30%">&nbsp${empinfo.gfpNo}&nbsp</td>
                                    <th class="printLabel" style="text-align:left;font-size: 14;">INSURANCE ACCOUNT NO.</th>
                                    <td class="printData" style="text-align:left;font-size: 14;" width="30%">&nbsp${empinfo.gisNo}&nbsp</td>
                                </tr>
                            </c:forEach>
                        </tbody></table>
                    <table class="table table-bordered" style=" border: 3px solid black;">
                        <thead style="border: 3px solid black;">
                            <tr style="height:45px">
                                <th colspan="6" class="printLabel" style="text-align:center;font-size: 16;">
                                    INSURANCE PASS BOOK
                                </th>
                            </tr>  
                            <tr style="height:45px;border: 3px solid black;">
                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="10%">SL NO</th>
                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="10%">Instrument<br/> No.</th>
                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="10%">Treasury <br/>Voucher No.</th>
                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="10%">Amount<br/>Deposited</th>
                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="30%">Name of the Treasury & Date of Deposit</th>
                                <th class="printLabel" style="text-align:center;font-size: 14;border: 3px solid black;" width="30%">Deposited By</th>
                            </tr>
                        </thead>
                        <tbody style="border: 3px solid black;">
                            <c:forEach items="${gisList}" var="gis" varStatus="cnt">
                                <tr style="height:90px;border: 3px solid black;">    
                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;">${cnt.index+1}</td>
                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;">${gis.instru}</td>
                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;">${gis.vchno}</td>
                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;">${gis.amoutDeposit}</td>
                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;">
                                        <c:if test = "${not empty gis.trName}">
                                            ${gis.trName}<br/>
                                        </c:if>    
                                        <strong>Date of Deposit: ${gis.dod}</strong>
                                    </td>
                                    <td class="printData" style="text-align:center;font-size: 12;border: 3px solid black;">${gis.depositBy}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>