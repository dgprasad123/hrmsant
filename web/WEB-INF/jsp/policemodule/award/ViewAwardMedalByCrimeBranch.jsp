<%-- 
    Document   : ViewAwardMedalByCrimeBranch
    Created on : 16 Mar, 2021, 3:00:19 PM
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
        <div id="wrapper">
            <% int i = 1;%>
            
            <form:form action="recommendAwardByRangeForm.htm" commandName="AwardMedalListForm" enctype="multipart/form-data">
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> RECOMMENDATION FOR AWARD OF CHIEF MINISTER'S MEDAL FOR EXCELLENCE IN INVESTIGATION </h3>

                        <div class="panel-heading">
                            
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

                                    <form:select path="gender" id="gender"  size="1" class="form-control">
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
                                    <form:input path="initialAppointRank" id="initialAppointRank" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="initialAppointCadre" id="initialAppointCadre" class="form-control" maxlength="100" readonly="true"/>
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
                                    <label for="punishmentMajor">  Punishment(s)  </label>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="punishmentDetails"> Details of Penalty </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="punishmentDetails" id="punishmentDetails" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="punishmentYears"> Year(s) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="punishmentYears" id="punishmentYears" class="form-control" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="pendingEnquiryDetails"> Details of any enquiry pending against the officer.</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:textarea path="pendingEnquiryDetails" id="pendingEnquiryDetails" class="form-control" rows="4" cols="100" maxlength="450" readonly="true"/>
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
                                    <label></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dpcifany" id="dpcifany" class="form-control" onclick="toggleDiv()">
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
                                    <label for="dpcifany"> give  details </label>
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
                                <div class="col-lg-2">
                                    <label for="courtCaseYear"> Details of the court cases pending against the recommended. if any</label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="courtCaseYear"> Court Case Year </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="courtCaseDetails"> Details of Charge </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="courtCaseStatus"> Present Status </label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-3">
                                    <form:input path="courtCaseYear" id="courtCaseYear" class="form-control" maxlength="4" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="courtCaseDetails" id="courtCaseDetails" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="courtCaseStatus" id="courtCaseStatus" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                            </div>   
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="acrYear1"> ACR grading for last 5 years.</label>
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
                                    <form:input path="acrYear1" id="acrYear1" class="form-control" maxlength="4" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="acrYear1Grading" id="acrYear1Grading" class="form-control" maxlength="50" readonly="true"/>
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
                                    <form:input path="acrYear2" id="acrYear2" class="form-control" maxlength="4" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="acrYear2Grading" id="acrYear2Grading" class="form-control" maxlength="50" readonly="true"/>
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
                                    <form:input path="acrYear3" id="acrYear3" class="form-control" maxlength="4" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="acrYear3Grading" id="acrYear3Grading" class="form-control" maxlength="50" readonly="true"/>
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
                                    <form:input path="acrYear4" id="acrYear4" class="form-control" maxlength="4" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="acrYear4Grading" id="acrYear4Grading" class="form-control" maxlength="50" readonly="true"/>
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
                                    <form:input path="acrYear5" id="acrYear5" class="form-control" maxlength="4" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="acrYear5Grading" id="acrYear5Grading" class="form-control" maxlength="50" readonly="true"/>
                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> <%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="srDocument"> Upload Annexure-II(Details of SR cases during Preceeding 03 years)  </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamesrDocument}">
                                        <a href="downloadSrDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamesrDocument}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        
                                </div>
                            </div> 
                            
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> <%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="nonSrDocument"> Upload Annexure-III(Details of NON-SR cases during Preceeding 03 years)  </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamenonSrDocument}">
                                        <a href="downloadNonSrDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamenonSrDocument}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> <%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="certificateDoc"> Certificate by Recommending Authority  </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamecertificateDoc}">
                                        <a href="downloadCertificateDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamecertificateDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="briefNote"> Citation </label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;" >
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="briefNote" id="briefNote" class="form-control" rows="4" cols="100" maxlength="2500" readonly="true"/>
                                </div>
                            </div>  
                               
                                
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="recommendStatusofDist"> Recommendation Status From Districts/Establishment </label>
                                </div>
                                <div class="col-lg-6">
                                    ${AwardMedalListForm.recommendStatusofDist}
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
                                    ${AwardMedalListForm.recommendStatusofRange}
                                    
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
                                    <form:input path="reasonFornotRecommend" id="reasonFornotRecommend" class="form-control" maxlength="90" readonly="true"/>
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
                                    <form:textarea path="furtherInfoByRange" id="furtherInfoByRange" class="form-control" rows="4" cols="100" readonly="true"/>
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
                        <div class="panel-footer">
                            
                        </div>
                    </div>

                </form:form>
            </div>
        </div>
    </body>
</html>
