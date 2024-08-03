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
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            function getGPCWiseSPCList() {

                var offcode = $('#hidOffCode').val();
                var postcode = $('#sltGenericPost').val();

                $('#sltSubstantivePost').empty();

                var url = "joiningGetGPCWiseSPCListJSON.htm?gpc=" + postcode + "&offcode=" + offcode;
                $('#sltSubstantivePost').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltSubstantivePost').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
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
                if ($('#txtBasic').val() == "") {
                    alert("Please enter Pay");
                    $('#txtBasic').focus();
                    return false;
                }
                if ($("[name=rdPayCommission]:checked").length == 0) {
                    alert("Please select Pay Commission");
                    return false;
                }
                if ($("#sltGenericPost").val() == "") {
                    alert("Please select Generic Post");
                    return false;
                }
                if ($("#sltSubstantivePost").val() == "") {
                    alert("Please select Substantive Post");
                    return false;
                }
                if ($("#sltGender").val() == "") {
                    alert("Please select Gender");
                    return false;
                }
                if ($('#txtMobile').val() == "") {
                    alert("Please enter Mobile");
                    $('#txtMobile').focus();
                    return false;
                }
                if ($('#txtAccNo').val() == "") {
                    alert("Please enter Account No");
                    $('#txtAccNo').focus();
                    return false;
                }
                if ($("#chkIfAssumed").val() == "") {
                    alert("Please select whether you want to Deduct Contribution");
                    $('#chkIfAssumed').focus();
                    return false;
                }
                if ($('#txtDOB').val() == "") {
                    alert("Please enter Date of Birth");
                    $('#txtDOB').focus();
                    return false;
                }
                if ($('#txtDOJ').val() == "") {
                    alert("Please enter Date of Joining in Government");
                    $('#txtDOJ').focus();
                    return false;
                }
                if ($('#sltPostGroup').val() == "") {
                    alert("Please select Post Group");
                    $('#sltPostGroup').focus();
                    return false;
                }
                return true;
            }

            function toggleGP() {
                if ($("[name=rdPayCommission]:checked").val() == 6) {
                    $("#txtGP").attr("readonly", false);
                } else if ($("[name=rdPayCommission]:checked").val() == 7) {
                    $("#txtGP").attr("readonly", true);
                    $("#txtGP").val('0');
                }
            }
        </script>
    </head>
    <body>
        <form:form action="saveRegularizationContractual.htm" method="POST" commandName="regularizeService">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Regularization of Contractual Employee (Employee Name - <strong>${regularizeService.empname}</strong>)
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="hregid" id="hregid"/>
                        <input type="hidden" id="hidOffCode" value="${selectedEmpOffice}"/>

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
                                    <form:input class="form-control txtDate" path="txtNotOrdDt" id="txtNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                            <div class="col-lg-4"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtBasic">Current Basic<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtBasic" id="txtBasic" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="rdPayCommission">Pay Commission<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="rdPayCommission" value="6" onchange="toggleGP();"/> 6&nbsp;
                                <form:radiobutton path="rdPayCommission" value="7" onchange="toggleGP();"/> 7
                            </div>
                            <div class="col-lg-1">
                                <label for="rdPayCommission">Grade Pay</label>&nbsp;
                            </div>
                            <div class="col-lg-1">
                                <form:input class="form-control" path="txtGP" id="txtGP" onkeypress="return onlyIntegerRange(event)" readonly="true"/>
                            </div>
                            <div class="col-lg-2"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltGenericPost">Generic Post<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="sltGenericPost" id="sltGenericPost" class="form-control" onchange="getGPCWiseSPCList();">
                                    <form:option value="">--Select Generic Post--</form:option>
                                    <form:options items="${postlist}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <label for="sltSubstantivePost">Substantive Post<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltSubstantivePost" id="sltSubstantivePost" class="form-control">
                                    <form:option value="">--Select Substantive Post--</form:option>
                                    <form:options items="${spclist}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-4"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltGender">Gender<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="sltGender" id="sltGender" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="F">FEMALE</form:option>
                                    <form:option value="M">MALE</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtMobile">Mobile<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtMobile" id="txtMobile" maxlength="10" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-4"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltAcctType">Account Type<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="sltAcctType" id="sltAcctType" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="PRAN">PRAN</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtAccNo">PRAN No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtAccNo" id="txtAccNo" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-4"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="chkIfAssumed">Do You want to deduct Contribution<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="chkIfAssumed" id="chkIfAssumed" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="Y">No</form:option>
                                    <form:option value="N">Yes</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-8"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtDOB">DOB<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control txtDate" path="txtDOB" id="txtDOB" readonly="true"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtDOJ">DOJ in Government<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control txtDate" path="txtDOJ" id="txtDOJ" readonly="true"/>
                            </div>
                            <div class="col-lg-4"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtAadharNo">AADHAR NO<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtAadharNo" id="txtAadharNo" maxlength="12"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="sltPostGroup">Post Group<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltPostGroup" id="sltPostGroup" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="A">A</form:option>
                                    <form:option value="B">B</form:option>
                                    <form:option value="C">C</form:option>
                                    <form:option value="D">D</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-4"></div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnRegularizationService" value="Save Regularization" class="btn btn-default" onclick="return saveCheck();"/>
                        <c:if test="${not empty msg}">
                            <span style="font-weight: bold; color: #00ee00;">
                                Data Saved.
                            </span>
                        </c:if>
                        <c:if test="${not empty regularizeService.hregid}">
                            <%--<input type="button" name="btnRegularizationService" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');"/>--%>
                        </c:if>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
