<%-- 
    Document   : PropertyStatementAdmin
    Created on : 6 Aug, 2020, 11:42:41 AM
    Author     : Manisha
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">                
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
            $(document).ready(function () {
                $(".loader").hide();
                $.post("GetFiscalYearListJSON.htm", function (data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });

            });
            function searchPropertyStatementList() {
                var fiscalyear = $("#fiscalyear").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                $("#pagingstatus").text("");
                $("#propertydatagrid").empty();
                $(".loader").show();
                $("#searchbtn").attr("disabled", true);
                $.getJSON("getSearchPROPERTYList.htm", {fiscalyear: fiscalyear, searchCriteria: searchCriteria, searchString: searchString, page: 1, rows: rowSize})
                        .done(function (data) {
                            var totalPropertyFound = data.totalPropertyFound;
                            totalPages = Math.round(totalPropertyFound / rowSize);
                            $("#pagingstatus").text("1 of " + totalPages);
                            $("#paging").val(1);
                            if (data.propertyList) {
                                var propertyList = data.propertyList;
                                populateDataInGrid(propertyList);
                            } else {
                                alert("Error Occured");
                            }
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
                        .fail(function (jqxhr, textStatus, error) {
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                            alert("Error Occured");
                        });
            }

            function gotoPage() {
                var pageNo = $("#paging").val();
                if (pageNo == "") {
                    alert("Insert Page No");
                    $("#paging").focus();
                    return false;
                }
                $("#propertydatagrid").empty();
                $(".loader").show();
                $("#searchbtn").attr("disabled", true);
                var fiscalyear = $("#fiscalyear").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                $.post("getSearchPROPERTYList.htm", {fiscalyear: fiscalyear, searchCriteria: searchCriteria, searchString: searchString, page: pageNo, rows: rowSize})
                        .done(function (data) {
                            $("#pagingstatus").text(pageNo + " of " + totalPages);
                            var propertyList = data.propertyList;
                            populateDataInGrid(propertyList);
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
            }

            function unLock() {
                var yearlyPropertyId = $("input[name='yearlyPropertyId']:checked").val();

                if (yearlyPropertyId) {
                    if (confirm("Are you sure to unlock?")) {
                        $.getJSON("updatePROPERTYList.htm", {yearlyPropertyId: yearlyPropertyId})
                                .done(function (data) {
                                    if (data.msg == 'Property Statement is Unlocked') {
                                        var objtd = $("input[name='yearlyPropertyId']:checked").parent().siblings()[10];
                                        $(objtd).html("NOT SUBMITTED");
                                    }
                                    alert(data.msg);
                                })
                                .fail(function (jqxhr, textStatus, error) {
                                    $(".loader").hide();
                                    $("#unlockbtn").attr("disabled", false);
                                    alert("Error Occured");
                                });


                    }
                } else {
                    alert("Select a Row");
                }
            }



            function populateDataInGrid(propertyList) {
                for (var i = 0; i < propertyList.length; i++) {
                    var row = '<tr>' +
                            '<td><input type = "radio" name="yearlyPropertyId" value=' + propertyList[i].yearlyPropertyId + '> </td>' +
                            '<td>' + propertyList[i].row + '</td>' +
                            '<td>' + propertyList[i].empid + '</td>' +
                            '<td>' + propertyList[i].gpfno + '</td>' +
                            '<td>' + propertyList[i].empname + '</td>' +
                            '<td>' + propertyList[i].post + '</td>' +
                            '<td>' + propertyList[i].dob + '</td>' +
                            '<td>' + propertyList[i].postgroup + '</td>' +
                            '<td>' + propertyList[i].cadreName + '</td>' +
                            '<td>' + propertyList[i].currentoffice + '</td>' +
                            '<td>' + propertyList[i].mobile + '</td>' +
                            '<td>' + propertyList[i].status + '</td>';

                    row = row + '</tr>';
                    $("#propertydatagrid").append(row);
                }
                $('.openPopup').on('click', function () {
                    var dataURL = $(this).attr('data-href');
                    $('.myModalBody').load(dataURL, function () {
                        $('#myModal').modal({show: true});
                    });
                });


            }

        </script>
    </head>
    <body style="margin-top: 10px;">        
        <div class="container-fluid">            
            <div class="row" style="margin-bottom: 10px;">                        
                <div class="col-lg-1">
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
                        <option value="mobile">Mobile</option>

                    </select>
                </div>
                <div class="col-lg-2"><input type="text" name="searchString" id="searchString" class="form-control"> </div>

                <div class="col-lg-1">
                    <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return searchPropertyStatementList()">Search</button>                            
                </div>     
                <div class="col-lg-1"> 
                    <div class="loader"></div> 
                    <span id="pagingstatus"></span>
                </div>                                       
                <div class="col-lg-1">
                    <input type="text" name="paging" id="paging" class="form-control">                            
                </div>
                <div class="col-lg-1">
                    <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return gotoPage()">Go</button>                        
                </div>
                <div class="col-lg-1">
                    <button type="submit" class="form-control btn-danger" id="unlockbtn" onclick="return unLock()">Unlock</button>
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
                                    <th width="5%" align="center">Group</th>
                                    <th width="10%">Cadre</th>
                                    <th width="10%">Current Office</th>
                                    <th width="10%">Mobile</th>
                                    <th width="20%">Status</th>                                            
                                </tr>
                            </thead>
                            <tbody id="propertydatagrid">                                        

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>


