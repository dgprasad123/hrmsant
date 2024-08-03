<%-- 
    Document   : DuplicateSubstantivePostList
    Created on : Feb 14, 2022, 1:12:35 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

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

            function duplicateSpcDetails() {
                var curSpc = $("#spc").val();
                var url = "duplicateSpcDetails.htm?curSpc=" + curSpc;

                window.location = url;

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
                                    <i class="fa fa-file"></i> Duplicate Post List 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <form:form action="substantivePostDetails.htm" commandName="substantivePostDetails"  method="post">
                        <input type="hidden" name='hidOffCode' value='' id='hidOffCode'/>

                        <div class="row">
                            <div class="col-lg-12">
                                <h3>Duplicate Post List</h3>
                                </br>

                                <div class="form-group">
                                    <label>Department:</label> 
                                    ${DeptName}
                                </div>
                                <div class="form-group">
                                    <label>Office:</label>
                                    ${officeName}
                                </div>
                            </div>
                        </div>

                        <div class="row" style="margin-top: 10px;">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl</th>
                                            <th>Substantive Post Code</th>
                                            <th>Substantive Post Name</th>  
                                            <th>No of Employees Mapped</th>
                                        </tr>
                                    </thead>
                                    <tbody id="dupspclist">
                                        <c:if test="${not empty dupSPCList}">
                                            <c:set var="slno" value="${0}" />
                                            <c:forEach items="${dupSPCList}" var="duplist">
                                                <c:set var="slno" value="${slno +1}" />
                                                <tr>
                                                    <td>${slno} </td>
                                                    <td>${duplist.spc} </td>
                                                    <td>${duplist.spn}</td>
                                                    <td><a href="duplicateSpcDetails.htm?curSpc=${duplist.spc}&offCode=${officeCode}" style="display:block;background: red;color:#FFFFFF;font-size:10pt;width:50%;text-align: center;font-weight:bold;" target="_blank" >${duplist.cntDuplicateSpc}</a></td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${empty dupSPCList}">
                                            <tr>
                                                <td colspan="4" align="center" style="color:red;font-size: 20px;">
                                                    DATA NOT AVAILABLE
                                                </td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div> 
                    </form:form>
                </div>
            </div>
        </div>

        <!-- Modal -->
        <div id="myModal" class="modal fade" role="dialog">
            <div class="modal-dialog">

            </div>
        </div>
    </body>
</html>