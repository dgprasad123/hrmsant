<%-- 
    Document   : ConclusionProceedingsFirstPage
    Created on : Nov 2, 2018, 12:58:15 PM
    Author     : manisha
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript"  src="js/basicjavascript.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            var tauthtype = "";
            var tpunishmentrow;
            $(document).ready(function() {
                hideAllTab();
                $('.datepickertxt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#initNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#finalOrderDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

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
            function showTab(tabId) {
                hideAllTab();
                $('#tab' + tabId).show();
            }

            function getDeptWiseOfficeList() {
                var deptcode = $('#hidAuthDeptCode').val();
                $('#hidAuthOffCode').empty();
                var url = 'getDDOOfficeListJSON.htm?deptcode=' + deptcode;
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
                //url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#authSpc').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });

            }




            function getPost() {
                if (tauthtype == "A") {
                    $('#notdept').val($('#hidAuthDeptCode').val());
                    $('#notoffice').val($('#hidAuthOffCode').val());
                    $('#notspc').val($('#authSpc').val());
                    $('#notauthority').val($('#authSpc option:selected').text());
                } else if (tauthtype == "P") {
                    $('#posteddept').val($('#hidAuthDeptCode').val());
                    $('#postedoffice').val($('#hidAuthOffCode').val());
                    $('#postedspc').val($('#authSpc').val());
                    $('#postedPostName').val($('#authSpc option:selected').text());
                } else if (tauthtype == "S") {
                    $('#showcausenotdept').val($('#hidAuthDeptCode').val());
                    $('#showcausenotoffice').val($('#hidAuthOffCode').val());
                    $('#showcausenotspc').val($('#authSpc').val());
                    $('#showcausenotauthority').val($('#authSpc option:selected').text());
                } else if (tauthtype == "C") {
                    $('#receiprofcompliancenotdept').val($('#hidAuthDeptCode').val());
                    $('#receiprofcompliancenotoffice').val($('#hidAuthOffCode').val());
                    $('#receiprofcompliancenotspc').val($('#authSpc').val());
                    $('#receiprofcompliancenotauthority').val($('#authSpc option:selected').text());
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

            function saveCheck() {
                var radiolength = $("input[name='punishmentType']:checked").length;
                if ($('#initNotOrdNo').val() == "") {
                    alert("Please enter Order No");
                    $('#initNotOrdNo').focus();
                    return false;
                }
                if ($('#initNotOrdDt').val() == "") {
                    alert("Please enter Order Date");
                    return false;
                }
                if ($('#postedPostName').val() == "") {
                    alert("Please select Details of Posting");
                    return false;
                }
            <%--if (radiolength == 0) {
                alert("Please select Any Radio Button");
                return false;
            } --%>
                return true;
            }
            function saveCheckForResult() {
                var radiolength = $("input[name='punishmentType']:checked").length;
                if (radiolength == 0) {
                    alert("Please select Any Radio Button");
                    return false;
                }
            }
            function selectauthtype(authtype) {
                tauthtype = authtype;
                if (authtype == "P") {
                    $(".modal-title").html("Post Held During Delinquency");
                } else {
                    $(".modal-title").html("Disciplinary Authority");
                }
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
            function editPunishDetails(me, tabId, punishmentdetailsid) {
                hideAllTab();
                tpunishmentrow = me;
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
                } else if (punishmentDetails.pentype == '10') {
                    punishmentName = "Black Mark";
                }
                var html = '<tr><td>1</td>';
                html = html + '<td>' + punishmentName + '</td>';
                html = html + '<td>' + punishmentDetails.wefdate + '</td>';
                html = html + '<td></td>'
                html = html + '<td></td>'
                html = html + '<td>';
                html = html + '<a class="btn btn-primary" href="javascript:void(0)" onclick="editPunishDetails(this, \'' + punishmentDetails.concprocid + '\',' + punishmentDetails.concprocid + ')"><span class="glyphicon glyphicon-pencil"></span> Edit</a> | ';
                html = html + '<a class="btn btn-primary" href="javascript:void(0)" onclick="deletePunishDetails(this, \'01\',3490)"><span class="glyphicon glyphicon-remove"></span> Delete</a>';
                html = html + '</td></tr>';
                $("#punishmenttbl").append(html);
            }







            function deleteAttachment() {
                tConcprocid = $("#concprocid").val();
                tEmpid = $("#empid").val();
                if (confirm('Are you sure you want to delete?')) {
                    $.post("deleteAttachmentDetail.htm", {concprocid: tConcprocid, empid: tEmpid})
                            .done(function(data) {
                                alert("Deleted Sucessfully");
                            })
                } else {
                    alert("Some Error Occured");
                }
            }
            function savePunishmentDetails(pentype) {
                var vpdtype = "F";
                var url = "savePunishmentDetails.htm";
                var concprocid = $("#concprocid").val();
                var punishmentdetailsid = $("#punishmentdetailsid").val();
                $(tpunishmentrow).parent().parent().remove();
                if (pentype == "01") {
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
                else if (pentype == "10") {

                }

            }
        </script>
    </head>
    <body>
        <form:form action="saveConclusionProceedingsData.htm" method="post" commandName="conclusionProceedingsForm" enctype="multipart/form-data">
            <form:hidden path="empid"/> 
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Initiation of Proceeding
                    </div>
                    <%--<div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                        </div>
                        <div class="col-lg-1" style="text-align: left;">   
                            <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/> 
                        </div>
                        <div class="col-lg-3"></div>
                        <div class="col-lg-6"></div>
                    </div> --%>
                    <div class="panel-body">                                       
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">1.Memorandum No.<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:hidden path="concprocid"/>
                                <form:hidden path="initnotid"/>                                
                                <form:input class="form-control" path="initNotOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt"> 2.Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="initNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="spc">3. Details of Disciplinary Authority<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-9">
                                <form:hidden path="notdept"/>
                                <form:hidden path="notoffice"/>
                                <form:hidden path="notspc"/>                               
                                <form:input class="form-control" path="notauthority" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" onclick="selectauthtype('A')" data-target="#authorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="postedspn">4.Post held during Delinquency<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-9">
                                <form:hidden path="posteddept"/>
                                <form:hidden path="postedoffice"/>
                                <form:hidden path="postedspc"/> 
                                <form:input class="form-control" path="postedPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" onclick="selectauthtype('P')" data-target="#authorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtDescOP">5.Rule Under Which<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-9">
                                <form:select path="ruleofproc" class="form-control">
                                    <form:option value="rule15" >Rule 15</form:option> 
                                    <form:option value="rule16" >Rule 16</form:option> 
                                    <form:option value="rule17" >Rule 17</form:option>
                                    <form:option value="rule7" >Rule 7</form:option>
                                    <form:option value="rule15 and 17" >Rule 15 and 17</form:option>
                                    <form:option value="rule15 and 7" >Rule 15 and 7</form:option>
                                    <form:option value="rule15 and 17 and 7" >Rule 15 and 17 and 7 </form:option>
                                    <form:option value="rule16 and 17" >Rule 16 and 17</form:option>
                                    <form:option value="rule16 and 7" >Rule 16 and 7</form:option>
                                    <form:option value="rule16 and 17 and 7" >Rule 16 and 17 and 7</form:option>
                                    <form:option value="Disciplinary Rule 7/ocs(Pention Rule)">Disciplinary Rule 7/ocs(Pention Rule)</form:option>
                                </form:select> 
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                    </div>

                </div>
                <c:if test="${conclusionProceedingsForm.concprocid gt 0}">
                    <div class="panel panel-default">
                        <div class="panel-body">

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="txtNotOrdNo">1.Office Order No </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:hidden path="resnotid"/>
                                    <form:input class="form-control" path="conclusionOrdNo" maxlength="51"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="showcauseOrdDt">2.Office Order Date </label>
                                </div>
                                <div class="col-lg-2">
                                    <div class="input-group date">
                                        <form:input class="form-control datepickertxt" path="conclusionOrdDt" readonly="true"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-2" >3.Documents <span style="color: red">*</span> </label> 
                                <div class="form-group row" id="backlogDocument"> 
                                    <c:if test="${not empty conclusionProceedingsForm.originalFilename}">
                                        <a href="downloadAttachmentOfInitiationProceeding.htm?concprocid=${proc.concprocid}" class="btn btn-default">
                                            <span class="glyphicon glyphicon-paperclip"></span> ${conclusionProceedingsForm.originalFilename}
                                        </a>
                                        <input type="button" name="action" value="Remove" class="btn btn-danger" onclick="deleteAttachment()"/>
                                    </c:if>
                                    <input type="file" name="uploadDocument" id="uploadDocument"  class="form-control-file"/>
                                </div> 
                            </div>
                            <div class="form-group">
                                <div class="col-lg-2">
                                    <b>If Censure</b><input type="radio" id="censure" name="punishmentType" value="censure"> 
                                </div>
                                <div class="col-lg-2"> 
                                    <b>Free From Charge</b><input type="radio" id="freefromcharge" name="punishmentType" value="freefromcharge"> 
                                </div>
                                <div class="col-lg-2">
                                    <b>If Punishment</b><input type="radio" id="punishment" name="punishmentType" value="punishment"> 
                                </div>                                                                    
                            </div>
                            <c:if test="${conclusionProceedingsForm.resnotid gt 0 and conclusionProceedingsForm.punishmentType eq 'freefromcharge'}">
                                <div class="form-group">
                                    <div class="col-lg-2">
                                        <label for="postedspn">Narration</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:textarea class="form-control" path="narrationForFreeCharge"/><br/>
                                        <span style="font-style: italic;color: #008000;">(Not More than 1000 Character)</span> <span id="chargedtlslen">(0/1000)</span>                          
                                    </div>
                                </div> 



                            </c:if> 
                            <c:if test="${conclusionProceedingsForm.resnotid gt 0 and conclusionProceedingsForm.punishmentType eq 'punishment'}">
                                <div class="row" style="margin-bottom: 7px;">
                                    <table class="table table-bordered" id="show">
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
                                            <c:forEach items="${punishdetailsList}" var="punishdetails" varStatus="cnt">
                                                <tr>
                                                    <td>${cnt.index+1}</td>
                                                    <td>${punishdetails.punishmenttypedesc}</td>
                                                    <td>${punishdetails.wefdate}</td>
                                                    <td>${punishdetails.tilldate}</td>
                                                    <td>${punishdetails.narration}</td>  
                                                    <td>
                                                        <a class="btn btn-primary" href="javascript:void(0)" onclick="editPunishDetails(this, '${punishdetails.pentype}',${punishdetails.punishmentdetailsid})"><span class="glyphicon glyphicon-pencil"></span> Edit</a> | 
                                                        <a class="btn btn-primary" href="javascript:void(0)" onclick="deletePunishDetails(this, '${punishdetails.pentype}',${punishdetails.punishmentdetailsid})"><span class="glyphicon glyphicon-remove"></span> Delete</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div> 
                            </div>

                            <div class="row" style="margin-bottom: 7px;" align="center" >
                                <div class="btn-group">
                                    <button type="button" class="btn btn-primary" onclick="showTab('01')">Fine</button>
                                    <button type="button" class="btn btn-primary" onclick="showTab('09')">Recovery</button>
                                    <button type="button" class="btn btn-primary" onclick="showTab('02')">Withholding Increment</button>
                                    <button type="button" class="btn btn-primary" onclick="showTab('03')">Withholding Promotion</button>
                                    <button type="button" class="btn btn-primary" onclick="showTab('04')">Suspension</button>
                                    <button type="button" class="btn btn-primary" onclick="showTab('05')">Reduction to Lower Service</button>
                                    <button type="button" class="btn btn-primary" onclick="showTab('06')">Compulsory Retirement</button>
                                    <button type="button" class="btn btn-primary" onclick="showTab('07')">Removal from Service</button>
                                    <button type="button" class="btn btn-primary" onclick="showTab('08')">Dismissal from Service</button>
                                    <button type="button" class="btn btn-primary" onclick="showTab('10')">Black Mark</button>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="postedspn">Narration</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:textarea class="form-control" path="punishmentRewarded"/><br/>
                                    <span style="font-style: italic;color: #008000;">(Not More than 1000 Character)</span> <span id="chargedtlslen">(0/1000)</span>                          
                                </div>
                            </div>
                        </c:if> 
                        <div class="row" style="margin-bottom: 7px;">
                            <input type="hidden" name="punishmentdetailsid" id="punishmentdetailsid" value="0"/>

                            <div class="form-horizontal" id="tab01">
                                <div class="form-group">
                                    <label class="control-label col-sm-2"><h3 style="text-decoration: underline;">Fine</h3></label>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">With Effect Date:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <input type="text" class="form-control datepickertxt" id="finewef" name="finewef" readonly="true">                                    
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>                                    
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Fine Amount:</label>
                                    <div class="col-sm-2">
                                        <input type="text" class="form-control" id="fineamount" name="fineamount" maxlength="11" onkeypress="return onlyInteger(event)">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Recovery Type:</label>
                                    <div class="col-sm-4">          
                                        <input type="radio" name="recvtype" value="O"/> One Time
                                        <input type="radio" name="recvtype" value="I"/> Installment
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">No. of Installments:</label>
                                    <div class="col-sm-2">          
                                        <input type="text" class="form-control" id="noinstll" name="noinstll" maxlength="2" onkeypress="return onlyInteger(event)">
                                    </div>
                                </div>
                                <div class="form-group">        
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <input type="button" class="btn btn-default" value="Cancel" onclick="cancelPunishmentDetails()"/>
                                        <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('01')"/>                                        
                                    </div>
                                </div>
                            </div>
                            <div class="form-horizontal" id="tab09">
                                <div class="form-group">
                                    <label class="control-label col-sm-2"><h3 style="text-decoration: underline;">Recovery</h3></label>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">With Effect Date:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <input type="text" class="form-control datepickertxt" id="finewef" name="finewef" readonly="true">                                    
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>                                    
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Fine Amount:</label>
                                    <div class="col-sm-2">
                                        <input type="text" class="form-control" id="fineamount" name="fineamount" maxlength="11" onkeypress="return onlyInteger(event)">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Recovery Type:</label>
                                    <div class="col-sm-4">          
                                        <input type="radio" name="recvtype" value="O"/> One Time
                                        <input type="radio" name="recvtype" value="I"/> Installment
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">No. of Installments:</label>
                                    <div class="col-sm-2">          
                                        <input type="text" class="form-control" id="noinstll" name="noinstll" maxlength="2" onkeypress="return onlyInteger(event)">
                                    </div>
                                </div>
                                <div class="form-group">        
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <input type="button" class="btn btn-default" value="Cancel" onclick="cancelPunishmentDetails()"/>
                                        <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('01')"/>                                        
                                    </div>
                                </div>
                            </div>
                            <!-- Fine / Recovery -->
                            <div class="form-horizontal" id="tab02">
                                <div class="form-group">
                                    <label class="control-label col-sm-2"><h3 style="text-decoration: underline;">Withholding Increment</h3></label>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">With Effect Date:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <input type="text" class="form-control datepickertxt" id="withincwef" name="withincwef" readonly="true"/>                                            
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Scale of Pay:</label>
                                    <div class="col-sm-2">          
                                        <input type="text" class="form-control" id="scalepay" name="scalepay" maxlength="100">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">No. of Increment to be with hold:</label>
                                    <div class="col-sm-2">          
                                        <input type="text" class="form-control" id="noofinc" name="noofinc" maxlength="2" onkeypress="return onlyInteger(event)">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Withhold Increment with cumulative effect:</label>
                                    <div class="col-sm-4">          
                                        <input type="checkbox" id="withincum" name="withincum" value="Y"/>                                        
                                    </div>
                                </div>

                                <div class="form-group">        
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <input type="button" class="btn btn-default" value="Cancel" onclick="cancelPunishmentDetails()"/>
                                        <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('02')"/>                                       
                                    </div>
                                </div>
                            </div>

                            <div class="form-horizontal" id="tab03">
                                <div class="form-group">
                                    <label class="control-label col-sm-2"><h3 style="text-decoration: underline;">Withholding Promotion</h3></label>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">With Effect Date:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <input type="text" class="form-control datepickertxt" id="withpromwef" name="withpromwef" readonly="true"/>                                            
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">To Date:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <input type="text" class="form-control datepickertxt" id="promtodate" name="promtodate" readonly="true"/>                                            
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">No. of Promotion to be with hold:</label>
                                    <div class="col-sm-2">          
                                        <%-- <input type="text" class="form-control" id="noofpromotion" name="noofpromotion" maxlength="2"  onkeypress="return onlyInteger(event)"/> --%>
                                        <form:select path="ruleofproc" class="form-control">
                                            <form:option value="year1" >1</form:option> 
                                            <form:option value="year2" >2</form:option> 
                                            <form:option value="year3" >3</form:option>
                                            <form:option value="year4" >4</form:option>
                                            <form:option value="year5" >5</form:option>
                                            <form:option value="year6" >6</form:option>
                                            <form:option value="year7" >7 </form:option>
                                            <form:option value="year8" >8</form:option>
                                            <form:option value="year9" >9</form:option>
                                        </form:select> 
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Withhold Promotion with cumulative effect:</label>
                                    <div class="col-sm-4">          
                                        <input type="checkbox" id="withpromcum" name="withpromcum" value="Y"/>                                        
                                    </div>
                                </div>

                                <div class="form-group">        
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <input type="button" class="btn btn-default" value="Cancel" onclick="cancelPunishmentDetails()"/>
                                        <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('03')"/>
                                    </div>
                                </div>
                            </div>
                            <!-- Withholding Promotion -->                            
                            <div class="form-horizontal" id="tab04">
                                <div class="form-group">
                                    <label class="control-label col-sm-2"><h3 style="text-decoration: underline;">Suspension</h3></label>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Suspension Order No:</label>
                                    <div class="col-sm-2">
                                        <input type="text" class="form-control"  name="suspordno" id="suspordno">                                 
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Effect From Date:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <input type="text" class="form-control datepickertxt" id="suspwefd" name="suspwefd" readonly="true"/>                                            
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">To Date:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <input type="text" class="form-control datepickertxt" id="susptodate" name="susptodate" readonly="true"/>                                            
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </div>                                
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Period to be treated as:</label>
                                    <div class="col-sm-4">          
                                        <input type="radio" name="suspperiod" value="Y"/> As on Duty 
                                        <input type="radio" name="suspperiod" value="N"/> As Such 
                                        <input type="radio" name="suspperiod" value="L"/> As on Leave 
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Type of Leave:</label>
                                    <div class="col-sm-4">
                                        <form:select path="tolid" id="typeofleave" class="form-control">
                                            <form:option value="">--Select--</form:option>
                                            <form:options items="${leavelist}" itemValue="tolid" itemLabel="tol"/>
                                        </form:select>

                                    </div>
                                </div>

                                <div class="form-group">        
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('04')"/>
                                    </div>
                                </div>
                            </div>
                            <!-- Suspension -->                            
                            <div class="form-horizontal" id="tab05">
                                <div class="form-group">
                                    <label class="control-label col-sm-4"><h3 style="text-decoration: underline;">Reduction to Lower Service</h3></label>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">With Effect Date:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <input type="text" class="form-control datepickertxt" id="rednsrvwefd" name="rednsrvwefd" readonly="true"/>                                            
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">To Date:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <input type="text" class="form-control datepickertxt" id="rednservtodate" name="rednservtodate" readonly="true"/>                                            
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Department:</label>
                                    <div class="col-sm-4">
                                        <select class="form-control" name="rednservdepartment" id="rednservdepartment">
                                            <option value="">Select</option>
                                            <c:forEach items="${departmentList}" var="department">
                                                <option value="${department.deptCode}">${department.deptName}</option>
                                            </c:forEach>                                        
                                        </select>
                                    </div>

                                    <%--<div class="col-sm-4">
                                        <select class="form-control" name="rednservdepartment" id="rednservdepartment">
                                            <option value="">Select</option>
                                        </select>                                        
                                    </div>--%>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Office:</label>
                                    <div class="col-sm-4">
                                        <select class="form-control" name="rednservoffice" id="rednservoffice">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                    <%--  <div class="col-sm-4">
                                          <select class="form-control" name="rednservoffice" id="rednservoffice">
                                              <option value="">Select</option>
                                          </select>                                        
                                      </div>--%>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Cadre to which demoted:</label>
                                    <div class="col-sm-4">
                                        <select class="form-control" name="redncadre" id="redncadre">
                                            <option value="">Select</option>
                                        </select>                                        
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Grade to which demoted:</label>
                                    <div class="col-sm-4">
                                        <select class="form-control" name="redngrade" id="redngrade">
                                            <option value="">Select</option>
                                        </select>                                        
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Demoted Post:</label>
                                    <div class="col-sm-4">
                                        <select class="form-control" name="lwrpost" id="lwrpost">
                                            <option value="">Select</option>
                                        </select>                                        
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Demoted Scale of Pay:</label>
                                    <div class="col-sm-4">          
                                        <input type="text" class="form-control" id="lwrscalepay" name="lwrscalepay" maxlength="100"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Allowed to earn Increment in this period:</label>
                                    <div class="col-sm-4">          
                                        <%-- <input type="radio" name="allwdinc" value="Y"/> Yes   --%>
                                        <input type="radio" name="allwdinc" value="N"/> No 
                                    </div>
                                </div>

                                <div class="form-group">        
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('05')"/>
                                        <input type="button" class="btn btn-default" value="Cancel" onclick="cancelPunishmentDetails()"/>
                                    </div>
                                </div>
                            </div>
                            <!-- Reduction to Lower Service -->                            
                            <div class="form-horizontal" id="tab06">
                                <div class="form-group">
                                    <label class="control-label col-sm-2"><h3 style="text-decoration: underline;">Compulsory Retirement</h3></label>
                                </div>                               
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Date of Retirement:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date" id="processDate">
                                            <input type="text" class="form-control datepickertxt" name="duedateofretire" id="duedateofretire" readonly="true"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>                                    
                                </div>                                

                                <div class="form-group">        
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <input type="button" class="btn btn-default" value="Cancel" onclick="cancelPunishmentDetails()"/>
                                        <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('06')"/>
                                    </div>
                                </div>
                            </div>
                            <!-- Compulsory Retirement -->                            
                            <div class="form-horizontal" id="tab07">
                                <div class="form-group">
                                    <label class="control-label col-sm-2"><h3 style="text-decoration: underline;">Removal from Service</h3></label>
                                </div>                               
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Date of Removal from Service:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date">
                                            <input type="text" class="form-control datepickertxt" id="duedateofremoval" name="duedateofremoval" readonly="true"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>                                    
                                </div>                                
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Disqualification for Future Employment:</label>
                                    <div class="col-sm-4">          
                                        <input type="radio" value="Y" name="remfutemp"/> Yes  
                                        <input type="radio" value="N" name="remfutemp"/> No 
                                    </div>
                                </div>
                                <div class="form-group">        
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <input type="button" class="btn btn-default" value="Cancel" onclick="cancelPunishmentDetails()"/>
                                        <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('07')"/>
                                    </div>
                                </div>
                            </div>
                            <!-- Removal from Service -->                            
                            <div class="form-horizontal" id="tab08">
                                <div class="form-group">
                                    <label class="control-label col-sm-2"><h3 style="text-decoration: underline;">Dismissal from Service</h3></label>
                                </div>                               
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Date of Dismissal from Service:</label>
                                    <div class="col-lg-2">
                                        <div class="input-group date">
                                            <input type="text" class="form-control datepickertxt" id="dismsrvdate" name="dismsrvdate" readonly="true"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-time"></span>
                                            </span>
                                        </div>                                
                                    </div>                                    
                                </div>                                

                                <div class="form-group">        
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <input type="button" class="btn btn-default" value="Cancel" onclick="cancelPunishmentDetails()"/>
                                        <input type="button" class="btn btn-default" value="Add" onclick="savePunishmentDetails('08')"/>
                                    </div>
                                </div>
                            </div>
                            <!-- Dismissal from Service -->
                        </div>
                    </div>

                </div>
            </c:if> 
            <div class="panel panel-default">
                <div class="panel-footer">
                    <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return saveCheck()"/>
                    <input type="submit" name="action" value="Back" class="btn btn-default"/>
                </div>
            </div>
        </form:form>

        <div id="authorityModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Disciplinary Authority</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltDept">Department</label>
                            </div>
                            <div class="col-lg-9">
                                <select name="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                    <option value="">--Select Department--</option>
                                    <c:forEach items="${departmentList}" var="dept">
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



    </body>
</html>
