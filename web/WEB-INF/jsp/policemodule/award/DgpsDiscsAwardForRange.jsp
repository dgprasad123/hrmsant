<%-- 
    Document   : DgpsDiscsAwardForRange
    Created on : 28 Feb, 2021, 7:53:52 PM
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
            $(document).ready(function() {
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
                togglerangeStatus();
            });


            function togglerangeStatus() {
                if ($("#recommendStatusofRange").val() == 'NOT RECOMMENDED') {
                    $("#hidereasonFornotRecommend").show();
                } else {
                    $("#hidereasonFornotRecommend").hide();
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
        </script>
    </head>
    <body>
        <c:set var="slno" value="0" scope="page"/>
        <div id="wrapper">
            <% int i = 1;%>
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>     
            <form:form action="recommendAwardByRangeForm.htm" commandName="AwardMedalListForm">
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> RECOMMENDATION FOR AWARD OF DGP'S DISCS TO POLICE PERSONNEL FOR SPECTACULAR SERVICE RENDERED ON THE OCCASION OF POLICE FORMATION DAY  ON 01.04.2024 </h3>
                        <h3 style="text-align:center"> <b>Employee Id: ${AwardMedalListForm.empId} </b></h3>
                        <div class="panel-heading">
                            <a href="awardormedalListForRangeOffice.htm?awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&awardYear=${AwardMedalListForm.awardYear}&sltAwardOccasion=${AwardMedalListForm.sltAwardOccasion}"><input type="button" class="btn btn-primary" value="Back"/></a> 
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
                                    <form:hidden path="sltAwardOccasion"/>
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
                                <div class="col-lg-6">
                                    <form:input path="fullname" id="fullname" class="form-control" readonly="true"/>
                                    <form:hidden path="empId"/>
                                </div>

                                <div class="col-lg-3">
                                    <img id="loginUserPhoto" style="border:1px solid #a3a183;padding:3px;" onerror="callNoImage()"  alt="ProfileImage" src='displayprofilephoto.htm?empid=${AwardMedalListForm.empId}' width="200" height="200" />
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="designation">(A) Designation</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="designation" id="designation" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="serviceBookCopy">(B) Present Place Of Posting (in details) <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="PresentPostingPlace" id="PresentPostingPlace" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                            </div>


                            <%--<div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="gpfNo"> GPF No. </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="gpfNo" id="gpfNo" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-3">

                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div> --%>
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
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="doa"> Date of Appointment </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="doa" id="doa" class="form-control" readonly="true"/>
                                </div>
                            </div>   
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="serviceBookCopy"> 1st Page of Service Book be attached</label>
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
                                    <label for="dob"> GPF NO: </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="GpfNo" id="GpfNo" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">

                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="docPresentRank"> Date of Continuing in the Present Rank </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="docPresentRank" id="docPresentRank" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dojDistEst"> Date of joining in concerned Dist./ Esst. </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dojDistEst" id="dojDistEst" class="form-control" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-11">
                                    <label for="docPresentRank">  Reward (category wise) during the Financial Year 2023-24 </label>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="moneyReward"> i) Money Reward </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="moneyReward" id="moneyReward" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="rewardsAmt"> Total Amount in Rupees </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="rewardsAmt" id="rewardsAmt" class="form-control" maxlength="10" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="commendation"> ii) Commendation/ High Commendation </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="commendation" id="commendation" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="gsMark"> iii) G.S. Mark </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="gsMark" id="gsMark" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="appreciation"> iv) Appreciation </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="appreciation" id="appreciation" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="designation">v) A.A.R</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="aar" id="aar" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="anyOtherRewards"> vi) Any other rewards </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="anyOtherRewards" id="anyOtherRewards" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="anyOtherRewardsDesc" id="anyOtherRewardsDesc" class="form-control" rows="4" cols="60" maxlength="1500" placeholder="Specify" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>




                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Punishments (Category wise)</label>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label> During the Financial Year 2023-24</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Punishment Number</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Punishment Detail</label>
                                </div>
                            </div>


                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMajorPrior"> a) Major </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajordgp" id="punishmentMajordgp" class="form-control" maxlength="10" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:textarea path="punishmentMajorDetails" id="punishmentMajorDetails" class="form-control" maxlength="1000" readonly="true"/>
                                    <span style="color: red">(Maximum 1000 Character allowed)</span>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameMajorPunishment}">
                                        <a href="downloadMajorPunishmentForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameMajorPunishment}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMinorPrior"> b) Minor </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinordgp" id="punishmentMinordgp" class="form-control" maxlength="10" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:textarea path="punishmentMinorDetails" id="punishmentMinorDetails" class="form-control" maxlength="1000" readonly="true"/>
                                    <span style="color: red">(Maximum 1000 Character allowed)</span>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameMinorPunishment}">
                                        <a href="downloadMinorPunishmentForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameMinorPunishment}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Preeceding Financial Year 2023-24</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Punishment Number</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Punishment Detail</label>
                                </div>
                            </div>


                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMajorPrior"> a) Major </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorpreeciding" id="punishmentMajorpreeciding" class="form-control" maxlength="10" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:textarea path="punishmentMajorpreecidingDetails" id="punishmentMajorpreecidingDetails" class="form-control" maxlength="1000" readonly="true"/>
                                    <span style="color: red">(Maximum 1000 Character allowed)</span>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameMajorPunishmentpreeciding}">
                                        <a href="downloadMajorPunishmentForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameMajorPunishmentpreeciding}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMinorPrior"> b) Minor </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorpreeciding" id="punishmentMinorpreeciding" class="form-control" maxlength="10" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:textarea path="punishmentMinorpreecidingDetails" id="punishmentMinorpreecidingDetails" class="form-control" maxlength="1000" readonly="true"/>
                                    <span style="color: red">(Maximum 1000 Character allowed)</span>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameMinorPunishmentpreeciding}">
                                        <a href="downloadMinorPunishmentForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameMinorPunishment}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="moneyReward"> Whether he/she has received DGP's Discs during the previous year.<span style="color: red">*</span></label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select class="form-control" path="prevAwardCmb" id="prevAwardCmb">
                                        <form:option value="No">No</form:option>
                                        <form:option value="Yes">Yes</form:option>
                                    </form:select>
                                </div>


                            </div>  
                            <div class="row" style="margin-bottom: 7px;" id="previosuAwardId">
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-1">
                                    <label for="awardMedalPreviousYear"> Year </label>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="awardMedalPreviousYear" id="awardMedalPreviousYear" class="form-control" maxlength="4" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="awardMedalRank"> Rank </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="awardMedalRank" id="awardMedalRank" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="awardMedalPostingPlace"> Place of Posting </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="awardMedalPostingPlace" id="awardMedalPostingPlace" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="moneyReward"> Whether any HRPC/Criminal/ Vigilance cases/ Departmental Proceeding is pending against him/her.</label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dpcifany" id="dpcifany" class="form-control">
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
                                </div>
                            </div>    
                            <div class="row" style="margin-bottom: 7px;" id="hideDiscDocumentRow2">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="discDetails"> give  details </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="discDetails" id="discDetails" class="form-control" rows="4" cols="100" maxlength="450" readonly="true"/>
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
                                    <label for="discCCROll">CCRs for last 3 years (in case of ASI/HM and SI/Sergeant/Dy.Sub/DSI may be uploaded.) </label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="hideCCROLLRow">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="discCCRollone"> Upload document </label>
                                </div>
                                <div class="col-lg-3">
                                    2020-21&nbsp;
                                    <form:input path="ccrolltworemarks" id="ccrolltworemarks" class="form-control" maxlength="50" placeholder="Remarks"/>
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameCCRolltwo}">
                                        <a href="downloadCCROLLMultipleForAward.htm?doctype=CCROLLTwo&attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameCCRolltwo}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>
                                <div class="col-lg-3">
                                    2021-22&nbsp;
                                    <form:input path="ccrollthreeremarks" id="ccrollthreeremarks" class="form-control" maxlength="50" placeholder="Remarks"/>
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollthree}">
                                        <a href="downloadCCROLLMultipleForAward.htm?doctype=CCROLLThree&attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameCCRollthree}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>
                                <div class="col-lg-3">
                                    2022-23&nbsp;
                                    <form:input path="ccrolloneremarks" id="ccrolloneremarks" class="form-control" maxlength="50" placeholder="Remarks"/>
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone}">
                                        <a href="downloadCCROLLMultipleForAward.htm?doctype=CCROLLOne&attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameCCRollone}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMajorPrior"> CCR Reference </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:textarea path="ccrRefenrence" id="ccrRefenrence" class="form-control" rows="4" cols="100" maxlength="3500" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: center">

                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label>i) Whether Property Statement Submitted /Not <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="propertyStatementSubmittedifAny" id="propertyStatementSubmittedifAny" class="form-control">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="dateofSubmittingPropertyDiv">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="dateofServing"> Date of submission</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dateofPropertySubmittedByOfficer" id="dateofPropertySubmittedByOfficer" class="form-control" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label>ii) Submission Status Date Of Property Return Statement On HRMS Portal  Date <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="dateofPropertySubmittedByHRMS" id="dateofPropertySubmittedByHRMS" class="form-control" readonly="true" style="width:50%"/>

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dob"> Mobile No: </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="mobile" id="mobile" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">

                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dob"> Email Id: </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="email" id="email" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">

                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="briefNote"> Brief note on spectacular meritorious services/ 
                                        detection rendered during the Financial year 2022-23. </label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;" >
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="briefNote" id="briefNote" class="form-control" rows="4" cols="100" maxlength="3500" readonly="true"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label> </label>
                                </div>
                                <div class="col-lg-9">
                                    <label style="color:red"> (Maximum 500 words allowed) </label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="recommendStatusofDist"> Recommendation Status  From District / Establishment</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:select path="recommendStatusofDist" id="recommendStatusofDist" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="RECOMMENDED">RECOMMENDED</form:option>
                                        <form:option value="NOT RECOMMENDED">NOT RECOMMENDED</form:option>
                                    </form:select>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;" id="hideDiscDocumentRow2">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="discDetails"> Any Other Information From District / Establishment</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="OtherInformationFromDistrictDgps" id="OtherInformationFromDistrictDgps" class="form-control" rows="4" cols="100" maxlength="1000" readonly="true"/>
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
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="furtherInfoByRange"> Any other Information </label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;" >
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="furtherInfoByRange" id="furtherInfoByRange" class="form-control" rows="4" cols="100"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label> </label>
                                </div>
                                <div class="col-lg-9">
                                    <label style="color:red"> (Maximum 100 words allowed) </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <a href="awardormedalListForRangeOffice.htm?awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&awardYear=${AwardMedalListForm.awardYear}&sltAwardOccasion=${AwardMedalListForm.sltAwardOccasion}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                        <c:if test="${empty AwardMedalListForm.rangeSubmittedOn}">
                        <input type="submit" value="Save and Submit Form" name="btn" class="btn btn-success" onclick="return validateForm()"/>
                    </c:if>
                </div>
            </div>

        </form:form>
    </div>
</div>
</body>
</html>
