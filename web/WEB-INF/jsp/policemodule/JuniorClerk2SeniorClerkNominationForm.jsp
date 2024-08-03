<%-- 
    Document   : JuniorClerk2SeniorClerkNominationForm
    Created on : 29 Jan, 2022, 11:14:16 AM
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

                $("#documentForPreliminaryExam").hide();
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

                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                $('#postingOrDeputationInOtherDistrictToDate.txtDate').datetimepicker({
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

                isPassPreliminaryExamination();


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
                var postingDistrict = $('input[name=postingOrDeputationInOtherDistrict]').length;
                if ($("#gradeSerialNo").val() == '') {
                    alert('Gradation Serial Number Can not be blank');
                    document.getElementById('gradeSerialNo').focus();
                    return false;
                }
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
                if ($("#presentRankDistName").val() == '') {
                    alert('present Posting can not be blank.');
                    document.getElementById('presentRankDistName').focus();
                    return false;
                }

                if ($("#appointmentMode").val() == '') {
                    alert(' Appointment Mode can not be blank.');
                    document.getElementById('appointmentMode').focus();
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
           



                if ($("#initialRank").val() == '') {
                    alert('Initial Rank can not be blank.');
                    document.getElementById('initialRank').focus();
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
                if ($("#isCompletedRegulartenYears").val() == '') {
                    alert('Please Choose Is Completed Regular ten Years.');
                    document.getElementById('isCompletedRegulartenYears').focus();
                    return false;
                }
                if ($("#isPassPrelimilaryExam").val() == '') {
                    alert('Please Select Is Pass Prelimilary Exam.');
                    document.getElementById('isPassPrelimilaryExam').focus();
                    return false;
                }

                if ($("#isPassPrelimilaryExam").val() == 'Yes') {
            <c:if test="${empty nominationForm.originalFileNameprelimilaryExamDocument}">
                    if ($("#prelimilaryExamDocument").val() == '') {
                        alert('Please Upload prelimilary Exam Document. ');
                        document.getElementById('prelimilaryExamDocument').focus();
                        return false;
                    } else {
                        var fi = document.getElementById("prelimilaryExamDocument");
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

                if ($("#punishmentMajorPrior").val() == '') {
                    alert('Please enter Punishment Major Prior. ');
                    document.getElementById('punishmentMajorPrior').focus();
                    return false;
                }
                if ($("#punishmentMajorDuring").val() == '') {
                    alert('Please enter Punishment Major During. ');
                    document.getElementById('punishmentMajorDuring').focus();
                    return false;
                }
                if ($("#punishmentMinorPrior").val() == '') {
                    alert('Please enter Punishment Minor Prior. ');
                    document.getElementById('punishmentMinorPrior').focus();
                    return false;
                }
                if ($("#punishmentMinorDuring").val() == '') {
                    alert('Please enter Punishment Minor During. ');
                    document.getElementById('punishmentMinorDuring').focus();
                    return false;
                }

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

                for (var i = 0; i < postingDistrict; i++) {
                    if ($("#postingOrDeputationInOtherDistrict" + i).val() == '') {
                        alert('posting Or Deputation In Other District  can not be blank!');
                        $("#postingOrDeputationInOtherDistrict" + i).focus();
                        return false;
                        break;
                    }
                    if ($("#postingOrDeputationInOtherDistrictFromDate" + i).val() == '') {
                        alert('posting Or Deputation In Other District From Date can not be blank!');
                        $("#postingOrDeputationInOtherDistrictFromDate" + i).focus();
                        return false;
                        break;
                    }
                    if ($("#postingOrDeputationInOtherDistrictToDate" + i).val() == '') {
                        alert('posting Or Deputation In Other District To Date can not be blank!');
                        $("#postingOrDeputationInOtherDistrictToDate" + i).focus();
                        return false;
                        break;
                    }
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
                if ($("#dpcifany").val() == '') {
                    alert('Please enter Departmental/ Disciplinary Proceedings/ Vigilance Enquiry/ Criminal Cases ');
                    document.getElementById('dpcifany').focus();
                    return false;
                }
                if ($("#dateofServingifAny").val() == '') {
                    alert('Please enter If charge has been served on the nominee ');
                    document.getElementById('dateofServingifAny').focus();
                    return false;
                }
                if ($("#criminalcasePresentStatusifAny").val() == '') {
                    alert('Please enter Present Status Of Criminal Case ');
                    document.getElementById('criminalcasePresentStatusifAny').focus();
                    return false;
                }
                if ($("#isContempletedAsOnDate").val() == '') {
                    alert('Please enter Is Contempleted As On Date ');
                    document.getElementById('isContempletedAsOnDate').focus();
                    return false;
                }
                if ($("#recommendStatusDistrict").val() == '') {
                    alert('Please enter Recommend Status District');
                    document.getElementById('recommendStatusDistrict').focus();
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
            function isPassPreliminaryExamination() {
                if ($("#isPassPrelimilaryExam").val() == 'Yes') {
                    $("#documentForPreliminaryExam").show();
                } else {
                    $("#documentForPreliminaryExam").hide();
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
            function togglepropertyDiv() {
                if ($("#propertyStatementSubmittedifAny").val() == 'Yes') {
                    $("#dateofSubmittingPropertyDiv").show();
                } else {
                    $("#dateofSubmittingPropertyDiv").hide();
                }
            }


            function addDifferentDistrictandEstablishment() {
                var childlength = $("div#differentDistrictandEstablishment > div").length;

                var rowcount = childlength;

                var formstring = "<div class='row' style='margin-bottom: 7px;'>";
                formstring = formstring + "<div class='col-lg-1'></div>";
                formstring = formstring + "<div class='col-lg-3'></div>";
                formstring = formstring + "<div class='col-lg-2'><input type='text' name='postingOrDeputationInOtherDistrict' id='postingOrDeputationInOtherDistrict" + rowcount + "' class='form-control'/></div>";
                formstring = formstring + "<div class='col-lg-2'><input type='text' name='postingOrDeputationInOtherDistrictFromDate' id='postingOrDeputationInOtherDistrictFromDate" + rowcount + "' class='form-control txtDate' readonly='true'/></div>";
                formstring = formstring + "<div class='col-lg-1'><input type='text' name='postingOrDeputationInOtherDistrictToDate' id='postingOrDeputationInOtherDistrictToDate" + rowcount + "' class='form-control txtDate' maxlength='4' onkeypress='return numbersOnly(event)'/></div>";
                formstring = formstring + "<div class='col-lg-1'></div>";
                formstring = formstring + "</div>";

                $("div#differentDistrictandEstablishment > div:last").after(formstring);

                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
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
                        <h4 style="text-align:center"><b><u> NOMINATION ROLL </u></b></h4>
                        <h3 style="text-align:center">
                            <b> 
                                <div style="margin-bottom: 7px; text-align: center">                                                                       
                                    <label for="fathersname">Nomination Serial No.</label>                                    
                                    <form:input path="gradeSerialNo" id="gradeSerialNo" maxlength="4" onkeypress="return numbersOnly(event)"/>                                    
                                </div> 
                            </b>
                        </h3>
                        <h3 style="text-align:center">
                            <b>RECOMMENDATION OF ELIGIBLE JUNIOR CLERK FOR PROMOTION TO THE RANK OF SR. CLERK OF DPO CADRE-2023</b>
                        </h3>
                        <h3 style="text-align:center"><b>
                                <label for="fathersname">Employee Id</label> 
                                ${nominationForm.empId}</b>
                        </h3>
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
                                    <label for="officeName">NAME OF THE DIST./ESTT. :</label>
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
                                    <label for="fathersname">GPF No</label>
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
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','CASTE');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNamecasteCertificate}">
                                        <input type="file" name="casteCertificate" id="casteCertificate" class="fileupload"/> <span style="color:red">(Only PDF)</span>
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
                                <div class="col-lg-2">
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
                                    <label for="qualification">Educational Qualification<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="qualification" id="qualification" class="form-control"/>
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
                                    <label for="serviceBookCopy">1st Page of Service Book be attached <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
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
                                    <label for="homeDistrict"> Home District <span style="color: red">*</span> </label>
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
                                    <label><%=i++%></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingUnintDoj">DATE OF ENLISTMENT/JOINING <span style="color: red">*</span></label>
                                </div>

                                <div class="col-lg-2">
                                    <form:input path="doeGov" id="doeGov" class="form-control" readonly="true"/> 
                                </div>
                                <div class="col-lg-2">

                                </div>

                                <div class="col-lg-2">

                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>
                                        <%=i++%>
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="appointmentmode">Mode Of Appointment <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="appointmentMode" id="appointmentMode"  size="1" class="form-control">
                                        <form:option value="">Select</form:option>
                                        <form:option value="direct recruitment">DIRECT RECRUITMENT</form:option>
                                        <form:option value="ras">RAS</form:option>
                                    </form:select>
                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingUnintDoj">(A) PRESENT RANK <span style="color: red">*</span></label>
                                </div>

                                <div class="col-lg-3">
                                    <form:input path="initialRank" id="initialRank" class="form-control"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingPlace">(B) DATE OF JOINING (AS JR. CLERK) <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="jodInspector" id="jodInspector" class="form-control" readonly="true"/> 
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingUnintDoj">(A) PRESENT POSTING <span style="color: red">*</span></label>
                                </div>

                                <div class="col-lg-3">
                                    <form:input path="presentRankDistName" id="presentRankDistName" class="form-control" />
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingPlace">(B) DATE OF JOINING IN PRESENT POSTING <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="doeInpresentRankDist" id="doeInpresentRankDist" class="form-control" readonly="true"/> 
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> LENGTH OF SERVICE IN THE PRESENT RANK AS ON 01.01.2023</label>
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
                                    <label for="appointmentmode">Whether Completed Regular 03 YEARS OF AS ON 01.01.2023 <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <div class="row" style="margin-bottom: 7px;"> 
                                        <form:select path="isCompletedRegulartenYears" id="isCompletedRegulartenYears" class="form-control" onclick="isPassPreliminaryExamination()">
                                            <form:option value=""> Select </form:option>
                                            <form:option value="No"> No </form:option>
                                            <form:option value="Yes"> Yes </form:option>
                                        </form:select>

                                    </div>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> Whether passed Preliminary Accounts Examination  <span style="color: red">*</span> </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="isPassPrelimilaryExam" id="isPassPrelimilaryExam" class="form-control" onclick="isPassPreliminaryExamination()">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
                                    </form:select>
                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;" id="documentForPreliminaryExam">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="documentServingCopy"> Upload document </label>
                                </div>
                                <div class="col-lg-6">
                                    <c:if test="${not empty nominationForm.originalFileNameprelimilaryExamDocument}">
                                        <a href="downloadPreliminaryExam.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNameprelimilaryExamDocument}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','PRELIMEXAM');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNameprelimilaryExamDocument}">
                                        <input type="file" name="prelimilaryExamDocument"  id="prelimilaryExamDocument"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointYear">
                                        PAST POSTING/DEPUTATION IN DIFFERENT DISTRICTS/ESTABLISHMENT:
                                        (from Enlistment to till Date):<span style="color: red">*</span>
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
                                                <input type="text" name="postingOrDeputationInOtherDistrict" id="postingOrDeputationInOtherDistrict0" value="${dlist.districtName}" class="form-control" />
                                            </div>
                                            <div class="col-lg-2">
                                                <input type="text" name="postingOrDeputationInOtherDistrictFromDate" id="postingOrDeputationInOtherDistrictFromDate0" value="${dlist.districtFromDate}" class="form-control txtDate" readonly="true"/>
                                            </div>
                                            <div class="col-lg-1">
                                                <input type="text" name="postingOrDeputationInOtherDistrictToDate" id="postingOrDeputationInOtherDistrictFromDate0" value="${dlist.districtToDate}" class="form-control txtDate" readonly="true"/>
                                            </div>

                                            <div class="col-lg-1"><button type="button" onclick="addDifferentDistrictandEstablishment();" class="btn btn-danger">Add</button></div>
                                            <div class="col-lg-1"><button type="button" onclick="removeDifferentDistrictandEstablishment();" class="btn btn-danger">Remove</button></div>
                                        </div>
                                    </c:forEach>
                                </c:if> 
                                <c:if test="${empty nominationForm.differentDistrictandEstablishmentList}">
                                    <div class="row" style="margin-bottom: 10px;">
                                        <div class="col-lg-1"></div>
                                        <div class="col-lg-3"></div>
                                        <div class="col-lg-2">
                                            <form:input path="postingOrDeputationInOtherDistrict" id="postingOrDeputationInOtherDistrict0" class="form-control"/>
                                        </div>
                                        <div class="col-lg-2">
                                            <form:input path="postingOrDeputationInOtherDistrictFromDate" id="postingOrDeputationInOtherDistrictFromDate0" class="form-control txtDate" readonly="true"/>
                                        </div>
                                        <div class="col-lg-1">
                                            <form:input path="postingOrDeputationInOtherDistrictToDate" id="postingOrDeputationInOtherDistrictToDate0" class="form-control txtDate" readonly="true"/>
                                        </div>

                                        <div class="col-lg-1">
                                            <button type="button" onclick="addDifferentDistrictandEstablishment();" class="btn btn-danger">Add</button>
                                        </div>
                                    </div>
                                </c:if>
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
                                    <form:input path="rewardGSMarkPrior" id="rewardGSMarkPrior" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardGSMarkDuring" id="rewardGSMarkDuring" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardGSMarkFrom" id="rewardGSMarkFrom" class="form-control" maxlength="10"/>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="rewardMoneyOherPrior"> b) Money reward/ Other Awards </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMoneyOherPrior" id="rewardMoneyOherPrior" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMoneyOherDuring" id="rewardMoneyOherDuring" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMoneyOherFrom" id="rewardMoneyOherFrom" class="form-control" maxlength="10"/>
                                </div>

                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1" style="text-align: center">

                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="rewardMedalsPrior"> c) Other Awards/Medals/ recognisation if any </label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMedalsPrior" id="rewardMedalsPrior" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMedalsDuring" id="rewardMedalsDuring" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="rewardMedalsFrom" id="rewardMedalsFrom" class="form-control" maxlength="10"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Punishments <span style="color: red">*</span></label>
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
                                    <form:input path="punishmentMajorPrior" id="punishmentMajorPrior" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorDuring" id="punishmentMajorDuring" class="form-control" maxlength="10"/>
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
                                    <label for="punishmentMinorPrior"> b) Minor <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorPrior" id="punishmentMinorPrior" class="form-control" maxlength="10"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorDuring" id="punishmentMinorDuring" class="form-control" maxlength="10"/>
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
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="decissionmaking">Power of Decision making and assuming responsibility <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="powerOfDecissionMaking" id="powerOfDecissionMaking" class="form-control" rows="4" cols="100" maxlength="1000"/>
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
                                <div class="col-lg-6">
                                    <form:input path="physicalFitness" id="physicalFitness" class="form-control" maxlength="200"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="mentalFitness"> b) Mental <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="mentalFitness" id="mentalFitness" class="form-control" maxlength="200"/>
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
                                    <form:input path="honestyIntegrity" id="honestyIntegrity" class="form-control" maxlength="200"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="adverseIfany"> Status of CC Rolls preceeding 5 years of the DPC as on 31.03.2023 </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="adverseDetail" id="adverseDetail" class="form-control" rows="4" cols="100" maxlength="450"/>
                                </div>

                            </div> 


                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> DISCIPLINARY PROCEEDINGS/ VIGILANCE/CRIMINAL /HRPC CASES
                                        IF ANY INITIATED (AS ON DATE)   </label>
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
                                    <form:textarea path="discDetails" id="discDetails" class="form-control" rows="4" cols="100" maxlength="450"/>
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
                                    <label for="dateofServingifAny"> If charge has been served on the nominee in disciplinary proceeding/Criminal case? </label>
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
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','SERV_CHRG');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a> 
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
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','CONTEMPLET');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNameContempletedDocument}">
                                        <input type="file" name="contempletedDocument"  id="contempletedDocument"/> <span style="color:red">(Only PDF)</span>
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
                                    <label for="recommendStatus"> Nomination Status from Dist/Estt</label>
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

