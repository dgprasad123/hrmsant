<%-- 
    Document   : viewMinisterDetail
    Created on : 8 Feb, 2022, 12:12:51 PM
    Author     : Devi
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script src="js/jquery.min.js" type="text/javascript"></script>           
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <style>
            .row-margin{
                margin-bottom: 16px;
            }
        </style>
    </head>
    <body style="font-family: Helvetica; margin: 10px;">
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    View Minister Details 
                </div>
                <div class="panel-body">

                    <div class="row row-margin">
                        <div class="col-lg-2">
                            Current Post Name : 
                            
                        </div>
                        <div class="col-lg-9">
                            
                            <strong>
                                <c:out value="${mindetailsForm.ministertype}"/>
                            </strong>                          
                        </div>
                        <div class="col-lg-1"></div>
                    </div>

                    <div class="row row-margin">
                        <div class="col-lg-2">
                            Current Department Name :
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                 <c:out value="${mindetailsForm.deptname}"/>
                            </strong>
                            
                        </div>
                        <div class="col-lg-1"></div>
                    </div>
                       <div class="row row-margin">
                        <div class="col-lg-2">
                            Name :
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${mindetailsForm.fullname}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>  
                      <div class="row row-margin">
                        <div class="col-lg-2">
                            User ID :
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${mindetailsForm.userid}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>    
                        <div class="row row-margin">
                        <div class="col-lg-2">
                            Password :
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${mindetailsForm.password}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>   
                     <div class="row row-margin">
                        <div class="col-lg-2">
                            Mobile Number :
                        </div>
                        <div class="col-lg-9">
                            <strong>
                                <c:out value="${mindetailsForm.mobileno}"/>
                            </strong>
                        </div>
                        <div class="col-lg-1"></div>
                    </div>           
                </div>
                <div class="panel-footer">
                    <a href="MinisterDetails.htm"> <button type="button" class="btn btn-warning btn-md">&laquo;Back</button></a>
                </div>
            </div>
        </div>
    </body>
</html>

