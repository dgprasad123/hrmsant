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
            <jsp:param name="menuHighlight" value="FAMILYPAGESB" />
        </jsp:include>
        <div id="profile_container">
          
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <table class="table table-bordered">
                    <thead>
                        <tr class="bg-primary text-white">
                            <th>#</th>
                            <th>Name</th>
                            <th>Relation</th>
                            <th>Gender</th>
                             <th>Nominee</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                         <c:set var="nomineeStaus" value="N" />
                        <c:forEach items="${familyRelationList}" var="familyRelation" varStatus="cnt">
                            <tr>
                                <th scope="row">${cnt.index+1}</th>
                                <td>${familyRelation.initials} ${familyRelation.fname} ${familyRelation.mname} ${familyRelation.lname}</td>
                                <td>${familyRelation.relation}</td>
                                <td>${familyRelation.gender}</td>
                                 <td>
                                        <c:if test="${familyRelation.is_Nominee eq 'Y'}">
                                            <c:set var="nomineeStaus" value="Y" />
                                            <img src="images/verified.png" width="20" height="20"/>
                                        </c:if>
                                    </td>
                                <c:if test="${familyRelation.isLocked eq 'N'}">
                                    <td>
                                   <a href="employeeFamilyDetail.htm?slno=${familyRelation.slno}" class="btn btn-default"><span class="glyphicon glyphicon-pencil"></span> Edit</a>                                       
                                    </td>
                                </c:if>
                                <c:if test="${familyRelation.isLocked eq 'Y'}">
                                    <td><img src="images/Lock.png" width="20" height="20"/></td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                         <c:if test="${nomineeStaus eq 'N'}">
                             <strong style="color:red">**Please update your Nominee Details(Atleast one of the family member must be a Nominee)  </strong>
                         </c:if>
            </div>
            <div class="row" style="border: 1px solid #ddd;padding: 5px;">
                <form:form action="employeeFamilyNew.htm">
                    <table class="table table-bordered">
                        <thead>
                            <tr class="bg-primary text-white">
                                <th>
                                   <input type="submit" name="action" value="Add New" class="btn btn-default"/>                                    
                                </th>
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
