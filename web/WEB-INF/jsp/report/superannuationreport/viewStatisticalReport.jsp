<%-- 
    Document   : viewStatisticalReport
    Created on : Nov 16, 2023, 11:32:36 AM
    Author     : Madhusmita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>
        <script type="text/javascript" src="js/jquery.easyui.min.js"></script>
        <script type="text/javascript" src="js/datagrid-detailview.js"></script>
        <script type="text/javascript" src="js/webcam.js"></script>
        <script type="text/javascript"  src="js/jquery.colorbox-min.js"></script>
        <script type="text/javascript" >
            $(document).ready(function() {

                $("#btnExport2Tabl").click(function() {
                    var x = $("#tblExport").clone();
                    $("#tblExport").find("tr td a").replaceWith(function() {
                        return $.text([this]);
                    });
                    tableToExcel("tblExport", "test", "SanctionedPostReport.xls");
                    $("#tblExport").html(x);
                });
            });
        </script>
    </head>

    <body>
        <div class="container-fluid" style="padding-top: 20px;padding-bottom: 75px;">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <h3 style="color:  #0071c5;" align="center"><strong> <u>Statistical Report of Post Sanctioned Strength</u></strong></h3>
                            <h3 style="color:  #0071c5;" align="center" <u>${OffName}</u></h3>
                        </div>
                        <div style="position:fixed;top:60px;right:110px;text-align:right;">
                            <input type="button" id="btnExport2Tabl" class="btn btn-primary"  value="Export to Excel"/>
                            <%--<button type="button" id="btnExport2Tabl" onclick="tableToExcel('tblExport', 'Report')" >Export to Excel</button>--%>
                        </div>
                    </div>

                </div>
                <div class="panel-body">                   
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover table-striped">
                            <thead>
                                <tr style="background-color: #0071c5;color: #ffffff;">
                                    <th>Sl No</th>
                                    <th>Post Name</th>
                                    <th>Sanctioned Strength</th>                                                
                                    <th>Present Strength</th>
                                    <th>Vacancy</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${postlist}" var="postList" varStatus="cnt">
                                    <tr>
                                        <td>${cnt.index+1}</td>
                                        <td>${postList.postname}</td>
                                        <td>${postList.totalpost}</td>
                                        <td>${postList.meninposition}</td>
                                        <td>${postList.vacancy}</td>
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
