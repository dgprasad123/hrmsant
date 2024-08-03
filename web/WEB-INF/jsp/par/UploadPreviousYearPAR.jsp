<%-- 
    Document   : UploadPreviousYearPAR
    Created on : 8 Dec, 2021, 4:30:34 PM
    Author     : Manisha
--%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <script type="text/javascript"  src="js/basicjavascript.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <%-- <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script> --%>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {

                //togglePARStatusDiv();
                $("#finalGradingDIV").hide();
                $("#nrcreasonDiv").hide();

                var fisclyear = $('#fiscalyear').val();
                var finyear = fisclyear.split("-");
                var min_year = finyear[0];
                var max_year = parseInt(finyear[0]) + 1;

                var curdate = new Date();
                var dd = curdate.getDate();
                var mm = curdate.getMonth() + 1; //January is 0!
                var yyyy = curdate.getFullYear();
                //End - Current Date

                var maxDPDate;
                /*if (max_year <= yyyy) {
                 maxDPDate = new Date(max_year, 2, 31);
                 } else if (max_year > yyyy) {
                 maxDPDate = new Date(yyyy, mm, dd);
                 }*/
                if (max_year < yyyy) {
                    maxDPDate = new Date(max_year, 2, 31);
                } else if (max_year == yyyy) {
                    //maxDPDate = new Date(max_year, 2, 31);
                    if (mm > 2) {
                        maxDPDate = new Date(max_year, 2, 31);
                    } else {
                        maxDPDate = new Date(max_year, 2, dd);
                    }
                } else if (max_year > yyyy) {
                    maxDPDate = new Date(yyyy, mm, dd);
                }
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    minDate: new Date(min_year, 3, 1),
                    //maxDate: new Date(max_year, 2, 31),
                    maxDate: maxDPDate,
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });


                $("#DeptCode").change(function() {
                    $('#cadrename').empty();
                    var url = 'getCadreListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, obj) {
                            $('#cadrename').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                });
            });
            function monthint(monthname)
            {
                var tmonthint = "";
                switch (monthname)
                {
                    case "JAN":
                        tmonthint = "01";
                        return tmonthint;
                        break;
                    case "FEB":
                        tmonthint = "02";
                        return tmonthint;
                        break;
                    case "MAR":
                        tmonthint = "03";
                        return tmonthint;
                        break;
                    case "APR":
                        tmonthint = "04";
                        return tmonthint;
                        break;
                    case "MAY":
                        tmonthint = "05";
                        return tmonthint;
                        break;
                    case "JUN":
                        tmonthint = "06";
                        return tmonthint;
                        break;
                    case "JUL":
                        tmonthint = "07";
                        return tmonthint;
                        break;
                    case "AUG":
                        tmonthint = "08";
                        return tmonthint;
                        break;
                    case "SEP":
                        tmonthint = "09";
                        return tmonthint;
                        break;
                    case "OCT":
                        tmonthint = "10";
                        return tmonthint;
                        break;
                    case "NOV":
                        tmonthint = "11";
                        return tmonthint;
                        break;
                    case "DEC":
                        tmonthint = "12";
                        return tmonthint;
                        break;
                    default:
                        tmonthint = "0";
                }


            }

            function saveUploadDocumentDetails() {
                if ($("#fiscalyear").val() == "") {
                    alert("Please Choose Financial Year");
                    $("#fiscalyear").focus();
                    return false;
                } else if ($("#empId").val() == "") {
                    alert("Please Enter the Employee Id");
                    $("#empId").focus();
                    return false;
                } else if ($("#parTypeForPreviousYearPAR").val() == '') {
                    alert("Please enter PAR Type");
                    $("#parTypeForPreviousYearPAR").focus();
                    return false;
                } else if ($("#cadreName").val() == "") {
                    alert("Please Enter Cdre to which the Employee belongs");
                    $("#cadreName").focus();
                    return false;
                } else if ($("#postGroupType").val() == "") {
                    alert("Please Enter the Post Group Type");
                    $("#postGroupType").focus();
                    return false;
                } else if ($("#designationDuringPeriod").val() == "") {
                    alert("Please Enter the Designation during the Period");
                    $("#designationDuringPeriod").focus();
                    return false;
                } else if ($("#previousYearPARDocument").val() == "") {
                    alert("Please Upload Document");
                    $("#previousYearPARDocument").focus();
                    return false;
                } else if ($("#prdFrmDate").val() == "") {
                    alert("Please Choose Period From");
                    $("#prdFrmDate").focus();
                    return false;
                } else if ($("#prdToDate").val() == "") {
                    alert("Please Choose Period to");
                    $("#prdToDate").focus();
                    return false;
                }

                if (($('#prdFrmDate').val() != '') && ($('#prdToDate').val() != '')) {
                    var ftemp = $("#prdFrmDate").val().split("-");
                    var ttemp = $("#prdToDate").val().split("-");
                    var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                    var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);
                    if (fdt > tdt) {
                        alert("From Date must be less than To Date");
                        return false;
                    }
                }
                var oneDay = 24 * 60 * 60 * 1000;
                if ($("#parTypeForPreviousYearPAR").val() == 'NRC') {
                    if ($("#nrcReasonForPreviousYearPAR").val() == "") {
                        alert("Please Choose NRC Reason");
                        $("#nrcReasonForPreviousYearPAR").focus();
                        return false;
                    }

                    if ($("#prdFrmDate").val() != '' && $("#prdToDate").val() != '') {
                        var ftemp = $("#prdFrmDate").val().split("-");
                        var ttemp = $("#prdToDate").val().split("-");
                        var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                        var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);

                        var oneDay = 24 * 60 * 60 * 1000;
                        var diffdays = Math.round(Math.abs((fdt.getTime() - tdt.getTime()) / (oneDay)));
                        alert(diffdays);
                        if (diffdays > 120) {
                            alert("NRC period must be at less than or equal to 120 days.");
                            return false;
                        }
                    }
                } else if ($("#parTypeForPreviousYearPAR").val() == 'PAR') {

                    if ($("#finalGradingForPreviousYrPAR").val() == "") {
                        alert("Please Choose Final Grading");
                        $("#finalGradingForPreviousYrPAR").focus();
                        return false;
                    }
                    if ($("#prdFrmDate").val() != '' && $("#prdToDate").val() != '') {
                        var ftemp = $("#prdFrmDate").val().split("-");
                        var ttemp = $("#prdToDate").val().split("-");
                        var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                        var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);

                        var oneDay = 24 * 60 * 60 * 1000;
                        var diffdays = Math.round(Math.abs((fdt.getTime() - tdt.getTime()) / (oneDay)));
                        alert(diffdays);
                        if (diffdays < 120) {
                            alert("PAR period must be greater than 120 days.");
                            return false;
                        }
                    }
                }

                return true;
            }



            function changepost() {
                $('#winsubstantivepost').window('open');
            }
            function getDeptWiseOfficeList() {
                var deptcode = $('#hidAuthDeptCode').val();
                $('#hidAuthOffCode').empty();
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }
            function getOfficeWisePostList() {
                var offcode = $('#hidAuthOffCode').val();
                $('#authSpc').empty();
                var url = 'getOfficeWithSPCList.htm?offcode=' + offcode;
                url = 'getEmployeeWithSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                $('#authSpc').append('<option value="">--Select Post--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#authSpc').append('<option value="' + obj.spc + "-" + obj.empid + '">' + obj.postname + '</option>');
                    });
                });

            }

            function getdepartmentWisecadreList() {
                var deptcode = $('#hidAuthDeptCode').val();
                $('#hidAuthOffCode').empty();
                var url = 'getCadreListJSON.htm?deptcode=' + deptcode;
                $('#cadreName').append('<option value="">--Select cadre--</option>');
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#cadreName').append('<option value="' + obj.cadrecode + '">' + obj.cadreName + '</option>');
                    });
                });
            }



            function getPost() {
                tspc = $('#authSpc').val().split("-");
                $('#hidspc').val(tspc[0]);

                $('#conclusionnotdept').val($('#hidAuthDeptCode').val());
                $('#conclusionnotoffice').val($('#hidAuthOffCode').val());
                $('#conclusionnotspc').val($('#authSpc').val());
                $('#designationDuringPeriod').val($('#authSpc option:selected').text());
            }
            function togglePARStatusDiv() {
                if ($("#parTypeForPreviousYearPAR").val() == 'NRC') {
                    $("#nrcreasonDiv").show();
                    $("#finalGradingDIV").hide();

                } else if ($("#parTypeForPreviousYearPAR").val() == 'PAR') {
                    $("#finalGradingDIV").show();
                    $("#nrcreasonDiv").hide();

                    
                }
            }


            /* function employeeDetails(String empId){
             empId = $("#empId").val();
             alert(empId);
             $.post("getemployeeDetailsList.htm", {empId: empId})
             .done(function(data) {
             var emp = data.emp;
             })
             
             }*/
            function getCadre() {
                alert($('#cadrename option:selected').text());
                $('#cadreName').val($('#cadrename option:selected').text());

            }


        </script>
    </head>
    <body>
        <div id="page-wrapper">
            <form:form action="uploadPreviousYearPARList.htm" commandName="uploadPreviousPAR" method="post" class="form-horizontal" enctype="multipart/form-data">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="category">Financial Year<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="fiscalyear" id="fiscalyear" class="form-control">
                                        <option value="">Year</option>
                                        <form:options items="${fiscyear}" itemValue="fy" itemLabel="fy"/>
                                    </form:select>
                                </div>
                            </div>

                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="fullname">Employee Id<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <form:input path="empId" id="empId" class="form-control"/>
                                </div>
                                <%-- <button type="submit" class="btn btn-primary" onclick="employeeDetails('${parAdminProperties.empId}')">Search</button> --%>
                                <input type="submit" name="action" class="btn btn-default" value="Search">
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="category">Employee Name</label>
                                </div>
                                <div class="col-lg-3">
                                    ${emp.fullName}
                                    <%--<form:input path="empName" id="empName" class="form-control"/>  --%>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="category">PAR Type</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="parTypeForPreviousYearPAR" id="parTypeForPreviousYearPAR" class="form-control" onclick="togglePARStatusDiv()">
                                        <form:option value="">Select</form:option>
                                        <form:option value="PAR">PAR</form:option>
                                        <form:option value="NRC">NRC</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="cadrename">Service to which the officer belongs<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <%--${emp.cadrename} --%>
                                    <form:input class="form-control" path="cadreName" readonly="true"/>
                                </div>
                                <div class="col-lg-1">
                                    <button type="button" class="btn btn-primary" data-toggle="modal"  data-target="#cadreModal">
                                        <span class="glyphicon glyphicon-search"></span> Add Cadre
                                    </button>
                                </div>

                                <%-- <form:input path="cadrecode" id="cadrecode" class="form-control"/>--%>
                            </div>



                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="fullname">Post Group Type<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-3">
                                    <select name="postGroupType" id="postGroupType" class="form-control">
                                        <option value="">Select</option>
                                        <option value="A">A</option>
                                        <option value="B">B</option>
                                    </select> 
                                </div>
                            </div>



                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="designation">Designation during the period of report<span style="color: red">*</span></label>
                                </div>
                                <c:if test="${empty parAdminProperties.designationDuringPeriod}"> 
                                    <div class="col-lg-3">
                                        <form:hidden path="hidspc"/>
                                        <form:input class="form-control" path="designationDuringPeriod" readonly="true"/>
                                    </div>
                                    <div class="col-lg-1">
                                        <button type="button" class="btn btn-primary" data-toggle="modal"  data-target="#designationDuringperiodModal">
                                            <span class="glyphicon glyphicon-search"></span> Search
                                        </button>
                                    </div>
                                </c:if>
                                <c:if test="${not empty pbean.designationDuringPeriod}">
                                    <div class="col-lg-9">
                                        ${pbean.designationDuringPeriod}
                                    </div>
                                </c:if>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">

                                <div class="col-lg-2">
                                    <label for="fullname">PAR Period From<span style="color: red">*</span></label>
                                </div>

                                <div class="col-lg-3">

                                    <form:input path="prdFrmDate" id="prdFrmDate" readonly="true" class="txtDate"/>
                                    <%-- <form:input path="prdFrmDate" id="prdFrmDate" class="form-control"/>  --%>
                                </div> 
                                <div class="col-lg-1">
                                    <label for="category">PAR Period TO<span style="color: red">*</span></label>
                                </div>
                                <div class="col-lg-2">
                                    <form:input path="prdToDate" id="prdToDate" readonly="true" class="txtDate"/>
                                    <%-- <form:input path="prdToDate" id="prdToDate" class="form-control"/>  --%>
                                </div>

                            </div>
                            <div class="row" style="margin-bottom: 7px;"  id="nrcreasonDiv">
                                <div class="col-lg-2">
                                    <label for="category">Reason For NRC</label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="nrcReasonForPreviousYearPAR" id="nrcReasonForPreviousYearPAR">
                                        <form:option value="">Select</form:option>
                                        <form:option value="01">Period is less than 4 month</form:option>
                                        <form:option value="02">Availed commuted leave</form:option>
                                        <form:option value="03">Extension of Joining time</form:option>
                                        <form:option value="04">On Training</form:option>
                                        <form:option value="05">Under Suspension</form:option>
                                        <form:option value="06">Other Reasons</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="fullname">Upload Document</label>
                                </div>
                                <div class="col-lg-3">
                                    <input type="file" name="previousYearPARDocument" id="previousYearPARDocument"  class="form-control-file"/>
                                    <span style="font-style: italic;color: #CD5C5C;">(Only pdf / zip File are allowed)</span>
                                </div>
                            </div> 
                            <div class="row" style="margin-bottom: 7px;" id="finalGradingDIV">
                                <div class="col-lg-2">
                                    <label for="fullname">Final Grading</label>
                                </div>
                                <div class="col-lg-3">

                                    <select name="finalGradingForPreviousYrPAR" id="finalGradingForPreviousYrPAR">
                                        <option value="">Select</option>
                                        <c:forEach items="${gradelist}" var="grade">                                                                        
                                            <option value="${grade.value}">${grade.label}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div> 
                        </div>
                    </div>
                    <div class="panel-footer">
                        <%-- <c:if test="${isAppraiseeSubmittedPAR eq 'N'}"> --%>
                        <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return saveUploadDocumentDetails()"/>
                        <%-- </c:if>--%>
                        <input type="submit" name="action" value="Get Uploaded List" class="btn btn-default"/>
                        <input type="submit" name="action" value="Cancel" class="btn btn-default"/>
                        <div>
                            <span style="display:block;text-align:center;color:red;"><c:out value="${parerrmsg}"/></span>
                        </div>            
                    </div>
                </div>
            </div>

            <%-- Modal to get Designation during Period--%>
            <div id="designationDuringperiodModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Designation During Period</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                        <option value="">--Select Department--</option>
                                        <c:forEach items="${deptlist}" var="dept">
                                            <option value="${dept.deptCode}">${dept.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWisePostList();">
                                        <option value="">--Select Office--</option>
                                        <c:forEach items="${sancOfflist}" var="toffice">
                                            <option value="${toffice.offCode}">${toffice.offName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <select path="authSpc" id="authSpc" class="form-control" onchange="getPost();">
                                        <option value="">--Select Post--</option>
                                        <c:forEach items="${sancPostlist}" var="post">
                                            <option value="${post.spc}">${post.postname}</option>
                                        </c:forEach>                                         
                                    </select>
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

            <%-- Modal to get Cadre--%>
            <div id="cadreModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Add Cadre</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <select name="DeptCode" id="DeptCode" class="form-control">
                                        <option value="">--Select Department--</option>
                                        <c:forEach items="${deptlist}" var="dept">
                                            <option value="${dept.deptCode}">${dept.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Cadre</label>
                                </div>
                                <div class="col-lg-9">
                                    <select class="form-control" name="hidcadrename" id="cadrename" onchange="getCadre()
                                                    ;">
                                        <option value="">Select</option>
                                    </select>                                        
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
    </div>
</body>
</html>
