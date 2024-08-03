<%-- 
    Document   : AwardCompletedRecomendationCompletedList
    Created on : 2 Mar, 2021, 2:51:52 PM
    Author     : Surendra
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
    </head>
    <body>
    <form:form action="getAwardRecomendationCompletedList.htm" commandName="AwardMedalListForm">
        <form:hidden path="awardMedalTypeId"/>
        <form:hidden path="awardYear"/>
        <div id="page-wrapper">
            <div class="panel panel-default">
                <h3 style="text-align:center"> Award/Medal List</h3>

                <div class="panel-heading">
                    Recommendation completed at Range Level

                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 7px;">

                            <div class="col-lg-12">
                                <c:if test="${AwardMedalListForm.awardMedalTypeId eq '07'}">
                                    DGP'S DISCS AWARD
                                </c:if>
                                <c:if test="${AwardMedalListForm.awardMedalTypeId eq '14'}">
                                    AWARD OF CHIEF MINISTER'S MEDAL FOR EXCELLENCE IN INVESTIGATION (Year ${AwardMedalListForm.awardYear})
                                </c:if>    
                            </div>  

                        </div>

                        <div class="row" style="margin-bottom: 7px;">

                            <div class="col-lg-12" style="text-align:center">
                                Select District/Establishment 
                                <form:select path="submittedRangeOff">
                                    <form:option value="">All</form:option>
                                    <form:options items="${rangeList}" itemLabel="label" itemValue="value"/>
                                </form:select>
                                    <input type="submit" value="Get List"/>

                            </div>  

                        </div>
                    <div class="table-responsive">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped" id="tabid">
                                <thead>
                                    <tr>
                                        <th width="5%">#</th>
                                        <th width="15%">Range Name</th>
                                        <th width="15%">District /Establishment Name</th>
                                        <th width="15%">Employee Name / Rank </th>
                                        <th width="15%">Recommendation Status(Dist/Establishment)</th>
                                        <th width="15%">Recommendation Status(Range)</th>
                                        <th width="10%">Submitted On</th>
                                        <th width="10%">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${awardeeList}" var="award" varStatus="counter">
                                        <tr>
                                            <td>${counter.count} </td>
                                            <td>${award.submittedRangeOff}</td>
                                            <td>${award.offName}</td>
                                            <td> ${award.fullname} </br> ${award.designation} </td>
                                            <td> ${award.recommendStatusofDist}</td>
                                            <td> ${award.recommendStatusofRange}</td>
                                            <td> ${award.rangeSubmittedOn}</td>
                                            <td> 
                                                <a href="awardormedalFormLoadForRange.htm?rewardMedalId=${award.rewardMedalId}&awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}" target="_blank">View</a>  
                                                <c:if test="${not empty award.submittedOn}">| <a href="awardormedalDGPPDF.htm?rewardMedalId=${award.rewardMedalId}&awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}" target="_blank">Download</a> </c:if>
                                                </td>
                                            </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>

        </div>
    </form:form>
</body>
</html>
