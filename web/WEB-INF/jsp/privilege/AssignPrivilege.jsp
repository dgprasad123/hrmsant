<%-- 
    Document   : ModuleList
    Created on : Nov 21, 2016, 6:08:30 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#deptCode').chosen();

            });

            function getOfficeList() {
                var loginid =${loginNm};
                var distCode =${distcode};
                var url = '';

                if (loginid == '2166') {
                    url = 'getOfficeListJSON.htm?deptcode=' + $("#deptCode").val();
                } else {
                    url = 'getDistwiseOfficeListJSON.htm?deptcode=' + $("#deptCode").val() + '&distCode=' + distCode;
                }
                //var url = 'getOfficeListJSON.htm?deptcode=' + $("#deptCode").val();
                // var url = 'getDistwiseOfficeListJSON.htm?deptcode=' + $("#deptCode").val() + '&distCode=' + distCode;
                $.getJSON(url, function(data) {
                    $('#offCode').empty();
                    $('#postcode').empty();
                    $('#offCode').append("<option value=\"\">--Select--</option>");
                    $.each(data, function(i, obj) {
                        $('#offCode').append($('<option>').text(obj.offName).attr('value', obj.offCode));
                    });
                }).done(function() {
                    $('#offCode').chosen();
                    $("#offCode").trigger("chosen:updated");
                });
            }
            function getPostList() {
                var url = 'getEmployeeWithSPCList.htm?offcode=' + $('#offCode').val();
                $.getJSON(url, function(data) {
                    $('#postcode').empty();
                    $('#postcode').append("<option value=\"\">--Select--</option>");
                    $.each(data, function(i, obj) {
                        var empid = obj.empid;

                        $('#postcode').append($('<option>').text(obj.postname + ", " + obj.empname).attr('value', obj.spc + "-" + empid));
                    });
                }).done(function() {
                    $('#postcode').chosen();
                    $("#postcode").trigger("chosen:updated");
                });
            }
            function ShowAssignedPrivilege() {
                if ($('#offCode').val() == "") {
                    alert("Choose Office");
                    return false;
                }
                if ($('#postcode').val() == "") {
                    alert("Choose Post");
                    return false;
                }
                $('#offWisePrivilegeList').html("");
                url = 'getAllAssignedPrivileges.htm?offCode=' + $('#offCode').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        var tempspc = "'" + obj.privspc + "'";
                        $('#offWisePrivilegeList').append('<tr><td>' + (i + 1) + '</td><td>' + obj.privspn + ' > ' + obj.empname + ' > ' + obj.gpfno + '</td><td><a href="javascript:revokeUser(' + tempspc + ')" class="btn  btn-default"><span class="glyphicon glyphicons-remove"></span>Revoke</a></td></tr>');
                    });
                });

                $('#privilegeList').html("");
                var url = 'getAssignedPrivilege.htm?spc=' + $('#postcode').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#privilegeList').append('<tr><td>' + (i + 1) + '</td><td>' + obj.rolename + ' > ' + obj.modulegroup + ' > ' + obj.modulename + '</td><td><a href="javascript:revoke(' + obj.privmapid + ')" class="btn  btn-default"><span class="glyphicon glyphicons-remove"></span>Revoke</a></td></tr>');
                    });
                });
            }
            function showNewPrivilegeWindow() {
                if ($('#offCode').val() == null) {
                    alert("Choose Office");
                    return false;
                }
                if ($('#postcode').val() == null) {
                    alert("Choose Post");
                    return false;
                }
                $.getJSON('getRoleList.htm', function(data) {
                    $('#role').empty();
                    $('#modulegroup').empty();
                    $('#module').empty();
                    $.each(data, function(i, obj) {
                        $('#role').append($('<option>').text(obj.label).attr('value', obj.value));
                    });
                });
                $("#myModal").modal('show');
            }
            function showModuleGroup() {
                var url = 'getModuleGroupList.htm?role=' + $('#role').val();
                $.getJSON(url, function(data) {
                    $('#modulegroup').empty();
                    $('#module').empty();
                    $.each(data, function(i, obj) {
                        $('#modulegroup').append($('<option>').text(obj.modGrpName).attr('value', obj.modId));
                    });
                });
            }
            function showModule() {
                var url = 'getModuleList.htm?modulegroup=' + $('#modulegroup').val();
                $.getJSON(url, function(data) {
                    $('#module').empty();
                    $.each(data, function(i, obj) {
                        $('#module').append($('<option>').text(obj.modname).attr('value', obj.modid));
                    });
                });
            }
            function revoke(privmapid) {
                var spc = $('#postcode').val();
                var url = 'revokePrivilage.htm?spc=' + spc + '&privmapid=' + privmapid;
                $.ajax({url: url, success: function(result) {
                        if (result == "Y") {
                            alert("Revoked");
                            ShowAssignedPrivilege();
                        } else {
                            alert("Some Error Occured");
                        }
                    }});
            }
            function revokeUser(privspc) {
                var url = 'revokeUserPrivilege.htm?privspc=' + privspc;
                if (confirm("Are you want to Revoke?")) {
                    $.ajax({
                        url: url,
                        method: "POST",
                        success: function(result) {
                            ShowAssignedPrivilege();
                        }
                    });
                }
            }
            function assignPrivilege() {
                var modgrpid = $('#modulegroup').val();
                var modid = $('#module').val();
                var empid = "";

                var spc = $('#postcode').val();
                var roleid = $('#role').val();

                if ($('#modulegroup').val() == null) {
                    modgrpid = 0;
                }
                if ($('#module').val() == null) {
                    modid = 0;
                }

                if (spc.indexOf("-") > -1) {
                    var splitSpc = spc.split("-");
                    spc = splitSpc[0];
                    empid = splitSpc[1];
                }
                //alert(${spc});

                var url = 'assignPrivilege.htm?spc=' + spc + '&roleid=' + roleid + '&modgrpid=' + modgrpid + '&modid=' + modid + '&hrmsid=' + empid;
                $.ajax({url: url, success: function(data) {
                        if (data == "Y") {
                            ShowAssignedPrivilege();
                            $("#msg").addClass("alert-success");
                            $("#msg").text("Privilege Assigned");
                        } else if (data == "E") {
                            $("#msg").addClass("alert-danger");
                            $("#msg").text("Already Assigned");
                        } else if (data == "N") {
                            $("#msg").addClass("alert-danger");
                            $("#msg").text("There is some error occured");
                        }
                    }});
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>  
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Privilege List 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Privilege Detail</h2>

                            <div class="form-group">
                                <label>Department</label>                                    
                                <select class="form-control" name="deptCode" id="deptCode" onchange="getOfficeList()">
                                    <option value="">--Select--</option>
                                    <c:forEach items="${deptList}" var="dept">
                                        <option value="${dept.deptCode}">${dept.deptName}</option>
                                    </c:forEach>                                        
                                </select>                                   
                            </div>

                            <div class="form-group">
                                <label>Office</label>
                                <select class="form-control"  name="offCode" id="offCode" onchange="getPostList()">
                                    <option value="">--Select--</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Post</label>
                                <select class="form-control" name="postcode" id="postcode">
                                    <option value="">--Select--</option>
                                </select>
                            </div>
                            <button type="button" class="btn btn-default" onclick="ShowAssignedPrivilege()">Show Assigned Privilege</button>
                            <button type="button" class="btn btn-default" onclick="showNewPrivilegeWindow()">Assign New Privilege</button>
                        </div>
                    </div>
                    <div class="row" style="margin-top: 10px;">
                        <div class="table-responsive">
                            <h4>OFFICE WISE PRIVILEGE LIST</h4>
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Sl No</th>
                                        <th>Privilege Assigned To Post</th>                                                                                 
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody id="offWisePrivilegeList">

                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="row" style="margin-top: 10px;">
                        <div class="table-responsive">
                            <h4>EMPLOYEE WISE MODULE LIST</h4>
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Sl No</th>
                                        <th>Module Name</th>                                        
                                        <th>Action</th>                                            
                                    </tr>
                                </thead>
                                <tbody id="privilegeList">

                                </tbody>
                            </table>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">New Privilege</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label>Role</label>
                            <select class="form-control"  name="role" id="role" onchange="showModuleGroup()">                             
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Module Group</label>
                            <select class="form-control"  name="modulegroup" id="modulegroup" onchange="showModule()" >                             
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Module</label>
                            <select class="form-control"  name="module" id="module" >                             
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <span id="msg"></span>
                        <button type="button" class="btn btn-default" onclick="assignPrivilege()">Save</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
