<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            $(document).ready(function () {
                $("#deptName").change(function () {
                    $('#offcode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function (result) {
                        $.each(result, function (i, field) {
                            $('#offcode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));

                        });
                    });
                });
                $('.openPopup').on('click', function () {
                    var dataURL = $(this).attr('data-href');
                    $('.modal-body').load(dataURL, function () {
                        $('#myModal').modal({show: true});
                    });
                });

            });
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <form:form action="LocateEmployeeAdmin.htm" commandName="searchEmployee" method="post" autocomplete="off">
                    <input autocomplete="false" name="hidden" type="text" style="display:none;">
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
                                <form:select class="form-control" path="offcode">
                                    <form:option value="" label="Select"/>
                                    <form:options items="${officeList}" itemLabel="offName" itemValue="offCode"/>  
                                </form:select>
                            </div>
                        </div>
                        
                        <div class="row">
                            <label class="control-label col-sm-3">Year</label>
                            <div class="col-sm-3">
                                <form:select class="form-control" path="year">
                                    <option value="0">Select</option>
                                    <option value="2016">2016</option>
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
                                </form:select>
                            </div>
                            <label class="control-label col-sm-3" >Search String</label>
                            <div class="col-sm-3" >
                                <form:input path="searchString" class="form-control"/>
                            </div>
                        </div>
                    </div> 
                    <input type="submit" value="Search"/>
                    <div class="panel-footer">
                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th width="10%">Hrms Id</th>
                                            <th width="25%">Employee Name</th>
                                            <th width="10%">Date of Birth</th>
                                            <th width="5%">Edit</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${empSearchResult.employeeList}" var="employee">
                                            <tr>
                                                <td>${employee.empid}</td>
                                                <td>${employee.fname} ${employee.mname} ${employee.lname}</td>
                                                <td>${employee.dob}</td>                                               
                                                
                                                <td>
                                                   
                                                    <a target="_blank" href="EditEmpoyeeDataAdmin.htm?empid=${employee.empid}" class="label label-success">Edit</a>|
                                                    
                                                    
                                                </td>
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
            <script>
                    function open_popup(ids){
                       var con=confirm("Do you want to Reset the password");
                       if(con){
                          var URL="ResetEmpoyeePassword.htm?empid="+ids;
                          window.open (URL,"mywindow","width=500,height=400,left=50px,top=50px,titlebar=0,status=0,menubar=0");
                       }
             
                    }
            </script>    
    </body>
</html>


