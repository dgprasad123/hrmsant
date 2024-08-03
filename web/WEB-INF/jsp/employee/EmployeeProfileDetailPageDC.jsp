<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% int i = 1;%>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Employee Profile Acknowledgement</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">  
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>

    </head>
    <body>





        <div style=" margin-bottom: 5px;" class="panel panel-info">
            <div class="panel-body">
                <h1 style="font-size:18pt;text-align:center;margin-top:0px;font-weight:bold;">EMPLOYEE PROFILE DATA VERIFICATION</h1>
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
                        <td style="font-weight:bold;color: red;">${emp.cadreId}
                            <c:if test="${empty emp.cadreId || emp.cadreId eq 'OTHERS CADRE'}">
                                &nbsp;&nbsp;<span >(Please update the cadre at DDO end.)</span>
                            </c:if>    


                        </td>
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
                        <td style="font-weight:bold;">${address.psCode}
                            <input type="hidden" id='id_presentpscode' value="${address.psCode}"/>
                        </td>
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
                        <c:forEach items="${identityInfoList}" var="identityList" varStatus="cnt">
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
                        <c:forEach items="${emplanguageList}" var="emplanguage" varStatus="cnt">
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
                            <c:forEach items="${familyRelationList}" var="familyRelation" varStatus="cnt">
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

                </div>
                <input type="hidden" id='id_nomineeStaus' value="${nomineeStaus}"/></div>

        </div>

        <div style=" margin-top: 0.5px;" class="panel panel-info">
            <div class="panel-body">
                <div>
                    <c:if test="${not empty empprofilecompletedstatus.dateOfProfileCompletion}">
                        <span style="display:block;text-align: center;font-weight: bold;font-size: 14px;color: red;">Profile completed on <c:out value="${empprofilecompletedstatus.dateOfProfileCompletion}"/> from IP <c:out value="${empprofilecompletedstatus.ipOfProfileCompletion}"/></span>
                    </c:if>
                </div>
                <div class="pull-left">

                </div>
            </div> 
        </div>
    </body>
</html>
