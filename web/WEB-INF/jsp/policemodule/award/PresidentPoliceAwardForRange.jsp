<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">    
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                togglerangeStatus();
            });

            function togglerangeStatus() {
                if ($("#recommendStatusofRange").val() == 'NOT RECOMMENDED') {
                    $("#hidereasonFornotRecommend").show();
                } else {
                    $("#hidereasonFornotRecommend").hide();
                }
            }

            function validateForm() {
                if ($("#recommendStatusofRange").val() == '') {
                    alert('Please select Recommendation Status for Range.');
                    return false;
                } else if ($("#recommendStatusofRange").val() == 'NOT RECOMMENDED') {
                    if ($("#reasonFornotRecommend").val() == '') {
                        alert('Please enter reason for not recommended.');
                        document.getElementById('reasonFornotRecommend').focus();
                        return false;
                    }
                }
                if (confirm("Are you sure to submit.")) {
                    return true;
                } else {
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <% int i = 1;%>
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>
            <form:form action="recommendAwardByRangeForm.htm" commandName="AwardMedalListForm">
                <form:input path="offName" id="offName" class="form-control" readonly="true" />
                <form:hidden path="offCode"/>
                <form:hidden path="awardMedalTypeId"/>
                <form:hidden path="awardYear"/>
                <form:hidden path="rewardMedalId"/>
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> RECOMMENDATION FOR THE AWARD OF PRESIDENT'S POLICE MEDAL FOR DISTINGUISHED SERVICE/POLICE MEDAL FOR MERITORIOUS SERVICE ON THE OCCASION OF Independence day - 2024</h3>
                        <div style="text-align: center;font-weight: bold; font-size: 20px;">
                            <c:out value="${AwardMedalListForm.fullname}"/>
                        </div>
                        <h3 style="text-align:center"><b>
                                <label for="fathersname">Employee Id</label> 
                                ${AwardMedalListForm.empId}</b>
                        </h3>
                        <div class="panel-heading">
                            <a href="awardormedalListForRangeOffice.htm?awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&awardYear=${AwardMedalListForm.awardYear}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                                <c:if test="${empty AwardMedalListForm.rangeSubmittedOn}">
                                <input type="submit" value="Save Form" name="btn" class="btn btn-success" onclick="return validateForm()"/>
                                <%--<input type="submit" value="Delete" name="btn" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>--%>
                            </c:if>
                        </div>

                        <div class ="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fullname">(a) Name<span style="color: red">*</span><br />(As per Service Record)<br />(In capital letter)</label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="firstName" id="firstName" class="form-control" maxlength="100" readonly="true"/>
                                    <form:hidden path="empId"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="middleName" id="middleName" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="lastName" id="lastName" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <img id="loginUserPhoto" style="border:1px solid #a3a183;padding:3px;" onerror="callNoImage()"  alt="ProfileImage" src='displayprofilephoto.htm?empid=${AwardMedalListForm.empId}' width="200" height="200" />
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label>(b) Name in Hindi</label>
                                </div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fathername"> Father's Name </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="fatherfirstname" id="fatherfirstname" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="fathermiddlename" id="fathermiddlename" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="fatherlastname" id="fatherlastname" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dob"> Date of Birth/Sex </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dob" id="dob" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="gender" id="gender"  size="1" class="form-control">
                                        <form:option value="M">Male</form:option>
                                        <form:option value="F">Female</form:option>
                                        <form:option value="T">Transgender</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dob"> Age As on 15.08.2024 </label>
                                </div>
                                <div class="col-lg-3">
                                    ${AwardMedalListForm.ageInYear} Year
                                </div>
                                <div class="col-lg-3">
                                    ${AwardMedalListForm.ageInMonth} Month
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltCategory"> Whether belongs to (SC/ST/OBC/General)<br />(Mandatory) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="sltCategory" id="category" class="form-control" readonly="true" />
                                </div>

                            </div>


                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltCategory"> Country<br />(Mandatory) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="country" id="country" class="form-control" readonly="true"/>
                                </div>

                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">

                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltCategory"> Nationality<br />(Mandatory) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="nationality" id="nationality" class="form-control" readonly="true"/>

                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltCategory"> Religion<br />(Mandatory) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="religious" id="religious" class="form-control" readonly="true"/>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltCategory"> State<br />(Mandatory) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="state" id="state" class="form-control" readonly="true"/>
                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointYear"> Initial appointment </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointYear"> Date of<br />Joining </label>
                                </div>
                                <div class="col-lg-1">
                                    <label for="initialAppointRank"> Rank </label>
                                </div>
                                <div class="col-lg-1">
                                    <label for="initialAppointCadre"> Service </label>
                                </div>
                                <div class="col-lg-1">
                                    <label for="initialAppointCadre"> Cadre </label>
                                </div>
                                <div class="col-lg-1">
                                    <label for="initialAppointCadre"> Category </label>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointYear">  </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="initialAppointDate" id="initialAppointDate" class="form-control" readonly="true"/>
                                    <%--<form:input path="initialAppointDate" id="initialAppointDate" class="form-control txtDate" style="width:150px;" readonly="true"/>--%>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="initialAppointRank" id="initialAppointRank" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="initialAppointService" id="initialAppointService" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="initialAppointCadre" id="initialAppointCadre" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:select path="initialAppointCategory" id="initialAppointCategory" class="form-control" readonly="true">
                                        <form:option value="">Select</form:option>
                                        <form:option value="GROUP-A">Group-A</form:option>
                                        <form:option value="GROUP-B">Group-B</form:option>
                                        <form:option value="GROUP-C">Group-C</form:option>
                                        <form:option value="GROUP-D">Group-D</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="serviceBookCopy">1st Page of Service Book be attached</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameSB}">
                                        <a href="downloadServiceBookCopyForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameSB}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                       
                                </div>

                            </div>

                            <div class="row">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointYear"> Date of Posting in different ranks and length of service. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointYear"> Rank </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointRank"> Date </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointCadre"> Length of Service </label>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-1">
                                    Year
                                </div>
                                <div class="col-lg-1">
                                    Month
                                </div>
                                <div class="col-lg-1">
                                    Days
                                </div>
                            </div>

                            <div id="differentRanks">
                                <c:if test="${not empty AwardMedalListForm.differentRankList}">
                                    <c:forEach items="${AwardMedalListForm.differentRankList}" var="dlist">
                                        <div class="row" style="margin-bottom: 10px;">
                                            <div class="col-lg-1"></div>
                                            <div class="col-lg-3"></div>
                                            <div class="col-lg-2">
                                                <input type="text" name="differentRank" class="form-control" value="${dlist.rankname}"/>
                                            </div>
                                            <div class="col-lg-2">
                                                <input type="text" name="differentDate" class="form-control" value="${dlist.rankdate}" readonly="true"/>
                                            </div>
                                            <div class="col-lg-1">
                                                <input type="text" name="totaldifferentrankyears" class="form-control" value="${dlist.rankyear}" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                            </div>
                                            <div class="col-lg-1">
                                                <input type="text" name="totaldifferentrankmonths" class="form-control" value="${dlist.rankmonth}" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                            </div>
                                            <div class="col-lg-1">
                                                <input type="text" name="totaldifferentrankdays" class="form-control" value="${dlist.rankdays}" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                            </div>
                                            <div class="col-lg-1"></div>
                                        </div>
                                    </c:forEach>
                                </c:if>
                                <div class="row" style="margin-bottom: 10px;">
                                    <div class="col-lg-1"></div>
                                    <div class="col-lg-3"></div>
                                    <div class="col-lg-2">
                                        <form:input path="differentRank" id="differentRank0" class="form-control" readonly="true"/>
                                    </div>
                                    <div class="col-lg-2">
                                        <form:input path="differentDate" id="differentDate0" class="form-control txtDate" readonly="true"/>
                                    </div>
                                    <div class="col-lg-1">
                                        <form:input path="totaldifferentrankyears" id="totaldifferentrankyears0" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                    </div>
                                    <div class="col-lg-1">
                                        <form:input path="totaldifferentrankmonths" id="totaldifferentrankmonths0" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                    </div>
                                    <div class="col-lg-1">
                                        <form:input path="totaldifferentrankdays" id="totaldifferentrankdays0" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                    </div>
                                    <div class="col-lg-1">
                                        <button type="button" onclick="addDifferentRanks();" class="btn btn-danger">Add</button>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointYear"> Total Police Service<br />(As On 15-08-2024) </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointYear"> Years </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointRank"> Months </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointCadre"> Days </label>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-2">

                                    <form:input path="totalpoliceyears" id="totalpoliceyears" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2">

                                    <form:input path="totalpolicemonths" id="totalpolicemonths" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2">

                                    <form:input path="totalpolicedays" id="totalpolicedays" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="presentPosting">(a) Present Posting, With complete postal address with PIN Code</label>
                                </div>
                                <div class="col-lg-2">
                                    Designation
                                </div>
                                <div class="col-lg-2">
                                    Place
                                </div>
                                <div class="col-lg-1">
                                    PIN Code
                                </div>
                                <div class="col-lg-2">
                                    Date
                                </div>
                                <div class="col-lg-1"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-2">
                                    <form:input path="presentPosting" id="presentPosting" class="form-control" maxlength="200" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="presentPostingPlace" id="presentPostingPlace" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="presentPostingPIN" id="presentPostingPIN" class="form-control" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="presentPostingDate" id="presentPostingDate" class="form-control txtDate" readonly="true"/>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>

                            <%--<div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-3">
                                    <label>(a) Above details in Hindi(Mandatory)</label>
                                </div>
                                <div class="col-lg-2">
                                    <input type="text" name="presentPostingHindi" class="form-control" maxlength="200"/>
                                </div>
                                <div class="col-lg-2">
                                    <input type="text" name="presentPostingPlaceHindi" class="form-control" maxlength="200"/>
                                </div>
                                <div class="col-lg-1">
                                    <input type="text" name="presentPostingPINHindi" class="form-control" maxlength="200"/>
                                </div>
                                <div class="col-lg-2">
                                    <input type="text" name="presentPostingDateHindi" class="form-control" maxlength="200"/>
                                </div>
                                <div class="col-lg-1"></div>
                            </div> --%>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="sltDeputation"> Whether on Deputation </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="sltDeputation" id="sltDeputation" class="form-control" onclick="toggleDeputationDiv()">
                                        <form:option value="">Select</form:option>
                                        <form:option value="No">NO</form:option>
                                        <form:option value="Yes">YES</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-2">

                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;" id="deputationDiv">
                                <div class="col-lg-1"><label for="sltDeputation"> Detail </label></div>
                                <div class="col-lg-2">
                                    <form:textarea path="deputationDetail" id="deputationDetail" rows="4" cols="120" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="deputationDate">Date of joining on deputation </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="deputationDate" id="deputationDate" class="form-control txtDate" maxlength="4" readonly="true"/>
                                </div>
                                <div class="col-lg-7"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Rewards </label>
                                </div>
                                <div class="col-lg-3">
                                    <label> No. </label>
                                </div>
                                <div class="col-lg-3">
                                    <label> Total amount in Rs. </label>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="cashAwardsNo"> A) Cash Awards </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="cashAwardsNo" id="cashAwardsNo" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="cashAwardsAmt" id="cashAwardsAmt" class="form-control" maxlength="10" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label> B) Others </label>
                                </div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="presidentcommendation"> i) Commendation </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="presidentcommendation" id="presidentcommendation" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="presidentcommendationAmt" id="presidentcommendationAmt" class="form-control" maxlength="10" onkeypress="return numbersOnly(event)"/>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="presidentappreciation"> ii) Appreciation </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="presidentappreciation" id="presidentappreciation" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="presidentappreciationAmt" id="presidentappreciationAmt" class="form-control" maxlength="10" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="goodServiceEntries"> iii) Good Service Entries </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="goodServiceEntries" id="goodServiceEntries" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="goodServiceEntriesAmt" id="goodServiceEntriesAmt" class="form-control" maxlength="10" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label for="anyOtherRewards"> iv) Any other rewards </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="anyOtherRewards" id="anyOtherRewards" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="anyOtherRewardsDesc" id="anyOtherRewardsDesc" class="form-control" rows="4" cols="60" maxlength="1500" placeholder="Specify" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="meritoriousYear"> If Police Medal for Meritorious Service awarded<span style="color: red">*</span> </label>
                                </div>
                                <div class="col-lg-1">
                                    ${AwardMedalListForm.approvMeritoriousServiceAwarded}
                                </div>
                                <div class="row" style="margin-bottom: 7px;"> 
                                    <div class="col-lg-2">                                    
                                        <input type="radio" id="approvMeritoriousService" name="approvMeritoriousServiceAwarded" value="yes" onclick="radioClickedForMeritoriousServiceAwarded()"><b> (a) Yes </b>
                                    </div>

                                    <div class="col-lg-2">
                                        <input type="radio" id="NotapprovMeritoriousServiceAwarded" name="approvMeritoriousServiceAwarded" value="No" onclick="radioClickedForMeritoriousServiceAwarded()"> <b>No</b>
                                    </div> 
                                </div>
                                <div id="meritoriousServiceAwardedDiv">
                                    <div class="col-lg-3">
                                        Year
                                    </div>
                                    <div class="col-lg-3">
                                        <form:input path="meritoriousYear" id="meritoriousYear" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)"/>
                                    </div>
                                    <div class="col-lg-3">Occasion(RD/ID)</div>
                                    <div class="col-lg-3">
                                        <form:select path="sltOccasion" id="sltOccasion" class="form-control">
                                            <form:option value="">Select</form:option>
                                            <form:option value="RD">RD</form:option>
                                            <form:option value="ID">ID</form:option>
                                        </form:select>
                                    </div>
                                </div>
                            </div>




                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Punishment(s)<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    Details of Penalty<br />(Major/Minor)
                                </div>
                                <div class="col-lg-2">
                                    Year(s).
                                </div>
                                <div class="col-lg-2">
                                    Order No.
                                </div>
                                <div class="col-lg-2">
                                    Order Date
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    (In case of minor penalty from 2010 to 2023)
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="penaltydetails" id="penaltydetails" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="penaltyyear" id="penaltyyear" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="penaltyOrderNo" id="penaltyOrderNo" class="form-control" maxlength="4" style="width:100px;" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="penaltyOrderDate" id="penaltyOrderDate" class="form-control txtDate" style="width:200px;" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    (In case of minor penalty 2024 to till date)
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="minorpenaltydetails" id="minorpenaltydetails" class="form-control"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="minorpenaltyyear" id="minorpenaltyyear" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="minorpenaltyOrderNo" id="minorpenaltyOrderNo" class="form-control" maxlength="4" style="width:100px;" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="minorpenaltyOrderDate" id="minorpenaltyOrderDate" class="form-control txtDate" style="width:200px;" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">Punishment Upload(if any)</div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamepunishmentDoc}">
                                        <a href="downloadMedalPunishmentDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamepunishmentDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                </div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="medicalCategory"> Medical Category(i.e SHAPE-1)<br />(Mandatory) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="medicalCategory" id="medicalCategory" class="form-control"/>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameMedicalCategoryDoc}">
                                        <a href="downloadMedicalCategoryDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameMedicalCategoryDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        </c:if>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="enquirypending"> Details of any enquiry pending against the officer/men.<span style="color: red">*</span> </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="ifenquirypending" id="ifenquirypending" class="form-control" onclick="toggleinquiryDiv()">
                                        <form:option value="">Select</form:option>
                                        <form:option value="No">No</form:option>
                                        <form:option value="Yes">Yes</form:option>
                                    </form:select>
                                </div>

                                <div class="col-lg-3"></div>
                            </div>


                            <div class="row" style="margin-bottom: 7px;" id="inquiryDiv">
                                <div class="col-lg-3">
                                    <form:textarea path="enquirypending" id="enquirypending" class="form-control" rows="4" cols="60" maxlength="350"/>
                                </div>
                                <div class="col-lg-2">Enquiry Upload(if any)</div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameenquiryDoc}">
                                        <a href="downloadEnquiryDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameenquiryDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        </c:if>
                                </div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Details of disciplinary proceedings pending / contemplated against the recommendee, if any </label>
                                </div>
                                <div class="col-lg-3">
                                    Year
                                </div>
                                <div class="col-lg-3">
                                    Nature of Allegation.
                                </div>
                                <div class="col-lg-3">
                                    Present Status.
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-3">
                                    <form:input path="dpcyear" id="dpcyear" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dpcnatureallegation" id="dpcnatureallegation" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dpcpresentstatus" id="dpcpresentstatus" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">Disciplinary Proceedings Upload(if any)</div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamedpcDoc}">
                                        <a href="downloadDISCPCDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamedpcDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        </c:if>
                                </div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Details of the court cases pending against the recommendee, if any </label>
                                </div>
                                <div class="col-lg-3">
                                    Year
                                </div>
                                <div class="col-lg-3">
                                    Details of charge.
                                </div>
                                <div class="col-lg-3">
                                    Present Status.
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-3">
                                    <form:input path="courtCasePendingYear" id="courtCasePendingYear" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="courtCasePendingDetails" id="courtCasePendingDetails" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="courtCasePendingStatus" id="courtCasePendingDetails" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">Court Case Upload(if any)</div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamecourtCaseDoc}">
                                        <a href="downloadCourtCaseDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamecourtCaseDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        </c:if>
                                </div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> If the recommendee is of Group-A or Group-B officer,his GPF A/C No.(Required to collect ACR status from HRMS). </label>
                                </div>
                                <div class="col-lg-3">
                                    GPF A/C No-<c:out value="${AwardMedalListForm.gpfNo}"/>
                                </div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-3"></div>
                            </div>

                            <%--<div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> A.C.R. Grading for last 10 years* </label>
                                </div>
                                <div class="col-lg-3">
                                    Year
                                </div>
                                <div class="col-lg-3">
                                    Grading
                                </div>
                                <div class="col-lg-3"></div>
                            </div>--%>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-8">
                                    <table border="0" width="100%">
                                        <tr>
                                            <th width="60%">
                                                A.C.R. Grading for last 10 years*
                                            </th>
                                            <th width="10%">Year</th>
                                            <th width="15%">Grading</th>
                                            <th width="15%"></th>
                                        </tr>
                                        <tr>
                                            <td rowspan="13" width="60%">
                                                [2013-2014 to 2022 - 2023]<br /> In case of maximum 3 NIC,ACR grading for last 13 years*.<br />
                                                [2010-2011 to 2022-2023]<br />Similarly, in case ACR is given as per Calendar year,<br />
                                                [2010-2022]<br />O.S. –Outstanding, V.G. – Very Good, G.- Good, A.V.-Average,<br />
                                                NIC – Not initiating Certificate,Adv.–Adverse, MS-Missing ,N.A.–Not Applicable .<br />
                                                (ACRs are not written in case of Constable and below in some organizations)<br />
                                                *ACR gradings should be distinctly indicated as<br />Outstanding, Very Good, Good, Average etc. wherever<br />
                                                different grading are applicable in different cadre, the<br />
                                                same should be converted by the recommending<br />
                                                organization into the equivalent acceptable ACR<br />
                                                grading. (Viz. OS, VG, G, AV, NIC, ADV.,MS,NA)before<br />
                                                forwarding the recommendation.
                                            </td>
                                            <td>2010-11</td>
                                            <td>
                                                <form:input path="acrGrading4" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>

                                            <td width="15%">

                                            </td>
                                        </tr>



                                        <tr>
                                            <td>2011-12</td>
                                            <td>
                                                <form:input path="acrGrading5" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2012-13</td>
                                            <td>
                                                <form:input path="acrGrading6" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2013-14</td>
                                            <td>
                                                <form:input path="acrGrading7" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2014-15</td>
                                            <td>
                                                <form:input path="acrGrading8" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2015-16</td>
                                            <td>
                                                <form:input path="acrGrading9" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2016-17</td>
                                            <td>
                                                <form:input path="acrGrading10" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2017-18</td>
                                            <td>
                                                <form:input path="acrGrading11" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2018-19</td>
                                            <td>
                                                <form:input path="acrGrading12" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2019-20</td>
                                            <td>
                                                <form:input path="acrGrading13" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td width="10%">2020-21</td>
                                            <td width="15%">
                                                <form:input path="acrGrading1" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td> 
                                        </tr>
                                        <tr>
                                            <td>2021-22</td>
                                            <td>
                                                <form:input path="acrGrading2" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>2022-23</td>
                                            <td>
                                                <form:input path="acrGrading3" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label>do you want to attach scan copy CCR of ASI/SI/Equivalent <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="acrCopyifAny" id="acrCopyifAny" class="form-control" onclick="toggleACRDiv()">
                                        <form:option value=""> Select </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="acrattachmentDiv">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="dateofServing"> Attachment (2009-2022)</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameAcrGrading1Doc}">
                                        <a href="downloadACRGradingDocument.htm?doctype=ACR1&attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameAcrGrading1Doc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        </c:if>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="acrreferenceDiv">
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="dateofServing"> Reference of sending</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="acrGradingDetail" class="form-control" rows="4" cols="60" maxlength="500" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> ACR Grading for last ten years (in number) </label>
                                </div>
                                <div class="col-lg-1">OS</div>
                                <div class="col-lg-1">VG</div>
                                <div class="col-lg-1">GOOD</div>
                                <div class="col-lg-1">AVG</div>
                                <div class="col-lg-1">NIC</div>
                                <div class="col-lg-1">ADV</div>
                                <div class="col-lg-1">MS</div>
                                <div class="col-lg-1">NA</div>
                                <div class="col-lg-1"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-1">
                                    <form:input path="acrGradingOS" class="form-control" style="width:100px;" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="acrGradingVS" class="form-control" style="width:100px;" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="acrGradingGood" class="form-control" style="width:100px;" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="acrGradingAvg" class="form-control" style="width:100px;" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="acrGradingNic" class="form-control" style="width:100px;" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="acrGradingAdv" class="form-control" style="width:100px;" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="acrGradingMs" class="form-control" style="width:100px;" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="acrGradingNa" class="form-control" style="width:100px;" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="email"> Email address(Mandatory) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="email" id="email" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="mobile"> Mobile No(Mandatory) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="mobile" id="mobile" class="form-control" maxlength="10" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="briefdescription"> Brief description of work justifying award of Medal (No posting details) In order of importance (not exceeding 500 words each) </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="briefdescription" id="briefdescription" class="form-control" maxlength="3500" rows="6" cols="80" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-3"></div>
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
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="recommendStatusofDist"> Nomination Status From Districts/Establishment </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:select path="recommendStatusofDist" id="recommendStatusofDist" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="NOMINATED">NOMINATED</form:option>
                                        <form:option value="NOT NOMINATED">NOT NOMINATED</form:option>
                                    </form:select>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> <%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="integrityCertificateDoc"> INTEGRITY CERTIFICATE  </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameintegrityCertificateDoc}">
                                        <a href="downloadIntegrityDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamecertificateDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        </c:if>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> <%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="medicalCertificateDoc"> PHYSICAL/MEDICAL CERTIFICATE  </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamemedicalcertificateDoc}">
                                        <a href="downloadMedicalDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamemedicalcertificateDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        </c:if>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> <%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="certificateDoc"> Certificate </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamecertificateDoc}">
                                        <a href="downloadCertificateDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamecertificateDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        </c:if>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="recommendStatusofRange"> Recommendation Status for Range </label>
                                </div>
                                <div class="col-lg-6">
                                    <form:select path="recommendStatusofRange" id="recommendStatusofRange" class="form-control" onclick="togglerangeStatus()">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="RECOMMENDED">RECOMMENDED</form:option>
                                        <form:option value="NOT RECOMMENDED">NOT RECOMMENDED</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="hidereasonFornotRecommend">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="reasonFornotRecommend">Reason for Not Recommend</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="reasonFornotRecommend" id="reasonFornotRecommend" class="form-control" maxlength="90"/>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="furtherInfoByRange"> Any other Information </label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;" >
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="furtherInfoByRange" id="furtherInfoByRange" class="form-control" rows="4" cols="100"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label> </label>
                                </div>
                                <div class="col-lg-9">
                                    <label style="color:red"> (Maximum 100 words allowed) </label>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer">
                            <a href="awardormedalListForRangeOffice.htm?awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&awardYear=${AwardMedalListForm.awardYear}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                                <c:if test="${empty AwardMedalListForm.rangeSubmittedOn}">
                                <input type="submit" value="Save Form" name="btn" class="btn btn-success" onclick="return validateForm()"/>
                                <%--<input type="submit" value="Delete" name="btn" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>--%>
                            </c:if>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>
