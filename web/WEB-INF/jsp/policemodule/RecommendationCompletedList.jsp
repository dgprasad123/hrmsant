<%-- 
    Document   : RecommendationCompletedList
    Created on : 8 Dec, 2020, 12:25:45 PM
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
                                Recommendation Completion Report
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
                                <th width="5%"> Sl No </th>
                                <th width="15%"> Dist/Establishment </th>
                                <th width="15%"> Full Name </th>
                                <th width="10%"> Category </th>
                                <th width="10%"> DOB </th>
                                <th width="10%"> Date of enlistment with rank </th>
                                <th width="10%"> Date of joining as SUB INSPECTOR </th>
                                <th width="10%"> Remarks of Nomination Authority </th>
                                
                                <th width="10%"> Recommend Status</th>
                                <th width="5%"> View </th>
                                <th width="10%"> Download Form</th>
                                <th width="10%"> Attachment </th>
                                <th width="10%"> Action </th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:if test="${not empty listData}">
                                
                                <c:forEach items="${listData}" var="list" varStatus="count">

                                    <tr>
                                        <td><c:out value="${count.index + 1}"/></td>
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
                                        <td>
                                            <c:out value="${list.remarksNominationGeneral}"/>
                                        </td>
                                        
                                        <td>
                                            <c:if test="${list.recommendStatus eq 'NOT RECOMMENDED'}">
                                                <span style="color:red"><c:out value="${list.recommendStatus}"/></span>
                                            </c:if>
                                           <c:if test="${list.recommendStatus eq 'RECOMMENDED'}">
                                                <c:out value="${list.recommendStatus}"/>
                                            </c:if>
                                        </td>
                                        <td>
                                            <a href="EmployeeNominationViewControllerForRange.htm?nominationMasterId=${list.nominationMasterId}&nominationDetailId=${list.nominationDetailId}" target="_blank">View</a>
                                        </td>
                                        <td>
                                            <a href="downloadNominationFormByRecommend.htm?nominationMasterId=${list.nominationMasterId}&nominationDetailId=${list.nominationDetailId}" target="_blank"><i class="fa fa-file-pdf-o" aria-hidden="true"></i></a>
                                        </td>
                                        <td>
                                            <a href="downloadAllAttachment.htm?nominationMasterId=${list.nominationMasterId}&nominationDetailId=${list.nominationDetailId}" target="_blank"><i class="fa fa-paperclip" aria-hidden="true"></i></a>
                                        </td>
                                        <td>
                                            <a href="RevertNominationFromDGOffice.htm?nominationMasterId=${list.nominationMasterId}" onclick="return confirm('Are you sure to Revert?');">Revert</a>
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



