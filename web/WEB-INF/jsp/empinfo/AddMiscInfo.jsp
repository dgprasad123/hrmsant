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

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/servicehistory.js"></script>
        <script type="text/javascript">
            function getDeptWiseOfficeList(type) {

                $('#hidAuthOffCode').empty();
                $('#authSpc').empty();

                var url = 'getOfficeListJSON.htm?deptcode=' + $('#hidAuthDeptCode').val();
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList(type) {
                $('#authSpc').empty();
                url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + $('#hidAuthOffCode').val();
                $('#authSpc').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
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

            function getOtherOrgPost() {
                if ($('#hidOthSpc').val() != "") {
                    $('#authPostName').val($('#hidOthSpc option:selected').text());

                    $('#hidTempAuthPost').val($('#hidOthSpc').val());
                    $('#hidTempDeptCode').val($('#hidAuthDeptCode').val());
                    $('#hidTempAuthOffCode').val($('#hidAuthOffCode').val());
                }
            }

            function openMiscellaneousModel() {
                var orgType = $('input[name="radpostingauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $("#transferAuthorityModal").modal("show");
                } else if (orgType == 'GOI') {
                    $("#miscOtherOrgModal").modal("show");
                }
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
                if ($('#note').val() == "") {
                    alert("Please enter Note.");
                    $('#note').focus();
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <form:form action="saveMiscInfo.htm" method="post" commandName="MiscInfoForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <strong style="font-size:14pt;">Miscellaneous</strong>
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empId" id="empId"/>
                        <form:hidden path="notificationId" id="notificationId"/>
                        <form:hidden path="hidTempAuthOffCode" id="hidTempAuthOffCode"/>
                        <form:hidden path="hidTempAuthPost" id="hidTempAuthPost"/>
                        <form:hidden path="hidTempDeptCode" id="hidTempDeptCode"/>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                            </div>
                            <div class="col-lg-10">
                                <form:checkbox path="ifVisible" id="ifVisible" value="N" /> 
                                <label for="ifVisible" style="color:#0067C4">Not to print this transaction on Computerised Service Book</label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="orderNumber">Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="orderNumber" maxlength="50" id="orderNumber"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="orderDate">Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="orderDate" id="orderDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="authPostName">Details of Authority <span style="color: red">*</span></label>
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
                            <div class="col-lg-2"></div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="authPostName" id="authPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" onclick="openMiscellaneousModel();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>    

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Note<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" maxlength="1000" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>

                    </div>

                    <div class="panel-footer">
                        <button type="submit" name="submit" value="Save" class="btn btn-default" onclick="return saveCheck();">Save</button>
                        <input type="button" value="Cancel" class="btn btn-default" onclick="self.location = 'MiscInfoList.htm'" />
                    </div>
                </div>
            </div>

            <div id="transferAuthorityModal" class="modal" role="dialog">
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
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>                

            <div id="miscOtherOrgModal" class="modal" role="dialog">
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
                                    <label for="hidOthSpc">Authority</label>
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
        </form:form>
    </body>
    <script type="text/javascript">
        $(function() {
            $('#orderDate').datetimepicker({
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
