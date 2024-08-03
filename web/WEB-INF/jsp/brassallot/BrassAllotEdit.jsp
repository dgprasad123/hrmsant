<%-- 
    Document   : BrassAllotEdit
    Created on : Jul 17, 2018, 11:52:35 AM
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
                $('#wefDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                if ($('#hidPostedDeptCode').val() != "") {
                    $('#hidPostedOffCode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + $('#hidPostedDeptCode').val();
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        });
                    }).done(function(result) {
                        $('#hidPostedOffCode').val($('#hidTempPostedOffCode').val());
                        $('#postedspc').empty();
                        var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + $('#hidPostedOffCode').val();
                        $('#postedspc').append('<option value="">--Select Post--</option>');
                        $.getJSON(url, function(data) {
                            $.each(data, function(i, obj) {
                                $('#postedspc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                            });
                        }).done(function(result) {
                            $('#postedspc').val($('#hidTempPostedPost').val());
                        });

                        //Start Field Off Code
                        $('#sltPostedFieldOff').empty();
                        var url = 'transferGetFieldOffListJSON.htm?offcode=' + $('#hidPostedOffCode').val();
                        $('#sltPostedFieldOff').append('<option value="">--Select--</option>');
                        $.getJSON(url, function(data) {
                            $.each(data, function(i, obj) {
                                $('#sltPostedFieldOff').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                            });
                        }).done(function(result) {
                            $('#sltPostedFieldOff').val($('#hidTempPostedFieldOffCode').val());
                        });
                        //End Field Off Code
                    });
                }

                if ($('#hidNotifyingDeptCode').val() != "") {
                    $('#hidNotifyingOffCode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + $('#hidNotifyingDeptCode').val();
                    $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        });
                    }).done(function(result) {
                        $('#hidNotifyingOffCode').val($('#hidTempNotifyingOffCode').val());
                        $('#notifyingSpc').empty();
                        var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + $('#hidNotifyingOffCode').val();
                        $('#notifyingSpc').append('<option value="">--Select Post--</option>');
                        $.getJSON(url, function(data) {
                            $.each(data, function(i, obj) {
                                $('#notifyingSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                            });
                        }).done(function(result) {
                            $('#notifyingSpc').val($('#hidTempNotifyingPost').val());
                        });
                    });
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

                if ($('#sltPostingDept').val() != "") {
                    $('#sltGenericPost').empty();
                    var url = 'getDeptWisePostListJSON.htm?deptCode=' + $('#sltPostingDept').val();
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#sltGenericPost').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                        }).done(function(result) {
                            $('#sltGenericPost').val($('#hidTempGenericPost').val());
                        });
                    });
                }
            });
            function getDeptWiseOfficeList(type) {
                var deptcode;
                if (type == "N") {
                    deptcode = $('#sltDept').val();
                    $('#sltDept').empty();
                } else if (type == "P") {
                    deptcode = $('#hidPostedDeptCode').val();
                    $('#hidPostedOffCode').empty();
                }
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                if (type == "N") {
                    $('#sltOffice').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    $('#sltOffice').append('<option value="">--Select Office--</option>');
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
                            $('#hidVerifyingSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
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
                $('#authVerify').val($('#hidVerifyingSpc option:selected').text());
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
                    $('#ordDate').focus();
                    return false;
                }
                if ($('#brassNo').val() == "") {
                    alert("Please Enter Brass No");
                    $('#brassNo').focus();
                    return false;
                }
                if ($('#wefDt').val() == "") {
                    alert("Please enter With Effect Date");
                    $('#wefDt').focus();
                    return false;
                }
                if ($('#wefTime').val() == "") {
                    alert("Please enter Time");
                    $('#wefTime').focus();
                    return false;
                }
                var wefDt = $("#wefDt").val().split("-");
                var hideMaxDate = $("#hideMaxDate").val().split("-");
                var wdt = new Date(wefDt[2], monthint(wefDt[1].toUpperCase()), wefDt[0]);
                var mxdt = new Date(hideMaxDate[2], monthint(hideMaxDate[1].toUpperCase()), hideMaxDate[0]);
                if (mxdt > wdt) {
                    alert("With Effect Date must be greater than previous entry date");
                    $("#wefDt").focus();
                    return false;
                }
                
                if(confirm('Are you sure to Save?')){
                    return true;
                }else{
                    return false;
                }
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
                var url = 'getDescriptionJSON.htm?cadreLevel=' + $('#sltCadreLevel').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltDescription').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }
            function getOfficeList(obj) {
                $('#deptName').empty();
                var deptCode = obj.value;
                //alert(deptCode);
                if (deptCode != '') {
                    var url = 'getOfficeListJSON.htm?deptcode=' + deptCode;
                    $('#sltOffice').append('<option value="">--Select Office--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#sltOffice').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        });
                    });
                } else {
                    $('#sltOffice').children().remove().end().append('<option selected value="">--Select Office--<\/option>');
                }
            }
            function getOfficeListModal(obj) {
                $('#deptName').empty();
                var deptCode = obj.value;
                //alert(deptCode);
                if (deptCode != '') {
                    //var url = 'getOfficeListJSON.htm?deptcode=' + deptCode;
                    var url = 'getofficelistForBacklogEntry.htm?deptcode=' + deptCode;
                    $('#hidVerifyingOffCode').append('<option value="">--Select Office--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#hidVerifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        });
                    });
                } else {
                    $('#hidVerifyingOffCode').children().remove().end().append('<option selected value="">--Select Office--<\/option>');
                }
            }
            function getSpcList(obj) {
                var hidVerifyingOffCode = obj.value;
                //alert(hidVerifyingOffCode);
                if (hidVerifyingOffCode != '') {
                    var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + hidVerifyingOffCode;
                    $('#hidVerifyingSpc').append('<option value="">--Select--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#hidVerifyingSpc').append('<option value="' + obj.spc + '">' + obj.postname + '</option>');
                        });
                    });
                } else {
                    $('#hidVerifyingSpc').children().remove().end().append('<option selected value="">--Select--<\/option>');
                }
            }
            function validatedate(inputText)
            {
                var dateformat = /^(0?[1-9]|[12][0-9]|3[01])[\/\-](0?[1-9]|1[012])[\/\-]\d{4}$/;
                // Match the date format through regular expression
                if (inputText.value.match(dateformat))
                {
                    document.form1.text1.focus();
                    //Test which seperator is used '/' or '-'
                    var opera1 = inputText.value.split('/');
                    var opera2 = inputText.value.split('-');
                    lopera1 = opera1.length;
                    lopera2 = opera2.length;
                    // Extract the string into month, date and year
                    if (lopera1 > 1)
                    {
                        var pdate = inputText.value.split('/');
                    }
                    else if (lopera2 > 1)
                    {
                        var pdate = inputText.value.split('-');
                    }
                    var dd = parseInt(pdate[0]);
                    var mm = parseInt(pdate[1]);
                    var yy = parseInt(pdate[2]);
                    // Create list of days of a month [assume there is no leap year by default]
                    var ListofDays = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
                    if (mm == 1 || mm > 2)
                    {
                        if (dd > ListofDays[mm - 1])
                        {
                            alert('Invalid date format!');
                            return false;
                        }
                    }
                    if (mm == 2)
                    {
                        var lyear = false;
                        if ((!(yy % 4) && yy % 100) || !(yy % 400))
                        {
                            lyear = true;
                        }
                        if ((lyear == false) && (dd >= 29))
                        {
                            alert('Invalid date format!');
                            return false;
                        }
                        if ((lyear == true) && (dd > 29))
                        {
                            alert('Invalid date format!');
                            return false;
                        }
                    }
                }
                else
                {
                    alert("Invalid date format!");
                    document.form1.text1.focus();
                    return false;
                }
            }
            function editAction() {
                $('#hidMode').val('E');

            }


        </script>
    </head>
    <body>
        <form:form action="addNewBrass.htm" method="POST" commandName="addNewBrassForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Brass No
                    </div>
                    <div class="panel-body">
                        <form:hidden path="hideMaxDate" id="hideMaxDate"/>
                        <form:hidden path="notId" id="hidNotID"/>
                        <form:hidden path="mode" id="hidmode"/>
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
                            <div class="col-lg-2">
                                <label for="ordno">Office Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="ordno" id="ordno"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="ordDate">Order Date<span style="color: red">*</span></label>
                            </div>

                            <div class="col-lg-2">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="ordDate" id="ordDate" readonly="true" onclick="validatedate(document.form1.text1)"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>                                
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="authVerify">Details Of Verifying Authority</label>
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="authVerify" id="authVerify" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#verifyingAuthorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltDept">Select Department</label>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltDept" id="sltDept" class="form-control" onchange="getOfficeList(this)">
                                    <form:option value="">--Select Department--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltOffice">Select Office</label>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltOffice" id="sltOffice" class="form-control" >
                                    <form:option value="">--Select Office--</form:option>
                                    <form:options items="${offlist}" itemValue="offCode" itemLabel="offName"/>
                                </form:select>                                                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltDist">Select District</label>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltDist" id="sltDist" class="form-control">
                                    <form:option value="">--Select District--</form:option>
                                    <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2" >
                                <label for="brassNo">Brass No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2" >
                                <form:input class="form-control" path="brassNo" id="brassNo"/>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="wefDt">With Effect Date<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <div class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="wefDt" id="wefDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="wefTime">Time<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="wefTime" id="wefTime" class="form-control">
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
                        <c:if test="${empty addNewBrassForm.brId}">
                            <input type="submit" name="submit" class="btn btn-default btn-primary" style="width:130px" onclick="return saveCheck();" value="Save"/>
                        </c:if>
                        <c:if test="${not empty addNewBrassForm.brId}">
                            <input type="submit" class="btn btn-default btn-primary" style="width:130px" onclick="return saveCheck();" value="Update"/>                            
                        </c:if>
                        <input type="submit" name="submit" class="btn btn-default btn-primary" style="width:130px" value="Back"/>
                    </div>
                </div>
                <div id="verifyingAuthorityModal" class="modal" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Verifying Authority</h4>
                            </div>
                            <div class="modal-body">
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="hidVerifyingDeptCode">Department</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="hidVerifyingDeptCode" id="hidVerifyingDeptCode" class="form-control" onchange="getOfficeListModal(this)">
                                            <form:option value="">--Select--</form:option>
                                            <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="hidVerifyingOffCode">Office</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="hidVerifyingOffCode" id="hidVerifyingOffCode" class="form-control" onchange="getSpcList(this)">
                                            <form:option value="">--Select--</form:option>
                                            <form:options items="${offlist1}" itemValue="offCode" itemLabel="offName"/>
                                        </form:select>
                                    </div>
                                    <div class="col-lg-1">
                                    </div>
                                </div>
                                <div class="row" style="margin-bottom: 7px;">
                                    <div class="col-lg-2">
                                        <label for="hidVerifyingSpc">Post</label>
                                    </div>
                                    <div class="col-lg-9">
                                        <form:select path="hidVerifyingSpc" id="hidVerifyingSpc" class="form-control" onchange="getPost('N');">
                                            <form:option value="">--Select--</form:option>
                                            <form:options items="${postlist}" itemValue="spc" itemLabel="postname"/>
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
            </div>
        </form:form>
    </body>
</html>
