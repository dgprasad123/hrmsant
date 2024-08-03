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

    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <strong style="font-size:14pt;">Unavailed Joining Time</strong>
                </div>
                <div class="panel-body">

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="orderNumber">Order No:</label>
                        </div>
                        <div class="col-lg-2">   
                            ${leaveDetail.orderNumber}
                        </div>
                        <div class="col-lg-2">
                            <label for="orderDate">Order Date:</label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date" id="processDate">
                                ${leaveDetail.orderDate}

                            </div>                                
                        </div>
                    </div>

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-12" style="background:#FFD6AF">
                            <label for="spc">Details of Authority <span style="color: red">*</span></label>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="fromDate">Department:</label>
                        </div>
                        <div class="col-lg-10">
                            <div class='input-group date' id='processDate'>
                                ${leaveDetail.departmentName}
                            </div>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="fromDate">Office:</label>
                        </div>
                        <div class="col-lg-10">
                            <div class='input-group date' id='processDate'>
                                ${leaveDetail.officeName}
                            </div>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="">Post:</label>
                        </div>
                        <div class="col-lg-10">
                            <div class='input-group date' id='processDate'>
                                ${leaveDetail.authPostName}
                            </div>
                        </div>
                    </div>    
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-12">
                            <label for="spc" style="color:#0081B7">Period of Unavailed Joining Time</label>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="fromDate">From Date:</label>
                        </div>
                        <div class="col-lg-2">
                            <div class='input-group date' id='processDate'>
                                ${leaveDetail.fromDate}
                            </div>
                        </div>
                        <div class="col-lg-2">
                            <label for="toDate">To Date:</label>
                        </div>
                        <div class="col-lg-2">
                            <div class='input-group date' id='processDate'>
                                ${leaveDetail.toDate}

                            </div>
                        </div>
                    </div>



                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="note">Note:</label>
                        </div>
                        <div class="col-lg-9">
                            ${leaveDetail.note}
                        </div>
                        <div class="col-lg-1">
                        </div>
                    </div>

                </div>

                <div class="panel-footer">
                    <input type="button" value="&laquo; Back to Unavailed Joining Time List" class="btn btn-default" style="background:#0076AA;color:#FFFFFF;" onclick="self.location = 'UnavailedLeaveList.htm'" />
                </div>
            </div>
        </div>


    </body>
</html>
