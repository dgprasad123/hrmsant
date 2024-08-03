<%-- 
    Document   : AuthReportViewPage
    Created on : 24 Feb, 2018, 12:17:57 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <head>
        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function openRevertReason() {
                $('#revertReasonModal').modal('show');
            }
            function saveRevert() {
                var revertStatus = true;
                if ($('#revertReason').val() == '') {
                    alert("Please provide Reason for Revert");
                    revertStatus = false;
                    return false;
                } else {
                    //alert("Length is: "+$('#revertReason').val().length);
                    var revertReasonLength = $('#revertReason').val().length;
                    if (revertReasonLength > 1000) {
                        alert("Reason for Revert must be within 1000 characters!");
                        revertStatus = false;
                        return false;
                    }
                }
                if (revertStatus == true) {
                    if (confirm("Are you sure to Revert?")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        </script>
    </head>
</head>
<body>
    <form:form action="approvedAERByAuth.htm" >
        <form:hidden path="taskid"/>
        <div class="container">
            <h1 align="center"> ${OffName}</h1>
            <h2 align="center">PROFORMA</h2>
            <div align="center"> 
                <c:if test="${submitted eq 'N'}">
                    <input type="submit" value="Approve" name="btnAer" class="btn btn-primary" onclick="return confirm('Are you sure to Approve?')"/>&nbsp;
                    <button type="button" class="btn btn-warning" onclick="openRevertReason();">Revert</button>
                </c:if>
                <c:if test="${submitted eq 'Y'}">
                    Approved
                </c:if>
                <c:if test="${submitted eq 'R'}">
                    Reverted
                </c:if>
            </div><br />
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
        <div id="revertReasonModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Reason for Revert</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-2">
                                <label for="sltDept">Reason</label>
                            </div>
                            <div class="col-lg-9">
                                <form:textarea path="revertReason" id="revertReason" rows="4" cols="70"/>
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <input type="submit" value="Revert" name="btnAer" class="btn btn-warning" onclick="return saveRevert();"/>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </form:form>
</body>
</html>
