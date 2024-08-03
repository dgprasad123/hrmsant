<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
                if ($('#offCode').val() != "") {
                    getPostList();
                }
                if ($('#hidPostCode').val() != "") {
                    ShowAssignedPrivilege();
                }
            });

            function getOfficeList() {
                var distCode =${distcode};
                //var url = 'getOfficeListJSON.htm?deptcode=' + $("#deptCode").val();
                var url = 'getDistwiseOfficeListJSON.htm?deptcode=' + $("#deptCode").val() + '&distCode=' + distCode;
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
                        $('#postcode').append($('<option>').text(obj.postname + ", " + obj.empname).attr('value', obj.spc+"-"+empid));
                    });
                }).done(function() {
                    $('#postcode').val($('#hidPostCode').val());
                    $('#postcode').chosen();
                    $("#postcode").trigger("chosen:updated");
                });
            }
            function ShowAssignedPrivilege() {
                $('#offWisePrivilegeList').html("");
                url = 'getAllAssignedPrivileges.htm?offCode=' + $('#offCode').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        var tempspc = "'" + obj.privspc + "'";
                        $('#offWisePrivilegeList').append('<tr><td>' + (i + 1) + '</td><td>' + obj.privspn + ' > ' + obj.empname + ' > ' + obj.gpfno + '</td><td><a href="javascript:revokeUser(' + tempspc + ')" class="btn  btn-default"><span class="glyphicon glyphicons-remove"></span>Revoke</a></td></tr>');
                    });
                });
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
            function assignGroupPrivilege() {
                var checkBoxArray = "";
                var spc = $('#postcode').val();
                var empid = "";

                if (spc.indexOf("-") > -1) {
                    var splitSpc = spc.split("-");
                    spc = splitSpc[0];
                    empid = splitSpc[1];
                }
                //alert("SPC is:" + spc);
                $("input[name='chkAbstractModuleId']:checked").each(function() {
                    if (checkBoxArray == "") {
                        checkBoxArray = $(this).val();
                    } else {
                        checkBoxArray = checkBoxArray + "," + $(this).val();
                    }
                });
                //alert(checkBoxArray);
                if (checkBoxArray == "") {
                    alert("Please Select Module Name");
                } else {
                    var url = 'assignGroupWisePrivilege.htm?checkedAbstractModule=' + checkBoxArray + '&spc=' + spc + '&hrmsid=' + empid;
                    ;
                    $.ajax({
                        url: url,
                        success: function(data) {
                            if (data == "Y") {
                                $("#msg").addClass("alert-success");
                                $("#msg").text("Privilege Assigned.");
                            } else if (data == "E") {
                                $("#msg").addClass("alert-danger");
                                $("#msg").text("Already Assigned.");
                            } else if (data == "N") {
                                $("#msg").addClass("alert-danger");
                                $("#msg").text("There is some error.");
                            }
                        }
                    });
                }
            }

            function revokeGroupPrivilege() {
                var checkBoxArray = "";
                var spc = $('#postcode').val();
                //alert("SPC is:" + spc);
                $("input[name='chkAbstractModuleId']:checked").each(function() {
                    if (checkBoxArray == "") {
                        checkBoxArray = $(this).val();
                    } else {
                        checkBoxArray = checkBoxArray + "," + $(this).val();
                    }
                });
                //alert(checkBoxArray);
                if (checkBoxArray == "") {
                    alert("Please Select Module Name");
                } else {
                    var url = 'revokeGroupWisePrivilege.htm?checkedAbstractModule=' + checkBoxArray + '&spc=' + spc;
                    $.ajax({
                        url: url,
                        success: function(data) {
                            if (data == "R") {
                                $("#msg").addClass("alert-danger");
                                $("#msg").text("Privilege Revoked");
                            } else {
                                alert("Some Error Occured");
                            }
                        }
                    });
                }
            }

            function submitValidation() {
                if ($('#offCode').val() == "") {
                    alert("Choose Office");
                    return false;
                }
                if ($('#postcode').val() == "") {
                    alert("Choose Post");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <input type="hidden" id="hidPostCode" value="${hidPostCode}"/>
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
                    <form:form action="displayAssignPrivilegeGroupWise.htm" method="POST" commandName="command">
                        <input type="hidden" name="distcode" id="distcode" value="${distcode}"/>
                        <div class="row">
                            <div class="col-lg-12">
                                <h2>Privilege Detail</h2>

                                <div class="form-group">
                                    <label>Department</label>                                    
                                    <form:select class="form-control" path="deptCode" id="deptCode" onchange="getOfficeList()">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptList}" itemValue="deptCode" itemLabel="deptName"/>                                        
                                    </form:select>                                   
                                </div>

                                <div class="form-group">
                                    <label>Office</label>
                                    <form:select class="form-control" path="offCode" id="offCode" onchange="getPostList()">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="form-group">
                                    <label>Post</label>
                                    <form:select class="form-control" path="postcode" id="postcode">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${postlist}" itemValue="spc" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <input type="submit" name="btnGroupWisePrivilegeBtn" value="Show Group Wise Assigned Privilege" class="btn btn-default" onclick="return submitValidation();"/>
                            </div>
                        </div>
                    </form:form>
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
                    <div>
                        <div class="table-responsive">
                            <h4>SELECT GROUP WISE PRIVILEGE</h4>
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Module Name</th>                                       
                                    </tr>
                                </thead>

                                <tbody id="abstractList">
                                    <c:forEach items="${abstractList}" var="alist">
                                        <tr>
                                            <td>
                                                <c:if test="${not empty alist.assignedGroupId}">
                                                    <input type="checkbox" name="chkAbstractModuleId" value="${alist.abstractGroupId}" class="form-control" checked="true"/>
                                                </c:if>
                                                <c:if test="${empty alist.assignedGroupId}">
                                                    <input type="checkbox" name="chkAbstractModuleId" value="${alist.abstractGroupId}" class="form-control"/>
                                                </c:if>
                                            </td>
                                            <td>
                                                ${alist.abstractGroupName}
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <table class="table table-bordered table-hover table-striped">
                                <span id="msg"></span>
                                <input type="button" name="btnGroupWisePrivilegeBtn" value="Assign" class="btn btn-success" onclick="assignGroupPrivilege();"/>&nbsp;
                                <input type="button" name="btnGroupWisePrivilegeBtn" value="Revoke" class="btn btn-danger" onclick="revokeGroupPrivilege();"/>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
