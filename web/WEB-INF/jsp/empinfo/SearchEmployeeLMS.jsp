<%-- 
    Document   : SearchEmployeeLMS
    Created on : Feb 24, 2020, 12:53:50 PM
    Author     : Madhusmita
--%>

<%@ page contentType="text/html;charset=windows-1252" session="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
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
            $(document).ready(function () {
                $("#deptName").change(function () {
                    $('#offcode').empty();
                    $('#offcode').append($('<option>', {
                        value: "",
                        text: "--Select--"
                    }));
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;

                    $.getJSON(url, function (result) {
                        $.each(result, function (i, field) {
                            $('#offcode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));

                        });
                    });
                    //alert($('#hidUserype').val());
                });
                $('.openPopup').on('click', function () {
                    var dataURL = $(this).attr('data-href');
                    $('.modal-body').load(dataURL, function () {
                        $('#myModal').modal({show: true});
                    });
                });
                
                $('#txtDOB').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

            });

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
            function validateSearch(){
                if($("#dob").val() != ''){
                    if($("#criteria").val() == ''){
                        alert("Please select Search Criteria!");
                        return false;
                    }
                }
            }
            
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="LocateEmployeeLMS.htm" commandName="searchEmployee" method="post" autocomplete="off">
                    <form:hidden path="usertype" id="hidUserype"/>
                    <form:hidden path="hidEmpid" id="hidEmpid"/>                    
                    <div class="container-fluid">
                        <div class="row">
                            <label class="control-label col-sm-3">Department Name</label>
                            <div class="col-sm-3" style="width:60%;">
                                <form:select class="form-control" path="deptName">
                                    <form:option value="" label="Select"/>
                                    <form:options items="${departmentList}" itemLabel="deptName" itemValue="deptCode"/>                                        
                                </form:select>
                            </div>
                        </div><br/>

                        <div class="row">
                            <label class="control-label col-sm-3">Office Name</label>
                            <div class="col-sm-3" style="width:60%;">
                                <form:select class="form-control" path="offcode">
                                    <form:option value="" label="Select"/>
                                    <form:options items="${officeList}" itemLabel="offName" itemValue="offCode"/>  
                                </form:select>
                            </div>
                        </div>
                        <br/>

                        <div class="row">
                            <label class="control-label col-sm-3">Search criteria</label>
                            <div class="col-sm-3">
                                <form:select class="form-control" path="criteria">
                                    <form:option value="">select</form:option>
                                    <form:option value="GPFNO">GPF NO</form:option>
                                    <form:option value="HRMSID">HRMS ID</form:option>
                                    <form:option value="FNAME">FIRST NAME</form:option>
                                    <form:option value="LNAME">LAST NAME</form:option>
                                    <form:option value="MOBILE">Mobile Number</form:option>
                                    <form:option value="QRNO">QUARTER NO</form:option>
                                </form:select>
                            </div>
                            <label class="control-label col-sm-2" >Search String</label>
                            <div class="col-sm-2" >
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
                    <input type="submit" size="20px" value="Search" onclick="return validateSearch();"/>
                    <div class="panel-footer">
                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="10%">Hrms Id</th>
                                            <th width="18%">Employee Name</th>
                                            <th width="8%">Date of Birth</th>
                                            <th width="3%">View</th>
                                            <th width="3%">View Employee Profile</th>
                                            <th width="8%">View Service Book</th>
                                                <c:if test="${searchEmployee.usertype=='A' && (searchEmployee.username eq 'employee_hrms_view' || searchEmployee.username eq 'vigilanceadmin')}">
                                                <th width="8%">View Pay Slip</th>
                                                </c:if>
                                                <%--<c:if test="${searchEmployee.usertype=='A'}">
                                                <th width="10%">Status</th>
                                                </c:if>--%>
                                                <%-- <th width="32%">Action</th>--%>
                                                <th></th>
                                                <th>NEW REPORT</th>
                                                
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${empSearchResult.employeeList}" var="employee">
                                            <tr>
                                                <td>${employee.empid}</td>
                                                <td>${employee.fname} ${employee.mname} ${employee.lname}</td>
                                                <td>${employee.dob}</td>                                               
                                                <td><a href="javascript:void(0);" data-href="EmployeeBasicProfileLMS.htm?empid=${employee.empid}" class="openPopup label label-success">View</a></td>
                                                <td><a target="_blank" href="viewProfileVerificationControllerDC.htm?empId=${employee.empid}" class="label label-primary">Verify Employee Profile</a></td>
                                                <td><a target="_blank" href="PoliceServiceBook.htm?empid=${employee.empid}" class="label label-primary">Service Book</a></td>
                                                <c:if test="${searchEmployee.usertype=='A' && (searchEmployee.username eq 'employee_hrms_view' || searchEmployee.username eq 'vigilancesp' || searchEmployee.username eq 'vigilancedirectorate') }">
                                                    <td><a target="_blank" href="PaySlipList.htm?empId=${employee.empid}" class="label label-primary">Pay Slip</a></td>
                                                </c:if>
                                                <td><a href="empProfileDetailsNew.htm?empId=${employee.empid}" class="label label-danger" target="_blank">Employee Detail Report</a></td>
                                            </tr>
                                        </c:forEach>

                                    </tbody>
                                </table>
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