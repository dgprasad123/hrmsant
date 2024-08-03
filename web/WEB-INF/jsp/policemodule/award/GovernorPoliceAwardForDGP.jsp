<%-- 
    Document   : GovernorPoliceAwardForDGP
    Created on : 23 Nov, 2021, 2:08:36 PM
    Author     : Manisha
--%>



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
            <form:form action="recommendAwardByRangeForm.htm" commandName="AwardMedalListForm" enctype="multipart/form-data">
                <form:input path="offName" id="offName" class="form-control" readonly="true" />
                <form:hidden path="offCode"/>
                <form:hidden path="awardMedalTypeId"/>
                <form:hidden path="awardYear"/>
                <form:hidden path="rewardMedalId"/>
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> RECOMMENDATION FOR THE AWARD OF GOVERNOR’S MEDAL TO POLICE PERSONNEL FOR THE YEAR-2022 TO BE PRESENTED ON REPUBLIC DAY,2023.</h3>
                        <div style="text-align: center;font-weight: bold; font-size: 20px;">
                            <c:out value="${AwardMedalListForm.fullname}"/>
                        </div>
                        <div class="panel-heading">
                            <a href="awardormedalList.htm?awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&awardYear=${AwardMedalListForm.awardYear}"><input type="button" class="btn btn-primary" value="Back"/></a> 
                                <c:if test="${empty AwardMedalListForm.submittedOn}">
                                <input type="submit" value="Save Form" name="btn" class="btn btn-success" onclick="return validateForm()"/>
                                <input type="submit" value="Delete" name="btn" class="btn btn-danger" onclick="return confirm('Are you sure to delete?')"/>
                            </c:if>
                        </div>

                        <div class ="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fullname">(a) Name<br />(As per Service Record)<br />(In capital letter)<span style="color: red">*</span></label>
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
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fathername"> Father's Name </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="fatherfirstname" id="fatherfirstname" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="fathermiddlename" id="fathermiddlename" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="fatherlastname" id="fatherlastname" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fullname">(a) Date of Birth:</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dob" id="dob" class="form-control" readonly="true"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label>(b) Sex:</label>
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
                                    <label for="Presentplaceofposting"> Religion:
                                        <span style="color: red">*</span></label>
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
                                    <label for="category">Category</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="category" id="category" class="form-control" readonly="true">
                                        <c:forEach items="${categoryList}" var="category">
                                            <form:option value="${category.categoryid}" label="${category.categoryName}"/>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>



                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="Presentplaceofposting"> Rank / Designation with :<br>
                                        Present place of posting <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="presentPostingRankandDesignation" id="presentPostingRankandDesignation" class="form-control" maxlength="100" readonly="true"/>
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
                                    <label for="initialAppointYear"> Year </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointRank"> Rank </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointCadre"> Service </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointCadre"> Cadre </label>
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
                                    <form:input path="initialAppointDate" id="initialAppointDate" class="form-control txtDate" style="width:150px;" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="initialAppointRank" id="initialAppointRank" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="initialAppointService" id="initialAppointService" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="initialAppointCadre" id="initialAppointCadre" class="form-control" maxlength="100" readonly="true"/>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointYear">Date of Posting and length:<br>
                                        of service in different ranks </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointYear"> Rank </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="initialAppointRank"> Date </label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointCadre"> Length of Service in different ranks.</label>
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

                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-3">
                                    <label for="initialAppointYear"> Total service as on<br />(As on 31.12.2023) </label>
                                </div>
                                <input type="button" onclick="getTotalServiceDate()" value="Total Date" class="btn-primary"/>                                
                                <span id="totalDate"></span>

                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-2">
                                    <form:input path="totalpoliceyears" id="totalGovernoryears" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="totalpolicemonths" id="totalGovernormonths" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="totalpolicedays" id="totalGovernordays" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
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
                                    <label for="cashAwardsNo"> A) Cash rewards </label>
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
                                    <form:input path="presidentcommendation" id="presidentcommendation" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)" readonly="true"/>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="presidentcommendationAmt" id="presidentcommendationAmt" class="form-control" maxlength="10" onkeypress="return numbersOnly(event)" readonly="true"/>
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
                                    <label for="meritoriousYear"> If Governor Medal Awarded earlier <span style="color: red">*</span></label>
                                </div>
                                <div class="row" style="margin-bottom: 7px;"> 
                                    <div class="col-lg-2">
                                        <%-- <div class="col-lg-2">                                    
                                             <input type="radio" id="approvGovernorMedaAwarded" name="approvGovernorMedalAwarded" value="yes" onclick="radioClickedForApprovalOfAuthority()"><b> (a) Yes </b>
                                         </div>

                                    <div class="col-lg-2">
                                        <input type="radio" id="NotapprovGovernorMedaAwarded" name="approvGovernorMedalAwarded" value="No" onclick="radioClickedForApprovalOfAuthority()"> <b>No</b>
                                    </div>  --%>
                                        <form:select path="approvGovernorMedalAwarded" class="form-control" onclick="toggleapprovGovernorMedalAwardedDiv()">
                                            <form:option value=""> Select </form:option>
                                            <form:option value="Yes"> Yes </form:option>
                                            <form:option value="No"> No </form:option>
                                        </form:select>
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;" id="meritoriousYearDiv">
                                    <div class="col-lg-3">
                                        Year:
                                    </div>
                                    <div class="col-lg-3">
                                        <form:select path="meritoriousYear" id="meritoriousYear" class="form-control">
                                            <form:option value="">--Select--</form:option>
                                            <form:options items="${yearlist}" itemLabel="label" itemValue="value"/>
                                        </form:select>
                                    </div>
                                </div>
                            </div>



                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Punishment(s) </label>
                                </div>
                                <div class="col-lg-3">
                                    Details of Penalty<br />(Major/Minor)
                                </div>
                                <%-- <div class="col-lg-2">
                                     Order No.
                                 </div>
                                 <div class="col-lg-2">
                                     Year(s).
                                 </div>--%>


                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    a) In case of Major During service Career <span style="color: red">*</span>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="ifMajorPunishment" id="ifMajorPunishment" class="form-control" onclick="toggleMajorPunishmentDiv()">
                                        <form:option value="">Select</form:option>
                                        <form:option value="No">No</form:option>
                                        <form:option value="Yes">Yes</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-3" id="penaltydetailsMojorDiv">
                                    <form:textarea path="penaltydetailsMojor" id="penaltydetailsMojor" class="form-control" readonly="true"/>
                                </div>
                                <%--  <div class="col-lg-2">
                                      <form:input path="penaltyOrderNo" id="penaltyOrderNo" class="form-control" maxlength="4" style="width:100px;"/>
                                  </div>
                                  <div class="col-lg-2">
                                      <form:input path="penaltyyear" id="penaltyyear" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)"/>
                                  </div> --%>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    b)  In case of Minor During preceeding five years <span style="color: red">*</span>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="ifMinorPunishment" id="ifMinorPunishment" class="form-control" onclick="toggleMinorPunishmentDiv()">
                                        <form:option value="">Select</form:option>
                                        <form:option value="No">No</form:option>
                                        <form:option value="Yes">Yes</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-3" id="penaltydetailsMinorDiv">
                                    <form:textarea path="penaltydetailsMinor" id="penaltydetailsMinor" class="form-control" readonly="true"/>
                                </div>
                                <%-- <div class="col-lg-2">
                                     <form:input path="penaltyOrderNo" id="penaltyOrderNo" class="form-control" maxlength="4" style="width:100px;"/>
                                 </div>
                                 <div class="col-lg-2">
                                     <form:input path="penaltyyear" id="penaltyyear" class="form-control" maxlength="4" onkeypress="return numbersOnly(event)"/>
                                 </div>--%>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">Punishment Upload(if any)</div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamepunishmentDoc}">
                                        <a href="downloadMedalPunishmentDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamepunishmentDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNamepunishmentDoc}">
                                        <input type="file" name="punishmentDoc" id="punishmentDoc" class="fileupload"/> <span style="color:red">(Only PDF, file size maximum 3 MB  )</span>
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
                                    <label for="enquirypending"> Details of any other enquiry :<br>
                                        pending against the officer(Departmental Proceeding / Vigilance Case/ Criminal Case/ HRPC related Case) <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="ifenquirypending" id="ifenquirypending" class="form-control" onclick="toggleenquiryPendingDiv()">
                                        <form:option value="">Select</form:option>
                                        <form:option value="No">No</form:option>
                                        <form:option value="Yes">Yes</form:option>
                                    </form:select>
                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;" id="enquiryDocDiv">
                                <div class="col-lg-3">
                                    <form:textarea path="enquirypending" id="enquirypending" class="form-control" rows="4" cols="60" maxlength="350" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">Enquiry Upload(if any)</div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameenquiryDoc}">
                                        <a href="downloadEnquiryDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameenquiryDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','ENQ');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameenquiryDoc}">
                                        <input type="file" name="enquiryDoc" id="enquiryDoc" class="fileupload"/> <span style="color:red">(Only PDF, file size maximum 3 MB  )</span>
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
                                    <label> Details of disciplinary proceedings pending / contemplated against the recommendee, if any <span style="color: red">*</span></label>
                                </div>

                                <div class="col-lg-3">
                                    <form:select path="ifdisciplinaryProceedingpending" id="ifdisciplinaryProceedingpending" class="form-control" onclick="toggleDisciplinaryProceedingDiv()">
                                        <form:option value="">Select</form:option>
                                        <form:option value="No">No</form:option>
                                        <form:option value="Yes">Yes</form:option>
                                    </form:select>
                                </div>

                            </div>

                            <div class="row" style="margin-bottom: 7px;" id="dpcDocDiv">
                                <div class="col-lg-3">
                                    <form:textarea path="disciplinaryProceedingpending" id="disciplinaryProceedingpending" class="form-control" rows="4" cols="60" maxlength="350"/>
                                </div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">Disciplinary Proceedings Upload(if any)</div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamedpcDoc}">
                                        <a href="downloadDISCPCDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamedpcDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','DISCPC');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNamedpcDoc}">
                                        <input type="file" name="dpcDoc" id="dpcDoc" class="fileupload"/> <span style="color:red">(Only PDF, file size maximum 3 MB  )</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                            </div>

                            <%-- <div class="row" style="margin-bottom: 7px;">
                                 <div class="col-lg-1">
                                     <label><%=i++%>.</label>
                                 </div>
                                 <div class="col-lg-2">
                                     <label> Details of court cases pending against the recommendee, if any </label>
                                 </div>
                             </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-3">
                                    <form:textarea path="courtCasePendingDetails" id="courtCasePendingDetails" class="form-control" maxlength="100"/>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">Court Case Upload(if any)</div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNamecourtCaseDoc}">
                                        <a href="downloadCourtCaseDocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNamecourtCaseDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                        <a href="javascript:removeAttachedDocument('${AwardMedalListForm.rewardMedalId}','COURT');"><i class="glyphicon glyphicon-trash" style="color:red;"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNamecourtCaseDoc}">
                                        <input type="file" name="courtCaseDoc" id="courtCaseDoc" class="fileupload"/> <span style="color:red">(Only PDF, file size maximum 3 MB  )</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                            </div> --%>

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
                                    <label for="dateofServing"> Date of serving</label>
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
                                    <label> Recommendee'S GPF A/C No.(Required to collect ACR status from HRMS). </label>
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
                                                A.C.R. Grading for last 5 years<br>
                                                (In Case Of Missing/Not Initiated Previous 1 Year ACR may be added)
                                            </th>
                                            <th width="10%">Year</th>
                                            <th width="15%">Grading</th>
                                            <th width="15%"></th>
                                        </tr>
                                        <tr>
                                            <td rowspan="13" width="60%">
                                                O.S. –Outstanding, V.G. – Very Good, G.- Good, A.V.-Average,<br />
                                                NRC – No Remarks Certificate,Adv.–Adverse, MS-Missing ,N.A.–Not Applicable .<br />
                                            </td>


                                        </tr>
                                        <tr>
                                            <td>2016-2017</td>
                                            <td>
                                                <form:input path="acrGrading10" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2017-2018</td>
                                            <td>
                                                <form:input path="acrGrading11" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2018-2019</td>
                                            <td>
                                                <form:input path="acrGrading12" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2019-2020</td>
                                            <td>
                                                <form:input path="acrGrading13" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2020-2021</td>
                                            <td>
                                                <form:input path="acrGrading14" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>
                                        <tr>
                                            <td>2021-2022</td>
                                            <td>
                                                <form:input path="acrGrading9" class="form-control" style="width:100px;" maxlength="5" readonly="true"/>
                                            </td>
                                            <td></td>
                                        </tr>

                                    </table>
                                </div>

                                <div class="col-lg-3"></div>
                            </div>
                            <div class="row" style="margin-bottom: 15px;">

                                <div class="col-lg-2">
                                    <label for="email"> Available PAR of SI / ASI & equivalent ranks </label>
                                </div>
                                <div class="col-lg-3">
                                    <div>


                                        <c:if test="${not empty AwardMedalListForm.originalFileNameAcrGrading1Doc}">
                                            <a href="downloadACRGradingDocument.htm?doctype=ACR1&attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameAcrGrading1Doc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>&nbsp;
                                            </c:if>
                                            <c:if test="${empty AwardMedalListForm.originalFileNameAcrGrading1Doc}">
                                            <input type="file" name="acrGrading1Doc" id="acrGrading1Doc" class="acrfileupload"/> <span style="color:red">(Only PDF, file size maximum 5 MB  )</span>
                                        </c:if>

                                    </div>
                                </div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-3"></div>
                            </div>



                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="email"> Email address <span style="color: red">*</span></label>
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
                                    <label for="mobile"> Mobile No<span style="color: red">*</span> </label>
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
                                    <label for="briefdescription"> Brief description of work justifying award of Medal (No posting details) In order of importance (not exceeding 500 words each) <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="briefdescription" id="briefdescription" class="form-control" maxlength="3500" rows="4" cols="60" readonly="true"/>
                                </div>
                                <div class="col-lg-3"></div>
                                <div class="col-lg-3"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="recommendStatusofDist"> Recommendation Status From Districts/Establishment <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-6">
                                    <form:select path="recommendStatusofDist" id="recommendStatusofDist" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:option value="RECOMMENDED">RECOMMENDED</form:option>
                                        <form:option value="NOT RECOMMENDED">NOT RECOMMENDED</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="otherInformationofRange"> Any other information from Districts/Establishment </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="otherInformationofRange" id="otherInformationofRange" class="form-control" maxlength="3500" rows="4" cols="60"/>
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
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="otherInformationofRange"> Any other information from Range </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:textarea path="furtherInfoByRange" id="furtherInfoByRange" class="form-control" maxlength="3500" rows="4" cols="100"/>
                                </div>
                            </div>


                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="ProformaB"> Certificate( Proforma- B). </label>
                                </div>

                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameperformaBDoc}">
                                        <a href="downloadPerformaBdocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameperformaBDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameperformaBDoc}">
                                        <input type="file" name="performaBDoc" id="performaBDoc" class="fileupload"/> <span style="color:red">(Only PDF, file size maximum 3 MB  )</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="ProformaC"> Certificate( Proforma- C). </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameperformaCDoc}">
                                        <a href="downloadPerformaCdocument.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameperformaCDoc}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameperformaCDoc}">
                                        <input type="file" name="performaCDoc" id="performaCDoc" class="fileupload"/> <span style="color:red">(Only PDF, file size maximum 3 MB  )</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-2"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>


