<%@page contentType="text/html" pageEncoding="UTF-8" buffer="128kb"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <c:set var="r" value="${pageContext.request}" />
    <base href="${initParam['BaseURLPath']}" />  
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="resources/css/colorbox.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/jquery.min.js" type="text/javascript"></script> 
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/basicjavascript.js"></script>
        <script type="text/javascript" src="js/servicehistory.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {

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

                var fiscalYear = $('#fiscalYear').val();
                var tempfiscalYear = fiscalYear.split("-");
                var tempfiscalYear1 = tempfiscalYear[0];
                var tempfiscalYear2 = parseInt(tempfiscalYear[0]) + 1;

                var fromdate = "";
                var todate = "";

                if ($('#partialFromDate').val() != '' && $('#partialToDate').val() != '') {
                    fromdate = $('#partialFromDate').val();
                    todate = $('#partialToDate').val();
                    fromdate = formatDate(fromdate);
                    todate = formatDate(todate);
                } else {
                    fromdate = "01-APR-" + tempfiscalYear1;
                    todate = "31-MAR-" + tempfiscalYear2;
                }

                var fdate = fromdate.split("-");
                var tdate = todate.split("-");

                var tempFrommonth = "";
                var tempTomonth = "";
                if ($('#partialFromDate').val() != '' && $('#partialToDate').val() != '') {
                    tempFrommonth = fdate[1];
                    tempTomonth = tdate[1];
                } else {
                    tempFrommonth = monthint(fdate[1].toUpperCase());
                    tempTomonth = monthint(tdate[1].toUpperCase());
                }
                //alert("From Date is: "+new Date(fdate[2], tempFrommonth, fdate[0]));
                //alert("To Date is: "+new Date(tdate[2], tempTomonth, tdate[0]));
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    minDate: moment(new Date(fdate[2], tempFrommonth, fdate[0])),
                    maxDate: moment(new Date(tdate[2], tempTomonth, tdate[0]))
                });

                $(document).on('click', "#delete1", function() {
                    $(this).closest('tr').remove();
                });

                var url = "GetPARGradeListJSON.htm";
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltGrading').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });

                $("#DeptCode").change(function() {
                    $('#cadrename').empty();
                    var url = 'getCadreListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, obj) {
                            $('#cadrename').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                });

            });

            function formatDate(date) {
                var d = new Date(date),
                        month = '' + (d.getMonth()),
                        day = '' + d.getDate(),
                        year = d.getFullYear();

                if (month.length < 2)
                    month = '0' + month;
                if (day.length < 2)
                    day = '0' + day;

                return [day, month, year].join('-');
            }

            function getSelectedReportingPost(rowid) {
                var url = 'GetSanctionedAuthorityListInitiatePAR.htm?processCode=3&authType=reporting&rowid=' + rowid + '&fslYear=' + $('#fiscalYear').val() + '&parId=0';
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "80%"});
            }
            function getSelectedReviewingPost(rowid) {
                var url = 'GetSanctionedAuthorityListInitiatePAR.htm?processCode=3&authType=reviewing&rowid=' + rowid + '&fslYear=' + $('#fiscalYear').val() + '&parId=0';
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "80%"});
            }
            function getSelectedAcceptingPost(rowid) {
                var url = 'GetSanctionedAuthorityListInitiatePAR.htm?processCode=3&authType=accepting&rowid=' + rowid + '&fslYear=' + $('#fiscalYear').val() + '&parId=0';
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "80%"});
            }

            function SelectSpn(empId, empName, desig, spc, authType, idvalue)
            {
                //alert("authType: "+authType+" and empname is:" +empName+" and desig is: "+desig);
                $.colorbox.close();
                if (authType == 'reporting') {
                    $("#txtReportingAuth" + idvalue).val(empName + "," + desig);
                    $("#hidReportingEmpId" + idvalue).val(empId);
                    $("#hidReportingSpcCode" + idvalue).val(spc);
                } else if (authType == 'reviewing') {
                    $("#txtReviewingAuth" + idvalue).val(empName + "," + desig);
                    $("#hidReviewingEmpId" + idvalue).val(empId);
                    $("#hidReviewingpcCode" + idvalue).val(spc);
                } else if (authType == 'accepting') {
                    $("#txtAcceptingAuth" + idvalue).val(empName + "," + desig);
                    $("#hidAcceptingEmpId" + idvalue).val(empId);
                    $("#hidAcceptingSpcCode" + idvalue).val(spc);
                }
            }

            function addrowforall(authType) {
                var fiscalYear = $('#fiscalYear').val();
                var tempfiscalYear = fiscalYear.split("-");
                var tempfiscalYear1 = tempfiscalYear[0];
                var tempfiscalYear2 = parseInt(tempfiscalYear[0]) + 1;

                var fromdate = "01-APR-" + tempfiscalYear1;
                var todate = "31-MAR-" + tempfiscalYear2;

                var fdate = fromdate.split("-");
                var tdate = todate.split("-");

                if (authType == 'reporting') {
                    trid1 = $('#tab1 tr').length;
                    $('#tab1 > tbody:last').append('<tr>' +
                            '<td><input type="hidden" id="hidReportingEmpId' + trid1 + '" name="hidReportingEmpId"/><input type="hidden" id="hidReportingSpcCode' + trid1 + '" name="hidReportingSpcCode"/><input id="txtReportingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtReportingAuth" class="form-control" style="width:70%"/>' +
                            '<a href="javascript:getSelectedReportingPost(' + trid1 + ')" style="text-decoration: none;color:black;">&nbsp;<button type="button">Search</button></a>' +
                            '<button type="button" id="delete1" value="Remove">Delete</buton></td>' +
                            '<td><input type="text" name="txtReportingAuthFromDate" id="txtReportingAuthFromDate' + trid1 + '" size="13" class="form-control txtDate" readonly="true"/>&nbsp;<input type="text" name="txtReportingAuthToDate" id="txtReportingAuthToDate' + trid1 + '" size="13" class="form-control txtDate" readonly="true"/></td>' +
                            '</tr>');
                } else if (authType == 'reviewing') {
                    trid1 = $('#tab2 tr').length;
                    $('#tab2 > tbody:last').append('<tr><td><input type="hidden" id="hidReviewingEmpId' + trid1 + '" name="hidReviewingEmpId">' +
                            '<input type="hidden" id="hidReviewingpcCode' + trid1 + '" name="hidReviewingpcCode"><input id="txtReviewingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtReviewingAuth" class="form-control" style="width:70%"/>' +
                            '<a href="javascript:getSelectedReviewingPost(' + trid1 + ')" style="text-decoration: none;color:black;">&nbsp;<button type="button">Search</button></a>' +
                            '<button type="button" id="delete1" value="Remove">Delete</buton></td>' +
                            '<td><input type="text" name="txtRevieiwingAuthFromDate" id="txtRevieiwingAuthFromDate' + trid1 + '" size="13" class="form-control txtDate" readonly="true"/>&nbsp;' +
                            '<input type="text" name="txtRevieiwingAuthToDate" id="txtRevieiwingAuthToDate' + trid1 + '" size="13" class="form-control txtDate" readonly="true"/></td></tr>');
                } else if (authType == 'accepting') {
                    trid1 = $('#tab3 tr').length;
                    $('#tab3 > tbody:last').append('<tr><td><input type="hidden" id="hidAcceptingEmpId' + trid1 + '" name="hidAcceptingEmpId">' +
                            '<input type="hidden" id="hidAcceptingSpcCode' + trid1 + '" name="hidAcceptingSpcCode"><input id="txtAcceptingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtAcceptingAuth" class="form-control" style="width:70%"/>' +
                            '<a href="javascript:getSelectedAcceptingPost(' + trid1 + ')" style="text-decoration: none;color:black;">&nbsp;<button type="button">Search</button></a>' +
                            '<button type="button" id="delete1" value="Remove">Delete</buton></td>' +
                            '<td><input type="text" name="txtAcceptingAuthFromDate" id="txtAcceptingAuthFromDate' + trid1 + '" size="13" class="form-control txtDate" readonly="true"/>&nbsp;' +
                            '<input type="text" name="txtAcceptingAuthToDate" id="txtAcceptingAuthToDate' + trid1 + '" size="13" class="form-control txtDate" readonly="true"/></td></tr>');
                }
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true,
                    minDate: moment(new Date(fdate[2], monthint(fdate[1].toUpperCase()), fdate[0])),
                    maxDate: moment(new Date(tdate[2], monthint(tdate[1].toUpperCase()), tdate[0]))
                });
            }

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

            function savecheck() {
                var status = true;

                if ($('#fromDate').val() == '') {
                    alert("Please enter From Date");
                    status = false;
                    return false;
                }

                if ($('#toDate').val() == '') {
                    alert("Please enter To Date");
                    status = false;
                    return false;
                }

                if (($('#fromDate').val() != '') && ($('#toDate').val() != '')) {
                    var ftemp = $("#fromDate").val().split("-");
                    var ttemp = $("#toDate").val().split("-");
                    var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                    var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);
                    if (fdt > tdt) {
                        alert("From Date must be less than To Date");
                        return false;
                    }
                }

                if (!BlankNumericFieldValidation("ratingattitude", "Attitude to Work")) {
                    status = false;
                    return false;
                }
                if (!BlankNumericFieldValidation("ratingcoordination", "Co-ordination ability")) {
                    status = false;
                    return false;
                }
                if (!BlankNumericFieldValidation("ratingresponsibility", "Sense of responsibility")) {
                    status = false;
                    return false;
                }
                if (!BlankNumericFieldValidation("teamworkrating", "Ability to work in a team")) {
                    status = false;
                    return false;
                }
                if (!BlankNumericFieldValidation("ratingcomskill", "Communication skill")) {
                    status = false;
                    return false;
                }
                if (!BlankNumericFieldValidation("ratingitskill", "Knowledge of Rules")) {
                    status = false;
                    return false;
                }
                if (!BlankNumericFieldValidation("ratingleadership", "Leadership Qualities")) {
                    status = false;
                    return false;
                }
                if (!BlankNumericFieldValidation("ratinginitiative", "Initiative")) {
                    status = false;
                    return false;
                }
                if (!BlankNumericFieldValidation("ratingdecisionmaking", "Decision-making ability")) {
                    status = false;
                    return false;
                }
                if (!BlankNumericFieldValidation("ratequalityofwork", "Quality of Work")) {
                    status = false;
                    return false;
                }

                var grading = $('#sltGrading').val();
                if (grading == '') {
                    alert("Please select Overall Grading");
                    status = false;
                    return false;
                } else if (grading != '') {
                    var grde = grading;
                    var avg = "Below Average";
                    var outs = "Outstanding";
                    var decs = (grde == 1) ? avg : outs;
                    if ((grde == 1 || grde == 5) && $('#gradingNote').val() == '') {
                        alert("As you have selected Overall Grading as " + decs + " you must enter Justification");
                        $('#gradingNote').focus();
                        status = false;
                        return false;
                    }
                }

                if (status == true) {
                    $('#parAuthorityModal').modal("toggle");
                    //return true;
                }
            }
            function copytemplate(me, textarea) {
                comboname = $(me).prop("name");
                if (comboname == "authnotetemplate") {
                    //inadequaciesNotetemplate
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

            function submitAuth() {
                var reportinglength = $('input[name=hidReportingEmpId]').length;
                var reviewinglength = $('input[name=hidReviewingEmpId]').length;
                var acceptinglength = $('input[name=hidAcceptingEmpId]').length;
                var oneDay = 24 * 60 * 60 * 1000;

                for (var i = 0; i < reportinglength; i++) {
                    if ($("#hidReportingEmpId" + i).val() == '') {
                        alert('Reporting Authority can not be blank!');
                        $("#txtReportingAuth" + i).focus();
                        return false;
                        break;
                    }
                    if ($("#txtReportingAuthFromDate" + i).val() == '') {
                        alert('Reporting Authority From Date can not be blank!');
                        $("#txtReportingAuthFromDate" + i).focus();
                        return false;
                        break;
                    } else {

                    }
                    if ($("#txtReportingAuthToDate" + i).val() == '') {
                        alert('Reporting Authority To Date can not be blank!');
                        $("#txtReportingAuthToDate" + i).focus();
                        return false;
                        break;
                    }
                    var ftemp = $("#txtReportingAuthFromDate" + i).val().split("-");
                    var ttemp = $("#txtReportingAuthToDate" + i).val().split("-");
                    var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                    var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);
                    if (fdt > tdt) {
                        alert("From Date must be less than To Date");
                        return false;
                    }
                    var diffdays = Math.round(Math.abs((fdt.getTime() - tdt.getTime()) / (oneDay)));
                    if (diffdays < 120) {
                        alert("Reporting Authority Period must be at least 120 days.");
                        return false;
                    }
                }


                for (var i = 0; i < reviewinglength; i++) {
                    if ($("#hidReviewingEmpId" + i).val() == '') {
                        alert('Reviewing Authority can not be blank!');
                        $("#txtReviewingAuth" + i).focus();
                        return false;
                        break;
                    }
                    if ($("#txtRevieiwingAuthFromDate" + i).val() == '') {
                        alert('Reviewing Authority From Date can not be blank!');
                        $("#txtReviewingAuthFrmDt" + i).focus();
                        return false;
                        break;
                    }
                    if ($("#txtRevieiwingAuthToDate" + i).val() == '') {
                        alert('Reviewing Authority To Date can not be blank!');
                        $("#txtReviewingAuthToDt" + i).focus();
                        return false;
                        break;
                    }
                    var ftemp = $("#txtRevieiwingAuthFromDate" + i).val().split("-");
                    var ttemp = $("#txtRevieiwingAuthToDate" + i).val().split("-");
                    var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                    var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);
                    if (fdt > tdt) {
                        alert("From Date must be less than To Date");
                        return false;
                    }
                    var diffdays = Math.round(Math.abs((fdt.getTime() - tdt.getTime()) / (oneDay)));
                    if (diffdays < 120) {
                        //alert("Authority Period must be at least 120 days.");
                        //return false;
                    }
                }

                for (var i = 0; i < acceptinglength; i++) {
                    if ($("#hidAcceptingEmpId" + i).val() == '') {
                        alert('Accepting Authority can not be blank!');
                        $("#txtAcceptingAuth" + i).focus();
                        return false;
                        break;
                    }
                    if ($("#txtAcceptingAuthFromDate" + i).val() == '') {
                        alert('Accepting Authority From Date can not be blank!');
                        $("#txtAcceptingAuthFrmDt" + i).focus();
                        return false;
                        break;
                    }
                    if ($("#txtAcceptingAuthToDate" + i).val() == '') {
                        alert('Accepting Authority To Date can not be blank!');
                        $("#txtAcceptingAuthToDt" + i).focus();
                        return false;
                        break;
                    }
                    var ftemp = $("#txtAcceptingAuthFromDate" + i).val().split("-");
                    var ttemp = $("#txtAcceptingAuthToDate" + i).val().split("-");
                    var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                    var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);
                    if (fdt > tdt) {
                        alert("From Date must be less than To Date");
                        return false;
                    }
                    var diffdays = Math.round(Math.abs((fdt.getTime() - tdt.getTime()) / (oneDay)));
                    if (diffdays < 120) {
                        //alert("Authority Period must be at least 120 days.");
                        //return false;
                    }
                }

                if (confirm("Are you sure to Initiate the PAR?")) {
                    return true;
                } else {
                    return false;
                }


            }
            function getdepartmentWisecadreList() {
                var deptcode = $('#hidAuthDeptCode').val();
                $('#hidAuthOffCode').empty();
                var url = 'getCadreListJSON.htm?deptcode=' + deptcode;
                $('#cadreName').append('<option value="">--Select cadre--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#cadreName').append('<option value="' + obj.cadrecode + '">' + obj.cadreName + '</option>');
                    });
                });
            }


            function getDeptWiseOfficeList() {
                var deptcode = $('#hidAuthDeptCode').val();
                $('#hidAuthOffCode').empty();
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }
            function getOfficeWisePostList() {
                var offcode = $('#hidAuthOffCode').val();
                $('#authSpc').empty();
                var url = 'getOfficeWithSPCList.htm?offcode=' + offcode;
                //url = 'getEmployeeWithSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#authSpc').append('<option value="">--Select Post--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + "-" + obj.empid + '">' + obj.postname + '</option>');
                    });
                });

            }
            function getPost() {
                tspc = $('#authSpc').val().split("-");
                $('#hidspc').val(tspc[0]);

                $('#conclusionnotdept').val($('#hidAuthDeptCode').val());
                $('#conclusionnotoffice').val($('#hidAuthOffCode').val());
                $('#conclusionnotspc').val($('#authSpc').val());
                $('#designationDuringPeriod').val($('#authSpc option:selected').text());
            }
            function getCadreForInitiatePAR() {
                alert($('#cadrename option:selected').text());
                $('#cadreName').val($('#cadrename option:selected').text());

            }
        </script>
        <style type="text/css">
            .table-bordered > tbody > tr > td{
                border: 0px;
            }
            .table > tbody > tr > td, .table{
                border: 0px;
            }
            .form-control{
                display: inline;
            }
            @media (min-width: 800px) {
                .modal-dialog {
                    width: 600;
                    margin: 30px auto;
                }
                .modal-content {
                    -webkit-box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
                    box-shadow: 0 5px 15px rgba(0, 0, 0, .5);
                }
                .modal-sm {
                    width: 300px;
                }
            }
        </style>
    </head>
    <body>
        <form:form action="par/initiateOtherPARRemarks.htm" method="POST" commandName="initiateOtherPARForm" class="form-inline">
            <input type="hidden" name="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
            <form:hidden path="appraiseeEmpId"/>
            <form:hidden path="fiscalYear" id="fiscalYear"/>

            <%--<form:hidden path="hidReportingEmpId" id="hidReportingEmpId0" value="${loginid}"/>--%>
            <form:hidden path="hidReportingEmpId" id="hidReportingEmpId0"/>
            <form:hidden path="hidReviewingEmpId" id="hidReviewingEmpId0"/>
            <form:hidden path="hidAcceptingEmpId" id="hidAcceptingEmpId0"/>
            <%--<form:hidden path="hidReportingSpcCode" id="hidReportingSpcCode0" value="${loginspc}"/>--%>
            <form:hidden path="hidReportingSpcCode" id="hidReportingSpcCode0"/>
            <form:hidden path="hidReviewingpcCode" id="hidReviewingpcCode0"/>
            <form:hidden path="hidAcceptingSpcCode" id="hidAcceptingSpcCode0"/>

            <input type="hidden" id="partialFromDate" value="${partialFromDate}"/>
            <input type="hidden" id="partialToDate" value="${partialToDate}"/>

            <c:set var = "parid1" value = "${parId}"/>
            <h6 style="text-align:center;font-weight:bold;font-size:16px"><span style="color: red"> ${initiateOtherPARForm.errmsg}</span></h3>
                <c:if test="${not empty parid1 && parid1 == 'Y' && empty partialFromDate}">
                <h6 style="text-align:center;font-weight:bold;font-size:16px"><span style="color: red"> <c:out value="${appraiseename}"/> has already created PAR for <c:out value="${initiateOtherPARForm.fiscalYear}"/>.</span></h6>
            </c:if>
            <c:if test="${not empty parId && (parId != '0' || parId == 'N' || not empty partialFromDate)}">
                <c:if test="${parId != 'N'}">
                    <form:hidden path="parid"/>
                </c:if>
                <div class="row" style="text-align:center;margin-top: 20px;margin-bottom: 20px;">
                    <div class="col-lg-12" style="font-weight:bold;font-size:16px;">
                        INITIATE PAR FOR <c:out value="${appraiseename}"/>
                    </div>
                </div>
                <hr />    
                <div class="row" style="margin-bottom: 7px;">
                    <div class="col-lg-2" style="text-align:center;">
                        From Date
                    </div>
                    <div class="col-lg-2">
                        <form:input path="fromDate" id="fromDate" class="txtDate"/>
                    </div>
                    <div class="col-lg-2" style="text-align:center;">
                        To Date
                    </div>
                    <div class="col-lg-2">
                        <form:input path="toDate" id="toDate" class="txtDate"/>
                    </div>
                </div>
                <div class="row" style="margin-bottom: 7px;">
                    <div class="col-lg-2">
                        <label for="cadrename">Service to which the officer belongs</label>
                    </div>
                    <div class="col-lg-3">
                        <%--${emp.cadrename} --%>
                        <form:input class="form-control" path="cadreName" readonly="true"/>
                    </div>
                    <div class="col-lg-1">
                        <button type="button" class="btn btn-primary" data-toggle="modal"  data-target="#cadreModal">
                            <span class="glyphicon glyphicon-search"></span> Add Cadre
                        </button>
                    </div>

                    <%-- <form:input path="cadrecode" id="cadrecode" class="form-control"/>--%>
                </div>
                <div class="row" style="margin-bottom: 7px;">
                    <div class="col-lg-2">
                        <label for="fullname">Post Group Type</label>
                    </div>
                    <div class="col-lg-3">
                        <select name="postGroupType" id="postGroupType" class="form-control">
                            <option value="A">A</option>
                            <option value="B">B</option>
                        </select> 
                    </div>
                </div>
                <div class="row" style="margin-bottom: 7px;">
                    <div class="col-lg-2">
                        <label for="designation">Designation during the period of report</label>
                    </div>
                    <c:if test="${empty parAdminProperties.designationDuringPeriod}"> 
                        <div class="col-lg-3">
                            <form:hidden path="hidspc"/>
                            <form:input class="form-control" path="designationDuringPeriod" readonly="true"/>
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-primary" data-toggle="modal"  data-target="#designationDuringperiodModal">
                                <span class="glyphicon glyphicon-search"></span> Search
                            </button>
                        </div>
                    </c:if>
                </div>

                <div style="width:100%;overflow: auto;margin-top:5px;border:1px solid #5F9B24;">
                    <div style="background-color:#5F9B24;color:#FFFFFF;padding:5px;font-weight:bold;" align="left">Remarks of Reporting Authority</div>
                    <table border="0" cellpadding="5" cellspacing="0" width="100%" class="tableview">
                        <tr style="height: 40px">
                            <td width="70%">

                                <c:if test="${empty pardetail.reportingdata}">
                                    <table class="table table-bordered">
                                        <tr>
                                            <td><span>1. Assessment of work output, attributes & functional competencies.</span>(This should be on a relative scale of 1-5, with 1 referring to the lowest level & 5   to the highest level. Please indicate your rating for the officer against each item.) </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <table width="60%" style="margin-left:35px;">
                                                    <tr>
                                                        <th width="15%">Description</td>
                                                        <th width="15%">Rating</td>
                                                        <th width="15%">Description</td>
                                                        <th width="15%">Rating</td>
                                                    </tr>
                                                    <tr>
                                                        <td>(a)  Attitude to work    :</td>
                                                        <td>
                                                            <input type="hidden" id="hidratingattitude" value="${ratingattitude}"/>                                                                                    
                                                            <select name="ratingattitude" id="ratingattitude" style="width: 100px;">
                                                                <option value="">Select</option>
                                                            </select>                                                                              
                                                        </td>  
                                                        <td>(f) Co-ordination ability:</td>
                                                        <td>
                                                            <input type="hidden" id="hidratingcoordination" value="${ratingcoordination}"/>
                                                            <select name="ratingcoordination" id="ratingcoordination" style="width: 100px;">
                                                                <option value="">Select</option>
                                                            </select> 
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>(b)  Sense of responsibility:    </td>
                                                        <td>
                                                            <input type="hidden" id="hidratingresponsibility" value="${ratingresponsibility}"/>
                                                            <select name="ratingresponsibility" id="ratingresponsibility" style="width: 100px;">
                                                                <option value="">Select</option>
                                                            </select>
                                                        </td>
                                                        <td>(g) Ability to work in a team:</td>
                                                        <td>
                                                            <input type="hidden" id="hidteamworkrating" value="${teamworkrating}"/>
                                                            <select name="teamworkrating" id="teamworkrating" style="width: 100px;">
                                                                <option value="">Select</option>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>(c)  Communication skill :  </td>
                                                        <td>
                                                            <input type="hidden" id="hidratingcomskill" value="${ratingcomskill}"/>
                                                            <select name="ratingcomskill" id="ratingcomskill" style="width: 100px;">
                                                                <option value="">Select</option>
                                                            </select>                                                                                
                                                        </td>
                                                        <td>(h) Knowledge of Rules/Procedures/ IT  Skills/ Relevant Subject :</td>
                                                        <td>
                                                            <input type="hidden" id="hidratingitskill" value="${ratingitskill}"/>
                                                            <select name="ratingitskill" id="ratingitskill" style="width: 100px;">
                                                                <option value="">Select</option>
                                                            </select> 
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>(d)  Leadership Qualities :  </td>
                                                        <td>
                                                            <input type="hidden" id="hidratingleadership" value="${ratingleadership}"/>
                                                            <select name="ratingleadership" id="ratingleadership" style="width: 100px;">
                                                                <option value="">Select</option>
                                                            </select>
                                                        </td>
                                                        <td>(i) Initiative :</td>
                                                        <td>
                                                            <input type="hidden" id="hidratinginitiative" value="${ratinginitiative}"/>
                                                            <select name="ratinginitiative" id="ratinginitiative" style="width: 100px;">
                                                                <option value="">Select</option>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>(e) Decision-making ability :  </td>
                                                        <td>
                                                            <input type="hidden" id="hidratingdecisionmaking" value="${ratingdecisionmaking}"/>
                                                            <select name="ratingdecisionmaking" id="ratingdecisionmaking" style="width: 100px;">
                                                                <option value="">Select</option>
                                                            </select>
                                                        </td>
                                                        <td>(j) Quality of Work :</td>
                                                        <td>
                                                            <input type="hidden" id="hidratequalityofwork" value="${ratequalityofwork}"/>
                                                            <select name="ratequalityofwork" id="ratequalityofwork" style="width: 100px;">
                                                                <option value="">Select</option>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td><span>2. (a) General Assessment </span>(Please give an overall assessment of the officer including   his/her   attitude towards  S.T/S.C/Weaker Sections &  relation  with public):</td>
                                        </tr>
                                        <tr>
                                            <td style="padding-left: 20px;"> Standard Template:
                                                <%--<form:textarea path="authNote" class="form-control" rows="4" id="authNote" style="width:80%;height:60px;text-align:left;"/> --%>
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
                                            <td><textarea name="authNote" class="form-control" rows="4" id="authNote" style="width:80%;height:60px;text-align:left;"></textarea><td>
                                        </tr>
                                        <tr>
                                            <td><span>(b) Assessment Of Performance Of 5t (out of 20%): </span></td>
                                        </tr>
                                        <tr>
                                            <td><span>(i) 10% on 5T charter of Department: (out of 10%): </span></td>
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
                                            <td style="padding-left: 20px;"><textarea name="fiveTChartertenpercent" class="textareacolor" rows="4" id="fiveTChartertenpercent" style="width:80%;height:60px;text-align:left;"></textarea></td>
                                        </tr>
                                        <tr>
                                            <td><span>(ii) 5% on 5T charter of Government: </span></td>
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
                                            <td style="padding-left: 20px;"><textarea name="fiveTCharterfivePercent" class="textareacolor" rows="4" id="fiveTCharterfivePercent" style="width:80%;height:60px;text-align:left;"></textarea></td>
                                        </tr>
                                        <tr>
                                            <td><span>(iii)  5% on Mo Sarkar: </span></td>
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
                                            <td style="padding-left: 20px;"><textarea name="fiveTComponentmoSarkar" class="textareacolor" rows="4" id="fiveTComponentmoSarkar" style="width:80%;height:60px;text-align:left;"></textarea></td>
                                        </tr>
                                        <tr>
                                            <td>3. Inadequacies, deficiencies or shortcomings, if any (Remarks to be treated as adverse ):</td>
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
                                            <td style="padding-left: 20px;"><textarea name="inadequaciesNote" class="textareacolor" rows="4" id="inadequaciesNote" style="width:80%;height:60px;text-align:left;"></textarea></td>
                                        </tr>  
                                        <tr>
                                            <td>4. Integrity (If integrity is doubtful or  adverse please write “Not certified” in the space below and justify your remarks in box 4 above):</td>
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
                                            <td style="padding-left: 20px;"><textarea name="integrityNote"  class="textareacolor" rows="4" id="integrityNote" style="width:80%;height:60px;text-align:left;"></textarea></td>
                                        </tr> 
                                        <tr>
                                            <td> 5. Overall Grading :

                                                <form:select path="sltGrading" id="sltGrading" class="form-control">
                                                    <form:option value="">--Select--</form:option>
                                                </form:select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>6. For  Overall Grading  “Below Average” /  “Outstanding”  please provide justification in the space below.:</td>
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
                                    </table>
                                </c:if>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:if>
            <div align="center">
                <div class="controlpanelDiv">	
                    <table width="100%" cellpadding="0" cellspacing="0" >
                        <tr style="height:40px">
                            <td align="left" class="skinbutton sb_active" width="40%">
                                <span style="padding-left:10px;">
                                    <%--<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#parAuthorityModal">
                                        Submit
                                    </button>--%>
                                    <c:if test="${not empty parId && (parId != '0' || parId == 'N' || not empty partialFromDate)}">
                                        <button type="button" class="btn btn-primary" onclick="savecheck();">Submit PAR</button>
                                        <input type="submit" name="forwardpar" class="btn btn-primary" value="Back"/>
                                    </c:if>

                                </span>
                            </td>
                            <td width="60%" style="color:black;">

                            </td>
                        </tr>                        
                    </table>                                                
                </div>
            </div>

            <div id="parAuthorityModal" class="modal" role="dialog">
                <div class="modal-dialog" style="width:1200px;">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Select Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-6">
                                    <label for="txtReportingAuth">Reporting Authority</label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-11">
                                    <table id="tab1" width="100%" cellpadding="0" cellspacing="0">
                                        <tbody>
                                            <tr>
                                                <td width="70%">
                                                    <%--<form:input path="txtReportingAuth" id="txtReportingAuth0" class="form-control" style="width:70%" readonly="true" value="${loginname}"/>--%>
                                                    <form:input path="txtReportingAuth" id="txtReportingAuth0" class="form-control" style="width:70%" readonly="true"/>
                                                    <%--<a href="javascript:getSelectedReportingPost(0)" style="text-decoration: none;color:black;">
                                                        <button type="button">Search</button>&nbsp;
                                                    </a>--%>
                                                    <button type="button" onclick="return addrowforall('reporting')">Add</button>
                                                </td>
                                                <td width="30%">
                                                    <form:input path="txtReportingAuthFromDate" id="txtReportingAuthFromDate0" size="13" class="form-control txtDate" readonly="true"/>
                                                    <form:input path="txtReportingAuthToDate" id="txtReportingAuthToDate0" size="13" class="form-control txtDate" readonly="true"/>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-6">
                                    <label for="txtReviewingAuth">Reviewing Authority</label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                </div>
                                <div class="col-lg-11">
                                    <table id="tab2" width="100%" cellpadding="0" cellspacing="0">
                                        <tbody>
                                            <tr>
                                                <td width="70%">
                                                    <form:input path="txtReviewingAuth" id="txtReviewingAuth0" class="form-control" style="width:70%" readonly="true"/>
                                                    <a href="javascript:getSelectedReviewingPost(0)" style="text-decoration: none;color:black;">
                                                        <button type="button">Search</button>&nbsp;
                                                    </a>
                                                    <button type="button" onclick="return addrowforall('reviewing')">Add</button>
                                                </td>
                                                <td width="30%">
                                                    <form:input path="txtRevieiwingAuthFromDate" id="txtRevieiwingAuthFromDate0" size="13" class="form-control txtDate" readonly="true"/>
                                                    <form:input path="txtRevieiwingAuthToDate" id="txtRevieiwingAuthToDate0" size="13" class="form-control txtDate" readonly="true"/>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-6">
                                    <label for="txtAcceptingAuth">Accepting Authority</label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                </div>
                                <div class="col-lg-11">
                                    <table id="tab3" width="100%" cellpadding="0" cellspacing="0">
                                        <tbody>
                                            <tr>
                                                <td width="70%">
                                                    <form:input path="txtAcceptingAuth" id="txtAcceptingAuth0" class="form-control" style="width:70%" readonly="true"/>
                                                    <a href="javascript:getSelectedAcceptingPost(0)" style="text-decoration: none;color:black;">
                                                        <button type="button">Search</button>&nbsp;
                                                    </a>
                                                    <button type="button" onclick="return addrowforall('accepting')">Add</button>
                                                </td>
                                                <td width="30%">
                                                    <form:input path="txtAcceptingAuthFromDate" id="txtAcceptingAuthFromDate0" size="13" class="form-control txtDate" readonly="true"/>
                                                    <form:input path="txtAcceptingAuthToDate" id="txtAcceptingAuthToDate0" size="13" class="form-control txtDate" readonly="true"/>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">                            
                            <input type="submit" name="forwardpar" value="Submit" class="btn btn-primary" onclick="return submitAuth();"/>&nbsp;
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <div id="cadreModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Add Cadre</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="DeptCode" id="DeptCode" class="form-control">
                                        <option value="">--Select Department--</option>
                                        <c:forEach items="${deptlist}" var="dept">
                                            <option value="${dept.deptCode}">${dept.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Cadre</label>
                                </div>
                                <div class="col-lg-9">
                                    <select class="form-control" name="hidcadrename" id="cadrename" onchange="getCadreForInitiatePAR();">
                                        <option value="">Select</option>
                                    </select>                                        
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

            <div id="designationDuringperiodModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Designation During Period</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <option value="">--Select Department--</option>
                                        <c:forEach items="${deptlist}" var="dept">
                                            <option value="${dept.deptCode}">${dept.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                        <option value="">--Select Office--</option>
                                        <c:forEach items="${sancOfflist}" var="toffice">
                                            <option value="${toffice.offCode}">${toffice.offName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <select path="authSpc" id="authSpc" class="form-control" onchange="getPost();">
                                        <option value="">--Select Post--</option>
                                        <c:forEach items="${sancPostlist}" var="post">
                                            <option value="${post.spc}">${post.postname}</option>
                                        </c:forEach>                                         
                                    </select>
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
        </form:form>
</body>
</html>
