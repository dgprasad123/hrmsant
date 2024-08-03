<%-- 
    Document   : MyProfile
    Created on : Aug 14, 2018, 2:30:50 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>        
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="EDUCATIONPAGESB" />
        </jsp:include>
        <div id="profile_container">
           
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <table class="table table-bordered">
                    <thead>
                        <tr class="bg-primary text-white">
                            <th>#</th>
                            <th>Qualification</th>
                            <th>Stream</th>
                            <th>Year of Pass</th>
                            <th>Degree</th>
                            <th>Subject</th>
                            <th>Institute</th>
                            <th>Board/University</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${educationList}" var="education" varStatus="cnt">
                            <tr>
                                <th scope="row">${cnt.index+1}</th>
                                <td>${education.qualification}</td>
                                <td>${education.faculty}</td>
                                <td>${education.yearofpass}</td>
                                <td>${education.degree}</td>
                                <td>${education.subject}</td>
                                <td>${education.institute}</td>
                                <td>${education.board}</td>
                                <c:if test="${education.isLocked eq 'N'}">
                                    <td><a href="employeeEducationDetail.htm?qfn_id=${education.qfn_id}" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> Edit</a></td>
                                </c:if>
                                <c:if test="${education.isLocked eq 'Y'}">
                                    <td><img src="images/Lock.png" width="20" height="20"/></td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="employeeEducationNew.htm" method="post" commandName="address">
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th><input type="submit" name="action" value="Add New" class="btn btn-default"/></th>
                            </tr>
                        </thead>
                    </table>
                </form:form>
            </div>
        </div>
    </body>
    <script type="text/javascript">
        $(document).ready(function () {
            setTimeout(function () {
                $("#notification_blk").slideUp();
            }, 5000);
        });
    </script> 
</html>
