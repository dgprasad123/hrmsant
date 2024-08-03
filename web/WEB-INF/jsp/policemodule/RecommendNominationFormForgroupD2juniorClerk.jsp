<%-- 
    Document   : RecommendNominationFormForgroupD2juniorClerk
    Created on : 18 Jan, 2022, 10:44:42 AM
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
                <form:form action="saveRecommedNominationFormController.htm" commandName="nominationForm">
                    <form:hidden path="nominationMasterId"/>
                    <form:hidden path="nominationDetailId"/>
                    <form:hidden path="nominationFormId"/>
                    <form:hidden path="sltNominationForPost"/>

                    <div class="panel panel-default">
                        <h4 style="text-align:center"> <b>RECOMMENDATION OF ELIGIBLE GROUP - D FOR PROMOTION TO THE RANK OF JUNIOR CLERK-2023</b></h4>
                        <h3 style="text-align:center">
                            <b> 
                                <div style="margin-bottom: 7px; text-align: center">
                                    <label for="fathersname">Employee Id</label> 
                                    ${nominationForm.empId}
                                </div>

                            </b>
                        </h3>
                        <div class="panel-heading">
                            <a href="viewNominationrollForRangeOffice.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a>

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
                                    <label for="fullname">Name in (full)</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="fullname" id="fullname" class="form-control" readonly="true"/>
                                    <form:hidden path="empId"/>
                                </div>


                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fathersname">Father's Name(full)</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="fathersname" id="fathersname" class="form-control" readonly="true"/>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="category">(A) Category</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="category" id="category" class="form-control">
                                        <c:forEach items="${categoryList}" var="category">
                                            <form:option value="${category.categoryid}" label="${category.categoryName}"/>
                                        </c:forEach>
                                    </form:select>
                                </div>
                                <div class="col-lg-2">
                                    <label for="serviceBookCopy">(B) CASTE CERTIFICATE</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNamecasteCertificate}">
                                        <a href="downloadCasteCertificateDocument.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNamecasteCertificate}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>

                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label>Gender</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="gender" id="gender"  size="1" class="form-control">
                                        <form:option value="M">Male</form:option>
                                        <form:option value="F">Female</form:option>
                                        <form:option value="T">Transgender</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="designation"> Designation </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="currentDesignation" id="currentDesignation" class="form-control" readonly="true"/>

                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dob">Date of Birth</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="dob" id="dob" class="form-control" readonly="true" style="width:50%"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="serviceBookCopy">1st Page of Service Book be attached</label>
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
                                    <label for="homeDistrict"> Home District </label>
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
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="qualification">Educational Qualification</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="qualification" id="qualification" class="form-control" readonly="true"/>
                                </div>

                            </div> 

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingUnintDoj">Date of joining (IN POLICE DEPT)</label>
                                </div>

                                <div class="col-lg-2">
                                    <form:input path="doeGov" id="doeGov" class="form-control" readonly="true"/> 
                                </div>


                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>
                                        <%=i++%>
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="serviceBookCopy">INITIAL RANK(GROUP-D) IN WHICH APPOINTED</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="initialRank" id="initialRank" class="form-control" readonly="true"/>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>
                                        <%=i++%>
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="appointmentmode">Mode Of Appointment</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="appointmentMode" id="appointmentMode"  size="1" class="form-control">
                                        <form:option value="direct recruitment">DIRECT RECRUITMENT</form:option>
                                        <form:option value="ras">RAS</form:option>
                                    </form:select>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="meritoriousYear"> Whether Any Promotion Given </label>
                                </div>
                                <div class="row" style="margin-bottom: 7px;"> 
                                    <div class="col-lg-2">                                    
                                        <input type="radio" id="approvGovernorMedaAwarded" name="isAnyPromotionearlier" value="yes" onclick="radioClickedForApprovalOfAuthority()"><b> (a) Yes </b>
                                    </div>

                                    <div class="col-lg-2">
                                        <input type="radio" id="NotapprovGovernorMedaAwarded" name="isAnyPromotionearlier" value="No" onclick="radioClickedForApprovalOfAuthority()"> <b>No</b>
                                    </div> 
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="promotionalrankDiv">
                                <div class="col-lg-3">
                                </div>
                                <div class="col-lg-2">
                                    <label for="meritoriousYear"> Promotional Rank Name </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="promotionalRankName" id="promotionalRankName" class="form-control" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>
                                        <%=i++%>
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="appointmentmode">Date Of Joining In Promotional Rank</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="doeinpromotional" id="doeinpromotional" class="form-control" readonly="true"/> 
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>
                                        <%=i++%>
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="appointmentmode">Date Of Joining In Present Rank</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="jodInspector" id="jodInspector" class="form-control" readonly="true"/> 
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>
                                        <%=i++%>
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="appointmentmode">Date Of Joining In Present Rank DIST/ESTT</label>
                                </div>

                                <div class="col-lg-2">
                                    <label for="appointmentmode">District Name</label> 
                                    <form:input path="presentRankDistName" id="presentRankDistName" class="form-control" readonly="true"/> 
                                </div>
                                <div class="col-lg-2">
                                    <label for="appointmentmode">DD-MM-YYYY</label>
                                    <form:input path="doeInpresentRankDist" id="doeInpresentRankDist" class="form-control" readonly="true"/> 
                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Total Period Of Service (will be auto calculate as on 01/01/2023):-</label>
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
                                    <label>
                                        <%=i++%>
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="appointmentmode">Whether Completed Regular 10 Years of as On 01.01.2023</label>
                                </div>
                                <div class="col-lg-3">
                                    <div class="row" style="margin-bottom: 7px;"> 
                                        <div class="col-lg-2">                                    
                                            <input type="radio" id="CompletedRegulartenYears" name="isCompletedRegulartenYears" value="yes" onclick="radioClickedForApprovalOfAuthority()"><b>Yes </b>
                                        </div>

                                        <div class="col-lg-2">
                                            <input type="radio" id="NotCompletedRegulartenYears" name="isCompletedRegulartenYears" value="No" onclick="radioClickedForApprovalOfAuthority()"> <b>No</b>
                                        </div> 
                                    </div>
                                </div>

                            </div>


                            <div class="row">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointYear">
                                        Posting/Deputation In Different Districts/Establishment:
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointYear"> NAME OF THE DISTRICTS/ESTTS </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointRank"> From Date </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointCadre"> To Date </label>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>

                            </div>

                            <div id="differentDistrictandEstablishment">
                                <c:if test="${not empty nominationForm.differentDistrictandEstablishmentList}">
                                    <c:forEach items="${nominationForm.differentDistrictandEstablishmentList}" var="dlist"> 
                                        <div class="row" style="margin-bottom: 10px;">
                                            <div class="col-lg-1"></div>
                                            <div class="col-lg-3"></div>
                                            <div class="col-lg-2">
                                                <input type="text" name="postingOrDeputationInOtherDistrict" value="${dlist.districtName}" class="form-control" readonly="true"/>
                                            </div>
                                            <div class="col-lg-2">
                                                <input type="text" name="postingOrDeputationInOtherDistrictFromDate" value="${dlist.districtFromDate}" class="form-control" readonly="true"/>
                                            </div>
                                            <div class="col-lg-1">
                                                <input type="text" name="postingOrDeputationInOtherDistrictToDate" value="${dlist.districtToDate}" class="form-control" readonly="true"/>
                                            </div>

                                            <div class="col-lg-1"></div>
                                        </div>
                                    </c:forEach>
                                </c:if> 
                                <div class="row" style="margin-bottom: 10px;">
                                    <div class="col-lg-1"></div>
                                    <div class="col-lg-3"></div>
                                    <div class="col-lg-2">
                                        <form:input path="postingOrDeputationInOtherDistrict" id="postingOrDeputationInOtherDistrict" class="form-control" readonly="true"/>
                                    </div>
                                    <div class="col-lg-2">
                                        <form:input path="postingOrDeputationInOtherDistrictFromDate" id="postingOrDeputationInOtherDistrictFromDate" class="form-control" readonly="true"/>
                                    </div>
                                    <div class="col-lg-1">
                                        <form:input path="postingOrDeputationInOtherDistrictToDate" id="postingOrDeputationInOtherDistrictToDate" class="form-control txtDate" readonly="true"/>
                                    </div>


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
                                    <label> From 1.1.2023 to till date of submission of Nomination Rolls </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <label> Attachments </label>
                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMajorPrior"> a) Major </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorPrior" id="punishmentMajorPrior" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorDuring" id="punishmentMajorDuring" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorFrom" id="punishmentMajorFrom" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <c:if test="${not empty nominationForm.originalFileNameMajorPunishmentForGRD}">
                                        <a href="downloadMajorPunishmentForGroupD.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameMajorPunishmentForGRD}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','MAP');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMinorPrior"> b) Minor </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorPrior" id="punishmentMinorPrior" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorDuring" id="punishmentMinorDuring" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorFrom" class="form-control"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <c:if test="${not empty nominationForm.originalFileNameMinorPunishmentForGRD}">
                                        <a href="downloadMinorPunishmentForGroupD.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameMinorPunishmentForGRD}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','MIP');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                </div>

                            </div>
                            <%--<div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMajorPrior"> a) Major </label>
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
                                    <label for="punishmentMinorPrior"> b) Minor </label>
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

                            </div>  --%>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> Vigilance Case/Criminal Case/Hrpc Related Cases Pending (AS ON DATE)  </label>
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
                                    <label for="dateofServingifAny"> Departmental Proceeding Pending(AS ON DATE) </label>
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
                            <div class="row" style="margin-bottom: 7px;" id="hideDiscDETAILRow">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> give  details </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="servingChargeDetail" id="servingChargeDetail" class="form-control" rows="4" cols="100" maxlength="450" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dateofServingifAny"> Contempleted(AS ON DATE) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="isContempletedAsOnDate" id="isContempletedAsOnDate" class="form-control" onclick="toggleContempletedDiv()">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="N"> No </form:option>
                                        <form:option value="Y"> Yes </form:option>
                                    </form:select>
                                </div>

                            </div> 

                            <div class="row" style="margin-bottom: 7px;" id="contempletedAsOnDate">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="documentServingCopy"> Upload document </label>
                                </div>
                                <div class="col-lg-6">
                                    <c:if test="${not empty nominationForm.originalFileNameContempletedDocument}">
                                        <a href="downloadServing.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameContempletedDocument}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>

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
                                    <label for="casteCertificate"> Annexure-A(Willingness Application)</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNamewillingnessCertificateForSrclerk}">
                                        <a href="downloadWillingnessCertificateForGroupD.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNamewillingnessCertificateForSrclerk}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        
                                </div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="recommendStatus"> Nomination Status from Dist/Estt</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:select path="recommendStatusDistrict" id="recommendStatusDistrict" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="RECOMMENDED">NOMINATED</form:option>
                                        <form:option value="NOT RECOMMENDED">NOT NOMINATED</form:option>
                                    </form:select>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="periodParticularsTraining">General Remarks if any </label>
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
                                    <label for="recommendStatus"> Recommendation Status of Recommending Authority</label>
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
