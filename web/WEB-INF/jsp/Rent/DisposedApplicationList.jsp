<%-- 
    Document   : Increment Proposal List
    Created on : 20 Jun, 2016, 12:14:12 PM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resources Management System, Government of Odisha</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>        
        <script type="text/javascript">
 

        </script>


<style type="text/css">
            body{margin:0px;font-family: 'Roboto', sans-serif;background:#F7F7F7}
           .training_form td{padding:6px;}
            .form-control{height:30px;}
            body{margin:0px;font-family: 'Arial', sans-serif;background:#F7F7F7}
            #left_container{background:#2A3F54;width:18%;float:left;min-height:700px;color:#FFFFFF;font-size:15pt;font-weight:bold;}
            #left_container ul{list-style-type:none;margin:0px;padding:0px;}
            #left_container ul li a{display:block;color:#EEEEEE;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}
            #left_container ul li a:hover{background:#465F79;color:#FFFFFF;}
            #left_container ul li a.sel{display:block;color:#EEEEEE;background:#367CAD;font-weight:normal;font-size:10pt;text-decoration:none;padding:10px 0px;padding-left:15px;}            
            table {border:1px solid #DADADA;}
            .panel-header{background:#5593BC;color:#FFFFFF;}
            .panel-title{margin-bottom:5px;}
            .panel-body{font-size:15pt;}
            .datagrid-header{background:#EAEAEA;border-style:none;}
            .datagrid-header-row{font-weight:bold;}
            .datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber, .datagrid-cell-rownumber{font-size:10pt;}
            .tblres td{padding:5px;}
        </style>

        <script type="text/javascript">
            var monthNames = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
            ];


            $(document).ready(function() {
                $('#processDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });
                
                $('#orderDate').datetimepicker({
                    format: 'D-MMM-YYYY',
                    useCurrent: false,
                    ignoreReadonly: true
                });                
                
            });

            function openInputForm(proposalId) {
                $('#dd').dialog('open');
                $('#eventForm').form('clear');
                $('#proposalId').val(proposalId);
            }

            function myformatter(date) {
                var y = date.getFullYear();
                var m = date.getMonth();
                var d = date.getDate();
                //alert(date);
                //(d < 10 ? ('0' + d) : d) + '-' + (m < 10 ? ('0' + m) : m) + '-' + y;
                return (d < 10 ? ('0' + d) : d) + '-' + monthNames[m] + '-' + y;
            }
            function myparser(s) {
                if (!s)
                    return new Date();
                var ss = (s.split('-'));
                var found = $.inArray(ss[1], monthNames);
                var y = parseInt(ss[0], 10);
                var m = parseInt(found + 1, 10);
                var d = parseInt(ss[2], 10);
                if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
                    return new Date(d, m - 1, y); //d + '-' + monthNames[m - 1] + '-' + y;
                } else {
                    return new Date();
                }
            }




            function openInputForm(proposalId) {
                $('#dd').dialog('open');
                $('#eventForm').form('clear');
                $('#proposalId').val(proposalId);
            }
            function deleteProposal(proposalId)
            {
                if (confirm("Are you sure you want to remove the Proposal?"))
                {
                    $.ajax({
                        type: "get",
                        url: 'DeleteIncrementProposal.htm',
                        data: 'proposalId=' + proposalId,
                        cache: false,
                        success: function(retVal) {
                            self.location = 'displayProposalListpage.htm?offCode=';
                        }
                    });
                }
            }
            function validateUpdate()
            {
                if($('#orderNumber').val() == '')
                {
                    alert("Please enter Order Number.");
                    $('#orderNumber')[0].focus();
                    return;
                }
                if($('#orderDate').val() == '')
                {
                    alert("Please select Order Date.");
                    $('#orderDate')[0].focus();
                    return;
                }     
                    $.ajax({
                        type: "get",
                        url: 'updateorderInfo.htm',
                        data: 'propmastId=' + $('#modal_proposal_id').val()+'&ordno='+$('#orderNumber').val()+'&ordDate='+$('#orderDate').val(),
                        cache: false,
                        success: function(retVal) {
                            self.location = 'displayProposalListpage.htm?offCode=';
                        }
                    });                
            }
        </script>
    </head>

    <body>
        <jsp:include page="Header.jsp">
            <jsp:param name="menuHighlight" value="DISPOSEDNDCAPPLICATIONS" />
        </jsp:include>
                <h1 style="margin:0px;font-size:18pt;color:#777777;border-bottom:1px solid #DADADA;padding-bottom:5px;">Disposed NDC Application List (GA Quarter Pool)</h1>
        <form:form action="newProposalMaster.htm" method="post" commandName="QuarterBean">
            <div class="container-fluid" style="font-size:12pt;">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <table class="table table-bordered" style="font-size:11pt;">
                            <thead>
                                <tr bgcolor="#EAEAEA">
                                    <th>Application Date</th>
                                    <th>Employee Name</th>
                                    <th>Designation at the time of Retirement</th>
                                    <th>Date of Retirement</th>
                                    <th>Mobile</th>
                                    <th>Quarter Details</th>
                                    <th>Quarter Vacated?</th>
                                    <th style="text-align:center;">Status</th>
                                    <th style="text-align:center;">Action</th>
                                    <th style="text-align:center;">Download</th>
                                    <th style="text-align:center;">Upload Final</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${applicationList}" var="aList">
                                    <tr<c:if test="${aList.hasOccupied == 'No'}"> bgcolor="#FFFFE2"</c:if>>
                                        <td>${aList.dateCreated}</td>
                                        <td>${aList.fullName}</td>
                                        <td>${aList.designation}</td>
                                        <td>${aList.dateOfRetirement}</td>
                                        <td>${aList.mobile}</td>
                                        <td><c:if test="${aList.hasOccupied == 'No'}">N/A</c:if><c:if test="${aList.hasOccupied == 'Yes'}">Unit: ${aList.quarterUnit}<br />Qtr Type: ${aList.quarterType}<br />Building No: ${aList.buildingNo}</c:if></td>
                                        <td><c:if test="${aList.hasOccupied == 'No'}">N/A</c:if><c:if test="${aList.hasOccupied == 'Yes'}">${aList.hasVacated}</c:if></td>
                                        <td align="center">${aList.applicationStatus}
                                            <c:if test="${aList.applicationStatus == 'No NDC'}"><br /><span style="font-size:9pt;color:#890000;"><strong>Reason:</strong> ${aList.nondcRemark}<span></c:if></td>
                                        <td align="center"><a href="NDCDetailAction.htm?applicationId=${aList.applicationId}">Action</a></td>
                                        <td align="center"><c:if test="${aList.isNDCGenerated == 'Y'}"><a href="GenerateNDCpdf.htm?applicationId=${aList.applicationId}" target="_blank">Download NDC</a></c:if>
                                            <c:if test="${aList.hasRecoveryIntimation == 'Y'}">
                                                <a href="GenerateRIpdf.htm?applicationId=${aList.applicationId}" target="_blank">Payment Intimation</a>
                                            </c:if>
                                        </td>
                                        <td align="center"><c:if test="${aList.isNDCGenerated == 'Y'}">
                                                <c:if test="${aList.hasFinalNDC == 'N'}">
                                                <a href="javascript: void(0)" onclick="javascript: window.open('UploadFinalNDC.htm?applicationId=${aList.applicationId}', '', 'width=800,height=600')">Upload Final NDC</a>
                                                </c:if>
                                                <c:if test="${aList.hasFinalNDC == 'Y'}">
                                                    <a href="downloadNDC.htm?applicationId=${aList.applicationId}" target="_blank">Download Final NDC</a>
                                                    <br /><a href="javascript: void(0)" onclick="javascript: window.open('UploadFinalNDC.htm?applicationId=${aList.applicationId}', '', 'width=800,height=600')" style="color:#890000;">Re-upload</a>
                                                </c:if>
                                            </c:if><c:if test="${aList.hasRecoveryIntimation == 'Y'}">
                                                <c:if test="${aList.hasFinalNDC == 'N'}">
                                                <a href="javascript: void(0)" onclick="javascript: window.open('UploadFinalNDC.htm?applicationId=${aList.applicationId}', '', 'width=800,height=600')">Upload Payment Intimation</a>
                                                </c:if>
                                                <c:if test="${aList.hasFinalNDC == 'Y'}">
                                                    <a href="downloadNDC.htm?applicationId=${aList.applicationId}" target="_blank"><c:if test="${aList.hasRecoveryIntimation == 'Y'}">Download Payment Intimation</c:if><c:if test="${aList.hasRecoveryIntimation == 'N'}">Download Final NDC</c:if></a>
                                                    <br /><a href="javascript: void(0)" onclick="javascript: window.open('UploadFinalNDC.htm?applicationId=${aList.applicationId}', '', 'width=800,height=600')" style="color:#890000;">Re-upload</a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        </tr>
                                
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                 
                </div>
            </div>
        </form:form>
            </div>
        </div>                
    </body>
</html>



