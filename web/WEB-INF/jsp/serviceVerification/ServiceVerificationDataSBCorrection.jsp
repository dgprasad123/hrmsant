<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function getDeptWiseOfficeList(type) {
                var deptcode;

                deptcode = $('#sltDept').val();
                $('#sltOffice').empty();
                $('#sltSpc').empty();

                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                $('#sltOffice').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltOffice').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode;
                offcode = $('#sltOffice').val();
                $('#sltSpc').empty();
                $('#sltSpc').append('<option value="">--Select--</option>');
                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }

            function getPost() {
                $('#hidAuthSpc').val($('#sltSpc').val());
                $('#authName').val($('#sltSpc option:selected').text());

                $('#hidAuthOffice').val($('#sltOffice').val());
                $('#hidAuthDept').val($('#sltDept').val());

                $("#serviceVerificationAuthorityModal").modal("hide");
            }

            function getOtherOrgPost() {
                $('#hidAuthSpc').val($('#hidOthSpc').val());
                $('#authName').val($('#hidOthSpc option:selected').text());

                $('#hidAuthOffice').val('');
                $('#hidAuthDept').val('');

                $("#serviceVerificationOtherOrgModal").modal("hide");
            }

            function openServiceVerificationModal() {
                var orgType = $('input[name="radpostingauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $("#serviceVerificationAuthorityModal").modal("show");
                } else if (orgType == 'GOI') {
                    $("#serviceVerificationOtherOrgModal").modal("show");
                }
            }

            function validateForm() {
                if ($('#txtfdate').val() == '') {
                    alert('Please select From Date.');
                    return false;
                }

                if ($('#txttdate').val() == '') {
                    alert('Please select To Date.');
                    return false;
                }

                if ($('#sltftime').val() == '') {
                    alert('Please select From Time.');
                    return false;
                }

                if ($('#sltttime').val() == '') {
                    alert('Please select To Time.');
                    return false;
                }
                var orgType = $('input[name="radpostingauthtype"]:checked').val();
                if (orgType == "GOO") {
                    if ($('#hidAuthSpc').val() == "") {
                        alert('Please select Authority.');
                        return false;
                    }
                } else if (orgType == "GOI") {
                    if ($('#hidOthSpc').val() == "") {
                        alert('Please select Authority.');
                        return false;
                    }
                }

                var isToDateGreater = compareWithSystemDate();
                if (isToDateGreater == "Y") {
                    alert("To Date should not more than Verified Date");
                    $("#txtVerifiedOn").val($("#curdate").val());
                    return false;
                }
            }

            function compareWithSystemDate() {

                var isToDateGreater = "N";

                var curdate = new Date();

                var verifieddateTemp = $("#txtVerifiedOn").val();

                var verifieddateArr = verifieddateTemp.split("-");
                var verifiedYear = verifieddateArr[2];
                var verifiedMonth = monthint(verifieddateArr[1]);
                var verifiedDay = verifieddateArr[0];

                var verifieddate = new Date(verifiedYear, verifiedMonth, verifiedDay);

                var todateTemp = $("#txttdate").val();

                var todateArr = todateTemp.split("-");
                var todateYear = todateArr[2];
                var todateMonth = monthint(todateArr[1]);
                var todateDate = todateArr[0];

                var todate = new Date(todateYear, todateMonth, todateDate);

                if (todate > verifieddate) {
                    isToDateGreater = "Y";
                }
                return isToDateGreater;
            }

            function monthint(monthname)
            {
                var tmonthint = "";
                switch (monthname)
                {
                    case "Jan":
                        tmonthint = "0";
                        return tmonthint;
                        break;
                    case "Feb":
                        tmonthint = "1";
                        return tmonthint;
                        break;
                    case "Mar":
                        tmonthint = "2";
                        return tmonthint;
                        break;
                    case "Apr":
                        tmonthint = "3";
                        return tmonthint;
                        break;
                    case "May":
                        tmonthint = "4";
                        return tmonthint;
                        break;
                    case "Jun":
                        tmonthint = "5";
                        return tmonthint;
                        break;
                    case "Jul":
                        tmonthint = "6";
                        return tmonthint;
                        break;
                    case "Aug":
                        tmonthint = "7";
                        return tmonthint;
                        break;
                    case "Sep":
                        tmonthint = "8";
                        return tmonthint;
                        break;
                    case "Oct":
                        tmonthint = "9";
                        return tmonthint;
                        break;
                    case "Nov":
                        tmonthint = "10";
                        return tmonthint;
                        break;
                    case "Dec":
                        tmonthint = "11";
                        return tmonthint;
                        break;
                    default:
                        tmonthint = "0";
                }
            }
            
            function approveCheck() {
                if (confirm("Do you want to Approve?")) {
                    return true;
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form action="addServiceVerificationDataSBCorrection.htm" method="POST" commandName="command">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Service Verification
                    </div>
                    <div class="panel-body">
                        <form:hidden path="txtsvid" />
                        <form:hidden path="txtempid" />

                        <form:hidden id="hidAuthSpc" path="hidAuthSpc"/>
                        <form:hidden id="hidAuthOffice" path="hidAuthOffice"/>
                        <form:hidden id="hidAuthDept" path="hidAuthDept"/>
                        <form:hidden path="correctionid"/>
                        <form:hidden path="entrytypeSBCorrection"/>

                        <input type="hidden" id="curdate" value="${command.txtVerifiedOn}"/>
                        <c:if test="${not empty requestedSBLang}">
                            <div class="alert alert-danger" role="alert">
                                <c:out value="${requestedSBLang}"/>
                            </div>
                        </c:if>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="txtfdate">From Date <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                <div class="input-group date" id="txtfdate1">
                                    <form:input class="form-control txtDate" path="txtfdate" id="txtfdate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>     
                            </div>
                            <div class="col-lg-3">
                                <label for="sltftime"> From Time <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <form:select path="sltftime" class="form-control">
                                    <form:option value="">--Select One--</form:option>
                                    <form:option value="FN">FORE NOON</form:option>
                                    <form:option value="AN">AFTER NOON</form:option>
                                </form:select>                             
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="txttdate">To Date <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                <div class="input-group date" id="txttdate1">
                                    <form:input class="form-control txtDate" path="txttdate" id="txttdate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>     
                            </div>
                            <div class="col-lg-3">
                                <label for="sltttime"> To Time <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <form:select path="sltttime" class="form-control">
                                    <form:option value="">--Select One--</form:option>
                                    <form:option value="FN">FORE NOON</form:option>
                                    <form:option value="AN">AFTER NOON</form:option>
                                </form:select>                             
                            </div>
                        </div>            

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="txttdate">Verified On <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                <div class="input-group date txtDate">
                                    <form:input class="form-control txtDate" path="txtVerifiedOn" id="txtVerifiedOn" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>     
                            </div>
                            <div class="col-lg-3"></div>
                            <div class="col-lg-3"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="radpostingauthtype">Sanctioning Authority<span style="color: red"> *</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOI" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6">
                                <form:input class="form-control" path="authName" id="authName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-3">
                                <button type="button" class="btn btn-primary" onclick="openServiceVerificationModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="note">Note if any</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="notes" id="notes" cols="100" rows="4"/>                           
                            </div>
                        </div>    
                    </div>     
                    <div class="panel-footer">
                        <c:if test="${mode eq 'E'}">
                            <input type="submit" name="action" value="Save" class="btn btn-success" onclick="return saveCheck();"/>
                            <input type="submit" name="action" value="Submit" id="saveBtn" class="btn btn-default" onclick="return confirm('Are you sure to submit?');"/>
                        </c:if>
                        <c:if test="${mode eq 'D'}">
                            <input type="submit" name="action" value="Approve" class="btn btn-success" onclick="return approveCheck();"/>
                        </c:if>
                    </div>
                </div>

                <div id="serviceVerificationAuthorityModal" class="modal" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Sanctioning Authority</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="sltDept">Department</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="sltDept" id="sltDept" class="form-control" onchange="getDeptWiseOfficeList()">
                                            <option value="">--Select Department--</option>
                                            <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="sltOffice">Office</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="sltOffice" id="sltOffice" class="form-control" onchange="getOfficeWisePostList()">
                                            <option value="">--Select Office--</option>
                                            <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="sltSpc">Post</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="sltSpc" id="sltSpc" class="form-control" onchange="getPost();">
                                            <option value="">--Select Post--</option>
                                            <form:options items="${postlist}" itemValue="spc" itemLabel="postname"/>
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
                <div id="serviceVerificationOtherOrgModal" class="modal" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Sanctioning Authority</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="hidOthSpc">Service Verified By</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="hidOthSpc" id="hidOthSpc" class="form-control" onchange="getOtherOrgPost();">
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
            </div>
        </form:form>
    </body>
</html>
