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
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            function getDeptWiseOfficeList() {
                var deptcode = $('#deptCode').val();
                $('#hidOffCode').empty();
                $('#hidOffCode').append('<option value="">--Select Office--</option>');
                $('#hidSpc').empty();
                $('#hidSpc').append('<option value="">--Select Post--</option>');
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                var url = "";
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                } else {
                    url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                }
                
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#hidOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList() {
                var offcode = $('#hidOffCode').val();
                $('#hidSpc').empty();
                $('#hidSpc').append('<option value="">--Select Post--</option>');

                var url = 'getOfficeWithSPCList.htm?offcode=' + offcode;
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#hidSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }

            function getPost() {
                $('#sancAuthPostName').val($('#hidSpc option:selected').text());
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
                var ordno = $('#txtSanctionOrderNo').val();
                var orddt = $('#txtSanctionOrderDt').val();
                
                $('#saveBtn').hide();
                if (ordno == '') {
                    alert("Enter Order No");
                    $('#saveBtn').show();
                    return false;
                }
                if (orddt == '') {
                    alert("Enter Order Date");
                    $('#saveBtn').show();
                    return false;
                }
                if (confirm("Do you want to Save?")) {
                    $('#showMessage').show();
                    return true;
                } else {
                    $('#saveBtn').show();
                    $('#showMessage').hide();
                    return false;
                }
            }
            
            function openSanctioningAuthorityModal(){
                var orgType = $('input[name="radsancauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $("#incrementSanctionAuthorityModal").modal("show");
                } else if (orgType == 'GOI') {
                    $("#incrementSanctionOtherOrgModal").modal("show");
                }
            }
            
            function getOtherOrgPost() {
                $('#sancAuthPostName').val($('#hidSancAuthorityOthSpc option:selected').text());
                
                $('#deptCode').val('');
                $('#hidOffCode').val('');
                $('#hidSpc').val('');

                $("#incrementSanctionOtherOrgModal").modal("hide");
            }
        </script>
    </head>
    <body>
        <form:form action="SaveCancelIncrementSanction.htm" method="POST" commandName="incrementForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Increment
                    </div>
                    <div class="panel-body">
                        <form:hidden path="hnotid"/>
                        <form:hidden path="linkid"/>
                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                            </div>
                            <div class="col-lg-1" style="text-align: left;">   
                                <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/> 
                            </div>
                            <div class="col-lg-3"></div>
                            <div class="col-lg-6"></div>
                        </div>
                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtSanctionOrderNo">Sanction Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtSanctionOrderNo" id="txtSanctionOrderNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtSanctionOrderDt"> Sanction Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group txtDate" id="processDate">
                                    <form:input class="form-control" path="txtSanctionOrderDt" id="txtSanctionOrderDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="sancAuthPostName">Sanctioning Authority</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radsancauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radsancauthtype" value="GOI" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>
                                
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-6">
                                <form:input class="form-control" path="sancAuthPostName" id="sancAuthPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" onclick="openSanctioningAuthorityModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtIncrNote">Note(if any)</label>
                            </div>
                            <div class="col-lg-6">
                                <form:textarea class="form-control" path="txtIncrNote" id="txtIncrNote"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnIncr" value="Save" id="saveBtn" class="btn btn-default" onclick="return saveCheck();"/>
                        <input type="submit" name="btnIncr" value="Back" class="btn btn-default"/>
                        <c:if test="${incrementForm.hnotid gt 0}">
                            <input type="submit" name="btnIncr" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');"/>
                        </c:if>
                        <span id="showMessage" style="color:red;font-weight:16px;display:none;">
                            Increment Data is being saved.
                        </span>
                    </div>
                </div>
            </div>

            <div id="incrementSanctionAuthorityModal" class="modal" role="dialog">
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
                                    <label for="deptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="deptCode" id="deptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <option value="">--Select Department--</option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
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
                                    <form:select path="hidOffCode" id="hidOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidSpc">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidSpc" id="hidSpc" class="form-control" onchange="getPost();">
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
                            
            <div id="incrementSanctionOtherOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Sanctioned Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="hidSancAuthorityOthSpc">Sanctioned Authority</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidSancAuthorityOthSpc" id="hidSancAuthorityOthSpc" class="form-control" onchange="getOtherOrgPost();">
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
