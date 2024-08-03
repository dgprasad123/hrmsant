<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtWEFDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                $('#txtCadreJoiningWEFDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function getDeptWiseOfficeList(type) {
                var deptcode = $('#hidNotifyingDeptCode').val();
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');

                var url = "";
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                } else {
                    url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                }

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode = $('#hidNotifyingOffCode').val();

                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#notifyingSpc').append('<option value="">--Select Post--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#notifyingSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });
            }

            function getPost(type) {
                $('#notifyingPostName').val($('#notifyingSpc option:selected').text());
            }

            function onlyIntegerRange(e) {
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 57) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 57) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }

            function saveCheck() {


                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                    return false;
                }
                
                if ($('#txtNotOrdNo').val() == "") {
                    alert("Please enter Order No");
                    $('#txtNotOrdNo').focus();
                    return false;
                }
                if ($('#txtNotOrdDt').val() == "") {
                    alert("Please enter Order Date");
                    return false;
                }
                var sltCadreDept = $('#sltCadreDept').val();
                if (sltCadreDept == "") {
                     alert("Please Select Cadre Controlling Department");
                    $('#sltCadreDept').focus();
                    return false;
                }
                var sltCadre = $('#sltCadre').val();
                if (sltCadre == "") {
                     alert("Please Select Name of the Cadre");
                    $('#sltCadre').focus();
                    return false;
                }

                if ($('#txtGP').val() == "") {
                    alert("Please enter Grade Pay");
                    $('#txtGP').focus();
                    return false;
                }
                if ($('#txtBasic').val() == "") {
                    alert("Please enter Pay");
                    $('#txtBasic').focus();
                    return false;
                }
                if ($('#txtWEFDt').val() == "") {
                    alert("Please enter Date of Effect of Pay");
                    return false;
                }
                
                if($("#sltPostGroup").val() == ""){
                    alert("Please select Post Group");
                    return false;
                }
                return true;
            }

            function getPostCodeDeptWise() {
                $('#sltGenericPost').empty();
                var url = 'getDeptWisePostListJSON.htm?deptCode=' + $('#sltPostingDept').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltGenericPost').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                    });
                });
            }

            function getDeptWiseCadre() {
                $('#sltCadre').empty();
                var url = 'getCadreListJSON.htm?deptcode=' + $('#sltCadreDept').val();
                  $('#sltCadre').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltCadre').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getCadreWiseGrade() {
                $('#sltGrade').empty();
                var url = 'getGradeListCadreWiseJSON.htm?cadreCode=' + $('#sltCadre').val();
                 $('#sltGrade').append('<option value="">--Select--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltGrade').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }
            function getDescription() {
                $('#sltDescription').empty();
                $('#sltDescription').append('<option value="">--Select--</option>');
                var url = 'getDescriptionJSON.htm?cadreLevel=' + $('#sltCadreLevel').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltDescription').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function openNotifyingAuthorityModal() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                } else {
                    var orgType = $('input[name="radpostingauthtype"]:checked').val();
                    if (orgType == 'GOO') {
                        $('#recruitmentAuthorityModal').modal("show");
                    } else if (orgType == 'GOI') {
                        $("#recruitmentOtherOrgModal").modal("show");
                    }
                }
            }

            function getOtherOrgPost() {
                $('#notifyingPostName').val($('#hidOthSpc option:selected').text());

                $('#hidNotifyingDeptCode').val('');
                $('#hidNotifyingOffCode').val('');
                $("#notifyingSpc").val();
                $("#recruitmentOtherOrgModal").modal("hide");
            }
        </script>
    </head>
    <body>
        <form:form action="saveRecruitment.htm" method="POST" commandName="recruitmentModel">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Recruitment
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="hnotid" id="hnotid"/>
                        <form:hidden path="hpayid" id="hpayid"/>
                        <form:hidden path="hcadId" id="hcadId"/>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/> 
                            </div>
                            <div class="col-lg-3"></div>
                            <div class="col-lg-2"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label for="rdTransaction">Select type of Transaction</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:radiobutton path="rdTransaction" value="S" onclick="removePostingData();"/> Service Book Entry(Backlog)
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="rdTransaction" value="C" onclick="removePostingData();"/> Current Transaction(will effect current Pay or Post)
                            </div>
                            <div class="col-lg-2"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltNotificationType">Notification Type<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-9">   
                                <form:select class="form-control" path="sltNotificationType" id="sltNotificationType">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="FIRST_APPOINTMENT">REGULAR RECRUITMENT</form:option>
                                    <form:option value="REHABILITATION">REHABILITATION</form:option>
                                    <form:option value="VALIDATION">VALIDATION</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">Notification Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtNotOrdNo" id="txtNotOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt">Notification Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtNotOrdDt" id="txtNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="radpostingauthtype">Notifying Authority<span style="color: red"> *</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOI" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="notifyingPostName" id="notifyingPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" onclick="openNotifyingAuthorityModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label>Details of Cadre, Grade and Post</label>
                            </div>
                            <div class="col-lg-8">

                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(a) Cadre Controlling Department<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltCadreDept" id="sltCadreDept" class="form-control" onchange="getDeptWiseCadre();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(b) Name of the Cadre<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltCadre" id="sltCadre" class="form-control" onchange="getCadreWiseGrade();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${cadrelist}" itemValue="value" itemLabel="label"/>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(c) Name of the Grade
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltGrade" id="sltGrade" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${gradelist}" itemValue="value" itemLabel="label"/>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(d) Cadre Level
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltCadreLevel" id="sltCadreLevel" class="form-control" onchange="getDescription();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${lvlList}" itemValue="value" itemLabel="label"/>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(e) Description
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltDescription" id="sltDescription" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${desclist}" itemValue="value" itemLabel="label"/>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(f) Allotment Year
                            </div>
                            <div class="col-lg-3">
                                <form:input path="txtAllotmentYear" id="txtAllotmentYear" class="form-control" maxlength="4"/>
                            </div>
                            <div class="col-lg-7"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(g) Cadre Id
                            </div>
                            <div class="col-lg-3">
                                <form:input path="txtCadreId" id="txtCadreId" class="form-control" maxlength="10"/>
                            </div>
                            <div class="col-lg-7"></div>
                        </div>
                        <br />
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-6">
                                <label>Please Fill up Column below if Post Details is Available</label>
                            </div>
                            <div class="col-lg-4">

                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(i) Posting Department
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltPostingDept" id="sltPostingDept" class="form-control" onchange="getPostCodeDeptWise();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(ii) Name of the Generic Post
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltGenericPost" id="sltGenericPost" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${gpclist}" itemValue="postcode" itemLabel="post"/>
                                </form:select>
                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                        <br />
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label>Post Classification</label>
                            </div>
                            <div class="col-lg-8">
                                <form:radiobutton path="rdPostClassification" value="A"/>&nbsp;Adhoc
                                <form:radiobutton path="rdPostClassification" value="T"/>&nbsp;Temporary
                                <form:radiobutton path="rdPostClassification" value="O"/>&nbsp;On Probation
                                <form:radiobutton path="rdPostClassification" value="P"/>&nbsp;Permanent
                                <form:radiobutton path="rdPostClassification" value=""/>&nbsp;None
                                <br />
                                <form:radiobutton path="rdPostStatus" value="O"/>&nbsp;Officiating
                                <form:radiobutton path="rdPostStatus" value="S"/>&nbsp;Substantive
                                <form:radiobutton path="rdPostStatus" value="A"/>&nbsp;None
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>
                        <br />
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="txtCadreJoiningWEFDt">Date of Effect of Joining in Cadre / Post<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <div class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="txtCadreJoiningWEFDt" id="txtCadreJoiningWEFDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="sltCadreJoiningWEFTime">Time<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltCadreJoiningWEFTime" id="sltCadreJoiningWEFTime" class="form-control">
                                    <option value="">-Select-</option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>Details of Pay</label>
                            </div>
                            <div class="col-lg-9">

                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (a) Scale of Pay/Pay Band
                            </div>
                            <div class="col-lg-5">
                                <form:select path="sltPayScale" id="sltPayScale" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${payscalelist}" itemValue="payscale" itemLabel="payscale"/>
                                </form:select>
                            </div>
                            <div class="col-lg-5">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (b) Grade Pay<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtGP" id="txtGP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (c) Pay in Substantive Post<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtBasic" id="txtBasic" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (d) Special Pay
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtSP" id="txtSP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (e) Personal Pay
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPP" id="txtPP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (f) Other Pay
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtOP" id="txtOP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (g) Description of Other Pay
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="txtDescOP" id="txtDescOP"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtWEFDt">Date of Effect of Pay<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="txtWEFDt" id="txtWEFDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="sltWEFTime">Time<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltWEFTime" id="sltWEFTime" class="form-control">
                                    <option value="">-Select-</option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>
                        
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Post Group</label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltPostGroup" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="A">A</form:option>
                                    <form:option value="B">B</form:option>
                                    <form:option value="C">C</form:option>
                                    <form:option value="D">D</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-8"></div>
                        </div>
                            
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Note(if any)</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <a href="RecruitmentList.htm"><input type="button" name="btnRecruitment" value="Back" class="btn btn-default"/></a>
                        <input type="submit" name="btnRecruitment" value="Save Recruitment" class="btn btn-default" onclick="return saveCheck();"/>
                        <c:if test="${not empty recruitmentModel.hnotid}">
                            <%--<input type="submit" name="btnRecruitment" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');"/>--%>
                        </c:if>
                    </div>
                </div>
            </div>

            <div id="recruitmentAuthorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Notifying Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingDeptCode" id="hidNotifyingDeptCode" class="form-control" onchange="getDeptWiseOfficeList('N');">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOffCode" id="hidNotifyingOffCode" class="form-control" onchange="getOfficeWisePostList('N');">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="notifyingSpc" id="notifyingSpc" class="form-control" onchange="getPost('N');">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${postlist}" itemValue="spc" itemLabel="postname"/>
                                    </form:select>
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

            <div id="recruitmentOtherOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Sanctioning Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="hidOthSpc">Sanctioned By</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidOthSpc" id="hidOthSpc" class="form-control" onchange="getOtherOrgPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgOfflist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
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
    </body>
</html>
