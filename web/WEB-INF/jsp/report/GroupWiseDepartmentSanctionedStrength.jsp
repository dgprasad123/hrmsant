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
                        DEPARTMENT WISE POST
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-striped table-bordered" width="90%">
                        <thead>
                            <tr>
                                <th width="5%">SL No</th>
                                <th width="40%">Department</th>
                                <th width="15%">Group A</th>
                                <th width="15%">Group B</th>
                                <th width="15%">Group C</th>
                                <th width="15%">Group D</th>
                            </tr>
                        </thead>
                        <hr />
                        <c:if test="${not empty deptlist}">
                            <tbody>
                                <tr>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td colspan="4" align="center">Sanctioned Strength / Men in Position</td>
                                </tr>
                                <c:forEach var="list" items="${deptlist}" varStatus="count">
                                    <tr>
                                        <td>
                                            ${count.index + 1}
                                        </td>
                                        <td>
                                            <c:out value="${list.deptName}"/>
                                        </td>
                                        <td>
                                            <!--<a href="getOfficeWiseSanctionedPostData.htm?deptCode=<c:out value="${list.deptCode}"/>"><c:out value="${list.groupA_SanctionedStrength}"/></a> /
                                            <a href="getOfficeWiseMenInPosition.htm?deptCode=<c:out value="${list.deptCode}"/>"><c:out value="${list.groupA_MenInPosition}"/></a>-->
                                            <c:out value="${list.groupA_SanctionedStrength}"/> /
                                            <c:out value="${list.groupA_MenInPosition}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.groupB_SanctionedStrength}"/> /
                                            <c:out value="${list.groupB_MenInPosition}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.groupC_SanctionedStrength}"/> /
                                            <c:out value="${list.groupC_MenInPosition}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.groupD_SanctionedStrength}"/> /
                                            <c:out value="${list.groupD_MenInPosition}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
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
