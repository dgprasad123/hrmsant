<%-- 
    Document   : PARDistrictwisePrivilegeForPolice
    Created on : 9 Jun, 2022, 12:41:37 PM
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
        <link href="css/sb-admin.css" rel="stylesheet" type="text/css">
        <link href="css/select2.min.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" type="text/css" href="css/jquery.dataTables.css">

        <script src="js/jquery.min.js"></script> 
        <script src="js/jquery2.0.3.min.js"></script>
        <script src="js/jquery-ui.min.js"></script> 
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/jquery.dataTables.js"></script>
        <script src="js/select2.min.js"></script>
        
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
            .new_sty{width:33%;}
            .new_sty li{width: 98%;}
            .new_sty li a{width: 100%;}
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
        <div id="wrapper"> 
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>
            <form:form id="siParForm" action="uploadPreviousYearSiPar.htm" method="post" commandName="ParAdminSearchCriteria">
                <div id="page-wrapper">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h3 style="text-align:center"> Previous Year PAR for SI & Equivalent Ranks Uploaded List </h3>
                        </div>
                        
                        <div class="panel-body" style="border-bottom: 2px solid #A6ECD9;">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2" align="left">
                                    <input type="submit" class="btn btn-success btn-md" id="downloadBtn" name="UploadPar" value="Upload Previous Year PAR" /> 
                                </div>
                                <div class="col-lg-10"> &nbsp; </div>
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
                                                    <th width="7%"> Financial Year </th>
                                                    <th width="5%"> PAR Type </th>
                                                    <th width="17%"> PAR Uploaded By Employee Name, Post </th> 
                                                    <th width="17%"> PAR Uploaded For Employee Name, <br/>GPF/ PRAN No, Post</th>                    
                                                    <th width="7%"> PAR Uploaded On </th>
                                                    <th width="10%"> Final Grading For PAR </th>
                                                    <th width="10%"> Attachment </th>
                                                </tr>
                                            </thead>
                                            <!-- <tbody id="pardatagrid"> pkm </tbody> -->
                                            <tbody> 
                                                <c:if test="${not empty UploadSiParList}">
                                                    <c:forEach items="${UploadSiParList}" var="eachPar" varStatus="cnt">
                                                        <tr>
                                                            <td>${cnt.index + 1}</td>
                                                            <td>${eachPar.fiscalyear}</td>
                                                            <td>${eachPar.parTypeForPreviousYearPAR}</td>
                                                            <td> ${eachPar.previousyrParUploadedbyempName} </td>
                                                            <td><b style="color:#0000FE;">${eachPar.empName}, </b><b style="color:#FF4500">(${eachPar.gpfNo})</b>,<br/>${eachPar.postName}</td>
                                                            <td> ${eachPar.previousyrParUploadOn} </td>
                                                            <td> ${eachPar.finalGradingNameForPreviousYrPAR} </td>
                                                            <td> 
                                                                <a href="downloadAttachmentOfPreviousYearPAR.htm?parId=${eachPar.parId}" >
                                                                    <span class="glyphicon glyphicon-paperclip"></span> ${eachPar.originalFileNameForpreviousYearPAR}
                                                                </a>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>        
                    </div>
                </div>
            </form:form>
        </div>    
</body>
</html>


