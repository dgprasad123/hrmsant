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
        <link href="css/colorbox.css" rel="stylesheet" type="text/css">    
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.colorbox-min.js"></script>

        <style>
            .close {
                color: white;
                float: right;
                font-size: 28px;
                font-weight: bold;
            }

            .close:hover,
            .close:focus {
                color: #000;
                text-decoration: none;
                cursor: pointer;
            }

            .modal-header {
                padding: 2px 16px;
                background-color: lightblue;
                color: white;
            }

            .modal-body {padding: 5px 30px;height: 100px;}

            .modal-footer {
                padding: 2px 16px;
                background-color: lightblue;
                color: white;
            }

            #myInput {
                background-image: url('/css/searchicon.png');
                background-position: 10px 10px;
                background-repeat: no-repeat;
                width: 100%;
                font-size: 16px;
                padding: 12px 20px 12px 40px;
                border: 1px solid #ddd;
                margin-bottom: 12px;
            }

            #myTable {
                border-collapse: collapse;
                width: 100%;
                border: 1px solid #ddd;
                font-size: 16px;
            }

            #myTable th, #myTable td {
                text-align: left;
                padding: 12px;
            }

            #myTable tr {
                border-bottom: 1px solid #ddd;
            }

            #myTable tr.header {
                background-color: darkslategrey;
                color: #FFFFFF;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function () {
                $(".iframe").colorbox({iframe: true, width: "80%", height: "80%"});
            });
            function requestNOC(me) {
                $(me).val("NOC Requested");
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
                                    <i class="fa fa-dashboard"></i>  <a href="">Dashboard</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> Monitoring NOC
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Monitoring NOC From Crime Branch & Vigilance</h2>
                            <div class="table-responsive">
                                <form:form action="#">
                                    <div class="container container-fluid" style="padding-top: 10px;">
                                        <div class="row">
                                            <div class="col-1">&nbsp;</div>
                                            <div class="col-5">
                                                <a href="NocListFromVigilanceCB.htm?noctype=cb" target='_blank'><input type="button" name="recommendation" class="btn btn-primary" value="a) NOC Pending at Crime Branch" /></a>
                                            </div>
                                            <div class="col-1">&nbsp;</div>
                                        </div>
                                        <div class="row">
                                            <div class="col-1">&nbsp;</div>
                                            <div class="col-5">
                                                <a href="CompletedNocListFromVigilanceCB.htm?noctype=cb" target='_blank'><input type="button" name="recommendation" class="btn btn-warning" value="b) Noc Completed by Crime Branch" /></a>
                                            </div>
                                            <div class="col-1">&nbsp;</div>
                                        </div>
                                        <div class="row">
                                            <div class="col-1">&nbsp;</div>
                                            <div class="col-5">
                                                <a href="NocListFromVigilanceCB.htm?noctype=vigilance" target='_blank'> <input type="button" name="recommendation" class="btn btn-primary" value="c) NOC Pending at Vigilance" /></a>
                                            </div>
                                            <div class="col-1">&nbsp;</div>

                                        </div>
                                         <div class="row">
                                            <div class="col-1">&nbsp;</div>
                                            <div class="col-5">
                                                <a href="CompletedNocListFromVigilanceCB.htm?noctype=vigilance" target='_blank'><input type="button" name="recommendation" class="btn btn-warning" value="d) NOC Completed by Vigilance" /></a>
                                            </div>
                                            <div class="col-1">&nbsp;</div>

                                        </div>
                                        
                                        <div class="row">
                                            <div class="col-1">&nbsp;</div>
                                            <div class="col-5">
                                                &nbsp;
                                            </div>
                                            <div class="col-1">&nbsp;</div>

                                        </div>
                                        
                                        
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>                                           
                </div>
            </div>
        </div>
</html>


