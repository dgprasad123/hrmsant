<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";

%>
<script type='text/javascript'>
    function delete_data(ids){
     var con=confirm("Do you want to delete this information");
     if(con){
         window.location="deleteEmpStrengthData.htm?taskId="+ids;
         
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
                                    <i class="fa fa-file"></i>Block Level Extension Officer List 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="newBleo.htm">New Block Level Extention Officer</a>
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Block Level Extention Officer List</h2>
                            <div class="table-responsive">
                                <table id="account" class="table table-bordered table-hover table-striped">
                                    <thead style='background-color:black;color:white;font-size:14px'>
                                        <tr>
                                           
                                            <th>Group Name </th>
                                            <th>Name of the block level extension officers</th>
                                            <th>No. of Sanctioned strength</th>
                                            <th>No. of present vacancy</th>                  
                                            <th>Present Status on filling up the vacant posts</th>
                                            <th>&nbsp;</th>



                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${bleoList}" var="bleo">
                                            <tr>
                                                 <td>${bleo.groupName}</td>
                                                <td>${bleo.postName}</td>
                                               
                                                <td>${bleo.sanctionStrength}</td>
                                                <td>${bleo.previousVacancy}</td>
                                                <td>${bleo.previousStatus}</td>
                                                 <td><a href='editEmpStrength.htm?Taskid=${bleo.recruitEoId}'><img src="images/action.png" alt="Saction Order" height="20px" width="20px" /></a>
                                                  <a href="#" onclick="delete_data(${bleo.recruitEoId})"><img src="images/delete_icon.png" alt="Saction Order" height="20px" width="20px" /></a>
                                                 </td>
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