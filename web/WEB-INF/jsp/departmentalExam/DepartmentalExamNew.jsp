<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('.txtDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });

            function getDistrictAndDepartmentWiseOfficeList() {
                var deptcode = $("#hidAuthDeptCode").val();
                var distcode = $("#hidAuthDistCode").val();

                $("#hidAuthOffCode").empty();
                $("#hidAuthOffCode").append('<option value="">--Select Office--</option>');

                //var url = "getOfficeListDistrictAndDepartmentJSON.htm?deptcode=" + deptcode + "&distcode=" + distcode;
                var url = "getOfficeListDistrictAndDepartmentForBacklogEntryJSON.htm?deptcode=" + deptcode + "&distcode=" + distcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWiseGenericPostList() {
                var offcode = $("#hidAuthOffCode").val();

                $("#authPost").empty();
                $("#authPost").append('<option value="">--Select Generic Post--</option>');

                var url = "getAuthorityPostListJSON.htm?offcode=" + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $("#authPost").append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getPost() {
                $('#conductingPostName').val($('#authPost option:selected').text());
            }
            
            function saveCheck(){
                if($("#txtExamName").val() == ""){
                    alert("Please enter Name of the Exam");
                    $("#txtExamName").focus();
                    return false;
                }
                if($("#txtInstituteName").val() == ""){
                    alert("Please enter Name of the Institution");
                    $("#txtInstituteName").focus();
                    return false;
                }
                /*if($("#txtFromDate").val() == ""){
                    alert("Please enter From Date");
                    $("#txtFromDate").focus();
                    return false;
                }
                if($("#txtToDate").val() == ""){
                    alert("Please enter To Date");
                    $("#txtToDate").focus();
                    return false;
                }*/
                if($("#sltNotDept").val() == ""){
                    alert("Please select Notifying Department");
                    return false;
                }
                if($("input[name=rdExamResult]:checked").length == 0){
                    alert("Please select Result of Examination");
                    return false;
                }
                if($("#conductingPostName").val() == ""){
                    alert("Please select Conducting Authority");
                    return false;
                }
              return true;  
            }
        </script>
    </head>
    <body>
        <form:form action="SaveDepartmentalExam.htm" method="POST" commandName="deptExamForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Departmental Exam
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="examId" id="examId"/>
                        <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-4">
                            <label for="chkNotSBPrint">Check Not to Print in Service Book</label>
                        </div>
                        <div class="col-lg-3">   
                            <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/>
                        </div>
                        <div class="col-lg-3"></div>
                        <div class="col-lg-2"></div>
                    </div>

                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-1">1.</div>
                            <div class="col-lg-2">
                                <label for="txtExamName">Name of the Exam<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="txtExamName" id="txtExamName" maxlength="300"/>
                            </div>
                            <div class="col-lg-6"></div>
                        </div>

                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-1">2.</div>
                            <div class="col-lg-2">
                                <label for="txtInstituteName">Name of the Institution<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="txtInstituteName" id="txtInstituteName" maxlength="150"/>
                            </div>
                            <div class="col-lg-6"></div>
                        </div>

                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-1">3.</div>
                            <div class="col-lg-11">
                                <label>Period of Examination</label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-1">
                                a) From date<!--<span style="color: red">*</span>-->
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control txtDate" path="txtFromDate" id="txtFromDate" readonly="true"/>
                            </div>
                            <div class="col-lg-1">
                                b) To date<!--<span style="color: red">*</span>-->
                            </div>
                            <div class="col-lg-2">
                                <form:input class="form-control txtDate" path="txtToDate" id="txtToDate" readonly="true"/>
                            </div>
                            <div class="col-lg-4"></div>
                        </div>

                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-1">4.</div>
                            <div class="col-lg-11">
                                <label>Notification Details</label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-1"></div>
                            <div class="col-lg-2">
                                (a) Notification No
                            </div>
                            <div class="col-lg-3">   
                                <form:input class="form-control" path="txtNotNo" id="txtNotNo"/>
                            </div>
                            <div class="col-lg-6"></div>
                        </div>

                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-1"></div>
                            <div class="col-lg-2">
                                (b) Notification Date
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control txtDate" path="txtNotDate" id="txtNotDate" readonly="true"/>
                            </div>
                            <div class="col-lg-7"></div>
                        </div>

                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-1"></div>
                            <div class="col-lg-2">
                                (c) Notifying Department<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-3">   
                                <form:select path="sltNotDept" id="sltNotDept" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>
                            </div>
                            <div class="col-lg-6"></div>
                        </div>

                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-1">5.</div>
                            <div class="col-lg-2">
                                <label for="txtInstituteName">Result of Examination<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="rdExamResult" id="rdExamResult" value="Y"/> Cleared
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="rdExamResult" id="rdExamResult" value="N"/> Not Cleared
                            </div>
                            <div class="col-lg-4"></div>
                        </div>

                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-1">6.</div>
                            <div class="col-lg-2">
                                <label for="conductingPostName">Details of Conducting Department</label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="conductingPostName" id="conductingPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-6">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#conductingAuthorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                            <div class="col-lg-3"></div>
                        </div>
                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-1">7.</div>
                            <div class="col-lg-2">
                                <label for="txtNote">Note(if any)</label>
                            </div>
                            <div class="col-lg-5">
                                <form:textarea class="form-control" path="txtNote" id="txtNote"/>
                            </div>
                            <div class="col-lg-4">
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="btnDeptExam" value="Save" class="btn btn-default" onclick="return saveCheck();"/>
                        <a href="DepartmentalExamList.htm">
                            <button type="button" class="btn btn-default">Back</button>
                        </a>
                        <c:if test="${not empty deptExamForm.examId}">
                            <%--<input type="submit" name="btnDeptExam" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');"/>--%>
                        </c:if>
                    </div>
                </div>
            </div>

            <div id="conductingAuthorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Conducting Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidAuthDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidAuthDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthDistCode" id="hidAuthDistCode" class="form-control" onchange="getDistrictAndDepartmentWiseOfficeList();">
                                        <form:option value="">--Select District--</form:option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidAuthOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWiseGenericPostList();">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${officelist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authPost">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authPost" id="authPost" class="form-control" onchange="getPost();">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${gpclist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>                
        </form:form>
    </body>
</html>
