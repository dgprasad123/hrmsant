<%-- 
    Document   : ViewSupperannuationProjectionGraphOnlyDept
    Created on : 20 Jun, 2020, 9:06:42 AM
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
        <script src="js/exporting.js"></script>
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
                
                
                
                <div class='buttons'>

                    <c:forEach var="yearObj" items="${yearList}" varStatus="loop">
                        <c:if test="${loop.index lt 1}">
                            <button id='${yearObj.value}' class="active">
                        </c:if>
                        
                        <c:if test="${loop.index gt 0}">
                            <button id='${yearObj.value}'>
                        </c:if>
                        
                            ${yearObj.value}
                        </button>
                    </c:forEach>



                </div>
                <div class="pull-right"> <a href ="ViewSupperannuationProjectionGraphDrillDown.htm"> Department Wise Projection </a> </div>
                <div id="container"></div>

                <script>
                    var dataPrev = {
                    ${prevData}

                    };

                    var data = {
                    ${curData}
                    };

                    var groups = [{
                            name: 'Group-A',
                            color: 'rgb(201, 36, 39)'
                        }, {
                            name: 'Group-B',
                            color: 'rgb(201, 36, 39)'
                        }, {
                            name: 'Group-C',
                            color: 'rgb(0, 82, 180)'
                        }, {
                            name: 'Group-D',
                            color: 'rgb(0, 0, 0)'
                        }];


                    function getData(data) {
                        return data.map(function (country, i) {
                            return {
                                name: country[0],
                                y: country[1],
                                color: groups[i].color
                            };
                        });
                    }

                    var chart = Highcharts.chart('container', {
                        chart: {
                            type: 'column'
                        },
                        title: {
                            text: 'Superannuation Projection Report- Group Wise.'
                        },
                        plotOptions: {
                            series: {
                                grouping: false,
                                borderWidth: 0
                            }
                        },
                        legend: {
                            enabled: false
                        },
                        tooltip: {
                            shared: true,
                            headerFormat: '<span style="font-size: 15px">{point.point.name}</span><br/>',
                            pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y} Employees</b><br/>'
                        },
                        xAxis: {
                            type: 'category',
                            max: 3,
                            labels: {
                                useHTML: true,
                                animate: true,
                                formatter: function () {
                                    var value = this.value,
                                            output;

                                    groups.forEach(function (country) {
                                        if (country.name === value) {
                                            output = country.name;
                                        }
                                    });

                                    return '<span>' + output + '</span>';
                                }
                            }

                        },
                        yAxis: [{
                                title: {
                                    text: 'No. of Employees'
                                },
                                showFirstLabel: false
                            }],
                        series: [{
                                color: 'rgb(158, 159, 163)',
                                pointPlacement: -0.2,
                                linkedTo: 'main',
                                data: dataPrev[${defaultYear}].slice(),
                                name: '${prevdefaultYear}'
                            }, {
                                name: '${defaultYear}',
                                id: 'main',
                                dataSorting: {
                                    enabled: true,
                                    matchByName: true
                                },
                                dataLabels: [{
                                        enabled: true,
                                        inside: true,
                                        style: {
                                            fontSize: '16px'
                                        }
                                    }],
                                data: getData(data[${defaultYear}]).slice()
                            }],
                        exporting: {
                            allowHTML: true
                        }
                    });

                    var years = [${yearArray}];

                    years.forEach(function (year) {
                        var btn = document.getElementById(year);

                        btn.addEventListener('click', function () {

                            document.querySelectorAll('.buttons button.active').forEach(function (active) {
                                active.className = '';
                            });
                            btn.className = 'active';

                            chart.update({
                                title: {
                                    text: 'Superannuation Projection Report ' + year + ' '
                                },
                                series: [{
                                        name: year - 1,
                                        data: dataPrev[year].slice()
                                    }, {
                                        name: year,
                                        data: getData(data[year]).slice()
                                    }]
                            }, true, false, {
                                duration: 800
                            });
                        });
                    });

                </script>

            </div>
        </div>      
    </body>
</html>
