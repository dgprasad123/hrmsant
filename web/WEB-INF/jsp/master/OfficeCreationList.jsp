<%-- 
    Document   : OfficeCreationList
    Created on : Nov 17, 2021, 1:02:37 PM
    Author     : Madhusmita
--%>

<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript">
            var ofcType = "B";
        </script>
    </head>
    <body>
        <div id="wrapper">
             <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>                                
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="newofficecreation.htm">New Office</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="editOfficeDetails.htm">Rename Office</a>

                                </li>
                                <li class="active">
                                    <%--<i class="fa fa-file"></i> <a href="newofficecreation.htm?&hidOfcType=B">Backlog Office</a>--%>
                                    <i class="fa fa-file"></i> <a href="backlogOfficeCreation.htm">Backlog Office</a>

                                </li>
                            </ol>
                        </div>
                    </div> 
                    <div class="row">
                        <form:form action="getOfficeCreationRenameBackLog.htm" commandName="Office" method="post">
                            
                            <div class="col-lg-8">
                                <div class="row" style="align:left;">                                        
                                        <h2 style="color: blue;size: 100px;text-align:center">${msgOfcCreated}</h2>
                                    </div>                           
                            </div>                            
                        </form:form>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
