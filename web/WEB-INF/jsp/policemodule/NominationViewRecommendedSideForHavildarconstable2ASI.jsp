<%-- 
    Document   : NominationViewRecommendedSideForHavildarconstable2ASI
    Created on : 7 Sep, 2022, 11:21:36 AM
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
                $('#postingUnintDoj').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#jodInspector').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#dateofServing').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#passingTrainingdate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });


                $('#doeGov').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                toggleCCROLLDiv();

                toggleDiv();

                toggleFitnessDiv();

                toggleServicingDiv();


            });

            function toggleDiv() {
                if ($("#dpcifany").val() == 'Y') {
                    $("#hideDiscDocumentRow").show();
                    $("#hideDiscDocumentRow2").show();
                } else {
                    $("#hideDiscDocumentRow").hide();
                    $("#hideDiscDocumentRow2").hide();
                }
            }

            function toggleCCROLLDiv() {
                if ($("#adverseIfany").val() == 'Yes') {
                    $("#hideCCROLLRow").show();
                } else {
                    $("#hideCCROLLRow").hide();
                }
            }

            function toggleFitnessDiv() {
                if ($("#physicalFitnessDocumentStatus").val() == 'Yes') {
                    $("#hidePhysicalFitnessRow").show();
                    $("#hidecdmoremarksrow").show();

                } else {
                    $("#hidePhysicalFitnessRow").hide();
                    $("#hidecdmoremarksrow").hide();
                }
            }




            function toggleServicingDiv() {
                if ($("#dateofServingifAny").val() == 'Y') {
                    $("#dateofServingDiv").show();
                    $("#hideServingChargeRow").show();

                } else {
                    $("#dateofServingDiv").hide();
                    $("#dateofServing").val('');
                    $("#hideServingChargeRow").hide();
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
                <form:form action="savenominationFormController.htm" commandName="nominationForm">
                    <form:hidden path="nominationMasterId"/>
                    <form:hidden path="nominationDetailId"/>
                    <form:hidden path="nominationFormId"/>
                    <form:hidden path="nominationtype"/>
                    <form:hidden path="sltNominationForPost"/>

                    <div class="panel panel-default">
                        <h3 style="text-align:center"><b><u> PROFORMA </u></b></h3>
                        <h4 style="text-align:center"><b><u> NOMINATION ROLL </u></b></h4>
                        <h3 style="text-align:center">
                            <b> 
                                <div style="margin-bottom: 7px; text-align: center">                                                                       
                                    <label for="fathersname">Nomination Serial No.</label>                                    
                                    <form:input path="gradeSerialNo" id="gradeSerialNo" maxlength="4" onkeypress="return numbersOnly(event)"/>                                    
                                </div> 
                            </b>
                        </h3>
                        <h3 style="text-align:center"><b>PROMOTION OF CONSTABLES/LNKS/HAVS./CI HAVs TO OFFICIATE ASI OF POLICE <br> CSB-2023</b></h3>
                        <h3 style="text-align:center"><b>
                                <label for="fathersname">Employee Id</label> 
                                ${nominationForm.empId}</b>
                        </h3>
                        <div class="panel-heading">
                            <a href="editNominationroll.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 
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
                                    <label for="fullname"> a) Name in (full) <span style="color: red">*</span></label>
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
                                    <label for="fathersname">b)Designation</label>
                                </div>
                                <div class="col-lg-6"> 
                                    <form:input path="currentDesignation" id="currentDesignation" class="form-control" readonly="true"/>
                                    <%-- <form:select path="presentRank" id="presentRank" class="form-control">  
                                         <c:if test="${nominationForm.sltNominationForPost eq '140070'}">
                                             <form:option value="140070">CONSTABLES/LNK/HAV/CI HAV</form:option>
                                         </c:if>
                                         <c:if test="${nominationForm.sltNominationForPost eq '140858'}">
                                             <form:option value="140858">ASSISTANT SUB INSPECTOR OF POLICE</form:option>
                                         </c:if>

                                    </form:select> --%>
                                </div>

                            </div> 



                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="Category"> c) Category <span style="color: red">*</span></label>
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
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNamecasteCertificate}">
                                        <a href="downloadCasteCertificateDocument.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNamecasteCertificate}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>

                                </div>
                            </div> 

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="fathersname">d)Father's Name(full) <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="fathersname" id="fathersname" class="form-control" readonly="true"/>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="dob"> e) Date of Birth <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="dob" id="dob" class="form-control" readonly="true" style="width:50%"/>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="qualification"> f) Educational Qualification <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <c:if test="${nominationForm.nominationtype eq 'EXM'}">
                                        <form:input path="qualification" id="qualification" class="form-control" readonly="true"/>
                                    </c:if>
                                    <c:if test="${nominationForm.nominationtype ne 'EXM'}">
                                        <form:input path="qualification" id="qualification" class="form-control"/>
                                    </c:if>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="homeDistrict">  g) Home District <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${nominationForm.sltpostName eq '140070' && nominationForm.nominationtype eq 'EXM'}">
                                        <form:select path="homeDistrict" id="homeDistrict" class="form-control">
                                            <form:option value=""  disabled="true">-Select One-</form:option>
                                            <form:options items="${districtList}" itemLabel="distName" itemValue="distCode"/>
                                        </form:select>
                                    </c:if>
                                    <c:if test="${nominationForm.nominationtype ne 'EXM'}">
                                        <form:select path="homeDistrict" id="homeDistrict" class="form-control">
                                            <form:option value="">-Select One-</form:option>
                                            <form:options items="${districtList}" itemLabel="distName" itemValue="distCode"/>
                                        </form:select>
                                    </c:if>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="postingPlace">h) Present Place of Posting with date of joining. <span style="color: red">*</span></label>
                                </div>

                                <div class="col-lg-2">
                                    <form:input path="postingPlace" id="postingPlace"  placeholder="Place" class="form-control"/>  
                                </div>
                                <div class="col-lg-2">

                                </div>

                                <div class="col-lg-2">
                                    <form:input path="postingUnintDoj" id="postingUnintDoj" placeholder="DD/MM/YYYY" class="form-control"/>  
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
                                    <label for="doeGov"> Date of enlistment with rank (Entry in Govt. Service) <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="doeGov" id="doeGov" class="form-control" readonly="true" style="width:50%"/>  

                                </div>
                                <div class="col-lg-1">
                                    <label for="serviceBookCopy"> Rank <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="rankJoiningovservice" id="rankJoiningovservice" class="form-control" readonly="true" style="width:50%"/> 
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>

                                <div class="col-lg-1">
                                    <label for="serviceBookCopy"> 1st Page of Service Book be attached <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
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
                                    <label for="jodInspector"> Date of joining in the present Rank <br>(CONSTABLES/LNK/HAV/CI HAV) </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="jodInspector" id="jodInspector" class="form-control" readonly="true" style="width:50%"/>  
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Length of service in:-</label>
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
                                    <label> (a) Place/Date of passing Basic
                                        Constable Course of Training</label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label>Trg. Place</label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label style="text-align:center">From <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label style="text-align:center">To <span style="color: red">*</span></label>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="coursepassing">  </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="passingPlaceForConstableTraining" id="passingPlaceForConstableTraining"  placeholder="Place" class="form-control"/>  

                                </div>
                                <div class="col-lg-2">
                                    <form:input path="passingYearFromForConstableTraining" id="passingYearFromForConstableTraining" placeholder="DD/MM/YYYY" class="form-control"/> 
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="passingYearToForConstableTraining" id="passingYearToForConstableTraining" placeholder="DD/MM/YYYY" class="form-control"/> 
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label> (b) Passing of Written Examination to
                                        undergo ASI’s Course of Training</label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label>O.O. No.</label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label style="text-align:center">Date <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label style="text-align:center"></label>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="passingexam">  </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="orderNopassingForConstableTraining" id="orderNopassingForConstableTraining" class="form-control"/> 

                                </div>
                                <div class="col-lg-2">
                                    <form:input path="orderDatepassingForConstableTraining" id="orderDatepassingForConstableTraining" placeholder="DD/MM/YYYY" class="form-control"/> 
                                </div>
                                <div class="col-lg-2">

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
                                    <label> Prior to 01.01.2018 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> During last 5 years i.e. w.e.f. </br> 1.1.2018 to 31.12.2022 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> From 1.1.23 till date of submission of Nomination Rolls </label>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="rewardGSMarkPrior"> a) G.S. Mark </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardGSMarkPrior" id="rewardGSMarkPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardGSMarkDuring" id="rewardGSMarkDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardGSMarkFrom" id="rewardGSMarkFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="rewardMoneyOherPrior"> b) Money reward/ Other Awards </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMoneyOherPrior" id="rewardMoneyOherPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMoneyOherDuring" id="rewardMoneyOherDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMoneyOherFrom" id="rewardMoneyOherFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>

                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="rewardMedalsPrior"> c) Medals/ Recognisation if any </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMedalsPrior" id="rewardMedalsPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMedalsDuring" id="rewardMedalsDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMedalsFrom" id="rewardMedalsFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
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
                                    <label> Prior to 01.01.2018 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> During last 5 years i.e. w.e.f. </br> 1.1.2018 to 31.12.2022 </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> From 1.1.23 till date of submission of Nomination Rolls </label>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMajorPrior"> a) Major <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorPrior" id="punishmentMajorPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorDuring" id="punishmentMajorDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorFrom" id="punishmentMajorFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMinorPrior"> b) Minor <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorPrior" id="punishmentMinorPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorDuring" id="punishmentMinorDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorFrom" id="punishmentMinorFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)"/>
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
                                    <form:textarea path="punishmentDetail" id="punishmentDetail" class="form-control" rows="4" cols="100"  maxlength="450"/>
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
                                    <form:input path="powerofDecesion" id="powerofDecesion" class="form-control" maxlength="200"/>
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
                            <c:if test="${nominationForm.sltpostName eq '140070'}">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-1">
                                        <label> </label>
                                    </div>
                                    <div class="col-lg-11">
                                        <label for="physicalFitnessDocumentStatus"> (Physical fitness certificate Shall be attached obtaining from CDMO mentioning there that the Nominee is physically fit to undergo the ASI's Course of Training) </label>
                                    </div>


                                </div> 

                            </c:if>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="mentalFitness"> b) Mental </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="mentalFitness" id="mentalFitness" class="form-control" maxlength="200"/>
                                </div>

                            </div>
                            <c:if test="${nominationForm.sltpostName eq '140858'}">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-1">
                                        <label> </label>
                                    </div>
                                    <div class="col-lg-2">
                                        <label for="physicalFitnessDocumentStatus"> (c) Whether medical fitness certificate attached issued from concerned CDMO in Form No.39 of Schedule-IX</label>
                                    </div>
                                    <div class="col-lg-3">
                                        <form:select path="physicalFitnessDocumentStatus" id="physicalFitnessDocumentStatus" class="form-control">
                                            <form:option value=""> --Select One-- </form:option>
                                            <form:option value="No"> No </form:option>
                                            <form:option value="Yes"> Yes </form:option>
                                        </form:select>
                                    </div>

                                </div> 
                            </c:if>    
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="honestyIntegrity"> Standard of honesty and integrity <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="honestyIntegrity" id="honestyIntegrity" class="form-control" maxlength="200"/>
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
                                    <form:input path="dateofPropertySubmittedByOfficer" id="dateofPropertySubmittedByOfficer" class="form-control"/>
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
                                    <label for="dpcifany"> Disciplinary Proceeding/Vigilance Cases/ Criminal cases, if any, pending/initiated (details be attached in .pdf form)</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dpcifany" id="dpcifany" class="form-control" onclick="toggleDiv()">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="N"> No </form:option>
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
                                    <form:textarea path="discDetails" id="discDetails" class="form-control" rows="4" cols="100" maxlength="450"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dateofServingifAny"> If charge has been served on the nominee in Departmental Proceeding/ Criminal Case? if Yes indicate the date of serving charge and Memo of Evidence. </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dateofServingifAny" id="dateofServingifAny" class="form-control" onclick="toggleServicingDiv()">
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
                                    <label for="remarksNominatingProfessional"> a) Professional ability and specific flair, if any. <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="remarksNominatingProfessional" id="remarksNominatingProfessional" class="form-control" rows="4" cols="100" maxlength="450"/>
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
                                    <label for="remarksNominationGeneral"> b) General Suitability <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:select path="recommendStatusDistrict" id="recommendStatusDistrict" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="RECOMMENDED">Nominated</form:option>
                                        <form:option value="NOT RECOMMENDED">NOT Nominated</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="remarksNominationGeneral"> (c)General Remarks if any from Nominating Authority </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="viewOfrecommendStatusDistrict" id="viewOfrecommendStatusDistrict" class="form-control" rows="4" cols="100"  maxlength="450"/>
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
                                    <form:select path="recommendStatus" id="recommendStatus" class="form-control" readonly="true">
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
                            <a href="editNominationroll.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
