<%-- 
    Document   : HierarchyList
    Created on : 9 Jan, 2018, 12:19:15 PM
    Author     : Surendra
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <title>JSP Page</title>
    </head>
    <body>
        <div class="list-group">
            <h1 style="text-align:center;color:#008900;font-weight:bold;">Select Generic Post</h1>
            <ul style="list-style-type:none;">
                <c:forEach var="postlist" items="${PostList}">
                    <li style="float:left;width:30%;margin-right:10px;margin-bottom:10px;"><a href="showworkflowrouting.htm?postcode=<c:out value="${postlist.postcode}"/>" class="list-group-item" style="color:#000890;font-weight:bold;"><c:out value="${postlist.post}"/></a></li>
                </c:forEach>
            </ul>
        </div> 
    </body>
</html>
