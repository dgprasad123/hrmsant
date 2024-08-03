<%-- 
    Document   : OfficeList
    Created on : Nov 18, 2017, 1:02:16 PM
    Author     : manisha
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
            function addCheck() {
                if ($('#dept').val() == "")
                {
                    alert("Please select Department and Search Cadre List");
                    $('#dept').focus();
                    return false;
                }
            }
            function getCadreList() {
                var url = 'getCadreListJSON.htm?deptcode=' + $("#deptCode").val();
                var valText = $("#deptCode option:selected").html();
                $("#hiddenDeptName").val(valText);
                +
                        $.getJSON(url, function(data) {
                            $('#cadreCode').empty();

                            $('#cadreCode').append("<option value=\"\">--Select--</option>");

                            // $('#cadreCode').append($('<option>').text('Select Office').attr('value', ''));
                            $.each(data, function(i, obj) {
                                $('#cadreCode').append($('<option>').text(obj.label).attr('value', obj.value));
                            });
                        }).done(function() {

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
                                    <i class="fa fa-file"></i> Cadrewise Grade List 
                                </li>                            
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <form:form action="CadreWiseGrade.htm" commandName="grade" method="post">
                            <div class="form-group"><label class="control-label col-sm-2">Department Name: </label></div>
                            <div class="col-lg-8">
                                <form:select path="deptCode" class="form-control" onchange="getCadreList()" id="deptCode">
                                    <form:option value="">Select Department</form:option>
                                    <form:options items="${departmentList}" itemValue="deptCode" itemLabel="deptName"/>                                
                                </form:select>                            
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group"><label class="control-label col-sm-2">Cadre Name: </label></div>                               
                            <div class="col-lg-8">
                                <form:select class="form-control" path="cadreCode" id="cadreCode">
                                    <form:option value="">Select</form:option>
                                    <form:options items="${cadrelist}" itemLabel="label" itemValue="value"/>
                                </form:select>
                            </div>
                        </div>
                        <div class="row" >
                            <div  class="col-lg-2"> </div>
                            <div class="col-lg-8" align="center"><input type="submit" id="search-btn" name="action" value="search" class="form-control" style="width:200px; margin-top: 10px;"/></div>
                            <div  class="col-lg-2"></div>
                        </div>
                    </form:form>                    
                    <div class="text-center" id="add-new">
                        <!--<a href="getGradeDetail.htm"><button type="button" class="btn btn-info" onclick="return addCheck()">Add New Grade</button>  </a>
                        <a href="cadreList.htm"><button type="button" class="btn btn-warning btn-md">&laquo;Back to List Page</button></a>-->  
                        <a href="getGradeDetail.htm?deptCode=${grade.deptCode}&cadreCode=${grade.cadreCode}">
                            <button type="button" class="btn btn-primary" style=" margin-top: 10px;">Add New Grade</button></a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-12">
                        <h3 class="heading-color">Cadrewise Grade List</h3>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Sl No</th>                                           
                                        <th>Grade Name</th>
                                        <th>Grade Level</th>
                                        <th>No of Sanction Post</th> 
                                        <th>Edit</th>
                                    </tr>
                                </thead>
                                <tbody>

                                    <c:if test="${empty gradelist}">
                                        <div class="text-center" style="margin: 30px;">
                                            <h4 style="color: #071c59ab;font-size: 20px;">Data not available, Add as required.</h4>
                                        </div>
                                    </c:if>

                                <c:if test="${not empty gradelist}">
                                    <c:forEach items="${gradelist}" var="gradeList" varStatus="count">
                                        <tr>
                                            <td>${count.index + 1}</td>
                                            <td>${gradeList.grade}</td>
                                            <td>${gradeList.gradeLevel}</td> 
                                            <td>${gradeList.sanction}</td> 
                                            <td>
                                                <a href="updateGrade.htm?cadreGradeCode=${gradeList.cadreGradeCode}&deptCode=${grade.deptCode}&cadreCode=${grade.cadreCode}">
                                                    <button type="submit" name="action" value="Edit" class="btn btn-primary">Edit<i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach> 
                                </c:if>                                
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


