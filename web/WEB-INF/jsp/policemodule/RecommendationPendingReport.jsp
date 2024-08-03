<%-- 
    Document   : RecommendationPendingReport
    Created on : 8 Dec, 2020, 12:26:10 PM
    Author     : Surendra
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
                    <div class="row">
                        <div class="col-lg-12">
                            <h2 align="center">
                                Recommendation Pending Report
                            </h2>
                            <h3 align="center">
                                <u> Nomination for ${nominationPost}</u>
                            </h3>

                        </div>
                    </div>
                    <hr />

                </div>
                <div class="panel-body">
                    <table class="table table-bordered" width="100%">
                        <thead>
                            <tr>
                                <th width="5%">Sl No</th>
                                <th width="15%">Range Name</th>
                                <th width="20%">Dist/Establishment</th>
                                <th width="20%">Full Name</th>
                                <th width="10%">Category</th>
                                <th width="10%">DOB</th>
                                <th width="10%"> Date of enlistment with rank </th>
                                <th width="10%"> Date of joining as Inspector </th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty listData}">
                                
                                <c:forEach items="${listData}" var="list" varStatus="count">

                                    <tr>
                                        <td><c:out value="${count.index + 1}"/></td>
                                        <td>
                                            <c:out value="${list.officeCode}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.officeName}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.fullname}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.category}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.dob}"/>
                                        </td>
                                        
                                        <td>
                                           <c:out value="${list.doeGov}"/>
                                        </td>
                                        <td>
                                            <c:out value="${list.jodInspector}"/>
                                        </td>
                                    </tr>
                                </c:forEach>

                            </c:if>
                            
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">

                </div>
            </div>
        </div>
    </body>
</html>


