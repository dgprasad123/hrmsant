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
                    <strong style="font-size:14pt;">Miscellaneous</strong>
                </div>
                <div class="panel-body">

                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="orderNumber">Order No:</label>
                        </div>
                        <div class="col-lg-2">   
                            ${miscDetail.orderNumber}
                        </div>
                        <div class="col-lg-2">
                            <label for="orderDate">Order Date:</label>
                        </div>
                        <div class="col-lg-2">
                            <div class="input-group date" id="processDate">
                                ${miscDetail.orderDate}

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
                                ${miscDetail.departmentName}
                            </div>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="fromDate">Office:</label>
                        </div>
                        <div class="col-lg-10">
                            <div class='input-group date' id='processDate'>
                                ${miscDetail.officeName}
                            </div>
                        </div>
                    </div>
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="">Post:</label>
                        </div>
                        <div class="col-lg-10">
                            <div class='input-group date' id='processDate'>
                                ${miscDetail.authPostName}
                            </div>
                        </div>
                    </div>    
                    
                    
                    <div class="row" style="margin-bottom: 7px;">
                        <div class="col-lg-2">
                            <label for="note">Note:</label>
                        </div>
                        <div class="col-lg-9">
                            ${miscDetail.note}
                        </div>
                        <div class="col-lg-1">
                        </div>
                    </div>

                </div>

                <div class="panel-footer">
                    <input type="button" value="&laquo; Back to Miscellaneous List" class="btn btn-default" style="background:#0076AA;color:#FFFFFF;" onclick="self.location = 'MiscInfoList.htm'" />
                </div>
            </div>
        </div>


    </body>
</html>
