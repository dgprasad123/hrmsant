<%-- 
    Document   : PARCustdianOfficeWise
    Created on : 4 Jan, 2024, 10:55:16 AM
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
                border-radius: 50%;
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
            function searchPar() {
                var fiscalyear = $("#fiscalyear").val();
                var searchCriteria = $("#searchCriteria").val();
                var searchString = $("#searchString").val();
                if (fiscalyear == "") {
                    alert("Choose Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                if ($("#searchCriteria").val() != "") {
                    if ($("#searchString").val() == "") {
                        alert("please Enter all the Search value");
                        $("#searchString").focus();
                        return false;
                    }
                }
                $(".loader").show();
                $("#searchbtn").attr("disabled", true);
                $.post("getSearchOfficeWiseParList.htm",
                        {fiscalyear: fiscalyear, searchCriteria: searchCriteria, searchString: searchString}
                ).done(function(data) {
                    if (data) {
                        var parlist = data;
                        populateDataInGrid(parlist);
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

            function populateDataInGrid(parlist) {
                $("#officeWisePardatagrid").empty();
                for (var i = 0; i < parlist.length; i++) {
                    var row = '<tr>' +
                            '<td>' + (i + 1) + '</td>' +
                            '<td>' + parlist[i].fiscalyear + '</td>' +
                            '<td>' + '<b style="color:#0000FF">' + parlist[i].empId + '<br>' + parlist[i].gpfno + '</td>' +
                            '<td>' + '<b style="color:#0000FF">' + parlist[i].empName + '<br>' + '<b style="color:#FF4500;">' + parlist[i].mobile + '</b>' + '</td>' +
                            '<td>' + parlist[i].dob + '</td>' +
                            '<td>' + parlist[i].groupName + '</td>' +
                            '<td>' + parlist[i].postName + '</td>' +
                            '<td>' + parlist[i].cadreName + '</td>' +
                            '<td>' + parlist[i].currentoffice + '</td>' +
                            '<td>' + '<b style="color:#0000FE;">' + parlist[i].parstatus + '</td>' +
                            '<td><a href="officeWiseParDataCustodianView.htm?empId=' + parlist[i].empId + '&fiscalyear=' + $("#fiscalyear").val() + '&encParId=' + parlist[i].encParId + '">View</a></td>' +
                            //'<td><a href="javascript:void(0);" onclick="javascript: window.open(\'viewPARAdmindetail.htm?empId=' + parlist[i].empId + '&fiscalyear=' + $("#fiscalyear").val() +'\', \'\', \'width=600,height=800,top=200,left=200,menubar=0\')">View</a>' +
                            '</tr>';
                    $("#officeWisePardatagrid").append(row);
                }
            }

            function setSearchStringFormat(me) {
                if ($(me).val() == "dob") {
                    $("#searchString").attr("placeholder", "dd-mm-yyyy");
                } else {
                    $("#searchString").removeAttr("placeholder");
                }
            }


        </script>

    </head>
    <body style="margin-top:0px;background:#188B7A;">  
     <div id="wrapper" style="padding-left: 0px;">  
        <div id="page-wrapper">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 style="text-align:center">Office Wise List Of PAR </h3>
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
                    <div class="col-lg-2" align="right"><b>Search Criteria</b></div>
                    <div class="col-lg-2">
                        <select name="searchCriteria" id="searchCriteria" class="form-control" onchange="setSearchStringFormat(this)">
                            <option value=""> ALL </option>
                            <option value="empid"> Employee Id </option>
                            <option value="empname"> First Name </option> 
                            <option value="lastname"> Last Name </option>
                            <option value="gpfno"> GPF/PRAN NO </option>
                            <option value="dob"> DOB </option>
                            <option value="mobile"> Mobile No </option>
                        </select>
                    </div>

                    <div class="col-lg-2">
                        <input type="text" name="searchString" id="searchString" class="form-control"> 
                    </div>
                    <div class="col-lg-1" align="left">
                        <button class="form-control btn-primary" id="searchbtn" onclick="searchPar()">Search</button>                             
                    </div>
                    <div class="col-lg-1"> <div class="loader"></div> </div>
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
                                            <th width="9%"> Employee Id<br>GPF / PRAN NO </th> 
                                            <th width="15%"> Employee Name<br>Mobile</th>                    
                                            <th width="7%"> Date of Birth </th>
                                            <th width="4%" align="center"> Post Group </th>
                                            <th width="12%"> Designation </th>
                                            <th width="10%"> Cadre </th>
                                            <th width="12%"> Place of Posting </th>
                                            <th width="13%"> Status </th>
                                            <th width="4%"> Action </th>  
                                        </tr>
                                    </thead>
                                    <tbody id="officeWisePardatagrid"> 

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


