<%-- 
    Document   : AdminGrievanceDashBoard
    Created on : Jan 30, 2018, 12:56:29 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/grievancedashboard.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
        <script type="text/javascript">
            google.charts.load('current', {'packages': ['bar']});
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var data = google.visualization.arrayToDataTable([
                    ['Month', 'Received', 'Rejected', 'Pending', 'Disposed'],
                    ['January', 1000, 400, 200, 400],
                    ['February', 1170, 460, 250, 300],
                    ['March', 660, 1120, 300, 430],
                    ['April', 660, 1120, 300, 430],
                    ['May', 660, 1120, 300, 430],
                    ['June', 660, 1120, 300, 430],
                    ['July', 660, 1120, 300, 430],
                    ['August', 660, 1120, 300, 430],
                    ['September', 660, 1120, 300, 430],
                    ['October', 660, 1120, 300, 430],
                    ['November', 660, 1120, 300, 430],
                    ['December', 1030, 540, 350, 540]
                ]);

                var options = {
                    chart: {
                        title: 'Grievance Summary',
                        subtitle: 'Year: 2017',
                    }
                };

                var chart = new google.charts.Bar(document.getElementById('columnchart_material'));

                chart.draw(data, google.charts.Bar.convertOptions(options));
            }
        </script>
    </head>
    <body>
        <div class="col-md-12 col-sm-12" style="padding:10px;">
            <div class="market-updates" style="margin-top: 20px;">
                <div class="col-md-3 market-update-gd" >
                    <div class="market-update-block clr-block-1" style="box-shadow: 10px 10px 5px #888888;">
                        <div class="col-md-12 market-update-left">
                            <h3 align="center"><a href="adminGrievanceDashBoardDtl.htm" style="color: #fff;">Received</a></h3>
                            <h4 align="center">${dashBoardDetail.totalGrievance}</h4>
                        </div>

                        <div class="clearfix"> </div>
                    </div>
                </div>
                <div class="col-md-3 market-update-gd">
                    <div class="market-update-block clr-block-2" style="box-shadow: 10px 10px 5px #888888;">
                        <div class="col-md-12 market-update-left">
                            <h3 align="center"><a href="adminGrievanceDashBoardDtl.htm?status=D" style="color: #fff;">Disposed</a></h3>
                            <h4 align="center">${dashBoardDetail.disposedGrievance}</h4>
                        </div>
                        <div class="clearfix"> </div>
                    </div>
                </div>
                <div class="col-md-3 market-update-gd">
                    <div class="market-update-block clr-block-3" style="box-shadow: 10px 10px 5px #888888;">
                        <div class="col-md-12 market-update-left">
                            <h3 align="center"><a href="adminGrievanceDashBoardDtl.htm?status=R" style="color: #fff;">Rejected</a></h3>
                            <h4 align="center">${dashBoardDetail.rejectedGrievance}</h4>
                        </div>
                        <div class="clearfix"> </div>
                    </div>
                </div>
                <div class="col-md-3 market-update-gd">
                    <div class="market-update-block clr-block-4" style="box-shadow: 10px 10px 5px #888888;">
                        <div class="col-md-12 market-update-left">
                            <h3 align="center"><a href="adminGrievanceDashBoardDtl.htm?status=P" style="color: #fff;">Pending</a></h3>
                            <h4 align="center">${dashBoardDetail.pendingGrievance}</h4>
                        </div>
                        <div class="clearfix"> </div>
                    </div>
                </div>
                <div class="clearfix"> </div>
            </div>

            <h4 style="margin:10px;">Below Graph is a placeholder.(Real Graph is under Construction)</h4>
            <div class="row" style="margin-top:30px;">
                <div class="col-md-12">
                    <div id="columnchart_material" style="width: 98%; height: 500px;border:1px solid #E3E3E3;padding:5px;box-shadow: 10px 10px 5px #888888;"></div>
                </div>
            </div>
        </div>
    </body>
</html>
