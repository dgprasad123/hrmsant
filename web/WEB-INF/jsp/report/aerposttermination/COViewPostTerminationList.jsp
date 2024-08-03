<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>HRMS</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

        <script type="text/javascript" src="js/jquery.min-1.9.1.js"></script>        
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function openApproveModal(proposalId, fy) {

                $('#proposalId').val(proposalId);
                // alert(fy);
                $('#financialYear').val(fy);
                $("#approveModal").modal('show');
                //alert("Value of AER ID is: "+$('#aerId').val());
            }

            function openDeclineModal(proposalId, fy) {
                $('#proposalId').val(proposalId);
                $('#financialYear').val(fy);
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
            function show_consolidated_report(){
              var fy=$('#fy').val();
             
              window.location="COWiseSummaryReport.htm?fy="+fy;
                
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
            .dropdown-menu{
                min-width: 120px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h2 align="center">DDO Wise Post termination List</h2>
                    <div class="row">
                        <h3 align="center"> 
                            Financial Year <form:form action="COViewPostTerminationList.htm" commandName="command">
                                <form:select path="fy" id="fy">
                                    <form:option value="-Select-"> </form:option>
                                    <form:option value="2018-19"> </form:option>
                                     <form:option value="2019-20"> </form:option>
                                      <form:option value="2020-21"> </form:option>
                                </form:select>

                                <input type="submit" name="btnPTAer" value="Show List" class="btn btn-primary"/>
                                <input type="button" name="btnPTAer" value="Consolidated Report" class="btn btn-primary" onclick="show_consolidated_report()"/>
                            </form:form>
                        </h3>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered" width="100%">
                        <thead>
                            <tr>
                                <th width="10%">SL No</th>
                                <th width="30%">Office Name</th>
                                <th width="30%">Submitted By</th>
                                <th width="10%">Submitted On</th>
                                <th width="10%">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${postterminationcolist}" var="offlist" varStatus="count">
                                <tr>
                                    <td>
                                        ${count.index + 1}
                                    </td>
                                    <td>
                                        <c:out value="${offlist.offName}"/>
                                    </td>
                                    <td> 
                                        <c:out value="${offlist.empName}"/>&nbsp;(<c:out value="${offlist.post}"/>)
                                    </td>
                                    <td>
                                        <c:out value="${offlist.submittedDate}"/>
                                    </td>
                                    <td>
                                        <c:if test="${ not empty offlist.isAoApproved}">
                                            <a href="downloadPDFScheduleIAByCO.htm?termId=${offlist.proposalId}&financialYear=${financialYear}&type=1"/>View</a>
                                        </c:if>           

                                        <c:if test="${ empty offlist.isAoApproved}">
                                            <div class="dropdown">
                                                <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">Select Action
                                                    <span class="caret"></span></button>
                                                <ul class="dropdown-menu">

                                                    <li><a href="javascript:openApproveModal(${offlist.proposalId},'${financialYear}');">Approve</a></li>
                                                    <li><a href="javascript:openDeclineModal(${offlist.proposalId},'${financialYear}');">Decline</a></li>
                                                    <li> <a href="downloadPDFScheduleIAByCO.htm?termId=${offlist.proposalId}&financialYear=${financialYear}&type=1"/>View</a></li>  


                                                </ul>
                                            </div>
                                        </c:if>          

                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <div class="panel-footer"></div>
            </div>
        </div>
        <form:form action="COWiseTerminationStatus.htm" commandName="command">
            <input type="hidden" name="proposalId" id="proposalId"/> 
            <input type="hidden" name="financialYear" id="financialYear"/>   
            <input type="hidden" name="ay" id="ay"/> 
            <div id="approveModal" class="modal" role="dialog">
                <div class="modal-dialog">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Sanctioning Authority</h4>
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

            <div id="declineModal" class="modal fade" role="dialog">
                <div class="modal-dialog" style="width:800px;background:#FFFFFF;">
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal">&times;</button>
                            <h4 class="modal-title">Reason for Decline</h4>
                        </div>
                        <div class="modal-body">
                            <textarea name="revertReason"  class="form-control" rows="5" id="revertReason"></textarea>

                        </div>
                        <div class="modal-footer">
                            <input type="submit" name="btnAer" value="Decline" class="btn btn-danger" onclick="return validateDecline();"/>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </body>
</html>
