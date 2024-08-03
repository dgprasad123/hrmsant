<%-- 
    Document   : AwardMedalList
    Created on : 25 Feb, 2021, 7:54:08 AM
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


                $("#addMoreemployeeModal").on("show.bs.modal", function(e) {
                    if ($("#sltpostName").val() == '') {
                        alert('Please Select Rank and press Get Employee then click on  ADD More Employee.');
                        return false;
                    } else if ($("#sltNominationForPost").val() == '') {
                        alert('Please Select Nomination for and press Get Employee then click on  ADD More Employee.');
                        return false;
                    } else {
                        var link = $(e.relatedTarget);
                        $(this).find(".modal-body").load(link.attr("href"));
                    }
                });

            });
            function validate() {
                if ($("#awardMedalTypeId").val() == '') {
                    alert('Please select type of Award/Medal.');
                    return false;
                }
                if ($("#awardYear").val() == '') {
                    alert('Please select Year.');
                    return false;
                }
                if ($("#sltAwardOccasion").val() == '') {
                    alert('Please select Occasion.');
                    return false;
                }
            }
        </script>
    </head>
    <body>

        <div id="wrapper">
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>     
            <form:form action="awardormedalList.htm" commandName="AwardMedalListForm">
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <h3 style="text-align:center"> Award/Medal List</h3>

                        <div class="panel-heading">


                        </div>
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="awardMedalName"> Select type of Award/Medal <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select class="form-control" path="awardMedalTypeId" id="awardMedalTypeId">
                                        <form:option value="">--Select One--</form:option>
                                        <form:options items="${awardType}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </div>  
                                <div class="col-lg-2">
                                    <label for="awardYear"> Select Year <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select class="form-control" path="awardYear" id="awardYear">
                                        <form:option value="">--Select One--</form:option>
                                        <%--<form:option value="2021">2021</form:option>
                                        <form:option value="2022">2022</form:option>
                                        <form:option value="2023">2023</form:option> --%>
                                        <form:option value="2024">2024</form:option>
                                    </form:select>
                                </div>

                                <div class="col-lg-1"></div>
                            </div>

                            <div class="row">
                                <div class="col-lg-3">
                                    <label for="sltAwardOccasion"> Select Occasion <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select class="form-control" path="sltAwardOccasion" id="sltAwardOccasion">
                                        <form:option value="">--Select One--</form:option>
                                         <form:option value="ID">Independence Day</form:option> 
                                       <%-- <form:option value="PFD">Police Formation Day</form:option> 
                                         <form:option value="RD">Republic Day</form:option>  --%>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                    <input type="submit" name="action" value="Ok" class="btn btn-primary" onclick="return validate()"/>
                                </div>
                                <div class="col-lg-5"></div>
                            </div>

                            <div class="table-responsive" style="margin-top:5px;">
                                <div class="table-responsive">
                                    <c:if test="${not empty AwardMedalListForm.awardMedalTypeId}">
                                        <a href="searchEmployeeForAward.htm?awardYear=${AwardMedalListForm.awardYear}&awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&sltAwardOccasion=${AwardMedalListForm.sltAwardOccasion}" data-remote="false" data-toggle="modal" title="Add More Candidate" data-target="#addMoreemployeeModal" id="addmore"><input type="button" value="Add Candidate" class="btn btn-success"/></a>
                                        </c:if>
                                    <table class="table table-bordered table-hover table-striped" id="tabid">
                                        <thead>
                                            <tr>
                                                <th width="5%">#</th>
                                                <th width="10%">HRMS ID/ GPF No</th>
                                                <th width="20%">Employee Name</th>
                                                <th width="10%">DOB/DOJ</th>
                                                <th width="10%">Completion </br> Status</th>
                                                <th width="15%">Submitted On</th>
                                                <th width="15%">Submitted To</th>
                                                <th width="15%">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${awardeeList}" var="award" varStatus="counter">
                                                <tr>
                                                    <td>${counter.count} </td>
                                                    <td>${award.empId}</br>${award.gpfNo}</td>
                                                    <td>${award.fullname}</td>
                                                    <td>${award.dob}</br>${award.doa}</td>

                                                    <td> <c:if test="${award.completedStatus eq 'Y'}"> Completed </c:if> </td>
                                                    <td> ${award.submittedOn}</td>
                                                    <td> ${award.submittedRangeOff}</td>
                                                    <td> 
                                                        <a href="awardormedalFormLoad.htm?rewardMedalId=${award.rewardMedalId}&awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&sltAwardOccasion=${AwardMedalListForm.sltAwardOccasion}">Edit</a> | 
                                                        <c:if test="${award.completedStatus eq 'Y' && empty award.submittedOn}">
                                                            <a href="submit2rangeOffice.htm?rewardMedalId=${award.rewardMedalId}&awardYear=${AwardMedalListForm.awardYear}&awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}&sltAwardOccasion=${AwardMedalListForm.sltAwardOccasion}" onclick="return confirm('Are you sure to Submit?')"> Submit to Range </a>
                                                        </c:if>
                                                        <c:if test="${award.completedStatus eq 'Y' && not empty award.submittedOn}"> 
                                                            <a href="awardormedalDGPPDF.htm?rewardMedalId=${award.rewardMedalId}&awardMedalTypeId=${AwardMedalListForm.awardMedalTypeId}" target="_blank">Download</a>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                    </div>
                    <div id="addMoreemployeeModal" class="modal fade" role="dialog">
                        <div class="modal-dialog" style="width:1000px;">
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h4 class="modal-title">Employee Search Panel</h4>
                                </div>
                                <div class="modal-body">

                                </div>
                                <div class="modal-footer">                       
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>

    </body>
</html>
