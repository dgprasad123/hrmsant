<%@taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%-- 
    Document   : SupperannuationProjectionGraphSixMonthOnlyAO
    Created on : 29 Jun, 2020, 8:01:43 PM
    Author     : Surendra
--%>


<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/highcharts.js"></script>
        <script src="js/drilldown.js">
            < script src = "js/exporting.js" ></script>
        <script src="js/export-data.js"></script>

        <style>
            #container {
                min-width: 310px;
                max-width: 800px;
                height: 400px;
                margin: 0 auto
            }

            .buttons {
                min-width: 310px;
                text-align: center;
                margin-bottom: 1.5rem;
                font-size: 0;
            }

            .buttons button {
                cursor: pointer;
                border: 1px solid silver;
                border-right-width: 0;
                background-color: #f8f8f8;
                font-size: 1rem;
                padding: 0.5rem;
                outline: none;
                transition-duration: 0.3s;
            }

            .buttons button:first-child {
                border-top-left-radius: 0.3em;
                border-bottom-left-radius: 0.3em;
            }

            .buttons button:last-child {
                border-top-right-radius: 0.3em;
                border-bottom-right-radius: 0.3em;
                border-right-width: 1px;
            }

            .buttons button:hover {
                color: white;
                background-color: rgb(158, 159, 163);
                outline: none;
            }

            .buttons button.active {
                background-color: #0051B4;
                color: white;
            }

        </style>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                <div class="container-fluid">
                    <form:form action="ViewSupperannuationProjectionGraphDrillDown.htm" commandName="command">

                        <div class="row">
                            <div class="col-lg-3 form-group">
                                Enter From Month
                            </div>
                            <div class="col-lg-3 form-group">
                                <form:select path="sltFromMonth" class="form-control"> 
                                    <option value=""> -- Select Month -- </option>
                                    <form:option value="1"> January </form:option>
                                    <form:option value="2"> February </form:option>
                                    <form:option value="3">  March </form:option>
                                    <form:option value="4"> April </form:option>
                                    <form:option value="5"> May </form:option>
                                    <form:option value="6"> June </form:option>
                                    <form:option value="7"> July </form:option>
                                    <form:option value="8"> August </form:option>
                                    <form:option value="9"> September </form:option>
                                    <form:option value="10"> October </form:option>
                                    <form:option value="11"> November </form:option>
                                    <form:option value="12"> December </form:option>

                                </form:select>
                            </div>
                            <div class="col-lg-3 form-group">
                                Enter To Month
                            </div>
                            <div class="col-lg-3 form-group">
                                <form:select path="sltToMonth" class="form-control"> 
                                    <option value=""> -- Select Month -- </option>
                                    <form:option value="1"> January </form:option>
                                    <form:option value="2"> February </form:option>
                                    <form:option value="3">  March </form:option>
                                    <form:option value="4"> April </form:option>
                                    <form:option value="5"> May </form:option>
                                    <form:option value="6"> June </form:option>
                                    <form:option value="7"> July </form:option>
                                    <form:option value="8"> August </form:option>
                                    <form:option value="9"> September </form:option>
                                    <form:option value="10"> October </form:option>
                                    <form:option value="11"> November </form:option>
                                    <form:option value="12"> December </form:option>

                                </form:select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-3 form-group">
                                Enter From Year
                            </div>
                            <div class="col-lg-3 form-group">

                                <form:select path="sltFromYear" class="form-control"> 
                                    <option value=""> -- Select Year-- </option>
                                    <c:forEach var="yearObj" items="${yearList}" varStatus="loop">
                                        <form:option value="${yearObj.value}">${yearObj.value} </form:option>
                                    </c:forEach>
                                </form:select>


                            </div>
                            <div class="col-lg-3 form-group">
                                Enter To Year
                            </div>
                            <div class="col-lg-3 form-group">

                                <form:select path="sltToYear" class="form-control"> 
                                    <option value="">-- Select Year-- </option>
                                    <c:forEach var="yearObj" items="${yearList}" varStatus="loop">
                                        <form:option value="${yearObj.value}">${yearObj.value} </form:option>
                                    </c:forEach>
                                </form:select>


                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-10"> </div>
                            <div class="col-lg-2 pull-right"> <button type="submit" class="form-control btn btn-danger">View</button> </div>
                        </div>

                        <div class="row">
                            <div class="col-lg-12">
                                <div id="container" style="width:100%"></div>

                                <script>



                // Create the chart
                Highcharts.chart('container', {
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'Superannuation Projection for the period ${fromMonth} , ${fromYear} - ${toMonth}, ${toYear}'
                    },
                    subtitle: {
                        text: 'Click the columns to view Department wise.'
                    },
                    accessibility: {
                        announceNewData: {
                            enabled: true
                        }
                    },
                    xAxis: {
                        type: 'category',
                        labels: {
                            rotation: -45,
                            style: {
                                fontSize: '13px',
                                fontFamily: 'Verdana, sans-serif'
                            }
                        }
                    },
                    yAxis: {
                        title: {
                            text: 'Number of Employees'
                        }

                    },
                    legend: {
                        enabled: false
                    },
                    plotOptions: {
                        series: {
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                                format: '{point.y}'
                            }
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                        pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> of total<br/>'
                    },
                    series: [
                        {
                            name: "Super Annuation",
                            colorByPoint: true,
                            data: ${periodProjectList}
                        }
                    ],
                    drilldown: {
                        series: [${deptwiseList}]
                    }
                });

                                </script>

                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>

    </div>      
</body>
</html>

