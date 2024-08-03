<%-- 
    Document   : PARCaderList
    Created on : Jan 13, 2020, 4:26:44 PM
    Author     : manisha
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
            .myModalBody{}
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

                });
                $("#offcode").change(function() {
                    var url = 'getPostCodeListJSON.htm?offcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $('#postCode').empty();
                        $.each(result, function(i, field) {
                            $('#postCode').append($('<option>', {
                                value: field.value,
                                text: field.label
                            }));
                        });
                    });
                });

                //getEmployeeNameWithSPCJSON
                $("#postCode").change(function() {
                    alert($("#postCode"));
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

            function openSetPrivilegedWindow() {
                $("#showDepartmentdetail").show();
                var radioValue = $("input[name='rdCadre']:checked").val();
                var radioValue1 = $("#deptCode").val();
                if (!radioValue1) {
                    alert("Please Select the Department");
                } else if (radioValue) {
                    getAssignedPrivilegedList();
                    $('#setPrivileged').modal('show');
                } else {
                    alert("Please Select the Cadre");
                }
            }
            function openSetPrivilegedOfficewiseWindow() {
                $('#setPrivilegedOffcwise').modal('show');


            }

            function getAssignedPrivilegedList() {
                var tCadrecode = $("input[name='rdCadre']:checked").val();
                var url = 'geteAssignPrivilegeDetail.htm?cadrecode=' + tCadrecode;
                $.getJSON(url, function(result) {
                    $('#privilageAssignedList').empty();
                    $.each(result, function(i, field) {
                        var trhtml = '<tr>';
                        trhtml = trhtml + '<td>' + (i + 1) + '</td>';
                        trhtml = trhtml + '<td>' + field.empName + '</td>';
                        trhtml = trhtml + '<td>' + field.empDesg + '</td>';
                        trhtml = trhtml + '<td>' + field.postGroup + '</td>';
                        trhtml = trhtml + '<td align="center"><a href="#" onclick="removeCadrePrivilage(\'' + field.spc + '\',\'' + field.postGroup + '\',\'' + tCadrecode + '\')"><span class="glyphicon glyphicon-remove"></span>' + '</a></td>';
                        trhtml = trhtml + '</tr>';
                        $('#privilageAssignedList').append(trhtml);
                    });
                });
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

            function assignCadrePrivilage() {
                tSpc = $("#employee").val();
                tCadrecode = $("input[name='rdCadre']:checked").val();
                tEmpPostGroup = $("input[name='emppostGroup']:checked").val();
                tCadredeptCode = $("#deptCode").val();
                tEmployeedeptCode = $("#deptName").val();
                alert(tEmployeedeptCode);
                //if((tCadredeptCode == tEmployeedeptCode)) {
                if ((tCadredeptCode == tEmployeedeptCode && tEmployeedeptCode != 11) || tEmployeedeptCode == 11) {
                    if (tSpc && tCadrecode && tEmpPostGroup) {
                        $.post("assignCadrePrivilage.htm", {spc: tSpc, cadrecode: tCadrecode, postGroup: tEmpPostGroup}, "json")
                                .done(function(data) {
                                    if (data.msg == "Y") {
                                        getAssignedPrivilegedList();
                                        alert("Saved Sucessfully");
                                    } else if (data.msg == "N") {
                                        alert("Error Occured");
                                    } else if (data.msg == "D") {
                                        alert("Privilage is already Assigned");
                                    }
                                })
                    } else if (!tEmpPostGroup) {
                        alert("Choose Group");
                    }
                } else {
                    alert("Please Choose the Employee Of Same Department As Cadre");
                }
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
            function removeCadrePrivilage(vSpc, vPostGroup, vCadrecode) {
                $.post("removeCadrePrivilage.htm", {spc: vSpc, cadrecode: vCadrecode, postGroup: vPostGroup}, "json")
                        .done(function(data) {
                            getAssignedPrivilegedList();
                            alert("Delete Sucessfully");
                        })
            }
            function removeOfficeWisePrivilage(vSpc, vPostGroup, vOffcode) {
                $.post("removeOfficeWisePrivilage.htm", {spc: vSpc, Offcode: vOffcode, postGroup: vPostGroup}, "json")
                        .done(function(data) {
                            getOfficewiseprivilageAssignedList();
                            alert("Delete Sucessfully");
                        })
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
                                <i class="fa fa-dashboard"></i>  <a href="index.html">Admin Cadre List</a>
                            </li>

                        </ol>
                    </div>
                </div>
                <div class="form-group">
                    <button type="button" class="btn btn-primary" onclick="openSetPrivilegedWindow()">Set Privileged Cadre Wise</button>
                    <button type="button" class="btn btn-primary" onclick="openSetPrivilegedOfficewiseWindow()">Set Privileged Office wise</button> 
                </div>
                <div class="row" id="showDepartmentdetail">
                    <form:form action="parCaderList.htm" commandName="cadre" method="post">
                        <div class="col-lg-2">Department Name</div>
                        <div class="col-lg-8">
                            <form:select path="deptCode" class="form-control">
                                <form:option value="">Select</form:option>
                                <form:options items="${departmentList}" itemValue="deptCode" itemLabel="deptName"/>                                
                            </form:select>                            
                        </div>
                        <div class="col-lg-2"><button type="submit" class="form-control">Submit</button> </div>
                    </form:form>
                </div>

                <div class="row" id="showCadreDetail">
                    <div class="col-lg-12">
                        <h2>Cadre List</h2>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="1%"></th> 
                                        <th>Sl No</th>
                                        <th>Cadre Name</th>
                                    </tr>
                                </thead>
                                <tbody>                                        
                                    <c:forEach items="${cadrelist}" var="cadre" varStatus="count">
                                        <tr>
                                            <td><input type="radio" name="rdCadre" value="${cadre.value}" /> </td>
                                            <td>${count.index + 1}</td>
                                            <td>${cadre.label}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>


            <div id="setPrivileged" class="modal fade" >
                <div class="modal-dialog modal-lg">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Set Cadre List</h4>
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
                            <button type="button" class="btn btn-primary" onclick="assignCadrePrivilage()">Assign Cadre</button>                            
                            <button type="button" class="btn btn-danger" data-dismiss="modal">cancel</button>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-9">Below Employees are Assigned to this Cadre</label>
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
                                <tbody id="privilageAssignedList">                                    

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <div id="setPrivilegedOffcwise" class="modal fade" >
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

                                        <select class="form-control" name="offcodeforofficewise" id="offcodeforofficewise">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-3">Post Name</label>
                                    <div class="col-sm-9">
                                        <select class="form-control" name="postCodeforofficewise" id="postCodeforofficewise">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-3">Employee Name</label>
                                    <div class="col-sm-9">
                                        <select class="form-control" name="employeeforofficewise" id="employeeforofficewise">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-3">Authorization type</label>
                                    <label class="radio-inline">
                                        <input type="radio" value="S" name="authorizationType">S
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" value="all" name="authorizationType">All
                                    </label>
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
    </body>
</html>


