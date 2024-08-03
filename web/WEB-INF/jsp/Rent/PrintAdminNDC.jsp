<%-- 
    Document   : ApplyNDC
    Created on : 1 Sep, 2020, 11:04:55 AM
    Author     : Manoj PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Apply Quarter NDC</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
        
    </head>
    <body onload="javascript: window.print();">
                 <div class="container">
                <h3>NDC Application Details</h3>
                <div class="panel-group">

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Personal Details:</div>
                        <input type="hidden" name="applicationId" id="applicationId" value="${qBean.applicationId}" />
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                                <tr>
                                    <td align="right" width="25%">Application ID:</td>
                                    <td><strong>RNTAPP-${qBean.applicationId}</strong></td>
                                </tr>                                
                                <tr>
                                    <td align="right" width="25%">Full Name:</td>
                                    <td><strong>${qBean.fullName}</strong></td>
                                </tr>
                                <tr>
                                    <td align="right">Father&rsquo;s Name:</td>
                                    <td><strong>${qBean.fatherName}</strong></td>
                                </tr>
                                <tr>
                                    <td align="right">Present Designation at the time of Retirement:</td>
                                    <td><strong>${qBean.designation}</strong></td>
                                </tr>
                                <tr>
                                    <td align="right">Date of Retirement:</td>
                                    <td><strong>${qBean.dateOfRetirement}</strong></td>
                                </tr>
                                <tr>
                                    <td align="right">Department from which retired:</td>
                                    <td><strong>${qBean.retirementDept}</strong></td>
                                </tr> 
                                <tr>
                                    <td align="right">Pension Sanctioning Authority:</td>
                                    <td>${qBean.pensionSanctioningAuthority}</td>
                                </tr>
                                <tr>
                                    <td align="right" width="30%">Mobile:</td>
                                    <td>${qBean.mobile}</td>
                                </tr>
                                <tr>
                                    <td align="right">Email:</td>
                                    <td>${qBean.email}</td>
                                </tr>  
                            </table>      

                        </div>
                    </div>

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Quarter Information:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">
                            <tr>
                                <td colspan="2"><strong>Last Quarter Details:</strong>
                                        <table class="table table-bordered">
                                            <tr style="background:#EAEAEA;font-weight:bold;">
                                                <td>Unit</td>
                                                <td>Quarter Type</td>
                                                <td>Building No</td>
                                            </tr>
                                            <tr>
                                                <td>${qBean.quarterUnit}</td>
                                                <td>${qBean.quarterType}</td>
                                                <td>${qBean.buildingNo}</td>
                                            </tr>             

                                                                                    
                                        </table>
                                    </td>
                                </tr> 
                                <tr>
                                    <td colspan="2"><strong>Quarter Vacated?</strong> Yes</td>
                                </tr>
                                <tr>
                                    <td colspan="2"><strong>Outstanding amount as per the eQuarter Ledger:</strong> <span style="color:#008900;font-weight:bold;">Rs. ${qBean.ledgerAmount}</span>
                                    <input type="hidden" id="ledgerAmount" value="${qBean.ledgerAmount}" /></td>
                                </tr>
                                <tr>
                                    <td width="350"><strong>Outstanding amount in respect of other Quarters (if any):</strong></td>
                                    <td><strong>Rs. ${qBean.outstandingAmount}</strong></td>
                                </tr>                                
                            </table>
                        </div>
                    </div> 
 


                </div>
            </div>

    </body>
</html>
