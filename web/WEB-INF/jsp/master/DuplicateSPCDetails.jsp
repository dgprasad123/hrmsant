<%-- 
    Document   : DuplicateSPCDetails
    Created on : Feb 15, 2022, 3:28:37 PM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
            $(window).load(function() {
                $("#unmappedSpc").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);

                    $(this).find(".modal-body").load(link.attr("href"));

                });
            });

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
                                    <i class="fa fa-file"></i> Duplicate Substantive Post Details 
                                </li>                                
                            </ol>
                        </div>
                    </div>
                    <form:form action="substantivePostDetails.htm" commandName="substantivePost"  method="post">
                        <form:hidden id="hidOffCode" path="hidOffCode"/>
                        <form:hidden id="hidGpc" path="hidGpc"/>

                        <div class="row">
                            <div class="col-lg-6">
                                <h3>List of Employees Mapped In One Substantive Post </h3>                               
                                </br>                               
                            </div>
                            <div class="col-lg-6">
                                <a href="duplicateSPC.htm?officeCode=${officeCode}"><button type="button" class="btn btn-primary" style="width:100px">Back</button></a>                            
                                </br>                               
                            </div>
                        </div>                                           
                    </form:form>

                    <div class="row" style="margin-top: 10px;">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Sl</th>
                                        <th>HRMS ID</th>
                                        <th>GPF / PRAN</th>  
                                        <th>IS REGULAR</th>
                                        <th>DESIGNATION</th>
                                        <th>POST CODE</th>
                                        <th>EMPLOYEE NAME</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:if test="${not empty dupSPClistDetails}">
                                        <c:set var="slno" value="${0}" />   
                                        <c:forEach items="${dupSPClistDetails}" var="dupemplist">
                                            <c:set var="slno" value="${slno +1}" />
                                            <tr>
                                                <td>${slno} </td>
                                                <td>${dupemplist.empid} </td>
                                                <td>${dupemplist.gpfNo} </td>
                                                <td>${dupemplist.isRegular} </td>
                                                <td>${dupemplist.spn} </td>
                                                <td>${dupemplist.gpc} </td>
                                                <td>${dupemplist.empName} </td>
                                                <%--<td data-toggle="modal" data-target="#newBank"><input type='submit' name='Submit' value='Assign SPC' class="btn btn-danger" /></td>--%>
                                                <td>
                                                    <a href="unmappedSpcAll.htm?offCode=${dupemplist.offCode}&gpc=${dupemplist.gpc}&empID=${dupemplist.empid}" data-remote="false" data-toggle="modal" data-target="#unmappedSpc" ><button type="button" class="btn btn-danger" style="width:150px" >Assign SPC</button></a>
                                                </td>
                                            </tr> 
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${empty dupSPClistDetails}">
                                        <tr>
                                            <td colspan="10" align="center" style="color:red;font-size: 20px;">
                                                NO DUPLICATES
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div id="unmappedSpc" class="modal fade" role="dialog">
                        <div class="modal-dialog">
                            <!-- Modal content-->
                            <div class="modal-content modal-lg">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h3 class="table-active"><u>Map Substantive Post</u></h3>
                                </div>
                                <div class="modal-body"></div>
                                <div class="modal-footer">                       
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>


    </body>
</html>