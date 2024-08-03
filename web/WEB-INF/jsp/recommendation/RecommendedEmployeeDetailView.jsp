<%-- 
    Document   : RecommendedEmployeeDetailView
    Created on : Oct 31, 2020, 7:40:10 PM
    Author     : Manas
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $("#remarksdiv").hide();
                $("#submitbtn").hide();
                $("#cancelbtn").hide();
                maxLength(document.getElementById("overallviews"));
            });

            function showRemark() {
                $("#remarksdiv").show();
                $("#submitbtn").show();
                $("#approvebtn").hide();
                $("#declinebtn").hide();
                $("#cancelbtn").show();
            }
            function cancelRemark() {
                $("#remarksdiv").hide();
                $("#submitbtn").hide();
                $("#cancelbtn").hide();
                $("#approvebtn").show();
                $("#declinebtn").show();
            }
            function validateForm() {
                return confirm('Are you sure you want to Approve?');
                var fup = document.getElementById('overallviewsdocument');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                var ext = ext.toLowerCase();

                if (fileName != "" && ext != "pdf") {
                    alert("Upload pdf files only For Overall views");
                    fup.focus();
                    return false;
                }

                var recommendationandCommendation = $("#recommendationandCommendation").val();

                if (recommendationandCommendation == "") {
                    alert("Please Enter Reasons for Nomination");
                    $("#recommendationandCommendation").focus();
                    return false;
                }
                var fi = document.getElementById("overallviewsdocument");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 20000) {
                    alert("File too Big, please select a file less than 20mb");
                    $("#overallviewsdocument").val('');
                    return false;
                }
            }
            function maxLength(el) {
                if (!('maxLength' in el)) {
                    var max = el.attributes.maxLength.value;
                    el.onkeypress = function () {
                        if (this.value.length >= max)
                            return false;
                    };
                }
            }
        </script>
    </head>

    <body>
        <div class="container-fluid">
            <form:form action="approveRecommendedEmployeeDetail.htm" commandName="recommendationDetailBean" method="post" class="form-horizontal" enctype="multipart/form-data">
                <div class="panel panel-default">
                    <div class="panel-body"> 
                        <div class="form-group">
                            <label class="control-label col-sm-3" >1.Name and designation of the employee:</label>
                            <div class="col-sm-8"> 
                                <form:hidden path="recommendationId"/>
                                <form:hidden path="recommendeddetailId"/>
                                <form:hidden path="taskId"/>
                                <b>${recommendationDetailBean.recommendedempname}, ${recommendationDetailBean.recommendedpost}</b>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3">2.Nomination Type:</label>
                            <div class="col-sm-8"> <b style="text-transform: capitalize;">${recommendationDetailBean.recommenadationType}</b>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" >3.Name Of the Office:</label>
                            <div class="col-sm-8"> <b>${recommendationDetailBean.recommendedempofficename}</b>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-3" >4. Detail information:</label>
                            <div class="col-sm-8"> 
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" >a) Commendation issued by Government Authorities</label>
                            <div class="col-sm-7">${recommendationDetailBean.recommendationandCommendation}</div>
                            <div class="form-group row" id="uploadNocDocumentDiv">                            
                                <c:if test="${not empty recommendationDetailBean.authoritiesoriginalfilename}">
                                    <a href="downloadFile.htm?recommendeddetailId=${recommendationDetailBean.recommendeddetailId}&documentTypeName=authorities">${recommendationDetailBean.authoritiesoriginalfilename}</a>
                                </c:if>
                            </div> 
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" >b) Exceptional work done in public interest</label>
                            <div class="col-sm-7"> ${recommendationDetailBean.empExceptionalWork}</div>
                            <div class="form-group row">
                                <c:if test="${not empty recommendationDetailBean.exceptionalworkoriginalfilename}">
                                    <a href="downloadFile.htm?recommendeddetailId=${recommendationDetailBean.recommendeddetailId}&documentTypeName=exceptional">${recommendationDetailBean.exceptionalworkoriginalfilename}</a>
                                </c:if>
                            </div> 
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" >c) Any document(s) certifying performance and conduct</label>
                            <div class="col-sm-7"> ${recommendationDetailBean.empActivitiesandIssue}</div>
                            <div class="form-group row">
                                <c:if test="${not empty recommendationDetailBean.otheractivitiesoriginalfilename}">
                                    <a href="downloadFile.htm?recommendeddetailId=${recommendationDetailBean.recommendeddetailId}&documentTypeName=other">${recommendationDetailBean.otheractivitiesoriginalfilename}</a>
                                </c:if>
                            </div> 
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" >d) Reason(s) for nomination by field office</label>
                            <div class="col-sm-7"> ${recommendationDetailBean.reasonforrecommendation}</div>                             
                        </div>

                        <hr/>
                        <c:if test="${empty recommendationDetailBean.isApproved}">
                            <div class="form-group" id ="remarksdiv">
                                <label class="control-label col-sm-3">Overall views on why the employee is considered worthy of nomination</label>

                                <div class="col-sm-7"> 
                                    <form:textarea class="form-control" path="overallviews" maxlength="3000"/>                                
                                    <span style="font-size: 12px;color: red;">(Not more than 200 Words)</span>
                                </div>
                                <div class="form-group row">
                                    <label for="fa" class="col-sm-2 col-form-label">Document (if any) <span style="font-size: x-small;color: red;">(Max 20 MB)</span></label>
                                    <div class="col-sm-2">
                                        <input type="file" class="form-control-file" id="overallviewsdocument" name='overallviewsdocument'>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${recommendationDetailBean.isSubmittedToDept eq 'Y'}">
                            <div class="form-group">
                                <label class="control-label col-sm-3" >Overall views on why the employee is considered worthy of nomination</label>
                                <div class="col-sm-7"> ${recommendationDetailBean.overallviews}</div>
                                <div class="form-group row">
                                    <c:if test="${not empty recommendationDetailBean.overallviewsoriginalfilename}">
                                        <a href="downloadFile.htm?recommendeddetailId=${recommendationDetailBean.recommendeddetailId}&documentTypeName=overallviews">${recommendationDetailBean.overallviewsoriginalfilename}</a>
                                    </c:if>
                                </div>
                            </div>
                        </c:if>


                    </div>
                    <div class="panel-footer">
                        <c:if test="${empty recommendationDetailBean.isApproved}">
                            <input type="button" value="Cancel" class="btn btn-default" id="cancelbtn" onclick="cancelRemark()"/>
                            
                        </c:if>
                             <a href="nominationDetailPdfView.htm?recommendeddetailId=${recommendationDetailBean.recommendeddetailId}" class="btn-default" target="_blank"><button type="button" class="btn btn-default">Download</button></a>
                        <c:if test="${LoginUserBean.loginusertype ne 'A'}">
                            <input type="submit" name="action" value="Back" class="btn btn-default" />
                        </c:if>
                    </div>
                </div>
            </form:form>
        </div>
    </body>
</html>
