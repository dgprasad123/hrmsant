<%-- 
    Document   : DistrictList
    Created on : 5 May, 2022, 1:25:22 PM
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
            function addCheck(){
                if ($('#state').val() == "")
                {
                    alert("Please select State and Search District List");
                    $('#state').focus();
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
                margin-bottom: 30px;
            }
            .th-bg{
                background-color: oldlace;
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
                                <li class="active">
                                    <i class="fa fa-file"></i> District List 
                                </li>                            
                            </ol>
                        </div>
                    </div>
                    
                    <div class="row">
                        <form:form action="DistrictList.htm" commandName="district" method="post">
                            
                      <div class="col-lg-2">Select State</div>
                        <div class="col-lg-8">
                            <form:select path="stateCode" id="state" class="form-control">
                                <form:option value="">Select State</form:option>
                                <form:options items="${stateList}" itemValue="statecode" itemLabel="statename"/>                                
                            </form:select>                            
                        </div>
                        <div class="col-lg-2" id="search-bttn"><button type="submit" class="btn btn-info">Search</button> </div>   
                        </div>   
                            
                            
                        <div class="row">  
                        <div class="col-lg-12">
                            <h3 class="heading-color">District List</h3>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th class="th-bg">Sl No</th>
                                            <th class="th-bg">District Code</th>
                                            <th class="th-bg">District Name</th>
                                            <th class="th-bg">Action</th>                                           
                                        </tr>
                                    </thead>
                                    <tbody>                                          
                                        <c:forEach items="${districtList}" var="dist" varStatus="count">
                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td>${dist.distCode}</td>
                                                <td>${dist.distName}</td>
                                                <td><a href="getDistrictDetail.htm?distCode=${dist.distCode}">Edit</a></td>                                    
                                            </tr>
                                        </c:forEach>                                     
                                    </tbody>
                                </table>
                            </div>
                           
                            <div class="panel-footer" id="add-new">
                                <a href="getDistrictDetail.htm?stateCode=${district.stateCode}"><button type="button" class="btn btn-info" onclick="return addCheck()">Add New District</button>  </a>
                                <a href="DistrictList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>    
                            </div>
                          
                        </div>
                    </div>
                   </form:form>
                </div>
            </div>
        </div>
    </body>
</html>

