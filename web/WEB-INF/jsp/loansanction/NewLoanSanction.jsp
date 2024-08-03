<%-- 
    Document   : NewLoanSanction
    Created on : Oct 26, 2017, 12:54:11 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script language="javascript" type="text/javascript">
            $(document).ready(function() {
                $('#btid').hide();
                $('#btidLabel').show();
                $('#sltloan').change(function() {
                    var loanTp = $(this).val();
                    if (loanTp == "NPSL") {
                        $("#governmentContr").show();
                    } else if (loanTp == "OR" || loanTp == "OVDL" || loanTp == "PA") {
                        $('#btid').show();
                        $('#btidLabel').hide();
                        $("#governmentContr").hide();
                        $("#sltGovernmentContr").val('');
                    } else {
                        $('#btidLabel').show();
                        $('#btid').hide();
                        $("#governmentContr").hide();
                        $("#sltGovernmentContr").val('');
                    }

                    /*if (loanTp == "BICA" || loanTp == "CMPA" || loanTp == "MOPA" || loanTp == "MCA" || loanTp == "VE") {
                     $('#nowDeduct').empty();
                     $('#nowDeduct').append('<option value="P">PRINCIPAL</option>');
                     } else {
                     $('#nowDeduct').empty();
                     $('#nowDeduct').append('<option value="P">PRINCIPAL</option>');
                     $('#nowDeduct').append('<option value="I">INTEREST</option>');
                     }*/

                });

                if ($('#sltloan').val() == "OR" || $('#sltloan').val() == "OVDL" || $('#sltloan').val() == "PA") {
                    $('#btid').show();
                    $('#btidLabel').hide();
                }
                if ($('#sltloan').val() == "NPSL") {
                    $("#governmentContr").show();
                }
                /*if ($("#agcalculationid").val() == "") {
                 if ($('#sltloan').val() == "BICA" || $('#sltloan').val() == "CMPA" || $('#sltloan').val() == "MOPA" || $('#sltloan').val() == "MCA" || $('#sltloan').val() == "VE") {
                 $('#nowDeduct').empty();
                 $('#nowDeduct').append('<option value="P">PRINCIPAL</option>');
                 } else {
                 $('#nowDeduct').empty();
                 $('#nowDeduct').append('<option value="P">PRINCIPAL</option>');
                 $('#nowDeduct').append('<option value="I">INTEREST</option>');
                 }
                 }*/
                $('#btid').on("paste", function(e) {
                    e.preventDefault();
                });
                
                var myfile = "";
                $(document).on("change","#gcfile", function() {                
                    myfile = $(this).val();
                    var ext = myfile.split('.').pop();
                    if (ext != "pdf") {
                        alert("Invalid File Type");
                        $(this).val('');
                        return false;
                    } else {
                        //alert(this.files[0].size);
                        var fsize = this.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 3072) {
                            alert("File too Big, please select a file less than 3mb");
                            $(this).val('');
                            return false;
                        }
                    }
                });
            });
            function getBranchList(me) {
                $('option', $('#branch')).not(':eq(0)').remove();
                $.ajax({
                    type: "POST",
                    url: "getBranchListJSON.htm?bankCode=" + $(me).val(),
                    success: function(data) {
                        $.each(data, function(i, obj)
                        {
                            $('#branch').append($('<option>', {
                                value: obj.branchcode,
                                text: obj.branchname
                            }));

                        });
                    }
                });
            }
            function validateform() {
                if ($("#orderno").val() == "") {
                    alert("Order No can not blank");
                    $("#orderno").focus();
                    return false;
                }
                if ($("#orderdate").val() == "") {
                    alert("Order Date can not blank");
                    $("#orderdate").focus();
                    return false;
                }
                if ($("#sltloan").val() == "") {
                    alert("Please Select Loan Type");
                    $("#sltloan").focus();
                    return false;
                } else {
                    if ($('#sltloan').val() == "OR" || $('#sltloan').val() == "OVDL" || $('#sltloan').val() == "PA") {
                        if ($('#btid').val() == '') {
                            alert("Bt Id can not blank.");
                            $("#btid").focus();
                            return false;
                        }

                    }
                }
                if ($('#nowDeduct').val() == "") {
                    alert("Please select Now Deduct");
                    return false;
                }
                if ($('#sltloan').val() != "" && ($('#sltloan').val() == "OR" || $('#sltloan').val() == "OVDL" || $('#sltloan').val() == "PA")) {
                    if ($('#voucherNo').val() == "") {
                        alert("Please Enter Treasury Voucher No");
                        $('#voucherNo').focus();
                        return false;
                    }
                    if ($('#voucherDate').val() == "") {
                        alert("Please Enter Treasury Voucher Date");
                        $('#voucherDate').focus();
                        return false;
                    }
                    if ($('#treasuryname').val() == "") {
                        alert("Please select Treasury");
                        return false;
                    }
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

            function getDistrictAndDepartmentWiseOfficeList() {
                var deptcode;
                var distcode;

                deptcode = $('#hidSancDeptCode').val();
                distcode = $('#hidSancDistCode').val();
                $('#hidSancOffCode').empty();
                $('#hidSancOffCode').append('<option value="">--Select Office--</option>');

                //var url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                var url = 'getOfficeListDistrictAndDepartmentForBacklogEntryJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {

                        $('#hidSancOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');

                    });
                });

            }

            function getOfficeWiseGenericPostList() {
                var offcode;
                var url;

                offcode = $('#hidSancOffCode').val();
                $('#hidSancGenericPost').empty();
                $('#hidSancGenericPost').append('<option value="">--Select Generic Post--</option>');
                url = "getAuthorityPostListJSON.htm?offcode=" + offcode;


                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {

                        $('#hidSancGenericPost').append('<option value="' + obj.value + '">' + obj.label + '</option>');

                    });
                });
            }

            function getGenericPostWiseSPCList() {
                var offcode;
                var gpc;
                var url;

                offcode = $('#hidSancOffCode').val();
                gpc = $('#hidSancGenericPost').val();
                url = "getAuthoritySubstantivePostListJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                $('#sancSpc').empty();
                $('#sancSpc').append('<option value="">--Select Substantive Post--</option>');


                //var url = "getVacantPostListJSON.htm?postcode="+gpc+"&offcode="+offcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {

                        $('#sancSpc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');

                    });
                });
            }

            function removeDepedentDropdown(type) {
                $('#hidSancDistCode').val('');

                $('#hidSancOffCode').empty();
                $('#hidSancOffCode').append('<option value="">--Select Office--</option>');

                $('#hidSancGenericPost').empty();
                $('#hidSancGenericPost').append('<option value="">--Select Generic Post--</option>');

                $('#sancSpc').empty();
                $('#sancSpc').append('<option value="">--Select Substantive Post--</option>');

            }

            function getPost(type) {

                $('#authority').val($('#sancSpc option:selected').text());

            }

            function updateLoanSanctionSBData() {

                var checknottoPrint = $("input[type=checkbox][name=chkNotSBPrint]:checked").val();
                if (confirm("Are you to Save?")) {
                    $.ajax({
                        url: "updateLoanSanctionSBData.htm",
                        method: "POST",
                        data: $("#loanform").serialize(),
                        success: function(data) {
                            if (data == "1") {
                                window.location = "loansanction.htm";
                            } else {
                                alert("Error Occured");
                            }
                        }
                    });
                }
            }
            
            function deleteLoanSanctionAttachment(loanid) {
                if(confirm('Are you sure to Delete this Attachment?')){
                    $.ajax({
                        type: "POST",
                        data: {loanId: loanid},
                        url: "deleteLoanSanctionAttachment.htm",
                        dataType: "json",
                        success: function (data) {
                            if(data.deletestatus == 1){
                                alert("Attachment Deleted");
                                $("#attachfile").html("<input type=\"file\" name=\"gcfile\" id=\"gcfile\" class=\"form-control\"/><span style=\"color:red\">(Only PDF, file size maximum 3 MB)</span>");
                            }else{
                                alert("Deletion Failed");
                            }
                        }
                    });
                }
            }
        </script>
    </head>
    <body>
        <form:form action="saveloanSanction.htm" method="post" commandName="command" id="loanform" enctype="multipart/form-data">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Loan Sanction
                    </div>        
                    <div class="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                            </div>
                            <div class="col-lg-3">   
                                <input type="checkbox" name="chkNotSBPrint" value="Y" id="chkNotSBPrint" class="form-control"/> 
                            </div>
                            <div class="col-lg-3"></div>
                            <div class="col-lg-2"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="orderno">Order No: <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:hidden path="notid"/>
                                <form:hidden path="empid" />
                                <form:hidden path="loanid"/>
                                <form:hidden path="agcalculationid" id="agcalculationid"/>
                                <form:hidden path="agAdrefid"/>

                                <form:hidden path="prinloanid"/>

                                <c:if test="${command.isverified ne 'Y'}">
                                    <form:input path="orderno" class="form-control" id="orderno"/>
                                </c:if>
                                <c:if test="${command.isverified eq 'Y'}">
                                    <form:hidden path="orderno" class="form-control" id="orderno"/>
                                    ${command.orderno}
                                </c:if>
                            </div>
                            <div class="col-lg-2">
                                <label for="orderdate">Order Date: <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <c:if test="${command.isverified ne 'Y'}">
                                    <div class='input-group date' id='processDate'>
                                        <form:input class="form-control" id="orderdate" path="orderdate" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>     
                                </c:if>
                                <c:if test="${command.isverified eq 'Y'}">
                                    <form:hidden  id="orderdate" path="orderdate" />
                                    ${command.orderdate}
                                </c:if>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltloan">Loan Name: <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <c:if test="${not empty command.loanid}">
                                    <form:hidden path="sltloan"/>
                                    <form:select path="sltloan" id="sltloan" class="form-control" disabled="true">
                                        <form:option value="">Select Loan</form:option>
                                        <form:options items="${loanTypeList}" itemValue="loanType" itemLabel="loanName"/> 
                                    </form:select>
                                </c:if>
                                <c:if test="${empty command.loanid}">
                                    <form:select path="sltloan" id="sltloan" class="form-control">
                                        <form:option value="">Select Loan</form:option>
                                        <form:options items="${loanTypeList}" itemValue="loanType" itemLabel="loanName"/> 
                                    </form:select>
                                </c:if>
                            </div>
                            <div class="col-lg-2">
                                <label for="btid">BT ID:</label>
                            </div>
                            <div class="col-lg-2">
                                <span id="btidLabel">${command.btid} </span>
                                <form:input path="btid" id="btid" class="form-control" onkeypress="return onlyIntegerRange(event)" maxlength="6"/>
                            </div>
                        </div>
                        <c:if test="${command.sltloan eq 'NPSL'}">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltGovernmentContr">Government Contribution:</label>
                                </div>
                                <div class="col-lg-2">
                                    <span id="governmentContr">
                                        <form:select path="sltGovernmentContr" id="sltGovernmentContr" class="form-control">
                                            <form:option value="">--Select--</form:option>
                                            <form:option value="10">10</form:option>
                                            <form:option value="14">14</form:option>
                                        </form:select>
                                    </span>
                                </div>
                                <div class="col-lg-2">
                                    <label for="gcfile">Upload Document:</label>
                                </div>
                                <div class="col-lg-2">
                                    <c:if test="${not empty command.originalFileNameGcfileDoc}">
                                        <span id="attachfile">
                                            <a href="downloadNPSLGovernmentContributionDocument.htm?loanid=${command.loanid}" target="_blank">${command.originalFileNameGcfileDoc}</a>&nbsp;&nbsp;&nbsp;
                                            <a href="javascript:deleteLoanSanctionAttachment('${command.loanid}');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </span>
                                    </c:if>
                                    <c:if test="${empty command.originalFileNameGcfileDoc}">
                                        <input type="file" name="gcfile" id="gcfile" class="form-control"/><span style="color:red">(Only PDF, file size maximum 3 MB)</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-4"></div>
                            </div>
                        </c:if>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="authority">Sanctioning Authority:</label>
                            </div>
                            <div class="col-lg-9">
                                <%--<input type="text" name="authority" class="form-control" id="authority"> --%>
                                 <form:input class="form-control" path="authority" id="authority"/>    
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#loanSanctionModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtamount">Amount in Rs.:</label>
                            </div>
                            <div class="col-lg-2">
                                <c:if test="${not empty command.agcalculationid}">
                                    <form:input path="txtamount" class="form-control" id="txtamount" readonly="true"/>
                                </c:if>
                                <c:if test="${empty command.agcalculationid}">
                                    <form:input path="txtamount" class="form-control" id="txtamount"/>
                                </c:if>
                            </div>
                            <div class="col-lg-2">
                                <label for="accountNo">Account No :</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input path="accountNo" class="form-control" id="accountNo"/>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="bank">Bank Name:</label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="bank" id="bank" class="form-control" onchange="getBranchList(this)">
                                    <option value="">Select Bank</option>
                                    <form:options items="${bankList}" itemValue="bankcode" itemLabel="bankname"/> 
                                </form:select>                            
                            </div>
                            <div class="col-lg-2">
                                <label for="branch">Branch Name :</label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="branch" class="form-control" id="branch">
                                    <option value="">Select Branch</option>
                                </form:select>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>Treasury voucher No:</label>
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control" id="voucherNo" path="voucherNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label >Date:</label>
                            </div>
                            <div class="col-lg-2">
                                <div class='input-group date' id='processDate'>
                                    <form:input class="form-control" id="voucherDate" path="voucherDate" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>                        
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>Treasury :</label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="treasuryname" class="form-control" id="treasuryname">
                                    <option value="">Select Treasury</option>
                                    <form:options items="${treasuryList}" itemValue="treasuryCode" itemLabel="treasuryName"/>
                                </form:select>
                            </div>                                                
                        </div>
                        <div class="row" style="margin-bottom: 7px;margin-top: 65px;">
                            <div class="col-lg-2">
                                <label>Now Deduct :<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="nowDeduct" class="form-control" id="nowDeduct">
                                    <c:if test="${empty command.nowDeduct || command.nowDeduct eq 'P'}">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="P">PRINCIPAL</form:option>
                                    </c:if>
                                    <c:if test="${not empty command.nowDeduct && command.nowDeduct eq 'I'}">
                                        <form:option value="I">INTEREST</form:option>
                                    </c:if>
                                </form:select>
                            </div>                                                
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-12">
                                <table width="100%">                                
                                    <tr>                                    
                                        <td><label>Original Amount</label></td>
                                        <td><label>Total No of Instal.</label></td>
                                        <td><label>Instalment Amount</label></td>
                                        <td><label>Paid Instal No</label></td>
                                        <td><label>Monhtly Instl No(if required)</label></td>
                                        <td><label>Cumulative Amount paid</label></td>
                                        <td><label>Completed Recovery</label></td>
                                    </tr>
                                    <tr>                                    
                                        <td>
                                            <c:if test="${not empty command.agcalculationid}">
                                                <form:input path="originalAmt" class="form-control" id="originalAmt" readonly="true"/>
                                            </c:if>
                                            <c:if test="${empty command.agcalculationid}">
                                                <form:input path="originalAmt" class="form-control" id="originalAmt"/>
                                            </c:if>
                                        </td>
                                        <td><form:input path="totalNoOfInsl" class="form-control" id="totalNoOfInsl"/></td>
                                        <td><form:input path="instalmentAmount" class="form-control" id="instalmentAmount"/></td>
                                        <td><form:input path="lastPaidInstalNo" class="form-control" id="lastPaidInstalNo"/></td>
                                        <td><form:input path="monthlyinstlno" class="form-control" id="monthlyinstlno"/></td>
                                        <td><form:input path="cumulativeAmtPaid" class="form-control" id="cumulativeAmtPaid"/></td>
                                        <td><form:checkbox path="completedRecovery" class="form-control" value="1" id="completedRecovery"/></td>
                                    </tr>                                
                                </table>
                            </div>
                        </div>                                    
                    </div>            
                    <div class="panel-footer">

                        <c:if test="${command.completedRecovery ne 1}">

                            <c:if test="${empty Monthwiseinstl}" >
                                <input type="submit" class="btn btn-default" value="Save Loan" name="action" onclick="return validateform()"/>
                            </c:if>
                            <%--<c:if test="${not empty Monthwiseinstl && (AllowUpdate eq 'Y' || command.sltloan eq 'GA'|| command.sltloan eq 'TPFGA') }" >--%>
                            <c:if test="${not empty Monthwiseinstl && (AllowUpdate eq 'Y' || (command.sltloan ne 'HBA' && command.sltloan ne 'SHBA' && command.sltloan ne 'CMPA' && command.sltloan ne 'MCA' && command.sltloan ne 'VE' && command.sltloan ne 'MOPA'))}" >
                                <input type="submit" class="btn btn-default" value="Save Loan" name="action" onclick="return validateform()"/>
                            </c:if>    
                        </c:if>
                        <!-- For Not to Print Button in Service Book -->
                        <input type="button" class="btn btn-default" value="Save Loan(Only for Not to Print)" onclick="updateLoanSanctionSBData();"/>
                        <!-- For Not to Print Button in Service Book -->

                        <input type="submit" class="btn btn-default" value="Back" name="action"/>
                        <c:if test="${command.sltloan eq 'BICA' || command.sltloan eq 'CMPA' || command.sltloan eq 'MOPA' || command.sltloan eq 'MCA' || command.sltloan eq 'VE'}">
                            <c:if test="${not empty command.agcalculationid}">
                                <span style="font-size:15px;font-weight: bold;color:#F00000">
                                    This data is generated by AG&E. If any discrepancy found then contact AG&E.
                                </span>
                            </c:if>
                        </c:if>
                    </div>
                </div>
                <div class="row" style="margin: 5px;">
                    <h3>Loan Recovery History</h3>
                    <p>The Below table will show the month wise recovery history of the employee</p>
                </div>
                <div class="row" style="margin: 5px;border: 1px solid #000000;">
                    <table class="table table-hover">
                        <thead>
                            <tr class="success">
                                <th>Sl No</th>
                                <th>Month-year</th>
                                <th>Instalment Amount</th>
                                <th>Now Deduct</th>
                                <th>Ref</th>
                                <th>Total Amt. Paid</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${Monthwiseinstl}" var="instlment" varStatus="cnt">
                                <tr>
                                    <td>${cnt.index+1}</td>
                                    <td>${instlment.month}-${instlment.year}</td>
                                    <td>${instlment.instalmentAmount}</td>
                                    <td>${instlment.nowdedn}</td>
                                    <td>${instlment.refDesc}</td>
                                    <td>${instlment.cumulativeAmtPaid}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>
            <div id="loanSanctionModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Details of Sanctioning Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidSancDeptCode" id="hidSancDeptCode" class="form-control" onchange="removeDepedentDropdown();">
                                        <option value="">Select Department</option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidSancDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidSancDistCode" id="hidSancDistCode" class="form-control" onchange="getDistrictAndDepartmentWiseOfficeList();">
                                        <option value="">--Select District--</option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidSancOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidSancOffCode" id="hidSancOffCode" class="form-control" onchange="getOfficeWiseGenericPostList();">
                                        <option value="">--Select Office--</option>
                                        <form:options items="${offList}" itemValue="offCode" itemLabel="offName"/>                                       
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidSancGenericPost">Generic Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidSancGenericPost" id="hidSancGenericPost" class="form-control" onchange="getGenericPostWiseSPCList();">
                                        <option value="">--Select Generic Post--</option>
                                        <form:options items="${gpcpostedList}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sancSpc">Substantive Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sancSpc" id="sancSpc" class="form-control" onchange="getPost();">
                                        <option value="">--Select Substantive Post--</option>
                                         <form:options items="${spcList}" itemValue="value" itemLabel="label"/>
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
        <script type="text/javascript">
            $(function() {
                $('#voucherDate').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
                $('#orderdate').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
            });
        </script>
    </body>
</html>
