<%-- 
    Document   : parCustodianOfficeWiseView
    Created on : 4 Jan, 2024, 4:38:15 PM
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

        </script>
    </head>
    
        <body style="margin-top:0px;background:#188B7A;"> 
            <div id="wrapper" style="padding-left: 0px;"> 
                <div id="page-wrapper">
                <form:form action="OfficeWiseParCustodian.htm" method="post" commandName="ParAdminSearchCriteria">
                    <div id="page-wrapper">
                        <div class="panel panel-default">

                            <div class="panel-heading">
                                <h3 style="text-align:center"> &nbsp; </h3>
                            </div>

                            <div width="100%"> 
                                <div align="center" style="margin: 10px;">
                                    <span id="buttonarea">
                                        <input type="hidden" name="fiscalyear" id="fiscalyear" value="${Fyear}"/>
                                        <input type="submit" class="btn btn-info btn-lg" id="searchbtn" name="Back" value="Back"/>
                                    </span>
                                </div>
                            </div>



                            <div class="panel-body">
                                <div class="row">
                                    <div class="col-lg-12">                            
                                        <div class="table-responsive">
                                            <table id="datatable" class="table table-bordered table-hover table-striped" width="99%">
                                                <thead class="tblTrColor">
                                                    <tr>
                                                        <th width="3%">Sl No</th>
                                                        <th width="10%">Employee Name, </br> Par Id</th>
                                                        <th width="10%">Period</th>
                                                        <th width="10%">Designation During the period of Report</th>

                                                        <th width="20%">Status, <br>Pending At Authority with Designation</th> 
                                                        <th width="10%">Pending At Authority From</th>
                                                        <th width="5%" colspan="2" style="text-align: center">Action /<br> Attachment<br>(if any)</th>
                                                        
                                                    </tr>
                                                </thead>

                                                <tbody>
                                                    <c:if test="${not empty pardetail}">
                                                        <c:forEach items="${pardetail}" var="pardetails" varStatus="cnt">
                                                            <tr>
                                                                <!-- <td><input type="radio" onclick="toggleButtons(this)" name="rdparid" parstatusid="${pardetails.parstatusid}" isreview="${pardetails.isreview}" value="${pardetails.parId}-${pardetails.taskId}"/> </td> -->
                                                                <td>${cnt.index + 1}</td>

                                                                <td> <b style="color:#0000FF">${pardetails.empName}</b> </br> <b style="color:#FF4500">${pardetails.parId}</b> </td>
                                                                <td> ${pardetails.prdFrmDate} to ${pardetails.prdToDate} </td>
                                                                <td> ${pardetails.postName} </td>

                                                                <td> <b style="color:#FF4500">${pardetails.parstatus}</b>
                                                                    <c:if test="${pardetails.pendingAtAuthName ne ''}">
                                                                        , </br><b style="color:#0000FF">${pardetails.pendingAtAuthName}, </br> ${pardetails.pendingAtSpc}</b>
                                                                    </c:if>
                                                                    <c:if test="${pardetails.parstatus eq 'REQUESTED FOR NRC'}">
                                                                        , </br><b style="color:#0000FF">${pardetails.nrcDetails}</b>
                                                                    </c:if>     
                                                                </td>
                                                                <td> ${pardetails.parPendingDateFrom} </td>

                                                                <td width="5%" align="center">
                                                                    <c:if test="${pardetails.parstatus ne 'REQUESTED FOR NRC'}">
                                                                        <a target="_blank" href="getviewPARAdmindetail.htm?parId=${pardetails.parId}&taskId=${pardetails.taskId}"> View PAR </a> 
                                                                    </c:if>
                                                                    <c:if test="${pardetails.parstatusid  eq '17'}">
                                                                        <a target="_blank" href="getNRCdetail.htm?parId=${pardetails.parId}">view NRC</a>
                                                                    </c:if>
                                                                </td>
                                                                <td width="5%" align="center">
                                                                    <c:if test="${pardetails.parstatusid  eq '17' && pardetails.isNRCAttchPresent eq 'Y'}">
                                                                        <a target="_blank" href="DownloadparNRCAttachment.htm?parId=${pardetails.parId}">view Attachment</a>
                                                                    </c:if>
                                                                    <c:if test="${pardetails.parstatusid  eq '116'}">
                                                                        <a target="_blank" href="downloadAttachmentOfPreviousYearPAR.htm?parId=${pardetails.parId}">view Attachment</a>
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
                </form:form>
            </div>    
        </body>
</html>


