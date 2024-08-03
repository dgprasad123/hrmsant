<%-- 
    Document   : PARDistrictwisePrivilegeForPolice
    Created on : 9 Jun, 2022, 12:41:37 PM
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
        <link href="css/sb-admin.css" rel="stylesheet" type="text/css">
        <link href="css/select2.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">

        <!-- 
        <script src="js/par/jquery.min.js"></script>
        <script src="js/par/bootstrap.min.js"></script>
         -->
        <script src="js/jquery.min.js"></script> 
        <script src="js/jquery2.0.3.min.js"></script>
        <script src="js/jquery-ui.min.js"></script> 
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jquery.dataTables.js"></script>
        <script src="js/select2.min.js"></script>

        <style>
            .tblTrColor{
                background: rgb(174,238,209);
                background: radial-gradient(circle, rgba(174,238,209,0.9976191160057774) 0%, rgba(148,231,233,1) 100%);
                color: #000000;
                font-weight: bold;
            }
        </style>
        <script type="text/javascript">

            function validation() {
                if ($("#deptName").val() == "") {
                    alert("please Enter the department Name");
                    $("#deptName").focus();
                    return false;
                }
            }
            $(document).ready(function() {
                $('#datatable').DataTable();
                $("#offcodeforofficewise").select2();
                $("#postCodeforofficewise").select2();
                $("#employeeforofficewise").select2();
                
                $("#showDepartmentdetail").hide();

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

                $("#deptNameforoffwise").change(function() {
                    $('#offcodeforofficewise').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#offcodeforofficewise').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                    var url = 'getDeptWisePostListJSON.htm?deptCode=' + this.value;
                    $.getJSON(url, function(result) {
                        $('#postCodeforofficewise').empty();
                        $.each(result, function(i, field) {
                            $('#postCodeforofficewise').append($('<option>', {
                                value: field.postcode,
                                text: field.post
                            }));
                        });
                    });
                });

                //getEmployeeNameWithSPCJSON
                $("#postCodeforofficewise").change(function() {
                    var url = 'getEmployeeNameWithSPCJSON.htm?offcode=' + $('#offcodeforofficewise').val() + '&postCode=' + this.value;
                    $.getJSON(url, function(result) {
                        $('#employeeforofficewise').empty();
                        $.each(result, function(i, field) {
                            $('#employeeforofficewise').append($('<option>', {
                                value: field.spc,
                                text: field.empname + '(' + field.empid + ',' + field.gpfNo + ')'

                            }));
                        });
                    });
                });

            });


            function openSetPrivilegedOfficewiseWindow() {
                $('#setPrivilegedOffcwise').modal('show');
            }


            function getOfficewiseprivilageAssignedList() {
                tOffcode = $("#offcodeforofficewise").val();
                var url = 'getOfficeWiseAssignPrivilegeDetail.htm?Offcode=' + tOffcode;
                $.getJSON(url, function(result) {
                    $('#officewiseprivilegedList').empty();
                    $.each(result, function(i, field) {
                        var trhtml = '<tr>';
                        trhtml = trhtml + '<td>' + (i + 1) + '</td>';
                        trhtml = trhtml + '<td>' + field.empName + '</td>';
                        trhtml = trhtml + '<td>' + field.empDesg + '</td>';
                        trhtml = trhtml + '<td>' + field.postGroup + '</td>';
                        trhtml = trhtml + '<td align="center"><a href="#" onclick="removeOfficeWisePrivilage(\'' + field.spc + '\',\'' + field.postGroup + '\',\'' + tOffcode + '\')"><span class="glyphicon glyphicon-remove"></span>' + '</a></td>';
                        trhtml = trhtml + '</tr>';
                        $('#officewiseprivilegedList').append(trhtml);
                    });
                });
            }

            function assignOfficewisePrivilage() {
                tSpc = $("#employeeforofficewise").val();
                tOffcode = $("#offcodeforofficewise").val();
                tAuthorizationType = $("input[name='authorizationType']:checked").val();
                tEmpPostGroup = $("input[name='emppostGroup']:checked").val();
                if (tSpc && tOffcode && tAuthorizationType) {
                    $.post("assignOfficewisePrivilage.htm", {spc: tSpc, offcodeforofficewise: tOffcode, authorizationType: tAuthorizationType, postGroup: tEmpPostGroup}, "json")
                            .done(function(data) {
                                if (data.msg == "Y") {
                                    getOfficewiseprivilageAssignedList();
                                    alert("Saved Sucessfully");
                                } else if (data.msg == "N") {
                                    alert("Error Occured");
                                } else if (data.msg == "D") {
                                    alert("Privilage is already Assigned");
                                }
                            })

                } else if (!tOffcode) {
                    alert("Choose Department Name and Office Name");
                } else if (!tAuthorizationType) {
                    alert("Choose Authorization Type");
                } else if (!tEmpPostGroup) {
                    alert("Choose Post Group Type");
                }
            }

            function removeOfficeWisePrivilage(vSpc, vPostGroup, vOffcode) {
                $.post("removeOfficeWisePrivilage.htm", {spc: vSpc, Offcode: vOffcode, postGroup: vPostGroup}, "json")
                        .done(function(data) {
                            getOfficewiseprivilageAssignedList();
                            alert("Delete Sucessfully");
                        })
            }
        </script>
    </head>
    <div id="wrapper">
        <jsp:include page="../tab/hrmsadminmenu.jsp"/>
        <form:form action="parDistrictWisePrivilegeForPolice.htm" commandName="cadre">
            <div id="page-wrapper">
                <div class="panel panel-default">
                    <h3 style="text-align:center;color:#0000FE"> <b> District Wise Privilege Officers List for PAR </b> </h3>
                    <div class="form-group">
                        <button type="button" class="btn btn-primary" onclick="openSetPrivilegedOfficewiseWindow()">Set Privileged Office wise</button>
                        <%--<input type="submit" name="action" value="Get Assigned PrivilegeList" class="btn btn-primary"> --%>
                    </div>
                    <div class="panel panel-default">
                        <div class="panel-body">  
                            <table id="datatable" class="table table-bordered table-hover table-striped">
                                <thead class="tblTrColor">
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
                                    <c:forEach items="${distWisePrivilegedList}" var="districtwiseprivile" varStatus="cnt">
                                        <tr>
                                            <td>${cnt.index + 1}</td>
                                            <td>${districtwiseprivile.priviligeAuhName}</td>
                                            <td>${districtwiseprivile.priviligeAuhDesignation}</td>
                                            <td>${districtwiseprivile.officeName}</td>
                                            <td>${districtwiseprivile.postGrp}</td>
                                            <td><a href="removeDistrictWisePrivilegeForPolice.htm?spc=${districtwiseprivile.spc}&postGrp=${districtwiseprivile.postGrp}"><span class="glyphicon glyphicon-remove"></span></a></td>
                                        </tr>
                                    </c:forEach>      
                                </tbody>
                            </table>
                        </div>

                    </div>

                    <div id="setPrivilegedOffcwise" class="modal fade">
                        <div class="modal-dialog modal-lg">
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Office wise Privilege</h4>
                                </div>
                                <div class="modal-body">
                                    <form class="form-horizontal">
                                        <div class="form-group">
                                            <label class="control-label col-sm-3">Department Name </label>
                                            <div class="col-sm-9">
                                                <select class="form-control" name="deptNameforoffwise" id="deptNameforoffwise">
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

                                                <select class="form-control" name="offcodeforofficewise" id="offcodeforofficewise" style="width: 100%">
                                                    <option value="">Select</option>                                            
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-sm-3">Post Name</label>
                                            <div class="col-sm-9">
                                                <select class="form-control" name="postCodeforofficewise" id="postCodeforofficewise" style="width: 100%">
                                                    <option value="">Select</option>                                            
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-sm-3">Employee Name</label>
                                            <div class="col-sm-9">
                                                <select class="form-control" name="employeeforofficewise" id="employeeforofficewise" style="width: 100%">
                                                    <option value="">Select</option>                                            
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-sm-3">Authorization type</label>
                                            <label class="radio-inline">
                                                <input type="radio" value="S" name="authorizationType">S
                                            </label>
                                            <%--<label class="radio-inline">
                                                <input type="radio" value="all" name="authorizationType">All
                                            </label> --%>
                                            <span style="font-style: italic;color: #008000;">(Authorization Type 'S' is not allowed to view the Details of PAR)</span>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-sm-3">Select Post Group type</label>
                                            <label class="radio-inline">
                                                <input type="radio" value="A" name="emppostGroup">A
                                            </label>
                                            <label class="radio-inline">
                                                <input type="radio" value="B" name="emppostGroup">B
                                            </label>
                                        </div>
                                    </form>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-primary" onclick="assignOfficewisePrivilage()">Assign Privilege</button>                            
                                    <button type="button" class="btn btn-danger" data-dismiss="modal">cancel</button>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-9">Below Employees are Assigned to this Office</label>
                                </div>
                                <div style="padding: 5px;">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Name</th>
                                                <th>Designation</th>
                                                <th>Group</th>
                                                <th>Remove</th>
                                            </tr>
                                        </thead>
                                        <tbody id="officewiseprivilegedList">                                    

                                        </tbody>
                                    </table>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</body>
</html>


