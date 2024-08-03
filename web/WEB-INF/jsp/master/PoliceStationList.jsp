<%-- 
    Document   : PoliceStationList
    Created on : 7 May, 2022, 12:05:11 PM
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
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap4.min.css"/>
        <script type="text/javascript"  src="js/jquery-1.12.4.js"></script>
        <script type="text/javascript"  src="js/jquery.dataTables.min.js"></script>
        <script type="text/javascript"  src="js/dataTables.bootstrap4.min.js"></script>
        
        
        <script type="text/javascript"> 
             $(document).ready(function() {
                $('#example').DataTable({
                    "order": [[0, "asc"]]
                });
            });
            
             function addCheck(){
                if ($('#stateCode').val() == "")
                {
                    alert("Please select State");
                    $('#stateCode').focus();
                    return false;
                }  
                if ($('#distCode').val() == "")
                {
                    alert("Please select District and Search Police Station List");
                    $('#distCode').focus();
                    return false;
                }  
            }
                
              function getDistList() {
                var url = 'getStateWiseDistrictList.htm?stateCode=' + $("#stateCode").val();
                var valText = $("#stateCode option:selected").html();
               
             
                $.getJSON(url, function (data) {
                    $('#distCode').empty();

                    $('#distCode').append($('<option>').text('Select District').attr('value', ''));
                    $.each(data, function (i, obj) {
                        $('#distCode').append($('<option>').text(obj.distName).attr('value', obj.distCode));
                    });
                });
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
                                    <i class="fa fa-file"></i> Police Station List 
                                </li>                            
                            </ol>
                        </div>
                    </div>
                    
                    <div class="row">
                        <form:form action="PoliceStationList.htm" commandName="pstation" method="post">
                            <div class="container">
                            <div class="row row-margin">     
                                <div class="col-lg-2">Select State</div>
                                <div class="col-lg-8">
                                    <form:select path="stateCode" id="stateCode" class="form-control" onchange="getDistList()">
                                        <form:option value="">Select State</form:option>
                                        <form:options items="${stateList}" itemValue="statecode" itemLabel="statename"/>                                
                                    </form:select>                            
                                </div>                        
                            </div>
                            <div class="row row-margin">
                                <div class="col-lg-2">Select District</div>
                                <div class="col-lg-8">
                                    <form:select path="distCode" id="distCode" class="form-control">
                                        <form:option value="">Select District</form:option>
                                        <form:options items="${districtList}" itemValue="distCode" itemLabel="distName"/>                                
                                    </form:select>                            
                                </div>
                                <div class="col-lg-2" id="search-bttn"><button type="submit" class="btn btn-info">Search</button> </div>  
                            </div>
                      </div>                            
                        <div class="col-lg-12">
                            <h3 class="heading-color">Police Station List </h3>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped" id="example">
                                    <thead>
                                        <tr>
                                            <th class="th-bg">Sl No</th>
                                            <th class="th-bg">Police Station Code</th>
                                            <th class="th-bg">Police Station Name</th>
                                            <th class="th-bg">Action</th>                                           
                                        </tr>
                                    </thead>
                                    <tbody>                                           
                                        <c:forEach items="${psList}" var="ps" varStatus="count">
                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td>${ps.psCode}</td>
                                                <td>${ps.psName}</td>
                                                <td><a href="getPsDetail.htm?psCode=${ps.psCode}">Edit</a></td>                                    
                                            </tr>
                                        </c:forEach>                                      
                                    </tbody>
                                </table>
                            </div>
                            
                            <div class="panel-footer" id="add-new">
                                <a href="getPsDetail.htm?stateCode=${pstation.stateCode}&distCode=${pstation.distCode}"><button type="button" class="btn btn-info" onclick="return addCheck()">Add New Police Station</button>  </a>
                                <a href="PoliceStationList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>    
                            </div>
                          
                        </div>
                    </div>
                   </form:form>
                </div>
            </div>
        </div>
    </body>
</html>
