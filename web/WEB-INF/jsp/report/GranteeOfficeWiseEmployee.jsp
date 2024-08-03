<%-- 
    Document   : GranteeOfficeWiseEmployee
    Created on : 18 May, 2019, 11:52:44 AM
    Author     : Surendra
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
        <jsp:include page="../report/aerTab.jsp">
            <jsp:param name="menuHighlight" value="PART_B" />
        </jsp:include>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">

                    </div>
                </div>
                <div class="panel-body">
                    <table width="100%" class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr style="background-color: #0071c5;color: #ffffff;">
                                <th>Sl No</th>
                                <th width="40%">EMPLOYEE<br/>GROUP<br/>CADRE<br/>GRADE </th>
                                <th>HRMS ID</th>
                                <th>ACCOUNT TYPE</th>
                                <th>GPF NO</th>
                                <th width="40%">POST</th>
                                <th>GENDER<br />CATEGORY</th>
                                <th>DOB<br/>DOS</th>
                                <th>JOINING DATE<br/>HOME DIST</th>
                                <th>BASIC<br/>GR.PAY</th>
                                <th>REGULAR STATUS</th>
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
                    <table width="100%" class="table table-bordered table-hover table-striped"> 
                        <thead>
                            <tr>
                        <h3 style="background-color:#0071c5; color:#ffffff; text-align: center; font-weight: bold;"></h3>                                            
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
                <div class="panel-footer">

                </div>
            </div>
        </div>

    </body>
</html>

