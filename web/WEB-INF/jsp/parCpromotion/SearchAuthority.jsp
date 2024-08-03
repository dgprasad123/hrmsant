<%-- 
    Document   : SearchAuthority
    Created on : 28 Jul, 2020, 10:43:17 AM
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
                    //var url = 'getPARPostListJSON.htm?deptcode='+ deptcode +"&offCode="+ this.value;
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

        </script>
    </head>

    <form:form action="searchAuthortityList.htm" method="POST" commandName="groupCEmployee" class="form-horizontal">

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
                        <input type="submit" name="action" class="btn btn-default" value="Get Authority"/> 
                    </div>
                </div>
            </div>
        </div>


        <div class="panel panel-default">
            <div class="panel-body" style="overflow: auto;">
                <div class="table-responsive">
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Employee Name</th>
                                <th class="col-sm-6">Designation</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${authoritylist}" var="higherAuthority" varStatus="count">
                                <tr>

                                    <td><input type = "radio" name="reportingempId" value='${higherAuthority.reportingempId}-${higherAuthority.reportingspc}'> </td>
                                    <td>${higherAuthority.reportingempname}</td>
                                    <td>${higherAuthority.reportingpost}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="panel-footer">
                <form:hidden path="groupCpromotionId"/>
                <form:hidden path="remarkauthoritytype"/>
                <form:hidden path="taskId"/>
                <input type="submit" name="action" value="Submit" class="btn btn-default" />
            </div>
        </div>


    </form:form>

</html>
