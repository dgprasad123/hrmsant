<%-- 
    Document   : forwardNotFitForPromotionDetail
    Created on : May 28, 2020, 11:05:14 AM
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
        
    </head>
    <form:form action="forwardNotFitForPromotionDetail.htm" method="POST" commandName="groupCEmployee" class="form-inline">
        <div class="row">
            <div class="col-lg-12">
                <h2>Not Fit For Promotion Employee Detail</h2>
                <div class="table-responsive">
                    <table class="table table-bordered table-hover table-striped">
                        <thead>
                            <tr>
                                <th width="1%"></th> 
                                <th>Sl No</th>
                                <th>Employee Name</th>
                                <th>Designation</th>
                                <th>Remark</th>
                                
                            </tr>
                        </thead>
                        <tbody>                                        
                            <c:forEach items="${notfitForPronotionempList}" var="notfitForPronotionemp" varStatus="count">
                                <tr>
                                    <td></td>
                                    <td>${count.index + 1}</td>
                                    <td>${notfitForPronotionemp.reviewedempname}</td>
                                    <td>${notfitForPronotionemp.reviewedpost}</td>
                                    <td>
                                        
                                        <c:if test="${notfitForPronotionemp.isfitforpromotionReporting eq 'N'}">
                                            <span class="label label-danger">Not Fit For Shouldering Higher Responsibility</span>
                                            <div>${notfitForPronotionemp.reportingRemarks}
                                                <a href="" class="btn btn-default" >
                                                    <span class="glyphicon glyphicon-paperclip"></span> ${notfitForPronotionemp.originalFilename}
                                                </a> 
                                            </div>
                                        </c:if>
                                    </td>
                                   
                                   
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="panel-footer">                   
                    <input type="submit" name="action" class="btn btn-default" value="Forward"/>
                </div>
            </div>
        </div>
    </form:form>
</html>
