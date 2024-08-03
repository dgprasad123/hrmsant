<%-- 
    Document   : PARViewer
    Created on : 26 Nov, 2020, 1:22:12 PM
    Author     : Manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


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
            .tblTrColor{
                background: rgb(174,238,209);
                background: radial-gradient(circle, rgba(174,238,209,0.9976191160057774) 0%, rgba(148,231,233,1) 100%);
                color: #000000;
                font-weight: bold;
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
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                var searchParStatus = $("#searchParStatus").val();
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
                searchParStatus = $("#searchParStatus").val();
                $.post("getSearchPARList.htm", {fiscalyear: fiscalyear, searchParStatus: searchParStatus, searchCriteria: searchCriteria, searchString: searchString, page: pageNo, rows: rowSize})
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
                for (var i = 0; i < parlist.length; i++) {
                    var row = '<tr>' +
                            '<td>' + parlist[i].pageNo + '</td>' +
                            '<td>' + parlist[i].fiscalyear + '</td>' +
                            '<td style="color:#0000FE;">' + parlist[i].empId + ',<br />' + parlist[i].gpfno + '</td>' +
                            '<td>' + parlist[i].empName + ',<br />' + parlist[i].postName + '</td>' +
                            '<td>' + parlist[i].dob + '</td>' +
                            '<td>' + parlist[i].groupName + '</td>' +
                            '<td>' + parlist[i].cadreName + '</td>' +
                            '<td>' + parlist[i].currentoffice + '</td>' +
                            '<td style="color:#0000FE;">' + parlist[i].mobile + '</td>' +
                            '<td>' + parlist[i].prdFrmDate + '</td>' +
                            '<td>' + parlist[i].prdToDate + '</td>' +
                            '<td style="color:#0000FE;">' + parlist[i].parstatus + ',<br />' + parlist[i].pendingAtAuthName + ',<br />' + parlist[i].pendingAtSpc + '</td>' +
                            '<td>' + parlist[i].parPendingDateFrom + '</td>' +
                            '<td><a href="javascript:void(0);" data-href="viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + $("#fiscalyear").val() + '" class="openPopup">View</a></td>' +
                            //'<td><a href="javascript:void(0);" onclick="javascript: window.open(\'viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + $("#fiscalyear").val() +'\', \'\', \'width=600,height=800,top=200,left=200,menubar=0\')">View</a>' +
                            '</tr>';
                    $("#pardatagrid").append(row);
                }
                $('.openPopup').on('click', function() {
                    if ($("#authorizationType").val() == "S") {
                        alert("You don't have privilige to view PAR");
                    } else {
                        var dataURL = $(this).attr('data-href');
                        $('.myModalBody').load(dataURL, function() {
                            $('#myModal').modal({show: true});
                        });
                    }
                });
            }
            function confirmDownload() {
                var privilegedSpc = $("#privilegedSpc").val();
                var fiscalyear = $("#fiscalyear").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                var searchParStatus = $("#searchParStatus").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;

                }
                //window.location = "pendingPARAtAuthorityExcelView.htm?fiscalyear=" + fiscalyear + "&searchCriteria=" + searchCriteria + "&searchString=" + searchString + "&searchParStatus=" + searchParStatus;
                window.location = "downloadpendingPARAtAuthorityExcelView.htm?fiscalyear=" + fiscalyear + "&searchCriteria=" + searchCriteria + "&searchString=" + searchString + "&searchParStatus=" + searchParStatus;
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

        </script>
    </head>



    <body style="margin-top:0px;background:#188B7A;">        
        <div id="wrapper" style="padding-left: 0px;"> 
            <div id="page-wrapper">                
                <div class="row" style="margin-bottom: 10px;">                        
                    <div class="col-lg-2">
                        <select name="fiscalyear" id="fiscalyear" class="form-control">
                            <option value="">Year</option>
                            <option value="all">All</option>
                        </select>                            
                    </div>
                    <div class="col-lg-1"><label>Search Criteria</label></div>
                    <div class="col-lg-2">
                        <select name="searchCriteria" id="searchCriteria" class="form-control">
                            <option value="">ALL</option>
                            <option value="empid">Employee Id</option>
                            <option value="empname">First Name</option>  
                            <option value="gpfno">GPF/PRAN NO</option>
                            <option value="dob">DOB</option>

                        </select>
                    </div>
                    <input type="hidden" name="authorizationType" id="authorizationType" value="${pap.authorizationType}" />
                    <div class="col-lg-2"><input type="text" name="searchString" id="searchString" class="form-control"> </div>

                    <div class="col-lg-1">
                        <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return searchPar()">Search</button>                            
                    </div>
                    <div class="col-lg-1"> <div class="loader"></div> </div>
                    <div class="col-lg-1"><span id="pagingstatus"></span></div>
                    <div class="col-lg-1">
                        <input type="text" name="paging" id="paging" class="form-control">                            
                    </div>
                    <div class="col-lg-1">
                        <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return gotoPage()">Go</button>
                    </div>
                </div>
                <div class="row" style="margin-bottom: 10px;">
                    <div class="col-lg-2">
                        <select name="searchParStatus" id="searchParStatus" class="form-control">
                            <option value="">ALL</option>
                        </select>                            
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-info btn-md" target="_blank" onclick="confirmDownload('${fiscalyear}');">Download</button>
                       <%-- <input type="button" name="action" value="Download" class="btn btn-primary" onclick="confirmDownload()"/> --%>
                    </div>
                </div>


                <div class="row">
                    <div class="col-lg-12">                            
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead class="tblTrColor">
                                    <tr>
                                        <th width="1%">Sl</th> 
                                        <th width="6%">Financial Year</th>
                                        <th width="6%">Employee Id<br>GPF/PRAN NO</th> 
                                        <th width="15%">Employee Name<br>Designation</th>                    
                                        <th width="8%">Date of Birth</th>
                                        <th width="5%" align="center">Group</th>
                                        <th width="10%">Cadre</th>
                                        <th width="10%">Office Name</th>
                                        <th width="10%">Mobile</th>
                                        <th width="10%">PAR Period From</th>
                                        <th width="10%">PAR Period To</th>
                                        <th width="20%">Status<br>Pending At Authority with Designation</th> 
                                        <th width="20%">Pending At Authority From</th>
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

    </body>
</html>


