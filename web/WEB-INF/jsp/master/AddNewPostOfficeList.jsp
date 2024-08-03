<%-- 
    Document   : AddNewPostOfficeList
    Created on : 7 May, 2022, 4:11:47 PM
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
                if ($('#postOfficeName').val() == "")
                {
                    alert("Please Enter Post Office Name");
                    $('#postOfficeName').focus();
                    return false;
                }
                if ($('#pinCode').val() == "")
                {
                    alert("Please Enter PIN Code");
                    $('#pinCode').focus();
                    return false;
                }
                
            }
            function numberOnly(id) {              
                var element = document.getElementById(id);             
                element.value = element.value.replace(/[^0-9]/gi, "");
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
                        <form:form action="savePostOffice.htm" commandName="postoff" method="post">
                            <form:hidden class="form-control" path="hidPoCode" id="hidPoCode"/>
                            <form:hidden class="form-control" path="hidstateCode" id="hidstateCode"/>
                            <form:hidden class="form-control" path="hiddistCode" id="hiddistCode"/>
                            <div class="row-margin first-row">
                                <c:if test="${empty postoff.hidPoCode}">
                                    <h3 class="heading-color">Add New Post Office</h3>
                                </c:if>
                                <c:if test="${not empty postoff.hidPoCode}">
                                    <h3 class="heading-color">Update Post Office</h3>
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
                                        <form:select path="distCode" class="form-control" disabled="true" >
                                            <form:option value="">Select District</form:option>
                                            <form:options items="${districtList}"  itemValue="distCode" itemLabel="distName"/>                                
                                        </form:select>  
                                    </div>
                                </div>    
                                <c:if test="${not empty postoff.hidPoCode}">
                                <div class="row row-margin first-row">
                                    <div class="col-lg-2">
                                        <label for="note">Post Office Code:</label>
                                    </div>
                                    <div class="col-sm-6">
                                        <form:input cssClass="form-control" path="postOfficeCode" id="postOfficeCode" readonly="true" />
                                    </div>
                                </div> 
                                </c:if>
                            <div class="row row-margin">
                                <div class="col-lg-2">
                                    <label for="note">Post Office Name: <span style="color: red">*</span></label></label>
                                </div>
                                <div class="col-sm-6">
                                    <form:input cssClass="form-control" path="postOfficeName" id="postOfficeName" placeholder="Enter Post Office Name" />
                                </div>
                            </div>  
                             <div class="row row-margin">
                                <div class="col-lg-2">
                                    <label for="note">PIN Code: <span style="color: red">*</span></label></label>
                                </div>
                                <div class="col-sm-6">
                                    <form:input cssClass="form-control" path="pinCode" id="pinCode" maxlength="6" oninput="numberOnly(this.id);" name="number"  placeholder="Enter PIN Code" />
                                </div>
                            </div>    
                            <div class="form-group col-sm-12" style="margin-top:20px">
                                <label class="control-label col-sm-1"></label>
                                <div class="text-center col-sm-12" >     
                                    <button type="submit" class="btn btn-primary" name="action" value="Save PO" style="width: 10%;" onclick="return saveCheck();">Save</button> 
                                    <a href="PostOfficeList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>
                                </div>
                            </div>   
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
