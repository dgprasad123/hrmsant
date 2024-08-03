<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
            .myModalBody{}
        </style>

        <script type="text/javascript">
            $(document).ready(function() {
                var table = $('#datatable').DataTable();
                $(".loader").hide();

                /*$("#openRequestModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });*/
                
                $("#openRequestModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
            });

            function validateModule() {
                if ($("#sbReqModuleName").val() == "") {
                    alert("Please select the Module Name");
                    $("#sbReqModuleName").focus();
                    return false;
                }
                $(".loader").show();
            }
        </script>
    </head>
    <body style="margin-top:0px;background:#E4EFFF;"> 
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <span style="color:red;font-weight: bold;font-size:16px;display: block;text-align:center;">
                                SERVICE BOOK CORRECTION
                            </span>
                        </div>
                    </div>
                </div>
                <div class="panel-body">
                    <div id="wrapper" style="padding-left: 0px;"> 
                        <div id="page-wrapper">
                            <div class="row">
                                <div class="col-lg-12">                            
                                    <div class="table-responsive">
                                        <table id="datatable" class="table table-bordered table-hover table-striped" width="100%">
                                            <thead class="tblTrColor">
                                                <tr>
                                                    <th width="2%"> # </th> 
                                                    <th width="12%">MODULE NAME</th>
                                                    <th width="25%">CURRENT SERVICE BOOK ENTRY</th> 
                                                    <th width="25%">REQUESTED SERVICE BOOK ENTRY</th>          
                                                    <th width="10%">REQUEST DATE</th>
                                                    <th width="5%">ENTRY TYPE</th>
                                                    <th width="10%">Action</th>  
                                                </tr>
                                            </thead>

                                            <tbody>
                                                <c:if test="${not empty sbcorrectionlist}">
                                                    <c:forEach items="${sbcorrectionlist}" var="eachData" varStatus="cnt">
                                                        <tr>
                                                            <td>${cnt.index + 1}</td>
                                                            <td>${eachData.hidNotType}</td>
                                                            <td>${eachData.previousSBLanguage}</td>                                                    
                                                            <td>
                                                                <c:if test="${not empty eachData.sbLanguageRequested && eachData.isProcessed eq 'Y'}">
                                                                    <b style="color:#008000;">${eachData.sbLanguageRequested}</b>
                                                                </c:if>
                                                                <c:if test="${not empty eachData.sbLanguageRequested && eachData.isProcessed ne 'Y'}">
                                                                    <b style="color:#FF4500;">${eachData.sbLanguageRequested}</b>
                                                                </c:if>
                                                            </td>
                                                            <td>${eachData.requestDate}</td>
                                                            <td>
                                                                <c:if test="${eachData.entrytype eq 'NEW'}">
                                                                    ${eachData.entrytype}
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                <c:if test="${not empty eachData.sbLanguageRequested && eachData.isProcessed eq 'N'}">
                                                                    <%--<a href="editIncrement.htm?notId=${eachData.hidNotId}&incrId=${eachData.moduleid}" target="_blank">Edit</a>--%>
                                                                    <c:if test="${eachData.isRejected eq 'N'}">
                                                                        <a href="${eachData.editLink}" data-remote="false" data-toggle="modal" title="Click to Open" data-target="#openRequestModal">
                                                                            <button type="button" class="btn btn-priamry">View</button>
                                                                        </a>&nbsp;
                                                                        <c:set var="etab" value="${fn:replace(eachData.hidNotType,' ','+')}"/>
                                                                        <%--<a href="RejectRequestedServiceBookLanguage.htm?hidNotId=${eachData.hidNotId}&hidNotType=${etab}" onclick="confirm('Are you sure to Reject this Entry?')">
                                                                            <button type="button" class="btn btn-success">Approve</button>
                                                                        </a>--%>
                                                                        <a href="RejectRequestedServiceBookLanguage.htm?empid=${empid}&hidNotId=${eachData.hidNotId}&hidNotType=${etab}" onclick="confirm('Are you sure to Reject this Entry?')">
                                                                            <button type="button" class="btn btn-danger">Reject</button>
                                                                        </a>
                                                                    </c:if>
                                                                    <c:if test="${eachData.isRejected eq 'Y'}">
                                                                        <span style="display: block; font-weight: bold; text-align: center; color: #FF4500;">Rejected</span>
                                                                    </c:if>
                                                                </c:if>
                                                                <c:if test="${not empty eachData.sbLanguageRequested && eachData.isProcessed eq 'Y'}">
                                                                    <span style="display: block; font-weight: bold; text-align: center; color: #FF4500;">Approved</span>
                                                                </c:if>
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
                <div class="panel-footer"></div>
            </div>
        </div>

        <div id="openRequestModal" class="modal fade" role="dialog">
            <div class="modal-dialog" style="width:70%;">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title"> <b style="color:#FF4500;">${ModuleName}</b> </h4>
                    </div>
                    <div class="modal-body"> &nbsp; </div>
                    <div class="modal-footer">
                        <div id='preview'></div>
                        <div class="col-md-4 form-group"> &nbsp; </div>
                        <div class="col-md-4 form-group" align="center"></div>
                        <div class="col-md-4 form-group"> &nbsp; </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
