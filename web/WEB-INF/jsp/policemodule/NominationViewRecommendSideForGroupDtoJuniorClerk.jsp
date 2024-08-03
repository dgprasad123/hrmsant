<%-- 
    Document   : NominationViewRecommendSideForGroupDtoJuniorClerk
    Created on : 6 Nov, 2023, 4:16:34 PM
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

                $('#doeinpromotional').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#doeInpresentRankDist').datetimepicker({
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

                $('#postingOrDeputationInOtherDistrictFromDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                $('#postingOrDeputationInOtherDistrictToDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                toggleCCROLLDiv();

                toggleDiv();

                toggleFitnessDiv();

                toggleServicingDiv();


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
                if ($("#fullname").val() == '') {
                    alert('Name in (full) can not be blank.');
                    document.getElementById('fullname').focus();
                    return false;
                }
                if ($("#cadreName").val() == '') {
                    alert('Cadre Name can not be blank.');
                    document.getElementById('cadreName').focus();
                    return false;
                }
                if ($("#category").val() == '') {
                    alert('Category can not be blank.');
                    document.getElementById('category').focus();
                    return false;
                }
                if ($("#fathersname").val() == '') {
                    alert('Fathers name can not be blank.');
                    document.getElementById('fathersname').focus();
                    return false;
                }
                if ($("#dob").val() == '') {
                    alert('Date of birth can not be blank.');
                    document.getElementById('dob').focus();
                    return false;
                }
                if ($("#qualification").val() == '') {
                    alert('Educational Qualification can not be blank.');
                    document.getElementById('qualification').focus();
                    return false;
                }
                if ($("#homeDistrict").val() == '') {
                    alert('Please select Home District');
                    document.getElementById('homeDistrict').focus();
                    return false;
                }

                if ($("#postingPlace").val() == '') {
                    alert(' Unit of Posting can not be blank.');
                    document.getElementById('postingPlace').focus();
                    return false;
                }

                if ($("#postingUnintDoj").val() == '') {
                    alert(' Unit of Posting with date of joining can not be blank.');
                    document.getElementById('postingUnintDoj').focus();
                    return false;
                }
                if ($("#doeGov").val() == '') {
                    alert('Date of enlistment with rank (Entry in Govt. Service) can not be blank.');
                    document.getElementById('doeGov').focus();
                    return false;
                }
                var _validFileExtensions = [".pdf"];
            <c:if test="${empty nominationForm.originalFileNameSB}">
                if ($("#serviceBookCopy").val() == '') {
                    alert('Please Upload 1st page of Service Book. ');
                    document.getElementById('serviceBookCopy').focus();
                    return false;
                } else {
                    var fi = document.getElementById("serviceBookCopy");
                    var fsize = fi.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 3072) {
                        alert("File too Big, please select a file less than 3mb");
                        return false;
                    }

                    if (fi.value.length > 0) {
                        var blnValid = false;
                        for (var j = 0; j < _validFileExtensions.length; j++) {
                            var sCurExtension = _validFileExtensions[j];
                            if (fi.value.substr(fi.value.length - sCurExtension.length, sCurExtension.length).toLowerCase() == sCurExtension.toLowerCase()) {
                                blnValid = true;
                                break;
                            }
                        }

                        if (!blnValid) {
                            alert("Sorry, " + fi.value + " is invalid, allowed extensions are: " + _validFileExtensions.join(", "));
                            return false;
                        }
                    }



                }
            </c:if>


                if ($("#jodInspector").val() == '') {
                    alert('Present rank with date of joining can not be blank.');
                    document.getElementById('jodInspector').focus();
                    return false;
                }
                if ($("#doeinpromotional").val() == '') {
                    alert('Date Of joining in Promotional Rank can not be blank.');
                    document.getElementById('doeinpromotional').focus();
                    return false;
                }





                if ($("#periodParticularsTraining").val() == '') {
                    alert('Please enter Period and Particulars of training and courses undergone');
                    document.getElementById('periodParticularsTraining').focus();
                    return false;
                }

                if ($("#officeOrderNo").val() == '') {
                    alert('Present enter Office order No.');
                    document.getElementById('officeOrderNo').focus();
                    return false;
                }

                if ($("#passingTrainingdate").val() == '') {
                    alert('Present enter Date of passing ASIs course of training.');
                    document.getElementById('passingTrainingdate').focus();
                    return false;
                }



            <c:if test="${empty nominationForm.originalFileNamePunishment}">
                if ($("#punishmentCopy").val() != '') {
                    var fi = document.getElementById("punishmentCopy");
                    var fsize = fi.files.item(0).size;
                    var file = Math.round((fsize / 1024));
                    if (file >= 4096) {
                        alert("File too Big, please select a file less than 4mb");
                        return false;
                    }


                    var filePath = fi.value;
                    // Allowing file type 
                    var allowedExtensions = /(\.pdf)$/i;
                    if (!allowedExtensions.exec(filePath)) {
                        alert('Invalid file type, Only PDF allowed');
                        filePath.value = '';
                        return false;
                    }
                }
            </c:if>

                if ($("#powerofDecesion").val() == '') {
                    alert('Please enter Power of decision making and assuming responsibility. ');
                    document.getElementById('powerofDecesion').focus();
                    return false;
                }
                if ($("#physicalFitness").val() == '') {
                    alert('Please enter details about Physical Fitness. ');
                    document.getElementById('physicalFitness').focus();
                    return false;
                }




                if ($("#physicalFitnessDocumentStatus").val() == 'Yes') {


            <c:if test="${empty nominationForm.originalFileNameFitnessDocument}">
                    if ($("#fitnessDocument").val() == '') {
                        alert('Please Upload Physical fitness certificate. ');
                        document.getElementById('fitnessDocument').focus();
                        return false;
                    } else {
                        var fi = document.getElementById("fitnessDocument");
                        var fsize = fi.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 3072) {
                            alert("File too Big, please select a file less than 3mb");
                            return false;
                        }


                        var filePath = fi.value;
                        // Allowing file type 
                        var allowedExtensions = /(\.pdf)$/i;
                        if (!allowedExtensions.exec(filePath)) {
                            alert('Invalid file type');
                            filePath.value = '';
                            return false;
                        }
                    }
            </c:if>
                }

                if ($("#mentalFitness").val() == '') {
                    alert('Please enter details about Mental Fitness. ');
                    document.getElementById('mentalFitness').focus();
                    return false;
                }

                if ($("#physicalFitnessDocumentStatus").val() == '') {
                    alert('Please select physical fitness attach info . ');
                    document.getElementById('physicalFitnessDocumentStatus').focus();
                    return false;
                }
                if ($("#physicalFitnessDocumentStatus").val() == 'Yes') {
                    if ($("#remarksofCdmo").val() == '') {
                        alert('Please select Remarks of CDMO. ');
                        document.getElementById('remarksofCdmo').focus();
                        return false;
                    }
                }

                if ($("#honestyIntegrity").val() == '') {
                    alert('Please enter Standard of honesty and integrity. ');
                    document.getElementById('honestyIntegrity').focus();
                    return false;
                }


                if ($("#dpcifany").val() == 'Y') {
            <c:if test="${empty nominationForm.originalFileName}">
                    if ($("#discDocument").val() == '') {
                        alert('Please Upload Departmental/ Disciplinary Proceedings/ Vigilance Enquiry/ Criminal Cases document. ');
                        document.getElementById('discDocument').focus();
                        return false;
                    } else {
                        var fi = document.getElementById("discDocument");
                        var fsize = fi.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 3072) {
                            alert("File too Big, please select a file less than 3mb");
                            return false;
                        }


                        var filePath = fi.value;
                        // Allowing file type 
                        var allowedExtensions = /(\.pdf)$/i;
                        if (!allowedExtensions.exec(filePath)) {
                            alert('Invalid file type');
                            filePath.value = '';
                            return false;
                        }
                        if ($("#discDetails").val() == '') {
                            alert('Please enter details. ');
                            document.getElementById('discDetails').focus();
                            return false;
                        }

                    }
            </c:if>

                }


                if ($("#dateofServingifAny").val() == 'Y') {
            <c:if test="${empty nominationForm.originalFileNameServing}">
                    if ($("#documentServingCopy").val() == '') {
                        alert('Please Upload Serving Charge. ');
                        document.getElementById('documentServingCopy').focus();
                        return false;
                    } else {
                        var fi = document.getElementById("documentServingCopy");
                        var fsize = fi.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 3072) {
                            alert("File too Big, please select a file less than 3mb");
                            return false;
                        }


                        var filePath = fi.value;
                        // Allowing file type 
                        var allowedExtensions = /(\.pdf)$/i;
                        if (!allowedExtensions.exec(filePath)) {
                            alert('Invalid file type');
                            filePath.value = '';
                            return false;
                        }
                    }
            </c:if>
                    if ($("#dateofServing").val() == '') {
                        alert('Please enter date of serving charge. ');
                        document.getElementById('dateofServing').focus();
                        return false;
                    }
                }



                if ($("#declarationAccept").val() == '') {
                    alert('Please select seiral no 15.');
                    document.getElementById('declarationAccept').focus();
                    return false;
                }


                if ($("#remarksNominatingProfessional").val() == '') {
                    alert('Please enter Professional ability and specific flair, if any ');
                    document.getElementById('remarksNominatingProfessional').focus();
                    return false;
                }

                if ($("#remarksNominationGeneral").val() == '') {
                    alert('Please enter General Suitability ');
                    document.getElementById('remarksNominationGeneral').focus();
                    return false;
                }
            }

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
            function toggleContempletedDiv() {
                if ($("#isContempletedAsOnDate").val() == 'Y') {
                    $("#contempletedAsOnDate").show();

                } else {
                    $("#contempletedAsOnDate").hide();
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
                            <a href="viewNominationrollList.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                        </div>
                        <div class="panel panel-default">
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
                                            <c:if test="${empty nominationForm.originalFileNameMajorPunishmentForGRD}">
                                            <input type="file" name="MajorPunishmentForGRDDocument"  id="serviceBookCopy" /> <span style="color:red">(Only PDF)</span>
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
                                            <c:if test="${empty nominationForm.originalFileNameMinorPunishmentForGRD}">
                                            <input type="file" name="MinorPunishmentForGRDDocument"  id="serviceBookCopy" /> <span style="color:red">(Only PDF)</span>
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
                            </div>
                            <div class="panel-footer">
                                <a href="viewNominationrollList.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 

                            </div>
                        </div>

                    </div>

                </div>
            </form:form>
        </div>
    </div>
</body>
</html>

