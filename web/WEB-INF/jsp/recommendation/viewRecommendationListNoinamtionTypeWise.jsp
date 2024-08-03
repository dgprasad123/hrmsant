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
                                    <i class="fa fa-file"></i> Nominated Employee List 
                                </li>
                            </ol>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Nominated Employee List</h2>
                            <div class="table-responsive">
                                <table id="myTable" class="table table-striped table-bordered table-success" width="100%" cellspacing="0">
                                    <thead>
                                        <tr style="font-weight:bold;font-size:12pt;">                                            
                                            <td align="center" width="25%">Nomination Type</td>
                                            <td align="center">A</td>
                                            <td align="center">B</td>
                                            <td align="center">C</td>
                                            <td align="center">D</td>
                                            <td align="center">Total</td>
                                        </tr>

                                    </thead>
                                    <tbody> 
                                        <tr>
                                            <td>within batch promotion</td>
                                            <td>${recommendationList['deptDistrictWiseNominationWB'].pendingatcollectorateA}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationWB'].pendingatcollectorateB}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationWB'].pendingatcollectorateC}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationWB'].pendingatcollectorateD}</td>
                                            <td>
                                                ${recommendationList['deptDistrictWiseNominationWB'].pendingatcollectorateA+
                                                recommendationList['deptDistrictWiseNominationWB'].pendingatcollectorateB+
                                                recommendationList['deptDistrictWiseNominationWB'].pendingatcollectorateC+
                                                recommendationList['deptDistrictWiseNominationWB'].pendingatcollectorateD}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>across batch promotion</td>
                                            <td>${recommendationList['deptDistrictWiseNominationAB'].pendingatcollectorateA}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationAB'].pendingatcollectorateB}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationAB'].pendingatcollectorateC}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationAB'].pendingatcollectorateD}</td>
                                            <td>
                                                ${recommendationList['deptDistrictWiseNominationAB'].pendingatcollectorateA+
                                                recommendationList['deptDistrictWiseNominationAB'].pendingatcollectorateB+
                                                recommendationList['deptDistrictWiseNominationAB'].pendingatcollectorateC+
                                                recommendationList['deptDistrictWiseNominationAB'].pendingatcollectorateD}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>incentives award</td>
                                            <td>${recommendationList['deptDistrictWiseNominationIA'].pendingatcollectorateA}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationIA'].pendingatcollectorateB}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationIA'].pendingatcollectorateC}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationIA'].pendingatcollectorateD}</td>
                                            <td>
                                                ${recommendationList['deptDistrictWiseNominationIA'].pendingatcollectorateA+
                                                recommendationList['deptDistrictWiseNominationIA'].pendingatcollectorateB+
                                                recommendationList['deptDistrictWiseNominationIA'].pendingatcollectorateC+
                                                recommendationList['deptDistrictWiseNominationIA'].pendingatcollectorateD}
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>Premature Retirement</td>
                                            <td>${recommendationList['deptDistrictWiseNominationPR'].pendingatcollectorateA}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationPR'].pendingatcollectorateB}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationPR'].pendingatcollectorateC}</td>
                                            <td>${recommendationList['deptDistrictWiseNominationPR'].pendingatcollectorateD}</td>
                                            <td>
                                                ${recommendationList['deptDistrictWiseNominationPR'].pendingatcollectorateA+
                                                recommendationList['deptDistrictWiseNominationPR'].pendingatcollectorateB+
                                                recommendationList['deptDistrictWiseNominationPR'].pendingatcollectorateC+
                                                recommendationList['deptDistrictWiseNominationPR'].pendingatcollectorateD}
                                            </td>
                                        </tr>

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>                                           
                </div>
            </div>
        </div>
</html>


