<%@page contentType="text/html" pageEncoding="UTF-8" autoFlush="true" buffer="64kb"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">     
        <link href="css/sb-admin.css" rel="stylesheet">

        <script src="js/jquery.min.js" type="text/javascript"></script>      
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $("#employeeListModal").on("show.bs.modal", function(e) {
                    var link = $(e.relatedTarget);
                    $(this).find(".modal-body").load(link.attr("href"));
                });
            });
            $(document).on("click", ".open-submitFormModal", function() {
                var nominationId = $(this).data('id');
                $(".modal-body #nominationId").val(nominationId);
            });
        </script>
    </head>
    <body>
        <div id="wrapper">
            <jsp:include page="../tab/hrmsadminmenu.jsp"/>        
            <div id="page-wrapper">

                <div class="panel panel-default">
                    <h3 style="text-align:center">Eligible and willingness List for Assistant Sub-Inspector of Police</h3>

                    <div class="panel-heading">
                        <a href="submitASINominationForFieldOffice.htm"><button class="btn btn-success">New Nomination</button></a>   
                    </div>
                    <div class="panel-body">
                        <div class="table-responsive">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Created On</th>
                                            <th>Nomination For</th>
                                            <th>Submitted On</th>
                                            <th>Submitted To</th>
                                            <th colspan="3" style="text-align: center">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${asiNominationList}" var="obj" varStatus="counter">
                                            <tr>
                                                <td width="5%">${counter.count}</td>
                                                <td width="10%">${obj.createdOn}</td>
                                                <td width="20%">
                                                    ${obj.sltNominationForPost}
                                                    <a href="viewASINominationEmployeeList.htm?nominationMasterId=${obj.nominationMasterId}" data-remote="false" data-toggle="modal" title="View Employee List" data-target="#employeeListModal">
                                                        <span class="glyphicon glyphicon-info-sign" style="font-size: 16px;"></span>
                                                    </a>
                                                </td>
                                                <td width="10%">${obj.submittedOn}</td>
                                                <td width="20%">${obj.submittedToOffice}</td>
                                                <td width="5%" style="text-align: center">
                                                    <c:if test="${obj.submittedStatusForFieldOffice ne 'Y'}">
                                                        <a href="editASINomination.htm?nominationMasterId=${obj.nominationMasterId}" title="Click to Edit"><i class="fa fa-pencil-square-o fa-2x"></i></a>
                                                        </c:if>
                                                        <c:if test="${obj.submittedStatusForFieldOffice eq 'Y'}">
                                                        <a href="ASINominatedListDetailView.htm?nominationMasterId=${obj.nominationMasterId}" title="View"><i class="fa fa-check-square-o fa-2x"></i></a>
                                                        </c:if>
                                                </td>
                                                <td width="5%" style="text-align: center"> 
                                                    <c:if test="${obj.submittedStatusForFieldOffice ne 'Y' && obj.formCompletionStatus eq 'Y' && obj.nominatedEmployeeCount ne '0'}">
                                                        <a href="javascript:void(0);" data-toggle="modal" data-id="${obj.nominationMasterId}" class="open-submitFormModal" data-target="#submitFormModal" title="Click to Submit"><i class="fa fa-arrow-circle-right  fa-2x"></i>
                                                        </a>
                                                    </c:if>
                                                    <c:if test="${obj.submittedStatusForFieldOffice eq 'Y'}">
                                                        Submitted
                                                    </c:if>
                                                </td>
                                                <td width="5%" style="text-align: center">
                                                    <a href="downloadASINominationAnnexureAExcel.htm?nominationMasterId=${obj.nominationMasterId}" title="Annexure-A">Annexure-A</a><br />
                                                    <a href="downloadASINominationAnnexureBExcel.htm?nominationMasterId=${obj.nominationMasterId}" title="Annexure-B">Annexure-B</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <a href="submitASINominationForFieldOffice.htm"><button class="btn btn-success">New Nomination</button></a> 
                    </div>
                </div>
                <form:form action="submitASINominationForFieldOffice.htm" commandName="EmpDetNom">
                    <div class="modal fade" id="submitFormModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-lg">
                            <div class="modal-content">
                                <!-- Modal Header -->
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal">
                                        <span aria-hidden="true">&times;</span>
                                        <span class="sr-only">Close</span>
                                    </button>
                                    <h4 class="modal-title" id="myModalLabel">
                                        Submit Nomination Form
                                    </h4>
                                </div>
                                <!-- Modal Body -->
                                <div class="modal-body">
                                    <p class="reqStatusMsg"></p>
                                    <form:hidden path="nominationId"/>
                                    <div class="form-group">
                                        <label  class="col-sm-2 control-label" for="reqName">Select Range Office</label>
                                        <div class="col-sm-10">
                                            <select name="sltRangeOffice" class="form-control" id="sltRangeOffice">
                                                <option value="CTCHOM0050000">DIRECTOR GENERAL AND INSPECTOR GENERAL, CUTTACK</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <!-- Modal Footer -->
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <input type="submit" name="action" class="btn btn-primary" value="Submit">
                                </div>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        <!-- Request Modal -->
        <div id="employeeListModal" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Employee List</h4>
                    </div>
                    <div class="modal-body" id="emplistmodalbody">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
