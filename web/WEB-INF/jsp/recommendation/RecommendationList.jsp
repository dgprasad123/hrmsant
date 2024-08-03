<%-- 
    Document   : ParCPromotionReport
    Created on : May 28, 2020, 12:53:17 PM
    Author     : manisha
--%>


<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#deptName").change(function() {
                    $('#offcode').empty();
                    var url = 'getOfficeListJSON.htm?deptcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $.each(result, function(i, field) {
                            $('#offcode').append($('<option>', {
                                value: field.offCode,
                                text: field.offName
                            }));
                        });
                    });
                });

                $("#offcode").change(function() {
                    var url = 'getAllSPCWithEmployee.htm?offcode=' + this.value;
                    $.getJSON(url, function(result) {
                        $('#postCode').empty();
                        $.each(result, function(i, field) {
                            $('#postCode').append($('<option>', {
                                value: field.spc,
                                text: field.postname + ", (" + field.empname + ")"
                            }));
                        });
                    });
                });
            });
            function openaddGroupCWindow() {
                $('#empDetail').modal('show');
            }
            function openAuthorityListWindow() {
                $('#setAuthority').modal('show');

            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Recommendation
                </div>
                <div class="panel-body">
                    <table class="table table-bordered">
                        <thead>
                            <tr>                                                                
                                <th width="3%">#</th>
                                <th width="8%">Created On Date</th>
                                <th width="8%">Submitted On Date</th>
                                <th width="8%">Recommendation Type</th>
                                <th width="20%">Pending At</th>
                                <th width="10%">Status</th>
                                <th width="20%">Action</th>
                                <th width="20%">Submit</th>
                            </tr>                            
                        </thead>
                        <tbody>
                            <c:forEach items="${recommendationList}" var="recommendationBean" varStatus="count">
                                <tr>
                                    <td>${count.index+1}</td>
                                    <td>${recommendationBean.createdondate}</td>
                                    <td>&nbsp;</td>
                                    <td>${recommendationBean.recommenadationType}</td>
                                    <td>&nbsp;</td>
                                    <td>&nbsp;</td>
                                    <td>
                                        <a href="viewRecommendedEmployeeList.htm?recommendationId=${recommendationBean.recommendationId}" class="btn-default"><button type="button" class="btn btn-default">Detail</button></a>
                                        <a href="showGroupCEmployee.htm?recommendationId=${recommendationBean.recommendationId}" class="btn-default"><button type="button" class="btn btn-default">Add Employee</button></a>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-primary" onclick="openAuthorityListWindow()">Submit</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">                    
                    <div class="btn-group">
                        <button type="button" class="btn btn-primary" onclick="openaddGroupCWindow()">Add New</button>
                    </div>
                </div>
            </div>
        </div>


        <%-- modal for Assign New Recommendation--%>
        <div id="setAuthority" class="modal fade" role="dialog">
            <div class="modal-dialog  modal-lg">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Choose Office Name and department</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">

                            <div class="form-group">
                                <label class="control-label col-sm-2">Department Name: </label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="deptName" id="deptName">
                                        <option value="">Select</option>
                                        <c:forEach items="${departmentList}" var="department">
                                            <option value="${department.deptCode}">${department.deptName}</option>
                                        </c:forEach>                                        
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Office Name</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="offcode" id="offcode">
                                        <option value="">Select</option>                                            
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-2">Substantive Post:</label>
                                <div class="col-sm-10">
                                    <select class="form-control" name="postCode" id="postCode">
                                        <option value="">Select</option>                                            
                                    </select>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary">Submit</button>
                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
