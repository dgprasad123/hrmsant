<%-- 
    Document   : DeputationData
    Created on : 28 Apr, 2018, 10:34:40 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/chosen.jquery.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function() {
                $('#txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    maxDate: moment()
                });
                $('#txtWEFrmDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    maxDate: moment()
                });
                $('#txtTillDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
//                    maxDate: moment()
                });

                toggleOrganisationType();

            });
            function getDeptWiseOfficeList(type) {
                var deptcode;
                if (type == "A") {
                    deptcode = $('#hidNotifyingDeptCode').val();
                    $('#hidNotifyingOffCode').empty();
                    $('#hidNotiSpc').empty();
                } else if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    $('#hidPostedOffCode').empty();
                    $('#hidPostedSpc').empty();
                }
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;

                var url = "";
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                } else {
                    if (type == "A") {
                        url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                    } else if (type == "P") {
                        //url = 'getOtherOrgOfficeListJSON.htm?deptcode=' + deptcode;
                        url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                    }
                }

                if (type == "A") {
                    $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    $('#hidPostedOffCode').append('<option value="">------Select Office------</option>');
                    $('#hidPostedSpc').append('<option value="">-------Select Post------</option>');
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                }).done(function() {
                    if (type == "A") {
                        $("#hidNotifyingOffCode").trigger("chosen:updated");
                    } else if (type == "P") {
                        $('#hidPostedOffCode').chosen();
                        $("#hidPostedOffCode").trigger("chosen:updated");
                        url = "getForeignBodyPostListJSON.htm?deptcode=" + deptcode;
                        $.getJSON(url, function(data) {
                            $.each(data, function(i, obj) {
                                if (type == "P") {
                                    $('#hidPostedSpc').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                                }
                            });
                        })
                    }
                });
            }

            function getOfficeWisePostList(type) {
                var offcode;
                if (type == "A") {
                    offcode = $('#hidNotifyingOffCode').val();
                    $('#hidNotiSpc').empty();
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    $('#hidPostedSpc').empty();
                }
                var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + offcode;
                if (type == "A") {
                    url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#hidNotiSpc').append('<option value="">--Select Post--</option>');
                } else if (type == "P") {
                    $('#hidPostedSpc').append('<option value="">--Select Post--</option>');
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#hidNotiSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        }
                    });
                }).done(function() {
                    if (type == "A") {
                        $('#hidNotiSpc').chosen();
                        $("#hidNotiSpc").trigger("chosen:updated");
                    } else if (type == "P") {
                        $('#hidPostedSpc').chosen();
                        $("#hidPostedSpc").trigger("chosen:updated");
                    }
                });

                //Start Field Off Code
                if (type == "P") {
                    $('#sltFieldOffice').empty();
                    var url = 'transferGetFieldOffListJSON.htm?offcode=' + $('#hidPostedOffCode').val();
                    $('#sltFieldOffice').append('<option value="">--Select--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#sltFieldOffice').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
                //End Field Off Code
            }

            function toggleOrganisationType() {

                var orgType = $('input[name="radpostingauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $('#fieldOfficeDiv').show();
                } else if (orgType == 'GOI' || orgType == 'ISO') {
                    $('#fieldOfficeDiv').hide();
                }
            }

            function getPost(type) {
                if (type == "A") {
                    $('#notifyingSpc').val($('#hidNotiSpc option:selected').text());
                } else if (type == "P") {
                    $('#postedSpc').val($('#hidPostedSpc option:selected').text());
                }
            }

            function openDeputationPostingModal() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                } else {
                    var orgType = $('input[name="radpostingauthtype"]:checked').val();
                    if (orgType == 'GOO') {
                        $("#deputationPostedModal").modal("show");
                    } else if (orgType == 'GOI') {
                        $("#deputationPostedOtherOrgModal").modal("show");
                    } else if (orgType == 'ISO') {
                        $("#interstateOrgModal").modal("show");
                    }
                }
            }

            function closePostingModal() {
                $("#deputationPostedModal").modal("hide");
            }

            function closePostingOtherOrgModal() {
                $("#deputationPostedOtherOrgModal").modal("show");

                $('#postedSpc').val($('#hidPostedOtherOrgSpc option:selected').text());

            }

            function getGOIPost() {
                $('#postedSpc').val($('#hidPostedOtherOrgSpc option:selected').text());
            }

            function getISOPost() {
                $('#postedSpc').val($('#hidInterStateSPC option:selected').text());
            }

            function getSubCadreStatus() {
                $('#sltSubCadreStatus').empty();
                var url = 'deputationGetSubCadreStatusListJSON.htm?cadrestat=' + $('#sltCadreStatus').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltSubCadreStatus').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }


            function saveCheck() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                    return false;
                }
                if ($('#txtNotOrdNo').val() == '') {
                    alert('Notification Order No should not be blank.');
                    $('#txtNotOrdNo').focus();
                    return false;
                }

                if ($('#txtNotOrdDt').val() == '') {
                    alert('Notification Order Date should not be blank.');
                    $('#txtNotOrdDt').focus();
                    return false;
                }

                if ($('#sltCadreStatus').val() == '') {
                    alert('Select Cadre Status.');
                    $('#sltCadreStatus').focus();
                    return false;
                }

                if ($('#postedSpc').val() == '') {
                    alert('Select Details of Posting.');
                    $('#postedSpc').focus();
                    return false;
                }

                if ($('#txtWEFrmDt').val() == '') {
                    alert('Select With Effect From Date.');
                    $('#txtWEFrmDt').focus();
                    return false;
                }

                if ($('#sltWEFrmTime').val() == '') {
                    alert('Select With Effect From Time.');
                    $('#sltWEFrmTime').focus();
                    return false;
                }

                /*if ($('#txtTillDt').val() == '') {
                 alert('Select Till Date.');
                 $('#txtTillDt').focus();
                 return false;
                 }*/

                /*if ($('#sltTillTime').val() == '') {
                 alert('Select Till Date Time.');
                 $('#sltTillTime').focus();
                 return false;
                 }*/
            }

            function openDeputationNotifyingModal() {
                var orgType = $('input[name="radnotifyingauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $("#deputationAuthorityModal").modal("show");
                } else if (orgType == 'GOI') {
                    $("#deputationNotifyingOtherOrgModal").modal("show");
                }
            }
            function getNotifyingOtherOrgPost() {
                $("#notifyingSpc").val($('#hidNotifyingOthSpc option:selected').text());
                $("#deputationNotifyingOtherOrgModal").modal("hide");
            }

            function getGOIData() {
                $('#hidPostedOtherOrgSpc').empty();
                var url = "getGOIOfficeListJSON.htm?category=" + $('#hidGOICategory').val();
                $('#hidPostedOtherOrgSpc').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidPostedOtherOrgSpc').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getInterStateData() {
                $('#hidInterStateSPC').empty();
                var url = "getStateWiseOfficeListJSON.htm?statecode=" + $('#hidInterState').val();
                $('#hidInterStateSPC').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidInterStateSPC').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }
        </script>
    </head>
    <body>
        <form:form action="saveDeputation.htm" method="post" commandName="deputationForm">
            <form:hidden path="hidNotId"/>
            <form:hidden path="depId"/>
            <form:hidden path="hidTransferId"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Deputation 
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/> 
                            </div>
                            <div class="col-lg-3"></div>
                            <div class="col-lg-2"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label for="rdTransaction">Select type of Transaction</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:radiobutton path="rdTransaction" value="S" checked="checked" onclick="removePostingData();"/> Service Book Entry(Backlog)
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="rdTransaction" value="C" checked="checked" onclick="removePostingData();"/> Current Transaction(will effect current Pay or Post)
                            </div>
                            <div class="col-lg-2"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">Notification Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtNotOrdNo" id="txtNotOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt"> Notification Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtNotOrdDt" id="txtNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="radnotifyingauthtype">Details of Notifying Authority</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOO" id="notifyingGOO"/> 
                                <label for="notifyingGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOI" id="notifyingGOI"/> 
                                <label for="notifyingGOI"> Government of India </label>
                            </div>
                        </div>           

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="notifyingSpc" id="notifyingSpc" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" onclick="openDeputationNotifyingModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltCadreStatus"> Cadre Status <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select class="form-control" path="sltCadreStatus" id="sltCadreStatus" onchange="getSubCadreStatus();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${cadrestatuslist}" itemLabel="label" itemValue="value"/>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <label for="sltSubCadreStatus">Sub Cadre Status<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select class="form-control" path="sltSubCadreStatus" id="sltSubCadreStatus">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${subCadrestatuslist}" itemLabel="label" itemValue="value"/>
                                </form:select>
                            </div>
                        </div>       

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="postedSpc"> Details of Posting </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOO" onclick="toggleOrganisationType()" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOI" onclick="toggleOrganisationType()" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="ISO" onclick="toggleOrganisationType()" id="postedISO"/> 
                                <label for="postedISO"> Inter State </label>
                            </div>
                        </div>    

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="postedSpc"> </label>
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="postedSpc" id="postedSpc" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button  type="button" class="btn btn-primary" onclick="openDeputationPostingModal()">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;" id="fieldOfficeDiv">
                            <div class="col-lg-2">
                                <label for="sltFieldOffice">Field Office</label>
                            </div>
                            <div class="col-lg-9">   
                                <form:select path="sltFieldOffice" id="sltFieldOffice" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${fieldofflist}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-1">   
                            </div>
                        </div> 
                        <div class="row" style="margin-bottom: 7px;" id="blankDiv">
                            <div class="col-lg-2">
                                &nbsp;
                            </div>
                            <div class="col-lg-9">   
                                &nbsp;
                            </div>
                            <div class="col-lg-1">   
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="depuDetails"> Deputation Period Details </label>
                            </div>
                        </div>  

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="chkExtnDptnPrd"> If Extension of Deputation Period </label>
                            </div>
                            <div class="col-lg-2">
                                <form:checkbox path="chkExtnDptnPrd" id="chkExtnDptnPrd" value=""/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtWEFrmDt">With Effect From<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtWEFrmDt" id="txtWEFrmDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="Time">Time<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select class="form-control" path="sltWEFrmTime" id="sltWEFrmTime">
                                    <form:option value="">-Select-</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>  
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="fieldoffice">Till Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtTillDt" id="txtTillDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="Time">Time<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select class="form-control" path="sltTillTime" id="sltTillTime">
                                    <form:option value="">-Select-</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>  
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note"> Note </label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>

                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save Deputation" class="btn btn-success" onclick="return saveCheck()"/>

                        <c:if test="${empty deputationForm.depId && deputationForm.hidNotId gt 0}">
                            <input type="submit" name="action" value="Delete" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>
                        </c:if>

                        <input type="submit" name="action" value="Back to List Page" class="btn btn-default"/>
                    </div>
                </div>
            </div>

            <div id="deputationAuthorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Notifying Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotifyingDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingDeptCode" id="hidNotifyingDeptCode" class="form-control" onchange="getDeptWiseOfficeList('A');">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotifyingOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOffCode" id="hidNotifyingOffCode" class="form-control" onchange="getOfficeWisePostList('A');">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${sancOfflist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotiSpc">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotiSpc" id="hidNotiSpc" class="form-control" onchange="getPost('A');">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${sancPostlist}" itemValue="spc" itemLabel="postname"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>                   

            <div id="deputationPostedModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Posted Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedDeptCode">Posted Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDeptCode" id="hidPostedDeptCode" class="form-control" onchange="getDeptWiseOfficeList('P');">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:option value="O">OTHERS</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedOffCode">Posted Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <c:if test="${not empty nextofficecode}">
                                        <form:select path="hidPostedOffCode" id="hidPostedOffCode" class="form-control" onchange="getOfficeWisePostList('P');">
                                            <form:option value="">--Select Office--</form:option>
                                            <form:options items="${otherOrgPostedOfflist}" itemValue="offCode" itemLabel="offName"/>
                                        </form:select>
                                    </c:if>
                                    <c:if test="${empty nextofficecode}">
                                        <form:select path="hidPostedOffCode" id="hidPostedOffCode" class="form-control" onchange="getOfficeWisePostList('P');">
                                            <form:option value="">--Select Office--</form:option>
                                            <form:options items="${postedOfflist}" itemValue="offCode" itemLabel="offName"/>
                                        </form:select>
                                    </c:if>
                                    <%--<form:select path="hidPostedOffCode" id="hidPostedOffCode" class="form-control" onchange="getOfficeWisePostList('P');">
                                         <form:option value="">--Select Office--</form:option>
                                         <form:options items="${postedOffList}" itemValue="offCode" itemLabel="offName"/>
                                     </form:select>--%>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedSpc">Posted Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedSpc" id="hidPostedSpc" class="form-control" onchange="getPost('P');">
                                        <form:option value="">--Select Post--</form:option>
                                        <c:if test="${not empty nextofficecode}">
                                            <form:options items="${foreignbodypostlist}" itemValue="postcode" itemLabel="post"/>
                                        </c:if>
                                        <c:if test="${empty nextofficecode}">
                                            <form:options items="${postedPostList}" itemValue="spc" itemLabel="postname"/>
                                        </c:if>

                                        <%--<form:options items="${postedPostList}" itemValue="spc" itemLabel="postname"/>--%>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div> 

            <div id="deputationPostedOtherOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Deputation to Government of India</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidGOICategory">Category</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidGOICategory" id="hidGOICategory" class="form-control" onchange="getGOIData();">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="M">Ministry</form:option>
                                        <form:option value="O">Others</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedOtherOrgSpc">Deputed To</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOthSpc" id="hidPostedOtherOrgSpc" class="form-control" onchange="getGOIPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${GOIOfficeList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <div id="interstateOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Interstate Deputation</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidInterState">State</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidInterState" class="form-control" onchange="getInterStateData();">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${statelist}" itemValue="statecode" itemLabel="statename"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidInterStateSPC">Deputed To</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidInterStateSPC" id="hidInterStateSPC" class="form-control" onchange="getISOPost();">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${interStateOfficeList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <div id="deputationNotifyingOtherOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Notifying Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotifyingOthSpc">Authority</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOthSpc" id="hidNotifyingOthSpc" class="form-control" onchange="getNotifyingOtherOrgPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgfflist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
