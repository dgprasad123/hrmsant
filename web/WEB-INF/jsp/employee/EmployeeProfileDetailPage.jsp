<%-- 
    Document   : EmployeeProfileDetailPage
    Created on : 30 Mar, 2019, 11:05:31 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#chkProfileAcknowledge').change(function() {
                    if (this.checked) {
                        $('.btnSubmit').prop("disabled", false);
                    } else {
                        $('.btnSubmit').prop("disabled", true);
                    }
                }).change();
                
                profileAcknowledgementValidation();
            });
            function verifyDelineData() {
                if ($('#txtDeclineReason').val() == "") {
                    alert("Please Enter Reason for Decline");
                    return false;
                } else {
                    if (confirm("Äre you sure to Decline?")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            var flag = false;
            function profileAcknowledgementValidation() {
                var errormsg = "";
                if ($("#dob").val() == "") {
                    errormsg = '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Date of Birth is empty</div>"';

                }
                if ($("#gender").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Gender is empty</div>';
                }
                if ($("#marital").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Marital Status is empty</div>';
                }
                if ($("#postGrpType").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Post Group is empty</div>';
                }
                if ($("#height").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Height is empty</div>';
                }
                if ($("#bloodgrp").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Blood Group is empty</div>';
                }
                if ($("#homeTown").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Home Town is empty</div>';
                }
                if ($("#religionName").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Religion is empty</div>';
                }
                if ($("#domicil").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Domicil is empty</div>';
                }
                if ($("#idmark").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Id mark is empty</div>';
                }
                if ($("#mobile").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Mobile is empty</div>';
                }
                if ($("#email").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Email is empty</div>';
                }
                if ($("#hidPermAddress").val() == "N") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Permanent Address is missing</div>';
                }
                if ($("#hidPresentAddress").val() == "N") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Present Address is missing</div>';
                }
                if ($("#hidIdentityFlag").val() == "N") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Identity is empty</div>';
                }
                if ($("#hidLanguageFlag").val() == "N") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Language is empty</div>';
                }
                if ($("#hidEducationFlag").val() == "N") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Education is empty</div>';
                }
                if ($("#familyRelationList").val() == "N") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Family details is empty</div>';
                }
                if ($("#isAadharVerified").val() == "N") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please Verify your Aadhar No. from Identity Tab!</div>';
                }
                 if ($("#id_nomineeStaus").val() == "N") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please update your Nominee Details</div>';
                }
                 if ($("#id_presentprPsCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PRESENT Address</div>';
                }
               if ($("#id_permanentpsCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PERMANENT Address</div>';
                }
                
                  if ($("#id_permanentdistCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PERMANENT Address</div>';
                } 
                if ($("#id_presentprDistCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PRESENT Address</div>';
                }
                if ($("#id_permanentblockCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PERMANENT Address</div>';
                } 
                 if ($("#id_presentblockCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PRESENT Address</div>';
                }
                
                 if ($("#id_permanentpostCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PRESENT Address</div>';
                }
                 if ($("#id_presentpostCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PRESENT Address</div>';
                }
                   
                  if ($("#id_permanentvillageCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PRESENT Address</div>';
                }
                  if ($("#id_presentvillageCode").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PRESENT Address</div>';
                }
                if ($("#id_permanentpin").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PRESENT Address</div>';
                }
              if ($("#id_presentpin").val() == "") {
                    errormsg += '<div class="col-sm-4" style="margin-bottom:5px;"><img src="images/cross-icon.png" width="20" style="vertical-align:middle" /> Please complete your PRESENT Address</div>';
                }
                if (errormsg)
                {
                    $('#error_msg').html("<ul>" + errormsg + "</ul>");
                    $('#error_wrapper').slideDown();
                    flag = true;
                }
            }
            
            function confirmSubmit()
            {
                if (flag)
                {
                    alert("Please ensure to complete Profile data before submission.");
                    window.scrollTo(0, 0);
                    return false;
                }
                if (confirm('Are you sure to Submit Profile Data?'))
                {

                }
            }
        </script>
        <style type="text/css">
            .control-label {
                padding-top: 7px;
                margin-bottom: 0;
                text-align: left;
            }
            .row{
                margin-bottom: 5px;
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
        <div class="alert alert-danger" style="border:2px solid #F00;display:none;" id="error_wrapper">
                <h4 class="alert-heading" style="font-size:14pt;font-weight:bold;">Check the following details in order to Complete your Profile!</h4>
                <div class="row" id ="error_msg" style="color:#FF0000;font-size:12pt;">

                </div>
            </div> 
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading" align="center" style="background-color: #868686;color: #ffffff;font-size: xx-large;"></div>
                <div class="panel-body">
                    <h1 style="font-size:18pt;text-align:center;margin-top:0px;font-weight:bold;">Profile Acknowledgement</h1>
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
                            <td style="font-weight:bold;">${emp.gisName}</td>
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
                            <td align="right">Date from which in regular service with GoO(dd-MMM-yyyy):</td>
                            <td style="font-weight:bold;">${emp.joindategoo} ${$.timeOfEntryGoo}</td>
                            <td align="right">Date of entry into Govt. service(dd-MMM-yyyy):</td>
                            <td style="font-weight:bold;">${emp.doeGov} ${emp.txtwefTime}</td>
                        </tr>  
                        <tr>
                            <td align="right">Cadre:</td>
                            <td style="font-weight:bold;color: red;">${emp.cadreId}</td>
                            <td align="right"></td>
                            <td style="font-weight:bold;"></td>
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
                                <c:out value="${emp.maritalStatus}"/>
                                <%--<c:if test="${emp.marital=='1'}">Married</c:if>
                                <c:if test="${emp.marital!='1'}">Unmarried</c:if>--%>
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
                            <td style="font-weight:bold;">${emp.religionName}</td>
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
                    </table>
                    <table class="table table-bordered" style="margin-top:50px;">
                        <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                            <td colspan="5">Employee Identity:</td>
                        </tr>
                        <tr class="bg-primary text-white" >
                            <th>#</th>
                            <th>Identity Type</th>
                            <th>Identity No</th>
                            <th>Place of Issue</th>
                            <th>Date of Issue</th>
                        </tr>
                        <tbody>
                            <c:set var="aadharStaus" value="N" />
                            <c:forEach items="${IdList}" var="identityList" varStatus="cnt">
                                <tr>
                                    <td scope="row">${cnt.index+1}</td>
                                    <td>${identityList.identityDocType}</td>
                                    <td>
                                        ${identityList.identityNo}
                                        <c:if test="${identityList.identityDocType eq 'AADHAAR'}">
                                            <c:if test="${identityList.isVerified eq 'Y'}">
                                                <c:set var="aadharStaus" value="Y" />
                                                <img src="images/verified.png" width="20" height="20"/>
                                            </c:if>
                                            <c:if test="${identityList.isVerified eq 'N' }">
                                                <img src="images/error.png" width="20" height="20"/>
                                                <strong style="color:red">Please validate your AADHAAR</strong>
                                            </c:if>
                                            <c:if test="${empty identityList.isVerified}">
                                                <img src="images/error.png" width="20" height="20"/>
                                                <strong style="color:red">Please validate your AADHAAR</strong>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${identityList.identityDocType eq 'PAN'}">
                                            <c:if test="${fn:length(identityList.identityNo) ne 10}">
                                                <span style="color:red;">Invalid PAN</span>
                                            </c:if>
                                        </c:if>
                                    </td>
                                    <td>${identityList.placeOfIssue}</td>
                                    <td>${identityList.issueDate}</td>
                                </tr>
                            </c:forEach>

                        </tbody>
                    </table>
                    <input type="hidden" id="isAadharVerified" value="${aadharStaus}"/>
                    <table class="table table-bordered" style="margin-top:50px;">
                        <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                            <td colspan="7">Language:</td>
                        </tr>
                        <tr class="bg-primary text-white">
                            <th width="5%">#</th>
                            <th width="20%">Language</th>
                            <th width="10%">Can read</th>
                            <th width="10%">Can Write</th>
                            <th width="10%">Can Speak</th>
                            <th>Is Mother Tongue</th>
                            <th width="10%">Status</th>

                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${languageList}" var="emplanguage" varStatus="cnt">
                                <tr>
                                    <td scope="row">${cnt.index+1}</td>
                                    <td>${emplanguage.language}</span></td>
                                    <td><c:if test="${emplanguage.ifread eq 'Y'}"><span style="color: #008000;" class="glyphicon glyphicon-ok"></c:if></td>
                                    <td><c:if test="${emplanguage.ifwrite eq 'Y'}"><span style="color: #008000;" class="glyphicon glyphicon-ok"></c:if></td>
                                    <td><c:if test="${emplanguage.ifspeak eq 'Y'}"><span style="color: #008000;" class="glyphicon glyphicon-ok"></c:if></td>
                                    <td><c:if test="${emplanguage.ifmlang eq 'Y'}"><span style="color: #008000;" class="glyphicon glyphicon-ok"></c:if></td>                            
                                        <td>

                                        <c:if test="${emplanguage.isLocked eq 'Y'}">
                                            <img src="images/Lock.png" width="20" height="20"/>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <table class="table table-bordered" style="margin-top:50px;">
                        <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                            <td colspan="8">Employee Education</td>
                        </tr>
                        <tr class="bg-primary text-white">
                            <th>#</th>
                            <th>Qualification</th>
                            <th>Stream</th>
                            <th>Year of Pass</th>
                            <th>Subject</th>
                            <th>Institute</th>
                            <th>Board/University</th>
                        </tr>
                        <tbody>
                            <c:forEach items="${educationList}" var="education" varStatus="cnt">
                                <tr>
                                    <td scope="row">${cnt.index+1}</td>
                                    <td>${education.qualification}</td>
                                    <td>${education.faculty}</td>
                                    <td>${education.yearofpass}</td>
                                    <td>${education.subject}</td>
                                    <td>${education.institute}</td>
                                    <td>${education.board}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                        <table class="table table-bordered">
                            <tr style="background:#004085;color:#FFFFFF;font-weight:bold;">
                                <td colspan="8">Employee Family</td>
                            </tr>
                            <tr class="bg-primary text-white">
                                <th>#</th>
                                <th>Name</th>
                                <th>Relation</th>
                                <th>Gender</th>
                                <th>Identity Type</th>
                                <th>Identity No</th>
                                <th>Marital Status</th>
                                <th>Nominee</th>
                            </tr>
                            <tbody>
                                <c:set var="nomineeStaus" value="N" />
                                <c:forEach items="${familyList}" var="familyRelation" varStatus="cnt">
                                    <tr>
                                        <td scope="row">${cnt.index+1}</td>
                                        <td>${familyRelation.initials} ${familyRelation.fname} ${familyRelation.mname} ${familyRelation.lname}</td>
                                        <td>${familyRelation.relation}</td>
                                        <td>${familyRelation.gender}</td>
                                        <td>${familyRelation.identityDocType}</td>
                                        <td>${familyRelation.identityDocNo}</td>

                                        <td>${familyRelation.strMarriageStatus}</td>
                                        <td>
                                            <c:if test="${familyRelation.is_Nominee eq 'Y'}">
                                                <c:set var="nomineeStaus" value="Y" />
                                                <img src="images/verified.png" width="20" height="20"/>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <div align="center">       
                            <c:if test="${nomineeStaus eq 'N'}">
                                <strong style="color:red">**Please update your Nominee Details  </strong>
                            </c:if>
                        </div>     
                        <input type="hidden" id='id_nomineeStaus' value="${nomineeStaus}"/></div
                </div>
                <form:form action="SaveAndAcknowledgeEmployeeProfileVerification.htm" method="POST" commandName="Employee">
                    <form:hidden id="empid" path="empid" value="${emp.empid}"/>
                    <form:hidden id="deptCode" path="deptCode" value="${deptCode}"/>
                    <form:hidden id="ifprofileVerified" path="ifprofileVerified" value="${emp.ifprofileVerified}"/>
                    <form:hidden path="dob" value="${emp.dob}"/>
                    <form:hidden path="gender" value="${emp.gender}"/>
                    <form:hidden path="marital" value="${emp.marital}"/>
                    <form:hidden path="postGrpType" value="${emp.postGrpType}"/>
                    <form:hidden path="height" value="${emp.height}"/>
                    <form:hidden path="bloodgrp" value="${emp.bloodgrp}"/>
                    <form:hidden path="homeTown" value="${emp.homeTown}"/>
                    <form:hidden path="religionName" value="${emp.religionName}"/>
                    <form:hidden path="domicil" value="${emp.domicil}"/>
                    <form:hidden path="idmark" value="${emp.idmark}"/>
                    <form:hidden path="mobile" value="${emp.mobile}"/>
                    <form:hidden path="email" value="${emp.email}"/>

                    <c:if test="${not empty IdList}">
                        <input type="hidden" id="hidIdentityFlag" value="Y"/>
                    </c:if>
                    <c:if test="${empty IdList}">
                        <input type="hidden" id="hidIdentityFlag" value="N"/>
                    </c:if>
                    <c:if test="${not empty languageList}">
                        <input type="hidden" id="hidLanguageFlag" value="Y"/>
                    </c:if>
                    <c:if test="${empty languageList}">
                        <input type="hidden" id="hidLanguageFlag" value="N"/>
                    </c:if>
                    <c:if test="${not empty educationList}">
                        <input type="hidden" id="hidEducationFlag" value="Y"/>
                    </c:if>
                    <c:if test="${empty educationList}">
                        <input type="hidden" id="hidEducationFlag" value="N"/>
                    </c:if>
                    <c:if test="${not empty familyList}">
                        <input type="hidden" id="hidFamilyFlag" value="Y"/>
                    </c:if>
                    <c:if test="${empty familyList}">
                        <input type="hidden" id="hidFamilyFlag" value="N"/>
                    </c:if>
                    <c:if test="${hasPermAddress ne '0'}">
                        <input type="hidden" id="hidPermAddress" value="Y"/>
                    </c:if>
                    <c:if test="${hasPermAddress eq '0'}">
                        <input type="hidden" id="hidPermAddress" value="N"/>
                    </c:if>
                    <c:if test="${hasPresentAddress ne '0'}">
                        <input type="hidden" id="hidPresentAddress" value="Y"/>
                    </c:if>
                    <c:if test="${hasPresentAddress eq '0'}">
                        <input type="hidden" id="hidPresentAddress" value="N"/>
                    </c:if>   
                    <c:if test="${not empty emp.ifprofileVerified && emp.ifprofileVerified eq 'N'}">
                        <div class="row">
                            <label class="control-label col-sm-3" >&emsp;Reason for Decline </label>
                            <div class="col-sm-6" >
                                <form:textarea path="txtDeclineReason" id="txtDeclineReason" rows="3" cols="50"/>
                            </div>
                        </div>
                    </c:if>
                    <table>
                        <tr style="font-weight:bold;">
                            <td colspan="4"><input type="checkbox" name="chkProfileAcknowledge" id="chkProfileAcknowledge"<c:if test="${not empty empprofilecompletedstatus.dateOfProfileCompletion}"> checked="checked"</c:if>/></td>
                            <td>&nbsp;I <c:out value="${verifierName}"/> HAS VERIFIED THE ABOVE PROFILE DATA OF <c:out value="${employeeName}"/> AND FOUND CORRECT TO BEST OF MY KNOWLEDGE AND BELIEF.</td>
                        </tr>
                    </table>
                    <div class="panel-footer row">
                        <div class="col-xs-12 text-center">
                            <c:if test="${not empty empprofilecompletedstatus.dateOfProfileCompletion}">
                                <span style="display:block;text-align: center;font-weight: bold;font-size: 14px;color: red;">Profile verified on <c:out value="${empprofilecompletedstatus.dateOfProfileCompletion}"/> from IP <c:out value="${empprofilecompletedstatus.ipOfProfileCompletion}"/></span>
                            </c:if>
                            <c:if test="${not empty emp.ifprofileVerified && emp.ifprofileVerified eq 'N'}">
                                <input type="submit" name="btnSave" value="Approve" class="btn btn-default btn-primary btnSubmit" onclick="return confirmSubmit();"/> 
                                <input type="submit" name="btnSave" value="Decline" class="btn btn-default btn-primary btnSubmit" onclick="return verifyDelineData();"/> 
                            </c:if>
                            <a href="showProfileReportController.htm">
                                <input type="button" value="Back" class="btn btn-default btn-primary"/> 
                            </a>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</body>
</html>