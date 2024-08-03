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
        <title>24Q Form</title>
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
            <jsp:param name="menuHighlight" value="FORM" />
        </jsp:include>
        <div style="width:95%;margin:0px auto;">
            <h1 style="margin:0px;margin-top:8px;font-size:13pt;font-weight:bold;text-align:center;">Quarterly statement of deduction of tax under sub section (3) of section 200 of the Income Tax Act, 1961 in respect of Salary.</h1>
        </div>
        <div style="width:95%;margin:0px auto;border:1px solid #DADADA;margin-top:10px;">
            <table width="100%" class="frmTable">
                <tr style="font-weight:bold;background:#EAEAEA;">
                    <td colspan="4">1.Particulars of Statement</td>
                </tr>
                <tr>
                    <td width="30%">Tax Deduction and Collection Account No. (TAN) </td>
                    <td width="20%" style="color:#890000;">${IB.tan}</td>
                    <td width="30%">Financial Year</td>
                    <td style="color:#890000;">2019-20</td>
                </tr>
                <tr>
                    <td>Last Tax Deduction and Collection Account No.</td>
                    <td></td>
                    <td>Assessment Year</td>
                    <td style="color:#890000;">2020-21</td>
                </tr>  
                <tr>
                    <td>Permanent Account No.</td>
                    <td style="color:#890000;">AAAAA1234A</td>
                    <td>Type of Deductor</td>
                    <td style="color:#890000;">State Government</td>
                </tr> 
                <tr>
                    <td>Is this a Revised Return (Yes/No)</td>
                    <td style="color:#890000;">No</td>
                    <td>Last Deductor Type</td>
                    <td></td>
                </tr>                 
            </table>
<table width="100%" class="frmTable" style="margin-top:10px;">
                <tr style="font-weight:bold;background:#EAEAEA;">
                    <td colspan="4">2.Particulars of Deductor (Employer)</td>
                </tr>
                <tr>
                    <td width="30%">Name: </td>
                    <td width="20%" style="color:#890000;">${IB.officeName}</td>
                    <td width="30%">Department</td>
                    <td style="color:#890000;">${IB.deptName}</td>
                </tr>
                <tr>
                    <td>DDO Code</td>
                    <td style="color:#890000;">${IB.ddoCode}</td>
                    <td>Flat No</td>
                    <td></td>
                </tr>  
                <tr>
                    <td>Name of the Premises/Building</td>
                    <td></td>
                    <td>Road/Street/Lane</td>
                    <td></td>
                </tr> 
                <tr>
                    <td>Area/Location</td>
                    <td style="color:#890000;">${IB.offAddress}</td>
                    <td>Town/City/District</td>
                    <td></td>
                </tr> 
                <tr>
                    <td>Pin Code</td>
                    <td style="color:#890000;">${IB.pinCode}</td>
                    <td>State</td>
                    <td style="color:#890000;">Odisha</td>
                </tr>
                <tr>
                    <td>Telephone</td>
                    <td style="color:#890000;">${IB.phone}</td>
                    <td>Email</td>
                    <td></td>
                </tr> 
                <tr>
                    <td>Alternate Telephone</td>
                    <td></td>
                    <td>Alternate Email</td>
                    <td></td>
                </tr>
                <tr>
                    <td>AIN (Account Office Identification Number)</td>
                    <td></td>
                    <td>Has Address changed since last return?</td>
                    <td></td>
                </tr>                
            </table>  
<table width="100%" class="frmTable" style="margin-top:10px;">
                <tr style="font-weight:bold;background:#EAEAEA;">
                    <td colspan="4">3.Particulars of the Person responsible for Deduction of Tax</td>
                </tr>
                <tr>
                    <td width="30%">Name: </td>
                    <td width="20%" style="color:#890000;">${IB.employerName}</td>
                    <td width="30%">Permanent Account Number</td>
                    <td></td>
                </tr>
                <tr>
                    <td>Designation</td>
                    <td style="color:#890000;">${IB.designation}</td>
                    <td>Flat No</td>
                    <td></td>
                </tr>  
                <tr>
                    <td>Name of the Premises/Building</td>
                    <td></td>
                    <td>Road/Street/Lane</td>
                    <td></td>
                </tr> 
                <tr>
                    <td>Area/Location</td>
                    <td></td>
                    <td>Town/City/District</td>
                    <td></td>
                </tr> 
                <tr>
                    <td>Pin Code</td>
                    <td></td>
                    <td>State</td>
                    <td></td>
                </tr>
                <tr>
                    <td>Telephone</td>
                    <td></td>
                    <td>Email</td>
                    <td>${IB.email}</td>
                </tr> 
                <tr>
                    <td>Alternate Telephone</td>
                    <td></td>
                    <td>Alternate Email</td>
                    <td></td>
                </tr>
                <tr>
                    <td>Has Address changed since last return?</td>
                    <td></td>
                    <td>Mobile No.</td>
                    <td style="color:#890000;">${IB.mobile}</td>
                </tr>                
            </table>              
        </div>
    </body>
</html>
