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
                togglePayCommission();
            });

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
            function togglePayCommission() {
                var rdPayCommission = $("input[name='rdPayCommission']:checked").val();


                if (rdPayCommission == '6' || ($('#hidpaycomm').val() == '6')) {
                    $('#div6pay').show();
                    $("#div7pay").hide();
                    $("#sltPayScale").val('');

                } else {
                    $('#div6pay').hide();
                    $("#div7pay").show();
                    // $("#sltPayScale").val('');

                }
            }
            //window.onload=togglePayCommission();

            function saveCheck() {
                var rdPayCommission = $("input[name='rdPayCommission']:checked").val();
                var frmDt = $('#frmDate').val().replace('-', ' ').replace('-', ' ');
                var toDt = $('#toDate').val().replace('-', ' ').replace('-', ' ');
                
//                if(frmDt == '')
//                {
//                    alert("Please enter from Date.");
//                    return false;
//                }
//                if(toDt == '')
//                {
//                    alert("Please enter To Date.");
//                    return false;
//                }                
                if (Date.parse(frmDt) > Date.parse(toDt)) {
                    alert("From Date must be less than Equal To To Date!");
                    $('#frmDate').focus();
                    $('#toDate').focus();
                    return false;
                }
                if ($('#txtNotOrdNo').val() == "") {
                    alert("Please enter Order No");
                    $('#txtNotOrdNo').focus();
                    return false;
                }
                if ($('#txtNotOrdDt').val() == "") {
                    alert("Please enter Order Date");
                    $('#txtNotOrdDt').focus();
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
                if ($('#sltPostGroup').val() == "") {
                    alert("Please select Post Group");
                    $('#sltPostGroup').focus();
                    return false;
                }
                if (rdPayCommission == '6') {
                    var gp = $('#txtGP').val();
                    if (gp == '') {
                        alert("Enter Grade Pay");
                        gp.focus();
                        return false;
                    }
                    if ($('#sltPayScale').val() == '') {
                        alert("Enter Scale Of Pay");
                        $('#sltPayScale').focus();
                        return false;
                    }
                }

                if (rdPayCommission == '7') {
                    if ($("#payLevel").val() == '') {
                        alert('Please select Pay Level.');
                        $("#payLevel").focus();
                        return false;
                    }
                    if ($("#payCell").val() == '') {
                        alert('Please select Pay Cell.');
                        $("#payCell").focus();
                        return false;
                    }
                }
                if ($('#frmDate').val() == '') {
                    alert("Please select From Date");
                    $('#frmDate').focus();
                    return false;
                }
                if ($('#toDate').val() == '') {
                    alert("Please select To Date");
                    $('#toDate').focus();
                    return false;
                }
                if ($('#sltContractualType').val() == '') {
                    alert("Please select Contractual Type");
                    return false;
                }
                if ($('#txtRegularizationDate').val() == '') {
                    alert("Please enter Regularization date");
                    $('#txtRegularizationDate').focus();
                    return false;
                }
                return true;
            }

            <%-- function toggleGP() {
                 if ($("[name=rdPayCommission]:checked").val() == 6) {
                     $("#txtGP").attr("readonly", false);
                 } else if ($("[name=rdPayCommission]:checked").val() == 7) {
                     $("#txtGP").attr("readonly", true);
                     $("#txtGP").val('0');
                 }
             }--%>
        </script>
    </head>
    <body>
        <form:form action="saveRegularizationContractual6Years.htm" method="POST" commandName="regularizeService">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Regularization of Contractual(6 Years) Employee (Employee Name - <strong>${regularizeService.empname}</strong>, HRMS ID - <strong>${regularizeService.empid}</strong>)
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="hregid" id="hregid"/>
                        <form:hidden path="hidAadhar"/>
                        <form:hidden path="hidSpc" />
                        <form:hidden path="hidGpc" />
                        <form:hidden path="txtDOB" />
                        <form:hidden path="txtDOJ" />

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
                        <br/>                           

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
                                <label>Details of Pay</label>
                            </div>
                            <div class="col-lg-9">

                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="Commission">Select Pay Commission <span style="color: red">*</span></label>
                            </div>

                            <div class="col-lg-2">   
                                <form:radiobutton class="custom-control-input" id="rdPayCommission" path="rdPayCommission" value="6" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" for="defaultUnchecked">6th Pay</label>

                            </div>
                            <div class="col-lg-6">
                                <form:radiobutton class="custom-control-input" id="rdPayCommission" path="rdPayCommission" value="7" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" for="defaultUnchecked">7th Pay</label>

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;"  id="div7pay">
                            <div class="col-lg-2">
                                <label for="payLevel">Pay Level<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   

                                <form:select path="payLevel" id="payLevel" class="form-control">
                                    <form:option value="">-Select-</form:option>
                                    <form:options items="${paylevelList}" itemLabel="label" itemValue="value"/>
                                </form:select>   

                            </div>
                            <div class="col-lg-2">
                                <label for="payCell"> Pay Cell <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="payCell" id="payCell" class="form-control">
                                    <form:option value="">-Select-</form:option>
                                    <form:options items="${payCellList}" itemLabel="label" itemValue="value"/>
                                </form:select>                                
                            </div>
                        </div> 
                        <div class="row" style="margin-bottom: 7px;" id="div6pay">
                            <div class="col-lg-2">
                                <label for="div6pay"> 
                                    Revised Scale of Pay/Pay Band</label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltPayScale" id="sltPayScale" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${payscalelist}" itemValue="payscale" itemLabel="payscale"/>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtGP">Grade Pay</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtGP" id="txtGP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtBasic">Current Basic<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtBasic" id="txtBasic" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="frmDate">Contractual Period From<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control txtDate" path="frmDate" id="frmDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                            <div class="col-lg-2">
                                <label for="toDate">Contractual Period To<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control txtDate" path="toDate" id="toDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
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
                                <label for="txtAadharNo">AADHAR NO<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtAadharNo" id="txtAadharNo" maxlength="12"  disabled="true"/>
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
                            
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltContractualType">Contractual Type<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="sltContractualType" id="sltContractualType" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="G">GENERAL ADMINISTRATION</form:option>
                                    <form:option value="E">EDUCATION</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt">Regularization Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control txtDate" path="txtRegularizationDate" id="txtRegularizationDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                            <div class="col-lg-4"></div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${empty msg}">
                            <input type="submit" name="btnRegularizationService" value="Save Regularization" class="btn btn-default" onclick="return saveCheck();"/>
                        </c:if>
                        <c:if test="${not empty msg}">
                            <input type="submit" name="btnRegularizationService" value="Save Regularization" class="btn btn-default" onclick="return saveCheck();"  disabled="true" />
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
