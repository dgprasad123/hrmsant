<%-- 
    Document   : RecommendNominationFormForSubInspector2Inspector
    Created on : 18 Nov, 2021, 4:39:30 PM
    Author     : Manisha
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
                <form:form action="saveRecommedNominationFormController.htm" commandName="nominationForm" enctype="multipart/form-data">
                    <form:hidden path="nominationMasterId"/>
                    <form:hidden path="nominationDetailId"/>
                    <form:hidden path="nominationFormId"/>
                    <form:hidden path="sltNominationForPost"/>

                    <div class="panel panel-default">
                        <h3 style="text-align:center"> PROFORMA-1</h3>
                        <h4 style="text-align:center"> NOMINATION ROLL </h4>
                        <div class="panel-heading">
                            <a href="viewNominationrollList.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 

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
                                <div class="col-lg-1">
                                    <label for="category">Category</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="category" id="category" class="form-control" readonly="true">
                                        <c:forEach items="${categoryList}" var="category">
                                            <form:option value="${category.categoryid}" label="${category.categoryName}"/>
                                        </c:forEach>
                                    </form:select>
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

                                </div>
                                <div class="col-lg-2">
                                    <label for="dob"> c) Date of Birth</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="dob" id="dob" class="form-control" readonly="true" style="width:50%"/>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="qualification"> d) Educational Qualification</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="qualification" id="qualification" class="form-control" readonly="true"/>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="homeDistrict">  e) Home District </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="homeDistrict" id="homeDistrict" class="form-control" readonly="true">
                                        <form:option value="">-Select One-</form:option>
                                        <form:options items="${districtList}" itemLabel="distName" itemValue="distCode"/>
                                    </form:select>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="postingPlace">f) Present Place of Posting </label>
                                </div>

                                <div class="col-lg-2">
                                    <form:input path="postingPlace" id="postingPlace" class="form-control" readonly="true"/>  
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingUnintDoj">g) Date of joining (in Present Place of Posting)</label>
                                </div>

                                <div class="col-lg-2">
                                    <form:input path="postingUnintDoj" id="postingUnintDoj" class="form-control" readonly="true"/>  
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="serviceBookCopy"> h) 1st Page of Service Book be attached</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNameSB}">
                                        <a href="downloadServiceBookCopy.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameSB}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNameSB}">
                                        <input type="file" name="serviceBookCopy"  id="serviceBookCopy" /> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="casteCertificate"> i) Caste Certificate(if any)</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNamecasteCertificate}">
                                        <a href="downloadCasteCertificateDocument.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNamecasteCertificate}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNamecasteCertificate}">
                                        <input type="file" name="casteCertificate" id="casteCertificate" class="fileupload"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="adverseIfany"> Cadre List </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="cadreName" id="cadreName" class="form-control" onclick="toggleCCROLLDiv()" readonly="true">
                                        <form:option value=""> --Select Cadre-- </form:option>
                                        <form:option value="1453"> DISTRICT CADRE </form:option>
                                        <form:option value="1493"> BATTALION CADRE(ODIA COY)</form:option>
                                        <form:option value="1494"> BATALLION  CADRE(GURKHA COY)</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="doeGov"> a) Date of enlistment with rank (Entry in Govt. Service)</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="doeGov" id="doeGov" class="form-control" readonly="true"/>  
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
                                        <c:if test="${empty nominationForm.originalFileNameSB}">
                                        <input type="file" name="serviceBookCopy"  id="serviceBookCopy" /> <span style="color:red">(Only PDF)</span>
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
                                    <form:select path="presentRank" id="presentRank" class="form-control" readonly="true">  
                                        <c:if test="${nominationForm.sltNominationForPost eq '140293'}">
                                            <form:option value="140858">ASSISTANT SUB INSPECTOR OF POLICE</form:option>
                                        </c:if>
                                        <c:if test="${nominationForm.sltNominationForPost eq '140599'}"> 
                                            <form:option value="140293">SUB-INSPECTOR OF POLICE</form:option>
                                        </c:if>

                                    </form:select>
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
                                    <label> Length of service in (will be auto calculate as on 01-JAN-2022):-</label>
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
                                    <label for="periodParticularsTraining"> (a) Period and Particulars of training and courses undergone </label>
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
                                    <label> Prior to 01.01.2017 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> During last 5 years i.e. w.e.f. </br> 1.1.2017 to 31.12.2021 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> From 1.1.22 till date of submission of Nomination Rolls </label>
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
                                    <label for="rewardMoneyOherPrior"> b) Money reward/ Other Awards </label>
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
                                    <label for="rewardMedalsPrior"> c) Other Awards/Medals/ recognisation if any </label>
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
                                    <label> Prior to 01.01.2017 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> During last 5 years i.e. w.e.f. </br> 1.1.2017 to 31.12.2021 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> From 1.1.22 till date of submission of Nomination Rolls </label>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMajorPrior"> a) Major </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorPrior" id="punishmentMajorPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorDuring" id="punishmentMajorDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorFrom" id="punishmentMajorFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMinorPrior"> b) Minor </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorPrior" id="punishmentMinorPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorDuring" id="punishmentMinorDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorFrom" id="punishmentMinorFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>

                            </div> 


                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="powerofDecesion"> Power of decision making and assuming responsibility </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="powerofDecesion" id="powerofDecesion" class="form-control" maxlength="200" readonly="true"/>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Fitness </label>
                                </div>
                                <div class="col-lg-3">

                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="physicalFitness"> a) Physical </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="physicalFitness" id="physicalFitness" class="form-control" maxlength="200" readonly="true"/>
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
                            <c:if test="${nominationForm.sltNominationForPost eq '140293'}">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-1">
                                        <label> </label>
                                    </div>
                                    <div class="col-lg-2">
                                        <label for="physicalFitnessDocumentStatus"> c) Whether medical fitness certificate attached issued from concerned CDMO in Form No.39 of schedule-IX.</label>
                                    </div>
                                    <div class="col-lg-3">
                                        <form:select path="physicalFitnessDocumentStatus" id="physicalFitnessDocumentStatus" class="form-control" onclick="toggleFitnessDiv()" readonly="true">
                                            <form:option value=""> --Select One-- </form:option>
                                            <form:option value="No"> No </form:option>
                                            <form:option value="Yes"> Yes </form:option>
                                        </form:select>
                                    </div>

                                </div> 

                                <div class="row" style="margin-bottom: 7px;" id="hidePhysicalFitnessRow">
                                    <div class="col-lg-1">
                                        <label> </label>
                                    </div>
                                    <div class="col-lg-2">
                                        <label for="fitnessDocument"> Upload document  </label>
                                    </div>
                                    <div class="col-lg-3">
                                        <c:if test="${not empty nominationForm.originalFileNameFitnessDocument}">
                                            <a href="downloadFitnessDocument.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameFitnessDocument}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                            </c:if>
                                            <c:if test="${empty nominationForm.originalFileNameFitnessDocument}">
                                            <input type="file" name="fitnessDocument"  id="fitnessDocument"/> <span style="color:red">(Only PDF)</span>
                                        </c:if>
                                    </div>

                                </div>
                                <div class="row" style="margin-bottom: 7px;" id="hidecdmoremarksrow">
                                    <div class="col-lg-1">
                                        <label> </label>
                                    </div>
                                    <div class="col-lg-2">
                                        <label for="remarksofCdmo"> Remarks of CDMO </label>
                                    </div>
                                    <div class="col-lg-6">
                                        <form:select path="remarksofCdmo" id="remarksofCdmo" class="form-control" readonly="true">
                                            <form:option value="">-- Select One --</form:option> 
                                            <form:option value="FIT">FIT</form:option>
                                            <form:option value="UNFIT">UNFIT</form:option>
                                        </form:select>
                                    </div>

                                </div>
                            </c:if>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="honestyIntegrity"> Standard of honesty and integrity </label>
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
                                    <label for="adverseIfany"> CC Rolls during last 5 years as on 31.03.2021 </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="adverseIfany" id="adverseIfany" class="form-control" onclick="toggleCCROLLDiv()" readonly="true">
                                        <form:option value=""> --Select One-- </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
                                    </form:select>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;" id="hideCCROLLRow">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="discCCROll"> Upload document </label>
                                </div>
                                <div class="col-lg-6">
                                    <c:if test="${not empty nominationForm.originalFileNameCCROll}">
                                        <a href="downloadCCroll.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameCCROll}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNameCCROll}">
                                        <input type="file" name="discCCROll"  id="discCCROll"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>

                            </div>    
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> Departmental/ Disciplinary Proceedings/ Vigilance Enquiry/ Criminal Cases, if any, Pending initiated   </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dpcifany" id="dpcifany" class="form-control" onclick="toggleDiv()" readonly="true">
                                        <form:option value=""> No </form:option>
                                        <form:option value="Y"> Yes </form:option>
                                    </form:select>
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
                                        <c:if test="${empty nominationForm.originalFileName}">
                                        <input type="file" name="discDocument"  id="discDocument"/> <span style="color:red">(Only PDF)</span>
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
                                    <form:textarea path="discDetails" id="discDetails" class="form-control" rows="4" cols="100" maxlength="450" readonly="true"/>
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
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dateofServingifAny"> If charge has been served on the nominee in Departmental Proceeding/ Criminal Case? if Yes indicate the date of serving charge and ME. </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dateofServingifAny" id="dateofServingifAny" class="form-control" onclick="toggleServicingDiv()" readonly="true">
                                        <form:option value=""> No </form:option>
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
                                        <c:if test="${empty nominationForm.originalFileNameServing}">
                                        <input type="file" name="documentServingCopy"  id="documentServingCopy"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>

                            </div>    
                            <div class="row" style="margin-bottom: 7px;" id="dateofServingDiv">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="dateofServing"> Date of serving charge </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dateofServing" id="dateofServing" class="form-control" readonly="true"/>
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
                                    <label for="recommendStatus"> Recommendation Status </label>
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
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="periodParticularsTraining">Recommendation/ views of Recommending Authority </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="remarkRecommendation" id="remarkRecommendation" class="form-control" rows="4" cols="100"  maxlength="450"/>
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
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="periodParticularsTraining">Remarks of C.S.B. </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="remarksOfCSB" id="remarksOfCSB" class="form-control" rows="4" cols="100"  maxlength="450"/>
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
                        </div>


                    </div>
                    <div class="panel-footer">
                        <a href="viewNominationrollForRangeOffice.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                        <input type="submit" value="Save Form" class="btn btn-success" onclick="return validateForm()"/>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</body>
</html>

