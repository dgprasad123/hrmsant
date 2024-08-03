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
        <title> :: Performance Appraisal :: </title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>

        <script type="text/javascript">
            var templateHeading;
            $(document).ready(function() {
            <%-- $('#sickReportOnDate').datetimepicker({
                 format: 'D-MMM-YYYY',
                 useCurrent: false,
                 ignoreReadonly: true
             }); --%>

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
                var aa = $("#totalofA").val(tot);
                var avg = tot / 4;
                $('#avgofA').html(avg);
            }
            function calculateTotB() {
                totalofB = parseInt(0 + $("#ratingitskill").val()) + parseInt(0 + $("#ratingAttitudeStScSection").val());
                var bb = $("#totalofB").val(totalofB);
                var avgB = totalofB / 2;
                $('#avgofB').html(avgB);
            }
            function calculateTotC() {
                totalofC = parseInt(0 + $("#fiveTChartertenpercent").val()) + parseInt(0 + $("#fiveTCharterfivePercent").val()) + parseInt(0 + $("#fiveTComponentmoSarkar").val());
                var cc = $("#totalofC").val(totalofC);
                var avg = (totalofC / 3).toFixed(2);
                $('#avgofC').html(avg);
            }
            function calculateTotD() {
                totalofD = parseInt(0 + $("#ratingQualityOfOutput").val()) + parseInt(0 + $("#ratingeffectivenessHandlingWork").val());
                var dd = $("#totalofD").val(totalofD);
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
                    if ($("#ratingcoordination").val() == "") {
                        alert("Please Select rating coordination");
                        $("#ratingcoordination").focus();
                        return false;

                    }
                    if ($("#ratingresponsibility").val() == "") {
                        alert("Please Select rating responsibility")
                        $("#ratingresponsibility").focus();
                        return false;

                    }
                    if ($("#teamworkrating").val() == "") {
                        alert("Please Select Ability to work in a team");
                        $("#teamworkrating").focus();
                        return false;

                    }
                    if ($("#ratingcomskill").val() == "") {
                        alert("Please Select Communication skill");
                        $("#ratingcomskill").focus();
                        return false;

                    }
                    if ($("#ratingitskill").val() == "") {
                        alert("Please Select Knowledge of Rules");
                        $("#ratingitskill").focus();
                        return false;

                    }
                    if ($("#ratingleadership").val() == "") {
                        alert("Please Select Leadership Qualities");
                        $("#ratingleadership").focus();
                        return false;

                    }
                    if ($("#ratinginitiative").val() == "") {
                        alert("Please Select Initiative");
                        $("#ratinginitiative").focus();
                        return false;

                    }
                    if ($("#ratingdecisionmaking").val() == "") {
                        alert("Please Select Decision-making ability");
                        $("#ratingdecisionmaking").focus();
                        return false;

                    }
                    if ($("#ratequalityofwork").val() == "") {
                        alert("Please Select Quality of Work");
                        $("#ratequalityofwork").focus();
                        return false;

                    }
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
        </style>
    </head>
    <body>
        
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
                                    
                    <div style="width:100%;overflow: auto;margin-top:5px;margin-bottom: 20px;border:1px solid #5095ce;">
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

                                                        <input type="submit" name="forwardpar" value="Save" class="btn btn-default" onclick="return savecheck()"/> 
                                                        <input type="submit" name="forwardpar" value="Submit" class="btn btn-default" onclick="return submitcheck()"/> 

                                                        <%--<html:submit property="submit" style="margin-left:100px;color:red;" value="Revert" styleClass="btn1" />--%>
                                                        <%
                                                            revertlink = "revertPAR.htm?parid=" + parId;
                                                        %>
                                                        <%--<html:link action='<%=revertlink%>' target="_blank" styleClass="btn1">Revert</html:link>--%>
                                                        <c:if test="${pardetail.parstatus == '6'}">
                                                            <a href="javascript:revertClick();" class="btn btn-default">Revert</a>
                                                        </c:if>
                                                        <c:if test="${pardetail.parstatus == '7'}">
                                                            <a href="javascript:revertClick();" class="btn btn-default">Revert</a>
                                                        </c:if>
                                                        <c:if test="${pardetail.parstatus == '8'}">
                                                            <a href="javascript:revertClick();" class="btn btn-default">Revert</a>
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
                                        EMP ID - <b> <c:out value="${pardetail.applicantempid}"/></b>,&nbsp;
                                        PAR ID - <b> <c:out value="${pardetail.parid}"/></b> &nbsp;
                                        Submitted On - <b> <c:out value="${pardetail.submitted_on}"/></b>
                                    </td>
                                </tr>                        
                            </table>                                                
                        </div>
                    </div>                
                </div>
                                    
            </div>
        </div>    
       

</body>
</html>

