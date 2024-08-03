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
        <title>Performance Appraisal</title>

        <link href="resources/css/colorbox.css" rel="stylesheet">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="resources/css/themes/default/easyui.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/themes/icon.css"/>

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/basicjavascript.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            var templateHeading;
            $(document).ready(function() {
                $(".documentDiv").hide();
                $(".documentDivReviewing").hide();
                $(".documentDivAccepting").hide();



                $('#sltGrading').combobox({
                    onChange: function(value) {
                        var grading = $(this).combobox('getValue');
                        var grde = grading;
                        var avg = "Below Average";
                        var outs = "Outstanding";
                        var decs = (grde == 1) ? avg : outs;
                        if ((grde == 1 || grde == 5)) {
                            $(".documentDiv").show();
                        } else {
                            $(".documentDiv").hide();
                        }
                    }
                })
                $('#sltReviewGrading').combobox({
                    onChange: function(value) {
                        var reviewgrading = $(this).combobox('getValue');
                        var grde = reviewgrading;
                        var avg = "Below Average";
                        var outs = "Outstanding";
                        var decs = (grde == 1) ? avg : outs;
                        if ((grde == 1 || grde == 5)) {
                            $(".documentDivReviewing").show();
                        } else {
                            $(".documentDivReviewing").hide();
                        }
                    }
                })
                $('#sltAcceptingGrading').combobox({
                    onChange: function(value) {
                        var acceptinggrading = $(this).combobox('getValue');
                        var grde = acceptinggrading;
                        var avg = "Below Average";
                        var outs = "Outstanding";
                        var decs = (grde == 1) ? avg : outs;
                        if ((grde == 1 || grde == 5)) {
                            $(".documentDivAccepting").show();
                        } else {
                            $(".documentDivAccepting").hide();
                        }
                    }
                })

                $("#collapseOne").on("hide.bs.collapse", function() {
                    $(".divcls").html('<span class="pull-right glyphicon glyphicon-plus"></span> Absentee Statement<span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>');
                });
                $("#collapseOne").on("show.bs.collapse", function() {
                    $(".divcls").html('<span class="pull-right glyphicon glyphicon-minus"></span> Absentee Statement <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>');
                });

                $("#collapsetwo").on("hide.bs.collapse", function() {
                    $(".divcls1").html('<span class="pull-right glyphicon glyphicon-plus"></span> Achievements <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>');
                });
                $("#collapsetwo").on("show.bs.collapse", function() {
                    $(".divcls1").html('<span class="pull-right glyphicon glyphicon-minus"></span> Achievements <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>');
                });

                $("#collapseThree").on("hide.bs.collapse", function() {
                    $(".divcls2").html('<span class="pull-right glyphicon glyphicon-plus"></span> Other Details <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>');
                });
                $("#collapseThree").on("show.bs.collapse", function() {
                    $(".divcls2").html('<span class="pull-right glyphicon glyphicon-minus"></span> Other Details <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>');
                });

                $.post("GetPARGradeListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#ratingattitude').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#ratingcoordination').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#ratingresponsibility').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#teamworkrating').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#ratingcomskill').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#ratingitskill').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
                        $('#ratingleadership').append($("<option></option>").attr("value", data[i].value).text(data[i].value));
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
                    $('#sltGrading').combobox('setValue', $('#hidsltGrading').val());
                }
                if ($('#hidsltReviewGrading').val() > 0) {
                    $('#sltReviewGrading').combobox('setValue', $('#hidsltReviewGrading').val());
                }
                if ($('#hidsltAcceptingGrading').val() > 0) {
                    $('#sltAcceptingGrading').combobox('setValue', $('#hidsltAcceptingGrading').val());
                }



            })
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
            var allowedExtensions = /(\.pdf)$/i;
            function uploadValidation(me) {
                if ($(me).val() != '') {
                    var fileId = $(me).attr("id");
                    var fi = document.getElementById(fileId);
                    var fsize = fi.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 4096) {
                        alert("File too Big, please select a file less than 4mb");
                        $("#" + fileId).val('');
                        return false;
                    } else {
                        if (!allowedExtensions.exec($("#" + fileId).val())) {
                            alert('Please upload file having extensions .pdf only.');
                            $("#" + fileId).val('');
                            return false;
                        } else {
                            $("#clearbtn").show();
                            return true;
                        }

                    }
                }
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
                    var grading = $('#sltGrading').combobox('getValue');
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
                        } else if ((grde == 2 || grde == 3 || grde == 4) && $('#gradingDocumentReporting').val() != '') {
                            alert("Please Delete the attached Document");
                            $(".documentDiv").show();
                            $('#gradingDocumentReporting').focus();
                            return false;
                        }
                    }
                } else if (parstatus == "7") {
                    if ($('#reviewingNote').val() == '') {
                        alert("Please enter Reviewing Note");
                        $('#reviewingNote').focus();
                        return false;
                    }
                    var reviewinggrading = $('#sltReviewGrading').combobox('getValue');
                    if (reviewinggrading == '') {
                        alert("Please select Reviewing Overall Grading");
                        $('#sltReviewGrading').focus();
                        return false;
                    }
                    if ((reviewinggrading == 2 || reviewinggrading == 3 || reviewinggrading == 4) && $('#gradingDocumentReviewing').val() != '') {
                        alert("Please Delete the attached Document");
                        $(".documentDivReviewing").show();
                        $('#gradingDocumentReviewing').focus();
                        return false;
                    }
                } else if (parstatus == "8") {
                    if ($('#acceptingNote').val() == '') {
                        alert("Please enter Accepting Note");
                        $('#acceptingNote').focus();
                        return false;
                    }
                    var acceptinggrading = $('#sltAcceptingGrading').combobox('getValue');
                    if (acceptinggrading == '') {
                        alert("Please select Accepting Overall Grading");
                        $('#sltAcceptingGrading').focus();
                        return false;
                    }
                    if ((acceptinggrading == 2 || acceptinggrading == 3 || acceptinggrading == 4) && $('#gradingDocumentAccepting').val() != '') {
                        alert("Please Delete the attached Document");
                        $(".documentDivAccepting").show();
                        $('#gradingDocumentAccepting').focus();
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
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "100%"});
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
                } else if (comboname == "acceptingNotetemplate") {
                    templateHeading = "Accepting Note";
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
            function removeGradingAttachmentReporting(prtid, parId) {
                if (confirm('Are you sure to Delete this Attachment?')) {
                    $.ajax({
                        type: "POST",
                        data: {prtid: prtid, parId: parId},
                        url: "removeGradingAttachmentReporting.htm",
                        dataType: "json",
                        success: function(data) {
                            if (data.deletestatus == 'Y') {
                                //location.reload();
                                window.location = "par/PARDetailView.htm?parid=" + $("#encodedparid").val() + "&taskid=" + $("#taskId").val() + "&auth=";
                            } else {
                                alert("Deletion Failed. Try Again after some time.");
                            }
                        }
                    });
                }
            }
            function removeGradingAttachmentReviewing(prtid, parId) {
                if (confirm('Are you sure to Delete this Attachment?')) {
                    $.ajax({
                        type: "POST",
                        data: {prtid: prtid, parId: parId},
                        url: "removeGradingAttachmentReviewing.htm",
                        dataType: "json",
                        success: function(data) {
                            if (data.deletestatus == 'Y') {
                                //location.reload();
                                window.location = "par/PARDetailView.htm?parid=" + $("#encodedparid").val() + "&taskid=" + $("#taskId").val() + "&auth=";
                            } else {
                                alert("Deletion Failed. Try Again after some time.");
                            }
                        }
                    });
                }
            }
            function removeGradingAttachmentAccepting(pactid, parId) {
                if (confirm('Are you sure to Delete this Attachment?')) {
                    $.ajax({
                        type: "POST",
                        data: {pactid: pactid, parId: parId},
                        url: "removeGradingAttachmentAccepting.htm",
                        dataType: "json",
                        success: function(data) {
                            if (data.deletestatus == 'Y') {
                                //location.reload();
                                window.location = "par/PARDetailView.htm?parid=" + $("#encodedparid").val() + "&taskid=" + $("#taskId").val() + "&auth=";
                            } else {
                                alert("Deletion Failed. Try Again after some time.");
                            }
                        }
                    });
                }
            }


        </script>
        <style type="text/css">
            body{
                font-family: Arial;
                font-size:13px;
                .red-color {
                    color:red;
                }
            }
        </style>

    </head>
    <body>
        <div align="center" style="margin-top:5px;margin-bottom:10px;">
            <div align="center">
                <input type="hidden" id="encodedparid" value="${encodedparid}"/>
                <table border="0" width="99%" cellpadding="0" cellspacing="0" style="font-size:12px; font-family:verdana;">
                    <tr>
                        <td style="background-color:#5095ce;color:#FFFFFF;padding:0px;font-weight:bold;" align="center">
                            <c:if test="${pardetail.parType eq 'SiPar'}">
                                <h2>Performance Appraisal Report (PAR) for SI & Equivalent Ranks (Group - B)</h2>
                            </c:if>
                            <c:if test="${pardetail.parType ne 'SiPar'}">
                                <h2>Performance Appraisal Report (PAR) for Group 'A' & 'B' officers of Govt. of Odisha</h2>
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
                        <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 17px" align="left">Details of Transmission / Movement of PAR</div>
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
                        <div style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 17px" align="left">Personal Information</div>                            
                        <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 1. </td>
                                <td width="20%">Applicant</td>
                                <td width="70%">
                                    <span style="font-weight: bold;"> <c:out value="${pardetail.applicant}"/> </span>
                                </td> 
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
                                <td width="20%">Appraisal Period</td>
                                <td width="70%">
                                    <span>
                                        From : <c:out value="${pardetail.parPeriodFrom}"/> -  To: <c:out value="${pardetail.parPeriodTo}"/>
                                    </span>
                                </td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 4. </td>
                                <td width="20%">Date of Birth</td>
                                <td width="70%"><span><c:out value="${pardetail.dob}"/></span></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 5. </td>
                                <td width="20%">Service to which the officer belongs</td>
                                <td width="70%"><span><c:out value="${pardetail.empService}"/></span></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 6. </td>
                                <td width="20%">Group to which the officer belongs</td>
                                <td width="70%"> <span><c:out value="${pardetail.empGroup}"/></span></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 7. </td>
                                <td width="20%">Designation during the period of report</td>
                                <td width="70%"><span><c:out value="${pardetail.apprisespn}"/></span></td>
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 8. </td>
                                <td width="20%">Office to where posted</td>
                                <td width="70%"><span><c:out value="${pardetail.empOffice}"/></span></td>                                         
                            </tr>
                            <tr style="height: 40px">                               
                                <td align="center" width="10%"> 9. </td>
                                <td width="20%">Head Quarter(if any)</td>
                                <td width="70%"><span><c:out value="${pardetail.sltHeadQuarter}"/></span></td>
                            </tr>
                        </table>
                    </div>


                    <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                        <div  class="divcls" style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 17px" align="left" data-toggle="collapse" data-target="#collapseOne">
                            Absentee Statement 
                            <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>
                            <span class="pull-right glyphicon glyphicon-plus"></span>
                        </div>                        
                        <div class="collapse" id="collapseOne">
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
                    </div>


                    <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                        <div class="divcls1" style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 17px" align="left" data-toggle="collapse" data-target="#collapsetwo">
                            Achievements
                            <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>
                            <span class="pull-right glyphicon glyphicon-plus"></span>
                        </div>                            
                        <div id="collapsetwo" class="collapse">
                            <table border="0" cellpadding="5" cellspacing="0" width="95%" class="tableview">             
                                <tr>
                                    <th align="left" width="5%"><b>SL No</b></th>
                                    <th align="left" width="16%"><b>Task</b></th>
                                    <th align="left" width="16%"><b>Target</b></th>
                                    <th align="left" width="16%"><b>Achievement</b></th>
                                    <th align="left" width="10%"><b>Achievement(%)</b></th>
                                    <th align="left" width="16%"><b>Attachment(if any)</b></th>
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
                    </div>


                    <%-- <div class="accordion" id="myAccordion"> --%>
                    <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">

                        <div class="divcls2" style="background-color:#5095ce;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 17px" align="left" data-toggle="collapse" data-target="#collapseThree">
                            Other Details
                            <span style="font-style: italic;background-color:#ce5050;color:#FFFFFF;">Please Click here For Details</span>
                            <span class="pull-right glyphicon glyphicon-plus"></span>
                        </div>
                        <div id="collapseThree" class="collapse">
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
                                    <td align="center" width="10%"> 2. </td>
                                    <td width="20%">3.(i)Significant work, if any, done</td>
                                    <td width="70%">
                                        <span>
                                            <c:out value="${pardetail.specialcontribution}" escapeXml="false"/>
                                        </span>
                                    </td> 
                                </tr>
                                <c:if test="${parMastForm.fiscalyear ne '2014-15' && parMastForm.fiscalyear ne '2015-16' && parMastForm.fiscalyear ne '2016-17' && parMastForm.fiscalyear ne '2017-18' && parMastForm.fiscalyear ne '2018-19'}">
                                    <tr style="height: 40px">                               
                                        <td align="center" width="10%">  </td>
                                        <td width="20%">(ii) Work Done For Implementation of 5TS(Transparency,Teamwork,Technology,Transformation and Time):</td>
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
                    </div>

                    <form action="par/forwardPAR.htm" method="POST" commandName="parDetail" enctype="multipart/form-data">
                        <input type="hidden" name="csrfPreventionSalt" id="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
                        <input type="hidden" name="parid" id="parid" value="${pardetail.parid}"/>
                        <%--<input type="hidden" name="parid" id="parid" value="${encodedparid}"/>--%>
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
                                <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 17px" align="left">Remarks of Reporting Authority</div>
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
                                                                <td style="font-weight:bold;text-decoration:underline;">
                                                                    <c:out value="${reportingdt.reportingauthName}"/>
                                                                </td>
                                                                <%} else {%>
                                                                <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
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
                                                                <td style="font-weight:bold;text-decoration:underline;">
                                                                    <c:out value="${reportingdt.reportingauthName}"/>
                                                                </td>
                                                                <%} else {%>
                                                                <td style="border-top:1px solid black;font-weight:bold;text-decoration:underline;">
                                                                    <c:out value="${reportingdt.reportingauthName}"/>
                                                                </td>
                                                                <%}%>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 20px;"><span>1. Assessment of work output, attributes & functional competencies.</span>(This should be on a relative scale of 1-5, with 1 referring to the lowest level & 5   to the highest level. Please indicate your rating for the officer against each item.)</td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <table width="60%" style="margin-left:35px;">
                                                                        <tr>
                                                                            <th width="15%">Description</th>
                                                                            <th width="15%">Rating</th>
                                                                            <th width="15%">Description</th>
                                                                            <th width="15%">Rating</th>
                                                                        </tr>

                                                                        <tr>
                                                                            <td>(a)  Attitude to work    :</td>
                                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.ratingattitude}"/></div></td>
                                                                            <td>(f) Co-ordination ability:</td>
                                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.ratingcoordination}"/></div></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(b)  Sense of responsibility:    </td>
                                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.ratingresponsibility}"/></div></td>
                                                                            <td>(g) Ability to work in a team:</td>
                                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.teamworkrating}"/></div></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(c)  Communication skill :  </td>
                                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.ratingcomskill}"/></div></td>
                                                                            <td>(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject :</td>
                                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.ratingitskill}"/></div></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(d)  Leadership Qualities :  </td>
                                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.ratingleadership}"/></div></td>
                                                                            <td>(i) Initiative :</td>
                                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.ratinginitiative}"/></div></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>(e)  Decision-making ability :  </td>
                                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.ratingdecisionmaking}"/></div></td>
                                                                            <td>(j) Quality of Work :</td>
                                                                            <td><div style="padding:5px;"><c:out value="${reportingdt.ratequalityofwork}"/></div></td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 10px;"><span>2. (i) General Assessment </span>(Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public):</td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 20px;">${reportingdt.authNote}</td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <c:if test="${pardetail.fiscalYear ne '2014-15' && pardetail.fiscalYear ne '2015-16' && pardetail.fiscalYear ne '2016-17' && pardetail.fiscalYear ne '2017-18' && pardetail.fiscalYear ne '2018-19'}">
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;"><span>(ii) Assessment Of Performance Of 5t  </span>(out of 20%): </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;"><span>(i) 10% on 5T charter of Department:  </span></td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 20px;">${reportingdt.fiveTChartertenpercent}</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;"><span>(ii) 5% on 5T charter of Government:  </span></td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 20px;">${reportingdt.fiveTCharterfivePercent}</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;"><span>(iii)  5% on Mo Sarkar:  </span></td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 20px;">${reportingdt.fiveTComponentmoSarkar}</td>
                                                                </tr>
                                                            </c:if>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 10px;"><span>3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse ):</span></td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 20px;"><div><c:out value="${reportingdt.inadequaciesNote}"/></div></td>
                                                            </tr> 
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 10px;"><span>4. Integrity (If integrity is doubtful or  adverse please write "Not certified" in the space below and justify your remarks in box 4 above):</span></td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 10px;"><div style="padding:5px;"><c:out value="${reportingdt.integrityNote}"/></div></td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 10px;"><span> 5. Overall Grading : </span><span style="padding:5px;"><c:out value="${reportingdt.sltGradingName}"/></span></td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 10px;"><span>6. For  Overall Grading  Below Average /  Outstanding  please provide justification in the   space below.:</span></td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 20px;"><div style="padding:5px;"><c:out value="${reportingdt.gradingNote}"/></div></td>
                                                            </tr> 
                                                            <tr>
                                                                <td style="padding-left: 10px;">7. For  Overall Grading  “Below Average” /  “Outstanding”  please upload Document if any.:</td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 20px;"><span style="color:red"><b> Document (if any):</b></span>
                                                                    <c:if test="${not empty reportingdt.originalFileNamegradingDocumentReporting}">
                                                                        <a href="DownloadGradingAttchmentReporting.htm?parId=${reportingdt.parId}&prtid=${reportingdt.prtid}" target="_blank"><span style="font-style: bold;color:red">Download Document</span><i class="glyphicon glyphicon-download-alt" style="color:red"></i></a>
                                                                        </c:if> 
                                                                        <c:if test="${empty reportingdt.originalFileNamegradingDocumentReporting}">
                                                                        <span style="color:red">Not Available</span>
                                                                    </c:if>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </c:if>
                                                    <c:if test="${reportingdt.isreportingcompleted != 'Y' && reportingdt.isreportingcompleted ne 'F'}">
                                                        <c:if test="${reportingdt.iscurrentreporting == 'Y'}">

                                                            <table>
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
                                                                <tr>
                                                                    <td style="padding-left: 10px;"><span>1. Assessment of work output, attributes & functional competencies.</span>(This should be on a relative scale of 1-5, with 1 referring to the lowest level & 5   to the highest level. Please indicate your rating for the officer against each item.) </td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;">
                                                                        <table width="60%" style="margin-left:35px;">
                                                                            <tr>
                                                                                <th width="15%">Description</th>
                                                                                <th width="15%">Rating</th>
                                                                                <th width="15%">Description</th>
                                                                                <th width="15%">Rating</th>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>(a)  Attitude to work    :</td>
                                                                                <td>
                                                                                    <input type="hidden" id="hidratingattitude" value="${reportingdt.ratingattitude}"/>                                                                                    
                                                                                    <select name="ratingattitude" id="ratingattitude" style="width: 100px;">
                                                                                        <option value="">Select</option>
                                                                                    </select>                                                                              
                                                                                </td>                                                                                
                                                                                <td>(f) Co-ordination ability:</td>
                                                                                <td>
                                                                                    <input type="hidden" id="hidratingcoordination" value="${reportingdt.ratingcoordination}"/>
                                                                                    <select name="ratingcoordination" id="ratingcoordination" style="width: 100px;">
                                                                                        <option value="">Select</option>
                                                                                    </select> 
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>&nbsp;</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>(b)  Sense of responsibility:    </td>
                                                                                <td>
                                                                                    <input type="hidden" id="hidratingresponsibility" value="${reportingdt.ratingresponsibility}"/>
                                                                                    <select name="ratingresponsibility" id="ratingresponsibility" style="width: 100px;">
                                                                                        <option value="">Select</option>
                                                                                    </select>
                                                                                </td>
                                                                                <td>(g) Ability to work in a team:</td>
                                                                                <td>
                                                                                    <input type="hidden" id="hidteamworkrating" value="${reportingdt.teamworkrating}"/>
                                                                                    <select name="teamworkrating" id="teamworkrating" style="width: 100px;">
                                                                                        <option value="">Select</option>
                                                                                    </select>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>&nbsp;</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>(c)  Communication skill :  </td>
                                                                                <td>
                                                                                    <input type="hidden" id="hidratingcomskill" value="${reportingdt.ratingcomskill}"/>
                                                                                    <select name="ratingcomskill" id="ratingcomskill" style="width: 100px;">
                                                                                        <option value="">Select</option>
                                                                                    </select>                                                                                </td>
                                                                                <td>(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject :</td>
                                                                                <td>
                                                                                    <input type="hidden" id="hidratingitskill" value="${reportingdt.ratingitskill}"/>
                                                                                    <select name="ratingitskill" id="ratingitskill" style="width: 100px;">
                                                                                        <option value="">Select</option>
                                                                                    </select> 
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>&nbsp;</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>(d)  Leadership Qualities :  </td>
                                                                                <td>
                                                                                    <input type="hidden" id="hidratingleadership" value="${reportingdt.ratingleadership}"/>
                                                                                    <select name="ratingleadership" id="ratingleadership" style="width: 100px;">
                                                                                        <option value="">Select</option>
                                                                                    </select>
                                                                                </td>
                                                                                <td>(i) Initiative :</td>
                                                                                <td>
                                                                                    <input type="hidden" id="hidratinginitiative" value="${reportingdt.ratinginitiative}"/>
                                                                                    <select name="ratinginitiative" id="ratinginitiative" style="width: 100px;">
                                                                                        <option value="">Select</option>
                                                                                    </select>
                                                                                </td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>&nbsp;</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <td>(e) Decision-making ability :  </td>
                                                                                <td>
                                                                                    <input type="hidden" id="hidratingdecisionmaking" value="${reportingdt.ratingdecisionmaking}"/>
                                                                                    <select name="ratingdecisionmaking" id="ratingdecisionmaking" style="width: 100px;">
                                                                                        <option value="">Select</option>
                                                                                    </select>
                                                                                </td>
                                                                                <td>(j) Quality of Work :</td>
                                                                                <td>
                                                                                    <input type="hidden" id="hidratequalityofwork" value="${reportingdt.ratequalityofwork}"/>
                                                                                    <select name="ratequalityofwork" id="ratequalityofwork" style="width: 100px;">
                                                                                        <option value="">Select</option>
                                                                                    </select>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;"><span>2.(i) General Assessment </span>(Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public):</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>

                                                                <tr>
                                                                    <td style="padding-left: 20px;"> Standard Template: 
                                                                        <select name="authnotetemplate" id="authnotetemplate" style="width: 300px;" onchange="copytemplate(this, 'authNote')">
                                                                            <option value="">Select</option>
                                                                            <c:forEach items="${authnote}" var="authnotetemplate">
                                                                                <option value="${authnotetemplate}">${authnotetemplate}</option>                                                                                
                                                                            </c:forEach>
                                                                            <option value="Custom">Custom</option>
                                                                        </select> 
                                                                    </td>

                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 20px;"><textarea name="authNote" class="textareacolor" rows="4" id="authNote" style="width:80%;height:60px;text-align:left;">${reportingdt.authNote}</textarea></td>
                                                                </tr>
                                                                <c:if test="${pardetail.fiscalYear ne '2014-15' && pardetail.fiscalYear ne '2015-16' && pardetail.fiscalYear ne '2016-17' && pardetail.fiscalYear ne '2017-18' && pardetail.fiscalYear ne '2018-19'}">
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span>(ii) Assessment Of Performance Of 5t  </span>(out of 20%): </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span>(i) 10% on 5T charter of Department:  </span>(out of 10%):</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 20px;"> Standard Template:
                                                                            <select name="fivetDepartmenttemplate" id="fivetDepartmenttemplate" style="width: 300px;" onchange="copytemplate(this, 'fiveTChartertenpercent')">
                                                                                <option value="">Select</option>
                                                                                <c:forEach items="${fiveTChartertenpercent}" var="fivetDepartmenttemplate">
                                                                                    <option value="${fivetDepartmenttemplate}">${fivetDepartmenttemplate}</option>
                                                                                </c:forEach>
                                                                                <option value="Custom">Custom</option>
                                                                            </select>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 20px;"><textarea name="fiveTChartertenpercent" class="textareacolor" rows="4" id="fiveTChartertenpercent" style="width:80%;height:60px;text-align:left;">${reportingdt.fiveTChartertenpercent}</textarea></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span>(ii) 5% on 5T charter of Government:  </span>(out of 5%):</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 20px;"> Standard Template:
                                                                            <select name="fivetGovernmenttemplate" id="fivetGovernmenttemplate" style="width: 300px;" onchange="copytemplate(this, 'fiveTCharterfivePercent')">
                                                                                <option value="">Select</option>
                                                                                <c:forEach items="${fiveTCharterfivePercent}" var="fivetGovernmenttemplate">
                                                                                    <option value="${fivetGovernmenttemplate}">${fivetGovernmenttemplate}</option>
                                                                                </c:forEach>
                                                                                <option value="Custom">Custom</option>
                                                                            </select>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 20px;"><textarea name="fiveTCharterfivePercent" class="textareacolor" rows="4" id="fiveTCharterfivePercent" style="width:80%;height:60px;text-align:left;">${reportingdt.fiveTCharterfivePercent}</textarea></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 10px;"><span>(iii)  5% on Mo Sarkar:  </span>(out of 5%):</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 20px;"> Standard Template:
                                                                            <select name="fivetMoSarkartemplate" id="fivetMoSarkartemplate" style="width: 300px;" onchange="copytemplate(this, 'fiveTComponentmoSarkar')">
                                                                                <option value="">Select</option>
                                                                                <c:forEach items="${fiveTComponentmoSarkar}" var="fivetMoSarkartemplate">
                                                                                    <option value="${fivetMoSarkartemplate}">${fivetMoSarkartemplate}</option>
                                                                                </c:forEach>
                                                                                <option value="Custom">Custom</option>
                                                                            </select>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 20px;"><textarea name="fiveTComponentmoSarkar" class="textareacolor" rows="4" id="fiveTComponentmoSarkar" style="width:80%;height:60px;text-align:left;">${reportingdt.fiveTComponentmoSarkar}</textarea></td>
                                                                    </tr>
                                                                </c:if>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr> 
                                                                    <td style="padding-left: 10px;">3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse ): </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>

                                                                <tr>
                                                                    <td style="padding-left: 20px;"> Standard Template:
                                                                        <select name="inadequaciesNotetemplate" id="inadequaciesNotetemplate" style="width: 300px;" onchange="copytemplate(this, 'inadequaciesNote')">
                                                                            <option value="">Select</option>
                                                                            <c:forEach items="${inadequaciesNote}" var="inadequaciesNotetemplate">
                                                                                <option value="${inadequaciesNotetemplate}">${inadequaciesNotetemplate}</option>
                                                                            </c:forEach>
                                                                            <option value="Custom">Custom</option>
                                                                        </select>

                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>

                                                                <tr>
                                                                    <td style="padding-left: 20px;"><textarea name="inadequaciesNote" class="textareacolor" rows="4" id="inadequaciesNote" style="width:80%;height:60px;text-align:left;">${reportingdt.inadequaciesNote}</textarea></td>
                                                                </tr> 
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;">4. Integrity (If integrity is doubtful or  adverse please write “Not certified” in the space below and justify your remarks in box 4 above):</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>

                                                                <tr>
                                                                    <td style="padding-left: 20px;"> Standard Template:
                                                                        <select name="integrityNotetemplate" id="integrityNotetemplate" style="width: 300px;" onchange="copytemplate(this, 'integrityNote')">
                                                                            <option value="">Select</option>
                                                                            <c:forEach items="${integrityNote}" var="integrityNotetemplate">
                                                                                <option value="${integrityNotetemplate}">${integrityNotetemplate}</option>
                                                                            </c:forEach>
                                                                            <option value="Custom">Custom</option>
                                                                        </select>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>

                                                                <tr>
                                                                    <td style="padding-left: 20px;"><textarea name="integrityNote"  class="textareacolor" rows="4" id="integrityNote" style="width:80%;height:60px;text-align:left;">${reportingdt.integrityNote}</textarea></td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;"> 5. Overall Grading :
                                                                        <input type="hidden" id="hidsltGrading" value="${reportingdt.sltGrading}"/>
                                                                        <input name="sltGrading" id="sltGrading" class="easyui-combobox" style="width:20%" data-options="valueField:'value',textField:'label',url:'GetPARGradeListJSON.htm'"/>
                                                                        <%--<select name="sltGrading">
                                                                            <option value="">Select</option>
                                                                            <c:forEach var="grd" items="${gradelist}">
                                                                                <option value="${grd.value}">${grd.label}</option>
                                                                            </c:forEach>
                                                                        </select>--%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;">6. For  Overall Grading  “Below Average” /  “Outstanding”  please provide justification in the   space below.:</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>

                                                                <tr>
                                                                    <td style="padding-left: 20px;"> Standard Template:
                                                                        <select name="gradingNotetemplate" id="gradingNotetemplate" style="width: 300px;" onchange="copytemplate(this, 'gradingNote')">
                                                                            <option value="">Select</option>                                                                            
                                                                            <c:forEach items="${gradingNote}" var="gradingNotetemplate">
                                                                                <option value="${gradingNotetemplate}">${gradingNotetemplate}</option>
                                                                            </c:forEach>
                                                                            <option value="Custom">Custom</option>
                                                                        </select>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 20px;"><textarea name="gradingNote" class="textareacolor" rows="4" id="gradingNote" style="width:80%;height:60px;text-align:left;">${reportingdt.gradingNote}</textarea></td>
                                                                </tr> 
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr class="documentDiv">
                                                                    <td style="padding-left: 10px;">7. For  Overall Grading  “Below Average” /  “Outstanding”  please upload Document if any.:</td>
                                                                </tr>
                                                                <tr class="documentDiv">
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr class="documentDiv">
                                                                    <td style="padding-left: 10px;"> 
                                                                        <table>
                                                                            <tr>
                                                                                <td>
                                                                                    <span style="color:red">
                                                                                        <b> Upload Document(if Any):</b>
                                                                                    </span>
                                                                                </td>
                                                                                <td>
                                                                                    <c:if test="${not empty reportingdt.originalFileNamegradingDocumentReporting}">
                                                                                        <a href="DownloadGradingAttchmentReporting.htm?parId=${reportingdt.parId}&prtid=${reportingdt.prtid}" target="_blank"><span style="font-style: bold;color:red">Download Document</span><i class="glyphicon glyphicon-download-alt" style="color:red"></i></a><br>
                                                                                        <a href="javascript:removeGradingAttachmentReporting(${reportingdt.prtid},${reportingdt.parId});"><span style="font-style: bold;color:red">Delete Document</span><i class="glyphicon glyphicon-trash" style="color:red;"></i></a> 
                                                                                        </c:if> 
                                                                                        <c:if test="${empty reportingdt.originalFileNamegradingDocumentReporting}">
                                                                                        <input type="file" name="gradingDocumentReporting"  id="gradingDocumentReporting" onchange="return uploadValidation(this)"/> <span style="font-style: bold;color:red">(Only pdf Files are allowed)(Not More than 2 mb)</span>
                                                                                    </c:if>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>

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
                                <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;;font-size: 17px" align="left">Remarks of Reviewing Authority</div>
                                <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">                                        
                                    <tr style="height: 40px">
                                        <td width="70%">
                                            <c:if test="${not empty pardetail.reviewingdata}">
                                                <%slno = 0;%>
                                                <c:forEach var="reviewingdt" items="${pardetail.reviewingdata}">
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
                                                                <td style="padding-left: 20px;">
                                                                    <div style="color: red;"><b>PAR is Force Forwarded</b></div>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </c:if>
                                                    <c:if test="${reviewingdt.isreviewingcompleted == 'T'}">
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
                                                                <td style="padding-left: 20px;">
                                                                    <div style="color: red;"><b>PAR is Force Forwarded Due to Office Of the Demmitted Authorities in the PAR Recording Change</b></div>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                    </c:if>
                                                    <c:if test="${reviewingdt.isreviewingcompleted == 'Y'}">
                                                        <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
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
                                                                <td style="padding-left: 10px;"><span>1. Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.</span></td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 20px;">
                                                                    <c:out value="${reviewingdt.reviewingNote}"/>
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

                                                            <tr>
                                                                <td style="padding-left: 10px;">3. For  Overall Grading  “Below Average” /  “Outstanding”  please upload Document if any.:</td>
                                                            </tr>
                                                            <tr>
                                                                <td>&nbsp;</td>
                                                            </tr>
                                                            <tr>
                                                                <td style="padding-left: 20px;"><span style="color:red"><b> Document(if Any):</b></span>
                                                                    <c:if test="${not empty reviewingdt.originalFileNamegradingDocumentReviewing}">
                                                                        <a href="DownloadGradingAttchmentReviewing.htm?parId=${reviewingdt.parId}&prtid=${reviewingdt.prtid}" target="_blank"><span style="font-style: bold;color:red">Download Document</span><i class="glyphicon glyphicon-download-alt" style="color:red"></i></a>
                                                                        </c:if> 
                                                                        <c:if test="${empty reviewingdt.originalFileNamegradingDocumentReviewing}">
                                                                        <span style="color:red">Not Available</span>
                                                                    </c:if>
                                                                </td>
                                                            </tr>

                                                        </table>
                                                    </c:if>                                                    
                                                    <c:if test="${reviewingdt.isreviewingcompleted != 'Y'}">
                                                        <c:if test="${pardetail.parstatus == '7'}">
                                                            <c:if test="${reviewingdt.iscurrentreviewing == 'Y'}">
                                                                <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
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
                                                                        <td style="padding-left: 10px;"><span>1. Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.</span></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>

                                                                    <tr>
                                                                        <td style="padding-left: 20px;"> Standard Template:
                                                                            <select name="reviewingNotetemplate" id="reviewingNotetemplate" style="width: 300px;" onchange="copytemplate(this, 'reviewingNote')">
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
                                                                        <td style="padding-left: 20px;"><textarea name="reviewingNote" class="textareacolor" rows="4" id="reviewingNote" style="width:80%;height:60px;text-align:left;">${reviewingdt.reviewingNote}</textarea></td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;"> <span>2. Overall Grading Given By Reviewing Authority  :</span>                                                                           
                                                                            <input type="hidden" id="hidsltReviewGrading" value="${reviewingdt.sltReviewGrading}"/>
                                                                            <input name="sltReviewGrading" id="sltReviewGrading" class="easyui-combobox" style="width:20%" data-options="valueField:'value',textField:'label',url:'GetPARGradeListJSON.htm'"/>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr class="documentDivReviewing">
                                                                        <td style="padding-left: 10px;">3. For  Overall Grading  “Below Average” /  “Outstanding”  please upload Document if any.:</td>
                                                                    </tr>
                                                                    <tr class="documentDivReviewing">
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr class="documentDivReviewing">
                                                                        <td style="padding-left: 10px;"> 
                                                                            <table>
                                                                                <tr>
                                                                                    <td>
                                                                                        <span style="color:red">
                                                                                            <b> Upload Document(if Any):</b>
                                                                                        </span>
                                                                                    </td>
                                                                                    <td>
                                                                                        <c:if test="${not empty reviewingdt.originalFileNamegradingDocumentReviewing}">
                                                                                            <a href="DownloadGradingAttchmentReviewing.htm?parId=${reviewingdt.parId}&prtid=${reviewingdt.prtid}" target="_blank"><span style="font-style: bold;color:red">Download Document</span><i class="glyphicon glyphicon-download-alt" style="color:red"></i></a><br>
                                                                                            <a href="javascript:removeGradingAttachmentReviewing(${reviewingdt.prtid},${reviewingdt.parId});"><span style="font-style: bold;color:red">Delete Document</span><i class="glyphicon glyphicon-trash" style="color:red;"></i></a> 
                                                                                            </c:if> 
                                                                                            <c:if test="${empty reviewingdt.originalFileNamegradingDocumentReviewing}">
                                                                                            <input type="file" name="gradingDocumentReviewing"  id="gradingDocumentReviewing" onchange="return uploadValidation(this)"/> 
                                                                                            <span style="font-style: bold;color:red">(Only pdf Files are allowed)(Not More than 2 mb)</span>
                                                                                        </c:if>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                </table>
                                                            </c:if>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${reviewingdt.isreviewingcompleted eq 'N' and reviewingdt.isreviewingcompleted != 'Y'}"> 
                                                        <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
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
                                                                <td><span>1. Please Indicate if you agree with the general assessment/ adverse remarks/ overall grading  made by the   Reporting Authority, and give your assessment.</span></td>
                                                            </tr>
                                                            <tr>
                                                                <td></td>
                                                            </tr>
                                                            <tr>
                                                                <td> <span>2. Overall Grading Given By Reviewing Authority:</span>

                                                                </td>
                                                            </tr>
                                                        </table>                                                
                                                    </c:if>
                                                    <%slno += 1;%>
                                                </c:forEach>
                                            </c:if>
                                        </td> 
                                    </tr>
                                </table>
                            </div>

                            <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5095ce;">
                                <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;font-size: 17px" align="left">Remarks of Accepting Authority</div>
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
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;">3. For  Overall Grading  “Below Average” /  “Outstanding”  please upload Document if any.:</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 20px;"> <span style="color:red"><b> Document (if any):</b></span>
                                                                        <c:if test="${not empty acceptingdt.originalFileNamegradingDocumentAccepting}">
                                                                            <a href="DownloadGradingAttchmentAccepting.htm?parId=${acceptingdt.parId}&pactid=${acceptingdt.pactid}" target="_blank"><span style="font-style: bold;color:red">Download Document</span><i class="glyphicon glyphicon-download-alt" style="color:red"></i></a>
                                                                            </c:if> 
                                                                            <c:if test="${empty acceptingdt.originalFileNamegradingDocumentAccepting}">
                                                                            <span style="color:red">Not Available</span>
                                                                        </c:if>
                                                                    </td>
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
                                                        <c:if test="${acceptingdt.isacceptingcompleted == 'T'}">
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
                                                                        <div style="color: red;"><b> PAR is Force Forwarded Due to Office Of the Demmitted Authorities in the PAR Recording Change</b></div>
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
                                                                        <td style="padding-left: 10px;">
                                                                            1. Accepting Note
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;"> Standard Template:
                                                                            <select name="acceptingNotetemplate" id="acceptingNotetemplate" style="width: 300px;" onchange="copytemplate(this, 'acceptingNote')">
                                                                                <option value="">Select</option>
                                                                                <c:forEach items="${acceptingNote}" var="acceptingNotetemplate">
                                                                                    <option value="${acceptingNotetemplate}">${acceptingNotetemplate}</option>
                                                                                </c:forEach>
                                                                                <option value="Custom">Custom</option>
                                                                            </select>

                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>&nbsp;</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td style="padding-left: 20px;">
                                                                            <textarea name="acceptingNote" class="textareacolor" rows="4" id="acceptingNote" style="width:80%;height:60px;text-align:left;">${acceptingdt.acceptingNote}</textarea>
                                                                        </td>
                                                                    </tr> 
                                                                </c:if>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr>
                                                                    <td style="padding-left: 10px;"> <span>2. Overall Grading Given By Accepting Authority  :</span>                                                                            
                                                                        <input type="hidden" id="hidsltAcceptingGrading" value="${acceptingdt.sltAcceptingGrading}"/>
                                                                        <input name="sltAcceptingGrading" id="sltAcceptingGrading" class="easyui-combobox" style="width:20%" data-options="valueField:'value',textField:'label',url:'GetPARGradeListJSON.htm'"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr class="documentDivAccepting">
                                                                    <td style="padding-left: 10px;">3. For  Overall Grading  “Below Average” /  “Outstanding”  please upload Document if any.:</td>
                                                                </tr>
                                                                <tr class="documentDivAccepting">
                                                                    <td>&nbsp;</td>
                                                                </tr>
                                                                <tr class="documentDivAccepting">
                                                                    <td style="padding-left: 10px;"> 
                                                                        <table>
                                                                            <tr>
                                                                                <td>
                                                                                    <span style="color:red">
                                                                                        <b> Upload Document(if Any):</b>
                                                                                    </span>
                                                                                </td>
                                                                                <td>
                                                                                    <c:if test="${not empty acceptingdt.originalFileNamegradingDocumentAccepting}">
                                                                                        <a href="DownloadGradingAttchmentAccepting.htm?parId=${acceptingdt.parId}&pactid=${acceptingdt.pactid}" target="_blank"><span style="font-style: bold;color:red">Download Document</span><i class="glyphicon glyphicon-download-alt" style="color:red"></i></a><br>
                                                                                        <a href="javascript:removeGradingAttachmentAccepting(${acceptingdt.pactid},${acceptingdt.parId});"><span style="font-style: bold;color:red">Delete Document</span><i class="glyphicon glyphicon-trash" style="color:red;"></i></a> 
                                                                                        </c:if> 
                                                                                        <c:if test="${empty acceptingdt.originalFileNamegradingDocumentAccepting}">
                                                                                        &nbsp;<input type="file" name="gradingDocumentAccepting"  id="gradingDocumentAccepting" onchange="return uploadValidation(this)"/> <span style="font-style: bold;color:red">(Only pdf Files are allowed)(Not More than 2 mb)</span>
                                                                                    </c:if>
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
                                    String encparId = "";
                                    int taskId = 0;
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
                                <a href='<%=pdflink%>' target="_blank"  class="btn btn-success">Download</a>
                                <c:if test="${pardetail.ishideremark == 'N'}">
                                    <c:if test="${pardetail.parstatus != '21' && pardetail.parstatus < 9}">
                                        <c:if test="${pardetail.isClosedFiscalYearAuthority != 'Y' && pardetail.isAuthorityAbleToGiveRemarks != 'No'}">

                                            <input type="submit" name="forwardpar" value="Save" class="btn btn-info" onclick="return savecheck()"/> 
                                            <input type="submit" name="forwardpar" value="Submit" class="btn btn-danger" onclick="return submitcheck()"/> 

                                            <%--<html:submit property="submit" style="margin-left:100px;color:red;" value="Revert" styleClass="btn1" />--%>
                                            <%
                                                revertlink = "revertPAR.htm?parid=" + parId;
                                            %>
                                            <%--<html:link action='<%=revertlink%>' target="_blank" styleClass="btn1">Revert</html:link>--%>
                                            <c:if test="${pardetail.parstatus == '6'}">
                                                <a href="javascript:revertClick();"  class="btn btn-warning">Revert</a>
                                            </c:if>
                                            <c:if test="${pardetail.parstatus == '7'}">
                                                <a href="javascript:revertClick();"  class="btn btn-warning">Revert</a>
                                            </c:if>
                                            <c:if test="${pardetail.parstatus == '8'}">
                                                <a href="javascript:revertClick();" class="btn btn-warning">Revert</a>
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
                            EMP ID - <c:out value="${pardetail.applicantempid}"/>,
                            PAR ID - <c:out value="${pardetail.parid}"/>
                            Submitted On - <c:out value="${pardetail.submitted_on}"/>
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

</body>
</html>
