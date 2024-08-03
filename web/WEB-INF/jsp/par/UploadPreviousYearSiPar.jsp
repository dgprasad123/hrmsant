<%-- 
    Document   : PARDistrictwisePrivilegeForPolice
    Created on : 9 Jun, 2022, 12:41:37 PM
    Author     : Manisha
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <link href="css/sb-admin.css" rel="stylesheet" type="text/css">
        <link href="css/select2.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">

        <script src="js/jquery.min.js"></script> 
        <script src="js/jquery2.0.3.min.js"></script>
        <script src="js/jquery-ui.min.js"></script> 
        <script src="js/bootstrap.min.js"></script>
        <script language="javascript" src="js/jquery.datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/jquery.dataTables.js"></script>
        <script src="js/select2.min.js"></script>
        
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
            .myModalBody{}
            .new_sty{width:33%;}
            .new_sty li{width: 98%;}
            .new_sty li a{width: 100%;}
            .star {
                color: #FF0000;
                font-size: 19px;
            }
        </style>
        
        <script type="text/javascript">
            $(document).ready(function() {
                //var table = $('#datatable').DataTable();
                $("#hidAuthDeptCode").select2();
                $("#hidAuthOffCode").select2();
                $("#authSpc").select2();
                                      
                $(".loader").hide();
                $("#finalGradingDiv").hide();
                $("#nrcReasonDiv").hide();
                $('#ErrMsg').hide();
                $('#ErrMsg1').hide();
                
                $('.txtDate').datetimepicker({
                    timepicker: false,
                    format: 'd-M-Y',
                    minDate: new Date(min_year, 3, 1),
                    //maxDate: curdate,
                    maxDate: maxDPDate,
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            });
            
            function validateDateRange() {
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
                    //maxDate: curdate,
                    maxDate: maxDPDate,
                    closeOnDateSelect: true,
                    validateOnBlur: false
                });
            }
            
            function searchPar() {
                if ($("#fiscalyear").val() == "") {
                    alert("please Select the Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                $(".loader").show();
            }
                          
            function togglePARTypeDiv() {
                if ($("#parTypeForPreviousYearPAR").val() == 'NRC') {
                    $("#nrcReasonDiv").show();
                    $("#finalGradingDiv").hide();

                } else if ($("#parTypeForPreviousYearPAR").val() == 'PAR') {
                    $("#nrcReasonForPreviousYearPAR").val("");
                    $("#finalGradingDiv").show();
                    $("#nrcReasonDiv").hide();
                }
            }
            
            function getEmpParData () {
                var empId = $("#empId").val();
                var fyYear = $("#fiscalyear").val();
                var frmDate = $("#prdFrmDate").val();
                var toDate = $("#prdToDate").val();
                if(empId !='' && fyYear != '' && frmDate!='' && toDate!='')  
                {
                    $.ajax({
                        dataType: 'text',
                        type: "POST",
                        url:"getEmpAndSiParData.htm?linkId="+empId+"&fyVal="+fyYear+"&fDate="+frmDate+"&tDate="+toDate,
                        success: function(result){
                            var arr = result.split('@');               
                            var empName = arr[0];
                            var errMsg = arr[1];
                            $('#EmpName').html(empName); 
                            
                            if(errMsg == 'Err'){
                                $('#ErrMsg').show();
                                $('#btn_Save').hide();
                            }if(errMsg == 'Err1'){
                                $('#ErrMsg1').show();
                                $('#btn_Save').hide();
                            }
                            else if(errMsg == 'Pass'){
                                $('#ErrMsg').hide();
                                $('#btn_Save').show();
                            }
                        }
                    });  
                } 
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
            
            function getPost() {
                tspc = $('#authSpc').val().split("-");
                $('#hidspc').val(tspc[0]);

                $('#conclusionnotdept').val($('#hidAuthDeptCode').val());
                $('#conclusionnotoffice').val($('#hidAuthOffCode').val());
                $('#conclusionnotspc').val($('#authSpc').val());
                $('#designationDuringPeriod').val($('#authSpc option:selected').text());
            }
            
            function saveUploadDetails() {
                if($('#fiscalyear').val() == "")
                {
                    alert("Please Select Financial Year");
                    $('#fiscalyear').focus();
                    return false;
                }
                var parType = $("#parTypeForPreviousYearPAR").val();
                if (parType == '') {
                    alert("Please enter PAR Type");
                    $("#parTypeForPreviousYearPAR").focus();
                    return false;
                }
                if($('#prdFrmDate').val() == "")
                {
                    alert("Please Enter PAR Period From");
                    $('#prdFrmDate').focus();
                    return false;
                }
                if ($("#prdToDate").val() == "") {
                    alert("Please Choose Period to");
                    $("#prdToDate").focus();
                    return false;
                }
                if ($("#empId").val() == "") {
                    alert("Please Enter the Employee Id");
                    $("#empId").focus();
                    return false;
                }
                if ($("#designationDuringPeriod").val() == "") {
                    alert("Please Select the Designation during the Period");
                    $("#designationDuringPeriod").focus();
                    return false;
                }
                if ($("#subInspectorType").val() == "") {
                    alert("Please Select Sub Inspector Type");
                    $("#subInspectorType").focus();
                    return false;
                }
                if (parType == "NRC") {
                    if ($("#nrcReasonForPreviousYearPAR").val() == "") {
                        alert("Please Select NRC Reason");
                        $("#nrcReasonForPreviousYearPAR").focus();
                        return false;
                    }
                }else if (parType == "PAR") {
                    if ($("#finalGradingForPreviousYrPAR").val() == "") {
                        alert("Please Select Final Grading");
                        $("#finalGradingForPreviousYrPAR").focus();
                        return false;
                    }
                }
                if ($("#previousYearPARDocument").length > 0) {
                    if ($('#previousYearPARDocument').val() != '') {
                        var ext = $('#previousYearPARDocument').val().split('.').pop().toLowerCase();
                        var filesize = $("#previousYearPARDocument")[0].files[0].size;
                        if ($.inArray(ext, ['pdf', 'PDF']) == -1) {
                            alert('pdf or PDF files only! for Upload Previous Year PAR.');
                            return false;
                        }
                        if (filesize > 1000000) {
                            alert('Image Size must not exceed 1 MB!');
                            return false;
                        }
                    } else {
                        alert("Please select an Pdf File for Upload Previous Year PAR");
                        return false;
                    }
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
                            alert("NRC period must be less than or equal to 120 days.");
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
            
        </script>
        
    </head>
        <div id="wrapper"> 
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <form:form id="siParForm" action="uploadPreviousYearSiPar.htm" method="post" commandName="UploadPreviousPAR" enctype="multipart/form-data">
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 style="text-align:center"> Upload Previous Year PAR for SI & Equivalent Ranks (Group - B) </h3>
                        </div>
                        
                        <div class="panel-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label> <b class="star">*</b> Financial Year </label>
                                </div>
                                <div class="col-lg-3"> 
                                    <form:select path="fiscalyear" id="fiscalyear" class="form-control" onchange="validateDateRange()">
                                        <option value=""> -- Fiscal Year-- </option>
                                        <form:options items="${FiscYear}" itemValue="fy" itemLabel="fy"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-2">
                                    <label><b class="star">*</b> PAR Type </label> 
                                </div>
                                <div class="col-lg-4">
                                    <form:select path="parTypeForPreviousYearPAR" id="parTypeForPreviousYearPAR" class="form-control" onchange="togglePARTypeDiv()" style="width:75%">
                                        <form:option value=""> -- Select PAR Type-- </form:option>
                                        <form:option value="PAR"> PAR </form:option>
                                        <form:option value="NRC"> NRC </form:option>
                                    </form:select>
                                </div>
                            </div>
                                
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label><b class="star">*</b> PAR Period From </label>    
                                </div>
                                <div class="col-lg-3"> <form:input path="prdFrmDate" id="prdFrmDate" readonly="true" class="form-control txtDate"/> </div>
                                <div class="col-lg-2"> <label> <b class="star">*</b> PAR Period To </label> </div>
                                <div class="col-lg-4"> <form:input path="prdToDate" id="prdToDate" readonly="true" class="form-control txtDate" style="width:75%"/> </div>
                            </div>
                                
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label> <b class="star">*</b> Employee Id </label>
                                </div>
                                <div class="col-lg-3"> <form:input path="empId" id="empId" class="form-control" maxlength="10" onblur="getEmpParData()"/> </div> 
                                <div class="col-lg-6"> <label>Employee Name : </label> <b style="color:#FF4500" id="EmpName"></b> </div>
                            </div>
                            
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3"> <label> Service to which the officer belongs </label> </div>
                                <div class="col-lg-3"> ODISHA POLICE-III </div>
                                <div class="col-lg-2"> <label> Post Group Type </label> </div>
                                <div class="col-lg-4" style="text-align: left"> B </div>
                            </div>
                                
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label><b class="star">*</b> Designation during the period of report </label>
                                </div>
                                <div class="col-lg-7">
                                    <form:input class="form-control" path="designationDuringPeriod" id="designationDuringPeriod" readonly="true"/>
                                </div>
                                <div class="col-lg-2"> 
                                    <button type="button" class="btn btn-info" data-toggle="modal"  data-target="#designationDuringperiodModal">
                                        <span class="glyphicon glyphicon-search"></span> Search
                                    </button>
                                </div>
                            </div>
                                
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label> Place of Posting during the period of report </label>   
                                </div>
                                <div class="col-lg-3"> <form:input class="form-control" path="placeOfPostingDuringPeriod" id="placeOfPostingDuringPeriod"/> </div>
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-3"> <form:hidden path="hidspc"/> </div>
                                <div class="col-lg-1"> &nbsp; </div>
                            </div>
                                
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label><b class="star">*</b> Sub Inspector Type </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="subInspectorType" id="subInspectorType" class="form-control">
                                        <form:option value=""> -- Select SI Type-- </form:option>
                                        <form:option value="Si1"> Sub Inspector (Civil) </form:option>
                                        <form:option value="Si2"> Sub Inspector (Armed) </form:option>
                                        <form:option value="Si3"> Sub Inspector (Equivalent) </form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-3"> &nbsp; </div>
                                <div class="col-lg-1"> &nbsp; </div>
                            </div>    
                                
                            <div class="row" style="margin-bottom: 7px;" id="nrcReasonDiv">
                                <div class="col-lg-3">
                                    <label><b class="star">*</b> Reason For NRC </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="nrcReasonForPreviousYearPAR" id="nrcReasonForPreviousYearPAR" class="form-control">
                                        <form:option value=""> -- Select Reason For NRC -- </form:option>
                                        <form:option value="01">Period is less than 4 month</form:option>
                                        <form:option value="02">Availed commuted leave</form:option>
                                        <form:option value="03">Extension of Joining time</form:option>
                                        <form:option value="04">On Training</form:option>
                                        <form:option value="05">Under Suspension</form:option>
                                        <form:option value="06">Other Reasons</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-3"> &nbsp; </div>
                                <div class="col-lg-1"> &nbsp; </div>
                            </div>
                                
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label><b class="star">*</b> Upload Previous Year PAR </label>
                                </div>
                                <div class="col-lg-3">
                                    <input type="file" name="previousYearPARDocument" id="previousYearPARDocument"  class="form-control-file"/>
                                    <b style="font-style: italic;color: #FF4500;">(Only pdf File with 1 MB size are allowed)</b>
                                </div>
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-3"> &nbsp; </div>
                                <div class="col-lg-1"> &nbsp; </div>
                            </div>    
                                
                            <div class="row" style="margin-bottom: 7px;" id="finalGradingDiv">
                                <div class="col-lg-3">
                                    <label><b class="star">*</b> Final Grading </label>
                                </div>
                                <div class="col-lg-3">
                                    <form:select path="finalGradingForPreviousYrPAR" id="finalGradingForPreviousYrPAR" class="form-control">
                                        <option value=""> -- Select Final Grade -- </option>
                                        <c:forEach items="${GradeList}" var="grade">                                                                        
                                            <option value="${grade.value}">${grade.label}</option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                                <div class="col-lg-2"> &nbsp; </div>
                                <div class="col-lg-3"> &nbsp; </div>
                                <div class="col-lg-1"> &nbsp; </div>
                            </div>
                            
                            <div class="row" style="margin-bottom: 7px;" id="finalGradingDiv">
                                <div class="col-lg-12" style="text-align: center;"> 
                                    <b id="ErrMsg" style="color:#FF0000;font-size: 17px;"> Duplicate PAR Period. </b><br/>
                                    <b id="ErrMsg1" style="color:#FF0000;font-size: 17px;"> The minimum period for recording remark is four months (120 days). </b>
                                    <b style="color:#FF0000;font-size: 17px;">${ParErrMsg}<b/>
                                </div>
                            </div> 
                                
                        </div>
                    </div>
                                
                    <div class="panel-footer">
                        <div class="row" style="margin-bottom: 5px;">
                            <div class="btn-group col-lg-4"> &nbsp; </div>
                            <div class="col-lg-1" align="right">
                                <input type="submit" class="btn btn-success btn-lg" id="btn_Save" name="Save" value="Save" onclick="return saveUploadDetails()"/>
                            </div>
                            <div class="col-lg-2"> <div class="loader"></div> </div>
                            <div class="btn-group col-lg-5"> &nbsp; </div>
                        </div>

                    </div>
                                
                </div>
                                
                <%-- Modal to get Designation during Period--%>
                <div id="designationDuringperiodModal" class="modal" role="dialog">
                    <div class="modal-dialog" style="width:950px;">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Designation During Period</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label>Department</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <select name="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList();" style="width: 100%">
                                            <option value=""> -- Select Department -- </option>
                                            <c:forEach items="${DeptList}" var="dept">
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
                                        <select name="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWisePostList();" style="width: 100%">
                                            <option value=""> -- Select Office -- </option>
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
                                        <select path="authSpc" id="authSpc" class="form-control" onchange="getPost();" style="width: 100%">
                                            <option value=""> -- Select Post -- </option>
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
                                <button type="button" class="btn btn-priamry" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>                
                                
            </form:form>
        </div>    
</body>
</html>


