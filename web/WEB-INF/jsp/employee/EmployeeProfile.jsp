
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% int i = 1;
%>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee Profile</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">  
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/common.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <script type="text/javascript"  src="js/basicjavascript.js"></script>
        <script language="javascript" type="text/javascript">
            $(document).ready(function() {
                $('#bankaccno').bind("cut copy paste", function(e) {
                    e.preventDefault();
                });
            });
            function saveProfileData() {
                var deptCode = $('#deptCode').val();
                if (deptCode == 13) {
                    var staffType = $('#staffType').val();
                    if ($("#staffType:checked").length == 0) {
                        alert("Please select Staff Type");
                        return false;
                    }
                    var staffTypeVal = $("input[name='staffType']:checked").val();
                    if (staffTypeVal == "T") {
                        var staffCategorys = $("input[name='staffCategory']:checked").val();
                        if ($(".classStaffCategory:checked").length == 0) {
                            alert("Please select Staff Category");
                            return false;
                        }
                    }

                    if (staffCategorys == "Lecturer(CB)") {
                        var staffPlacement = $("input[name='staffPlacement']:checked").val();
                        if ($(".classStaffPlacement:checked").length == 0) {
                            alert("Please select Placement Type");
                            return false;
                        }
                    }
                    if (staffPlacement == "Placement from State Scale") {
                        var cDesignation = $('#cDesignation').val();
                        var iDesignation = $('#iDesignation').val();
                        if (cDesignation == "") {
                            alert("Please select Current Designation");
                            return false;
                        }
                        if (iDesignation == "") {
                            alert("Please select Initial Designation");
                            return false;
                        }
                    }
                }
                // alert(deptCode);
                // return false;




                var dob = $('#dob').html();
                if (dob == '') {
                    alert("Date of Birth can not be blank");
                    return false;
                }
                var joindategoo = $('#joindategoo1').val();
                if (joindategoo == '') {
                    alert("Please enter Date from which in continuous service with GoO(dd-MMM-yyyy)");
                    $('#joindategoo1').focus();
                    return false;
                }
                var doeGov = $('#doeGov1').val();
                if (doeGov == '') {
                    alert("Please enter Date of entry into Govt. service(dd-MMM-yyyy)");
                    $('#doeGov1').focus();
                    return false;
                }
                var radioValue = $("input[name='gender']:checked").val();
                if (radioValue == '') {
                    alert("Please choose Gender");
                    return false;
                }
                var marital = $('#marital').val();
                if (marital == '') {
                    alert("Please select marital status");
                    $('#marital').focus();
                    return false;
                }

                var postGrpType = $('#postGrpType').val();
                if (postGrpType == '') {
                    alert("Please select Post Group Type");
                    $('#postGrpType').focus();
                    return false;
                }
                var height = $('#height').val();
                if (height == '0.0') {
                    alert("Please enter height");
                    $('#height').focus();
                    return false;
                }
                var bloodgrp = $('#bloodgrp').val();
                if (bloodgrp == '') {
                    alert("Please select Blood Group");
                    $('#bloodgrp').focus();
                    return false;
                }
                var homeTown = $('#homeTown').val();
                if (homeTown == '') {
                    alert("Please Enter Home Town");
                    $('#homeTown').focus();
                    return false;
                }
                var religion = $('#religion').val();
                if (religion == '') {
                    alert("Please select Religion");
                    $('#religion').focus();
                    return false;
                }
                var domicil = $('#domicil').val();
                if (domicil == '') {
                    alert("Please Enter Domicil");
                    $('#domicil').focus();
                    return false;
                }
                var idmark = $('#idmark').val();
                if (idmark == '') {
                    alert("Please Enter Personal Identification Mark");
                    $('#idmark').focus();
                    return false;
                }

                /*var bankaccno = $('#bankaccno').val();
                 if (bankaccno == '') {
                 alert("Please Enter Bank Account Number");
                 $('#bankaccno').focus();
                 return false;
                 }*/
                var mob = /^[1-9]{1}[0-9]{9}$/;
                var mobile = $('#mobile').val();
                if (mobile == '') {
                    alert("Please Enter Mobile Number");
                    $('#mobile').focus();
                    return false;
                } else if (mob.test($.trim($("#mobile").val())) == false) {
                    alert('Please enter a valid mobile number.');
                    $('#mobile').focus();
                    return false;
                }
                var stremail = $('#email').val();
                if (stremail == '' && $("#chkemail")[0].checked == false) {
                    alert("Please enter your email. Else check the box that you don't have any email.");
                    $('#email').focus();
                    return false;
                }
                $('#sltGPFAssmued').attr('disabled', false);
                //return false;
            }
            function getBranchList(me) {

                $('option', $('#sltbranch')).not(':eq(0)').remove();
                $.ajax({
                    type: "POST",
                    url: "bankbranchlistJSON.htm?bankcode=" + $(me).val(),
                    success: function(data) {
                        $.each(data, function(i, obj)
                        {
                            $('#sltbranch').append($('<option>', {
                                value: obj.branchcode,
                                text: obj.branchname

                            }));

                        });
                    }
                });
            }
            function callNoImage() {

                var userPhoto = document.getElementById('profilePhoto');
                userPhoto.src = "images/NoEmployee.png";

            }
            function callNoImage() {

                var userPhoto = document.getElementById('profilePhoto');
                userPhoto.src = "images/NoEmployee.png";

            }
            function UploadImage() {
                var url = 'fileUploadDdoForm.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "50%", overlayClose: false, onClosed: refreshImage});
            }

            function refreshImage() {
                $("#profilePhoto").attr('src', 'profileddophoto.htm?' + 'date=' + (new Date()).getTime());
            }
            function UploadJointPhoto()
            {
                var url = 'UploadJointPhotoDDO.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "50%", overlayClose: false, onClosed: refreshImage});
            }
            function callNoImageJointPhoto() {

                var userPhoto = document.getElementById('jointPhoto');
                userPhoto.src = "images/NoEmployee.png";

            }
            function getSupDate(empId, retiredYear, supanndate, dob)
            {

                var empid = document.getElementById("empid").value;
                var url = "getSuperannuationData.htm?empId=" + empid + "&retiredYear=" + retiredYear;
                $.getJSON(url, function(data) {
                    $("#txtDos1").val(data.superandate);

                });
            }
            function getSupYear(empId, retiredYear, supanndate, dob)
            {

                var empid = document.getElementById("empid").value;
                var url = "getSuperannuationData.htm?empId=" + empid + "&retiredYear=" + retiredYear;
                $.getJSON(url, function(data) {
                    $("#txtDos1").val(data.superandate);

                });
            }
            function getDeptWiseCadre() {
                $('#cadreType').empty();
                $('#cadreType').append('<option value="">--Select Department--</option>');
                var url = 'getCadreListJSON.htm?deptcode=' + $('#deptType').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#cadreType').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getCadreWiseGrade() {
                $('#gradeType').empty();
                var url = 'getGradeListCadreWiseJSON.htm?cadreCode=' + $('#cadreType').val();

                $.getJSON(url, function(data) {
                    $('#gradeType').append('<option value="">SELECT\n\
            </option>');
                    $.each(data, function(i, obj) {
                        $('#gradeType').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            /*function phonenumber()
             {
             var mob = /^[1-9]{1}[0-9]{9}$/;
             if (mob.test($.trim($("#mobile").val())) == false) {
             alert('Please enter a valid mobile number.');
             $('#mobile').val("");
             return false;
             }
             }*/
            function AvoidSpace(event) {
                var k = event ? event.which : window.event.keyCode;
                if (k == 32)
                    return false;
            }
            function staff_type(vals) {
                if (vals == "T") {
                    //  $("#id_placement_type").show();
                    $("#id_staff_category").show();
                } else {
                    $("#id_placement_type").hide();
                    $("#id_staff_category").hide();
                    $("#id_staff_idesignation").hide();
                    $("#id_staff_cdesignation").hide();
                    $("#id_psc_year").hide();
                    $('.classStaffPlacement').prop('checked', false);
                    $('.classStaffCategory').prop('checked', false);
                    $("#cDesignation").val("");
                    $("#iDesignation").val("");
                    $("#id_psc_year").val("");
                }
            }
            function staff_Placement(vals) {
                if (vals == "Placement from State Scale") {
                    $("#id_staff_idesignation").show();
                    $("#id_staff_cdesignation").show();
                    $("#id_psc_year").show();
                } else {
                    $("#id_staff_idesignation").hide();
                    $("#id_staff_cdesignation").hide();
                    $("#id_psc_year").hide();
                    $("#cDesignation").val("");
                    $("#iDesignation").val("");
                    $("#pscYear").val("");
                }
            }
            function staff_category(vals) {
                if (vals == "Lecturer(CB)") {
                    $("#id_placement_type").show();

                } else {
                    $("#id_placement_type").hide();
                    $("#id_staff_idesignation").hide();
                    $("#id_staff_cdesignation").hide();
                    $("#id_psc_year").hide();
                    $('.classStaffPlacement').prop('checked', false);

                }
            }
            function validateEmailcBox()
            {
                if ($("#chkemail")[0].checked) {
                    $('#email').val('');
                }
            }
            function validateEmailtBox()
            {
                if ($("#email").val() == '') {
                    $("#chkemail")[0].checked = true;
                }
                else
                {
                    $("#chkemail")[0].checked = false;
                }
            }

        </script>

    </head>

    <body>
        <form:form name="myForm"  action="profile.htm" method="POST" commandName="emp">
            <form:hidden id="empid" path="empid" value="${emp.empid}"/>
            <form:hidden id="deptCode" path="deptCode" value="${deptCode}"/>
            <form:hidden id="ifprofileVerified" path="ifprofileVerified" value="${emp.ifprofileVerified}"/>

            <div style=" margin-bottom: 5px;" class="panel panel-info">
                <div class="panel-body">

                    <table border="0" cellpadding="0" cellspacing="0"  width="100%">
                        <tr>
                            <td width="5%">&nbsp;</td>
                            <td width="25%">&nbsp;</td>
                            <td width="30%" align="center">&nbsp;</td>
                            <td width="15%" align="center">&nbsp;</td>
                            <td width="30%" align="center">&nbsp;</td>

                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Employee's Full Name:</td>
                            <td colspan="3" width="">
                                <c:out value="${emp.empName}" />

                            </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>GPF/TPF/PRAN Assumed(Set Yes for Dummy):</td>
                            <td>
                                <form:select path="sltGPFAssmued" id="sltGPFAssmued" style="width:34%;" class="form-control" disabled="true">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="Y">Yes</form:option>
                                    <form:option value="N">No</form:option>
                                </form:select>
                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Employee's ${emp.accttype} No.</td>
                            <td>
                        <html:hidden id="empid" path="empid" value="${emp.empid}"/>
                        <span><c:out value="${emp.gpfno}" />&nbsp;(HRMS ID:<c:out value="${emp.empid}" />)</span>
                        </td>
                        <td>&nbsp; </td>
                        <td></td>
                        </tr>

                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>GIS Type:</td>
                            <td>
                                <form:select id="gisType" path="gisType" style="width:34%;" class="form-control">
                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                    <c:forEach items="${gisList}" var="gis">
                                        <form:option value="${gis.schemeId}" label="${gis.schemeName}"/>
                                    </c:forEach>                                 
                                </form:select> 
                            </td>
                            <td align="left" colspan="1" rowspan="4" valign="top">
                                <fieldset>
                                    <legend align="left" style="border-bottom: 0px;">
                                        <span style="color: black"><img id="profilePhoto"  src='displayemployeeprofilephoto.htm' onerror="callNoImage()" width="120px" height="120px;" style="border-radius: 50%;"></span>
                                        <br/> <a href="javascript:UploadImage()" class="btn btn-info" style="margin-top:2px;"> Upload Profile Photo</a><br />
                                    </legend>
                                </fieldset>
                            </td>	

                            <td align="left" colspan="1" rowspan="4" valign="top">
                                <fieldset>
                                    <legend align="left" style="border-bottom: 0px;">
                                        <span style="color: black"><img id="jointPhoto"  src='displayjointPhoto.htm?empid=${emp.empid}' onerror="callNoImageJointPhoto()" width="120px" height="120px;"  style="border-radius: 50%;"></span>
                                        <br/> <a href="javascript:UploadJointPhoto()"  class="btn btn-danger" style="margin-top:2px;"> Upload Joint Photograph</a><br />
                                    </legend>
                                </fieldset>
                            </td>
                        </tr>

                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>GIS No:</td>
                            <td>
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <%--<form:input path="gisNo" class="form-control" style="width:30%;" id="gisNo" placeholder="Enter GIS No" readonly="true"/>--%>
                                    <form:input path="gisNo" class="form-control" style="width:30%;" id="gisNo" placeholder="Enter GIS No"/>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:input path="gisNo" class="form-control" style="width:30%;" id="gisNo" placeholder="Enter GIS No"/>
                                </c:if>
                            </td>
                            <td>&nbsp; </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>
                                Date of Birth(dd-MMM-yyyy):&nbsp;<span style="color: red">*</span>
                            </td>
                            <td>

                                <span id="dob"><c:out value="${emp.dob}" /></span>

                            </td>

                            <td align="left" >

                            </td>	
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>
                                Select Age of Superannuation (in Years)
                            </td>
                            <td>
                                <c:if test="${(empty emp.radyear1) || (emp.radyear1 eq '0')}">
                                    <input type="radio" name="radyear1" id="radyear1" value="58"  onclick="getSupDate(${emp.empid}, '58', '${emp.txtDos}', '${emp.dob}')" />58&nbsp;
                                    <input type="radio" name="radyear1" id="radyear2" value="60"  onclick="getSupDate(${emp.empid}, '60', '${emp.txtDos}', '${emp.dob}')"/>60&nbsp;
                                    <input type="radio" name="radyear1" id="radyear3" value="62"  onclick="getSupDate(${emp.empid}, '62', '${emp.txtDos}', '${emp.dob}')"/>62&nbsp;
                                    <input type="radio" name="radyear1" id="radyear4" value="65"  onclick="getSupDate(${emp.empid}, '65', '${emp.txtDos}', '${emp.dob}')"/>65&nbsp;
                                </c:if>
                                <c:if test="${emp.radyear1=='58'}">
                                    <input type="radio" name="radyear1" id="radyear1" value="58"  onclick="getSupDate(${emp.empid}, '58', '${emp.txtDos}', '${emp.dob}')" checked="true"/>58&nbsp;
                                    <input type="radio" name="radyear1" id="radyear2" value="60"  onclick="getSupDate(${emp.empid}, '60', '${emp.txtDos}', '${emp.dob}')"/>60&nbsp;
                                    <input type="radio" name="radyear1" id="radyear3" value="62"  onclick="getSupDate(${emp.empid}, '62', '${emp.txtDos}', '${emp.dob}')"/>62&nbsp;
                                    <input type="radio" name="radyear1" id="radyear4" value="65"  onclick="getSupDate(${emp.empid}, '65', '${emp.txtDos}', '${emp.dob}')"/>65&nbsp;
                                </c:if>
                                <c:if test="${emp.radyear1=='60'}">
                                    <input type="radio" name="radyear1" id="radyear1" value="58"  onclick="getSupDate(${emp.empid}, '58', '${emp.txtDos}', '${emp.dob}')" />58&nbsp;
                                    <input type="radio" name="radyear1" id="radyear2" value="60"  onclick="getSupDate(${emp.empid}, '60', '${emp.txtDos}', '${emp.dob}')" checked="true"/>60&nbsp;
                                    <input type="radio" name="radyear1" id="radyear3" value="62"  onclick="getSupDate(${emp.empid}, '62', '${emp.txtDos}', '${emp.dob}')"/>62&nbsp;
                                    <input type="radio" name="radyear1" id="radyear4" value="65"  onclick="getSupDate(${emp.empid}, '65', '${emp.txtDos}', '${emp.dob}')"/>65&nbsp;

                                </c:if>
                                <c:if test="${emp.radyear1=='62'}">
                                    <input type="radio" name="radyear1" id="radyear1" value="58"  onclick="getSupDate(${emp.empid}, '58', '${emp.txtDos}', '${emp.dob}')" />58&nbsp;
                                    <input type="radio" name="radyear1" id="radyear2" value="60"  onclick="getSupDate(${emp.empid}, '60', '${emp.txtDos}', '${emp.dob}')" />60&nbsp;
                                    <input type="radio" name="radyear1" id="radyear3" value="62"  onclick="getSupDate(${emp.empid}, '62', '${emp.txtDos}', '${emp.dob}')" checked="true"/>62&nbsp;
                                    <input type="radio" name="radyear1" id="radyear4" value="65"  onclick="getSupDate(${emp.empid}, '65', '${emp.txtDos}', '${emp.dob}')"/>65&nbsp;
                                </c:if>
                                <c:if test="${emp.radyear1=='65'}">
                                    <input type="radio" name="radyear1" id="radyear1" value="58"  onclick="getSupDate(${emp.empid}, '58', '${emp.txtDos}', '${emp.dob}')" />58&nbsp;
                                    <input type="radio" name="radyear1" id="radyear2" value="60"  onclick="getSupDate(${emp.empid}, '60', '${emp.txtDos}', '${emp.dob}')" />60&nbsp;
                                    <input type="radio" name="radyear1" id="radyear3" value="62"  onclick="getSupDate(${emp.empid}, '62', '${emp.txtDos}', '${emp.dob}')" />62&nbsp;
                                    <input type="radio" name="radyear1" id="radyear4" value="65"  onclick="getSupDate(${emp.empid}, '65', '${emp.txtDos}', '${emp.dob}')" checked="true"/>65&nbsp;
                                </c:if>
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr height="40px">
                            <td align="center" ><%=i++%>.</td>
                            <td>Date of Superannuation(dd-MMM-yyyy):</td>
                            <td>
                                <div class='input-group date' style="width:40%;" id='processDate'>
                                    <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                        <form:input class="form-control"  path="txtDos" id="txtDos1" readonly="true" />

                                    </c:if>
                                    <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                        <form:input class="form-control" id="txtDos1" path="txtDos" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </c:if>

                                </div>

                            </td>
                            <td>&nbsp; </td>
                            <td>&nbsp; </td>
                        </tr>

                        <tr height="40px">
                            <td align="center" ><%=i++%>.</td>
                            <td>Date from which in regular service with GoO(dd-MMM-yyyy):&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <div class='input-group date' style="width:40%;" id='joindategoo'>
                                    <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                        <form:input class="form-control"   path="joindategoo" readonly="true" />

                                    </c:if>
                                    <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                        <form:input class="form-control"  id="joindategoo1"  path="joindategoo" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </c:if>

                                </div>
                            </td>
                            <td>Time &nbsp;<span style="color: red">*</span></td>
                            <td>  
                                <form:select id="timeOfEntryGoo" class="form-control" path="timeOfEntryGoo" style="width:30%;">
                                    <c:if test="${emp.timeOfEntryGoo!='FN' && emp.timeOfEntryGoo!='AN'}">
                                <option value="">--Select One--</option>
                                <option value="FN">FORENOON</option>
                                <option value="AN">AFTERNOON</option>
                            </c:if>
                            <c:if test="${emp.timeOfEntryGoo=='FN'}">
                                <option value="">--Select One--</option>
                                <option value="FN" selected>FORENOON</option>
                                <option value="AN">AFTERNOON</option> 
                            </c:if>
                            <c:if test="${emp.timeOfEntryGoo=='AN'}">
                                <option value="">--Select One--</option>
                                <option value="FN">FORENOON</option>
                                <option value="AN"  selected>AFTERNOON</option> 
                            </c:if>
                        </form:select> 
                        </td>
                        </tr>

                        <tr height="40px">
                            <td align="center" ><%=i++%>.</td>
                            <td>Date of entry into Govt. service(dd-MMM-yyyy):&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <div class='input-group date' style="width:40%;" id='doeGov'>
                                    <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                        <form:input class="form-control" path="doeGov" readonly="true"/>
                                    </c:if>
                                    <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                        <form:input class="form-control"  id="doeGov1" path="doeGov" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </c:if>

                                </div>

                            </td>
                            <td >Time &nbsp;<span style="color: red">*</span></td>
                            <td>
                                <form:select id="txtwefTime" class="form-control" path="txtwefTime" style="width:30%;">
                                    <c:if test="${emp.txtwefTime!='FN' && emp.txtwefTime!='AN'}">
                                <option value="">--Select One--</option>
                                <option value="FN">FORENOON</option>
                                <option value="AN">AFTERNOON</option>
                            </c:if>
                            <c:if test="${emp.txtwefTime=='FN'}">
                                <option value="">--Select One--</option>
                                <option value="FN" selected>FORENOON</option>
                                <option value="AN">AFTERNOON</option> 
                            </c:if>
                            <c:if test="${emp.txtwefTime=='AN'}">
                                <option value="">--Select One--</option>
                                <option value="FN">FORENOON</option>
                                <option value="AN"  selected>AFTERNOON</option> 
                            </c:if>
                        </form:select>   
                        </td>
                        </tr>

                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Gender:&nbsp;<span style="color: red">*</span></td>
                            <td >


                                <c:if test="${emp.gender=='M'}">
                                    <form:radiobutton name="gender" path="gender" value="M" checked="true"/>
                                    Male&nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name="gender" path="gender" value="F"/>Female
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name="gender" path="gender" value="T"/>Transgender
                                </c:if>
                                <c:if test="${emp.gender=='F'}">
                                    <form:radiobutton name="gender" path="gender" value="M"/>
                                    Male&nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name="gender" path="gender" value="F" checked="true"/>Female
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name="gender" path="gender" value="T"/>Transgender
                                </c:if>
                                <c:if test="${emp.gender=='T'}">
                                    <form:radiobutton name="gender" path="gender" value="M"/>
                                    Male&nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name="gender" path="gender" value="F"/>Female
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name="gender" path="gender" value="T" checked="true"/>Transgender
                                </c:if>    

                                <c:if test="${empty emp.gender }">
                                    <form:radiobutton name="gender" path="gender" value="M" />
                                    Male&nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name="gender" path="gender" value="F"/>Female
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name="gender" path="gender" value="T"/>Transgender
                                </c:if>

                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Marital Status:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:select id="marital" path="marital" style="width:34%;" class="form-control">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${maritallist}" var="marital">
                                            <form:option value="${marital.maritalId}" label="${marital.maritalStatus}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                    <%--<form:hidden path="marital"/>--%>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:select id="marital" path="marital" style="width:34%;" class="form-control">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${maritallist}" var="marital">
                                            <form:option value="${marital.maritalId}" label="${marital.maritalStatus}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                </c:if>

                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Category:&nbsp;</td>
                            <td>
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:select id="category" path="category" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${categoryList}" var="category">
                                            <form:option value="${category.categoryid}" label="${category.categoryName}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                    <%--<form:hidden path="category"/>--%>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:select id="category" path="category" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${categoryList}" var="category">
                                            <form:option value="${category.categoryid}" label="${category.categoryName}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                </c:if>

                            </td>
                            <td>&nbsp; </td>
                        </tr>

                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Post</td>
                            <td colspan="3" width="">
                                <c:out value="${emp.spn}" />
                            </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Cadre</td>
                            <td colspan="3" width="">
                                <strong><c:out value="${emp.cadreId}" /></strong><br />
                                Do you want to change your cadre?
                                <input type="radio" name="change_cadre" id="change_cadre1" value="Y" onclick="javascript: $('#cadre_blk').slideDown();" /> <label for="change_cadre1">Yes</label>
                                <input type="radio" name="change_cadre" id="change_cadre2" value="N" checked="checked" onclick="javascript: $('#cadre_blk').slideUp();" /> <label for="change_cadre2">No</label>
                            </td>
                        </tr> 
                         <tr height="40px"  id="cadre_blk" style="display:none;">
                            <td >&nbsp;</td>
                            <td>&nbsp;</td>
                            <td colspan="3" >
                                <a href="JoiningCadreList.htm" class="btn btn-warning" role="button">Change Cadre</a> 
                              
                            </td>
                        </tr>                       

                        <tr>
                            <td align="center"><%=i++%>.</td>


                            <td>Post Group:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:select id="postGrpType" path="postGrpType" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${postgrouplist}" var="postgroup">
                                            <form:option value="${postgroup.value}" label="${postgroup.label}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                    <%--<form:hidden path="postGrpType"/>--%>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:select id="postGrpType" path="postGrpType" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${postgrouplist}" var="postgroup">
                                            <form:option value="${postgroup.value}" label="${postgroup.label}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                </c:if>

                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Height(in cm):&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:input path="height" class="form-control" style="width:40%;" id="height" onkeypress="return onlyNumbers(event)" maxlength="5" placeholder="Enter Height"/>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:input path="height" class="form-control" style="width:40%;" id="height" onkeypress="return onlyNumbers(event)" maxlength="5" placeholder="Enter Height"/>
                                </c:if>
                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Blood Group:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:select id="bloodgrp" path="bloodgrp" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${bloodGrpList}" var="bloodgrp">
                                            <form:option value="${bloodgrp.bloodgrpId}" label="${bloodgrp.bloodgrp}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                    <%--<form:hidden path="bloodgrp"/>--%>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:select id="bloodgrp" path="bloodgrp" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${bloodGrpList}" var="bloodgrp">
                                            <form:option value="${bloodgrp.bloodgrpId}" label="${bloodgrp.bloodgrp}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                </c:if>

                            </td>
                            <td>&nbsp; </td>
                        </tr>

                        <!--************ CHANGED BY PKM STARTS *********** -->     
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Declaration of Home Town:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:input path="homeTown" class="form-control" id="homeTown" placeholder="Please Enter HomeTown"/>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:input path="homeTown" class="form-control" id="homeTown" placeholder="Please Enter HomeTown"/>
                                </c:if>


                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <!--************ CHANGED BY PKM ENDS *********** -->     
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Religion:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:select id="religion" path="religion" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${religionList}" var="religion">
                                            <form:option value="${religion.religionId}" label="${religion.religion}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                    <%--<form:hidden path="religion"/>--%>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:select id="religion" path="religion" class="form-control" style="width:60%;">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <c:forEach items="${religionList}" var="religion">
                                            <form:option value="${religion.religionId}" label="${religion.religion}"/>
                                        </c:forEach>                                 
                                    </form:select> 
                                </c:if>

                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Domicile:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:input path="domicil" class="form-control" style="width:40%;" id="domicil" placeholder="Please Enter Domicil"/>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:input path="domicil" class="form-control" style="width:40%;" id="domicil" placeholder="Please Enter Domicil"/>
                                </c:if>


                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Personal Identification Mark:&nbsp;<span style="color: red">*</span></td>
                            <td colspan="5">
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:input path="idmark" class="form-control" id="idmark" style="width:60%;" placeholder="Please Enter If Any Identification Mark"/>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:input path="idmark" class="form-control" id="idmark" style="width:60%;" placeholder="Please Enter If Any Identification Mark"/>
                                </c:if>
                            </td>
                        </tr>
                        <form:hidden path="sltBank"/>
                        <form:hidden path="sltbranch"/>
                        <form:hidden path="bankaccno"/>

                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Mobile Number:&nbsp;<span style="color: red">*</span></td>
                            <td colspan="1">
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:input path="mobile" class="form-control" id="mobile" style="width:60%;" onkeypress="return phonenumber();" placeholder="Please Enter Mobile No" maxlength="10"/>
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:input path="mobile" class="form-control" id="mobile" style="width:60%;" onkeypress="return phonenumber();" placeholder="Please Enter Mobile No" maxlength="10"/>
                                </c:if>
                                <i style='color:red'>* Please update the Aadhaar Based Mobile</i>
                            </td>
                            <td>&nbsp;  </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Email:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <c:if test="${emp.ifprofileVerified eq 'Y'}">
                                    <form:input path="email" class="form-control" id="email" style="width:60%;"  placeholder="Please Enter Email" onkeydown="validateEmailtBox();" onkeyup="validateEmailtBox();" />
                                </c:if>
                                <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                    <form:input path="email" class="form-control" id="email" style="width:60%;"  placeholder="Please Enter Email" onkeydown="validateEmailtBox();" onkeyup="validateEmailtBox();"/>
                                    <input type="checkbox" id="chkemail" name="chkemail" value="Y" onclick="validateEmailcBox();"  />
                                    <label for="chkemail"> I don't have an Email</label><br>
                                </c:if>
                            </td>/
                            <td>&nbsp; </td>
                        </tr>
                    </table>


                </div>
            </div>

            <c:if test="${deptCode=='13'}">
                <div style=" margin-bottom: 5px;" class="panel panel-info">
                    <div class="panel-body">
                        Additional Information
                        <table border="0" cellpadding="0" cellspacing="0"  width="100%">
                            <tr>
                                <td width="5%">&nbsp;</td>
                                <td width="25%">&nbsp;</td>
                                <td width="30%" align="center">&nbsp;</td>
                                <td width="15%" align="center">&nbsp;</td>
                                <td width="30%" align="center">&nbsp;</td>

                            </tr>
                            <tr height="40px">
                                <td align="center"><%=i++%>.</td>
                                <td>Staff Type:</td>
                                <td colspan="3" width="">

                                    <c:if test="${empty emp.staffType }">
                                        <form:radiobutton name="staffType" path="staffType" id='staffType' value="T" onclick="staff_type(this.value)" />
                                        Teaching Staff&nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffType" path="staffType" id='staffType' value="NT" onclick="staff_type(this.value)"/>NON-Teaching Staff                                 

                                    </c:if>
                                    <c:if test="${emp.staffType eq 'T'}">
                                        <form:radiobutton name="staffType" path="staffType" id='staffType' value="T" onclick="staff_type(this.value)" checked='true' />
                                        Teaching Staff&nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffType" path="staffType" id='staffType' value="NT" onclick="staff_type(this.value)"/>NON-Teaching Staff                                 

                                    </c:if> 
                                    <c:if test="${emp.staffType eq 'NT'}">
                                        <form:radiobutton name="staffType" path="staffType" id='staffType' value="T" onclick="staff_type(this.value)" />
                                        Teaching Staff&nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffType" path="staffType" id='staffType' value="NT" onclick="staff_type(this.value)"  checked='true'/>NON-Teaching Staff                                 

                                    </c:if>       
                                </td>
                            </tr>



                            <tr height="40px"  id='id_staff_category' >
                                <td align="center"><%=i++%>.</td>
                                <td>Staff Category:</td>
                                <td colspan="3" width="">

                                    <c:if test="${empty emp.staffCategory }">
                                        <form:radiobutton name="staffCategory" path="staffCategory" value="Lecturer(CB)"  class='classStaffCategory' onclick="staff_category(this.value)"/>
                                        Lecturer(CB)&nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffCategory" path="staffCategory"   value="Junior Lecturer(State Scale)" class='classStaffCategory' onclick="staff_category(this.value)"/>Junior Lecturer(State Scale)                                 
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffCategory" path="staffCategory"  value="Lecturer(State Scale)" class='classStaffCategory' onclick="staff_category(this.value)"/>Lecturer(State Scale) 
                                    </c:if>

                                    <c:if test="${emp.staffCategory eq 'Lecturer(CB)'}">
                                        <form:radiobutton name="staffCategory" path="staffCategory" value="Lecturer(CB)"  class='classStaffCategory' onclick="staff_category(this.value)"  checked='true'/>
                                        Lecturer(CB)&nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffCategory" path="staffCategory"   value="Junior Lecturer(State Scale)" class='classStaffCategory' onclick="staff_category(this.value)"/>Junior Lecturer(State Scale)                                 
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffCategory" path="staffCategory"  value="Lecturer(State Scale)" class='classStaffCategory' onclick="staff_category(this.value)"/>Lecturer(State Scale)                          

                                    </c:if> 

                                    <c:if test="${emp.staffCategory eq 'Junior Lecturer(State Scale)'}">
                                        <form:radiobutton name="staffCategory" path="staffCategory" value="Lecturer(CB)"  class='classStaffCategory' onclick="staff_category(this.value)" />
                                        Lecturer(CB)&nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffCategory" path="staffCategory"   value="Junior Lecturer(State Scale)" class='classStaffCategory' onclick="staff_category(this.value)"  checked='true'/>Junior Lecturer(State Scale)                                 
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffCategory" path="staffCategory"  value="Lecturer(State Scale)" class='classStaffCategory' onclick="staff_category(this.value)"/>Lecturer(State Scale)                          

                                    </c:if>  
                                    <c:if test="${emp.staffCategory eq 'Lecturer(State Scale)'}">
                                        <form:radiobutton name="staffCategory" path="staffCategory" value="Lecturer(CB)"  class='classStaffCategory' onclick="staff_category(this.value)" />
                                        Lecturer(CB)&nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffCategory" path="staffCategory"   value="Junior Lecturer(State Scale)" class='classStaffCategory' onclick="staff_category(this.value)"/>Junior Lecturer(State Scale)                                 
                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffCategory" path="staffCategory"  value="Lecturer(State Scale)" class='classStaffCategory' onclick="staff_category(this.value)"  checked='true'/>Lecturer(State Scale)                          

                                    </c:if>     

                                </td>
                            </tr>
                            <tr height="40px" id='id_placement_type' >
                                <td align="center"><%=i++%>.</td>
                                <td>Placement Type:</td>
                                <td colspan="3" width="">

                                    <c:if test="${empty emp.staffPlacement }">
                                        <form:radiobutton name="staffPlacement" path="staffPlacement" id='staffPlacement' value="Direct Recruitment" class='classStaffPlacement' onclick="staff_Placement(this.value)" />
                                        Direct Recruitment&nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffPlacement" path="staffPlacement" id='staffPlacement' value="Placement from State Scale" class='classStaffPlacement' onclick="staff_Placement(this.value)" />Placement from State Scale                            

                                    </c:if>
                                    <c:if test="${emp.staffPlacement eq 'Direct Recruitment' }">
                                        <form:radiobutton name="staffPlacement" path="staffPlacement" id='staffPlacement' value="Direct Recruitment" class='classStaffPlacement' onclick="staff_Placement(this.value)" checked='true'/>
                                        Direct Recruitment&nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffPlacement" path="staffPlacement" id='staffPlacement' value="Placement from State Scale" class='classStaffPlacement' onclick="staff_Placement(this.value)" />Placement from State Scale                            

                                    </c:if>  
                                    <c:if test="${emp.staffPlacement eq 'Placement from State Scale' }">
                                        <form:radiobutton name="staffPlacement" path="staffPlacement" id='staffPlacement' value="Direct Recruitment" class='classStaffPlacement' onclick="staff_Placement(this.value)" />
                                        Direct Recruitment&nbsp;&nbsp;&nbsp;&nbsp;
                                        <form:radiobutton name="staffPlacement" path="staffPlacement" id='staffPlacement' value="Placement from State Scale" class='classStaffPlacement' onclick="staff_Placement(this.value)" checked='true'/>Placement from State Scale                            

                                    </c:if>     
                                </td>
                            </tr>
                            <tr height="40px" id='id_psc_year' >
                                <td align="center"><%=i++%>.</td>
                                <td>PSC Year:</td>
                                <td colspan="3" width="">
                                    <form:input path="pscYear" class="form-control" id="pscYear" style="width:60%;"  placeholder="Please Enter PSC Year" maxlength="4"/>

                                </td>
                            </tr>
                            <tr height="40px" id='id_staff_cdesignation' >
                                <td align="center"><%=i++%>.</td>
                                <td>Current Designation:</td>
                                <td colspan="3" width="">

                                    <form:select id="cDesignation" class="form-control" path="cDesignation"  style="width:30%;">
                                <option value="">--Select One--</option>
                                <option value="Asst.Prof(Stage-I)"  <c:if test="${emp.cDesignation eq 'Asst.Prof(Stage-I)' }">  selected='true' </c:if> >Asst.Prof(Stage-I)</option>
                                <option value="Asst.Prof(Stage-II)"  <c:if test="${emp.cDesignation eq 'Asst.Prof(Stage-II)' }">  selected='true' </c:if>>Asst.Prof(Stage-II)</option>                            
                                <option value="Asst.Prof(Stage-III)"  <c:if test="${emp.cDesignation eq 'Asst.Prof(Stage-III)' }">  selected='true' </c:if>>Asst.Prof(Stage-III)</option>
                                <option value="Associate Professor"  <c:if test="${emp.cDesignation eq 'Associate Professor' }">  selected='true' </c:if>>Associate Professor</option>
                                <option value="Professor"  <c:if test="${emp.cDesignation eq 'Professor' }">  selected='true' </c:if>>Professor</option>
                            </form:select> 
                            </td>
                            </tr>
                            <tr height="40px" id='id_staff_idesignation' >
                                <td align="center"><%=i++%>.</td>
                                <td>Initial Designation:</td>
                                <td colspan="3" width="">

                                    <form:select id="iDesignation" class="form-control" path="iDesignation"  style="width:30%;">
                                <option value="">--Select One--</option>
                                <option value="Asst.Prof(Stage-I)"  <c:if test="${emp.iDesignation eq 'Asst.Prof(Stage-I)' }">  selected='true' </c:if> >Asst.Prof(Stage-I)</option>
                                <option value="Asst.Prof(Stage-II)"  <c:if test="${emp.iDesignation eq 'Asst.Prof(Stage-II)' }">  selected='true' </c:if>>Asst.Prof(Stage-II)</option>                            
                                <option value="Asst.Prof(Stage-III)"  <c:if test="${emp.iDesignation eq 'Asst.Prof(Stage-III)' }">  selected='true' </c:if>>Asst.Prof(Stage-III)</option>
                                <option value="Associate Professor"  <c:if test="${emp.iDesignation eq 'Associate Professor' }">  selected='true' </c:if>>Associate Professor</option>
                                <option value="Professor"  <c:if test="${emp.iDesignation eq 'Professor' }">  selected='true' </c:if>>Professor</option>
                            </form:select> 
                            </td>
                            </tr>
                        </table>
                    </div>
                </div> 
            </c:if>

            <script type="text/javascript">
                $(function() {
                    $('#txtDos1').datetimepicker({
                        format: 'D-MMM-YYYY',
                        useCurrent: false,
                        ignoreReadonly: true
                    });
                    $('#joindategoo1').datetimepicker({
                        format: 'D-MMM-YYYY',
                        useCurrent: false,
                        ignoreReadonly: true
                    });
                    $('#doeGov1').datetimepicker({
                        format: 'D-MMM-YYYY',
                        useCurrent: false,
                        ignoreReadonly: true
                    });
                });

            </script>
            <div style=" margin-top: 0.5px;" class="panel panel-info">
                <div class="panel-body">
                    <div class="pull-left">

                        <c:if test="${emp.ifprofileVerified ne 'Y'  || deptCode eq '13'}">
                            <input type="submit" name="save" value="Save" class="btn btn-primary" onclick="return saveProfileData();"/>
                        </c:if>
                        <input type="submit" name="back" value="Back" class="btn btn-primary" />
                    </div>
                </div> 
            </div>
        </form:form>

    </body>
</html>
