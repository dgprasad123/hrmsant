<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/common.js" type="text/javascript"></script>
        <script type="text/javascript">
            function showNewEmployeeWindow() {
                $("#myModal").modal('show');
            }
            function viewEmployeeData(empid) {
                url = "getCadreEmpData.htm?empid=" + empid;             
                clearform("myModal");
                $.getJSON(url, function (data) {
                    $("#myModal").find('input:text, input:checkbox').each(function () {
                        var temp = $(this).attr("id");
                     //   alert(temp);
                        $(this).val(data[temp]);
                    });
                    //fname mname lname
                });
                $("#myModal").modal('show');
            }
            function loadCombo() {
                for (i = 1970; i < 2016; i++) {
                    $('#cardeallotmentyear').append($('<option>', {value: i, text: i}));
                }
            }
            function loadCombo(url, comboid, combotext, combovalue) {
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#' + comboid).append($('<option>').text(obj[combotext]).attr('value', obj[combovalue]));
                    });
                });
            }
            $(document).ready(function () {
                loadCombo();
                loadCombo("getStateListJSON.htm", "homestate", "statename", "statecode");
            });
        </script>
    </head>
    <body>
        <div id="page-wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div class="container-fluid">
                <!-- Page Heading -->

                <div class="row">
                    <div class="col-lg-12">
                        <h2>IAS List</h2>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Sl No</th>                                            
                                        <th>Name</th>
                                        <th>Id No</th>
                                        <th>Recruitment Source</th>
                                        <th>Allotment Year</th>
                                        <th>Post Position</th>
                                        <th>Station</th>
                                        <th>Additional Charge</th>
                                        <th>Remark</th>
                                        <th>Pay Scale</th>

                                    </tr>
                                </thead>
                                <tbody>                                        
                                    <c:forEach items="${employees}" var="employee" varStatus="counter">
                                        <tr>
                                            <td> ${counter.count}</td>                                                
                                            <td>${employee.fname} ${employee.mname} ${employee.lname}</td>     
                                            <td>${employee.cadreId}</td>
                                            <td>${employee.recsource}</td>
                                            <td>${employee.cardeallotmentyear}</td>
                                            <td>${employee.spn}</td>
                                            <td>${employee.station}</td>
                                            <td>${employee.addlCharge}</td>
                                            <td>${employee.remark}</td>
                                            <td>${employee.payScale}</td>
                                            <td>
                                                <!--
                                                <a href='javascript:viewEmployeeData("")'>Edit</a>
                                                -->
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:900px;">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">New Employee</h4>
                    </div>
                    <div class="modal-body" style="height:450px;">

                        <div class="form-inline">
                            <label class="control-label col-sm-4">Name:</label>
                            <div class="col-sm-8">
                                <input type="text" class="form-control"  name="fname" placeholder="First Name" id="fname" />
                                <input type="text" class="form-control"  name="mname" placeholder="Middle Name" id="mname" />
                                <input type="text" class="form-control"  name="lname" placeholder="Last Name" id="lname" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">ID Number:</label>
                            <div class="col-sm-8">
                                <input type="text" id="idmark" name="idmark" size="20" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Allotment Year:</label>
                            <div class="col-sm-8">
                                <select class="form-control"  name="cardeallotmentyear" id="cardeallotmentyear" maxlength="4">                                    
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Recruitment Source:</label>
                            <div class="col-sm-8">
                                <select class="form-control"  name="recryear" id="recryear" >
                                    <option>R.R.</option>
                                    <option>SCS</option>
                                    <option>NSCS</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">DOJ to the Service:</label>
                            <div class="col-sm-8">
                                <input type="date" name="dojg" class="form-control" placeholder="Date of Joining to the Service"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Home State:</label>
                            <div class="col-sm-8">
                                <select class="form-control"  name="homestate" id="homestate" >

                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Home District:</label>
                            <div class="col-sm-8">
                                <select class="form-control"  name="homedist" id="homedist" >
                                    <option>R.R.</option>
                                    <option>SCS</option>
                                    <option>NSCS</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Address:</label>
                            <div class="col-sm-8">
                                <textarea class="form-control" name="permanentaddr" id="permanentaddr"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Current Position:</label>
                            <div class="col-sm-8">
                                <input type="text" id="spn" name="spn" size="20" readonly="true" maxlength="100" class="form-control"/>  
                                <button type="button" class="btn btn-primary">Change</button></div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">DOJ to Current Position:</label>
                            <div class="col-sm-8">
                                <input type="date" name="dateOfCurPosting" id="dateOfCurPosting" class="form-control" placeholder="Date of Joining to Current Position"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-4">Pay Scale:</label>
                            <div class="col-sm-8"><input type="text" id="payScale" name="payScale" size="20" maxlength="100" class="form-control"/></div>
                        </div>

                    </div>
                    <div class="modal-footer">
                        <span id="msg"></span>
                        <button type="button" class="btn btn-default">Save</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                    <div>&nbsp;</div>
                </div>
            </div>
        </div>
    </body>
</html>
