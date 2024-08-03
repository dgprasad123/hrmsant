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
        <link href="css/select2.min.css" rel="stylesheet" type="text/css">
        
        <script src="js/jquery.min.js"></script>
        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/select2.min.js"></script>
        
        
        <script type="text/javascript">
            $(document).ready(function() {
                $(this).bind("contextmenu", function (e) {
                    e.preventDefault();
                });
                           
                // Initialize select2 for searchable combo box
                    $("#username").select2();
                
            });
            
            function ShowAssignedPrivilege() {
                if ($('#username').val() == "") {
                    alert("Choose User Name");
                    return false;
                }

                $('#privilegeList').empty();
                var url = 'getAssignedPrivilegeUserNameSpecific.htm?username=' + $('#username').val();
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#privilegeList').append('<tr><td>' + i + '</td><td>' + ' > ' + ' > ' + obj.modulename + '</td><td><a href="javascript:revoke(' + obj.privmapid + ')" class="btn  btn-default"><span class="glyphicon glyphicons-remove"></span>Revoke</a></td></tr>');
                    });
                });
            }
            function showNewPrivilegeWindow() {

                if ($('#username').val() == "") {
                    alert("Choose User Name");
                    return false;
                }
                $.getJSON('getModuleListUserNameSpecific.htm', function (data) {
                    $('#module').empty();
                    $.each(data, function (i, obj) {
                        $('#module').append($('<option>').text(obj.label).attr('value', obj.value));
                    });
                });
                $("#myModal").modal('show');
            }

            function revoke(privmapid) {
                var username = $('#username').val();
                var url = 'revokePrivilageUserNameSpecific.htm?username=' + username + '&privmapid=' + privmapid;
                $.ajax({url: url, success: function (result) {
                        if (result == "Y") {
                            alert("Revoked");
                            ShowAssignedPrivilege();
                        } else {
                            alert("Some Error Occured");
                        }
                    }});
            }
            function assignPrivilege() {
                if ($('#module').val() == null) {
                    alert("Choose Module");
                    return false;
                }
                var username = $('#username').val();
                var modid = $('#module').val();
                var url = 'assignPrivilegeUserNameSpecific.htm?username=' + username + '&modid=' + modid;
                $.ajax({url: url, success: function (data) {
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
            function getUserName() {
                if ($('#usertype').val() != "") {
                    var url = 'getUserNameUsertypeSpecific.htm?usertype=' + $('#usertype').val();
                    $.ajax({url: url, success: function (data) {
                            $('#username').empty();
                            $.each(data, function (i, obj) {
                                $('#username').append($("<option></option>")
                                                .attr("value", obj)
                                                .text(obj));
                            });
                        }});
                }
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

                        <h2>Privilege Detail User Name Specific</h2>

                        <div class="form-group">
                            <label class="control-label col-sm-1">User Type</label>
                            <div class="col-sm-2">
                                <select class="form-control" name="usertype" id="usertype" onchange="getUserName()">
                                    <option value="">Select</option>
                                    <c:forEach items="${usertypelist}" var="usertype">
                                        <option value="${usertype}">${usertype}</option>
                                    </c:forEach>                                        
                                </select>   
                            </div>
                            <label class="control-label col-sm-1">User Name</label>
                            <div class="col-sm-4">
                                <select class="form-control" name="username" id="username">
                                    <c:forEach items="${userList}" var="user">
                                        <option value="${user}">${user}</option>
                                    </c:forEach>                                        
                                </select>   
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group" style="margin-top: 10px;">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" class="btn btn-default" onclick="ShowAssignedPrivilege()">Show Assigned Privilege</button>
                                <button type="button" class="btn btn-default" onclick="showNewPrivilegeWindow()">Assign New Privilege</button>
                            </div>
                        </div>
                    </div>

                    <div class="row" style="margin-top: 10px;">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Sl</th>
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
