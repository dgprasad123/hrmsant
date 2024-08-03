<%-- 
    Document   : PARViewer
    Created on : 26 Nov, 2020, 1:22:12 PM
    Author     : Manisha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        
        <link href="css/sb-admin.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">
        
        <script src="js/jquery2.0.3.min.js"></script>
        <script src="js/jquery-ui.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jquery.dataTables.js"></script>
        
        <style type="text/css">
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
        </style>
        
        <script type="text/javascript">
            $(document).ready(function() {
                var table = $('#datatable').DataTable();
                $(".loader").hide();
            });
        
            function searchPar() {
                if ($("#fiscalyear").val() == "") {
                    alert("please Select the Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
                $(".loader").show();
            }
            
            function confirmDownload() {
                if ($("#fiscalyear").val() == "") {
                    alert("please Select the Fiscal Year");
                    $("#fiscalyear").focus();
                    return false;
                }
            }

        </script>
    </head>

    <body style="margin-top:0px;background:#E4EFFF;"> 
        
        <form:form id="siParForm" action="offWiseSiPARList.htm" method="post" commandName="ParAdminSearchCriteria">
        <div id="wrapper" style="padding-left: 0px;"> 
            <div id="page-wrapper">                
                <div class="row" style="margin-bottom: 10px;">                        
                    <div class="col-lg-1"> <input type="hidden" name="authorizationType" id="authorizationType" value="${pap.authorizationType}"/> </div>
                    <div class="col-lg-2">
                        <select name="fiscalyear" id="fiscalyear" class="form-control">
                            <option value=""> Fiscal Year </option>
                            <c:forEach items="${fiscyear}" var="eachFy">
                                <option value="${eachFy.fy}" <c:if test="${eachFy.fy eq FyYear}">selected="selected"</c:if>>${eachFy.fy}</option>
                            </c:forEach>
                        </select>                            
                    </div>
                    <div class="col-lg-1" align="left">
                        <input type="submit" class="btn btn-warning btn-md" id="searchbtn" name="Search" value="Search" onclick="return searchPar()"/>
                    </div>
                    
                    <div class="col-lg-1"> <div class="loader"></div> </div>
                    <div class="col-lg-2" align="left">
                        <input type="submit" class="btn btn-info btn-md" id="downloadBtn" name="Download" value="Download" onclick="return confirmDownload()"/>
                    </div>
                    <div class="btn-group col-lg-4"> &nbsp; </div>
                </div>

                <div class="row">
                    <div class="col-lg-12">                            
                        <div class="table-responsive">
                            <table id="datatable" class="table table-bordered table-hover table-striped">
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
                                <!-- <tbody id="pardatagrid"> pkm </tbody> -->
                                <tbody>                                        
                                    <c:forEach items="${SiParDataList}" var="eachPar" varStatus="cnt">
                                        <tr>
                                            <td>${cnt.index + 1}</td>
                                            <td>${eachPar.fiscalyear}</td>
                                            <td><b style="color:#0000FE;">${eachPar.empId}</b>,<br/>${eachPar.gpfno}</td>
                                            <td>${eachPar.empName}<br/><b style="color:#FF4500;">(Mob- ${eachPar.mobile})</b></td>
                                            <td> ${eachPar.dob} </td>
                                            <td> ${eachPar.groupName} </td>
                                            <td> ${eachPar.postName} </td>
                                            <td> ${eachPar.cadreName} </td>
                                            <td> ${eachPar.currentOfficeName} </td>
                                            <td> <b style="color:#0000FE;">${eachPar.parstatus}</b> 
                                                <c:if test="${eachPar.parstatus eq 'REQUESTED FOR NRC'}">
                                                    , </br>${eachPar.nrcDetails}
                                                </c:if>
                                            </td>
                                            <td> 
                                                <c:if test="${eachPar.parstatus ne 'REQUESTED FOR NRC'}">
                                                    <a href="viewSiParDetail.htm?empId=${eachPar.empId}&fiscalyear=${eachPar.fiscalyear}&encParId=${eachPar.encParId}" style="color:#FF4500;"> View </a>
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
        </form:form>

    </body>
</html>


