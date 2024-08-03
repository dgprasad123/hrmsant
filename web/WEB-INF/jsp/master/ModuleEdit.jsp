<%-- 
    Document   : ModuleEdit
    Created on : Nov 21, 2016, 6:41:25 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
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
                                    <i class="fa fa-file"></i> Module List
                                </li>
                            </ol>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Module Detail</h2>
                            <form:form role="form" action="saveModule.htm"  method="post">
                                <div class="form-group">
                                    <label>Module Name</label>
                                    <form:hidden path="modid"/>      
                                    <form:input path="modname" cssClass="form-control" placeholder="Enter Module Name" autocomplete="off"/>                                    
                                </div>

                                <div class="form-group">
                                    <label>Module URL</label>
                                    <form:input path="modurl" cssClass="form-control" placeholder="Enter url" autocomplete="off"/>                                    
                                </div>
                                <div class="form-group">
                                    <label>Employee Specific</label>
                                    <form:input path="empspecific" cssClass="form-control" autocomplete="off"/>                                    
                                </div>
                                <div class="form-group">
                                    <label>Convert URL</label>
                                    <form:input path="converturl" cssClass="form-control" autocomplete="off"/>                                    
                                </div>
                                <div class="form-group">
                                    <label>Module Serial No.</label>
                                    <form:input path="modserial" cssClass="form-control" autocomplete="off"/>                                    
                                </div>
                                <div class="form-group">
                                    <label>Open In New Window</label>
                                    <form:input path="newwindow" cssClass="form-control" autocomplete="off"/>                                    
                                </div>
                                <div class="form-group">
                                    <label>Help URL</label>
                                    <form:input path="helpurl" cssClass="form-control" autocomplete="off"/>                                    
                                </div>
                                <div class="form-group">
                                    <label>Module Type</label>
                                    <form:input path="moduletype" cssClass="form-control" autocomplete="off"/>                                    
                                </div>
                                <div class="form-group">
                                    <label>Hide URL</label>
                                    <form:input path="hideurl" cssClass="form-control" autocomplete="off"/>                                    
                                </div>
                                <button type="submit" class="btn btn-default">Submit Button</button>
                                <button type="reset" class="btn btn-default">Reset Button</button>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
