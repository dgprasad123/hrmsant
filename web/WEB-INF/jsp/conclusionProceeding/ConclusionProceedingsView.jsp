<%-- 
    Document   : ConclusionProceedingsView
    Created on : Aug 20, 2020, 10:56:44 AM
    Author     : Manas
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
                url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
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
                return true;
            }
            function selectauthtype(authtype) {
                tauthtype = authtype;
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
                $("#punishmenttbl").append(html);
            }
            function savePunishmentDetails(pentype) {
                var url = "savePunishmentDetails.htm";
                var concprocid = $("#concprocid").val();
                var punishmentdetailsid = $("#punishmentdetailsid").val();
                if (pentype == "01") {
                    var wefdate = $("#finewef").val();
                    var fineamount = $("#fineamount").val();
                    var recvtype = $("input[name='recvtype']:checked").val();
                    var noinstll = $("#noinstll").val();
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate,
                        fineamount: fineamount, recvtype: recvtype, noinstll: noinstll})
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
                        scalepay: scalepay, noofinc: noofinc, withincum: withincum})
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
                        tilldate: tilldate, noofpromotion: noofpromotion, withpromcum: withpromcum})
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

                } else if (pentype == "05") {

                } else if (pentype == "06") {
                    var wefdate = $("#duedateofretire").val();
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate})
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
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate, remfutemp: remfutemp})
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
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate})
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
                    $.post(url, {concprocid: concprocid, punishmentdetailsid: punishmentdetailsid, pentype: pentype, wefdate: wefdate})
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
        <form:form action="conclusionProceedingView.htm" method="post" commandName="conclusionProceedingsForm" enctype="multipart/form-data">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Initiation of Proceeding
                    </div>
                    <div class="panel-body">                                       
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">1.Memorandum No.<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:hidden path="concprocid"/>
                                <form:hidden path="initnotid"/>                                
                                ${conclusionProceedingsForm.initNotOrdNo}
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt"> 2.Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                ${conclusionProceedingsForm.initNotOrdDt}
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
                                ${conclusionProceedingsForm.notauthority}
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
                                ${conclusionProceedingsForm.postedPostName}
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtDescOP">5.Rule Under Which<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-9">
                                ${conclusionProceedingsForm.ruleofproc}
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
                                    ${conclusionProceedingsForm.conclusionOrdNo}
                                </div>
                                <div class="col-lg-2">
                                    <label for="showcauseOrdDt">2.Office Order Date </label>
                                </div>
                                <div class="col-lg-2">
                                    ${conclusionProceedingsForm.conclusionOrdDt}
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="control-label col-sm-2" >3.Documents <span style="color: red">*</span> </label> 

                                <div class="form-group row" id="backlogDocument">  
                                    ${conclusionProceedingsForm.uploadDocument}
                                </div> 
                            </div>

                           

                            <c:if test="${conclusionProceedingsForm.resnotid gt 0}">
                                <div class="row" style="margin-bottom: 7px;">
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
                                            <c:forEach items="${punishdetailsList}" var="punishdetails" varStatus="cnt">
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
                                </div> 
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="postedspn">Narration</label>
                                </div>
                                <div class="col-lg-9">
                                    ${conclusionProceedingsForm.punishmentRewarded}
                                </div>
                            </div>
                        </c:if> 

                    </div>

                </div>
            </c:if> 
            <div class="panel panel-default">
                <div class="panel-footer">
                    <input type="submit" name="action" value="Back" class="btn btn-default"/>
                </div>
            </div>
        </form:form>


    </body>
</html>
