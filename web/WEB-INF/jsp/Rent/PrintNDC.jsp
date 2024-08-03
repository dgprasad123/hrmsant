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
           <c:if test="${qBean.isApplied}">
                 <div class="container">
                <h3>Application Acknowledgement for Quarters NDC (GA Pool Quarters)</h3>
                <div class="panel-group">

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Personal Details:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:11pt;">
                                <tr>
                                    <td align="right" width="25%">Application ID:</td>
                                    <td><strong>RNTAPP-${qBean.applicationId}</strong><input type="hidden" name="fullName" value="${qBean1.fullName}" /></td>
                                </tr>                                
                                <tr>
                                    <td align="right" width="25%">Full Name:</td>
                                    <td><strong>${qBean1.fullName}</strong><input type="hidden" name="fullName" value="${qBean1.fullName}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Father&rsquo;s Name:</td>
                                    <td><strong>${user.fname}</strong><input type="hidden" name="fatherName" value="${user.fname}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Present Designation at the time of Retirement:</td>
                                    <td><strong>${qBean.retSPC}</strong><input type="hidden" name="designation" value="${qBean.retSPC}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Date of Retirement:</td>
                                    <td><strong>${user.empDos}</strong><input type="hidden" name="dateOfRetirement" value="${user.empDos}" /></td>
                                </tr>
                                <tr>
                                    <td align="right">Department from which retired:</td>
                                    <td><strong>${qBean.retirementDept}</strong><input type="hidden" name="retirementDept" value="${qBean.retirementDept}" /></td>
                                </tr> 
                                <tr>
                                    <td align="right">Pension Sanctioning Authority:</td>
                                    <td>${qBean.pensionSanctioningAuthority}</td>
                                </tr>
                                <tr>
                                    <td align="right" width="30%">Mobile:</td>
                                    <td>${user.mobile}</td>
                                </tr>
                                <tr>
                                    <td align="right">Email:</td>
                                    <td>${user.lname}</td>
                                </tr>  
                            </table>      

                        </div>
                    </div>

                    <div class="panel panel-success">
                        <div class="panel-heading" style="font-weight:bold;font-size:13pt;">Other Information:</div>
                        <div class="panel-body">
                            <table class="table" style="font-size:12pt;">

                                    <c:if test="${qBean.hasOccupied == 'Yes'}">
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
</c:if>
                            </table>

                        </div>
                    </div>    
 


                </div>
            </div>

            </c:if>
    </body>
</html>
