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

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {

            });

            function getDeptWiseOfficeList() {
                $('#hidOffCode').empty();
                var url = 'getOfficeListJSON.htm?deptcode=' + $('#hidDeptCode').val();
                $('#hidOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }


            function getDistrictAndDepartmentWiseOfficeList() {
                var deptcode;
                var distcode;

                if ($('#hidDeptCode').val() == '') {
                    alert('Please select Department.');
                    $('#hidDeptCode').focus();
                    return false;
                }
                if ($('#hidDistCode').val() == '') {
                    alert('Please select District.');
                    $('#hidDistCode').focus();
                    return false;
                }
                deptcode = $('#hidDeptCode').val();
                distcode = $('#hidDistCode').val();
                $('#hidOffCode').empty();
                $('#hidOffCode').append('<option value="">--Select Office--</option>');

                var url = "";
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getOfficeListDistrictAndDepartmentForBacklogEntryJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                } else {
                    url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                }

                //var url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWiseGPCList(offcode) {
                var offCode = "";
                if (offcode != '') {
                    offCode = offcode;
                } else {
                    offCode = $('#hidOffCode').val();
                }
                //alert(offCode);

                $('#hidPostCode').empty();
                var url = 'getPostCodeListJSON.htm?offcode=' + offCode;
                $('#hidPostCode').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidPostCode').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });

                $('#sltFieldOffice').empty();
                var url = 'joiningGetFieldOffListJSON.htm?offcode=' + offCode;
                $('#sltFieldOffice').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltFieldOffice').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getGPCWiseSPCList(offCode, gpc) {
                var offcode = "";
                var postcode = "";
                if (gpc != '') {
                    offcode = offCode;
                    postcode = gpc;
                } else {
                    offcode = $('#hidOffCode').val();
                    postcode = $('#hidPostCode').val();
                }
                alert($('#notType').val());

                $('#spc').empty();
                var url = "";

                /*if ($("#isbacklog").prop("checked") == true) {
                 url = 'getGPCWiseSPCListJSON.htm?offcode=' + offcode + '&gpc=' + postcode;
                 } else {
                 url = 'joiningGetGPCWiseSPCListJSON.htm?offcode=' + offcode + '&gpc=' + postcode;
                 }*/
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = "getGPCWiseSPCListJSON.htm?gpc=" + postcode + "&offcode=" + offcode;
                } else if ($("input[name=rdTransaction]:checked").val() == "C") {
                    if ($('#notType').val() == "DEPUTATION") {
                        url = "getGPCWiseSPCListJSON.htm?gpc=" + postcode + "&offcode=" + offcode;
                    } else {
                        url = "joiningGetGPCWiseSPCListJSON.htm?gpc=" + postcode + "&offcode=" + offcode;
                    }
                }
                $('#spc').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#spc').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }
            function getPost() {
                $('#spn').val($('#spc option:selected').text());
                $('#joiningPostingModal').modal('toggle');
            }

            function getOtherOrgPost(type) {
                if (type == "P") {
                    $('#spn').val($('#hidPostedOthSpc option:selected').text());

                    $('#hidDeptCode').val('');
                    $('#hidDistCode').val('');
                    $('#hidOffCode').val('');
                    $('#hidPostCode').val('');
                    $('#jspc').val('');

                    $("#joiningPostingOthOrgModal").modal("hide");
                    $("#joiningPostingGOIModal").modal("hide");
                }
            }

            function getGOIOfficeWisePost() {
                var url = "getGOIOfficeWisePostList.htm?goioffcode=" + $("#hidGOIOffCode").val();

                $('#hidGOIPostedSPC').empty();
                $('#hidGOIPostedSPC').append('<option value="">--Select--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidGOIPostedSPC').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function toggleTypeVal() {
                $('#tempType').val("");
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                    var tra = $('input[name="rdTransaction"]:checked').val();
                    if (tra == 'S') {
                        $('#SGOO').show();
                        $('#CGOI').show();
                    } else if (tra == 'C') {
                        $('#SGOO').show();
                    }
                }
                else {
                    var orgType = $('input[name="radpostingauthtype"]:checked').val();
                    if (orgType == 'GOO') {
                        $('#joiningPostingModal').modal("show");
                        //$('#spc').val('');
                    } else if (orgType == 'GOI') {
                        if ($("#notType").val() == 'FIRST_APPOINTMENT') {
                            $("#joiningPostingGOIModal").modal("show");
                        } else {
                            $("#joiningPostingOthOrgModal").modal("show");
                        }
                    }
                }
            }

            function removePostingData() {
                var tra = $('input[name="rdTransaction"]:checked').val();
                //alert($('input[name="rdTransaction"]:checked').val());
                if (tra == 'S') {
                    $('#SGOO').show();
                    $('#CGOI').show();
                }
                else if (tra == 'C') {
                    $('#SGOO').show();
                    $('#CGOI').hide();
                }
                if ($("#joinId").val() == "") {
                    $('#spn').val('');
                    $('#hidPostCode').val('');
                    $('#spc').val('');
                }
            }


            function removeDepedentDropdown() {
                $('#hidDistCode').val('');

                $('#hidOffCode').empty();
                $('#hidOffCode').append('<option value="">--Select Office--</option>');

                $('#hidPostCode').empty();
                $('#hidPostCode').append('<option value="">--Select Generic Post--</option>');

                $('#spc').empty();
                $('#spc').append('<option value="">--Select Substantive Post--</option>');
            }

            function validateJoining() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                    return false;
                }
                if ($('#joiningOrdDt').val() == '') {
                    alert("Please Enter Order Date");
                    $('#joiningOrdDt').focus();
                    return false;
                }
                if ($('#joiningDt').val() == '') {
                    alert("Please Enter Join Date");
                    $('#joiningDt').focus();
                    return false;
                }
                if ($('#txtWEFDt').val() == '') {
                    alert("Please Enter Date of Effect of Pay");
                    $('#txtWEFDt').focus();
                    return false;
                }
                if ($('input[name="radpostingauthtype"]:checked').val() == 'GOO') {
                    if ($('#spc').val() == '') {
                        alert("Please Select Govt. of Odisha Posting Details");
                        return false;
                    }
                }
                if ($('input[name="radpostingauthtype"]:checked').val() == 'GOI' && $("#notType").val() != 'FIRST_APPOINTMENT') {
                    if ($('#hidPostedOthSpc').val() == '') {
                        alert("Please Select Govt. of India Posting Details");
                        return false;
                    }
                } else if ($('input[name="radpostingauthtype"]:checked').val() == 'GOI' && $("#notType").val() == 'FIRST_APPOINTMENT') {
                    if ($('#hidGOIPostedSPC').val() == '') {
                        alert("Please Select Govt. of India Posting Details");
                        return false;
                    }
                }
                return true;
            }

            function getGOIPost() {
                $('#spn').val($('#hidGOIPostedSPC option:selected').text());
                $('#joiningPostingGOIModal').modal('toggle');
            }
        </script>
    </head>
    <body>
        <form:form action="saveEmployeeJoining.htm" method="post" commandName="jForm">
            <form:hidden path="jempid"/>
            <form:hidden path="notId"/>
            <form:hidden path="notType"/>
            <form:hidden path="rlvId"/>
            <form:hidden path="joinId"/>
            <form:hidden path="hidLcrId"/>

            <%--<form:hidden path="hidTempOffCode" id="hidTempOffCode"/>
            <form:hidden path="hidTempPostCode" id="hidTempPostCode"/>
            <form:hidden path="hidTempSpc" id="hidTempSpc"/>
            <form:hidden path="hidTempFieldOffCode" id="hidTempFieldOffCode"/>--%>


            <input type="hidden" id="tempType" value="V"/>
            <input type="hidden" id="entpsc" value="${entpsc}"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Joining
                    </div>
                    <div class="panel-body">
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
                                <form:radiobutton path="rdTransaction" value="S" id="backlog" onclick="removePostingData();"/> Service Book Entry(Backlog)
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="rdTransaction" value="C" id="current" onclick="removePostingData();" /> Current Transaction(will effect current Pay or Post)
                            </div>
                            <div class="col-lg-2"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>Notification Order Details</label>
                            </div>
                            <div class="col-lg-6">
                                <%--<form:checkbox path="isbacklog" id="isbacklog" value="Y"/> Is Backlog Entry <span style="font-style: initial;color: red;font-weight: bold;font-size: 10px;">(If the check box is checked then it will not affect current posting details)</span>--%>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (a) Type
                            </div>
                            <div class="col-lg-6">
                                <form:input class="form-control" path="notType" id="notType" disabled="true"/>
                            </div>
                            <div class="col-lg-2">

                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (b) Order No
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="notOrdNo" id="notOrdNo" readonly="true"/>
                            </div>
                            <div class="col-lg-2">
                                (c) Order Date
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="notOrdDt" id="notOrdDt" readonly="true"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (d) Department Name
                            </div>
                            <div class="col-lg-10">
                                <form:input class="form-control" path="notiDeptName" id="notiDeptName" readonly="true"/>
                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (e) Office Name
                            </div>
                            <div class="col-lg-10">
                                <form:input class="form-control" path="notiOffName" id="notiOffName" readonly="true"/>
                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (f) Authority
                            </div>
                            <div class="col-lg-10">
                                <form:input class="form-control" path="notiSpn" id="notiSpn" readonly="true"/>
                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (g) Note
                            </div>
                            <div class="col-lg-10">
                                <form:textarea class="form-control" path="notNote" id="notNote" readonly="true"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-6">
                                <label>Relieve Order Details</label>
                            </div>
                            <div class="col-lg-2">   

                            </div>
                            <div class="col-lg-2">

                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>    

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (a) Relieve Report/Letter No.
                            </div>
                            <div class="col-lg-6">
                                <form:input path="rlvOrdNo" id="rlvOrdNo" readonly="true"/>
                            </div>
                            <div class="col-lg-2">
                                (b) Order Date
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input path="rlvOrdDt" id="rlvOrdDt" readonly="true"/>
                                </div>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (c) Relieved On
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input path="rlvDt" id="rlvDt" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                (d) Relieved Time
                            </div>
                            <div class="col-lg-2">
                                <form:input path="rlvTime" id="rlvTime" readonly="true"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (e) Due Date of Joining
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input path="joiningDueDt" id="joiningDueDt" readonly="true"/>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                (f) Joining Time
                            </div>
                            <div class="col-lg-2">
                                <form:input path="joiningDueTime" id="joiningDueTime" readonly="true"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-6">
                                <label>Joining Order Details</label>
                            </div>
                            <div class="col-lg-2">   

                            </div>
                            <div class="col-lg-2">

                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (a) Joining Report/Letter No.<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-6">
                                <form:input path="joiningOrdNo" id="joiningOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                (b) Order Date<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group txtDate" id="processDate">
                                    <form:input path="joiningOrdDt" id="joiningOrdDt" readonly="true" class="form-control txtDate"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (c) Joined On<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date txtDate" id="processDate">
                                    <form:input path="joiningDt" id="joiningDt" readonly="true" class="form-control txtDate"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                (d) Joined Time<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltJoiningTime" id="sltJoiningTime" class="form-control">
                                    <option value="">-Select-</option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                (e) Date of Effect of Pay<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date txtDate" id="processDate">
                                    <form:input path="txtWEFDt" id="txtWEFDt" readonly="true" class="form-control txtDate"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (f) Unavailed joining Time Granted as EL
                            </div>
                            <div class="col-lg-2">
                                <form:checkbox path="chkujt" id="chkujt" value="Y"/>
                            </div>
                            <div class="col-lg-2">

                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (g) From Date
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date txtDate" id="processDate">
                                    <form:input path="ujtFrmDt" id="ujtFrmDt" readonly="true" class="form-control txtDate"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                (h) To Date
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date txtDate" id="processDate">
                                    <form:input path="ujtToDt" id="ujtToDt" readonly="true" class="form-control txtDate"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <br/>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="postedspn">Details of Posting</label>
                            </div>
                            <div class="col-lg-2" id="SGOO" >
                                <form:radiobutton path="radpostingauthtype" value="GOO" id="postedGOO" /> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>

                            <div class="col-lg-2" id="CGOI" >
                                <form:radiobutton path="radpostingauthtype" value="GOI" id="postedGOI" /> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <%--(i) Posting Details <span style="color: red">*</span>--%>
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="spn" id="spn"/>                           
                            </div>
                            <div class="col-lg-1">                                
                                <button type="button" class="btn btn-primary" onclick="toggleTypeVal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>                                   
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                Field Office
                            </div>
                            <div class="col-lg-9">
                                <form:select path="sltFieldOffice" id="sltFieldOffice" class="form-control">
                                    <option value="">--Select Office--</option>
                                    <form:options items="${fieldofflist}" itemValue="value" itemLabel="label"/>
                                </form:select>                        
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Note(if any)</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">                        
                        <button type="submit" name="action" value="Save" class="btn btn-default" id="btnSave" onclick="return validateJoining();">Save Joining</button>
                        <c:if test="${not empty jForm.joinId}">
                            <%--<button type="submit" name="action" value="Delete" class="btn btn-default">Delete Joining</button>--%>
                        </c:if>
                        <button type="submit" name="action" value="Back" class="btn btn-default">Back</button>
                    </div>
                </div>
            </div>


            <div id="joiningPostingModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Details of Posting</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidDeptCode" id="hidDeptCode" class="form-control" onchange="removeDepedentDropdown();">
                                        <option value="">--Select Department--</option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidDistCode" id="hidDistCode" class="form-control" onchange="getDistrictAndDepartmentWiseOfficeList();">
                                        <form:option value="">--Select District--</form:option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidOffCode" id="hidOffCode" class="form-control" onchange="getOfficeWiseGPCList('');">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${officeList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostCode">Generic Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostCode" id="hidPostCode" class="form-control" onchange="getGPCWiseSPCList('','');">
                                        <form:option value="">--Select Generic Post--</form:option>
                                        <form:options items="${gpclist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="spc">Substantive Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="jspc" id="spc" class="form-control" onchange="getPost();">
                                        <form:option value="">--Select Substantive Post--</form:option>
                                        <c:if test="${not empty jForm.joinId && not empty jForm.hidOffCode}">
                                            <form:option value="${jForm.jspc}">${jForm.spn}</form:option>
                                        </c:if>
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
            <div id="joiningPostingOthOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Details of Posting</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="hidPostedOthSpc">Posting Details</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOthSpc" id="hidPostedOthSpc" class="form-control" onchange="getOtherOrgPost('P');">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgOfflist}" itemValue="value" itemLabel="label"/>
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
            <div id="joiningPostingGOIModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Details of Posting</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="hidGOIOffCode">Office Details</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidGOIOffCode" id="hidGOIOffCode" class="form-control" onchange="getGOIOfficeWisePost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgOfflist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="hidGOIPostedSPC">Post Details</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidGOIPostedSPC" id="hidGOIPostedSPC" class="form-control" onchange="getGOIPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${goipostlist}" itemValue="value" itemLabel="label"/>
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
        </form:form>
    </body>
    <script type="text/javascript">
        $(function() {
            $('.txtDate').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
        });
    </script>
</html>
