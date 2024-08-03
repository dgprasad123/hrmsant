<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/sb-admin.css" rel="stylesheet" type="text/css">
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/commissionmenu.jsp"/>                
            <div id="page-wrapper">

                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="#">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Selected Candidates Categories List                             </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="AddSelectedCandidateCategory.htm">Add New Categories List</a>
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Selected Candidates Categories List</h2>
                            <div class="table-responsive">
                                <table id="account" class="table table-bordered table-striped" >
                                    <thead>
                                        <tr style='background-color:black;color:white;font-size:14px'>

                                            <th>Department</th>
                                            <th>Group Name</th>
                                            <th>Cadre</th>
                                            <th>Post</th>
                                            <th>Order Number</th>
                                            <th>Order Date</th>
                                            <th>No of Selected Candidates</th>                                            
                                            <th>&nbsp;</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${cCategoryList}" var="cCategory">
                                            <tr scope="row">

                                                <td>${cCategory.departmentId}</td>                                                
                                                <td>${cCategory.groupName}</td>                                                
                                                <td>${cCategory.cadreId}</td>                                                
                                                <td>${cCategory.postId}</td>
                                                <td>${cCategory.orderNumber}</td>
                                                <td>${cCategory.orderDate}</td>
                                                <td>${cCategory.noOfSelectedCanddiates}</td>                                                
                                                <td><a href='addSelectedCandidates.htm?categoryId=${cCategory.categoryId}'><img src="images/user_plus_icon.png" alt="Saction Order" width="24px" title="Add Candidate List" /></a></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>	
                                </table>

                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </body>
</html>