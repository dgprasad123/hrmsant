<%-- 
    Document   : AddNewDistrict
    Created on : 5 May, 2022, 1:25:53 PM
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
                if ($('#distName').val() == "")
                {
                    alert("Please enter District Name");
                    $('#distName').focus();
                    return false;
                }
                if ($('#activedist').val() == "")
                {
                    alert("Please Select District Active Status");
                    $('#activedist').focus();
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
                        <form:form action="saveDistrict.htm" commandName="district" method="post">
                            <form:hidden class="form-control" path="hiddistCode" id="hiddistCode"/>
                            <form:hidden class="form-control" path="hidstateCode" id="hidstateCode"/>
                            
                            <div class="row-margin first-row">
                                <c:if test="${empty district.hiddistCode}">
                                    <h3 class="heading-color">Add New District</h3>
                                </c:if>
                                <c:if test="${not empty district.hiddistCode}">
                                    <h3 class="heading-color">Update District</h3>
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
                                <c:if test="${not empty district.hiddistCode}">
                                <div class="row row-margin first-row">
                                    <div class="col-lg-2">
                                        <label for="note">District Code:</label>
                                    </div>
                                    <div class="col-sm-6">
                                        <form:input cssClass="form-control" path="distCode" id="distCode" readonly="true" />
                                    </div>
                                </div> 
                                </c:if>
                            <div class="row row-margin">
                                <div class="col-lg-2">
                                    <label for="note">District Name: <span style="color: red">*</span></label></label>
                                </div>
                                <div class="col-sm-6">
                                    <form:input cssClass="form-control" path="distName" id="distName" placeholder="Enter District Name" />
                                </div>
                            </div>  
                             <div class="row row-margin">
                               <label class="control-label col-sm-2" style="font-size: 15px;">Active District <span style="color: red">*</span></label>
                                      <div class="col-sm-6">                                       
                                            <form:select path="activedist" id="activedist" class="form-control">
                                                 <form:option value="Y"> Yes </form:option>
                                                <form:option value="N"> No </form:option>
                                            </form:select>
                                     </div>                                   
                               </div>   
   
                            <div class="form-group col-sm-12" style="margin-top:20px">
                                <label class="control-label col-sm-1"></label>
                                <div class="text-center col-sm-12" >     
                                    <button type="submit" class="btn btn-primary" name="action" value="Save District" style="width: 10%;" onclick="return saveCheck();">Save</button> 
                                    <a href="DistrictList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>
                                </div>
                            </div>   
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
