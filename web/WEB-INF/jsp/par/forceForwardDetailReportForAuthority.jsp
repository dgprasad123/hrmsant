<%-- 
    Document   : forceForwardDetailReportForAuthority
    Created on : 21 Aug, 2023, 2:44:13 PM
    Author     : Manisha
--%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ page contentType="text/html;charset=UTF-8"%>

<%
    String contextPath = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
            }
            th, td {
                padding: 5px;
                text-align: left;    
            }
            .table-responsive {
                max-height:450px;
                font-size: 10px;
            }
            .table-bordered{
                font-size: 12px;
            }
        </style>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#recordDIV").hide();

                $.post("GetFiscalYearListJSON.htm", function(data) {
                    for (var i = 0; i < data.length; i++) {
                        $('#fiscalyear').append($('<option>', {value: data[i].fy, text: data[i].fy}));
                    }
                });
            });

            function searchforForwardList() {
                var fiscalyear = $("#fiscalyear").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                $.post("getforceForwardListAuthority.htm", {fiscalyear: fiscalyear})
                        .done(function(data) {
                            var forceForwardList = data;
                            populateDataInGrid(forceForwardList);
                        })



            }

            function populateDataInGrid(forceForwardList) {
                $("#forceforwarddatagrid").empty();
                if (forceForwardList.length > 0) {
                    for (var i = 0; i < forceForwardList.length; i++) {
                        var row = '<tr>' +
                                '<td>' + (i + 1) + '</td>' +
                                '<td>' + forceForwardList[i].empId + '</td>' +
                                '<td>' + forceForwardList[i].empName + '</td>' +
                                '<td>' + forceForwardList[i].parId + '</td>' +
                                '<td>' + forceForwardList[i].forceforwardOn + '</td>';
                        $("#forceforwarddatagrid").append(row);
                    }
                } else {
                    $("#recordDIV").show();
                }


            }
        </script>
    </head>
    <body>
        <div id="page-wrapper">
            <div class="container-fluid">
                <div class="panel panel-primary">
                    <div class="panel-heading" align="center" style="background-color: #0071c5;color: #ffffff;font-size: xx-large;">Force Forward Detail List</div>

                    <%--<h3 style="text-align: right"> <input type="button" name="action" value="Download As Excel" class="btn btn-primary" /></h3> --%>

                    <div class="panel panel-default">
                        <div class="panel-body">
                            <div class="col-lg-2">
                                <select name="fiscalyear" id="fiscalyear" class="form-control">
                                    <option value="">Year</option>
                                    <option value="all">All</option>
                                </select>                            
                            </div>
                            <div class="col-lg-1">
                                <button type="submit" class="form-control btn-primary" id="searchbtn" onclick="return searchforForwardList()">Search</button>                            
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-12">                            
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead style="background-color: #0071c5;color: #ffffff;">
                                        <tr>
                                            <th width="3%">#</th>
                                            <th width="25%">Employee Id</th>
                                            <th width="25%">Employee Name</th>
                                            <th width="25%">ParId</th>
                                            <th width="15%">Force Forward On</th>
                                        </tr>
                                    </thead>
                                    <tbody id="forceforwarddatagrid">                                        

                                    </tbody>

                                </table>
                            </div>
                        </div>
                    </div>
                    <span id="recordDIV" style="font-weight:bold; color: #FF0000;font-size: xx-large; text-align: center">
                        No Record Found
                    </span>
                    

                </div>
            </div>
        </div>
    </body>
</html>

