<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% int i = 1;%>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee Pension Acknowledgement</title>
        <link href="../font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">  
        <link href="../css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="../js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="../js/bootstrap.min.js"></script>
        <script>

            function updateNominee(serialNo) {
                var updateflag = true;
                if ($("#priorityLevel").val() == '') {
                    alert("Please select Priority Level");
                    updateflag = false;
                    return false;
                }
                if ($("#retirementBenefitType").val() == '') {
                    alert("Please select Retirement Benefit Type");
                    updateflag = false;
                    return false;
                }
                if ($("#nomineeTyp").val() == '') {
                    alert("Please select Nominee Type");
                    updateflag = false;
                    return false;
                }
                if (updateflag) {
                    var retirementBenefitType = document.getElementsByName("retirementBenefitType")[0].value;
                    var nomineeTyp = document.getElementsByName("nomineeTyp")[0].value;
                    var priorityLevel = document.getElementsByName("priorityLevel")[0].value;
                    var data = {
                        serialNo: serialNo,
                        retirementBenefitType: retirementBenefitType,
                        nomineeTyp: nomineeTyp,
                        priorityLevel: priorityLevel
                    };
                    var url = 'updatepension?serialNo=' + serialNo + '&empId=' + $('#hrmsEmpId').val();
                    $.ajax({
                        url: url,
                        type: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify(data),
                        success: function(response) {
                            console.log('Nominee updated successfully');
                        },
                        error: function(xhr, status, error) {
                            console.error('Error updating nominee:', error);
                        }
                    });
                    getNomineeShareTotal();
                }
            }

            function getNomineeShareTotal() {
                var shareO = 0;
                var shareA = 0;
                var rowCount = $('#employeedata tr').length;
                for (var i = 1; i <= rowCount; i++) {
                    $("tr.nomineeListTr_" + i).each(function() {
                        var selectValue = $(this).find("td:eq(15)>select").val();
                        if (selectValue == "O") {
                            shareO = shareO + parseInt($(this).find("td:eq(6)").text());
                            //quantity2 = $(this).find("input.id").val();
                        }
                        if (selectValue == "A") {
                            shareA = shareA + parseInt($(this).find("td:eq(6)").text());
                            //quantity2 = $(this).find("input.id").val();
                        }
                    });
                }


                if (shareO == 100) {
                    $("#msgforshare100").text("");
                    $("#hidSubmit").val("Y");
                }


                if (shareA == 100) {
                    $("#msgforshare100").text("");
                    $("#hidSubmit").val("Y");
                }
            }

            var stopSubmit = false;
            $(document).ready(function() {
                $('#chkPensionAcknowledge').change(function() {
                    if (this.checked) {
                        $('#btnSubmit').prop("disabled", false);
                    } else {
                        $('#btnSubmit').prop("disabled", true);
                    }
                }).change()

                pensionAcknowledgementValidation();
            });

            var flag = false;
            function pensionAcknowledgementValidation() {
                var errormsg = "";
                if ($("#tpfSeries").val() == "") {
                    errormsg = '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> GPF Series is empty</div>"';
                }
                if ($("#tpfAcNo").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> GPF Account Number is empty</div>';
                }
                if ($("#salutationEmp").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Employee Salutation is empty</div>';
                }
                if ($("#employeeFirstName").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Employee First is empty</div>';
                }
                if ($("#employeeLastName").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Employee Last is empty</div>';
                }
                if ($("#salutationgurdian").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Guardian Salutation is empty</div>';
                }
                if ($("#dependentName").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Guardian Name is empty</div>';
                }
                if ($("#relation_type").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Relation is empty</div>';
                }
                if ($("#religionId").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Religion is empty</div>';
                }
                if ($("#penIdnMark").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Pension Id mark is empty</div>';
                }
                if ($("#penIdnMark2").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Pension Id mark 2 is empty</div>';
                }
                if ($("#height").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Employee height is empty</div>';
                }
                if ($("#sex").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Sex is missing</div>';
                }
                if ($("#intMaritalStatusTypeId").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Marital status is empty</div>';
                }
                if ($("#nationalityId").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Nationality is empty</div>';
                }
                if ($("#designationId").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Designation is empty</div>';
                }
                if ($("#retirementDate").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Retirement date is empty</div>';
                }
                if ($("#penCategoryId").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Pention category is empty</div>';
                }
//                if ($("#cvp").val() == "") {
//                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> CVP is empty</div>';
//                }
//                if ($("#cvpPercentage").val() == "") {
//                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your CVP percentage</div>';
//                }
                if ($("#retirementType").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your retirement type</div>';
                }
                if ($("#panNo").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your pan number</div>';
                }
                if ($("#ifscCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your bank IFSC code</div>';
                }
                if ($("#bankBranch").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your bank branch</div>';
                }
                if ($("#bankAcctNo").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your bank Account Number</div>';
                }
                if ($("#treasuryCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your treasery code</div>';
                }
                if ($("#mobileNo").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your mobile number</div>';
                }
                if ($("#ddoName").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your ddo name</div>';
                }
                if ($("#retirementType").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your retirement type</div>';
                }
                if ($("#panNo").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your pan number</div>';
                }
                if ($("#perAddcity").val() == "" || $("#perAddtown").val() == "" || $("#perAddpoliceStation").val() == "" || $("#perAddstateId").val() == ""
                        || $("#districtCode").val() == "" || $("#perAddpin").val() == "") {
                    //   alert("Inside Town validation");
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your permanent address details</div>';
                }
                if ($("#commAddcity").val() == "" || $("#commAddtown").val() == "" || $("#commAddpoliceStation").val() == "" || $("#commAddstateId").val() == ""
                        || $("#commDistrictCode").val() == "" || $("#commAddpin").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your Communication Address details</div>';
                }

                var anyFieldNotEmpty = false;

                if ($("#prevPenType").val() || $("#prevPenSource").val() || $("#prevPenAmt").val() || $("#prevPPOOrFPPONo").val() || $("#prevPenPayTresCode").val() || $("#prevPensionEfffromDate").val() || $("#prevPenBankIfscCd").val() || $("#prevPenBankBranch").val() || $("#prevPenPia").val()) {
                    anyFieldNotEmpty = true;
                }

                if (anyFieldNotEmpty) {
                    if (!$("#prevPenType").val() || !$("#prevPenSource").val() || !$("#prevPenAmt").val() || !$("#prevPPOOrFPPONo").val() || !$("#prevPenPayTresCode").val() || !$("#prevPensionEfffromDate").val() || !$("#prevPenBankIfscCd").val() || !$("#prevPenBankBranch").val() || !$("#prevPenPia").val()) {
                        errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please fill in all remaining previous pension details</div>';
                    }
                }
                //alert("errormsg is: "+errormsg);
                if (errormsg)
                {
                    $('#error_msg').html("<ul>" + errormsg + "</ul>");
                    $('#error_wrapper').slideDown();
                    flag = true;
                } else {
                    flag = false;
                }
            }

            function confirmSubmit() {
                pensionAcknowledgementValidation();
                if (flag || stopSubmit) {
                    alert("Please complete all your profile data before submitting.");
                    window.scrollTo(0, 0);
                    return false;
                } else if ($("#hidSubmit").val() != 'Y') {
                    alert("Please complete all your profile data before submitting.");
                    window.scrollTo(0, 0);
                    return false;
                } else {
                    if (confirm('Are you sure you want to submit profile data?')) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }








        </script>

        <style>
            @keyframes blink {
                0% { color: FF0000; }
                50% { color: transparent; }
                100% { color: FF0000; }
            }
        </style>

    </head>
    <body >
        <form:form name="myForm" action="SubmitPensionAcknowledgement.htm" method="POST"
                   commandName="employee">

            <form:hidden id="hrmsEmpId" path="hrmsEmpId" value="${employee.hrmsEmpId}"/>
            <form:hidden path="tpfSeries"/>
            <form:hidden path="tpfAcNo"/>
            <form:hidden path="salutationEmp"/>
            <form:hidden path="employeeFirstName"/>
            <form:hidden path="employeeLastName"/>
            <form:hidden path="religionId"/>
            <form:hidden path="penIdnMark"/>
            <form:hidden path="penIdnMark2"/>
            <form:hidden path="height"/>
            <form:hidden path="sex"/>
            <form:hidden path="intMaritalStatusTypeId"/>
            <form:hidden path="nationalityId"/>
            <form:hidden path="designationId"/>
            <form:hidden path="retirementDate"/>
            <input type="hidden" value="${employee.penCategoryId}" />
            <input type="hidden" id="hidSubmit" />
            <form:hidden path="cvp"/>
            <form:hidden path="cvpPercentage"/>
            <input type="hidden" value="${employee.retirementType}" />
            <form:hidden path="panNo"/>
            <form:hidden path="ifscCode"/>
            <form:hidden path="bankBranch"/>
            <form:hidden path="bankAcctNo"/>
            <form:hidden path="treasuryCode"/>
            <form:hidden path="mobileNo"/>
            <form:hidden path="ddoName"/>
            <form:hidden path="mailId"/>
            <form:hidden path="perAddcity"/>
            <form:hidden path="perAddtown"/>
            <form:hidden path="perAddpoliceStation"/>
            <form:hidden path="perAddstateId"/>
            <form:hidden path="districtCode"/>
            <form:hidden path="perAddpin"/>
            <form:hidden path="commAddcity"/>
            <form:hidden path="commAddtown"/>
            <form:hidden path="commAddpoliceStation"/>
            <form:hidden path="commAddstateId"/>
            <form:hidden path="commDistrictCode"/>
            <form:hidden path="commAddpin"/>
            <form:hidden path="prevPenType"/>
            <form:hidden path="prevPenSource"/>
            <form:hidden path="prevPenAmt"/>
            <form:hidden path="prevPPOOrFPPONo"/>
            <form:hidden path="prevPenPayTresCode"/>
            <form:hidden path="prevPensionEfffromDate"/>
            <form:hidden path="prevPenBankIfscCd"/>
            <form:hidden path="prevPenBankBranch"/>
            <form:hidden path="prevPenPia"/>          

            <div style=" margin-bottom: 5px;" class="panel panel-info">
                &nbsp;
                <div class="panel-body" style="background-color: #cfe2f3;">
                    <h1 style="font-size:18pt;text-align:center;margin-top:0px;font-weight:bold;"> Pensioner/GPF Holder Details </h1> 
                </div>

                <div>
                    <h5 style="color: red; animation: blink 1s infinite; float: right;"> <strong>HELP LINE NO : +91 8763545188 &nbsp;</strong></h5>
                </div> 

                <div class="container mt-5">
                    <div class="alert alert-primary text-center" role="alert">
                        ${message}
                    </div>
                </div>

                <div class="alert alert-danger" style="border:2px solid #F00;display:none;" id="error_wrapper">
                    <h4 class="alert-heading" style="font-size:14pt;font-weight:bold;">Check the following details in order to Complete your Profile!</h4>
                    <div class="row" id ="error_msg" style="color:#FF0000;font-size:12pt;">
                    </div>
                </div>

                <table class="table table-striped">
                    <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                        <td colspan="8"> Employee Details</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">Gpf Series:<strong style="color:red">*</strong></td>
                        <td width="20%" style="font-weight:bold;">${employee.tpfSeries}</td>                 
                        <td width="15%" align="right">Account Number:<strong style="color:red">*</strong></td>
                        <td >${employee.tpfAcNo}</td>
                        <td width="15%" align="right">HRMS Employee Id:</td>
                        <td >${employee.hrmsEmpId}</td>
                        <td colspan="2">&nbsp;</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">Salutation:<strong style="color:red">*</strong></td>
                        <td width="20%" style="font-weight:bold;">${employee.salutationEmp}</td>                  
                        <td width="15%" align="right">First Name:<strong style="color:red">*</strong></td>
                        <td >${employee.employeeFirstName}</td>
                        <td width="15%" align="right">Middle Name:</td>
                        <td >${employee.employeeMiddleName}</td>                
                        <td width="15%" align="right">Last  Name:<strong style="color:red">*</strong></td>
                        <td >${employee.employeeLastName}</td>
                    </tr>
                </table> 
                <table class="table table-striped">
                    <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                        <td colspan="10"> Guardian Details</td>
                    </tr>
                    <c:forEach items="${pensionGuardianDetails}" var="guardianDetails">
                        <tr>
                            <td width="15%" align="right">Salutation:<strong style="color:red">*</strong></td>

                            <td>
                                <c:if test="${empty guardianDetails.salutationgurdian}">
                                    <span style="color:red;">Required</span>
                                    <script>
                                        stopSubmit = true;</script>
                                    </c:if>
                                    <c:if test="${not empty guardianDetails.salutationgurdian}">
                                        ${guardianDetails.salutationgurdian}
                                    </c:if>
                            </td>

                            <td width="15%" align="right">Guardian First Name:<strong style="color:red">*</strong></td>

                            <td>
                                <c:if test="${empty guardianDetails.guardianfname}">
                                    <span style="color:red;">Required</span>
                                    <script>
                                        stopSubmit = true;</script>
                                    </c:if>
                                    <c:if test="${not empty guardianDetails.guardianfname}">
                                        ${guardianDetails.guardianfname}
                                    </c:if>
                            </td>
                            <td width="15%" align="right">Guardian Middle Name:</td>
                            <td>${guardianDetails.guardianmname}</td>
                            <td width="15%" align="right">Guardian Last Name:<strong style="color:red">*</strong></td>
                            <td>
                                <c:if test="${empty guardianDetails.guardianlname}">
                                    <span style="color:red;">Required</span>
                                    <script>
                                        stopSubmit = true;</script>
                                    </c:if>
                                    <c:if test="${not empty guardianDetails.guardianlname}">
                                        ${guardianDetails.guardianlname}
                                    </c:if>
                            </td>

                            <td width="15%" align="right">Relation:<strong style="color:red">*</strong></td>
                            <td>
                                <c:if test="${empty guardianDetails.relation_type}">
                                    <span style="color:red;">Required</span>
                                    <script>
                                        stopSubmit = true;</script>
                                    </c:if>
                                    <c:if test="${not empty guardianDetails.relation_type}">
                                        ${guardianDetails.relation_type}
                                    </c:if>
                            </td>

                        </tr>
                    </c:forEach>
                </table>

                <table class="table table-striped">
                    <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                        <td colspan="8"> Personal Details</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">Date of Birth:<strong style="color:red">*</strong></td>
                        <td width="20%" style="font-weight:bold;">${employee.dob}</td>                      
                        <td width="15%" align="right">Religion:</td>
                        <td >${employee.religionId}</td>
                    </tr> 
                    <tr>
                        <td width="15%" align="right">Identification Mark 1:<strong style="color:red">*</strong></td>
                        <td >${employee.penIdnMark}</td>                      
                        <td width="15%" align="right">Identification Mark 2<strong style="color:red">*</strong>:</td>
                        <td >${employee.penIdnMark2}</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">Height (in feet & inches):<strong style="color:red">*</strong></td>
                        <td >${employee.height}</td>                      
                        <td width="15%" align="right">Sex:</td>
                        <td >${employee.sex}</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">Marital Status:</td>
                        <td >${employee.intMaritalStatusTypeId}</td>                      
                        <td width="15%" align="right">Nationality:</td>
                        <td >${employee.nationalityId}</td>
                    </tr>   
                </table>    

                <table class="table table-striped">
                    <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                        <td colspan="8"> Official Details</td>
                    </tr>

                    <tr>
                        <td width="15%" align="right">Designation<strong style="color:red">*</strong>:</td>
                        <td width="20%" style="font-weight:bold;">${employee.designationId}</td>                      
                        <td width="15%" align="right">Retirement Date<strong style="color:red">*</strong>:</td>
                        <td >${employee.retirementDate}</td>
                    </tr> 
                    <tr>
                        <td width="15%" align="right">Pension Post<strong style="color:red">*</strong></td>
                        <td>  
                            <select name="postCode" id="postCode" class="form-control" required>
                                <option value="">--Select Post--</option>
                                <c:forEach var="post" items="${employee.availablePosts}">
                                    <option value="${post.postCode}" ${post.postCode eq employee.postCode ? 'selected' : ''}>
                                        ${post.postName}
                                        &nbsp
                                        <c:if test="${post.userType eq 'G'}">
                                        <h5 style="font-weight: bold;">(AG)</h5>
                                    </c:if>
                                    <c:if test="${post.userType eq 'T'}">
                                        <h5 style="font-weight: bold;">(CA)</h5>
                                    </c:if>
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                        <td width="15%" align="right">Pension Category:<strong style="color:red">*</strong></td>
                        <td style="width: 20px">  
                            <select name="penCategoryId" id="penCategoryId" class="form-control" required>
                                <option value="" <c:if test="${empty employee.penCategoryId}"> selected="selected"</c:if>>--Select--</option>
                                <option value="2" <c:if test="${not empty employee.penCategoryId && employee.penCategoryId==2}"> <c:out
                                                value='selected="selected"' /></c:if>>Family Pension</option>
                                <option value="1" <c:if test="${not empty employee.penCategoryId && employee.penCategoryId==1}"> <c:out
                                                value='selected="selected"' /></c:if>>Normal Pension</option>
                                </select>
                            </td>                      
                        </tr>

                        <tr>
                            <td width="15%" align="right">Retirement Type:<strong style="color:red">*</strong></td>
                            <td>                       
                                <select name="retirementType" class="form-control" id='retirementType' required>

                                    <option value="" <c:if test="${not empty employee.retirementType && employee.retirementType eq ''}"> <c:out
                                                value='selected="selected"' /></c:if>>--Select--</option>
                                <option value="1" <c:if test="${not empty employee.retirementType && employee.retirementType eq '1'}"> <c:out
                                                value='selected="selected"' /></c:if>>Retiring</option>
                                <option value="2" <c:if test="${not empty employee.retirementType && employee.retirementType eq '2'}"> <c:out
                                                value='selected="selected"' /></c:if>>Pro-rata Pension</option>
                                <option value="3" <c:if test="${not empty employee.retirementType && employee.retirementType eq '3'}"> <c:out
                                                value='selected="selected"' /></c:if>>Suparannuation</option>   
                                <option value="4" <c:if test="${not empty employee.retirementType && employee.retirementType eq '4'}"> <c:out
                                                value='selected="selected"' /></c:if>>Compassionate Allowance</option>
                                <option value="5" <c:if test="${not empty employee.retirementType && employee.retirementType eq '5'}"> <c:out
                                                value='selected="selected"' /></c:if>>Voluntary Retirement</option>                             
                                </select>
                            </td> 
                            <td width="15%" align="right">Pan No:</td>
                            <td >${employee.panNo}</td>
                    </tr>  
                </table> 
                <table class="table table-striped">
                    <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                        <td colspan="8"> Payment and Contact Details</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">IFSC Code<strong style="color:red">*</strong>:</td>
                        <td width="20%" style="font-weight:bold;">${employee.ifscCode}</td>                      
                        <td width="15%" align="right"> Bank Branch:</td>
                        <td >${employee.bankBranch}</td>
                    </tr>  
                    <tr>
                        <td width="15%" align="right">Bank Account No<strong style="color:red">*</strong>:</td>
                        <td >${employee.bankAcctNo}</td>                      
                        <td width="15%" align="right">District (Last Served):</td>
                        <td >${employee.lastDistrictServe}</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">Payable Treasury<strong style="color:red">*</strong>:</td>
                        <td >${employee.treasuryCode}</td>                      
                        <td width="15%" align="right">Mobile No<strong style="color:red">*</strong>:</td>
                        <td >${employee.mobileNo}</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">DDO Code/ DDO Name<strong style="color:red">*</strong>:</td>
                        <td >${employee.ddoName}</td>                      
                        <td width="15%" align="right">Mail Id:</td>
                        <td >${employee.mailId}</td>
                    </tr>
                </table>

                <table class="table table-striped">
                    <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                        <td colspan="8"> Pensioner's Permanent Address</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">City/Village<strong style="color:red">*</strong>:</td>
                        <td width="20%" style="font-weight:bold;">${employee.perAddcity}</td>                      
                        <td width="15%" align="right"> Town<strong style="color:red">*</strong>:</td>
                        <td >${employee.perAddtown}</td>
                    </tr>  
                    <tr>
                        <td width="15%" align="right">Police Station<strong style="color:red">*</strong>:</td>
                        <td >${employee.perAddpoliceStation}</td>                      
                        <td width="15%" align="right">State<strong style="color:red">*</strong>:</td>
                        <td >${employee.perAddstateId}</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">District<strong style="color:red">*</strong>:</td>
                        <td >${employee.districtCode}</td>                      
                        <td width="15%" align="right">PinCode<strong style="color:red">*</strong>:</td>
                        <td >${employee.perAddpin}</td>
                    </tr>
                </table>
                <table class="table table-striped">
                    <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                        <td colspan="8"> Pensioner's Communication Address</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">City/Village<strong style="color:red">*</strong>:</td>
                        <td width="20%" style="font-weight:bold;">${employee.commAddcity}</td>                      
                        <td width="15%" align="right"> Town<strong style="color:red">*</strong>:</td>
                        <td >${employee.commAddtown}</td>
                    </tr>  
                    <tr>
                        <td width="15%" align="right">Police Station<strong style="color:red">*</strong>:</td>
                        <td >${employee.commAddpoliceStation}</td>                      
                        <td width="15%" align="right">State<strong style="color:red">*</strong>:</td>
                        <td >${employee.commAddstateId}</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">District<strong style="color:red">*</strong>:</td>
                        <td >${employee.commDistrictCode}</td>                      
                        <td width="15%" align="right">PinCode<strong style="color:red">*</strong>:</td>
                        <td >${employee.commAddpin}</td>
                    </tr>
                </table>

                <table class="table table-striped">
                    <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                        <td colspan="8"> Previous Pension Details(If Any) </td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">Pension Type<strong style="color:red">*</strong>:</td>
                        <td width="20%" style="font-weight:bold;">${employee.prevPenType}</td>                      
                        <td width="15%" align="right"> Source<strong style="color:red">*</strong>:</td>
                        <td >${employee.prevPenSource}</td>
                    </tr>  
                    <tr>
                        <td width="15%" align="right">Pension Amount<strong style="color:red">*</strong>:</td>
                        <td >${employee.prevPenAmt}</td>                      
                        <td width="15%" align="right">PPO/FPPO No<strong style="color:red">*</strong>:</td>
                        <td >${employee.prevPPOOrFPPONo}</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">Payable Treasury<strong style="color:red">*</strong>:</td>
                        <td >${employee.prevPenPayTresCode}</td>                      
                        <td width="15%" align="right">Pension Effective From Date<strong style="color:red">*</strong>:</td>
                        <td >${employee.prevPensionEfffromDate}</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">IFSC Code:</td>
                        <td >${employee.prevPenBankIfscCd}</td>                      
                        <td width="15%" align="right">Bank Branch:</td>
                        <td >${employee.prevPenBankBranch}</td>
                    </tr>
                    <tr>
                        <td width="15%" align="right">Pension Issuing Authority:</td>
                        <td >${employee.prevPenPia}</td> 
                    </tr>
                </table>

                <table class="table table-striped" id="employeedata">
                    <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                        <td colspan="19">Nominee Details </td>
                    </tr>
                    <c:set var="nomineeStaus" value="N" />
                    <tr>
                        <th>Name<strong style="color:red">*</strong></th> 
                        <th>Relation<strong style="color:red">*</strong></th>
                        <th>Gender<strong style="color:red">*</strong></th> 
                        <th>Status<strong style="color:red">*</strong></th>
                        <th>DOB<strong style="color:red">*</strong></th>
                        <th>Mobile No</th>
                        <th>Share %<strong style="color:red">*</strong></th>
                        <th>IFSC Code</th>
                        <th>Account No</th>
                        <th>Bank Branch</th>
                        <th>Address</th>
                        <!-- <th>CVP Applied<strong style="color:red">*</strong></th>
                         <th>CVP Percentage<strong style="color:red">*</strong></th>-->
                        <th>Minor <strong style="color:red">*</strong></th>
                        <th>Guardian Details<strong style="color:red">*</strong></th>
                        <th>Priority Label<strong style="color:red">*</strong></th>
                        <th>Retirement Benefit type<strong style="color:red">*</strong></th>
                        <th>Nominee Type<strong style="color:red">*</strong></th>
                        <th>Action</th>
                    </tr>    
                    <c:forEach items="${nomineeList}" var="familyRelation" varStatus="cnt">
                        <c:if test="${familyRelation.nomineeType eq 'Y' && familyRelation.sharePercentage ne '0'}">
                            <tr class="nomineeListTr_${cnt.index + 1}">
                                <c:if test="${familyRelation.nomineeTyp eq 'O'}">
                                    <c:set var="OtotalSharePercentage" value="${OtotalSharePercentage + familyRelation.sharePercentage}" />
                                </c:if>

                                <c:if test="${familyRelation.nomineeTyp eq 'A'}">
                                    <c:set var="AtotalSharePercentage" value="${AtotalSharePercentage + familyRelation.sharePercentage}" />
                                </c:if>
                                <td>
                                    <c:if test="${empty familyRelation.salutationNominee or empty familyRelation.nomineeName}">
                                        <span style="color:red;">Required</span>
                                        <script>
                                            stopSubmit = true;</script>
                                        </c:if>
                                        <c:if test="${not (empty familyRelation.salutationNominee) and not (empty familyRelation.nomineeName)}">
                                            ${familyRelation.salutationNominee} ${familyRelation.nomineeName}
                                        </c:if>
                                </td>

                                <td>
                                    <c:if test="${empty familyRelation.relation}">
                                        <span style="color:red;">Required</span>
                                        <script>
                                            stopSubmit = true;</script>
                                        </c:if>
                                        <c:if test="${not empty familyRelation.relation}">
                                            ${familyRelation.relation}
                                        </c:if>
                                </td>
                                <td>
                                    <c:if test="${empty familyRelation.sex}">
                                        <span style="color:red;">Required</span>
                                        <script>
                                            stopSubmit = true;</script>
                                        </c:if>
                                        <c:if test="${not empty familyRelation.sex}">
                                            ${familyRelation.sex}
                                        </c:if>
                                </td>
                                <td>
                                    <c:if test="${empty familyRelation.nomineeMaritalStatusId}">
                                        <span style="color:red;">Required</span>
                                        <script>
                                            stopSubmit = true;</script>
                                        </c:if>
                                        <c:if test="${not empty familyRelation.nomineeMaritalStatusId}">
                                            ${familyRelation.nomineeMaritalStatusId}
                                        </c:if>
                                </td>
                                <td>
                                    <c:if test="${empty familyRelation.dob}">
                                        <span style="color:red;">Required</span>
                                        <script>
                                            stopSubmit = true;</script>
                                        </c:if>
                                        <c:if test="${not empty familyRelation.dob}">
                                            ${familyRelation.dob}
                                        </c:if>
                                </td>                            
                                <td>${familyRelation.mobileNo} </td>
                                <td>
                                    <c:if test="${empty familyRelation.sharePercentage}">
                                        <span style="color:red;">Required</span>
                                        <script>
                                            stopSubmit = true;</script>
                                        </c:if>
                                        <c:if test="${not empty familyRelation.sharePercentage}">
                                            ${familyRelation.sharePercentage}
                                        </c:if>
                                </td>
                                <td >${familyRelation.ifscCode}</td>
                                <td >${familyRelation.bankAccountNo}</td>
                                <td >${familyRelation.bankBranchName}</td>
                                <td >${familyRelation.nomineeAddress}</td>

                                <%--<td >${familyRelation.cvp}</td>
                                <td>
                                    <c:if test="${familyRelation.cvp eq 'Y'}">
                                        <c:if test="${empty familyRelation.cvpPercentage}">
                                            <span style="color:red;">Required</span>
                                            <script>
                                                stopSubmit = true;</script>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${not empty familyRelation.cvpPercentage}">
                                            ${familyRelation.cvpPercentage}
                                        </c:if>
                                </td>--%>

                                <td >${familyRelation.minorFlag}</td>

                                <td>
                                    <c:if test="${familyRelation.minorFlag eq 'yes'}">
                                        <c:if test="${empty familyRelation.gurdianName || empty familyRelation.salutationGaurdian}">
                                            <span style="color:red;">Required</span>
                                            <script>
                                                stopSubmit = true;</script>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${not empty familyRelation.gurdianName || not empty familyRelation.salutationGaurdian}">
                                            ${familyRelation.salutationGaurdian} ${familyRelation.gurdianName}
                                        </c:if>
                                </td>
                                <c:if test="${ empty employee.status || employee.status ne 'Y' }">
                                    <td>
                                        <select name="priorityLevel" id="priorityLevel" style="padding: 5px; font-size: 16px; border-radius: 5px; border: 1px solid #ccc;">
                                            <option value="">select</option>
                                            <option value="1" ${familyRelation.priorityLevel == '1' ? 'selected' : ''}>1</option>
                                            <option value="2" ${familyRelation.priorityLevel == '2' ? 'selected' : ''}>2</option>
                                            <option value="3" ${familyRelation.priorityLevel == '3' ? 'selected' : ''}>3</option>
                                        </select>
                                    </td>
                                    <td>
                                        <select name="retirementBenefitType" id="retirementBenefitType" style="padding: 5px; font-size: 16px; border-radius: 5px; border: 1px solid #ccc;">
                                            <option value="">select</option>
                                            <option value="1" ${familyRelation.retirementBenefitType == '1' ? 'selected' : ''}>Pension</option>
                                            <option value="2" ${familyRelation.retirementBenefitType == '2' ? 'selected' : ''}>Gratuity</option>
                                            <option value="3" ${familyRelation.retirementBenefitType == '3' ? 'selected' : ''}>CVP</option>
                                        </select>
                                    </td>

                                    <td>
                                        <select name="nomineeTyp" id="nomineeTyp" style="padding: 5px; font-size: 16px; border-radius: 5px; border: 1px solid #ccc;">
                                            <option value="">select</option>
                                            <option value="O" ${familyRelation.nomineeTyp == 'O' ? 'selected' : ''}>Original</option>
                                            <option value="A" ${familyRelation.nomineeTyp == 'A' ? 'selected' : ''}>Alternate</option>
                                        </select>
                                    </td>
                                    <td>
                                        <button type="button" id="updateButton" onclick="updateNominee(${familyRelation.serialNo})" class="btn btn-success">Update</button>
                                    </td>
                                </c:if>
                                <c:if test="${not empty employee.status && employee.status eq 'Y' }">
                                    <td>
                                        ${familyRelation.priorityLevel}
                                    </td>
                                    <td>
                                        ${familyRelation.retirementBenefitType}
                                    </td>

                                    <td>
                                        ${familyRelation.nomineeTyp}
                                    </td>
                                    <td></td>
                                </c:if>

                            </tr>  
                        </c:if>
                    </c:forEach> 

                    <c:if test="${not empty OtotalSharePercentage && OtotalSharePercentage ne 100 && not empty AtotalSharePercentage && AtotalSharePercentage ne 100 }">
                        <script>
                            stopSubmit = true;</script>
                        <tr>
                            <td colspan="18">
                                <span style="color:red;" id="msgforshare100">Original/Alternate share percentages must be 100%</span>
                            </td>
                        </tr>
                    </c:if>


                </table>  
                <c:if test="${ employee.status eq 'N'  }">
                    <span style="color:red;" id="msgforshare100">Original/Alternate share percentages must be 100%</span>
                </c:if>
                <table class="table table-striped">
                    <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                        <td colspan="16">Family Details </td>
                    </tr>
                    <c:set var="nomineeStaus" value="N" />
                    <tr>
                        <th>Name<strong style="color:red">*</strong></th> 
                        <th>Relation<strong style="color:red">*</strong></th>
                        <th>Gender<strong style="color:red">*</strong></th> 
                        <th>Status<strong style="color:red">*</strong></th>
                        <th>DOB<strong style="color:red">*</strong></th>
                        <th>Mobile No</th>
                        <th>Share %</th>
                        <th>IFSC Code</th>
                        <th>Account No</th>
                        <th>Bank Branch</th>
                        <th>Address</th>
                        <th>Remarks</th>
                        <!--   <th>CVP Applied<strong style="color:red">*</strong></th>
                          <th>CVP Percentage<strong style="color:red">*</strong></th>   >-->
                        <th>Minor<strong style="color:red">*</strong></th>
                        <th>Guardian Details<strong style="color:red">*</strong></th>
                        <th>Is HandiCapped<strong style="color:red">*</strong></th>
                        <th>HandiCapped Type<strong style="color:red">*</strong></th>
                    </tr>    
                    <c:forEach items="${nomineeList}" var="familyRelation" varStatus="cnt">

                        <%--<c:if test="${familyRelation.nomineeType ne 'Y'}">--%>
                        <tr>
                            <td>
                                <c:if test="${empty familyRelation.salutationNominee or empty familyRelation.nomineeName}">
                                    <span style="color:red;">Required</span>
                                    <script>
                                        stopSubmit = true;
                                    </script>
                                </c:if>
                                <c:if test="${not (empty familyRelation.salutationNominee) and not (empty familyRelation.nomineeName)}">
                                    ${familyRelation.salutationNominee} ${familyRelation.nomineeName}
                                </c:if>
                            </td>

                            <td>
                                <c:if test="${empty familyRelation.relation}">
                                    <span style="color:red;">Required</span>
                                    <script>
                                        stopSubmit = true;</script>
                                    </c:if>
                                    <c:if test="${not empty familyRelation.relation}">
                                        ${familyRelation.relation}
                                    </c:if>
                            </td>
                            <td>
                                <c:if test="${empty familyRelation.sex}">
                                    <span style="color:red;">Required</span>
                                    <script>
                                        stopSubmit = true;</script>
                                    </c:if>
                                    <c:if test="${not empty familyRelation.sex}">
                                        ${familyRelation.sex}
                                    </c:if>
                            </td>
                            <td>
                                <c:if test="${empty familyRelation.nomineeMaritalStatusId}">
                                    <span style="color:red;">Required</span>
                                    <script>
                                        stopSubmit = true;</script>
                                    </c:if>
                                    <c:if test="${not empty familyRelation.nomineeMaritalStatusId}">
                                        ${familyRelation.nomineeMaritalStatusId}
                                    </c:if>
                            </td>
                            <td>
                                <c:if test="${empty familyRelation.dob}">
                                    <span style="color:red;">Required</span>
                                    <script>
                                        stopSubmit = true;</script>
                                    </c:if>
                                    <c:if test="${not empty familyRelation.dob}">
                                        ${familyRelation.dob}
                                    </c:if>
                            </td>                                  
                            <td >${familyRelation.mobileNo}</td>
                            <td >${familyRelation.sharePercentage}</td>
                            <td >${familyRelation.ifscCode}</td>
                            <td >${familyRelation.bankAccountNo}</td>
                            <td >${familyRelation.bankBranchName}</td>
                            <td >${familyRelation.nomineeAddress}</td>
                            <td >${familyRelation.remarks}</td>

                            <%--<td >${familyRelation.cvp}</td>
                              <td>
                                  <c:if test="${familyRelation.cvp eq 'Y'}">
                                      <c:if test="${empty familyRelation.cvpPercentage}">
                                          <span style="color:red;">Required</span>
                                          <script>
                                              stopSubmit = true;</script>
                                          </c:if>
                                      </c:if>
                                      <c:if test="${not empty familyRelation.cvpPercentage}">
                                          ${familyRelation.cvpPercentage}
                                      </c:if>
                              </td>--%>

                            <td >${familyRelation.minorFlag}</td>
                            <td>
                                <c:if test="${familyRelation.minorFlag eq 'Yes'}">
                                    <c:if test="${empty familyRelation.gurdianName || empty familyRelation.salutationGaurdian}">
                                        <span style="color:red;">Required</span>
                                        <script>
                                            stopSubmit = true;
                                        </script>
                                    </c:if>
                                </c:if>
                                <c:if test="${not empty familyRelation.gurdianName || not empty familyRelation.salutationGaurdian}">
                                    ${familyRelation.salutationGaurdian}${familyRelation.gurdianName}
                                </c:if>
                            </td>
                            <td >${familyRelation.familyHandicappedFlag}</td>
                            <td>
                                <c:if test="${familyRelation.familyHandicappedFlag eq 'Yes'}">
                                    <c:if test="${empty familyRelation.familyHandicappedTypeId}">
                                        <span style="color:red;">Required</span>
                                        <script>
                                            stopSubmit = true;</script>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${not empty familyRelation.familyHandicappedTypeId}">
                                        ${familyRelation.familyHandicappedTypeId}
                                    </c:if>
                            </td>

                        </tr>  
                        <%--</c:if>--%>
                    </c:forEach>

                </table>    
                <div class="form-check mt-4">
                    <input class="form-check-input" type="checkbox" name="chkPensionAcknowledge"
                           id="chkPensionAcknowledge"   <c:if test="${not empty employee.status && employee.status eq 'Y' }"> checked="checked"</c:if>/>
                           <label class="form-check-label" for="chkPensionAcknowledge">I hereby declare that all the information
                               furnished by me are true to the best of my knowledge.</label>
                    </div>
                <c:if test="${ empty employee.status || employee.status ne 'Y' }">
                    <div class="panel panel-secondary mt-4">
                        <div class="panel-body">
                            <div class="text-left">
                                <input type="submit" name="btnSave" id="btnSubmit" value="Submit" class="btn btn-success btn-lg" onclick="return confirmSubmit();" disabled/>
                                &nbsp;
                                <input type="submit" name="btnSave" id="btnSubmit" class="btn btn-success btn-lg" value="Refresh"/>
                            </div>
                        </div>
                    </div>
                </c:if> 
            </div>   
        </form:form>
    </body>
</html>
