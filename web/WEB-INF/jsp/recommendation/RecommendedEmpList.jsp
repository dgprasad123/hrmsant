<%-- 
    Document   : RecommendedEmpList
    Created on : 19 Oct, 2020, 11:18:30 AM
    Author     : Manisha
--%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
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
            function openaddGroupCWindow() {
                $('#empDetail').modal('show');
            }
            function removeEmployee(recommendeddetailId, me) {
                var url = "removerecommendationEmployeeList.htm";
                $.post(url, {recommendeddetailId: recommendeddetailId})
                        .done(function (data) {
                            if (data.msg == "Y") {
                                $(me).parent().parent().remove();
                                alert("Employee Sucessfully Removed");
                            } else {
                                alert("Some Error Occured");
                            }
                        });

            }
        </script>
    </head>
    <body>
        <form:form commandName="recommendationDetailBean" method="post" action="viewRecommendedEmployeeList.htm">
            <form:hidden path="recommendationId"/>
            <form:hidden path="recommenadationType"/>
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Selected Nomination Employee List
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>                                                                
                                    <th width="3%">#</th>
                                    <th width="8%">Emp Id</th>
                                    <th width="8%">GPF No</th>
                                    <th width="8%">Emp Name</th>                                    
                                    <th width="8%">Designation</th>
                                    <th width="8%">Details</th>
                                    <th width="8%">
                                        <c:if test="${recommendationDetailBeanObj.taskId eq 0 && recommendationDetailBeanObj.isSubmittedToDept ne 'Y'}">
                                            Action
                                        </c:if>
                                        <c:if test="${recommendationDetailBeanObj.isSubmittedToDept eq 'Y'}">
                                            Sent to
                                        </c:if>
                                    </th>
                                </tr>                            
                            </thead>
                            <tbody>
                                <c:forEach items="${recommendedList}" var="recommendation" varStatus="count">
                                    <tr <c:if test="${recommendation.dataintegrity eq 'N'}">style="color: red;"</c:if>>
                                        <td>${count.index+1}</td>
                                        <td>${recommendation.recommendedempId}</td>
                                        <td>${recommendation.recommendedempGpfNo}</td>
                                        <td>${recommendation.recommendedempname}</td>                                        
                                        <td>${recommendation.recommendedpost}</td>
                                        <td>

                                            <a href="editRecommendedEmployeeDetail.htm?recommendeddetailId=${recommendation.recommendeddetailId}" class="btn btn-default">
                                                <c:if test="${recommendation.dataintegrity eq 'Y'}">
                                                    <span class="glyphicon glyphicon-check btn btn-success"></span>    
                                                </c:if>                                                
                                                Detail
                                            </a>
                                        </td>
                                        <td>
                                            <c:if test="${recommendationDetailBeanObj.taskId eq 0 && recommendation.isSubmittedToDept ne 'Y'}">
                                                <input type="button" value="Remove" onclick="removeEmployee(${recommendation.recommendeddetailId}, this)" class="btn btn-danger"/>
                                            </c:if>
                                            <c:if test="${recommendation.isSubmittedToDept eq 'Y'}">
                                                ${recommendation.departmentName}
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">                    
                        <div class="btn-group">
                            <input type="submit" name="action" value="Back"/>                    
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
