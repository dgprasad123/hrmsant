<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<script type='text/javascript'>
    function delete_data(ids,month,year){
     var con=confirm("Do you want to delete this information");
     
     if(con){
         window.location="deleteVacantPost.htm?taskId="+ids+"&month="+month+"&year="+year;
         
     }
    }
</script>    
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
            <jsp:include page="../tab/deptadminmenu.jsp"/>        
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
                                    <i class="fa fa-file"></i> Department Vacant Post List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="newDeptvacantpost.htm">New Department Vacant Post</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                    <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
                    <jsp:useBean id="date" class="java.util.Date" />
                    <fmt:formatDate value="${date}" pattern="yyyy" var="currentYear" />
                    <fmt:formatDate value="${date}" pattern="MMMM" var="currentMonth" />
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Department Vacant Post List</h2>
                             
                            <div class="table-responsive">
                              
                                <table id="account" class="table table-bordered table-striped" >
                                    <thead>
                                         <tr style='background-color:black;color:white;font-size:14px'>
                                             <th colspan="4">&nbsp;</th>
                                            <th colspan="3">Base Level Post</th>
                                            <th colspan="3">Promotional Posts</th>
                                           
                                           
                                            <th>&nbsp;</th>
                                        </tr>
                                        <tr style='background-color:black;color:white;font-size:14px'>
                                            <th>Month</th>
                                            <th>Year</th>
                                            <th>Group</th>
                                          
                                            <th>Post</th>
                                            <th>Sanctioned Strength</th>
                                            <th>Men in Position</th>
                                            <th>Vacancy</th>
                                            <th>Sanctioned Strength</th>
                                            <th>Men in Position</th>
                                            <th>Vacancy</th>                                        
                                            <th>&nbsp;</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${vacantPostList}" var="vacantPost">
                                            <tr scope="row">
                                                 <td>${vacantPost.month}</td>
                                                  <td>${vacantPost.year}</td>
                                                <td>${vacantPost.groupName}</td>                                                
                                               
                                                <td>${vacantPost.postId}</td>
                                                
                                                <td>${vacantPost.sancPost}</td>
                                                <td>${vacantPost.maninPost}</td>
                                                <td>${vacantPost.vacancy}</td>				
                                                <td>${vacantPost.sancPostPro}</td>
                                                <td>${vacantPost.maninPostPro}</td>
                                              
                                                  <td>${vacantPost.vacancyPro}</td>
                                                  <c:if test = "${not empty vacantPost.month && vacantPost.month==currentMonth}">
                                                <td nowrap>
                                                    <a href='editvacantpost.htm?Taskid=${vacantPost.vacantPostId}&month=${vacantPost.month}&year=${vacantPost.year}'><img src="images/action.png" alt="Saction Order" height="20px" width="20px" /></a>
                                                     <a href="#" onclick="delete_data(${vacantPost.vacantPostId},'${vacantPost.month}',${vacantPost.year})"><img src="images/delete_icon.png" alt="Saction Order" height="20px" width="20px" /></a>
                                                </td>
                                                
                                                </c:if>
                                                <c:if test = "${not empty vacantPost.month && vacantPost.month!=currentMonth}">
                                                <td nowrap>&nbsp;                                                  
                                                </td>
                                                
                                                </c:if>
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