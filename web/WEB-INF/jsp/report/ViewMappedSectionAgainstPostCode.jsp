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

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default"></div>
            <div class="panel-heading"></div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-lg-4"></div>
                    <div class="col-lg-4">
                        <span style="font-weight: bold; font-size: 18px;">Mapped Section List</span>
                    </div>
                    <div class="col-lg-4"></div>
                </div>
                <table class="table table-striped table-bordered" width="90%">
                    <thead>
                        <tr>
                            <th width="30%"></th>
                            <th width="10%">Sl No</th>
                            <th width="30%">Section Name</th>
                            <th width="30%"></th>
                        </tr>
                        <c:if test="${not empty mappedsectionnamelist}">
                            <c:forEach var="sectionname" items="${mappedsectionnamelist}" varStatus="count">
                                <tr>
                                    <td>&nbsp;</td>
                                    <td>${count.index + 1}</td>
                                    <td>${sectionname.label}</td>
                                    <td>&nbsp;</td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </thead>
                </table>
            </div>
            <div class="panel-footer"></div>
        </div>
    </body>
</html>
