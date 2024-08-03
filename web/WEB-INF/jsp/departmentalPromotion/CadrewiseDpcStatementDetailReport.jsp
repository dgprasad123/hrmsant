<%-- 
    Document   : CadrewiseNrcStatementDetailReport
    Created on : Feb 19, 2020, 3:34:01 PM
    Author     : manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
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
        <style type="text/css">
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
            }
            #myInput {
                background-image: url('/images/searchicon.png'); /* Add a search icon to input */
                background-position: 10px 12px; /* Position the search icon */
                background-repeat: no-repeat; /* Do not repeat the icon image */
                width: 100%; /* Full-width */
                font-size: 16px; /* Increase font-size */
                padding: 12px 20px 12px 40px; /* Add some padding */
                border: 1px solid #ddd; /* Add a grey border */
                margin-bottom: 12px; /* Add some space below the input */
            }
        </style>
        <script>
            function addEmployeeToDPC(me, empId) {
                var url = "addcadrewisenrcStatementDetailReport.htm";
                var vdpcId = $("#dpcId").val();
                var parentHtmlElement = $(me).parent();
                $(parentHtmlElement).html('<span class="label label-danger">Loading..</span>');
                $.post(url, {dpcId: vdpcId, hrmsId: empId})
                        .done(function(data) {
                            var html = '<span class="label label-success">Added</span>';
                            html = html+'<a href="javascript:void(0);" class="btn btn-danger" onclick="removeEmployeeFromDPC(this,\''+empId+'\')"><span class="glyphicon glyphicon-remove"></span> Remove</a>';
                            $(parentHtmlElement).html(html);
                        });

            }
            function removeEmployeeFromDPC(me, empId){
                var url = "removecadrewisenrcStatementDetailReport.htm";
                var vdpcId = $("#dpcId").val();
                var parentHtmlElement = $(me).parent();
                $(parentHtmlElement).html('<span class="label label-danger">Loading..</span>');
                $.post(url, {dpcId: vdpcId, hrmsId: empId})
                        .done(function(data) {
                            $(parentHtmlElement).html('<a href="javascript:void(0);" class="btn btn-success" onclick="addEmployeeToDPC(this,\''+empId+'\')">Add</a>');
                        });
            }
            function myFunction() {
                // Declare variables
                var input, filter, table, tr, td, i, txtValue;
                input = document.getElementById("myInput");
                filter = input.value.toUpperCase();
                table = document.getElementById("myTable");
                tr = table.getElementsByTagName("tr");

                // Loop through all table rows, and hide those who don't match the search query
                for (i = 0; i < tr.length; i++) {
                    td = tr[i].getElementsByTagName("td")[1];
                    if (td) {
                        txtValue = td.textContent || td.innerText;
                        if (txtValue.toUpperCase().indexOf(filter) > -1) {
                            tr[i].style.display = "";
                        } else {
                            tr[i].style.display = "none";
                        }
                    }
                }
            }
        </script>
    </head>
    <body style="background-color:#FFFFFF;margin-top:10px;">
        <form:form action="viewdpcStatement.htm" method="POST" commandName="departmentPromotionBean" class="form-inline">
            <form:hidden path="dpcId"/>
            <div style="padding: 0px 15px 0px 15px;">
                <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for names..">
            </div>
            <div class="panel-body table-responsive" style="height: 540px;">                
                <table class="table table-bordered" id="myTable">
                    <thead>
                        <tr>
                            <th width="5%">Si No.</th>
                            <th width="10%">Employee Name / designation</th>
                            <th width="10%">Group</th>
                            <th width="10%">Date Of Birth</th>
                            <th width="10%">Employee Type</th>
                            <th width="10%">Action</th>
                        </tr>
                    </thead>
                    <tbody>                                        
                        <c:forEach items="${employeeListCadrewise}" var="employee" varStatus="cnt">
                            <tr>
                                <td>${cnt.index+1}</td>
                                <td>${employee.empName}, ${employee.empPost}</td>
                                <td>${employee.postGroupType}</td>
                                <td>${employee.dob}</td>
                                <td>${employee.deployType}</td>
                                <td> 
                                    <c:if test="${employee.isadded eq 'N'}">
                                        <a href="javascript:void(0);" class="btn btn-success" onclick="addEmployeeToDPC(this,'${employee.hrmsId}')">Add</a>
                                    </c:if>
                                    <c:if test="${employee.isadded eq 'Y'}">
                                        <span class="label label-success">Added</span>
                                        <a href="javascript:void(0);" class="btn btn-danger" onclick="removeEmployeeFromDPC(this,'${employee.hrmsId}')"><span class="glyphicon glyphicon-remove"></span> Remove</a>
                                    </c:if>
                                </td>
                                
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="panel-footer" style="margin: 5px 15px 0px 15px;">
                <input type="submit" class="btn btn-primary" name="action" value="View"/>  
                <input type="submit" class="btn btn-primary" name="action" value="Back"/>  
            </div>
        </form:form>
    </body>
</html>
