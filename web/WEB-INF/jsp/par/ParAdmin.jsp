<%-- 
    Document   : ParAdmin
    Created on : Nov 18, 2019, 1:02:16 PM
    Author     : manisha
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>
        <script type="text/javascript">
            var rowSize = 20;
            var totalPages = 0;
            $(document).ready(function() {
                $(".loader").hide();
                comboBoxChanged();
                $.post("GetFiscalYearListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });
                $.post("getParStatusrListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#searchParStatus').append($('<option>', {value: data[i].statusId, text: data[i].statusName}));
                    }
                });

                $("#deptName").change(function() {
                    $('#deptcode').empty();
                    var url = 'getCadreListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, obj) {
                            $('#cadrecode').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                        });
                    });
                });


                $("#deptNamemappost").change(function() {
                    alert(1111);
                    $('#offcodemappost').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#offcodemappost').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                });

                $("#offcodemappost").change(function() {
                    var url = 'getAllSPCWithEmployee.htm?offcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $('#postCodemappost').empty();
                        $.each(result, function(i, field) {
                            $('#postCodemappost').append($('<option>', {
                                value: field.spc,
                                text: field.postname + ", (" + field.empname + ")"
                            }));
                        });
                    });
                });

                $("#deptNamemappostEmp").change(function() {
                    alert(1111);
                    $('#offcodemappostEmp').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#offcodemappostEmp').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                });
                $("#offcodemappostEmp").change(function() {
                    var url = 'getAllSPCWithEmployee.htm?offcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $('#postCodemappostEmp').empty();
                        $.each(result, function(i, field) {
                            $('#postCodemappostEmp').append($('<option>', {
                                value: field.spc,
                                text: field.postname + ", (" + field.empname + ")"
                            }));
                        });
                    });
                });

            });
            function searchPar() {
                var fiscalyear = $("#fiscalyear").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                var searchParStatus = $("#searchParStatus").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                if ($("#searchCriteria").val() != "") {
                    if ($("#searchString").val() == "") {
                        alert("please Enter all the Search value");
                        $("#searchString").focus();
                        return false;
                    }
                }
                $(".loader").show();
                $("#searchbtn").attr("disabled", true);
                $.post("getSearchPARList.htm", {fiscalyear: fiscalyear, searchCriteria: searchCriteria, searchString: searchString, searchParStatus: searchParStatus, page: 1, rows: rowSize})
                        .done(function(data) {
                            var totalPARFound = data.totalPARFound;
                            totalPages = Math.round(totalPARFound / rowSize);
                            $("#pagingstatus").text("1 of " + totalPages);
                            $("#paging").val(1);
                            var parlist = data.parlist;
                            populateDataInGrid(parlist);
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
            }

            function searchReviewedParList() {
                var fiscalyear = $("#fiscalyear").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                var searchCriteria1 = $("#searchCriteria1").val();
                var searchString1 = $("#searchString1").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                if ($("#searchCriteria").val() != "") {
                    if ($("#searchString").val() == "") {
                        alert("please Enter all the Search value");
                        $("#searchString").focus();
                        return false;
                    }
                }
                if ($("#searchCriteria1").val() != "") {
                    if ($("#searchString1").val() == "") {
                        alert("please Enter all the Search value");
                        $("#searchString").focus();
                        return false;
                    }
                }
                $(".loader").show();
                $("#searchreviewedbtn").attr("disabled", true);
                $.post("getSearchReviewedPARList.htm", {fiscalyear: fiscalyear, searchCriteria: searchCriteria, searchString: searchString, searchCriteria1: searchCriteria1, searchString1: searchString1, page: 1, rows: rowSize})
                        .done(function(data) {
                            var totalPARFound = data.totalPARFound;
                            totalPages = Math.round(totalPARFound / rowSize);
                            $("#pagingstatus").text("1 of " + totalPages);
                            $("#paging").val(1);
                            var parlist = data.parlist;
                            populateDataInGrid(parlist);
                            $(".loader").hide();
                            $("#searchreviewedbtn").attr("disabled", false);
                        })
            }
            function gotoPage() {
                var pageNo = $("#paging").val();
                if (pageNo == "") {
                    alert("Insert Page No");
                    $("#paging").focus();
                    return false;
                }
                $(".loader").show();
                $("#searchbtn").attr("disabled", true);
                fiscalyear = $("#fiscalyear").val();
                searchCriteria = $("#searchCriteria").val();
                searchString = $("#searchString").val();
                $.post("getSearchPARList.htm", {fiscalyear: fiscalyear, searchCriteria: searchCriteria, searchString: searchString, page: pageNo, rows: rowSize})
                        .done(function(data) {
                            $("#pagingstatus").text(pageNo + " of " + totalPages);
                            var parlist = data.parlist;
                            populateDataInGrid(parlist);
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
            }



            function populateDataInGrid(parlist) {
                $("#pardatagrid").empty();
                var initiatedBy = "";
                for (var i = 0; i < parlist.length; i++) {
                    if (parlist[i].initiatedByEmpId != "") {
                        initiatedBy = "(Initiated By Authority)";
                    }
                    if (parlist[i].isreview == 'Y') {
                        var row = '<tr style="color:white; background:#228B22;">' +
                                '<td><input type = "radio" name="rdEmp" value=' + parlist[i].parId + '> </td>' +
                                '<td>' + parlist[i].pageNo + '</td>' +
                                '<td>' + parlist[i].fiscalyear + '</td>' +
                                '<td>' + parlist[i].empId + '</td>' +
                                '<td>' + parlist[i].gpfno + '</td>' +
                                '<td>' + parlist[i].empName + '</td>' +
                                '<td>' + parlist[i].postName + '</td>' +
                                '<td>' + parlist[i].dob + '</td>' +
                                '<td>' + parlist[i].groupName + '</td>' +
                                '<td>' + parlist[i].cadreName + '</td>' +
                                '<td>' + parlist[i].currentoffice + '</td>' +
                                '<td>' + parlist[i].mobile + '</td>' +
                                '<td>' + parlist[i].parstatus + initiatedBy + '</td>';
                        if (parlist[i].parstatus == "PAR NOT CREATED") {
                            row = row + '<td>&nbsp;</td>';
                        } else {
                            row = row + '<td><a href="javascript:void(0);" data-href="viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + parlist[i].fiscalyear + '" class="openPopup" style="color:white; background:##f8f9f5;">View</a></td>';
                        }
                        row = row + '</tr>';
                        $("#pardatagrid").append(row);
                    } else if (parlist[i].isadversed == 'Y' && parlist[i].isreview == 'N') {
                        var row = '<tr style="color:white; background:#840000;">' +
                                '<td><input type = "radio" name="rdEmp" value=' + parlist[i].parId + '> </td>' +
                                '<td>' + parlist[i].pageNo + '</td>' +
                                '<td>' + parlist[i].fiscalyear + '</td>' +
                                '<td>' + parlist[i].empId + '</td>' +
                                '<td>' + parlist[i].gpfno + '</td>' +
                                '<td>' + parlist[i].empName + '</td>' +
                                '<td>' + parlist[i].postName + '</td>' +
                                '<td>' + parlist[i].dob + '</td>' +
                                '<td>' + parlist[i].groupName + '</td>' +
                                '<td>' + parlist[i].cadreName + '</td>' +
                                '<td>' + parlist[i].currentoffice + '</td>' +
                                '<td>' + parlist[i].mobile + '</td>' +
                                '<td>' + parlist[i].parstatus + initiatedBy + '</td>' +
                                '<td><a href="javascript:void(0);" data-href="viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + parlist[i].fiscalyear + '" class="openPopup" style="color:white; background:##f8f9f5;">View</a></td>' +
                                //'<td><a href="javascript:void(0);" onclick="javascript: window.open(\'viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + $("#fiscalyear").val() +'\', \'\', \'width=600,height=800,top=200,left=200,menubar=0\')">View</a>' +
                                '</tr>';
                        $("#pardatagrid").append(row);
                    } else {
                        var row = '<tr>' +
                                '<td><input type = "radio" name="rdEmp" value=' + parlist[i].parId + '> </td>' +
                                '<td>' + parlist[i].pageNo + '</td>' +
                                '<td>' + parlist[i].fiscalyear + '</td>' +
                                '<td>' + parlist[i].empId + '</td>' +
                                '<td>' + parlist[i].gpfno + '</td>' +
                                '<td>' + parlist[i].empName + '</td>' +
                                '<td>' + parlist[i].postName + '</td>' +
                                '<td>' + parlist[i].dob + '</td>' +
                                '<td>' + parlist[i].groupName + '</td>' +
                                '<td>' + parlist[i].cadreName + '</td>' +
                                '<td>' + parlist[i].currentoffice + '</td>' +
                                '<td>' + parlist[i].mobile + '</td>' +
                                '<td>' + parlist[i].parstatus + initiatedBy + '</td>';
                        if (parlist[i].parstatus == "PAR NOT CREATED") {
                            row = row + '<td>&nbsp;</td>';
                        } else {
                            row = row + '<td><a href="javascript:void(0);" data-href="viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + parlist[i].fiscalyear + '" class="openPopup">View</a></td>';
                        }
                        row = row + '</tr>';
                        $("#pardatagrid").append(row);
                    }
                }
                $('.openPopup').on('click', function() {
                    var dataURL = $(this).attr('data-href');
                    $('.myModalBody').load(dataURL, function() {
                        $('#myModal').modal({show: true});
                    });
                });


            }

            function showChangeCadreWindow() {
                var radioValue = $("input[name='rdEmp']:checked").val();
                if (radioValue) {
                    $('#setCadre').modal('show');
                } else {
                    alert("Select Employee");
                }
            }
            function openMapPostWindow() {
                var radioValue = $("input[name='rdEmp']:checked").val();
                if (radioValue) {
                    $('#setMap').modal('show');
                } else {
                    alert("Select Employee");
                }
            }


            function opensetAuthorityWindow() {
                $(".loader").show();
                var selectedParId = $("input[name='rdEmp']:checked").val();
                var fiscialYear = $("#fiscalyear").val();
                if (selectedParId) {
                    var dataURL = "preferedAuthortityListView.htm?processid=3&fiscalYear=" + fiscialYear + "&parId=" + selectedParId;
                    $('#authoritymodalbody').load(dataURL, function() {
                        $('#setAuth').modal({show: true});
                        $(".loader").hide();
                    });
                } else {
                    alert("Select Employee");
                }

            }
            function opensearchAuthorityWindow() {
                $('#searchAuth').modal('show');
            }

            function validation() {
                if ($("#deptName").val() == "") {
                    alert("please Enter the department Name");
                    $("#deptName").focus();
                    return false;
                }
            }
            function validation() {
                if ($("#cadrecode").val() == "") {
                    alert("please Enter the cadre Name");
                    $("#cadrecode").focus();
                    return false;
                }
            }
            function validationpost() {
                if ($("#deptName").val() == "") {
                    alert("please Enter the department Name");
                    $("#deptName").focus();
                    return false;
                }
            }
            function saveCadreInfo() {

                tCadrecode = $("#cadrecode").val();
                tParId = $("input[name='rdEmp']:checked").val();
                tEmpGroup = $("input[name='empGroup']:checked").val();

                if (tCadrecode && tParId && tEmpGroup) {
                    $.post("updateCadredetail.htm", {cadrecode: tCadrecode, parId: tParId, empGroup: tEmpGroup})
                            .done(function(data) {
                                alert("Saved Sucessfully");

                            })
                } else if (!tCadrecode) {
                    alert("Choose Cadre");
                } else if (!tEmpGroup) {
                    alert("Choose Group");
                }
            }
            function saveMapPost() {

                tempId = $("input[name='rdEmp']:checked").val();
                tdeptNamemappost = $("#deptNamemappostEmp").val();
                toffcodemappost = $("#offcodemappostEmp").val();
                tpostCodemappost = $("#postCodemappostEmp").val();
                alert(tpostCodemappost);
                alert(tempId);
                if (tempId && toffcodemappost && tpostCodemappost) {
                    alert("called");
                    $.post("saveMapPost.htm", {parId: tempId, offcode: toffcodemappost, spc: tpostCodemappost})
                            .done(function(data) {
                                alert("Saved Sucessfully");

                            })
                } else if (!tdeptNamemappost) {
                    alert("Choose department Name");

                }

            }
            function getauthority() {
                var deptCode = $('#deptNamemappost').val();
                var offCode = $('#offcodemappost').val();
                var spc = $('#appriseSPC').val();
                $.post("getPARPostListJSON.htm", {deptCode: deptCode, offCode: offCode, spc: spc})
                        .done(function(data) {
                            $("#preferedauthgrid").empty();
                            var postList = data.rows;
                            alert(postList.length);
                            for (var i = 0; i < postList.length; i++) {
                                var row = '<tr>';
                                row = row + '<td><input type="checkbox" name="preferedpostcode" value="' + postList[i].postcode + '"/></td>';
                                row = row + '<td>' + postList[i].post + '</td>';
                                row = row + '</tr>';
                                $("#preferedauthgrid").append(row);
                            }
                        })

            }
            function savePreferedAuthorityInfo() {
                var preferedpostcode = "";
                var deptCode = $('#deptNamemappost').val();
                var offCode = $('#offcodemappost').val();
                var spc = $('#appriseSPC').val();
                var vpreferedpostcode = $("input[name='preferedpostcode']:checked");
                for (var i = 0; i < vpreferedpostcode.length; i++) {
                    preferedpostcode = preferedpostcode + $(vpreferedpostcode[i]).val() + ",";
                }
                $.post("addAuthoritySPCJson.htm", {preferedpostcode: preferedpostcode, hidDeptCode: deptCode, hidOffCode: offCode, spc: spc})
                        .done(function(data) {
                            alert("Saved Sucessfully");
                            $('#searchAuth').modal('hide');

                            var selectedParId = $("input[name='rdEmp']:checked").val();
                            var fiscialYear = $("#fiscalyear").val();

                            var dataURL = "preferedAuthortityListView.htm?processid=3&fiscalYear=" + fiscialYear + "&parId=" + selectedParId;
                            $('#authoritymodalbody').load(dataURL, function() {
                                $('#setAuth').modal({show: true});
                            });


                        })

            }
            function getparList(fiscalyear) {
                var fiscalyear = $("#fiscalyear").val();
                if ($("#fiscalyear").val() == "") {
                    alert("please Choose the Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                } else {
                    alert(fiscalyear);
                    window.location = "groupwiseParStatementReport.htm?fiscalyear=" + fiscalyear;
                }

            }

            function validateSearchInput(event) {
                var selectedValue = document.getElementById("searchCriteria").value;
                var input = event.target.value;

                var regex;

                switch (selectedValue) {
                    case "empname":
                        // Allow only characters and spaces
                        regex = /^[A-Z]*$/;
                        if (!regex.test(input)) {
                            alert("Please enter only characters in Capital Letter.");
                            event.target.value = "";
                        }
                        break;
                    case "empid":
                        // Allow only digits
                        regex = /^[0-9]*$/;
                        if (!regex.test(input)) {
                            alert("Please enter only digits.");
                            event.target.value = "";
                        }
                        break;
                    case "gpfno":
                        // Allow only digits
                        regex = /^[0-9A-Za-z\s]*$/;
                        if (!regex.test(input)) {
                            alert("Please enter GPFNO which contains either digits or letters or a combination of both.");
                            event.target.value = "";
                        }
                        break;
                    case "dob":
                        // Allow only date of birth format (dd-mm-yyyy)
                        regex = /^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-\d{4}$/;
                        if (!regex.test(input)) {
                            // Only trigger validation if full date is entered
                            if (input.length === 10) {
                                alert("Please enter date of birth in format dd-mm-yyyy.");
                                event.target.value = "";
                            }
                        }
                        break;
                    case "mobileno":
                        // Allow only digits
                        regex = /^[0-9]*$/;
                        if (!regex.test(input)) {
                            alert("Please enter digits Only");
                            event.target.value = "";
                        }
                        break;
                    default:
                        break;
                }
            }

            function comboBoxChanged() {
                var selectedValue = document.getElementById("searchCriteria").value;
                var searchInputField = document.getElementById("searchString");
                searchInputField.value = "";
                searchInputField.disabled = false; // Enable the search input field

                switch (selectedValue) {
                    case "empname":
                        $("#searchString").attr("placeholder", "CAPITAL LETTER ONLY");
                        searchInputField.addEventListener("input", validateSearchInput);
                        break;
                    case "dob":
                        $("#searchString").attr("placeholder", "dd-mm-yyyy");
                        searchInputField.addEventListener("input", validateSearchInput);
                        break;
                    case "lastname":
                        $("#searchString").attr("placeholder", "Enter Last Name");
                        searchInputField.addEventListener("input", validateSearchInput);
                        break;
                    case "gpfno":
                        $("#searchString").attr("placeholder", "Enter GPFNO");
                        searchInputField.addEventListener("input", validateSearchInput);
                        break;
                    case "empid":
                        $("#searchString").attr("placeholder", "Enter EMPID");
                        searchInputField.addEventListener("input", validateSearchInput);
                        break;
                    case "mobileno":
                        $("#searchString").attr("placeholder", "Enter Mobile Number");
                        searchInputField.addEventListener("input", validateSearchInput);
                        break;
                    default:
                        // Disable the search input field and remove input event listener
                        searchInputField.disabled = true;
                        searchInputField.removeEventListener("input", validateSearchInput);
                        break;
                }
            }

        </script>
    </head>

    <body style="margin-top:0px;background:#188B7A;">
        <jsp:include page="../tab/ParMenu.jsp"/> 
        <div id="wrapper"> 
            <div id="page-wrapper" style="margin-top:80px;z-index:0;padding: 20px 19px;">

                <div class="row">
                    <div class="col-lg-12">                            
                        <ol class="breadcrumb">
                            <li>
                                <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                            </li>
                            <li class="active">
                                <i class="fa fa-file"></i> PAR List 
                            </li>                                
                        </ol>
                    </div>
                </div>
                <div class="row" style="margin-bottom: 10px;">                        
                    <div class="col-lg-2">
                        <select name="fiscalyear" id="fiscalyear" class="form-control">
                            <option value="">Year</option>
                            <option value="all">All</option>
                        </select>                            
                    </div>
                    <div class="col-lg-1"><label>Search Criteria</label></div>
                    <div class="col-lg-2">
                        <select name="searchCriteria" id="searchCriteria" class="form-control" onchange="comboBoxChanged()">
                            <option value="">ALL</option>
                            <option value="empid">Employee Id</option>
                            <option value="empname">First Name</option>  
                            <option value="gpfno">GPF/PRAN NO</option>
                            <option value="dob">DOB</option>
                            <option value="lastname">Last Name</option>
                            <option value="mobileno">Mobile No</option>

                        </select>
                    </div>
                    <div class="col-lg-2"><input type="text" name="searchString" id="searchString" class="form-control"> </div>

                    <div class="col-lg-1">
                        <select name="searchParStatus" id="searchParStatus" class="form-control">
                            <option value="">status</option>
                        </select>                            
                    </div>
                    <div class="col-lg-1">
                        <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return searchPar()">Search</button>                            
                    </div>                    
                    <div class="col-lg-1"><span id="pagingstatus"></span></div>
                    <div class="col-lg-1">
                        <input type="text" name="paging" id="paging" class="form-control">                            
                    </div>
                    <div class="col-lg-1">
                        <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return gotoPage()">Go</button>
                    </div>
                </div>

                <div class="row" style="margin-bottom: 10px;">                        
                    <div class="col-lg-1"><label>Total Reviewed</label></div>
                    <div class="col-lg-2">
                        <select name="searchCriteria1" id="searchCriteria1" class="form-control">
                            <option value="">ALL</option>
                            <option value="reviewed">Reviewed</option>
                            <option value="nonreviewed">Non Reviewed</option>  
                        </select>
                    </div>
                    <div class="col-lg-2"><input type="text" name="searchString1" id="searchString1" class="form-control"> </div>
                    <div class="col-lg-1">
                        <button type="submit" class="form-control btn-primary" id="searchreviewedbtn" onclick="return searchReviewedParList()">Select</button> 
                    </div>
                    <div class="col-lg-1"> <div class="loader"></div> </div>
                    <div class="col-lg-1">
                        <button class="btn-primary" onclick="getparList('${fiscalyear}');">Par Statement Report</button>
                    </div>
                </div>

                <div class="row" style="margin-bottom: 10px;">
                    <div class="col-lg-10">
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary" onclick="showChangeCadreWindow()">Set Cadre</button>
                            <button type="button" class="btn btn-primary" onclick="opensetAuthorityWindow()">Set Authority</button>
                            <button type="button" class="btn btn-primary" onclick="openMapPostWindow()">Map Post</button> 
                        </div> 
                    </div>
                </div>
                <div class="row" style="margin-bottom: 10px;">
                    <div class="col-lg-10">
                        <div class="btn-group">
                            <a href="parCadreChangeDetail.htm"><button type="button" class="btn btn-primary">Cadre Change Detail</button></a><img src="images/new_icon.gif" height="35" style="vertical-align:top;margin-top:-5px;" />
                            <a href="parViewedByDetailList.htm"><button type="button" class="btn btn-primary">PAR Viewed Detail List</button></a><img src="images/new_icon.gif" height="35" style="vertical-align:top;margin-top:-5px;" />
                        </div> 
                    </div>
                </div>

                <div class="row">
                    <div class="col-lg-12">                            
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th width="1%"></th> 
                                        <th width="1%">Sl</th>
                                        <th width="1%">Financial Year</th>
                                        <th width="6%">Employee Id</th> 
                                        <th width="8%">GPF No</th> 
                                        <th width="15%">Employee Name</th>                    
                                        <th width="20%">Designation</th>
                                        <th width="8%">Date of Birth</th>
                                        <th width="5%" align="center">Group</th>
                                        <th width="10%">Cadre</th>
                                        <th width="10%">Current Office</th>
                                        <th width="10%">Mobile</th>
                                        <th width="20%">Status</th>                                            
                                        <th>Action</th> 
                                    </tr>
                                </thead>
                                <tbody id="pardatagrid">                                        

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="myModal" role="dialog" >
            <div class="modal-dialog  modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">View PAR Detail</h4>
                    </div>
                    <div class="modal-body myModalBody">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <div id="setCadre" class="modal fade" >
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Set Cadre List</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-sm-3">Department Name </label>
                                <div class="col-sm-9">
                                    <select class="form-control" name="deptName" id="deptName">
                                        <option value="">Select</option>
                                        <c:forEach items="${departmentList}" var="department">
                                            <option value="${department.deptCode}">${department.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-3">Cadre Name</label>
                                <div class="col-sm-9">
                                    <select class="form-control" name="cadrecode" id="cadrecode">
                                        <option value="">Select</option>                                            
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-3">Post Group</label>
                                <label class="radio-inline">
                                    <input type="radio" value="A" name="empGroup">A
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" value="B" name="empGroup">B
                                </label>
                            </div>
                        </form>
                    </div>

                    <div class="modal-footer">
                        <%--   <form:form action="viewPARAdmindetail.htm" >--%>
                        <div class="col-sm-10" >
                            <%--  <form:hidden path="empId"/> --%>

                            <a href="javascript:saveCadreInfo()" class="btn btn-primary">Save</a> 
                            <button type="button" class="btn btn-danger" data-dismiss="modal">cancel</button>
                        </div>
                        <%-- </form:form> --%>
                    </div>
                </div>
            </div>
        </div>

        <%-- Set Authority modal--%>
        <div id="setAuth" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Authority Window</h4>
                    </div>
                    <div class="modal-body" id="authoritymodalbody">

                    </div>
                    <div class="modal-footer">

                        <div class="col-sm-10" >
                            <button type="button" class="btn btn-primary" onclick="opensearchAuthorityWindow()">Search</button>
                            <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%-- Search Authority Modal--%>
        <div id="searchAuth" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Authority Window</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-sm-2">Department Name: </label>
                                <div class="col-sm-8">
                                    <select class="form-control" name="deptNamemappost" id="deptNamemappost">
                                        <option value="">Select</option>
                                        <c:forEach items="${departmentList}" var="department">
                                            <option value="${department.deptCode}">${department.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Office Name</label>
                                <div class="col-sm-8">
                                    <select class="form-control" name="offcodemappost" id="offcodemappost">
                                        <option value="">Select</option>                                            
                                    </select>
                                </div>
                                <div class="col-sm-2">
                                    <button type="button" class="btn btn-primary" onclick="getauthority()">Get Authority</button>
                                </div>
                            </div>
                            <div class="form-group" style="height: 500px;overflow: auto;">
                                <table class="table" style="width:100%">
                                    <thead>
                                        <tr>
                                            <th>Check</th>
                                            <th>Authority</th>
                                        </tr>
                                    </thead>
                                    <tbody id="preferedauthgrid">
                                        <tr>
                                            <td></td>
                                            <td></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </form>
                        <div class="modal-footer">
                            <div class="col-sm-10" >
                                <a href="javascript:savePreferedAuthorityInfo()" class="btn btn-primary">Save</a> 
                                <button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%-- modal for Map Post--%>
        <div id="setMap" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Map Post</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label class="control-label col-sm-2">Department Name: </label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="deptNamemappostEmp" id="deptNamemappostEmp">
                                        <option value="">Select</option>
                                        <c:forEach items="${departmentList}" var="department">
                                            <option value="${department.deptCode}">${department.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Office Name</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="offcodemappostEmp" id="offcodemappostEmp">
                                        <option value="">Select</option>                                            
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Substantive Post:</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="postCodemappostEmp" id="postCodemappostEmp">
                                        <option value="">Select</option>                                            
                                    </select>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <a href="javascript:saveMapPost()" class="btn btn-primary">Save</a> 
                        <a href="#" class="btn btn-info btn-lg">
                            <span class="glyphicon glyphicon-remove" data-dismiss="modal"></span> cancel 
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>


