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
                $('.date').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function getDeptWiseOfficeList() {
                var deptcode = $('#hidNotifyingDeptCode').val();
                $('#hidNotifyingOffCode').empty();
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;

                $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList() {
                var offcode = $('#hidNotifyingOffCode').val();
                $('#notifyingSpc').empty();
                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#notifyingSpc').append('<option value="">--Select Post--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#notifyingSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }

            function getPost() {
                $('#notifyingPostName').val($('#notifyingSpc option:selected').text());
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
            }

            function openNotifyingAuthorityModal() {
                var orgType = $('input[name="radnotifyingauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $("#payFixationAuthorityModal").modal("show");
                } else if (orgType == 'GOI') {
                    $("#payFixationOtherOrgModal").modal("show");
                }
            }

            function getOtherOrgPost() {
                $('#notifyingPostName').val($('#hidNotifyingOthSpc option:selected').text());

                $('#hidNotifyingDeptCode').val('');
                $('#hidNotifyingOffCode').val('');
                $('#notifyingSpc').val('');

                $("#payFixationOtherOrgModal").modal("hide");
            }

        </script>
        <style type="text/css">
            .col-md-1 {
                width: 10%;
            }
            .col-md-2{
                width: 13%;
            }
            .slno{
                width: 3% !important;
            }
        </style>
    </head>
    <body>
        <form:form action="SaveCancelPayFixation.htm" method="POST" commandName="payFixation">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <c:if test="${payFixation.notType == 'PAYFIXATION'}">
                            Employee Pay Fixation
                        </c:if>
                        <c:if test="${payFixation.notType == 'PAYREVISION'}">
                            Employee Pay Revision
                        </c:if>
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="linkid" id="linkid"/>
                        <form:hidden path="notId" id="notId"/>
                        <form:hidden path="notType" id="notType"/>
                        <form:hidden path="payid" id="payid"/>
                        <form:hidden path="payRecordId" id="payRecordId"/>

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
                                <div class="input-group date">
                                    <form:input class="form-control" path="txtNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="notifyingPostName">Notifying Authority</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOI" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="notifyingPostName" id="notifyingPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" onclick="openNotifyingAuthorityModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
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
                        <a href="PayFixationList.htm?notType=${payFixation.notType}" ><input type="button" name="btnPayFixation" value="Back" class="btn btn-primary"/> </a>
                        <input type="submit" name="btnPayFixation" value="Save" class="btn btn-success" onclick="return saveCheck()"/>

                        <c:if test="${not empty payFixation.payid}">
                            <input type="submit" name="btnPayFixation" value="Delete" class="btn btn-danger" onclick="return confirm('Are you sure to delete?');"/>
                        </c:if>
                    </div>
                </div>

                <div id="payFixationAuthorityModal" class="modal" role="dialog">
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
                                        <form:select path="hidNotifyingDeptCode" id="hidNotifyingDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                            <form:option value="">--Select--</form:option>
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
                                        <form:select path="hidNotifyingOffCode" id="hidNotifyingOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                            <form:option value="">--Select--</form:option>
                                            <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="notifyingSpc">Post</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="notifyingSpc" id="notifyingSpc" class="form-control" onchange="getPost();">
                                            <form:option value="">--Select--</form:option>
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
                <div id="payFixationOtherOrgModal" class="modal" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Notifying Authority</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-3">
                                        <label for="hidNotifyingOthSpc">Notified By</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="hidNotifyingOthSpc" id="hidNotifyingOthSpc" class="form-control" onchange="getOtherOrgPost();">
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
