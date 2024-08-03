<%-- 
    Document   : CmExcellenceInvestigationForRange
    Created on : 8 Mar, 2021, 4:44:41 PM
    Author     : Surendra
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">    
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('#docPresentRank').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#dojDistEst').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                $('#datePresentRank').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });



                toggleDiv();
                togglerangeStatus();
                togglePendingEnquiryDiv();
                toggleCourtCaseDiv();
                toggleCriminalcaseDiv();
                toggleChargesheetedDiv();
                toggleProceedingDiv();
                toggleSelScientificTool();
                toggleInvstScientificEvd();
                toggleInvstInnovMethods();
                toggleInvstAttachConfis();
            });


            function toggleDiv() {
                if ($("#dpcifany").val() == 'Y') {
                    $("#hideDiscDocumentRow").show();
                    $("#hideDiscDocumentRow2").show();
                    $("#hideDiscDocumentRow3").show();
                } else {
                    $("#hideDiscDocumentRow").hide();
                    $("#hideDiscDocumentRow2").hide();
                    $("#hideDiscDocumentRow3").hide();
                }
            }
            function numbersOnly(evt) {
                evt = (evt) ? evt : window.event;
                var charCode = (evt.which) ? evt.which : evt.keyCode;
                if (charCode > 31 && (charCode < 48 || charCode > 57)) {
                    return false;
                } else {
                    return true;
                }
            }
            function toggleSelScientificTool() {

                if ($("#selScientificTool").val() == 'Y') {
                    $("#hideInvstScientificAids").show();
                } else {
                    $("#hideInvstScientificAids").hide();
                }
            }
            function toggleInvstInnovMethods() {
                if ($("#selInvstInnovMethods").val() == 'Y') {
                    $("#hideInvstInnovMethods").show();
                } else {
                    $("#hideInvstInnovMethods").hide();
                }
            }
            function toggleInvstScientificEvd() {
                if ($("#selInvstScientificEvd").val() == 'Y') {
                    $("#hideInvstScientificEvd").show();
                } else {
                    $("#hideInvstScientificEvd").hide();
                }
            }
            function toggleInvstAttachConfis() {
                if ($("#selInvstAttachConfis").val() == 'Y') {
                    $("#hideInvstAttachConfis").show();
                } else {
                    $("#hideInvstAttachConfis").hide();
                }
            }
            function togglerangeStatus() {
                if ($("#recommendStatusofRange").val() == 'NOT RECOMMENDED') {
                    $("#hidereasonFornotRecommend").show();
                } else {
                    $("#hidereasonFornotRecommend").hide();
                }
            }
            function toggleCourtCaseDiv() {
                if ($("#courtcaseifany").val() == 'Y') {
                    $("#courtcase1").show();
                    $("#courtcase2").show();
                } else {
                    $("#courtcase1").hide();
                    $("#courtcase2").hide();
                }
            }

            function togglePendingEnquiryDiv() {
                if ($("#Ifenquirypending").val() == 'Y') {
                    $("#hidePendingDocumentRow1").show();
                    $("#hidePendingDocumentRow2").show();
                    $("#hidePendingDocumentRow3").show();
                } else {
                    $("#hidePendingDocumentRow1").hide();
                    $("#hidePendingDocumentRow2").hide();
                    $("#hidePendingDocumentRow3").hide();
                }
            }
            function toggleCriminalcaseDiv() {
                if ($("#criminalcaseifany").val() == 'Y') {
                    $("#hideCriminalcaseRow").show();
                    $("#hideCriminalcaseRow1").show();
                    $("#hideCriminalcaseRow2").show();
                } else {
                    $("#hideCriminalcaseRow").hide();
                    $("#hideCriminalcaseRow1").hide();
                    $("#hideCriminalcaseRow2").hide();
                }
            }
            function toggleChargesheetedDiv() {
                if ($("#chargesheetedifany").val() == 'Y') {
                    $("#hideChargesheetedRow").show();
                    $("#hideChargesheetedRow1").show();
                    $("#hideChargesheetedRow2").show();
                } else {
                    $("#hideChargesheetedRow").hide();
                    $("#hideChargesheetedRow1").hide();
                    $("#hideChargesheetedRow2").hide();
                }
            }
            function toggleProceedingDiv() {
                if ($("#meetingProceeding").val() == 'Y') {
                    $("#hideProceedingRow").show();
                    $("#hideProceedingRow1").show();
                    $("#hideProceedingRow2").show();
                } else {
                    $("#hideProceedingRow").hide();
                    $("#hideProceedingRow1").hide();
                    $("#hideProceedingRow2").hide();
                }
            }

            function validateForm() {
                if ($("#recommendStatusofRange").val() == '') {
                    alert('Please select Recommendation Status for Range.');
                    return false;
                } else if ($("#recommendStatusofRange").val() == 'NOT RECOMMENDED') {
                    if ($("#reasonFornotRecommend").val() == '') {
                        alert('Please enter reason for not recommended.');
                        document.getElementById('reasonFornotRecommend').focus();
                        return false;
                    }
                }
                if (confirm("Are you sure to submit.")) {
                    return true;
                } else {
                    return false;
                }
            }
            function editSRCases(srCaseId) {
                $.ajax({
                    method: "POST",
                    url: "editSRCases.htm",
                    data: {srCaseId: srCaseId},
                    success: function (data) {
                        $('#srCaseId').val(srCaseId);
                        $('#srCaseNo').val(data.srCaseNo);
                        $('#regDate').val(data.regDate);
                        $('#frDate').val(data.frDate);
                        $('#dtlsTeamUse').val(data.dtlsTeamUse);
                        $('#noOfExhibits').val(data.noOfExhibits);
                        $('#convictionDtls').val(data.convictionDtls);
                        $('#crimeProceedAttach').val(data.crimeProceedAttach);


                    },
                    error: function () {
                        $("#msg").html("Error Occured");
                        $("#msg").css({"color": "red", "fontWeight": "bold"});
                        alert('Data not Save');
                    }
                });
            }
            function editnonSrcaseCases(nonSrcaseId) {
                $.ajax({
                    method: "POST",
                    url: "editNonSRCases.htm",
                    data: {nonSrcaseId: nonSrcaseId},
                    success: function (data) {
                        $('#nonSrcaseId').val(nonSrcaseId);
                        $('#noOfNonSrcaseInvst2018').val(data.noOfNonSrcaseInvst2018);
                        $('#noOfNonSrcaseInvst2019').val(data.noOfNonSrcaseInvst2019);
                        $('#noOfNonSrcaseInvst2020').val(data.noOfNonSrcaseInvst2020);
                        $('#noFinalizedInThirtydays2018').val(data.noFinalizedInThirtydays2018);
                        $('#noFinalizedInThirtydays2019').val(data.noFinalizedInThirtydays2019);
                        $('#noFinalizedInThirtydays2020').val(data.noFinalizedInThirtydays2020);
                        $('#noFinalizedAfterThirtydays2018').val(data.noFinalizedAfterThirtydays2018);
                        $('#noFinalizedAfterThirtydays2019').val(data.noFinalizedAfterThirtydays2019);
                        $('#noFinalizedAfterThirtydays2020').val(data.noFinalizedAfterThirtydays2020);
                        $('#noStillPending2018').val(data.noStillPending2018);
                        $('#noStillPending2019').val(data.noStillPending2019);
                        $('#noStillPending2020').val(data.noStillPending2020);
                        $('#noOfSrcaseInvst2018').val(data.noOfSrcaseInvst2018);
                        $('#noOfSrcaseInvst2019').val(data.noOfSrcaseInvst2019);
                        $('#noOfSrcaseInvst2020').val(data.noOfSrcaseInvst2020);
                        $('#noSrFinalizedInThirtydays2018').val(data.noSrFinalizedInThirtydays2018);
                        $('#noSrFinalizedInThirtydays2019').val(data.noSrFinalizedInThirtydays2019);
                        $('#noSrFinalizedInThirtydays2020').val(data.noSrFinalizedInThirtydays2020);
                        $('#noSrFinalizedAfterThirtydays2018').val(data.noSrFinalizedAfterThirtydays2018);
                        $('#noSrFinalizedAfterThirtydays2019').val(data.noSrFinalizedAfterThirtydays2019);
                        $('#noSrFinalizedAfterThirtydays2020').val(data.noSrFinalizedAfterThirtydays2020);
                        $('#noSrStillPending2018').val(data.noSrStillPending2018);
                        $('#noSrStillPending2019').val(data.noSrStillPending2019);
                        $('#noSrStillPending2020').val(data.noSrStillPending2020);

                    },
                    error: function () {
                        $("#msg").html("Error Occured");
                        $("#msg").css({"color": "red", "fontWeight": "bold"});
                        alert('Data not Save');
                    }
                });
            }
            function editInvstCases(invstCaseId) {
                $.ajax({
                    method: "POST",
                    url: "editInvestigatedCases.htm",
                    data: {invstCaseId: invstCaseId},
                    success: function (data) {

                        $('#invstCaseId').val(invstCaseId);
                        $('#psName').val(data.psName);
                        $('#invstCaseNo').val(data.invstCaseNo);
                        $('#invstDateReg').val(data.invstDateReg);
                        if (data.invstCsFr == 'UI') {
                            $("#invstCsFrUi").prop('checked', true);
                        }
                        if (data.invstCsFr == 'UT') {
                            $("#invstCsFrUt").prop('checked', true);
                        }
                        if (data.invstCsFr == 'PL') {
                            $("#invstCsFrPl").prop('checked', true);
                        }
                        $('#invstCsFr').val(data.invstCsFr);
                        $('#invstFinalSubDate').val(data.invstFinalSubDate);
                        $('#invstSrNonsr').val(data.invstSrNonsr);
                        $('#invstBriefCase').val(data.invstBriefCase);
                        $('#invstInnovMethods').val(data.invstInnovMethods);
                        $('#invstScientificAids').val(data.invstScientificAids);
                        $('#invstScientificEvd').val(data.invstScientificEvd);
                        $('#invstPromptness').val(data.invstPromptness);
                        $('#invstAttachConfis').val(data.invstAttachConfis);
                        $('#invstChallenges').val(data.invstChallenges);
                        $('#invstConvcDtls').val(data.invstConvcDtls);
                        $('#originalJudgementCopy').val(data.originalJudgementCopy);
                        $("#selScientificTool").val(data.selScientificTool);
                        $("#selInvstInnovMethods").val(data.selInvstInnovMethods);
                        $("#selInvstScientificEvd").val(data.selInvstScientificEvd);
                        $("#selInvstAttachConfis").val(data.selInvstAttachConfis);
                        if (data.originalJudgementCopy == undefined) {
                            $("#upload").show();
                        } else {
                            $("#upload").hide();
                            $("#downloadJcopy").empty();
                            $("#downloadJcopy").append('<a href="downloadJudgementCopy.htm?attachId=' + invstCaseId + ' &target=_blank">' + data.originalJudgementCopy + '</a>');
                            $("#download").show();
                        }

                        // $("#upload").hide();

                    },
                    error: function () {
                        $("#msg").html("Error Occured");
                        $("#msg").css({"color": "red", "fontWeight": "bold"});
                        alert('Data not Found');
                    }
                });
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <% int i = 1;%>
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>     
            <form:form action="recommendAwardByRangeForm.htm" commandName="AwardMedalListForm" enctype="multipart/form-data">
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> RECOMMENDATION FOR AWARD OF CHIEF MINISTER'S MEDAL FOR EXCELLENCE IN INVESTIGATION </h3>

                        <div class="panel-heading">
                            <a href="awardormedalListForRangeOffice.htm?awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&awardYear=${AwardMedalListForm.awardYear}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                                <c:if test="${empty AwardMedalListForm.rangeSubmittedOn}">
                                <input type="submit" value="Save and Submit Form" name="btn" class="btn btn-success" onclick="return validateForm()"/>
                            </c:if>
                        </div>
                        <div class ="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="officeName">Name of the Range/Office</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="offName" id="offName" class="form-control" readonly="true" />
                                    <form:hidden path="offCode"/>
                                    <form:hidden path="awardMedalTypeId"/>
                                    <form:hidden path="awardYear"/>
                                    <form:hidden path="rewardMedalId"/>
                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fullname"> Name (In Capitals)</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="fullname" id="fullname" class="form-control" readonly="true"/>
                                    <form:hidden path="empId"/>
                                </div>
                                <div class="col-lg-3">

                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameSB}">
                                        <a href="downloadServiceBookCopyForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameSB}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>

                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fathername"> Father's Name </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="fathername" id="fathername" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-3">

                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dob"> Date of Birth </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dob" id="dob" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dob"> As on 01-01-2021 </label>
                                </div>
                                <div class="col-lg-3">
                                    ${AwardMedalListForm.ageInYear} Year
                                </div>
                                <div class="col-lg-3">
                                    ${AwardMedalListForm.ageInMonth} Month
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="gender"> Gender </label>
                                </div>
                                <div class="col-lg-3">

                                    <form:select path="gender" id="gender" readonly="true" size="1" class="form-control">
                                        <form:option value="M">Male</form:option>
                                        <form:option value="F">Female</form:option>
                                        <form:option value="T">Transgender</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-3">

                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointYear"> Initial appointment </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointYear"> Year </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointRank"> Rank </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointCadre"> Cadre </label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointYear">  </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="initialAppointYear" id="initialAppointYear" class="form-control" maxlength="4" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="initialAppointRank" id="initialAppointRank" readonly="true" size="1" class="form-control">
                                        <form:option value="">--Select One--</form:option>
                                        <form:option value="CO">Constable</form:option>
                                        <form:option value="SI">SI</form:option>
                                        <form:option value="DS">DSP</form:option>
                                        <form:option value="AS">ASP</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="initialAppointCadre" id="initialAppointCadre" readonly="true" size="1" class="form-control">
                                        <form:option value="">--Select One--</form:option>
                                        <form:option value="A">Group-A</form:option>
                                        <form:option value="B">Group-B</form:option>
                                        <form:option value="C">Group-C</form:option>
                                        <form:option value="D">Group-D</form:option>
                                    </form:select>
                                </div>
                            </div>    
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="designation"> Present Rank </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="designation" id="designation" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="datePresentRank"> Date since when in present rank </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="datePresentRank" id="datePresentRank" class="form-control" readonly="true"/>
                                </div>
                            </div>   

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="presentPosting"> Present Posting </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="presentPosting" id="presentPosting" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingAddress"> Complete Postal Address  </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="postingAddress" id="postingAddress" class="form-control" rows="4" cols="60" maxlength="350" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-11">
                                    <label for="moneyReward">  Nos. of Cash reward/ Good entries </label>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="moneyReward"> i) Cash Reward </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="moneyReward" id="moneyReward" class="form-control" maxlength="3" readonly="true"/>
                                </div>
                            </div>  

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="appreciation"> ii) Appreciation </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="appreciation" id="appreciation" class="form-control" maxlength="3" readonly="true"/>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="otherReward"> iii) Other </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="otherReward" id="otherReward" class="form-control" maxlength="3" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-11">
                                    <label for="punishmentMajor">  Punishment(s) in the last 5 years </label>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-2">
                                    <label> Major </label>
                                </div>
                                <div class="col-lg-1">
                                    <label> Year </label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Minor </label>
                                </div>
                                <div class="col-lg-1">
                                    <label> Year </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-2">
                                    <label><form:input path="punishmentmajor1" id="punishmentmajor1" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-1">
                                    <label> <form:input path="punishmentmajyear1"  value="2020" id="punishmentmajyear1" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-2">
                                    <label> <form:input path="punishmentminor1" id="punishmentminor1" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-1">
                                    <label> <form:input path="punishmentminyear1"  value="2020" id="punishmentminyear1" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-2">
                                    <label><form:input path="punishmentmajor2" id="punishmentmajor2" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-1">
                                    <label> <form:input path="punishmentmajyear2"  value="2019" readonly="true" id="punishmentmajyear2" class="form-control"/></label>
                                </div>
                                <div class="col-lg-2">
                                    <label> <form:input path="punishmentminor2" id="punishmentminor2" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-1">
                                    <label> <form:input path="punishmentminyear2" value="2019" id="punishmentminyear2" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-2">
                                    <label><form:input path="punishmentmajor3" id="punishmentmajor3" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-1">
                                    <label> <form:input path="punishmentmajyear3" value="2018" id="punishmentmajyear3" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-2">
                                    <label> <form:input path="punishmentminor3" id="punishmentminor3" readonly="true"  class="form-control"/></label>
                                </div>
                                <div class="col-lg-1">
                                    <label> <form:input path="punishmentminyear3" value="2018" readonly="true" id="punishmentminyear3" class="form-control"/></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-2">
                                    <label><form:input path="punishmentmajor4" id="punishmentmajor4" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-1">
                                    <label> <form:input path="punishmentmajyear4" value="2017" readonly="true" id="punishmentmajyear4" class="form-control"/></label>
                                </div>
                                <div class="col-lg-2">
                                    <label> <form:input path="punishmentminor4" id="punishmentminor4" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-1">
                                    <label> <form:input path="punishmentminyear4" value="2017" readonly="true" id="punishmentminyear4" class="form-control"/></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-2">
                                    <label><form:input path="punishmentmajor5" id="punishmentmajor5" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-1">
                                    <label> <form:input path="punishmentmajyear5" value="2016" id="punishmentmajyear5" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-2">
                                    <label> <form:input path="punishmentminor5" id="punishmentminor5" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-1">
                                    <label> <form:input path="punishmentminyear5" value="2016" id="punishmentminyear5" readonly="true" class="form-control"/></label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="pendingEnquiryDetails">Details of any enquiry pending against the officer.</label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:select path="Ifenquirypending" id="Ifenquirypending" readonly="true" class="form-control" onclick="togglePendingEnquiryDiv()">
                                        <form:option value=""> No </form:option>
                                        <form:option value="Y"> Yes </form:option>
                                    </form:select>
                                </div>


                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="hidePendingDocumentRow1">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="pendingEnquiryDetails"> Upload document  </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameenquiryDoc}">
                                        <a href="downloadEnquiryDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameenquiryDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameenquiryDoc}">
                                        <input type="file" name="enquiryDoc"  id="enquiryDoc"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>
                            </div>    
                            <div class="row" style="margin-bottom: 7px;" id="hidePendingDocumentRow2">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="pendingEnquiryDetails"> give  details </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="pendingEnquiryDetails" readonly="true" id="pendingEnquiryDetails" class="form-control" rows="4" cols="100" maxlength="450"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;" id="hidePendingDocumentRow3">
                                <div class="col-lg-3">
                                    <label> </label>
                                </div>
                                <div class="col-lg-9">
                                    <label style="color:red"> (Maximum 450 characters allowed) </label>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="dpcifany"> Whether any Criminal/ Vigilance cases/ Departmental Proceeding is pending against him/her.</label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dpcifany" readonly="true" id="dpcifany" class="form-control" onclick="toggleDiv()">
                                        <form:option value=""> No </form:option>
                                        <form:option value="Y"> Yes </form:option>
                                    </form:select>
                                </div>


                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="hideDiscDocumentRow">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> Upload document  </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileName}">
                                        <a href="downloadDPCForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileName}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileName}">
                                        <input type="file" name="discDocument"  id="discDocument"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>
                            </div>    
                            <div class="row" style="margin-bottom: 7px;" id="hideDiscDocumentRow2">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> give  details </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="discDetails" readonly="true" id="discDetails" class="form-control" rows="4" cols="100" maxlength="450"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;" id="hideDiscDocumentRow3">
                                <div class="col-lg-3">
                                    <label> </label>
                                </div>
                                <div class="col-lg-9">
                                    <label style="color:red"> (Maximum 450 characters allowed) </label>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="dpcifany"> Details of the court cases pending against the recommended. if any</label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:select path="courtcaseifany" readonly="true" id="courtcaseifany" class="form-control" onclick="toggleCourtCaseDiv()">
                                        <form:option value=""> No </form:option>
                                        <form:option value="Y"> Yes </form:option>
                                    </form:select>
                                </div>


                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="courtcase1">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="pendingEnquiryDetails"></label>
                                </div>
                                <div class="col-lg-3" >
                                    <label for="courtCaseYear"> Court Case Year </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="courtCaseDetails"> Details of Charge </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="courtCaseStatus"> Present Status </label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="courtcase2">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:input path="courtCaseYear" id="courtCaseYear" readonly="true" class="form-control" maxlength="4"  onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="courtCaseDetails" id="courtCaseDetails" readonly="true" class="form-control" maxlength="100" />
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="courtCaseStatus" id="courtCaseStatus" readonly="true"  class="form-control" maxlength="100"/>
                                </div>
                            </div>    
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="pendingEnquiryDetails"> ACR grading for last 5 years.</label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="acrYear1"> Year </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="acrYear1Grading"> Grading </label>
                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:input path="acrYear1" id="acrYear1" value="2015-16" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="acrYear1Grading" readonly="true" id="acrYear1Grading" class="form-control" onclick="toggleCourtCaseDiv()">
                                        <form:option value=""> --Select One-- </form:option>
                                        <form:option value="O"> Outstanding </form:option>
                                        <form:option value="V"> Very good </form:option>
                                        <form:option value="G"> Good </form:option>
                                        <form:option value="A"> Average </form:option>
                                        <form:option value="N"> NRC </form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:input path="acrYear2" id="acrYear2" value="2016-17" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="acrYear2Grading" readonly="true" id="acrYear2Grading" class="form-control" onclick="toggleCourtCaseDiv()">
                                        <form:option value=""> --Select One-- </form:option>
                                        <form:option value="O"> Outstanding </form:option>
                                        <form:option value="V"> Very good </form:option>
                                        <form:option value="G"> Good </form:option>
                                        <form:option value="A"> Average </form:option>
                                        <form:option value="N"> NRC </form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:input path="acrYear3" id="acrYear3" value="2017-18" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="acrYear3Grading" readonly="true" id="acrYear3Grading" class="form-control" onclick="toggleCourtCaseDiv()">
                                        <form:option value=""> --Select One-- </form:option>
                                        <form:option value="O"> Outstanding </form:option>
                                        <form:option value="V"> Very good </form:option>
                                        <form:option value="G"> Good </form:option>
                                        <form:option value="A"> Average </form:option>
                                        <form:option value="N"> NRC </form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:input path="acrYear4" id="acrYear4" value="2018-19" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="acrYear4Grading" readonly="true" id="acrYear4Grading" class="form-control" onclick="toggleCourtCaseDiv()">
                                        <form:option value=""> --Select One-- </form:option>
                                        <form:option value="O"> Outstanding </form:option>
                                        <form:option value="V"> Very good </form:option>
                                        <form:option value="G"> Good </form:option>
                                        <form:option value="A"> Average </form:option>
                                        <form:option value="N"> NRC </form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:input path="acrYear5" id="acrYear5" value="2019-20" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="acrYear5Grading" readonly="true" id="acrYear5Grading" class="form-control" onclick="toggleCourtCaseDiv()">
                                        <form:option value=""> --Select One-- </form:option>
                                        <form:option value="O"> Outstanding </form:option>
                                        <form:option value="V"> Very good </form:option>
                                        <form:option value="G"> Good </form:option>
                                        <form:option value="A"> Average </form:option>
                                        <form:option value="N"> NRC </form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="dpcifany">Any  criminal case pending against the officer</label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:select path="criminalcaseifany" id="criminalcaseifany" readonly="true" class="form-control" onclick="toggleCriminalcaseDiv()">
                                        <form:option value=""> No </form:option>
                                        <form:option value="Y"> Yes </form:option>
                                    </form:select>
                                </div>


                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="hideCriminalcaseRow">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> Upload document  </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamesrDocument}">
                                        <a href="downloadSrDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamesrDocument}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNamesrDocument}">
                                        <input type="file" name="srDocument"  id="srDocument"/> <span style="color:red">(Only PDF, file size maximum 3 MB  )</span>
                                    </c:if>
                                </div>
                            </div>    
                            <div class="row" style="margin-bottom: 7px;" id="hideCriminalcaseRow1">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> give  details </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="annexure2Details" id="annexure2Details" readonly="true" class="form-control" rows="4" cols="100" maxlength="450"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;" id="hideCriminalcaseRow2">
                                <div class="col-lg-3">
                                    <label> </label>
                                </div>
                                <div class="col-lg-9">
                                    <label style="color:red"> (Maximum 450 characters allowed) </label>
                                </div>
                            </div>   

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="dpcifany">  Has the officer been chargesheeted in any case</label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:select path="chargesheetedifany" id="chargesheetedifany" readonly="true" class="form-control" onclick="toggleChargesheetedDiv()">
                                        <form:option value=""> No </form:option>
                                        <form:option value="Y"> Yes </form:option>
                                    </form:select>
                                </div>


                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="hideChargesheetedRow">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> Upload document  </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamenonSrDocument}">
                                        <a href="downloadNonSrDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamenonSrDocument}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNamenonSrDocument}">
                                        <input type="file" name="nonSrDocument"  id="nonSrDocument"/> <span style="color:red">(Only PDF, file size maximum 3 MB  )</span>
                                    </c:if>
                                </div>
                            </div>    
                            <div class="row" style="margin-bottom: 7px;" id="hideChargesheetedRow1">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> give  details </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="annexure3Details" readonly="true" id="annexure3Details" class="form-control" rows="4" cols="100" maxlength="450"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;" id="hideChargesheetedRow2">
                                <div class="col-lg-3">
                                    <label> </label>
                                </div>
                                <div class="col-lg-9">
                                    <label style="color:red"> (Maximum 450 characters allowed) </label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="dpcifany">  Proceeding of the meeting</label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:select path="meetingProceeding" id="meetingProceeding" class="form-control" onclick="toggleProceedingDiv()">
                                        <form:option value=""> No </form:option>
                                        <form:option value="Y"> Yes </form:option>
                                    </form:select>
                                </div>


                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="hideProceedingRow">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> Upload document  </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameProccdingDocument}">
                                        <a href="downloadProceedingmeetingDoc.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameProccdingDocument}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameProccdingDocument}">
                                        <input type="file" name="proceedingDocument"  id="proceedingDocument"/> <span style="color:red">(Only PDF, file size maximum 3 MB  )</span>
                                    </c:if>
                                </div>
                            </div>    
                            <div class="row" style="margin-bottom: 7px;" id="hideProceedingRow1">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> give  details </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="proceedingDetails" id="proceedingDetails" class="form-control" rows="4" cols="100" maxlength="450"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;" id="hideProceedingRow2">
                                <div class="col-lg-3">
                                    <label> </label>
                                </div>
                                <div class="col-lg-9">
                                    <label style="color:red"> (Maximum 450 characters allowed) </label>
                                </div>
                            </div>   




                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="recommendStatusofRange"> Recommendation Status for Range </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:select path="recommendStatusofRange" id="recommendStatusofRange" class="form-control" onclick="togglerangeStatus()">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="RECOMMENDED">RECOMMENDED</form:option>
                                        <form:option value="NOT RECOMMENDED">NOT RECOMMENDED</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="hidereasonFornotRecommend">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="reasonFornotRecommend">Reason for Not Recommend</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="reasonFornotRecommend" id="reasonFornotRecommend" class="form-control" maxlength="90"/>
                                </div>
                            </div>

                        </div>
                        <c:if test="${not empty srCaseList}">                    
                            <div class="table-responsive" style="margin-top:5px;">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped" id="tabid">
                                        <thead>
                                            <tr><th colspan="9" style="color:#005CB9; font-size: 15px;text-align:center;">Details of all SR cases of 2020 investigated by IO, ${AwardMedalListForm.fullname},${AwardMedalListForm.designation}
                                                    Exclude part investigation cases
                                                    For C.M’s Medal for Excellence in Investigation
                                                </th></tr>
                                            <tr >
                                                <th width="5%" style=" font-size: 14px;" class="font">Sl. No.</th>
                                                <th width="10%" style="font-size: 14px;">Case No. & Date of Registration with Sections of the law</th>
                                                <th width="15%" style="font-size: 14px;">Specify if CS or FR Date of Chargesheet/FR Time taken in Submission of CS/FR</th>
                                                <th width="15%" style=" font-size: 14px;">Details of use of the DFSL Team/Use of Polygraph/Fingerprint Match if any/DNA profiling if any</th>
                                                <th width="10%" style="font-size: 14px;">No.of Exhibits sent for Forensic Examination</th>
                                                <th width="15%" style=" font-size: 14px;">Attachment and Confiscation of the Proceed of Crime(wherever applicable)</th>
                                                <th width="15%" style=" font-size: 14px;">Conviction Details if any</th>
                                                <th width="5%" style=" font-size: 14px;">Edit</th>
                                                <th width="5%" style="font-size: 14px;">Delete</th>
                                            </tr>
                                        </thead>
                                        <tbody>

                                            <c:forEach items="${srCaseList}" var="srcase" varStatus="counter">
                                                <tr>
                                                    <td style="font-size: 15px;">${counter.count} </td>
                                                    <td style="font-size: 15px;">${srcase.srCaseNo}</br>${srcase.srRegDate}</td>
                                                    <td style="font-size: 15px;">${srcase.frDate}</td>
                                                    <td style="font-size: 15px;">${srcase.dfslTeamDtls}</td>
                                                    <td style="font-size: 15px;"> ${srcase.exhibitsNoSent}</td>
                                                    <td style="font-size: 15px;"> ${srcase.srAttachment}</td>
                                                    <td style="font-size: 15px;"> ${srcase.convictionDtls}</td>

                                                    <td style="font-size: 15px;">

                                                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#addSRCasesModal" onclick="editSRCases('${srcase.srCaseId}')">
                                                            <span class="glyphicon glyphicon-file"></span> Edit
                                                        </button>

                                                    </td>

                                                    <td style="font-size: 15px;">
                                                        <c:if test="${empty AwardMedalListForm.submittedOn}">
                                                            <a href="deleteSRForm.htm?action=Delete&srCaseId=${srcase.srCaseId}&awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&rewardMedalId=${AwardMedalListForm.rewardMedalId}" class="btn btn-success btn-sm"  onclick="return confirm('Are you sure to Delete?')"><span class="glyphicon glyphicon-file"></span>Delete</a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:if>  
                        <c:if test="${not empty invstCaseList}"> 
                            <div class="table-responsive" style="margin-top:5px;">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped" id="tabid">
                                        <thead>
                                            <tr><th colspan="9" style="color:#005CB9; font-size: 15px;text-align:center;">DETAILS OF BEST 2 CASES INVESTIGATED (part investigation to be excluded) AND/OR DETAILS OF CONVICTION RECEIVED (may include cases registered earlier)
                                                </th></tr>
                                            <tr >
                                                <th width="5%" style=" font-size: 14px;" class="font">Sl. No.</th>
                                                <th width="25%" style="font-size: 14px;">PS Name</th>
                                                <th width="20%" style="font-size: 14px;">Case No</th>
                                                <th width="15%" style=" font-size: 14px;">Date Of Registration</th>
                                                <th width="10%" style="font-size: 14px;">CS/FR</th>
                                                <th width="15%" style=" font-size: 14px;">SR/Non ST</th>
                                                <th width="5%" style=" font-size: 14px;">Edit</th>
                                                <th width="5%" style="font-size: 14px;">Delete</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${invstCaseList}" var="invstcase" varStatus="counter">
                                                <tr> 
                                                    <td style="font-size: 15px;">${counter.count} </td>
                                                    <td style="font-size: 15px;">${invstcase.psName}</td>
                                                    <td style="font-size: 15px;">${invstcase.invstCaseNo}</td>
                                                    <td style="font-size: 15px;">${invstcase.invstRegdate}</td>
                                                    <td style="font-size: 15px;"> ${invstcase.invstCsFr}</td>
                                                    <td style="font-size: 15px;"> ${invstcase.invstSrNonSr}</td>
                                                    <td style="font-size: 15px;">
                                                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#bestTwoCasesModal" onclick="editInvstCases('${invstcase.invstcaseId}')">
                                                            <span class="glyphicon glyphicon-file"></span> Edit
                                                        </button>
                                                    </td>

                                                    <td style="font-size: 15px;">
                                                        <c:if test="${empty AwardMedalListForm.submittedOn}">
                                                            <a href="deleteInvstCase.htm?action=Delete&invstcaseId=${invstcase.invstcaseId}&awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&rewardMedalId=${AwardMedalListForm.rewardMedalId}" class="btn btn-success btn-sm"  onclick="return confirm('Are you sure to Delete?')"><span class="glyphicon glyphicon-file"></span>Delete</a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${not empty nonSrCaseList}"> 
                            <div class="table-responsive" style="margin-top:5px;">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped" id="tabid">
                                        <thead>
                                            <tr><th colspan="15" style="color:#005CB9; font-size: 15px;text-align:center;">No of cases investigated by IO, ${AwardMedalListForm.fullname},${AwardMedalListForm.designation}
                                                    For C.M’s Medal for Excellence in Investigation
                                                </th></tr>
                                            <tr >
                                                <th width="6%" style=" font-size: 14px;" class="font">Sl. No.</th>
                                                <th width="21%" colspan="3" style="font-size: 14px;">No. Of Non SR cases investigated(exclude part investigation)</th>
                                                <th width="21%" colspan="3" style="font-size: 14px;">No. Of Non SR cases in which Investigation finalized in 30 days</th>
                                                <th width="21%" colspan="3" style=" font-size: 14px;">No. Of Non SR cases in which Investigation finalized after 30 days</th>
                                                <th width="21%" colspan="3" style="font-size: 14px;">Investigation still pending</th>
                                                <th width="5%" style=" font-size: 14px;">Edit</th>
                                                <th width="5%" style="font-size: 14px;">Delete</th>
                                            </tr>
                                            <tr >
                                                <th width="6%" style=" font-size: 14px;" class="font"></th>
                                                <th width="7%" style="font-size: 14px;">2018</th>
                                                <th width="7%" style="font-size: 14px;">2019</th>
                                                <th width="7%" style=" font-size: 14px;">2020</th>
                                                <th width="7%" style="font-size: 14px;">2018</th>
                                                <th width="7%" style="font-size: 14px;">2019</th>
                                                <th width="7%" style=" font-size: 14px;">2020</th>
                                                <th width="7%" style="font-size: 14px;">2018</th>
                                                <th width="7%" style="font-size: 14px;">2019</th>
                                                <th width="7%" style=" font-size: 14px;">2020</th>
                                                <th width="7%" style="font-size: 14px;">2018</th>
                                                <th width="7%" style="font-size: 14px;">2019</th>
                                                <th width="7%" style=" font-size: 14px;">2020</th>
                                                <th width="5%" style=" font-size: 14px;"></th>
                                                <th width="5%" style="font-size: 14px;"></th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                            <c:forEach items="${nonSrCaseList}" var="nonsrcase" varStatus="counter">
                                                <tr> 
                                                    <td style="font-size: 15px;">${counter.count} </td>
                                                    <td style="font-size: 15px;">${nonsrcase.noofcases2018} </td>
                                                    <td style="font-size: 15px;">${nonsrcase.noofcases2019}</td>
                                                    <td style="font-size: 15px;">${nonsrcase.noofcases2020}</td>
                                                    <td style="font-size: 15px;">${nonsrcase.noofcasesin2018}</td>
                                                    <td style="font-size: 15px;">${nonsrcase.noofcasesin2019}</td>
                                                    <td style="font-size: 15px;">${nonsrcase.noofcasesin2020}</td>
                                                    <td style="font-size: 15px;">${nonsrcase.noofcasesafter2018} </td>
                                                    <td style="font-size: 15px;">${nonsrcase.noofcasesafter2019}</td>
                                                    <td style="font-size: 15px;">${nonsrcase.noofcasesafter2020}</td>
                                                    <td style="font-size: 15px;">${nonsrcase.pendingcases2018} </td>
                                                    <td style="font-size: 15px;">${nonsrcase.pendingcases2019}</td>
                                                    <td style="font-size: 15px;">${nonsrcase.pendingcases2020}</td>
                                                    <td style="font-size: 15px;">
                                                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#nonsrcasesdtls" onclick="editnonSrcaseCases('${nonsrcase.nonSrcaseId}')">
                                                            <span class="glyphicon glyphicon-file"></span> Edit
                                                        </button>

                                                    </td>
                                                    <td style="font-size: 15px;">
                                                        <c:if test="${empty AwardMedalListForm.submittedOn}">
                                                            <a href="deleteNonSrCaseCase.htm?action=Delete&nonSrcaseId=${nonsrcase.nonSrcaseId}&awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&rewardMedalId=${AwardMedalListForm.rewardMedalId}" class="btn btn-success btn-sm"  onclick="return confirm('Are you sure to Delete?')"><span class="glyphicon glyphicon-file"></span>Delete</a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:if>                    
                        <div id="addSRCasesModal" class="modal fade" role="dialog">
                            <div class="modal-dialog" style="width:1000px;">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">

                                        <h4 class="modal-title">Add SR Cases</h4>
                                        <form:hidden path="srCaseId" id="srCaseId"/>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-7">
                                                <label >Case No</label>
                                            </div>

                                            <div class="col-lg-4">
                                                <form:input class="form-control" path="srCaseNo" id="srCaseNo" />

                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-7">
                                                <label >Date Of registration</label>
                                            </div>

                                            <div class="col-lg-4">

                                                <form:input class="form-control" path="regDate" id="regDate" readonly="true"/>


                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-7">
                                                <label >CS/FR Date of Chargesheet</label>
                                            </div>
                                            <div class="col-lg-4">

                                                <form:input class="form-control" path="frDate" id="frDate" readonly="true"/>


                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-7">
                                                <label>Details of use of the DFSL Team/Use of Polygraph/Fingerprint Match if any/DNA profiling</label>
                                            </div>
                                            <div class="col-lg-4">
                                                <form:input class="form-control" path="dtlsTeamUse" id="dtlsTeamUse"  />

                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-7">
                                                <label>No.of Exhibits sent for Forensic Examination</label>
                                            </div>
                                            <div class="col-lg-4">
                                                <form:input class="form-control" path="noOfExhibits" id="noOfExhibits"  />

                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-7">
                                                <label>Attachment and Confiscation of the Proceed of Crime(wherever applicable)</label>
                                            </div>
                                            <div class="col-lg-4">
                                                <form:input class="form-control" path="crimeProceedAttach"  id="crimeProceedAttach" /> 

                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-7">
                                                <label>Conviction Details if any</label>
                                            </div>
                                            <div class="col-lg-4">
                                                <form:input class="form-control" path="convictionDtls" id="convictionDtls" />
                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>

                                        <div class="modal-footer">
                                            <c:if test="${empty AwardMedalListForm.submittedOn}">
                                                <input type="submit" value="Add" name="btn" class="btn btn-default" /> 
                                            </c:if>

                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>                      
                        </div>
                        <%i = 1;%>                    
                        <div id="bestTwoCasesModal" class="modal fade" role="dialog">
                            <div class="modal-dialog" style="width:1500px;">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <form:hidden path="invstCaseId"/>
                                        <h4 class="modal-title">BEST 2 CASES INVESTIGATED / CONVICTION RECEIVED</h4>

                                    </div>
                                    <div class="modal-body">
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-1">
                                                <label><%=i++%>.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="dob"> PS Name </label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:input class="form-control" path="psName" id="psName" />
                                            </div>
                                            <div class="col-lg-1">
                                                <label><%=i++%>.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="doa"> Case No. with sections of law </label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:textarea class="form-control" path="invstCaseNo" id="invstCaseNo" rows="4" cols="50" maxlength="500"/>
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-1">
                                                <label><%=i++%>.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="dob">Date of registration</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:input class="form-control" path="invstDateReg" id="invstDateReg" readonly="true" onblur="populateNoOfDays();"/>

                                            </div>
                                            <div class="col-lg-1">
                                                <label><%=i++%>.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="doa"> CS or FR </label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:radiobutton path="invstCsFr" id="invstCsFrUt" value="UT" onclick="toggleCsFr()"/> <label for="invstCsFrUt">Under Trial</label>
                                                <form:radiobutton path="invstCsFr" id="invstCsFrPl" value="PL" onclick="toggleCsFr()"/> <label for="invstCsFrPl">Preliminary</label>
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;" >
                                            <div id="rowFinalSub">
                                                <div class="col-lg-1">
                                                    <label><%=i++%>.</label>
                                                </div>
                                                <div class="col-lg-2">
                                                    <label for="dob">Date of submission of final form</label>
                                                </div>
                                                <div class="col-lg-3">
                                                    <form:input class="form-control" path="invstFinalSubDate" id="invstFinalSubDate" readonly="true" onblur="populateNoOfDays();"/>
                                                </div>
                                            </div>
                                            <div>
                                                <div class="col-lg-1">
                                                    <label><%=i++%>.</label>
                                                </div>
                                                <div class="col-lg-2">
                                                    <label for="doa"> SR or Non SR </label>
                                                </div>
                                                <div class="col-lg-3">
                                                    <form:select path="invstSrNonsr" id="invstSrNonsr" class="form-control">
                                                        <form:option value="">--Select--</form:option>
                                                        <form:option value="SR">SR</form:option>
                                                        <form:option value="NSR">NON SR</form:option>
                                                    </form:select>

                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-1">
                                                <label>a.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="dob">Brief of the case</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:textarea class="form-control" path="invstBriefCase" id="invstBriefCase" rows="4" cols="50" />
                                            </div>
                                            <div class="col-lg-1">
                                                <label>b.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="doa"> Innovative methods/ specific professional investigative skill used</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:select path="selInvstInnovMethods" id="selInvstInnovMethods" class="form-control" onclick="toggleInvstInnovMethods()">
                                                    <form:option value="">--Select--</form:option>
                                                    <form:option value="">No</form:option>
                                                    <form:option value="Y">Yes</form:option>
                                                </form:select>
                                                <div  style="margin-top: 7px;" id="hideInvstInnovMethods">
                                                    <form:textarea class="form-control" path="invstInnovMethods" id="invstInnovMethods" rows="4" cols="50"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-1">
                                                <label>c.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="dob">Use of scientific aids and forensic tools in investigation</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:select path="selScientificTool" id="selScientificTool" class="form-control" onclick="toggleSelScientificTool()">
                                                    <form:option value="">--Select--</form:option>
                                                    <form:option value="">No</form:option>
                                                    <form:option value="Y">Yes</form:option>
                                                </form:select>
                                                <div  style="margin-top: 7px;" id="hideInvstScientificAids">
                                                    <form:textarea class="form-control" path="invstScientificAids" id="invstScientificAids" rows="4" cols="50"/>
                                                </div>
                                            </div>
                                            <div class="col-lg-1">
                                                <label>d.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="doa"> Arraigning of scientific evidence</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:select path="selInvstScientificEvd" id="selInvstScientificEvd" class="form-control" onclick="toggleInvstScientificEvd()">
                                                    <form:option value="">--Select--</form:option>
                                                    <form:option value="">No</form:option>
                                                    <form:option value="Y">Yes</form:option>
                                                </form:select>
                                                <div  style="margin-top: 7px;" id="hideInvstScientificEvd">
                                                    <form:textarea class="form-control" path="invstScientificEvd" id="invstScientificEvd" rows="4" cols="50" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-1">
                                                <label>e.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="dob">Promptness in filing charge sheet</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:input class="form-control" path="invstPromptness" id="invstPromptness" readonly="true" />
                                            </div>
                                            <div class="col-lg-1">
                                                <label>f.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="doa"> Attachment and confiscation of crime proceeds(if applicable)</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:select path="selInvstAttachConfis" id="selInvstAttachConfis" class="form-control" onclick="toggleInvstAttachConfis()">
                                                    <form:option value="">--Select--</form:option>
                                                    <form:option value="">No</form:option>
                                                    <form:option value="Y">Yes</form:option>
                                                </form:select>
                                                <div  style="margin-top: 7px;" id="hideInvstAttachConfis">
                                                    <form:textarea class="form-control" path="invstAttachConfis" id="invstAttachConfis" rows="4" cols="50" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-1">
                                                <label>g.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="dob">Skills displayed in overcoming challenges in investigation</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:textarea class="form-control" path="invstChallenges" id="invstChallenges" rows="4" cols="50" />
                                            </div>
                                            <div class="col-lg-1">
                                                <label>h.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="doa"> Conviction details</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:textarea class="form-control" path="invstConvcDtls" id="invstConvcDtls" rows="4" cols="50" />
                                            </div>
                                        </div>

                                        <div class="row" style="margin-bottom: 7px;">
                                            <div class="col-lg-1">
                                                <label></label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label>Judgement copy( if available)</label>
                                            </div>

                                            <div class="col-lg-3">
                                                <span id="download">
                                                    <span id="downloadJcopy">
                                                    </span>
                                                </span>
                                                <span id="upload">
                                                    <input type="file" name="judgementCopy"  id="judgementCopy" /> <span style="color:red">(Only PDF)</span>
                                                </span>
                                            </div>
                                            <div class="col-lg-1">
                                            </div>
                                        </div>

                                        <div class="modal-footer">
                                            <c:if test="${empty AwardMedalListForm.submittedOn}">
                                                <input type="submit" value="Enter Case" name="entercase" class="btn btn-default" onclick="return validateInvestigationForm()"/>
                                            </c:if>
                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>                      
                        </div>   
                        <div id="nonsrcasesdtls" class="modal fade" role="dialog">
                            <div class="modal-dialog" style="width:1000px;">
                                <!-- Modal content-->
                                <div class="modal-content">
                                    <div class="modal-header">

                                        <h4 class="modal-title">DETAILS OF SR & NON SR CASES INVESTIGATED FROM 2018 TO 2020</h4>
                                        <form:hidden path="nonSrcaseId"/>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row" style="border-top: 1px solid;"> 
                                            <div class="col-xs-4 col-sm-4 col-md-1" >

                                            </div> 
                                            <div class="col-xs-12 col-sm-4 col-md-5" style=" border-right: 1px solid black;height: 40px;">

                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-3" style=" border-right: 1px solid black;height: 40px;">
                                                Non SR
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-3" >
                                                SR
                                            </div>
                                        </div>
                                        <div class="row" style="border-top: 1px solid;"> 
                                            <div class="col-xs-12 col-sm-12 col-md-1" >
                                                1
                                            </div> 
                                            <div class="col-xs-12 col-sm-12 col-md-5" style=" border-left: 1px solid black;height: 135px;">
                                                No. Of cases investigated(exclude part investigation)
                                            </div>

                                            <div class="col-xs-12 col-sm-12 col-md-1" style=" border-left: 1px solid black;">
                                                <div class="row" >
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        2018
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        2019
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        2020
                                                    </div>

                                                </div>

                                            </div>

                                            <div class="col-xs-12 col-sm-12 col-md-2" style=" border-left: 1px solid black;">
                                                <div class="row" style=" border-right: 1px solid black;">
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noOfNonSrcaseInvst2018" id="noOfNonSrcaseInvst2018" />
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noOfNonSrcaseInvst2019" id="noOfNonSrcaseInvst2019" />
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        <form:input class="form-control" path="noOfNonSrcaseInvst2020" id="noOfNonSrcaseInvst2020" />
                                                    </div>

                                                </div>
                                            </div>

                                            <div class="col-xs-12 col-sm-12 col-md-3" style=" border-right: 1px solid black;">
                                                <div class="row" style=" border-right: 1px solid black;">
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noOfSrcaseInvst2018" id="noOfSrcaseInvst2018" />
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noOfSrcaseInvst2019" id="noOfSrcaseInvst2019" />
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        <form:input class="form-control" path="noOfSrcaseInvst2020" id="noOfSrcaseInvst2020" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" style="border-top: 1px solid;"> 
                                            <div class="col-xs-12 col-sm-12 col-md-1" >
                                                2
                                            </div> 
                                            <div class="col-xs-12 col-sm-12 col-md-5" style=" border-left: 1px solid black;height: 135px;">
                                                No. Of cases in which Investigation finalized in 30 days
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-1" style=" border-left: 1px solid black;">
                                                <div class="row" >
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        2018
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        2019
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        2020
                                                    </div>

                                                </div>
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-2" style=" border-left: 1px solid black;">
                                                <div class="row" style=" border-right: 1px solid black;">
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noFinalizedInThirtydays2018" id="noFinalizedInThirtydays2018" />
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noFinalizedInThirtydays2019" id="noFinalizedInThirtydays2019" />
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        <form:input class="form-control" path="noFinalizedInThirtydays2020" id="noFinalizedInThirtydays2020" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-xs-12 col-sm-12 col-md-3">
                                                <div class="row" style=" border-right: 1px solid black;">
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noSrFinalizedInThirtydays2018" id="noSrFinalizedInThirtydays2018" />
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noSrFinalizedInThirtydays2019" id="noSrFinalizedInThirtydays2019" />
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        <form:input class="form-control" path="noSrFinalizedInThirtydays2020" id="noSrFinalizedInThirtydays2020" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" style="border-top: 1px solid;"> 
                                            <div class="col-xs-12 col-sm-12 col-md-1" >
                                                3
                                            </div> 
                                            <div class="col-xs-12 col-sm-12 col-md-5" style=" border-left: 1px solid black;height: 135px;">
                                                No. Of cases in which Investigation finalized after 30 days
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-1" style=" border-left: 1px solid black;">
                                                <div class="row" >
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        2018
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        2019
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        2020
                                                    </div>

                                                </div>
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-2" style=" border-left: 1px solid black;">
                                                <div class="row" style=" border-right: 1px solid black;">
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noFinalizedAfterThirtydays2018" id="noFinalizedAfterThirtydays2018" />
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noFinalizedAfterThirtydays2019" id="noFinalizedAfterThirtydays2019" />
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        <form:input class="form-control" path="noFinalizedAfterThirtydays2020" id="noFinalizedAfterThirtydays2020" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-3" style=" border-right: 1px solid black;">
                                                <div class="row" style=" border-right: 1px solid black;">
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noSrFinalizedAfterThirtydays2018" id="noSrFinalizedAfterThirtydays2018" />
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noSrFinalizedAfterThirtydays2019" id="noSrFinalizedAfterThirtydays2019" />
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        <form:input class="form-control" path="noSrFinalizedAfterThirtydays2020" id="noSrFinalizedAfterThirtydays2020" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row" style="border-top: 1px solid;border-bottom: 1px solid;"> 
                                            <div class="col-xs-12 col-sm-12 col-md-1" >
                                                4
                                            </div> 
                                            <div class="col-xs-12 col-sm-12 col-md-5" style=" border-left: 1px solid black;height: 135px;">
                                                Investigation still pending
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-1" style=" border-left: 1px solid black;">
                                                <div class="row" >
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        2018
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        2019
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        2020
                                                    </div>

                                                </div>
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-2" style=" border-left: 1px solid black;">
                                                <div class="row" style=" border-right: 1px solid black;">
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noStillPending2018" id="noStillPending2018" />
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noStillPending2019" id="noStillPending2019" />
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        <form:input class="form-control" path="noStillPending2020" id="noStillPending2020" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12 col-sm-12 col-md-3" style=" border-right: 1px solid black;">
                                                <div class="row" style=" border-right: 1px solid black;">
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noSrStillPending2018" id="noSrStillPending2018" />
                                                    </div>
                                                    <div class="col-xs-12" style="margin-bottom: 7px;border-bottom: 1px solid;height: 40px;">
                                                        <form:input class="form-control" path="noSrStillPending2019" id="noSrStillPending2019" />
                                                    </div>
                                                    <div class="col-xs-12" style="height: 40px;">
                                                        <form:input class="form-control" path="noSrStillPending2020" id="noSrStillPending2020" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <c:if test="${empty AwardMedalListForm.submittedOn}">
                                                <input type="submit" value="Enter Non SR Case" name="enternonsrcase" class="btn btn-default" />
                                            </c:if>

                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </div>            
                                    </div>
                                </div>                      
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <h3 style="text-align:center"> CERTIFICATE BY RECOMMENDING AUTHORITY </h3>
                            <div style="width:90%;margin: 0 auto;font-size:13px; font-family:verdana;">                    
                                <table border="0" width="100%"  cellspacing="0" cellpadding="0" style="font-size:14px; font-family:verdana;"> 
                                    <tr style="height:40px;">
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="90%" style="text-align:left;">1. Certified that the integrity of Shri./Smt  ${AwardMedalListForm.fullname} Designation ${AwardMedalListForm.designation} Son/Daughter of Shri ${AwardMedalListForm.fathername} Date of birth ${AwardMedalListForm.dob} recommended for the award of “Chief Minister’s Medal for Excellence in Investigation” for the year  ${AwardMedalListForm.awardYear} is above suspicion.</td>
                                    </tr>
                                    <tr style="height:20px;">
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr style="height:40px;">
                                        <td style="text-align:left;">2.	He/She has not been awarded with any stricture/ adverse comment in any Court of law in a case investigated by him.</td>
                                    </tr>
                                    <tr style="height:20px;">
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr style="height:40px;">
                                        <td style="text-align:left;">3.	It is further certified that no judicial or departmental proceedings are being contemplated/pending against him. Similarly, no vigilance case is being contemplated/pending against him.</td>
                                    </tr>
                                    <tr style="height:20px;">
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr style="height:40px;">
                                        <td style="text-align:left;">4.	It is also certified that the recommendee has not been given any major Penalty and has not been awarded with more than one minor punishment in last 5 years</td>
                                    </tr>
                                    <tr style="height:20px;">
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr style="height:40px;">
                                        <td style="text-align:left;">5.	It is also certified that the character and antecedents (of the proposed awardee) has been duly verified and nothing adverse is reported against him. No adverse entry exists in the ACR of the officer for last 5 years</td>
                                    </tr>
                                    <tr style="height:20px;">
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr style="height:40px;">
                                        <td style="text-align:left;">6.	It is also certified that he has not been awarded “Chief Minister’s Medal for Excellence in Investigation” earlier</td>
                                    </tr>
                                    <tr style="height:20px;">
                                        <td>&nbsp;</td>
                                    </tr>

                                </table> 
                            </div>
                        </div>
                        <div class="panel-footer">
                            <a href="awardormedalListForRangeOffice.htm?awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&awardYear=${AwardMedalListForm.awardYear}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                                <c:if test="${empty AwardMedalListForm.rangeSubmittedOn}">
                                <input type="submit" value="Save and Submit Form" name="btn" class="btn btn-success" onclick="return validateForm()"/>
                            </c:if>
                        </div>                           

                    </form:form>
                </div>
            </div>
    </body>
</html>


