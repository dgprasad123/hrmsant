<%-- 
    Document   : PropertyStatementReportForDDO
    Created on : 3 Feb, 2023, 12:23:29 PM
    Author     : Manisha
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
        <script type="text/javascript">

            var rowSize = 20;
            var totalPages = 0;
            $(document).ready(function() {

                $('#dobwise').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

                $("#showalldobString").hide();
                $("#showallString").hide();

                $(".loader").hide();
                $.post("GetFiscalYearListCYWiseJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });
            });
            function searchPropertyStatementList() {
                var fiscalyear = $("#fiscalyear").val();
                var offcode = $("#offcode").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString;
                if (searchCriteria == "dob") {
                    searchString = $("#dobwise").val();
                } else {
                    searchString = $("#searchString").val();
                }
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                if (offcode == "") {
                    alert("Please Choose Any Office");
                    $("#offcode").focus();
                    return false;
                }
                $("#pagingstatus").text("");
                $("#propertydatagrid").empty();
                $(".loader").show();
                $("#searchbtn").attr("disabled", true);
                $.getJSON("getSearchPROPERTYListForDDO.htm", {fiscalyear: fiscalyear, offcode: offcode, searchCriteria: searchCriteria, searchString: searchString, page: 1, rows: rowSize})
                        .done(function(data) {
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
                        .fail(function(jqxhr, textStatus, error) {
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
                var offcode = $("#offcode").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                $.post("getSearchPROPERTYListForDDO.htm", {fiscalyear: fiscalyear, offcode: offcode, searchCriteria: searchCriteria, searchString: searchString, page: pageNo, rows: rowSize})
                        .done(function(data) {
                            $("#pagingstatus").text(pageNo + " of " + totalPages);
                            var propertyList = data.propertyList;
                            populateDataInGrid(propertyList);
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
            }


            function populateDataInGrid(propertyList) {
                for (var i = 0; i < propertyList.length; i++) {
                    var row = '<tr>' +
                            '<td>' + propertyList[i].row + '</td>' +
                            '<td>' + propertyList[i].empid + '</td>' +
                            '<td>' + propertyList[i].gpfno + '</td>' +
                            '<td>' + propertyList[i].empname + '</td>' +
                            '<td>' + propertyList[i].post + '</td>' +
                            '<td>' + propertyList[i].dob + '</td>' +
                            '<td>' + propertyList[i].postgroup + '</td>' +
                            '<td>' + propertyList[i].cadreName + '</td>' +
                            '<td>' + propertyList[i].currentoffice + '</td>' +
                            '<td>' + propertyList[i].mobile + '</td>';
                    if (propertyList[i].status == "NOT SUBMITTED") {
                        row = row + '<td>' + '<span class="label label-danger">Not Submitted</span></td>';

                    } else if (propertyList[i].status == "SUBMITTED") {
                        row = row + '<td>' + '<span class="label label-success">Submitted</span></td>';
                    } else {
                        row = row + '<td>' + '<span class="label label-primary">Not Created</span></td>';
                    }
                    if (propertyList[i].submittedon != "") {
                        row = row + '<td>' + propertyList[i].submittedon + '</td>';
                    } else {
                        row = row + '<td>' + '';
                    }
                    if (propertyList[i].status == "NOT SUBMITTED" || propertyList[i].status == "PROPERTY NOT CREATED") {
                        row = row + '<td>' + '';

                    } else {
                        row = row + '<td>' + '<a href="viewPropertyStatementPDFForDDO.htm?yearlyPropId=' + propertyList[i].yearlyPropertyId + '"  class="btn-default" target="_blank"><button type="button" class="btn btn-primary">Download</button></a></td>';
                    }
                    '<td>' + propertyList[i].status + '</td>';
                    row = row + '</tr>';
                    $("#propertydatagrid").append(row);
                }
                $('.openPopup').on('click', function() {
                    var dataURL = $(this).attr('data-href');
                    $('.myModalBody').load(dataURL, function() {
                        $('#myModal').modal({show: true});
                    });
                });
            }
            function getpropertyList(fiscalyear) {
                var fiscalyear = $("#fiscalyear").val();
                if ($("#fiscalyear").val() == "") {
                    alert("please Choose the Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                } else {
                    alert(fiscalyear);
                    window.location = "groupwisePropertyStatementReport.htm?fiscalyear=" + fiscalyear;
                }

            }
            function validation() {
                var searchCriteria = $("#searchCriteria").val();
                if (searchCriteria == "dob") {
                    $("#showalldobString").show();
                    $("#showallString").hide();
                } else {
                    $("#showallString").show();
                    $("#showalldobString").hide();
                }
            }

        </script>
    </head>


    <body style="background-color: #FFFFFF;">
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading" align="center" style="background-color: #0071c5;color: #ffffff;font-size: xx-large;"> Property Statement Report</div>

                <div class="panel-body">
                    <div class="row" style="margin-bottom: 18px;">                        
                        <div class="col-lg-1">
                            <select name="fiscalyear" id="fiscalyear" class="form-control">
                                <option value="">Year</option>
                            </select> 	                           
                        </div>
                        <div class="col-lg-1"><label>Search Criteria</label></div>
                        <div class="col-lg-2">
                            <select name="searchCriteria" id="searchCriteria" class="form-control" onclick="return validation()">
                                <option value="">ALL</option>
                                <option value="empid">Employee Id</option>
                                <option value="empname">First Name</option>  
                                <option value="gpfno">GPF/PRAN NO</option>
                                <option value="mobile">Mobile</option>
                                <option value="dob">Date Of Birth</option>

                            </select>
                        </div>
                        <div class="col-lg-2" id="showallString">
                            <input type="text" name="searchString" id="searchString" class="form-control"> 
                        </div>
                        <div class="col-lg-2" id="showalldobString">
                            <input type="text" name="dob" id="dobwise" class="form-control" readonly="true"/>  
                        </div>

                        <div class="col-lg-1"><label>Office Wise</label></div>
                        <div class="col-sm-2">
                            <select name="offcode" id="offcode" class="form-control">
                                <option value="">ALL</option>
                                <c:forEach items="${officeListDDO}" var="officeList">
                                    <option value="${officeList.offcode}">${officeList.curOfficeName}</option>
                                </c:forEach>
                            </select>                                
                        </div>
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
                    </div>
                </div>

                <div class="row">
                    <div class="col-lg-12">                            
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
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
                                        <th width="20%">Submitted On</th>
                                        <th width="20%">Action</th>
                                    </tr>
                                </thead>
                                <tbody id="propertydatagrid">                                        

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>


