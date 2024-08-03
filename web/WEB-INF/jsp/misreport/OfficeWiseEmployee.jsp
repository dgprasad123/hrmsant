<%-- 
    Document   : OfficeWiseEmployee
    Created on : Feb 9, 2017, 4:42:58 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>

        <div id="wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;">EMPLOYEE LIST</div>
                        <div class="panel-body">
                            <div class="table-responsive">
                                <table width="100%" class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr style="background-color: #0071c5;color: #ffffff;">
                                            <th>Sl No</th>
                                            <th width="40%">EMPLOYEE<br/>GROUP<br/>CADRE<br/>GRADE </th>
                                            <th>HRMS ID</th>
                                            <th>ACCOUNT TYPE</th>
                                            <th>GPF NO</th>
                                            <th width="40%">POST</th>
                                            <th>GENDER<br />POST GRP<br />CATEGORY</th>
                                            <th>DOB<br/>DOS</th>
                                            <th>JOINING DATE<br/>HOME DIST</th>
                                            <th>BASIC<br/>GR.PAY</th>
                                            <th>REGULAR STATUS<br />PAY COMMISSION</th>
                                            <th width="10%">EMAIL ID<br />MOBILE NO</th>
                                            <th>AADHAR NO<br />EPIC NO</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${employees}" var="billGroup">
                                            <tr style="font-weight:bold;background:#FFD1AA;">
                                                <td colspan="13">${billGroup.billGroupName}</td>
                                            </tr>
                                            <c:forEach items="${billGroup.empList}" var="emp" varStatus="count">
                                                <tr>
                                                    <td>${count.index + 1}</td>
                                                    <td>${emp.fname} ${emp.mname} ${emp.lname}<br/>${emp.cadreCode}<br/>${emp.cadreGrade}</td>
                                                    <td>${emp.empid}</td>
                                                    <td>${emp.accttype}</td>
                                                    <td>${emp.gpfno}</td>
                                                    <td>${emp.post}</td>
                                                    <td>${emp.gender}<br />${emp.postGrpType}<br />${emp.category}</td>
                                                    <td>${emp.dob}<br/>${emp.dor}</td>
                                                    <td>${emp.joindategoo}<br/>${emp.permanentdist}</td>
                                                    <td>${emp.basic}<br/>${emp.gp}</td>
                                                    <td>${emp.remark}<br />${emp.sltPayCommission}</td>
                                                    <td>${emp.email}<br />${emp.mobile}</td>
                                                    <td>${emp.aadhaarno}<br />${emp.epic}</td>
                                                </tr>
                                            </c:forEach>
                                        </c:forEach>    
                                    </tbody>
                                </table>
                                <table width="100%" class="table table-bordered table-hover table-striped"> 
                                    <thead>
                                        <tr>
                                            <h3 style="background-color:#0071c5; color:#ffffff; text-align: center; font-weight: bold;">CONTRACTUAL</h3>                                            
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${contEmployees}" var="billGroup">
                                            <tr style="font-weight:bold;background:#FFD1AA;">
                                                <td colspan="13">${billGroup.billGroupName}</td>
                                            </tr>
                                            <c:forEach items="${billGroup.empList}" var="emp" varStatus="count">
                                                <tr>
                                                    <td>${count.index + 1}</td>
                                                    <td width="15%">${emp.fname} ${emp.mname} ${emp.lname}<br/>${emp.cadreCode}<br/>${emp.cadreGrade}</td>
                                                    <td>${emp.empid}</td>
                                                    <td>${emp.accttype}</td>
                                                    <td>${emp.gpfno}</td>
                                                    <td>${emp.post}</td>
                                                    <td>${emp.gender}<br />${emp.postGrpType}</td>
                                                    <td>${emp.dob}<br/>${emp.dor}</td>
                                                    <td>${emp.joindategoo}<br/>${emp.permanentdist}</td>
                                                    <td>${emp.basic}<br/>${emp.gp}</td>
                                                    <td>${emp.remark}</td>
                                                    <td>${emp.email}<br />${emp.mobile}</td>
                                                    <td>${emp.aadhaarno}<br />${emp.epic}</td>
                                                </tr>
                                            </c:forEach>
                                        </c:forEach>    
                                    </tbody>                                    
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
