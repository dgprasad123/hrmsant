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
                $("input[type='checkbox'][name='chkAddlCharge']").click(function(){
                    if($(this).prop("checked") == true){
                        $("#rlvFrmPost").hide();
                        $("#sltRlvPost").val('');
                        $("#addlChargeRlvFrmPost").show();
                    }else if($(this).prop("checked") == false){
                        $("#rlvFrmPost").show();
                        $("#addlChargeRlvFrmPost").hide();
                        $("#sltAddlCharge").val('');
                    }
                });
            });

            function getDistrictAndDepartmentWiseOfficeList() {
                var deptcode;
                var distcode;
                if ($('#authHidDeptCode').val() == '') {
                    alert('Please select Department.');
                    $('#authHidDeptCode').focus();
                    return false;
                }
                if ($('#authHidDistCode').val() == '') {
                    alert('Please select District.');
                    $('#authHidDistCode').focus();
                    return false;
                }
                deptcode = $('#authHidDeptCode').val();
                distcode = $('#authHidDistCode').val();
                $('#authHidOffCode').empty();
                $('#authHidOffCode').append('<option value="">--Select Office--</option>');

                var url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#authHidOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWiseGenericPostList() {
                var offcode;
                var url;
                offcode = $('#authHidOffCode').val();
                $('#authHidGPC').empty();
                $('#authHidGPC').append('<option value="">--Select Generic Post--</option>');
                url = "getAuthorityPostListJSON.htm?offcode=" + offcode;

                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#authHidGPC').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getGenericPostWiseSPCList() {
                var offcode;
                var gpc;
                var url;
                offcode = $('#authHidOffCode').val();
                gpc = $('#authHidGPC').val();
                url = "getAuthoritySubstantivePostListJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                $('#authSpc').empty();
                $('#authSpc').append('<option value="">--Select Substantive Post--</option>');

                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                    });
                });
            }

            function removeDepedentDropdown() {
                $('#authHidDistCode').val('');

                $('#authHidOffCode').empty();
                $('#authHidOffCode').append('<option value="">--Select Office--</option>');

                $('#authHidGPC').empty();
                $('#authHidGPC').append('<option value="">--Select Generic Post--</option>');

                $('#authSpc').empty();
                $('#authSpc').append('<option value="">--Select Substantive Post--</option>');
            }

            function getPost() {
                $('#authPostName').val($('#authHidGPC option:selected').text());
            }

            function saveCheck() {

                if ($('#txtRlvOrdNo').val() == '') {
                    alert("Please Enter Relieve No");
                    $('#txtRlvOrdNo').focus();
                    return false;
                }
                if ($('#txtRlvOrdDt').val() == '') {
                    alert("Please Enter Relieve Order Date");
                    return false;
                }
                if ($('#txtRlvDt').val() == '') {
                    alert("Please Enter Relieve Date");
                    return false;
                }
                if ($('#hidNotType').val() != 'RETIREMENT') {
                    if ($('#txtJoinDt').val() == '') {
                        alert("Please Enter Joining Date");
                        return false;
                    }
                }
                if ($("input[name=rdRqRl]:checked").length == 0) {
                    alert("Please select either Relinquished or Relieved");
                    return false;
                }
                var transtatus = $('#transactionStatus').val();
                if (transtatus == "S") {
                    if ($('#authPostName').val() == "") {
                        alert("Please select Authority");
                        return false;
                    }
                } else if (transtatus == "C") {
                    if ($('#sltRlvPost').val() == "") {
                        alert("Please select Post from which employee will be relieved");
                        return false;
                    }
                }
                return true;
            }
            function validateRelieve() {
                if (saveCheck()) {
                    $('#btnSave').hide();
                } else {
                    return false;
                }
            }
            function hideDropDown() {
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    $('#rlvFrmPost').hide();
                    $('#auth').show();
                    $('#sltRlvPost').val('');
                } else if ($("input[name=rdTransaction]:checked").val() == "C") {
                    $('#rlvFrmPost').show();
                    $('#auth').hide();
                    $('#authPostName').val('');
                }
            }
            
            
        </script>
    </head>
    <body>
        <form:form action="saveRelieve.htm" method="post" commandName="rlvForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Relieve&nbsp;
                        <c:if test="${not empty errMsg}">
                            <span style="color: #F00000;">
                                <c:out value="${errMsg}"/>
                            </span>
                        </c:if>
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empId" id="empId"/>
                        <form:hidden path="hidNotId" id="hidNotId"/>
                        <form:hidden path="doe" id="doe"/>
                        <form:hidden path="rlvId" id="rlvId"/>
                        <form:hidden path="hidNextOfficeCode" id="hidNextOfficeCode"/>
                        <form:hidden path="transactionStatus" id="transactionStatus"/>

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
                                <label for="rdTransaction">Type of Transaction</label>
                            </div>

                            <c:if test="${not empty rlvForm.transactionStatus && rlvForm.transactionStatus eq 'S'}">
                                <div class="col-lg-6">
                                    <%--<form:radiobutton path="rdTransaction" value="S" onclick="hideDropDown();"/> Service Book Entry(Backlog)--%>
                                    <span style="font-size: 15px; color: red; font-weight: bold;">
                                        Service Book Entry(Backlog) as selected in <c:out value="${rlvForm.hidNotType}"/> module entry.
                                    </span>
                                </div>
                            </c:if>

                            <c:if test="${not empty rlvForm.transactionStatus && rlvForm.transactionStatus eq 'C'}">
                                <div class="col-lg-6">
                                    <%--<form:radiobutton path="rdTransaction" value="C" onclick="hideDropDown();"/> Current Transaction(will effect current Pay or Post)--%>
                                    <span style="font-size: 15px; color: red; font-weight: bold;">
                                        Current Transaction(will effect current Pay or Post) as selected in <c:out value="${rlvForm.hidNotType}"/> module entry.
                                    </span>
                                </div>
                            </c:if>

                            <div class="col-lg-2"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-6">
                                <label>Notification Order Details</label>
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
                                (a) Type
                            </div>
                            <div class="col-lg-6">
                                <form:input class="form-control" path="hidNotType" id="hidNotType" readonly="true"/>
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
                                <form:input class="form-control" path="ordNo" id="ordNo" readonly="true"/>
                            </div>
                            <div class="col-lg-2">
                                (c) Order Date
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="ordDt" id="ordDt" readonly="true"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (d) Department Name
                            </div>
                            <div class="col-lg-10">
                                <form:input class="form-control" path="deptname" id="deptname" readonly="true"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (e) Office Name
                            </div>
                            <div class="col-lg-10">
                                <form:input class="form-control" path="offname" id="offname" readonly="true"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (f) Authority
                            </div>
                            <div class="col-lg-10">
                                <form:input class="form-control" path="postname" id="postname" readonly="true"/>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (g) Note
                            </div>
                            <div class="col-lg-10">
                                <form:textarea class="form-control" path="note" id="note" readonly="true"/>
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
                            <div class="col-lg-3">
                                (a) Relieve Report/Letter No.<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <form:input path="txtRlvOrdNo" id="txtRlvOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                (b) Date<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input path="txtRlvOrdDt" id="txtRlvOrdDt" class="txtDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                (c) Relieved On<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input path="txtRlvDt" id="txtRlvDt" class="txtDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                (d) Time<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltRlvTime" id="sltRlvTime" class="form-control">
                                    <option value="">-Select-</option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                (e) Due Date of Joining<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input path="txtJoinDt" id="txtJoinDt" class="txtDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                (f) Time<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltJoinTime" id="sltJoinTime" class="form-control">
                                    <option value="">-Select-</option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">

                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="rdRqRl" value="Y"/> Relinquished
                            </div>
                            <div class="col-lg-2">

                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="rdRqRl" value="N"/> Relieved
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                Is Additional Charge
                            </div>
                            <div class="col-lg-2">
                                <input type="checkbox" name="chkAddlCharge" id="chkAddlCharge"/>
                            </div>
                            <div class="col-lg-8"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;" id="rlvFrmPost">
                            <div class="col-lg-2">
                                (g) Relieved From<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltRlvPost" id="sltRlvPost" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${relievedPostlist}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;display: none;" id="addlChargeRlvFrmPost">
                            <div class="col-lg-2">
                                (g) Relieved From Additional Charges
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltAddlCharge" id="sltAddlCharge" class="form-control">
                                    <form:options items="${addlChargeList}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-2"></div>
                        </div>
                        <%--<c:if test="${not empty rlvForm.transactionStatus && rlvForm.transactionStatus eq 'S'}">
                            <div class="row" style="margin-bottom: 7px;" id="auth">
                                <div class="col-lg-2">
                                    Relieved From:
                                </div>
                                <div class="col-lg-9">
                                    <form:input class="form-control" path="authPostName" id="authPostName"/>                           
                                </div>
                                <div class="col-lg-1">
                                    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#relieveAuthorityModal">
                                        <span class="glyphicon glyphicon-search"></span> Search
                                    </button>
                                </div>
                            </div>
                        </c:if>--%>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                Note(if any)
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="txtRelieveNote" id="txtRelieveNote"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">                        
                        <input type="submit" name="action" value="Back" class="btn btn-warning  btn-md"/>
                        <input type="submit" name="action" value="Save Relieve" id="btnSave" class="btn btn-default" onclick="return validateRelieve();"/>                        
                    </div>
                </div>
            </div>

            <div id="relieveAuthorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authHidDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authHidDeptCode" id="authHidDeptCode" class="form-control" onchange="removeDepedentDropdown();">
                                        <option value="">--Select Department--</option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authHidDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authHidDistCode" id="authHidDistCode" class="form-control" onchange="getDistrictAndDepartmentWiseOfficeList();">
                                        <option value="">--Select District--</option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authHidOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authHidOffCode" id="authHidOffCode" class="form-control" onchange="getOfficeWiseGenericPostList();">
                                        <option value="">--Select Office--</option>
                                        <form:options items="${authOfficeList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authHidGPC">Generic Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authHidGPC" id="authHidGPC" class="form-control" onchange="getPost();">
                                        <option value="">--Select Generic Post--</option>
                                        <form:options items="${authGPCList}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <%--<div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authSpc">Substantive Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authSpc" id="authSpc" class="form-control" onchange="getPost();">
                                        <option value="">--Select Substantive Post--</option>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>--%>
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
        $(function () {
            $('.txtDate').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });
        });
    </script>
</html>
