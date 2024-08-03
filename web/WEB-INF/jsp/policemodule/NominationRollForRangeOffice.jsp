<%-- 
    Document   : NominationRollForRangeOffice
    Created on : 27 Oct, 2020, 10:54:10 AM
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
        <link href="css/bootstrap.min.css" rel="stylesheet"  type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>      
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <title>JSP Page</title>
        <script type="text/javascript">
            $(document).ready(function() {

            });
            $(document).on("click", ".open-submitFormModal", function() {
                var nominationId = $(this).data('id');
                $(".modal-body #nominationId").val(nominationId);
            });

            function validateSubmit() {
                if ($("#sltRangeOffice").val() == '') {
                    alert('Please select Office');
                    return false;
                } else {
                    return true;
                }
            }
            function searchRecommendationListByRange() {
                var fiscalyear = $("#fiscalyear").val();
                var sltNominationForPost = $("#sltNominationForPost").val();
                var sltpostName = $("#sltpostName").val();

                if ($("#sltNominationForPost").val() == "") {
                    alert("please select Nomination For Post");
                    $("#sltNominationForPost").focus();
                    return false;
                }
                if ($("#fiscalyear").val() == "") {
                    alert("please select Financial Year");
                    $("#fiscalyear").focus();
                    return false;
                }
            }
        </script>


    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                <form:form action="viewnominationFormControllerForRangeOffice.htm" commandName="EmpDetNom">

                    <div class="panel panel-default">
                        <h3 style="text-align:center"> Recommendation List</h3>

                        <div class="panel-body">
                            <div class="row">
                                <div class="col-lg-2" style="text-align: left">
                                    <label for="sltNominationForPost">  Select Nomination For </label>
                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <form:select path="sltNominationForPost" id="sltNominationForPost" class="form-control">
                                        <form:option value=""> --Select One-- </form:option>
                                        <form:options items="${newRanklist}" itemLabel="label" itemValue="value"/> 
                                    </form:select>
                                </div>
                                <div class="col-lg-3">
                                    <label for="financialyear"> Select Year <span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:select path="fiscalyear" id="fiscalyear" class="form-control">
                                        <form:option value="">Year</form:option>
                                        <form:options items="${fiscyear}" itemValue="fy" itemLabel="fy"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-2" style="text-align: left">
                                    <input type="submit" name="action" value="Search" class="btn btn-primary" onclick="searchRecommendationListByRange()"/>
                                </div>


                            </div>
                            <div class="table-responsive">
                                <div class="table-responsive">
                                    <table class="table table-bordered table-hover table-striped">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Submitted By</th>
                                                <th>Submitted On</th>
                                                <th>Current Rank</th>
                                                <th>Nomination For</th>
                                                <th>Forwarded To</th>
                                                <th>Forwarded On</th>
                                                <th colspan="3" style="text-align: center">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${nominationList}" var="obj" varStatus="counter">
                                                <tr>
                                                    <td width="4%">${counter.count}</td>
                                                    <td width="20%">${obj.submittedByOffice}</td>
                                                    <td width="7%">${obj.submittedOn}</td>
                                                    <td width="15%">${obj.sltpostName}</td>
                                                    <td width="15%">${obj.sltNominationForPost}</td>
                                                    <td width="20%">${obj.forwardedToOffice}</td>
                                                    <td width="7%">${obj.forwardedOn}</td>
                                                    <td width="4%" style="text-align: center">
                                                        <c:if test="${obj.forwardedStatus ne 'Y'}">
                                                            <a href="viewNominationrollForRangeOffice.htm?nominationMasterId=${obj.nominationMasterId}" title="Click to Recommend"><i class="fa fa-pencil-square-o fa-2x"></i>
                                                            </a>
                                                        </c:if>
                                                        <c:if test="${obj.forwardedStatus eq 'Y'}">
                                                            <a href="showNominationrollForRangeOffice.htm?nominationMasterId=${obj.nominationMasterId}" title="Click to View"><i class="fa fa-check-square-o fa-2x"></i>
                                                            </a>
                                                        </c:if>
                                                    </td>
                                                    
                                                    <td width="4%" style="text-align: center">
                                                        <c:if test="${obj.forwardedStatus ne 'Y' && obj.formCompletionStatus eq 'Y'}">
                                                            <a href="javascript:void(0);" data-toggle="modal" data-id="${obj.nominationMasterId}" class="open-submitFormModal" data-target="#submitFormModal" title="Click to Submit"><i class="fa fa-arrow-circle-right  fa-2x"></i>
                                                            </a>
                                                        </c:if>
                                                        <c:if test="${obj.forwardedStatus eq 'Y'}">
                                                            Submitted
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
                    <div class="modal fade" id="submitFormModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content">
                                <!-- Modal Header -->
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">
                                        <span aria-hidden="true">&times;</span>
                                        <span class="sr-only">Close</span>
                                    </button>
                                    <h4 class="modal-title" id="myModalLabel">
                                        Submit Recommendation Form
                                    </h4>
                                </div>

                                <!-- Modal Body -->
                                <div class="modal-body">
                                    <p class="reqStatusMsg"></p>
                                    <form:hidden path="nominationId" id="nominationId"/>
                                    <div class="form-group">
                                        <label  class="col-sm-2 control-label" for="reqName">Select Office</label>
                                        <div class="col-sm-10">
                                            <select name="sltRangeOffice" class="form-control" id="sltRangeOffice">
                                                <option value="">--Select One--</option>
                                                <option value="CTCHOM0050000">DIRECTOR GENERAL AND INSPECTOR GENERAL</option>
                                            </select>
                                        </div>
                                    </div>



                                </div>

                                <!-- Modal Footer -->
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <input type="submit" name="action" class="btn btn-primary" value="Submit" onclick="return validateSubmit()">
                                </div>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        <!-- Request Modal -->

    </body>
</html>

