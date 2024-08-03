<%-- 
    Document   : AnnualEstablishmentList
    Created on : 20 Feb, 2018, 11:16:29 AM
    Author     : Surendra
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>HRMS</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">

            $(document).ready(function() {
                $('.openPopup').on('click', function() {

                    var dataURL = $(this).attr('data-href');

                    $('#modalbody').load(dataURL, function() {
                        $('#processUserModal').modal({show: true});
                    });
                });

                $('.openMappedBillGroupModal').on('click', function() {

                    var dataURL = $(this).attr('data-href');

                    $('#mappedbillgroupmodalbody').load(dataURL, function() {
                        $('#mappedBillGroupModal').modal({show: true});
                    });
                });

                $('.openRevertModal').on('click', function() {

                    var dataURL = $(this).attr('data-href');
                    //alert("dataURL is: "+dataURL);
                    $('#revertReasonModal #revertReason').load(dataURL, function() {
                        $('#revertReasonModal').modal({show: true});
                    });
                });

                $('.openFlowPopup').on('click', function() {

                    var dataURL = $(this).attr('data-href');
                    //alert("dataURL is: "+dataURL);
                    $('#aerFlowModal #flowlist').load(dataURL, function() {
                        $('#aerFlowModal').modal({show: true});
                    });
                });
            });

            function validate() {
                if ($("#financialYear").val() == '') {
                    alert('Please select Financial Year.');
                    $("#financialYear").focus();
                    return false;
                }
            }
            function viewRevertReason(aerid) {
                $(".modal-body").load("viewRevertReason.htm?aerId=" + aerid);
                $("#revertReasonModal").modal('show');
            }

            function getDeptWiseOfficeList() {
                var deptcode = $('#hidDeptCode').val();
                var url = 'getControllingOfficelist.htm?deptcode=' + deptcode;

                $('#hidOffCode').empty();
                $('#hidOffCode').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function(data) {
                    $.each(data, function(i, obj) {
                        $('#hidOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function openApproveModal(aerid) {
                $('#aerId').val(aerid);
                $("#approveModal").modal('show');
                //alert("Value of AER ID is: "+$('#aerId').val());
            }

            function openDeclineModal(aerid) {
                $('#aerId').val(aerid);
                $("#declineModal").modal('show');
            }

            function validateDecline() {
                if ($('#revertReason').val() == "") {
                    alert("Please enter Reason for Decline");
                    return false;
                }
                if (confirm('Are you sure to Decline?')) {
                    return true;
                }
            }

            function validateApprove() {
                if ($('#hidDeptCode').val() == "") {
                    alert("Please select Department");
                    return false;
                }
                if ($('#hidOffCode').val() == "") {
                    alert("Please select Office");
                    return false;
                }
                if (confirm("Are you sure to Approve?")) {
                    return true;
                } else {
                    return false;
                }
            }
        </script>

        <style type="text/css">
            .dropdown-submenu {
                position: relative;
            }

            .dropdown-submenu .dropdown-menu {
                top: 0;
                left: 100%;
                margin-top: -1px;
            }
        </style>

    </head>
    <body>

        <form:form action="createAER.htm" commandName="command">


            <div class="container-fluid">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-lg-12">
                                <h1 align="center"> ${OffName} </h1>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <h3 align="center"> Financial Year 
                                    <form:select path="financialYear" id="financialYear">
                                        <form:option value="" label="Select" cssStyle="width:30%"/>
                                        <form:options items="${fiscyear}" itemLabel="fy" itemValue="fy"/>
                                        <%--<form:option value="2017-18"> </form:option>
                                        <form:option value="2018-19"> </form:option>--%>
                                    </form:select>

                                    <button type="submit" name="btnAer" value="Search" class="btn btn-primary" onclick="return validate()">Show AER List</button>   
                                </h3> 
                            </div>
                        </div>    
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sl No</th>
                                    <th>Financial Year</th>                                    
                                    <th>Submitted On</th>
                                    <th>Status</th>
                                    <th>CO Office Code</th>
                                    <th>Revert Reason</th>
                                    <th colspan="3" align="center">Action</th>

                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${EstablishmentList}" var="establish">
                                    <tr>
                                        <td> ${establish.serialno} </td>
                                        <td> ${establish.fy} </td>
                                        
                                        <td> ${establish.submittedDate} </td>
                                        <td> 
                                            ${establish.status}
                                            <a href="javascript:void(0);" data-href="viewAERProcessUserNameList.htm?aerId=${establish.aerId}" class="openPopup">
                                                <span class="glyphicon glyphicon-info-sign" style="font-size: 16px;"></span>
                                            </a>
                                            <a href="javascript:void(0);" data-href="viewAERFlowList.htm?aerId=${establish.aerId}" class="openFlowPopup">
                                                Flow
                                            </a>
                                        </td>
                                        <td> ${establish.coOffCode} (${establish.coCode}) </td>
                                        <td>
                                            <c:if test="${establish.showApproveLink ne 'Y'}">
                                                <c:if test="${not empty establish.revertReason}">
                                                    <a href="javascript:void(0);" data-href="viewRevertReason.htm?aerId=${establish.aerId}" class="openRevertModal">
                                                        <img src="./images/revert.png" width="25" height="25" alt="Reverted" title="Reverted"/>
                                                    </a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <td> &nbsp;
                                            <c:if test="${establish.totpostinAerreport gt 0}">
                                                <a href="aerReportPartA.htm?aerId=${establish.aerId}">View</a> 
                                            </c:if>
                                            <c:if test="${establish.totpostinAerreport eq 0}">
                                                <a href="aerReportPartA.htm?aerId=${establish.aerId}">Edit</a><br />
                                                <a href="javascript:void(0);" data-href="viewAERMappedBillGroupList.htm?aerId=${establish.aerId}" class="openMappedBillGroupModal">
                                                    View/Edit Bill Group
                                                </a>
                                            </c:if>
                                        </td>    
                                        <td> &nbsp;
                                            <c:if test="${establish.totpostinAerreport gt 0 && empty establish.submittedDate}">
                                                <a a href="deleteDataFormAerReport.htm?aerId=${establish.aerId}" onclick="return confirm('Are you sure to Delete Remarks for Part-A and Part-B ?')">Delete Remarks </a> 
                                            </c:if>
                                            <c:if test="${establish.totpostinAerreport eq 0}">
                                                <a a href="deleteReportPartA.htm?aerId=${establish.aerId}" onclick="return confirm('Are you sure to Delete Aer?')">Delete AER</a> 
                                            </c:if>
                                            <c:if test="${establish.totpostinAerreport gt 0 && not empty establish.submittedDate}">
                                                <a href="DownloadaerPDFReport.htm?aerId=${establish.aerId}&financialYear=${establish.fy}" target="_blank">
                                                    <span class="fa fa-file-pdf-o" style="color:red"></span> Schedule-I</a>
                                                </c:if>  

                                        </td>    
                                        <td> &nbsp;        
                                            <c:if test="${not empty establish.aerId}">
                                                <c:if test="${empty establish.status}">
                                                    <a href="submitForPrivewEstablishmentReport.htm?fy=${establish.fy}&aerId=${establish.aerId}">Submit</a>
                                                </c:if>


                                                <c:if test="${establish.showApproveLink eq 'Y'}">
                                                    <span class="dropdown">
                                                        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">Select Action
                                                            <span class="caret"></span></button>
                                                        <ul class="dropdown-menu">
                                                            <li><a href="javascript:openApproveModal('${establish.aerId}');">Approve</a></li>
                                                            <li><a href="javascript:openDeclineModal('${establish.aerId}');">Decline</a></li>
                                                        </ul>
                                                    </span>
                                                </c:if>
                                            </c:if>

                                        </td>

                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${empty command.financialYear}">
                            <button id="btncreate" type="submit" name="btnAer" value="CreateAER" class="btn btn-primary " style="display:none" onclick="return validate()">Create AER</button>
                        </c:if>
                        <c:if test="${not empty command.financialYear}">
                            <c:if test="${isOperator eq 'Y' && isApprover eq 'Y'}">
                                <button id="btncreate" type="submit" name="btnAer" value="CreateAER" class="btn btn-primary " style="display:block" onclick="return validate()">Create AER</button>
                            </c:if>
                            <c:if test="${isOperator eq 'Y' && isApprover ne 'Y'}">
                                <button id="btncreate" type="submit" name="btnAer" value="CreateAER" class="btn btn-primary " style="display:block" onclick="return validate()">Create AER</button>
                            </c:if>
                        </c:if>

                    </div>
                </div>
            </div>

            <form:hidden path="aerId" id="aerId"/>
            <div id="approveModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Select Controlling Office</h4>
                        </div>
                        <div class="modal-body">
                            <div class="row" style="margin-bottom: 7px;">
                                <div class="col-lg-2">
                                    <label for="hidOffCode">Office</label>
                                </div>
                                <div class="col-lg-9">
                                    <form:select path="hidOffCode" id="hidOffCode" class="form-control">
                                        <form:option value="" label="Select Office" cssStyle="width:30%"/>
                                        <form:options items="${colist}" itemLabel="label" itemValue="value"/>
                                    </form:select>
                                </div>
                                <div class="col-lg-1">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <%--<input type="submit" name="btnAer" value="Approve" class="btn btn-success" onclick="return confirm('Are you sure to Approve?')"/>--%>
                            <input type="submit" name="btnAer" value="Approve" class="btn btn-success" onclick="return validateApprove();"/>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <div id="declineModal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:800px;background:#FFFFFF;">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Reason for Decline</h4>
                        </div>
                        <div class="modal-body">
                            <form:textarea path="revertReason" id="revertReason" rows="5" cols="50"/>
                        </div>
                        <div class="modal-footer">
                            <input type="submit" name="btnAer" value="Decline" class="btn btn-danger" onclick="return validateDecline();"/>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <div id="processUserModal" class="modal fade" role="dialog">
                <div class="modal-dialog modal-lg">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">User List</h4>
                        </div>
                        <div class="modal-body" id="modalbody">

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <div id="mappedBillGroupModal" class="modal fade" role="dialog">
                <div class="modal-dialog modal-lg">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Mapped Bill Group</h4>
                        </div>
                        <div class="modal-body" id="mappedbillgroupmodalbody">

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <div id="revertReasonModal" class="modal fade" role="dialog">
                <div class="modal-dialog modal-lg">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Revert Reason</h4>
                        </div>
                        <div class="modal-body" id="revertReason">

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>

            <div id="aerFlowModal" class="modal fade" role="dialog">
                <div class="modal-dialog modal-lg">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Flow List</h4>
                        </div>
                        <div class="modal-body" id="flowlist">

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>

    </body>
</html>