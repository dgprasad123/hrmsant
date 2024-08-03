<%-- 
    Document   : SelectedDelinquentOfficerListForongoingDPRule15
    Created on : 28 Jan, 2021, 5:33:29 PM
    Author     : Manisha
--%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8" autoFlush="true" buffer="64kb"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript"  src="js/basicjavascript.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>

        <script type="text/javascript">
            var vpdtype;
            $(document).ready(function() {
                hideAllTab();
                $("#alreadyDAApproval").hide();
                $("#sendForApproval").hide();
                $("#noticetoDO").hide();
                $("#alreadyservedDiv").hide();
                $("#sendshowcauseDiv").hide();
                $("#appointmentofIoandPO").hide();
                $("#appointmentofIoPODetail").hide();
                $("#ioandPoremarksDiv").hide();
                $("#ioandPoremarksDiv").hide();
                $("#ioandPoandAporemarksDiv").hide();
                $("#serveToDelinquentOnIoRemarks").hide();
                $("#sendserveDelinquentOnIoRemarks").hide();
                $("#serveDelinquentOnIoRemarksDiv").hide();
                $("#sendserveDelinquentOnIoRemarks").hide();
                $("#PunishmentDetailONpresentationOfDOonIoReport").hide();
                $("#ExanaurationDetailONpresentationOfDOonIoReport").hide();
                $("#MinorPenaltyONpresentationOfDOonIoReport").hide();
                $("#ExanaurationONpresentationOfDOonIoReport").hide();
                $("#reinquiryonDelinquentOfficerDiv").hide();
                $("#sendIoPoApo").hide();
                $("#MinorPenaltyONpresentationOfDO").hide();
                $("#exanaurationOnRepresentationOfDO").hide();
                $("#thirdNoticeToDelinquentForPunishmentProposed").hide();
                $("#serveDelinquentthirdNoticeDetail").hide();
                $("#sendDelinquentthirdNotice").hide();
                $("#exanaurationDetailONpresentationOfDOonIoReport").hide();
                $("#PunishmentDetailONpresentationOfDOonafterAssignpunishment").hide();
                $("#ExanaurationDetailONpresentationOfAfterAssignPunishment").hide();
                $("#PunishmentAssignmentBygovt").hide();

                $('.datepickertxt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('.datepickerclass').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#memoDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#showCauseOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                if ($("#isAuthorityApprove").val() == 'Y') {
                    $("#alreadyDAApproval").show();
                    $("#sendForApproval").hide();
                }
                if ($("#hasSendNoticetoDO").val() == 'Y') {
                    $("#alreadyservedDiv").show();
                }
                if ($("#hasIoAppointed").val() == 'Y') {
                    $("#appointmentofIoPODetail").show();
                }
                if ($("#hasIoRemarks").val() == 'Y') {
                    $("#ioandPoandAporemarksDiv").show();
                }
                if ($("#hasSendSecondNotice").val() == 'Y') {
                    $("#serveDelinquentOnIoRemarksDiv").show();
                    $("#serveToDelinquentOnIoRemarks").show();
                }
                if ($("#hasSendthirdshowCause").val() == 'Y') {
                    $("#PunishmentDetailONpresentationOfDOonIoReport").show();
                    $("#thirdNoticeToDelinquentForPunishmentProposed").show();
                    $("#serveDelinquentthirdNoticeDetail").show();

                }
                $("#rednservdepartment").change(function() {
                    $('#rednservoffice').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#rednservoffice').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                    url = 'getDeptWisePostListJSON.htm?deptCode=' + this.value;
                    $('#lwrpost').empty();
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#lwrpost').append($('<option>', {
                                value: field.postcode,
                                text: field.post
                            }));
                        });
                    });

                });
                $("#rednservdepartment").change(function() {
                    $('#redncadre').empty();
                    var url = 'getCadreListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, obj) {
                            $('#redncadre').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                });
                $("#redncadre").change(function() {
                    $('#redngrade').empty();
                    var url = 'getGradeListCadreWiseJSON.htm?cadreCode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, obj) {
                            $('#redngrade').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                });


            });
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
                url = 'getEmployeeWithSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#authSpc').append('<option value="">--Select Post--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + "-" + obj.empid + '">' + obj.postname + '</option>');
                    });
                });

            }
            function getPost() {
                if (tauthtype == "A") {
                    tspc = ($('#authSpc').val()).split("-");
                    $('#dahrmsid').val(tspc[1]);
                    $('#daspc').val(tspc[0]);

                    $('#dadept').val($('#hidAuthDeptCode option:selected').val());
                    $('#daoffice').val($('#hidAuthOffCode option:selected').val());
                    $('#disciplinaryauthority').val($('#authSpc option:selected').text());
                } else if (tauthtype == "I") {
                    t1spc = ($('#authSpc').val()).split("-");
                    $('#ioEmpHrmsId').val(t1spc[1]);
                    $('#ioEmpSPC').val(t1spc[0]);
                    $('#inquiryauthority').val($('#authSpc option:selected').text());
                } else if (tauthtype == "P") {
                    t2spc = ($('#authSpc').val()).split("-");
                    $('#poHrmsId').val(t2spc[1]);
                    $('#poSPC').val(t2spc[0]);
                    $('#presentingauthority').val($('#authSpc option:selected').text());
                } else if (tauthtype == "AP") {
                    t3spc = ($('#authSpc').val()).split("-");
                    $('#apoHrmsId').val(t3spc[1]);
                    $('#apoSPC').val(t3spc[0]);
                    $('#additionalpresentingauthority').val($('#authSpc option:selected').text());
                } else if (tauthtype == "U") {
                    $('#conclusionnotdept').val($('#hidAuthDeptCode').val());
                    $('#conclusionnotoffice').val($('#hidAuthOffCode').val());
                    $('#conclusionnotspc').val($('#authSpc').val());
                    $('#conclusionnotauthority').val($('#authSpc option:selected').text());
                } else if (tauthtype == "E") {
                    $('#conclusionnotdept').val($('#hidAuthDeptCode').val());
                    $('#conclusionnotoffice').val($('#hidAuthOffCode').val());
                    $('#conclusionnotspc').val($('#authSpc').val());
                    // $('#conclusionnotauthority').val($('#authSpc option:selected').text());
                }

            }
            function selectauthtype(authtype) {
                tauthtype = authtype;
            }
            function hideAllTab() {
                $('#tab01').hide();
                $('#tab02').hide();
                $('#tab03').hide();
                $('#tab04').hide();
                $('#tab05').hide();
                $('#tab06').hide();
                $('#tab07').hide();
                $('#tab08').hide();
                $('#tab09').hide();
            }
            function showTab(tabId, pdtype) {
                //hideAllTab();
                vpdtype = pdtype;
                $('#tab' + tabId).modal('show');
            }
            function deletePunishDetails(me, tabId, punishmentdetailsid) {
                if (confirm('Are you sure you want to delete?')) {
                    hideAllTab();
                    var url = "deletePunishmentDetails.htm";
                    $.post(url, {punishmentdetailsid: punishmentdetailsid, pentype: tabId})
                            .done(function(data) {
                                if (data.msg == "Y") {
                                    $(me).parent().parent().remove();
                                } else {
                                    alert("Some Error Occured");
                                }
                            });
                }
            }
            function editPunishDetails(tabId, punishmentdetailsid) {
                hideAllTab();
                $('#tab' + tabId).show();
                var url = "editPunishmentDetails.htm";
                $.post(url, {punishmentdetailsid: punishmentdetailsid, pentype: tabId})
                        .done(function(data) {
                            if (tabId == "01") {
                                $("#punishmentdetailsid").val(punishmentdetailsid);
                                $("#finewef").val(data.wefdate);
                                $("#fineamount").val(data.fineamount);
                                $("input[name=recvtype][value=" + data.recvtype + "]").attr('checked', 'checked');
                                $("#finewef").val(data.wefdate);
                                $("#noinstll").val(data.noinstll);
                            } else if (tabId == "02") {
                                $("#punishmentdetailsid").val(punishmentdetailsid);
                                $("#withincwef").val(data.wefdate);
                                $("#scalepay").val(data.scalepay);
                                $("#noofinc").val(data.noofinc);
                                $("input[name=withincum][value=" + data.withincum + "]").attr('checked', 'checked');
                            } else if (tabId == "03") {
                                $("#punishmentdetailsid").val(punishmentdetailsid);
                                $("#withpromwef").val(data.wefdate);
                                $("#promtodate").val(data.tilldate);
                                $("#noofpromotion").val(data.noofpromotion);
                                $("input[name=withpromcum][value=" + data.withpromcum + "]").attr('checked', 'checked');
                            } else if (tabId == "04") {
                                $("#punishmentdetailsid").val(punishmentdetailsid);
                                $("#suspwefd").val(data.wefdate);
                                $("#susptodate").val(data.tilldate);
                                $("input[name=suspperiod][value=" + data.treatasduty + "]").attr('checked', 'checked');
                                $("#typeofleave").val(data.tolId);
                            } else if (tabId == "05") {
                                $("#punishmentdetailsid").val(punishmentdetailsid);
                                $("#rednsrvwefd").val(data.wefdate);
                                $("#rednservtodate").val(data.tilldate);
                                $("#rednservdepartment").val(data.depCode);
                                $("#rednservoffice").val(data.offCode);
                                $("#redncadre").val(data.cadreCode);
                                $("#redngrade").val(data.grade);
                                $("#lwrpost").val(data.postCode);
                                $("#lwrscalepay").val(data.payScale);
                                $("#allwdinc").val(data.earnInc);
                            } else if (tabId == "06") {
                                $("#punishmentdetailsid").val(punishmentdetailsid);
                                $("#duedateofretire").val(data.wefdate);
                            } else if (tabId == "07") {
                                $("#punishmentdetailsid").val(punishmentdetailsid);
                                $("#duedateofremoval").val(data.wefdate);
                                $("input[name=remfutemp][value=" + data.remfutemp + "]").attr('checked', 'checked');
                            } else if (tabId == "08") {
                                $("#punishmentdetailsid").val(punishmentdetailsid);
                                $("#dismsrvdate").val(data.wefdate);
                            }
                        });
            }
            function cancelPunishmentDetails() {
                hideAllTab();
                $("#punishmentdetailsid").val(0);
            }
            /*
             function savePunishmentDetails(pentype,pdtype) {
             var url = "savePunishmentDetails.htm";
             var concprocid = $("#concprocid").val();
             var punishmentdetailsid = $("#punishmentdetailsid").val();
             if (pentype == "01") {
             var wefdate = $("#finewef").val();
             var fineamount = $("#fineamount").val();
             var recvtype = $("input[name='recvtype']:checked").val();
             var noinstll = 0;
             if($("#noinstll").val() != ""){
             alert($("#noinstll").val());
             noinstll = $("#noinstll").val();
             }
             alert(noinstll);
             $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate,
             fineamount: fineamount, recvtype: recvtype, noinstll: noinstll})
             .done(function (data) {
             $("#punishmentdetailsid").val(0);
             alert("Saved");
             });
             } else if (pentype == "02") {
             var wefdate = $("#withincwef").val();
             var scalepay = $("#scalepay").val();
             var noofinc = $("#noofinc").val();
             var withincum = $("#withincum").val();
             $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate,
             scalepay: scalepay, noofinc: noofinc, withincum: withincum})
             .done(function (data) {
             $("#punishmentdetailsid").val(0);
             alert("Saved");
             });
             } else if (pentype == "03") {
             var wefdate = $("#withpromwef").val();
             var tilldate = $("#promtodate").val();
             var noofpromotion = $("#noofpromotion").val();
             var withpromcum = $("#withpromcum").val();
             $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate,
             tilldate: tilldate, noofpromotion: noofpromotion, withpromcum: withpromcum})
             .done(function (data) {
             $("#punishmentdetailsid").val(0);
             alert("Saved");
             });
             } else if (pentype == "04") {
             
             } else if (pentype == "05") {
             
             } else if (pentype == "06") {
             var wefdate = $("#duedateofretire").val();
             $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate})
             .done(function (data) {
             $("#punishmentdetailsid").val(0);
             alert("Saved");
             });
             } else if (pentype == "07") {
             var wefdate = $("#duedateofremoval").val();
             var remfutemp = $("input[name='remfutemp']:checked").val();
             $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate, remfutemp: remfutemp})
             .done(function (data) {
             $("#punishmentdetailsid").val(0);
             alert("Saved");
             });
             } else if (pentype == "08") {
             var wefdate = $("#dismsrvdate").val();
             $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate})
             .done(function (data) {
             $("#punishmentdetailsid").val(0);
             alert("Saved");
             });
             }
             }*/
            function radioClickedForApprovalOfAuthority() {
                var radioValue = $("input[name='approvalByAuthority']:checked").val();
                if (radioValue == "AlreadyApproveByDA") {
                    $("#alreadyDAApproval").show();
                    $("#sendForApproval").hide();
                } else if (radioValue == "NotApproveByDA") {
                    $("#sendForApproval").show();
                    $("#alreadyDAApproval").hide();
                }
            }
            function radioClickedForFirstShowCause() {
                var radioValue = $("input[name='showcause']:checked").val();
                if (radioValue == "AlreadyServed") {
                    $("#alreadyservedDiv").show();
                    $("#sendshowcauseDiv").hide();
                } else if (radioValue == "NotServed") {
                    $("#sendshowcauseDiv").show();
                    $("#alreadyservedDiv").hide();
                }
            }
            function radioClickedondefenceremark() {
                var radioValue = $("input[name='defenceremarkByDO']:checked").val();
                if (radioValue == "Inquiry") {
                    $("#appointmentofIoPODetail").show();
                    $("#MinorPenaltyONpresentationOfDO").hide();
                    $("#exanaurationOnRepresentationOfDO").hide();
                } else if (radioValue == "MinorPenalty") {
                    $("#MinorPenaltyONpresentationOfDO").show();
                    $("#appointmentofIoPODetail").hide();
                    $("#exanaurationOnRepresentationOfDO").hide();
                } else if (radioValue == "Exoneration") {
                    $("#exanaurationOnRepresentationOfDO").show();
                    $("#MinorPenaltyONpresentationOfDO").hide();
                    $("#appointmentofIoPODetail").hide();
                }
            }
            function radioClickedforisIoReportSubmitted() {
                var radioValue = $("input[name='isIoReportSubmitted']:checked").val();
                if (radioValue == "IoReportSubmit") {
                    $("#ioandPoandAporemarksDiv").show();
                    $("#sendIoPoApo").hide();
                } else if (radioValue == "IoReportNotSubmit") {
                    $("#sendIoPoApo").show();
                    $("#ioandPoandAporemarksDiv").hide();
                }
            }
            function radioClickedforIoRemarksReview() {
                var radioValue = $("input[name='ioremarksreviewcheck']:checked").val();
                if (radioValue == "ioremarksAccepted") {
                    $("#serveToDelinquentOnIoRemarks").show();
                    $("#reinquiryonDelinquentOfficerDiv").hide();

                } else if (radioValue == "ioremarksRejected") {
                    $("#reinquiryonDelinquentOfficerDiv").show();
                    $("#serveToDelinquentOnIoRemarks").hide();
                }
            }
            function radioClickedForserveDelinquentOnIoRemarks() {
                var radioValue = $("input[name='serveDelinquentOnIoRemarks']:checked").val();
                if (radioValue == "AlreadyServedDelinquentOnIoRemarks") {
                    $("#serveDelinquentOnIoRemarksDiv").show();
                    $("#sendserveDelinquentOnIoRemarks").hide();
                } else if (radioValue == "NotServedDelinquentOnIoRemarks") {
                    $("#sendserveDelinquentOnIoRemarks").show();
                    $("#serveDelinquentOnIoRemarksDiv").hide();
                }
            }
            function radioClickedforPunishmentOnpresentationOfDOOnIoReport() {
                var radioValue = $("input[name='punishmentProposedOnRepresentationOfDoOnIoReport']:checked").val();
                if (radioValue == "punishmentOnRepresentationOfDOOnIoReport") {
                    $("#PunishmentDetailONpresentationOfDOonIoReport").show();
                    $("#thirdNoticeToDelinquentForPunishmentProposed").show();
                    $("#exanaurationDetailONpresentationOfDOonIoReport").hide();
                } else if (radioValue == "exanarutionOnRepresentationOfDOOnIoReport") {
                    $("#exanaurationDetailONpresentationOfDOonIoReport").show();
                    $("#PunishmentDetailONpresentationOfDOonIoReport").hide();
                    $("#thirdNoticeToDelinquentForPunishmentProposed").hide();
                }
            }

            function radioClickedForthirdNoticeToDelinquentForPunishmentProposed() {
                var radioValue = $("input[name='thirdNoticeToDelinquentPunishmentProposed']:checked").val();
                if (radioValue == "AlreadyServedDelinquentthirdNotice") {
                    $("#serveDelinquentthirdNoticeDetail").show();
                    $("#sendDelinquentthirdNotice").hide();
                } else if (radioValue == "NotServedDelinquentthirdNotice") {
                    $("#sendDelinquentthirdNotice").show();
                    $("#serveDelinquentthirdNoticeDetail").hide();
                }
            }
            function radioClickedforPunishmentOnpresentationOfDOOnpunishment() {
                var radioValue = $("input[name='ProposedpunishmentOnRepresentationOfDOOnpunishment']:checked").val();
                if (radioValue == "proposedpunishmentOnRepresentationOfDOOpunishment") {
                    $("#PunishmentAssignmentBygovt").show();
                    $("#PunishmentDetailONpresentationOfDOonafterAssignpunishment").show();
                    $("#ExanaurationDetailONpresentationOfAfterAssignPunishment").hide();
                } else if (radioValue == "exanarutionOnRepresentationOfDOOpunishment") {
                    $("#ExanaurationDetailONpresentationOfAfterAssignPunishment").show();
                    $("#PunishmentDetailONpresentationOfDOonafterAssignpunishment").hide();
                    $("#PunishmentAssignmentBygovt").hide();

                }
            }
            function validation() {
                /*If already approved case*/
                var radioValue = $("input[name='approvalByAuthority']:checked").val();
                if (radioValue == "AlreadyApproveByDA") {
                    if ($("#memoNo").val() == "") {
                        alert("Please enter Memorandum Number");
                        $("#memoNo").focus();
                        return false;
                    }
                    if ($("#memoDate").val() == "") {
                        alert("Please enter Memorandum date");
                        $("#memoDate").focus();
                        return false;
                    }
                    if ($("#memorandumdocument").val() == "") {
                        alert("Please Choose the File For Memorandum");
                        $("#memorandumdocument").focus();
                        return false;
                    }
                    if ($("#disciplinaryauthority").val() == "") {
                        alert("Please Enter Disciplinary Authority Detail");
                        $("#disciplinaryauthority").focus();
                        return false;
                    }
                    var fup = document.getElementById('memorandumdocument');
                    if (fup) {
                        var fileName = fup.value;
                        var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                        var ext = ext.toLowerCase();

                        if (fileName != "" && (ext != "pdf" && ext != "zip")) {
                            alert("Upload pdf/zip files only For Memorandum document");
                            fup.focus();
                            return false;
                        }

                        var fsize = fup.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 20000) {
                            alert("File too Big, please select a file less than 20mb");
                            $("#memorandumdocument").val('');
                            return false;
                        }
                    }

                }

                /*In 1st showcause case*/
                var radioValue = $("input[name='showcause']:checked").val();
                if (radioValue == "AlreadyServed") {
                    if ($("#showCauseOrdDt").val() == "") {
                        alert("Please enter showcause issue date");
                        $("#showCauseOrdDt").focus();
                        return false;
                    }
                    if ($("#firstshowcausedocument").val() == "") {
                        alert("Please Choose the File For First Show cause");
                        $("#firstshowcausedocument").focus();
                        return false;
                    }
                    var fup2 = document.getElementById('firstshowcausedocument');
                    if (fup2) {
                        var fileName2 = fup2.value;
                        var ext2 = fileName2.substring(fileName2.lastIndexOf('.') + 1);
                        var ext2 = ext2.toLowerCase();

                        if (fileName2 != "" && (ext2 != "pdf" && ext2 != "zip")) {
                            alert("Upload pdf/zip files only For First Showcause document By Delinquent Officer");
                            fup2.focus();
                            return false;
                        }
                        var fsize = fup2.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 20000) {
                            alert("File too Big, please select a file less than 20mb");
                            $("#firstshowcausedocument").val('');
                            return false;
                        }
                    }
                }


                if ($("#disciplinaryauthority").val() == "") {
                    alert("Please enter Disciplinary Authority Detail");
                    $("#disciplinaryauthority").focus();
                    return false;
                }
                if ($("#defenceByDOdocument").val() == "") {
                    alert("Please Choose the File");
                    $("#defenceByDOdocument").focus();
                    return false;
                }
                /*If authority decision is inquiry*/
                var radioValue = $("input[name='defenceremarkByDO']:checked").val();
                if (radioValue == "Inquiry") {
                    if ($("#ioAppoinmentOrdNo").val() == "") {
                        alert("Please input Order Number For Io Appointment");
                        $("#ioAppoinmentOrdNo").focus();
                        return false;
                    }
                    if ($("#ioAppoinmentOrdDt").val() == "") {
                        alert("Please input Order Date For Io Appointment");
                        $("#ioAppoinmentOrdDt").focus();
                        return false;
                    }
                    if ($("#inquiryauthority").val() == "") {
                        alert("Please enter Inquiry Authority Detail");
                        $("#inquiryauthority").focus();
                        return false;
                    }
                    if ($("#presentingauthority").val() == "") {
                        alert("Please enter Presenting Authority Detail");
                        $("#presentingauthority").focus();
                        return false;
                    }
                } else if (radioValue == "Exoneration") {

                }
                if ($("#ioRemarksOrdNo").val() == "") {
                    alert("Please input Order Date For IO Remarks");
                    $("#ioRemarksOrdNo").focus();
                    return false;
                }
                if ($("#ioRemarksOrdDt").val() == "") {
                    alert("Please input Order Date For IO Remarks");
                    $("#ioRemarksOrdDt").focus();
                    return false;
                }

                var radioValue = $("input[name='isIoReportSubmitted']:checked").val();
                if (radioValue == "IoReportSubmit") {
                    if ($("#remarksByIOdocument").val() == "") {
                        alert("Please Choose the File For IO Remarks");
                        $("#remarksByIOdocument").focus();
                        return false;
                    }
                    var fup4 = document.getElementById('remarksByIOdocument');
                    if (fup4) {
                        var fileName4 = fup4.value;
                        var ext4 = fileName4.substring(fileName4.lastIndexOf('.') + 1);
                        var ext4 = ext4.toLowerCase();

                        if (fileName4 != "" && (ext4 != "pdf" && ext4 != "zip")) {
                            alert("Upload pdf/zip files only For Remarks By Io document");
                            fup4.focus();
                            return false;
                        }
                        var fsize = fup4.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 20000) {
                            alert("File too Big, please select a file less than 20mb");
                            $("#remarksByIOdocument").val('');
                            return false;
                        }
                    }
                }

                var radioValue = $("input[name='ioremarksreviewcheck']:checked").val();
                if (radioValue == "ioremarksAccepted") {
                    if ($("#ordNoForNoticetoDOOnIoRemark").val() == "") {
                        alert("Please enter the Order Number");
                        $("#ordNoForNoticetoDOOnIoRemark").focus();
                        return false;
                    }
                    if ($("#ordDtForNoticeOnTODOIoRemark").val() == "") {
                        alert("Please enter the Order date");
                        $("#ordDtForNoticeOnTODOIoRemark").focus();
                        return false;
                    }


                    if ($("#noticetoDOOnIoRemarkdocument").val() == "") {
                        alert("Please Choose the File");
                        $("#noticetoDOOnIoRemarkdocument").focus();
                        return false;
                    }
                    var fup6 = document.getElementById('noticetoDOOnIoRemarkdocument');
                    if (fup6) {
                        var fileName6 = fup6.value;
                        var ext6 = fileName6.substring(fileName6.lastIndexOf('.') + 1);
                        var ext6 = ext6.toLowerCase();

                        if (fileName6 != "" && (ext6 != "pdf" && ext6 != "zip")) {
                            alert("Upload pdf/zip files only For Notice to Delinquent Officer document");
                            fup6.focus();
                            return false;
                        }
                        var fsize = fup6.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 20000) {
                            alert("File too Big, please select a file less than 20mb");
                            $("#noticetoDOOnIoRemarkdocument").val('');
                            return false;
                        }
                    }
                }

                var radioValue = $("input[name='serveDelinquentOnIoRemarks']:checked").val();
                if (radioValue == "AlreadyServedDelinquentOnIoRemarks") {
                    if ($("#secondshowCauseOrdDt").val() == "") {
                        alert("Please enter Notice Serve Date");
                        $("#secondshowCauseOrdDt").focus();
                        return false;
                    }
                }




                if ($("#doRepresentationOnsecondshowCauseOrdDt").val() == "") {
                    alert("Please enter Receipt Date");
                    $("#doRepresentationOnsecondshowCauseOrdDt").focus();
                    return false;
                }
                if ($("#secondshowcausedocumentondoRepresentation").val() == "") {
                    alert("Please Choose the document");
                    $("#secondshowcausedocumentondoRepresentation").focus();
                    return false;
                }
                /* var radioValue = $("input[name='punishmentProposedOnRepresentationOfDoOnIoReport']:checked").val();
                 if (radioValue == "exanarutionOnRepresentationOfDOOnIoReport") {
                 if ($("#exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport").val() == "") {
                 alert("Please Enter Exanauration Final Order Number");
                 $("#exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport").focus();
                 return false;
                 }
                 if ($("#exanaurationfinalOrddateOnRepresentationOfDoOnIoReport").val() == "") {
                 alert("Please Enter Exanauration Final Order Date");
                 $("#exanaurationfinalOrddateOnRepresentationOfDoOnIoReport").focus();
                 return false;
                 }
                 }*/
                if ($("#defenceByDOdocument").val() == "") {
                    alert("Please Choose the File");
                    $("#defenceByDOdocument").focus();
                    return false;
                }

                var radioValue = $("input[name='punishmentProposedOnRepresentationOfDoOnIoReport']:checked").val();
                if (radioValue == "punishmentOnRepresentationOfDOOnIoReport") {
                    if ($("#ordNoForthirdNoticetoDOOnForPunishment").val() == "") {
                        alert("Please Choose the Ord No For Notice to DO For Punishment");
                        $("#ordNoForthirdNoticetoDOOnForPunishment").focus();
                        return false;
                    }
                    if ($("#OrdDtForthirdNoticetoDOOnForPunishment").val() == "") {
                        alert("Please Choose the Ord Date For Notice to DO For Punishment");
                        $("#OrdDtForthirdNoticetoDOOnForPunishment").focus();
                        return false;
                    }
                    if ($("#thirdNoticetoDOOnForPunishmentdocument").val() == "") {
                        alert("Please Choose the document");
                        $("#thirdNoticetoDOOnForPunishmentdocument").focus();
                        return false;
                    }

                    var fup8 = document.getElementById('thirdNoticetoDOOnForPunishmentdocument');
                    if (fup8) {
                        var fileName8 = fup8.value;
                        var ext8 = fileName8.substring(fileName8.lastIndexOf('.') + 1);
                        var ext8 = ext8.toLowerCase();

                        if (fileName8 != "" && (ext8 != "pdf" && ext8 != "zip")) {
                            alert("Upload pdf/zip files only For Notice to Delinquent Officer document");
                            fup8.focus();
                            return false;
                        }

                        var fsize = fup8.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 20000) {
                            alert("File too Big, please select a file less than 20mb");
                            $("#thirdNoticetoDOOnForPunishmentdocument").val('');
                            return false;
                        }
                    }
                }
                var radioValue = $("input[name='thirdNoticeToDelinquentPunishmentProposed']:checked").val();
                if (radioValue == "AlreadyServedDelinquentthirdNotice") {
                    if ($("#thirdshowCauseOrdDt").val() == "") {
                        alert("Please Choose the Ord No For Served to Delinquent Officer");
                        $("#thirdshowCauseOrdDt").focus();
                        return false;
                    }


                }
                /*For Representation Against Proposed Punishment */
                if ($("#thirdshowCauseReplyByDAOrdDt").val() == "") {
                    alert("Please Choose the date");
                    $("#thirdshowCauseReplyByDAOrdDt").focus();
                    return false;
                }

                if ($("#thirdshowcausedocumentondoRepresentation").val() == "") {
                    alert("Please Choose the document");
                    $("#thirdshowcausedocumentondoRepresentation").focus();
                    return false;
                }






                var fup3 = document.getElementById('defenceByDOdocument');
                if (fup3) {
                    var fileName3 = fup3.value;
                    var ext3 = fileName3.substring(fileName3.lastIndexOf('.') + 1);
                    var ext3 = ext3.toLowerCase();

                    if (fileName3 != "" && (ext3 != "pdf" && ext3 != "zip")) {
                        alert("Upload pdf/zip files only For Remarks By Io document");
                        fup3.focus();
                        return false;
                    }
                    var fsize = fup3.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 20000) {
                        alert("File too Big, please select a file less than 20mb");
                        $("#defenceByDOdocument").val('');
                        return false;
                    }
                }



                var fup5 = document.getElementById('remarksByIOdocument');
                if (fup5) {
                    var fileName5 = fup5.value;
                    var ext5 = fileName5.substring(fileName5.lastIndexOf('.') + 1);
                    var ext5 = ext5.toLowerCase();

                    if (fileName5 != "" && (ext5 != "pdf" && ext5 != "zip")) {
                        alert("Upload pdf/zip files only For Remarks By Io document");
                        fup5.focus();
                        return false;
                    }
                    var fsize = fup5.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 20000) {
                        alert("File too Big, please select a file less than 20mb");
                        $("#remarksByIOdocument").val('');
                        return false;
                    }
                }


                var fup7 = document.getElementById('secondshowcausedocumentondoRepresentation');
                if (fup7) {
                    var fileName7 = fup7.value;
                    var ext7 = fileName7.substring(fileName7.lastIndexOf('.') + 1);
                    var ext7 = ext7.toLowerCase();

                    if (fileName7 != "" && (ext7 != "pdf" && ext7 != "zip")) {
                        alert("Upload pdf/zip files only For Notice to Delinquent Officer document");
                        fup7.focus();
                        return false;
                    }
                    var fsize = fup7.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 20000) {
                        alert("File too Big, please select a file less than 20mb");
                        $("#secondshowcausedocumentondoRepresentation").val('');
                        return false;
                    }
                }




                var fup9 = document.getElementById('thirdshowcausedocumentondoRepresentation');
                if (fup9) {
                    var fileName9 = fup9.value;
                    var ext9 = fileName9.substring(fileName9.lastIndexOf('.') + 1);
                    var ext9 = ext9.toLowerCase();

                    if (fileName9 != "" && (ext9 != "pdf" && ext9 != "zip")) {
                        alert("Upload pdf/zip files only For Notice to Delinquent Officer document");
                        fup8.focus();
                        return false;
                    }

                    var fsize = fup9.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 20000) {
                        alert("File too Big, please select a file less than 20mb");
                        $("#thirdshowcausedocumentondoRepresentation").val('');
                        return false;
                    }
                }

            }
            function addToTable(punishmentDetails) {
                var punishmentName = "";
                if (punishmentDetails.pentype == '01') {
                    punishmentName = "Fine";
                } else if (punishmentDetails.pentype == '02') {
                    punishmentName = "Withholding Increment";
                } else if (punishmentDetails.pentype == '03') {
                    punishmentName = "Withholding Promotion";
                } else if (punishmentDetails.pentype == '04') {
                    punishmentName = "Suspension";
                } else if (punishmentDetails.pentype == '05') {
                    punishmentName = "Reduction to Lower Service";
                } else if (punishmentDetails.pentype == '06') {
                    punishmentName = "Compulsory Retirement";
                } else if (punishmentDetails.pentype == '07') {
                    punishmentName = "Removal From Service";
                } else if (punishmentDetails.pentype == '08') {
                    punishmentName = "Dismisal From Service";
                } else if (punishmentDetails.pentype == '09') {
                    punishmentName = "Recovery";
                }
                var html = '<tr><td>1</td>';
                html = html + '<td>' + punishmentName + '</td>';
                html = html + '<td>' + punishmentDetails.wefdate + '</td>';
                html = html + '<td></td>'
                html = html + '<td></td>'
                html = html + '<td>';
                html = html + '<a class="btn btn-primary" href="javascript:void(0)" onclick="editPunishDetails(\'' + punishmentDetails.concprocid + '\',' + punishmentDetails.concprocid + ')"><span class="glyphicon glyphicon-pencil"></span> Edit</a> | ';
                html = html + '<a class="btn btn-primary" href="javascript:void(0)" onclick="deletePunishDetails(this, \'01\',3490)"><span class="glyphicon glyphicon-remove"></span> Delete</a>';
                html = html + '</td></tr>';
                if (vpdtype == "P") {
                    $("#punishmenttbl").append(html);
                } else if (vpdtype == "F") {
                    $("#punishmentbygovt").append(html);
                }
            }

            function savePunishmentDetails(pentype) {//pdtype means proposed punishment or final punishment
                var url = "savePunishmentDetails.htm";
                var concprocid = $("#daId").val();
                var punishmentdetailsid = $("#punishmentdetailsid").val();
                if (pentype == "01") {
                    var wefdate = $("#finewef").val();
                    var fineamount = $("#fineamount").val();
                    var recvtype = $("input[name='recvtype']:checked").val();
                    var noinstll = 0;
                    if ($("#noinstll").val() != "") {
                        noinstll = $("#noinstll").val();
                    }
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate,
                        fineamount: fineamount, recvtype: recvtype, noinstll: noinstll, pdtype: vpdtype})
                            .done(function(data) {
                                if (data.msg == 'Y') {
                                    $("#punishmentdetailsid").val(0);
                                    addToTable(data);
                                    alert("Saved");
                                } else {
                                    alert("Error Occured");
                                }
                            });
                } else if (pentype == "02") {
                    var wefdate = $("#withincwef").val();
                    var scalepay = $("#scalepay").val();
                    var noofinc = $("#noofinc").val();
                    var withincum = $("#withincum").val();
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate,
                        scalepay: scalepay, noofinc: noofinc, withincum: withincum, pdtype: vpdtype})
                            .done(function(data) {
                                if (data.msg == 'Y') {
                                    $("#punishmentdetailsid").val(0);
                                    addToTable(data);
                                    alert("Saved");
                                } else {
                                    alert("Error Occured");
                                }
                            });
                } else if (pentype == "03") {
                    var wefdate = $("#withpromwef").val();
                    var tilldate = $("#promtodate").val();
                    var noofpromotion = $("#noofpromotion").val();
                    var withpromcum = $("#withpromcum").val();
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate,
                        tilldate: tilldate, noofpromotion: noofpromotion, withpromcum: withpromcum, pdtype: vpdtype})
                            .done(function(data) {
                                if (data.msg == 'Y') {
                                    $("#punishmentdetailsid").val(0);
                                    addToTable(data);
                                    alert("Saved");
                                } else {
                                    alert("Error Occured");
                                }
                            });
                } else if (pentype == "04") {
                    var suspordno = $("#suspordno").val();
                    var wefdate = $("#suspwefd").val();
                    var tilldate = $("#susptodate").val();
                    var treatasduty = $('input[name=suspperiod]:checked').val();
                    var tolId = $("#typeofleave").val();
                    alert(treatasduty);
                    if (!treatasduty) {
                        alert("Please select Period of Suspension to be treated as");
                        return false;
                    }
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, suspordno: suspordno, wefdate: wefdate,
                        tilldate: tilldate, treatasduty: treatasduty, tolId: tolId, pdtype: vpdtype})
                            .done(function(data) {
                                if (data.msg == 'Y') {
                                    $("#punishmentdetailsid").val(0);
                                    addToTable(data);
                                    alert("Saved");
                                } else {
                                    alert("Error Occured");
                                }
                            });
                } else if (pentype == "05") {
                    var wefdate = $("#rednsrvwefd").val();
                    var tilldate = $("#rednservtodate").val();
                    var depCode = $("#rednservdepartment").val();
                    var offCode = $("#rednservoffice").val();
                    var cadreCode = $("#redncadre").val();
                    var grade = $("#redngrade").val();
                    var postCode = $("#lwrpost").val();
                    var payScale = $("#lwrscalepay").val();
                    var earnInc = $("#allwdinc").val();
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate,
                        tilldate: tilldate, depCode: depCode, offCode: offCode, cadreCode: cadreCode, grade: grade, postCode: postCode, payScale: payScale, earnInc: earnInc, pdtype: vpdtype})
                            .done(function(data) {
                                if (data.msg == 'Y') {
                                    $("#punishmentdetailsid").val(0);
                                    addToTable(data);
                                    alert("Saved");
                                } else {
                                    alert("Error Occured");
                                }
                            });
                } else if (pentype == "06") {
                    var wefdate = $("#duedateofretire").val();
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate, pdtype: vpdtype})
                            .done(function(data) {
                                if (data.msg == 'Y') {
                                    $("#punishmentdetailsid").val(0);
                                    addToTable(data);
                                    alert("Saved");
                                } else {
                                    alert("Error Occured");
                                }
                            });
                } else if (pentype == "07") {
                    var wefdate = $("#duedateofremoval").val();
                    var remfutemp = $("input[name='remfutemp']:checked").val();
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate, remfutemp: remfutemp, pdtype: vpdtype})
                            .done(function(data) {
                                if (data.msg == 'Y') {
                                    $("#punishmentdetailsid").val(0);
                                    addToTable(data);
                                    alert("Saved");
                                } else {
                                    alert("Error Occured");
                                }
                            });
                } else if (pentype == "08") {
                    var wefdate = $("#dismsrvdate").val();
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate, pdtype: vpdtype})
                            .done(function(data) {
                                if (data.msg == 'Y') {
                                    $("#punishmentdetailsid").val(0);
                                    addToTable(data);
                                    alert("Saved");
                                } else {
                                    alert("Error Occured");
                                }
                            });
                }

                else if (pentype == "09") {
                    var wefdate = $("#finewef").val();
                    var fineamount = $("#fineamount").val();
                    var recvtype = $("input[name='recvtype']:checked").val();
                    var noinstll = $("#noinstll").val();
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate,
                        fineamount: fineamount, recvtype: recvtype, noinstll: noinstll, pdtype: vpdtype})
                            .done(function(data) {
                                if (data.msg == 'Y') {
                                    $("#punishmentdetailsid").val(0);
                                    addToTable(data);
                                    alert("Saved");
                                } else {
                                    alert("Error Occured");
                                }
                            });
                }

            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <form:form action="editRule15ProceedingForOngoingDPRule15.htm" method="post" commandName="pbean" enctype="multipart/form-data">
                    <form:hidden path="dadid"/>
                    <form:hidden path="daId" />
                    <form:hidden path="dpStatus"/>
                    <form:hidden path="isAuthorityApprove"/>
                    <form:hidden path="hasSendNoticetoDO"/>
                    <input type="hidden" name="hasReplyByDelinquentOfficer" value="${defencebean.hasReplyByDelinquentOfficer}"/>
                    <input type="hidden" name="defid" value="${defencebean.defid}"/>
                    <input type="hidden" name="daioid" value="${ioBean.daioid}"/>
                    <form:hidden path="daioid"/>
                    <form:hidden path="hasIoAppointed"/>
                    <form:hidden path="hasIoRemarks"/>
                    <form:hidden path="hasSendSecondNotice"/>
                    <form:hidden path="hasDoRepresentOnsecondshowCause"/>
                    <form:hidden path="hasSendthirdshowCause"/>

                    <div class="form-group">
                        <label class="control-label col-sm-2">Delinquent Officer Name:</label>
                        <c:forEach items="${delinquentofficerList}" var="delinquent">
                            <input type="hidden" name="delinquent" value="${delinquent.empid}-${delinquent.spc}"/>
                            ${delinquent.fullname},${delinquent.spn}<br/>
                        </c:forEach>

                    </div>

                    <div class="panel panel-default" id="articleOfCharge">
                        <div class="panel-heading">
                            Draft Charge
                        </div>
                        <div class="panel-body">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="3%">#</th>
                                        <th width="25%">Article of Charge</th>
                                        <th width="25%">Statement Of Imputation</th>
                                        <th width="25%">Memo Of Evidence</th>
                                        <th width="15%">Witness</th>
                                        <th width="15%">Action</th>
                                    </tr>
                                </thead>
                                <tbody>                                        
                                    <c:forEach items="${articleOfChargeList}" var="articleOfCharge" varStatus="cnt">
                                        <tr>
                                            <td>${cnt.index + 1}</td>
                                            <td>
                                                ${articleOfCharge.articleOfCharge} ${articleOfCharge.articlesofChargeoriginalfilename}
                                                <c:if test="${not empty articleOfCharge.articlesofChargeoriginalfilename}">
                                                    <a href="downloadFileForArticleOfCharge.htm?dacid=${articleOfCharge.dacid}&documentTypeName=articlecharge" class="btn btn-default">
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${articleOfCharge.articlesofChargeoriginalfilename}</a>
                                                    </c:if>
                                            </td>
                                            <td>
                                                ${articleOfCharge.statementOfImputation}
                                                <c:if test="${not empty articleOfCharge.statementOfImputationoriginalfilename}">
                                                    <a href="downloadFileForArticleOfCharge.htm?dacid=${articleOfCharge.dacid}&documentTypeName=statementimputation" class="btn btn-default">
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${articleOfCharge.statementOfImputationoriginalfilename}</a>
                                                    </c:if>
                                            </td>
                                            <td>
                                                ${articleOfCharge.memoOfEvidence}
                                                <c:if test="${not empty articleOfCharge.memoofEvidenceoriginalfilename}">
                                                    <a href="downloadFileForArticleOfCharge.htm?dacid=${articleOfCharge.dacid}&documentTypeName=memoevidence" class="btn btn-default" >
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${articleOfCharge.memoofEvidenceoriginalfilename}</a>
                                                    </c:if>
                                            </td>
                                            <td>
                                                <a  href="employeeWitnessList.htm?action=witnessList&dacid=${articleOfCharge.dacid}&daId=${articleOfCharge.daId}" class="btn btn-default" >Witness</a>   
                                            </td> 
                                            <td>
                                                <c:if test="${pbean.isAuthorityApprove ne 'Y'}">
                                                    <a href="editNewDisccharge.htm?dacid=${articleOfCharge.dacid}&daId=${articleOfCharge.daId}" class="btn btn-default" >Edit </a>
                                                </c:if>
                                            </td>                            
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        <c:if test="${pbean.isAuthorityApprove ne 'Y'}">
                            <div class="panel-footer">
                                <input type="submit" name="action" class="btn btn-default" value="Add New"/>
                            </div>
                        </c:if>
                    </div>

                    <div class="panel panel-default">
                        <c:if test="${pbean.isAuthorityApprove ne 'Y'}">
                            <div class="panel-heading">
                                Whether Approve By Disciplinary Authority
                            </div>

                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">                                    
                                    <input type="radio" id="AlreadyApproveByDA" name="approvalByAuthority" value="AlreadyApproveByDA" onclick="radioClickedForApprovalOfAuthority()"> <b>Already Approved</b>
                                </div>

                                <div class="col-lg-2">
                                    <input type="radio" id="NotApproveByDA" name="approvalByAuthority" value="NotApproveByDA" onclick="radioClickedForApprovalOfAuthority()"> <b>Not Approved</b>
                                </div>                           
                            </div>
                        </c:if>

                        <div class="panel panel-default" id="alreadyDAApproval">
                            <div class="panel-heading">
                                Memorandum Of Charge
                            </div>
                            <div class="panel-body" style="margin-bottom: 7px;">
                                <div class="row" style="margin-bottom: 7px;">  
                                    <div class="col-lg-2">
                                        <label>1.Memorandum No<span style="color: red">*</span></label>
                                    </div>

                                    <c:if test="${empty pbean.memoNo}">
                                        <div class="col-lg-2">
                                            <form:input class="form-control" path="memoNo" maxlength="51"/> 
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.memoNo}">
                                        <div class="col-lg-2">
                                            ${pbean.memoNo}
                                        </div>
                                    </c:if>

                                    <div class="col-lg-2">
                                        <label for="showcauseOrdDt"> 2.Memorandum Date<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${empty pbean.memoDate}">
                                        <div class="col-lg-2">
                                            <div class="input-group date">
                                                <form:input class="form-control datepickerclass" path="memoDate" readonly="true"/>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>                                
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.memoDate}">
                                        ${pbean.memoDate}
                                    </c:if> 
                                </div>
                                <div class="row" style="margin-bottom: 7px;">  
                                    <div class="col-lg-2">
                                        <label for="document">3.Document(Memorandum Copy)<span style="color: red">*</span><br>
                                            <span style="color: #c83939;font-style: italic;">(Maximum File size is 20 mb per attachment, Only pdf/zip file can be uploaded.)</span>
                                        </label>
                                    </div>

                                    <c:if test="${not empty pbean.memorandumoriginalFileName}">
                                        <a href="downloadMemorandumAttachment.htm?daId=${pbean.daId}">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.memorandumoriginalFileName}</a>
                                        </c:if>
                                        <c:if test="${empty pbean.memorandumoriginalFileName}">
                                        <div class="form-group row">                            
                                            <input type="file" name="memorandumdocument" id="memorandumdocument"  class="form-control-file"/>
                                        </div>
                                    </c:if>
                                </div> 

                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="spc">4.Disciplinary Authority Detail<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${empty pbean.disciplinaryauthority}"> 
                                        <div class="col-lg-9">
                                            <form:hidden path="dahrmsid"/>
                                            <form:hidden path="daspc"/>
                                            <form:hidden path="daoffice"/>
                                            <form:input class="form-control" path="disciplinaryauthority" readonly="true"/>
                                        </div>
                                        <div class="col-lg-1">
                                            <button type="button" class="btn btn-primary" data-toggle="modal" onclick="selectauthtype('A')" data-target="#authorityModal">
                                                <span class="glyphicon glyphicon-search"></span> Search
                                            </button>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.disciplinaryauthority}">
                                        <div class="col-lg-9">
                                            ${pbean.disciplinaryauthority}
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="row" id="sendForApproval" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <a href="forwardRule15DiscProceding.htm?daId=${pbean.daId}" class="btn btn-primary">Forward to Disciplinary Authority</a>
                            </div>
                        </div>
                    </div>
                    <c:if test="${pbean.isAuthorityApprove eq 'Y'}">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                Whether Served to Delinquent Officer
                            </div>
                            <c:if test="${pbean.hasSendNoticetoDO ne 'Y'}">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2"> 
                                        <input type="radio" id="ShowcauseIssued" name="showcause" value="AlreadyServed" onclick="radioClickedForFirstShowCause()"> <b>Served</b>
                                    </div>
                                    <div class="col-lg-2">
                                        <input type="radio" id="ShowcauseNotIssued" name="showcause" value="NotServed" onclick="radioClickedForFirstShowCause()">  <b>Not Served</b>
                                    </div>  
                                </div>
                            </c:if>
                            <div class="panel-body" id="alreadyservedDiv" >
                                <div class="row" style="margin-bottom: 7px;"> 
                                    <div class="col-lg-2">
                                        <label> 1.Date Of Receipt<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${empty pbean.showCauseOrdDt}">
                                        <div class="col-lg-2">
                                            <div class="input-group date">
                                                <form:input class="form-control datepickerclass"  path="showCauseOrdDt" readonly="true"/> 
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>                                
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.showCauseOrdDt}">
                                        <div class="col-lg-2">
                                            ${pbean.showCauseOrdDt}
                                        </div>
                                    </c:if>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">  
                                    <div class="col-lg-2">
                                        <label for="document">2.Document(Served Copy)<span style="color: red">*</span><br>
                                            <span style="color: #c83939;font-style: italic;">(Maximum File size is 20 mb per attachment, Only pdf/zip file can be uploaded.)</span>
                                        </label>
                                    </div>
                                    <c:if test="${not empty pbean.firstshowcauseoriginalFileName}">
                                        <a href="downloadFirstShowCauseAttachment.htm?daId=${pbean.daId}">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.firstshowcauseoriginalFileName}</a>
                                        </c:if>
                                        <c:if test="${empty pbean.firstshowcauseoriginalFileName}">
                                        <div class="form-group row">                            
                                            <input type="file" name="firstshowcausedocument" id="firstshowcausedocument"  class="form-control-file"/>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="row" id="sendshowcauseDiv" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <input type ="button" value="Send Show Cause" />
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${pbean.hasSendNoticetoDO eq 'Y'}"> 
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                Written Statement By Delinquent Officer
                            </div>
                            <div class="panel-body">
                                <div class="row" style="margin-bottom: 7px;"> 
                                    <div class="col-lg-2">
                                        <label> 1.Date Of Receipt</label>
                                    </div>
                                    <c:if test="${empty pbean.writtenStatemenyByDOOnDt}"> 
                                        <div class="col-lg-2">
                                            <div class="input-group date">
                                                <form:input class="form-control datepickerclass"  path="writtenStatemenyByDOOnDt" readonly="true"/> 
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>                                
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.writtenStatemenyByDOOnDt}">
                                        <div class="col-lg-2">
                                            ${pbean.writtenStatemenyByDOOnDt}
                                        </div>
                                    </c:if>
                                </div>


                                <div class="row" style="margin-bottom: 7px;">  
                                    <div class="col-lg-2">
                                        <label for="document">2.Document(Defence Statement  Copy)<span style="color: red">*</span><br>
                                            <span style="color: #c83939;font-style: italic;">(Maximum File size is 20 mb per attachment, Only pdf/zip file can be uploaded.)</span>
                                        </label>
                                    </div>

                                    <c:if test="${not empty pbean.defenceByDOoriginalFileName}">
                                        <a href="downloadDefenceStatementByDelinquentOfficerAttachment.htm?dadid=${pbean.dadid}">
                                            <span class="glyphicon glyphicon-paperclip"></span>${pbean.defenceByDOoriginalFileName}</a>
                                        </c:if>
                                        <c:if test="${empty pbean.defenceByDOoriginalFileName}">
                                        <div class="form-group row">                            
                                            <input type="file" name="defenceByDOdocument" id="defenceByDOdocument"  class="form-control-file"/>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${pbean.hasReplyByDelinquentOfficer eq 'Y'}">
                        <c:if test="${pbean.hasIoAppointed ne 'Y'}"> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2"> 
                                    <input type="radio" id="InquiryOnDefenceRemark" name="defenceremarkByDO" value="Inquiry" onclick="radioClickedondefenceremark()"><b>Inquiry</b>
                                </div>
                                <div class="col-lg-2">
                                    <input type="radio" id="MinorPenmaltyOnDefenceRemark" name="defenceremarkByDO" value="MinorPenalty" onclick="radioClickedondefenceremark()"><b>Minor Penalty</b>
                                </div> 
                                <div class="col-lg-2">
                                    <input type="radio" id="MajorPenmaltyOnDefenceRemark" name="defenceremarkByDO" value="Exoneration" onclick="radioClickedondefenceremark()"><b>Exoneration</b>
                                </div> 

                            </div>
                        </c:if> 

                        <div class="panel panel-default" id="appointmentofIoPODetail">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    I.O(Inquiry Officer),P.O(Presenting Officer)/M.O(Marshalling Officer) and A.P.O(Additional Presenting Officer) Detail
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label>1.Office Order No<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${empty pbean.ioAppoinmentOrdNo}">
                                        <div class="col-lg-2">
                                            <form:input class="form-control" path="ioAppoinmentOrdNo" maxlength="51"/> 
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.ioAppoinmentOrdNo}">
                                        <div class="col-lg-2">
                                            ${pbean.ioAppoinmentOrdNo}
                                        </div>
                                    </c:if>

                                    <div class="col-lg-2">
                                        <label> 2.Office Order Date<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${empty pbean.ioAppoinmentOrdDt}"> 
                                        <div class="col-lg-2">
                                            <div class="input-group date">
                                                <form:input class="form-control datepickerclass" path="ioAppoinmentOrdDt" readonly="true"/> 
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>                                
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.ioAppoinmentOrdDt}">
                                        <div class="col-lg-2">
                                            ${pbean.ioAppoinmentOrdDt}
                                        </div>
                                    </c:if>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="spc">3. Details of Inquiry Officer<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${empty pbean.inquiryauthority}">
                                        <div class="col-lg-9">
                                            <form:hidden path="ioEmpHrmsId"/>
                                            <form:hidden path="ioEmpSPC"/>
                                            <form:input class="form-control" path="inquiryauthority" readonly="true"/>
                                        </div>
                                        <div class="col-lg-1">
                                            <button type="button" class="btn btn-primary" data-toggle="modal" onclick="selectauthtype('I')" data-target="#authorityModal">
                                                <span class="glyphicon glyphicon-search"></span> Search
                                            </button>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.inquiryauthority}">
                                        <div class="col-lg-9">
                                            ${pbean.inquiryauthority}
                                        </div>
                                    </c:if>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="spc">4. Details of Presenting Officer<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${empty pbean.presentingauthority}">
                                        <div class="col-lg-9">
                                            <form:hidden path="poHrmsId"/>
                                            <form:hidden path="poSPC"/>
                                            <form:input class="form-control" path="presentingauthority" readonly="true"/>
                                        </div>
                                        <div class="col-lg-1">
                                            <button type="button" class="btn btn-primary" data-toggle="modal" onclick="selectauthtype('P')" data-target="#authorityModal">
                                                <span class="glyphicon glyphicon-search"></span> Search
                                            </button>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.presentingauthority}">
                                        <div class="col-lg-9">
                                            ${pbean.presentingauthority}
                                        </div>
                                    </c:if>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="spc">5. Details of Additional Presenting Officer</label>
                                    </div>
                                    <c:if test="${empty pbean.additionalpresentingauthority}">
                                        <div class="col-lg-9">
                                            <form:hidden path="apoHrmsId"/>
                                            <form:hidden path="apoSPC"/>
                                            <form:input class="form-control" path="additionalpresentingauthority" readonly="true"/>
                                        </div>
                                        <div class="col-lg-1">
                                            <button type="button" class="btn btn-primary" data-toggle="modal" onclick="selectauthtype('AP')" data-target="#authorityModal">
                                                <span class="glyphicon glyphicon-search"></span> Search
                                            </button>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.additionalpresentingauthority}">
                                        <div class="col-lg-9">
                                            ${pbean.additionalpresentingauthority}
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <c:if test="${pbean.hasIoAppointed ne 'Y'}">
                            <!--Minor penalty-->
                            <div class="panel panel-default" id="MinorPenaltyONpresentationOfDO">
                                <div class="panel-heading">
                                    OPSC Consultation
                                </div>
                                <div class="panel-body">
                                    <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                        <div class="col-lg-2">
                                            <label>1.Letter No</label>
                                        </div>
                                        <c:if test="${not empty pbean.consultationOrdnoOnRepresentationOfDoOnIoReport}">
                                            ${pbean.consultationOrdnoOnRepresentationOfDoOnIoReport}
                                        </c:if>
                                        <c:if test="${empty pbean.consultationOrdnoOnRepresentationOfDoOnIoReport}">
                                            <div class="col-lg-2">
                                                <form:input class="form-control" path="consultationOrdnoOnRepresentationOfDoOnIoReport" maxlength="51"/> 
                                            </div>
                                        </c:if>
                                        <div class="col-lg-2">
                                            <label> 2.Date</label>
                                        </div>
                                        <c:if test="${empty pbean.consultationOrddateOnRepresentationOfDoOnIoReport}">
                                            <div class="col-lg-2">
                                                <div class="input-group date" id="processDate">
                                                    <form:input class="form-control datepickerclass" path="consultationOrddateOnRepresentationOfDoOnIoReport" readonly="true"/> 
                                                    <span class="input-group-addon">
                                                        <span class="glyphicon glyphicon-time"></span>
                                                    </span>
                                                </div>                                
                                            </div>
                                        </c:if>
                                        <c:if test="${not empty pbean.consultationOrddateOnRepresentationOfDoOnIoReport}">
                                            <div class="col-lg-2">
                                                ${pbean.consultationOrddateOnRepresentationOfDoOnIoReport}
                                            </div>
                                        </c:if>
                                    </div>

                                    <div class="col-lg-2">
                                        <label for="document">3.Document(document For OPSC Consultation)</label>
                                    </div>
                                    <c:if test="${not empty pbean.consultationOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                        <a href="" class="btn btn-default">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.firstshowcauseoriginalFileName}</a>
                                        </c:if>
                                        <c:if test="${empty pbean.consultationOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                        <div class="form-group row">                            
                                            <input type="file" name="consultationdocumentOnRepresentationOfDoOnIoReport" id="consultationdocumentOnRepresentationOfDoOnIoReport"  class="form-control-file"/>
                                        </div>
                                    </c:if>

                                </div>

                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        OPSC Concurrence
                                    </div>
                                    <div class="panel-body">
                                        <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                            <div class="col-lg-2">
                                                <label>1.Letter No</label>
                                            </div>
                                            <c:if test="${not empty pbean.concurranceOrdnoOnRepresentationOfDoOnIoReport}">
                                                ${pbean.concurranceOrdnoOnRepresentationOfDoOnIoReport}
                                            </c:if>
                                            <c:if test="${empty pbean.concurranceOrdnoOnRepresentationOfDoOnIoReport}">
                                                <div class="col-lg-2">
                                                    <form:input class="form-control" path="concurranceOrdnoOnRepresentationOfDoOnIoReport" maxlength="51"/> 
                                                </div>
                                            </c:if>
                                            <div class="col-lg-2">
                                                <label> 2.Date</label>
                                            </div>
                                            <c:if test="${empty pbean.concurranceOrddateOnRepresentationOfDoOnIoReport}"> 
                                                <div class="col-lg-2">
                                                    <div class="input-group date" id="processDate">
                                                        <form:input class="form-control datepickerclass" path="concurranceOrddateOnRepresentationOfDoOnIoReport" readonly="true"/> 
                                                        <span class="input-group-addon">
                                                            <span class="glyphicon glyphicon-time"></span>
                                                        </span>
                                                    </div>                                
                                                </div>
                                            </c:if>
                                            <c:if test="${not empty pbean.concurranceOrddateOnRepresentationOfDoOnIoReport}">
                                                <div class="col-lg-2">
                                                    ${pbean.concurranceOrddateOnRepresentationOfDoOnIoReport}
                                                </div>
                                            </c:if> 
                                        </div>
                                        <div class="panel-body">
                                            <div class="row" style="margin-bottom: 7px;">  
                                                <div class="col-lg-2">
                                                    <label for="document">3.Document(document For OPSC Concurrence)</label>
                                                </div>

                                                <c:if test="${not empty pbean.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                    <a href="" class="btn btn-default">
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${pbean.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport}</a>
                                                    </c:if>
                                                    <c:if test="${empty pbean.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                    <div class="form-group row">                            
                                                        <input type="file" name="concurrancedocumentOnRepresentationOfDoOnIoReport" id="concurrancedocumentOnRepresentationOfDoOnIoReport"  class="form-control-file"/>
                                                    </div> 
                                                </c:if>


                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        Final Order
                                    </div>
                                    <div class="row" style="margin-bottom: 7px;">
                                        <div class="col-lg-2">
                                            <label>1.Order No<span style="color: red">*</span></label>
                                        </div>

                                        <c:if test="${empty pbean.finalOrdnoOnRepresentationOfDoOnIoReport}">
                                            <div class="col-lg-2">
                                                <form:input class="form-control" path="finalOrdnoOnRepresentationOfDoOnIoReport" maxlength="51"/> 
                                            </div>
                                        </c:if>
                                        <c:if test="${not empty pbean.finalOrdnoOnRepresentationOfDoOnIoReport}">
                                            <div class="col-lg-2">
                                                ${pbean.finalOrdnoOnRepresentationOfDoOnIoReport}
                                            </div>
                                        </c:if>
                                        <div class="col-lg-2">
                                            <label> 2.Date<span style="color: red">*</span></label>
                                        </div>
                                        <c:if test="${empty pbean.finalOrddateOnRepresentationOfDoOnIoReport}"> 
                                            <div class="col-lg-2">
                                                <div class="input-group date" id="processDate">
                                                    <form:input class="form-control datepickerclass" path="finalOrddateOnRepresentationOfDoOnIoReport" readonly="true"/> 
                                                    <span class="input-group-addon">
                                                        <span class="glyphicon glyphicon-time"></span>
                                                    </span>
                                                </div>                                
                                            </div>
                                        </c:if>
                                        <c:if test="${not empty pbean.finalOrddateOnRepresentationOfDoOnIoReport}">
                                            <div class="col-lg-2">
                                                ${pbean.finalOrddateOnRepresentationOfDoOnIoReport}
                                            </div>
                                        </c:if>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row" style="margin-bottom: 7px;">  
                                            <div class="col-lg-2">
                                                <label for="document">3.Document(document For Final Order)<span style="color: red">*</span></label>
                                            </div>

                                            <c:if test="${not empty pbean.finalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                <a href="">
                                                    <span class="glyphicon glyphicon-paperclip"></span> ${pbean.finalOriginalfilenameOnRepresentationOfDoOnIoReport}</a>
                                                </c:if>
                                                <c:if test="${empty pbean.finalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                <div class="form-group row">                            
                                                    <input type="file" name="finaldocumentOnRepresentationOfDoOnIoReport" id="finaldocumentOnRepresentationOfDoOnIoReport"  class="form-control-file"/>
                                                </div> 
                                            </c:if>


                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>             


                        <div class="panel panel-default" id="exanaurationOnRepresentationOfDO">
                            <div class="panel-heading">
                                Final Order
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Order No<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.exanaurationfinalOrdnumberOnopscConcurrance}">
                                    <div class="col-lg-2">
                                        <form:input class="form-control" path="exanaurationfinalOrdnumberOnopscConcurrance" maxlength="51"/> 
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.exanaurationfinalOrdnumberOnopscConcurrance}">
                                    <div class="col-lg-2">
                                        ${pbean.exanaurationfinalOrdnumberOnopscConcurrance} 
                                    </div>
                                </c:if>
                                <div class="col-lg-2">
                                    <label> 2.Date<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.exanaurationfinalOrddateOnopscConcurrance}"> 
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <form:input class="form-control datepickerclass" path="exanaurationfinalOrddateOnopscConcurrance" readonly="true"/> 
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.exanaurationfinalOrddateOnopscConcurrance}">
                                    <div class="col-lg-2">
                                        ${pbean.exanaurationfinalOrddateOnopscConcurrance}
                                    </div>
                                </c:if> 
                            </div>
                            <div class="panel-body">
                                <div class="row" style="margin-bottom: 7px;">  
                                    <div class="col-lg-2">
                                        <label for="document">3.Document(document For Final Order)<span style="color: red">*</span></label>
                                    </div>

                                    <c:if test="${not empty pbean.exanaurationfinalOriginalfilenameOnopscConcurrance}">
                                        <a href="">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.exanaurationfinalOriginalfilenameOnopscConcurrance}</a>
                                        </c:if>
                                        <c:if test="${empty pbean.exanaurationfinalOriginalfilenameOnopscConcurrance}">
                                        <div class="form-group row">  
                                            <input type="file" name="exanaurationfinaldocumentOnopscConcurrance" id="exanaurationfinaldocumentOnopscConcurrance"  class="form-control-file"/>
                                        </div> 
                                    </c:if>


                                </div>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${pbean.hasIoAppointed eq 'Y'}">
                        <c:if test="${pbean.hasIoRemarks ne 'Y'}">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2"> 
                                    <input type="radio" id="IoReportSubmit" name="isIoReportSubmitted" value="IoReportSubmit" onclick="radioClickedforisIoReportSubmitted()"> <b>Report Of IO Submitted</b>
                                </div>
                                <div class="col-lg-2">
                                    <input type="radio" id="IoReportNotSubmit" name="isIoReportSubmitted" value="IoReportNotSubmit" onclick="radioClickedforisIoReportSubmitted()"> <b>Report Of IO Not Submitted</b>
                                </div> 
                            </div>
                        </c:if>
                        <div class="panel panel-default" id="ioandPoandAporemarksDiv">
                            <div class="panel-heading">
                                I.O'S Findings
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Letter Number<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.ioRemarksOrdNo}">
                                    <div class="col-lg-2">
                                        <form:input class="form-control" path="ioRemarksOrdNo" maxlength="51"/> 
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.ioRemarksOrdNo}">
                                    <div class="col-lg-2">
                                        ${pbean.ioRemarksOrdNo}
                                    </div>
                                </c:if>
                                <div class="col-lg-2">
                                    <label>2.Date<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.ioRemarksOrdDt}">
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <form:input class="form-control datepickerclass" path="ioRemarksOrdDt" readonly="true"/> 
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.ioRemarksOrdDt}">
                                    <div class="col-lg-2">
                                        ${pbean.ioRemarksOrdDt}
                                    </div>
                                </c:if> 
                            </div>
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label for="document">3.Document(IO's Report)<span style="color: red">*</span><br>
                                        <span style="color: #c83939;font-style: italic;">(Maximum File size is 20 mb per attachment, Only pdf/zip file can be uploaded.)</span>
                                    </label>
                                </div>
                                <c:if test="${not empty pbean.remarksByIOoriginalFileName}">
                                    <a href="downloadIoRemarksDetailOngoingDPAttachment.htm?daId=${pbean.daId}">
                                        <span class="glyphicon glyphicon-paperclip"></span>${pbean.remarksByIOoriginalFileName}</a>
                                    </c:if>
                                    <c:if test="${empty pbean.remarksByIOoriginalFileName}">
                                    <div class="form-group row">                            
                                        <input type="file" name="remarksByIOdocument" id="remarksByIOdocument"  class="form-control-file"/>
                                    </div> 
                                </c:if>
                            </div>
                        </div>
                        <div class="row" id="sendIoPoApo" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <a href="" class="btn btn-primary">Send to IO /PO/APO</a>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${pbean.hasIoRemarks eq 'Y'}">
                        <c:if test="${pbean.hasSendSecondNotice ne 'Y'}">
                            <div class="row" style="margin-bottom: 7px;" id="ioremarksreviewDiv">
                                <div class="col-lg-2"> 
                                    <input type="radio" id="ioremarksAccepted" name="ioremarksreviewcheck" value="ioremarksAccepted" onclick="radioClickedforIoRemarksReview()"><b>Accepted</b>
                                </div>
                                <div class="col-lg-2">
                                    <input type="radio" id="ioremarksNotRejected" name="ioremarksreviewcheck" value="ioremarksRejected" onclick="radioClickedforIoRemarksReview()"><b>Not Accepted</b>
                                </div>   
                            </div>
                        </c:if>
                        <div class="panel panel-default" id="serveToDelinquentOnIoRemarks">
                            <div class="panel-heading">
                                Notice to the Delinquent Officer
                            </div>
                            <div class="form-group row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1. Letter No<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.ordNoForNoticetoDOOnIoRemark}">
                                    <div class="col-lg-2">
                                        <form:input class="form-control" path="ordNoForNoticetoDOOnIoRemark" maxlength="51"/> 
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.ordNoForNoticetoDOOnIoRemark}">
                                    <div class="col-lg-2">
                                        ${pbean.ordNoForNoticetoDOOnIoRemark}
                                    </div>
                                </c:if>
                                <div class="col-lg-2">
                                    <label> 2. Date<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.ordDtForNoticeOnTODOIoRemark}">
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <form:input class="form-control datepickerclass"  path="ordDtForNoticeOnTODOIoRemark" readonly="true"/> 
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.ordDtForNoticeOnTODOIoRemark}">
                                    <div class="col-lg-2">
                                        ${pbean.ordDtForNoticeOnTODOIoRemark}
                                    </div>
                                </c:if>
                            </div>
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label for="document">3. Document(Notice to Delinquent)<span style="color: red">*</span><br>
                                        <span style="color: #c83939;font-style: italic;">(Maximum File size is 20 mb per attachment, Only pdf/zip file can be uploaded.)</span>
                                    </label>
                                </div>
                                <c:if test="${not empty pbean.noticetoDOOnIoRemarkoriginalFileName}">
                                    <a href="downloadSecondShowCauseDetailsAttachment.htm?daId=${pbean.daId}">
                                        <span class="glyphicon glyphicon-paperclip"></span>${pbean.noticetoDOOnIoRemarkoriginalFileName}</a>
                                    </c:if>
                                    <c:if test="${empty pbean.noticetoDOOnIoRemarkoriginalFileName}">
                                    <div class="form-group row">                            
                                        <input type="file" name="noticetoDOOnIoRemarkdocument" id="noticetoDOOnIoRemarkdocument"  class="form-control-file"/>
                                    </div> 
                                </c:if>
                            </div>
                            <c:if test="${pbean.hasSendSecondNotice ne 'Y'}">
                                <div class="row" style="margin-bottom: 7px;">  
                                    <div class="col-lg-2">
                                        <input type="radio" id="alreadyServed" name="serveDelinquentOnIoRemarks" value="AlreadyServedDelinquentOnIoRemarks" onclick="radioClickedForserveDelinquentOnIoRemarks()"> <b>Served</b>
                                    </div>  
                                    <div class="col-lg-2">
                                        <input type="radio" id="notServed" name="serveDelinquentOnIoRemarks" value="NotServedDelinquentOnIoRemarks" onclick="radioClickedForserveDelinquentOnIoRemarks()"> <b>Not Served</b>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        <div class="row" id="sendserveDelinquentOnIoRemarks" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <a href="" class="btn btn-primary">Send To Delinquent</a>
                            </div>
                        </div>
                        <div class="panel panel-default" id="serveDelinquentOnIoRemarksDiv">                            
                            <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                <div class="col-lg-2">
                                    <label>Date Of Service<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.secondshowCauseOrdDt}">
                                    <div class="col-lg-2">
                                        <div class="input-group date">
                                            <form:input class="form-control datepickerclass" path="secondshowCauseOrdDt" readonly="true"/> 
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.secondshowCauseOrdDt}">
                                    <div class="col-lg-2">
                                        ${pbean.secondshowCauseOrdDt}
                                    </div>
                                </c:if>
                            </div>
                        </div>
                        <div class="panel panel-default" id="reinquiryonDelinquentOfficerDiv" >
                            <div class="panel-heading">
                                Reinquiry against Delinquent Officer
                            </div>


                            <div class="form-group row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Office Order No<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.showCauseOrdNo}">
                                    <div class="col-lg-2">
                                        <form:input class="form-control" path="showCauseOrdNo" maxlength="51"/> 
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.showCauseOrdNo}">
                                    <div class="col-lg-2">
                                        ${pbean.showCauseOrdNo}>
                                    </div>
                                </c:if>
                                <div class="col-lg-2">
                                    <label> 2.Office Order Date<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.showCauseOrdDt}">
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <form:input class="form-control datepickerclass"  path="showCauseOrdDt" readonly="true"/> 
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </c:if>
                                <c:if test="${empty pbean.showCauseOrdDt}">
                                    <div class="col-lg-2">
                                        ${pbean.showCauseOrdDt}
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${pbean.hasSendSecondNotice eq 'Y'}">
                        <div class="panel panel-default" id="representationOfDelinquentOnIoRemarks">
                            <div class="panel-heading">
                                Representation  By Delinquent Officer
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Letter No</label>
                                </div>
                                <c:if test="${empty pbean.doRepresentationOnsecondshowCauseOrdNo}">
                                    <div class="col-lg-2">
                                        <form:input class="form-control" path="doRepresentationOnsecondshowCauseOrdNo" maxlength="51"/> 
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.doRepresentationOnsecondshowCauseOrdNo}">
                                    <div class="col-lg-2">
                                        ${pbean.doRepresentationOnsecondshowCauseOrdNo}
                                    </div>
                                </c:if>
                                <div class="col-lg-2">
                                    <label> 2.Date Of Receipt<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.doRepresentationOnsecondshowCauseOrdDt}">
                                    <div class="col-lg-2">
                                        <div class="input-group date">
                                            <form:input class="form-control datepickerclass" path="doRepresentationOnsecondshowCauseOrdDt" readonly="true"/> 
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.doRepresentationOnsecondshowCauseOrdDt}">
                                    <div class="col-lg-2">
                                        ${pbean.doRepresentationOnsecondshowCauseOrdDt}
                                        <form:hidden path="doRepresentationOnsecondshowCauseOrdDt"/> 
                                    </div>
                                </c:if>
                            </div>
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label for="document">3.Document (Representation against IO's Report)<span style="color: red">*</span><br>
                                        <span style="color: #c83939;font-style: italic;">(Maximum File size is 20 mb per attachment, Only pdf/zip file can be uploaded.)</span>
                                    </label>
                                </div>
                                <c:if test="${not empty pbean.secondshowcauseoriginalFileNameondoRepresentation}">
                                    <a href="downloadAttachedFileForRepresentationOfDOOnSecondShowCause.htm?daId=${pbean.daId}">
                                        <span class="glyphicon glyphicon-paperclip"></span>${pbean.secondshowcauseoriginalFileNameondoRepresentation}</a>
                                        <form:hidden path="doRepresentationOnsecondshowCauseOrdNo" />
                                        <form:hidden path="secondshowcauseoriginalFileNameondoRepresentation" />
                                    </c:if>
                                    <c:if test="${empty pbean.secondshowcauseoriginalFileNameondoRepresentation}">
                                    <div class="form-group row">                            
                                        <input type="file" name="secondshowcausedocumentondoRepresentation" id="secondshowcausedocumentondoRepresentation"  class="form-control-file"/>
                                    </div> 
                                </c:if>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${pbean.hasDoRepresentOnsecondshowCause eq 'Y'}">
                        <div class="panel panel-default" id="considerationByDA">
                            <div class="panel-heading">
                                Consideration By the Disciplinary Authority
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <input type="radio" id="punishmentOnConsiderationOfDORepresentationOnIoReport" name="punishmentProposedOnRepresentationOfDoOnIoReport" value="punishmentOnRepresentationOfDOOnIoReport" onclick="radioClickedforPunishmentOnpresentationOfDOOnIoReport()"><b>Punishment Proposed</b>
                                </div>
                                <div class="col-lg-2">
                                    <input type="radio" id="exanarutionOnConsiderationOfDORepresentationOnIoReport" name="punishmentProposedOnRepresentationOfDoOnIoReport" value="exanarutionOnRepresentationOfDOOnIoReport" onclick="radioClickedforPunishmentOnpresentationOfDOOnIoReport()"><b>Exoneration</b>
                                </div> 
                            </div>
                            <div class="panel panel-default" id="exanaurationDetailONpresentationOfDOonIoReport">
                                <div class="panel-heading">
                                    Final Order
                                </div>
                                <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                    <div class="col-lg-2">
                                        <label>1.Order No<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${empty pbean.exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport}">
                                        <div class="col-lg-2">
                                            <form:input class="form-control" path="exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport" maxlength="51"/> 
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport}">
                                        <div class="col-lg-2">
                                            ${pbean.exanaurationfinalOrdnoOnRepresentationOfDoOnIoReport}
                                        </div>
                                    </c:if>
                                    <div class="col-lg-2">
                                        <label> 2.Date<span style="color: red">*</span></label>
                                    </div>
                                    <c:if test="${empty pbean.exanaurationfinalOrddateOnRepresentationOfDoOnIoReport}">
                                        <div class="col-lg-2">
                                            <div class="input-group date">
                                                <form:input class="form-control datepickerclass" path="exanaurationfinalOrddateOnRepresentationOfDoOnIoReport" readonly="true"/> 
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>                                
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty pbean.exanaurationfinalOrddateOnRepresentationOfDoOnIoReport}">
                                        <div class="col-lg-2">
                                            ${pbean.exanaurationfinalOrddateOnRepresentationOfDoOnIoReport}
                                            <form:hidden path="exanaurationfinalOrddateOnRepresentationOfDoOnIoReport"/> 
                                        </div>
                                    </c:if>
                                </div>
                                <div class="panel-body">
                                    <div class="row" style="margin-bottom: 7px;">  
                                        <div class="col-lg-2">
                                            <label for="document">3.Document(document For Final Order)<span style="color: red">*</span></label>
                                        </div>
                                        <c:if test="${not empty pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                            <a href="">
                                                <span class="glyphicon glyphicon-paperclip"></span> ${pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}
                                            </a>
                                        </c:if>
                                        <c:if test="${empty pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                            <div class="form-group row">                            
                                                <input type="file" name="exanaurationfinaldocumentOnRepresentationOfDoOnIoReport" id="exanaurationfinaldocumentOnRepresentationOfDoOnIoReport"  class="form-control-file"/>
                                            </div> 
                                        </c:if>
                                    </div>
                                </div>

                            </div>
                            <c:if test="${not empty pbean.doRepresentationOnsecondshowCauseOrdDt && not empty pbean.secondshowcauseoriginalFileNameondoRepresentation}">
                                <div class="panel panel-default" id="PunishmentDetailONpresentationOfDOonIoReport">
                                    <table class="table table-bordered">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Punishment Awarded</th>
                                                <th>With Effect Date</th>
                                                <th>Till Date</th>
                                                <th>Narration</th> 
                                                <th>Action</th>
                                            </tr>
                                        </thead>
                                        <tbody id="punishmenttbl">
                                            <c:forEach items="${punishdetailsListProposed}" var="punishdetails" varStatus="cnt">
                                                <tr>
                                                    <td>${cnt.index+1}</td>
                                                    <td>${punishdetails.punishmenttypedesc}</td>
                                                    <td>${punishdetails.wefdate}</td>
                                                    <td>${punishdetails.tilldate}</td>
                                                    <td>${punishdetails.narration}</td>  
                                                    <td>
                                                        <a class="btn btn-primary" href="javascript:void(0)" onclick="editPunishDetails('${punishdetails.pentype}',${punishdetails.punishmentdetailsid})"><span class="glyphicon glyphicon-pencil"></span> Edit</a> | 
                                                        <a class="btn btn-primary" href="javascript:void(0)" onclick="deletePunishDetails(this, '${punishdetails.pentype}',${punishdetails.punishmentdetailsid})"><span class="glyphicon glyphicon-remove"></span> Delete</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>

                                    <c:if test="${pbean.hasSendthirdshowCause ne 'Y'}">
                                        <div class="panel panel-default" align="center">
                                            <div class="btn-group">
                                                <button type="button" class="btn btn-primary" onclick="showTab('01', 'P')">Fine</button>
                                                <button type="button" class="btn btn-primary" onclick="showTab('09', 'P')">Recovery</button>
                                                <button type="button" class="btn btn-primary" onclick="showTab('02', 'P')">Withholding Increment</button>
                                                <button type="button" class="btn btn-primary" onclick="showTab('03', 'P')">Withholding Promotion</button>
                                                <button type="button" class="btn btn-primary" onclick="showTab('04', 'P')">Suspension</button>
                                                <button type="button" class="btn btn-primary" onclick="showTab('05', 'P')">Reduction to Lower Service</button>
                                                <button type="button" class="btn btn-primary" onclick="showTab('06', 'P')">Compulsory Retirement</button>
                                                <button type="button" class="btn btn-primary" onclick="showTab('07', 'P')">Removal from Service</button>
                                                <button type="button" class="btn btn-primary" onclick="showTab('08', 'P')">Dismissal from Service</button> 
                                            </div>
                                        </div>
                                    </c:if>
                                </div> 
                            </c:if>

                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <input type="hidden" name="punishmentdetailsid" id="punishmentdetailsid" value="0"/>
                        </div>
                    </c:if>
                    <div class="panel panel-default" id="thirdNoticeToDelinquentForPunishmentProposed">
                        <div class="panel-heading">
                            Notice Regarding Proposed Punishment
                        </div>
                        <div class="form-group row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>1.Letter No<span style="color: red">*</span></label>
                            </div>
                            <c:if test="${empty pbean.ordNoForthirdNoticetoDOOnForPunishment}">
                                <div class="col-lg-2">
                                    <form:input class="form-control" path="ordNoForthirdNoticetoDOOnForPunishment" maxlength="51"/> 
                                </div>
                            </c:if>
                            <c:if test="${not empty pbean.ordNoForthirdNoticetoDOOnForPunishment}">
                                <div class="col-lg-2">
                                    ${pbean.ordNoForthirdNoticetoDOOnForPunishment}
                                </div>
                            </c:if>
                            <div class="col-lg-2">
                                <label> 2.Date<span style="color: red">*</span></label>
                            </div>
                            <c:if test="${empty pbean.ordDtForthirdNoticetoDOOnForPunishment}">
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass"  path="ordDtForthirdNoticetoDOOnForPunishment" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </c:if>
                            <c:if test="${not empty pbean.ordDtForthirdNoticetoDOOnForPunishment}">
                                <div class="col-lg-2">
                                    ${pbean.ordDtForthirdNoticetoDOOnForPunishment}
                                </div>
                            </c:if>
                        </div>
                        <div class="row" style="margin-bottom: 7px;"> 
                            <div class="col-lg-2">
                                <label for="document">3.Document(document For notice to delinquent)<span style="color: red">*</span><br>
                                    <span style="color: #c83939;font-style: italic;">(Maximum File size is 20 mb per attachment, Only pdf/zip file can be uploaded.)</span>
                                </label>
                            </div>
                            <c:if test="${not empty pbean.thirdNoticetoDOOnForPunishmentorgFileName}">
                                <a href="downloadAttachedFileForThirdShowCauseDetails.htm?daId=${pbean.daId}">
                                    <span class="glyphicon glyphicon-paperclip"></span>${pbean.thirdNoticetoDOOnForPunishmentorgFileName}
                                </a>
                            </c:if>
                            <c:if test="${empty pbean.thirdNoticetoDOOnForPunishmentorgFileName}">
                                <div class="form-group row">                            
                                    <input type="file" name="thirdNoticetoDOOnForPunishmentdocument" id="thirdNoticetoDOOnForPunishmentdocument"  class="form-control-file"/>
                                </div> 
                            </c:if>
                        </div>
                        <c:if test="${pbean.hasSendthirdshowCause ne 'Y'}">
                            <div class="row" style="margin-bottom: 7px;">  
                                <div class="col-lg-2">
                                    <input type="radio" id="alreadyServed" name="thirdNoticeToDelinquentPunishmentProposed" value="AlreadyServedDelinquentthirdNotice" onclick="radioClickedForthirdNoticeToDelinquentForPunishmentProposed()"> <b>Served</b>
                                </div>  
                                <div class="col-lg-2">
                                    <input type="radio" id="notServed" name="thirdNoticeToDelinquentPunishmentProposed" value="NotServedDelinquentthirdNotice" onclick="radioClickedForthirdNoticeToDelinquentForPunishmentProposed()"> <b>Not Served</b>
                                </div>
                            </div>
                        </c:if>
                        <div class="row" id="sendDelinquentthirdNotice" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <a href="" class="btn btn-primary">Send To Delinquent</a>
                            </div>
                        </div>
                        <div class="panel panel-default" id="serveDelinquentthirdNoticeDetail">
                            <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                <div class="col-lg-2">
                                    <label>Date Of Service<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date">
                                        <form:input class="form-control datepickerclass" path="thirdshowCauseOrdDt" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>
                        </div>
                    </div>

                    <c:if test="${pbean.hasSendthirdshowCause eq 'Y'}">
                        <div class="panel panel-default" id="representationOfDelinquentthirdShowcause">
                            <div class="panel-heading">
                                Representation Against Proposed Punishment
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label>1.Letter No</label>
                                </div>
                                <c:if test="${empty pbean.thirdshowCauseReplyByDAordNo}">
                                    <div class="col-lg-2">
                                        <form:input class="form-control" path="thirdshowCauseReplyByDAordNo" maxlength="51"/> 
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.thirdshowCauseReplyByDAordNo}">
                                    <div class="col-lg-2">
                                        ${pbean.thirdshowCauseReplyByDAordNo}
                                    </div>
                                </c:if>
                                <div class="col-lg-2">
                                    <label> 2.Date Of Receipt<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty pbean.thirdshowCauseReplyByDAOrdDt}">
                                    <div class="col-lg-2">
                                        <div class="input-group date">
                                            <form:input class="form-control datepickerclass" path="thirdshowCauseReplyByDAOrdDt" readonly="true"/> 
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.thirdshowCauseReplyByDAOrdDt}">
                                    <div class="col-lg-2">
                                        ${pbean.thirdshowCauseReplyByDAOrdDt}
                                    </div>
                                </c:if>

                            </div>
                            <div class="row" style="margin-bottom: 7px;"> 
                                <div class="col-lg-2">
                                    <label for="document">3.Document (Representation against IO's Report)<span style="color: red">*</span><br>
                                        <span style="color: #c83939;font-style: italic;">(Maximum File size is 20 mb per attachment, Only PDF/jpg/jpeg file can be uploaded.)</span>
                                    </label>
                                </div>
                                <c:if test="${not empty pbean.thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                    <a href="downloadAttachedFileForThirdShowReplyByDelinquentOfficer.htm?dadid=${pbean.dadid}">
                                        <span class="glyphicon glyphicon-paperclip"></span>${pbean.thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport}
                                    </a>
                                </c:if>
                                <c:if test="${empty pbean.thirdshowcauseOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                    <div class="form-group row">                            
                                        <input type="file" name="thirdshowcausedocumentondoRepresentation" id="thirdshowcausedocumentondoRepresentation"  class="form-control-file"/>
                                    </div> 
                                </c:if>
                            </div>
                        </div>
                    </c:if>


                    <c:if test="${pbean.hasReplyByDothirdshowCause eq 'Y'}">
                        <div class="panel panel-default" id="considerationByDA">
                            <div class="panel-heading">
                                Consideration By the Government
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <input type="radio" name="ProposedpunishmentOnRepresentationOfDOOnpunishment" value="proposedpunishmentOnRepresentationOfDOOpunishment" onclick="radioClickedforPunishmentOnpresentationOfDOOnpunishment()"><b>Punishment Proposed</b>
                                </div> 
                                <div class="col-lg-2">
                                    <input type="radio"  name="ProposedpunishmentOnRepresentationOfDOOnpunishment" value="exanarutionOnRepresentationOfDOOpunishment" onclick="radioClickedforPunishmentOnpresentationOfDOOnpunishment()"><b>Exoneration</b>
                                </div> 
                            </div>

                            <div class="row" style="margin-bottom: 7px;" id="PunishmentAssignmentBygovt">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Punishment Awarded</th>
                                            <th>With Effect Date</th>
                                            <th>Till Date</th>
                                            <th>Narration</th> 
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody id="punishmentbygovt">
                                        <c:forEach items="${punishdetailsListFinal}" var="punishdetails" varStatus="cnt">
                                            <tr>
                                                <td>${cnt.index+1}</td>
                                                <td>${punishdetails.punishmenttypedesc}</td>
                                                <td>${punishdetails.wefdate}</td>
                                                <td>${punishdetails.tilldate}</td>
                                                <td>${punishdetails.narration}</td>  
                                                <td>
                                                    <a class="btn btn-primary" href="javascript:void(0)" onclick="editPunishDetails('${punishdetails.pentype}',${punishdetails.punishmentdetailsid})"><span class="glyphicon glyphicon-pencil"></span> Edit</a> | 
                                                    <a class="btn btn-primary" href="javascript:void(0)" onclick="deletePunishDetails(this, '${punishdetails.pentype}',${punishdetails.punishmentdetailsid})"><span class="glyphicon glyphicon-remove"></span> Delete</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                                <div class="row" style="margin-bottom: 7px;" align="center">
                                    <div class="btn-group">
                                        <button type="button" class="btn btn-primary" onclick="showTab('01', 'F')">Fine</button>
                                        <button type="button" class="btn btn-primary" onclick="showTab('09', 'F')">Recovery</button>
                                        <button type="button" class="btn btn-primary" onclick="showTab('02', 'F')">Withholding Increment</button>
                                        <button type="button" class="btn btn-primary" onclick="showTab('03', 'F')">Withholding Promotion</button>
                                        <button type="button" class="btn btn-primary" onclick="showTab('04', 'F')">Suspension</button>
                                        <button type="button" class="btn btn-primary" onclick="showTab('05', 'F')">Reduction to Lower Service</button>
                                        <button type="button" class="btn btn-primary" onclick="showTab('06', 'F')">Compulsory Retirement</button>
                                        <button type="button" class="btn btn-primary" onclick="showTab('07', 'F')">Removal from Service</button>
                                        <button type="button" class="btn btn-primary" onclick="showTab('08', 'F')">Dismissal from Service</button> 
                                    </div>
                                </div>
                            </div> 

                            <div class="panel panel-default" id="PunishmentDetailONpresentationOfDOonafterAssignpunishment">
                                <div class="panel-heading">
                                    OPSC Consultation
                                </div>
                                <div class="panel-body">
                                    <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                        <div class="col-lg-2">
                                            <label>1.Letter No</label>
                                        </div>

                                        <c:if test="${empty pbean.consultationOrdnoOnRepresentationOfDoOnIoReport}">
                                            <div class="col-lg-2">
                                                <form:input class="form-control" path="consultationOrdnoOnRepresentationOfDoOnIoReport" maxlength="51"/> 
                                            </div>
                                        </c:if>
                                        <c:if test="${not empty pbean.consultationOrdnoOnRepresentationOfDoOnIoReport}">
                                            <div class="col-lg-2">
                                                ${pbean.consultationOrdnoOnRepresentationOfDoOnIoReport}
                                            </div>
                                        </c:if>
                                        <div class="col-lg-2">
                                            <label> 2.Date</label>
                                        </div>
                                        <div class="col-lg-2">
                                            <div class="input-group date" id="processDate">
                                                <form:input class="form-control datepickerclass" path="consultationOrddateOnRepresentationOfDoOnIoReport" readonly="true"/> 
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>                                
                                        </div>
                                    </div>

                                    <div class="col-lg-2">
                                        <label for="document">3.Document(document For OPSC Consultation)</label>
                                    </div>
                                    <c:if test="${not empty pbean.consultationOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                        <a href="">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.firstshowcauseoriginalFileName}
                                        </a>
                                    </c:if>
                                    <c:if test="${empty pbean.consultationOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                        <div class="form-group row">                            
                                            <input type="file" name="consultationdocumentOnRepresentationOfDoOnIoReport" id="consultationdocumentOnRepresentationOfDoOnIoReport"  class="form-control-file"/>
                                        </div>
                                    </c:if>

                                </div>

                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        OPSC Concurrence
                                    </div>
                                    <div class="panel-body">
                                        <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                            <div class="col-lg-2">
                                                <label>1.Letter No</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <form:input class="form-control" path="concurranceOrdnoOnRepresentationOfDoOnIoReport" maxlength="51"/> 
                                            </div>
                                            <div class="col-lg-2">
                                                <label> 2.Date</label>
                                            </div>
                                            <div class="col-lg-2">
                                                <div class="input-group date" id="processDate">
                                                    <form:input class="form-control datepickerclass" path="concurranceOrddateOnRepresentationOfDoOnIoReport" readonly="true"/> 
                                                    <span class="input-group-addon">
                                                        <span class="glyphicon glyphicon-time"></span>
                                                    </span>
                                                </div>                                
                                            </div>
                                        </div>
                                        <div class="panel-body">
                                            <div class="row" style="margin-bottom: 7px;">  
                                                <div class="col-lg-2">
                                                    <label for="document">3.Document((document For OPSC Concurrence)</label>
                                                </div>

                                                <c:if test="${not empty pbean.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                    <a href="">
                                                        <span class="glyphicon glyphicon-paperclip"></span> ${pbean.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport}
                                                    </a>
                                                </c:if>
                                                <c:if test="${empty pbean.concurranceOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                    <div class="form-group row">                            
                                                        <input type="file" name="concurrancedocumentOnRepresentationOfDoOnIoReport" id="concurrancedocumentOnRepresentationOfDoOnIoReport"  class="form-control-file"/>
                                                    </div> 
                                                </c:if>


                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        Final Order
                                    </div>
                                    <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                        <div class="col-lg-2">
                                            <label>1.Order No<span style="color: red">*</span></label>
                                        </div>
                                        <div class="col-lg-2">
                                            <form:input class="form-control" path="finalOrdnoOnRepresentationOfDoOnIoReport" maxlength="51"/> 
                                        </div>
                                        <div class="col-lg-2">
                                            <label> 2.Date<span style="color: red">*</span></label>
                                        </div>
                                        <div class="col-lg-2">
                                            <div class="input-group date" id="processDate">
                                                <form:input class="form-control datepickerclass" path="finalOrddateOnRepresentationOfDoOnIoReport" readonly="true"/> 
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-time"></span>
                                                </span>
                                            </div>                                
                                        </div>
                                    </div>
                                    <div class="panel-body">
                                        <div class="row" style="margin-bottom: 7px;">  
                                            <div class="col-lg-2">
                                                <label for="document">3.Document(document For Final Order)<span style="color: red">*</span></label>
                                            </div>

                                            <c:if test="${not empty pbean.finalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                <a href="">
                                                    <span class="glyphicon glyphicon-paperclip"></span> ${pbean.finalOriginalfilenameOnRepresentationOfDoOnIoReport}
                                                </a>
                                            </c:if>
                                            <c:if test="${empty pbean.finalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                                <div class="form-group row">                            
                                                    <input type="file" name="finaldocumentOnRepresentationOfDoOnIoReport" id="finaldocumentOnRepresentationOfDoOnIoReport"  class="form-control-file"/>
                                                </div> 
                                            </c:if>


                                        </div>
                                    </div>
                                </div>
                            </div> 

                            <%-- --%>
                        </c:if>
                        <div class="panel panel-default" id="ExanaurationDetailONpresentationOfAfterAssignPunishment">
                            <div class="panel-heading">
                                Final Order
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="serveDelinquentDetail">
                                <div class="col-lg-2">
                                    <label>1.Order No<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input class="form-control" path="exanaurationfinalOrdNumberafterPunishment" maxlength="51"/> 
                                </div>
                                <div class="col-lg-2">
                                    <label> 2.Date<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date" id="processDate">
                                        <form:input class="form-control datepickerclass" path="exanaurationfinalOrddateafterPunishment" readonly="true"/> 
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>
                            <div class="panel-body">
                                <div class="row" style="margin-bottom: 7px;">  
                                    <div class="col-lg-2">
                                        <label for="document">Document(document For Final Order)<span style="color: red">*</span></label>
                                    </div>


                                    <c:if test="${not empty pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                        <a href="">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}
                                        </a>
                                    </c:if>
                                    <c:if test="${empty pbean.exanaurationfinalOriginalfilenameOnRepresentationOfDoOnIoReport}">
                                        <div class="form-group row">                            
                                            <input type="file" name="exanaurationfinaldocumentOnRepresentationOfDoOnIoReport" id="exanaurationfinaldocumentOnRepresentationOfDoOnIoReport"  class="form-control-file"/>
                                        </div> 
                                    </c:if>

                                </div>
                            </div>
                        </div> 
                    </div>
                </div>

                <div class="panel-footer">
                    <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validation()"/>
                    <input type="submit" name="action" value="Back" class="btn btn-default"/>
                </div>
            </div>
        </div>

        <div id="authorityModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Authority Authority</h4>
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

    </form:form>

    <div id="tab01" class="modal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Fine</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-4">With Effect Date:</label>
                            <div class="col-lg-8">
                                <div class="input-group date" id="processDate">
                                    <input type="text" class="form-control datepickertxt" id="finewef" name="finewef" readonly="true">                                    
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>                                    
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Fine Amount:</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="fineamount" name="fineamount" maxlength="11" onkeypress="return onlyInteger(event)">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Recovery Type:</label>
                            <div class="col-sm-8">          
                                <input type="radio" name="recvtype" value="O"/> One Time
                                <input type="radio" name="recvtype" value="I"/> Installment
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">No. of Installments:</label>
                            <div class="col-sm-8">          
                                <input type="text" class="form-control" id="noinstll" name="noinstll" maxlength="2" onkeypress="return onlyInteger(event)">
                            </div>
                        </div>
                    </form>
                    <div class="modal-footer">        
                        <div class="col-sm-offset-2 col-sm-10">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel"/>
                            <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('01', 'P')"/>                                        
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- withholding Increment -->

    <div id="tab02" class="modal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Withholding Increment</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-4">With Effect Date:</label>
                            <div class="col-lg-8">
                                <div class="input-group date" id="processDate">
                                    <input type="text" class="form-control datepickertxt" id="withincwef" name="withincwef" readonly="true"/>                                            
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Scale of Pay:</label>
                            <div class="col-sm-8">          
                                <input type="text" class="form-control" id="scalepay" name="scalepay" maxlength="100">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">No. of Increment to be with hold:</label>
                            <div class="col-sm-8">          
                                <input type="text" class="form-control" id="noofinc" name="noofinc" maxlength="2" onkeypress="return onlyInteger(event)">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Withhold Increment with cumulative effect:</label>
                            <div class="col-sm-8">          
                                <input type="checkbox" id="withincum" name="withincum" value="Y"/>                                        
                            </div>
                        </div>
                    </form>
                    <div class="modal-footer">        
                        <div class="col-sm-offset-2 col-sm-10">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel"/>
                            <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('02', 'P')"/>                                        
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- withholding promotion -->
    <div id="tab03" class="modal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Withholding Promotion</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-4">With Effect Date:</label>
                            <div class="col-lg-8">
                                <div class="input-group date" id="processDate">
                                    <input type="text" class="form-control datepickertxt" id="withpromwef" name="withpromwef" readonly="true"/>                                            
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">To Date:</label>
                            <div class="col-lg-8">
                                <div class="input-group date" id="processDate">
                                    <input type="text" class="form-control datepickertxt" id="promtodate" name="promtodate" readonly="true"/>                                            
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">No. of Promotion to be with hold:</label>
                            <div class="col-sm-8">          
                                <input type="text" class="form-control" id="noofpromotion" name="noofpromotion" maxlength="2"  onkeypress="return onlyInteger(event)"/> 
                                <%-- <form:select path="ruleofproc" class="form-control">
                                     <form:option value="year1" >1</form:option> 
                                     <form:option value="year2" >2</form:option> 
                                     <form:option value="year3" >3</form:option>
                                     <form:option value="year4" >4</form:option>
                                     <form:option value="year5" >5</form:option>
                                     <form:option value="year6" >6</form:option>
                                     <form:option value="year7" >7 </form:option>
                                     <form:option value="year8" >8</form:option>
                                     <form:option value="year9" >9</form:option>
                                 </form:select> --%>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Withhold Promotion with cumulative effect:</label>
                            <div class="col-sm-8">          
                                <input type="checkbox" id="withpromcum" name="withpromcum" value="Y"/>                                        
                            </div>
                        </div>
                    </form>
                    <div class="modal-footer">        
                        <div class="col-sm-offset-2 col-sm-10">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel"/>
                            <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('03', 'P')"/>                                        
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- withholding promotion -->          



    <!-- withholding promotion -->
    <div id="tab04" class="modal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Suspension</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-4">Suspension Order No:</label>
                            <div class="col-sm-8">
                                <select class="form-control" id="suspordno" name="suspordno">
                                    <option value="">Select</option>
                                </select>                                        
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Effect From Date:</label>
                            <div class="col-lg-8">
                                <div class="input-group date" id="processDate">
                                    <input type="text" class="form-control datepickertxt" id="suspwefd" name="suspwefd" readonly="true"/>                                            
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">To Date:</label>
                            <div class="col-lg-8">
                                <div class="input-group date" id="processDate">
                                    <input type="text" class="form-control datepickertxt" id="susptodate" name="susptodate" readonly="true"/>                                            
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>                                
                        <div class="form-group">
                            <label class="control-label col-sm-4">Period to be treated as:</label>
                            <div class="col-sm-8">          
                                <input type="radio" name="suspperiod" value="Y"/> As on Duty 
                                <input type="radio" name="suspperiod" value="N"/> As Such 
                                <input type="radio" name="suspperiod" value="L"/> As on Leave 
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Type of Leave:</label>
                            <div class="col-sm-8">
                                <select class="form-control" id="typeofleave" name="typeofleave">
                                    <option value="">Select</option>
                                </select>
                            </div>
                        </div>
                    </form>
                    <div class="modal-footer">        
                        <div class="col-sm-offset-2 col-sm-10">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel"/>
                            <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('04', 'P')"/>                                        
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Reduction to Lower Service -->
    <div id="tab05" class="modal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Reduction to Lower Service</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-4">With Effect Date:</label>
                            <div class="col-lg-8">
                                <div class="input-group date" id="processDate">
                                    <input type="text" class="form-control datepickertxt" id="rednsrvwefd" name="rednsrvwefd" readonly="true"/>                                            
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">To Date:</label>
                            <div class="col-lg-8">
                                <div class="input-group date" id="processDate">
                                    <input type="text" class="form-control datepickertxt" id="rednservtodate" name="rednservtodate" readonly="true"/>                                            
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Department:</label>
                            <div class="col-sm-8">
                                <select class="form-control" name="rednservdepartment" id="rednservdepartment">
                                    <option value="">Select</option>
                                    <c:forEach items="${departmentList}" var="department">
                                        <option value="${department.deptCode}">${department.deptName}</option>
                                    </c:forEach>                                        
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Office:</label>
                            <div class="col-sm-8">
                                <select class="form-control" name="rednservoffice" id="rednservoffice">
                                    <option value="">Select</option>
                                </select>                                        
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Cadre to which demoted:</label>
                            <div class="col-sm-8">
                                <select class="form-control" name="redncadre" id="redncadre">
                                    <option value="">Select</option>
                                </select>                                        
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Grade to which demoted:</label>
                            <div class="col-sm-8">
                                <select class="form-control" name="redngrade" id="redngrade">
                                    <option value="">Select</option>
                                </select>                                        
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Demoted Post:</label>
                            <div class="col-sm-8">
                                <select class="form-control" name="lwrpost" id="lwrpost">
                                    <option value="">Select</option>
                                </select>                                        
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Demoted Scale of Pay:</label>
                            <div class="col-sm-8">          
                                <input type="text" class="form-control" id="lwrscalepay" name="lwrscalepay" maxlength="100"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Allowed to earn Increment in this period:</label>
                            <div class="col-sm-8">          
                                <input type="radio" name="allwdinc" value="N"/> No 
                            </div>
                        </div>
                    </form>
                    <div class="modal-footer">        
                        <div class="col-sm-offset-2 col-sm-10">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel"/>
                            <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('05', 'P')"/>                                        
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <!-- Reduction to Lower Service -->
    <div id="tab06" class="modal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Compulsory Retirement</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-4">Date of Retirement:</label>
                            <div class="col-lg-8">
                                <div class="input-group date" id="processDate">
                                    <input type="text" class="form-control datepickertxt" name="duedateofretire" id="duedateofretire" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>                                    
                        </div> 
                    </form>
                    <div class="modal-footer">        
                        <div class="col-sm-offset-2 col-sm-10">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel"/>
                            <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('06', 'P')"/>                                        
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Removal from Service -->
    <div id="tab07" class="modal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Removal from Service</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-4">Date of Removal from Service:</label>
                            <div class="col-lg-8">
                                <div class="input-group date">
                                    <input type="text" class="form-control datepickertxt" id="duedateofremoval" name="duedateofremoval" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>                                    
                        </div>                                
                        <div class="form-group">
                            <label class="control-label col-sm-4">Disqualification for Future Employment:</label>
                            <div class="col-sm-8">          
                                <input type="radio" value="Y" name="remfutemp"/> Yes  
                                <input type="radio" value="N" name="remfutemp"/> No 
                            </div>
                        </div>
                        <div class="form-group">        
                            <div class="col-sm-offset-2 col-sm-10">
                                <input type="button" class="btn btn-default" value="Cancel" onclick="cancelPunishmentDetails()"/>
                                <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('07', 'P')"/>
                            </div>
                        </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Removal from Service -->                            


    <div id="tab08" class="modal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Dismissal from Service</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-4">Date of Dismissal from Service:</label>
                            <div class="col-lg-8">
                                <div class="input-group date">
                                    <input type="text" class="form-control datepickertxt" id="dismsrvdate" name="dismsrvdate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>                                    
                        </div> 
                    </form>
                </div>
                <div class="modal-footer">        
                    <div class="col-sm-offset-2 col-sm-10">
                        <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel"/>
                        <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('08', 'P')"/>                                        
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div id="tab09" class="modal" role="dialog">
        <div class="modal-dialog">
            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Recovery</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="control-label col-sm-4">Recovery:</label>
                            <div class="col-lg-8">
                                <div class="input-group date" id="processDate">
                                    <input type="text" class="form-control datepickertxt" id="finewef" name="finewef" readonly="true">                                    
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>                                    
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Fine Amount:</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control" id="fineamount" name="fineamount" maxlength="11" onkeypress="return onlyInteger(event)">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Recovery Type:</label>
                            <div class="col-sm-8">          
                                <input type="radio" name="recvtype" value="O"/> One Time
                                <input type="radio" name="recvtype" value="I"/> Installment
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">No. of Installments:</label>
                            <div class="col-sm-8">          
                                <input type="text" class="form-control" id="noinstll" name="noinstll" maxlength="2" onkeypress="return onlyInteger(event)">
                            </div>
                        </div>
                    </form>
                    <div class="modal-footer">        
                        <div class="col-sm-offset-2 col-sm-10">
                            <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel"/>
                            <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('09', 'P')"/>                                        
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>

</html>


