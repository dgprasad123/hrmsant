<%-- 
    Document   : ViewRecommendationDetail
    Created on : 30 Oct, 2020, 12:39:42 PM
    Author     : Manisha
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style type="text/css">
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
        <script type="text/javascript">
            function requestNOC(me) {
                $(me).val("NOC Requested");
            }

        </script>
    </head>
    <body>
        <div class="container-fluid">
            <form:form action="submittodepartment.htm" commandName="recommendationDetailBean" method="post" onsubmit="return confirm('Are you sure you want to submit?')">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Nominated Employee List
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>                                                                
                                    <th width="3%">#</th>
                                    <th width="8%">Emp Id</th>
                                    <th width="8%">GPF / PRAN No</th>
                                    <th width="8%">Emp Name</th>                                    
                                    <th width="8%">Designation</th>
                                    <th width="8%">Details</th>
                                    <th width="8%">Status</th>
                                </tr>                            
                            </thead>
                            <tbody>
                                <c:forEach items="${recommendationDetailList}" var="recommendation" varStatus="count">
                                    <tr>
                                        <td>${count.index+1}</td>
                                        <td>${recommendation.recommendedempId}</td>
                                        <td>${recommendation.recommendedempGpfNo}</td>
                                        <td>${recommendation.recommendedempname}</td>                                        
                                        <td>${recommendation.recommendedpost}</td>
                                        <td>                                            
                                            <a href="viewRecommendedEmployeeDetail.htm?recommendeddetailId=${recommendation.recommendeddetailId}" class="btn btn-default" >Detail </a>
                                        </td>
                                        <td>
                                            <c:if test="${recommendation.isApproved eq 'Y'}">
                                                <span class="label label-success">Approved</span>
                                            </c:if>
                                            <c:if test="${recommendation.isApproved eq 'N'}">
                                                <span class="label label-danger">Declined</span>
                                            </c:if>
                                            <c:if test="${recommendation.isApproved ne 'Y' && recommendation.isApproved ne 'N'}">
                                                <span class="label label-info">Pending</span>
                                            </c:if>
                                        </td>                                      
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <form:hidden path="taskId"/>
                        <form:hidden path="recommendationId"/>
                        <c:if test="${recommendationDetailBean.isSubmittedToDept ne 'Y'}">
                            
                        </c:if>



                    </div>
                </div>
            </form:form>
        </div>        
    </body>
</html>
