<%-- 
    Document   : NominationViewForASI2SIorSI2INSRange
    Created on : 29 Dec, 2020, 7:31:58 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">    
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {


                toggleDiv();

            });

            function numbersOnly(evt) {
                evt = (evt) ? evt : window.event;
                var charCode = (evt.which) ? evt.which : evt.keyCode;
                if (charCode > 31 && (charCode < 48 || charCode > 57)) {
                    return false;
                } else {
                    return true;
                }
            }
            function validateForm() {
                if ($("#recommendStatus").val() == '') {
                    alert('Please select Recommendation .');
                    document.getElementById('recommendStatus').focus();
                    return false;
                }

            }

            function toggleDiv() {
                if ($("#dpcifany").val() == 'Y') {
                    $("#hideDiscDocumentRow").show();
                } else {
                    $("#hideDiscDocumentRow").hide();
                }
            }
        </script>

        <title>JSP Page</title>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <% int i = 1;%>
                <form:form action="saveRecommedNominationFormController.htm" commandName="nominationForm">
                    <form:hidden path="nominationMasterId"/>
                    <form:hidden path="nominationDetailId"/>
                    <form:hidden path="nominationFormId"/>
                    <form:hidden path="sltNominationForPost"/>
                    <div class="panel panel-default">
                        <h3 style="text-align:center"><b> NOMINATION ROLL</b></h3>
                        <h3 style="text-align:center">
                            <b> 
                                <div style="margin-bottom: 7px; text-align: center">                                                                       
                                    <label for="fathersname"><b>Nomination Serial No.</b></label>                                    
                                    <form:input path="gradeSerialNo" id="gradeSerialNo" maxlength="4" onkeypress="return numbersOnly(event)"/>                                    
                                </div> 
                            </b>
                        </h3>
                        <h3 style="text-align:center"><b>PROMOTION OF ASSISTANT SUB INSPECTOR OF POLICE (ASI) TO THE RANK OF SUB INSPECTOR (SI)<br> CSB-2024</b></h3>
                        <h3 style="text-align:center"><b>
                                <label for="fathersname">Employee Id</label> 
                                ${nominationForm.empId}</b>
                        </h3>
                        <div class="panel-heading">
                            <a href="showNominationrollForRangeOffice.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                        </div>

                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="officeName">Name of the Range/Office</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="officeName" id="officeName" class="form-control" readonly="true" />
                                    <form:hidden path="officeCode"/>
                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fullname"> a) Name in (full)</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="fullname" id="fullname" class="form-control" readonly="true"/>
                                    <form:hidden path="empId"/>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="fathersname">b)Father's Name(full)</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="fathersname" id="fathersname" class="form-control" readonly="true"/>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="category">c) Category</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="category" id="category" class="form-control">
                                        <c:forEach items="${categoryList}" var="category">
                                            <form:option value="${category.categoryid}" label="${category.categoryName}"/>
                                        </c:forEach>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                    <label for="casteCertificate">Caste Certificate(if any)</label>
                                </div>
                                <div class="col-lg-2">
                                    <c:if test="${not empty nominationForm.originalFileNamecasteCertificate}">
                                        <a href="downloadCasteCertificateDocument.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNamecasteCertificate}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="dob"> d) Date of Birth</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="dob" id="dob" class="form-control" style="width:50%" readonly="true"/>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="qualification"> e) Educational Qualification</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="qualification" id="qualification" class="form-control" readonly="true"/>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="homeDistrict">  f) Home District </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="homeDistrict" id="homeDistrict" class="form-control">
                                        <form:option value="">-Select One-</form:option>
                                        <form:options items="${districtList}" itemLabel="distName" itemValue="distCode"/>
                                    </form:select>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="postingPlace">g) Present Place of Posting </label>
                                </div>

                                <div class="col-lg-2">
                                    <form:input path="postingPlace" id="postingPlace" class="form-control" readonly="true"/>  
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingUnintDoj">h) Date of joining (in Present Place of Posting)</label>
                                </div>

                                <div class="col-lg-2">
                                    <form:input path="postingUnintDoj" id="postingUnintDoj" class="form-control" readonly="true"/>  
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="fathersname">i)GPF No</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="gpfNo" id="gpfNo" class="form-control" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="doeGov">Date of enlistment with rank (Entry in Govt. Service) <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="doeGov" id="doeGov" class="form-control" readonly="true"/>  
                                </div>
                                <div class="col-lg-1">
                                    <label for="doeGov">Rank<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="rankJoiningovservice" id="rankJoiningovservice" class="form-control" readonly="true"/>  
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="serviceBookCopy"> b) 1st Page of Service Book be attached</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNameSB}">
                                        <a href="downloadServiceBookCopy.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameSB}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>

                            </div> 

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="jodInspector"> Present Rank </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="presentRank" id="presentRank" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="jodInspector"> Date of joining (in Present Rank) </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="jodInspector" id="jodInspector" class="form-control" readonly="true"/>  
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Length of service in (will be auto calculate as on 01-JAN-2023):-</label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label>Year</label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label style="text-align:center">Month</label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label style="text-align:center">Day</label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="yearinService">  </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="yearinService" id="yearinService" class="form-control" readonly="true" maxlength="2" onkeypress="return numbersOnly(event)"/>


                                </div>
                                <div class="col-lg-2">
                                    <form:input path="monthrinService" id="monthrinService" class="form-control" readonly="true" maxlength="2" onkeypress="return numbersOnly(event)"/>

                                </div>
                                <div class="col-lg-2">
                                    <form:input path="daysinService" id="daysinService" class="form-control" readonly="true" maxlength="2" onkeypress="return numbersOnly(event)"/>

                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="periodParticularsTraining"> (a) Period and Particulars of training and courses undergone <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="periodParticularsTraining" id="periodParticularsTraining" class="form-control" rows="4" cols="100"  maxlength="450" readonly="true"/>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label> </label>
                                </div>
                                <div class="col-lg-9">
                                    <label style="color:red"> (Maximum 450 characters allowed) </label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="officeOrderNo"> (b) Office order No <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="officeOrderNo" id="officeOrderNo" class="form-control" maxlength="18" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="passingTrainingdate"> 
                                        <c:if test="${nominationForm.sltNominationForPost eq '140293'}">
                                            (i) Date of passing ASIs course of training <span style="color: red">*</span>
                                        </c:if>
                                        <c:if test="${nominationForm.sltNominationForPost eq '140599'}">
                                            (i) Date of passing SIs course of training <span style="color: red">*</span>
                                        </c:if>

                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="passingTrainingdate" id="passingTrainingdate" class="form-control"/>
                                </div>
                            </div>    

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Rewards </label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Nature </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> Prior to 01.01.2019 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> During last 5 years i.e. w.e.f. </br> 1.1.2019 to 31.12.2023 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> From 1.1.24 till date of submission of Nomination Rolls </label>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="rewardGSMarkPrior"> a) G.S. Mark </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardGSMarkPrior" id="rewardGSMarkPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardGSMarkDuring" id="rewardGSMarkDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardGSMarkFrom" id="rewardGSMarkFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="rewardMoneyOherPrior"> b) Money reward</label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMoneyOherPrior" id="rewardMoneyOherPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMoneyOherDuring" id="rewardMoneyOherDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMoneyOherFrom" id="rewardMoneyOherFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>

                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="rewardMedalsPrior"> c)Other Awards/ Medals / Recognitions if any </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMedalsPrior" id="rewardMedalsPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMedalsDuring" id="rewardMedalsDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMedalsFrom" id="rewardMedalsFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Punishments </label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Nature </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> Prior to 01.01.2019 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> During last 5 years i.e. w.e.f. </br> 1.1.2019 to 31.12.2023 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> From 1.1.24 till date of submission of Nomination Rolls </label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMajorPrior"> a) Major </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorPrior" id="punishmentMajorPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentPrior()"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorDuring" id="punishmentMajorDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentDuring()"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorFrom" id="punishmentMajorFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentFrom()"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMinorPrior"> b) Minor </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentPrior()" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentDuring()" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentFrom()" readonly="true"/>
                                </div>
                            </div>                                                       

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMinorPrior"> c)Total </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="totalpunishment1" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="totalpunishment2" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="totalpunishment3" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="punishmentCopy">(c)In case of Major/Minor punishment awarded during preceding 5 years of the
                                        CSB, then copy of the entries of punishments in service book to be attached
                                        in .pdf form. <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="punishmentDetail" id="punishmentDetail" class="form-control" rows="4" cols="100"  maxlength="450" readonly="true"/>
                                    <label style="color:red"> (Maximum 500 characters allowed) </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNamePunishment}">
                                        <a href="downloadPunishmentDocument.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNamePunishment}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>
                            </div>  

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="powerofDecesion"> Power of decision making and assuming responsibility <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="powerofDecesion" id="powerofDecesion" class="form-control" maxlength="200" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="physicalFitness"> a) Physical <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-4">
                                    <form:select path="physicalFitness" id="physicalFitness" class="form-control">
                                        <form:option value=""> --Select One-- </form:option>
                                        <form:option value="Fit"> Fit </form:option>
                                        <form:option value="Unfit"> Unfit </form:option>
                                        <form:option value="No Certificate"> No Medical Certificate </form:option>
                                    </form:select>
                                </div>

                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNameFitnessDocument}">
                                        <a href="downloadFitnessDocument.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameFitnessDocument}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>


                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-11">
                                    <label for="physicalFitnessDocumentStatus"> (Physical fitness certificate Shall be attached obtaining from CDMO mentioning there that the Nominee is physically fit to undergo the SI's Course of Training) </label>
                                </div>
                            </div> 


                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="mentalFitness"> b) Mental </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="mentalFitness" id="mentalFitness" class="form-control" maxlength="200" readonly="true"/>
                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="honestyIntegrity"> Standard of honesty and integrity <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="honestyIntegrity" id="honestyIntegrity" class="form-control" maxlength="200" readonly="true"/>
                                </div>

                            </div>


                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="adverseIfany"> CC Rolls during last 5 years as on 31.03.2022 </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="adverseIfany" class="form-control" rows="4" cols="100" maxlength="450" readonly="true"/>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label>i) Whether Property Statement Submitted /Not <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="propertyStatementSubmittedifAny" id="propertyStatementSubmittedifAny" class="form-control" onclick="togglepropertyDiv()">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="dateofSubmittingPropertyDiv">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="dateofServing"> Date of submission</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dateofPropertySubmittedByOfficer" id="dateofPropertySubmittedByOfficer" class="form-control" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label>ii) Submission Status Date Of Property Return Statement On HRMS Portal  Date <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="dateofPropertySubmittedByHRMS" id="dateofPropertySubmittedByHRMS" class="form-control" readonly="true" style="width:50%"/>

                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> Disciplinary Proceeding/ Vigilance Case/ Criminal Case/ HRPC related Case, if any, Pending initiated   </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dpcifany" id="dpcifany" class="form-control" onclick="toggleDiv()">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
                                    </form:select>
                                </div>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;" id="hideDiscDocumentRow">
                            <div class="col-lg-1">
                                <label> </label>
                            </div>
                            <div class="col-lg-2">
                                <label for="dpcifany"> Upload document  </label>
                            </div>
                            <div class="col-lg-3">
                                <c:if test="${not empty nominationForm.originalFileName}">
                                    <a href="downloadDPC.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileName}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                    </c:if>
                            </div>
                        </div> 

                        <div class="row" style="margin-bottom: 7px;" id="hideDiscDocumentRow2">
                            <div class="col-lg-1">
                                <label> </label>
                            </div>
                            <div class="col-lg-2">
                                <label for="dpcifany"> give  details </label>
                            </div>
                            <div class="col-lg-6">
                                <form:textarea path="discDetails" id="discDetails" class="form-control" rows="4" cols="100" maxlength="1000" readonly="true"/>
                            </div>
                        </div>  
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label> </label>
                            </div>
                            <div class="col-lg-9">
                                <label style="color:red"> (Maximum 1000 characters allowed) </label>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label><%=i++%>.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="dateofServingif">i) If charge has been served on the nominee in Departmental Proceeding? if Yes indicate the date of serving charge and ME. </label>
                            </div>
                            <div class="col-lg-3">
                                <form:select path="dateofServingifAny" id="dateofServingifAny" class="form-control" onclick="toggleServicingDiv()">
                                    <form:option value=""> Select </form:option>
                                    <form:option value="N"> No </form:option>
                                    <form:option value="Y"> Yes </form:option>
                                </form:select>
                            </div>

                        </div> 
                        <div class="row" style="margin-bottom: 7px;" id="hideServingChargeRow">
                            <div class="col-lg-1">
                                <label> </label>
                            </div>
                            <div class="col-lg-2">
                                <label for="documentServingCopy"> Upload document </label>
                            </div>
                            <div class="col-lg-6">
                                <c:if test="${not empty nominationForm.originalFileNameServing}">
                                    <a href="downloadServing.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameServing}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                    </c:if>

                            </div>

                        </div>    
                        <div class="row" style="margin-bottom: 7px;" id="dateofServingDiv">
                            <div class="col-lg-1">

                            </div>
                            <<div class="col-lg-2">
                                <label for="dateofServing"> Date of serving charge </label>
                            </div> 
                            <div class="col-lg-3">
                                <form:input path="dateofServing" id="dateofServing" class="form-control" readonly="true"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="dpcifany"> give  details </label>
                            </div>
                            <div class="col-lg-5">
                                <form:textarea path="servingChargeDetail" id="servingChargeDetail" class="form-control" rows="4" cols="100" maxlength="450" readonly="true"/>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label> </label>
                                </div>
                                <div class="col-lg-9">
                                    <label style="color:red"> (Maximum 450 characters allowed) </label>
                                </div>
                            </div> 
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label><%=i++%>.</label>
                            </div>
                            <div class="col-lg-10">
                                <label> The details regarding Category, Service Particulars, Rewards, Punishments, Departmental Proceedings, Criminal Case furnished above are true to the best of my knowledge as per records available and are hereby certified </label>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label> </label>
                            </div>
                            <div class="col-lg-2">

                            </div>
                            <div class="col-lg-3">
                                <form:select path="declarationAccept" id="declarationAccept" class="form-control" onclick="toggleServicingDiv()">
                                    <form:option value=""> </form:option>
                                    <form:option value="Agree"> I Agree </form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label><%=i++%>.</label>
                            </div>
                            <div class="col-lg-10">
                                <label> Remarks of Nominating Authority </label>
                            </div>


                        </div> 
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label> </label>
                            </div>
                            <div class="col-lg-2">
                                <label for="remarksNominatingProfessional"> a) Professional ability and specific flair, if any. </label>
                            </div>
                            <div class="col-lg-6">
                                <form:textarea path="remarksNominatingProfessional" id="remarksNominatingProfessional" class="form-control" rows="4" cols="100" maxlength="450" readonly="true"/>
                            </div>

                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label> </label>
                            </div>
                            <div class="col-lg-9">
                                <label style="color:red"> (Maximum 480 characters allowed) </label>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label> </label>
                            </div>
                            <div class="col-lg-2">
                                <label for="remarksNominationGeneral"> b) General Suitability </label>
                            </div>
                            <div class="col-lg-6">
                                <form:textarea path="remarksNominationGeneral" id="remarksNominationGeneral" class="form-control" rows="4" cols="100" maxlength="1200" readonly="true"/>
                            </div>

                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label> </label>
                            </div>
                            <div class="col-lg-9">
                                <label style="color:red"> (Maximum 1200 characters allowed) </label>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label><%=i++%>. </label>
                            </div>
                            <div class="col-lg-2">
                                <label for="recommendStatus"> Recommendation Status from Nominating Authority<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-6">
                                <form:select path="recommendStatusDistrict" id="recommendStatusDistrict" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="NOMINATED">NOMINATED</form:option>
                                    <form:option value="NOT NOMINATED">NOT NOMINATED</form:option>
                                </form:select>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label><%=i++%>.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="generalremarks">General Remarks if any </label>
                            </div>
                            <div class="col-lg-6">
                                <form:textarea path="viewOfrecommendStatusDistrict" id="viewOfrecommendStatusDistrict" class="form-control" rows="4" cols="100"  maxlength="450" readonly="true"/>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label><%=i++%>. </label>
                            </div>
                            <div class="col-lg-2">
                                <label for="recommendStatus"> Recommendation Status From Recommending Authority</label>
                            </div>
                            <div class="col-lg-6">
                                <form:select path="recommendStatus" id="recommendStatus" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="RECOMMENDED">RECOMMENDED</form:option>
                                    <form:option value="NOT RECOMMENDED">NOT RECOMMENDED</form:option>
                                </form:select>
                            </div>
                        </div>  
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label> <%=i++%>. </label>
                            </div>
                            <div class="col-lg-2">
                                <label for="remarkRecommendation"> Views of Recommending Authority </label>
                            </div>
                            <div class="col-lg-6">
                                <form:textarea path="remarkRecommendation" id="remarkRecommendation" class="form-control" rows="4" cols="100" readonly="true"/>
                            </div>
                        </div>
                    </div>

                    <div class="panel-footer">
                        <a href="showNominationrollForRangeOffice.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 

                    </div>
                </div>
            </form:form>
        </div>
    </div>
</body>
</html>
