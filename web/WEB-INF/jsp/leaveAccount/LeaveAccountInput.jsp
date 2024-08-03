<%-- 
    Document   : LeaveAccountInput
    Created on : Jul 16, 2018, 5:36:38 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <title>::Employee Leave Account::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">      
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/bootstrap-datetimepicker.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#idleavePeriodFrom').datetimepicker({
                    format: 'D-MMM-YYYY'
                });
                $('#idleavePeriodTo').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false
                });
                $("#idleavePeriodFrom").on("dp.change", function(e) {
                    $('#idleavePeriodTo').data("DateTimePicker").minDate(e.date);
                });
                $("#idleavePeriodTo").on("dp.change", function(e) {
                    $('#idleavePeriodFrom').data("DateTimePicker").maxDate(e.date);
                });
            });
            function validate() {
                /*if ($('input[name=leavePeriod]:checked').length <= 0) {
                 alert("Please Select Leave Period");
                 return false;
                 }*/
                var radioValue = $("input[name='leavePeriod']:checked").val();
                if (radioValue) {
                    if (radioValue == "SP") {
                        if ($("#leavePeriodFrom").val() == "") {
                            alert("Please choose Period from");
                            return false;
                        }
                        if ($("#leavePeriodTo").val() == "") {
                            alert("Please choose Period to");
                            return false;
                        }
                    }
                } else {
                    alert("Please Select Leave Period");
                    return false;
                }
            }
        </script>
    </head>
    <body>
        <form:form class="form-horizontal" method="post" action="employeeLeaveAccount.htm" commandName="employeeLeaveAccount" style="margin-top : 10px;" onsubmit="return validate()">
            <div class="form-group">
                <label class="control-label col-sm-2" for="email">Leave Type:</label>
                <div class="col-sm-4">
                    <form:hidden path="empid"/>
                    <form:select path="leaveType" class="form-control">
                        <form:options items="${leaveType}" itemValue="tolid" itemLabel="tol"/>
                    </form:select>                                        
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2" for="pwd">Leave Period:</label>
                <div class="col-sm-10">
                    <form:radiobutton path="leavePeriod" value="EP"/> Entire Period
                    <form:radiobutton path="leavePeriod" value="SP"/> Select Period                    
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-2" for="pwd">From Date:</label>
                <div class="col-sm-2">
                    <div class='input-group date' id='idleavePeriodFrom'>
                        <form:input path="leavePeriodFrom"  class="form-control"/>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-time"></span>
                        </span>
                    </div>                                             
                </div>
                <label class="control-label col-sm-1" for="pwd">To Date:</label>
                <div class="col-sm-2">
                    <div class='input-group date' id='idleavePeriodTo'>
                        <form:input path="leavePeriodTo"  class="form-control"/>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-time"></span>
                        </span>
                    </div>                    
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="submit" class="btn btn-default" name="action" value="View"/>                    
                </div>
            </div>
        </form:form> 
    </body>
</html>
