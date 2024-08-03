<%-- 
    Document   : EmployeeAbsenteeDetail
    Created on : May 25, 2018, 6:29:15 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/servicehistory.js"></script>
        <script type="text/javascript">
            function validateForm() {
                if ($("#sltyear").val() == "0") {
                    alert("Please Select Year");
                    return false;
                }
                if ($("#fromDate").val() == "") {
                    alert("Please Enter From Date");
                    return false;
                }
                if ($("#toDate").val() == "") {
                    alert("Please Enter To Date");
                    return false;
                }
                if (($('#fromDate').val() != '') && ($('#toDate').val() != '')) {
                    var ftemp = $("#fromDate").val().split("-");
                    var ttemp = $("#toDate").val().split("-");
                    
                    var fdt = new Date(ftemp[2], monthint(ftemp[1].toUpperCase()), ftemp[0]);
                    var tdt = new Date(ttemp[2], monthint(ttemp[1].toUpperCase()), ttemp[0]);
                    if (fdt > tdt) {
                        alert("From Date must be less than To Date");
                        return false;
                    }
                    var year = parseInt($("#sltyear").val());
                    var month = parseInt($("#sltmonth").val());
                    
                    var fromyear = parseInt(ftemp[2]);
                    var toyear = parseInt(ttemp[2]);
                    
                    var frommonth = monthint(ftemp[1].toUpperCase());                    
                    var tomonth = monthint(ttemp[1].toUpperCase());
                    
                    if((fromyear != year) || (toyear != year)){
                        alert("Absentee Date must be within selected Year");
                        return false;
                    }
                    if((frommonth != month) || (tomonth != month)){
                        alert("Absentee Date must be within selected Month");
                        return false;
                    }
                    return true;
                }
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form:form class="form-horizontal" action="saveEmployeeAbsentee.htm" commandName="absentee">
                <form:hidden path="absid"/>
                <div class="panel panel-default">
                    <div class="panel-heading">Absentee Information</div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">Year:</label>
                            <div class='col-sm-2'>
                                <form:select path="sltyear" id="sltyear" class="form-control">
                                    <form:option value="0">Select</form:option>
                                    <form:options items="${yearlist}" itemLabel="label" itemValue="value"/>
                                </form:select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">Month:</label>
                            <div class='col-sm-2'>
                                <form:select path="sltmonth" id="sltmonth" class="form-control">
                                    <form:option value="00">January</form:option>
                                    <form:option value="01">February</form:option>
                                    <form:option value="02">March</form:option>
                                    <form:option value="03">April</form:option>
                                    <form:option value="04">May</form:option>
                                    <form:option value="05">June</form:option>
                                    <form:option value="06">July</form:option>
                                    <form:option value="07">August</form:option>
                                    <form:option value="08">September</form:option>
                                    <form:option value="09">October</form:option>
                                    <form:option value="10">November</form:option>
                                    <form:option value="11">December</form:option>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">From Date:</label>
                            <div class='col-sm-2'>
                                <div class='input-group date' id='fromDate1'>
                                    <form:input path="fromDate" class="form-control"/>  
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="email">To Date:</label>
                            <div class='col-sm-2'>
                                <div class='input-group date' id='toDate1'>
                                    <form:input path="toDate" class="form-control"/>  
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-lg-2">
                                <input type="submit" name="action" value="Save" class="btn btn-success" onclick="return validateForm()"/>
                                <input type="submit" name="action" value="Cancel" class="btn btn-success"/>
                            </div>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
        <script type="text/javascript">
            $(function() {
                $('#fromDate1').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
                $('#toDate1').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
            });
        </script>
    </body>
</html>
