<%-- 
    Document   : EmployeeCalendar
    Created on : Aug 9, 2018, 6:16:00 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- Custom CSS -->        
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            .muted{
                text-align: center;
            }
            .holiday{
                background-color: #CC0000;
                color: #FFFFFF;
                font-weight: bold;
                text-align: center;
            }
            .govtholiday{                
                text-align: center;
            }
            .govtholiday a{                
                color: #CC0000;
                font-weight: bold;
            }
            .holiday a{
                background-color: #CC0000;
                color: #FFFFFF;
                font-weight: bold;
            }
            .table-bordered > tbody > tr > td{
                text-align: center;
            }
        </style>
        <script>
            $(document).ready(function () {
                $('td[holiday="true"]').each(function (me) {
                    govtholiday = $(this).attr("govtholiday");
                    if (govtholiday) {
                        $(this).addClass("govtholiday");
                    } else {
                        $(this).addClass("holiday");
                    }
                });
                $('[data-toggle="tooltip"]').tooltip();
            });

        </script>
    </head>
    <body>
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="row" style="margin-bottom: 10px;margin-top: 10px;">                    
                    <c:forEach items="${calendarList}" var="calendar" begin="0" end="3">
                        <div class="col-lg-3" align="center">
                            <table class="table-condensed table-bordered table-striped" width="100%">
                                <thead>
                                    <tr>
                                        <th colspan="7" style="text-align: center;">
                                            <span class="btn-group">
                                                <p>${calendar.calendarMonth.monthName} ${calendar.calendarYear}</p>
                                            </span>
                                        </th>
                                    </tr>
                                    <tr>
                                        <th>Su</th>
                                        <th>Mo</th>
                                        <th>Tu</th>
                                        <th>We</th>
                                        <th>Th</th>
                                        <th>Fr</th>
                                        <th>Sa</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var = "week_data_row" begin = "0" end = "${calendar.calendarMonth.numberOfWeeks}">
                                        <tr>
                                            <c:forEach var = "i" begin = "0" end = "6">
                                                <c:choose>
                                                    <c:when test="${calendar.calendarMonth.days[week_data_row][i].caldate == 0}">
                                                        <td class="muted">&nbsp;</td> 
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:if test="${not empty calendar.calendarMonth.days[week_data_row][i].holidayName}">
                                                            <td holiday="${calendar.calendarMonth.days[week_data_row][i].isHoliday}" govtholiday="true"> 
                                                                <a href="#" data-toggle="tooltip" title="${calendar.calendarMonth.days[week_data_row][i].holidayName}">
                                                                    ${calendar.calendarMonth.days[week_data_row][i].caldate}
                                                                </a>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${empty calendar.calendarMonth.days[week_data_row][i].holidayName}">
                                                            <td holiday="${calendar.calendarMonth.days[week_data_row][i].isHoliday}"> 
                                                                ${calendar.calendarMonth.days[week_data_row][i].caldate}
                                                            </td>
                                                        </c:if>

                                                    </c:otherwise>
                                                </c:choose>

                                            </c:forEach>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </c:forEach>
                </div>


                <div class="row" style="margin-bottom: 10px;margin-top: 10px;">                    
                    <c:forEach items="${calendarList}" var="calendar" begin="4" end="7">
                        <div class="col-lg-3" align="center">
                            <table class="table-condensed table-bordered table-striped" width="100%">
                                <thead>
                                    <tr>
                                        <th colspan="7" style="text-align: center;">
                                            <span class="btn-group">
                                                <p>${calendar.calendarMonth.monthName} ${calendar.calendarYear}</p>
                                            </span>
                                        </th>
                                    </tr>
                                    <tr>
                                        <th>Su</th>
                                        <th>Mo</th>
                                        <th>Tu</th>
                                        <th>We</th>
                                        <th>Th</th>
                                        <th>Fr</th>
                                        <th>Sa</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var = "week_data_row" begin = "0" end = "${calendar.calendarMonth.numberOfWeeks}">
                                        <tr>
                                            <c:forEach var = "i" begin = "0" end = "6">
                                                <c:choose>
                                                    <c:when test="${calendar.calendarMonth.days[week_data_row][i].caldate == 0}">
                                                        <td class="muted">&nbsp;</td> 
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:if test="${not empty calendar.calendarMonth.days[week_data_row][i].holidayName}">
                                                            <td holiday="${calendar.calendarMonth.days[week_data_row][i].isHoliday}" govtholiday="true"> 
                                                                <a href="#" data-toggle="tooltip" title="${calendar.calendarMonth.days[week_data_row][i].holidayName}">
                                                                    ${calendar.calendarMonth.days[week_data_row][i].caldate}
                                                                </a>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${empty calendar.calendarMonth.days[week_data_row][i].holidayName}">
                                                            <td holiday="${calendar.calendarMonth.days[week_data_row][i].isHoliday}"> 
                                                                ${calendar.calendarMonth.days[week_data_row][i].caldate}
                                                            </td>
                                                        </c:if>
                                                    </c:otherwise>
                                                </c:choose>

                                            </c:forEach>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </c:forEach>
                </div>

                <div class="row" style="margin-bottom: 10px;margin-top: 10px;">                    
                    <c:forEach items="${calendarList}" var="calendar" begin="8" end="12">
                        <div class="col-lg-3" align="center">
                            <table class="table-condensed table-bordered table-striped" width="100%">
                                <thead>
                                    <tr>
                                        <th colspan="7" style="text-align: center;">
                                            <span class="btn-group">
                                                <p>${calendar.calendarMonth.monthName} ${calendar.calendarYear}</p>
                                            </span>
                                        </th>
                                    </tr>
                                    <tr>
                                        <th>Su</th>
                                        <th>Mo</th>
                                        <th>Tu</th>
                                        <th>We</th>
                                        <th>Th</th>
                                        <th>Fr</th>
                                        <th>Sa</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var = "week_data_row" begin = "0" end = "${calendar.calendarMonth.numberOfWeeks}">
                                        <tr>
                                            <c:forEach var = "i" begin = "0" end = "6">
                                                <c:choose>
                                                    <c:when test="${calendar.calendarMonth.days[week_data_row][i].caldate == 0}">
                                                        <td class="muted">&nbsp;</td> 
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:if test="${not empty calendar.calendarMonth.days[week_data_row][i].holidayName}">
                                                            <td holiday="${calendar.calendarMonth.days[week_data_row][i].isHoliday}" govtholiday="true"> 
                                                                <a href="#" data-toggle="tooltip" title="${calendar.calendarMonth.days[week_data_row][i].holidayName}">
                                                                    ${calendar.calendarMonth.days[week_data_row][i].caldate}
                                                                </a>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${empty calendar.calendarMonth.days[week_data_row][i].holidayName}">
                                                            <td holiday="${calendar.calendarMonth.days[week_data_row][i].isHoliday}"> 
                                                                ${calendar.calendarMonth.days[week_data_row][i].caldate}
                                                            </td>
                                                        </c:if>
                                                    </c:otherwise>
                                                </c:choose>

                                            </c:forEach>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </c:forEach>
                </div>


            </div>
        </div>
    </div>
</body>
</html>
