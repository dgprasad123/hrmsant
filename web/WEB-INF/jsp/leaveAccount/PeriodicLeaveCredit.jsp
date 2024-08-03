<%-- 
    Document   : PeriodicLeaveCredit
    Created on : Aug 28, 2018, 3:31:17 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>::Employee Leave Account::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            $(function () {
                $('#fromdate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#todate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });

            });
            function editLeaveCreditDetail(lcrId) {
                $.ajax({
                    method: "POST",
                    url: "editLeaveCreditDetail.htm",
                    data: {lcrId: lcrId},
                    success: function (data) {

                        $('#creId').val(data.creId);
                        $('#fromdate').val(data.fromdate);
                        $('#todate').val(data.todate);
                        $('#compMonths').val(data.compMonths);
                        $('#creleave').val(data.creleave);
                    },
                    error: function () {
                        $("#msg").html("Error Occured");
                        $("#msg").css({"color": "red", "fontWeight": "bold"});
                        alert('Error Occured');
                    }
                });
            }

            function updateLeaveAccount() {
                $("#msg").html("Inprogress");
                $("#msg").css({"color": "red", "fontWeight": "bold"});
                $("[name='selectedPeriod']").each(function () {
                    if ($(this).prop("checked")) {
                        var items = new Array();
                        $(this).parent().parent().children().each(function () {
                            items.push($(this).text());
                        })
                        $.ajax({
                            method: "POST",
                            url: "updateLeaveCreditData.htm",
                            data: {typeoflv: $("#tolid").val(), fromDate: items[0], toDate: items[1], compMonths: items[2], creleave: items[3]},
                            success: function (data) {

                            },
                            error: function () {
                                $("#msg").html("Error Occured");
                                $("#msg").css({"color": "red", "fontWeight": "bold"});
                                alert('Error Occured');
                            }
                        });
                    }
                });
                $("#msg").html("Completed");
                $("#msg").css({"color": "blue", "fontWeight": "bold"});
            }
        </script>
    </head>
    <body>
        <form:form class="form-horizontal" method="post" action="employeeLeaveCredit.htm" commandName="creditLeaveBean" style="margin-top : 10px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="form-group">
                        <label class="control-label col-sm-2" for="email">Select Type of Leave</label>
                        <div class="col-sm-4">
                            <form:select path="tolid" class="form-control">
                                <form:options items="${leaveType}" itemValue="tolid" itemLabel="tol"/>
                            </form:select>
                        </div>
                        <div class="col-sm-4">
                            <input type="submit" class="btn btn-default" name="action" value="View"/>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered" style="font-size:10pt;">
                        <thead>
                            <tr class="success" style="font-weight: bold;">	
                                <td colspan="2" align="center">Service Period</td>
                                <td width="15%" rowspan="2" align="center">Complete Months</td>
                                <td width="17%" rowspan="2" align="center"> No of Credited<br> Leave</td>
                                <td width="12%" rowspan="2" align="center">
                                    Update All<br> <input type="checkbox" name="ids[]" id="ids[]"/>
                                </td>
                                <td width="8%" rowspan="2" align="center"> View</td>
                                <td width="8%" rowspan="2" align="center"> Edit </td>
                        </tr>
                        <tr class="success" style="font-weight: bold;">
                            <td width="13%" align="center">From</td>
                            <td width="13%" align="center">To</td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td  colspan="4" align="left">
                                <b>Opening Balance By Date : <font color="red">${clv.openbaldate}&nbsp;${clv.fnan}</font></b></td>
                            <td align="right">&nbsp;</td>
                            <td align="right">&nbsp;</td>
                            <td align="center" style="font-size: 12;color: red">
                                <b>${clv.leaveCredited}&nbsp;</b>
                            </td>
                        </tr>
                        <c:forEach items="${alcleave}" var="cleave">
                            <tr>
                                <td align="center">${cleave.fromDate}</td>
                                <td align="center">${cleave.toDate}</td>
                                <td align="center">${cleave.compMonths}</td>
                                <td align="center">${cleave.creleave}</td>
                                <td align="center">
                                    <c:if test="${empty cleave.lcrId}">
                                        <input type="checkbox" name="selectedPeriod" />
                                    </c:if>
                                    <c:if test="${not empty cleave.lcrId}">
                                        &nbsp;
                                    </c:if>
                                </td>
                                <td align="center">
                                    <c:if test = "${cleave.lcrId != null}">
                                        <a href="javascript:viewLeaveCreditDetail('${cleave.lcrId}')" class="btn btn-success btn-sm">
                                            <span class="glyphicon glyphicon-file"></span> View
                                        </a>
                                    </c:if>
                                </td>
                                <td align="center">
                                    <c:if test = "${cleave.lcrId != null}">
                                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#editLeaveCreditDetail" onclick="editLeaveCreditDetail('${cleave.lcrId}')">
                                            <span class="glyphicon glyphicon-file"></span> Edit
                                        </button>
                                    </c:if>
                                </td>

                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="panel-footer">
                <input type="button" class="btn btn-success" value="Update" onclick="updateLeaveAccount()"/>
                <button type="submit" class="btn btn-success" name="Delete" data-dismiss="modal"  onclick="return confirm('Are you sure to Delete Leave credit ?')">Delete</button>
                <span id="msg"></span>
            </div>
        </div>
        <div id="editLeaveCreditDetail" class="modal" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Update Leave Credit</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label >From Date</label>
                            </div>
                            <form:hidden path="creId" id="creId"/>
                            <div class="col-lg-4">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="fromdate" id="fromdate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>

                                </div>   
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label >To Date</label>
                            </div>
                            <div class="col-lg-4">
                                <div class="input-group date" id="processDate">
                                    <form:input class="form-control" path="todate" id="todate" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>   
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label>Complete Months</label>
                            </div>
                            <div class="col-lg-4">
                                <form:input class="form-control" path="compMonths" id="compMonths" />

                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-4">
                                <label>No of Credited <br>Leave</label>
                            </div>
                            <div class="col-lg-4">
                                <form:input class="form-control" path="creleave" id="creleave" />
                            </div>
                            <div class="col-lg-1">
                            </div>
                        </div>

                        <div class="modal-footer">
                            <input type="submit" name="Update" class="btn btn-default" style="width:70px" value="Update"/> 
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

                        </div>
                    </div>
                </div>
            </div>                
        </form:form>            
</body>
</html>
