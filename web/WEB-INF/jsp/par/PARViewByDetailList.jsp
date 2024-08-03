<%-- 
    Document   : PARViewByDetailList
    Created on : 20 Dec, 2023, 5:16:48 PM
    Author     : Manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">    
        <link href="css/sb-admin.css" rel="stylesheet" type="text/css">
        <link href="css/select2.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">

        <script src="js/jquery.min.js"></script> 
        <script src="js/jquery2.0.3.min.js"></script>
        <script src="js/jquery-ui.min.js"></script> 
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jquery.dataTables.js"></script>
        <script src="js/select2.min.js"></script>

        <style>
            .loader {
                border: 16px solid #f3f3f3; /* Light grey */
                border-top: 16px solid #3498db; /* Blue */
                border-radius: 100%;
                width: 40px;
                height: 40px;
                animation: spin 2s linear infinite;
            }
            .tblTrColor{
                background: rgb(174,238,209);
                background: radial-gradient(circle, rgba(174,238,209,0.9976191160057774) 0%, rgba(148,231,233,1) 100%);
                color: #000000;
                font-weight: bold;
            }
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
            .pageHeadingTxt{
                background-color: #DDEDE0;
                font-weight: bold;
                text-transform: uppercase;
                text-align: center;
                color:#000;
                font-size:18px;
                line-height: 20px;
                position: relative;
                letter-spacing: 0.2px;
                padding: 10px 0px;
            }
            .pageSubHeading{
                background-color: #DDEDE0;
                font-weight: bold;
                text-transform: uppercase;
                text-align: center;
                color:#0D8CE7;
                font-size:17px;
                line-height: 20px;
                position: relative;
                letter-spacing: 0.2px;
                padding: 10px 0px;
            }
            .myModalBody{}
            .new_sty{width:33%;}
            .new_sty li{width: 98%;}
            .new_sty li a{width: 100%;}
        </style>


        <script type="text/javascript">
            var rowSize = 20;
            var totalPages = 0;
            $(document).ready(function() {
                $(".loader").hide();
            });
            function searchParDetailView() {
                var fiscalyear = $("#fiscalyear").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalYear").focus();
                    return false;
                }
               
                $(".loader").show();
                $.post("getSearchparViewedByDetailList.htm",
                        {fiscalyear: fiscalyear}
                ).done(function(data) {
                    if (data) {
                        var parListViewed = data;
                        populateDataInGrid(parListViewed);
                        $('#datatable').DataTable();
                    } else {
                        alert("Error Occured");
                    }
                    $(".loader").hide();
                    $("#searchbtn").attr("disabled", false);
                })
                        .fail(function() {
                            $(".loader").hide();
                            $("#searchbtn").attr("disabled", false);
                            alert("Error Occured");
                        });
            }

            function populateDataInGrid(parListViewed) {
                $("#parViewdatagrid").empty();
                for (var i = 0; i < parListViewed.length; i++) {
                    var row = '<tr>' +
                            '<td>' + (i + 1) + '</td>' +
                            '<td>' + parListViewed[i].fiscalyear + '</td>' +
                            '<td>' + '<b style="color:#0000FF">' + parListViewed[i].applicantEmpId + '<br>' + parListViewed[i].parId + '</td>' +
                            '<td>' + '<b style="color:#0000FF">' + parListViewed[i].applicantName + '<br>' + '<b style="color:#FF4500;">' + parListViewed[i].applicantPost + '</b>' + '</td>' +
                            '<td>' + parListViewed[i].applicantOfficeName + '</td>' +
                            '<td>' + parListViewed[i].applicantCadreName + '</td>' +
                            '<td>' + '<b style="color:#0000FF">' + parListViewed[i].viewedByUserType + '</b>' + '</td>' +
                            '<td>' + '<b style="color:#0000FF">' + parListViewed[i].viewedByUserName + '<br>' + '<b style="color:#FF4500;">' + parListViewed[i].viewedByUserPost + '</b>' + '</td>' +
                            '<td>' + parListViewed[i].loginByIp + '</td>' +
                            '<td>' + '<b style="color:#0000FE;">' + parListViewed[i].viewedOnDate + '</td>' +
                            '</tr>';
                    $("#parViewdatagrid").append(row);
                }
            }

           


        </script>

    </head>
    <div id="wrapper" style="padding: 0px"> 
        <div id="page-wrapper">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 style="text-align:center"><b style="color:#0000FF">List of PAR Viewed By Detail List </b></h3>
                </div>

                <div class="row" style="margin: 10px;"> 
                    <div class="col-lg-2">
                        <select name="fiscalyear" id="fiscalyear" class="form-control">
                            <option value=""> Fiscal Year </option>
                            <c:forEach items="${FiscYear}" var="eachFy">
                                <option value="${eachFy.fy}" <c:if test="${eachFy.fy eq FyYear}">selected="selected"</c:if>>${eachFy.fy}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="col-lg-1" align="left">
                        <button class="form-control btn-primary" id="searchbtn" onclick="searchParDetailView()">Search</button>                             
                    </div>
                    <div class="col-lg-1"> <div class="loader"></div> </div>
                    <div class="col-lg-1" align="left">
                        <%--<button class="btn btn-info btn-md" target="_blank" onclick="confirmDownload('${fiscalyear}');">Download Excel</button>
                        <input type="submit" class="btn btn-info btn-md" id="downloadBtn" name="Download" value="Download" onclick="return confirmDownload()"/> --%>
                    </div>
                </div>

                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-12">                            
                            <div class="table-responsive">
                                <table id="datatable" class="table table-bordered table-hover table-striped" width="99%">
                                    <thead class="tblTrColor">
                                        <tr>
                                            <th width="1%"> # </th> 
                                            <th width="8%"> Financial Year </th>
                                            <th width="9%"> Viewed Of Employee Id<br>parid </th> 
                                            <th width="15%"> Viewed Of Employee Name<br>Viewed Of Employee Post</th>                    
                                            <th width="7%"> Office Name </th>
                                            <th width="4%" align="center"> Viewed Of Employee Cadre </th>
                                            <th width="12%"> Viewed By User Type </th>
                                            <th width="10%"> Viewed By User Name <br>Viewed By User Designation</th>
                                            <th width="12%"> Viewed By IP </th>
                                            <th width="13%"> Viewed On Date </th>
                                        </tr>
                                    </thead>
                                    <tbody id="parViewdatagrid"> 

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>        
            </div>
        </div>
    </div>    
</body>
</html>


