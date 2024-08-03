<%-- 
    Document   : AnnualBudgetDDOList
    Created on : 10 Nov, 2020, 9:07:58 AM
    Author     : Surendra
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>:: HRMS, Government of Odisha ::</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">        

        <script src="js/jquery.min.js"></script>        
        <script src="js/bootstrap.min.js"></script>
        <script type="text/javascript">
            function viewRevertReason() {
                $("#budgetRevertModal").modal('show');
                var revertreason = $("#revertreason").html();
                $('#revertreasondata').html(revertreason);
            }
        </script>
    </head>
    <body>
        <div class="container-fluid">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <div class="row">
                        <div class="col-lg-12">
                            <h4>Annual Budget</h4>
                        </div>
                    </div>
                </div>
                <div class="row" style="margin-top:10px;margin-left:5px;">                           

                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-12">

                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th rowspan="2">Sl No</th>
                                            <th rowspan="2">Financial Year</th>
                                            <th rowspan="2">Created On</th>

                                            <th colspan="2">Action</th> 
                                            <th colspan="3">Status</th>
                                        </tr>
                                        <tr>
                                            <th> Edit/View </th>
                                            <th> Submit </th>
                                            <th> Delete </th>

                                            <th> Locked On </th>
                                            <th> Locked Status</th>
                                            <th> Submit Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>                                        
                                        <c:forEach items="${budgetlist}" var="blist" varStatus="counter">
                                            <tr>
                                                <td>${counter.count}</td>
                                                <td>${blist.fy}</td>
                                                <td>${blist.createdDate}</td>
                                                <td>
                                                    <c:if test="${blist.isLocked ne 'Y'}">
                                                        <a href="DDOAnnexureIIAController.htm?budgetid=${blist.budgetid}">Edit</a>
                                                    </c:if>
                                                    <c:if test="${blist.isLocked eq 'Y'}">
                                                        <a href="DDOAnnexureIIAController.htm?budgetid=${blist.budgetid}">View</a>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${blist.isLocked eq 'Y'}">
                                                        <c:if test="${blist.isSubmitted ne 'Y'}">
                                                            <a href="submitAnnualbudgetData.htm?budgetid=${blist.budgetid}" onclick="return confirm('Are you sure to Submit');">Submit</a>
                                                        </c:if>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${blist.isLocked ne 'Y'}">
                                                        <a href="deleteDDOAnnualBudget.htm?budgetid=${blist.budgetid}" onclick="return confirm('Are you sure to Delete?');">Delete</a>
                                                    </c:if>
                                                </td>
                                                <td>${blist.lockdate}</td>
                                                <td>
                                                    <c:if test="${blist.isLocked ne 'Y'}">
                                                        <a href="LockDDOBudget.htm?budgetid=${blist.budgetid}" onclick="return confirm('Are you sure to Lock');">Lock</a>
                                                    </c:if>
                                                    <c:if test="${blist.isLocked eq 'Y'}">
                                                        Locked
                                                    </c:if>
                                                </td>
                                                <td>
                                                    ${blist.status}
                                                    <c:if test="${not empty blist.isReverted && blist.isReverted eq 'Y'}">
                                                        <a href="javascript:viewRevertReason();">
                                                            <img src="./images/revert.png" width="25" height="25" alt="Reverted" title="Reverted"/>
                                                        </a>
                                                        <span id="revertreason" style="display:none;">

                                                            Reverted On - <strong><c:out value="${blist.revertDate}"/></strong><br /><br />

                                                            Revert Reason-<br />
                                                            <span style="color:red;font-weight: bold;">
                                                                <c:out value="${blist.revertReason}"/>
                                                            </span>
                                                        </span>
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
                <div class="panel-footer">
                    <a href="CreateNewBudgetData.htm"><input type="button" name="submit" class="btn btn-default btn-success" style="width:150px" value="Prepare Budget"/></a>
                </div>
            </div>
        </div>
        <div id="budgetRevertModal" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Reason of Revert</h4>
                    </div>
                    <div class="modal-body" id="revertreasondata">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
