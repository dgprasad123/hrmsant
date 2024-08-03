<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    int slno = 0;
%>
<html>
    <c:set var="r" value="${pageContext.request}" />
    <base href="${initParam['BaseURLPath']}" />  
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Performance Appraisal</title>

        <link href="resources/css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/hrmis.css"/>
        <link href="resources/css/colorbox.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/color.css"/>

        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <script type="text/javascript" src="js/servicehistory.js"></script>
        <script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
        <script type="text/javascript" src="js/jquery.colorbox-min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script> 

        <script type="text/javascript">
            $(document).ready(function() {
                var fdate = $('#hidparfrmdt').val().split("-");
                var tdate = $('#hidpartodt').val().split("-");
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    minDate: new Date(fdate[2], monthint(fdate[1].toUpperCase()), fdate[0]),
                    maxDate: new Date(tdate[2], monthint(tdate[1].toUpperCase()), tdate[0]),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });

                $('#delete1').live('click', function() {
                    var cur_tr = $(this).parents('tr');
                    //alert("cur_tr is: "+cur_tr);
                    cur_tr.remove();
                });
            });
            function getSelectedReportingPost(rowid) {
                var url = 'GetSanctionedAuthorityList.htm?processCode=3&authType=reporting&rowid=' + rowid + '&fslYear=' + $('#hidfiscalyear').val() + '&parId=' + $('#hidparid').val();
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "80%"});
            }
            function getSelectedReviewingPost(rowid) {
                var url = 'GetSanctionedAuthorityList.htm?processCode=3&authType=reviewing&rowid=' + rowid + '&fslYear=' + $('#hidfiscalyear').val() + '&parId=' + $('#hidparid').val();
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "80%"});
            }
            function getSelectedAcceptingPost(rowid) {
                var url = 'GetSanctionedAuthorityList.htm?processCode=3&authType=accepting&rowid=' + rowid + '&fslYear=' + $('#hidfiscalyear').val() + '&parId=' + $('#hidparid').val();
                $.colorbox({href: url, iframe: true, open: true, width: "80%", height: "80%"});
            }

            function addReportingrow() {
                trid1 = $('#tab1 tr').length;
                $('#tab1 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidReportingEmpId' + trid1 + '" name="hidReportingEmpId"><input type="hidden" id="hidReportingSpcCode' + trid1 + '" name="hidReportingSpcCode"><input id="txtReportingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtReportingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedReportingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtReportingAuthFrmDt" id="txtReportingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtReportingAuthToDt" id="txtReportingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                //var fdate = $('#apprisalfromdate').val().split("-");
                //var tdate = $('#apprisaltodate').val().split("-");
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    //minDate: new Date(fdate[2], monthint(fdate[1].toUpperCase()), fdate[0]),
                    //maxDate: new Date(tdate[2], monthint(tdate[1].toUpperCase()), tdate[0]),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            }

            function addReviewingrow() {
                trid1 = $('#tab2 tr').length;
                $('#tab2 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidReviewingEmpId' + trid1 + '" name="hidReviewingEmpId"><input type="hidden" id="hidReviewingpcCode' + trid1 + '" name="hidReviewingpcCode"><input id="txtReviewingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtReviewingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedReviewingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtReviewingAuthFrmDt" id="txtReviewingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtReviewingAuthToDt" id="txtReviewingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                //var fdate = $('#apprisalfromdate').val().split("-");
                //var tdate = $('#apprisaltodate').val().split("-");
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    //minDate: new Date(fdate[2], monthint(fdate[1].toUpperCase()), fdate[0]),
                    //maxDate: new Date(tdate[2], monthint(tdate[1].toUpperCase()), tdate[0]),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            }

            function addReportingRowAfterRevert() {
                trid1 = $('#tab1 tr').length - 1;
                if (trid1 == 0) {
                    $('#tab1 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidReportingEmpId' + trid1 + '" name="hidReportingEmpId"><input type="hidden" id="hidReportingSpcCode' + trid1 + '" name="hidReportingSpcCode"><input id="txtReportingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtReportingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedReportingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtReportingAuthFrmDt" id="txtReportingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtReportingAuthToDt" id="txtReportingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                } else {
                    $('#tab1 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidReportingEmpId' + trid1 + '" name="hidReportingEmpId"><input type="hidden" id="hidReportingSpcCode' + trid1 + '" name="hidReportingSpcCode"><input id="txtReportingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtReportingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedReportingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtReportingAuthFrmDt" id="txtReportingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtReportingAuthToDt" id="txtReportingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                }
                //var fdate = $('#apprisalfromdate').val().split("-");
                //var tdate = $('#apprisaltodate').val().split("-");
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    //minDate: new Date(fdate[2], monthint(fdate[1].toUpperCase()), fdate[0]),
                    //maxDate: new Date(tdate[2], monthint(tdate[1].toUpperCase()), tdate[0]),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            }

            function addReviewingRowAfterRevert() {
                trid1 = $('#tab2 tr').length;
                if (trid1 == 0) {
                    $('#tab2 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidReviewingEmpId' + trid1 + '" name="hidReviewingEmpId"><input type="hidden" id="hidReviewingpcCode' + trid1 + '" name="hidReviewingpcCode"><input id="txtReviewingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtReviewingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedReviewingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtReviewingAuthFrmDt" id="txtReviewingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtReviewingAuthToDt" id="txtReviewingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                } else {
                    $('#tab2 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidReviewingEmpId' + trid1 + '" name="hidReviewingEmpId"><input type="hidden" id="hidReviewingpcCode' + trid1 + '" name="hidReviewingpcCode"><input id="txtReviewingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtReviewingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedReviewingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtReviewingAuthFrmDt" id="txtReviewingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtReviewingAuthToDt" id="txtReviewingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                }
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    //minDate: new Date(fdate[2], monthint(fdate[1].toUpperCase()), fdate[0]),
                    //maxDate: new Date(tdate[2], monthint(tdate[1].toUpperCase()), tdate[0]),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            }

            function addAcceptingRowAfterRevert() {
                trid1 = $('#tab3 tr').length;
                if (trid1 == 0) {
                    $('#tab3 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidAcceptingEmpId' + trid1 + '" name="hidAcceptingEmpId"><input type="hidden" id="hidAcceptingSpcCode' + trid1 + '" name="hidAcceptingSpcCode"><input id="txtAcceptingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtAcceptingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedAcceptingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtAcceptingAuthFrmDt" id="txtAcceptingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtAcceptingAuthToDt" id="txtAcceptingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                } else {
                    $('#tab3 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidAcceptingEmpId' + trid1 + '" name="hidAcceptingEmpId"><input type="hidden" id="hidAcceptingSpcCode' + trid1 + '" name="hidAcceptingSpcCode"><input id="txtAcceptingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtAcceptingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedAcceptingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtAcceptingAuthFrmDt" id="txtAcceptingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtAcceptingAuthToDt" id="txtAcceptingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                }
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    //minDate: new Date(fdate[2], monthint(fdate[1].toUpperCase()), fdate[0]),
                    //maxDate: new Date(tdate[2], monthint(tdate[1].toUpperCase()), tdate[0]),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            }

            function addrowforall(authType) {
                if (authType == 'reporting') {
                    trid1 = $('#tab1 tr').length;
                    $('#tab1 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidReportingEmpId' + trid1 + '" name="hidReportingEmpId"><input type="hidden" id="hidReportingSpcCode' + trid1 + '" name="hidReportingSpcCode"><input id="txtReportingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtReportingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedReportingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtReportingAuthFrmDt" id="txtReportingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtReportingAuthToDt" id="txtReportingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                } else if (authType == 'reviewing') {
                    trid1 = $('#tab2 tr').length;
                    $('#tab2 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidReviewingEmpId' + trid1 + '" name="hidReviewingEmpId"><input type="hidden" id="hidReviewingpcCode' + trid1 + '" name="hidReviewingpcCode"><input id="txtReviewingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtReviewingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedReviewingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtReviewingAuthFrmDt" id="txtReviewingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtReviewingAuthToDt" id="txtReviewingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                } else if (authType == 'accepting') {
                    trid1 = $('#tab3 tr').length;
                    $('#tab3 > tbody:last').append('<tr><td width="10%"> &nbsp;</td><td width="30%"><input type="hidden" id="hidAcceptingEmpId' + trid1 + '" name="hidAcceptingEmpId"><input type="hidden" id="hidAcceptingSpcCode' + trid1 + '" name="hidAcceptingSpcCode"><input id="txtAcceptingAuth' + trid1 + '" type="text" readonly="true" value="" name="txtAcceptingAuth" size="70"></td><td width="15%"> <a href="javascript:getSelectedAcceptingPost(' + trid1 + ')" style="text-decoration: none;">&nbsp;<button type="button">Search</button></a><button type="button" id="delete1" value="Remove">Delete</buton></td><td width="45%"> From Date:&nbsp;<input type="text" name="txtAcceptingAuthFrmDt" id="txtAcceptingAuthFrmDt' + trid1 + '" class="txtDate" readonly="true"/>&nbsp;To Date: <input type="text" name="txtAcceptingAuthToDt" id="txtAcceptingAuthToDt' + trid1 + '" class="txtDate" readonly="true"/></td></tr>');
                }
                var fdate = $('#hidparfrmdt').val().split("-");
                var tdate = $('#hidpartodt').val().split("-");
                //alert("From date is: "+fdate[2]+"-"+monthint(fdate[1])+"-"+fdate[0]);
                //alert("To date is: "+tdate[2]+"-"+monthint(tdate[1])+"-"+tdate[0]);
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    minDate: new Date(fdate[2], monthint(fdate[1].toUpperCase()), fdate[0]),
                    maxDate: new Date(tdate[2], monthint(tdate[1].toUpperCase()), tdate[0]),
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
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

            function submitAuth() {
                var reportinglength = $('input[name=hidReportingEmpId]').length;
                var reviewinglength = $('input[name=hidReviewingEmpId]').length;
                var acceptinglength = $('input[name=hidAcceptingEmpId]').length;
                var oneDay = 24 * 60 * 60 * 1000;
                if ($('#hidparstatus').val() != '18' && $('#hidparstatus').val() != '19') {
                    for (var i = 0; i < reportinglength; i++) {
                        if ($("#hidReportingEmpId" + i).val() == '') {
                            alert('Reporting Authority can not be blank!');
                            $("#txtReportingAuth" + i).focus();
                            return false;
                            break;
                        }
                        if ($("#hidfiscalyear").val() != '2023-24') {
                            if ($("#hidReportingEmpId" + i).val() == '00000000') {
                                alert('Please Choose NRC For the PAR Period If Reporting Authority Not Available!');
                                $("#txtReportingAuth" + i).focus();
                                return false;
                                break;
                            }
                        } else {
                            if ($("#hidReportingEmpId" + i).val() == '00000000') {
                                alert('Please Choose Any Of Authority');
                                $("#txtReportingAuth" + i).focus();
                                return false;
                                break;
                            }
                        }
                        if ($("#txtReportingAuthFrmDt" + i).val() == '') {
                            alert('Reporting Authority From Date can not be blank!');
                            $("#txtReportingAuthFrmDt" + i).focus();
                            return false;
                            break;
                        } else {

                        }
                        if ($("#txtReportingAuthToDt" + i).val() == '') {
                            alert('Reporting Authority To Date can not be blank!');
                            $("#txtReportingAuthToDt" + i).focus();
                            return false;
                            break;
                        }
                        var ftemp = $("#txtReportingAuthFrmDt" + i).val().split("-");
                        var ttemp = $("#txtReportingAuthToDt" + i).val().split("-");
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
                }
                if ($('#hidparstatus').val() != '19') {
                    for (var i = 0; i < reviewinglength; i++) {
                        if ($("#hidReviewingEmpId" + i).val() == '') {
                            alert('Reviewing Authority can not be blank!');
                            $("#txtReviewingAuth" + i).focus();
                            return false;
                            break;
                        }
                        if ($("#hidfiscalyear").val() == '2023-24') {
                            if ($("#hidReviewingEmpId" + i).val() == '00000000') {
                                alert('Please Choose Any Of Reviewing Authority!');
                                $("#txtReviewingAuth" + i).focus();
                                return false;
                                break;
                            }
                        } 
                        if ($("#txtReviewingAuthFrmDt" + i).val() == '') {
                            alert('Reviewing Authority From Date can not be blank!');
                            $("#txtReviewingAuthFrmDt" + i).focus();
                            return false;
                            break;
                        }
                        if ($("#txtReviewingAuthToDt" + i).val() == '') {
                            alert('Reviewing Authority To Date can not be blank!');
                            $("#txtReviewingAuthToDt" + i).focus();
                            return false;
                            break;
                        }
                        var ftemp = $("#txtReviewingAuthFrmDt" + i).val().split("-");
                        var ttemp = $("#txtReviewingAuthToDt" + i).val().split("-");
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
                }
                if (($('#hidparstatus').val() == '0') || ($('#hidparstatus').val() == '16') || ($('#hidparstatus').val() == '18') || ($('#hidparstatus').val() == '19')) {
                    for (var i = 0; i < acceptinglength; i++) {
                        if ($("#hidAcceptingEmpId" + i).val() == '') {
                            alert('Accepting Authority can not be blank!');
                            $("#txtAcceptingAuth" + i).focus();
                            return false;
                            break;
                        }
                        if ($("#hidfiscalyear").val() == '2023-24') {
                            if ($("#hidAcceptingEmpId" + i).val() == '00000000') {
                                alert('Please Choose Any Of Accepting Authority!');
                                $("#txtAcceptingAuth" + i).focus();
                                return false;
                                break;
                            }
                        } 
                        if ($("#txtAcceptingAuthFrmDt" + i).val() == '') {
                            alert('Accepting Authority From Date can not be blank!');
                            $("#txtAcceptingAuthFrmDt" + i).focus();
                            return false;
                            break;
                        }
                        if ($("#txtAcceptingAuthToDt" + i).val() == '') {
                            alert('Accepting Authority To Date can not be blank!');
                            $("#txtAcceptingAuthToDt" + i).focus();
                            return false;
                            break;
                        }
                        var ftemp = $("#txtAcceptingAuthFrmDt" + i).val().split("-");
                        var ttemp = $("#txtAcceptingAuthToDt" + i).val().split("-");
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
                }
                if ($('#hidAchievementData').val() == 'N') {
                    alert("Achievement Data is blank!");
                }
                if ($('#hidOtherDetailsData').val() == 'N') {
                    alert("Other Details Data is blank!");
                }
                var x = confirm("Are you sure to Send the Par?");
                /*if (x)
                 return true;
                 else
                 return false;*/
                if (x) {
                    $('#btnSubmit').css("display", "none");
                    $('#msgPAR').css("display", "block");
                    return true;
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form action="par/sendPAR.htm" method="POST" commandName="ParSubmit">
            <input type="hidden" name="csrfPreventionSalt" value="<c:out value='${csrfPreventionSalt}'/>"/>
            <input type="hidden" name="hidparid" id="hidparid" value='${parMastForm.parid}'/>
            <input type="hidden" name="hidparstatus" id="hidparstatus" value='${parMastForm.parstatus}'/>
            <input type="hidden" name="hidtaskid" id="hidtaskid" value='${parMastForm.taskid}'/>
            <input type="hidden" name="hidfiscalyear" id="hidfiscalyear" value='${parMastForm.fiscalyear}'/>
            <input type="hidden" name="hidparfrmdt" id="hidparfrmdt" value='${parfrmdt}'/>
            <input type="hidden" name="hidpartodt" id="hidpartodt" value='${partodt}'/>
            <input type="hidden" name="maxhierarchyno" id="maxhierarchyno" value='${hierarchyno}'/>
            <input type="hidden" name="hidspc" id="hidspc" value='${parMastForm.spc}'/>

            <c:if test="${empty ParSubmitForm.reportList}">
                <input type="hidden" name="hidReportingEmpId" id="hidReportingEmpId0"/>
                <input type="hidden" name="hidReportingSpcCode" id="hidReportingSpcCode0"/>
            </c:if>
            <c:if test="${empty ParSubmitForm.reviewList}">
                <input type="hidden" name="hidReviewingEmpId" id="hidReviewingEmpId0"/>
                <input type="hidden" name="hidReviewingpcCode" id="hidReviewingpcCode0"/>
            </c:if>
            <c:if test="${empty ParSubmitForm.acceptList}">
                <input type="hidden" name="hidAcceptingEmpId" id="hidAcceptingEmpId0"/>
                <input type="hidden" name="hidAcceptingSpcCode" id="hidAcceptingSpcCode0"/>
            </c:if>

            <input type="hidden" id="hidAchievementData" value="${isAchievementPresent}"/>
            <input type="hidden" id="hidOtherDetailsData" value="${isOtherDetailsPresent}"/>

            <div align="center" class="easyui-panel" title="Authority Window" style="height: 500px;">
                <h3 style="text-align: center;color:green;"> 
                    <b>PAR For the Financial Year - ${parMastForm.fiscalyear}</b>
                </h3>
                <div width="100%" align="center">
                    <table width="100%" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="5%" align="center">
                                1.
                            </td>
                            <td width="95%">
                                Reporting Authority
                            </td>
                        </tr>
                    </table>
                    <br />
                    <c:if test="${parMastForm.parstatus > 0}">
                        <c:if test="${not empty ParSubmitForm.reportList}">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <c:forEach var="rptauth" items="${ParSubmitForm.reportList}">
                                    <%slno = slno + 1;%>
                                    <tr>
                                        <td width="10%">&nbsp;</td>
                                        <td width="90%">
                                            <%=slno%>.&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)<br />
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                        </td>
                                    </tr>

                                </c:forEach>
                            </table>
                            <c:if test="${parMastForm.parstatus == 16 || parMastForm.parstatus == 18}">
                                <c:if test="${empty ParSubmitForm.reviewList}">
                                    <table id="tab1" width="100%" cellpadding="0" cellspacing="0">
                                        <tbody>
                                            <tr>
                                                <td width="10%">&nbsp;</td>
                                                <td width="90%">
                                                    <button type="button" onclick="return addReportingRowAfterRevert()">Add</button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </c:if>
                            </c:if>
                        </c:if>
                        <c:if test="${empty ParSubmitForm.reportList}">
                            <table id="tab1" width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="10%"></td>
                                    <td width="30%">
                                        <input type="text" name="txtReportingAuth" id="txtReportingAuth0" readonly="true" size="70"/>
                                    </td>
                                    <td width="15%">&nbsp;
                                        <a href="javascript:getSelectedReportingPost(0)" style="text-decoration: none;">
                                            <button type="button">Search</button>&nbsp;
                                        </a>
                                        <button type="button" onclick="return addrowforall('reporting')">Add</button>
                                    </td>
                                    <td width="45%">
                                        From Date:&nbsp;<input type="text" name="txtReportingAuthFrmDt" id="txtReportingAuthFrmDt0" class="txtDate" readonly="true"/>&nbsp;
                                        To Date:&nbsp;<input type="text" name="txtReportingAuthToDt" id="txtReportingAuthToDt0" class="txtDate" readonly="true"/>
                                    </td>
                                </tr>
                            </table>
                        </c:if>
                    </c:if>
                    <c:if test="${parMastForm.parstatus == 0}">
                        <table id="tab1" width="100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="10%"></td>
                                <td width="30%">
                                    <input type="text" name="txtReportingAuth" id="txtReportingAuth0" readonly="true" size="70"/>
                                </td>
                                <td width="15%">&nbsp;
                                    <a href="javascript:getSelectedReportingPost(0)" style="text-decoration: none;">
                                        <button type="button">Search</button>&nbsp;
                                    </a>
                                    <button type="button" onclick="return addrowforall('reporting')">Add</button>
                                </td>
                                <td width="45%">
                                    From Date:&nbsp;<input type="text" name="txtReportingAuthFrmDt" id="txtReportingAuthFrmDt0" class="txtDate" readonly="true"/>&nbsp;
                                    To Date:&nbsp;<input type="text" name="txtReportingAuthToDt" id="txtReportingAuthToDt0" class="txtDate" readonly="true"/>
                                </td>
                            </tr>
                        </table>
                    </c:if>
                    <br />
                    <%slno = 0;%>
                    <table width="100%" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="5%" align="center">
                                2.
                            </td>
                            <td width="95%">
                                Reviewing Authority
                            </td>
                        </tr>
                    </table>
                    <br />
                    <c:if test="${parMastForm.parstatus >= 16}">
                        <c:if test="${not empty ParSubmitForm.reviewList}">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <c:forEach var="rptauth" items="${ParSubmitForm.reviewList}">
                                    <%slno = slno + 1;%>
                                    <tr>
                                        <td width="10%">&nbsp;</td>
                                        <td width="90%">
                                            <%=slno%>.&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)<br />
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <c:if test="${parMastForm.parstatus == 18 || parMastForm.parstatus == 19}">
                                <c:if test="${empty ParSubmitForm.acceptList}">
                                    <table id="tab2" width="100%" cellpadding="0" cellspacing="0">
                                        <tbody>
                                        <button type="button" onclick="return addReviewingRowAfterRevert()">Add</button>
                                        </tbody>
                                    </table>
                                </c:if>
                            </c:if>
                        </c:if>
                        <c:if test="${empty ParSubmitForm.reviewList}">
                            <table id="tab2" width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="10%"></td>
                                    <td width="30%">
                                        <input type="text" name="txtReviewingAuth" id="txtReviewingAuth0" readonly="true" size="70"/>
                                    </td>
                                    <td width="15%">&nbsp;
                                        <a href="javascript:getSelectedReviewingPost(0)" style="text-decoration: none;">
                                            <button type="button">Search</button>&nbsp;
                                        </a>
                                        <button type="button" onclick="return addrowforall('reviewing')">Add</button>
                                    </td>
                                    <td width="45%">
                                        From Date:&nbsp;<input type="text" name="txtReviewingAuthFrmDt" id="txtReviewingAuthFrmDt0" class="txtDate" readonly="true"/>&nbsp;
                                        To Date:&nbsp;<input type="text" name="txtReviewingAuthToDt" id="txtReviewingAuthToDt0" class="txtDate" readonly="true"/>
                                    </td>
                                </tr>
                            </table>
                        </c:if>
                    </c:if>
                    <c:if test="${parMastForm.parstatus == 0}">
                        <table id="tab2" width="100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="10%"></td>
                                <td width="30%">
                                    <input type="text" name="txtReviewingAuth" id="txtReviewingAuth0" readonly="true" size="70"/>
                                </td>
                                <td width="15%">&nbsp;
                                    <a href="javascript:getSelectedReviewingPost(0)" style="text-decoration: none;">
                                        <button type="button">Search</button>&nbsp;
                                    </a>
                                    <button type="button" onclick="return addrowforall('reviewing')">Add</button>
                                </td>
                                <td width="45%">
                                    From Date:&nbsp;<input type="text" name="txtReviewingAuthFrmDt" id="txtReviewingAuthFrmDt0" class="txtDate" readonly="true"/>&nbsp;
                                    To Date:&nbsp;<input type="text" name="txtReviewingAuthToDt" id="txtReviewingAuthToDt0" class="txtDate" readonly="true"/>
                                </td>
                            </tr>
                        </table>
                    </c:if>
                    <br />
                    <%slno = 0;%>
                    <table width="100%" cellpadding="0" cellspacing="0">
                        <tr>
                            <td width="5%" align="center">
                                3.
                            </td>
                            <td width="95%">
                                Accepting Authority
                            </td>
                        </tr>
                    </table>
                    <br />
                    <c:if test="${parMastForm.parstatus >= 16}">
                        <c:if test="${not empty ParSubmitForm.acceptList}">
                            <table width="100%" cellpadding="0" cellspacing="0">
                                <c:forEach var="rptauth" items="${ParSubmitForm.acceptList}">
                                    <%slno = slno + 1;%>
                                    <tr>
                                        <td width="10%">&nbsp;</td>
                                        <td width="90%">
                                            <%=slno%>.&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${rptauth.authorityname}"/> (<c:out value="${rptauth.authorityspn}"/>)<br />
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(<span style="font-size:10.5px;">From:<c:out value="${rptauth.fromdt}"/> To:<c:out value="${rptauth.todt}"/></span>)
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <c:if test="${parMastForm.parstatus >= 16}">
                                <table id="tab3" width="100%" cellpadding="0" cellspacing="0">
                                    <tbody>
                                    <button type="button" onclick="return addAcceptingRowAfterRevert()">Add</button>
                                    </tbody>
                                </table>
                            </c:if>
                        </c:if>
                        <c:if test="${empty ParSubmitForm.acceptList}">
                            <table id="tab3" width="100%" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="10%"></td>
                                    <td width="30%">
                                        <input type="text" name="txtAcceptingAuth" id="txtAcceptingAuth0" readonly="true" size="70"/>
                                    </td>
                                    <td width="15%">&nbsp;
                                        <a href="javascript:getSelectedAcceptingPost(0)" style="text-decoration: none;">
                                            <button type="button">Search</button>&nbsp;
                                        </a>
                                        <button type="button" onclick="return addrowforall('accepting')">Add</button>
                                    </td>
                                    <td width="45%">
                                        From Date:&nbsp;<input type="text" name="txtAcceptingAuthFrmDt" id="txtAcceptingAuthFrmDt0" class="txtDate" readonly="true"/>&nbsp;
                                        To Date:&nbsp;<input type="text" name="txtAcceptingAuthToDt" id="txtAcceptingAuthToDt0" class="txtDate" readonly="true"/>
                                    </td>
                                </tr>
                            </table>
                        </c:if>
                    </c:if>
                    <c:if test="${parMastForm.parstatus == 0}">
                        <table id="tab3" width="100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="10%"></td>
                                <td width="30%">
                                    <input type="text" name="txtAcceptingAuth" id="txtAcceptingAuth0" readonly="true" size="70"/>
                                </td>
                                <td width="15%">&nbsp;
                                    <a href="javascript:getSelectedAcceptingPost(0)" style="text-decoration: none;">
                                        <button type="button">Search</button>&nbsp;
                                    </a>
                                    <button type="button" onclick="return addrowforall('accepting')">Add</button>
                                </td>
                                <td width="45%">
                                    From Date:&nbsp;<input type="text" name="txtAcceptingAuthFrmDt" id="txtAcceptingAuthFrmDt0" class="txtDate" readonly="true"/>&nbsp;
                                    To Date:&nbsp;<input type="text" name="txtAcceptingAuthToDt" id="txtAcceptingAuthToDt0" class="txtDate" readonly="true"/>
                                </td>
                            </tr>
                        </table>
                    </c:if>
                </div>
            </div>
            <div style="margin-top:50px;">
                <div style="float:left;margin-left: 20px;">
                    <button type="submit" id="btnSubmit" class="easyui-linkbutton c1" style="width: 90px;height: 30px;" onclick="return submitAuth()">Send</button>
                    <div id="msgPAR" style="display:none;font-size: 16px;">PAR is Submitting</div>
                </div>
                <div align="center" style="float:left;">
                    <c:if test="${invalidperiod != 'N'}">
                        <c:if test="${invalidperiod == 'REP'}">
                            <span style="color:red;padding-left:30px;">
                                Reporting Authority From Date and To Date must be more than 120 Days.
                            </span>
                        </c:if>
                    </c:if>
                    <c:if test="${not empty isClosed}">
                        <c:if test="${isClosed == 'Y'}">
                            <span style="color:red;padding-left:30px;">
                                PAR Submission is Closed.
                            </span>
                        </c:if>
                    </c:if>
                    <c:if test="${not empty isParCreatedDateClosed}">
                            <span style="color:red;padding-left:30px;">
                                PAR Submission is Closed.
                            </span>
                    </c:if>
                </div>
            </div>
        </form>
    </body>
</html>
