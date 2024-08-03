<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
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
            $(document).ready(function() {
                $('#txtNotOrdDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtWEFDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
            function getDeptWiseOfficeList(type) {
                var deptcode;
                if (type == "A") {
                    deptcode = $('#hidAuthDeptCode').val();
                    $('#hidAuthOffCode').empty();
                } else if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    $('#hidPostedOffCode').empty();
                }
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
                if (type == "A") {
                    $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode;
                if (type == "A") {
                    offcode = $('#hidAuthOffCode').val();
                    $('#authSpc').empty();
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    $('#postedspc').empty();
                }
                var url = 'getOfficeWithSPCList.htm?offcode=' + offcode;
                if (type == "A") {
                    url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#authSpc').append('<option value="">--Select Post--</option>');
                } else if (type == "P") {
                    $('#postedspc').append('<option value="">--Select Post--</option>');
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#authSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        } else if (type == "P") {
                            $('#postedspc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        }
                    });
                });

                //Start Field Off Code
                if (type == "P") {
                    $('#sltPostedFieldOff').empty();
                    var url = 'transferGetFieldOffListJSON.htm?offcode=' + $('#hidPostedOffCode').val();
                    $('#sltPostedFieldOff').append('<option value="">--Select--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#sltPostedFieldOff').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
                //End Field Off Code
            }

            function getPost(type) {
                if (type == "A") {
                    $('#authPostName').val($('#authSpc option:selected').text());
                } else if (type == "P") {
                    $('#postedspn').val($('#postedspc option:selected').text());
                }
            }

            function onlyIntegerRange(e){
                var browser = navigator.appName;
                if (browser == "Netscape") {
                    var keycode = e.which;
                    if ((keycode >= 48 && keycode <= 57) || keycode == 8 || keycode == 0)
                        return true;
                    else
                        return false;
                } else {
                    if ((e.keyCode >= 48 && e.keyCode <= 57) || e.keycode == 8 || e.keycode == 0)
                        e.returnValue = true;
                    else
                        e.returnValue = false;
                }
            }

            function saveCheck() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                    return false;
                }
                
                if ($('#txtNotOrdNo').val() == "") {
                    alert("Please enter Order No");
                    $('#txtNotOrdNo').focus();
                    return false;
                }
                if ($('#txtNotOrdDt').val() == "") {
                    alert("Please enter Order Date");
                    return false;
                }
                if ($('#postedspn').val() == "") {
                    alert("Please select Details of Posting");
                    return false;
                }
                return true;
            }
        </script>
    </head>
    <body>
        <form:form action="saveRedesignationData.htm" method="post" commandName="redesignationForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Redesignation
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="hnotid" id="hnotid"/>
                        <form:hidden path="htrid" id="htrid"/>
                        <form:hidden path="hpayid" id="hpayid"/>
                        
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
                                <form:radiobutton path="rdTransaction" value="S" onclick="removePostingData();"/> Service Book Entry(Backlog)
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="rdTransaction" value="C" onclick="removePostingData();"/> Current Transaction(will effect current Pay or Post)
                            </div>
                            <div class="col-lg-2"></div>
                        </div>
                            
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">Notification Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="txtNotOrdNo" id="txtNotOrdNo"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt"> Notification Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="txtNotOrdDt" id="txtNotOrdDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="spc">Sanctioning Authority</label>
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="authPostName" id="authPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#redesignationAuthorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="postedspn">Details of Redesignation<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="postedPostName" id="postedspn" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#redesignationPostingModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltPostedFieldOff">(a) Field Office:</label>
                            </div>
                            <div class="col-lg-9">
                                <form:select path="sltPostedFieldOff" id="sltPostedFieldOff" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${fieldofflist}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" onclick="searchAuthority()">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label>Details of Pay</label>
                            </div>
                            <div class="col-lg-9">

                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltPayScale">(a) Scale of Pay/Pay Band</label>
                            </div>
                            <div class="col-lg-5">
                                <form:select path="sltPayScale" id="sltPayScale" class="form-control">
                                    <option value="">--Select--</option>
                                    <form:options items="${payscalelist}" itemValue="payscale" itemLabel="payscale"/>
                                </form:select>
                            </div>
                            <div class="col-lg-5">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtGP">(b) Grade Pay<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtGP" id="txtGP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtBasic">(c) Pay in Substantive Post<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtBasic" id="txtBasic" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtSP">(d) Special Pay</label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtSP" id="txtSP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtPP">(e) Personal Pay</label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPP" id="txtPP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtOP">(f) Other Pay</label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtOP" id="txtOP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtDescOP">(g) Description of Other Pay</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="txtDescOP" id="txtDescOP"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtWEFDt">Date of Effect of Pay<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="txtWEFDt" id="txtWEFDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="sltWEFTime">Time<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltWEFTime" id="sltWEFTime" class="form-control">
                                    <option value="">-Select-</option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="note">Note(if any)</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea class="form-control" path="note" id="note"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="action" value="Save Redesignation" class="btn btn-default" onclick="return saveCheck();"/>
                        <c:if test="${not empty redesignationForm.hnotid}">
                            <input type="submit" name="action" value="Delete" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?')"/>
                        </c:if>
                        <a href="RedesignationList.htm"><input type="button" name="action" value="Back to List Page" class="btn btn-default"/></a>
                    </div>
                </div>
            </div>

            <div id="redesignationAuthorityModal" class="modal" role="dialog">
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
                                    <form:select path="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="getDeptWiseOfficeList('A');">
                                        <option value="">--Select Department--</option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWisePostList('A');">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${sancOfflist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authSpc" id="authSpc" class="form-control" onchange="getPost('A');">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${sancPostlist}" itemValue="spc" itemLabel="postname"/>
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

            <div id="redesignationPostingModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Details of Posting</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="sltDept">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDeptCode" id="hidPostedDeptCode" class="form-control" onchange="getDeptWiseOfficeList('P');">
                                        <option value="">Select Department</option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOffCode" id="hidPostedOffCode" class="form-control" onchange="getOfficeWisePostList('P');">
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${postedOfflist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="note">Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="postedspc" id="postedspc" class="form-control" onchange="getPost('P');">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${postedPostlist}" itemValue="spc" itemLabel="postname"/>
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
