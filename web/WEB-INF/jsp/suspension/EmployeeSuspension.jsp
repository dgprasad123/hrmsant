<%-- 
    Document   : EmployeeSuspension
    Created on : 4 Jul, 2018, 11:15:43 AM
    Author     : Surendra
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">


            function getDeptWiseOfficeList(type) {
                var deptcode;

                deptcode = $('#sltDept').val();
                $('#sltOffice').empty();
                $('#spc').empty();

                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                $('#sltOffice').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltOffice').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getDeptWiseHQOfficeList(type) {
                var deptcode;

                deptcode = $('#slthqDeaprtment').val();
                $('#slthqoffice').empty();


                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $('#slthqoffice').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#slthqoffice').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode;
                offcode = $('#sltOffice').val();
                $('#spc').empty();
                var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#spc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                    });
                });


                $('#hidTempAuthOffice').val($('#sltOffice').val());
            }

            function getPost() {
                $('#hidTempAuthSpc').val($('#spc').val());
                $('#sltAuth').val($('#spc option:selected').text());



            }
            function validateForm() {
                
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                    return false;
                }
                
                if ($("#ordno").val() == '') {
                    alert('Please enter Order Number.');
                    $("#ordno").focus();
                    return false;
                }
                if ($("#ordDate").val() == '') {
                    alert('Please enter Order Date.');
                    $("#ordDate").focus();
                    return false;
                }

                if ($("#hidTempAuthSpc").val() == '') {
                    alert('Please Select Authority.');
                    $("#hidTempAuthSpc").focus();
                    return false;
                }

                if ($("#wefdate").val() == '') {
                    alert('Please enter with effect from date.');
                    $("#wefdate").focus();
                    return false;
                }

                if ($("#sltweftime").val() == '') {
                    alert('Please select Time.');
                    $("#sltweftime").focus();
                    return false;
                }

                var x = $("#txtallowance").val();

                if ($("#txtallowance").val() == '' || (isNaN(x) || x < 1)) {
                    alert('Subsistence Allowance not a valid Amount');
                    $("#txtallowance").focus();
                    return false;
                }

            }


            function isDate(date, msg) {
                // dd-mmm-yyyy format

                if (date.length == 0) {
                    return true; // Ignore null value
                }
                if (date.length == 10) {
                    date = "0" + date; // Add a leading zero
                }
                if (date.length != 11) {
                    alert(msg);
                    return false;
                }
                day = date.substring(0, 2);
                month = date.substring(3, 6).toUpperCase();
                year = date.substring(7, 11);
                if (isNaN(day) || (day < 0) || isNaN(year) || (year < 1)) {
                    alert(msg);
                    return false;
                }

                // Ensure valid month and set maximum days for that month...
                if ((month == "JAN") || (month == "jan") || (month == "MAR") || (month == "mar") || (month == "MAY") || (month == "may") ||
                        (month == "JUL") || (month == "jul") || (month == "AUG") || (month == "aug") || (month == "OCT") || (month == "oct") ||
                        (month == "DEC")) {
                    monthdays = 31
                }
                else if ((month == "APR") || (month == "apr") || (month == "JUN") || (month == "jun") || (month == "sep") || (month == "SEP") ||
                        (month == "NOV") || (month == "nov")) {
                    monthdays = 30
                }
                else if ((month == "FEB") || (month == "feb")) {
                    monthdays = ((year % 4) == 0) ? 29 : 28;
                }
                else {
                    alert(msg);
                    return false;
                }
                if (day > monthdays) {
                    alert(msg);
                    return false;
                }
                return true;
            }


        </script>
    </head>
    <body>
        <form:form action="addSuspensionController.htm" method="POST" commandName="command">
            <div class="container-fluid">
                <form:hidden path="suspensionId" />
                <form:hidden path="empid"/>
                <form:hidden path="notId"/>

                <form:hidden id="hidTempAuthOffice" path="hidTempAuthOffice"/>
                <form:hidden id="hidTempAuthSpc" path="hidTempAuthSpc"/>
                <form:hidden id="hidTempHeadQuarterOffice" path="hidTempHeadQuarterOffice"/>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        HRMS ID: ${command.empid}
                    </div>
                    <div class="panel-body">
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

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label for="rdTransaction">Select type of Transaction</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:radiobutton path="rdTransaction" value="S"/> Service Book Entry(Backlog)
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="rdTransaction" value="C"/> Current Transaction(will effect current Pay or Post)
                            </div>
                            <div class="col-lg-2"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="ordno">Suspension Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="ordno" id="ordno"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="ordDate"> Suspension Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="ordDate" id="ordDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                            <div class="col-lg-3"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="sltAuth">Sanctioning Authority</label>
                            </div>
                            <div class="col-lg-8">
                                <form:input class="form-control" path="sltAuth" id="sltAuth" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#suspensionAuthorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="wefdate">With effect from Date <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="wefdate" id="wefdate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div> 
                            </div>
                            <div class="col-lg-2">
                                <label for="sltweftime"> Time <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltweftime" class="form-control">
                                    <form:option value="">--Select One--</form:option>
                                    <form:option value="FN">FORE NOON</form:option>
                                    <form:option value="AN">AFTER NOON</form:option>
                                </form:select>                             
                            </div>
                            <div class="col-lg-3"></div>
                        </div>  

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="txtallowance">Subsistence Allowance in Rs</label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtallowance" id="txtallowance"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="cadreStatus"> Update Cadre Status(LR) </label>
                            </div>
                            <div class="col-lg-2">
                                <form:checkbox path="cadreStatus" class="form-control" value="LR"/>
                            </div>
                            <div class="col-lg-3"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-12">
                                <label for="note">Provide Head Quarter Information </label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="slthqDeaprtment">Select Department</label>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="slthqDeaprtment" id="slthqDeaprtment" class="form-control" onchange="getDeptWiseHQOfficeList()">
                                    <option value="">--Select Department--</option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>                   
                            </div>
                            <div class="col-lg-1"></div>
                        </div>    

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="slthqoffice">Select Office</label>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="slthqoffice" id="slthqoffice" class="form-control">
                                    <option value="">--Select Office--</option>
                                    <form:options items="${hqOfflist}" itemValue="offCode" itemLabel="offName"/>
                                </form:select>                      
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="note">Note if any</label>
                            </div>
                            <div class="col-lg-8">
                                <form:textarea class="form-control" path="note" id="note" cols="100" rows="4"/>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>    
                    </div>     

                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save" class="btn btn-default" onclick="return validateForm()"/>
                        <c:if test="${not empty command.suspensionId}">
                            <input type="submit" name="action" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to Delete Suspension Data?')"/>
                        </c:if>
                        <input type="submit" name="action" value="Back" class="btn btn-default"/>
                    </div>
                </div>
            </div>

            <div id="suspensionAuthorityModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Sanctioning Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sltDept" id="sltDept" class="form-control" onchange="getDeptWiseOfficeList()">
                                        <option value="">--Select Department--</option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltOffice">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="sltOffice" id="sltOffice" class="form-control" onchange="getOfficeWisePostList()">
                                        <option value="">--Select Office--</option>
                                        <form:options items="${offList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltAuth23">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="spc" id="spc" class="form-control" onchange="getPost()">
                                        <option value="">--Select Post--</option>
                                        <form:options items="${postlist}" itemValue="spc" itemLabel="postname"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1"></div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
        <script type="text/javascript">
            $(function() {
                $('#ordDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#wefdate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
        </script>
    </body>
</html>
