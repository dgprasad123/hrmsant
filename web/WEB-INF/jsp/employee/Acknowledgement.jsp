
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
            function validateEmail()
            {
                if ($('#email').val())
                {
                    $("#chkemail")[0].checked = false;
                }
                if ($("#chkemail")[0].checked)
                {
                    $('#email').val('');
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
                    <h1 style="font-size:18pt;text-align:center;margin-top:0px;font-weight:bold;">Profile Acknowledgement Form</h1>
                    <table class="table table-striped">
                        <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                            <td colspan="4">Basic Profile Info</td>
                        </tr>
                        <tr>
                            <td width="25%" align="right">Employee's Full Name:</td>
                            <td width="20%" style="font-weight:bold;">${emp.empName}</td>
                            <td width="30%" align="right">GPF/TPF/PRAN Assumed(Set Yes for Dummy):</td>
                            <td style="font-weight:bold;"><c:if test="${emp.sltGPFAssmued eq 'N'}">No</c:if><c:if test="${emp.sltGPFAssmued eq 'Y'}">Yes</c:if></td>
                            </tr>
                            <tr>
                                    <td align="right">Employee's ${emp.accttype} No.:</td>
                            <td style="font-weight:bold;">${emp.gpfno}</td>
                            <td align="right">HRMS ID:</td>
                            <td style="font-weight:bold;">${emp.empid}</td>
                        </tr> 
                        <tr>
                            <td align="right">GIS Type:</td>
                            <td style="font-weight:bold;">${emp.gisType}</td>
                            <td align="right">GIS No.:</td>
                            <td style="font-weight:bold;">${emp.gisNo}</td>
                        </tr> 
                        <tr>
                            <td align="right">Date of Birth(dd-MMM-yyyy):</td>
                            <td style="font-weight:bold;">${emp.dob}</td>
                            <td align="right">Date of Superannuation:</td>
                            <td style="font-weight:bold;">${emp.txtDos}</td>
                        </tr> 
                        <tr>
                            <td align="right">Date from which in regular service with GoO(dd-MMM-yyyy)::</td>
                            <td style="font-weight:bold;">${emp.joindategoo} ${$.timeOfEntryGoo}</td>
                            <td align="right">Date of entry into Govt. service(dd-MMM-yyyy):</td>
                            <td style="font-weight:bold;">${emp.doeGov} ${emp.txtwefTime}</td>
                        </tr>  
                        <tr>
                            <td align="right">Gender:</td>
                            <td style="font-weight:bold;">
                                <c:if test="${emp.gender=='M'}">Male</c:if>
                                <c:if test="${emp.gender=='F'}">Female</c:if>
                                <c:if test="${emp.gender=='T'}">Transgender</c:if>
                                </td>
                                <td align="right">Marital Status:</td>
                                <td style="font-weight:bold;">
                                <c:if test="${emp.marital=='1'}">Married</c:if>
                                <c:if test="${emp.marital!='1'}">Unmarried</c:if>
                                </td>
                            </tr>   
                            <tr>
                                <td align="right">Category:</td>
                                <td style="font-weight:bold;">${emp.category}</td>
                            <td align="right">Post Group:</td>
                            <td style="font-weight:bold;">${emp.postGrpType}</td>
                        </tr>
                        <tr>
                            <td align="right">Height:</td>
                            <td style="font-weight:bold;">${emp.height}</td>
                            <td align="right">Blood Group:</td>
                            <td style="font-weight:bold;">${emp.bloodgrp}</td>
                        </tr> 
                        <tr>
                            <td align="right">Declaration of Home Town:</td>
                            <td style="font-weight:bold;">${emp.homeTown}</td>
                            <td align="right">Religion:</td>
                            <td style="font-weight:bold;">${emp.religion}</td>
                        </tr> 
                        <tr>
                            <td align="right">Domicile:</td>
                            <td style="font-weight:bold;">${emp.domicil}</td>
                            <td align="right">Personal Identification Mark:</td>
                            <td style="font-weight:bold;">${emp.idmark}</td>
                        </tr> 
                        <tr>
                            <td align="right">Mobile Number:</td>
                            <td style="font-weight:bold;">${emp.mobile}</td>
                            <td align="right">Email Address:</td>
                            <td style="font-weight:bold;">${emp.email}</td>
                        </tr> 
                        <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                            <td colspan="4">Permanent Address:</td>
                        </tr>
                        <tr>
                            <td align="right">House No/Flat No/Plot No/Block</td>
                             <td style="font-weight:bold;">${address.address}</td>
                             <td align="right">State:</td>
                             <td style="font-weight:bold;">${address.stateCode}</td>
                            </tr>
                             <tr>
                            <td align="right">District:</td>
                             <td style="font-weight:bold;">${address.distCode}</td>
                             <td align="right">Block</td>
                             <td style="font-weight:bold;">${address.blockCode}</td>
                            </tr>
                             <tr>
                            <td align="right">Police Station:</td>
                             <td style="font-weight:bold;">${address.psCode}</td>
                             <td align="right">Post Office:</td>
                             <td style="font-weight:bold;">${address.postCode}</td>
                            </tr>
                             <tr>
                            <td align="right">Village/Location:</td>
                             <td style="font-weight:bold;">${address.villageCode}</td>
                             <td align="right">Pin:</td>
                             <td style="font-weight:bold;">${address.pin}</td>
                            </tr>
                                <tr>
                            <td align="right">Telephone:</td>
                             <td style="font-weight:bold;" colspan="3">${address.stdCode} ${address.telephone} </td>
                             
                            </tr>
                             <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                            <td colspan="4">Present Address:</td>
                        </tr>
                        <tr>
                            <td align="right">House No/Flat No/Plot No/Block</td>
                             <td style="font-weight:bold;">${address.prAddress}</td>
                             <td align="right">State:</td>
                             <td style="font-weight:bold;">${address.prStateCode}</td>
                            </tr>
                             <tr>
                            <td align="right">District:</td>
                             <td style="font-weight:bold;">${address.prDistCode}</td>
                             <td align="right">Block</td>
                             <td style="font-weight:bold;">${address.prBlockCode}</td>
                            </tr>
                             <tr>
                            <td align="right">Police Station:</td>
                             <td style="font-weight:bold;">${address.prPsCode}</td>
                             <td align="right">Post Office:</td>
                             <td style="font-weight:bold;">${address.prPostCode}</td>
                            </tr>
                             <tr>
                            <td align="right">Village/Location:</td>
                             <td style="font-weight:bold;">${address.prVillageCode}</td>
                             <td align="right">Pin:</td>
                             <td style="font-weight:bold;">${address.prPin}</td>
                            </tr>
                                <tr>
                            <td align="right">Telephone:</td>
                             <td style="font-weight:bold;" colspan="3">${address.prStdCode} ${address.telephone} </td>
                             
                            </tr>
                                  <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                            <td colspan="4">Employee Identity:</td>
                        </tr>
                            <tr>
                            <tr style="font-weight:bold;">
                                <td colspan="4"><input type="checkbox" />
                                    I hereby declared that all the information furnished by me are true to the best of my knowledge.</td>
                            </tr>

                    </table>


                    </div>
                </div>

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


        </form:form>

    </body>
</html>
