<%-- 
    Document   : DPAdmin
    Created on : Oct 6, 2020, 4:53:20 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"  autoFlush="true" buffer="64kb"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
            var rowSize = 20;
            var totalPages = 0;
            $(document).ready(function () {
                $(".loader").hide();
                $("#deptName").change(function () {
                    $(".loader").show();
                    $('#offcode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;

                    $.getJSON(url, function (result) {
                        $.each(result, function (i, field) {
                            $('#offcode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));

                        });
                    });
                    $(".loader").hide();
                });
            });
            function searchEmployee() {
                var deptName = $("#deptName").val();
                var offcode = $("#offcode").val();
                var criteria = $("#criteria").val();
                var searchString = $("#searchString").val();
                /*if (fiscalyear == "") {
                 alert("Choose Fiscal Year");
                 $("#fiscalyear").focus();
                 return false;
                 }*/
                if ($("#criteria").val() != "") {
                    if ($("#searchString").val() == "") {
                        alert("please Enter the Search value");
                        $("#searchString").focus();
                        return false;
                    }
                }
                $(".loader").show();
                $("#searchbtn").attr("disabled", true);
                $.post("getSearchEmployeeList.htm", {deptName: deptName, offcode: offcode, criteria: criteria, searchString: searchString, page: 1, rows: rowSize})
                        .done(function (data) {
                            var totalEmpFound = data.totalEmpFound;
                            totalPages = Math.round(totalEmpFound / rowSize);
                            $("#pagingstatus").text("1 of " + totalPages);
                            $("#paging").val(1);
                            var employeeList = data.employeeList;
                            populateDataInGrid(employeeList);
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        });
            }
            function populateDataInGrid(employeeList) {
                $("#employeedatagrid").empty();
                for (var i = 0; i < employeeList.length; i++) {
                    var postname = "";
                    if (employeeList[i].post) {
                        postname = employeeList[i].post;
                    }
                    var row = '<tr>' +
                            '<td><input type = "radio" name="rdEmp" value=' + employeeList[i].empid + '> </td>' +
                            '<td>' + employeeList[i].empid + '</td>' +
                            '<td>' + employeeList[i].gpfno + '</td>' +
                            '<td>' + employeeList[i].empName + '</td>' +
                            '<td>' + postname + '</td>' +
                            '<td>' + employeeList[i].dob + '</td>' +
                            '<td>' + employeeList[i].postGrpType + '</td>' +
                            '<td>' + employeeList[i].mobile + '</td>'+
                            '<td>' + employeeList[i].depstatus + '</td>'+
                            '<td><a target="_blank" href="redirect2DPAInterface.htm?empId='+employeeList[i].empid+'">Show</a></td>';

                    row = row + '</tr>';
                    $("#employeedatagrid").append(row);
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="viewDPAdmin.htm" commandName="searchEmployee" method="post" autocomplete="off" class="form-horizontal">
                    <form:hidden path="usertype" id="hidUserype"/>
                    <form:hidden path="hidEmpid" id="hidEmpid"/>                    
                    <div class="container-fluid">
                        <div class="form-group">
                            <label class="control-label col-sm-2">Department Name</label>
                            <div class="col-sm-9">
                                <form:select class="form-control" path="deptName">
                                    <form:option value="" label="All"/>
                                    <form:options items="${departmentList}" itemLabel="deptName" itemValue="deptCode"/>                                        
                                </form:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-2">Office Name</label>
                            <div class="col-sm-9">
                                <form:select class="form-control" path="offcode">
                                    <form:option value="" label="All"/>
                                    <form:options items="${officeList}" itemLabel="offName" itemValue="offCode"/>  
                                </form:select>
                            </div>
                        </div>                        
                        <div class="form-group">
                            <label class="control-label col-sm-2">Search criteria</label>
                            <div class="col-sm-3">
                                <form:select class="form-control" path="criteria">
                                    <form:option value="">select</form:option>
                                    <form:option value="GPFNO">GPF NO</form:option>
                                    <form:option value="HRMSID">HRMS ID</form:option>
                                    <form:option value="FNAME">FIRST NAME</form:option>
                                    <form:option value="MOBILE">Mobile Number</form:option>
                                </form:select>
                            </div>
                            <label class="control-label col-sm-1" >Search String</label>
                            <div class="col-sm-2" >
                                <form:input path="searchString" class="form-control"/>
                            </div>
                            <div class="col-sm-2" >
                                Show Disciplinary Proceeding <input type="checkbox" value="Y" id="dpchk"/>
                            </div>
                            <div class="col-sm-2" >
                                <input type="button" value="Search" id="searchbtn" onclick="searchEmployee()"/>
                            </div>                            
                            <div class="col-lg-1"> <div class="loader"></div> </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-1" >Result</label>
                            <div class="col-lg-1"><span id="pagingstatus"></span></div>
                            <div class="col-lg-1">
                                <input type="text" name="paging" id="paging" class="form-control">                            
                            </div>
                            <div class="col-lg-1">
                                <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return gotoPage()">Go</button>
                            </div>
                        </div>


                        <!-- -->

                        <div class="row">
                            <div class="col-lg-12">                            
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th width="1%">#</th>                                             
                                                <th width="6%">Employee Id</th> 
                                                <th width="8%">GPF No</th> 
                                                <th width="15%">Employee Name</th>                    
                                                <th width="20%">Designation</th>
                                                <th width="8%">Date of Birth</th>
                                                <th width="5%" align="center">Group</th>                                                
                                                <th width="10%">Mobile</th>
                                                <th width="10%">Have Proceedings</th>
                                                <th width="20%">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody id="employeedatagrid">                                        

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>                            
                </form:form>
            </div>
    </body>
</html>
