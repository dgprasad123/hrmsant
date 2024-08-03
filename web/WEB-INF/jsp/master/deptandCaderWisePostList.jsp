<%-- 
    Document   : deptandCaderWisePostList
    Created on : 17 Feb, 2023, 3:00:59 PM
    Author     : Manisha
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

            $(document).ready(function() {
                $("#deptName").change(function() {
                    $('#cadrecode').empty();
                    var url = 'getCadreListDeptWiseJSON.htm?deptCode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#cadrecode').append($('<option>', {
                                value: field.cadreCode,
                                text: field.cadreName
                            }));

                        });
                    });
                });
            });

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
                                    <i class="fa fa-file"></i> Post List 
                                </li>                            
                            </ol>
                        </div>
                    </div>

                    <form:form action="deptandcadreWisePostList.htm" commandName="cadre" method="post">
                        <div class="row">
                            <label class="control-label col-sm-2"><div class="col-lg-2">Department Name</div></label>
                            <div class="col-lg-8">
                                <form:select class="form-control" path="deptCode" id="deptName">
                                    <form:option value="">Select</form:option>
                                    <c:forEach items="${departmentList}" var="department">
                                        <form:option value="${department.deptCode}">${department.deptName}</form:option>
                                    </c:forEach>                                        
                                </form:select>                            
                            </div>
                        </div>
                        <div class="row">
                            <div> <label class="control-label col-sm-2">Cadre Name: </label></div>                               
                            <div class="col-lg-8">
                                <form:select class="form-control" path="cadreCode" id="cadrecode">
                                    <form:option value="">Select</form:option>                                            
                                </form:select>
                            </div>
                        </div>
                        <div class="col-lg-2" id="search-bttn">
                            <button type="submit" class="form-control btn-primary">Search</button> 
                        </div>
                    </form:form>
                </div>
                <div class="row">
                    <div class="col-lg-12">
                        <h3 class="heading-color" style="text-align: center">Post List</h3>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Sl No</th>
                                        <th>post Code</th>
                                        <th>post Name</th>
                                    </tr>
                                </thead>
                                <tbody>                                        
                                    <c:forEach items="${postlist}" var="post" varStatus="count">
                                        <tr>
                                            <td>${count.index + 1}</td>
                                            <td>${post.postCode}</td>
                                            <td>${post.postGrp}</td>
                                        </tr>
                                    </c:forEach>                                     
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>


