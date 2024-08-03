<%-- 
    Document   : MyProfile
    Created on : Aug 14, 2018, 2:30:50 PM
    Author     : Manas
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script type="text/javascript" src="js/jquery.min.js"></script>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function deleteLanguage(slno) {
                if (confirm("Are you sure to Delete?")) {
                    self.location = "deleteEmployeeLanguage.htm?slno=" + slno;
                }
            }
            function editLanguage(slno) {
                self.location = "editEmployeeSingleLanguage.htm?slno=" + slno;
            }
        </script>
    </head>
    <body style="padding-top: 10px;">
        <jsp:include page="ProfileTabs.jsp">
            <jsp:param name="menuHighlight" value="LANGUAGEPAGESB" />
        </jsp:include>
        <div id="profile_container">
       
         
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <table class="table table-bordered">
                    <thead>
                        <tr class="bg-primary text-white">
                            <th width="5%">#</th>
                            <th width="20%">Language</th>
                            <th width="10%">Can read</th>
                            <th width="10%">Can Write</th>
                            <th width="10%">Can Speak</th>
                            <th width="20%">Is Mother Tongue</th>
                            <th width="25%">Action</th>                            
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${emplanguageList}" var="emplanguage" varStatus="cnt">
                            <tr>
                                <th scope="row">${cnt.index+1}</th>
                                <td>${emplanguage.language}</span></td>
                                <td><c:if test="${emplanguage.ifread eq 'Y'}"><span style="color: #008000;" class="glyphicon glyphicon-ok"></c:if></td>
                                <td><c:if test="${emplanguage.ifwrite eq 'Y'}"><span style="color: #008000;" class="glyphicon glyphicon-ok"></c:if></td>
                                <td><c:if test="${emplanguage.ifspeak eq 'Y'}"><span style="color: #008000;" class="glyphicon glyphicon-ok"></c:if></td>
                                <td><c:if test="${emplanguage.ifmlang eq 'Y'}"><span style="color: #008000;" class="glyphicon glyphicon-ok"></c:if></td>                            
                                 <td>
                                    <c:if test="${emplanguage.isLocked eq 'N'}">
                                        <a href="javascript:editLanguage('${emplanguage.slno}');">Edit</a>&emsp;
                                        <a href="javascript:deleteLanguage('${emplanguage.slno}');">Delete</a>
                                    </c:if>
                                    <c:if test="${emplanguage.isLocked eq 'Y'}">
                                        <img src="images/Lock.png" width="20" height="20"/>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="employeeLanguageEdit.htm">
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th><input type="submit" name="action" value="Add" class="btn btn-default"/></th>
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
