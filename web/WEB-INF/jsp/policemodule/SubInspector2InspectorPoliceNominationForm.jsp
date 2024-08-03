<%-- 
    Document   : SubInspector2InspectorPoliceNominationForm
    Created on : 18 Nov, 2021, 3:16:37 PM
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
                $('#servingChargeOfCaseDate').datetimepicker({
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
                $('#dateOfPassedTrainingAsi').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                $('#dateofPropertySubmittedByOfficer').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });


                toggleCCROLLDiv();

                toggleDiv();

                toggleFitnessDiv();

                toggleServicingDiv();

                togglePassTrainingDiv();

                togglepropertyDiv();


            });

            function calculatePunishmentPrior() {
                totalpunishment1 = parseInt(0 + $("#punishmentMajorPrior").val()) + parseInt(0 + $("#punishmentMinorPrior").val());
                $("#totalpunishment1").val(totalpunishment1);
            }
            function calculatePunishmentDuring() {
                totalpunishment2 = parseInt(0 + $("#punishmentMajorDuring").val()) + parseInt(0 + $("#punishmentMinorDuring").val());
                $("#totalpunishment2").val(totalpunishment2);
            }
            function calculatePunishmentFrom() {
                totalpunishment3 = parseInt(0 + $("#punishmentMajorFrom").val()) + parseInt(0 + $("#punishmentMinorFrom").val());
                $("#totalpunishment3").val(totalpunishment3);
            }

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
                if ($("#gradeSerialNo").val() == '') {
                    alert('gradation Serial Number can not be blank.');
                    document.getElementById('gradeSerialNo').focus();
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
                
                if ($("#presentRank").val() == '') {
                    alert('Present Rank Can not be Blank');
                    document.getElementById('presentRank').focus();
                    return false;
                }
                if ($("#havePassedTrainingAsi").val() == '') {
                    alert('Please Enter Have Passed Necessary Pre-Promotional training');
                    document.getElementById('havePassedTrainingAsi').focus();
                    return false;
                }
                if ($("#havePassedTrainingAsi").val() == 'Yes') {
                    if ($("#orderNoForPassedTrainingAsi").val() == '') {
                        alert('Please enter order No For Passed Training Asi');
                        document.getElementById('orderNoForPassedTrainingAsi').focus();
                        return false;
                    }
                    if ($("#dateOfPassedTrainingAsi").val() == '') {
                        alert('Please enter date of Passed Training Asi ');
                        document.getElementById('dateOfPassedTrainingAsi').focus();
                        return false;
                    }
                }
                if ($("#battalionWorkExperienceifany").val() == '') {
                    alert('Please Enter battalion Work Experience ifany');
                    document.getElementById('battalionWorkExperienceifany').focus();
                    return false;
                }
                if ($("#punishmentMajorPrior").val() == '') {
                    alert('Please enter punishment Major Prior. ');
                    document.getElementById('punishmentMajorPrior').focus();
                    return false;
                }
                if ($("#punishmentMajorDuring").val() == '') {
                    alert('Please enter punishment Major During. ');
                    document.getElementById('punishmentMajorDuring').focus();
                    return false;
                }
                if ($("#punishmentMajorFrom").val() == '') {
                    alert('Please enter punishment Major From. ');
                    document.getElementById('punishmentMajorFrom').focus();
                    return false;
                }
                if ($("#punishmentMinorPrior").val() == '') {
                    alert('Please enter punishment Minor Prior. ');
                    document.getElementById('punishmentMinorPrior').focus();
                    return false;
                }
                if ($("#punishmentMinorDuring").val() == '') {
                    alert('Please enter punishment Minor During. ');
                    document.getElementById('punishmentMinorDuring').focus();
                    return false;
                }
                if ($("#punishmentMinorFrom").val() == '') {
                    alert('Please enter punishment Minor From. ');
                    document.getElementById('punishmentMinorFrom').focus();
                    return false;
                }


                if ($("#jodInspector").val() == '') {
                    alert('Present rank with date of joining can not be blank.');
                    document.getElementById('jodInspector').focus();
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

                if ($("#dpcifany").val() == '') {
                    alert('Please enter Departmental/ Disciplinary Proceedings/ Vigilance Enquiry/ Criminal Cases ');
                    document.getElementById('dpcifany').focus();
                    return false;
                }


                if ($("#dpcifany").val() == 'Yes') {
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

                if ($("#dateofServingifAny").val() == '') {
                    alert('Please enter If charge has been served on the nominee in Departmental Proceeding/ Criminal Case ');
                    document.getElementById('dateofServingifAny').focus();
                    return false;
                }


                if ($("#dateofServingifAny").val() == 'Yes') {
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
                    if ($("#servingChargeOfCaseDate").val() == '') {
                        alert('Please enter date of serving charge. ');
                        document.getElementById('servingChargeOfCaseDate').focus();
                        return false;
                    }
                }



                if ($("#declarationAccept").val() == '') {
                    alert('Please select seiral no 15.');
                    document.getElementById('declarationAccept').focus();
                    return false;
                }
                if ($("#propertyStatementSubmittedifAny").val() == '') {
                    alert('Please enter property Statement Submittedif Any ');
                    document.getElementById('propertyStatementSubmittedifAny').focus();
                    return false;
                }
                if ($("#propertyStatementSubmittedifAny").val() == 'Yes') {
                    if ($("#dateofPropertySubmittedByOfficer").val() == '') {
                        alert('Please enter Property Statement Submitted Date. ');
                        document.getElementById('dateofPropertySubmittedByOfficer').focus();
                        return false;
                    }
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
                if ($("#recommendStatusDistrict").val() == '') {
                    alert('Please enter recommend Status District ');
                    document.getElementById('recommendStatusDistrict').focus();
                    return false;
                }
            }

            function toggleDiv() {
                if ($("#dpcifany").val() == 'Yes') {
                    $("#hideDiscDocumentRow").show();
                    $("#hideDiscDocumentRow2").show();
                    $("#hideDiscDocumentRow3").show();
                } else {
                    $("#hideDiscDocumentRow").hide();
                    $("#hideDiscDocumentRow2").hide();
                    $("#hideDiscDocumentRow3").hide();
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

            function togglePassTrainingDiv() {
                if ($("#havePassedTrainingAsi").val() == 'Yes') {
                    $("#dateofPassTrainingDiv").show();

                } else {
                    $("#dateofPassTrainingDiv").hide();
                }
            }




            function toggleServicingDiv() {
                if ($("#dateofServingifAny").val() == 'Yes') {
                    $("#dateofServingDiv").show();
                    $("#hideServingChargeRow").show();

                } else {
                    $("#dateofServingDiv").hide();
                    $("#dateofServing").val('');
                    $("#hideServingChargeRow").hide();
                }
            }

            function togglepropertyDiv() {
                if ($("#propertyStatementSubmittedifAny").val() == 'Yes') {
                    $("#dateofSubmittingPropertyDiv").show();
                } else {
                    $("#dateofSubmittingPropertyDiv").hide();
                }
            }
            function deletePoliceNominationAttachment(attchid, doctype) {
                if (confirm('Are you sure to Delete this Attachment?')) {
                    $.ajax({
                        type: "POST",
                        data: {attchId: attchid, doctype: doctype},
                        url: "deletePoliceNominationAttachment.htm",
                        dataType: "json",
                        success: function(data) {
                            if (data.deletestatus == 1) {
                                location.reload();
                            } else {
                                alert("Deletion Failed. Try Again after some time.");
                            }
                        }
                    });
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
                <form:form action="savenominationFormController.htm" commandName="nominationForm" enctype="multipart/form-data">
                    <form:hidden path="nominationMasterId"/>
                    <form:hidden path="nominationDetailId"/>
                    <form:hidden path="nominationFormId"/>
                    <form:hidden path="nominationtype"/>
                    <form:hidden path="sltNominationForPost"/>

                    <div class="panel panel-default">
                        <h4 style="text-align:center"><b> NOMINATION ROLL</b></h4>
                        <h3 style="text-align:center">
                            <b> 
                                <div style="margin-bottom: 7px; text-align: center">                                                                       
                                    <label for="fathersname">Nomination Serial No.</label>                                    
                                    <form:input path="gradeSerialNo" id="gradeSerialNo" maxlength="4" onkeypress="return numbersOnly(event)"/>                                    
                                </div> 
                            </b>
                        </h3>
                        <h3 style="text-align:center"><b> PROMOTION OF SUB INSPECTOR OF POLICE (ARMED) TO THE RANK OF INSPECTOR OF POLICE (ARMED) </b></h3>
                        <h3 style="text-align:center"><b> CSB - 2024 </b></h3>
                        <h3 style="text-align:center"><b>
                                <label for="fathersname">Employee Id</label> 
                                ${nominationForm.empId}</b></h3>
                        <div class="panel-heading">
                            <a href="editNominationroll.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                            <input type="submit" value="Save Form" class="btn btn-success"  onclick="return validateForm()"/>
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
                                    <form:input path="qualification" id="qualification" class="form-control"/>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="homeDistrict">  e) Home District </label>
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
                                    <label for="postingPlace">f) Unit of Posting (Official Place) </label>
                                </div>

                                <div class="col-lg-2">
                                    <form:input path="postingPlace" id="postingPlace" class="form-control"/>  
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingUnintDoj">g) Date of joining (in Present Place)</label>
                                </div>

                                <div class="col-lg-2">
                                    <form:input path="postingUnintDoj" id="postingUnintDoj" class="form-control"/>  
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="category">h) Category</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="category" id="category" class="form-control">
                                        <c:forEach items="${categoryList}" var="category">
                                            <form:option value="${category.categoryid}" label="${category.categoryName}"/>
                                        </c:forEach>
                                    </form:select>
                                </div>
                                <div class="col-lg-2">
                                    <label for="casteCertificate">i)Caste Certificate(if any)</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNamecasteCertificate}">
                                        <a href="downloadCasteCertificateDocument.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNamecasteCertificate}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','CASTE');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNamecasteCertificate}">
                                        <input type="file" name="casteCertificate" id="casteCertificate" class="fileupload"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="fathersname">j)GPF No</label>
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
                                    <label for="adverseIfany"> Cadre List </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="cadreName" id="cadreName" class="form-control" onclick="toggleCCROLLDiv()">
                                        <form:option value=""> --Select Cadre-- </form:option>
                                        <form:option value="1495"> GENERAL CADRE </form:option>
                                        <form:option value="1494"> BATALLION  CADRE(GURKHA COY)</form:option>
                                    </form:select>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="doeGov">Date of enlistment with rank (Entry in Govt. Service)</label>
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
                                    <label for="serviceBookCopy">1st Page of Service Book be attached</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNameSB}">
                                        <a href="downloadServiceBookCopy.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameSB}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','SB');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
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
                                    <form:select path="presentRank" id="presentRank" class="form-control">
                                        <form:option value="">Select</form:option>
                                        <form:option value="Dy.Subedar">Dy.Subedar</form:option>
                                        <form:option value="Sergeant">Sergeant</form:option>
                                        <form:option value="Drill SubInspector">Drill SI</form:option>
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
                                    <label> Length of service (Auto calculated From the Date Of Enlishment as on 31-DEC-2023):-</label>
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
                                    <label>a)  Length of service from presnt Rank: (Auto Calculated from the Present Rank as on 31-DEC-2023):-</label>
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
                                    <form:input path="yearinServiceFromPresentRank" id="yearinServiceFromPresentRank" class="form-control" readonly="true" maxlength="2" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="monthrinServiceFromPresentRank" id="monthrinServiceFromPresentRank" class="form-control" readonly="true" maxlength="2" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="daysinServiceFromPresentRank" id="daysinServiceFromPresentRank" class="form-control" readonly="true" maxlength="2" onkeypress="return numbersOnly(event)"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label>b)  Have Passed Necessary Pre-Promotional training Course as Pescribed by the DG and IG Of Police:-</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="havePassedTrainingAsi" id="havePassedTrainingAsi" class="form-control" onclick="togglePassTrainingDiv()">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
                                    </form:select>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="dateofPassTrainingDiv">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="servingChargeOfCaseDate"> Order Number of Passing Training</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="orderNoForPassedTrainingAsi" id="orderNoForPassedTrainingAsi" class="form-control" maxlength="6"/>  
                                </div>
                                <div class="col-lg-2">
                                    <label for="servingChargeOfCaseDate"> Date of Passing Training</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="dateOfPassedTrainingAsi" id="dateOfPassedTrainingAsi" class="form-control"/>  
                                </div>
                            </div> 


                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label>c)  Have at least 2 years of Work experience in a  Battalion / SOG excluding the Period of Leave,attachment and Deputation on(01-01-2024):-</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="battalionWorkExperienceifany" id="battalionWorkExperienceifany" class="form-control">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
                                        <form:option value="Exempted"> Exempted </form:option>
                                    </form:select>
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
                                    <form:textarea path="periodParticularsTraining" id="periodParticularsTraining" class="form-control" rows="4" cols="100"  maxlength="450"/>
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
                                    <label for="rewardMedalsPrior"> c) Other Awards/Medals/ recognisation if any </label>
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
                                    <form:input path="punishmentMinorPrior" id="punishmentMinorPrior" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentPrior()"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorDuring" id="punishmentMinorDuring" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentDuring()"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorFrom" id="punishmentMinorFrom" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentFrom()"/>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="punishmentMinorPrior"> c)Total </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="totalpunishment1" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentPrior()"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="totalpunishment2" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentDuring()"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="totalpunishment3" class="form-control" maxlength="3" onkeypress="return numbersOnly(event)" onkeyup="calculatePunishmentFrom()"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="punishmentCopy">d)In case of Major/Minor punishment awarded during preceding 5 years of the
                                        CSB, then copy of the entries of punishments in service book to be attached
                                        in .pdf form. <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="punishmentDetail" id="punishmentDetail" class="form-control" rows="8" cols="200"  maxlength="1000"/>
                                    <label style="color:red"> (Maximum 1000 characters allowed) </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNamePunishment}">
                                        <a href="downloadPunishmentDocument.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNamePunishment}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','PUNISH');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>    
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNamePunishment}">
                                        <input type="file" name="punishmentCopy" id="punishmentCopy" class="fileupload"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
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
                                    <label for="physicalFitness"> a) Physical </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="physicalFitness" id="physicalFitness" class="form-control" maxlength="200"/>
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
                                    <form:input path="mentalFitness" id="mentalFitness" class="form-control" maxlength="200"/>
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
                                        <form:select path="physicalFitnessDocumentStatus" id="physicalFitnessDocumentStatus" class="form-control" onclick="toggleFitnessDiv()">
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
                                        <form:select path="remarksofCdmo" id="remarksofCdmo" class="form-control">
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
                                    <form:input path="honestyIntegrity" id="honestyIntegrity" class="form-control" maxlength="200"/>
                                </div>

                            </div>


                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="adverseIfany"> PAR Status For the Preceeding 5 Years As on 31-03-2023</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="ccrollsDetail" id="ccrollsDetail" class="form-control" rows="4" cols="100" maxlength="200"/>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label>i) Whether Property Statement Submitted /Not <span style="color: red">(For the Financial Year 2023)*</span></label>
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
                                    <label for="dpcifany"> Disciplinary Proceedings/ Vigilance Enquiry/ Criminal Cases / HRPC Cases, if any, initiated   </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dpcifany" id="dpcifany" class="form-control" onclick="toggleDiv()">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
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
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','DPC');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
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
                                    <form:textarea path="discDetails" id="discDetails" class="form-control" rows="4" cols="100" maxlength="1000"/>
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
                                    <label for="dateofServingifAny"> If charge has been served on the nominee in Departmental Proceeding/ Criminal Case? if Yes indicate the date of serving charge and ME. </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dateofServingifAny" id="dateofServingifAny" class="form-control" onclick="toggleServicingDiv()">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
                                    </form:select>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;" id="dateofServingDiv">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="servingChargeOfCaseDate"> Date of serving charge </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="servingChargeOfCaseDate" id="servingChargeOfCaseDate" class="form-control"/>  
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
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','SERV_CHRG');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>    
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNameServing}">
                                        <input type="file" name="documentServingCopy"  id="documentServingCopy"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
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
                                    <label for="remarksNominationGeneral"> b) General Suitability </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="remarksNominationGeneral" id="remarksNominationGeneral" class="form-control" rows="4" cols="100" maxlength="1200"/>
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
                                    <label for="recommendStatus"> Nomination / Views of Nominating Authority<span style="color: red">*</span></label>
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
                                    <label for="generalremarks"> Remarks if any from Nominating Authority</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="viewOfrecommendStatusDistrict" id="viewOfrecommendStatusDistrict" class="form-control" rows="4" cols="100"  maxlength="450"/>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <a href="editNominationroll.htm?nominationMasterId=${nominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                            <input type="submit" value="Save Form" class="btn btn-success" onclick="return validateForm()"/>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>