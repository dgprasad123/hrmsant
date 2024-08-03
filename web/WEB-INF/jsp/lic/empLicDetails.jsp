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
        <script type='text/javascript'>
            function delete_group(ids,status) {
                if(status=="Y"){
                    var sta="Active";
                } else {
                     var sta="Stopped";
                    
                }
                var con = confirm("Do you want to "+sta+" this Loan information");
                if (con) {
                    window.location = "deleteLicData.htm?LicId=" + ids+"&status="+status;

                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">

                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th >SL NO</th>
                                <th>Type</th>
                                <th>Policy No</th>
                                <th>Amount</th>
                                <th>WEF</th>
                                <th>Month</th>
                                 <th>Year</th>
                                 <th>&nbsp;</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${licList}" var="lic" varStatus="cnt">
                                 <c:set var="empId" value="${lic.empid}" />
                                <tr>
                                    <td>${cnt.index+1}</td>
                                    <td>${lic.insuranceType}</td>
                                    <td>${lic.policyNo}</td>
                                    <td>${lic.subAmount}</td>
                                    <td>${lic.wef}</td>
                                    <td>${lic.month}</td>
                                    <td>${lic.year}</td>

                                    <td><a href="editEmployeeLic.htm?elId=${lic.elId}">Edit</a>&nbsp;|&nbsp;
                                        <c:if test = "${not empty lic.status && lic.status=='Active'}">
                                            <strong ><a href="#" onclick="delete_group(${lic.elId},'N')" style='color:green'>Active</a></strong> 
                                            
                                        </c:if>
                                        <c:if test = "${not empty lic.status && lic.status=='Stopped'}">
                                            <strong><a href="#" onclick="delete_group(${lic.elId},'Y')"  style='color:red'>Stopped</a></strong> 
                                            
                                        </c:if>
                                    
                                    </td>
                                    
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">                    
                    <button type="submit" class="btn btn-default" onclick="javascript: self.location = 'employeeLicAction.htm?empId=${EmpId}'">New LIC</button>                    
                </div>
            </div>
        </div>
    </body>
</html>
