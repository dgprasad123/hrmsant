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
                        <form:form action="viewRecommendationListForGroupb.htm" commandName="recommendationDetailBean" method="post">
                            <div class="col-lg-2" align="right"><b>Choose Group</b></div>
                            <div class="col-lg-2">
                                <form:select path="recommendedempgroup" class="form-control">                                                                
                                    <form:option value="B">Group B</form:option>
                                </form:select>                            
                            </div>
                            <div class="col-lg-2" align="right"><b>Nomination Type</b></div>
                            <div class="col-lg-2">
                                <form:select path="recommenadationType" class="form-control">
                                    <form:option value="All">All</form:option>
                                    <form:option value="within batch promotion">Out Of turn Promotion(within the batch)</form:option>
                                    <form:option value="across batch promotion">Out Of turn Promotion(across the batches)</form:option>  
                                    <form:option value="incentives award">Award Of Incentives</form:option>
                                    <form:option value="Premature Retirement">Premature Retirement</form:option>
                                </form:select>                            
                            </div>
                            <div class="col-lg-2"><button type="submit" class="btn btn-success" class="form-control ">Search</button> </div>
                        </form:form>
                    </div>                    


                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Nominated Employee List</h2>
                            <div class="table-responsive">
                                <table id="myTable" class="table table-striped table-bordered table-success" width="100%" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th>Sl No</th>
                                            <th>GPF / PRAN No</th>
                                            <th>Employee Name, Post <br/>Office Name</th>
                                            <th>Nominated By</th>
                                            <th>Nomination Type</th>
                                            <th>Detail</th>
                                            <th>PAR</th>
                                            <th>NOC From<br/> Crime Branch</th>
                                            <th>NOC From<br/> Vigilance</th>
                                        </tr>

                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${recommendationList}" var="recommendation" varStatus="count">
                                            <tr>
                                                <td>${count.index + 1}</td>
                                                <td>${recommendation.recommendedempGpfNo}</td>
                                                <td>
                                                    ${recommendation.recommendedempname}
                                                    <br/>${recommendation.recommendedpost}
                                                    <br/>${recommendation.recommendedempofficename}
                                                    <br/>Post Group: ${recommendation.recommendedpostgrp}</td>
                                                <td>${recommendation.nominatedbyemployee}</td>
                                                <td style="text-transform: capitalize;">${recommendation.recommenadationType}</td>
                                                <td>
                                                    <a href="viewRecommendedEmployeeDetail.htm?recommendeddetailId=${recommendation.recommendeddetailId}" class="iframe">Detail</a> | 
                                                    <a href="nominationDetailPdfView.htm?recommendeddetailId=${recommendation.recommendeddetailId}" target="_blank">
                                                        <img src="images/pdf.png" width="20" height="20">
                                                    </a>                                                    
                                                </td>
                                                <td><a href="getPARList.htm?recommendedempId=${recommendation.recommendedempId}" class="iframe">Detail</a></td>
                                                <td>
                                                    <c:if test="${ empty recommendation.cbnocreason &&  recommendation.cbranchnocstatus eq 'Y'}">
                                                        <span class="text-success" style="font-weight: bold;">Approved</span><br/>
                                                        <a href="GenerateNoc.htm?nocId=${recommendation.nocId}&hrmsid=${recommendation.recommendedempId}" class="btn btn-default" >
                                                            <span class="glyphicon glyphicon-paperclip"></span>  Download document
                                                        </a> 
                                                    </c:if>
                                                    <c:if  test="${not empty recommendation.cbnocreason && recommendation.vNocStatus eq 'Y'}">
                                                        <span class="text-danger" style="font-weight: bold;">Declined </span><br/>
                                                        <a href="downloadAttachmentOfNOC.htm?nocId=${recommendation.nocId}" class="btn btn-default" >
                                                            <span class="glyphicon glyphicon-paperclip"></span> Download document
                                                        </a> 
                                                    </c:if>
                                                    <c:if  test="${recommendation.cbranchnocstatus eq 'N'}">                                                   
                                                        NOC Requested
                                                    </c:if>                                                    
                                                </td>
                                                <td>
                                                    <c:if test="${ empty recommendation.vigilancenocreason &&  recommendation.vigilancenocstatus eq 'Y'}">
                                                        <span class="text-success" style="font-weight: bold;">Approved</span><br/>
                                                        <a href="GeneratevNoc.htm?nocId=${recommendation.nocId}&hrmsid=${recommendation.recommendedempId}" class="btn btn-default" >
                                                            <span class="glyphicon glyphicon-paperclip"></span>  Download document
                                                        </a> 
                                                    </c:if>
                                                    <c:if  test="${not empty recommendation.vigilancenocreason && recommendation.vigilancenocstatus eq 'Y'}">
                                                        <span class="text-danger" style="font-weight: bold;">Declined </span><br/>
                                                        <a href="downloadAttachmentOfNOC.htm?nocId=${pnoc.nocId}" class="btn btn-default" >
                                                            <span class="glyphicon glyphicon-paperclip"></span> Download document
                                                        </a> 
                                                    </c:if>    
                                                    <c:if  test="${recommendation.vigilancenocstatus eq 'N'}">                                                   
                                                        NOC Requested
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>                                           
                </div>
            </div>
        </div>
</html>


