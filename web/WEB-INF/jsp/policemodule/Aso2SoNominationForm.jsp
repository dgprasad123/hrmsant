<%-- 
    Document   : Aso2SoNominationForm
    Created on : 1 Nov, 2023, 4:00:33 PM
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
                $('#dateofPropertySubmittedByOfficer').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });


                toggleDiv();


                toggleServicingDiv();

                togglepropertyDiv();

                radioClickedForApprovalOfAuthority();




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
                var radiovalue = $("input[name='isAnyPromotionearlier']:checked").val();
                if (radiovalue == null) {
                    alert('Please enter is Any Promotion earlier');
                    return false;
                }
                if (radiovalue == 'yes') {
                    if ($("#promotionalRankName").val() == '') {
                        alert("please Enter promotional Rank Name");
                        document.getElementById('promotionalRankName').focus();
                        return false;
                    }
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
                    alert('Date Of Joining In Present Rank can not be blank.');
                    document.getElementById('jodInspector').focus();
                    return false;
                }
                var isCompletedRegulartenYearsradio = $("input[name=isCompletedRegulartenYears]:checked").val();
                if (isCompletedRegulartenYearsradio == null) {
                    alert('Is Completed Regular ten Years can not be blank.');
                    return false;
                }



                if ($("#dpcifany").val() == '') {
                    alert('Please enter Vigilance Case/Criminal Case/Hrpc Related Cases Pending (AS ON DATE) ');
                    document.getElementById('dpcifany').focus();
                    return false;
                }


                if ($("#dpcifany").val() == 'Y') {
            <c:if test="${empty nominationForm.originalFileName}">
                    if ($("#discDocument").val() == '') {
                        alert('Please Upload Vigilance Case/Criminal Case/Hrpc Related Cases document. ');
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
                            alert('Please enter details. of Vigilance Case/Criminal Case/Hrpc Related Cases');
                            document.getElementById('discDetails').focus();
                            return false;
                        }

                    }
            </c:if>

                }
                if ($("#dateofServingifAny").val() == '') {
                    alert('Please enter Departmental Proceeding Pending(AS ON DATE) ');
                    document.getElementById('dateofServingifAny').focus();
                    return false;
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
                        if ($("#servingChargeDetail").val() == '') {
                            alert('Please enter details. of  Departmental Proceeding Pending(AS ON DATE)');
                            document.getElementById('servingChargeDetail').focus();
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
                if ($("#isContempletedAsOnDate").val() == '') {
                    alert('Please enter isContempleted As On Date ');
                    document.getElementById('isContempletedAsOnDate').focus();
                    return false;
                }
                if ($("#isContempletedAsOnDate").val() == 'Y') {
                    if ($("#ContempletedDocument").val() == "") {
                        alert('Please enter Contempleted Document ');
                        document.getElementById('ContempletedDocument').focus();
                        return false;
                    }
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
                if ($("#recommendStatusDistrict").val() == '') {
                    alert('Please enter Recommend Status From District');
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



            function toggleServicingDiv() {
                if ($("#dateofServingifAny").val() == 'Y') {
                    $("#hideServingChargeRow").show();
                    $("#hideDiscDETAILRow").show();

                } else {
                    $("#hideServingChargeRow").hide();
                    $("#hideDiscDETAILRow").hide();
                }
            }
            function toggleContempletedDiv() {
                if ($("#isContempletedAsOnDate").val() == 'Y') {
                    $("#contempletedAsOnDate").show();

                } else {
                    $("#contempletedAsOnDate").hide();
                }
            }
            function radioClickedForApprovalOfAuthority() {
                //var radioValue = $("input[name='assessmentTypeReporting']:checked").val();
                var radiovalue = $("input[name='isAnyPromotionearlier']:checked").val();

                if (radiovalue == "yes") {
                    $("#promotionalrankDiv").show();
                } else {
                    $("#promotionalrankDiv").hide();
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
                         <h4 style="text-align:center"><b> NOMINATION ROLL</b></h4>
                        <h3 style="text-align:center">
                            <b> 
                                <div style="margin-bottom: 7px; text-align: center">                                                                       
                                    <label for="fathersname">Nomination Serial No.</label>                                    
                                    <form:input path="gradeSerialNo" id="gradeSerialNo" maxlength="4" onkeypress="return numbersOnly(event)"/>                                    
                                </div> 
                            </b>
                        </h3>
                        <h3 style="text-align:center"><b> PROMOTION OF ASSISTANT SECTION OFFICER OF POLICE  TO THE RANK OF SECTION OFFICER OF POLICE </b></h3>
                        <h3 style="text-align:center"><b> CSB - 2023 </b></h3>
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
                                    <form:input path="currentDesignation" id="currentDesignation" class="form-control"/>

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
                                    <form:input path="qualification" id="qualification" class="form-control"/>
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
                                    <form:input path="initialRank" id="initialRank" class="form-control"/>
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
                                    <form:input path="promotionalRankName" id="promotionalRankName" class="form-control"/>
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
                                    <form:input path="presentRankDistName" id="presentRankDistName" class="form-control" /> 
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
                                                <input type="text" name="postingOrDeputationInOtherDistrict" value="${dlist.districtName}" class="form-control" />
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
                                        <form:input path="postingOrDeputationInOtherDistrict" id="postingOrDeputationInOtherDistrict" class="form-control"/>
                                    </div>
                                    <div class="col-lg-2">
                                        <form:input path="postingOrDeputationInOtherDistrictFromDate" id="postingOrDeputationInOtherDistrictFromDate" class="form-control" readonly="true"/>
                                    </div>
                                    <div class="col-lg-1">
                                        <form:input path="postingOrDeputationInOtherDistrictToDate" id="postingOrDeputationInOtherDistrictToDate" class="form-control txtDate" readonly="true"/>
                                    </div>

                                    <div class="col-lg-1">
                                        <button type="button" onclick="addDifferentDistrictandEstablishment();" class="btn btn-danger">Add</button>
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
                                    <form:input path="punishmentMajorPrior" id="punishmentMajorPrior" class="form-control"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorDuring" id="punishmentMajorDuring" class="form-control"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMajorFrom" id="punishmentMajorFrom" class="form-control"/>
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
                                    <form:input path="punishmentMinorPrior" id="punishmentMinorPrior" class="form-control"/>
                                </div>
                                <div class="col-lg-2" style="text-align: center">
                                    <form:input path="punishmentMinorDuring" id="punishmentMinorDuring" class="form-control"/>
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
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','SERV_CHRG');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>    
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNameServing}">
                                        <input type="file" name="documentServingCopy"  id="documentServingCopy"/> <span style="color:red">(Only PDF)</span>
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
                                    <form:textarea path="servingChargeDetail" id="servingChargeDetail" class="form-control" rows="4" cols="100" maxlength="450"/>
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
                                        <input type="file" name="ContempletedDocument"  id="ContempletedDocument"/> <span style="color:red">(Only PDF)</span>
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
                                <div class="col-lg-2">
                                    <label for="casteCertificate"> Annexure-A(Willingness Application)</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.originalFileNamewillingnessCertificateForSrclerk}">
                                        <a href="downloadWillingnessCertificateForGroupD.htm?attachId=${nominationForm.nominationFormId}" target="_blank">${nominationForm.originalFileNamewillingnessCertificateForSrclerk}<i class="fa fa-file-pdf-o" style="color:red" aria-hidden="true"></i></a>
                                        <a href="javascript:deletePoliceNominationAttachment('${nominationForm.nominationFormId}','WILLINGNESS');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty nominationForm.originalFileNamewillingnessCertificateForSrclerk}">
                                        <input type="file" name="willingnessCertificateDocument" id="willingnessCertificateDocument" class="fileupload"/> <span style="color:red">(Only PDF)</span>
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

