<%-- 
    Document   : AnnualEstablishmentReportForAO
    Created on : Jul 9, 2019, 11:34:23 AM
    Author     : Manas
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
            $(document).ready(function () {
                $('.openPopup').on('click', function () {

                    var dataURL = $(this).attr('data-href');

                    $('#modalbody').load(dataURL, function () {
                        $('#processUserModal').modal({show: true});
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

                var url = 'getOfficeListJSON.htm?deptcode=' + deptcode;

                $('#hidOffCode').empty();
                $('#hidOffCode').append('<option value="">--Select Office--</option>');

                $.getJSON(url, function (data) {
                    $.each(data, function (i, obj) {
                        $('#hidOffCode').append('<option value="' + obj.offCode + '">' + obj.offName + '</option>');
                    });
                });
            }

            function openApproveModal(aerid) {
                $('#aerId').val(aerid);
                $("#approveModal").modal('show');
                //alert("Value of AER ID is: "+$('#aerId').val());
            }
            
            function openDisApproveModal(aerid) {
                $('#aerId').val(aerid);
                $("#disApproveModal").modal('show');
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

        <form:form action="displayAERlistForAdministrativeAuthority.htm" commandName="command">


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

                                    <button type="submit" name="btnAer" value="GetAOData" class="btn btn-primary" onclick="return validate()">Show AER List</button>   
                                </h3> 
                            </div>
                        </div>    
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th width="3%">Sl No</th>
                                    <th width="10%">AER Submitted CO Office Code</th>
                                    <th width="35%">AER Submitted CO Office</th>
                                    <th width="30%">Submitted By</th>
                                    <th width="7%">Submitted On</th>
                                    <th width="7%">Status</th>
                                    <th colspan="3" align="center">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${EstablishmentList}" var="establish">
                                    <tr>
                                        <td> ${establish.serialno} </td>
                                        <td> ${establish.coOffCode}(${establish.coCode}) </td>
                                        <td> ${establish.operatoroffName} </td>
                                        <td> ${establish.postname} </td>
                                        <td> ${establish.submittedDate} </td>
                                        <td> ${establish.status} </td>
                                        <td> 
                                            <c:if test="${not empty establish.aerId}">
                                                <a href="downloadaerPDFReportForScheduleII.htm?aerId=${establish.aerId}&financialYear=${establish.fy}" target="_blank">
                                                    <span class="fa fa-file-pdf-o" style="color:red"></span> Schedule-II
                                                </a><br />
                                                <a href="displayScheduleIListForAdministrativeAuthority.htm?coAerId=${establish.aerId}&financialYear=${establish.fy}" target="_blank">
                                                    <span class="fa fa-file-pdf-o" style="color:red"></span> Schedule-I
                                                </a>
                                            </c:if>    
                                        </td>    
                                        <td> &nbsp;        
                                            <c:if test="${not empty establish.aerId}">
                                                <c:if test="${establish.showApproveLink eq 'Y'}">
                                                    <%--<a href="javascript:openApproveModal('${establish.aerId}');">Approve</a>--%>
                                                    <div class="dropdown">
                                                        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">Select Action
                                                            <span class="caret"></span></button>
                                                        <ul class="dropdown-menu">
                                                            <li><a href="javascript:openApproveModal('${establish.aerId}');">Approve</a></li>
                                                            <li><a href="javascript:openDeclineModal('${establish.aerId}');">Decline</a></li>
                                                        </ul>
                                                    </div>
                                                </c:if>
                                                <c:if test="${establish.showDisApproveLink eq 'Y' && aoAerId lt 1}">
                                                    <a href="javascript:openDisApproveModal('${establish.aerId}');">DisApprove</a>
                                                </c:if>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${aoAerId lt 1}">
                            <c:if test="${empty command.aoOffCode}">
                                <button type="submit" name="btnAer" value="View" class="btn btn-primary">View Consolidated Report</button>
                            </c:if>
                            <c:if test="${not empty command.aoOffCode}">
                                <form:hidden path="aoOffCode"/>
                                <a class="btn btn-primary" href="displayAERlistForAdministrativeAuthority.htm?btnAer=GetAOData&financialYear=${command.financialYear}">Back</a>
                                <button type="submit" name="btnAer" value="ViewMultipleAO" class="btn btn-primary">View Consolidated Report</button>
                            </c:if>
                        </c:if>
                        <c:if test="${aoAerId gt 0}">
                            <a href="downloadaerPDFReportForScheduleIII.htm?aerId=${aoAerId}&financialYear=${command.financialYear}" target="_blank">
                                <span class="fa fa-file-pdf-o" style="color:red"></span> Schedule-III
                            </a>
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
                            <h4 class="modal-title">Approval</h4>
                        </div>
                        <div class="modal-body">

                        </div>
                        <div class="modal-footer">
                            <input type="submit" name="btnAer" value="Approve" class="btn btn-success" onclick="return confirm('Are you sure to Approve?')"/>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
            
            <div id="disApproveModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">DisApproval</h4>
                        </div>
                        <div class="modal-body">

                        </div>
                        <div class="modal-footer">
                            <input type="submit" name="btnAer" value="DisApprove" class="btn btn-success" onclick="return confirm('Are you sure to DisApprove?')"/>
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
        </form:form>

    </body>
</html>
