<%-- 
    Document   : LongTermTrainingScheduleData
    Created on : Sep 4, 2021, 12:46:05 PM
    Author     : Madhusmita
--%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Long Term Training</title>
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
            function getDistWiseOfficeList(type) {
                var deptcode;
                var distcode;
                if (type == "A") {
                    deptcode = $('#hidAuthDeptCode').val();
                    distcode = $('#hidAuthDistCode').val();
                    $('#hidAuthOffCode').empty();
                    $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    distcode = $('#hidPostedDistrictCode').val();
                    $('#hidPostedOffCode').empty();
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                }
                var url = "";
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getOfficeListDistrictAndDepartmentForBacklogEntryJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                } else {
                    url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
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

            function getOfficeWiseGenericPostList(type) {
                var offcode;
                var url;
                if (type == "A") {
                    offcode = $('#hidAuthOffCode').val();
                    $('#genericpostAuth').empty();
                    $('#genericpostAuth').append('<option value="">--Select Generic Post--</option>');
                    url = "getAuthorityPostListJSON.htm?offcode=" + offcode;
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    $('#genericpostPosted').empty();
                    $('#genericpostPosted').append('<option value="">--Select Generic Post--</option>');
                    url = "getPostCodeListJSON.htm?offcode=" + offcode;
                }
                //var url = 'getAuthorityPostListJSON.htm?offcode=' + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#genericpostAuth').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        } else if (type == "P") {
                            $('#genericpostPosted').append('<option value="' + obj.value + '">' + obj.label + '</option>');
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
            function populateNoOfDays() {
                var x = $('#complDate').val().split("-");
                var y = $('#startDate').val().split("-");
                var month1 = convertMonthNameToNumber(x[1]);
                var month2 = convertMonthNameToNumber(y[1]);
                var date1 = new Date(x[2], month1, x[0]);
                var date2 = new Date(y[2], month2, y[0]);
                if (date1 && date2) {
                    var datediff = days_between(date1, date2);
                    $('#txttotday').val(datediff);
                }
            }
            function convertMonthNameToNumber(monthName) {
                var myDate = new Date(monthName + " 1, 2000");
                var monthDigit = myDate.getMonth();
                return isNaN(monthDigit) ? 0 : (monthDigit + 1);
            }
            function days_between(date1, date2) {
                var ONE_DAY = 1000 * 60 * 60 * 24;
                var date1_ms = date1.getTime();
                var date2_ms = date2.getTime();
                var difference_ms = Math.abs(date1_ms - date2_ms);
                return Math.round((difference_ms / ONE_DAY) + 1);
            }
            function saveCheck() {

                if ($('#slttraintype').val() == "") {
                    alert("Please select Training Type");
                    return false;
                }
                if ($('#slttitle').val() == "") {
                    alert("Please select Training Title");
                    return false;
                }
                if ($('input[type="radio"][name="underwent"]:checked').length == 0) {
                    alert("Please select Allowed or Underwent");
                    return false;
                }
                if ($('#startDate').val() == "") {
                    alert("Please enter From Date");
                    $('#txtNotOrdNo').focus();
                    return false;
                }
                if ($('#complDate').val() == "") {
                    alert("Please enter To Date");
                    return false;
                }
                if ($('#txtDays').val() == "") {
                    alert("Please enter No of Days");
                    $('#txttotday').focus();
                    return false;
                }

                if ($('input[type="radio"][name="rdTransaction"]:checked').length == 0) {
                    alert("Please select Transaction Type");
                    return false;
                }
                if ($('input[type="radio"][name="rdTrainingauthtype"]:checked').length == 0) {
                    alert("Please select Authority Type");
                    return false;
                }
                return true;
            }
            function getGenericPostWiseSPCList(type) {
                var offcode;
                var gpc;
                var url;
                if (type == "A") {
                    offcode = $('#hidAuthOffCode').val();
                    gpc = $('#genericpostAuth').val();
                    url = "getAuthoritySubstantivePostListJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                    $('#authSpc').empty();
                    $('#authSpc').append('<option value="">--Select Substantive Post--</option>');
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    gpc = $('#genericpostPosted').val();
                    if ($("input[name=rdTransaction]:checked").val() == "S") {
                        url = "getSubstantivePostListBacklogEntryJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                    } else if ($("input[name=rdTransaction]:checked").val() == "C") {
                        url = "getVacantSubstantivePostListJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                    }
                    $('#postedspc').empty();
                    $('#postedspc').append('<option value="">----Select Substantive Post----</option>');
                }

                //var url = "getVacantPostListJSON.htm?postcode="+gpc+"&offcode="+offcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "A") {
                            $('#authSpc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                        } else if (type == "P") {
                            $('#postedspc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                        }
                    });
                });
            }
            function removeDepedentDropdown(type) {
                if (type == "A") {
                    $('#hidAuthDistCode').val('');
                    $('#hidAuthOffCode').empty();
                    $('#genericpostAuth').empty();
                    $('#authSpc').empty();
                } else if (type == "P") {
                    $('#hidPostedDistCode').val('');
                    $('#hidPostedOffCode').empty();
                    $('#genericpostPosted').empty();
                    $('#postedspc').empty();
                }
            }
            function openPostingModal() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                } else {
                    var orgType = $('input[name="rdTrainingauthtype"]:checked').val();
                    if (orgType == 'GOO') {
                        $('#trainingPostingModal').modal("show");
                    } else if (orgType == 'GOI') {
                        $("#trainingPostingOtherOrgModal").modal("show");
                    }
                }
            }
            function getDeptWiseOfficeList(type) {
                var deptcode;
                if (type == "N") {
                    deptcode = $('#hidNotifyingDeptCode').val();
                    $('#hidNotifyingOffCode').empty();
                } else if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    $('#hidPostedOffCode').empty();
                }
                //var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                var url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptcode;
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
                    var offcode = $('#hidNotifyingOffCode').val();
                    var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#notifyingSpc').empty();
                    $('#notifyingSpc').append('<option value="">--Select Post--</option>');

                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#notifyingSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        });
                    });
                } else if (type == "P") {
                    var offcode = $('#hidPostedOffCode').val();
                    var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + offcode;
                    $('#postedspc').empty();
                    $('#postedspc').append('<option value="">--Select Post--</option>');

                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#postedspc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        });
                    });
                }
            }

            function getPost() {
                $('#notifyingPostName').val($('#notifyingSpc option:selected').text());
            }
            function getPost(type) {
                if (type == "N") {
                    $('#notifyingPostName').val($('#notifyingSpc option:selected').text());
                } else if (type == "P") {
                    $('#postedspn').val($('#postedspc option:selected').text());
                }
            }
            function removePostingData() {
                $('#genericpostPosted').val('');
                $('#postedspc').val('');
                $('#postedspn').val('');
            }
            function getOtherOrgPost(type) {
                if (type == "P") {
                    $('#postedspn').val($('#hidPostedOthSpc option:selected').text());
                    $('#hidPostedDeptCode').val('');
                    $('#hidPostedDistCode').val('');
                    $('#hidPostedOffCode').val('');
                    $('#genericpostPosted').val('');
                    $('#postedspc').val('');
                    $("#trainingPostingOtherOrgModal").modal("hide");
                }
            }
            function getOfficeWiseGenericPostList(type) {
                var offcode;
                var url;

                offcode = $('#hidPostedOffCode').val();
                $('#genericpostPosted').empty();
                $('#genericpostPosted').append('<option value="">--Select Generic Post--</option>');
                url = "getPostCodeListJSON.htm?offcode=" + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "P") {
                            $('#genericpostPosted').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        }
                    });
                });
         
            }

        </script>
    </head>
    <body>
        <form:form id="fm" action="longTermTrainingList.htm" method="post" name="myForm" commandName="lttraining">
            <form:hidden id="empId" path="empId"/>
            <form:hidden id="trainId" path="trainId"/>
            <form:hidden id="hidNotId" path="hidNotId"/>
            <form:hidden id="hidPostedDistCode" path="hidPostedDistCode"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Long Term Training
                    </div>
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
                            <form:radiobutton path="rdTransaction" value="C" /> Current Transaction(will effect current Pay or Post)
                        </div>
                        <div class="col-lg-2"></div>
                    </div>
                    <div class="row" style="margin-top: 10px;margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="txtNotOrdNo">Order No<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">   
                            <form:input class="form-control" path="txtOrdNo" id="txtOrdNo"/>
                        </div>
                        <div class="col-lg-2">
                            <label for="txtNotOrdDt">Order Date<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date txtDate">
                                <form:input class="form-control txtDate" path="txtOrdDt" id="txtOrdDt" readonly="true"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>                                
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="notifyingPostName">Notifying Authority</label>
                        </div>
                        <div class="col-lg-9">
                            <form:input class="form-control" path="notifyingPostName" id="notifyingPostName" readonly="true"/>                           
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#trainingScheduleNotifyingAuthorityModal">
                                <span class="glyphicon glyphicon-search"></span> Search
                            </button>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-3">
                            <label for="postedspn">Details of Posting</label>
                        </div>
                        <div class="col-lg-2">
                            <form:radiobutton path="rdTrainingauthtype" value="GOO" id="postedGOO"/> 
                            <label for="postedGOO"> Government of Orissa </label>
                        </div>
                        <div class="col-lg-2">
                            <form:radiobutton path="rdTrainingauthtype" value="GOI" id="postedGOI"/> 
                            <label for="postedGOI"> Government of India </label>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2"></div>
                        <div class="col-lg-9">
                            <form:input class="form-control" path="postedPostName" id="postedspn" readonly="true"/>                           
                        </div>
                        <div class="col-lg-1">
                            <button type="button" class="btn btn-primary" onclick="openPostingModal();">
                                <span class="glyphicon glyphicon-search"></span> Search
                            </button>
                        </div>
                    </div>
                    <%--<div class="row" style="margin-bottom: 7px;margin-top: 15px;">
                        <div class="col-lg-2">
                            <label for="spc">Training Type<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-5">
                            <form:select path="slttraintype" id="slttraintype" class="form-control">
                                <form:option value="">--Select--</form:option>
                                <form:options items="${trainingType}" itemValue="trainingtypeid" itemLabel="trainingtype"/>
                            </form:select>                           
                        </div>
                    </div>--%>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label >Training Title<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-5">   
                            <form:select path="slttitle" id="slttitle" class="form-control">
                                <form:option value="">--Select--</form:option>
                                <form:options items="${trainingTitle}" itemValue="trainingTitleId" itemLabel="trainingTitle"/>
                            </form:select> 
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label>If Allowed or Underwent<span style="color: red">*</span></label>
                        </div>
                        <div class="col-lg-2">   
                            <form:radiobutton path="underwent" value="U"/>&nbsp;Underwent
                        </div>
                        <div class="col-lg-2">
                            <form:radiobutton path="underwent" value="A"/>&nbsp;Allowed
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label>Training Duration</label>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 12px;">
                        <div class="col-lg-2">
                            <label> From Date:
                                <!--<span style="color: red">*</span>-->
                            </label>
                        </div>
                        <div class="col-lg-2">   
                            <div class="input-group date txtDate">
                                <form:input class="form-control txtDate" path="startDate" id="startDate" readonly="true" />
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>   
                        </div>
                        <div class="col-lg-2">
                            <label> To Date:
                                <!-- <span style="color: red">*</span>-->
                            </label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date txtDate">
                                <form:input class="form-control txtDate" path="complDate" id="complDate" readonly="true" onblur="populateNoOfDays();"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-time"></span>
                                </span>
                            </div>   
                        </div>
                    </div>
                    <div class="row" style="margin-bottom:7px;">
                        <div class="col-lg-2">
                            <label >Total no of Days</label>
                        </div>
                        <div class="col-lg-2">   
                            <form:input class="form-control" path="txttotday" id="txttotday"/>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom:7px;">
                        <div class="col-lg-2">
                            <label >Co-ordinator</label>
                        </div>
                        <div class="col-lg-5">   
                            <form:input class="form-control" path="txtcoordinator" id="txtcoordinator"/>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom:7px;">
                        <div class="col-lg-2">
                            <label>Training Place</label>
                        </div>
                        <div class="col-lg-5">   
                            <form:input class="form-control" path="txttrplace" id="txttrplace"/>
                        </div>
                    </div>
                    <%--<div class="row" style="margin-bottom:7px;">
                        <div class="col-lg-2">
                            <label>Update Cadre Status(TR)</label>
                        </div>
                        <div class="col-lg-5">   
                            <form:checkbox path="nisb" value="TR" />
                        </div>
                    </div>--%>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="note">Note(if any)</label>
                        </div>
                        <div class="col-lg-6">
                            <form:textarea class="form-control" path="txtnote" id="txtnote"/>
                        </div>
                        <div class="col-lg-1">
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" name="save" value="Save" class="btn btn-primary" onclick="return saveCheck()" />
                        <input type="submit" name="cancel" value="Cancel" class="btn btn-primary"/> 
                    </div>    
                </div>
            </div>
            <div id="trainingScheduleNotifyingAuthorityModal" class="modal" role="dialog">
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
                                        <form:options items="${notifyingOfflist}" itemValue="offCode" itemLabel="offName"/>
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
                                        <form:options items="${notifyingPostlist}" itemValue="spc" itemLabel="postname"/>
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

            <div id="trainingPostingModal" class="modal" role="dialog">
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
                                    <label for="hidPostedDeptCode">Department</label>
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
                                    <label for="hidPostedDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDistCode" id="hidPostedDistrictCode" class="form-control" onchange="getDistWiseOfficeList('P');">
                                        <option value="">--Select District--</option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOffCode" id="hidPostedOffCode" class="form-control" onchange="getOfficeWiseGenericPostList('P');">
                                        <option value="">--Select Office--</option>
                                        <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="genericpostPosted">Generic Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="genericpostPosted" id="genericpostPosted" class="form-control" onchange="getGenericPostWiseSPCList('P');">
                                        <option value="">--Select Generic Post--</option>
                                        <form:options items="${gpcpostedList}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="postedspc">Substantive Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="postedspc" id="postedspc" class="form-control" onchange="getPost('P');">
                                        <option value="">--Select Substantive Post--</option>
                                        <form:options items="${postedspclist}" itemValue="spc" itemLabel="spn"/>
                                         <%--<form:options items="${postedspclist}" itemValue="value" itemLabel="label"/>--%>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <%--<div class="row" style="margin-bottom: 7px;">
                                   <div class="col-lg-2">
                                       <label for="postedspc">Post</label>
                                   </div>
                                   <div class="col-lg-9">
                                       <form:select path="postedspc" id="postedspc" class="form-control" onchange="getPost('P');">
                                           <option value="">--Select Post--</option>
                                           <form:options items="${postedspclist}" itemValue="spc" itemLabel="postname"/>
                                       </form:select>
                                   </div>
                                   <div class="col-lg-1">
                                   </div>
                               </div>--%>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            <div id="trainingPostingOtherOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Details of Posting</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="hidPostedOthSpc">Posting Details</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedOthSpc" id="hidPostedOthSpc" class="form-control" onchange="getOtherOrgPost('P');">
                                        <form:option value="">--Select Post--</form:option>
                                        <form:options items="${otherOrgOfflist}" itemValue="value" itemLabel="label"/>
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
