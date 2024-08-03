<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Human Resources Management System, Government of Odisha</title>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>
        <!-- Custom CSS -->
        <link href="css/sb-admin.css" rel="stylesheet">
        <script src="js/jquery.min.js" type="text/javascript"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/common.js" type="text/javascript"></script>
        <script src="js/moment.js" type="text/javascript"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script language="javascript" type="text/javascript" >
            $(document).ready(function () {

            });
            function deleteTransferProposal(proposalId)
            {
                if (confirm("Are you sure you want to delete the Proposal?"))
                {
                    $.ajax({
                        url: 'DeleteTransferProposal.htm',
                        type: 'get',
                        data: 'proposalId=' + proposalId,
                        success: function (retVal) {
                            self.location = 'TransferProposalList.htm';
                        }
                    });
                }
            }
            function saveApproval() {
                if ($('#orderNumber').val() == '') {
                    alert("Please enter Order Number.");
                    $('#orderNumber')[0].focus();
                    return false;
                }
                if ($('#orderDate').val() == '') {
                    alert("Please select Order Date.");
                    $('#orderDate')[0].focus();
                    return false;
                }

                if (confirm("Are you sure you want to approve the Proposal?")) {
                    $.ajax({
                        url: 'SaveApproval.htm',
                        type: 'get',
                        data: 'proposalId=' + $('#proposalId').val() + '&orderNumber=' + $('#orderNumber').val() + '&orderDate=' + $('#orderDate').val(),
                        success: function (retVal) {
                            self.location = 'TransferProposalList.htm?proposalType=A';
                        }
                    });
                }
            }
        </script>

    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">
                <div class="container-fluid">
                    <!-- Page Heading -->
                    <div class="row">
                        <div class="col-lg-12">                            
                            <ol class="breadcrumb">
                                <li>
                                    <i class="fa fa-dashboard"></i>  <a href="index.html">Dashboard</a>
                                </li>
                                <li>
                                    <i class="fa fa-file"></i> Transfer / Deputation Proposal 
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="TransferProposal.htm">New Transfer / Deputation Proposal</a>
                                </li>
                                <li class="active">
                                    <i class="fa fa-file"></i> <a href="PromotionProposal.htm">New Promotion Proposal</a>
                                </li>
                            </ol>
                        </div>
                    </div>





                    <div class="row">
                        <div class="col-lg-12">
                            <form:form class="form-horizontal" action="TransferProposalList.htm" commandName="TransferProposalForm">
                                <div class="form-group">
                                    <label class="control-label col-sm-2" for="cadrecode">Browse:</label>
                                    <div class="col-sm-4">

                                        <form:select path="proposalType" class="form-control">
                                            <form:option value="A">All Transfer Proposals</form:option>
                                            <form:option value="N">Current Transfer Proposals</form:option>
                                            <form:option value="Y">Archived Transfer Proposals</form:option>
                                        </form:select>
                                    </div>
                                    <div class="col-sm-2">
                                        <input type="submit" name="action" value="Search" class="btn btn-default"/>                                        
                                    </div>
                                </div>
                            </form:form>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">
                            <h2>Proposal List</h2>
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>Sl No.</th>
                                            <th>Proposal For</th>
                                            <th>Proposal Date</th>
                                            <th>No. of Employees</th>
                                            <th>Order Number</th>
                                            <th>Order Date</th>
                                            <th width="10%">Edit</th>
                                            <th width="10%">Authority</th>
                                            <th width="12%">Download Draft</th>
                                            <th width="10%">Action</th>
                                            <th width="10%">Delete</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="tpList" items="${tpList}" varStatus="theCount">
                                            <tr>
                                                <td>${theCount.index+1}</td>
                                                <td>
                                                    <c:if test="${tpList.proposalType eq 'T'}">
                                                        Transfer / Deputation
                                                    </c:if>
                                                    <c:if test="${tpList.proposalType eq 'P'}">
                                                        Promotion
                                                    </c:if>
                                                </td>
                                                <td>${tpList.dateofEntry}</td>
                                                <td>${tpList.numEmps}</td>
                                                <td>${tpList.orderNumber}</td>
                                                <td>${tpList.orderDate}</td>
                                                <td>
                                                    <c:if test="${tpList.isApproved == 'N'}">
                                                        <c:if test="${tpList.proposalType eq 'T'}">
                                                            <a href="TransferProposal.htm?proposalId=${tpList.proposalId}" style="background:#0379B4" class="btn btn-sm btn-success">Edit</a>
                                                        </c:if>
                                                        <c:if test="${tpList.proposalType eq 'P'}">
                                                            <a href="PromotionProposal.htm?proposalId=${tpList.proposalId}" style="background:#0379B4" class="btn btn-sm btn-success">Edit</a>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${tpList.isApproved == 'N'}">                                                        
                                                        <a href="AuthorityInfo.htm?proposalId=${tpList.proposalId}" style="background:#0379B4" class="btn btn-sm btn-success">Other Inputs</a>                                                        
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${tpList.proposalType eq 'T'}">
                                                        <a href="downloadDraftTransferPDF.htm?proposalId=${tpList.proposalId}" style="background:#0379B4" class="btn btn-sm btn-success" target="_blank">
                                                            <c:if test="${tpList.isApproved == 'Y'}">
                                                                Download Letter
                                                            </c:if>
                                                            <c:if test="${tpList.isApproved == 'N'}">
                                                                Download Draft
                                                            </c:if>

                                                        </a>
                                                    </c:if>
                                                    <c:if test="${tpList.proposalType eq 'P'}">
                                                        <a href="downloadDraftPromotionPDF.htm?proposalId=${tpList.proposalId}" style="background:#0379B4" class="btn btn-sm btn-success" target="_blank">
                                                            <c:if test="${tpList.isApproved == 'Y'}">
                                                                Download Letter
                                                            </c:if>
                                                            <c:if test="${tpList.isApproved == 'N'}">
                                                                Download Draft
                                                            </c:if>
                                                        </a>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${tpList.isApproved == 'Y'}">
                                                        <span style="color:#008900;font-weight:bold;">Despatched</span>
                                                    </c:if>
                                                    <c:if test="${tpList.isApproved == 'N'}">
                                                        <a href="showedespatchwindow.htm?proposalId=${tpList.proposalId}" style="background:#0379B4" class="btn btn-sm btn-success">Despatch The Letter</a>
                                                        <!--<a href="javascript:void(0)" data-toggle="modal" data-target="#transferAuthorityModal" style="background:#0379B4" class="btn btn-sm btn-success" onclick="javascript:$('#proposalId').val(${tpList.proposalId})">Approve</a>-->
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${tpList.isApproved == 'N'}">
                                                        <a href="javascript:void(0)" style="background:#0379B4" class="btn btn-sm btn-success" onclick="javascript: deleteTransferProposal(${tpList.proposalId})">Delete</a>
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
        </div>
    </body>
    <div id="transferAuthorityModal" class="modal fade" role="dialog">
        <div class="modal-dialog" style="border: 1px solid #0000FF;">
            <!-- Modal content-->
            <div class="modal-content" >
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title" style="color:#0067C4">Approve Details</h4>
                </div>
                <div class="modal-body">
                    <table align="center" cellpadding="2" cellspacing="2">
                        <tr style="height: 40px">
                            <th>Order Number : </th>
                            <td><input type="text" name="orderNumber" id="orderNumber" class="form-control" /></td>
                        </tr>
                        <tr style="height: 40px">
                            <th>Order Date: </th>
                            <td>
                                <div class='input-group date' id='orderDate1'><input type="text" id="orderDate" name="orderDate" path="orderDate" class="form-control" />
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-time"></span>
                                    </span>
                                </div>
                            </td>
                        </tr>

                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" onclick="saveApproval()">Save</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript">
        $(function () {

            $('#orderDate').datetimepicker({
                format: 'D-MMM-YYYY',
                useCurrent: false,
                ignoreReadonly: true
            });

        });
    </script>
</html>
