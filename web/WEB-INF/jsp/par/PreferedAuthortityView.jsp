<%-- 
    Document   : PreferedAuthortityView
    Created on : May 19, 2020, 5:54:06 PM
    Author     : manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <input type="hidden" name="appriseSPC" id="appriseSPC" value="${appriseSPC}"/>
        <table class="table" >
            <thead>
                <tr>
                    <th>#</th>
                    <th>Name of the Employee/Substantive Post</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${sanctionedauthoritylist}" var="authoritylist" varStatus="cnt">
                <tr>
                    <td>${cnt.index+1}</td>
                    <td>${authoritylist.empname} / ${authoritylist.post}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
