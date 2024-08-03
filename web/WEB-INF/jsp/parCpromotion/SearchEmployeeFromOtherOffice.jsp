<%-- 
    Document   : SearchEmployeeFromOtherOffice
    Created on : 27 Apr, 2021, 12:15:35 PM
    Author     : Manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>
        <script type="text/javascript">

            $(document).ready(function() {

                $("#deptCode").change(function() {
                    $('#offCode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#offCode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                });

                $("#offCode").change(function() {
                    var url = 'getAllSPCWithEmployee.htm?offCode=' + this.value;
                    $.getJSON(url, function(result) {
                        $('#postCodemappost').empty();
                        $.each(result, function(i, field) {
                            $('#postCodemappost').append($('<option>', {
                                value: field.spc,
                                text: field.postname + ", (" + field.empname + ")"
                            }));
                        });
                    });
                });

            });

            function addEmployeeToGroupc(me, reviewedEmpId, reviewedSpc, groupCpromotionId) {
                var url = "addgroupcEmpList.htm";
                $.post(url, {reviewedempId: reviewedEmpId, reviewedspc: reviewedSpc, groupCpromotionId: groupCpromotionId})
                        .done(function(data) {
                            console.log($(me).html());
                            $(me).parent().html("<span>Added</span>");
                        });

            }

        </script>
    </head>
    <div id="page-wrapper">
        <div class="container-fluid">
            <form:form action="searchEmployeeList.htm" method="POST" commandName="groupCInitiatedbean" class="form-horizontal">
                <form:hidden path="groupCpromotionId"/>
                <div class="panel panel-default">
                    <div class="panel-body">                       

                        <div class="form-group">
                            <label class="control-label col-sm-2">Department Name: </label>
                            <div class="col-sm-8">
                                <select class="form-control" name="deptCode" id="deptCode">
                                    <option value="">Select</option>
                                    <c:forEach items="${departmentList}" var="department">
                                        <option value="${department.deptCode}">${department.deptName}</option>
                                    </c:forEach>                                        
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-2">Office Name</label>
                            <div class="col-sm-8">
                                <select class="form-control" name="offCode" id="offCode">
                                    <option value="">Select</option>                                            
                                </select>
                            </div>
                            <div class="col-sm-2">
                                <input type="submit" name="action" class="btn btn-default" value="Get Employee"/> 
                            </div>
                        </div>

                        <div style="height: 500px;overflow: auto;">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>

                                    <tr>
                                        <th>#</th>
                                        <th>Employee Name</th>
                                        <th>Designation</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${empList}" var="employee" varStatus="cnt">
                                        <tr>
                                            <td>${cnt.index+1}</td>
                                            <td>${employee.reviewedempname}</td>
                                            <td>${employee.reviewedpost}</td>
                                            <td>
                                                <c:if test="${employee.alreadyAdded eq 'Y'}">
                                                    <span>Already Added</span>
                                                </c:if>
                                                <c:if test="${employee.alreadyAdded eq 'N'}">
                                                    <button type="button" onclick="addEmployeeToGroupc(this, '${employee.reviewedempId}', '${employee.reviewedspc}', '${groupCInitiatedbean.groupCpromotionId}')" class="btn btn-default">Add</button>
                                                </c:if>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="panel-footer">
                            <input type="submit" name="action" class="btn btn-default" value="Get Selected Employee List"/>
                            <input type="submit" name="action" class="btn btn-default" value="Back"/>
                        </div>

                    </div>
                </div>

            </form:form>
        </div>
    </div>
</html>
