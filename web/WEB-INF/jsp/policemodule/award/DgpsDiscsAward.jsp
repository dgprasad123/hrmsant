<%-- 
    Document   : DgpsDiscsAward
    Created on : 25 Feb, 2021, 8:33:40 AM
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
                $('body').on('hidden.bs.modal', '.modal', function() {
                    $(this).removeData('bs.modal');
                });

                $('#doa').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
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
                $('#regDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#frDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#invstDateReg').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#invstFinalSubDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#dateofPropertySubmittedByOfficer').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                togglePrevAwardDiv();
                toggleDiv();
                togglepropertyDiv();

                $('.fileupload').on('change', function() {
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
            function toggleCsFr() {
                var radcsfr = $("input[name='invstCsFr']:checked").val();
                if (radcsfr == 'UI') {
                    $("#rowFinalSub").hide();
                } else {
                    $("#rowFinalSub").show();
                }
            }
            function populateNoOfDays() {
                var x = $('#invstFinalSubDate').val().split("-");
                var y = $('#invstDateReg').val().split("-");
                var month1 = convertMonthNameToNumber(x[1]);
                var month2 = convertMonthNameToNumber(y[1]);
                var date1 = new Date(x[2], month1, x[0]);
                var date2 = new Date(y[2], month2, y[0]);
                if (date1 && date2) {
                    var datediff = days_between(date1, date2);
                    $('#invstPromptness').val(datediff);
                }
            }
            function convertMonthNameToNumber(monthName) {
                var myDate = new Date(monthName + " 1, 2000");
                var monthDigit = myDate.getMonth();
                return isNaN(monthDigit) ? 0 : (monthDigit + 1);
            }
            function days_between(date1, date2) {
                var ONE_DAY = 1000 * 60 * 60 * 24;
                var date1_ms = date1.getTime();
                var date2_ms = date2.getTime();
                var difference_ms = Math.abs(date1_ms - date2_ms);
                return Math.round((difference_ms / ONE_DAY) + 1);
            }
            function resetSrVal() {
                $('#srCaseId').val("");
                $('#srCaseNo').val("");
                $('#regDate').val("");
                $('#frDate').val("");
                $('#dtlsTeamUse').val("");
                $('#noOfExhibits').val("");
                $('#convictionDtls').val("");
                $('#crimeProceedAttach').val("");
            }
            function resetInvstVal() {
                $('#invstCaseId').val("");
                $('#psName').val("");
                $('#invstCaseNo').val("");
                $('#invstDateReg').val("");
                $("#invstCsFrUi").prop('checked', false);
                $("#invstCsFrUt").prop('checked', false);
                $("#invstCsFrPl").prop('checked', false);
                $('#invstCsFr').val("");
                $('#invstFinalSubDate').val("");
                $('#invstSrNonsr').val("");
                $('#invstBriefCase').val("");
                $('#invstInnovMethods').val("");
                $('#invstScientificAids').val("");
                $('#invstScientificEvd').val("");
                $('#invstPromptness').val("");
                $('#invstAttachConfis').val("");
                $('#invstChallenges').val("");
                $('#invstConvcDtls').val("");
                $('#originalJudgementCopy').val("");
                if ($('#originalJudgementCopy').val() == undefined) {
                    $("#upload").show();
                    $("#download").hide();
                } else {
                    $("#upload").hide();
                }
            }


            function togglePrevAwardDiv() {
                if ($("#prevAwardCmb").val() == 'Yes') {
                    $("#previosuAwardId").show();
                } else {
                    $("#previosuAwardId").hide();
                }
            }

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
            function togglepropertyDiv() {
                if ($("#propertyStatementSubmittedifAny").val() == 'Yes') {
                    $("#dateofSubmittingPropertyDiv").show();
                } else {
                    $("#dateofSubmittingPropertyDiv").hide();
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
            function callNoImage() {
                var userPhoto = document.getElementById('loginUserPhoto');
                userPhoto.src = "images/NoEmployee.png";
            }
            function validateForm() {
                var _validFileExtensions = [".pdf"];
            <c:if test="${empty AwardMedalListForm.originalFileNameSB}">
                if ($("#serviceBookCopy").val() == '') {
                    alert('Please Upload 1st page of Service Book. ');
                    document.getElementById('serviceBookCopy').focus();
                    return false;
                } else {
                    var fi = document.getElementById("serviceBookCopy");
                    var fsize = fi.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 3072) {
                        alert("File too Big, please select a file less than 3mb");
                        return false;
                    }

                    if (fi.value.length > 0) {
                        var blnValid = false;
                        for (var j = 0; j < _validFileExtensions.length; j++) {
                            var sCurExtension = _validFileExtensions[j];
                            if (fi.value.substr(fi.value.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                                blnValid = true;
                                break;
                            }
                        }

                        if (!blnValid) {
                            alert("Sorry, " + fi.value + " is invalid, allowed extensions are: " + _validFileExtensions.join(", "));
                            return false;
                        }
                    }



                }
            </c:if>
                if ($("#docPresentRank").val() == '') {
                    alert('Date of Continuing in the Present Rank cannot be blank.');
                    document.getElementById('docPresentRank').focus();
                    return false;
                }
                if ($("#dojDistEst").val() == '') {
                    alert('Date of joining in concerned Dist./ Esst. can not be blank.');
                    document.getElementById('dojDistEst').focus();
                    return false;
                }
                if ($("#PresentPostingPlace").val() == '') {
                    alert('Present Posting Place can not be blank.');
                    document.getElementById('PresentPostingPlace').focus();
                    return false;
                }


                if ($("#dpcifany").val() == 'Y') {
            <c:if test="${empty AwardMedalListForm.originalFileName}">
                    if ($("#discDocument").val() == '') {
                        alert('Please Upload Departmental/ Disciplinary Proceedings/ Vigilance Enquiry/ Criminal Cases document. ');
                        document.getElementById('discDocument').focus();
                        return false;
                    } else {
                        var fi = document.getElementById("discDocument");
                        var fsize = fi.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 3072) {
                            alert("File too Big, please select a file less than 3mb");
                            return false;
                        }


                        var filePath = fi.value;
                        // Allowing file type 
                        var allowedExtensions = /(\.pdf)$/i;
                        if (!allowedExtensions.exec(filePath)) {
                            alert('Invalid file type');
                            filePath.value = '';
                            return false;
                        }
                        if ($("#discDetails").val() == '') {
                            alert('Please enter details. ');
                            document.getElementById('discDetails').focus();
                            return false;
                        }

                    }
            </c:if>
                }


                if ($("#prevAwardCmb").val() == '') {
                    alert('Please enter whether he/she has recieved DGP Disc. ');
                    document.getElementById('prevAwardCmb').focus();
                    return false;
                }

                if ($("#punishmentMajordgp").val() == '') {
                    alert('Please enter Punishment Major. ');
                    document.getElementById('punishmentMajordgp').focus();
                    return false;
                }
                if ($("#punishmentMinordgp").val() == '') {
                    alert('Please enter Punishment Minor. ');
                    document.getElementById('punishmentMinordgp').focus();
                    return false;
                }
                if ($("#punishmentMajorpreeciding").val() == '') {
                    alert('Please enter Punishment Major Preeciding. ');
                    document.getElementById('punishmentMajorpreeciding').focus();
                    return false;
                }
                if ($("#punishmentMinorpreeciding").val() == '') {
                    alert('Please enter Punishment Minor Preeciding. ');
                    document.getElementById('punishmentMinorpreeciding').focus();
                    return false;
                }
                if ($("#propertyStatementSubmittedifAny").val() == '') {
                    alert('Please enter property Statement Submittedif Any ');
                    document.getElementById('propertyStatementSubmittedifAny').focus();
                    return false;
                }
                if ($("#propertyStatementSubmittedifAny").val() == 'Yes') {
                    if ($("#dateofPropertySubmittedByOfficer").val() == '') {
                        alert('Please enter Property Statement Submitted Date. ');
                        document.getElementById('propertyStatementSubmittedifAny').focus();
                        return false;
                    }
                }

                if ($("#recommendStatusofDist").val() == '') {
                    alert('Please select Recommendation Status. ');
                    document.getElementById('recommendStatusofDist').focus();
                    return false;
                }

                if ($("#email").val() == '') {
                    alert('Email Id Can not be blank ');
                    document.getElementById('email').focus();
                    return false;
                }


            }
            function removeAttachedDocument(attchid, doctype) {
                if (confirm('Are you sure to Delete this Attachment?')) {
                    $.ajax({
                        type: "POST",
                        data: {attchId: attchid, doctype: doctype},
                        url: "deletePoliceMedalAttachment.htm",
                        dataType: "json",
                        success: function(data) {
                            if (data.deletestatus == 1) {
                                location.reload();
                            } else {
                                alert("Deletion Failed. Try Again after some time.");
                            }
                        }
                    });
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <% int i = 1;%>
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>     
            <form:form action="awardormedalForm.htm" commandName="AwardMedalListForm" enctype="multipart/form-data">
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> RECOMMENDATION FOR AWARD OF DGP'S DISCS TO POLICE PERSONNEL FOR SPECTACULAR SERVICE RENDERED ON THE OCCASION OF POLICE FORMATION DAY  ON 01.04.2024 </h3>
                        <h3 style="text-align:center"> <b>Employee Id: ${AwardMedalListForm.empId} </b></h3>

                        <div class="panel-heading">
                            <a href="awardormedalList.htm?awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&awardYear=${AwardMedalListForm.awardYear}&sltAwardOccasion=${AwardMedalListForm.sltAwardOccasion}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                                <c:if test="${empty AwardMedalListForm.submittedOn}">
                                <input type="submit" value="Save Form" name="btn" class="btn btn-success" onclick="return validateForm()"/>
                                <input type="submit" value="Delete" name="btn" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>
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
                                    <form:input path="PresentPostingPlace" id="PresentPostingPlace" class="form-control" maxlength="100"/>
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
                                    <form:input path="doa" id="doa" class="form-control"/>
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
                                        <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone and empty AwardMedalListForm.submittedOn}">
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','SB');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameSB}">
                                        <input type="file" name="serviceBookCopy"  id="serviceBookCopy" /> <span style="color:red">(Only PDF)</span>
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
                                    <form:input path="moneyReward" id="moneyReward" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="rewardsAmt"> Total Amount in Rupees </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="rewardsAmt" id="rewardsAmt" class="form-control" maxlength="10" onkeypress="return numbersOnly(event)"/>
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
                                    <form:input path="commendation" id="commendation" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
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
                                    <form:input path="gsMark" id="gsMark" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
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
                                    <form:input path="appreciation" id="appreciation" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
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
                                    <form:input path="aar" id="aar" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="anyOtherRewards"> vi) Any other rewards </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="anyOtherRewards" id="anyOtherRewards" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="anyOtherRewardsDesc" id="anyOtherRewardsDesc" class="form-control" rows="4" cols="60" maxlength="1500" placeholder="Specify"/>
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
                                    <form:input path="punishmentMajordgp" id="punishmentMajordgp" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-5" style="text-align: center">
                                    <form:textarea path="punishmentMajorDetails" id="punishmentMajorDetails" class="form-control" maxlength="1000"/>
                                    <span style="color: red">(Maximum 1000 Character allowed)</span>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameMajorPunishment}">
                                        <a href="downloadMajorPunishmentForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameMajorPunishment}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone and empty AwardMedalListForm.submittedOn}">
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','MAJORPUNISH');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameMajorPunishment}">
                                        <input type="file" name="MajorPunishmentDocument"  id="MajorPunishment" /> <span style="color:red">(Only PDF)</span>
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
                                    <form:input path="punishmentMinordgp" id="punishmentMinordgp" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-5" style="text-align: center">
                                    <form:textarea path="punishmentMinorDetails" id="punishmentMinorDetails" class="form-control" maxlength="1000"/>
                                    <span style="color: red">(Maximum 1000 Character allowed)</span>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameMinorPunishment}">
                                        <a href="downloadMinorPunishmentForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameMinorPunishment}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone and empty AwardMedalListForm.submittedOn}">
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','MINORPUNISH');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameMinorPunishment}">
                                        <input type="file" name="MinorPunishmentDocument"  id="MinorPunishment" /> <span style="color:red">(Only PDF)</span>
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
                                    <form:input path="punishmentMajorpreeciding" id="punishmentMajorpreeciding" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-5" style="text-align: center">
                                    <form:textarea path="punishmentMajorpreecidingDetails" id="punishmentMajorpreecidingDetails" class="form-control" maxlength="1000"/>
                                    <span style="color: red">(Maximum 1000 Character allowed)</span>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameMajorPunishmentpreeciding}">
                                        <a href="downloadMajorPunishmentForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameMajorPunishmentpreeciding}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone and empty AwardMedalListForm.submittedOn}">
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','MAJORPUNISHPREECIDING');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameMajorPunishmentpreeciding}">
                                        <input type="file" name="MajorPunishmentpreecidingDocument"  id="MajorPunishment" /> <span style="color:red">(Only PDF)</span>
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
                                    <form:input path="punishmentMinorpreeciding" id="punishmentMinorpreeciding" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-5" style="text-align: center">
                                    <form:textarea path="punishmentMinorpreecidingDetails" id="punishmentMinorpreecidingDetails" class="form-control" rows="4" cols="100" maxlength="1000"/>
                                    <span style="color: red">(Maximum 1000 Character allowed)</span>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameMinorPunishmentpreeciding}">
                                        <a href="downloadMinorPunishmentForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameMinorPunishment}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone and empty AwardMedalListForm.submittedOn}">
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','MINORPUNISHPREEDIDING');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>    
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameMinorPunishmentpreeciding}">
                                        <input type="file" name="MinorPunishmentpreecidingDocument"  id="MinorPunishment" /> <span style="color:red">(Only PDF)</span>
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
                                    <form:select class="form-control" path="prevAwardCmb" id="prevAwardCmb" onclick="togglePrevAwardDiv()">
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
                                    <form:input path="awardMedalPreviousYear" id="awardMedalPreviousYear" class="form-control" maxlength="4"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="awardMedalRank"> Rank </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="awardMedalRank" id="awardMedalRank" class="form-control" maxlength="100"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="awardMedalPostingPlace"> Place of Posting </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="awardMedalPostingPlace" id="awardMedalPostingPlace" class="form-control" maxlength="100"/>
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
                                        <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone and empty AwardMedalListForm.submittedOn}">
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','DPC');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
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
                                    <label for="discDetails"> give  details </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="discDetails" id="discDetails" class="form-control" rows="4" cols="100" maxlength="450"/>
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
                                        <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone and empty AwardMedalListForm.submittedOn}">
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','CCROLLTwo');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameCCRolltwo}">
                                        <input type="file" name="discCCRolltwo" class="fileupload"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-3">
                                    2021-22&nbsp;
                                    <form:input path="ccrollthreeremarks" id="ccrollthreeremarks" class="form-control" maxlength="50" placeholder="Remarks"/>
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollthree}">
                                        <a href="downloadCCROLLMultipleForAward.htm?doctype=CCROLLThree&attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameCCRollthree}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone and empty AwardMedalListForm.submittedOn}">
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','CCROLLThree');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameCCRollthree}">
                                        <input type="file" name="discCCRollthree" class="fileupload"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-3">
                                    2022-23&nbsp;
                                    <form:input path="ccrolloneremarks" id="ccrolloneremarks" class="form-control" maxlength="50" placeholder="Remarks"/>
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone}">
                                        <a href="downloadCCROLLMultipleForAward.htm?doctype=CCROLLOne&attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameCCRollone}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone and empty AwardMedalListForm.submittedOn}">
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','CCROLLOne');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameCCRollone}">
                                        <input type="file" name="discCCRollone" class="fileupload"/> <span style="color:red">(Only PDF)</span>
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
                                    <form:textarea path="ccrRefenrence" id="ccrRefenrence" class="form-control" rows="4" cols="100" maxlength="3500"/>
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
                                    <form:select path="propertyStatementSubmittedifAny" id="propertyStatementSubmittedifAny" class="form-control" onclick="togglepropertyDiv()">
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
                                    <form:input path="dateofPropertySubmittedByOfficer" id="dateofPropertySubmittedByOfficer" class="form-control"/>
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
                                    <form:input path="email" id="email" class="form-control" />
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
                                    <form:textarea path="briefNote" id="briefNote" class="form-control" rows="4" cols="100" maxlength="3500"/>
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
                                    <form:textarea path="OtherInformationFromDistrictDgps" id="OtherInformationFromDistrictDgps" class="form-control" rows="4" cols="100" maxlength="1000"/>
                                </div>
                            </div> 
                        </div>
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
                                        <h4 class="modal-title">BEST 2 CASES INVESTIGATED</h4>

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
                                                <form:input class="form-control" path="invstCaseNo" id="invstCaseNo" />
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
                                                <form:radiobutton path="invstCsFr" id="invstCsFrUi" value="UI" onclick="toggleCsFr()"/> <label for="invstCsFrUi">Under Investigation</label>
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
                                                <form:input class="form-control" path="invstBriefCase" id="invstBriefCase" />
                                            </div>
                                            <div class="col-lg-1">
                                                <label>b.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="doa"> Innovative methods/ specific professional investigative skill used</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:input class="form-control" path="invstInnovMethods" id="invstInnovMethods" />
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
                                                <form:input class="form-control" path="invstScientificAids" id="invstScientificAids" />
                                            </div>
                                            <div class="col-lg-1">
                                                <label>d.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="doa"> Arraigning of scientific evidence</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:input class="form-control" path="invstScientificEvd" id="invstScientificEvd" />
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
                                                <form:input class="form-control" path="invstPromptness" id="invstPromptness" readonly="true"/>
                                            </div>
                                            <div class="col-lg-1">
                                                <label>f.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="doa"> Attachment and confiscation of crime proceeds(if applicable)</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:input class="form-control" path="invstAttachConfis" id="invstAttachConfis" />
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
                                                <form:input class="form-control" path="invstChallenges" id="invstChallenges" />
                                            </div>
                                            <div class="col-lg-1">
                                                <label>h.</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <label for="doa"> Conviction details</label>
                                            </div>
                                            <div class="col-lg-3">
                                                <form:input class="form-control" path="invstConvcDtls" id="invstConvcDtls" />
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
                                                <input type="submit" value="Enter Case" name="entercase" class="btn btn-default" />
                                            </c:if>
                                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>                      
                        </div>


                        <div class="panel-footer">

                            <a href="awardormedalList.htm?awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&awardYear=${AwardMedalListForm.awardYear}&sltAwardOccasion=${AwardMedalListForm.sltAwardOccasion}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                                <c:if test="${empty AwardMedalListForm.submittedOn}">
                                <input type="submit" value="Save Form" name="btn" class="btn btn-success" onclick="return validateForm()"/>
                                <input type="submit" value="Delete" name="btn" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>
                            </c:if>
                        </div>
                    </div>

                </form:form>
            </div>
        </div>
    </body>
</html>
