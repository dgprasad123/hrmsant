<%-- 
    Document   : RecommendedEmployeeDetail
    Created on : 19 Oct, 2020, 11:42:30 AM
    Author     : Manisha
--%>



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .form-group {
                margin-bottom: 0px;
            }
            .myModalBody{}
        </style>
        <script type="text/javascript">
            $(document).ready(function () {

                $("#deptName").change(function () {
                    $('#postCode').empty();
                    var url = 'getDeptWisePostListJSON.htm?deptCode=' + this.value;
                    $.getJSON(url, function (result) {
                        $.each(result, function (i, field) {
                            $('#postCode').append($('<option>', {
                                value: field.postcode,
                                text: field.post
                            }));
                        });
                    });
                });
                $('#recommendationandCommendationdocument').on('change', function (evt) {
                    console.log(this.files[0].size);
                });
                maxLength(document.getElementById("recommendationandCommendation"));
                maxLength(document.getElementById("empExceptionalWork"));
                maxLength(document.getElementById("empActivitiesandIssue"));
                maxLength(document.getElementById("reasonforrecommendation"));

            });

            function savedocuments() {

                if ($("#reasonforrecommendation").val() == "") {
                    alert("Reason(s) for nomination by field office cannot be blank");
                    $("#reasonforrecommendation").focus();
                    return false;
                }
                var fup = document.getElementById('recommendationandCommendationdocument');
                var fileName = fup.value;
                var ext = fileName.substring(fileName.lastIndexOf('.') + 1);
                var ext = ext.toLowerCase();

                var fup2 = document.getElementById('empExceptionalWorkdocument');
                var fileName2 = fup2.value;
                var ext2 = fileName2.substring(fileName2.lastIndexOf('.') + 1);
                var ext2 = ext2.toLowerCase();

                var fup3 = document.getElementById('otherActivitiesDocument');
                var fileName3 = fup3.value;
                var ext3 = fileName3.substring(fileName3.lastIndexOf('.') + 1);
                var ext3 = ext3.toLowerCase();

                if (fileName != "" && (ext != "pdf" && ext != "zip")) {
                    alert("Upload pdf/zip files only For Any recommendation and Commendation document");
                    fup.focus();
                    return false;
                }
                if (fileName2 != "" && (ext2 != "pdf" && ext2 != "zip")) {
                    alert("Upload pdf/zip files only For Any employee Exceptional Work document");
                    fup2.focus();
                    return false;
                }
                if (fileName3 != "" && (ext3 != "pdf" && ext3 != "zip")) {
                    alert("Upload pdf/zip files only For Any employee Activities and Issuedocument");
                    fup2.focus();
                    return false;
                }
                var fi = document.getElementById("recommendationandCommendationdocument");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 20000) {
                    alert("File too Big, please select a file less than 20mb");
                    $("#recommendationandCommendationdocument").val('');
                    return false;
                }

                var fi = document.getElementById("empExceptionalWorkdocument");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 20000) {
                    alert("File too Big, please select a file less than 20mb");
                    $("#empExceptionalWorkdocument").val('');
                    return false;
                }

                var fi = document.getElementById("otherActivitiesDocument");
                var fsize = fi.files.item(0).size;
                var file = Math.round((fsize / 1024));
                if (file >= 20000) {
                    alert("File too Big, please select a file less than 20mb");
                    $("#otherActivitiesDocument").val('');
                    return false;
                }

            }

            function openrecommendationWindow() {
                $('#recommended').modal('show');

            }
            function assignNewRecommendation() {

                $('#deptname').html($('#deptName option:selected').text());
                $('#postname').html($('#postCode option:selected').text());

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
        <form:form action="saveRecommendedEmployeeDetail.htm" commandName="recommendationDetailBean" method="post" class="form-horizontal" enctype="multipart/form-data">
            <div class="container-fluid">

                <div class="panel panel-default">
                    <div class="panel-body"> 
                        <div class="form-group">
                            <label class="control-label col-sm-3" >1.Name and designation of the employee:</label>
                            <div class="col-sm-8"> 
                                <form:hidden path="recommendationId"/>
                                <form:hidden path="recommendeddetailId"/>
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
                            <div class="col-sm-8"> <b>${recommendationDetailBean.recommendedempofficename}</b></div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" >4. Detail information:</label>
                            <div class="col-sm-8">                           </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-3" >a) Commendation issued by Government Authorities</label>
                            <div class="col-sm-7"> 
                                <c:if test="${empty recommendationDetailBean.submittedondate}">
                                    <form:textarea class="form-control" path="recommendationandCommendation" maxlength="3400"/>
                                    <span style="font-size: 12px;color: red;">(Not more than 200 Words)</span>
                                </c:if>
                                <c:if test="${not empty recommendationDetailBean.submittedondate}">
                                    <p>${recommendationDetailBean.recommendationandCommendation}</p>
                                </c:if>
                            </div>
                            <div class="form-group row">
                                <c:if test="${empty recommendationDetailBean.authoritiesoriginalfilename && empty recommendationDetailBean.submittedondate}">
                                    <label for="fa" class="col-sm-2 col-form-label">Document (if any) <span style="font-size: x-small;color: red;">(Max 20 MB)</span></label>
                                    <div class="col-sm-2">                                        
                                        <input type="file" class="form-control-file" id="recommendationandCommendationdocument" name='recommendationandCommendationdocument'>                                        
                                    </div>
                                </c:if>
                                <c:if test="${not empty recommendationDetailBean.authoritiesoriginalfilename}">
                                    <a href="downloadFile.htm?recommendeddetailId=${recommendationDetailBean.recommendeddetailId}&documentTypeName=authorities">${recommendationDetailBean.authoritiesoriginalfilename}</a>
                                </c:if>
                            </div> 
                        </div>

                        <div class="form-group">
                            <label class="control-label col-sm-3" >b) Exceptional work done in public interest</label>
                            <div class="col-sm-7">
                                <c:if test="${empty recommendationDetailBean.submittedondate}">
                                    <form:textarea class="form-control" path="empExceptionalWork" maxlength="3400"/>
                                    <span style="font-size: 12px;color: red;">(Not more than 200 Words)</span>
                                </c:if>
                                <c:if test="${not empty recommendationDetailBean.submittedondate}">
                                    <p>${recommendationDetailBean.empExceptionalWork}</p>
                                </c:if>
                            </div>
                            <div class="form-group row">
                                <c:if test="${not empty recommendationDetailBean.exceptionalworkoriginalfilename}">
                                    <a href="downloadFile.htm?recommendeddetailId=${recommendationDetailBean.recommendeddetailId}&documentTypeName=exceptional">${recommendationDetailBean.exceptionalworkoriginalfilename}</a>
                                </c:if>

                                <c:if test="${empty recommendationDetailBean.exceptionalworkoriginalfilename && empty recommendationDetailBean.submittedondate}">
                                    <label for="fa" class="col-sm-2 col-form-label">Document (if any) <span style="font-size: x-small;color: red;">(Max 20 MB)</span></label>
                                    <div class="col-sm-2">
                                        <input type="file" class="form-control-file" id="empExceptionalWorkdocument" name='empExceptionalWorkdocument'>                                                                            
                                    </div>
                                </c:if>

                            </div> 
                        </div>


                        <div class="form-group">
                            <label class="control-label col-sm-3" >c) Any document(s) certifying performance and conduct</label>
                            <div class="col-sm-7">
                                <c:if test="${empty recommendationDetailBean.submittedondate}">
                                    <form:textarea class="form-control" path="empActivitiesandIssue" maxlength="3400"/>
                                    <span style="font-size: 12px;color: red;">(Not more than 200 Words)</span>
                                </c:if>
                                <c:if test="${not empty recommendationDetailBean.submittedondate}">
                                    <p>${recommendationDetailBean.empActivitiesandIssue}</p>
                                </c:if>
                            </div>
                            <div class="form-group row">
                                <c:if test="${not empty recommendationDetailBean.otheractivitiesoriginalfilename}">
                                    <a href="downloadFile.htm?recommendeddetailId=${recommendationDetailBean.recommendeddetailId}&documentTypeName=other">${recommendationDetailBean.otheractivitiesoriginalfilename}</a>
                                </c:if>                            
                                <c:if test="${empty recommendationDetailBean.otheractivitiesoriginalfilename && empty recommendationDetailBean.submittedondate}">
                                    <label for="fa" class="col-sm-2 col-form-label">Document (if any) <span style="font-size: x-small;color: red;">(Max 20 MB)</span></label>
                                    <div class="col-sm-2">
                                        <input type="file" class="form-control-file" id="otherActivitiesDocument" name='otherActivitiesDocument'>                                                                            
                                    </div>
                                </c:if>
                            </div>                                                 
                        </div>
                        <c:if test="${office.lvl ne '01' && office.lvl ne '02' && office.category ne 'DISTRICT COLLECTORATE'}">
                            <div class="form-group">
                                <label class="control-label col-sm-3"  >

                                    d) Reason(s) for nomination by field office <span style="color: red">*</span>                                

                                </label>
                                <div class="col-sm-7">
                                    <c:if test="${empty recommendationDetailBean.submittedondate}">
                                        <form:textarea class="form-control" path="reasonforrecommendation"  maxlength="3400" />
                                        <span style="font-size: 12px;color: red;">(Not more than 200 Words)</span>
                                    </c:if>
                                    <c:if test="${not empty recommendationDetailBean.submittedondate}">
                                        <p>${recommendationDetailBean.reasonforrecommendation}</p>
                                    </c:if>
                                </div>                                                                        
                            </div>
                        </c:if>
                        <c:if test="${office.lvl eq '01' || office.lvl eq '02' || office.category eq 'DISTRICT COLLECTORATE'}">
                            <div class="form-group">
                                <label class="control-label col-sm-3" >d) Overall views on why the employee is considered worthy of nomination</label>
                                <div class="col-sm-7">
                                    <c:if test="${empty recommendationDetailBean.submittedondate}">
                                        <form:textarea class="form-control" path="overallviews" maxlength="3400"/>
                                        <span style="font-size: 12px;color: red;">(Not more than 200 Words)</span>
                                    </c:if>
                                    <c:if test="${not empty recommendationDetailBean.submittedondate}">
                                        <p>${recommendationDetailBean.reasonforrecommendation}</p>
                                    </c:if>
                                </div>                                                                        
                            </div>
                        </c:if>
                    </div>
                </div>

            </div>

            <div class="panel-footer">
                <c:if test="${empty recommendationDetailBean.submittedondate}">
                    <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return savedocuments()" />
                </c:if>
                <input type="submit" name="action" value="Back" class="btn btn-default" />
                <span class="label label-success">${msg}</span>
            </div>




            <%-- modal for Assign New Recommendation--%>
            <div id="recommended" class="modal fade" role="dialog">
                <div class="modal-dialog  modal-lg">

                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Choose Office Name and department For Recommendation</h4>
                        </div>
                        <div class="modal-body">
                            <form class="form-horizontal">
                                <input type="hidden" name="recommendedempId" id="recommendedempId" value=""/>      
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Department Name: </label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="deptName" id="deptName">
                                            <option value="">Select</option>
                                            <c:forEach items="${departmentList}" var="department">
                                                <option value="${department.deptCode}">${department.deptName}</option>
                                            </c:forEach>  
                                        </select>
                                    </div>
                                </div>


                                <div class="form-group">
                                    <label class="control-label col-sm-2">Post Name:</label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="postCode" id="postCode">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" onclick="assignNewRecommendation()">Select</button>
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>


