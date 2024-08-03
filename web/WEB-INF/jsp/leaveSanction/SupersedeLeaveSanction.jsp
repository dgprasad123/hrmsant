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
            $(document).ready(function () {
                $('.dateField').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                var deptcode = $('#hidSanctioningDeptCode').val();
                if (deptcode == 'LM') {
                    $("#postdiv").hide();
                } else {
                    $("#postdiv").show();
                }
            });
            function getDeptWiseOfficeList() {
                var deptcode = $('#hidSanctioningDeptCode').val();
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                $('#hidSanctioningOffCode').empty().append('<option value="">--Select Office--</option>');
                if (deptcode == 'LM') {
                    $("#postdiv").hide();
                } else {
                    $("#postdiv").show();
                }
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#hidSanctioningOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList() {
                var deptcode = $('#hidSanctioningDeptCode').val();
                var offcode = $('#hidSanctioningOffCode').val();
                if (deptcode == 'LM') {
                    $('#sanctioningPostName').val($('#hidSanctioningOffCode option:selected').text());
                } else {
                    var url = 'getSanctioningSPCOfficeWiseListJSONF2.htm?deptcode=' + deptcode + '&offcode=' + offcode;
                    $('#sanctioningSpc').empty().append('<option value="">--Select Post--</option>');

                    $.getJSON(url, function (data) {
                        $.each(data, function (i, obj) {
                            $('#sanctioningSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        });
                    });
                }
            }

            function getPost() {
                $('#sanctioningPostName').val($('#sanctioningSpc option:selected').text());
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
                if ($('#txtNotOrdNo').val() == "") {
                    alert("Please enter Order No");
                    $('#txtNotOrdNo').focus();
                    return false;
                }
                if ($('#txtNotOrdDt').val() == "") {
                    alert("Please enter Order Date");
                    return false;
                }
                return true;
            }
            
            function openSanctioningModal(){
                var orgType = $('input[name="radsanctioningauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $("#leaveSanctionAuthorityModal").modal("show");
                } else if (orgType == 'GOI') {
                    $("#leaveSanctionOtherOrgModal").modal("show");
                }
            }
            
            function getOtherOrgPost() {
                $('#sanctioningPostName').val($('#hidSanctioningOthSpc option:selected').text());
                
                $('#hidSanctioningDeptCode').val('');
                $('#hidSanctioningOffCode').val('');
                $('#sanctioningSpc').val('');

                $("#leaveSanctionOtherOrgModal").modal("hide");
            }
        </script>
    </head>
    <body>
        <form:form action="SaveSupersedeLeaveSanction.htm" method="POST" commandName="leaveSanctionForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Supersede Employee Sanction of Leave
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="linkid" id="linkid"/>
                        <form:hidden path="hnotid" id="hnotid"/>
                        <form:hidden path="hleaveId" id="hleaveId"/>
                        <form:hidden path="ordType" id="ordType"/>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label for="chkNotSBPrint">
                                    Check Not to Print in Service Book
                                </label>                                
                            </div>
                            <div class="col-lg-3">
                                <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/>
                            </div>
                            <div class="col-lg-3"></div>
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
                                <label for="txtNotOrdDt">Notification Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control dateField" path="txtNotOrdDt" id="txtNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="sanctioningPostName">Sanctioning Authority</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radsanctioningauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radsanctioningauthtype" value="GOI" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>
                                
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-8">
                                <form:input class="form-control" path="sanctioningPostName" id="sanctioningPostName" readonly="true"/>
                            </div>
                            <div class="col-lg-2">
                                <button type="button" class="btn btn-primary" onclick="openSanctioningModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltLeaveType">Type of Leave</label>
                            </div>
                            <div class="col-lg-5">
                                <form:select path="sltLeaveType" id="sltLeaveType" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${leavetypelist}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-5"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>Period of Sanction</label>
                            </div>
                            <div class="col-lg-9"></div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(a) Date From<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control dateField" path="txtFrmDt" id="txtFrmDt" readonly="true"/>
                                    <span class="input-group-addon txtDate">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(b) Date To<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control dateField" path="txtToDt" id="txtToDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(c) Prefix From
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control dateField" path="txtPrefixFrom" id="txtPrefixFrom" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(d) Prefix To
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control dateField" path="txtPrefixTo" id="txtPrefixTo" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(e) Suffix From
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control dateField" path="txtSuffixFrom" id="txtSuffixFrom" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(f) Suffix To
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control dateField" path="txtSuffixTo" id="txtSuffixTo" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(g) Join Time From
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control dateField" path="txtJoinTimeFrom" id="txtJoinTimeFrom" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                &nbsp;&nbsp;(h) Join Time To
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control dateField" path="txtJoinTimeTo" id="txtJoinTimeTo" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="chkMedicalCertificate">If Medical Certificate Submitted</label>
                            </div>
                            <div class="col-lg-8">
                                <form:checkbox path="chkMedicalCertificate" value="Y"/>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="chkCommuted">If Commutted</label>
                            </div>
                            <div class="col-lg-8">
                                <form:checkbox path="chkCommuted" value="Y"/>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>    

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="chkUpdateCadreStatus">Update Cadre Status(JPR)</label>
                            </div>
                            <div class="col-lg-8">
                                <form:checkbox path="chkUpdateCadreStatus" value="LR"/>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="chkIfLongTermBasis">If Long Term Basis</label>
                            </div>
                            <div class="col-lg-8">
                                <form:checkbox path="chkIfLongTermBasis" value="Y"/>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Note(if any)</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnLeaveSanction" value="Save"  class="btn btn-default" onclick="return saveCheck();"/>
                        <c:if test="${not empty leaveSanctionForm.hleaveId}">
                            <input type="submit" name="btnLeaveSanction" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');"/>
                        </c:if>
                    </div>
                </div>
            </div>

            <div id="leaveSanctionAuthorityModal" class="modal" role="dialog">
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
                                    <label for="hidSanctioningDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidSanctioningDeptCode" id="hidSanctioningDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidSanctioningOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidSanctioningOffCode" id="hidSanctioningOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${sancOffList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="postdiv">
                                <div class="col-lg-2">
                                    <label for="sanctioningSpc">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sanctioningSpc" id="sanctioningSpc" class="form-control" onchange="getPost();">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${sancPostList}" itemValue="spc" itemLabel="postname"/>
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
                            
            <div id="leaveSanctionOtherOrgModal" class="modal" role="dialog">
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
                                    <label for="hidSanctioningOthSpc">Sanctioned By</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidSanctioningOthSpc" id="hidSanctioningOthSpc" class="form-control" onchange="getOtherOrgPost();">
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
        </form:form>
    </body>
</html>
