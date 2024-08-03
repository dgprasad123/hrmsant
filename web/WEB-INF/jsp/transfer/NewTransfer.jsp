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
                if ($('#hidPostedDeptCode').val() != "") {
                    var distcode = $('#hidPostedDistCode').val();
                    $('#hidPostedOffCode').empty();
                    //var url = 'getOfficeListJSON.htm?deptcode=' + $('#hidPostedDeptCode').val();
                    var url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + $('#hidPostedDeptCode').val() + '&distcode=' + distcode;
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#hidPostedOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        });
                    }).done(function(result) {
                        $('#hidPostedOffCode').val($('#hidTempPostedOffCode').val());
                        var offcode = $('#hidPostedOffCode').val();
                        var gpc = $('#genericpostPosted').val();
                        $('#postedspc').empty();
                        $('#postedspc').append('<option value="">--Select Post--</option>');
                        //var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + $('#hidPostedOffCode').val();

                        var url;
                        if ($("input[name=rdTransaction]:checked").val() == "S") {
                            url = "getSubstantivePostListBacklogEntryJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                        } else if ($("input[name=rdTransaction]:checked").val() == "C") {
                            url = "getVacantSubstantivePostListJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                        }
                        $.getJSON(url, function(data) {
                            $.each(data, function(i, obj) {
                                $('#postedspc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
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

                if ($('#hidAuthDeptCode').val() != "") {
                    var distcode = $('#hidAuthDistCode').val();
                    $('#hidAuthOffCode').empty();
                    //var url = 'getOfficeListJSON.htm?deptcode=' + $('#hidAuthDeptCode').val();
                    var url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + $('#hidAuthDeptCode').val() + '&distcode=' + distcode;
                    $('#hidAuthOffCode').append('<option value="">--Select Office--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#hidAuthOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        });
                    }).done(function(result) {
                        $('#hidAuthOffCode').val($('#hidTempAuthOffCode').val());
                        $('#authSpc').empty();
                        var gpc = $('#genericpostAuth').val();
                        //var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + $('#hidAuthOffCode').val();
                        var url = "getAuthoritySubstantivePostListJSON.htm?postcode=" + gpc + "&offcode=" + $('#hidAuthOffCode').val();
                        $('#authSpc').append('<option value="">--Select Post--</option>');
                        $.getJSON(url, function(data) {
                            $.each(data, function(i, obj) {
                                $('#authSpc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                            });
                        }).done(function(result) {
                            $('#authSpc').val($('#hidTempAuthPost').val());
                        });
                    });
                }

                togglePayCommission();
            });
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
                    distcode = $('#hidPostedDistCode').val();
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

            function openSanctioningAuthorityModal() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                } else {
                    var orgType = $('input[name="radsanctioningauthtype"]:checked').val();
                    if (orgType == 'GOO') {
                        $('#transferAuthorityModal').modal("show");
                    } else if (orgType == 'GOI') {
                        $("#transferAuthorityOtherOrgModal").modal("show");
                    }
                }
            }

            function openPostingModal() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                } else {
                    var orgType = $('input[name="radpostingauthtype"]:checked').val();
                    if (orgType == 'GOO') {
                        $('#transferPostingModal').modal("show");
                    } else if (orgType == 'GOI') {
                        $("#transferPostingOtherOrgModal").modal("show");
                    }
                }
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
                    $('#postedspc').append('<option value="">--Select Substantive Post--</option>');
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
            function removePostingData() {
                var url = '';
                var transType = $("input[name='rdTransaction']:checked").val();
                $('#genericpostPosted').val('');
                $('#postedspc').val('');
                $('#postedspn').val('');
                
                var empLawLevelStatus = $('#hidStatus').val();
                if (empLawLevelStatus == 'Y') {
                    if (transType == 'S') {
                        url = "getPayMatrixLevelAllForLAWJSON.htm?";
                    }
                    if (transType == 'C') {
                        url = "getPayMatrixLevelForLAWJSON.htm?";
                    }
                } else {
                    url = "getPaymatrixList2017JSON.htm?";
                }

                $.getJSON(url, function(data) {
                    $('#payLevel').empty();
                    $.each(data, function(i, obj) {
                        $('#payLevel').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });

                });

            }


            /*function getOfficeWisePostList(type) {
             var offcode;
             if (type == "A") {
             offcode = $('#hidAuthOffCode').val();
             $('#authSpc').empty();
             } else if (type == "P") {
             offcode = $('#hidPostedOffCode').val();
             $('#postedspc').empty();
             }
             var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + offcode;
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
             }*/

            function getPost(type) {
                if (type == "A") {
                    $('#authPostName').val($('#authSpc option:selected').text());
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



            function togglePayCommission() {
                var rdoPaycomm = $("input[name='rdoPaycomm']:checked").val();
                if (rdoPaycomm == '6' || rdoPaycomm == '5') {
                    $('.pay6th').show();
                    $("#div7pay").hide();
                    $("#payLevel").val('');
                    $("#payCell").val('');
                } else {
                    $('.pay6th').hide();
                    $("#div7pay").show();
                    $("#sltPayScale").val('');
                    $("#txtGP").val('');
                }
            }

            function saveCheck() {
                var payCommVal = $("input[name='rdoPaycomm']:checked").val();
                //var rdServiceBook = document.getElementsByTagName('rdTransaction');
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
                if (payCommVal == '6') {
                    var gp = $('#txtGP').val();
                    if (gp == '') {
                        alert("Enter Grade Pay");
                        return false;
                    }
                }

                if (payCommVal == '7') {
                    if ($("#payLevel").val() == '' && $("#payLevelBacklog").val() == '') {
                        alert('Please select Pay Level.');
                        return false;
                    }
                    if ($("#payCell").val() == '' && $("#payCellBacklog").val() == '') {
                        alert('Please select Pay Cell.');
                        return false;
                    }
                }
                /*if ($('#txtGP').val() == "") {
                 alert("Please enter Grade Pay");
                 $('#txtGP').focus();
                 return false;
                 }*/
                if ($('#txtBasic').val() == "") {
                    alert("Please enter Pay");
                    $('#txtBasic').focus();
                    return false;
                }
                if ($('#txtWEFDt').val() == "") {
                    alert("Please enter Date of Effect of Pay");
                    return false;
                }
                $('#btnSave').hide();
                return true;
            }

            function getOtherOrgPost(type) {
                if (type == "S") {
                    $('#authPostName').val($('#hidSanctioningOthSpc option:selected').text());

                    $('#hidAuthDeptCode').val('');
                    $('#hidAuthDistCode').val('');
                    $('#hidAuthOffCode').val('');
                    $('#genericpostAuth').val('');
                    $('#authSpc').val('');

                    $("#transferAuthorityModal").modal("hide");
                } else if (type == "P") {
                    $('#postedspn').val($('#hidPostedOthSpc option:selected').text());

                    $('#hidPostedDeptCode').val('');
                    $('#hidPostedDistCode').val('');
                    $('#hidPostedOffCode').val('');
                    $('#genericpostPosted').val('');
                    $('#postedspc').val('');

                    $("#transferAuthorityOtherOrgModal").modal("hide");
                }
            }

            function getpayCellList() {
                var url = '';
                var matrixLevel = $('#payLevel').val();
                var empLawLevelStatus = $('#hidStatus').val();                
                if (empLawLevelStatus == 'Y') {
                    url = "getMatrixCellListLawLevelWiseJSON.htm?matrixLevelValue=" + matrixLevel;
                } else {
                    url = "getMatrixCellListGPLevelWiseJSON.htm?matrixLevelValue=" + matrixLevel;
                }

                $.getJSON(url, function(data) {
                    $('#payCell').empty();
                    $.each(data, function(i, obj) {
                        $('#payCell').append('<option value="' + obj.value + '">' + obj.label + '</option>')
                    });

                });

            }
            
        </script>
    </head>
    <body>
        <form:form action="saveTransfer.htm" method="post" commandName="transferForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Transfer
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="transferId" id="transferId"/>
                        <form:hidden path="hnotid" id="hnotid"/>
                        <form:hidden path="hpayid" id="hpayid"/>

                        <form:hidden path="hidTempAuthOffCode" id="hidTempAuthOffCode"/>
                        <form:hidden path="hidTempAuthPost" id="hidTempAuthPost"/>

                        <form:hidden path="hidTempPostedOffCode" id="hidTempPostedOffCode"/>
                        <form:hidden path="hidTempPostedPost" id="hidTempPostedPost"/>
                        <form:hidden path="hidTempPostedFieldOffCode" id="hidTempPostedFieldOffCode"/>
                        <form:hidden path="hidStatus" id="hidStatus"/>

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
                                <form:radiobutton path="rdTransaction" id="rdTransaction" value="S" onclick="removePostingData();"/> Service Book Entry(Backlog)
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="rdTransaction" id="rdTransaction" value="C" onclick="removePostingData();"/> Current Transaction(will effect current Pay or Post)
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
                            <div class="col-lg-3">
                                <label for="authPostName">Sanctioning Authority</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radsanctioningauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radsanctioningauthtype" value="GOI" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="authPostName" id="authPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <%--<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#transferAuthorityModal">--%>
                                <button type="button" class="btn btn-primary" onclick="openSanctioningAuthorityModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="postedspn">Details of Posting</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radpostingauthtype" value="GOI" id="postedGOI"/> 
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

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltPostedFieldOff">(a) Field Office:</label>
                            </div>
                            <div class="col-lg-9">
                                <form:select path="sltPostedFieldOff" id="sltPostedFieldOff" class="form-control">
                                    <option value="">--Select--</option>
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
                                <label for="Commission">Select Pay Commission <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:radiobutton class="custom-control-input" id="rdoPaycomm" path="rdoPaycomm" value="5" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" for="defaultUnchecked">5th Pay</label>
                            </div>                            
                            <div class="col-lg-2">   
                                <form:radiobutton class="custom-control-input" id="rdoPaycomm" path="rdoPaycomm" value="6" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" for="defaultUnchecked">6th Pay</label>
                            </div>
                            <div class="col-lg-6">
                                <form:radiobutton class="custom-control-input" id="rdoPaycomm" path="rdoPaycomm" value="7" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" for="defaultUnchecked">7th Pay</label>
                            </div>
                        </div>

                        <div class="row pay6th" style="margin-bottom: 7px;">
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

                        <div class="row pay6th" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="txtGP">(b) Grade Pay<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtGP" id="txtGP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;" id="div7pay">
                            <div class="col-lg-2">
                                <label for="payLevel">(a) Pay Level<span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="payLevel" id="payLevel" class="form-control" onchange="getpayCellList()">
                                    <form:option value="">-Select-</form:option>
                                    <form:options items="${paylevelList}" itemLabel="label" itemValue="value"/>
                                </form:select>   
                            </div>
                            <div class="col-lg-2">
                                <label for="payCell">(b) Pay Cell <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">
                                <form:select path="payCell" id="payCell" class="form-control">
                                    <form:option value="">-Select-</form:option>
                                    <form:options items="${payCellList}" itemLabel="label" itemValue="value"/>
                                </form:select>                                
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
                                <label for="chkCdrSts">Update Cadre Status(JPR)</label>
                            </div>
                            <div class="col-lg-2">
                                <form:checkbox path="chkCdrSts" id="chkCdrSts" value="Y"/>
                            </div>
                            <div class="col-lg-8">
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
                        <button type="submit" name="submit" value="Save" class="btn btn-default" id="btnSave" onclick="return saveCheck();">Save Transfer</button>
                        <c:if test="${not empty transferForm.transferId}">
                            <%--<button type="submit" name="submit" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');">Delete</button>--%>
                        </c:if>
                        <button type="submit" name="submit" value="Back" class="btn btn-default">Back</button>
                    </div>
                </div>
            </div>

            <div id="transferAuthorityModal" class="modal" role="dialog">
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
                                    <label for="hidAuthDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthDeptCode" id="hidAuthDeptCode" class="form-control" onchange="removeDepedentDropdown('A');">
                                        <option value="">--Select Department--</option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidAuthDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthDistCode" id="hidAuthDistCode" class="form-control" onchange="getDistWiseOfficeList('A');">
                                        <option value="">--Select District--</option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidAuthOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidAuthOffCode" id="hidAuthOffCode" class="form-control" onchange="getOfficeWiseGenericPostList('A');">
                                        <option value="">--Select Office--</option>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="genericpostAuth">Generic Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="genericpostAuth" id="genericpostAuth" class="form-control" onchange="getGenericPostWiseSPCList('A');">
                                        <option value="">--Select Generic Post--</option>
                                        <form:options items="${gpcauthlist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="authSpc">Substantive Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="authSpc" id="authSpc" class="form-control" onchange="getPost('A');">
                                        <option value="">--Select Substantive Post--</option>
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

            <div id="transferAuthorityOtherOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Sanctioning Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="hidSanctioningOthSpc">Sanctioned By</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidSanctioningOthSpc" id="hidSanctioningOthSpc" class="form-control" onchange="getOtherOrgPost('S');">
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

            <div id="transferPostingModal" class="modal" role="dialog">
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
                                    <form:select path="hidPostedDeptCode" id="hidPostedDeptCode" class="form-control" onchange="removeDepedentDropdown('P');">
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
                                    <form:select path="hidPostedDistCode" id="hidPostedDistCode" class="form-control" onchange="getDistWiseOfficeList('P');">
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

            <div id="transferPostingOtherOrgModal" class="modal" role="dialog">
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
    <script type="text/javascript">
        $(function() {
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
    </script>
</html>
