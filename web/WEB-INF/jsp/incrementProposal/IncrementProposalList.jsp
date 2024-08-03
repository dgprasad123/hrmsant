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
        <form:form action="newProposalMaster.htm" method="post" commandName="IncrementProposal">
            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <input type="hidden" name="modal_proposal_id" id="modal_proposal_id" />
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="10%">Creation Date</th>
                                    <th width="10%">Month</th>
                                    <th width="10%">Year</th>
                                    <th width="10%">Edit</th>
                                    <th width="10%" style="text-align:center">Update Order</th>
                                    <th width="10%" style="text-align:center;">Export Draft</th>
                                    <th width="10%" style="text-align:center;">Delete</th>
                                    <th width="10%" style="text-align:center;">Status</th>
                                    <th width="10%" style="text-align:center;">Verifying Authority</th>
                                    <th width="10%" style="text-align:center;">Approving Authority</th>
                                    <th>Remarks</th>
                                    <th width="10%">Submit</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${ProposalList}" var="propose">
                                    <tr>
                                        <td>${propose.lastUpdated}</td>
                                        <td>${propose.monthasString}</td>
                                        <td>${propose.proposalYear}</td>
                                        <td><c:if test="${propose.processStatusName ne 'APPROVED' && propose.processStatusName ne 'PENDING' && propose.processStatusName ne 'DECLINE' && propose.processStatusName ne 'PENDING FOR VERIFICATION' && propose.processStatusName ne 'PENDING FOR APPROVAL'}"><c:if test="${empty propose.orderno}"><a href="EditIncrementProposal.htm?proposalId=${propose.proposalId}&pmonth=${propose.proposalMonth}&pyear=${propose.proposalYear}">Edit</a></c:if></c:if></td>
                                        <td align="center"><c:if test="${propose.processStatusName eq 'APPROVED'}"><c:if test="${propose.isOrderUpdated == 'N'}">
                                                    
                                                    <a href="UpdateEmpOrders.htm?proposalId=${propose.proposalId}&month=${propose.monthasString}" class="btn btn-success"> Update Order </a>
                                                </c:if><c:if test="${propose.isOrderUpdated == 'Y'}"><a href="UpdateEmpOrders.htm?proposalId=${propose.proposalId}&month=${propose.monthasString}" class="btn btn-success">View Order</a></c:if></c:if></td>
                                        <td align="center"><a href="javascript:void(0)" onclick="window.open('generatepdf.htm?proposalId=${propose.proposalId}&signatory='+$('#signatoryAuthority').val(), '', 'width=600,height=600');"><img src="images/pdf_icon.png" title="Export as PDF" /></a><br /><input type="text" name="signatoryAuthority" id="signatoryAuthority" class="form-control" placeholder="Custom Signatory" /></td>
                                        <td align="center"><c:if test="${propose.processStatusName ne 'APPROVED' && propose.processStatusName ne 'PENDING' && propose.processStatusName ne 'DECLINE' && propose.processStatusName ne 'PENDING FOR VERIFICATION' && propose.processStatusName ne 'PENDING FOR APPROVAL'}"><c:if test="${empty propose.orderno}">
                                                <a href="javascript:void(0)" onclick="javascript: deleteProposal(${propose.proposalId})">Delete</a></c:if></c:if></td>
                                        <td style="color:#008900;font-weight:bold;text-align:center;">${propose.processStatusName}</td>
                                        <td style="font-size:8pt;">${propose.vAuthorityName}</td>
                                        <td style="font-size:8pt;">${propose.aAuthorityName}</td>
                                        <td>${propose.note}</td>
                                        <td ><c:if test="${propose.processStatusName ne 'APPROVED' && propose.processStatusName ne 'PENDING' && propose.processStatusName ne 'DECLINE' && propose.processStatusName ne 'PENDING FOR VERIFICATION' && propose.processStatusName ne 'PENDING FOR APPROVAL'}"><input type="button" value="Submit" onclick="self.location='submitProposal.htm?proposalId=${propose.proposalId}'" /></c:if>
                                            <c:if test="${propose.processStatusName eq 'PENDING'}">Submitted</c:if>
                                        </td>
                                        
                                        </tr>
                                
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- MODAL WINDOW FOR FORWARD MPR WORK STARTS -->    
                    <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal1" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
                                    <h4 class="modal-title">  Update Order No & Order Date </h4>
                                </div>
                                <div class="modal-body">
                                    <div class="row">
                                            <div class="col-md-6 form-group">
                                                <span style="color:#FF0000;">* </span> Order Number:<br />
                                                <input type="text" name="orderNumber" id="orderNumber" class="form-control" />
                                            </div>
                                            <div class="col-md-6 form-group">
                                                <span style="color:#FF0000;">* </span> Order Date:</label>
                                                
<div class='input-group date' id='processDate'><input type="text"  id="orderDate" name="orderDate" readonly="readonly" class="form-control" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span></div>                                                   
                                            </div>

                                            <div class="col-md-12 form-group" style="text-align:center;"> 
                                                <input type ="button" class="btn btn-primary" value="Save" onclick="javascript: validateUpdate()" />
                                            </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div> 
                    <!-- FORWARD MPR WORK ENDS -->                    
                    <div class="panel-footer">

                        <input type="submit" name="action" value="Create Increment Proposal" class="btn btn-default"/>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>



