<%-- 
    Document   : viewSuperannuationDetailsOfficewise
    Created on : Sep 11, 2023, 3:37:11 PM
    Author     : Madhusmita
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script> 
        <script src="js/jquery.freezeheader.js"></script>
        <script src="js/bootstrap.min.js"></script>


        <script type="text/javascript">
            $(document).ready(function() {
                $("#dataTab").freezeHeader();

                // $("#dttabl").DataTable();

                $("#btnExport2Tabl").click(function() {
                    var x = $("#dttabl").clone();
                    $("#dttabl").find("tr td a").replaceWith(function() {
                        return $.text([this]);
                    });

                    tableToExcel("dttabl", "BillGroupWiseReport", "BillGroupWiseReport.xls");
                    $("#dttabl").html(x);
                });

            });
            function viewMACPStatus(empid) {
                var finTrData = "";
                //alert(empid);
                $('#empList').empty();
                var url = 'viewMacpRacpJSON.htm?empid=' + empid;
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        finTrData = finTrData + "<tr>" +
                                "<td>" + (i + 1) + "</td>" +
                                "<td>" + obj.empname + "<br/>" + obj.fathername + "<br/>" + obj.dateOfBirth + "</td>" +
                                "<td>" + obj.appointmentrank + "</td>" +
                                "<td>" + obj.presentrank + "<br/>" + obj.dateOfpresentrank + "</td>" +                                
                                "<td>" + obj.yearofservice + "</td>" +
                                "<td>" + 0 + "</td>" +
                                "<td>" + 0 + "</td>" +
                                "<td>" + obj.noofpromotion + "</td>" +
                                "<td>" + obj.noofupgradation + "</td>" +
                                "<td>" + obj.statuspropertystatement + "<br/>" + obj.dateofpropertystatement + "</td>" +
                                "<td>" + 0 + "</td>" +
                                "<td>" + 0 + "</td>" +
                                "<td>" + 0 + "</td>" +
                                "<td>" + 0 + "</td>" +
                                "</tr>";
                    });

                    //$('#empHeading').append("HRMS ID:").append($("#finalEmpId").val());
                    $('#empList').append(finTrData);
                });

            }
        </script>

        <style>
            table{
                border-collapse: collapse;
                border: 1px solid;
            }
            table, th, td {
                border: 1px solid black;
            }
            .modal-ku {
                width: 2000px;
                margin: auto;
            }
        </style>
    </head>
    <body>

        <form:form role="form" action="getOfficeBillVerificationStatus.htm" commandName="billBean" method="post" >


            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">

                        <h3 style="color:green; text-align: center;"><u>${distName} District Superannuation Report</u></h3>
                        <h4 style="color:green; text-align: center;"><u>${fdate} To ${tdate}</u></h4>
                        <br/><br/>

                        <div class="row" style="margin-bottom: 7px; ">

                        </div>
                        <div class="col-lg-1">
                            <%-- <input type="button" id="btnExport2Tabl" class="btn btn-primary" value="Export to Excel"/>--%>
                        </div>                    

                        <div class="row">
                            <div class="col-lg-12">

                                <div class="table-responsive" style="overflow:auto;">
                                    <table class="table table-striped" id="dataTab">
                                        <thead>
                                            <tr>
                                                <th style="background-color:lightblue;color: #000000; ">SL NO</th>
                                                <th style="background-color:lightblue;color: #000000; ">OFFICE NAME</th>
                                                <th style="background-color:lightblue;color: #000000; text-align:center; ">NAME OF EMPLOYEE</th>
                                                <th style="background-color:lightblue;color: #000000; text-align:center; ">HRMS ID</th>
                                                <th style="background-color:lightblue;color: #000000; text-align:center; ">GPF NO</th>
                                                <th style="background-color:lightblue;color: #000000; ">DATE OF BIRTH</th>
                                                <th style="background-color:lightblue;color: #000000; ">DATE OF APPOINTMENT</th>
                                                <th style="background-color:lightblue;color: #000000; ">DATE OF RETIREMENT</th>
                                                <th style="background-color:lightblue;color: #000000; ">DETAILS</th>                                               
                                            </tr>
                                        </thead>
                                        <tbody> 
                                            <c:forEach items="${emplist}" var="elist" varStatus="Count">

                                                <tr style="font-family:serif;background-color: #e6e7e2; ">
                                                    <td style="text-align:center;"><c:out value="${Count.index+1}"/></td>
                                                    <td><c:out value="${elist.offName}"/></td>
                                                    <td><c:out value="${elist.empname}"/></td>
                                                    <td><c:out value="${elist.empid}"/></td>
                                                    <td><c:out value="${elist.gpfNo}"/></td>
                                                    <td><c:out value="${elist.dateOfBirth}"/></td>
                                                    <td><c:out value="${elist.dateOfJoining}"/></td>
                                                    <td><c:out value="${elist.dateOfSuperannuation}"/></td>
                                                    <td><a href="#" onclick="viewMACPStatus('${elist.empid}');" data-remote="false" data-toggle="modal" data-target="#viewMACPmodal">RACP/MACP</a></td>
                                                    <%--<td><a href="aqbillreportOfcWise.htm?format=f2&billNo=${ofclist.billNo}&offcode=${ofclist.offCode}" target="_blank">AQ REPORT-2</a></td>--%>

                                                </tr>

                                            </c:forEach>

                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="viewMACPmodal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:65%;" >
                    <!-- Modal content-->
                    <div class="modal-content modal-lg" style="width:125%;">
                        <div class="modal-header" style=" text-align: center;">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h3 class="table-active"><u>Statement for Allowing Assured Career Progression Schemes</u></h3>
                            <h4 id="empHeading" class="empheading"><B><U></U></B></h4>
                        </div>
                        <div class="modal-body" >
                            <table class="table" border="1"  cellspacing="10"  style="font-size:12px; font-family:verdana;" id="tabl1"> 
                                <thead>
                                    <tr>
                                        <th style="text-align:center;">Sl No</th>
                                        <th style="text-align:center; width: 15%;">Employee Name/<br>Father's Name/<br/>Date Of Birth</th>
                                        <th style="text-align:center;width: 10%;">Date of Appointment<br/>with rank</th>
                                        <th style="text-align:center;width: 10%;">Present rank with<br/>Date of Joinning</th>
                                        <th style="text-align:center;">Date of Completion of <br>10/20/30</th>
                                        <th style="text-align:center;">Non Qualifying Service Period<br>(If any)</th>
                                        <th style="text-align:center;">Total period of qualifying service<br/>as on</th>
                                        <th style="text-align:center;">No. of promotion availed<br/>with date</th>
                                        <th style="text-align:center;">No. of upgradation availed<br/>with date</th>
                                        <th style="text-align:center;width: 10%;">Whether up to date <br/>Annual Property Statement</th>
                                        <th style="text-align:center;">Whether up to date <br/>Punishment during <br/>last 5 years</th>
                                        <th style="text-align:center;">Whether up to date <br/>Remarks<br/>(Whether eligible for RACP/MACP)</th>
                                        <th style="text-align:center;">Major Punishment</th> 
                                        <th style="text-align:center;">Minor Punishment</th>
                                    </tr>
                                </thead>
                                <tbody id="empList">                                                    

                                </tbody>
                            </table> 

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



