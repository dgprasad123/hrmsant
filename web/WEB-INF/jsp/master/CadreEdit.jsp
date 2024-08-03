<%-- 
    Document   : cadre
    Created on : Apr 26, 2022, 3:22:16 PM
    Author     : Devikrushna
--%>

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
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">              
            function saveCheck(){
                if ($('#cadreName').val() == "")
                {
                    alert("Please enter Cadre Name");
                    $('#cadreName').focus();
                    return false;
                }   
            }
            
        </script>
        <style>
            .row-margin{
                margin-bottom: 20px;
                margin-top: 30px;
            }
            .first-row{
                margin-top: 20px;
            }
            .heading-color{
                color: #337ab7;
            }
        </style>
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
                            </ol>
                        </div>
                    </div>
                    <div class="container-fluid">                      
                        <form:form action="saveCadre.htm" commandName="cadre" method="post">
                            <form:hidden class="form-control" path="hidcadreCode" id="hidcadreCode"/>
                            <form:hidden class="form-control" path="hiddeptCode" id="hiddeptCode"/>
                            <div class="row-margin first-row">
                                <c:if test="${empty cadre.hidcadreCode}">
                                    <h3 class="heading-color">Add New Cadre</h3>
                                </c:if>
                                <c:if test="${not empty cadre.hidcadreCode}">
                                    <h3 class="heading-color">Update Cadre</h3>
                                </c:if>
                            </div>
                            <c:if test="${not empty cadre.hidcadreCode}">
                                <div class="row row-margin first-row">
                                    <div class="col-lg-2">
                                        <label for="note">Cadre Code:</label>
                                    </div>
                                    <div class="col-sm-6">
                                        <form:input cssClass="form-control" path="cadreCode" id="CadreCode" readonly="true" />
                                    </div>
                                </div> 
                            </c:if>         
                            <div class="row row-margin">
                                <div class="col-lg-2">
                                    <label for="note">Cadre Name: <span style="color: red">*</span></label></label>
                                </div>
                                <div class="col-sm-6">
                                    <form:input cssClass="form-control" path="cadreName" id="cadreName" placeholder="Enter Cadre Name" />
                                </div>
                            </div>                           
                                <div class="row row-margin">
                                    <div class="col-lg-2">
                                        <label for="note">Department Name</label>
                                    </div>
                                    <div class="col-sm-6">
                                        <form:select path="deptCode" class="form-control" disabled="true">
                                            <form:option value="">Select Department</form:option>
                                            <form:options items="${departmentList}" itemValue="deptCode" itemLabel="deptName" />                                
                                        </form:select> 
                                    </div>
                                </div>                             
                            <div class="form-group col-sm-12" style="margin-top:20px">
                                <label class="control-label col-sm-1"></label>
                                <div class="text-center col-sm-12" >     
                                    <button type="submit" class="btn btn-primary" name="action" value="Save Cadre" style="width: 10%;" onclick="return saveCheck();">Save</button> 
                                    <a href="cadreList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>
                                </div>
                            </div>   
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>


