<%-- 
    Document   : AllowancesNew
    Created on : Jun 30, 2021, 1:31:18 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
                $('#ordDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtWEFDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                $('#txtCadreJoiningWEFDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });


            });
            function getDeptWiseOfficeList(type) {
                var deptcode;
                if (type == "N") {
                    deptcode = $('#hidNotifyingDeptCode').val();
                    $('#hidNotifyingOffCode').empty();
                } else if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    $('#hidPostedOffCode').empty();
                }
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                if (type == "N") {
                    $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "N") {
                            $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        }
                    });
                });
            }


            function getOfficeWisePostList(type) {
                var offcode;
                if (type == "N") {
                    offcode = $('#hidNotifyingOffCode').val();
                    $('#notifyingSpc').empty();
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    $('#postedspc').empty();
                }
                var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + offcode;
                if (type == "N") {
                    url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#notifyingSpc').append('<option value="">--Select Post--</option>');
                } else if (type == "P") {
                    $('#postedspc').append('<option value="">--Select Post--</option>');
                }
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "N") {
                            $('#notifyingSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
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
            if ($('#sltCadreDept').val() != "") {
                $('#sltCadre').empty();
                var url = 'getCadreListJSON.htm?deptcode=' + $('#sltCadreDept').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltCadre').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                }).done(function(result) {
                    $('#sltCadre').val($('#hidTempCadre').val());
                    $('#sltGrade').empty();
                    var url = 'getGradeListCadreWiseJSON.htm?cadreCode=' + $('#sltCadre').val();
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#sltGrade').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    }).done(function(result) {
                        $('#sltGrade').val($('#hidTempGrade').val());
                        if ($('#sltCadreLevel').val() != '') {
                            $('#sltDescription').empty();
                            var url = 'getDescriptionJSON.htm?cadreLevel=' + $('#sltCadreLevel').val();
                            $.getJSON(url, function(data) {
                                $.each(data, function(i, obj) {
                                    $('#sltDescription').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                                });
                            }).done(function(result) {
                                $('#sltDescription').val($('#hidTempDescription').val());
                            });
                        }
                    });
                });
            }

            function getPost(type) {
                if (type == "N") {
                    $('#notifyingPostName').val($('#notifyingSpc option:selected').text());
                } else if (type == "P") {
                    $('#postedspn').val($('#postedspc option:selected').text());
                }
            }

            function onlyIntegerRange(e) {
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
                if ($('#ordno').val() == "") {
                    alert("Please enter Order No");
                    $('#ordno').focus();
                    return false;
                }
                if ($('#ordDate').val() == "") {
                    alert("Please enter Order Date");
                    return false;
                }
                if ($('#postedspn').val() == "") {
                    alert("Please select Details of Posting");
                    return false;
                }
                if ($('#notifyingPostName').val() == "") {
                    alert("Please enter Details of Notifying Authority");
                    return false;
                }
                if ($('#allowanceDesc').val() == "") {
                    alert("Please enter Allowance/Deduction Type");
                    return false;
                }

                return true;
            }

            function getPostCodeDeptWise() {
                $('#sltGenericPost').empty();
                var url = 'getDeptWisePostListJSON.htm?deptCode=' + $('#sltPostingDept').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltGenericPost').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                    });
                });
            }

            function getDeptWiseCadre() {
                $('#sltCadre').empty();
                var url = 'getCadreListJSON.htm?deptcode=' + $('#sltCadreDept').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltCadre').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }
            function getCadreWiseGrade() {
                $('#sltGrade').empty();
                var url = 'getGradeListCadreWiseJSON.htm?cadreCode=' + $('#sltCadre').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltGrade').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }
            function getDescription() {
                $('#sltDescription').empty();
                alert($('#sltCadreLevel').val());
                var url = 'getDescriptionJSON.htm?cadreLevel=' + $('#sltCadreLevel').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltDescription').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }
        </script>
    </head>
    <body>
        <form:form action="saveAllowance.htm" method="POST" commandName="AllowanceModel">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        New Allowance
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="hnotid" id="hnotid"/>
                        <form:hidden path="hCadId" id="hCadId"/>                     
                        <form:hidden path="hidAllowDedDesc" id="hidAllowDedDesc"/>



                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label for="chkNotSBPrint" >Check Not to Print in Service Book</label>
                            </div>
                            <div class="col-lg-3">   
                                <form:checkbox path="chkNotSBPrint" value="Y" class="form-control"/>
                            </div>
                            <div class="col-lg-3"></div>
                            <div class="col-lg-2"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtNotOrdNo">Notification Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="ordno" id="ordno"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="ordDate">Notification Order Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="ordDate" id="ordDate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="notifyingPostName">Details of Notifying Authority<span style="color: red">*</span></label>
                            </div>
                            <%--<div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOO" onclick="toggleOrganisationType()" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOI" onclick="toggleOrganisationType()" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>--%>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="notifyingPostName" id="notifyingPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#promotionAuthorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>
                    </div>


                    <br>
                    <div style=" margin-bottom: 5px;" class="panel panel-info">
                        <div class="panel-heading">Allowances / Deductions </div>
                        <div class="panel-body">
                            <br>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="allowance">Allowances/Deductions :<span style="color: red">*</span></label>
                                <div class="col-sm-3">

                                    <form:select path="sltAllowanceCode" id="allowanceId" class="form-control">
                                        <form:option value="">--Select--</form:option>
                                        <form:options items="${allowList}" itemLabel="allowanceDesc" itemValue="allowanceId"/>
                                    </form:select>                            
                                </div>

                                <label class="control-label col-sm-2" for="allowance">WEF Date :</label>
                                <div class="col-sm-3">
                                    <div class="input-group date" id="wefdate">
                                        <form:input class="form-control" path="txtWEFDt" id="txtWEFDt" readonly="true"/>
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>                                
                                </div>
                            </div>
                            <br><br>
                            <div class="form-group">
                                <label class="control-label col-sm-2" for="allowance">Amount :<span style="color: red">*</span></label>
                                <div class="col-sm-3">
                                    <form:input path="allowanceAmt" class="form-control"/>
                                </div>


                                <label class="control-label col-sm-2" for="allowance">Time :</label>
                                <div class="col-sm-3">
                                    <form:select path="sltWEFTime" id="sltWEFTime" class="form-control">
                                        <option value="">-Select-</option>
                                        <form:option value="FN">Fore Noon</form:option>
                                        <form:option value="AN">After Noon</form:option>
                                    </form:select>
                                </div>
                            </div>
                            <br> <br>
                            <div class="row" style="margin-bottom: 7px;">                               
                                <label class="control-label col-lg-2" for="note">Note(if any)</label>
                                <div class="col-lg-9">
                                    <form:textarea class="form-control" path="note" id="note"/>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <br>
                        </div>
                    </div>

                    <%--<div class="col-lg-9">
                        <table class="table table-bordered" >
                            <thead class="bg-primary text-white" style="text-align:center">
                                <tr>
                                    <th>#</th>
                                    <th style="text-align:center">Allowances/Deduction</th>
                                    <th style="text-align:center" >Amount</th>
                                    <th style="text-align:center">WEF Date</th>
                                    <th style="text-align:center">Time</th>
                                </tr>
                            </thead>
                        </table>
                    </div>--%>

                </div>

            </div>
            <div class="panel-footer ">
                <input type="submit" name="action" value="Save" class="btn btn-primary" onclick="return saveCheck();" />
                <input type="submit" name="action" value="BackToList" class="btn btn-primary"/>
            </div>
        </div>


        <div id="promotionAuthorityModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Notifying Authority</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltDept">Department</label>
                            </div>
                            <div class="col-lg-9">
                                <form:select path="hidNotifyingDeptCode" id="hidNotifyingDeptCode" class="form-control" onchange="getDeptWiseOfficeList('N');">
                                    <form:option value="">--Select--</form:option>
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
                                <form:select path="hidNotifyingOffCode" id="hidNotifyingOffCode" class="form-control" onchange="getOfficeWisePostList('N');">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${notoffList}" itemValue="offCode" itemLabel="offName"/>
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
                                <form:select path="notifyingSpc" id="notifyingSpc" class="form-control" onchange="getPost('N');">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${notSpc}" itemValue="spc" itemLabel="postname"/>
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

