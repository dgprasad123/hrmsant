<%-- 
    Document   : PARCustdian
    Created on : Dec 16, 2019, 11:53:04 AM
    Author     : manisha
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true"  buffer="64kb"%>


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
            function confirmDownload() {
                var privilegedSpc = $("#privilegedSpc").val();
                var fiscalyear = $("#fiscalyear").val();
                var cadrecode = $("#cadrecode").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                var searchParStatus = $("#searchParStatus").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                window.location = "pendingPARAtAuthorityExcelView.htm?fiscalyear=" + fiscalyear + "&cadrecode=" + cadrecode + "&searchCriteria=" + searchCriteria + "&searchString=" + searchString + "&searchParStatus=" + searchParStatus;
            }

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
            });
            function searchPar() {
                var fiscalyear = $("#fiscalyear").val();
                var cadrecode = $("#cadrecode").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                var searchParStatus = $("#searchParStatus").val();
                var searchCriteria1 = $("#searchCriteria1").val();
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
                $.post("getSearchPARList.htm", {fiscalyear: fiscalyear, cadrecode: cadrecode, searchCriteria: searchCriteria, searchString: searchString, searchParStatus: searchParStatus, searchCriteria1: searchCriteria1, page: 1, rows: rowSize})
                        .done(function(data) {
                            var totalPARFound = data.totalPARFound;
                            totalPages = Math.round(totalPARFound / rowSize) + 1;
                            alert(totalPages);
                            if (totalPages > 0) {
                                $("#pagingstatus").text("1 of " + totalPages);
                            } else if ((totalPARFound % rowSize) > 0) {
                                $("#pagingstatus").text("1 of 1");
                            }
                            $("#paging").val(1);
                            if (data.parlist) {
                                var parlist = data.parlist;
                                populateDataInGrid(parlist);
                            } else {
                                alert("Error Occured");
                            }
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
                        .fail(function() {
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                            alert("Error Occured");
                        })
            }

            function searchReviewedParList() {
                var fiscalyear = $("#fiscalyear").val();
                var searchParStatus = $("#searchParStatus").val();
                var searchCriteria1 = $("#searchCriteria1").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                $(".loader").show();
                $("#searchreviewedbtn").attr("disabled", true);
                $.post("getSearchReviewedPARList.htm", {fiscalyear: fiscalyear, searchParStatus: searchParStatus, searchCriteria1: searchCriteria1, page: 1, rows: rowSize})
                        .done(function(data) {
                            var totalPARFound = data.totalPARFound;
                            totalPages = Math.round(totalPARFound / rowSize) + 1;
                            alert("totalPages");
                            if (totalPages > 0) {
                                $("#pagingstatus").text("1 of " + totalPages);
                            } else if ((totalPARFound % rowSize) > 0) {
                                $("#pagingstatus").text("1 of 1");
                            }
                            $("#paging").val(1);
                            if (data.parlist) {
                                var parlist = data.parlist;
                                populateDataInGrid(parlist);
                            } else {
                                alert("Error Occured");
                            }
                            $(".loader").hide();
                            $("#searchreviewedbtn").attr("disabled", false);
                        })
                        .fail(function() {
                            $(".loader").hide();
                            $("#searchreviewedbtn").attr("disabled", false);
                            alert("Error Occured");
                        })
            }
            function searchAdverseParList() {
                var fiscalyear = $("#fiscalyear").val();
                var searchParStatus = $("#searchParStatus").val();
                var searchCriteria2 = $("#searchCriteria2").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                $(".loader").show();
                $("#searchadversedbtn").attr("disabled", true);
                $.post("getSearchAdversedPARList.htm", {fiscalyear: fiscalyear, searchParStatus: searchParStatus, searchCriteria2: searchCriteria2, page: 1, rows: rowSize})
                        .done(function(data) {
                            var totalPARFound = data.totalPARFound;
                            totalPages = Math.round(totalPARFound / rowSize);
                            $("#pagingstatus").text("1 of " + totalPages);
                            $("#paging").val(1);
                            if (data.parlist) {
                                var parlist = data.parlist;
                                populateDataInGrid(parlist);
                            } else {
                                alert("Error Occured");
                            }
                            $(".loader").hide();
                            $("#searchadversedbtn").attr("disabled", false);
                        })
                        .fail(function() {
                            $(".loader").hide();
                            $("#searchadversedbtn").attr("disabled", false);
                            alert("Error Occured");
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
                cadrecode = $("#cadrecode").val();
                searchParStatus = $("#searchParStatus").val();
                searchCriteria1 = $("#searchCriteria1").val();
                $.post("getSearchPARList.htm", {fiscalyear: fiscalyear, cadrecode: cadrecode, searchParStatus: searchParStatus, searchCriteria1: searchCriteria1, searchCriteria: searchCriteria, searchString: searchString, page: pageNo, rows: rowSize})
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
                    //alert(parlist[i].isreview);
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
                                '<td>' + parlist[i].parstatus + initiatedBy + '</td>' +
                                '<td><a href="javascript:void(0);" data-href="viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + parlist[i].fiscalyear + '" class="openPopup" style="color:white; background:##f8f9f5;">View</a></td>' +
                                //'<td><a href="javascript:void(0);" onclick="javascript: window.open(\'viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + $("#fiscalyear").val() +'\', \'\', \'width=600,height=800,top=200,left=200,menubar=0\')">View</a>' +
                                '</tr>';
                        $("#pardatagrid").append(row);
                    }  else if (parlist[i].isadversed == 'Y' && parlist[i].isreview == 'N') {
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
                                '<td>' + parlist[i].parstatus + initiatedBy + '</td>' +
                                '<td><a href="javascript:void(0);" data-href="viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + parlist[i].fiscalyear + '" class="openPopup">View</a></td>' +
                                //'<td><a href="javascript:void(0);" onclick="javascript: window.open(\'viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + $("#fiscalyear").val() +'\', \'\', \'width=600,height=800,top=200,left=200,menubar=0\')">View</a>' +
                                '</tr>';
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



            function getDeptWiseOfficeList() {
                var deptcode = $('#deptName').val();
                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                    });
                });
            }

            function getOfficeWisePostList(type) {
                var offcode = $('#offCode').val();
                var url = 'getTransferCadreWisePostListJSON.htm?offcode=' + offcode;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
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
            function openMapPostWindow() {
                $('#setMap').modal('show');
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
            /*function setSearchStringFormat(me) {
             if ($(me).val() == "dob") {
             $("#searchString").attr("placeholder", "dd-mm-yyyy");
             } else {
             $("#searchString").removeAttr("placeholder");
             }
             }*/


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
        <div id="wrapper" style="padding-left: 0px;"> 
            <div id="page-wrapper">

                <div class="row" style="margin-bottom: 10px;">
                    <div class="col-lg-9">


                        <div class="row" style="margin-bottom: 10px;">                        
                            <div class="col-lg-2">
                                <select name="fiscalyear" id="fiscalyear" class="form-control">
                                    <option value="">Year</option>
                                    <option value="all">All</option>
                                </select>                            
                            </div>
                            <div class="col-lg-2"><label>Search Criteria</label></div>
                            <div class="col-lg-2">
                                <select name="searchCriteria" id="searchCriteria" class="form-control" onchange="comboBoxChanged()">
                                    <option value="">ALL</option>
                                    <option value="empid">Employee Id</option>
                                    <option value="empname">First Name</option> 
                                    <option value="lastname">Last Name</option>
                                    <option value="gpfno">GPF/PRAN NO</option>
                                    <option value="dob">DOB</option>

                                </select>
                            </div>

                            <div class="col-lg-2">
                                <input type="text" name="searchString" id="searchString" class="form-control"> 
                            </div>

                            <div class="col-lg-1">
                                <label class="control-label">Cadre List:</label>
                            </div>
                            <div class="col-sm-2">
                                <select name="cadrecode" id="cadrecode" class="form-control">
                                    <option value="">ALL</option>
                                    <c:forEach items="${cadreList}" var="cadre">
                                        <option value="${cadre.cadreCode}">${cadre.cadreName}-${cadre.deptName}-${cadre.postGrp}</option>
                                    </c:forEach>
                                </select>                                
                            </div>

                        </div>
                        <div class="row" style="margin-bottom: 10px;">
                            <div class="col-lg-2">
                                <select name="searchParStatus" id="searchParStatus" class="form-control">
                                    <option value="">ALL</option>
                                </select>                            
                            </div>
                            <div class="col-lg-2"><label>Total Reviewed</label></div>
                            <div class="col-lg-2">
                                <select name="searchCriteria1" id="searchCriteria1" class="form-control">
                                    <option value="">ALL</option>
                                    <option value="reviewed">Reviewed</option>
                                    <option value="nonreviewed">Non Reviewed</option>  
                                    <option value="adverse">Adverse</option>  
                                </select>
                            </div>

                            <%-- <a href="getSearchAdversedPARList.htm">Total Adverse</a> --%>


                        </div>

                    </div>
                    <div class="col-lg-3">
                        <div class="row">
                            <div class="col-lg-3">
                                <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return searchPar()">Search</button>                            
                            </div>
                            <div class="col-lg-3"> <div class="loader"></div> <span id="pagingstatus"></span></div>                    
                            <div class="col-lg-3">
                                <input type="text" name="paging" id="paging" class="form-control">                            
                            </div>
                            <div class="col-lg-3">
                                <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return gotoPage()">Go</button>
                            </div>
                        </div>
                        <div class="row">

                        </div>
                        <div class="row">

                            <%--<div class="col-lg-6">
                                <button type="submit" class="form-control btn-primary" id="searchreviewedbtn" onclick="return searchReviewedParList()">Select</button> 
                            </div> --%>
                            <div class="col-lg-6">
                                <%--<a href="javascript:void(0)" onclick="confirmDownload()" target="_blank" class='text-info'><strong>Download</strong></a>--%>

                            </div>
                        </div>
                    </div>
                </div>

                <div class="row" style="margin-bottom: 10px;">
                    <div class="col-lg-10">
                        <div class="btn-group">
                            <%--  <button type="button" class="btn btn-primary" onclick="showChangeCadreWindow()">Set Cadre</button> 
                            <button type="button" class="btn btn-primary" onclick="opensetAuthorityWindow()">Set Authority</button>
                            <button type="button" class="btn btn-primary" onclick="openMapPostWindow()">Map Post</button>--%>
                            <input type="button" name="action" value="Download" class="btn btn-primary" onclick="confirmDownload()"/>
                        </div>
                        <%-- <a href="totalAdverseCommunicationList.htm"><button type="button" class="btn btn-primary">Adverse Communications</button></a> --%>
                    </div>
                </div>
                <%--  <form:form action="PreviousyearPARUpload.htm" commandName=""></form:form> --%>
                <div class="row" style="margin-bottom: 10px;">
                    <div class="col-lg-10">
                        <div class="btn-group">
                            <a href="uploadPreviousYearPARList.htm"><button type="button" class="btn btn-primary">Upload Previous Year PAR</button></a> 
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

        <%-- set cadre modal--%>
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
                                        <select class="form-control" name="deptName" id="deptName" onchange="getDeptWiseOfficeList();">
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
                                        <select class="form-control" name="offCode" id="offCode" onchange="getOfficeWisePostList();">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label col-sm-2">Substantive Post:</label>
                                    <div class="col-sm-10">
                                        <select class="form-control" name="postCode" id="postCode">
                                            <option value="">Select</option>                                            
                                        </select>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <a href="#" class="btn btn-info btn-lg">
                                <span class="glyphicon glyphicon-ok" onclick="return validationpost()"></span> Save
                            </a>
                            <a href="#" class="btn btn-info btn-lg">
                                <span class="glyphicon glyphicon-remove" data-dismiss="modal"></span> cancel 
                            </a>
                        </div>
                    </div>
                </div>
            </div>
    </body>
</html>


