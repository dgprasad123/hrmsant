<%-- 
    Document   : ASICenterPrivilegeList
    Created on : 25 Nov, 2022, 12:40:54 PM
    Author     : Manisha
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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
        <style type="text/css">
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 50%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .myModalBody{}
        </style>

        <style type="text/css">
            .table > tbody > tr > td{
                font-size: 12px;
            }
        </style>
        <script type="text/javascript">


            function searchcenterPrivilegeList() {
                var fiscalyear = $("#entryYear").val();
                $.post("getCenterPrivilegeYearWise.htm", {entryYear: fiscalyear})
                        .done(function(data) {
                            populateDataInGrid(data);

                        })
            }
            function populateDataInGrid(centerPrivList) {
                $("#centerdatagrid").empty();
                for (var i = 0; i < centerPrivList.length; i++) {

                    var row = '<tr>' +
                            '<td>' + centerPrivList[i].officeName + '</td>' +
                            '<td>' + centerPrivList[i].centerName + '</td>' +
                            '<td><a href="removeCenterPrivilegeYearWise.htm?officeCode=' + centerPrivList[i].officeCode + '" span class="glyphicon glyphicon-remove"></a></td>';

                    row = row + '</tr>';
                    $("#centerdatagrid").append(row);
                }
            }

        </script>
    </head>



    <body>
        <jsp:include page="../../tab/hrmsadminmenu.jsp"/> 
        <div id="wrapper"> 
            <div id="page-wrapper" style="margin-top:145px;z-index:0;">
                <div class="row">
                    <div class="col-lg-12">                            
                        <ol class="breadcrumb">
                            <li>
                                <i class="fa fa-dashboard"></i>  <a href="index.html">Center Allotment Detail</a>
                            </li>

                        </ol>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-2"><label>Financial Year:</label></div>
                    <div class="col-lg-2">
                        <select name="entryYear" id="entryYear" class="form-control">
                            <option value="2022">2022</option>
                            <option value="all">All</option>
                        </select>                            
                    </div>
                    <div class="col-lg-1">
                        <button type="button" class="form-control btn-primary" id="searchbtn" onclick="return searchcenterPrivilegeList()">Search</button>                            
                    </div>
                </div>

                <div class="row">
                    <div class="col-lg-12">
                        <h2>Center List</h2>
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover table-striped">
                                <thead>
                                    <tr>
                                        <th>Office Name</th>
                                        <th>Center Name</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>                                        
                                <tbody id="centerdatagrid">                                        

                                </tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>


