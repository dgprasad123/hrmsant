<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min.js"></script>  
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">

            <div class="panel panel-default">
                <div class="panel-header">
                    <div align="center" style="font-weight: bold;margin-top:20px;">
                        Schedule-II<br />
                        (Relating to Head of the Department, its attached Office and Sub-ordinate District Offices)<br /><br />
                        ANNUAL ESTABLISHMENT REVIEW TO BE FURNISHED BY HEADS OF DEPARTMENT TO THE ADMINISTRATIVE DEPARTMENT BY END OF
                        FEBRUARY EACH YEAR<br /><br />
                        The sanctioned strength of the Organisation including all its sub-ordinate offices as on the 1st January is as indicated
                        schedule-I relating to my own esthablishment as Head of the Office and in Schedule-I relating to all the establishments under my control
                        as budget Controlling Officer.<br />
                        I have reviewed the staff requirement having regard to the presdcribed yardsticks, wherever applicable. I certify that continuance
                        of all the posts except those set out in Schedule II-A is considered necessary. I further certify that orders have been issued terminating
                        such of the posts which need not continue beyond specified dates.
                    </div>
                </div><br />
                <div align="right">
                    <table width="90%">
                        <tr style="height:30px;">
                            <td width="50%"></td>
                            <td width="50%">
                                Signature____________________________________________
                            </td>
                        </tr>
                        <tr style="height:30px;">
                            <td></td>
                            <td>
                                Name_________________________________________________
                            </td>
                        </tr>
                        <tr style="height:30px;">
                            <td></td>
                            <td>
                                Designation__________________________________________
                            </td>
                        </tr>
                        <tr style="height:30px;">
                            <td></td>
                            <td>
                                Date_________________________________________________
                            </td>
                        </tr>
                        <tr style="height:30px;">
                            <td></td>
                            <td>
                                DDO Code_____________________________________________
                            </td>
                        </tr>
                        <tr style="height:30px;">
                            <td></td>
                            <td>
                                Budget Controlling Officer/<br />
                                Head of the Department Code:<br />
                                Administrative Department Code:
                            </td>
                        </tr>
                    </table>
                </div>
                <div class="panel-body">
                    <table class="table table-striped table-bordered" width="90%">
                        <thead>
                            <tr style="background-color: #d4d4d4;">
                                <th width="5%">SL No</th>
                                <th width="20%">Category of Employee<br />(Scale of Pay Wise)</th>
                                <th align="center" width="20%" colspan="3">Sanctioned Strength<br />of Teachers</th>
                                <th align="center" width="20%" colspan="3">Sanctioned Strength<br />of others(excluding teachers)</th>
                                <th align="center" width="20%" colspan="3">Total Sanctioned Strength of teachers and others</th>
                                <th align="center" width="20%" colspan="3">Vacancy Position of 1st Jan</th>
                            </tr>
                        </thead>
                        <hr />
                        <c:if test="${not empty data}">
                            <tbody>
                                <tr style="background-color: #d4d4d4;">
                                    <td>&nbsp;</td>
                                    <td>Category of Employee<br />(Scale of Pay Wise)</td>
                                    <td>Plan</td>
                                    <td>Non-Plan</td>
                                    <td>Total</td>
                                    <td>Plan</td>
                                    <td>Non-Plan</td>
                                    <td>Total</td>
                                    <td>Plan</td>
                                    <td>Non-Plan</td>
                                    <td>Total</td>
                                    <td>Plan Teacher/Other</td>
                                    <td>Non-Plan Teacher/Other</td>
                                    <td>Total Teacher/Other</td>
                                </tr>
                                <tr style="background-color: #d4d4d4;">
                                    <td>&nbsp;</td>
                                    <td>1</td>
                                    <td>2</td>
                                    <td>3</td>
                                    <td>4</td>
                                    <td>5</td>
                                    <td>6</td>
                                    <td>7</td>
                                    <td>8</td>
                                    <td>9</td>
                                    <td>10</td>
                                    <td>11</td>
                                    <td>12</td>
                                    <td>13</td>
                                </tr>
                                <c:forEach var="list" items="${data}" varStatus="count">
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${list.payscale}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.teacherSanctionedStrengthPlan}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.teacherSanctionedStrengthNonPlan}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.teacherSanctionedStrengthTotal}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.othersSanctionedStrengthPlan}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.othersSanctionedStrengthNonPlan}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.othersSanctionedStrengthTotal}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.totalPlan}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.totalNonPlan}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.totalSanctionedStrength}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.teacherVacancyPositionPlan}"/> / <c:out value="${list.othersVacancyPositionPlan}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.teacherVacancyPositionNonPlan}"/> / <c:out value="${list.othersVacancyPositionNonPlan}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.totalTeacherVacancyPosition}"/> / <c:out value="${list.totalOthersVacancyPosition}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <tr style="background-color: #d4d4d4;font-weight:bold;">
                                    <td>&nbsp;</td>
                                    <td>Total</td>
                                    <td>
                                        <c:out value="${teacherSanctionedStrengthPlanTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${teacherSanctionedStrengthNonPlanTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${teacherSanctionedStrengthTotalTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${othersSanctionedStrengthPlanTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${othersSanctionedStrengthNonPlanTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${othersSanctionedStrengthTotalTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${totalPlanTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${totalNonPlanTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${totalSanctionedStrengthTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${teacherVacancyPositionPlanTotal}"/> / <c:out value="${teacherVacancyPositionNonPlanTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${othersVacancyPositionPlanTotal}"/> / <c:out value="${othersVacancyPositionNonPlanTotal}"/>
                                    </td>
                                    <td>
                                        <c:out value="${totalTeacherVacancyPositionTotal}"/> / <c:out value="${totalOthersVacancyPositionTotal}"/>
                                    </td>
                                </tr>
                            </tbody>
                        </c:if>
                    </table>
                </div>
                <div class="panel-footer">

                </div>
            </div>
        </div>
    </body>
</html>
