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
        <link rel="stylesheet" type="text/css" href="resources/css/colorbox.css"/>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
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
            function saveProfileData() {

                var dob = $('#dob').html();
                if (dob == '') {
                    alert("Date of Birth can not be blank");
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
                //  alert(stremail);
                if (stremail == '' && $("#chkemail")[0].checked == false) {
                    alert("Please enter your email. Else check the box that you don't have any email.");
                    $('#email').focus();
                    return false;
                }

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
            function getSupDate(empId, retiredYear, supanndate, dob)
            {

                var empid = document.getElementById("empid").value;
                var url = "getSuperannuationData.htm?empId=" + empid + "&retiredYear=" + retiredYear;
                $.getJSON(url, function(data) {
                    $("#txtDos").val(data.superandate);

                });
            }
            function callNoImage() {

                var userPhoto = document.getElementById('profilePhoto');
                userPhoto.src = "images/NoEmployee.png";

            }
            function UploadImage() {
                var url = 'fileUploadForm.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "50%", overlayClose: false, onClosed: refreshImage});
            }
            /*   function UploadJointPhoto()
             {
             var url = 'UploadJointPhotoForm.htm';
             $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "50%", overlayClose: false, onClosed: refreshImage});
             }*/
            function callNoImageJointPhoto() {

                var userPhoto = document.getElementById('jointPhoto');
                userPhoto.src = "images/NoEmployee.png";

            }

            function refreshImage() {
                $("#profilePhoto").attr('src', 'profilephoto.htm?' + 'date=' + (new Date()).getTime());
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
            function UploadJointPhoto()
            {
                var url = 'UploadJointPhoto.htm';
                $.colorbox({href: url, iframe: true, open: true, width: "70%", height: "50%", overlayClose: false, onClosed: refreshImage});
            }

        </script>
        <style type="text/css">
            .card {
                background-color: #ffffff;
                border-radius: 6px;
                box-shadow: 0 2px 2px rgba(204, 197, 185, 0.5);
                color: #252422;
                margin-bottom: 20px;
                position: relative;
                z-index: 1;
            }
            .card .image {
                width: 100%;
                overflow: hidden;
                height: 260px;
                border-radius: 6px 6px 0 0;
                position: relative;
                -webkit-transform-style: preserve-3d;
                -moz-transform-style: preserve-3d;
                transform-style: preserve-3d;
            }
            .card .image img {
                width: 100%;
            }
            .card .content {
                padding: 15px 15px 10px 15px;
            }
            .card .header {
                padding: 20px 20px 0;
            }
            .card .description {
                font-size: 16px;
                color: #66615b;
                text-transform:capitalize;
            }
            .card h6 {
                font-size: 12px;
                margin: 0;
            }
            .card .category,
            .card label {
                font-size: 14px;
                font-weight: 400;
                color: #9A9A9A;
                margin-bottom: 0px;
            }
            .card .category i,
            .card label i {
                font-size: 16px;
            }
            .card label {
                font-size: 15px;
                margin-bottom: 5px;
            }
            .card .title {
                margin: 0;
                color: #252422;
                font-weight: 300;
            }
            .card .avatar {
                width: 50px;
                height: 50px;
                overflow: hidden;
                border-radius: 50%;
                margin-right: 5px;
            }
            .card .footer {
                padding: 0;
                line-height: 30px;
            }
            .card .footer .legend {
                padding: 5px 0;
            }
            .card .footer hr {
                margin-top: 5px;
                margin-bottom: 5px;
            }
            .card .stats {
                color: #a9a9a9;
                font-weight: 300;
            }
            .card .stats i {
                margin-right: 2px;
                min-width: 15px;
                display: inline-block;
            }
            .card .footer div {
                display: inline-block;
            }
            .card .author {
                font-size: 12px;
                font-weight: 600;
                text-transform: uppercase;
            }
            .card .author i {
                font-size: 14px;
            }
            .card.card-separator:after {
                height: 100%;
                right: -15px;
                top: 0;
                width: 1px;
                background-color: #DDDDDD;
                content: "";
                position: absolute;
            }
            .card .ct-chart {
                margin: 30px 0 30px;
                height: 245px;
            }
            .card .table tbody td:first-child,
            .card .table thead th:first-child {
                padding-left: 15px;
            }
            .card .table tbody td:last-child,
            .card .table thead th:last-child {
                padding-right: 15px;
            }
            .card .alert {
                border-radius: 4px;
                position: relative;
            }
            .card .alert.alert-with-icon {
                padding-left: 65px;
            }
            .card .icon-big {
                font-size: 3em;
                min-height: 64px;
            }
            .card .numbers {
                font-size: 2em;
                text-align: right;
            }
            .card .numbers p {
                margin: 0;
            }
            .card ul.team-members li {
                padding: 10px 0px;
            }
            .card ul.team-members li:not(:last-child) {
                border-bottom: 1px solid #F1EAE0;
            }

            .card-user .image {
                border-radius: 8px 8px 0 0;
                height: 150px;
                position: relative;
                overflow: hidden;
            }
            .card-user .image img {
                width: 100%;
            }
            .card-user .image-plain {
                height: 0;
                margin-top: 110px;
            }
            .card-user .author {
                text-align: center;
                text-transform: none;
                margin-top: -65px;
            }
            .card-user .author .title {
                color: #403D39;
            }
            .card-user .author .title small {
                color: #ccc5b9;
            }
            .card-user .avatar {
                width: 140px;
                height: 140px;
                border-radius: 50%;
                position: relative;
                margin-bottom: 15px;
            }
            .card-user .avatar.border-white {
                border: 5px solid #FFFFFF;
            }
            .card-user .avatar.border-gray {
                border: 5px solid #ccc5b9;
            }
            .card-user .title {
                font-weight: 600;
                line-height: 24px;
            }
            .card-user .description {
                text-transform: fullsize-kana;
                margin-top: 10px;
            }
            .card-user .content {
                min-height: 200px;
            }
            .card-user.card-plain .avatar {
                height: 190px;
                width: 190px;
            }

            .card-map .map {
                height: 500px;
                padding-top: 20px;
            }
            .card-map .map > div {
                height: 100%;
            }

            .card-user .footer,
            .card-price .footer {
                padding: 5px 15px 10px;
            }
            .card-user hr,
            .card-price hr {
                margin: 5px 15px;
            }

            .card-plain {
                background-color: transparent;
                box-shadow: none;
                border-radius: 0;
            }
            .card-plain .image {
                border-radius: 4px;
            }
            .card span{
                color: #252422;
                font-weight: 500;
                line-height: 24px;
                font-size: 16px;
            }
            .lblval{
                text-transform: capitalize;
                color: #252422;
                font-weight: 500;
                line-height: 24px;
                font-size: 16px;
            }
        </style>
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="PROFILEPAGESB" />
        </jsp:include>
        <form:form name="myForm"  action="empprofile.htm" method="POST" commandName="emp">
            <form:hidden id="empid" path="empid" value="${emp.empid}"/>
            <form:hidden path="sltGPFAssmued"/>
            <div style=" margin-bottom: 5px;" class="panel panel-info">
                <div class="panel-body">
                    <div align="center"><h3 style='color:green'>HRMS ID : <c:out value="${emp.empid}" /></h3></div>
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
                            <td>Employee's ${emp.accttype} No.:</td>
                            <td >
                        <html:hidden id="empid" path="empid" value="${emp.empid}"/>
                        <span><c:out value="${emp.gpfno}" /></span>
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
                                        <span style="color: black"><img id="profilePhoto"  src='profilephoto.htm' onerror="callNoImage()" width="120px" height="120px;" style="border-radius: 50%;"></span>
                                        <br/> <a href="javascript:UploadImage()" class="btn btn-info" style="margin-top:2px;"> Upload Profile Photo</a><br />
                                    </legend>
                                </fieldset>
                            </td>	

                            <!-- <td align="left" colspan="1" rowspan="4" valign="top">
                                 <fieldset>
                                     <legend align="left" style="border-bottom: 0px;">
                                         <span style="color: black"><img id="jointPhoto"  src='jointPhoto.htm' onerror="callNoImageJointPhoto()" width="120px" height="120px;"  style="border-radius: 50%;"></span>
                                         <br/> <a href="javascript:UploadJointPhoto()"  class="btn btn-danger" style="margin-top:2px;"> Upload Joint Photograph</a><br />
                                     </legend>
                                 </fieldset>
                             </td>-->
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
                                <form:input  path="gisNo" class="form-control" style="width:30%;" id="gisNo" placeholder="Enter GIS No"/>
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
                        <!--<tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>
                                Select Age of Superannuation (in Years)
                            </td>
                            <td>

                                <span id="radyear2"><c:out value="${emp.radyear1}" /></span>

                            </td>-->
                        <!--<td>
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
                    </tr>-->
                        <tr height="40px">
                            <td align="center" ><%=i++%>.</td>
                            <td>Date of Superannuation(dd-MMM-yyyy):</td>

                            <td>

                                <span id="txtDos"><c:out value="${emp.txtDos}" /></span>

                            </td>
                            <!--<div class='input-group date' style="width:40%;" id='processDate'>
                         
                            <span id="txtDos1"><c:out value="${emp.txtDos}" /></span>

                        </div>-->

                            <td>&nbsp; </td>
                            <td>&nbsp; </td>
                        </tr>

                        <tr height="40px">
                            <td align="center" ><%=i++%>.</td>
                            <td>Date from which in regular service with GoO(dd-MMM-yyyy):&nbsp;<span style="color: red">*</span></td>
                            <td><span id="joindategoo1"><c:out value="${emp.joindategoo}" /> <span id="timeOfEntryGoo"><c:out value="${emp.timeOfEntryGoo}" /></span></td>

                            <td>&nbsp; </td>
                            <td>&nbsp; </td>
                        </tr>

                        <tr height="40px">
                            <td align="center" ><%=i++%>.</td>
                            <td>Date of entry into Govt. service(dd-MMM-yyyy): &nbsp;<span style="color: red">*</span></td>

                            <td>

                                <span id="doeGov"><c:out value="${emp.doeGov}" />${emp.txtwefTime}</span>

                            </td>


                            <td >&nbsp;</td>
                            <td >&nbsp;</td>
                        </tr>

                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Gender:&nbsp;<span style="color: red">*</span></td>
                            <td >
                                <c:if test="${emp.gender=='M'}">
                                    <form:radiobutton name='gender' path="gender" value="M" checked="true"/>
                                    Male&nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name='gender' path="gender" value="F"/>Female
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name='gender' path="gender" value="T"/>Transgender
                                </c:if>
                                <c:if test="${emp.gender=='F'}">
                                    <form:radiobutton name='gender' path="gender" value="M"/>
                                    Male&nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name='gender' path="gender" value="F" checked="true"/>Female
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name='gender' path="gender" value="T"/>Transgender
                                </c:if>
                                <c:if test="${emp.gender=='T'}">
                                    <form:radiobutton name='gender' path="gender" value="M"/>
                                    Male&nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name='gender' path="gender" value="F"/>Female
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name='gender' path="gender" value="T" checked="true"/>Transgender
                                </c:if>    

                                <c:if test="${emp.gender==''}">
                                    <form:radiobutton name='gender' path="gender" value="M" />
                                    Male&nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name='gender' path="gender" value="F"/>Female
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <form:radiobutton name='gender' path="gender" value="T"/>Transgender
                                </c:if>
                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Marital Status:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <form:select id="marital" path="marital" style="width:34%;" class="form-control">
                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                    <c:forEach items="${maritallist}" var="marital">
                                        <form:option value="${marital.maritalId}" label="${marital.maritalStatus}"/>
                                    </c:forEach>                                 
                                </form:select> 
                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Category:&nbsp;</td>
                            <td>
                                <form:select id="category" path="category" class="form-control" style="width:60%;">
                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                    <c:forEach items="${categoryList}" var="category">
                                        <form:option value="${category.categoryid}" label="${category.categoryName}"/>
                                    </c:forEach>                                 
                                </form:select> 
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
                                <strong style="color:red"><c:out value="${emp.cadreId}" />
                                    <c:if test="${empty emp.cadreId || emp.cadreId eq 'OTHERS CADRE'}">
                                        &nbsp;&nbsp;<span >(Please update the cadre at DDO end.)</span>
                                    </strong>
                                </c:if><br/>                
                                Do you want to change your cadre?
                                <input type="radio" name="change_cadre" id="change_cadre1" value="Y" onclick="javascript: $('#cadremsg').slideDown();" /> <label for="change_cadre1">Yes</label>
                                <input type="radio" name="change_cadre" id="change_cadre2" value="N" checked="checked"  onclick="javascript: $('#cadremsg').slideUp();"  /> <label for="change_cadre2">No</label>
                                <div id="cadremsg" class="alert alert-danger" style="font-size:14pt;display:none;border:1px solid #FF0000;color:#FF0000;">Please update the cadre at DDO end.</div>



                            </td>
                        </tr> 
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Post Group:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <form:select id="postGrpType" path="postGrpType" class="form-control" style="width:60%;">
                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                    <c:forEach items="${postgrouplist}" var="postgroup">
                                        <form:option value="${postgroup.value}" label="${postgroup.label}"/>
                                    </c:forEach>                                 
                                </form:select> 

                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Height(in cm):&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <form:input path="height" class="form-control" style="width:40%;" id="height" onkeypress="return onlyNumbers(event)" maxlength="5" placeholder="Enter Height"/>
                            </td>
                            <td>&nbsp; </td>
                        </tr>

                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Blood Group:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <form:select id="bloodgrp" path="bloodgrp" class="form-control" style="width:60%;">
                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                    <c:forEach items="${bloodGrpList}" var="bloodgrp">
                                        <form:option value="${bloodgrp.bloodgrpId}" label="${bloodgrp.bloodgrp}"/>
                                    </c:forEach>                                 
                                </form:select> 
                            </td>
                            <td>&nbsp; </td>
                        </tr>

                        <!--************ CHANGED BY PKM STARTS *********** -->     
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Declaration of Home Town:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <form:input  path="homeTown" class="form-control" id="homeTown" placeholder="Please Enter HomeTown"/>

                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <!--************ CHANGED BY PKM ENDS *********** -->     
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Religion:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <form:select id="religion" path="religion" class="form-control" style="width:60%;">
                                    <form:option value="" label="Select" cssStyle="width:30%"/>
                                    <c:forEach items="${religionList}" var="religion">
                                        <form:option value="${religion.religionId}" label="${religion.religion}"/>
                                    </c:forEach>                                 
                                </form:select> 
                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Domicile:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <form:input  path="domicil" class="form-control" style="width:40%;" id="domicil" placeholder="Please Enter Domicil"/>

                            </td>
                            <td>&nbsp; </td>
                        </tr>
                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Personal Identification Mark:&nbsp;<span style="color: red">*</span></td>
                            <td colspan="5">
                                <form:input path="idmark" class="form-control" id="idmark" style="width:60%;" placeholder="Please Enter If Any Identification Mark"/>
                            </td>
                        </tr>

                        <tr height="40px">
                            <td align="center"><%=i++%>.</td>
                            <td>Mobile Number:&nbsp;<span style="color: red">*</span></td>
                            <td>
                                <form:input path="mobile" class="form-control" id="mobile" style="width:60%;" placeholder="Please Enter Mobile No"/>
                                <i style='color:red'>* Please update the Aadhaar linked Mobile Number</i>
                            </td>
                            <td>&nbsp; </td>
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



                    <script type="text/javascript">
                        $(function() {
                            $('#txtDos').datetimepicker({
                                format: 'D-MMM-YYYY',
                                useCurrent: false,
                                ignoreReadonly: true
                            });
                            $('#joindategoo').datetimepicker({
                                format: 'D-MMM-YYYY',
                                useCurrent: false,
                                ignoreReadonly: true
                            });
                            $('#doeGov').datetimepicker({
                                format: 'D-MMM-YYYY',
                                useCurrent: false,
                                ignoreReadonly: true
                            });
                        });
                    </script>

                </div>
            </div>
            <div style=" margin-top: 0.5px;" class="panel panel-info">
                <div class="panel-body">
                    <div class="pull-left">
                        <c:if test="${not empty empprofilecompletedstatus.dateOfProfileCompletion}">
                            <span style="display:block;text-align: center;font-weight: bold;font-size: 14px;color: red;">Profile completed on <c:out value="${empprofilecompletedstatus.dateOfProfileCompletion}"/> from IP <c:out value="${empprofilecompletedstatus.ipOfProfileCompletion}"/></span>
                        </c:if>
                        <c:if test="${empty empprofilecompletedstatus.dateOfProfileCompletion}">                           

                            <c:if test="${emp.ifprofileVerified ne 'Y'}">
                                <input type="submit" class="btn btn-primary" name="action" value="Save" onclick="return saveProfileData();"/>  
                            </c:if>
                        </c:if>   
                        <input type="submit" name="action" value="Back" class="btn btn-primary" />
                    </div>
                </div> 
            </div>
        </form:form>

    </body>
</html>
