<%-- 
    Document   : DownloadSuperAnnuationReportDDOWiseInExcel
    Created on : Jun 17, 2022, 4:28:35 PM
    Author     : Madhusmita
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
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">

        </script>   
    </head>

    <body>
        <div id="wrapper">
            <jsp:include page="../../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-lg-12">
                                    <h2 align="center">
                                        Superannuation Report 
                                    </h2>
                                </div>
                            </div>
                            <form:form action="downloadSuperannuatonReport.htm" commandName="command">
                                <div class="row">
                                    <div class="col-lg-3">
                                        <div class="col-lg-3">
                                            <form:select path="sltMonth" class="form-control">
                                                <form:option value="">--Select Month--</form:option>
                                                <form:option value="1">JANUARY</form:option>
                                                <form:option value="2">FEBRUARY</form:option>
                                                <form:option value="3">MARCH</form:option>
                                                <form:option value="4">APRIL</form:option>
                                                <form:option value="5">MAY</form:option>
                                                <form:option value="6">JUNE</form:option>
                                                <form:option value="7">JULY</form:option>
                                                <form:option value="8">AUGUST</form:option>
                                                <form:option value="9">SEPTEMBER</form:option>
                                                <form:option value="10">OCTOBER</form:option>
                                                <form:option value="11">NOVEMBER</form:option>
                                                <form:option value="12">DECEMBER</form:option>
                                            </form:select>
                                        </div>
                                        <div class="col-lg-3">
                                            <form:select path="sltYear" class="form-control">
                                                <form:option value="">--Select Year--</form:option>                                    
                                                <form:options itemLabel="label" itemValue="value" items="${yearList}"/>
                                            </form:select>
                                        </div>
                                    </div>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </body>
</html>
