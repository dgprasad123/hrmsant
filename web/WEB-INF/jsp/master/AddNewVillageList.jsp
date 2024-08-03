<%-- 
    Document   : AddNewVillageList
    Created on : 9 May, 2022, 11:54:22 AM
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
                if ($('#villageName').val() == "")
                {
                    alert("Please Enter Village Name");
                    $('#villageName').focus();
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
                        <form:form action="saveVillage.htm" commandName="village" method="post">
                            <form:hidden class="form-control" path="hidvilCode" id="hidvilCode"/>
                            <form:hidden class="form-control" path="hidstateCode" id="hidstateCode"/>
                            <form:hidden class="form-control" path="hiddistCode" id="hiddistCode"/>
                            <form:hidden class="form-control" path="hidpsCode" id="hidpsCode"/>
                            
                            <div class="row-margin first-row">
                                <c:if test="${empty village.hidvilCode}">
                                    <h3 class="heading-color">Add New Village</h3>
                                </c:if>
                                <c:if test="${not empty village.hidvilCode}">
                                    <h3 class="heading-color">Update Village</h3>
                                </c:if>
                            </div>                          
                                <div class="row row-margin">
                                    <div class="col-lg-2">
                                        <label for="note">Select State</label>
                                    </div>
                                    <div class="col-sm-6">
                                        <form:select path="stateCode" id="stateCode" class="form-control" disabled="true">
                                            <form:option value="">Select State</form:option>
                                            <form:options items="${stateList}" itemValue="statecode" itemLabel="statename"/>                                
                                        </form:select> 
                                    </div>
                                </div>                                                                         
                                <div class="row row-margin">
                                    <div class="col-lg-2">
                                        <label for="note">Select District</label>
                                    </div>
                                    <div class="col-sm-6">
                                        <form:select path="distCode" class="form-control" disabled="true">
                                            <form:option value="">Select District</form:option>
                                            <form:options items="${districtList}"  itemValue="distCode" itemLabel="distName"/>                                
                                        </form:select>  
                                    </div>
                                </div>                      
                                <div class="row row-margin">
                                    <div class="col-lg-2">
                                        <label for="note">Select Police Station</label>
                                    </div>
                                    <div class="col-sm-6">
                                        <form:select path="psCode" id="psCode" class="form-control" disabled="true">
                                            <form:option value="">Select Police Station</form:option>
                                            <form:options items="${poStList}" itemValue="psCode" itemLabel="psName"/>                                
                                        </form:select>   
                                    </div>
                                </div>                                                     
                                <c:if test="${not empty village.hidvilCode}">
                                <div class="row row-margin first-row">
                                    <div class="col-lg-2">
                                        <label for="note">Village Code:</label>
                                    </div>
                                    <div class="col-sm-6">
                                        <form:input cssClass="form-control" path="villageCode" id="villageCode" readonly="true" />
                                    </div>
                                </div> 
                                </c:if>
                            <div class="row row-margin">
                                <div class="col-lg-2">
                                    <label for="note">Village Name: <span style="color: red">*</span></label></label>
                                </div>
                                <div class="col-sm-6">
                                    <form:input cssClass="form-control" path="villageName" id="villageName" placeholder="Enter Village Name" />
                                </div>
                            </div>                                
                            <div class="form-group col-sm-12" style="margin-top:20px">
                                <label class="control-label col-sm-1"></label>
                                <div class="text-center col-sm-12" >     
                                    <button type="submit" class="btn btn-primary" name="action" value="Save Village" style="width: 10%;" onclick="return saveCheck();">Save</button> 
                                    <a href="VillageList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>
                                </div>
                            </div>   
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
