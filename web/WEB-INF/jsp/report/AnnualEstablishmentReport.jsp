<%-- 
    Document   : AnnualEstablishmentReport
    Created on : 15 Jan, 2018, 11:56:25 AM
    Author     : Surendra
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <head>
        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/chosen.css">

        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                $('#hidDeptCode').chosen();
            })
            function validate() {
                if ($("#controllingSpc").val() == '') {
                    alert('Please select authority.');
                    return false;
                }
                var r = confirm("Are you sure to submit?");
                if (r == true) {
                    return true;
                } else {
                    return false;
                }

            }
            function getDeptWiseOfficeList() {
                $('#hidOffCode').empty();
                var url = 'getOfficeListJSON.htm?deptcode=' + $('#hidDeptCode').val();
                $('#hidOffCode').append('<option value="">--Select Office--</option>');
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#hidOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                }).done(function () {
                    $("#hidOffCode").chosen();
                    $("#hidOffCode").trigger("chosen:updated");
                });
            }

            function getOfficeWiseGPCList() {
                var offCode = $('#hidOffCode').val();

                $('#controllingSpc').empty();
                var url = 'getAERAuthortiyList.htm?offcode=' + offCode;
                $('#controllingSpc').append('<option value="">--Select--</option>');
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#controllingSpc').append('<option value="' + obj.value + '">' + obj.label + '</option>');
                    });
                }).done(function () {
                    $("#controllingSpc").chosen();
                    $("#controllingSpc").trigger("chosen:updated");
                });
            }
        </script>
    </head>
</head>
<body>
    <form:form action="submitEstablishmentReport.htm" commandName="command">
        <form:hidden path="fy" value="${fy}"/>
        <form:hidden path="taskid"/>
        <form:hidden path="aerId"/>
        <div class="container">
            <h1 align="center"> ${OffName}</h1>
            <h2 align="center">PROFORMA</h2>
            <div align="center"> 
                <c:if test="${submitted eq 'N'}">
                    Select Controlling Officer 
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="sltDept">Department</label>
                        </div>
                        <div class="col-lg-9">
                            <form:select path="hidDeptCode" id="hidDeptCode" class="form-control" onchange="getDeptWiseOfficeList();">
                                <option value="">--Select Department--</option>
                                <form:options items="${deptlist}" itemValue="deptCode" itemLabel="deptName"/>
                            </form:select>
                        </div>
                        <div class="col-lg-1">
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="note">Office</label>
                        </div>
                        <div class="col-lg-9">
                            <form:select path="hidOffCode" id="hidOffCode" class="form-control" onchange="getOfficeWiseGPCList();">
                                <option value="">--Select Office--</option>
                            </form:select>
                        </div>
                        <div class="col-lg-1">
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="note">Substantive Post</label>
                        </div>
                        <div class="col-lg-9">
                            <form:select path="controllingSpc" id="controllingSpc" class="form-control">
                                <option value="">--Select--</option>
                            </form:select>
                        </div>
                        <div class="col-lg-1">
                        </div>
                    </div>
                    <input type="submit" value="Submit" name="btnAer" class="btn btn-primary" onclick="return validate()"/>
                    <input type="submit" value="Back" name="btnAer" class="btn btn-primary"/>
                </c:if>
                <c:if test="${submitted eq 'Y'}">
                    Report Submitted
                    <input type="submit" value="Download" name="btnAer"/>
                </c:if>
            </div>
            <br />


            <table class="table table-hover" style="border:1px solid">
                <thead>
                    <tr> <th colspan="5"> DDO Provided Sanction Strength </th> </tr>
                    <tr>
                        <th>Group A</th>
                        <th>Group B</th>
                        <th>Group C</th>
                        <th>Group D</th>
                        <th>Grant Id</th>
                    </tr>
                </thead>
                <tr>
                    <td> ${command.groupAData}</td>
                    <td> ${command.groupBData} </td>
                    <td> ${command.groupCData} </td>
                    <td> ${command.groupDData} </td>
                    <td> ${command.grantInAid} </td>
                </tr>
            </table>
            <table class="table table-hover" style="border:1px solid">
                <thead>
                <tr> <th colspan="5"> System Generated Sanction Strength </th> </tr>
                <tr>
                    <th>Group A</th>
                    <th>Group B</th>
                    <th>Group C</th>
                    <th>Group D </th>
                    <th>Grant Id</th>
                </tr>
                </thead>
                <tr>
                    <td> ${groupADataSystem} </td>
                    <td> ${groupBDataSystem} </td>
                    <td> ${groupCDataSystem} </td>
                    <td> ${groupDDataSystem} </td>
                    <td> ${grantInAidSystem} </td>
                </tr>
            </table>
            <br />
            <table class="table table-hover" style="border:1px solid">
                <thead>
                    <tr>
                        <th>Sl No</th>
                        <th>Posts</th>
                        <th>Group</th>
                        <th> 
                            Scale of Pay </br>
                            (6th Pay)
                        </th>
                        <th>Grade Pay</th>
                        <th> 
                            Scale of Pay </br>
                            (7th Pay)
                        </th>
                        <th> Level in the Pay </br> Matrix as per ORSP </br> Rules, 2017</th>
                        <th> Sanctioned </br> Strength </th>
                        <th> Men in Position </th>
                        <th> Vacancy Position </th>
                        <th> Remarks </th>
                    </tr>

                    <tr>
                        <th> 1 </th>
                        <th> 2 </th>
                        <th> 3 </th>
                        <th> 4 </th>
                        <th> 5 </th>
                        <th> 6 </th>
                        <th> 7 </th>
                        <th> 8 </th>
                        <th> 9 </th>
                        <th> 10 </th>
                        <th> 11 </th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="establish" items="${EstablishmentList}">
                        <tr>
                            <td> ${establish.serialno} </td>
                            <td> ${establish.postname} </td>
                            <td> ${establish.group} </td>
                            <td> ${establish.scaleofPay} </td>
                            <td> ${establish.gp} </td>
                            <td> ${establish.scaleofPay7th} </td>
                            <td> ${establish.level} </td>
                            <td> ${establish.sanctionedStrength} </td>
                            <td> ${establish.meninPosition} </td>
                            <td> ${establish.vacancyPosition} </td>
                            <td> &nbsp; </td>
                        </tr>
                    </c:forEach>

                <div> <!--<input type="submit" value="Submit" name="btnAer"/> --></div>
                </tbody>
            </table>
        </div>
    </form:form>
</body>
</html>
