<%-- 
    Document   : ModuleList
    Created on : Nov 21, 2016, 6:08:30 PM
    Author     : Manas Jena
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
            function getPostList(deptCode) {

                self.location = 'ViewDuplicatePosts.htm?deptCode=' + deptCode
            }
        </script>
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
                                    <i class="fa fa-file"></i> Privilege List 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <form role="form" class="form-horizontal" name="frmPost" id="frmPost">
                        <div class="row">
                            <div class="col-lg-12">
                                <h2>Duplicate Posts</h2>
                                <div class="form-group">
                                    <label class="control-label col-sm-1">Department</label>
                                    <div class="col-sm-8">
                                        <select class="form-control"  name="deptcode" id="deptCode" onchange="getPostList(this.value)" required="1">
                                            <option value=''>Select Department</option>
                                            <c:forEach items="${deptList}" var="dept" >
                                                <option value="${dept.deptCode}"<c:if test="${dept.deptCode eq deptCode}"> selected="selected"</c:if>>${dept.deptName}</option>
                                            </c:forEach>                                        
                                        </select>   
                                    </div>
                                </div>
                                <div style="position:fixed;top:170px;right:30px;text-align:right;">
                                    <input type="button" value="&laquo; Back to Merge Page" 
                                           class="btn btn-md btn-danger" onclick="javascript: self.location = 'DuplicatePostCorrection.htm?deptCode=' + $('#deptCode').val()" />                                    
                                </div>                                
                                <div class="panel-footer">
                                    <div class="table-responsive">
                                        <div class="table-responsive">
                                            <table class="table table-bordered table-hover">
                                                <thead>
                                                    <tr>
                                                        <th width="4%">Sl#</th>
                                                        <th>Post</th>
                                                        <th width="15%">No. of Employees</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${postList}" var="postList">
                                                        <tr style="font-weight:bold;color:#FFFFFF;background:#0374B4;">
                                                            <td colspan="5">Section ${postList.mergeGroup}</td>
                                                        </tr>
                                                    <c:forEach items="${postList.dataList}" var="dataList" varStatus="count">
                                                        <tr id="tr_${dataList.postcode}" style="background:<c:if test="${dataList.isCorrect eq 'Y'}">#FFE8C4</c:if><c:if test="${dataList.isCorrect eq 'N'}">#FFFFFF</c:if>">
                                                            <td>${count.index+1}</td>
                                                            <td>${dataList.post}</td>
                                                            <td >${dataList.numEmployees}</td>
                                                        </tr>
                                                    </c:forEach>
                                                        </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>


                                </div>
                            </div>

                        </div>
                    </form>
                </div>
            </div>



            <!-- Modal -->
            <div id="myModal" class="modal fade" role="dialog">
                <div class="modal-dialog">


                </div>
            </div>
    </body>
</html>
