<%-- 
    Document   : viewDetailReportOfPARCemplist
    Created on : May 29, 2020, 10:08:02 AM
    Author     : manisha
--%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">   
        <script src="js/jquery.min.js" type="text/javascript"></script>        
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript">
            function markNotfitforpromotion(me, reviewedEmpId, reviewedSpc) {
                var url = "notfitforpromotion.htm";
                $.post(url, {reviewedempId: reviewedEmpId, reviewedspc: reviewedSpc})
                        .done(function(data) {
                            $(me).parent().parent().prev().html('<span class="label label-danger">Not Fit For Shouldering Higher Responsibility</span>');
                            $(me).attr("disabled", true);
                            $(me).prev().attr("disabled", false);
                            ;
                        });

            }
            function markFitforpromotion(me, reviewedEmpId, reviewedSpc) {
                var url = "fitforpromotion.htm";
                $.post(url, {reviewedempId: reviewedEmpId, reviewedspc: reviewedSpc})
                        .done(function(data) {
                            $(me).parent().parent().prev().html('<span class="label label-success">Fit For Shouldering Higher Responsibility</span>');
                            $(me).attr("disabled", true);
                            $(me).next().attr("disabled", false);
                            ;
                        });

            }
        </script>
    </head>
    <form:form action="parCPromotionReport.htm" method="POST" commandName="groupCEmployee" class="form-inline">
        <div class="row">
            <div class="col-lg-12">
                <h2>Group c employee List</h2>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th width="1%"></th> 
                                <th>Sl No</th>
                                <th>Employee Name</th>
                                <th>Designation</th>
                                <th>Remark</th>
                                <th>Action</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>                                        
                            <c:forEach items="${empList}" var="employee" varStatus="count">
                                <tr>
                                    <td></td>
                                    <td>${count.index + 1}</td>
                                    <td>${employee.reviewedempname}</td>
                                    <td>${employee.reviewedpost}</td>
                                    <td>
                                        <c:if test="${employee.isfitforpromotionReporting eq 'Y'}">
                                            <span class="label label-success">Fit For Shouldering Higher Responsibility</span>
                                        </c:if>
                                        <c:if test="${employee.isfitforpromotionReporting eq 'N'}">
                                            <span class="label label-danger">Not Fit For Shouldering Higher Responsibility</span>
                                            <div>${employee.reportingRemarks}
                                                <a href="" class="btn btn-default" >
                                                    <span class="glyphicon glyphicon-paperclip"></span> ${employee.originalFilename}
                                                </a> 
                                            </div>
                                        </c:if>
                                    </td>
                                    <td>
                                        <div class="btn-group">
                                            <c:if test="${employee.isfitforpromotionReporting eq 'Y'}">
                                                <button type="button" class="btn btn-primary" disabled="disabled" onclick="markFitforpromotion(this, '${employee.reviewedempId}', '${employee.reviewedspc}')">Fit For Shouldering Higher Responsibility</button>
                                                <%--  <button type="button" class="btn btn-danger" onclick="markNotfitforpromotion(this, '${employee.reviewedempId}', '${employee.reviewedspc}')">Not Fit For Promotion</button>--%>

                                                <a href="remarkOfNotFitForPromotionReport.htm?reviewedempId=${employee.reviewedempId}&reviewedspc=${employee.reviewedspc}" class="btn-default" ><button type="button" class="btn btn-danger">Not Fit For Shouldering Higher Responsibility</button></a>
                                            </c:if>
                                            <c:if test="${employee.isfitforpromotionReporting eq 'N' and empty employee.reportingRemarks}">

                                                <%-- <button type="button" class="btn btn-primary" onclick="markFitforpromotion(this, '${employee.reviewedempId}', '${employee.reviewedspc}')">Fit For Promotion</button>
                                                   <button type="button" class="btn btn-danger" disabled="disabled" onclick="markNotfitforpromotion(this, '${employee.reviewedempId}', '${employee.reviewedspc}')">Not Fit For Promotion</button> --%>

                                                <a href="remarkOfNotFitForPromotionReport.htm?reviewedempId=${employee.reviewedempId}&reviewedspc=${employee.reviewedspc}" class="btn-default"><button type="button" class="btn btn-danger">Not Fit For Shouldering Higher Responsibility</button></a>
                                            </c:if>   
                                            <c:if test="${employee.isfitforpromotionReporting eq 'N' and not empty employee.reportingRemarks}">

                                                <%-- <button type="button" class="btn btn-primary" onclick="markFitforpromotion(this, '${employee.reviewedempId}', '${employee.reviewedspc}')">Fit For Promotion</button>
                                                 <button type="button" class="btn btn-danger" disabled="disabled" onclick="markNotfitforpromotion(this, '${employee.reviewedempId}', '${employee.reviewedspc}')">Not Fit For Promotion</button> --%>

                                                <a href="editremarkOfNotFitForPromotionReport.htm?reviewedempId=${employee.reviewedempId}&reviewedspc=${employee.reviewedspc}" class="btn-default"><button type="button" class="btn btn-primary">Edit</button></a>
                                                <a href="deleteremarkOfNotFitForPromotionReport.htm?reviewedempId=${employee.reviewedempId}&reviewedspc=${employee.reviewedspc}" class="btn-default"><button type="button" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?')">Remove</button></a>
                                            </c:if>   
                                        </div>
                                    </td>
                                    <td>
                                         <a><button type="button" class="btn btn-danger" onclick="return confirm('Are you sure to Delete?')">Remove</button></a> 
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                 <div class="panel-footer">                   
                  <input type="submit" name="action" class="btn btn-default" value="Back"/>                        
                </div>
            </div>
        </div>
    </form:form>
</html>
