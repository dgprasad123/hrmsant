<%-- 
    Document   : PARDetailViewForSI
    Created on : 14 Mar, 2023, 3:52:40 PM
    Author     : Manisha
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    int slno = 0;
    String fiscalyear = "";
    String atchid = "";
    String downloadlink = "";
    String pdflink = "";
    String revertlink = "";
%>
<html>
    <c:set var="r" value="${pageContext.request}" />
    <base href="${initParam['BaseURLPath']}" />  
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> :: Performance Appraisal ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

        <script type="text/javascript">
            var templateHeading;
            $(document).ready(function() {
                calculateTotA();
                calculateTotB();
                calculateTotC();
                calculateTotD();

                $.post("GetPARGradeListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        //$('#ratingattitude').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#ratingcoordination').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        //$('#ratingresponsibility').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#teamworkrating').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        //$('#ratingcomskill').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        //$('#ratingitskill').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        //$('#ratingleadership').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#ratinginitiative').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#ratingdecisionmaking').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#ratequalityofwork').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                    }
                    if ($('#hidratingattitude').val() > 0) {
                        $('#ratingattitude').val($('#hidratingattitude').val());
                    }
                    if ($('#hidratingcoordination').val() > 0) {
                        $('#ratingcoordination').val($('#hidratingcoordination').val());
                    }
                    if ($('#hidratingresponsibility').val() > 0) {
                        $('#ratingresponsibility').val($('#hidratingresponsibility').val());
                    }
                    if ($('#hidteamworkrating').val() > 0) {
                        $('#teamworkrating').val($('#hidteamworkrating').val());
                    }
                    if ($('#hidratingcomskill').val() > 0) {
                        $('#ratingcomskill').val($('#hidratingcomskill').val());
                    }
                    if ($('#hidratingitskill').val() > 0) {
                        $('#ratingitskill').val($('#hidratingitskill').val());
                    }
                    if ($('#hidratingleadership').val() > 0) {
                        $('#ratingleadership').val($('#hidratingleadership').val());
                    }
                    if ($('#hidratinginitiative').val() > 0) {
                        $('#ratinginitiative').val($('#hidratinginitiative').val());
                    }
                    if ($('#hidratingdecisionmaking').val() > 0) {
                        $('#ratingdecisionmaking').val($('#hidratingdecisionmaking').val());
                    }
                    if ($('#hidratequalityofwork').val() > 0) {
                        $('#ratequalityofwork').val($('#hidratequalityofwork').val());
                    }
                });

                if ($('#hidsltGrading').val() > 0) {
                    $('#sltGrading').val($('#hidsltGrading').val());
                }
                if ($('#hidsltReviewGrading').val() > 0) {
                    $('#sltReviewGrading').val($('#hidsltReviewGrading').val());
                }
                if ($('#hidsltAcceptingGrading').val() > 0) {
                    $('#sltAcceptingGrading').val($('#hidsltAcceptingGrading').val());
                }

            });

            function onlyIntegerRange(e)
            {
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 53) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 53) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }

            function calculateTotA() {
                var tot = parseInt(0 + $("#ratingattitude").val()) + parseInt(0 + $("#ratingresponsibility").val()) + parseInt(0 + $("#ratingcomskill").val()) + parseInt(0 + $("#ratingleadership").val());
                $("#totalofA").val(tot);
                var avg = tot / 4;
                $('#avgofA').html(avg);
            }
            function calculateTotB() {
                var totalofB = parseInt(0 + $("#ratingitskill").val()) + parseInt(0 + $("#ratingAttitudeStScSection").val());
                $("#totalofB").val(totalofB);
                var avgB = totalofB / 2;
                $('#avgofB').html(avgB);
            }
            function calculateTotC() {
                var totalofC = parseInt(0 + $("#fiveTChartertenpercent").val()) + parseInt(0 + $("#fiveTCharterfivePercent").val()) + parseInt(0 + $("#fiveTComponentmoSarkar").val());
                $("#totalofC").val(totalofC);
                var avg = (totalofC / 3).toFixed(2);
                $('#avgofC').html(avg);
            }
            function calculateTotD() {
                var totalofD = parseInt(0 + $("#ratingQualityOfOutput").val()) + parseInt(0 + $("#ratingeffectivenessHandlingWork").val());
                $("#totalofD").val(totalofD);
                var avgD = totalofD / 2;
                $('#avgofD').html(avgD);
            }

            function savecheck() {
                var parstatus = $("#parstatus").val();
                if (parstatus == "6") {

                    if ($("#ratingattitude").val() == "") {
                        alert("Please Select Attitude to Work");
                        $("#ratingattitude").focus();
                        return false;
                    }
                    if ($("#ratingresponsibility").val() == "") {
                        alert("Please Select Sense of responsibility")
                        $("#ratingresponsibility").focus();
                        return false;
                    }
                    if ($("#ratingcomskill").val() == "") {
                        alert("Please Select Communication skill");
                        $("#ratingcomskill").focus();
                        return false;
                    }
                    if ($("#ratingleadership").val() == "") {
                        alert("Please Select Leadership Qualities");
                        $("#ratingleadership").focus();
                        return false;
                    }
                    if ($("#ratingitskill").val() == "") {
                        alert("Please Select Knowledge of Criminal Laws");
                        $("#ratingitskill").focus();
                        return false;
                    }
                    if ($("#ratingAttitudeStScSection").val() == "") {
                        alert("Please Select Attitude towards ST/SC/Weaker Sections & relation with Public");
                        $("#ratingAttitudeStScSection").focus();
                        return false;
                    }
                    if ($("#fiveTChartertenpercent").val() == "") {
                        alert("Please Select 10% on 5T charter of Department: (out of 10%)");
                        $("#fiveTChartertenpercent").focus();
                        return false;
                    }
                    if ($("#fiveTCharterfivePercent").val() == "") {
                        alert("Please Select 5% on 5T charter of Government: (out of 5%)");
                        $("#fiveTCharterfivePercent").focus();
                        return false;
                    }
                    if ($("#fiveTComponentmoSarkar").val() == "") {
                        alert("Please Select 5% on Mo Sarkar: (out of 5%)");
                        $("#fiveTComponentmoSarkar").focus();
                        return false;
                    }
                    if ($("#ratingQualityOfOutput").val() == "") {
                        alert("Please Select Quality of output and effectiveness");
                        $("#ratingQualityOfOutput").focus();
                        return false;
                    }
                    if ($("#ratingeffectivenessHandlingWork").val() == "") {
                        alert("Please Select Effectiveness in handling ");
                        $("#ratingeffectivenessHandlingWork").focus();
                        return false;
                    }
                    if ($("#penPictureOfOficerNote").val() == "") {
                        alert("Please Enter Pen picture of Officer");
                        $("#penPictureOfOficerNote").focus();
                        return false;
                    }
                    if ($("#inadequaciesNote").val() == "") {
                        alert("Please Enter Inadequacies, deficiencies or shortcomings, if any");
                        $("#inadequaciesNote").focus();
                        return false;
                    }
                    if ($("#integrityNote").val() == "") {
                        alert("Please Enter Integrity ( If integrity is doubtful or adverse");
                        $("#integrityNote").focus();
                        return false;
                    }
//                    if ($("#stateOfHealth").val() == "") {
//                        alert("Please Enter State of Health");
//                        $("#stateOfHealth").focus();
//                        return false;
//                    }
                    var grading = $('#sltGrading').val();
                    if (grading == '') {
                        alert("Please select Overall Grading");
                        return false;
                    } else if (grading != '') {
                        var grde = grading;
                        var avg = "Below Average";
                        var outs = "Outstanding";
                        var decs = (grde == 1) ? avg : outs;
                        if ((grde == 1 || grde == 5) && $('#gradingNote').val() == '') {
                            alert("As you have selected Overall Grading as " + decs + " you must enter Justification");
                            $('#gradingNote').focus();
                            return false;
                        }
                    }

                } else if (parstatus == "7") {
                    if ($('#reviewingNote').val() == '') {
                        alert("Please enter Reviewing Note");
                        $('#reviewingNote').focus();
                        return false;
                    }
                    var reviewinggrading = $('#sltReviewGrading').val();
                    if (reviewinggrading == '') {
                        alert("Please select Reviewing Overall Grading");
                        $('#sltReviewGrading').focus();
                        return false;
                    }
                } else if (parstatus == "8") {
                    if ($('#acceptingNote').val() == '') {
                        alert("Please enter Accepting Note");
                        $('#acceptingNote').focus();
                        return false;
                    }
                    var acceptinggrading = $('#sltAcceptingGrading').val();
                    if (acceptinggrading == '') {
                        alert("Please select Accepting Overall Grading");
                        $('#sltAcceptingGrading').focus();
                        return false;
                    }
                }
                return true;
            }
            function submitcheck() {
                var isSubmit = savecheck();
                //alert("isSubmit is: "+isSubmit);
                if (isSubmit == true) {
                    var isConfirm = confirm("Are you sure to Submit the Remarks?");
                    if (isConfirm == true) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            function revertClick() {
                var url = 'par/RevertPAR.htm?fiscalyr=' + $('#fiscalYear').val() + '&parId=' + $('#parid').val() + '&parStatus=' + $('#parstatus').val() + '&taskId=' + $('#taskId').val() + '&isreportingcompleted=' + $('#isreportingcompleted').val() + '&csrfPreventionSalt=' + $("#csrfPreventionSalt").val();
                $('#revertPAR').modal('show');
                $('#revertPAR .modal-body').load(url, function() {

                });
            }

            function copytemplate(me, textarea) {
                comboname = $(me).prop("name");
                if (comboname == "authnotetemplate") {
                    inadequaciesNotetemplate
                    templateHeading = "GA";
                } else if (comboname == "integrityNotetemplate") {
                    templateHeading = "Intregity Note";
                } else if (comboname == "gradingNotetemplate") {
                    templateHeading = "Grading Note";
                } else if (comboname == "fivetDepartmenttemplate") {
                    templateHeading = "FiveTDepartment Note";
                } else if (comboname == "fivetGovernmenttemplate") {
                    templateHeading = "FiveTGovernment Note";
                } else if (comboname == "fivetMoSarkartemplate") {
                    templateHeading = "FiveTMoSarkar Note";
                } else if (comboname == "inadequaciesNotetemplate") {
                    templateHeading = "Adverse Note";
                } else if (comboname == "reviewingNotetemplate") {
                    templateHeading = "Reviewing Note";
                }
                var txt = $(me).val();
                if (txt == 'Custom') {
                    $('#setCustomForAuthnote').modal('show');
                    getSavedTemplateList();
                } else if (txt == '') {

                } else {
                    $("#" + textarea).val(txt);
                }
            }
            function saveCustomTemplate() {
                var txttemplateContent = $("#templateContent").val();
                if (txttemplateContent) {
                    $.post("saveCustomForAuthNote.htm", {templateContent: txttemplateContent, templateHeading: templateHeading})
                            .done(function(data) {
                                alert("Saved Sucessfully");
                                getSavedTemplateList();
                                $('#' + comboname + ' option[value="Custom"]').remove();
                                $("#" + comboname).append('<option value="' + txttemplateContent + '">' + txttemplateContent + '</option>')
                                $("#" + comboname).append('<option value="Custom">Custom</option>')
                            })
                } else if (!txttemplateContent) {
                    alert("Please Enter Content");

                }

            }
            function getSavedTemplateList() {
                var url = 'getloginWisesaveTemplateDetail.htm?templateHeading=' + templateHeading;
                $.getJSON(url, function(data) {
                    $('#loginwisetemplateList').empty();
                    for (i = 0; i < data.length; i++) {
                        var trhtml = '<tr>';
                        trhtml = trhtml + '<td>' + (i + 1) + '</td>';
                        trhtml = trhtml + '<td>' + data[i].templateContent + '</td>';
                        trhtml = trhtml + '<td align="center"><a href="javascript:void(0)" onclick="removeloginWisesaveTemplateDetail(\'' + data[i].templateId + '\')"><span class="glyphicon glyphicon-remove"></span>' + '</a></td>';
                        trhtml = trhtml + '</tr>';
                        $('#loginwisetemplateList').append(trhtml);
                    }

                });
            }
            function removeloginWisesaveTemplateDetail(vtemplateId) {

                $.post("removeloginWisesaveTemplateDetail.htm", {templateId: vtemplateId}, "json")
                        .done(function(data) {
                            getSavedTemplateList();
                            alert("Delete Sucessfully");
                        })
            }

        </script>
        <style type="text/css">
            body{
                font-family: Arial;
                font-size:13px;
            }
            .star {
                color: #FF0000;
                font-size: 17px;
            }
        </style>
    </head>
    <body>

        <c:if test="${RepotClose eq 'Yes'}">
            <h2 style="color:#FF4500;text-align: center"> Remarks of Reporting Authority for Performance Appraisal Report (PAR) for SI & Equivalent Ranks will be available soon. </h2>
        </c:if>

        <c:if test="${RepotClose eq 'No'}">
            <div align="center" style="margin-top:5px;margin-bottom:10px;">
                <div align="center">
                    <table border="0" width="99%" cellpadding="0" cellspacing="0" style="font-size:12px; font-family:verdana;">
                        <tr>
                            <td style="background-color:#5095ce;color:#FFFFFF;padding:0px;font-weight:bold;" align="center">
                                <c:if test="${pardetail.parType eq 'SiPar'}">
                                    <h2>Performance Appraisal Report (PAR) for SI & Equivalent Ranks (Group - B)</h2>
                                </c:if>
                            </td>
                        </tr>                        
                    </table>
                </div>
            </div>

            <div style="width:100%;overflow: auto;margin-top:5px;border:1px">
                <div align="center">
                    <div style="width:99%;">                        
                        <div style="width:100%;overflow: auto;margin-top:1px;border:1px solid #5095ce;">
                            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Details of Transmission / Movement of PAR</div>
                            <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                <tr style="height: 40px">                               
                                    <td align="center" valign="top" width="10%"> 1. </td>
                                    <td width="20%" valign="top">Reporting Authority</td>
                                    <td width="70%">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <c:if test="${not empty pardetail.reportingauth}">
                                                <c:forEach var="rptauth" items="${pardetail.reportingauth}">
                                                    <%slno = slno + 1;%>
                                                    <tr>
                                                        <td width="5%"><%=slno%>.</td>
                                                        <c:if test="${rptauth.isPendingReportingAuthority == 'Y'}">
                                                            <td width="95%" style="color:red;">
                                                                <c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)<br />
                                                                (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                                                &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:black;">(Pending at this end)</span>      
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${rptauth.isPendingReportingAuthority != 'Y'}">
                                                            <td width="95%">
                                                                <c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)<br />
                                                                (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                                            </td>
                                                        </c:if>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </table>
                                    </td>
                                </tr>
                                <%slno = 0;%>
                                <tr style="height: 40px">                               
                                    <td align="center" valign="top" width="10%"> 2. </td>
                                    <td width="20%" valign="top">Reviewing Authority</td>
                                    <td width="70%">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <c:if test="${not empty pardetail.reviewingauth}">
                                                <c:forEach var="rptauth" items="${pardetail.reviewingauth}">
                                                    <%slno = slno + 1;%>
                                                    <tr>
                                                        <td width="5%"><%=slno%>.</td>
                                                        <c:if test="${rptauth.isPendingReviewingAuthority == 'Y'}">
                                                            <td width="95%" style="color:red;">
                                                                <c:out value="${rptauth.authorityname}"/>(<c:out value="${rptauth.authorityspn}"/>)<br />
                                                                (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>
                                                                &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:black;">(Pending at this end)</span>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${rptauth.isPendingReviewingAuthority != 'Y'}">
                                                            <td width="95%">
                                                                <c:out value="${rptauth.authorityname}"/>(<c:out value="${rptauth.authorityspn}"/>)<br />
                                                                (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>
                                                            </td>
                                                        </c:if>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </table>
                                    </td>
                                </tr>
                                <%slno = 0;%>
                                <tr style="height: 40px">
                                    <td align="center" valign="top" width="10%"> 3. </td>
                                    <td width="20%" valign="top">Accepting Authority</td>
                                    <td width="70%">
                                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                            <c:if test="${not empty pardetail.acceptingauth}">
                                                <c:forEach var="rptauth" items="${pardetail.acceptingauth}">
                                                    <%slno = slno + 1;%>
                                                    <tr>
                                                        <td width="5%"><%=slno%>.</td>
                                                        <c:if test="${rptauth.isPendingAcceptingAuthority == 'Y'}">
                                                            <td width="95%" style="color:red;">
                                                                <c:out value="${rptauth.authorityname}"/>(<c:out value="${rptauth.authorityspn}"/><br />
                                                                (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                                                &nbsp;&nbsp;&nbsp;&nbsp;<span style="color:black;">(Pending at this end)</span>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${rptauth.isPendingAcceptingAuthority != 'Y'}">
                                                            <td width="95%">
                                                                <c:out value="${rptauth.authorityname}"/>(<c:out value="${rptauth.authorityspn}"/><br />
                                                                (<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                                            </td>
                                                        </c:if>
                                                    </tr>
                                                </c:forEach>
                                            </c:if>
                                        </table>    
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div style="width:100%;overflow: auto;margin-top:1px;border:1px solid #5095ce;">                                                        
                            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Personal Information</div>                            
                            <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 1. </td>
                                    <td width="20%"> Applicant </td>
                                    <td width="70%"> <b> <c:out value="${pardetail.applicant}"/> </b> </td> 
                                    <td rowspan="9" valign="top">
                                        <img id="loginUserPhoto" style="border:1px solid #a3a183;padding:3px;" onerror="callNoImage()"  alt="ProfileImage" src='displayprofilephoto.htm?empid=${pardetail.applicantempid}' width="100" height="100" />
                                    </td>
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 2. </td>
                                    <td width="20%">Fiscal Year</td>
                                    <td width="70%">
                                        <span>
                                            <c:out value="${pardetail.fiscalYear}"/>
                                        </span>
                                    </td> 
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 3. </td>
                                    <td width="20%">Appraisal Period .</td>
                                    <td width="70%">
                                        <span>
                                            From : <c:out value="${pardetail.parPeriodFrom}"/> -  To: <c:out value="${pardetail.parPeriodTo}"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 4. </td>
                                    <td width="20%">Date of Birth .</td>
                                    <td width="70%"><span><c:out value="${pardetail.dob}"/></span></td>
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 5. </td>
                                    <td width="20%">Service to which the officer belongs .</td>
                                    <td width="70%"><span><c:out value="${pardetail.empService}"/></span></td>
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 6. </td>
                                    <td width="20%">Group to which the officer belongs .</td>
                                    <td width="70%"> <span><c:out value="${pardetail.empGroup}"/></span></td>
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 7. </td>
                                    <td width="20%">Designation during the period of report .</td>
                                    <td width="70%"><span><c:out value="${pardetail.apprisespn}"/></span></td>
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 8. </td>
                                    <td width="20%">Office to where posted .</td>
                                    <td width="70%"><span><c:out value="${pardetail.empOffice}"/></span></td>                                         
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="10%"> 9. </td>
                                    <td width="20%">Head Quarter(if any) .</td>
                                    <td width="70%"><span><c:out value="${pardetail.sltHeadQuarter}"/></span></td>
                                </tr>

                                <c:if test="${pardetail.parType eq 'SiPar'}">
                                    <tr style="height: 40px">                               
                                        <td align="center" width="10%"> 10. </td>
                                        <td width="20%"> Sub Inspector Type </td>
                                        <td width="70%"><span><c:out value="${pardetail.siType}"/></span></td>
                                    </tr>
                                    <tr style="height: 40px">                               
                                        <td align="center" width="10%"> 11. </td>
                                        <td width="20%"> Place of Posting </td>
                                        <td width="70%"><span><c:out value="${pardetail.placeOfPostingSi}"/></span></td>
                                    </tr>
                                </c:if>

                            </table>
                        </div>
                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Absentee Statement</div>                        
                            <table border="0" cellpadding="5" cellspacing="0" width="95%" class="tableview">             
                                <tr>
                                    <th align="left" width="15%"><b>From Date</b></th>
                                    <th align="left" width="15%"><b>To Date</b></th>
                                    <th align="left" width="15%"><b>Leave/ Training</b></th>
                                    <th align="left" width="15%"><b>Type of Leave</b></th>
                                </tr>  
                            </table>
                            <table border="0" cellpadding="5" cellspacing="0" width="95%" class="tableview">
                                <c:if test="${not empty pardetail.leaveAbsentee}">
                                    <c:forEach var="AbsenteeBean" items="${pardetail.leaveAbsentee}">
                                        <tr height="40px">
                                            <td width="15%" align="left"><c:out value="${AbsenteeBean.fromDate}"/></td>
                                            <td width="15%" align="left"><c:out value="${AbsenteeBean.toDate}"/></td>
                                            <c:if test="${AbsenteeBean.absenceCause == 'L'}">
                                                <td width="15%" align="left">Leave</td>
                                            </c:if>
                                            <c:if test="${AbsenteeBean.absenceCause == 'T'}">
                                                <td width="15%" align="left">Training</td>
                                            </c:if>
                                            <td width="15%" align="left"><c:out value="${AbsenteeBean.leaveType}"/></td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </table>
                        </div>
                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Achievements</div>                            
                            <table border="0" cellpadding="5" cellspacing="0" width="95%" class="tableview">             
                                <tr>
                                    <th align="left" width="5%"><b>SL No</b></th>
                                    <th align="left" width="15%"><b>Task</b></th>
                                    <th align="left" width="15%"><b>Target</b></th>
                                    <th align="left" width="15%"><b>Achievement</b></th>
                                    <th align="left" width="10%"><b>Achievement(%)</b></th>
                                    <th align="left" width="15%"><b>Attachment(if any)</b></th>
                                </tr>  
                            </table>
                            <table border="0" cellpadding="5" cellspacing="0" width="95%" class="tableview"> 
                                <c:if test="${not empty pardetail.fiscalYear}">
                                    <c:set var="fyear" value="${pardetail.fiscalYear}"/>
                                    <%
                                        fiscalyear = (String) pageContext.getAttribute("fyear");
                                    %>
                                </c:if>
                                <c:if test="${not empty pardetail.achivementList}">
                                    <c:forEach var="AchievementBean" items="${pardetail.achivementList}">
                                        <c:if test="${not empty AchievementBean.attachmentId}">
                                            <c:set var="attid" value="${AchievementBean.attachmentId}"/>
                                            <%
                                                atchid = (String) pageContext.getAttribute("attid");
                                            %>
                                        </c:if>
                                        <%
                                            downloadlink = "DownloadAchievementAttachment.htm?attId=" + atchid + "&fiscalyr=" + fiscalyear;
                                        %>
                                        <tr height="40px">
                                            <td width="5%" align="left"><c:out value="${AchievementBean.slno}"/></td>
                                            <td width="15%" align="left"><c:out value="${AchievementBean.task}"/></td>
                                            <td width="15%" align="left"><c:out value="${AchievementBean.target}"/></td>
                                            <td width="15%" align="left"><c:out value="${AchievementBean.achievement}"/></td>
                                            <td width="10%" align="left"><c:out value="${AchievementBean.percentAchievement}"/></td>
                                            <td width="15%" align="left">
                                                <a href='<%=downloadlink%>' style="text-decoration:none;">
                                                    <c:out value="${AchievementBean.attachmentname}"/>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                            </table>
                        </div>
                        <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                            <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Other Details</div>
                            <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">                                         
                                <tr style="height: 40px">                               
                                    <td align="center" width="8%"> 1. </td>
                                    <td width="25%">Brief description of duties/tasks entrusted.(in about 100 words)</td>
                                    <td width="70%">
                                        <span>
                                            <c:out value="${pardetail.selfappraisal}" escapeXml="false"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr style="height: 40px">                              
                                    <td align="center" width="8%"> 2. </td>
                                    <td width="25%">3.(i)Significant work, if any, done</td>
                                    <td width="70%">
                                        <span>
                                            <c:out value="${pardetail.specialcontribution}" escapeXml="false"/>
                                        </span>
                                    </td> 
                                </tr>
                                <c:if test="${parMastForm.fiscalyear ne '2014-15' && parMastForm.fiscalyear ne '2015-16' && parMastForm.fiscalyear ne '2016-17' && parMastForm.fiscalyear ne '2017-18' && parMastForm.fiscalyear ne '2018-19'}">
                                    <tr style="height: 40px">                               
                                        <td align="center" width="8%">  </td>
                                        <td width="25%">(ii) Work Done For Implementation of 5TS (Transparency,Teamwork,Technology,Transformation and Time)</td>
                                        <td width="70%">
                                            <span>
                                                <c:out value="${pardetail.fiveTComponentappraise}" escapeXml="false"/>
                                            </span>
                                        </td> 
                                    </tr>
                                </c:if>

                                <tr style="height: 40px">                               
                                    <td align="center" width="8%"> 3. </td>
                                    <td width="25%">Hindrance</td>
                                    <td width="70%">
                                        <span>
                                            <c:out value="${pardetail.reason}" escapeXml="false"/>
                                        </span>
                                    </td>
                                </tr>
                                <tr style="height: 40px">                               
                                    <td align="center" width="8%"> 4. </td>
                                    <td width="25%">Place</td>
                                    <td width="70%">
                                        <span>
                                            <c:out value="${pardetail.place}"/>
                                        </span>
                                    </td> 
                                </tr>
                            </table>
                        </div>
                        <form action="par/forwardPAR.htm" method="POST" commandName="parDetail">
                            <input type="hidden" name="csrfPreventionSalt" id="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
                            <input type="hidden" name="parid" id="parid" value="${pardetail.parid}"/>
                            <input type="hidden" name="taskid" id="taskId" value="${pardetail.taskid}"/>
                            <input type="hidden" name="parstatus" id="parstatus" value="${pardetail.parstatus}"/>
                            <input type="hidden" name="apprisespc" id="apprisespc" value="${pardetail.apprisespc}"/>
                            <input type="hidden" name="isreportingcompleted" value="${pardetail.isreportingcompleted}"/>
                            <input type="hidden" name="reportingempid" value="${pardetail.reportingempid}"/>
                            <input type="hidden" name="isreviewingcompleted" value="${pardetail.isreviewingcompleted}"/>
                            <input type="hidden" name="isacceptingcompleted" value="${pardetail.isacceptingcompleted}"/>
                            <input type="hidden" name="fiscalYear" id="fiscalYear" value="${pardetail.fiscalYear}"/>
                            <input type="hidden" name="urlName" value="${pardetail.urlName}"/>

                            <c:if test="${pardetail.ishideremark != null && pardetail.ishideremark == 'N'}">
                                <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5F9B24;">
                                    <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 15px" align="left">Remarks of Reporting Authority</div>
                                    <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                        <tr style="height: 40px">
                                            <td width="70%">
                                                <c:if test="${not empty pardetail.reportingdata}">
                                                    <%slno = 0;%>
                                                    <c:forEach var="reportingdt" items="${pardetail.reportingdata}">
                                                        <c:if test="${reportingdt.isreportingcompleted == 'F'}">
                                                            <table>
                                                                <tr>
                                                                    <%if (slno == 0) {%>
                                                                    <td style="font-weight:bold;text-decoration:underline;padding-left: 10px;">
                                                                        <c:out value="${reportingdt.reportingauthName}"/>
                                                                    </td>
                                                                    <%} else {%>
                                                                    <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;padding-left: 10px;">
                                                                        <c:out value="${reportingdt.reportingauthName}"/>
                                                                    </td>
                                                                    <%}%>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        <div style="color: red;"><b>PAR is Force Forwarded On Date: ${reportingdt.submittedon}</b></div>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </c:if>
                                                        <c:if test="${reportingdt.isreportingcompleted == 'Y'}">
                                                            <table>
                                                                <tr>
                                                                    <%if (slno == 0) {%>
                                                                    <td style="font-weight:bold;text-decoration:underline;padding-left: 10px;">
                                                                        <c:out value="${reportingdt.reportingauthName}"/>
                                                                    </td>
                                                                    <%} else {%>
                                                                    <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;padding-left: 10px;">
                                                                        <c:out value="${reportingdt.reportingauthName}"/>
                                                                    </td>
                                                                    <%}%>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;"><span><b>1.</b> Personal Attribute(On a scale of 1-5 weightage for this section in 40%) “A”</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding: 5px;">
                                                                        <table width="60%" style="margin-left:35px;" border="0">
                                                                            <tr style="padding: 5px;height: 40px;">
                                                                                <td width="35%">(a) Attitude to work </td>
                                                                                <td width="20%">
                                                                                    <c:if test="${reportingdt.ratingattitude eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingattitude eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingattitude eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingattitude eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingattitude eq '5'}"> Outstanding </c:if>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr style="padding: 5px;height: 40px;">
                                                                                    <td>(b)  Sense of responsibility:    </td>
                                                                                    <td>
                                                                                    <c:if test="${reportingdt.ratingresponsibility eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingresponsibility eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingresponsibility eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingresponsibility eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingresponsibility eq '5'}"> Outstanding </c:if>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr style="padding: 5px;height: 40px;">
                                                                                    <td>(c)  Communication skill :  </td>
                                                                                    <td>
                                                                                    <c:if test="${reportingdt.ratingcomskill eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingcomskill eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingcomskill eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingcomskill eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingcomskill eq '5'}"> Outstanding </c:if>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr style="padding: 5px;height: 40px;">
                                                                                    <td>(d)  Leadership Qualities :  </td>
                                                                                    <td>
                                                                                    <c:if test="${reportingdt.ratingleadership eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingleadership eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingleadership eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingleadership eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingleadership eq '5'}"> Outstanding </c:if>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr> <td>&nbsp;</td> </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span><b>2.</b> Functional (On a Scale of 1-5,weightage for this section is 20%)“B”</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding: 5px;">
                                                                            <table width="99%" style="margin-left:35px;" border="0">
                                                                                <tr>
                                                                                    <td width="75%">(a)  Knowledge of Criminal Laws/ Police Manuals and	rules/ Procedures/IT Skills/ Local norms in the relevant subjects:</td>
                                                                                    <td width="20%">
                                                                                    <c:if test="${reportingdt.ratingitskill eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingitskill eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingitskill eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingitskill eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingitskill eq '5'}"> Outstanding </c:if>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr><td colspan="2">&nbsp;</td></tr>
                                                                                <tr>
                                                                                    <td>(b)  Attitude towards ST/SC/Weaker Sections & relation with Public:    </td>
                                                                                    <td>
                                                                                    <c:if test="${reportingdt.ratingAttitudeStScSection eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingAttitudeStScSection eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingAttitudeStScSection eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingAttitudeStScSection eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingAttitudeStScSection eq '5'}"> Outstanding </c:if>
                                                                                    </td>
                                                                                </tr>

                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr> <td>&nbsp;</td> </tr>

                                                                    <tr> 
                                                                        <td style="padding-left: 10px;"><span><b>3.</b> Assessment Of Performance Of 5T (20%):“C” </span> </td>
                                                                    </tr>
                                                                    <tr> <td>&nbsp;</td> </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;">${reportingdt.authNote}</td>
                                                                </tr>
                                                                <tr> <td>&nbsp;</td> </tr>
                                                                <tr>
                                                                    <td style="padding: 5px;">
                                                                        <table width="70%" style="margin-left:35px;" border="0">
                                                                            <tr>
                                                                                <td width="15%">(a)  10% on 5T charter of Department: (out of 10%):</td>
                                                                                <td width="10%"> 
                                                                                    <c:if test="${reportingdt.fiveTChartertenpercent eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTChartertenpercent eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTChartertenpercent eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTChartertenpercent eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTChartertenpercent eq '5'}"> Outstanding </c:if>
                                                                                    </td>                                                                                
                                                                                </tr>
                                                                                <tr> <td>&nbsp;</td> </tr>
                                                                                <tr>
                                                                                    <td>(b)  5% on 5T charter of Government: (out of 5%):  </td>
                                                                                    <td>
                                                                                    <c:if test="${reportingdt.fiveTCharterfivePercent eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTCharterfivePercent eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTCharterfivePercent eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTCharterfivePercent eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTCharterfivePercent eq '5'}"> Outstanding </c:if>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>&nbsp;</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>(c)  5% on Mo Sarkar: (out of 5%):  </td>
                                                                                    <td>
                                                                                    <c:if test="${reportingdt.fiveTComponentmoSarkar eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTComponentmoSarkar eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.fiveTComponentmoSarkar eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTComponentmoSarkar eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.fiveTComponentmoSarkar eq '5'}"> Outstanding </c:if>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>&nbsp;</td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span><b>4.</b> Assessment of Work output (on scale of 1-5 weightage for this section 20%) “D”</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;">
                                                                            <table width="99%" border="0" style="margin-left:25px;">
                                                                                <tr>
                                                                                    <td width="70%">
                                                                                    <c:if test="${pardetail.siType eq 'Sub Inspector (Civil)'}">
                                                                                        (a) Quality of output and effectiveness in investigation/ Enquiry (Eg. Convictions):
                                                                                    </c:if>
                                                                                    <c:if test="${pardetail.siType eq 'Sub Inspector (Armed)'}">
                                                                                        (a) Quality of output and effectiveness in management of Stores/ Procurement/ Maintenance of Arms & Ammunitions:
                                                                                    </c:if>
                                                                                    <c:if test="${pardetail.siType eq 'Sub Inspector (Equivalent)'}">
                                                                                        (a) Quality of output and effectiveness in management of equipment/stores/records pertaining to their field:
                                                                                    </c:if>
                                                                                </td>
                                                                                <td width="25%">
                                                                                    <c:if test="${reportingdt.ratingQualityOfOutput eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingQualityOfOutput eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingQualityOfOutput eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingQualityOfOutput eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingQualityOfOutput eq '5'}"> Outstanding </c:if>
                                                                                    </td>                                                                                

                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>&nbsp;</td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td>
                                                                                    <c:if test="${pardetail.siType eq 'Sub Inspector (Civil)'}">
                                                                                        (b) Effectiveness in handling Law & Order/ Collection of Intelligence / command /control over Subordinates:
                                                                                    </c:if>
                                                                                    <c:if test="${pardetail.siType eq 'Sub Inspector (Armed)'}">
                                                                                        (b) Effectiveness in handling Force management / command /control over Subordinates/ interest in their training /development/ welfare:
                                                                                    </c:if>
                                                                                    <c:if test="${pardetail.siType eq 'Sub Inspector (Equivalent)'}">
                                                                                        (b) Effectiveness in handling work pertaining to their field/ command/control over Subordinates/ interest in their training /development/ welfare:
                                                                                    </c:if>
                                                                                </td>
                                                                                <td>
                                                                                    <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '1'}"> Below Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '2'}"> Average </c:if>
                                                                                    <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '3'}"> Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '4'}"> Very Good </c:if>
                                                                                    <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '5'}"> Outstanding </c:if>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>



                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><b>5.</b> Pen picture of Officer (not more than 100 words)</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;">
                                                                        ${reportingdt.penPictureOfOficerNote}
                                                                    </td>                                                                                
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>

                                                                <tr>
                                                                    <td style="padding-left: 10px;"><b>6.</b> Inadequacies, deficiencies or shortcomings, if any, not more than 200 words (Remarks to be treated as adverse)</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;">
                                                                        ${reportingdt.inadequaciesNote}
                                                                    </td> 
                                                                </tr>                                                                               
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>

                                                                <tr>
                                                                    <td style="padding-left: 10px;"><b>7.</b> Integrity ( If integrity is doubtful or adverse please write “Not certified” in the space below and justify your remarks here.</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;">
                                                                        ${reportingdt.integrityNote}
                                                                    </td>                                                                                
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;"><b>8.</b> State of Health</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <td style="padding-left: 10px;">
                                                                    <table width="99%" border="0" style="margin-left:35px;">
                                                                        <tr>
                                                                            <td width="60%">(a) State of Health (please indicate whether the officer’s state of health is)</td>
                                                                            <td width="35%"> ${reportingdt.stateOfHealth} </td>                                                                                
                                                                        </tr>
                                                                        <tr>
                                                                            <td>&nbsp;</td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(b) Sick Report (if more than 10 days at one time) mention period of sick report</td>
                                                                            <td> ${reportingdt.sickReportOnDate} </td>                                                                                
                                                                        </tr>
                                                                        <tr>
                                                                            <td>&nbsp;</td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(c) please indicate if appraisee reported sick to avoid posting on transfer or a specific duty.</td>
                                                                            <td> ${reportingdt.sickDetails} </td>                                                                                
                                                                        </tr>
                                                                    </table>
                                                                </td>

                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <!-- <tr> 
                                                                    <td style="padding-left: 10px;">9. Overal Grading( put Signature in appropriate box)( A+B+C+D)/4= </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr> -->

                                                                <tr>
                                                                    <td style="padding-left: 20px;"><b>9.</b> Overal Grading &nbsp; 
                                                                        <c:if test="${reportingdt.sltGrading eq '1'}"> Below Average </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '2'}"> Average </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '3'}"> Good </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '4'}"> Very Good </c:if>
                                                                        <c:if test="${reportingdt.sltGrading eq '5'}"> Outstanding </c:if>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><b>10.</b> For  Overall Grading  “Below Average” /  “Outstanding”  please provide justification in the   space below.:</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;">
                                                                        ${reportingdt.gradingNote}
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>

                                                            </table>
                                                        </c:if>
                                                        <c:if test="${reportingdt.isreportingcompleted != 'Y' && reportingdt.isreportingcompleted ne 'F'}">
                                                            <c:if test="${reportingdt.iscurrentreporting == 'Y'}">

                                                                <table width="100%" border="0">
                                                                    <tr> <td style="padding-left: 10px;"> &nbsp; </td> </tr>
                                                                    <tr>
                                                                        <%if (slno == 0) {%>
                                                                        <td style="font-weight:bold;text-decoration:underline;">
                                                                            <c:out value="${reportingdt.reportingauthName}"/>
                                                                        </td>
                                                                        <%} else {%>
                                                                        <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                            <c:out value="${reportingdt.reportingauthName}"/>
                                                                        </td>
                                                                        <%}%>
                                                                    </tr>

                                                                    <tr> <td style="padding-left: 10px;"> &nbsp; </td> </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span><b>1.</b> Personal Attribute (On a scale of 1-5 weightage for this section in 40%) “A” </span></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding: 5px;">
                                                                            <table width="60%" style="margin-left:35px;" border="0">
                                                                                <tr>
                                                                                    <td width="25%"> <span class="star">*</span> (a) Attitude to work </td>
                                                                                    <td width="20%">
                                                                                        <select name="ratingattitude" id="ratingattitude" class="form-control" onchange="calculateTotA()">
                                                                                            <option value="">Select</option>
                                                                                            <option value="1" <c:if test="${reportingdt.ratingattitude eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.ratingattitude eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.ratingattitude eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.ratingattitude eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.ratingattitude eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td> 
                                                                                    </tr>
                                                                                    <tr> <td colspan="2">&nbsp;</td> </tr>
                                                                                    <tr>
                                                                                        <td> <span class="star">*</span> (b) Sense of responsibility </td>   
                                                                                        <td>
                                                                                            <select name="ratingresponsibility" id="ratingresponsibility" class="form-control" onchange="calculateTotA()">
                                                                                                <option value="">Select</option>
                                                                                                <option value="1" <c:if test="${reportingdt.ratingresponsibility eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.ratingresponsibility eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.ratingresponsibility eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.ratingresponsibility eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.ratingresponsibility eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr> <td colspan="2">&nbsp;</td> </tr>
                                                                                    <tr>
                                                                                        <td> <span class="star">*</span> (c) Communication skill </td>
                                                                                        <td>
                                                                                            <select name="ratingcomskill" id="ratingcomskill" class="form-control" onchange="calculateTotA()">
                                                                                                <option value="">Select</option>
                                                                                                <option value="1" <c:if test="${reportingdt.ratingcomskill eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.ratingcomskill eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.ratingcomskill eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.ratingcomskill eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.ratingcomskill eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr> <td colspan="2">&nbsp;</td> </tr>
                                                                                    <tr>    
                                                                                        <td> <span class="star">*</span> (d) Leadership Qualities </td>
                                                                                        <td>
                                                                                            <select name="ratingleadership" id="ratingleadership" class="form-control" onchange="calculateTotA()">
                                                                                                <option value="">Select</option>
                                                                                                <option value="1" <c:if test="${reportingdt.ratingleadership eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.ratingleadership eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.ratingleadership eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.ratingleadership eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.ratingleadership eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr> <td colspan="2">&nbsp;</td> </tr>
                                                                                    <tr style="font-weight: bold;">
                                                                                        <td align="right"> A = (Total)&nbsp; </td>
                                                                                        <td align="left">
                                                                                            <input type="text" name="totalofA" id="totalofA" readonly="true" value="${reportingdt.totalofA}"/> / 4 = <b id="avgofA"></b> </b>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>


                                                                    <tr><td style="padding-left: 10px;"> &nbsp; </td> </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span><b>2.</b> Functional (On a Scale of 1-5,weightage for this section is 20%) “B” </span></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding: 5px;">
                                                                            <table width="65%" style="margin-left:25px;" border="0">    
                                                                                <tr>
                                                                                    <td width="40%"> <span class="star">*</span> (i) Knowledge of Criminal Laws/ Police Manuals and rules/Procedures/IT Skills/ Local norms in the relevant subjects </td>
                                                                                    <td width="15%">
                                                                                        <select name="ratingitskill" id="ratingitskill" class="form-control" onchange="calculateTotB()">
                                                                                            <option value="">Select</option>
                                                                                            <option value="1" <c:if test="${reportingdt.ratingitskill eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.ratingitskill eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.ratingitskill eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.ratingitskill eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.ratingitskill eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td>  
                                                                                    </tr>
                                                                                    <tr> <td colspan="2">&nbsp;</td> </tr>
                                                                                    <tr>
                                                                                        <td width="35%"> <span class="star">*</span> (ii) Attitude towards ST/SC/Weaker Sections & relation with Public </td>
                                                                                        <td width="14%">
                                                                                            <select name="ratingAttitudeStScSection" id="ratingAttitudeStScSection" class="form-control" onchange="calculateTotB()">
                                                                                                <option value="">Select</option>
                                                                                                <option value="1" <c:if test="${reportingdt.ratingAttitudeStScSection eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.ratingAttitudeStScSection eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.ratingAttitudeStScSection eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.ratingAttitudeStScSection eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.ratingAttitudeStScSection eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr> <td colspan="2">&nbsp;</td> </tr>

                                                                                    <tr style="font-weight: bold;">
                                                                                        <td align="right"> B = (Total) &nbsp; </td>
                                                                                        <td align="left">
                                                                                            <input type="text" name="totalofB" id="totalofB" readonly="true" value="${reportingdt.totalofB}"/> / 2 = <b><span id="avgofB"></span></b>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>

                                                                    <tr> <td>&nbsp;</td> </tr>  

                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span><b>3.</b> Assessment Of Performance Of 5T (20%) “C” </span></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding: 5px;">
                                                                            <table width="70%" style="margin-left:35px;" border="0">
                                                                                <tr>
                                                                                    <td width="15%"> <span class="star">*</span> (a) 10% on 5T charter of Department: (out of 10%) </td>
                                                                                    <td width="10%">
                                                                                        <select name="fiveTChartertenpercent" id="fiveTChartertenpercent" class="form-control" onchange="calculateTotC()">
                                                                                            <option value="">Select</option>
                                                                                            <option value="1" <c:if test="${reportingdt.fiveTChartertenpercent eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.fiveTChartertenpercent eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.fiveTChartertenpercent eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.fiveTChartertenpercent eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.fiveTChartertenpercent eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td>   
                                                                                    <tr>
                                                                                    <tr> <td colspan="2">&nbsp;</td> </tr>    
                                                                                    <tr>    
                                                                                        <td> <span class="star">*</span> (b) 5% on 5T charter of Government: (out of 5%) </td>
                                                                                        <td>
                                                                                            <select name="fiveTCharterfivePercent" id="fiveTCharterfivePercent" class="form-control" onchange="calculateTotC()">
                                                                                                <option value="">Select</option>
                                                                                                <option value="1" <c:if test="${reportingdt.fiveTCharterfivePercent eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.fiveTCharterfivePercent eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.fiveTCharterfivePercent eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.fiveTCharterfivePercent eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.fiveTCharterfivePercent eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr> <td colspan="2">&nbsp;</td> </tr>
                                                                                    <tr>
                                                                                        <td> <span class="star">*</span> (c) 5% on Mo Sarkar: (out of 5%) </td>
                                                                                        <td>
                                                                                            <select name="fiveTComponentmoSarkar" id="fiveTComponentmoSarkar" class="form-control" onchange="calculateTotC()">
                                                                                                <option value="">Select</option>
                                                                                                <option value="1" <c:if test="${reportingdt.fiveTComponentmoSarkar eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.fiveTComponentmoSarkar eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.fiveTComponentmoSarkar eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.fiveTComponentmoSarkar eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.fiveTComponentmoSarkar eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td>                                                                                
                                                                                    </tr>
                                                                                    <tr> <td colspan="2">&nbsp;</td> </tr>
                                                                                    <tr style="font-weight: bold;">
                                                                                        <td align="right"> <b> C = (Total) </b> &nbsp;</td>
                                                                                        <td><input type="text" name="totalofC" id="totalofC" readonly="true" value="${reportingdt.totalofC}"/> / 3 = <b><span id="avgofC"></span></td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>


                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span><b>4.</b> Assessment of Work output (on scale of 1-5 weightage for this section 20%) “D”</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;">
                                                                            <table width="70%" style="margin-left:35px;" border="0">
                                                                                <tr>
                                                                                    <td width="60%"> <span class="star">*</span> 
                                                                                        <c:if test="${pardetail.siType eq 'Sub Inspector (Civil)'}">
                                                                                            (a) Quality of output and effectiveness in investigation/ Enquiry (Eg. Convictions):
                                                                                        </c:if>
                                                                                        <c:if test="${pardetail.siType eq 'Sub Inspector (Armed)'}">
                                                                                            (a) Quality of output and effectiveness in management of Stores/ Procurement/ Maintenance of Arms & Ammunitions:
                                                                                        </c:if>
                                                                                        <c:if test="${pardetail.siType eq 'Sub Inspector (Equivalent)'}">
                                                                                            (a) Quality of output and effectiveness in management of equipment/stores/records pertaining to their field:
                                                                                        </c:if>
                                                                                    </td>
                                                                                    <td width="30%">
                                                                                        <select name="ratingQualityOfOutput" id="ratingQualityOfOutput" class="form-control" onchange="calculateTotD()">
                                                                                            <option value="">Select</option>
                                                                                            <option value="1" <c:if test="${reportingdt.ratingQualityOfOutput eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.ratingQualityOfOutput eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.ratingQualityOfOutput eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.ratingQualityOfOutput eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.ratingQualityOfOutput eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td>                                                                                
                                                                                    </tr>
                                                                                    <tr> <td>&nbsp;</td> </tr>
                                                                                    <tr>
                                                                                        <td> <span class="star">*</span> 
                                                                                        <c:if test="${pardetail.siType eq 'Sub Inspector (Civil)'}">
                                                                                            (b) Effectiveness in handling Law & Order/ Collection of Intelligence / command /control over Subordinates:
                                                                                        </c:if>
                                                                                        <c:if test="${pardetail.siType eq 'Sub Inspector (Armed)'}">
                                                                                            (b) Effectiveness in handling Force management / command /control over Subordinates/ interest in their training /development/ welfare:
                                                                                        </c:if>
                                                                                        <c:if test="${pardetail.siType eq 'Sub Inspector (Equivalent)'}">
                                                                                            (b) Effectiveness in handling work pertaining to their field/ command/control over Subordinates/ interest in their training /development/ welfare:
                                                                                        </c:if>
                                                                                    </td>
                                                                                    <td>
                                                                                        <select name="ratingeffectivenessHandlingWork" id="ratingeffectivenessHandlingWork" class="form-control" onchange="calculateTotD()">
                                                                                            <option value="">Select</option>
                                                                                            <option value="1" <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.ratingeffectivenessHandlingWork eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <tr> <td>&nbsp;</td> </tr>
                                                                                    <tr>
                                                                                        <td align="right"> <b>(D) = Total </b> </td>
                                                                                        <td>
                                                                                            <input type="text" name="totalofD" id="totalofD" readonly="true" value="${reportingdt.totalofD}"/> <b> / 2 = <span id="avgofD"> </b></span>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr> <td>&nbsp;</td> </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><b>5.</b>  <span class="star">*</span> Pen picture of Officer (not more than 100 words)</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;">
                                                                            <textarea name="penPictureOfOficerNote" class="textareacolor" rows="10" cols="80" id="penPictureOfOficerNote" style="width:99%;height:60px;text-align:left;resize: none;">${reportingdt.penPictureOfOficerNote}</textarea>
                                                                        </td>                                                                                
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><b>6.</b>  <span class="star">*</span> Inadequacies, deficiencies or shortcomings, if any, not more than 200 words (Remarks to be treated as adverse)</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;">
                                                                            <textarea name="inadequaciesNote" class="textareacolor" rows="4" id="inadequaciesNote" style="width:99%;height:60px;text-align:left;resize: none;">${reportingdt.inadequaciesNote}</textarea>
                                                                        </td> 
                                                                    </tr>                                                                               
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><b>7.</b>  <span class="star">*</span> Integrity ( If integrity is doubtful or adverse please write “Not certified” in the space below and justify your remarks here.</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;">
                                                                            <textarea name="integrityNote" id="integrityNote" class="textareacolor" rows="4" style="width:99%;height:60px;text-align:left;resize: none;">${reportingdt.integrityNote}</textarea>
                                                                        </td>                                                                                
                                                                    </tr>
                                                                    <tr> <td>&nbsp;</td> </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><b>8.</b> State of Health</td>
                                                                    </tr>
                                                                    <tr> <td>&nbsp;</td> </tr>
                                                                    <td style="padding-left: 10px;">
                                                                        <table width="60%" style="margin-left:35px;">
                                                                            <tr>
                                                                                <td>(a) State of Health (please indicate whether the officer’s state of health is)</td>

                                                                                <td>
                                                                                    <input type="hidden" id="stateOfHealth" value="${reportingdt.stateOfHealth}"/>
                                                                                    <select name="stateOfHealth" id="stateOfHealth" class="form-control">
                                                                                        <option value="">Select</option>
                                                                                        <option value="Good" <c:if test="${reportingdt.stateOfHealth eq 'Good'}">selected="selected"</c:if> > Good </option>
                                                                                        <option value="Indifferent" <c:if test="${reportingdt.stateOfHealth eq 'Indifferent'}">selected="selected"</c:if> > Indifferent </option>
                                                                                        <option value="Bad" <c:if test="${reportingdt.stateOfHealth eq 'Bad'}">selected="selected"</c:if> > Bad </option>
                                                                                        </select>
                                                                                    </td>                                                                                
                                                                                </tr>
                                                                                <tr> <td>&nbsp;</td> </tr>
                                                                                <tr>
                                                                                    <td>(b) Sick Report (if more than 10 days at one time) mention period of sick report</td>

                                                                                    <td>
                                                                                        <div style="position: relative">
                                                                                            <textarea name="sickReportOnDate" id="sickReportOnDate" class="textareacolor" rows="4" style="width:250%;height:100px;text-align:left;resize: none;">${reportingdt.sickReportOnDate}</textarea>
                                                                                    </div>
                                                                                </td>                                                                                
                                                                            </tr>
                                                                            <tr> <td>&nbsp;</td> </tr>
                                                                            <tr>
                                                                                <td>(c) Please indicate if appraisee reported sick to avoid posting on transfer or a specific duty</td>
                                                                                <td>
                                                                                    <textarea name="sickDetails"  class="textareacolor" rows="4" id="sickDetails" style="width:250%;height:100px;text-align:left;resize: none;">${reportingdt.sickDetails}</textarea>
                                                                                </td>                                                                                
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                    <tr> <td>&nbsp;</td> </tr>


                                                                    <tr> 
                                                                        <td style="padding-left: 10px;">
                                                                            <table width="75%" border="0">
                                                                                <tr>
                                                                                    <td width="18%"><b>9.</b>  <span class="star">*</span> Overall Grading ( A+B+C+D) / 4 </td>
                                                                                    <td width="15%"> &nbsp; </td> 
                                                                                    <td width="15%"> Overall Grading  </td>
                                                                                    <td width="15%"> 
                                                                                        <select name="sltGrading" id="sltGrading" class="form-control">
                                                                                            <option value=""> -- Select -- </option>
                                                                                            <option value="1" <c:if test="${reportingdt.sltGrading eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reportingdt.sltGrading eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reportingdt.sltGrading eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reportingdt.sltGrading eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reportingdt.sltGrading eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td> 
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>

                                                                        <tr> <td>&nbsp;</td> </tr>

                                                                        <tr>
                                                                            <td style="padding-left: 10px;"><b>10.</b> For  Overall Grading  “Below Average” /  “Outstanding”  please provide justification in the space below</td>
                                                                        </tr>
                                                                        <tr> <td>&nbsp;</td> </tr>
                                                                        <tr>
                                                                            <td style="padding-left: 20px;">
                                                                                <textarea name="gradingNote" class="textareacolor" rows="4" id="gradingNote" style="width:99%;height:60px;text-align:left;resize: none;">${reportingdt.gradingNote}</textarea>
                                                                        </td>
                                                                    </tr>
                                                                    <tr> <td>&nbsp;</td> </tr>

                                                                </table>
                                                            </c:if>
                                                        </c:if>
                                                        <%slno += 1;%>
                                                    </c:forEach>
                                                </c:if>
                                            </td>
                                        </tr>
                                    </table>
                                </div>



                                <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                                    <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Remarks of Reviewing Authority</div>
                                    <c:if test="${not empty pardetail.reviewingdata}">
                                        <%slno = 0;%>
                                        <c:forEach var="reviewingdt" items="${pardetail.reviewingdata}">
                                            <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                                <tr style="height: 40px">
                                                    <td width="70%">
                                                        <span>
                                                            <c:if test="${reviewingdt.isreviewingcompleted == 'Y'}">
                                                                <table width="100%">
                                                                    <tr>
                                                                        <%if (slno == 0) {%>
                                                                        <td style="font-weight:bold;text-decoration:underline;">
                                                                            <c:out value="${reviewingdt.reviewingauthName}"/>
                                                                        </td>
                                                                        <%} else {%>
                                                                        <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                            <c:out value="${reviewingdt.reviewingauthName}"/>
                                                                        </td>
                                                                        <%}%>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;">
                                                                            <div style="padding:5px;"><c:out value="${reviewingdt.reviewingNote}"/></div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;"> <span>2. Overall Grading Given By Reviewing Authority  :</span>
                                                                            <c:out value="${reviewingdt.reviewGrading}"/>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                </table>
                                                            </c:if>
                                                            <c:if test="${reviewingdt.isreviewingcompleted == 'F'}">
                                                                <table>
                                                                    <tr>
                                                                        <%if (slno == 0) {%>
                                                                        <td style="font-weight:bold;text-decoration:underline;">
                                                                            <c:out value="${reviewingdt.reviewingauthName}"/>
                                                                        </td>
                                                                        <%} else {%>
                                                                        <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                            <c:out value="${reviewingdt.reviewingauthName}"/>
                                                                        </td>
                                                                        <%}%>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;">
                                                                            No Remarks Given by the Authority.
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </c:if>

                                                            <c:if test="${reviewingdt.isreviewingcompleted != 'Y'}">
                                                                <c:if test="${reviewingdt.iscurrentreviewing == 'Y'}">
                                                                    <table width="100%">
                                                                        <tr>
                                                                            <%if (slno == 0) {%>
                                                                            <td style="font-weight:bold;text-decoration:underline;">
                                                                                <c:out value="${reviewingdt.reviewingauthName}"/>
                                                                            </td>
                                                                            <%} else {%>
                                                                            <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                                <c:out value="${reviewingdt.reviewingauthName}"/>
                                                                            </td>
                                                                            <%}%>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>&nbsp;</td>
                                                                        </tr>
                                                                    </table>

                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span><b>1.</b> Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.</span></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>

                                                                        <td style="padding-left: 20px;"> Standard Template:
                                                                            <select name="reviewingNotetemplate" id="reviewingNotetemplate" style="width: 400px;" onchange="copytemplate(this, 'reviewingNote')">
                                                                                <option value="">Select</option>
                                                                                <c:forEach items="${reviewingNote}" var="reviewingNotetemplate">
                                                                                    <option value="${reviewingNotetemplate}">${reviewingNotetemplate}</option>
                                                                                </c:forEach>
                                                                                <option value="Custom">Custom</option>
                                                                            </select>

                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 20px;"><textarea name="reviewingNote" class="textareacolor" rows="4" id="reviewingNote"  style="width:80%;height:60px;text-align:left;resize: none;">${reviewingdt.reviewingNote}</textarea></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;">
                                                                            <table width="35%" border="0">
                                                                                <tr>
                                                                                    <td width="20%" style="padding-left: 20px;"> <span><b>2.</b> Overall Grading Given By Reviewing Authority  :</span>  
                                                                                    <td width="10%"> 
                                                                                        <select name="sltReviewGrading" id="sltReviewGrading" class="form-control">
                                                                                            <option value=""> -- Select -- </option>
                                                                                            <option value="1" <c:if test="${reviewingdt.sltReviewGrading eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${reviewingdt.sltReviewGrading eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${reviewingdt.sltReviewGrading eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${reviewingdt.sltReviewGrading eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${reviewingdt.sltReviewGrading eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td> 
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>&nbsp;</td>
                                                                        </tr>
                                                                </c:if>
                                                            </c:if>
                                                        </span>
                                                    </td>
                                                </tr>
                                            </table>
                                            <%slno += 1;%>
                                        </c:forEach>
                                    </c:if>
                                </div>

                                <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                                    <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Remarks of Accepting Authority</div>
                                    <c:if test="${not empty pardetail.acceptingdata}">
                                        <%slno = 0;%>
                                        <c:forEach var="acceptingdt" items="${pardetail.acceptingdata}">
                                            <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
                                                <tr style="height: 40px">
                                                    <td width="70%">
                                                        <span>
                                                            <c:if test="${acceptingdt.isacceptingcompleted == 'Y'}">
                                                                <table width="100%">
                                                                    <tr>
                                                                        <%if (slno == 0) {%>
                                                                        <td style="font-weight:bold;text-decoration:underline;">
                                                                            <c:out value="${acceptingdt.acceptingauthName}"/>
                                                                        </td>
                                                                        <%} else {%>
                                                                        <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                            <c:out value="${acceptingdt.acceptingauthName}"/>
                                                                        </td>
                                                                        <%}%>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;">
                                                                            <div style="padding:5px;"><c:out value="${acceptingdt.acceptingNote}"/></div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;"> <span>2. Overall Grading Given By Accepting Authority  :</span>
                                                                            <c:out value="${acceptingdt.sltAcceptingGrading1}"/>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                </table>
                                                            </c:if>
                                                            <c:if test="${acceptingdt.isacceptingcompleted == 'F'}">
                                                                <table>
                                                                    <tr>
                                                                        <%if (slno == 0) {%>
                                                                        <td style="font-weight:bold;text-decoration:underline;">
                                                                            <c:out value="${acceptingdt.acceptingauthName}"/>
                                                                        </td>
                                                                        <%} else {%>
                                                                        <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                            <c:out value="${acceptingdt.acceptingauthName}"/>
                                                                        </td>
                                                                        <%}%>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;">
                                                                            No Remarks Given by the Authority.
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </c:if>

                                                            <c:if test="${acceptingdt.isacceptingcompleted != 'Y'}">
                                                                <c:if test="${acceptingdt.iscurrentaccepting == 'Y'}">
                                                                    <table width="100%">
                                                                        <tr>
                                                                            <%if (slno == 0) {%>
                                                                            <td style="font-weight:bold;text-decoration:underline;">
                                                                                <c:out value="${acceptingdt.acceptingauthName}"/>
                                                                            </td>
                                                                            <%} else {%>
                                                                            <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                                <c:out value="${acceptingdt.acceptingauthName}"/>
                                                                            </td>
                                                                            <%}%>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>&nbsp;</td>
                                                                        </tr>
                                                                    </table>

                                                                    <c:if test="${pardetail.parstatus == '8'}">
                                                                        <tr>
                                                                            <td style="padding-left: 10px;"> 1. Accepting Note </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td style="padding-left: 20px;">
                                                                                <textarea name="acceptingNote" class="textareacolor" rows="4" id="acceptingNote" style="width:80%;height:60px;text-align:left;resize: none;">${acceptingdt.acceptingNote}</textarea>
                                                                            </td>
                                                                        </tr>
                                                                    </c:if>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;">
                                                                            <table width="35%" border="0">
                                                                                <tr>
                                                                                    <td width="20%" style="padding-left: 20px;"> <span><b>2.</b> Overall Grading Given By Accepting Authority  :</span>  
                                                                                    <td width="10%"> 
                                                                                        <select name="sltAcceptingGrading" id="sltAcceptingGrading" class="form-control">
                                                                                            <option value=""> -- Select -- </option>
                                                                                            <option value="1" <c:if test="${acceptingdt.sltAcceptingGrading eq '1'}">selected="selected"</c:if> > Below Average </option>
                                                                                            <option value="2" <c:if test="${acceptingdt.sltAcceptingGrading eq '2'}">selected="selected"</c:if> > Average </option>
                                                                                            <option value="3" <c:if test="${acceptingdt.sltAcceptingGrading eq '3'}">selected="selected"</c:if> > Good </option>
                                                                                            <option value="4" <c:if test="${acceptingdt.sltAcceptingGrading eq '4'}">selected="selected"</c:if> > Very Good </option>
                                                                                            <option value="5" <c:if test="${acceptingdt.sltAcceptingGrading eq '5'}">selected="selected"</c:if> > Outstanding </option>
                                                                                            </select>
                                                                                        </td> 
                                                                                    </tr>
                                                                                </table>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>&nbsp;</td>
                                                                        </tr>
                                                                </c:if>
                                                            </c:if>
                                                        </span>
                                                    </td>
                                                </tr>
                                            </table>
                                            <%slno += 1;%>
                                        </c:forEach>
                                    </c:if>
                                </div>
                        </div>
                    </div>
                </div>  
            </c:if>
            <div align="center">
                <div class="controlpanelDiv">	
                    <table width="100%" cellpadding="0" cellspacing="0" >
                        <tr style="height:40px">
                            <td align="left" class="skinbutton sb_active">
                                <span style="padding-left:10px;">
                                    <%
                                        int parId = 0;
                                        int taskId = 0;
                                        String encparId = "";
                                    %>
                                    <c:if test="${not empty pardetail.parid}">
                                        <c:set var="pid" value="${pardetail.parid}"/>
                                        <c:set var="encryptpid" value="${encodedparid}"/>
                                        <%
                                            parId = Integer.parseInt(pageContext.getAttribute("pid") + "");
                                            encparId = pageContext.getAttribute("encryptpid") + "";
                                        %>
                                    </c:if>
                                    <c:if test="${not empty pardetail.taskid}">
                                        <c:set var="tid" value="${pardetail.taskid}"/>
                                        <%
                                            taskId = Integer.parseInt(pageContext.getAttribute("tid") + "");
                                        %>
                                    </c:if>                                        

                                    <%--<html:submit property="submit" value="Cancel" styleClass="btn1" />--%>
                                    <%
                                        //pdflink = "viewPAR.htm?parid="+parId+"&taskid="+taskId;
                                        pdflink = "viewPAR.htm?parid=" + encparId;
                                    %>
                                    <%--<html:submit property="submit" value="Download" styleClass="btn1" />--%>
                                    <a href='<%=pdflink%>' target="_blank" class="btn btn-success"><b>Download</b></a>
                                    <c:if test="${pardetail.ishideremark == 'N'}">
                                        <c:if test="${pardetail.parstatus != '21' && pardetail.parstatus < 9}">
                                            <c:if test="${pardetail.isClosedFiscalYearAuthority != 'Y'}">

                                                <input type="submit" name="forwardpar" value="Save" class="btn btn-info" onclick="return savecheck()"/> 
                                                <input type="submit" name="forwardpar" value="Submit" class="btn btn-warning" onclick="return submitcheck()"/> 

                                                <%--<html:submit property="submit" style="margin-left:100px;color:red;" value="Revert" styleClass="btn1" />--%>
                                                <%
                                                    revertlink = "revertPAR.htm?parid=" + parId;
                                                %>
                                                <%--<html:link action='<%=revertlink%>' target="_blank" styleClass="btn1">Revert</html:link>--%>
                                                <c:if test="${pardetail.parstatus == '6'}">
                                                    <a href="javascript:revertClick();" class="btn btn-danger">Revert</a>
                                                </c:if>
                                                <c:if test="${pardetail.parstatus == '7'}">
                                                    <a href="javascript:revertClick();" class="btn btn-danger">Revert</a>
                                                </c:if>
                                                <c:if test="${pardetail.parstatus == '8'}">
                                                    <a href="javascript:revertClick();" class="btn btn-danger">Revert</a>
                                                </c:if>
                                                <c:if test="${isClosed == 'Y'}">
                                                    <span style="color:red;">Remarks Submission for Financial Year <c:out value="${pardetail.fiscalYear}"/> is closed.</span>
                                                </c:if>
                                            </c:if>
                                        </c:if>
                                    </c:if>
                                </span>
                            </td>
                            <td width="60%" style="color:black;">
                                EMP ID - <b><c:out value="${pardetail.applicantempid}"/>,</b> &nbsp;
                                PAR ID - <b><c:out value="${pardetail.parid}"/></b> &nbsp;
                                Submitted On - <b><c:out value="${pardetail.submitted_on}"/></b>
                            </td>
                        </tr>                        
                    </table>                                                
                </div>
            </div>
        </form>

        <div id="setCustomForAuthnote" class="modal fade" >
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Set Custom For Authority Note</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-sm-3">Authority Note</label>
                                <div class="col-sm-9">
                                    <input type="text" name="templateContent" id="templateContent" style="width: 80%;height: 100px;"/>                                
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="modal-footer">
                        <div class="col-sm-10" >
                            <button type="button" class="btn btn-priamry" onclick="return saveCustomTemplate()">Save</button>
                            <%--<input type="button" value="Save" onclick="return saveCustomTemplate()"> --%>
                            <button type="button" class="btn btn-danger" data-dismiss="modal">cancel</button>
                        </div>
                    </div>

                    <div style="padding: 5px;">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Si No.</th>
                                    <th>Template text</th>
                                    <th>Remove</th>
                                </tr>
                            </thead>
                            <tbody id="loginwisetemplateList">                                    

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div id="revertPAR" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">

                    <input type="hidden" id="parId"/>
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Reason For Revert</h4>
                    </div>
                    <div class="modal-body">

                    </div>
                </div>
            </div>
        </div>
    </c:if>

</body>
</html>

