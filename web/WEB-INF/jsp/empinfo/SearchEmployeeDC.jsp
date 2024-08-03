<%@ page contentType="text/html;charset=windows-1252" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>        
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#deptName").change(function() {
                    //alert($("#deptName").val());
                    $('#offcode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;

                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#offcode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));

                        });
                    });
                    //alert($('#hidUserype').val());
                });
                $('.openPopup').on('click', function() {
                    var dataURL = $(this).attr('data-href');
                    $('.modal-body').load(dataURL, function() {
                        $('#myModal').modal({show: true});
                    });
                });

                $('#txtDOB').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

            });
            function alertMsg(empid)
            {
                if (confirm("Are You sure to Update ?"))
                {
                    self.location = "convertRegularToContractualSixYr.htm?empid=" + empid;

                } else {
                    return false;
                }

            }
            function empMapping(empid)
            {
                $('#hidEmpid').val(empid);
                //alert($('#hidEmpid').val());                

            }
            function open_popup(ids) {
                var con = confirm("Do you want to Reset the password");
                if (con) {
                    var URL = "ResetEmpoyeePassword.htm?empid=" + ids;
                    window.open(URL, "mywindow", "width=500,height=400,left=50px,top=50px,titlebar=0,status=0,menubar=0");
                }

            }

            function revert_thirdschedule(empid) {
                var con = confirm("Do you want to Revert Third Schedule?");
                if (con) {
                    var URL = "RevertThirdScheduleDC.htm?empid=" + empid;
                    window.open(URL, "mywindow", "width=500,height=400,left=50px,top=50px,titlebar=0,status=0,menubar=0");
                }
            }

            function revertThirdScheduleCont6Yrs(empid) {
                var con = confirm("Do you want to Revert Contractual 6 Years Third Schedule?");
                if (con) {
                    var URL = "RevertCont6YrsThirdSchedule.htm?empid=" + empid;
                    window.open(URL, "mywindow", "width=500,height=400,left=50px,top=50px,titlebar=0,status=0,menubar=0");
                }
            }

            function updateSBStatus(empId, sbStatus)
            {
                if (sbStatus == 'Y')
                {
                    msg = "Are you sure you want to update the Service Book Updation Status as Complete?";
                }
                else
                {
                    msg = "Are you sure you want to update the Service Book Updation Status as Incomplete?";
                }
                if (confirm(msg))
                {
                    $.ajax({
                        url: "UpdateServiceBookStatus.htm?empId=" + empId + '&sbStatus=' + sbStatus,
                        success: function(result) {
                            if (sbStatus == 'Y')
                            {
                                $('#status_blk').html('<a href="javascript:void(0)" onclick="javascript: updateSBStatus(\'' + empId + '\', \'N\')" class="label label-danger">Mark Service Book as Incomplete</a>');
                            }
                            else
                            {

                                $('#status_blk').html('<a href="javascript:void(0)" onclick="javascript: updateSBStatus(\'' + empId + '\', \'Y\')" class="label label-success">Mark Service Book as Complete</a>');
                            }
                        }});
                }
            }

            function validateSearch() {
                if ($("#dob").val() != '') {
                    if ($("#criteria").val() == '') {
                        alert("Please select Search Criteria!");
                        return false;
                    }
                }


                if ($("#searchString").val() != '') {

                    $("#searchString").val($("#searchString").val().trim());

                }

            }

        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="LocateEmployee.htm" commandName="searchEmployee" method="post" autocomplete="off">
                    <form:hidden path="usertype" id="hidUserype"/>
                    <form:hidden path="hidEmpid" id="hidEmpid"/>  
                    <form:hidden id="distcode" path="distcode"/>
                    <div class="container-fluid">
                        <div class="row">
                            <label class="control-label col-sm-3">Department Name</label>
                            <div class="col-sm-3" style="width:60%;">
                                <form:select class="form-control" path="deptName">
                                    <form:option value="" label="Select"/>
                                    <form:options items="${departmentList}" itemLabel="deptName" itemValue="deptCode"/>                                        
                                </form:select>
                            </div>
                        </div>

                        <div class="row">
                            <label class="control-label col-sm-3">Office Name</label>
                            <div class="col-sm-3" style="width:70%;">
                                <form:select class="form-control" path="offcode" onchange="getDistCode()">
                                    <form:option value="" label="Select"/>
                                    <form:options items="${officeList}" itemLabel="offName" itemValue="offCode"/>  
                                </form:select>
                            </div>
                        </div>

                        <div class="row">
                            <label class="control-label col-sm-3">Year</label>
                            <div class="col-sm-3">
                                <form:select class="form-control" path="year">
                                    <form:option value="0">Select</form:option>
                                    <form:option value="2016">2016</form:option>
                                </form:select>
                            </div>
                        </div>
                        <div class="row">
                            <label class="control-label col-sm-3">Search criteria</label>
                            <div class="col-sm-3">
                                <form:select class="form-control" path="criteria">
                                    <form:option value="">select</form:option>
                                    <form:option value="GPFNO">GPF NO</form:option>
                                    <form:option value="HRMSID">HRMS ID</form:option>
                                    <form:option value="FNAME">FIRST NAME</form:option>
                                    <form:option value="MOBILE">MOBILE NO</form:option>
                                    <form:option value="AADHAR">AADHAR NO</form:option>
                                    <form:option value="PAN">PAN NO</form:option>
                                    <%-- <form:option value="USERNAME">USER NAME</form:option>--%>
                                </form:select>
                            </div>
                            <label class="control-label col-sm-3">Search String</label>
                            <div class="col-sm-3" >
                                <form:input path="searchString" class="form-control"/>
                            </div>
                        </div>
                        <div class="row">
                            <label class="control-label col-sm-3">Date of Birth</label>
                            <div class="col-sm-3">
                                <form:input path="txtDOB" class="form-control" readonly="true"/>
                            </div>
                            <label class="control-label col-sm-3"></label>
                            <div class="col-sm-3" ></div>
                        </div>
                    </div> 
                    <input type="submit" value="Search" onclick="return validateSearch();"/>
                    <div class="panel-footer">
                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="5%">Hrms Id</th>
                                            <th width="18%">Employee Name</th>
                                            <th width="10%">Employee Type</th>
                                            <th width="5%">Date of Birth</th>
                                            <th width="3%">View</th>
                                                <%--<c:if test="${searchEmployee.usertype=='A'}">
                                                <th width="10%">Status</th>
                                                </c:if>--%>
                                            <th width="32%">Action</th>                                               


                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${empSearchResult.employeeList}" var="employee">
                                            <tr>
                                                <td>${employee.empid}</td>
                                                <td>${employee.fname} ${employee.mname} ${employee.lname} <br>${employee.post}</td>
                                                <td>
                                                    <c:if test="${employee.isRegular eq 'Y'}">
                                                        REGULAR
                                                    </c:if>
                                                    <c:if test="${employee.isRegular eq 'C'}">
                                                        SIX-YEAR CONTRACTUAL
                                                    </c:if>
                                                    <c:if test="${employee.isRegular eq 'N'}">
                                                        CONTRACTUAL
                                                    </c:if>
                                                    <c:if test="${employee.isRegular eq 'G'}">
                                                        WAGES
                                                    </c:if>
                                                    <c:if test="${employee.isRegular eq 'W'}">
                                                        WORK CHARGED
                                                    </c:if>
                                                    <c:if test="${employee.isRegular eq 'D'}">
                                                        NON GOVT. AIDED
                                                    </c:if>
                                                    <c:if test="${employee.isRegular eq 'A'}">
                                                        SPECIAL CATEGORY
                                                    </c:if>
                                                    <c:if test="${employee.isRegular eq 'B'}">
                                                        EX-CADRE
                                                    </c:if>
                                                </td>
                                                <td>${employee.dob}</td>                                               
                                                <td><a href="javascript:void(0);" data-href="EmployeeBasicProfile.htm?empid=${employee.empid}" class="openPopup label label-success">View</td>                                                
                                                <%-- <c:if test="${searchEmployee.usertype=='A'}">
                                                     <td>${employee.isRegular}</td>
                                                 </c:if>--%>



                                                <td>
                                                    <c:if test="${(searchEmployee.usertype eq 'D' && (matchDistCode eq 'Y' || employee.district eq loginDistcode))}">
                                                        <a target="_blank" href="getRollWiseLinkDC.htm?nodeID=${employee.empid}"  class="label label-danger">Administration</a>|
                                                        <a target="_blank" href="viewProfileVerificationControllerDC.htm?empId=${employee.empid}" class="label label-primary">Verify Employee Profile</a>
                                                        <c:if test="${logintype ne 'DEO'}">
                                                            |<a target="_blank" href="EditEmpoyeeData.htm?empid=${employee.empid}" class="label label-success">Edit</a>|
                                                            <%-- <a href="#" onclick="open_popup('${employee.empid}')" class="label label-danger" id='id2'>Reset Password</a>--%>
                                                            <%-- <a href="#" onclick="revert_thirdschedule('${employee.empid}')" class="label label-success">
                                                                 <span class="glyphicon glyphicon-circle-arrow-left"></span>&nbsp;Revert Third Schedule
                                                             </a>|--%>
                                                            <%-- <a href="LocateEmployeeAddress.htm?empId=${employee.empid}" class="label label-danger" target="_blank">
                                                                <span class="glyphicon glyphicon-circle-arrow-left"></span>&nbsp;Change Address
                                                            </a>--%>
                                                            <c:if test="${username eq 'services1'}">
                                                                <a target="_blank" href="ServiceBookValidatorEmployeeWise.htm?empid=${employee.empid}" class="label label-success">Authenticate e-Service Book</a>|
                                                                <span id="status_blk">
                                                                    <c:if test="${employee.weight eq 'Y'}">
                                                                        <a href="javascript:void(0)" onclick="javascript: updateSBStatus('${employee.empid}', 'N')" class="label label-danger"> Mark Service Book as Incomplete</a>
                                                                    </c:if>
                                                                    <c:if test="${employee.weight eq 'N'}">
                                                                        <a href="javascript:void(0)" onclick="javascript: updateSBStatus('${employee.empid}', 'Y')" class="label label-success"> Mark Service Book as Complete</a>
                                                                    </c:if>
                                                                </span>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${searchEmployee.usertype eq 'A' || searchEmployee.usertype eq 'D'|| searchEmployee.usertype eq 'S'}">
                                                            <%--      | <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#convtContractual" data-href="empUpdateInfo.htm?empid=${employee.empid}"  style="color: white;font-size:8px;" onclick="empMapping('${employee.empid}');">Update Info</button>
                                                            <%--   <c:if test="${employee.isRegular=='Y'}">
                                                                 | <a target="_blank" href="convertRegularToContractualSixYr.htm?empid=${employee.empid}"  class="label label-primary" style="font-size:10px" onclick="return alertMsg();">Regular To Contractual 6 Yr.</a>
                                                             </c:if>--%> 
                                                            <c:if test="${employee.isRegular=='C'}">
                                                                | <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#convtContractual"  style="color: white;font-size:10px;" onclick="empMapping('${employee.empid}');">Contractual 6 Yr. To Contractual</button>
                                                            </c:if>
                                                        </c:if>

                                                        <a href="ServiceBookInValidator.htm?empid=${employee.empid}" class="label label-danger" target="_blank">
                                                            <br/><span class="glyphicon glyphicon-circle-arrow-right"></span>&nbsp;InValidate e-Service Book ;&nbsp
                                                        </a>|
                                                        <a href="ServiceBookInValidatorAllGroupWise.htm?empid=${employee.empid}" class="label label-warning" target="_blank">
                                                            <span class="glyphicon glyphicon-circle-arrow-right"></span>&nbsp;InValidate Group Wise Service Book
                                                        </a>|
                                                        <a href="UnlockEmployeeProfile.htm?empid=${employee.empid}" class="label label-warning" target="_blank">
                                                            <span class="glyphicon glyphicon-circle-arrow-right"></span>&nbsp;Unlock Employee Profile
                                                        </a>
                                                        <a href="#" onclick="revertThirdScheduleCont6Yrs('${employee.empid}')" class="label label-danger">
                                                            <span class="glyphicon glyphicon-circle-arrow-right"></span>&nbsp;Revert Contractual 6 Years Third Schedule
                                                        </a>
                                                    </c:if>
                                                    <%--   <c:if test="${(searchEmployee.usertype eq 'D' && (matchDistCode eq 'N' || employee.district ne loginDistcode))}">
                                                         <a target="_blank" href="viewProfileVerificationControllerDC.htm?empId=${employee.empid}" class="label label-primary">Verify Employee Profile</a>
                                                     </c:if>--%>
                                                    <c:if test="${searchEmployee.usertype ne 'D'}">
                                                        <a target="_blank" href="getRollWiseLinkDC.htm?nodeID=${employee.empid}"  class="label label-danger">Administration</a>|
                                                        <a target="_blank" href="viewProfileVerificationControllerDC.htm?empId=${employee.empid}" class="label label-primary">Verify Employee Profile</a>
                                                        <c:if test="${logintype ne 'DEO'}">
                                                            |<a target="_blank" href="EditEmpoyeeData.htm?empid=${employee.empid}" class="label label-success">Edit</a>|
                                                            <%-- <a href="#" onclick="open_popup('${employee.empid}')" class="label label-danger" id='id2'>Reset Password</a>--%>
                                                            <%-- <a href="#" onclick="revert_thirdschedule('${employee.empid}')" class="label label-success">
                                                                 <span class="glyphicon glyphicon-circle-arrow-left"></span>&nbsp;Revert Third Schedule
                                                             </a>|--%>
                                                            <%--  <a href="LocateEmployeeAddress.htm?empId=${employee.empid}" class="label label-danger" target="_blank">
                                                                  <span class="glyphicon glyphicon-circle-arrow-left"></span>&nbsp;Change Address
                                                              </a>--%>
                                                            <c:if test="${username eq 'services1'}">
                                                                <a target="_blank" href="ServiceBookValidatorEmployeeWise.htm?empid=${employee.empid}" class="label label-success">Authenticate e-Service Book</a>|
                                                                <span id="status_blk">
                                                                    <c:if test="${employee.weight eq 'Y'}">
                                                                        <a href="javascript:void(0)" onclick="javascript: updateSBStatus('${employee.empid}', 'N')" class="label label-danger"> Mark Service Book as Incomplete</a>
                                                                    </c:if>
                                                                    <c:if test="${employee.weight eq 'N'}">
                                                                        <a href="javascript:void(0)" onclick="javascript: updateSBStatus('${employee.empid}', 'Y')" class="label label-success"> Mark Service Book as Complete</a>
                                                                    </c:if>
                                                                </span>
                                                            </c:if>
                                                        </c:if>
                                                        <c:if test="${searchEmployee.usertype eq 'A' || searchEmployee.usertype eq 'D'|| searchEmployee.usertype eq 'S'}">
                                                            <%--      | <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#convtContractual" data-href="empUpdateInfo.htm?empid=${employee.empid}"  style="color: white;font-size:8px;" onclick="empMapping('${employee.empid}');">Update Info</button>
                                                            <%--   <c:if test="${employee.isRegular=='Y'}">
                                                                 | <a target="_blank" href="convertRegularToContractualSixYr.htm?empid=${employee.empid}"  class="label label-primary" style="font-size:10px" onclick="return alertMsg();">Regular To Contractual 6 Yr.</a>
                                                             </c:if>--%> 
                                                            <c:if test="${employee.isRegular=='C'}">
                                                                | <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#convtContractual"  style="color: white;font-size:10px;" onclick="empMapping('${employee.empid}');">Contractual 6 Yr. To Contractual</button>
                                                            </c:if>
                                                        </c:if>

                                                        <a href="ServiceBookInValidator.htm?empid=${employee.empid}" class="label label-danger" target="_blank">
                                                            <br/><span class="glyphicon glyphicon-circle-arrow-right"></span>&nbsp;InValidate e-Service Book ;&nbsp
                                                        </a>|
                                                        <a href="ServiceBookInValidatorAllGroupWise.htm?empid=${employee.empid}" class="label label-warning" target="_blank">
                                                            <span class="glyphicon glyphicon-circle-arrow-right"></span>&nbsp;InValidate Group Wise Service Book
                                                        </a>|
                                                        <a href="UnlockEmployeeProfile.htm?empid=${employee.empid}" class="label label-warning" target="_blank">
                                                            <span class="glyphicon glyphicon-circle-arrow-right"></span>&nbsp;Unlock Employee Profile
                                                        </a>
                                                        <a href="#" onclick="revertThirdScheduleCont6Yrs('${employee.empid}')" class="label label-danger">
                                                            <span class="glyphicon glyphicon-circle-arrow-right"></span>&nbsp;Revert Contractual 6 Years Third Schedule
                                                        </a>
                                                    </c:if>
                                                </td> 

                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="modal fade" id="convtContractual" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->

                                <div class="modal-content modal-lg">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Contractual Employee</h4>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row">
                                            <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                                                <tr>                                           
                                                    <td style="text-align:left; font-size: 12px;padding-left:5px">
                                                        <label>EMPLOYEE CATEGORY:</label>
                                                    </td>                                                        
                                                    <td>
                                                        <form:select path="jobTypeId" id="jobTypeId"  size="1" class="form-control"  style="width:80%;"  >
                                                            <form:option value="">-Select-</form:option>
                                                            <c:forEach items="${conJob}" var="contEmployeeList" >
                                                                <form:option value="${contEmployeeList.jobTypeId}">${contEmployeeList.contJobType}</form:option>
                                                            </c:forEach>
                                                        </form:select>
                                                    </td>  
                                                </tr>
                                                <tr>                                           
                                                    <td style="text-align:left; font-size: 12px;padding-left:5px">
                                                        <form:label path="designation">EMPLOYEE DESIGNATION:</form:label>
                                                        </td>
                                                        <td style="padding-right:2px">
                                                        <form:input path="designation" class="form-control" style="width:80%;"/>                                                     
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </div>
                                    <br>
                                    <div class="modal-footer">
                                        <input type="submit" name="submit" class="btn btn-default" style="width:70px" value="Save" /> 

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal fade" id="empUpdateInfo" role="dialog">
                            <div class="modal-dialog">
                                <!-- Modal content-->

                                <div class="modal-content modal-lg">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                        <h4 class="modal-title">Employee Information</h4>
                                    </div>
                                    <div class="modal-body">
                                        <div class="row">
                                            <table border="0" width="100%"  cellspacing="0" style="font-size:12px; font-family:verdana;">
                                                <tr>                                           
                                                    <td style="text-align:left; font-size: 12px;padding-left:5px">
                                                        <form:label path="basicPay">Basic</form:label>
                                                        </td> 
                                                        <td style="padding-right:2px">
                                                        <form:input path="basicPay" class="form-control" style="width:80%;"/>                                                     
                                                    </td>
                                                    <td>                                                            
                                                    </td>  
                                                </tr>
                                                <tr>                                           
                                                    <td style="text-align:left; font-size: 12px;padding-left:5px">
                                                        <form:label path="gp">Grade Pay</form:label>
                                                        </td>
                                                        <td style="padding-right:2px">
                                                        <form:input path="gp" class="form-control" style="width:80%;"/>                                                     
                                                    </td>
                                                </tr>
                                                <tr>                                           
                                                    <td style="text-align:left; font-size: 12px;padding-left:5px">
                                                        <form:label path="postGrp">Post Group</form:label>
                                                        </td>
                                                        <td style="padding-right:2px">
                                                        <form:input path="postGrp" class="form-control" style="width:80%;"/>                                                     
                                                    </td>
                                                </tr>
                                                <tr>                                           
                                                    <td style="text-align:left; font-size: 12px;padding-left:5px">
                                                        <form:label path="curCadDept">Cadre Controlling Dept</form:label>
                                                        </td>
                                                        <td style="padding-right:2px">
                                                        <form:input path="curCadDept" class="form-control" style="width:80%;"/>                                                     
                                                    </td>
                                                </tr>
                                                <tr>                                           
                                                    <td style="text-align:left; font-size: 12px;padding-left:5px">
                                                        <form:label path="curCadreCode">Current Cadre</form:label>
                                                        </td>
                                                        <td style="padding-right:2px">
                                                        <form:input path="curCadreCode" class="form-control" style="width:80%;"/>                                                     
                                                    </td>
                                                </tr>
                                                <tr>                                           
                                                    <td style="text-align:left; font-size: 12px;padding-left:5px">
                                                        <form:label path="dob">Date Of Birth</form:label>
                                                        </td>
                                                        <td style="padding-right:2px">
                                                        <form:input path="dob" class="form-control" style="width:80%;"/>                                                     
                                                    </td>
                                                </tr>
                                                <tr>                                           
                                                    <td style="text-align:left; font-size: 12px;padding-left:5px">
                                                        <form:label path="payScale">Pay Scale</form:label>
                                                        </td>
                                                        <td style="padding-right:2px">
                                                        <form:input path="payScale" class="form-control" style="width:80%;"/>                                                     
                                                    </td>
                                                </tr>
                                                <tr>                                           
                                                    <td style="text-align:left; font-size: 12px;padding-left:5px">
                                                        <form:label path="depCode">Status</form:label>
                                                        </td>
                                                        <td style="padding-right:2px">
                                                        <form:input path="depCode" class="form-control" style="width:80%;"/>                                                     
                                                    </td>
                                                </tr>                                                
                                            </table>
                                        </div>
                                    </div>
                                    <br>
                                    <div class="modal-footer">
                                        <input type="submit" name="submit" class="btn btn-default" style="width:70px" value="Save" /> 

                                    </div>
                                </div>
                            </div>
                        </div>

                    </form:form>
                </div>
            </div>
            <div class="modal fade" id="myModal" role="dialog">
                <div class="modal-dialog">


                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Employee Basic Profile</h4>
                        </div>
                        <div class="modal-body">

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>

                </div>
            </div>              
    </body>
</html>


