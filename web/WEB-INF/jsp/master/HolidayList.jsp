<%-- 
    Document   : ModuleList
    Created on : Nov 21, 2016, 6:08:30 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <script src="js/bootstrap.min.js"></script>
        <script src="js/moment.js"></script>
        <script>
            function showNewHolidayWindow() {
                clearform("myModal");
                $("#myModal").modal('show');
            }
            function clearform(divid) {
                $("#" + divid).find('input').each(function () {
                    $(this).val("");
                });
            }
            function editHoliday(hid) {
                url = "editholiday.htm?hid=" + hid;
                clearform("myModal");
                $.getJSON(url, function (data) {
                    $("#myModal").find('input,select').each(function () {
                        if ($(this).attr("type") == "date") {
                            var temp = $(this).attr("id");
                            $(this).val(moment(data[temp], 'YYYY-MM-DD').format('DD/MM/YYYY'));
                        } else {
                            var temp = $(this).attr("id");
                            $(this).val(data[temp]);
                        }
                    });
                    //fname mname lname
                });

                $("#myModal").modal('show');
            }
            function removeHoliday(hid) {
                var url = 'removeHoliday.htm?hid=' + hid;
                $.ajax({url: url, success: function (result) {
                        if (result == 1) {
                            getHolidayList();
                            alert("Deleted");
                        } else {
                            alert("Some Error Occured");
                        }
                    }});

            }
            function saveHoliday() {
                var holidayName = $("#holidayName").val();
                var holidayType = $("#holidayType").val();
                var fdate = $("#fdate").val();
                var tdate = $("#tdate").val();
                var url = 'saveHoliday.htm?holidayName=' + holidayName + '&holidayType=' + holidayType + '&fdate=' + fdate + '&tdate=' + tdate;
                $.ajax({url: url, success: function (result) {
                        if (result) {
                            getHolidayList();
                            alert("Saved");
                        } else {
                            alert("Some Error Occured");
                        }
                    }});
            }
            function getHolidayList() {
                if ($('#year').val() == "") {
                    alert("Choose Year");
                    return false;
                }
                $('#holidayList').empty();
                var url = 'holidayListJSON.htm?year=' + $('#year').val();
                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        var noofdays = 0;
                        var one_day = 1000 * 60 * 60 * 24;
                        var fromDate = new Date(obj.fdate).getTime();
                        var toDate = new Date(obj.tdate).getTime();
                        noofdays = Math.round((toDate - fromDate) / one_day) + 1;
                        $('#holidayList').append('<tr><td>' + (i + 1) + '</td><td>' + obj.holidayName + '</td><td>' + obj.holidayType + '</td><td>' + obj.fdate + ' </td><td> ' + obj.tdate + '</td><td>' + noofdays + '</td><td>' + '<a href="javascript:removeHoliday(' + obj.holidayId + ')" class="btn  btn-default"><span class="glyphicon glyphicons-remove"></span>Remove</a>' + '<a href="javascript:editHoliday(' + obj.holidayId + ')" class="btn  btn-default"><span class="glyphicon glyphicons-remove"></span>Edit</a>' + '</td></tr>');
                    });
                });
            }
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Holiday List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="javascript:showNewHolidayWindow()">New Holiday</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="form-group">
                                <label>Office</label>
                                <select class="form-control"  name="year" id="year">
                                    <option value="2009">2009</option>
                                    <option value="2010">2010</option>
                                    <option value="2011">2011</option>
                                    <option value="2012">2012</option>
                                    <option value="2013">2013</option>
                                    <option value="2014">2014</option>
                                    <option value="2015">2015</option>
                                    <option value="2016">2016</option>
                                    <option value="2017">2017</option>
                                    <option value="2018">2018</option>
                                    <option value="2019">2019</option>
                                    <option value="2020">2020</option>
                                    <option value="2021">2021</option>
                                    <option value="2022">2022</option>
                                    <option value="2023">2023</option>
                                    <option value="2024">2024</option>
                                </select>
                            </div>
                            <button type="button" class="btn btn-default" onclick="getHolidayList()">Show Holiday</button>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Holiday List</h2>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>Description</th>
                                            <th>Type</th>
                                            <th>From Date</th>
                                            <th>To date</th>
                                            <th>No of Days</th>
                                            <th>Action</th>                                            
                                        </tr>
                                    </thead>
                                    <tbody id="holidayList">                                        

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">New Holiday</h4>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label>Holiday Name</label>                            
                            <input type="text" class="form-control"  name="holidayName" id="holidayName" />
                        </div>
                        <div class="form-group">
                            <label>Holiday Type</label>
                            <select name="holidayType" id="holidayType" class="form-control">
                                <option value="G">Fixed Holiday</option>
                                <option value="O">Optional Holiday</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>From Date</label>
                            <input type="date" class="form-control"  name="fdate" id="fdate" placeholder="dd/mm/yyyy" />
                        </div>
                        <div class="form-group">
                            <label>To date</label>
                            <input type="date" class="form-control"  name="tdate" id="tdate" placeholder="dd/mm/yyyy" />
                        </div>
                    </div>
                    <div class="modal-footer">
                        <span id="msg"></span>
                        <button type="button" class="btn btn-default" onclick="saveHoliday()">Save</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>
