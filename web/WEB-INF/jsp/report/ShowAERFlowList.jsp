<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        

        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>

    </head>
    <body>
        <div class="container" style="width:100%">
            <c:if test="${not empty flowlist}">
                <table class="table">
                    <tr>
                        <th>Action Date</th>
                        <th>Action By</th>
                        <th>Message(if any)</th>
                    </tr>
                    <c:forEach var="list" items="${flowlist}">
                        <tr>
                            <td>
                                <c:out value="${list.actionDate}"/>
                            </td>
                            <td>
                                <c:out value="${list.actionBy}"/>
                            </td>
                            <td>
                                <c:out value="${list.message}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </div>
    </body>
</html>
