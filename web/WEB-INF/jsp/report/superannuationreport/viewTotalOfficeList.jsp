<%-- 
    Document   : viewTotalOfficeList
    Created on : Nov 16, 2023, 2:53:45 PM
    Author     : Madhusmita
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>      
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <%--<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>--%>
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <!-- LAYOUT v 1.3.0 -->
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery-latest.js"></script>    
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/chosen.jquery.min.js"></script>
        <script src="js/jquery.freezeheader.js"></script>        
        <script type="text/javascript" src="js/tableToExcel.js"></script>


        <style type="text/css">
            table th {
                padding-top: 12px;
                padding-bottom: 12px;
                text-align: left;
                background-color: #4CAF50;
                color: white;
            }

            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid blue;
                border-right: 16px solid green;
                border-bottom: 16px solid red;
                border-radius: 50%;
                width: 60px;
                height: 60px;
                animation: spin 2s linear infinite; 
                margin-top: 400px;
                left: 50%;                
            }
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }            

        </style>
        <script type="text/javascript" >
            $(document).ready(function() {
                $("#tblExport").freezeHeader();
                // $('.loader').fadeOut();
                /*$(window).on('load', (function() {
                 $('#loader').hide();
                 $('#pagecontent').show();
                 });*/

                //export to excel without hyperlink

                $("#btnExport2Tabl").click(function() {
                    var x = $("#tblExport").clone();
                    $("#tblExport").find("tr td a").replaceWith(function() {
                        return $.text([this]);
                    });

                    tableToExcel("tblExport", "test", "SanctionedPostReport.xls");
                    $("#tblExport").html(x);
                });

                //export to excel by removing a specific column.

                /*$("#btnExport2Tabl").click(function() {
                 var tempTable = $("#tblExport").html();
                 $("#tblExport .excludeExport").remove();
                 tableToExcel("tblExport", "test", "trys");
                 $("#tblExport").html(tempTable);
                 });*/

            });



        </script>

    </head>
    <body>
        <div align="center">
            <div id="loader" style="position:fixed;top:60px;right:110px;align:center"></div>
        </div> 
        <div id="pagecontent">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h2 class="alert alert-info" style="text-align: center">Home Department Office List</h2>    
                            </div>
                            <%--<div style="position:fixed;top:60px;right:110px;text-align:right;">
                                <a href="downloadExcelPostWiseStrength.htm"><button type="btnDownloadExcel" class="btn btn-primary" data-dismiss="modal">Download Excel</button></a>
                            </div>--%>
                            <%--<div style="position:fixed;top:60px;right:110px;text-align:right;">
                                <input type="button" id="btnExport2Tabl" class="btn btn-primary"  value="Export to Excel"/>
                                <%--<button type="button" id="btnExport2Tabl" onclick="tableToExcel('tblExport', 'Report')" >Export to Excel</button>--%>
                            <%--</div>--%>
                            <div>

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered" id="tblExport" style="table-layout: auto;">
                            <thead>
                                <tr  class="thead-dark">
                                    <th style="text-align: center; color: #000">Sl No</th>
                                    <th style="color: #000">DDO CODE</th>
                                    <th style="text-align: center; color: #000">OFFICE NAME</th>
                                    <th style="text-align: center; color: #000">VIEW</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${ofcList}" var="offlist" varStatus="cnt">
                                    <tr id="idGpc1"> 

                                        <td style="text-align: center;color: #000">${cnt.index+1}</td>
                                        <td style="color: #000">${offlist.offCode}</td>
                                        <td style="color: #000">${offlist.offName}</td>
                                        <td style="text-align: center;color: #000"><a href="ViewstatisticalReportPostStrength.htm?offcode=${offlist.offCode}&offname=${offlist.offName}" target="_blank" >
                                                <button type="button" style="width:150px"  class="btn btn-primary">Statistical Report</button> </a>  </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>                 
                </div>
            </div>
        </div>

    </body>
</html>
