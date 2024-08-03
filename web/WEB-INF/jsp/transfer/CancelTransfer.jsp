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
        <script type="text/javascript">

            function getDistWiseOfficeList() {
                var deptcode;
                var distcode;
                deptcode = $('#hidAuthDeptCode').val();
                distcode = $('#hidAuthDistCode').val();
                $('#hidAuthOffCode').empty();
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');

                var url = 'getOfficeListDistrictAndDepartmentForBacklogEntryJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWiseGenericPostList() {
                var offcode = $('#hidAuthOffCode').val();
                $('#genericpostAuth').empty();
                $('#genericpostAuth').append('<option value="">--Select Generic Post--</option>');
                var url = "getAuthorityPostListJSON.htm?offcode=" + offcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#genericpostAuth').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function openSanctioningAuthorityModal() {
                var orgType = $('input[name="radsanctioningauthtype"]:checked').val();
                if (orgType == 'GOO') {
                    $('#transferAuthorityModal').modal("show");
                } else if (orgType == 'GOI') {
                    $("#transferAuthorityOtherOrgModal").modal("show");
                }
            }

            function getGenericPostWiseSPCList() {
                var offcode = $('#hidAuthOffCode').val();
                var gpc = $('#genericpostAuth').val();
                var url = "getAuthoritySubstantivePostListJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                $('#authSpc').empty();
                $('#authSpc').append('<option value="">--Select Substantive Post--</option>');

                //var url = "getVacantPostListJSON.htm?postcode="+gpc+"&offcode="+offcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                    });
                });
            }

            function getPost(type) {
                $('#authPostName').val($('#authSpc option:selected').text());
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
                if ($('#postedspn').val() == "") {
                    alert("Please select Details of Posting");
                    return false;
                }
                $('#btnSave').hide();
                return true;
            }

            function getOtherOrgPost() {
                $('#authPostName').val($('#hidSanctioningOthSpc option:selected').text());
                $('#hidAuthDeptCode').val('');
                $('#hidAuthDistCode').val('');
                $('#hidAuthOffCode').val('');
                $('#genericpostAuth').val('');
                $('#authSpc').val('');
                $("#transferAuthorityModal").modal("hide");
            }
        </script>
    </head>
    <body>
        <form:form action="SaveCancelTransfer.htm" method="post" commandName="transferForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Transfer
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>                        
                        <form:hidden path="hnotid" id="hnotid"/>
                        <form:hidden path="linkid" id="linkid"/>                        

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
                            <div class="col-lg-3">
                                <label for="authPostName">Sanctioning Authority</label>
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
                            <div class="col-lg-9">
                                <form:input class="form-control" path="authPostName" id="authPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <%--<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#transferAuthorityModal">--%>
                                <button type="button" class="btn btn-primary" onclick="openSanctioningAuthorityModal();">
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
                        <button type="submit" name="submit" value="Save" class="btn btn-default" id="btnSave" onclick="return saveCheck();">Save Transfer</button>
                        <c:if test="${not empty transferForm.transferId}">
                            <%--<button type="submit" name="submit" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');">Delete</button>--%>
                        </c:if>
                        <button type="submit" name="submit" value="Back" class="btn btn-default">Back</button>
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
                                    <label for="hidAuthDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="removeDepedentDropdown('A');">
                                        <option value="">--Select Department--</option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidAuthDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthDistCode" id="hidAuthDistCode" class="form-control" onchange="getDistWiseOfficeList('A');">
                                        <option value="">--Select District--</option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidAuthOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWiseGenericPostList('A');">
                                        <option value="">--Select Office--</option>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="genericpostAuth">Generic Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="genericpostAuth" id="genericpostAuth" class="form-control" onchange="getGenericPostWiseSPCList('A');">
                                        <option value="">--Select Generic Post--</option>
                                        <form:options items="${gpcauthlist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authSpc">Substantive Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authSpc" id="authSpc" class="form-control" onchange="getPost('A');">
                                        <option value="">--Select Substantive Post--</option>
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

            <div id="transferAuthorityOtherOrgModal" class="modal" role="dialog">
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
                                    <form:select path="hidSanctioningOthSpc" id="hidSanctioningOthSpc" class="form-control" onchange="getOtherOrgPost('S');">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgOfflist}" itemValue="value" itemLabel="label"/>
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
    <script type="text/javascript">
        $(function() {
            $('#txtNotOrdDt').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
            $('#txtWEFDt').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
        });
    </script>
</html>
