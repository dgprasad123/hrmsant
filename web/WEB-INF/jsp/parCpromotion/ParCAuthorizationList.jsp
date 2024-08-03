<%-- 
    Document   : ParCAuthorizationList
    Created on : 12 Jan, 2022, 11:57:36 AM
    Author     : Manisha
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


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

        </style>
        <script type="text/javascript">
            var togglePrivilageAssignment;
            $(document).ready(function() {
                $("#officewiseDiv").hide();
                $("#districtwiseDiv").hide();
                $("#deptName").change(function() {
                    $('#offcode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#offcode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                    var url = 'getDeptWisePostListJSON.htm?deptCode=' + this.value;
                    $.getJSON(url, function(result) {
                        $('#postCode').empty();
                        $.each(result, function(i, field) {
                            $('#postCode').append($('<option>', {
                                value: field.postcode,
                                text: field.post
                            }));
                        });
                    });
                });

                //getEmployeeNameWithSPCJSON
                $("#postCode").change(function() {
                    var url = 'getEmployeeNameWithSPCJSON.htm?offcode=' + $('#offcode').val() + '&postCode=' + this.value;
                    $.getJSON(url, function(result) {
                        $('#employee').empty();
                        $.each(result, function(i, field) {
                            $('#employee').append($('<option>', {
                                value: field.spc,
                                text: field.empname + '(' + field.empid + ',' + field.gpfNo + ')'

                            }));
                        });
                    });
                });

            });
            function getDeptWiseOfficeList() {
                var deptcode = $('#hidAuthDeptCode').val();
                $('#hidAuthOffCode').empty();
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function assignPrivilegeForGroupC() {
                var tSpc = $("#employee").val();
                var toffCode;
                var tdistCode;
                if (togglePrivilageAssignment == "O") {
                    toffCode = $("#hidAuthOffCode").val();
                    tdistCode = null;
                } else if (togglePrivilageAssignment == "D") {
                    tdistCode = $("#distCode").val();
                    toffCode = null;
                }
                $.post("assignPrivilegeForGroupC.htm", {spc: tSpc, hidAuthOffCode: toffCode, distCode: tdistCode}, "json")
                        .done(function(data) {
                            if (data.msg == "Y") {
                                getassignPrivilegeForGroupCList();
                                alert("Saved Sucessfully");
                            } else if (data.msg == "N") {
                                alert("Error Occured");
                            } else if (data.msg == "D") {
                                alert("Privilage is already Assigned");
                            }
                        })
            }
            function getassignPrivilegeForGroupCList() {
                tSpc = $("#employee").val();
                var url = 'getassignPrivilegeForGroupCDetail.htm?spc=' + tSpc;
                $.getJSON(url, function(result) {
                    $('#privilageAssignedList').empty();
                    $.each(result, function(i, field) {
                        var trhtml = '<tr>';
                        trhtml = trhtml + '<td>' + (i + 1) + '</td>';
                        trhtml = trhtml + '<td>' + field.priviligeAuhName + '</td>';
                        trhtml = trhtml + '<td>' + field.priviligeAuhDesignation + '</td>';
                        trhtml = trhtml + '<td align="center"><a href="#" onclick="removeGrouoCPrivilage(\'' + field.spc + '\')"><span class="glyphicon glyphicon-remove"></span>' + '</a></td>';
                        trhtml = trhtml + '</tr>';
                        $('#privilageAssignedList').append(trhtml);
                    });
                });
            }
            function removeGrouoCPrivilage(vSpc) {
                $.post("removeGroupCPrivilage.htm", {spc: vSpc}, "json")
                        .done(function(data) {
                            getassignPrivilegeForGroupCList();
                            alert("Delete Sucessfully");
                        })
            }

            function openSetPrivilegedWindow() {
                $('#setPrivileged').modal('show');
            }


            function assignPrivilegeForGroupCOfficewise() {
                $("#officewiseDiv").show();
                $("#districtwiseDiv").hide();
                togglePrivilageAssignment = "O";
            }
            function assignPrivilegeForGroupCdistrictwise() {
                $("#districtwiseDiv").show();
                $("#officewiseDiv").hide();
                togglePrivilageAssignment = "D";
            }


        </script>
        <style type="text/css">
            .table > tbody > tr > td{
                font-size: 12px;
            }
        </style>
    </head>



    <body style="margin-top:0px;background:#188B7A;">

        <jsp:include page="../tab/ParMenu.jsp"/>  
        <div id="wrapper"> 
            <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                <div class="row">
                    <div class="col-lg-12">                            
                        <ol class="breadcrumb">
                            <li>
                                <i class="fa fa-dashboard"></i>  <a href="index.html">Group C Privilege</a>
                            </li>

                        </ol>
                    </div>
                </div>


                <form:form action="groupCPrivilizationList.htm" commandName="groupCEmployee" method="post">
                    <div class="panel-body">
                        <button type="button" class="btn btn-primary" onclick="openSetPrivilegedWindow()">Set Privileged</button>
                        <input type="submit" name="action" value="Get Officewise Privilege List" class="btn btn-primary"/>
                        <input type="submit" name="action" value="Get Districtwise Privilege List" class="btn btn-primary"/>
                    </div>

                    <div class="panel panel-default">
                        <div class="panel-body">  
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Name</th>
                                        <th>Designation</th>
                                        <th>District Name</th>
                                        <th>Office Name</th>
                                        <th>Remove</th>
                                    </tr>
                                </thead>
                                <tbody>                                        
                                    <c:forEach items="${distOrOffwiseprivilegedList}" var="distOrOffwiseprivileged" varStatus="cnt">
                                        <tr>
                                            <td>${cnt.index + 1}</td>
                                            <td>${distOrOffwiseprivileged.priviligeAuhName}</td>
                                            <td>${distOrOffwiseprivileged.priviligeAuhDesignation}</td>
                                            <td>${distOrOffwiseprivileged.distName}</td>
                                            <td>${distOrOffwiseprivileged.offName}</td>
                                            <td><a href="removeGroupCPrivilagedistOrOffwise.htm?spc=${distOrOffwiseprivileged.spc}"><span class="glyphicon glyphicon-remove"></span></a></td>
                                        </tr>
                                    </c:forEach>      
                                </tbody>
                            </table>
                        </div>

                    </div>
                </form:form>

            </div>


            <div id="setPrivileged" class="modal fade" >
                <div class="modal-dialog modal-lg">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Set Privilige For GroupC</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <label class="control-label col-sm-3">Department Name </label>
                                    <div class="col-sm-9">
                                        <select class="form-control" name="deptName" id="deptName">
                                            <option value="">Select</option>
                                            <c:forEach items="${departmentList}" var="department">
                                                <option value="${department.deptCode}">${department.deptName}</option>
                                            </c:forEach>                                        
                                        </select>
                                    </div>
                                </div>                               

                                <div class="form-group">
                                    <label class="control-label col-sm-3">Office Name</label>
                                    <div class="col-sm-9">

                                        <select class="form-control" name="offcode" id="offcode">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                </div>


                                <div class="form-group">
                                    <label class="control-label col-sm-3">Post Name</label>
                                    <div class="col-sm-9">
                                        <select class="form-control" name="postCode" id="postCode">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-3">Employee Name</label>
                                    <div class="col-sm-9">
                                        <select class="form-control" name="employee" id="employee">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" id="officewisesearch" onclick="assignPrivilegeForGroupCOfficewise()">Assign Privilege Office Wise</button>
                            <button type="button" class="btn btn-primary" id="officewisesearch" onclick="assignPrivilegeForGroupCdistrictwise()">Assign Privilege District Wise</button>
                            <button type="button" class="btn btn-danger" data-dismiss="modal">cancel</button>
                        </div>
                        <div class="panel-body" id="officewiseDiv" >
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <option value="">--Select Department--</option>
                                        <c:forEach items="${departmentList}" var="dept">
                                            <option value="${dept.deptCode}">${dept.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="hidAuthOffCode" id="hidAuthOffCode" class="form-control">
                                        <option value="">--Select Office--</option>
                                        <c:forEach items="${sancOfflist}" var="toffice">
                                            <option value="${toffice.offCode}">${toffice.offName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="form-group">
                                <button type="button" class="btn btn-primary" onclick="assignPrivilegeForGroupC()">Assign Privilege</button>
                            </div>
                        </div>
                        <div class="panel-body" id="districtwiseDiv">
                            <div class="form-group">
                                <label class="control-label col-sm-3">District Name </label>
                                <div class="col-sm-9">
                                    <select class="form-control" name="distCode" id="distCode">
                                        <option value="">Select</option>
                                        <c:forEach items="${distlist}" var="dist">
                                            <option value="${dist.distCode}">${dist.distName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                            </div>                               

                            <div class="form-group">
                                <button type="button" class="btn btn-primary" onclick="assignPrivilegeForGroupC()">Assign Privilege</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-9">Below Employees are Assigned to this Privilege</label>
                        </div>
                        <div style="padding: 5px;">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Name</th>
                                        <th>Designation</th>
                                        <th>Remove</th>
                                    </tr>
                                </thead>
                                <tbody id="privilageAssignedList">                                    

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>


