<%-- 
    Document   : GroupCAdmin
    Created on : 24 Jan, 2022, 11:13:05 AM
    Author     : Manisha
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
                $.post("GetFiscalYearListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });
                $.post("getGroupCStatusListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#searchGroupCStatus').append($('<option>', {value: data[i].statusId, text: data[i].statusName}));
                    }
                });





            });
            function searchGroupCEmp() {
                var fiscalyear = $("#fiscalyear").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                var searchGroupCStatus = $("#searchGroupCStatus").val();
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
                $.post("getSearchGroupCDetailList.htm", {fiscalyear: fiscalyear, searchCriteria: searchCriteria, searchString: searchString, page: 1, rows: rowSize})
                        .done(function(data) {
                            var totalgroupCEmpFound = data.totalgroupCEmpFound;
                            totalPages = Math.round(totalgroupCEmpFound / rowSize);
                            $("#pagingstatus").text("1 of " + totalPages);
                            $("#paging").val(1);
                            var groupCEmplist = data.groupCEmplist;
                            populateDataInGrid(groupCEmplist);
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
            }





            function populateDataInGrid(groupCEmplist) {
                $("#groupCdatagrid").empty();
                for (var i = 0; i < groupCEmplist.length; i++) {

                    var row = '<tr>' +
                            '<td><input type = "radio" name="rdEmp" value=' + groupCEmplist[i].groupCpromotionId + '> </td>' +
                            '<td>' + groupCEmplist[i].pageNo + '</td>' +
                            '<td>' + groupCEmplist[i].reviewedempId + '</td>' +
                            '<td>' + groupCEmplist[i].gpfno + '</td>' +
                            '<td>' + groupCEmplist[i].reviewedempname + '</td>' +
                            '<td>' + groupCEmplist[i].reviewedpost + '</td>' +
                            '<td>' + groupCEmplist[i].dob + '</td>' +
                            '<td>' + groupCEmplist[i].reviewedEmpCurrentoffice + '</td>' +
                            '<td>' + groupCEmplist[i].groupCstatus + '</td>' +
                            '<td>' + groupCEmplist[i].mobile + '</td>';

                    row = row + '</tr>';
                    $("#groupCdatagrid").append(row);
                }
                $('.openPopup').on('click', function() {
                    var dataURL = $(this).attr('data-href');
                    $('.myModalBody').load(dataURL, function() {
                        $('#myModal').modal({show: true});
                    });
                });


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
                var fiscalyear = $("#fiscalyear").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                var searchGroupCStatus = $("#searchGroupCStatus").val();
                $.post("getSearchGroupCDetailList.htm", {fiscalyear: fiscalyear, searchCriteria: searchCriteria, searchString: searchString, page: pageNo, rows: rowSize})
                        .done(function(data) {
                            $("#pagingstatus").text(pageNo + " of " + totalPages);
                            var groupCEmplist = data.groupCEmplist;
                            populateDataInGrid(groupCEmplist);
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
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
                                <i class="fa fa-file"></i> Group C Employee List 
                            </li>                                
                        </ol>
                    </div>
                </div>
                <div class="row" style="margin-bottom: 10px;">                        
                    <div class="col-lg-2">
                        <select name="fiscalyear" id="fiscalyear" class="form-control">
                            <option value="">Year</option>
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
                            <option value="lastname">Last Name</option>
                            <option value="mobileno">Mobile No</option>

                        </select>
                    </div>
                    <div class="col-lg-2"><input type="text" name="searchString" id="searchString" class="form-control"> </div>

                    <div class="col-lg-1">
                        <select name="searchGroupCStatus" id="searchGroupCStatus" class="form-control">
                            <option value="">status</option>
                        </select>                            
                    </div>
                    <div class="col-lg-1">
                        <button type="button" class="form-control btn-primary" id="searchbtn" onclick="searchGroupCEmp()">Search</button>                            
                    </div>                    
                    <div class="col-lg-1"><span id="pagingstatus"></span></div>
                    <div class="col-lg-1">
                        <input type="text" name="paging" id="paging" class="form-control">                            
                    </div>
                    <div class="col-lg-1">
                        <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return gotoPage()">Go</button>
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
                                        <th width="6%">Employee Id</th> 
                                        <th width="8%">GPF No</th> 
                                        <th width="15%">Employee Name</th>                    
                                        <th width="20%">Designation</th>
                                        <th width="8%">Date of Birth</th>
                                        <th width="10%">Current Office</th>
                                        <th width="10%">Status</th>
                                        <th width="10%">Mobile</th>
                                    </tr>
                                </thead>
                                <tbody id="groupCdatagrid">                                        

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>



