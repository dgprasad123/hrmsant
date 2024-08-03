<%-- 
    Document   : PromotionProposal
    Created on : Jul 12, 2021, 5:17:43 PM
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
        <script type="text/javascript" src="js/jquery.min.js"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/servicehistory.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        <script type="text/javascript">
            var tpromotionProposalDetailId;
            var tempId;
            var toldSpc;
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
                
                $("#deptName").change(function () {
                    $('#cadrecode').empty();
                    $('#cadrecode').append('<option value="">Select</option>');
                    var url = 'getCadreListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function (result) {
                        $.each(result, function (i, obj) {
                            $('#ccadrecode').append('<option value="' + obj.value + '">' + obj.label + '</option>');
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
            function filterResult(cadreCode) {
                var proposalId = $('#proposalId').val();
                var searchby = $("#searchby").val();
                if (searchby == "cadre") {
                    var cadreCode = $("#cadrecode").val();                    
                    var cadreGrade = $("#gradecode").val();
                    $.post("getCadreGradeWiseEmpList.htm", {cadreCode: cadreCode,cadreGrade:cadreGrade, transferProposalId: proposalId})
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
            function addProposal(me, empId, spc) {
                $.ajax({
                    url: 'AddPromotionProposal.htm',
                    type: 'get',
                    data: 'empId=' + empId + '&spc=' + spc + '&proposalId=' + $('#proposalId').val(),
                    success: function (retVal) {
                        $('#proposalId').val(retVal);
                        self.location = 'PromotionProposal.htm?cadreCode=' + $('#cadreCode').val() + '&proposalId=' + retVal;
                    }
                });
            }
            function showPostingWindow(postingType, empId, promotionProposalDetailId, oldSpc) {
                tpromotionProposalDetailId = promotionProposalDetailId;
                $('#transferType').val(postingType);
                tempId = empId;
                toldSpc = oldSpc;
            }
            function showPayWindow() {

            }
            function savePosting() {
                thasAdditional = "N";
                if ($('#transferType').val() == "ADDITIONAL") {
                    thasAdditional = "Y";
                } else if ($('#transferType').val() == "DEPUTATION") {
                    thasAdditional = "D";
                }
                $.ajax({
                    url: 'UpdateNewSPCPromotion.htm',
                    type: 'get',
                    data: 'empId=' + tempId + '&cadreCode=' + $('#cadreCode').val() + '&oldSpc=' + toldSpc + '&newSPC=' + $('#hidTempAuthPost').val() + '&promotionProposalDetailId=' + tpromotionProposalDetailId + '&transferType=' + thasAdditional,
                    success: function (retVal) {
                        self.location = 'PromotionProposal.htm?cadreCode=' + $('#cadreCode').val() + '&proposalId=' + $('#proposalId').val();
                    }
                });
            }
            function showChangeCadreWindow() {
                $('#setCadre').modal('show');
            }
            function saveCadreInfo() {
                tCadrecode = $("#ccadrecode").val();
                tproposalId = $('#proposalId').val();
                tCadreGrade = $('#cadregrade').val();
                if (tCadrecode) {
                    $.post("updateNewCadrePromotion.htm", {cadreCode: tCadrecode, proposalId: tproposalId, cadreGrade: tCadreGrade})
                            .done(function (data) {
                                self.location = 'PromotionProposal.htm?cadreCode=' + $('#cadreCode').val() + '&proposalId=' + $('#proposalId').val();
                            })
                } else if (!tCadrecode) {
                    alert("Choose Cadre");
                }
            }
            function savePay() {
                $("#payleveldiv").html($("#paylevel").val());
            }
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
            function deleteEmployee(transferProposalDetailId) {
                if (confirm("Are you sure you want to remove the employee from the Proposal?"))
                {
                    $.ajax({
                        url: 'DeleteEmployee.htm',
                        type: 'get',
                        data: 'transferProposalDetailId=' + transferProposalDetailId,
                        success: function (retVal) {
                            self.location = 'PromotionProposal.htm?cadreCode=' + $('#cadreCode').val() + '&proposalId=' + $('#proposalId').val();
                        }
                    });
                }
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
                                    <i class="fa fa-file"></i> <a href="TransferProposalList.htm">Promotion Proposals</a> 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Promotion Proposal
                                </li>

                            </ol>
                        </div>
                    </div>
                    <form:form action="saveGIS.htm" method="post" commandName="TransferProposalForm">
                        <div class="container-fluid">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <strong style="font-size:14pt;">Promotion Proposal</strong>
                                </div>
                                <div align="right" style="margin:10px 10px">
                                    <input type="button" style="margin-left:20px;background:#008900" onClick="javascript: self.location = 'TransferProposalList.htm'" class="btn btn-sm btn-success" value="&laquo; Back to Promotion Proposal List" />
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
                                <div class="row">
                                    <div id="result_table" class="col-lg-7">
                                        <table id="example" class="table table-striped table-bordered" width="100%" cellspacing="0">
                                            <thead>
                                                <tr>
                                                    <th width="35%">Employee Name</th>
                                                    <th width="45%">Post</th>
                                                    <th width="15%">Action</th>
                                                </tr>
                                            </thead>

                                            <tbody id="emplist">
                                                <c:forEach var="tpList" items="${tpList}" varStatus="theCount">
                                                    <tr id="tr_${theCount.index+1}">
                                                        <td>${tpList.empName}</td>
                                                        <td>${tpList.postedPostName}</td>
                                                        <td align="center">
                                                            <input type="button" value="Add" style="background:#0379B4" class="btn btn-sm btn-success" onclick="javascript: addProposal(this, '${tpList.empId}', '${tpList.oldSpc}')"  />                                                                
                                                        </td>
                                                    </tr>
                                                </c:forEach> 
                                            </tbody>
                                        </table>                                    
                                    </div>
                                    <div  class="col-lg-5" id="transfer_blk">
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-sm-1" style="text-align:right;">
                                                <input type="button" value="Cadre" class="btn btn-sm btn-primary" onclick="showChangeCadreWindow()"/>
                                            </div>
                                            <div class="col-sm-10" style="text-align:left;">
                                                ${transferProposalListDetail.cadreName} (${transferProposalListDetail.cadreGrade})
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-sm-1" style="text-align:right;">
                                                <input type="button" value="Pay" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#payModal" onclick="showPayWindow()" style="margin-top:7px;background:#008900"/>
                                            </div>
                                            <div class="col-sm-10" style="text-align:left;" id="payleveldiv">

                                            </div>
                                        </div>
                                        <table class="table table-striped table-bordered" width="100%" cellspacing="0" style="margin-top:10px;">
                                            <thead>
                                                <tr style="font-weight:bold;background:#035F8B;color:#FFFFFF;font-size:13pt;">
                                                    <td colspan="3">Promotion Detail</td>
                                                </tr>
                                            </thead>
                                            <tbody id="tpdList">
                                                <c:forEach var="tpdList" items="${tpdList}" varStatus="theCount">
                                                    <tr id="trd_${theCount.index+1}">
                                                        <td>${tpdList.empName}<br /><span style='font-size:9pt;color:#008900;'>${tpdList.postedPostName}</span>
                                                            <br />
                                                            <span style="font-weight:bold;color:#890000;">Posting Detail:</span><br />
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
                                                                        <a href="javascript:void(0)" onclick="removePosting(this,${tpdListDetail.detailPostingId})"><span class="glyphicon glyphicon-remove"></span></a>
                                                                        <hr/>
                                                                    </div>
                                                                </c:if>

                                                            </c:forEach>                                                       
                                                        </td>
                                                        <td width="10%">
                                                            <button type="button" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#transferAuthorityModal" onclick="showPostingWindow('TRANSFER', '${tpdList.empId}',${tpdList.transferProposalDetailId}, '${tpdList.oldSpc}')">Posting</button><br/>
                                                            <input type="button" value="Additional" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#transferAuthorityModal" onclick="showPostingWindow('ADDITIONAL', '${tpdList.empId}',${tpdList.transferProposalDetailId}, '${tpdList.oldSpc}')" style="margin-top:7px;background:#008900"  />
                                                        </td>
                                                        <td width="10%">
                                                            <input type="button" value="Remove" style="background:#0379B4" class="btn btn-sm btn-success" onclick="javascript: deleteEmployee(${tpdList.transferProposalDetailId})"  />
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="payModal" class="modal" role="dialog">
                        <div class="modal-dialog">
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Choose Pay</h4>
                                </div>
                                <div class="modal-body">
                                    <div class="row" style="margin-bottom: 7px;">
                                        <div class="col-lg-2">
                                            <label for="sltDept">Pay Level</label>
                                        </div>
                                        <div class="col-lg-9">
                                            <input type="text" name="paylevel" id="paylevel" class="form-control"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" onclick="javascript: savePay()">Save Pay</button>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
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
                                    <button type="button" class="btn btn-default" onclick="javascript: savePosting()">Save New Posting</button>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

    <!--Cadre Information-->
    <div id="setCadre" class="modal fade" >
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
                                    <c:forEach items="${deptlist}" var="department">
                                        <option value="${department.deptCode}">${department.deptName}</option>
                                    </c:forEach>                                        
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3">Cadre Name</label>
                            <div class="col-sm-9">
                                <select class="form-control" name="ccadrecode" id="ccadrecode">
                                    <option value="">Select</option>                                            
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3">Grade Name</label>
                            <div class="col-sm-9">
                                <select class="form-control" name="cadregrade" id="cadregrade">
                                    <option value="">Select</option>                                            
                                </select>
                            </div>
                        </div>
                    </form>
                </div>

                <div class="modal-footer">                        
                    <div class="col-sm-10" >                            
                        <a href="javascript:saveCadreInfo()" class="btn btn-primary">Save</a> 
                        <button type="button" class="btn btn-danger" data-dismiss="modal">cancel</button>
                    </div>                        
                </div>
            </div>
        </div>
    </div>
    <!--Cadre Information-->

</body>
</html>
