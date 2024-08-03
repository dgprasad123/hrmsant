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
                if ($('#txtGP').val() == "") {
                    alert("Please enter Grade Pay");
                    $('#txtGP').focus();
                    return false;
                }
                if ($('#txtBasic').val() == "") {
                    alert("Please enter Pay");
                    $('#txtBasic').focus();
                    return false;
                }
                if ($('#txtWEFDt').val() == "") {
                    alert("Please enter Date of Effect of Pay");
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
        <form:form action="saveAbsorption.htm" method="POST" commandName="absorptionForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Absorption
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="hnotid" id="hnotid"/>
                        <form:hidden path="hpayid" id="hpayid"/>
                        <form:hidden path="hCadId" id="hCadId"/>

                        <form:hidden path="hidTempNotifyingOffCode" id="hidTempNotifyingOffCode"/>
                        <form:hidden path="hidTempNotifyingPost" id="hidTempNotifyingPost"/>

                        <form:hidden path="hidTempCadre" id="hidTempCadre"/>
                        <form:hidden path="hidTempGrade" id="hidTempGrade"/>
                        <form:hidden path="hidTempDescription" id="hidTempDescription"/>

                        <form:hidden path="hidTempGenericPost" id="hidTempGenericPost"/>

                        <form:hidden path="hidTempPostedOffCode" id="hidTempPostedOffCode"/>
                        <form:hidden path="hidTempPostedPost" id="hidTempPostedPost"/>
                        <form:hidden path="hidTempPostedFieldOffCode" id="hidTempPostedFieldOffCode"/>

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
                                <label for="txtNotOrdNo">Notification Order No<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:input class="form-control" path="ordno" id="ordno"/>
                            </div>
                            <div class="col-lg-2">
                                <label for="txtNotOrdDt">Notification Order Date<span style="color: red">*</span></label>
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
                                <label for="spc">Notifying Authority</label>
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="notifyingPostName" id="notifyingPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#promotionAuthorityModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label>Details of Cadre, Grade and Post</label>
                            </div>
                            <div class="col-lg-8">

                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(a) Cadre Controlling Department
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltCadreDept" id="sltCadreDept" class="form-control" onchange="getDeptWiseCadre();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(b) Name of the Cadre<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltCadre" id="sltCadre" class="form-control" onchange="getCadreWiseGrade();">
                                    <form:option value="">--Select--</form:option>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(c) Name of the Grade
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltGrade" id="sltGrade" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(d) Cadre Level
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltCadreLevel" id="sltCadreLevel" class="form-control" onchange="getDescription();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${lvlList}" itemValue="value" itemLabel="label"/>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(e) Description
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltDescription" id="sltDescription" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${descList}" itemValue="value" itemLabel="label"/>
                                </form:select>                           
                            </div>
                            <div class="col-lg-1"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(f) Allotment Year
                            </div>
                            <div class="col-lg-3">
                                <form:input path="txtAllotmentYear" id="txtAllotmentYear" class="form-control" maxlength="4"/>
                            </div>
                            <div class="col-lg-7"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(g) Cadre Id
                            </div>
                            <div class="col-lg-3">
                                <form:input path="txtCadreId" id="txtCadreId" class="form-control" maxlength="10"/>
                            </div>
                            <div class="col-lg-7"></div>
                        </div>
                        <br />
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-6">
                                <label>Please Fill up Column below if Post Details is Available</label>
                            </div>
                            <div class="col-lg-4">

                            </div>
                            <div class="col-lg-2">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(i) Posting Department
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltPostingDept" id="sltPostingDept" class="form-control" onchange="getPostCodeDeptWise();">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                </form:select>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                &nbsp;&nbsp;(ii) Name of the Generic Post
                            </div>
                            <div class="col-lg-8">
                                <form:select path="sltGenericPost" id="sltGenericPost" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>
                        <br />
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label>Post Classification</label>
                            </div>
                            <div class="col-lg-8">
                                <form:radiobutton path="rdPostClassification" value="A"/>&nbsp;Adhoc
                                <form:radiobutton path="rdPostClassification" value="T"/>&nbsp;Temporary
                                <form:radiobutton path="rdPostClassification" value="O"/>&nbsp;On Probation
                                <form:radiobutton path="rdPostClassification" value="P"/>&nbsp;Permanent
                                <form:radiobutton path="rdPostClassification" value=""/>&nbsp;None
                                <br />
                                <form:radiobutton path="rdPostStatus" value="O"/>&nbsp;Officiating
                                <form:radiobutton path="rdPostStatus" value="S"/>&nbsp;Substantive
                                <form:radiobutton path="rdPostStatus" value="A"/>&nbsp;None
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>
                        <br />
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="txtCadreJoiningWEFDt">Date of Effect of Joining in Cadre / Post<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <div class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="txtCadreJoiningWEFDt" id="txtCadreJoiningWEFDt" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-2">
                                <label for="sltCadreJoiningWEFTime">Time<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="sltCadreJoiningWEFTime" id="sltCadreJoiningWEFTime" class="form-control">
                                    <option value="">-Select-</option>
                                    <form:option value="FN">Fore Noon</form:option>
                                    <form:option value="AN">After Noon</form:option>
                                </form:select>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="chkUpdateCadreStatus">Update Cadre Status(JPR)</label>
                            </div>
                            <div class="col-lg-8">
                                <form:checkbox path="chkUpdateCadreStatus" value="JPR"/>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="chkJoinedAsSuch">Joined As Such</label>
                            </div>
                            <div class="col-lg-8">
                                <form:checkbox path="chkJoinedAsSuch" value="Y"/>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>

                        <br />
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
                                (a) Scale of Pay/Pay Band
                            </div>
                            <div class="col-lg-5">
                                <form:select path="sltPayScale" id="sltPayScale" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${payscalelist}" itemValue="payscale" itemLabel="payscale"/>
                                </form:select>
                            </div>
                            <div class="col-lg-5">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (b) Grade Pay<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtGP" id="txtGP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (c) Pay in Substantive Post<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtBasic" id="txtBasic" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (d) Special Pay
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtSP" id="txtSP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (e) Personal Pay
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtPP" id="txtPP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (f) Other Pay
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtOP" id="txtOP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                (g) Description of Other Pay
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
                        <input type="submit" name="btnPromotion" value="Save Absorption" class="btn btn-default" onclick="return saveCheck();"/>
                        <button type="submit" name="submit" value="Cancel" class="btn btn-default" >Cancel</button>
                    </div>
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
