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
                $('#RemStages').hide();
            });

            function changeTransactionBacklog(data) {                
                var curdate = new Date();
                if (data == 'S') {
                    $('#rdTransactionCurrent').hide();                   
                    $('#txtWEFDt').datetimepicker({
                        format: 'D-MMM-YYYY',
                        useCurrent: false,
                        //minDate: new Date(2020, 0, 1),
                        maxDate: curdate,
                        ignoreReadonly: true
                    });
                }
            }
            function changeTransactionCurrent(data) {
                
                var maxIncrementedDate = '${maxIncrDate}';
                var maxIncrDate = maxIncrementedDate.split("-");
                var min_date = parseInt(maxIncrDate[2]);
                var min_month = parseInt(maxIncrDate[1]) - 1;
                var min_year = parseInt(maxIncrDate[0]) + 1;
                //var curdate = new Date();

                if (data == 'C') {
                    $('#rdTransactionBacklog').hide();
                    $('#txtWEFDt').datetimepicker({
                        format: 'D-MMM-YYYY',
                        useCurrent: false,
                        minDate: new Date(min_year, min_month, min_date),
                        //maxDate: curdate,
                        ignoreReadonly: true
                    });
                }
            }


            $('#hidOffCode').empty();
            $('#hidOffCode').append('<option value="">--Select Office--</option>');
            $('#hidSpc').empty();
            $('#hidSpc').append('<option value="">--Select Post--</option>');
            var deptcode = $('#deptCode').val();

            var url = "";
            if ($("input[name=rdTransaction]:checked").val() == "S") {
                url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
            } else {
                url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
            }

            $.getJSON(url, function(data) {
                $.each(data, function(i, obj) {
                    $('#hidOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                });
            });


            function getDeptWiseOfficeList() {
                var deptcode = $('#deptCode').val();

                $('#hidOffCode').empty();
                $('#hidPostedSpc').empty();

                var url = "";

                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                } else {
                    url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                }
                $('#hidOffCode').append('<option value="">------Select Office------</option>');
                $('#hidPostedSpc').append('<option value="">-------Select Post------</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                }).done(function() {
//                    $('#hidOffCode').chosen(); 
                    $("#hidOffCode").trigger("chosen:updated");
                    var type = "";
                    url = "getForeignBodyPostListJSON.htm?deptcode=" + deptcode;
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            if (type == "P") {
                                $('#hidPostedSpc').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                            }
                        });
                    });
                });
            }

            function getOfficeWisePostList() {
                var offcode = $('#hidOffCode').val();
                $('#hidSpc').empty();
                $('#hidSpc').append('<option value="">--Select Post--</option>');

                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }

            function getPost() {
                $('#sancAuthPostName').val($('#hidSpc option:selected').text());
            }
            function getRemunerationAmount() {
                var remYear = $('#contRemStages').val();
                //alert(remYear);
                $('#contRemAmount').empty();
                $('#contRemAmount').append('<option value="">--Select Amount--</option>');

                var url = 'getPaymatrixContListJSON.htm?remYear=' + remYear;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#contRemAmount').append('<option value="' + obj.amt + '">' + obj.amt + '</option>');
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

            function togglePayCommission() {
                var rdoPaycomm = $("input[name='rdoPaycomm']:checked").val();

                if (rdoPaycomm == '6') {
                    $('#div6pay').show();
                    $("#div6paygp").show();
                    $("#div7pay").hide();
                    $('#RemStages').hide();
                    $("#newBasic").show();
                    $("#newBasicAmt").show();
                    $("#incrAmt").show();
                    $("#incrLvl").show();
                    $("#incrType").show();

                } else if (rdoPaycomm == '7') {
                    $('#div6pay').hide();
                    $("#div6paygp").hide();
                    $("#div7pay").show();
                    $('#RemStages').hide();
                    $("#newBasic").show();
                    $("#newBasicAmt").show();
                    $("#incrAmt").show();
                    $("#incrLvl").show();
                    $("#incrType").show();
                } else if (rdoPaycomm == 'REM') {
                    $('#RemStages').show();
                    //$('#RemAmount').hide();
                    $("#div7pay").hide();
                    $("#div6pay").hide();
                    $("#div6paygp").hide();
                    $("#newBasic").hide();
                    $("#newBasicAmt").hide();
                    $("#incrAmt").hide();
                    $("#incrLvl").hide();
                    $("#incrType").hide();
                }
            }

            function saveCheck() {
                var payCommVal = $("input[name='rdoPaycomm']:checked").val();
                var rdoPaycomm = $("input[name='rdoPaycomm']:checked").val();
                var ordno = $('#txtSanctionOrderNo').val();
                var orddt = $('#txtSanctionOrderDt').val();
                var wefdt = $('#txtWEFDt').val();
                var weftime = $('#txtWEFTime').val();
                var basic = $('#txtNewBasic').val();
                var incramt = $('#txtIncrAmt').val();
                var remAmt = $("#contRemAmount").val();


                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                    return false;
                }

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
                if (wefdt == '') {
                    alert("Enter With Effect Date");
                    $('#saveBtn').show();
                    return false;
                }
                if (weftime == '') {
                    alert("Enter With Effect Time");
                    $('#saveBtn').show();
                    return false;
                }

                if (payCommVal == '7') {
                    if ($("#payLevel").val() == '') {
                        alert('Please select Pay Level.');
                        return false;
                    }
                    if ($("#payCell").val() == '') {
                        alert('Please select Pay Cell.');
                        return false;
                    }
                }
                if (basic == '' && remAmt == '') {
                    alert("Enter New Basic");
                    $('#saveBtn').show();
                    return false;
                }
                if (rdoPaycomm == '6' || rdoPaycomm == '7') {
                    if (incramt == '') {
                        alert("Enter Increment Amount");
                        $('#saveBtn').show();
                        return false;
                    }
                }
                if (payCommVal == '6') {
                    var gp = $('#txtGradePay').val();
                    if (gp == '') {
                        alert("Enter Grade Pay");
                        $('#saveBtn').show();
                        return false;
                    }
                }
                if (rdoPaycomm != 'REM') {
                    if ($("#incrementType").val() == "") {
                        alert("Please select Increment Type");
                        return false;
                    }
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

            function openSanctioningAuthorityModal() {
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
        <form:form action="saveIncrement.htm" method="POST" commandName="incrementForm">          
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Increment
                    </div>
                    <div class="panel-body">
                        <form:hidden path="hnotid"/>
                        <form:hidden path="hidIncrId"/>
                        <form:hidden path="hidPayId"/>                       
                        <c:if test="${not empty requestedSBLang}">
                            <div class="alert alert-danger" role="alert">
                                <c:out value="${requestedSBLang}"/>
                            </div>
                        </c:if>                           
                        <div class="row" style="margin-bottom: 7px;">
                            <c:if test="${not empty maxIncrDate}">
                                <h4 style="color:red; text-align: center;" id="msg">With effect date should not be less than one year from last Date of Increment</h4>
                            </c:if>

                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                            </div>
                            <div class="col-lg-2" style="text-align: left;">   
                                <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/> 
                            </div>
                            <div class="col-lg-2"></div>
                            <div class="col-lg-6"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="rdTransaction">Select type of Transaction</label>
                            </div>
                            <div class="col-lg-3" id="rdTransactionBacklog">   
                                <form:radiobutton path="rdTransaction" value="S"  onchange="changeTransactionBacklog('S');"/>Service Book Entry(Backlog)
                            </div>
                            <div class="col-lg-3" id="rdTransactionCurrent">
                                <form:radiobutton path="rdTransaction" value="C"  onchange="changeTransactionCurrent('C');"/>Current Transaction(will effect current Pay or Post)
                            </div>
                            <div class="col-lg-2"></div>
                        </div>
                        <br/>

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
                                <label for="txtWEFDt">With Effect From<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <div class="input-group txtDate" id="processDate">
                                    <form:input class="form-control" path="txtWEFDt" id="txtWEFDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtWEFTime"> Time<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="txtWEFTime" id="txtWEFTime" class="form-control">
                                    <form:option value="">-Select-</form:option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>                                
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="Commission">Select Pay Commission <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:radiobutton class="custom-control-input" id="rdoPaycomm1" path="rdoPaycomm" value="6" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" for="rdoPaycomm1">6th Pay</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton class="custom-control-input" id="rdoPaycomm2" path="rdoPaycomm" value="7" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" for="rdoPaycomm2">7th Pay</label>
                            </div>
                            <c:if test="${not empty remEmpCategory}">
                                <div class="col-lg-1">
                                    <form:radiobutton class="custom-control-input" id="rdoPaycomm2" path="rdoPaycomm" value="REM" onclick="togglePayCommission()"/>
                                    <label class="custom-control-label" for="rdoPaycomm2">Remuneration</label>
                                </div>
                            </c:if>
                        </div>

                        <div class="row" style="margin-bottom: 7px;" id="div6pay">
                            <div class="col-lg-2">
                                <label for="sltPayScale">Scale of Pay/Pay Band</label>
                            </div>
                            <div class="col-lg-5">
                                <form:select path="sltPayScale" id="sltPayScale" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${payscalelist}" itemValue="payscale" itemLabel="payscale"/>
                                </form:select>
                            </div>
                            <div class="col-lg-5">
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;"  id="div6paygp">
                            <div class="col-lg-2">
                                <label for="txtGradePay">Grade Pay<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtGradePay" id="txtGradePay" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-8">

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
                        <div class="row" style="margin-bottom: 7px;" id="RemStages">
                            <div class="col-lg-2">
                                <label for="RemStages">Select Remuneration Stages <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="contRemStages" id="contRemStages" class="form-control" onchange="getRemunerationAmount();" >
                                    <form:option value="">-Select Stages-</form:option>
                                    <form:option value="1">First Year</form:option>
                                    <form:option value="2">Second Year</form:option>
                                    <form:option value="3">Third Year</form:option>
                                    <form:option value="4">Fourth Year</form:option>
                                    <form:option value="5">Fifth Year</form:option>
                                    <form:option value="6">Sixth Year</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-2">
                                <label for="RemAmount">Select Remuneration Amount/ <br/> New Basic(in Rs.) <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="contRemAmount" id="contRemAmount" class="form-control">
                                    <%--<form:option value="">-Select Amount-</form:option>--%> 
                                    <form:options items="${contRemAmtList}" itemLabel="amt" itemValue="amt"/>
                                </form:select>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div id="incrAmt">
                                <div class="col-lg-2">
                                    <label for="txtIncrAmt">Increment Amount(in Rs.)<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">   
                                    <form:input class="form-control" path="txtIncrAmt" id="txtIncrAmt" onkeypress="return onlyIntegerRange(event)"/>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtP_pay"> Personal Pay(in Rs.)</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtP_pay" id="txtP_pay" onkeypress="return onlyIntegerRange(event)"/>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtOthPay">Other Pay(in Rs.)</label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtOthPay" id="txtOthPay" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtSPay"> Special Pay(in Rs.)</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtSPay" id="txtSPay" onkeypress="return onlyIntegerRange(event)"/>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2" id="newBasic">
                                <label for="txtNewBasic">New Basic(in Rs.)<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2" id="newBasicAmt">   
                                <form:input class="form-control" path="txtNewBasic" id="txtNewBasic" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtDescOth"> Desc. of Other Pay</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" path="txtDescOth" id="txtDescOth"/>                                
                            </div>
                        </div>                         

                        <div class="row" style="margin-bottom: 7px;" id="incrLvl">
                            <div class="col-lg-2">
                                <label for="incrementLvl">Increment Level</label>
                            </div>
                            <div class="col-lg-4">   
                                <form:select path="incrementLvl" id="incrementLvl" class="form-control">
                                    <form:option value="">-Select-</form:option>
                                    <form:option value="1">First Increment</form:option>
                                    <form:option value="2">Second Increment</form:option>
                                    <form:option value="3">Third Increment</form:option>
                                    <form:option value="4">Fourth Increment</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-4"></div>
                            <div class="col-lg-2"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;" id="incrType">
                            <div class="col-lg-2">
                                <label for="incrementType">Increment Type<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-4">   
                                <form:select path="incrementType" id="incrementType" class="form-control">
                                    <form:option value="">-Select-</form:option>
                                    <form:option value="A">Annual Increment</form:option>
                                    <form:option value="S">Stagnation Increment</form:option>
                                    <form:option value="D">Advance Increment</form:option>
                                    <form:option value="T">Antedated</form:option>
                                    <form:option value="P">Previous</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-4"></div>
                            <div class="col-lg-2"></div>
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
                        <%--<c:if test="${not empty isEmployeeRegular && isEmployeeRegular eq 'Y'}">--%>
                        <input type="submit" name="btnIncr" value="Save" id="saveBtn" class="btn btn-default" onclick="return saveCheck();"/>
                        <%--</c:if>--%>
                        <input type="submit" name="btnIncr" value="Back" id="saveBtn" class="btn btn-default"/>
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
                                    <form:select path="hidOffCode" id="hidOffCode" class="form-control" onchange="getOfficeWisePostList();" >
                                        <form:option value="">--Select Office--</form:option>
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
                                        <form:option value="">--Select Post--</form:option>
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
