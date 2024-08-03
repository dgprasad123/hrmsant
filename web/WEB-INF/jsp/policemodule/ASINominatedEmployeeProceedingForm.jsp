<%-- 
    Document   : ASINominatedEmployeeProceedingForm
    Created on : 8 Dec, 2020, 6:03:05 PM
    Author     : Surendra
--%>

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
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function callNoImage() {
                var userPhoto = document.getElementById('userPhoto');
                userPhoto.src = "images/NoEmployee.png";
            }
            $(document).ready(function () {
                toggleDiv();

                toggleServicingDiv();
            });


            function toggleDiv() {
                if ($("#dpcifany").val() == 'Y') {
                    $("#hideDiscDocumentRow").show();
                } else {
                    $("#hideDiscDocumentRow").hide();
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
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                <div class="panel panel-default">
                    <h3 style="text-align:center"> Application cum willingness form for appearing in Written</h3>
                    <h3 style="text-align:center"> Examination for promotion to the rank of ASI </h3>
                    <div class="panel-heading">
                        <a href="qualifiedListofASIController.htm"><input type="button" class="btn btn-primary" value="Back"/></a> 
                        <input class="btn btn-success" type="button" name="action" value="Save"/>
                    </div>
                    <div class="panel-body">
                        <form:form action="saveASINominationApplicationForm.htm" commandName="nominationForm">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>1.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> a) Name (in full) (In Capitals)</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:out value="${nominationForm.fullname}"/>
                                </div>
                                <div class="col-lg-3">
                                    <img src="displayprofilephoto.htm?empid=${nominationForm.empId}" id="userPhoto" onerror="callNoImage()" height="150px" width="140px" border="2"/>
                                </div>
                                <div class="col-lg-3">
                                    <img src="displayNominatedEmployeeProfilePhoto.htm?nominationformId=${nominationForm.nominationFormId}" id="userPhoto" height="150px" width="140px" border="2"/><br />
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label>b) Category</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:out value="${nominationForm.category}"/>
                                </div>
                                <div class="col-lg-6"></div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label>c) Father's Name (in full) (In Capitals)</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:out value="${nominationForm.fathersname}"/>
                                </div>
                                <div class="col-lg-6"></div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label> d) Date of Birth (In figure)(In words)</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:out value="${nominationForm.dob}"/>
                                </div>
                                <div class="col-lg-6"></div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label> e) Educational Qualifications:</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:out value="${nominationForm.qualification}"/>
                                </div>
                                <div class="col-lg-6"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label> f) Mobile:</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:out value="${nominationForm.txtMobile}"/>
                                </div>
                                <div class="col-lg-6"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label> g) Home District </label>
                                </div>
                                <div class="col-lg-3">
                                    <c:out value="${nominationForm.homedistrictName}"/>
                                </div>
                                <div class="col-lg-6"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label> h) Date of Joining as Constable</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:out value="${nominationForm.doeGov}"/>
                                </div>
                                <div class="col-lg-2">
                                    <label>District in which appointed</label>
                                </div>
                                <div class="col-lg-4">
                                    <c:out value="${nominationForm.dojAppntdDist}"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label> i) Present Rank</label>
                                </div>
                                <div class="col-lg-2">
                                    <c:out value="${nominationForm.currentRank}"/>
                                </div>
                                <div class="col-lg-2">
                                    <label>Place of Posting</label>
                                </div>
                                <div class="col-lg-2">
                                    <c:out value="${nominationForm.postingPlace}"/>
                                </div>
                                <div class="col-lg-1">
                                    <label>Date</label>
                                </div>
                                <div class="col-lg-2">
                                    <c:out value="${nominationForm.postingDate}"/>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1"></div>
                                <div class="col-lg-2">
                                    <label> j) Date of joining as Constable in new District as per change of cadre in terms of PCO-342/2013</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:if test="${not empty nominationForm.dojNewDist}">
                                        <c:out value="${nominationForm.dojNewDist}"/>
                                    </c:if>
                                    <c:if test="${empty nominationForm.dojNewDist}">
                                        NA
                                    </c:if>
                                </div>
                                <div class="col-lg-6"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>2.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Date of Completion of Constable Course of Training</label>
                                </div>
                                <div class="col-lg-3">
                                    <c:out value="${nominationForm.txtTrainingCompletedDate}"/>
                                </div>
                                <div class="col-lg-6"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>3.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label> Length of Service as on 1/1/2020 from the date of completion of constable course of training</label>
                                </div>
                                <div class="col-lg-5">
                                    <c:out value="${nominationForm.yearinServiceLength}"/> Years-<c:out value="${nominationForm.monthinServiceLength}"/> Months-<c:out value="${nominationForm.daysinServiceLength}"/> Days
                                </div>
                                <div class="col-lg-4"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>4.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label>
                                        Whether joined as constable in district/establishment on redeployment from Battalion<br/>
                                        If yes indicate the date of joining as constable in the District/Establishment.
                                    </label>
                                </div>
                                <div class="col-lg-2">
                                    <c:out value="${nominationForm.sltRedeploymentJoining}"/>
                                </div>
                                <div class="col-lg-2">
                                    <c:out value="${nominationForm.txtRedeploymentJoiningDate}"/>
                                </div>
                                <div class="col-lg-2"></div>
                                <div class="col-lg-3"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>a.</label>
                                </div>
                                <div class="col-lg-2">
                                    <label>
                                        Length of Service as Constable after redeployment as on 1/1/2020 for those coming from Battalion to district on redeployment.
                                    </label>
                                </div>
                                <div class="col-lg-5">
                                    <c:out value="${nominationForm.yearinRedeploymentServiceLength}"/> Years-<c:out value="${nominationForm.monthRedeploymentServiceLength}"/> Months-<c:out value="${nominationForm.daysRedeploymentServiceLength}"/> Days
                                </div>
                                <div class="col-lg-4"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>5</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="adverseIfany"> Adverse entries recorded in the CC Rolls, if any, during last 5 years. </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="adverseIfany" id="adverseIfany" class="form-control">
                                        <form:option value=""> --Select One-- </form:option>
                                        <form:option value="No"> No </form:option>
                                        <form:option value="Yes"> Yes </form:option>
                                    </form:select>
                                </div>

                            </div> 

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>6</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dpcifany"> Departmental/ Disciplinary Proceedings/ Vigilance Enquiry/ Criminal Cases, if any, Pending initiated (details be enclosed in a separate sheet)  </label>
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

                                    <input type="file" name="discDocument"  id="discDocument"/> <span style="color:red">(Only PDF)</span>

                                </div>

                            </div>    
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-1">
                                    <label>7</label>
                                </div>
                                <div class="col-lg-2">
                                    <label for="dateofServingifAny"> If charge has been served on the nominee in Departmental Proceeding/ Criminal Case? if Yes indicate the date of serving charge and ME. </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="dateofServingifAny" id="dateofServingifAny" class="form-control" onclick="toggleServicingDiv()">
                                        <form:option value=""> No </form:option>
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

                                    <input type="file" name="documentServingCopy"  id="documentServingCopy"/> <span style="color:red">(Only PDF)</span>

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
                        </div>
                    </div>
                </form:form>
                <div class="panel-footer">
                    <a href="qualifiedListofASIController.htm"><input type="button" class="btn btn-primary" value="Back"/></a> 
                    <input class="btn btn-success" type="button" name="action" value="Save"/>
                </div>
            </div>
        </div>
    </body>
</html>

