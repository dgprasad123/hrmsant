
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script src="js/moment.js" type="text/javascript"></script>
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script language="javascript" type="text/javascript" >
              $(document).ready(function () {
                $('#txtperiodFrom').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtperiodTo').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtApproveFrom').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                $('#txtApproveTo').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
            });
        </script>
    </head>
    <body>

        <form:form action="editLeaveData.htm" commandName="leave" >
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 style="text-align:center"> Update Leave Applied Data </h3>
                    </div>
                    <form:hidden path="criteria"/>
                    <form:hidden path="searchString"/>
                    <form:hidden path="taskId"/>
                    <div class ="panel-body">
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label>1.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="cl">Leave Applied To</label>

                            </div>
                            <div class="col-lg-3">
                                <form:input path="submittedTo" class="form-control" readonly="true"/>
                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label>2.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="cl">Issuing Authority Name</label>

                            </div>
                            <div class="col-lg-3">
                                <form:input path="issuingAuthName" class="form-control" readonly="true"/>
                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label>3.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="cl">Leave Type</label>

                            </div>
                            <div class="col-lg-3">
                                <form:select path="sltleaveType" id="sltleaveType" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${leavetypelist}" itemValue="tolid" itemLabel="tol"/>
                                </form:select>
                            </div>
                            <div class="col-lg-3">

                            </div>
                            <div class="col-lg-3">

                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label>4.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="cl"> Leave Period (Applied) </label>
                            </div>
                            <div class="col-lg-1">
                                <label for="cl"> From Date: </label>
                            </div>
                            <div class="col-lg-2">
                                <div  class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="txtperiodFrom" id="txtperiodFrom" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-1">
                                <label for="cl"> To Date: </label>
                            </div>
                            <div class="col-lg-2">
                                <div  class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="txtperiodTo" id="txtperiodTo" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="row" style="margin-bottom: 7px;">
                            <div class="col-lg-1">
                                <label>5.</label>
                            </div>
                            <div class="col-lg-2">
                                <label for="cl"> Leave Period (Approved) </label>
                            </div>
                            <div class="col-lg-1">
                                <label for="cl"> From Date: </label>
                            </div>
                            <div class="col-lg-2">
                                <div  class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="txtApproveFrom" id="txtApproveFrom" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                            <div class="col-lg-1">
                                <label for="cl"> To Date: </label>
                            </div>
                            <div class="col-lg-2">
                                <div  class='input-group date' id='processDate'>
                                    <form:input class="form-control" path="txtApproveTo" id="txtApproveTo" readonly="true"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <input type="submit" value="Save" name="btn" class="btn btn-success" onclick="return validateForm()"/>
                        <input type="submit" value="Back" name="btn" class="btn btn-danger" />
                    </div>
                </div>
            </div>

            
        </form:form>


    </body>
</html>

