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
            $(document).ready(function() {
                $('#txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            function getDeptWiseOfficeList() {
                var deptcode = $('#hidPostedDeptCode').val();
                $('#hidPostedOffCode').empty();

                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;

                $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getDistrictAndDepartmentWiseOfficeList() {
                var deptcode;
                var distcode;
                deptcode = $('#hidPostedDeptCode').val();
                distcode = $('#hidPostedDistCode').val();
                $('#hidPostedOffCode').empty();
                $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                var url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }
            
            function removeDepedentDropdown() {
                $('#hidPostedDistCode').val('');
                
                $('#hidPostedOffCode').empty();
                $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
            }
            
            function getSelectedOfficeName() {
                $('#hidTempPostedOffCode').val($('#hidPostedOffCode option:selected').text());
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
                var result = confirm("Are you sure to Transfer employee?");

                if (result) {
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
                } else {
                    return false;
                }

                $('#btnSave').hide();
                return true;
            }
        </script>
    </head>
    <body>
        <form:form action="saveTransferContractual.htm" method="post" commandName="transferForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Transfer
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="transferId" id="transferId"/>

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
                                <label for="hidTempPostedOffCode">Transfer to Office<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="hidTempPostedOffCode" id="hidTempPostedOffCode" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#transferPostingModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="postedPostName">Transfer from POST</label>
                            </div>
                            <div class="col-lg-9">
                                <form:hidden path="postedPostName" value="${postnomenclature}"/>
                                <c:out value="${postnomenclature}"/>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${empty transferForm.transferId}">
                            <input type="submit" name="btnTransferCntr" value="Transfer" class="btn btn-default" id="btnSave" onclick="return saveCheck();"/>
                        </c:if>
                        <c:if test="${not empty transferForm.transferId}">
                            <input type="submit" name="btnTransferCntr" value="Update Transfer" class="btn btn-default" id="btnSave" onclick="return saveCheck();"/>
                            <input type="submit" name="btnTransferCntr" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');"/>
                        </c:if>
                        <input type="submit" name="btnTransferCntr" value="Back" class="btn btn-default"/>
                    </div>
                </div>
            </div>

            <div id="transferPostingModal" class="modal" role="dialog">
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
                                    <label for="hidPostedDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDeptCode" id="hidPostedDeptCode" class="form-control" onchange="removeDepedentDropdown();">
                                        <form:option value="">Select Department</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDistCode" id="hidPostedDistCode" class="form-control" onchange="getDistrictAndDepartmentWiseOfficeList();">
                                        <option value="">--Select District--</option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOffCode" id="hidPostedOffCode" class="form-control" onchange="getSelectedOfficeName();">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
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
