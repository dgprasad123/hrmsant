<%-- 
    Document   : ViewAwardMedalByDGP
    Created on : 2 Mar, 2021, 3:05:05 PM
    Author     : Surendra
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
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#docPresentRank').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#dojDistEst').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                togglePrevAwardDiv();
                toggleDiv();
                togglerangeStatus();
            });
            function togglePrevAwardDiv() {
                if ($("#prevAwardCmb").val() == 'Yes') {
                    $("#previosuAwardId").show();
                } else {
                    $("#previosuAwardId").hide();
                }
            }

            function toggleDiv() {
                if ($("#dpcifany").val() == 'Y') {
                    $("#hideDiscDocumentRow").show();
                    $("#hideDiscDocumentRow2").show();
                    $("#hideDiscDocumentRow3").show();
                } else {
                    $("#hideDiscDocumentRow").hide();
                    $("#hideDiscDocumentRow2").hide();
                    $("#hideDiscDocumentRow3").hide();
                }
            }

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

            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <% int i = 1;%>

            <form:form action="recommendAwardByRangeForm.htm" commandName="AwardMedalListForm">
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> RECOMMENDATION FOR AWARD OF DGP'S DISCS TO POLICE PERSONNEL FOR SPECTACULAR SERVICE RENDERED ON THE OCCASION OF ODISHA POLICE FORMATION DAY ON 01.04.2022 </h3>

                        <div class="panel-heading">

                        </div>
                        <div class ="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="officeName">Name of the Range/Office</label>
                                </div>
                                <div class="col-lg-6">
                                    <form:input path="offName" id="offName" class="form-control" readonly="true" />
                                    <form:hidden path="offCode"/>
                                    <form:hidden path="awardMedalTypeId"/>
                                    <form:hidden path="awardYear"/>
                                    <form:hidden path="rewardMedalId"/>
                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="fullname"> Name (In Capitals)</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="fullname" id="fullname" class="form-control" readonly="true"/>
                                    <form:hidden path="empId"/>
                                </div>
                                <div class="col-lg-3">

                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="designation"> Designation </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="designation" id="designation" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-3">

                                </div>
                                <div class="col-lg-3">

                                </div>
                            </div>   
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dob"> Date of Birth </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dob" id="dob" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="doa"> Date of Appointment </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="doa" id="doa" class="form-control" readonly="true"/>
                                </div>
                            </div>   
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="serviceBookCopy"> 1st Page of Service Book be attached</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameSB}">
                                        <a href="downloadServiceBookCopyForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameSB}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>

                                </div>

                            </div>     
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="docPresentRank"> Date of Continuing in the Present Rank </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="docPresentRank" id="docPresentRank" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dojDistEst"> Date of joining in concerned Dist./ Esst. </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="dojDistEst" id="dojDistEst" class="form-control" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-11">
                                    <label for="docPresentRank">  Reward (category wise) during the Financial Year 2021-22 </label>
                                </div>

                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="moneyReward"> i) Money Reward </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="moneyReward" id="moneyReward" class="form-control" maxlength="3" readonly="true"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="commendation"> ii) Commendation/ High Commendation </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="commendation" id="commendation" class="form-control" maxlength="3" readonly="true"/>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="gsMark"> iii) G.S. Mark </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="gsMark" id="gsMark" class="form-control" maxlength="3" readonly="true"/>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="appreciation"> iv) Appreciation </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="appreciation" id="appreciation" class="form-control" maxlength="3" readonly="true"/>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="aar"> iv) A.A.R </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="aar" id="aar" class="form-control" maxlength="3" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-11">
                                    <label for="punishmentMajor">  Punishment (Category wise) during the Financial Year 2021-22  </label>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="punishmentMajor"> Major </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="punishmentMajor" id="punishmentMajor" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-1">

                                </div>
                                <div class="col-lg-2">
                                    <label for="punishmentMinor"> Minor </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="punishmentMinor" id="punishmentMinor" class="form-control" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="moneyReward"> Whether he/she has received DGP's Discs during the previous year.</label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select class="form-control" path="prevAwardCmb" id="prevAwardCmb" onclick="togglePrevAwardDiv()">
                                        <form:option value="No">No</form:option>
                                        <form:option value="Yes">Yes</form:option>
                                    </form:select>
                                </div>


                            </div>  
                            <div class="row" style="margin-bottom: 7px;" id="previosuAwardId">
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-1">
                                    <label for="awardMedalPreviousYear"> Year </label>
                                </div>
                                <div class="col-lg-1">
                                    <form:input path="awardMedalPreviousYear" id="awardMedalPreviousYear" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="awardMedalRank"> Rank </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="awardMedalRank" id="awardMedalRank" class="form-control" readonly="true"/>
                                </div>
                                <div class="col-lg-2">
                                    <label for="awardMedalPostingPlace"> Place of Posting </label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="awardMedalPostingPlace" id="awardMedalPostingPlace" class="form-control" readonly="true"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="moneyReward"> Whether any HRPC/Criminal/ Vigilance cases/ Departmental Proceeding is pending against him/her.</label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dpcifany" id="dpcifany" class="form-control" onclick="toggleDiv()">
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
                                    <c:if test="${not empty AwardMedalListForm.originalFileName}">
                                        <a href="downloadDPCForAward.htm?attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileName}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
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
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="discCCROll">CCRs for last 3 years (in case of ASI/HM and SI/Sergeant/Dy.Sub/DSI may be uploaded.) </label>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;" id="hideCCROLLRow">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="discCCRollone"> Upload document </label>
                                </div>
                                <div class="col-lg-3">
                                    2018-19&nbsp;
                                    <form:input path="ccrolloneremarks" id="ccrolloneremarks" class="form-control" maxlength="50" placeholder="Remarks"/>
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollone}">
                                        <a href="downloadCCROLLMultipleForAward.htm?doctype=CCROLLOne&attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameCCRollone}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameCCRollone}">
                                        <input type="file" name="discCCRollone" class="fileupload"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-3">
                                    2019-20&nbsp;
                                    <form:input path="ccrolltworemarks" id="ccrolltworemarks" class="form-control" maxlength="50" placeholder="Remarks"/>
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameCCRolltwo}">
                                        <a href="downloadCCROLLMultipleForAward.htm?doctype=CCROLLTwo&attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameCCRolltwo}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameCCRolltwo}">
                                        <input type="file" name="discCCRolltwo" class="fileupload"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>
                                <div class="col-lg-3">
                                    2020-21&nbsp;
                                    <form:input path="ccrollthreeremarks" id="ccrollthreeremarks" class="form-control" maxlength="50" placeholder="Remarks"/>
                                    <c:if test="${not empty AwardMedalListForm.originalFileNameCCRollthree}">
                                        <a href="downloadCCROLLMultipleForAward.htm?doctype=CCROLLThree&attachId=${AwardMedalListForm.rewardMedalId}" target="_blank">${AwardMedalListForm.originalFileNameCCRollthree}<i class="fa fa-picture-o" style="color:red" aria-hidden="true"></i></a>
                                        </c:if>
                                        <c:if test="${empty AwardMedalListForm.originalFileNameCCRollthree}">
                                        <input type="file" name="discCCRollthree" class="fileupload"/> <span style="color:red">(Only PDF)</span>
                                    </c:if>
                                </div>
                            </div> 

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>.</label>
                                </div>
                                <div class="col-lg-10">
                                    <label for="briefNote"> Brief note on spectacular meritorious services/ 
                                        detection rendered during the Financial year 2021-22. </label>
                                </div>
                            </div>  
                            <div class="row" style="margin-bottom: 7px;" id="hideDiscDocumentRow2">
                                <div class="col-lg-1">
                                    <label> </label>
                                </div>
                                <div class="col-lg-2">

                                </div>
                                <div class="col-lg-6">
                                    <form:textarea path="briefNote" id="briefNote" class="form-control" rows="4" cols="100" maxlength="450" readonly="true"/>
                                </div>
                            </div>  

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label><%=i++%>. </label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="recommendStatusofDist"> Recommendation Status From Districts/Establishment </label>
                                </div>
                                <div class="col-lg-6">
                                    ${AwardMedalListForm.recommendStatusofDist}
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
                        </div>
                        <div class="panel-footer">

                        </div>
                    </div>

                </form:form>
            </div>
        </div>
    </body>
</html>

