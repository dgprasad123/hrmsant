<%-- 
    Document   : AERPostWiseStrengthForCO
    Created on : Dec 13, 2022, 11:47:16 AM
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
                
                //export to excel without hyperlink
                
                 $("#btnExport2Tabl").click(function() {
                    var x = $("#tblExport").clone();
                    $("#tblExport").find("tr td a").replaceWith(function() {
                        return $.text([this]);
                    });

                    tableToExcel("tblExport", "test", "SanctionedPostReport.xls");
                    $("#tblExport").html(x);
                });


            });


            function viewDuplicateEmpForCO(dupPost) {
                var emplist = "";
                $('#dupSpcEmpList').empty();
                var url = 'viewDuplicateEmpJSONForCO.htm?dupPostCode=' + dupPost;
                // alert(dupPost);
                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        emplist = emplist + "<tr>" +
                                "<td>" + (i + 1) + "</td>" +
                                "<td>" + obj.empName + "</td>" +
                                "<td>" + obj.empId + "</td>" +
                                "<td>" + obj.gpfNo + "</td>" +
                                "<td>" + obj.offName + "</td>" +
                                "</tr>";
                    });
                    $('#dupSpcEmpList').append(emplist);
                });
            }
            /*$('#btnExport2Tabl').click(function() {
             $("#tblExport").table2excel({
             $("a").removeAttr("href");
             alert('hi');
             exclude: ".xpage",
             name: "Worksheet Name",
             filename: "download.xls",
             fileext:".xls",
             exclude_img: true,
             exclude_links: true,
             exclude_inputs: true
             });
             });*/




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
                                <h2 class="alert alert-info" style="text-align: center">Post Wise Strength Report</h2>    
                            </div>
                            <%--<div style="position:fixed;top:60px;right:110px;text-align:right;">
                                <a href="downloadExcelPostWiseStrength.htm"><button type="btnDownloadExcel" class="btn btn-primary" data-dismiss="modal">Download Excel</button></a>
                            </div>--%>
                            <div style="position:fixed;top:60px;right:110px;text-align:right;">
                                <input type="button" id="btnExport2Tabl" class="btn btn-primary" value="Export to Excel"/>
                                <%--<button type="button" class="btnExport2Tabl" onclick="tableToExcel('tblExport', 'Report')" >Export to Excel</button>--%>
                            </div>
                            <div>

                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered" id="tblExport" style="table-layout: auto;">
                            <thead>
                                <tr  class="thead-dark">
                                    <th style="text-align: center; color: #000">Sl No</th>
                                    <th style="color: #000">Post Name</th>
                                    <th style="text-align: center; color: #000">Sanctioned Post</th>
                                    <th style="text-align: center; color: #000">Men In Position</th>
                                    <th style="text-align: center; color: #000">Vacancy</th>
                                    <th style="text-align: center; color: #000">Action</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="totSanctionedPost" value="${0}"/>
                                <c:set var="totMenInPosition" value="${0}"/>
                                <c:set var="totVacany" value="${0}"/>
                                <c:forEach items="${genericPostList}" var="genPost" varStatus="cnt">
                                    <c:if test="${genPost.gpc==genPost.dupGpc}">
                                    <div class="col-lg-12" style="">
                                        <h3 style="text-align: center;"><span style="color: red">Duplicate Post Found</span></h3>
                                    </div>
                                    <tr id="idGpc" style="background: lightpink;">                                
                                        <c:set var="totSanctionedPost" value="${totSanctionedPost+genPost.sanctionedStrength}"/>
                                        <c:set var="totMenInPosition" value="${totMenInPosition+genPost.meninPosition}"/>
                                        <c:set var="totVacany" value="${totVacany+genPost.vacancyPosition}"/>
                                        <td style="text-align: center;color: #000">${cnt.index+1}</td>
                                        <td style="color: #000">${genPost.postname}</td>
                                        <td style="text-align: center;color: #000">${genPost.sanctionedStrength}</td>
                                        <c:if test="${genPost.meninPosition eq '0'}">
                                            <td  style="text-align: center;color: #000">${genPost.meninPosition}</td>
                                        </c:if>
                                        <c:if test="${genPost.meninPosition ne '0'}">
                                            <td id="btnMenInPosition" style="text-align: center;color: #000"><a href="gpcWiseemployeeListForCO.htm?gpc=${genPost.dupGpc}" target="_blank">${genPost.meninPosition}</a></td>
                                            </c:if>
                                        <td style="text-align: center;color: #000">${genPost.vacancyPosition}</td>  
                                        <td style="text-align: center;color: #000">
                                            <a href="javascript:viewDuplicateEmpForCO('<c:out value="${genPost.dupGpc}"/>')" />      
                                            <button type="button" style="width:50%;font-weight:bold" class="btn btn-primary" data-remote="false" data-toggle="modal" data-target="#viewDuplicateEmpForCO" >View Duplicates</button>
                                            </a>
                                        </td>
                                    </tr>
                                </c:if>
                                <c:if test="${genPost.gpc != genPost.dupGpc}">
                                    <tr id="idGpc1"> 
                                        <c:set var="totSanctionedPost" value="${totSanctionedPost+genPost.sanctionedStrength}"/>
                                        <c:set var="totMenInPosition" value="${totMenInPosition+genPost.meninPosition}"/>
                                        <c:set var="totVacany" value="${totVacany+genPost.vacancyPosition}"/>
                                        <td style="text-align: center;color: #000">${cnt.index+1}</td>
                                        <td style="color: #000">${genPost.postname}</td>
                                        <td style="text-align: center;color: #000">${genPost.sanctionedStrength}</td>
                                        <c:if test="${genPost.meninPosition eq '0'}">
                                            <td style="text-align: center;color: #000">${genPost.meninPosition}</td>
                                        </c:if>
                                        <c:if test="${genPost.meninPosition ne '0'}">
                                            <td id="btnMenInPosition1" style="text-align: center;color: #000"><a href="gpcWiseemployeeListForCO.htm?gpc=${genPost.gpc}" target="_blank">${genPost.meninPosition}</a></td>
                                            </c:if>
                                        <td style="text-align: center;color: #000">${genPost.vacancyPosition}</td> 
                                        <td style="text-align: center;color: #000">
                                        </td>

                                    </tr>
                                </c:if>
                            </c:forEach>
                            <tr style="background-color: #0071c5;color: #ffffff;">
                                <th>&nbsp;</th>
                                <th>&nbsp;</th>
                                <th style="text-align: center; color: #000">${totSanctionedPost}</th>
                                <th style="text-align: center; color: #000">${totMenInPosition}</th>
                                <th style="text-align: center; color: #000">${totVacany}</th>   
                                <th style="text-align: center; color: #000">&nbsp;</th>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                    <div id="viewDuplicateEmpForCO" class="modal fade" role="dialog">
                        <div class="modal-dialog">
                            <!-- Modal content-->
                            <div class="modal-content modal-lg">
                                <div class="modal-header" align="center">
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                    <h3 class="table-active"><u>Employee Mapped in One generic Post</u></h3>
                                </div>
                                <div class="modal-body">
                                    <table class="table" border="1"  cellspacing="10"  style="font-size:12px; font-family:verdana;" id="tabl1"> 
                                        <thead>
                                            <tr >
                                                <th style="text-align:center;">Sl No</th>
                                                <th style="text-align:center;">Employee Name</th>
                                                <th style="text-align:center;">Hrms Id</th>
                                                <th style="text-align:center;">Gpf No</th>
                                                <th style="text-align:center;">Office Name</th>
                                            </tr>
                                        </thead>
                                        <tbody id="dupSpcEmpList" style="text-align:center;">                                                    

                                        </tbody>
                                    </table>  
                                </div>
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
