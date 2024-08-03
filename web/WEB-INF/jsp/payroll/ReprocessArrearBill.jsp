<%-- 
    Document   : ReprocessBill
    Created on : Oct 28, 2017, 9:40:13 AM
    Author     : Manas
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript">
            $(document).ready(function () {
                if ($('#txtbilltype').val() != 'ARREAR_6' && $('#txtbilltype').val() != 'ARREAR_6_J') {
                    $("#percentageArraer").hide();
                    $("#percentageArraer").val('');
                    $("#percentageArraerJudiciary").hide();
                    $("#percentageArraerJudiciary").val('');
                }else if($('#txtbilltype').val() == 'ARREAR_6'){
                    $("#percentageArraerJudiciary").hide();
                    $("#percentageArraerJudiciary").val('');
                }else if($('#txtbilltype').val() == 'ARREAR_6_J'){
                    $("#percentageArraer").hide();
                    $("#percentageArraer").val('');
                } else {
                    $("#percentageArraer").show();
                    $("#processFromDate").val('1-JAN-2016');
                    $("#processToDate").val('31-AUG-2017');
                    $('#processFromDate').attr('readonly', true);
                    $('#processToDate').attr('readonly', true);
                }
            });
            function validateForm() {
                var submitStatus = true;
                var sltBillType = $('#txtbilltype').val();

                if (sltBillType == '') {
                    alert('Please select bill Type.');
                    submitStatus = false;
                }
                if (sltBillType == 'ARREAR_6') {
                    if ($("#percentageArraer").val() == '') {
                        alert('Please select Arraer Percentage.');
                        $("#percentageArraer").focus();
                        submitStatus = false;
                        return false;
                    }
                }
                if (sltBillType == 'ARREAR_6_J') {
                    if ($("#percentageArraerJudiciary").val() == '') {
                        alert('Please select Judiciary Arrear Percentage.');
                        $("#percentageArraerJudiciary").focus();
                        return false;
                    }
                }
                var today = new Date();

                if ($('#processDateArr').val() == "") {
                    alert("Please Enter Process Date");
                    submitStatus = false;
                    return false;
                } else {
                    var pdate = $('#processDateArr').val().split("-");
                    var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                    for (var j = 0; j < months.length; j++) {
                        if (pdate[1] == months[j]) {
                            pdate[1] = months.indexOf(months[j]) + 1;
                        }
                    }
                    if (pdate[1] < 10) {
                        pdate[1] = '0' + pdate[1];
                    }
                    var formattedFromDateString = pdate[2] + ',' + pdate[1] + ',' + pdate[0];

                    var prdt = new Date(formattedFromDateString);

                    if (prdt.getTime() > today.getTime()) {
                        alert("Process Date should not be greater than Today's Date");
                        $('#processDateArr').focus();
                        submitStatus = false;
                        return false;
                    }
                }

                if ($('#processFromDate').val() == "") {
                    alert("Please Enter From Date");
                    submitStatus = false;
                    return false;
                } else {
                    var fromdate = $('#processFromDate').val().split("-");
                    var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                    for (var j = 0; j < months.length; j++) {
                        if (fromdate[1] == months[j]) {
                            fromdate[1] = months.indexOf(months[j]) + 1;
                        }
                    }
                    if (fromdate[1] < 10) {
                        fromdate[1] = '0' + fromdate[1];
                    }
                    var formattedFromDateString = fromdate[2] + ',' + fromdate[1] + ',' + fromdate[0];

                    var prdt = new Date(formattedFromDateString);

                    if (prdt.getTime() > today.getTime()) {
                        alert("From Date should not be greater than Today's Date");
                        $('#processFromDate').focus();
                        submitStatus = false;
                        return false;
                    }
                }

                if ($('#processToDate').val() == "") {
                    alert("Please Enter To Date");
                    submitStatus = false;
                    return false;
                } else {
                    var todate = $('#processToDate').val().split("-");
                    var months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                    for (var j = 0; j < months.length; j++) {
                        if (todate[1] == months[j]) {
                            todate[1] = months.indexOf(months[j]) + 1;
                        }
                    }
                    if (todate[1] < 10) {
                        todate[1] = '0' + todate[1];
                    }
                    var formattedFromDateString = todate[2] + ',' + todate[1] + ',' + todate[0];

                    var prdt = new Date(formattedFromDateString);

                    if (prdt.getTime() > today.getTime()) {
                        alert("To Date should not be greater than Today's Date");
                        $('#processToDate').focus();
                        submitStatus = false;
                        return false;
                    }
                }

                if(submitStatus == true){
                    $('#btn-process-child').hide();
                }
                return true;
            }
        </script>
    </head>
    <body>
        <form:form class="form-inline" action="prepareNewArrearBillform.htm" method="POST" commandName="command">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="modal-title">Reprocess Bill</h4>
                </div>
                <div class="panel-body">
                    <div class="form-group">
                        <form:hidden path="billgroupId"/>
                        <form:hidden path="sltYear"/>
                        <form:hidden path="sltMonth"/>

                        <%--<form:hidden path="sltFromMonth"/>
                        <form:hidden path="sltFromYear"/>
                        <form:hidden path="sltToYear"/>
                        <form:hidden path="sltToMonth"/>--%>

                        <form:hidden path="offCode"/>
                        <form:hidden path="billNo"/>
                        <form:hidden path="txtbilltype"/> 

                        <div class="row">                            
                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="percentage">  </label>
                                    <form:select path="percentageArraer" id="percentageArraer" class="form-control">
                                        <form:option value=""> -- Select Arrear Percent --</form:option>
                                        <form:option value="10"> 10% </form:option>
                                        <form:option value="50"> 50% </form:option>
                                        <form:option value="60"> 60% </form:option>
                                        <form:option value="30"> 30% </form:option>
                                        <form:option value="20"> 20% </form:option>
                                    </form:select>                
                                </div>
                            </div>
                            <div class="col-lg-3">
                                <div class="form-group">
                                    <label for="percentageArraerJudiciary">  </label>
                                    <form:select path="percentageArraerJudiciary" id="percentageArraerJudiciary" class="form-control">
                                        <form:option value=""> -- Select Judiciary Arrear Percent --</form:option>
                                        <form:option value="25"> 25% </form:option>
                                        <form:option value="50"> 50% </form:option>
                                    </form:select>                
                                </div>
                            </div>
                        </div>      
                        <div class="row">
                            <div class="col-lg-4">
                                <label for="processDateArr">Process Date:</label>
                                <div class='input-group date' id='processDate'>
                                    <form:input class="form-control" id="processDateArr" path="processDateArr"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-4">
                                <div class="form-group">
                                    <label for="sltFromYearArr">From Date:</label>
                                    <div class='input-group date' id='processFromDate1'>
                                        <form:input class="form-control" id="processFromDate" path="processFromDate" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-4">
                                <div class="form-group">
                                    <label for="sltFromMonthArr">To Date:</label>

                                    <div class='input-group date' id='processToDate1'>
                                        <form:input class="form-control" id="processToDate" path="processToDate" />
                                        <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-time"></span>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-4"></div>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
                <div class="panel-footer">
                    <input type="submit" class="btn btn-default" name="action" value="Process" id="btn-process-child" onclick="return validateForm();"/>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </form:form>
    </body>
    <script type="text/javascript">
        $(function () {
            $('#processDate').datetimepicker({
                format: 'D-MMM-YYYY'
            });

            $('#processFromDate1').datetimepicker({
                format: 'D-MMM-YYYY'
            });

            $('#processToDate1').datetimepicker({
                format: 'D-MMM-YYYY'
            });
        });
    </script>
</html>
