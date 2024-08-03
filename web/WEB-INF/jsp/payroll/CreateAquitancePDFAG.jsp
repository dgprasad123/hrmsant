<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
            <%--<jsp:include page="../tab/ServiceConditionAdminMenu.jsp"/>--%>
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">
                            <h1 class="page-header">
                                Blank Page
                                <small>Subheading</small>
                            </h1>
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="#">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Create PDF File
                                </li>
                            </ol>
                        </div>
                    </div>
                    <!-- /.row -->
                    <div class="row">
                        <form:form class="form-inline" action="createLTAAquitancePDFforAG.htm" method="POST" commandName="billBrowserForm" >
                            <div class="form-group">
                                <label for="sltTrCode">Choose Treasury:</label>
                                <form:select path="sltTrCode" class="form-control">
                                    <form:option value="">--Select--</form:option>
                                    <form:options items="${trlist}" itemLabel="treasuryName" itemValue="agtreasuryCode"/>
                                </form:select>    
                            </div>
                            <div class="form-group">
                                <label for="sltYear">Choose Year:</label>
                                <form:select path="sltYear" class="form-control">
                                    <form:option value="-1">--Select--</form:option>
                                    <form:options items="${yearlist}" itemLabel="label" itemValue="value"/>
                                </form:select>    
                            </div>
                            <div class="form-group">
                                <label for="sltMonth">Choose Month:</label>
                                <form:select path="sltMonth" class="form-control">
                                    <form:option value="0">January</form:option>
                                    <form:option value="1">February</form:option>
                                    <form:option value="2">March</form:option>
                                    <form:option value="3">April</form:option>
                                    <form:option value="4">May</form:option>
                                    <form:option value="5">June</form:option>
                                    <form:option value="6">July</form:option>
                                    <form:option value="7">August</form:option>
                                    <form:option value="8">September</form:option>
                                    <form:option value="9">October</form:option>
                                    <form:option value="10">November</form:option>
                                    <form:option value="11">December</form:option>
                                </form:select>
                            </div>                           
                            <input type="submit" name="btnSubmit" value="Download" class="btn btn-default"/>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
