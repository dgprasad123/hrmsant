<%-- 
    Document   : NominationRollDetailListViewPage
    Created on : 2 Nov, 2020, 2:50:26 PM
    Author     : Surendra
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $("#addemployeeModal").on("show.bs.modal", function (e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
            })
            function validate() {
                var numberofchecked = $('input:checkbox:checked').length;
                if (numberofchecked > 0) {
                    var c = confirm('You have selected ' + numberofchecked + ' no of employees for nomination. Are you sure to continue?');
                    if (c) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    alert('You have not selected any employee for nomination.');
                    return false;
                }
            }
        </script>

        <title>JSP Page</title>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> Nomination Detail Employee List</h3>

                        <div class="panel-heading">
                            <a href="nominationrollList.htm"><input type="button" class="btn btn-primary" value="Back"/></a> 
                        </div>
                        <div class="panel-body">
                             
                            <c:if test="${empList.size() gt 0}">
                                <c:if test="${not empty EmpDetNom.nominationMasterId}">
                                    <div class="table-responsive">
                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover table-striped">
                                                <thead>
                                                    <tr>
                                                        <th>#</th>
                                                        <th>HRMS ID/ GPF No</th>
                                                        <th>Employee Name</th>
                                                        <th>DOB/DOS</th>
                                                        <th>Date of Joining</th>
                                                        <th>Form Status</th>
                                                        <th style="text-align: center">Action</th>

                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${empList}" var="emp" varStatus="counter">
                                                        <tr>
                                                            <td width="10%" >${counter.count}</td>
                                                            <td width="10%">${emp.empId} </br> ${emp.gpfno}</td>
                                                            <td width="35%">${emp.empName}</td>
                                                            <td width="10%">
                                                                ${emp.dob} </br> <span style="color:red">${emp.dos}</span>
                                                            </td>
                                                            <td width="10%" style="text-align: center">${emp.doj}</td>
                                                            <td width="10%">
                                                                <c:if test="${emp.formCompletionStatus eq 'Y'}"> Completed </c:if>
                                                            </td>
                                                            <td width="10%" style="text-align: center">
                                                                    <a href="EmployeeNominationViewController.htm?nominationMasterId=${emp.nominationMasterId}&nominationDetailId=${emp.nominationDetailId}"><i class="fa fa-pencil-square-o  fa-2x"></i></a>
                                                            </td>
                                                            <td width="15%" style="text-align: center">
                                                                <a href="downloadNominationFormController.htm?nominationMasterId=${emp.nominationMasterId}&nominationDetailId=${emp.nominationDetailId}"><i class="fa fa-file-pdf-o  fa-2x"></i></a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </c:if>
                                
                            </c:if>
                        </div>
                        <div class="panel-footer">
                            <a href="nominationrollList.htm"><input type="button" class="btn btn-primary" value="Back"/></a> 
                            
                        </div>
                    </div>

                    
                
            </div>
        </div>
    </body>
</html>

