<%-- 
    Document   : PromotionProposal
    Created on : Jul 12, 2020, 5:17:43 PM
    Author     : Manas
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/servicehistory.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function () {
                $('#example').DataTable({
                    "order": [[0, "asc"]]
                });
            });
        </script>
        <script type="text/javascript">
            var tempId;
            var toldSpc;
            var ttransferProposalDetailId;
            $(document).ready(function () {
                $("#deptCode").change(function () {
                    $('#cadrecode').empty();
                    $('#officeCode').empty();
                    var url = 'getCadreListJSON.htm?deptcode=' + this.value;
                    $('#cadrecode').append('<option value="">--Select Cadre--</option>');
                    $.getJSON(url, function (result) {
                        $.each(result, function (i, obj) {
                            $('#cadrecode').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                });

                $("#deptCode").change(function () {
                    $('#officeCode').empty();
                    $('#cadrecode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $('#officeCode').append('<option value="">--Select Office--</option>');
                    $.getJSON(url, function (result) {
                        $.each(result, function (i, field) {
                            $('#officeCode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                });

                $("#cadrecode").change(function () {
                    $('#gradecode').empty();
                    var url = 'getGradeListCadreWiseJSON.htm?cadreCode=' + this.value;
                    $('#gradecode').append('<option value="">Select</option>');
                    $.getJSON(url, function (result) {
                        $.each(result, function (i, obj) {
                            $('#gradecode').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                });


            });
            function getDeptWiseOfficeList(type) {

                $('#hidAuthOffCode').empty();
                $('#authSpc').empty();

                var url = 'getOfficeListJSON.htm?deptcode=' + $('#hidAuthDeptCode').val();
                if ($('#transferType').val() == "DEPUTATION") {
                    url = 'getDeputedOfficeListJSON.htm?deptcode=' + $('#hidAuthDeptCode').val();
                }
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');

                    });
                });
            }

            function getOfficeWisePostList(type) {
                $('#authSpc').empty();
                url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + $('#hidAuthOffCode').val();
                $('#authSpc').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');

                    });
                });



            }

            function getPost(type) {
                $('#authPostName').val($('#authSpc option:selected').text());

                $('#hidTempAuthPost').val($('#authSpc').val());
                $('#hidTempDeptCode').val($('#hidAuthDeptCode').val());
                $('#hidTempAuthOffCode').val($('#hidAuthOffCode').val());

            }

            function onlyIntegerRange(e) {
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 57) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 57) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }

            function saveCheck() {
                if ($('#orderNumber').val() == "") {
                    alert("Please enter Order Number.");
                    $('#orderNumber').focus();
                    return false;
                }
                if ($('#orderDate').val() == "") {
                    alert("Please enter Order Date");
                    return false;
                }
                if ($('#authPostName').val() == "") {
                    alert("Please select Details of Authority.");
                    return false;
                }
                if ($('#fromDate').val() == "") {
                    alert("Please enter From Date.");
                    $('#fromDate')[0].focus();
                    return false;
                }
                if ($('#toDate').val() == "") {
                    alert("Please enter To Date.");
                    $('#toDate')[0].focus();
                    return false;
                }
                if (($('#fromDate').val() != '') && ($('#toDate').val() != '')) {
                    var ftemp = $("#fromDate").val().split("-");
                    var ttemp = $("#toDate").val().split("-");
                    var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                    var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);
                    if (fdt > tdt) {
                        alert("From Date must be less than To Date");
                        return false;
                    }
                }
                return true;
            }
            function removeRow(idx)
            {
                $('#tr_' + idx).css("background", '#FF2233');
                $('#tr_' + idx).fadeOut(300, function () {
                    $(this).remove();
                });
            }
            function removeDetailRow(idx)
            {
                $('#trd_' + idx).css("background", '#FF2233');
                $('#trd_' + idx).fadeOut(300, function () {
                    $(this).remove();
                });
            }
            function addProposal(me, empId, spc, isAdditional) {
                //$(me).parent().parent().remove();

                if (isAdditional == 'N') {
                    if (confirm("Are you sure you want to transfer this Employee?")) {
                        $.ajax({
                            url: 'AddTransferProposal.htm',
                            type: 'get',
                            data: 'empId=' + empId + '&cadreCode=' + $('#cadreCode').val() + '&spc=' + spc + '&proposalId=' + $('#proposalId').val() + '&hasAdditional=' + isAdditional,
                            success: function (retVal) {
                                $('#proposalId').val(retVal);
                                self.location = 'TransferProposal.htm?cadreCode=' + $('#cadreCode').val() + '&proposalId=' + retVal;
                            }
                        });
                    }
                } else if (isAdditional == 'Y') {
                    if (confirm("Are you sure you want to give Additional Charge?")) {
                        $.ajax({
                            url: 'AddTransferProposal.htm',
                            type: 'get',
                            data: 'empId=' + empId + '&cadreCode=' + $('#cadreCode').val() + '&spc=' + spc + '&proposalId=' + $('#proposalId').val() + '&hasAdditional=' + isAdditional,
                            success: function (retVal) {
                                $('#proposalId').val(retVal);
                                self.location = 'TransferProposal.htm?cadreCode=' + $('#cadreCode').val() + '&proposalId=' + retVal;
                            }
                        });
                    }
                } else if (isAdditional == 'D') {
                    $.ajax({
                        url: 'AddTransferProposal.htm',
                        type: 'get',
                        data: 'empId=' + empId + '&cadreCode=' + $('#cadreCode').val() + '&spc=' + spc + '&proposalId=' + $('#proposalId').val() + '&hasAdditional=' + isAdditional,
                        success: function (retVal) {
                            $('#proposalId').val(retVal);
                            self.location = 'TransferProposal.htm?cadreCode=' + $('#cadreCode').val() + '&proposalId=' + retVal;
                        }
                    });
                }
            }
            function getTransferEmployees(proposalId) {
                $.ajax({
                    url: 'GetTransferEmployees.htm',
                    type: 'get',
                    data: 'proposalId=' + proposalId,
                    success: function (retVal) {
                        $('#transfer_blk').html(retVal);
                    }
                });
            }
            function filterResult() {
                var proposalId = $('#proposalId').val();
                var searchby = $("#searchby").val();
                if (searchby == "cadre") {
                    var cadreCode = $("#cadrecode").val();
                    var cadreGrade = $("#gradecode").val();
                    $.post("getCadreGradeWiseEmpList.htm", {cadreCode: cadreCode, cadreGrade: cadreGrade, transferProposalId: proposalId})
                            .done(function (data) {
                                $("#emplist").empty();
                                $.each(data.empList, function (i, obj) {
                                    var html = html + '<tr><td>' + obj.empName + '</td>';
                                    html = html + '<td>' + obj.postedPostName + '</td>';
                                    html = html + '<td><input type="button" value="Add" style="background:#0379B4" class="btn btn-sm btn-success" onclick="javascript: addProposal(this,\'' + obj.empId + '\',\'' + obj.oldSpc + '\',\'N\')"  /><br />';
                                    html = html + '</td></tr>';
                                    $("#emplist").append(html);
                                })
                            })

                } else if (searchby == "office") {
                    var offcode = $("#officeCode").val();
                    $.post("getOfficeEmployeeList.htm", {offcode: offcode, transferProposalId: proposalId})
                            .done(function (data) {
                                $("#emplist").empty();
                                $.each(data, function (i, obj) {
                                    var html = html + '<tr><td>' + obj.fullname + '</td>';
                                    html = html + '<td>' + obj.post + '</td>';
                                    html = html + '<td><input type="button" value="Add" style="background:#0379B4" class="btn btn-sm btn-success" onclick="javascript: addProposal(this,\'' + obj.empid + '\',\'' + obj.spc + '\',\'N\')"  /><br />';
                                    html = html + '</td></tr>';
                                    $("#emplist").append(html);
                                })
                            })
                } else if (searchby == "gpfno") {
                    var gpfno = $("#gpfno").val();
                    $.post("getEmployeeDataasJson.htm", {gpfno: gpfno})
                            .done(function (data) {
                                $("#emplist").empty();
                                $.each(data, function (i, obj) {
                                    var html = html + '<tr><td>' + obj.fullname + '</td>';
                                    html = html + '<td>' + obj.curDesg + '</td>';
                                    html = html + '<td><input type="button" value="Add" style="background:#0379B4" class="btn btn-sm btn-success" onclick="javascript: addProposal(this,\'' + obj.empid + '\',\'' + obj.spc + '\',\'N\')"  /><br />';
                                    html = html + '</td></tr>';
                                    $("#emplist").append(html);
                                })
                            })
                } else if (searchby == "hrmsid") {
                    var hrmsid = $("#hrmsid").val();
                    $.post("getEmployeeDetails.htm", {hrmsid: hrmsid})
                            .done(function (data) {
                                $("#emplist").empty();
                                $.each(data, function (i, obj) {
                                    var html = html + '<tr><td>' + obj.fullname + '</td>';
                                    html = html + '<td>' + obj.spn + '</td>';
                                    html = html + '<td><input type="button" value="Add" style="background:#0379B4" class="btn btn-sm btn-success" onclick="javascript: addProposal(this,\'' + obj.empid + '\',\'' + obj.spc + '\',\'N\')"  /><br />';
                                    html = html + '</td></tr>';
                                    $("#emplist").append(html);
                                })
                            })
                }
            }
            function deleteEmployee(transferProposalDetailId) {
                if (confirm("Are you sure you want to remove the employee from the Proposal?"))
                {
                    $.ajax({
                        url: 'DeleteEmployee.htm',
                        type: 'get',
                        data: 'transferProposalDetailId=' + transferProposalDetailId,
                        success: function (retVal) {
                            self.location = 'TransferProposal.htm?cadreCode=' + $('#cadreCode').val() + '&proposalId=' + $('#proposalId').val();
                        }
                    });
                }
            }
            function savePosting() {
                thasAdditional = "N";
                if ($('#transferType').val() == "ADDITIONAL") {
                    thasAdditional = "Y";
                } else if ($('#transferType').val() == "DEPUTATION") {
                    thasAdditional = "D";
                }
                $.ajax({
                    url: 'UpdateNewSPC.htm',
                    type: 'get',
                    data: 'empId=' + tempId + '&cadreCode=' + $('#cadreCode').val() + '&oldSpc=' + toldSpc + '&newSPC=' + $('#hidTempAuthPost').val() + '&transferProposalDetailId=' + ttransferProposalDetailId + '&transferType=' + thasAdditional,
                    success: function (retVal) {
                        self.location = 'TransferProposal.htm?cadreCode=' + $('#cadreCode').val() + '&proposalId=' + $('#proposalId').val();
                    }
                });
            }
            function markAdditional(proposalID, isChecked) {
                if (isChecked) {
                    if (confirm("Are you sure you want to mark this as an additional charge?")) {

                    }
                }
            }
            function showPostingWindow(postingType, empId, transferProposalDetailId, oldSpc) {
                ttransferProposalDetailId = transferProposalDetailId;
                $('#transferType').val(postingType);
                tempId = empId;
                toldSpc = oldSpc;
            }
            function removePosting(me, detailPostingId) {
                $.post("deletePostingInfo.htm", {detailPostingId: detailPostingId})
                        .done(function (data) {
                            myObj = JSON.parse(data);
                            if (myObj.status == "S") {
                                $(me).parent().remove();
                            } else {
                                alert("Error Occured");
                            }
                        });
            }
            function open_serach_div(vals) {
                if (vals == "cadre") {
                    $("#deptCode").val("");
                    $("#cadrecode").val("");
                    $("#officeCode").val("");
                    $("#id_dept_details").show();
                    $("#id_grade_details").show();
                    $(".cls_cadre").show();
                    $(".cls_office").hide();
                    $("#id_gpf").hide();
                    $("#id_hrms").hide();
                    $("#gpfno").val("");
                    $("#hrmsid").val("");
                    $("#deptCode").show();
                    $("#deptlbl").show();


                }
                else if (vals == "office") {
                    $("#deptCode").val("");
                    $("#cadrecode").val("");
                    $("#officeCode").val("");
                    //$("#id_dept_details").show();
                    $("#deptlbl").show();
                    $("#deptCode").show();
                    $("#id_grade_details").hide();
                    //$(".cls_cadre").hide();
                    $(".cls_office").show();
                    $("#id_gpf").hide();
                    $("#id_hrms").hide();
                    $("#gpfno").val("");
                    $("#hrmsid").val("");
                } else if (vals == "gpfno") {
                    $("#id_dept_details").hide();
                    $("#id_grade_details").hide();
                    $(".cls_cadre").hide();
                    $(".cls_office").hide();
                    $("#id_gpf").show();
                    $("#id_hrms").hide();
                    $("#deptCode").val("");
                    $("#cadrecode").val("");
                    $("#officeCode").val("");
                } else if (vals == "hrmsid") {
                    $("#id_dept_details").hide();
                    $("#id_grade_details").hide();
                    $(".cls_cadre").hide();
                    $(".cls_office").hide();
                    $("#id_gpf").hide();
                    $("#id_hrms").show();
                    $("#deptCode").val("");
                    $("#cadrecode").val("");
                    $("#officeCode").val("");
                }

                $("#id_button").show();
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
                                <li>
                                    <i class="fa fa-file"></i> <a href="TransferProposalList.htm">Transfer Proposals</a> 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Transfer / Deputation Proposal</li>
                            </ol>
                        </div>
                    </div>
                    <form:form action="saveGIS.htm" method="post" commandName="TransferProposalForm">
                        <div class="container-fluid">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <strong style="font-size:14pt;">Transfer / Deputation Proposal</strong>
                                </div>
                                <div align="right" style="margin:10px 10px">
                                    <input type="button" style="margin-left:20px;background:#008900" onClick="javascript: self.location = 'TransferProposalList.htm'" class="btn btn-sm btn-success" value="&laquo; Back to Transfer Proposal List" />
                                </div>

                                <div class="panel panel-primary" style="margin-left:20px;margin-right:20px">

                                    <div class="panel-heading">Search Criteria</div>
                                    <div class="panel-body">
                                        <form:hidden path="empId" id="empId"/>
                                        <form:hidden path="proposalId" id="proposalId"/>
                                        <form:hidden path="transferProposalId" id="transferProposalId"/>
                                        <form:hidden path="hidTempAuthOffCode" id="hidTempAuthOffCode"/>
                                        <form:hidden path="hidTempAuthPost" id="hidTempAuthPost"/>
                                        <form:hidden path="hidTempDeptCode" id="hidTempDeptCode"/>
                                        <form:hidden path="transferType" id="transferType" />
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-2" style="text-align:right;">
                                                <label for="cadreCode">Search By:<span style="color: red">*</span></label>
                                            </div>
                                            <div class="col-lg-4">   
                                                <select class="form-control" name="searchby" id="searchby" onChange="open_serach_div(this.value)">
                                                    <option value="">-Select-</option>
                                                    <option value="cadre" <c:if test="${searcby == 'cadre'}"> selected="selected"</c:if>>Cadre</option>
                                                    <option value="office" <c:if test="${searcby == 'office'}"> selected="selected"</c:if>>Office</option> 
                                                    <option value="gpfno" <c:if test="${searcby == 'gpfno'}"> selected="selected"</c:if>>GPF No</option> 
                                                    <option value="hrmsid" <c:if test="${searcby == 'hrmsid'}"> selected="selected"</c:if>>HRMS ID</option> 
                                                    </select>
                                                </div>
                                                <div class="col-lg-2 cls_cadre" style="text-align:right;display:none;" >
                                                    <label>Cadre<span style="color: red">*</span></label>
                                                </div>
                                                <div class="col-lg-4 cls_cadre" style="display:none;">   
                                                    <select class="form-control" name="cadrecode" id="cadrecode">
                                                        <option value="">Select</option>                                            
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="row" id='id_dept_details' style="margin-bottom: 7px;">
                                                <div class="col-lg-2" id="deptlbl" style="text-align:right;display:none;" >
                                                    <label>Department:<span style="color: red">*</span></label>
                                                </div>
                                                <div class="col-lg-4 cls_cadre">
                                                    <select class="form-control" name="deptCode" id="deptCode" style="display:none;">
                                                        <option value="">-Select-</option>
                                                    <c:if test="${LoginUserBean.loginusername eq 'services1'}">
                                                        <option value="11">GENERAL ADMINISTRATION AND PUBLIC GRIEVANCE</option>
                                                    </c:if>
                                                    <c:if test="${LoginUserBean.loginusername eq 'homeadmin'}">
                                                        <option value="14">HOME</option>
                                                    </c:if>                                                                                                                                                                    
                                                </select>
                                            </div>
                                            <div class="col-lg-2 cls_cadre" style="text-align:right;display:none;" >
                                                <label>Grade<span style="color: red">*</span></label>
                                            </div>
                                            <div class="col-lg-4 cls_cadre" style="display:none;">   
                                                <select class="form-control" name="gradecode" id="gradecode">
                                                    <option value="">Select</option>                                            
                                                </select>
                                            </div>




                                            <div class="col-lg-2 cls_office" style="text-align:right;display:none">
                                                <label>Office<span style="color: red">*</span></label>
                                            </div>
                                            <div class="col-lg-4 cls_office"  style='display:none'>   
                                                <select class="form-control" name="officeCode" id="officeCode" >
                                                    <option value="">-Select Office-</option>                                                   
                                                </select>
                                            </div>
                                        </div>                                                    


                                        <div class="row" id='id_gpf' style="margin-bottom: 7px;display:none">
                                            <div class="col-lg-2" style="text-align:right;">
                                                <label>GPF No<span style="color: red">*</span></label>
                                            </div>
                                            <div class="col-lg-4">   
                                                <input type="text" name='gpfno' id='gpfno' class="form-control"/>
                                            </div>
                                        </div>  
                                        <div class="row" id='id_hrms' style="margin-bottom: 7px;display:none">
                                            <div class="col-lg-2" style="text-align:right;">
                                                <label for="cadreCode">HRMS ID<span style="color: red">*</span></label>
                                            </div>
                                            <div class="col-lg-4">   
                                                <input type="text" name='hrmsid' id='hrmsid' class="form-control"/>
                                            </div>
                                        </div>
                                        <div class="row" id='id_button' style="margin-bottom: 7px;display:none">
                                            <div class="col-lg-2" style="text-align:right;">
                                                &nbsp;
                                            </div>
                                            <div class="col-lg-4">   
                                                <input type='button' value="Search" name='button' class="btn btn-sm btn-danger" onClick="filterResult()"/>       
                                            </div>
                                        </div>        


                                    </div>
                                </div>


                                <div class="panel-body">



                                    <!-- <div class="row" style="margin-bottom: 7px;">
 
                                         <div class="col-lg-1" style="text-align:right;">
                                             <label for="cadreCode">Cadre:<span style="color: red">*</span></label>
                                         </div>
                                         <div class="col-lg-2">   
                                             <select class="form-control" name="cadreCode" id="cadreCode" onchange="javascript: filterResult(this.value)">
                                                 <option value="">-Select-</option>
                                                 <option value="1103"<c:if test="${cadreCode == '1103'}"> selected="selected"</c:if>>OAS</option> 
                                                 <option value="1101"<c:if test="${cadreCode == '1101'}"> selected="selected"</c:if>>IAS</option> 
                                                 </select>
                                             </div>
                                         </div>-->
                                        <div class="row">
                                            <div id="result_table" class="col-lg-7">
                                                <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0">
                                                    <thead>
                                                        <tr>
                                                            <th>Employee Name</th>
                                                            <th>Post</th>
                                                            <th width="15%">Action</th>
                                                        </tr>
                                                    </thead>

                                                    <tbody id="emplist">
                                                    <c:forEach var="tpList" items="${tpList}" varStatus="theCount">
                                                        <tr id="tr_${theCount.index+1}">
                                                            <td>${tpList.empName}</td>
                                                            <td>${tpList.postedPostName}</td>
                                                            <td align="center">
                                                                <input type="button" value="Add" style="background:#0379B4" class="btn btn-sm btn-success" onClick="javascript: addProposal('${tpList.empId}', '${tpList.oldSpc}', ${theCount.index+1}, 'N')"  />                                                                
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>                                    
                                        </div>
                                        <div  class="col-lg-5" id="transfer_blk">
                                            <table class="table table-striped table-bordered" width="100%" cellspacing="0" style="margin-top:80px;">
                                                <thead>
                                                    <tr style="font-weight:bold;background:#035F8B;color:#FFFFFF;font-size:13pt;">
                                                        <td colspan="3">Transfer / Deputation Detail</td>
                                                    </tr>
                                                </thead>
                                                <tbody id="tpdList">
                                                    <c:forEach var="tpdList" items="${tpdList}" varStatus="theCount">
                                                        <tr id="trd_${theCount.index+1}">
                                                            <td>${tpdList.empName}<br /><span style='font-size:9pt;color:#008900;'>${tpdList.postedPostName}</span>
                                                                <br /><span style="font-weight:bold;color:#890000;">Posting Detail:</span><br />
                                                                <c:forEach var="tpdListDetail" items="${tpdList.transferProposalDetail}" varStatus="theCount">
                                                                    <c:if test="${not empty tpdListDetail.newpost}">
                                                                        <div>
                                                                            <span style="color:#666;font-style:italic;">${tpdListDetail.newpost}</span>
                                                                            <c:if test="${tpdListDetail.isadditional == 'Y'}">
                                                                                <span style="font-weight:bold;color:#890000;">(Additional)</span>

                                                                            </c:if>
                                                                            <c:if test="${tpdListDetail.isadditional == 'D'}">
                                                                                <span style="font-weight:bold;color:#890000;">(Deputation)</span>

                                                                            </c:if>
                                                                            <a href="javascript:void(0)" onClick="removePosting(this,${tpdListDetail.detailPostingId})"><span class="glyphicon glyphicon-remove"></span></a>
                                                                            <hr/>
                                                                        </div>
                                                                    </c:if>

                                                                </c:forEach>

                                                                <c:if test="${not empty tpdList.newPostName}">
                                                                    <br /><span style="font-weight:bold;color:#890000;">Posting as:</span><br /><span style="color:#666;font-style:italic;">${tpdList.newPostName}</span>
                                                                </c:if>
                                                                <c:if test="${tpdList.hasAdditional == 'Y'}">
                                                                    <br /><span style="font-weight:bold;color:#890000;">Additional:</span><br /><span style="color:#666;font-style:italic;">${tpdList.additionalPostName}</span>
                                                                </c:if>
                                                                <c:if test="${tpdList.hasAdditional == 'D'}">
                                                                    <br /><span style="font-weight:bold;color:#890000;">Deputation:</span><br /><span style="color:#666;font-style:italic;">${tpdList.additionalPostName}</span>
                                                                </c:if>
                                                                <br />                                                    
                                                            </td>
                                                            <td width="10%">
                                                                <button type="button" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#transferAuthorityModal" onClick="showPostingWindow('TRANSFER', '${tpdList.empId}',${tpdList.transferProposalDetailId}, '${tpdList.oldSpc}')">Posting</button><br />
                                                                <input type="button" value="Additional" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#transferAuthorityModal" onClick="showPostingWindow('ADDITIONAL', '${tpdList.empId}',${tpdList.transferProposalDetailId}, '${tpdList.oldSpc}')" style="margin-top:7px;background:#008900"  /><br/>
                                                                <input type="button" value="Deputation" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#transferAuthorityModal" onClick="showPostingWindow('DEPUTATION', '${tpdList.empId}',${tpdList.transferProposalDetailId}, '${tpdList.oldSpc}')" style="margin-top:7px;background:#008900"  />
                                                            </td>
                                                            <td width="10%">
                                                                <input type="button" value="Remove" style="background:#0379B4" class="btn btn-sm btn-success" onClick="javascript: deleteEmployee(${tpdList.transferProposalDetailId})"  />
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel-footer">
                                </div>
                            </div>
                        </div>

                        <div id="transferAuthorityModal" class="modal" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Choose Post</h4>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-2">
                                                <label for="sltDept">Department</label>
                                            </div>
                                            <div class="col-lg-9">
                                                <form:select path="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList('A');">
                                                    <option value="">--Select Department--</option>
                                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-2">
                                                <label for="note">Office</label>
                                            </div>
                                            <div class="col-lg-9">
                                                <form:select path="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWisePostList('A');">
                                                    <option value="">--Select Office--</option>
                                                    <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-2">
                                                <label for="note">Post</label>
                                            </div>
                                            <div class="col-lg-9">
                                                <form:select path="authSpc" id="authSpc" class="form-control" onchange="getPost('A');">
                                                    <option value="">--Select Post--</option>
                                                    <form:options items="${postlist}" itemValue="spc" itemLabel="postname"/>
                                                </form:select>
                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" onClick="javascript: savePosting()">Save New Posting</button>
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        $(function () {
            $('#dateofDeposit').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
            $('#fromDate').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
            $('#toDate').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
        });
    </script>
</html>
