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
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <strong style="font-size:15pt;">LTC List</strong>
                    </div>
                    <div class="panel-body">
                        <p align="right"><a href="BasicInfo.htm" class="btn btn-default" style="background:#008900;color:#FFF;">Apply for LTC &raquo;</a></p>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Applied On</th>
                                    <th>From Date</th>
                                    <th>To Date</th>
                                    <th>Place of Visit</th>
                                    <th>State of Visit</th>
                                    <th>Mode of Journey</th>
                                    <th>Appx. Distance</th>
                                    <th>Cost by Train</th>
                                    <th>Cost by Road</th>
                                    <th>Advance Amount</th>
                                    <th>Edit</th>
                                    <th>View</th>
                                    <th>Status</th>
                                    <th>Remarks</th>
                                    <th>Order No</th>
                                    <th>Order Date</th>
                                    <th>Download</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${ltcList}" var="ltc">
                                    <tr>
                                        
                                        <td>${ltc.applicationDate}</td>
                                        <td>${ltc.fromDate}</td>
                                        <td>${ltc.toDate}</td>
                                        <td>${ltc.placeofVisit}</td>
                                        <td>${ltc.visitState}</td>
                                        <td>${ltc.modeOfJourney}</td>
                                        <td>${ltc.appropriateDistance}</td>
                                        <td>${ltc.costByTrain}</td>
                                        <td>${ltc.costByRoad}</td>
                                        <td>${ltc.advanceAmount}</td>
                                        <td><c:if test="${ltc.taskStatus <= 0}"><a href="BasicInfo.htm?id=${ltc.ltcId}">Edit</a></c:if></td>
                                        <td><a href="javascript:void(0)" onclick="javascript: window.open('ViewLTCDetail.htm?id=${ltc.ltcId}', 'Print LTC Application', 'width=800,height=700')">View</a></td>
                                        <td>${ltc.status}</td>
                                        <td>${ltc.verificationRemarks}</td>
                                        <td>${ltc.orderNo}</td>
                                        <td>${ltc.orderDate}</td>
                                        <td align="center"><c:if test="${ltc.taskStatus == 89 && !empty ltc.orderNo}"><a href="GenerateLTCOrder.htm?ltcId=${ltc.ltcId}" target="_blank"><img src="images/pdf_icon.png" /></a></c:if></td>
                                        </tr>
                                
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- MODAL WINDOW FOR FORWARD MPR WORK STARTS -->    
                    
                </div>
            </div>
    </body>
</html>



