<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
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
            $(document).ready(function () {
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

                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                toggleDiv();

                toggleRedploymentFields();

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
                if ($("#category").val() == '') {
                    alert("Please select Category");
                    document.getElementById('category').focus();
                    return false;
                }
                if ($("#txtMobile").val() == '') {
                    alert("Please enter Mobile");
                    document.getElementById('txtMobile').focus();
                    return false;
                }
                if ($("#doeGov").val() == '') {
                    alert("Please enter Date of Joining as Constable");
                    document.getElementById('doeGov').focus();
                    return false;
                }
                if ($("#dojAppntdDist").val() == '') {
                    alert("Please select District in which appointed");
                    document.getElementById('dojAppntdDist').focus();
                    return false;
                }
                if ($("#currentRank").val() == '') {
                    alert("Please select Present Rank");
                    document.getElementById('currentRank').focus();
                    return false;
                }
                if ($("#postingPlace").val() == '') {
                    alert("Please enter Posting Place of Present Rank");
                    document.getElementById('postingPlace').focus();
                    return false;
                }


                var redeploymentJoining = $('#sltRedeploymentJoining').val();
                if (redeploymentJoining == "No") {
                    if ($("#txtTrainingCompletedDate").val() == '') {
                        alert("Please enter Date of Completion of Constable Course of Training");
                        document.getElementById('txtTrainingCompletedDate').focus();
                        return false;
                    }
                } else if (redeploymentJoining == "Yes") {
                    if ($("#txtRedeploymentJoiningDate").val() == '') {
                        alert("Indicate the date of joining as constable in the District/Establishment");
                        document.getElementById('txtRedeploymentJoiningDate').focus();
                        return false;
                    }
                }
                if ($("#nominationFormId").val() != "") {
                    if ($("#photoAttchId").val() == "") {
                        alert("Please Upload Photo");
                        return false;
                    } else {
                        var fi = document.getElementById("nominatedEmployeePhoto");
                        var fsize = fi.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 3072) {
                            alert("File too Big, please select a file less than 3mb");
                            return false;
                        }


                        var filePath = fi.value;
                        // Allowing file type 
                        var allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
                        if (!allowedExtensions.exec(filePath)) {
                            alert('Invalid file type');
                            filePath.value = '';
                            return false;
                        }
                    }
                } else if ($("#nominationFormId").val() == "") {
                    if ($("#nominatedEmployeePhoto").val() == "") {
                        alert("Please Upload Photo");
                        return false;
                    } else {
                        var fi = document.getElementById("nominatedEmployeePhoto");
                        var fsize = fi.files.item(0).size;
                        var file = Math.round((fsize / 1024));
                        if (file >= 3072) {
                            alert("File too Big, please select a file less than 3mb");
                            return false;
                        }


                        var filePath = fi.value;
                        // Allowing file type 
                        var allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
                        if (!allowedExtensions.exec(filePath)) {
                            alert('Invalid file type');
                            filePath.value = '';
                            return false;
                        }

                    }
                }
                return true;
            }

            function toggleDiv() {
                if ($("#dpcifany").val() == 'Y') {
                    $("#hideDiscDocumentRow").show();
                } else {
                    $("#hideDiscDocumentRow").hide();
                }
            }

            function monthint(monthname)
            {
                var tmonthint = "";
                switch (monthname)
                {
                    case "JAN":
                        tmonthint = "01";
                        return tmonthint;
                        break;
                    case "FEB":
                        tmonthint = "02";
                        return tmonthint;
                        break;
                    case "MAR":
                        tmonthint = "03";
                        return tmonthint;
                        break;
                    case "APR":
                        tmonthint = "04";
                        return tmonthint;
                        break;
                    case "MAY":
                        tmonthint = "05";
                        return tmonthint;
                        break;
                    case "JUN":
                        tmonthint = "06";
                        return tmonthint;
                        break;
                    case "JUL":
                        tmonthint = "07";
                        return tmonthint;
                        break;
                    case "AUG":
                        tmonthint = "08";
                        return tmonthint;
                        break;
                    case "SEP":
                        tmonthint = "09";
                        return tmonthint;
                        break;
                    case "OCT":
                        tmonthint = "10";
                        return tmonthint;
                        break;
                    case "NOV":
                        tmonthint = "11";
                        return tmonthint;
                        break;
                    case "DEC":
                        tmonthint = "12";
                        return tmonthint;
                        break;
                    default:
                        tmonthint = "0";
                }


            }

            function getCourseCompletionDifference() {
                var date1 = new Date("1-Jan-2022");
                var courseCompDate = $("#txtTrainingCompletedDate").val().split("-");
                var completeDate = new Date(courseCompDate[2], monthint(courseCompDate[1].toUpperCase()), courseCompDate[0]).getTime();

                var curDate = new Date("2022", "1", "1").getTime();
                var enteredDate = new Date(monthint(courseCompDate[1].toUpperCase()) + "/" + courseCompDate[0] + "/" + courseCompDate[2]);
                var refDate = new Date("01" + "/" + "01" + "/" + "2022");

                if (completeDate > curDate) {
                    alert('Date of Completion of Constable Course of Training should not be greater than Length of Service as on 1/1/2022 ');
                    $("#txtTrainingCompletedDate").val('');
                } else {
                    getCourseCompletionDifferenceData(refDate, enteredDate, 'show_error_to_date');
                }
            }

            function getCourseCompletionDifferenceData(date1, date2, error_element) {
                var milli_diff = date1.getTime() - date2.getTime();//time difference in milliseonds
                if (milli_diff < 0) {
                    $('#show_age_to_date').hide();
                    $('#' + error_element).html('Please recheck date range').show().delay(5000).fadeOut();
                    return false;
                }
                var diff_days_total = (milli_diff / (1000 * 3600 * 24));
                var diff_years_float = (diff_days_total / (365));
                var diff_years = parseInt(diff_years_float);
                var diff_months_float = ((diff_years_float - diff_years) * 12);
                var diff_months = parseInt(diff_months_float);
                var diff_days_float = ((diff_months_float - diff_months) * 30);
                var diff_days = parseInt(diff_days_float);
                $('#yearinServiceLength').val(diff_years);
                $('#monthrinServiceLength').val(diff_months);
                $('#daysinServiceLength').val(diff_days);
            }

            function getRedeploymentDifference() {
                var courseCompDate = $("#txtRedeploymentJoiningDate").val().split("-");
                var completeDate = new Date(courseCompDate[2], monthint(courseCompDate[1].toUpperCase()), courseCompDate[0]).getTime();

                var curDate = new Date("2022", "1", "1").getTime();
                var enteredDate = new Date(monthint(courseCompDate[1].toUpperCase()) + "/" + courseCompDate[0] + "/" + courseCompDate[2]);
                var refDate = new Date("01" + "/" + "01" + "/" + "2022");

                if (completeDate > curDate) {
                    alert('Date of Completion of Constable Course of Training should not be greater than Length of Service as on 1/1/2022 ');
                    $("#txtTrainingCompletedDate").val('');
                } else {
                    getRedeploymentDifferenceData(refDate, enteredDate, 'show_error_to_date');
                }
            }

            function getRedeploymentDifferenceData(date1, date2, error_element) {
                var milli_diff = date1.getTime() - date2.getTime();//time difference in milliseonds
                if (milli_diff < 0) {
                    $('#show_age_to_date').hide();
                    $('#' + error_element).html('Please recheck date range').show().delay(5000).fadeOut();
                    return false;
                }
                var diff_days_total = (milli_diff / (1000 * 3600 * 24));
                var diff_years_float = (diff_days_total / (365));
                var diff_years = parseInt(diff_years_float);
                var diff_months_float = ((diff_years_float - diff_years) * 12);
                var diff_months = parseInt(diff_months_float);
                var diff_days_float = ((diff_months_float - diff_months) * 30);
                var diff_days = parseInt(diff_days_float);

                $('#yearinRedeploymentServiceLength').val(diff_years);
                $('#monthRedeploymentServiceLength').val(diff_months);
                $('#daysRedeploymentServiceLength').val(diff_days);
            }

            /*function monthDiff(d1, d2) {
             var months;
             months = (d2.getFullYear() - d1.getFullYear()) * 12;
             months -= d1.getMonth();
             months += d2.getMonth();
             return months <= 0 ? 0 : months;
             }*/
            function diff_months(dt2, dt1) {
                var diff = (dt2.getTime() - dt1.getTime()) / 1000;
                diff /= (60 * 60 * 24 * 7 * 4);
                return Math.abs(Math.round(diff));
            }

            function diff_years(dt2, dt1) {
                var diff = (dt2.getTime() - dt1.getTime()) / 1000;
                diff /= (60 * 60 * 24);
                return Math.abs(Math.round(diff / 365.25));
            }

            function dateDiffYear(dateold, datenew) {
                var ynew = datenew.getFullYear();
                var mnew = datenew.getMonth();
                var dnew = datenew.getDate();
                var yold = dateold.getFullYear();
                var mold = dateold.getMonth();
                var dold = dateold.getDate();
                var diff = ynew - yold;
                if (mold > mnew)
                    diff--;
                else
                {
                    if (mold == mnew)
                    {
                        if (dold > dnew)
                            diff--;
                    }
                }
                return diff;
            }

            function toggleRedploymentFields() {
                var redeploymentJoining = $('#sltRedeploymentJoining').val();
                if (redeploymentJoining == "No") {
                    $('#redeploymentjoiningdate').hide();
                    $('#redeploymentjoiningdays').hide();
                } else if (redeploymentJoining == "Yes") {
                    $('#redeploymentjoiningdate').show();
                    $('#redeploymentjoiningdays').show();
                }
            }

            function callNoImage() {
                var userPhoto = document.getElementById('userPhoto');
                userPhoto.src = "images/NoEmployee.png";
            }

            function toggleServicingDiv() {
                var dateofServingifAny = $('#dateofServingifAny').val();

                if (dateofServingifAny == "") {
                    $('#dateofServingDiv').hide();
                } else if (dateofServingifAny == "Y") {
                    $('#dateofServingDiv').show();
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="saveASINominationApplicationForm.htm" commandName="asiNominationForm" enctype="multipart/form-data">
                    <form:hidden path="nominationMasterId"/>
                    <form:hidden path="nominationDetailId"/>
                    <form:hidden path="nominationFormId" id="nominationFormId"/>
                    <form:hidden path="officeCode"/>
                    <form:hidden path="officeName"/>
                    <form:hidden path="photoAttchId" id="photoAttchId"/>
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> Application cum willingness form for appearing in Written</h3>
                        <h3 style="text-align:center"> Examination for promotion to the rank of ASI </h3>
                        <div class="panel-heading">
                            <a href="editASINomination.htm?nominationMasterId=${asiNominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                            <input type="submit" value="Save Form" class="btn btn-success" onclick="return validateForm()"/>
                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>1.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fullname" style="margin-bottom: 35px;"> a) Name (in full) (In Capitals)</label><br />
                                    <label>Brass No.</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="fullname" id="fullname" class="form-control" readonly="true"/>
                                    <form:hidden path="empId"/><br />
                                    <form:input path="brassno" id="brassno" class="form-control" maxlength="50"/>
                                </div>
                                <div class="col-lg-3">
                                    <img src="displayprofilephoto.htm?empid=${asiNominationForm.empId}" id="userPhoto" onerror="callNoImage()" height="150px" width="140px" border="2"/>
                                </div>
                                <div class="col-lg-3">
                                    <img src="displayNominatedEmployeeProfilePhoto.htm?nominationformId=${asiNominationForm.nominationFormId}" id="userPhoto" onerror="callNoImage()" height="150px" width="140px" border="2"/><br />
                                    Please upload recent photo<br /><input type="file" name="nominatedEmployeePhoto" id="nominatedEmployeePhoto" accept=".png, .jpg, .jpeg, .gif"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="category">b) Category</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="category" id="category" class="form-control" >
                                        <c:forEach items="${categoryList}" var="category">
                                            <form:option value="${category.categoryid}" label="${category.categoryName}"/>
                                        </c:forEach>
                                    </form:select>
                                </div>
                                <div class="col-lg-7"></div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="fathersname">c) Father's Name (in full) (In Capitals)</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="fathersname" id="fathersname" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-6"></div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="dob"> d) Date of Birth (In figure)(In words)</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="dob" id="dob" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-7"></div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="qualification"> e) Educational Qualifications:</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="qualification" id="qualification" class="form-control"/>
                                </div>
                                <div class="col-lg-6"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="txtMobile"> f) Mobile:</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="txtMobile" id="txtMobile" class="form-control" maxlength="10" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-6"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="homeDistrict"> g) Home District </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="homeDistrict" id="homeDistrict" class="form-control">
                                        <form:option value="">-Select One-</form:option>
                                        <form:options items="${districtList}" itemLabel="distName" itemValue="distCode"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-6"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="doeGov"> h) Date of Joining as Constable</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="doeGov" id="doeGov" class="form-control txtDate" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <label>District in which appointed</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="dojAppntdDist" id="dojAppntdDist" class="form-control">
                                        <form:option value="">-Select One-</form:option>
                                        <form:options items="${districtList}" itemLabel="distName" itemValue="distCode"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-2"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="currentRank"> i) Present Rank</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="currentRank" id="currentRank" class="form-control">
                                        <form:option value="">-Select One-</form:option>
                                        <form:option value="140070">CONSTABLES</form:option>
                                        <form:option value="140181">LANCE NAIKS</form:option>
                                        <form:option value="140124">HAVILDARS</form:option>
                                        <form:option value="140051">CI HAVILDARS</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-2">
                                    <label for="postingPlace">Place of Posting</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="postingPlace" id="postingPlace" class="form-control"/>
                                </div>
                                <div class="col-lg-1">
                                    <label for="postingDate">Date</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="postingDate" id="postingDate" class="form-control txtDate" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="dojNewDist"> j) Date of joining as Constable in new District as per change of cadre in terms of PCO-342/2013</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="dojNewDist" id="dojNewDist" class="form-control txtDate" readonly="true"/>  
                                </div>
                                <div class="col-lg-7"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>2.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="txtTrainingCompletedDate"> Date of Completion of Constable Course of Training</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="txtTrainingCompletedDate" id="txtTrainingCompletedDate" class="form-control txtDate" readonly="true" onblur="getCourseCompletionDifference();"/>
                                </div>
                                <div class="col-lg-7"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2">
                                    Years
                                </div>
                                <div class="col-lg-2">
                                    Months
                                </div>
                                <div class="col-lg-2">
                                    Days
                                </div>
                                <div class="col-lg-3"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>3.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="txtTrainingCompleted"> Length of Service as on 1/1/2022 from the date of completion of constable course of training</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="yearinServiceLength" id="yearinServiceLength" class="form-control" placeholder="Year" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="monthinServiceLength" id="monthrinServiceLength" class="form-control" placeholder="Month" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="daysinServiceLength" id="daysinServiceLength" class="form-control" placeholder="Days" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>4.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltRedeploymentJoining">
                                        Whether joined as constable in district/establishment on redeployment from Battalion<br/>
                                        If yes indicate the date of joining as constable in the District/Establishment.
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="sltRedeploymentJoining" id="sltRedeploymentJoining" class="form-control" onchange="toggleRedploymentFields();">
                                        <form:option value="No">No</form:option>
                                        <form:option value="Yes">Yes</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-2">
                                    <span id="redeploymentjoiningdate">
                                        <form:input path="txtRedeploymentJoiningDate" id="txtRedeploymentJoiningDate" class="form-control txtDate" readonly="true" onblur="getRedeploymentDifference();"/>
                                    </span>
                                </div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-3"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="redeploymentjoiningdays">
                                <div class="col-lg-1">
                                    <label>a.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="yearinServiceSpecialBranch">
                                        Length of Service as Constable after redeployment as on 1/1/2022 for those coming from Battalion to district on redeployment.
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="yearinRedeploymentServiceLength" id="yearinRedeploymentServiceLength" class="form-control" placeholder="Year" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="monthRedeploymentServiceLength" id="monthRedeploymentServiceLength" class="form-control" placeholder="Month" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="daysRedeploymentServiceLength" id="daysRedeploymentServiceLength" class="form-control" placeholder="Days" readonly="true"/>
                                </div>
                            </div>

                            <!--<div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>5.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltRedeploymentJoining">
                                        Undertaking of the SC/ST candidate to appear in the written Examination against UR category 
                                        for promotion to the rank of ASI of Police. 
                                        If not availed any benefits of Reservation at the time of their initial appointment as Constable 
                                        in District or Sepoy / Constable in Battalion cadre 
                                        then Select: Yes
                                        <span style="color:red"> Ref. State Police Hdqrs letter no. 35877/Board, dtd: 10/11/2020 (ANNEXURE-B) </span>
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="whetheravailedReservationCategory" id="whetheravailedReservationCategory" class="form-control" onchange="toggleRedploymentFields();">
                                        <form:option value="">-- Select One--</form:option>
                                        <form:option value="Yes">Yes</form:option>
                                    </form:select>
                                </div>
                                
                            </div>-->
                            
                                
                        </div>
                        <div class="panel-footer">
                            <a href="editASINomination.htm?nominationMasterId=${asiNominationForm.nominationMasterId}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                            <input type="submit" value="Save Form" class="btn btn-success" onclick="return validateForm();"/>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </body>
</html>
