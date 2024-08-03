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

                $('#txtCadreJoiningWEFDt').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                togglePayCommission();


                if ($('#hidNotifyingDeptCode').val() != "") {
                    var distcode = $('#hidNotifyingDistCode').val();

                    //var url = 'getOfficeListJSON.htm?deptcode=' + $('#hidNotifyingDeptCode').val();
                    //var url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + $('#hidNotifyingDeptCode').val() + '&distcode=' + distcode;
                    //$('#hidNotifyingOffCode').empty();
                    // $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            //$('#hidNotifyingOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                        });
                    }).done(function(result) {

                        $('#hidNotifyingOffCode').val($('#hidTempNotifyingOffCode').val());
                        var offcode = $('#hidNotifyingOffCode').val();
                        var gpc = $('#hidNotifyingGPC').val();

                        var url = "getAuthoritySubstantivePostListJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                        //var url = 'getSanctioningSPCOfficeWiseListJSON.htm?offcode=' + $('#hidNotifyingOffCode').val();
                        $('#notifyingSpc').empty();
                        $('#notifyingSpc').append('<option value="">--Select Post--</option>');
                        $.getJSON(url, function(data) {
                            $.each(data, function(i, obj) {
                                $('#notifyingSpc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                            });
                        }).done(function(result) {
                            $('#notifyingSpc').val($('#hidTempNotifyingPost').val());
                        });
                    });
                }



                if ($('#sltPostingDept').val() != "") {
                    $('#sltGenericPost').empty();
                    var url = 'getDeptWisePostListJSON.htm?deptCode=' + $('#sltPostingDept').val();
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            $('#sltGenericPost').append('<option value="' + obj.postcode + '">' + obj.post + '</option>');
                        });
                    }).done(function(result) {
                        $('#sltGenericPost').val($('#hidTempGenericPost').val());
                    });
                }
            });
            /*function getDeptWiseOfficeList(type) {
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
             }*/


            function getDistrictAndDepartmentWiseOfficeList(type) {
                var deptcode;
                var distcode;
                if (type == "N") {
                    if ($('#hidNotifyingDeptCode').val() == '') {
                        alert('Please select Department.');
                        $('#hidNotifyingDeptCode').focus();
                        return false;
                    }
                    if ($('#hidNotifyingDistCode').val() == '') {
                        alert('Please select District.');
                        $('#hidNotifyingDistCode').focus();
                        return false;
                    }
                    deptcode = $('#hidNotifyingDeptCode').val();
                    distcode = $('#hidNotifyingDistCode').val();
                    $('#hidNotifyingOffCode').empty();
                    $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');
                } else if (type == "P") {
                    if ($('#hidPostedDeptCode').val() == '') {
                        alert('Please select Department.');
                        $('#hidPostedDeptCode').focus();
                        return false;
                    }
                    if ($('#hidPostedDistrict').val() == '') {
                        alert('Please select District.');
                        $('#hidPostedDistrict').focus();
                        return false;
                    }
                    deptcode = $('#hidPostedDeptCode').val();
                    distcode = $('#hidPostedDistrict').val();
                    $('#hidPostedOffCode').empty();
                    $('#hidPostedOffCode').append('<option value="">--Select Office--</option>');
                }
                //url = 'getOfficeListDistrictAndDepartmentForBacklogEntryJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;

                url = "";
                if ($("input[name=rdTransaction]:checked").val() == "S") {
                    url = 'getOfficeListDistrictAndDepartmentForBacklogEntryJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                } else {
                    url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
                }

                //var url = 'getOfficeListDistrictAndDepartmentJSON.htm?deptcode=' + deptcode + '&distcode=' + distcode;
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

            function getOfficeWiseGenericPostList(type) {
                var offcode;
                var url;
                if (type == "N") {
                    offcode = $('#hidNotifyingOffCode').val();
                    $('#hidNotifyingGPC').empty();
                    $('#hidNotifyingGPC').append('<option value="">--Select Generic Post--</option>');
                    url = "getAuthorityPostListJSON.htm?offcode=" + offcode;
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    $('#hidPostedGPC').empty();
                    $('#hidPostedGPC').append('<option value="">--Select Generic Post--</option>');
                    url = "getPostCodeListJSON.htm?offcode=" + offcode;
                }
                //var url = 'getAuthorityPostListJSON.htm?offcode=' + offcode;

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        if (type == "N") {
                            $('#hidNotifyingGPC').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        } else if (type == "P") {
                            $('#hidPostedGPC').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        }
                    });
                });

                //Start Field Off Code
                if (type == "P") {
                    $('#sltPostedFieldOff').empty();
                    //var url = 'transferGetFieldOffListJSON.htm?offcode=' + $('#hidPostedOffCode').val();
                    $('#sltPostedFieldOff').append('<option value="">--Select--</option>');
                    $.getJSON(url, function(data) {
                        $.each(data, function(i, obj) {
                            //$('#sltPostedFieldOff').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                }
                //End Field Off Code
            }

            function getGenericPostWiseSPCList(type) {
                var offcode;
                var gpc;
                var url;
                if (type == "N") {
                    offcode = $('#hidNotifyingOffCode').val();
                    gpc = $('#hidNotifyingGPC').val();
                    url = "getAuthoritySubstantivePostListJSON.htm?postcode=" + gpc + "&offcode=" + offcode;
                    $('#notifyingSpc').empty();
                    $('#notifyingSpc').append('<option value="">--Select Substantive Post--</option>');
                } else if (type == "P") {
                    offcode = $('#hidPostedOffCode').val();
                    gpc = $('#hidPostedGPC').val();
                    //alert(offcode,gpc);

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
                        if (type == "N") {
                            $('#notifyingSpc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                        } else if (type == "P") {
                            $('#postedspc').append('<option value="' + obj.spc + '">' + obj.spn + '</option>');
                        }
                    });
                });
            }

            function removeDepedentDropdown(type) {
                if (type == "N") {
                    $('#hidNotifyingDistCode').val('');

                    $('#hidNotifyingOffCode').empty();
                    $('#hidNotifyingOffCode').append('<option value="">--Select Office--</option>');

                    $('#hidNotifyingGPC').empty();
                    $('#hidNotifyingGPC').append('<option value="">--Select Generic Post--</option>');

                    $('#notifyingSpc').empty();
                    $('#notifyingSpc').append('<option value="">--Select Substantive Post--</option>');
                } else if (type == "P") {
                    $('#hidPostedDistrict').val('');

                    //$('#hidPostedOffCode').empty();
                    //$('#hidPostedOffCode').append('<option value="">--Select Office--</option>');

                    $('#hidPostedGPC').empty();
                    $('#hidPostedGPC').append('<option value="">--Select Generic Post--</option>');

                    $('#postedspc').empty();
                    $('#postedspc').append('<option value="">--Select Substantive Post--</option>');
                }
            }


            function getPost(type) {
                if (type == "N") {
                    $('#notifyingPostName').val($('#notifyingSpc option:selected').text());
                } else if (type == "P") {
                    $('#postedspn').val($('#postedspc option:selected').text());
                    //$('#postedPostName').val($('#postedspc option:selected').text());
                    //alert($('#postedPostName').val());
                    //alert($('#postedspc'));
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
            //alert($('#hnotid').val());

            function saveCheck() {
                var payCommVal = $("input[name='rdoPaycomm']:checked").val();
                var rdTransactionVal = $("input[name='rdTransaction']:checked").val();
                var entryValue = "";
                var hidentryType = $('#hidEntryType').val();
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
                 var sltCadreDept = $('#sltCadreDept').val();
                if (sltCadreDept == "") {
                     alert("Please Select Cadre Controlling Department");
                    $('#sltCadreDept').focus();
                    return false;
                }
                if ($('#sltCadre').val() == "") {
                    alert("Please select Cadre");
                    return false;
                }
                /*alert($('#sltGrade').val());
                 if ($('#sltGrade option').length > 0 || ($('#sltGrade').val() == "" || $('sltGrade').val()==null)) {
                 alert("Please select Grade");
                 return false;
                 }*/
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
                    if ($("#payLevel").val() == '') {
                        alert('Please select Pay Level.');
                        return false;
                    }
                    if ($("#payCell").val() == '') {
                        alert('Please select Pay Cell.');
                        return false;
                    }
                }
                if ($('#sltPostGroup').val() == "") {
                 alert("Please Select Post Group");
                 $('#sltPostGroup').focus();
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
                //alert(rdTransactionVal);
                if (hidentryType != "") {
                    //alert(hidentryType);
                    if (rdTransactionVal != hidentryType) {
                        if (hidentryType == 'S') {
                            entryValue = 'Service Book BackLog';
                        } else if (hidentryType == 'C') {
                            entryValue = "Current";
                        }
                        alert("Transaction previously entered as " + entryValue + " Transaction");
                        return false;
                    }

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
                $('#sltCadre').append('<option value="">--Select Cadre--</option>');
                var url = 'getCadreListJSON.htm?deptcode=' + $('#sltCadreDept').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltCadre').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                });
            }

            function getCadreWiseGrade() {
                $('#sltGrade').empty();
                var url = 'getCadreGradeDetailsJSON.htm?cadreCode=' + $('#sltCadre').val();
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#sltGrade').append('<option value="' + obj.gradeCode + '">' + obj.grade + '</option>');
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

            function removePostingData() {
                var url = '';
                var transType = $("input[name='rdTransaction']:checked").val();
                $('#postedspn').val('');
                $('#hidPostedGPC').val('');
                $('#postedspc').val('');
               
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
            function hideGOI() {
                $('#CGOI').hide();
                $('#hidOffCode').empty();
                var url = 'getEmpCurrentPayData.htm?empid=' + $('#empid').val();
                $.getJSON(url, function(data) {
                    // alert('Showing Current Pay Record');
                    // alert(data.rdoPaycomm);
                    if (data.rdoPaycomm == '5') {
                        $('#rdoPaycomm5').val(data.rdoPaycomm);
                        $('#rdoPaycomm5').prop("checked", true);
                        $('.pay6th').show();
                        $("#div7pay").hide();
                        $('#sltPayScale').val('');
                        $('#txtGP').val('');

                    }
                    if (data.rdoPaycomm == '6') {
                        $('#rdoPaycomm6').val(data.rdoPaycomm);
                        $('#rdoPaycomm6').prop("checked", true);
                        $('#sltPayScale').val(data.sltPayScale);
                        $('#txtGP').val(data.txtGP);
                        $('.pay6th').show();
                        $("#div7pay").hide();
                    }
                    if (data.rdoPaycomm == '7') {
                        $('#rdoPaycomm7').val(data.rdoPaycomm);
                        $('#rdoPaycomm7').prop("checked", true);
                        $('#payLevel').val(data.payLevel);
                        $('#payCell').val(data.payCell);
                        $('.pay6th').hide();
                        $("#div7pay").show();
                    }
                    $('#sltPostGroup').val(data.sltPostGroup);
                    $('#txtBasic').val(data.txtBasic);

                });
            }
            function showGOI() {
                $('#SGOO').show();
                $('#CGOI').show();
            }

            function openNotifyingAuthorityModal() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please select Transaction type");
                } else {
                    //$('#promotionAuthorityModal').modal("show");
                    var orgType = $('input[name="radnotifyingauthtype"]:checked').val();
                    if (orgType == 'GOO') {
                        $("#promotionAuthorityModal").modal("show");
                    } else if (orgType == 'GOI') {
                        $("#promotionNotifyingAuthorityOtherOrgModal").modal("show");
                    }
                }
            }

            function openPostingModal() {
                if ($("input[name=rdTransaction]:checked").length == 0) {
                    alert("Please Select Transaction type");
                }
                if ($("input[name=radpostingauthtype]:checked").length == 0) {
                    alert("Please Select Type Of Posting");
                } else {
                    $('#promotionPostingModal').modal("show");
                    var orgType = $('input[name="radpostingauthtype"]:checked').val();
                    if (orgType == 'GOO') {
                        $("#promotionPostingModal").modal("show");
                        $("#promotionOtherOrgModal").modal("hide");
                    } else if (orgType == 'GOI') {
                        $("#promotionOtherOrgModal").modal("show");
                        $("#promotionPostingModal").modal("hide");
                    }
                }
            }

            function togglePayCommission() {
                var rdoPaycomm = $("input[name='rdoPaycomm']:checked").val();
                if (rdoPaycomm == '6' || rdoPaycomm == '5') {
                    $('.pay6th').show();
                    $("#div7pay").hide();
                    //$("#payLevel").val('');
                    //$("#payCell").val('');
                } else {
                    $('.pay6th').hide();
                    $("#div7pay").show();
                    $("#sltPayScale").val('');
                    $("#txtGP").val('');
                }
            }

            function getOtherOrgPost(type) {
                if (type == 'N') {

                    $('#notifyingPostName').val($('#hidNotifyingOthSpc option:selected').text());
                    $('#hidNotifyingDeptCode').val('');
                    $('#hidNotifyingDistCode').val('');
                    $('#hidNotifyingOffCode').val('');
                    $('#hidNotifyingGPC').val('');
                    $('#notifyingSpc').val('');
                    $("#promotionNotifyingAuthorityOtherOrgModal").modal("hide");
                    $("#promotionOtherOrgModal").modal("hide");
                } else if (type == 'P') {
                    $('#postedspn').val($('#hidPostingOthSpc option:selected').text());

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
        <form:form action="savePromotion.htm" method="POST" commandName="promotionForm">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Employee Promotion
                    </div>
                    <div class="panel-body">
                        <form:hidden path="empid" id="empid"/>
                        <form:hidden path="promotionId" id="promotionId"/>
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
                        <form:hidden path="hidGradeCode" id="hidGradeCode"/>
                        <form:hidden path="hidEntryType" id="hidEntryType"/>
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
                                <form:radiobutton path="rdTransaction" id="rdTransactionS" value="S" onclick="removePostingData();" onchange="showGOI();" /> Service Book Entry(Backlog)
                            </div>
                            <div class="col-lg-3">
                                <form:radiobutton path="rdTransaction" id="rdTransactionC" value="C" onclick="removePostingData();" onchange="hideGOI();"/> Current Transaction(will effect current Pay or Post)
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
                                <label for="txtNotOrdDt">Notification Order Date<span style="color: red">*</span></label>
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
                                <label for="notifyingPostName">Notifying Authority</label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOO" id="postedGOO"/> 
                                <label for="postedGOO"> Government of Orissa </label>
                            </div>
                            <div class="col-lg-2">
                                <form:radiobutton path="radnotifyingauthtype" value="GOI" id="postedGOI"/> 
                                <label for="postedGOI"> Government of India </label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2"></div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="notifyingPostName" id="notifyingPostName" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="btn btn-primary" onclick="openNotifyingAuthorityModal();">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltAllotmentDesc">Allotment Description</label>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="sltAllotmentDesc" id="sltAllotmentDesc" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${allotDescList}" itemValue="value" itemLabel="label"/>
                                </form:select>
                            </div>
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2"></div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="chkRetroPromotion">If Retrospective Promotion </label>
                            </div>
                            <div class="col-lg-2">   
                                <form:checkbox path="chkRetroPromotion" id="chkRetroPromotion" value="Y"/> 
                            </div>
                            <div class="col-lg-2"></div>
                            <div class="col-lg-2"></div>
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
                                &nbsp;&nbsp;(a) Cadre Controlling Department<span style="color: red">*</span>
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
                                    <form:options items="${cadreList}" itemValue="value" itemLabel="label"/>
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
                                    <form:option value="">--Select Grade--</form:option>
                                    <form:options items="${gradeList}" itemValue="gradeCode" itemLabel="grade"/>
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

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="chkProformaPromotion">Proforma Promotion</label>
                            </div>
                            <div class="col-lg-8">
                                <form:checkbox path="chkProformaPromotion" value="Y"/>
                            </div>
                            <div class="col-lg-1">

                            </div>
                        </div>
                        <br />
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-3">
                                <label for="postedspn">Details of Posting</label>
                            </div>
                            <div class="col-lg-2" id="SGOO" >
                                <form:radiobutton path="radpostingauthtype" value="GOO" id="promotedGOO" /> 
                                <label for="promotedGOO"> Government of Orissa </label>
                            </div>

                            <div class="col-lg-2" id="CGOI" >
                                <form:radiobutton path="radpostingauthtype" value="GOI" id="promotedGOI" /> 
                                <label for="promotedGOI"> Government of India </label>
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">                            
                            </div>
                            <div class="col-lg-9">
                                <form:input class="form-control" path="postedPostName" id="postedspn" readonly="true"/>                           
                            </div>
                            <div class="col-lg-1">
                                <%--<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#promotionPostingModal">
                                    <span class="glyphicon glyphicon-search"></span> Search
                                </button>--%>
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

                        <div class="row" style="margin-bottom: 7px;" >
                            <div class="col-lg-2">
                                <label for="Commission">Select Pay Commission <span style="color: red">*</span></label>
                            </div>
                            <div class="col-lg-2">   
                                <form:radiobutton  path="rdoPaycomm" id="rdoPaycomm5" value="5" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" >5th Pay</label>
                            </div>                            
                            <div class="col-lg-2">   
                                <form:radiobutton  path="rdoPaycomm" id="rdoPaycomm6" value="6" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" >6th Pay</label>
                            </div>
                            <div class="col-lg-6">
                                <form:radiobutton  path="rdoPaycomm" id="rdoPaycomm7" value="7" onclick="togglePayCommission()"/>
                                <label class="custom-control-label" >7th Pay</label>
                            </div>
                        </div>

                        <div class="row pay6th" style="margin-bottom: 7px;" id="div6pay">
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

                        <div class="row pay6th" style="margin-bottom: 7px;" id="div6pay">
                            <div class="col-lg-2">
                                (b) Grade Pay<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-3">
                                <form:input class="form-control" path="txtGP" id="txtGP" onkeypress="return onlyIntegerRange(event)"/>
                            </div>
                            <div class="col-lg-7">
                            </div>
                        </div>

                        <div class="row" style="margin-bottom: 7px;" id="div7pay">
                            <div class="col-lg-2">
                                (a) Pay Level<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-2">   
                                <form:select path="payLevel" id="payLevel" class="form-control" onchange="getpayCellList()">
                                    <form:option value="">-Select-</form:option>
                                    <form:options items="${paylevelList}" itemLabel="label" itemValue="value"/>
                                </form:select>   
                            </div>
                            <div class="col-lg-2">
                                (b) Pay Cell <span style="color: red">*</span>
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

                        <div class="row" id="postgrp" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                Post Group<span style="color: red">*</span>
                            </div>
                            <div class="col-lg-4">
                                <form:select path="sltPostGroup" id="sltPostGroup" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:option value="A">A</form:option>
                                    <form:option value="B">B</form:option>
                                    <form:option value="C">C</form:option>
                                    <form:option value="D">D</form:option>
                                </form:select>
                            </div>
                            <div class="col-lg-6"></div>
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
                        <a href="PromotionList.htm"> <button type="button" class="btn btn-warning btn-md"> &laquo; Back</button></a>
                        <input type="submit" name="btnPromotion" value="Save Promotion" class="btn btn-default" onclick="return saveCheck();"/>
                        <c:if test="${not empty promotionForm.promotionId}">
                            <%--<input type="button" name="btnPromotion" value="Delete" class="btn btn-default" onclick="return confirm('Are you sure to delete?');"/>--%>
                        </c:if>
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
                                    <label for="hidNotifyingDeptCode">Department</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingDeptCode" id="hidNotifyingDeptCode" class="form-control" onchange="removeDepedentDropdown('N');">
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotifyingDistCode">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingDistCode" id="hidNotifyingDistCode" class="form-control" onchange="getDistrictAndDepartmentWiseOfficeList('N');">
                                        <option value="">--Select District--</option>
                                        <form:options items="${distlist}" itemValue="distCode" itemLabel="distName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotifyingOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOffCode" id="hidNotifyingOffCode" class="form-control" onchange="getOfficeWiseGenericPostList('N');">
                                        <form:option value="">--Select Office--</form:option>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidNotifyingGPC">Generic Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingGPC" id="hidNotifyingGPC" class="form-control" onchange="getGenericPostWiseSPCList('N');">
                                        <option value="">--Select Generic Post--</option>
                                        <form:options items="${notifyingGpclist}" itemValue="value" itemLabel="label"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="notifyingSpc">Substantive Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="notifyingSpc" id="notifyingSpc" class="form-control" onchange="getPost('N');">
                                        <form:option value="">--Select Substantive Post--</form:option>

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

            <div id="promotionPostingModal" class="modal" role="dialog">
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
                                        <form:option value="">--Select Department--</form:option>
                                        <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedDistrict">District</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedDistrict" id="hidPostedDistrict" class="form-control" onchange="getDistrictAndDepartmentWiseOfficeList('P');">
                                        <form:option value="">--Select District--</form:option>
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
                                        <form:option value="">--Select Office--</form:option>
                                        <form:options items="${officeList}" itemValue="offCode" itemLabel="offName"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidPostedGPC">Generic Post</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostedGPC" id="hidPostedGPC" class="form-control" onchange="getGenericPostWiseSPCList('P');">
                                        <option value="">--Select Generic Post--</option>
                                        <form:options items="${postingGpcList}" itemValue="value" itemLabel="label"/>
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
                                        <form:option value="">--Select Substantive Post--</form:option>
                                        <form:options items="${postingSpcList}" itemValue="spc" itemLabel="spn"/>
                                        <%--<c:if test="${not empty promotionForm.hnotid && not empty promotionForm.hidPostedOffCode}">
                                            <form:option value="${promotionForm.hidTempPostedPost}">${promotionForm.postedPostName}</form:option>
                                        </c:if>--%>
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

            <div id="promotionNotifyingAuthorityOtherOrgModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Notifying Authority</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-3">
                                    <label for="hidNotifyingOthSpc">Notified By</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidNotifyingOthSpc" id="hidNotifyingOthSpc" class="form-control" onchange="getOtherOrgPost('N');">
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
            <div id="promotionOtherOrgModal" class="modal" role="dialog">
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
                                    <label for="hidPostingOthSpc">Posting Details</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidPostingOthSpc" id="hidPostingOthSpc" class="form-control" onchange="getOtherOrgPost('P');">
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
