<%-- 
    Document   : GroupCCustdianReport
    Created on : Jun 5, 2020, 11:36:18 AM
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
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                $(".loader").hide();
                $.post("GetFiscalYearListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });
                $.post("GetOfficePrivelegeListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#OfficeCode').append($('<option>', {value: data[i].offCode, text: data[i].offName}));
                    }
                });
                $.post("GetDistrictPriveligeListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#districtCode').append($('<option>', {value: data[i].distCode, text: data[i].distName}));
                    }
                });
            });
            function searchEmployee() {
                var fiscalyear = $("#fiscalyear").val();
                var districtCode = $("#districtCode").val();
                var offCode = $("#OfficeCode").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                $(".loader").show();
                $.post("getSearchEmpListForGroupC.htm", {fiscalyear: fiscalyear, distCode: districtCode, offCode: offCode})
                        .done(function(data) {
                            var groupCEmpList = data.groupCEmpList;
                            populateDataInGrid(groupCEmpList);
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                        })
            }
            function populateDataInGrid(groupCEmpList) {
                $("#groupcdatagrid").empty();
                emplist = groupCEmpList[0];
                for (var i = 0; i < emplist.length; i++) {
                    var row = '<tr>' +
                            '<td>' + (i + 1) + '</td>' +
                            '<td>' + emplist[i].reviewedempname + ',' + emplist[i].reviewedpost + '</td>';
                    if (emplist[i].isfitforShoulderingResponsibilityReporting == "Y") {
                        row = row + '<td>' + emplist[i].reportingempname + ',' + emplist[i].reportingpost + '<br>' +
                                ' <span class="label label-success">Fit For Shouldering Higher Responsibility</span> '

                                + '<span class="label label-primary">' + emplist[i].reportingondate + '</td>';
                    } else if (emplist[i].isfitforShoulderingResponsibilityReporting == "N") {
                        row = row + '<td>' + emplist[i].reportingempname + ',' + emplist[i].reportingpost + '<br>' +
                                '<br><span class="label label-danger"> Not Fit For Shouldering Higher Responsibility </span>,' + '<span class="label label-primary">' + emplist[i].reportingondate + '</span>'
                        + '<br>' + '<b>' +'(' + emplist[i].reportingRemarks + ')' + ' </b> ' +
                                '<a href = "downloadAttachmentOfGroupCForNotFit.htm?promotionId=' + emplist[i].promotionId + ' class = "btn btn-default">' +
                                ' <span class = "glyphicon glyphicon-paperclip"> </span>' + supressUndefined(emplist[i].originalFilename) +
                                ' </a>' +
                                ' </td>';

                    } else {
                        if (emplist[i].reportingempname == undefined) {
                            emplist[i].reportingempname = "";
                            row = row + '<td>' + emplist[i].reportingempname + '</td>';
                        } else {
                            row = row + '<td>' + emplist[i].reportingempname + ',' + emplist[i].reportingpost + '</td>';
                        }
                    }

                    if (emplist[i].isfitforShoulderingResponsibilityReviewing == "Y") {
                        row = row + '<td>' + emplist[i].reviewingempname + ',' + emplist[i].reviewingpost + '<br>' +
                             + '<br>' +   '<span class="label label-success">Fit For Shouldering Higher Responsibility</span>' + '<span class="label label-primary">' + emplist[i].reviewingondate + '</span>'
                        + '<br>' + '<b>' + '('+ supressUndefined(emplist[i].reviewingRemarks) + ')' + '</b>' + '</td>';
                    } else if (emplist[i].isfitforShoulderingResponsibilityReviewing == "N") {
                        row = row + '<td>' + emplist[i].reviewingempname + ',' + emplist[i].reviewingpost + '<br/>' +
                                '<span class="label label-danger"> Not Fit For Shouldering Higher Responsibility </span> ' + '<span class="label label-primary">' + emplist[i].reviewingondate + '</span>'
                        + '<br>' + '<b>' + '(' + supressUndefined(emplist[i].reviewingRemarks) + ')' + '</b>' + '</td>';
                    } else {
                        if (emplist[i].reviewingempname == undefined) {
                            emplist[i].reviewingempname = "";
                            row = row + '<td>' + emplist[i].reviewingempname + '</td>';
                        } else {
                            row = row + '<td>' + emplist[i].reviewingempname + ',' + emplist[i].reviewingpost + '</td>';
                        }
                    }

                    if (emplist[i].isfitforShoulderingResponsibilityAccepting == "Y") {

                        row = row + '<td>' + emplist[i].acceptingempname + ',' + emplist[i].acceptingpost + '<br>' +
                                '<span class="label label-success">Fit For Shouldering Higher Responsibility</span>' + '<span class="label label-primary">' + emplist[i].acceptingondate + '</span>'
                       + '<br>' + '<b>' + '(' + supressUndefined(emplist[i].acceptingRemarks) + ')' + '</b>' + '</td>';
                    }
                    else if (emplist[i].isfitforShoulderingResponsibilityAccepting == "N") {
                        row = row + '<td>' + emplist[i].acceptingempname + ',' + emplist[i].acceptingpost +
                                '<br/><span class="label label-danger"> Not Fit For Shouldering Higher Responsibility </span> ' + '<span class="label label-primary">' + emplist[i].acceptingondate + '</span>'
                       + '<br>' + '<b>' + '(' + supressUndefined(emplist[i].acceptingRemarks) + ')' + '</b>' +'</td>';
                    } else {
                        if (emplist[i].acceptingempname == undefined) {
                            emplist[i].acceptingempname = "";
                            row = row + '<td>' + emplist[i].acceptingempname + '</td>';
                        } else {
                            row = row + '<td>' + emplist[i].acceptingempname + ',' + emplist[i].acceptingpost + '</td>';
                        }
                    }

                    row = row + '<td></td>' +
                            '<td> <a href="groupCCustdianCommunicationDetail.htm?promotionId=' + emplist[i].promotionId + '"  class="btn-default" ><button type="button" class="btn btn-primary">communication</button></a></td>' +
                            '</tr>';
                    $("#groupcdatagrid").append(row);
                }
            }
            function supressUndefined(data) {
                if (data)
                    return data;
                else
                    return '';
            }

            function getGroupcDataFiscalYearWise() {
                var fiscalyear = $("#fiscalyear").val();
                var OfficeCode = $("#OfficeCode").val();
                var districtCode = $("#districtCode").val();
                alert(OfficeCode);
                alert(districtCode);
                if (fiscalyear == "") {
                    alert('Please Choose the Fiscal Year');
                } else {
                    window.location = "groupCCustdianReportForFitForPromotionandNotFitForPromotion.htm?fiscalyear=" + fiscalyear + "&OfficeCode=" + OfficeCode;
                }
            }

        </script>

    </head>
    <body>


        <div class="panel panel-default">
            <div class="panel-heading" align="center" style="background-color: #0071c5;color: #ffffff;font-size: xx-large;"> Group C Employee List</div>
            <form:form action="groupCCustdianReport.htm" method="POST" commandName="groupCEmployee" class="form-inline"> 
                <div class="panel-body" style="height: 550px;overflow: auto;">
                    <div class="table-responsive">
                        <div class="row" style="margin-bottom: 12px;"> 
                            <div class="col-lg-1">
                                <label>Financial Year:</label>
                            </div>
                            <div class="col-lg-2">
                                <select name="fiscalyear" id="fiscalyear" class="form-control" style="width: 100% !important;">
                                    <option value="">Year</option>
                                </select>                    
                            </div>
                            <div class="col-lg-1">
                                <label>Office Name:</label>
                            </div>
                            <div class="col-lg-2">
                                <select name="offcode" id="OfficeCode" class="form-control" style="width: 100% !important;">
                                    <option value="">choose office...</option>
                                </select> 
                            </div>
                            <div class="col-lg-1">
                                <label>District Name:</label>
                            </div>
                            <div class="col-lg-2">
                                <select name="distCode" id="districtCode" class="form-control" style="width: 100% !important;">
                                    <option value="">choose district...</option>
                                </select>                            
                            </div>
                            <div class="col-lg-1">
                                <button type="button" class="form-control btn-primary" id="searchbtn" onclick="return searchEmployee()">Search</button>
                            </div>
                            <div class="col-lg-1"> <div class="loader"></div> </div>
                            <div class="col-lg-1">
                                <input type="submit" class="btn btn-primary" name="action" value="Download"> 
                            </div>
                        </div>

                        <table class="table table-bordered table-hover table-striped">

                            <thead>
                                <tr>
                                    <th width="3%">SI NO</th>
                                    <th>Employee Name</th>                                    
                                    <th>Remarks of Reporting Authority /<br>Reporting On Date</th>
                                    <th>Remarks of Reviewing Authority /<br>Reviewing On Date</th>
                                    <th>Remarks of Accepting Authority /<br>Accepting On Date</th>
                                    <th>Remarks of Custodian</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody id="groupcdatagrid">                                        

                            </tbody>

                        </table>
                    </div>
                </form:form>
            </div>
        </div>

    </body>
</html>


