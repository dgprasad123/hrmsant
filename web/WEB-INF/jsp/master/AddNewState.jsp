<%-- 
    Document   : AddNewState
    Created on : 5 May, 2022, 12:09:40 PM
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
                
                if ($('#countryName').val() == "")
                {
                    alert("Please enter Country Name");
                    $('#countryName').focus();
                    return false;
                }
                if ($('#statename').val() == "")
                {
                    alert("Please enter State Name");
                    $('#statename').focus();
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
                        <form:form action="saveState.htm" commandName="state" method="post">
                            <form:hidden class="form-control" path="hidstateCode" id="hidstateCode"/>
                            <div class="row-margin first-row">
                                <c:if test="${empty state.hidstateCode}">
                                    <h3 class="heading-color">Add New State</h3>
                                </c:if>
                                <c:if test="${not empty state.hidstateCode}">
                                    <h3 class="heading-color">Update State</h3>
                                </c:if>
                            </div>
                                <div class="row row-margin first-row">
                                    <div class="col-lg-2">
                                        <label for="note">Country Name: <span style="color: red">*</span></label>
                                    </div>
                                    <div class="col-sm-6">
                                        <form:input cssClass="form-control" path="countryName" id="countryName" placeholder="Enter Country Name"  />
                                    </div>
                                </div> 
                                <c:if test="${not empty state.hidstateCode}">
                                <div class="row row-margin first-row">
                                    <div class="col-lg-2">
                                        <label for="note">State Code:</label>
                                    </div>
                                    <div class="col-sm-6">
                                        <form:input cssClass="form-control" path="statecode" id="statecode" readonly="true" />
                                    </div>
                                </div> 
                                </c:if>
                            <div class="row row-margin">
                                <div class="col-lg-2">
                                    <label for="note">State Name: <span style="color: red">*</span></label></label>
                                </div>
                                <div class="col-sm-6">
                                    <form:input cssClass="form-control" path="statename" id="statename" placeholder="Enter State Name" />
                                </div>
                            </div>  
                            
                           
                            <div class="form-group col-sm-12" style="margin-top:20px">
                                <label class="control-label col-sm-1"></label>
                                <div class="text-center col-sm-12" >     
                                    <button type="submit" class="btn btn-primary" name="action" value="Save State" style="width: 10%;" onclick="return saveCheck();">Save</button> 
                                    <a href="StateList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>
                                </div>
                            </div>   
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
