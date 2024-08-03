<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    DPCs Conducted in DEC-2020 
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-3">
                            Name of the Department/HOD/District:
                        </div>
                        <div class="col-lg-9">
                            <strong><c:out value="${officename}"/></strong>
                        </div>
                    </div>
                        <table class="table table-bordered" style="margin-top:40px;">
                        <thead>
                            <tr>
                                <th rowspan="2">Sl No</th>
                                <th rowspan="2">No. of DPCs due to be conducted in Dec-2020</th>
                                <th rowspan="2">No. of DPCs actually conducted</th>
                                <th colspan="5" style="text-align: center">No of employees considered for promotion in 2021</th>
                                <th rowspan="2">No of DPCs not conducted</th>
                                <th rowspan="2">Reason(s) thereof</th>
                            </tr>
                            <tr>
                                <th>Group-A</th>
                                <th>Group-B</th>
                                <th>Group-C</th>
                                <th>Group-D</th>
                                <th>Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${dpcconductedList}" var="dlist" varStatus="cnt">
                                <tr>
                                    <td width="8%">${cnt.index+1}</td>
                                    <td width="8%">
                                        <c:out value="${dlist.noDPCtobeConducted}"/>
                                    </td>
                                    <td width="8%">
                                        <c:out value="${dlist.noDPCConducted}"/>
                                    </td>
                                    <td width="8%">
                                        <c:out value="${dlist.groupAemp}"/>
                                    </td>
                                    <td width="8%">
                                        <c:out value="${dlist.groupBemp}"/>
                                    </td>
                                    <td width="8%">
                                        <c:out value="${dlist.groupCemp}"/>
                                    </td>
                                    <td width="8%">
                                        <c:out value="${dlist.groupDemp}"/>
                                    </td>
                                    <td width="8%">
                                        <c:out value="${dlist.groupTotal}"/>
                                    </td>
                                    <td width="8%">
                                        <c:out value="${dlist.noDPCNotConducted}"/>
                                    </td>
                                    <td width="36%">
                                        <c:out value="${dlist.reason}"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">
                    <form:form action="addDPCConductedPage.htm" method="post" commandName="dpcform">
                        <input type="submit" value="Add "class="btn btn-danger"/>
                    </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
