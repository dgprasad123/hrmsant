<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div class="container" style="width:100%">
            <c:if test="${not empty employeelist}">
                <c:forEach var="list" items="${employeelist}">
                    <div class="row">
                        <div class="col-lg-5 col-xs-12 col-centered">
                            <c:out value="${list.empName}"/>
                        </div>
                        <div class="col-lg-7 col-xs-12" style="border-left: 1px solid black;">
                            <c:out value="${list.sltpostName}"/>
                        </div>
                    </div>
                    <hr />
                </c:forEach>
            </c:if>
        </div>
    </body>
</html>
